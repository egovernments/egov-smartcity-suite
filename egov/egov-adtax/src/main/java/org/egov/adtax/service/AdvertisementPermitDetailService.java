/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.adtax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.autonumber.AdvertisementApplicationNumberGenerator;
import org.egov.adtax.autonumber.AdvertisementNumberGenerator;
import org.egov.adtax.autonumber.AdvertisementPermitNumberGenerator;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.HoardingAgencyWiseSearch;
import org.egov.adtax.entity.enums.AdvertisementStatus;
import org.egov.adtax.repository.AdvertisementPermitDetailRepository;
import org.egov.adtax.search.contract.HoardingSearch;
import org.egov.adtax.service.es.AdvertisementPermitDetailUpdateIndexService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.adtax.workflow.AdtaxWorkflowCustomDefaultImpl;
import org.egov.adtax.workflow.AdvertisementWorkFlowService;
import org.egov.collection.integration.services.CollectionIntegrationService;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementPermitDetailService {
    @Autowired
    private AdvertisementPermitDetailRepository advertisementPermitDetailRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    protected CollectionIntegrationService collectionIntegrationService;

    @Autowired
    private AdvertisementDemandService advertisementDemandService;

    @Autowired
    @Qualifier("adtaxWorkflowCustomDefaultImpl")
    private AdtaxWorkflowCustomDefaultImpl adtaxWorkflowCustomDefaultImpl;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AdvertisementPermitDetailUpdateIndexService advertisementPermitDetailUpdateIndexService;

    @Autowired
    private AdvertisementWorkFlowService advertisementWorkFlowService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
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
            advertisementPermitDetail
                    .setApplicationNumber(beanResolver.getAutoNumberServiceFor(AdvertisementApplicationNumberGenerator.class)
                            .getNextAdvertisementApplicationNumber(advertisementPermitDetail.getAdvertisement()));
        if (advertisementPermitDetail.getAdvertisement().getAdvertisementNumber() == null)
            advertisementPermitDetail.getAdvertisement()
                    .setAdvertisementNumber(beanResolver.getAutoNumberServiceFor(AdvertisementNumberGenerator.class)
                            .getNextAdvertisementNumber(advertisementPermitDetail.getAdvertisement()));
        if (advertisementPermitDetail.getAdvertisement().getLegacy() && advertisementPermitDetail.getPermissionNumber() == null)
            advertisementPermitDetail
                    .setPermissionNumber(beanResolver.getAutoNumberServiceFor(AdvertisementPermitNumberGenerator.class)
                            .getNextAdvertisementPermitNumber(advertisementPermitDetail.getAdvertisement()));
        advertisementPermitDetailRepository.save(advertisementPermitDetail);

        if (approvalPosition != null && approvalPosition > 0 && additionalRule != null
                && org.apache.commons.lang.StringUtils.isNotEmpty(workFlowAction))
            adtaxWorkflowCustomDefaultImpl.createCommonWorkflowTransition(advertisementPermitDetail,
                    approvalPosition, approvalComent, additionalRule, workFlowAction);
        // create or update index
        advertisementPermitDetailUpdateIndexService.updateAdvertisementPermitDetailIndexes(advertisementPermitDetail);
        return advertisementPermitDetail;
    }

    @Transactional
    public AdvertisementPermitDetail updateAdvertisementPermitDetailForLegacy(
            final AdvertisementPermitDetail advertisementPermitDetail) {

        advertisementDemandService.updateDemandForLegacyEntry(advertisementPermitDetail, advertisementPermitDetail
                .getAdvertisement().getDemandId());

        roundOfAllTaxAmount(advertisementPermitDetail);

        advertisementPermitDetailRepository.save(advertisementPermitDetail);
        // update index for legacy advertisement
        advertisementPermitDetailUpdateIndexService.updateAdvertisementPermitDetailIndexes(advertisementPermitDetail);
        return advertisementPermitDetail;
    }

    @Transactional
    public AdvertisementPermitDetail updateAdvertisementPermitDetail(
            final AdvertisementPermitDetail advertisementPermitDetail) {
        advertisementPermitDetailRepository.save(advertisementPermitDetail);
        // update index on advertisement deactivation
        advertisementPermitDetailUpdateIndexService.updateAdvertisementPermitDetailIndexes(advertisementPermitDetail);
        return advertisementPermitDetail;
    }

    @Transactional
    public AdvertisementPermitDetail updateAdvertisementPermitDetail(final AdvertisementPermitDetail advertisementPermitDetail,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
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
        if (anyDemandPendingForCollection && advertisementPermitDetail.getPreviousapplicationid() == null)
            advertisementDemandService.updateDemand(advertisementPermitDetail,
                    advertisementPermitDetail.getAdvertisement().getDemandId());

        roundOfAllTaxAmount(advertisementPermitDetail);
        advertisementPermitDetailRepository.save(advertisementPermitDetail);

        if (approvalPosition != null && additionalRule != null && org.apache.commons.lang.StringUtils.isNotEmpty(workFlowAction))
            adtaxWorkflowCustomDefaultImpl.createCommonWorkflowTransition(advertisementPermitDetail,
                    approvalPosition, approvalComent, additionalRule, workFlowAction);
        // update index on permit generation
        advertisementPermitDetailUpdateIndexService.updateAdvertisementPermitDetailIndexes(advertisementPermitDetail);
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

    public List<HoardingSearch> getAdvertisementSearchResult(final AdvertisementPermitDetail advPermitDetail,
            final String searchType) {

        final List<AdvertisementPermitDetail> advPermitDtl = advertisementPermitDetailRepository
                .searchAdvertisementPermitDetailBySearchParams(advPermitDetail);
        final HashMap<String, HoardingSearch> agencyWiseHoardingList = new HashMap<String, HoardingSearch>();
        final List<HoardingSearch> hoardingSearchResults = new ArrayList<>();

        advPermitDtl.forEach(result -> {
            final HoardingSearch hoardingSearchResult = new HoardingSearch();
            hoardingSearchResult.setAdvertisementNumber(result.getAdvertisement().getAdvertisementNumber());
            hoardingSearchResult.setApplicationNumber(result.getApplicationNumber());
            hoardingSearchResult.setApplicationFromDate(result.getApplicationDate());
            hoardingSearchResult.setAgencyName(result.getAgency() != null ? result.getAgency().getName() : "");
            hoardingSearchResult.setStatus(result.getAdvertisement().getStatus());
            hoardingSearchResult.setPermitStatus(result.getStatus().getCode());
            hoardingSearchResult.setPermissionNumber(result.getPermissionNumber());
            hoardingSearchResult.setId(result.getId());
            hoardingSearchResult.setLegacy(result.getAdvertisement().getLegacy());
            hoardingSearchResult.setCategoryName(result.getAdvertisement().getCategory().getName());
            hoardingSearchResult.setSubCategoryName(result.getAdvertisement().getSubCategory().getDescription());
            hoardingSearchResult.setOwnerDetail(result.getOwnerDetail() != null ? result.getOwnerDetail() : "");
            setWorkFlowDetails(result, hoardingSearchResult);
            if (result.getAdvertisement().getDemandId() != null) {
                hoardingSearchResult
                        .setFinancialYear(result.getAdvertisement().getDemandId().getEgInstallmentMaster().getDescription());
                if (searchType != null && "agency".equalsIgnoreCase(searchType)) {
                    if (result.getAgency() != null) {
                        // PASS DEMAND OF EACH HOARDING AND GROUP BY AGENCY WISE.
                        final Map<String, BigDecimal> demandWiseFeeDetail = advertisementDemandService
                                .checkPedingAmountByDemand(result);
                        // TODO: DO CODE CHANGE
                        final HoardingSearch hoardingSearchObj = agencyWiseHoardingList.get(result.getAgency().getName());
                        if (hoardingSearchObj == null) {
                            hoardingSearchResult.setPenaltyAmount(demandWiseFeeDetail
                                    .get(AdvertisementTaxConstants.PENALTYAMOUNT));
                            hoardingSearchResult.setPendingDemandAmount(demandWiseFeeDetail
                                    .get(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT));
                            hoardingSearchResult
                                    .setAdditionalTaxAmount(
                                            demandWiseFeeDetail.get(AdvertisementTaxConstants.ADDITIONALTAXAMOUNT));

                            hoardingSearchResult.setTotalAmount(
                                    hoardingSearchResult.getPendingDemandAmount().add(hoardingSearchResult.getPenaltyAmount())
                                            .add(hoardingSearchResult.getAdditionalTaxAmount() != null
                                                    ? hoardingSearchResult.getAdditionalTaxAmount() : BigDecimal.ZERO));
                            hoardingSearchResult.setTotalHoardingInAgency(1);
                            hoardingSearchResult.setHordingIdsSearchedByAgency(result.getId().toString());
                            agencyWiseHoardingList.put(result.getAgency().getName(), hoardingSearchResult);
                        } else {
                            final StringBuffer hoardingIds = new StringBuffer();
                            hoardingSearchObj.setPenaltyAmount(hoardingSearchObj.getPenaltyAmount().add(
                                    demandWiseFeeDetail.get(AdvertisementTaxConstants.PENALTYAMOUNT)));
                            hoardingSearchObj.setAdditionalTaxAmount(hoardingSearchObj.getAdditionalTaxAmount().add(
                                    demandWiseFeeDetail.get(AdvertisementTaxConstants.ADDITIONALTAXAMOUNT)));
                            hoardingSearchObj.setPendingDemandAmount(hoardingSearchObj.getPendingDemandAmount().add(
                                    demandWiseFeeDetail.get(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT)));
                            hoardingSearchObj.setTotalAmount(
                                    hoardingSearchObj.getPendingDemandAmount().add(hoardingSearchObj.getPenaltyAmount())
                                            .add(hoardingSearchResult.getAdditionalTaxAmount() != null
                                                    ? hoardingSearchResult.getAdditionalTaxAmount() : BigDecimal.ZERO));
                            hoardingSearchObj.setTotalHoardingInAgency(hoardingSearchObj.getTotalHoardingInAgency() + 1);

                            hoardingIds.append(hoardingSearchObj.getHordingIdsSearchedByAgency()).append("~")
                                    .append(result.getId());
                            hoardingSearchObj.setHordingIdsSearchedByAgency(hoardingIds.toString());
                            agencyWiseHoardingList.put(result.getAgency().getName(), hoardingSearchObj);
                        }
                    }
                } else {

                    final Map<String, BigDecimal> demandWiseFeeDetail = advertisementDemandService
                            .checkPedingAmountByDemand(result);
                    hoardingSearchResult.setPenaltyAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.PENALTYAMOUNT));
                    hoardingSearchResult.setPendingDemandAmount(demandWiseFeeDetail
                            .get(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT));
                    hoardingSearchResult
                            .setAdditionalTaxAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.ADDITIONALTAXAMOUNT));

                    hoardingSearchResult.setTotalAmount(
                            hoardingSearchResult.getPendingDemandAmount().add(hoardingSearchResult.getPenaltyAmount())
                                    .add(hoardingSearchResult.getAdditionalTaxAmount() != null
                                            ? hoardingSearchResult.getAdditionalTaxAmount() : BigDecimal.ZERO));
                    hoardingSearchResults.add(hoardingSearchResult);
                }
            }
        });
        if (agencyWiseHoardingList.size() > 0) {
            final List<HoardingSearch> agencyWiseFinalHoardingList = new ArrayList<HoardingSearch>();
            agencyWiseHoardingList.forEach((key, value) -> {
                agencyWiseFinalHoardingList.add(value);
            });
            return agencyWiseFinalHoardingList;
        }
        return hoardingSearchResults;

    }

    private void setWorkFlowDetails(AdvertisementPermitDetail result, final HoardingSearch hoardingSearchResult) {
        hoardingSearchResult.setUserName(result.getState() != null && result.getState().getOwnerPosition() != null
                ? advertisementWorkFlowService.getApproverName(result.getState().getOwnerPosition().getId()) : "");
        hoardingSearchResult.setPendingAction(result.getState() != null ? result.getState().getNextAction() : "");
    }

    public List<HoardingSearch> getAdvertisementSearchResult(final HoardingSearch hoardingSearch, final String hoardingType) {
        final List<AdvertisementPermitDetail> advPermitDtl = advertisementPermitDetailRepository
                .searchAdvertisementPermitDetailLike(hoardingSearch, hoardingType);
        final List<HoardingSearch> hoardingSearchResults = new ArrayList<>();
        advPermitDtl.forEach(result -> {
            final HoardingSearch hoardingSearchResult = new HoardingSearch();
            hoardingSearchResult.setAdvertisementNumber(result.getAdvertisement().getAdvertisementNumber());
            hoardingSearchResult.setApplicationNumber(result.getApplicationNumber());
            hoardingSearchResult.setApplicationFromDate(result.getApplicationDate());
            hoardingSearchResult.setAgencyName(result.getAgency() != null ? result.getAgency().getName() : "");
            hoardingSearchResult.setStatus(result.getAdvertisement().getStatus());
            hoardingSearchResult.setHordingIdsSearchedByAgency(result.getId().toString());
            hoardingSearchResult.setId(result.getId());
            hoardingSearchResult.setOwnerDetail(result.getOwnerDetail() != null ? result.getOwnerDetail() : "");
            hoardingSearchResults.add(hoardingSearchResult);
        });
        return hoardingSearchResults;
    }

    public Assignment getWfInitiator(final AdvertisementPermitDetail advertisementPermitDetail) {
        return assignmentService.getPrimaryAssignmentForUser(advertisementPermitDetail.getCreatedBy().getId());
    }

    public void updateStateTransition(final AdvertisementPermitDetail advertisementPermitDetail, final Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction) {
        if (approvalPosition != null && additionalRule != null && org.apache.commons.lang.StringUtils.isNotEmpty(workFlowAction))
            adtaxWorkflowCustomDefaultImpl.createCommonWorkflowTransition(advertisementPermitDetail,
                    approvalPosition, approvalComent, additionalRule, workFlowAction);
        // update index on collection
        advertisementPermitDetailUpdateIndexService.updateAdvertisementPermitDetailIndexes(advertisementPermitDetail);
    }

    public AdvertisementPermitDetail findByApplicationNumber(final String applicationNumber) {
        return advertisementPermitDetailRepository.findByApplicationNumber(applicationNumber);
    }

    @Transactional
    public AdvertisementPermitDetail renewal(final AdvertisementPermitDetail advertisementPermitDetail,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {

        // TODO: UPDATE DEMAND ON APPROVAL FROM COMMISSIONER. tILL THAT POINT NO NEED TO UPDATE.
        // TODO:DEMAND WE NEED TO UPDATE TO EXISTING DEMAND DETAIL.

        /*
         * if (advertisementPermitDetail != null && advertisementPermitDetail.getId() == null)
         * advertisementPermitDetail.getAdvertisement()
         * .setDemandId(advertisementDemandService.updateDemand(advertisementPermitDetail,
         * advertisementPermitDetail.getAdvertisement().getDemandId()));
         */

        // TODO: REJECTION OF RENEWAL WORKFLOW NOT HANDLED. We need to change advertisement status as active and old permit as
        // active.
        roundOfAllTaxAmount(advertisementPermitDetail);

        // DONTO CHANGE STATUS TO INACTIVE UNTILL NEW RECORD APPROVED.
        // advertisementPermitDetail.getPreviousapplicationid().setIsActive(false);

        advertisementPermitDetailRepository.save(advertisementPermitDetail);

        if (approvalPosition != null && approvalPosition > 0 && additionalRule != null
                && org.apache.commons.lang.StringUtils.isNotEmpty(workFlowAction))
            adtaxWorkflowCustomDefaultImpl.createCommonWorkflowTransition(advertisementPermitDetail,
                    approvalPosition, approvalComent, additionalRule, workFlowAction);
        // update index on renewal
        advertisementPermitDetailUpdateIndexService.updateAdvertisementPermitDetailIndexes(advertisementPermitDetail);
        return advertisementPermitDetail;
    }

    // TODO : CODE REVIEW PENDING
    public List<HoardingSearch> getRenewalAdvertisementSearchResult(final AdvertisementPermitDetail advPermitDetail,
            final String searchType) {

        final List<AdvertisementPermitDetail> advPermitDtl = advertisementPermitDetailRepository
                .searchActiveAdvertisementPermitDetailBySearchParams(advPermitDetail);
        final List<HoardingSearch> hoardingSearchResults = new ArrayList<>();

        advPermitDtl.forEach(result -> {
            final HoardingSearch hoardingSearchResult = new HoardingSearch();
            hoardingSearchResult.setAdvertisementNumber(result.getAdvertisement().getAdvertisementNumber());
            hoardingSearchResult.setApplicationNumber(result.getApplicationNumber());
            hoardingSearchResult.setApplicationFromDate(result.getApplicationDate());
            hoardingSearchResult.setAgencyName(result.getAgency() != null ? result.getAgency().getName() : "");
            hoardingSearchResult.setStatus(result.getAdvertisement().getStatus());
            hoardingSearchResult.setPermitStatus(result.getStatus().getCode());
            hoardingSearchResult.setPermissionNumber(result.getPermissionNumber());
            hoardingSearchResult.setId(result.getId());
            hoardingSearchResult.setCategoryName(result.getAdvertisement().getCategory().getName());
            hoardingSearchResult.setSubCategoryName(result.getAdvertisement().getSubCategory().getDescription());
            hoardingSearchResult.setOwnerDetail(result.getOwnerDetail() != null ? result.getOwnerDetail() : "");
            if (result.getAdvertisement().getDemandId() != null)
                hoardingSearchResult
                        .setFinancialYear(result.getAdvertisement().getDemandId().getEgInstallmentMaster().getDescription());
            hoardingSearchResults.add(hoardingSearchResult);
        });
        return hoardingSearchResults;
    }

    public AdvertisementPermitDetail findById(final Long id) {
        return advertisementPermitDetailRepository.findOne(id);
    }

    public List<HoardingSearch> getActiveAdvertisementSearchResult(final AdvertisementPermitDetail advPermitDetail,
            final String searchType) {

        final List<AdvertisementPermitDetail> advPermitDtl = advertisementPermitDetailRepository
                .searchActiveAdvertisementPermitDetailBySearchParams(advPermitDetail);
        final HashMap<String, HoardingSearch> agencyWiseHoardingList = new HashMap<String, HoardingSearch>();
        final List<HoardingSearch> hoardingSearchResults = new ArrayList<>();

        advPermitDtl.forEach(result -> {
            final HoardingSearch hoardingSearchResult = new HoardingSearch();
            hoardingSearchResult.setAdvertisementNumber(result.getAdvertisement().getAdvertisementNumber());
            hoardingSearchResult.setApplicationNumber(result.getApplicationNumber());
            hoardingSearchResult.setApplicationFromDate(result.getApplicationDate());
            hoardingSearchResult.setAgencyName(result.getAgency() != null ? result.getAgency().getName() : "");
            hoardingSearchResult.setStatus(result.getAdvertisement().getStatus());
            hoardingSearchResult.setPermitStatus(result.getStatus().getCode());
            hoardingSearchResult.setPermissionNumber(result.getPermissionNumber());
            hoardingSearchResult.setId(result.getId());
            hoardingSearchResult.setCategoryName(result.getAdvertisement().getCategory().getName());
            hoardingSearchResult.setSubCategoryName(result.getAdvertisement().getSubCategory().getDescription());
            hoardingSearchResult.setOwnerDetail(result.getOwnerDetail() != null ? result.getOwnerDetail() : "");
            if (result.getAdvertisement().getDemandId() != null) {
                hoardingSearchResult
                        .setFinancialYear(result.getAdvertisement().getDemandId().getEgInstallmentMaster().getDescription());
                if (searchType != null && "agency".equalsIgnoreCase(searchType) && result.getAgency() != null) {
                    // PASS DEMAND OF EACH HOARDING AND GROUP BY AGENCY WISE.
                    final Map<String, BigDecimal> demandWiseFeeDetail = advertisementDemandService
                            .checkPedingAmountByDemand(result);
                    // TODO: DO CODE CHANGE
                    final HoardingSearch hoardingSearchObj = agencyWiseHoardingList.get(result.getAgency().getName());
                    if (hoardingSearchObj == null) {
                        hoardingSearchResult.setPenaltyAmount(demandWiseFeeDetail
                                .get(AdvertisementTaxConstants.PENALTYAMOUNT));
                        hoardingSearchResult.setPendingDemandAmount(demandWiseFeeDetail
                                .get(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT));
                        hoardingSearchResult
                                .setAdditionalTaxAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.ADDITIONALTAXAMOUNT));

                        hoardingSearchResult.setTotalHoardingInAgency(1);
                        hoardingSearchResult.setHordingIdsSearchedByAgency(result.getId().toString());
                        agencyWiseHoardingList.put(result.getAgency().getName(), hoardingSearchResult);
                    } else {
                        final StringBuffer hoardingIds = new StringBuffer();
                        hoardingSearchObj.setPenaltyAmount(hoardingSearchObj.getPenaltyAmount().add(
                                demandWiseFeeDetail.get(AdvertisementTaxConstants.PENALTYAMOUNT)));
                        hoardingSearchObj.setAdditionalTaxAmount(hoardingSearchObj.getAdditionalTaxAmount().add(
                                demandWiseFeeDetail.get(AdvertisementTaxConstants.ADDITIONALTAXAMOUNT)));
                        hoardingSearchObj.setPendingDemandAmount(hoardingSearchObj.getPendingDemandAmount().add(
                                demandWiseFeeDetail.get(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT)));
                        hoardingSearchObj.setTotalHoardingInAgency(hoardingSearchObj.getTotalHoardingInAgency() + 1);

                        hoardingIds.append(hoardingSearchObj.getHordingIdsSearchedByAgency()).append("~")
                                .append(result.getId());
                        hoardingSearchObj.setHordingIdsSearchedByAgency(hoardingIds.toString());
                        agencyWiseHoardingList.put(result.getAgency().getName(), hoardingSearchObj);
                    }
                } else {

                    final Map<String, BigDecimal> demandWiseFeeDetail = advertisementDemandService
                            .checkPedingAmountByDemand(result);
                    hoardingSearchResult.setPenaltyAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.PENALTYAMOUNT));
                    hoardingSearchResult
                            .setAdditionalTaxAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.ADDITIONALTAXAMOUNT));
                    hoardingSearchResult.setPendingDemandAmount(demandWiseFeeDetail
                            .get(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT));
                    hoardingSearchResults.add(hoardingSearchResult);
                }
            }
        });
        if (agencyWiseHoardingList.size() > 0) {
            final List<HoardingSearch> agencyWiseFinalHoardingList = new ArrayList<HoardingSearch>();
            agencyWiseHoardingList.forEach((key, value) -> {
                agencyWiseFinalHoardingList.add(value);
            });
            return agencyWiseFinalHoardingList;
        }
        return hoardingSearchResults;

    }

    public List<HoardingAgencyWiseSearch> getAgencyWiseAdvertisementSearchResult(
            final AdvertisementPermitDetail advPermitDetail) {

        final List<AdvertisementPermitDetail> advPermitDtl = advertisementPermitDetailRepository
                .searchAdvertisementPermitDetailBySearchParams(advPermitDetail);
        final HashMap<String, HoardingAgencyWiseSearch> agencyWiseHoardingMap = new HashMap<String, HoardingAgencyWiseSearch>();
        final List<HoardingAgencyWiseSearch> agencyWiseFinalHoardingList = new ArrayList<HoardingAgencyWiseSearch>();

        advPermitDtl.forEach(result -> {
            if (result.getAgency() != null) {
                final HoardingAgencyWiseSearch hoardingSearchResult = new HoardingAgencyWiseSearch();
                hoardingSearchResult.setAdvertisementNumber(result.getAdvertisement().getAdvertisementNumber());
                hoardingSearchResult.setAgencyName(result.getAgency() != null ? result.getAgency().getName() : "");
                hoardingSearchResult.setCategoryName(result.getAdvertisement().getCategory().getName());
                hoardingSearchResult.setSubCategoryName(result.getAdvertisement().getSubCategory().getDescription());
                BigDecimal totalDemandAmount = BigDecimal.ZERO;
                BigDecimal totalCollectedAmount = BigDecimal.ZERO;
                BigDecimal totalPending = BigDecimal.ZERO;
                BigDecimal totalPenalty = BigDecimal.ZERO;
                BigDecimal totalAdditionalTax = BigDecimal.ZERO;
                final Map<String, BigDecimal> demandWiseFeeDetail = advertisementDemandService
                        .checkPendingAmountByDemand(result);
                totalDemandAmount = totalDemandAmount.add(demandWiseFeeDetail.get(AdvertisementTaxConstants.TOTAL_DEMAND));
                totalCollectedAmount = totalCollectedAmount
                        .add(demandWiseFeeDetail.get(AdvertisementTaxConstants.TOTALCOLLECTION));
                totalPending = totalPending.add(demandWiseFeeDetail.get(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT));
                totalPenalty = totalPenalty.add(demandWiseFeeDetail.get(AdvertisementTaxConstants.PENALTYAMOUNT));
                totalAdditionalTax = totalAdditionalTax
                        .add(demandWiseFeeDetail.get(AdvertisementTaxConstants.ADDITIONALTAXAMOUNT));

                final HoardingAgencyWiseSearch hoardingSearchObj = agencyWiseHoardingMap.get(result.getAgency().getName());
                if (hoardingSearchObj == null) {
                    hoardingSearchResult.setAgency(result.getAgency().getId());
                    hoardingSearchResult.setTotalDemand(totalDemandAmount);
                    hoardingSearchResult.setCollectedAmount(totalCollectedAmount);
                    hoardingSearchResult.setPendingAmount(totalDemandAmount.subtract(totalCollectedAmount));
                    hoardingSearchResult.setPenaltyAmount(totalPenalty);
                    hoardingSearchResult.setAdditionalTaxAmount(totalAdditionalTax);
                    hoardingSearchResult.setTotalHoardingInAgency(1);
                    hoardingSearchResult.setHordingIdsSearchedByAgency(result.getId().toString());
                    hoardingSearchResult.setOwnerDetail(result.getOwnerDetail() != null ? result.getOwnerDetail() : "");
                    agencyWiseHoardingMap.put(result.getAgency().getName(), hoardingSearchResult);
                } else {

                    hoardingSearchResult.setAgency(result.getAgency().getId());
                    hoardingSearchResult.setTotalDemand(
                            agencyWiseHoardingMap.get(result.getAgency().getName()).getTotalDemand().add(totalDemandAmount));
                    hoardingSearchResult.setCollectedAmount(agencyWiseHoardingMap.get(result.getAgency().getName())
                            .getCollectedAmount().add(totalCollectedAmount));
                    hoardingSearchResult.setPendingAmount(
                            agencyWiseHoardingMap.get(result.getAgency().getName()).getPendingAmount().add(totalPending));
                    hoardingSearchResult.setPenaltyAmount(
                            agencyWiseHoardingMap.get(result.getAgency().getName()).getPenaltyAmount().add(totalPenalty));
                    hoardingSearchResult.setAdditionalTaxAmount(
                            agencyWiseHoardingMap.get(result.getAgency().getName()).getAdditionalTaxAmount()
                                    .add(totalAdditionalTax));
                    hoardingSearchResult.setTotalHoardingInAgency(hoardingSearchObj.getTotalHoardingInAgency() + 1);
                    agencyWiseHoardingMap.put(result.getAgency().getName(), hoardingSearchResult);
                }

            }

        });
        if (agencyWiseHoardingMap.size() > 0)
            agencyWiseHoardingMap.forEach((key, value) -> {
                agencyWiseFinalHoardingList.add(value);
            });

        return agencyWiseFinalHoardingList;
    }

    @SuppressWarnings("unchecked")
    public List<AdvertisementPermitDetail> getAdvertisementPermitDetailBySearchParam(final Long agencyId, final Long category,
            final Long subcategory, final Long zone, final Long ward, final String ownerDetail) {

        final StringBuilder queryString = new StringBuilder();
        queryString
                .append(" from AdvertisementPermitDetail B where B.agency.id=:agencyId  and B.isActive=true and B.advertisement.status=:advertismentStatus ");
        if (category != null)
            queryString.append(" and B.advertisement.category.id =:category");
        if (subcategory != null)
            queryString.append("and B.advertisement.subCategory.id =:subcategory");
        if (zone != null)
            queryString.append("and B.advertisement.locality.id =:zone");
        if (ownerDetail != null && !"".equals(ownerDetail))
            queryString.append("and lower(B.ownerDetail)  like '%" + ownerDetail.toLowerCase() + "%'");
        if (ward != null)
            queryString.append("and B.advertisement.ward.id =:ward");
        final Query query = entityManager.unwrap(Session.class).createQuery(queryString.toString());
        query.setParameter("agencyId", agencyId);

        query.setParameter("advertismentStatus", AdvertisementStatus.ACTIVE);
        if (category != null)
            query.setParameter("category", category);
        if (subcategory != null)
            query.setParameter("subCategory", subcategory);
        if (zone != null)
            query.setParameter("zone", zone);
        /*
         * if (ownerDetail != null && !"".equals(ownerDetail)) query.setParameter("ownerDetail", ownerDetail.toLowerCase());
         */
        if (ward != null)
            query.setParameter("ward", ward);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<AdvertisementPermitDetail> getAdvertisementPermitDetailByWardAndAdvertisementNumber(Long wardno,
            String advertisementNo) {

        final StringBuilder queryString = new StringBuilder();
        queryString
                .append(" from AdvertisementPermitDetail B where B.isActive=true and B.advertisement.status=:advertismentStatus ");
        if (advertisementNo != null)
            queryString.append(" and B.advertisement.advertisementNumber =:advertisementNo ");
        if (wardno != null)
            queryString.append(" and B.advertisement.ward.boundaryNum =:ward ");
        final Query query = entityManager.unwrap(Session.class).createQuery(queryString.toString());

        if (advertisementNo != null)
            query.setParameter("advertisementNo", advertisementNo);
        query.setParameter("advertismentStatus", AdvertisementStatus.ACTIVE);
        if (wardno != null)
            query.setParameter("ward", wardno);
        return query.list();
    }
}
