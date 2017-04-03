package org.egov.bpa.application.service;

import java.math.BigDecimal;
import java.util.List;

import org.egov.bpa.application.entity.BpaApplication;
import org.egov.bpa.application.entity.BpaFeeDetail;
import org.egov.bpa.application.entity.BpaStatus;
import org.egov.bpa.application.repository.ApplicationBpaRepository;
import org.egov.bpa.application.service.collection.GenericBillGeneratorService;
import org.egov.bpa.utils.BpaConstants;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ApplicationBpaService extends GenericBillGeneratorService {

    @Autowired
    private ApplicationBpaRepository applicationBpaRepository;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private ApplicationBpaBillService applicationBpaBillService;

    @Transactional
    public BpaApplication createNewApplication(final BpaApplication application) {

        application.getSiteDetail().get(0).setApplication(application);
        if (!application.getBuildingDetail().isEmpty())
            application.getBuildingDetail().get(0).setApplication(application);
        if (application.getOwner() != null)
            application.getOwner().setApplication(application);
        final BpaStatus bpaStatus = getStatusByCodeAndModuleType("REGISTERED", BpaConstants.BPASTATUSMODULETYPE);
        application.setStatus(bpaStatus);
        if (application.getApplicantMode() != null)
            application.setApplicantMode("NEW");
        application.setDemand(applicationBpaBillService.createDemand(application));
        return applicationBpaRepository.save(application);
    }

    public BpaStatus getStatusByCodeAndModuleType(final String code, final String moduleName) {
        return (BpaStatus) persistenceService.find("from org.egov.bpa.application.entity.BpaStatus where moduleType=? and code=?",
                moduleName, code);
    }

    @Transactional
    public void saveAndFlushApplication(final BpaApplication application) {
        applicationBpaRepository.saveAndFlush(application);
    }

    public void setAdmissionFeeAmountForRegistration(final BpaApplication application) {
        if (application.getServiceType() != null && application.getServiceType().getId() != null) {
            final BigDecimal admissionfeeAmount = getTotalFeeAmountByPassingServiceTypeandArea(
                    application.getServiceType().getId(), BpaConstants.ADMISSIONFEEREASON);
            application.setAdmissionfeeAmount(admissionfeeAmount);
        } else {
            final BigDecimal admissionfeeAmount = getTotalFeeAmountByPassingServiceTypeandArea(1l,
                    BpaConstants.ADMISSIONFEEREASON);
            application.setAdmissionfeeAmount(admissionfeeAmount);
        }
    }

    public BigDecimal getTotalFeeAmountByPassingServiceTypeandArea(final Long serviceTypeId, final String feeType) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (serviceTypeId != null) {
            final Criteria feeCrit = applicationBpaBillService.createCriteriaforFeeAmount(serviceTypeId, feeType);
            final List<BpaFeeDetail> bpaFeeDetails = feeCrit.list();
            for (final BpaFeeDetail feeDetail : bpaFeeDetails)
                totalAmount = totalAmount.add(BigDecimal.valueOf(feeDetail.getAmount()));
        } else
            throw new ApplicationRuntimeException("Service Type Id is mandatory.");

        return totalAmount;
    }

}
