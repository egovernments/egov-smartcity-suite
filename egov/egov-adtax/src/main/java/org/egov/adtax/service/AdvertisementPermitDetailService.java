/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.adtax.service;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.exception.HoardingValidationError;
import org.egov.adtax.repository.AdvertisementPermitDetailRepository;
import org.egov.adtax.utils.AdTaxNumberGenerator;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.adtax.workflow.ApplicationWorkflowCustomDefaultImpl;
import org.egov.collection.integration.services.CollectionIntegrationService;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.utils.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementPermitDetailService {

    private final AdvertisementPermitDetailRepository advertisementPermitDetailRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    protected CollectionIntegrationService collectionIntegrationService;

    @Autowired
    private AdvertisementDemandService advertisementDemandService;

    @Autowired
    private ApplicationWorkflowCustomDefaultImpl applicationWorkflowCustomDefaultImpl;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private AdTaxNumberGenerator adTaxNumberGenerator;

    @Autowired
    private CityService cityService;

    @Autowired
    public AdvertisementPermitDetailService(final AdvertisementPermitDetailRepository advertisementPermitDetailRepository) {
        this.advertisementPermitDetailRepository = advertisementPermitDetailRepository;
    }

    @Transactional
    public AdvertisementPermitDetail createAdvertisementPermitDetail(final AdvertisementPermitDetail advertisementPermitDetail,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if (advertisementPermitDetail != null && advertisementPermitDetail.getId() == null)
            advertisementPermitDetail.getAdvertisement()
                    .setDemandId(advertisementDemandService.createDemand(advertisementPermitDetail));
        roundOfAllTaxAmount(advertisementPermitDetail);
        if (advertisementPermitDetail.getApplicationNumber() == null)
            advertisementPermitDetail.setApplicationNumber(adTaxNumberGenerator.generateApplicationNumber());
        if (advertisementPermitDetail.getAdvertisement().getAdvertisementNumber() == null)
            advertisementPermitDetail.getAdvertisement()
                    .setAdvertisementNumber(adTaxNumberGenerator.generateAdvertisementNumber());
        if (advertisementPermitDetail.getAdvertisement().getLegacy() && advertisementPermitDetail.getPermissionNumber() == null)
            advertisementPermitDetail.setPermissionNumber(adTaxNumberGenerator.generatePermitNumber());
        advertisementPermitDetailRepository.save(advertisementPermitDetail);

        if (approvalPosition != null && approvalPosition > 0 && additionalRule != null
                && StringUtils.isNotEmpty(workFlowAction))
            applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(advertisementPermitDetail,
                    approvalPosition, approvalComent, additionalRule, workFlowAction);
        return advertisementPermitDetail;
    }

    @Transactional
    public AdvertisementPermitDetail updateAdvertisementPermitDetail(final AdvertisementPermitDetail advertisementPermitDetail,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) throws HoardingValidationError {
        final boolean anyDemandPendingForCollection = advertisementDemandService
                .anyDemandPendingForCollection(advertisementPermitDetail);

        /*
         * if (!actualHoarding.getAgency().equals(advertisementPermitDetail.getAgency()) && anyDemandPendingForCollection) throw
         * new HoardingValidationError("agency", "ADTAX.001");
         */
        // If demand already collected for the current year, fee updated from
        // UI, do not update demand details. Update only fee details of hoarding.
        // We should not allow user to update demand if any collection happened in
        // the current year.

        /*
         * if (advertisementDemandService.collectionDoneForThisYear(actualHoarding) && anyDemandPendingForCollection &&
         * (!actualHoarding.getCurrentTaxAmount().equals(hoarding.getCurrentTaxAmount()) || checkEncroachmentFeeChanged(hoarding,
         * actualHoarding) || checkPendingTaxChanged(hoarding, actualHoarding))) throw new HoardingValidationError("taxAmount",
         * "ADTAX.002");
         */
        /*
         * if (!actualHoarding.getStatus().equals(advertisementPermitDetail.getStatus()) &&
         * advertisementPermitDetail.getStatus().equals(AdvertisementStatus.CANCELLED) && anyDemandPendingForCollection) throw new
         * HoardingValidationError("status", "ADTAX.003");
         */

        // If demand pending for collection, then only update demand details.
        // If demand fully paid and user changed tax details, then no need to
        // update demand details.
        if (anyDemandPendingForCollection)
            advertisementDemandService.updateDemand(advertisementPermitDetail,
                    advertisementPermitDetail.getAdvertisement().getDemandId());
        roundOfAllTaxAmount(advertisementPermitDetail);
        advertisementPermitDetailRepository.save(advertisementPermitDetail);
        if (approvalPosition != null && additionalRule != null && StringUtils.isNotEmpty(workFlowAction))
            applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(advertisementPermitDetail,
                    approvalPosition, approvalComent, additionalRule, workFlowAction);
        return advertisementPermitDetail;
    }

    private void roundOfAllTaxAmount(final AdvertisementPermitDetail advertisementPermitDetail) {
        if (advertisementPermitDetail.getEncroachmentFee() != null)
            advertisementPermitDetail
                    .setEncroachmentFee(advertisementPermitDetail.getEncroachmentFee().setScale(2, BigDecimal.ROUND_HALF_UP));

        if (advertisementPermitDetail.getTaxAmount() != null)
            advertisementPermitDetail
                    .setTaxAmount(advertisementPermitDetail.getTaxAmount().setScale(2, BigDecimal.ROUND_HALF_UP));

        if (advertisementPermitDetail.getAdvertisement().getPendingTax() != null)
            advertisementPermitDetail.getAdvertisement().setPendingTax(
                    advertisementPermitDetail.getAdvertisement().getPendingTax().setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public AdvertisementPermitDetail getAdvertisementPermitDetailsByApplicationNumber(final String applicationNumber) {
        return advertisementPermitDetailRepository.findByApplicationNumber(applicationNumber);
    }

    public AdvertisementPermitDetail findBy(final Long advPermitId) {
        return advertisementPermitDetailRepository.findOne(advPermitId);
    }

    public EgwStatus getStatusByModuleAndCode(final String code) {
        return egwStatusHibernateDAO.getStatusByModuleAndCode(AdvertisementTaxConstants.APPLICATION_MODULE_TYPE, code);
    }

    public String getCityCode() {
        return cityService.getCityByURL(EgovThreadLocals.getDomainName()).getCode();
    }
}
