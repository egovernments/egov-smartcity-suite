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

import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.AgencyWiseCollection;
import org.egov.adtax.entity.AgencyWiseCollectionDetail;
import org.egov.adtax.entity.AgencyWiseCollectionSearch;
import org.egov.adtax.repository.AgencyWiseCollectionRepository;
import org.egov.adtax.service.penalty.AdvertisementPenaltyCalculator;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class AgencyWiseCollectionService {
    private final AgencyWiseCollectionRepository agencyWiseCollectionRepository;
    private @Autowired AdvertisementPermitDetailService advertisementPermitDetailService;
    private @Autowired AppConfigValueService appConfigValuesService;
    private @Autowired AdvertisementDemandService advertisementDemandService;
    @Autowired
    private AdvertisementPenaltyCalculator advertisementPenaltyCalculator;

    @Autowired
    public AgencyWiseCollectionService(final AgencyWiseCollectionRepository agencyWiseCollectionRepository) {
        this.agencyWiseCollectionRepository = agencyWiseCollectionRepository;
    }

    public AgencyWiseCollection findBy(final Long id) {
        return agencyWiseCollectionRepository.findOne(id);
    }

    public AgencyWiseCollection findByBillNumber(final String billNumber) {
        return agencyWiseCollectionRepository.findByBillNumber(billNumber);
    }

    @Transactional
    public AgencyWiseCollection createAgencyWiseCollection(final AgencyWiseCollection agencyWiseCollection) {
        return agencyWiseCollectionRepository.save(agencyWiseCollection);
    }

    public AgencyWiseCollection getAgencyWiseCollectionByDemand(final EgDemand demand) {
        return agencyWiseCollectionRepository.findAgencyWiseCollectionByDemand(demand.getId());

    }

    /**
     * @param hoardingList
     * @return
     */
    public AgencyWiseCollection buildAgencyWiseObjectByHoardings(final String[] hoardingList) {

        final Set<AgencyWiseCollectionDetail> agencyWiseCollectionDetails = new HashSet<AgencyWiseCollectionDetail>(0);
        BigDecimal totalAmount = BigDecimal.ZERO;

        final AgencyWiseCollection agencyWiseCollection = new AgencyWiseCollection();

        final Installment installment = advertisementDemandService.getCurrentInstallment();

        Map<Installment, BigDecimal> penaltyReasons = null;

        for (final String hoardingId : hoardingList) {
            final AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findBy(Long
                    .valueOf(hoardingId.trim()));
            penaltyReasons = new HashMap<Installment, BigDecimal>();

            if (advertisementPermitDetail != null && advertisementPermitDetail.getAdvertisement() != null) {
                // BigDecimal penaltyAmount = BigDecimal.ZERO;
                // BigDecimal penaltyAmt = BigDecimal.ZERO;

                for (final EgDemandDetails demandDtl : advertisementPermitDetail.getAdvertisement().getDemandId()
                        .getEgDemandDetails())

                    if (demandDtl.getAmount().subtract(demandDtl.getAmtCollected()).compareTo(BigDecimal.ZERO) > 0) {
                        final BigDecimal amount = demandDtl.getAmount().subtract(demandDtl.getAmtCollected());

                        totalAmount = totalAmount.add(amount);

                        buildAgencyWiseCollectionDetail(agencyWiseCollectionDetails, agencyWiseCollection, demandDtl,
                                amount);

                    }

                penaltyReasons = advertisementPenaltyCalculator.getPenaltyByInstallment(advertisementPermitDetail);

                if (penaltyReasons != null && penaltyReasons.size() > 0) {
                    // check any demand reason already present
                    BigDecimal penaltyAmount = BigDecimal.ZERO;

                    for (Map.Entry<Installment, BigDecimal> penaltyReason : penaltyReasons.entrySet()) {
                        penaltyAmount = penaltyReason.getValue();

                        if (penaltyAmount.compareTo(BigDecimal.ZERO) > 0) {

                            totalAmount = totalAmount.add(penaltyAmount);

                            EgDemandReason pendingTaxReason = advertisementDemandService
                                    .getDemandReasonByCodeAndInstallment(
                                            AdvertisementTaxConstants.DEMANDREASON_PENALTY, penaltyReason.getKey());

                            final List<EgDemandDetails> penaltyDmtDtails = advertisementDemandService
                                    .getDemandDetailByPassingDemandDemandReason(advertisementPermitDetail
                                            .getAdvertisement().getDemandId(), pendingTaxReason);

                            final AgencyWiseCollectionDetail agencyWiseDt = new AgencyWiseCollectionDetail();
                            agencyWiseDt.setDemand(advertisementPermitDetail.getAdvertisement().getDemandId());
                            agencyWiseDt.setDemandreason(pendingTaxReason);
                            agencyWiseDt.setAmount(penaltyAmount);
                            agencyWiseDt.setAgencyWiseCollection(agencyWiseCollection);

                            if (penaltyDmtDtails != null && penaltyDmtDtails.size() > 0)
                                agencyWiseDt.setDemandDetail(penaltyDmtDtails.get(0));
                            else
                                agencyWiseDt.setDemandDetail(null);
                            agencyWiseCollectionDetails.add(agencyWiseDt);
                        }
                    }
                }
            }
        }

        if (agencyWiseCollection != null) {
            buildAgencyWiseDemand(agencyWiseCollectionDetails, totalAmount, agencyWiseCollection, installment);
            agencyWiseCollection.setAgencyWiseCollectionDetails(agencyWiseCollectionDetails);
            agencyWiseCollection.setTotalAmount(totalAmount);
        }
        return agencyWiseCollection;
    }

    /**
     * Grouping all the agency wise collection details to temporary demand.
     * Default adding status as history for this demand.
     * 
     * @param agencyWiseCollectionDetails
     * @param totalAmount
     * @param agencyWiseCollection
     * @param installment
     */
    private void buildAgencyWiseDemand(final Set<AgencyWiseCollectionDetail> agencyWiseCollectionDetails,
            final BigDecimal totalAmount, final AgencyWiseCollection agencyWiseCollection, final Installment installment) {
        final EgDemand agencyWiseDemand = new EgDemand();

        final HashMap<EgDemandReason, BigDecimal> demandReasonWiseList = getAmountGroupingDemandReason(agencyWiseCollectionDetails);

        if (demandReasonWiseList.size() > 0)
            demandReasonWiseList.forEach((key, value) -> {
                agencyWiseDemand.addEgDemandDetails(EgDemandDetails.fromReasonAndAmounts(value, key, BigDecimal.ZERO));
            });

        agencyWiseDemand.setEgInstallmentMaster(installment);
        agencyWiseDemand.setIsHistory("Y");
        agencyWiseDemand.setCreateDate(new Date());
        agencyWiseDemand.setBaseDemand(totalAmount);
        agencyWiseDemand.setModifiedDate(new Date());
        agencyWiseCollection.setAgencyWiseDemand(agencyWiseDemand);
    }

    private HashMap<EgDemandReason, BigDecimal> getAmountGroupingDemandReason(
            final Set<AgencyWiseCollectionDetail> agencyWiseCollectionDetails) {
        final HashMap<EgDemandReason, BigDecimal> demandReasonWiseList = new HashMap<EgDemandReason, BigDecimal>();

        for (final AgencyWiseCollectionDetail agencyWiseDtl : agencyWiseCollectionDetails)
            if (demandReasonWiseList.get(agencyWiseDtl.getDemandreason()) == null)
                demandReasonWiseList.put(agencyWiseDtl.getDemandreason(), agencyWiseDtl.getAmount());
            else
                demandReasonWiseList.put(agencyWiseDtl.getDemandreason(),
                        demandReasonWiseList.get(agencyWiseDtl.getDemandreason()).add(agencyWiseDtl.getAmount()));// TODO:CHECK
        return demandReasonWiseList;
    }

    private void buildAgencyWiseCollectionDetail(final Set<AgencyWiseCollectionDetail> agencyWiseCollectionDetails,
            final AgencyWiseCollection agencyWiseCollection, final EgDemandDetails demandDtl, final BigDecimal amount) {
        final AgencyWiseCollectionDetail agencyWiseDt = new AgencyWiseCollectionDetail();
        agencyWiseDt.setDemand(demandDtl.getEgDemand());
        agencyWiseDt.setDemandDetail(demandDtl);
        agencyWiseDt.setDemandreason(demandDtl.getEgDemandReason());
        agencyWiseDt.setAmount(amount);
        agencyWiseDt.setAgencyWiseCollection(agencyWiseCollection);
        agencyWiseCollectionDetails.add(agencyWiseDt);
    }

    public List<AgencyWiseCollectionSearch> buildAgencyWiseCollectionSearch(String[] hoardingList) {

        List<AgencyWiseCollectionSearch> permitDetails = new ArrayList<AgencyWiseCollectionSearch>();
        AgencyWiseCollectionSearch agencyWiseCollectionSearchResult = null;

        for (final String hoardingId : hoardingList) {
            final AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findBy(Long
                    .valueOf(hoardingId.trim()));

            if (!permitDetails.contains(advertisementPermitDetail))
                agencyWiseCollectionSearchResult = new AgencyWiseCollectionSearch();
            else {
                for (AgencyWiseCollectionSearch result : permitDetails) {
                    if (result.getAdvertisementPermitId().equals(advertisementPermitDetail.getId())) {
                        agencyWiseCollectionSearchResult = result;
                    }
                }

            }

            Map<String, BigDecimal> demandWiseFeeDetail = advertisementDemandService
                    .checkPedingAmountByDemand(advertisementPermitDetail);
           if(demandWiseFeeDetail.get(AdvertisementTaxConstants.PENALTYAMOUNT).compareTo(BigDecimal.ZERO)>0 ||
                   demandWiseFeeDetail.get(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT).compareTo(BigDecimal.ZERO)>0)
           {
            agencyWiseCollectionSearchResult.setAdvertisementNumber(advertisementPermitDetail.getAdvertisement()
                    .getAdvertisementNumber());
            agencyWiseCollectionSearchResult.setAdvertisementPermitId(advertisementPermitDetail.getId());
            agencyWiseCollectionSearchResult
                    .setAgencyName(advertisementPermitDetail.getAgency() != null ? advertisementPermitDetail
                            .getAgency().getName() : " ");
            agencyWiseCollectionSearchResult.setApplicationNumber(advertisementPermitDetail.getApplicationNumber());
            agencyWiseCollectionSearchResult.setPenaltyAmount(demandWiseFeeDetail
                    .get(AdvertisementTaxConstants.PENALTYAMOUNT));
            agencyWiseCollectionSearchResult.setPendingDemandAmount(demandWiseFeeDetail
                    .get(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT));
            permitDetails.add(agencyWiseCollectionSearchResult);
           }

        }

        return permitDetails;

    }

}
