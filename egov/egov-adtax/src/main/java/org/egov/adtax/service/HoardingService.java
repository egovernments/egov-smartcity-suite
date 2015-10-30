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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.entity.Hoarding;
import org.egov.adtax.entity.enums.HoardingStatus;
import org.egov.adtax.exception.HoardingValidationError;
import org.egov.adtax.repository.HoardingRepository;
import org.egov.adtax.search.contract.HoardingDcbReport;
import org.egov.adtax.search.contract.HoardingSearch;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.services.CollectionIntegrationService;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HoardingService {

    private final HoardingRepository hoardingRepository;
  
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
    public HoardingService(final HoardingRepository hoardingRepository) {
        this.hoardingRepository = hoardingRepository;
    }

    @Transactional
    public Hoarding createHoarding(final Hoarding hoarding) {
        if (hoarding != null && hoarding.getId() == null)
            hoarding.setDemandId(advertisementDemandService.createDemand(hoarding));
        roundOfAllTaxAmount(hoarding);
        return hoardingRepository.save(hoarding);
    }

    @Transactional
    public Hoarding updateHoarding(final Hoarding hoarding) throws HoardingValidationError {

        getCurrentSession().evict(hoarding);

        final Hoarding actualHoarding = getHoardingByHoardingNumber(hoarding.getHoardingNumber());
        final boolean anyDemandPendingForCollection = advertisementDemandService
                .anyDemandPendingForCollection(actualHoarding);

         if (!actualHoarding.getAgency().equals(hoarding.getAgency()) && anyDemandPendingForCollection)
            throw new HoardingValidationError("agency", "ADTAX.001");
        // If demand already collected for the current year, fee updated from
        // UI, do not update demand details. Update only fee details of hoarding.
        // We should not allow user to update demand if any collection happened in
        // the current year.

        if (advertisementDemandService.collectionDoneForThisYear(actualHoarding) && anyDemandPendingForCollection
                && (!actualHoarding.getTaxAmount().equals(hoarding.getTaxAmount())
                        || checkEncroachmentFeeChanged(hoarding, actualHoarding) || checkPendingTaxChanged(hoarding,
                            actualHoarding)))
            throw new HoardingValidationError("taxAmount", "ADTAX.002");
        if (!actualHoarding.getStatus().equals(hoarding.getStatus())
                && hoarding.getStatus().equals(HoardingStatus.CANCELLED) && anyDemandPendingForCollection)
            throw new HoardingValidationError("status", "ADTAX.003");

        // If demand pending for collection, then only update demand details.
        // If demand fully paid and user changed tax details, then no need to
        // update demand details.
        if (anyDemandPendingForCollection)
            advertisementDemandService.updateDemand(hoarding, actualHoarding.getDemandId());
        roundOfAllTaxAmount(hoarding);
        return hoardingRepository.save(hoarding);
    }

    private void roundOfAllTaxAmount(final Hoarding hoarding) {
        if(hoarding.getEncroachmentFee()!=null)
            hoarding.setEncroachmentFee(hoarding.getEncroachmentFee().setScale(2, BigDecimal.ROUND_HALF_UP));
        
        if(hoarding.getTaxAmount()!=null)
            hoarding.setTaxAmount( hoarding.getTaxAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
            
        if(hoarding.getPendingTax()!=null)
            hoarding.setPendingTax( hoarding.getPendingTax().setScale(2, BigDecimal.ROUND_HALF_UP)); 
    }

    private boolean checkPendingTaxChanged(Hoarding hoarding, Hoarding actualHoarding) {
        if (actualHoarding.getPendingTax()== null && hoarding.getPendingTax() != null)
            return true;
        else if (hoarding.getPendingTax() == null && actualHoarding.getPendingTax() != null)
            return true;
        else if (actualHoarding.getPendingTax() != null && hoarding.getPendingTax() != null
                && !actualHoarding.getPendingTax().equals(hoarding.getPendingTax()))
            return true;

        return false;
    }

    private boolean checkEncroachmentFeeChanged(final Hoarding hoarding, final Hoarding actualHoarding) {

        if (actualHoarding.getEncroachmentFee() == null && hoarding.getEncroachmentFee() != null)
            return true;
        else if (hoarding.getEncroachmentFee() == null && actualHoarding.getEncroachmentFee() != null)
            return true;
        else if (actualHoarding.getEncroachmentFee() != null && hoarding.getEncroachmentFee() != null
                && !actualHoarding.getEncroachmentFee().equals(hoarding.getEncroachmentFee()))
            return true;

        return false;
    }

    public List<Object[]> searchBySearchType(final Hoarding hoarding, final String searchType) {
        return hoardingRepository.fetchHoardingsBySearchType(hoarding, searchType);
    }
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Hoarding getHoardingByHoardingNumber(final String hoardingNumber) {
        return hoardingRepository.findByHoardingNumber(hoardingNumber);
    }

    public List<HoardingSearch> getHoardingSearchResult(final HoardingSearch hoardingSearch) {
        final List<Hoarding> hoardings = hoardingRepository.fetchHoardingsLike(hoardingSearch);
        final List<HoardingSearch> hoardingSearchResults = new ArrayList<>();
        hoardings.forEach(result -> {
            final HoardingSearch hoardingSearchResult = new HoardingSearch();
            hoardingSearchResult.setHoardingNumber(result.getHoardingNumber());
            hoardingSearchResult.setApplicationNumber(result.getApplicationNumber());
            hoardingSearchResult.setApplicationFromDate(result.getApplicationDate());
            hoardingSearchResult.setAgencyName(result.getAgency().getName());
            hoardingSearchResult.setStatus(result.getStatus());
            hoardingSearchResults.add(hoardingSearchResult);
        });
        return hoardingSearchResults;
    }
    
public List<HoardingDcbReport> getHoardingWiseDCBResult(final Hoarding hoarding) {
     List<HoardingDcbReport> HoardingDcbReportResults = new ArrayList<>();
    Map<String,BillReceiptInfo> billReceiptInfoMap = new HashMap<String,BillReceiptInfo>();
        if(hoarding!=null && hoarding.getDemandId()!=null)
 {
            for (EgDemandDetails demandDtl : hoarding.getDemandId().getEgDemandDetails()) {
                HoardingDcbReport hoardingReport = new HoardingDcbReport();
                Set<String> receiptNumbetSet = new HashSet<String>();
                StringBuffer agencyName = new StringBuffer();
                StringBuffer receiptNumber = new StringBuffer();
                hoardingReport.setDemandReason(demandDtl.getEgDemandReason().getEgDemandReasonMaster()
                        .getReasonMaster());
                hoardingReport.setInstallmentYearDescription(demandDtl.getEgDemandReason().getEgInstallmentMaster()
                        .getDescription());
                hoardingReport.setDemandAmount(demandDtl.getAmount());
                hoardingReport.setCollectedAmount(demandDtl.getAmtCollected());

                for (EgdmCollectedReceipt collRecpt : demandDtl.getEgdmCollectedReceipts()) {
                    if (!collRecpt.isCancelled()) {
                        receiptNumbetSet.add(collRecpt.getReceiptNumber());
                        receiptNumber.append(collRecpt.getReceiptNumber()).append(" ");
                    }
                }
                if (receiptNumbetSet.size() > 0) {
                    hoardingReport.setReceiptNumber(receiptNumber.toString());
                    billReceiptInfoMap = collectionIntegrationService.getReceiptInfo(
                            AdvertisementTaxConstants.SERVICE_CODE, receiptNumbetSet);

                }
                if (billReceiptInfoMap.size() > 0) {
                    for (Map.Entry<String, BillReceiptInfo> map : billReceiptInfoMap.entrySet()) {
                        agencyName.append(map.getValue().getPayeeName());
                        agencyName.append(" ");
                    }

                }
                hoardingReport.setPayeeName(agencyName.toString());
                HoardingDcbReportResults.add(hoardingReport);
            }
        }
    return HoardingDcbReportResults;

}
    public List<HoardingSearch> getHoardingSearchResult(final Hoarding hoarding, final String searchType) {

        final List<Hoarding> hoardings = hoardingRepository.fetchHoardingsBySearchParams(hoarding);
        final HashMap<String, HoardingSearch> agencyWiseHoardingList = new HashMap<String, HoardingSearch>();
        final List<HoardingSearch> hoardingSearchResults = new ArrayList<>();

        hoardings.forEach(result -> {
            final HoardingSearch hoardingSearchResult = new HoardingSearch();
            hoardingSearchResult.setHoardingNumber(result.getHoardingNumber());
            hoardingSearchResult.setApplicationNumber(result.getApplicationNumber());
            hoardingSearchResult.setApplicationFromDate(result.getApplicationDate());
            hoardingSearchResult.setAgencyName(result.getAgency().getName());
            hoardingSearchResult.setStatus(result.getStatus());
            if (result.getDemandId() != null){
                if (searchType != null && searchType.equalsIgnoreCase("agency")) {
                    // PASS DEMAND OF EACH HOARDING AND GROUP BY AGENCY WISE.
                     final Map<String, BigDecimal> demandWiseFeeDetail = advertisementDemandService.checkPedingAmountByDemand(result.getDemandId(),result.getPenaltyCalculationDate());
                     
                     final HoardingSearch hoardingSearchObj = agencyWiseHoardingList.get(result.getAgency().getName());
                    if (hoardingSearchObj == null) {
                        hoardingSearchResult.setPenaltyAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.PENALTYAMOUNT));
                        hoardingSearchResult.setPendingDemandAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT));
                        hoardingSearchResult.setTotalHoardingInAgency(1);
                        hoardingSearchResult.setHordingIdsSearchedByAgency(result.getId().toString());
                        agencyWiseHoardingList.put(result.getAgency().getName(), hoardingSearchResult);
                    } else {
                        StringBuffer hoardingIds= new StringBuffer();
                        hoardingSearchObj.setPenaltyAmount(
                                hoardingSearchObj.getPenaltyAmount().add(demandWiseFeeDetail.get(AdvertisementTaxConstants.PENALTYAMOUNT)));
                        hoardingSearchObj.setPendingDemandAmount(hoardingSearchObj.getPendingDemandAmount()
                                .add(demandWiseFeeDetail.get(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT)));
                        hoardingSearchObj.setTotalHoardingInAgency(hoardingSearchObj.getTotalHoardingInAgency() + 1);
                        
                        hoardingIds.append(hoardingSearchObj.getHordingIdsSearchedByAgency()).append("~").append(result.getId());
                           hoardingSearchObj.setHordingIdsSearchedByAgency(hoardingIds.toString()); 
                        // agencyWiseHoardingList.put(result.getAgency().getName(), hoardingSearchObj);
                                   }
                } else {
                     
                    final Map<String, BigDecimal> demandWiseFeeDetail = advertisementDemandService.checkPedingAmountByDemand(result.getDemandId(),result.getPenaltyCalculationDate());
                    hoardingSearchResult.setPenaltyAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.PENALTYAMOUNT));
                    hoardingSearchResult.setPendingDemandAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT));
                    hoardingSearchResults.add(hoardingSearchResult);
                }
            }
        });
        if (agencyWiseHoardingList.size() > 0)
            agencyWiseHoardingList.forEach((key, value) -> {
                hoardingSearchResults.add(value);
            });
        return hoardingSearchResults;

    }

    public Hoarding findByHoardingNumber(final String hoardingNumber) {
        return hoardingRepository.findByHoardingNumber(hoardingNumber);
    }
    public Hoarding findBy(final Long hoardingId) {
        return hoardingRepository.findOne(hoardingId);
    }
}
