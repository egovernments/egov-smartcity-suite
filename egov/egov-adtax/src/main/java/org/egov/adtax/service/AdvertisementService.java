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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.entity.enums.AdvertisementStatus;
import org.egov.adtax.exception.HoardingValidationError;
import org.egov.adtax.repository.AdvertisementRepository;
import org.egov.adtax.search.contract.HoardingDcbReport;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.services.CollectionIntegrationService;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementService {

    @Autowired
    private  AdvertisementRepository advertisementRepository;
  
    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
    @Autowired
    protected CollectionIntegrationService collectionIntegrationService;
    
    @Autowired
    private AdvertisementDemandService advertisementDemandService;

    @Transactional
    public Advertisement createAdvertisement(final Advertisement hoarding) {
        if (hoarding != null && hoarding.getId() == null)
            hoarding.setDemandId(advertisementDemandService.createDemand(hoarding));
        roundOfAllTaxAmount(hoarding);
        return advertisementRepository.save(hoarding);
    }

    @Transactional
    public Advertisement updateAdvertisement(final Advertisement advertisement) throws HoardingValidationError {

        getCurrentSession().evict(advertisement);

        final Advertisement actualHoarding = getHoardingByAdvertisementNumber(advertisement.getAdvertisementNumber());
        final boolean anyDemandPendingForCollection = advertisementDemandService
                .anyDemandPendingForCollection(actualHoarding);

     /*    if (!actualHoarding.getAgency().equals(hoarding.getAgency()) && anyDemandPendingForCollection)
            throw new HoardingValidationError("agency", "ADTAX.001");
     */   // If demand already collected for the current year, fee updated from
        // UI, do not update demand details. Update only fee details of hoarding.
        // We should not allow user to update demand if any collection happened in
        // the current year.

     /*   if (advertisementDemandService.collectionDoneForThisYear(actualHoarding) && anyDemandPendingForCollection
                && (!actualHoarding.getCurrentTaxAmount().equals(hoarding.getCurrentTaxAmount())
                        || checkEncroachmentFeeChanged(hoarding, actualHoarding) || checkPendingTaxChanged(hoarding,
                            actualHoarding)))
            throw new HoardingValidationError("taxAmount", "ADTAX.002");
     */   if (!actualHoarding.getStatus().equals(advertisement.getStatus())
                && advertisement.getStatus().equals(AdvertisementStatus.CANCELLED) && anyDemandPendingForCollection)
            throw new HoardingValidationError("status", "ADTAX.003");

        // If demand pending for collection, then only update demand details.
        // If demand fully paid and user changed tax details, then no need to
        // update demand details.
        if (anyDemandPendingForCollection)
            advertisementDemandService.updateDemand(advertisement, actualHoarding.getDemandId());
        roundOfAllTaxAmount(advertisement);
        return advertisementRepository.save(advertisement);
    }

    private void roundOfAllTaxAmount(final Advertisement hoarding) {
/*        if(hoarding.getCurrentEncroachmentFee()!=null)
            hoarding.setCurrentEncroachmentFee(hoarding.getCurrentEncroachmentFee().setScale(2, BigDecimal.ROUND_HALF_UP));
        
        if(hoarding.getCurrentTaxAmount()!=null)
            hoarding.setCurrentTaxAmount( hoarding.getCurrentTaxAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
            
        if(hoarding.getPendingTax()!=null)
            hoarding.setPendingTax( hoarding.getPendingTax().setScale(2, BigDecimal.ROUND_HALF_UP)); 
*/    }

    private boolean checkPendingTaxChanged(Advertisement hoarding, Advertisement actualHoarding) {
        if (actualHoarding.getPendingTax()== null && hoarding.getPendingTax() != null)
            return true;
        else if (hoarding.getPendingTax() == null && actualHoarding.getPendingTax() != null)
            return true;
        else if (actualHoarding.getPendingTax() != null && hoarding.getPendingTax() != null
                && !actualHoarding.getPendingTax().equals(hoarding.getPendingTax()))
            return true;

        return false;
    }

    private boolean checkEncroachmentFeeChanged(final Advertisement hoarding, final Advertisement actualHoarding) {

      /*  if (actualHoarding.getCurrentEncroachmentFee() == null && hoarding.getCurrentEncroachmentFee() != null)
            return true;
        else if (hoarding.getCurrentEncroachmentFee() == null && actualHoarding.getCurrentEncroachmentFee() != null)
            return true;
        else if (actualHoarding.getCurrentEncroachmentFee() != null && hoarding.getCurrentEncroachmentFee() != null
                && !actualHoarding.getCurrentEncroachmentFee().equals(hoarding.getCurrentEncroachmentFee()))
            return true;
*/
        return false;
    }

    public List<Object[]> searchBySearchType(final Advertisement hoarding, final String searchType) {
        return advertisementRepository.fetchAdvertisementBySearchType(hoarding, searchType);
    }
    
    public int getActivePermanentAdvertisementsByCurrentInstallment(Installment installment) {
        return advertisementRepository.findActivePermanentAdvertisementsByCurrentInstallment(installment);
    }
    @Transactional
    public List<Advertisement> findActivePermanentAdvertisementsByCurrentInstallmentAndNumberOfResultToFetch(
            Installment installment, int noOfResultToFetch) {
        return advertisementRepository.findActivePermanentAdvertisementsByCurrentInstallmentAndNumberOfResultToFetch(
                installment, noOfResultToFetch);
    }
    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Advertisement getHoardingByAdvertisementNumber(final String hoardingNumber) {
        return advertisementRepository.findByAdvertisementNumber(hoardingNumber);
    }
    
public List<HoardingDcbReport> getHoardingWiseDCBResult(final Advertisement hoarding) {
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
  

    public Advertisement findByAdvertisementNumber(final String hoardingNumber) {
        return advertisementRepository.findByAdvertisementNumber(hoardingNumber);
    }
    public Advertisement findBy(final Long hoardingId) {
        return advertisementRepository.findOne(hoardingId);
    }
    
    public Advertisement getAdvertisementByDemand(final EgDemand demand) {
        return advertisementRepository.findByDemandId(demand);
    }
}
