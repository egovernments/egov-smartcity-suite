/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.wtms.application.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.EgModules;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.search.elastic.entity.ApplicationIndex;
import org.egov.infra.search.elastic.entity.ApplicationIndexBuilder;
import org.egov.infra.search.elastic.service.ApplicationIndexService;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.masters.entity.ApplicationType;
import org.egov.wtms.masters.entity.ConnectionCharges;
import org.egov.wtms.masters.entity.DocumentNames;
import org.egov.wtms.masters.entity.DonationDetails;
import org.egov.wtms.masters.entity.SecurityDeposit;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
import org.egov.wtms.masters.service.ConnectionChargesService;
import org.egov.wtms.masters.service.DocumentNamesService;
import org.egov.wtms.masters.service.DonationDetailsService;
import org.egov.wtms.masters.service.DonationHeaderService;
import org.egov.wtms.masters.service.SecurityDepositService;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WaterConnectionDetailsService {

    protected WaterConnectionDetailsRepository waterConnectionDetailsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    private ApplicationProcessTimeService applicationProcessTimeService;

    @Autowired
    private ApplicationIndexService applicationIndexService;

    @Autowired
    private DocumentNamesService documentNamesService;

    @Autowired
    private DonationDetailsService donationDetailsService;

    @Autowired
    private SecurityDepositService securityDepositService;

    @Autowired
    private ConnectionChargesService connectionChargesService;

    @Autowired
    private DonationHeaderService donationHeaderService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private PropertyExternalService propertyExternalService;

    @Autowired
    public WaterConnectionDetailsService(final WaterConnectionDetailsRepository waterConnectionDetailsRepository) {
        this.waterConnectionDetailsRepository = waterConnectionDetailsRepository;
    }

    public WaterConnectionDetails findBy(final Long complaintTypeId) {
        return waterConnectionDetailsRepository.findOne(complaintTypeId);
    }

    public List<WaterConnectionDetails> findAll() {
        return waterConnectionDetailsRepository
                .findAll(new Sort(Sort.Direction.ASC, WaterTaxConstants.APPLICATION_NUMBER));
    }

    public WaterConnectionDetails findByApplicationNumber(final String applicationNumber) {
        return waterConnectionDetailsRepository.findByApplicationNumber(applicationNumber);
    }

    public WaterConnectionDetails load(final Long id) {
        return waterConnectionDetailsRepository.getOne(id);
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public Page<WaterConnectionDetails> getListWaterConnectionDetails(final Integer pageNumber,
            final Integer pageSize) {
        final Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC,
                WaterTaxConstants.APPLICATION_NUMBER);
        return waterConnectionDetailsRepository.findAll(pageable);
    }

    @Transactional
    public WaterConnectionDetails createNewWaterConnection(final WaterConnectionDetails waterConnectionDetails) {

        if (waterConnectionDetails.getApplicationNumber() == null)
            waterConnectionDetails.setApplicationNumber(applicationNumberGenerator.generate());

        if (waterConnectionDetails.getState() != null
                && waterConnectionDetails.getState().getValue().equals(WaterTaxConstants.APPROVED))
            waterConnectionDetails.setConnectionStatus(ConnectionStatus.ACTIVE);

        final Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());
        waterConnectionDetails.setDemand(createDemand(waterConnectionDetails));
        if (appProcessTime != null) {
            final Calendar c = Calendar.getInstance();
            c.setTime(waterConnectionDetails.getApplicationDate());
            c.add(Calendar.DATE, appProcessTime);
            waterConnectionDetails.setDisposalDate(c.getTime());
        }
        final WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository
                .save(waterConnectionDetails);
        createApplicationIndex(savedWaterConnectionDetails);
        return savedWaterConnectionDetails;
    }

    private void createApplicationIndex(final WaterConnectionDetails waterConnectionDetails) {
        final String strQuery = "select md from EgModules md where md.name=:name";
        final Query hql = getCurrentSession().createQuery(strQuery);
        hql.setParameter("name", WaterTaxConstants.MODULES_NAME);
        final ApplicationIndexBuilder applicationIndexBuilder = new ApplicationIndexBuilder(
                ((EgModules) hql.uniqueResult()).getName(), waterConnectionDetails.getApplicationNumber(),
                waterConnectionDetails.getApplicationDate(), waterConnectionDetails.getApplicationType().getName(),
                "Mr. Bean", waterConnectionDetails.getConnectionStatus().toString(), "/wtms/test.action");

        if (waterConnectionDetails.getDisposalDate() != null)
            applicationIndexBuilder.disposalDate(waterConnectionDetails.getDisposalDate());
        if (waterConnectionDetails.getConnection().getMobileNumber() != null)
            applicationIndexBuilder.mobileNumber(waterConnectionDetails.getConnection().getMobileNumber());
        final ApplicationIndex applicationIndex = applicationIndexBuilder.build();
        applicationIndexService.createApplicationIndex(applicationIndex);
    }

    public List<ConnectionType> getAllConnectionTypes() {
        return Arrays.asList(ConnectionType.values());
    }

    public Map<String, String> getConnectionTypesMap() {
        final Map<String, String> connectionTypeMap = new LinkedHashMap<String, String>();
        connectionTypeMap.put(ConnectionType.METERED.toString(), WaterTaxConstants.METERED);
        connectionTypeMap.put(ConnectionType.NON_METERED.toString(), WaterTaxConstants.NON_METERED);
        return connectionTypeMap;
    }

    public List<DocumentNames> getAllActiveDocumentNames(final ApplicationType applicationType) {
        return documentNamesService.getAllActiveDocumentNamesByApplicationType(applicationType);
    }

    public EgDemand createDemand(final WaterConnectionDetails waterConnectionDetails) {
        final Map<String, Object> feeDetails = new HashMap<String, Object>();

        final ConnectionCharges connectionCharges = connectionChargesService.findByTypeAndDate("Connection fee");
        final SecurityDeposit securityDeposit = securityDepositService
                .findByUsageTypeAndNoOfMonths(waterConnectionDetails.getUsageType(), Long.valueOf(6));
        final DonationDetails donationDetails = donationDetailsService.findByDonationHeader(donationHeaderService
                .findByCategoryandUsage(waterConnectionDetails.getCategory(), waterConnectionDetails.getUsageType()));
        if (connectionCharges != null)
            feeDetails.put(WaterTaxConstants.WATERTAX_CONNECTION_CHARGE, connectionCharges.getAmount());
        if (securityDeposit != null)
            feeDetails.put(WaterTaxConstants.WATERTAX_SECURITY_CHARGE, securityDeposit.getAmount());
        if (donationDetails != null)
            feeDetails.put(WaterTaxConstants.WATERTAX_DONATION_CHARGE, donationDetails.getAmount());

        final Query installmentQry = getCurrentSession().createQuery(
                "from Installment where module = :module and installmentYear <= current_date order by installmentYear desc");
        installmentQry.setEntity("module", moduleService.getModuleByName(WaterTaxConstants.EGMODULE_NAME));
        final Installment installment = (Installment) installmentQry.uniqueResult();

        double totalFee = 0.0;

        final Set<EgDemandDetails> dmdDetailSet = new HashSet<EgDemandDetails>();
        for (final String demandReason : feeDetails.keySet()) {
            dmdDetailSet.add(createDemandDetails((Double) feeDetails.get(demandReason), demandReason, installment));
            totalFee += (Double) feeDetails.get(demandReason);
        }

        final EgDemand egDemand = new EgDemand();
        egDemand.setBaseDemand(BigDecimal.valueOf(totalFee));
        egDemand.setEgInstallmentMaster(installment);
        egDemand.getEgDemandDetails().addAll(dmdDetailSet);
        egDemand.setIsHistory("N");
        egDemand.setMinAmtPayable(BigDecimal.valueOf(totalFee));
        egDemand.setCreateDate(new Date());
        egDemand.setModifiedDate(new Date());
        return egDemand;
    }

    private EgDemandDetails createDemandDetails(final Double amount, final String demandReason,
            final Installment installment) {

        final Query demandQuery = getCurrentSession().getNamedQuery("DEMANDREASONBY_CODE_AND_INSTALLMENTID");
        demandQuery.setParameter(0, demandReason);
        demandQuery.setParameter(1, installment.getId());
        final EgDemandReason demandReasonObj = (EgDemandReason) demandQuery.uniqueResult();
        final EgDemandDetails demandDetail = new EgDemandDetails();
        demandDetail.setAmount(BigDecimal.valueOf(amount));
        demandDetail.setAmtCollected(BigDecimal.ZERO);
        demandDetail.setAmtRebate(BigDecimal.ZERO);
        demandDetail.setEgDemandReason(demandReasonObj);
        demandDetail.setCreateDate(new Date());
        demandDetail.setModifiedDate(new Date());
        return demandDetail;
    }

    public String checkValidPropertyAssessmentNumber(final String asessmentNumber) {
        String errorMessage = "";
        final AssessmentDetails assessmentDetails = propertyExternalService.getPropertyDetails(asessmentNumber);
        if (assessmentDetails.getErrorDetails() != null && assessmentDetails.getErrorDetails().getErrorCode() != null)
            errorMessage = assessmentDetails.getErrorDetails().getErrorMessage();
        return errorMessage;
    }

}
