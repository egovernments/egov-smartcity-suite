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
package org.egov.adtax.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.adtax.entity.AgencyWiseCollection;
import org.egov.adtax.entity.AgencyWiseCollectionDetail;
import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.repository.AgencyWiseCollectionRepository;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AgencyWiseCollectionService {
    private final AgencyWiseCollectionRepository agencyWiseCollectionRepository;
    private @Autowired AdvertisementService hoardingService;
    private @Autowired AppConfigValueService appConfigValuesService;
    private @Autowired AdvertisementDemandService advertisementDemandService;

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
 * 
 * @param hoardingList
 * @return
 */
   public AgencyWiseCollection buildAgencyWiseObjectByHoardings(final String[] hoardingList) {

        final Set<AgencyWiseCollectionDetail> agencyWiseCollectionDetails = new HashSet<AgencyWiseCollectionDetail>(0);
        BigDecimal totalAmount = BigDecimal.ZERO;

        final AgencyWiseCollection agencyWiseCollection = new AgencyWiseCollection();

        final Installment installment = advertisementDemandService.getCurrentInstallment();
        final EgDemandReason pendingTaxReason = advertisementDemandService.getDemandReasonByCodeAndInstallment(
                AdvertisementTaxConstants.DEMANDREASON_PENALTY, installment);

        final AppConfigValues penaltyCalculationRequired = appConfigValuesService.getConfigValuesByModuleAndKey(
                AdvertisementTaxConstants.MODULE_NAME, AdvertisementTaxConstants.PENALTYCALCULATIONREQUIRED).get(0);

        for (final String hoardingId : hoardingList) {
            final Advertisement hoarding = hoardingService.findBy(Long.valueOf(hoardingId.trim()));

            if (hoarding != null) {
                BigDecimal penaltyAmount = BigDecimal.ZERO;
                BigDecimal penaltyAmt = BigDecimal.ZERO;

                for (final EgDemandDetails demandDtl : hoarding.getDemandId().getEgDemandDetails())
                    // Mean if installment is different than current, then
                    // calculate
                    // penalty
                    if (demandDtl.getAmount().subtract(demandDtl.getAmtCollected()).compareTo(BigDecimal.ZERO) > 0) {
                        final BigDecimal amount = demandDtl.getAmount().subtract(demandDtl.getAmtCollected());

                        if (penaltyCalculationRequired != null
                                && "YES".equalsIgnoreCase(penaltyCalculationRequired.getValue())) {

                            penaltyAmt = advertisementDemandService.calculatePenalty(demandDtl, amount,
                                    hoarding.getPenaltyCalculationDate());
                            totalAmount = totalAmount.add(penaltyAmt);
                            penaltyAmount = penaltyAmount.add(penaltyAmt);
                        }
                        totalAmount = totalAmount.add(amount);

                        buildAgencyWiseCollectionDetail(agencyWiseCollectionDetails, agencyWiseCollection, demandDtl,
                                amount);

                    }

                if (penaltyCalculationRequired != null && "YES".equalsIgnoreCase(penaltyCalculationRequired.getValue()))
                    if (penaltyAmount.compareTo(BigDecimal.ZERO) > 0) {
                        // check any demand reason already present
                        final List<EgDemandDetails> penaltyDmtDtails = advertisementDemandService
                                .getDemandDetailByPassingDemandDemandReason(hoarding.getDemandId(), pendingTaxReason);

                        final AgencyWiseCollectionDetail agencyWiseDt = new AgencyWiseCollectionDetail();
                        agencyWiseDt.setDemand(hoarding.getDemandId());
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

}
