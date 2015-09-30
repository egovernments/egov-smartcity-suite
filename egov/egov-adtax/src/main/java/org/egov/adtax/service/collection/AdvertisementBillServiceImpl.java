package org.egov.adtax.service.collection;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.demand.interfaces.BillServiceInterface;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementBillServiceImpl extends BillServiceInterface{
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private  AdvertisementDemandService advertisementDemandService;
 
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public List<EgBillDetails> getBilldetails(Billable billObj) {
        List<EgBillDetails> billDetailList = new ArrayList<EgBillDetails>();
        int orderNo = 1;
        AdvertisementBillable advBillable = (AdvertisementBillable)billObj;
        EgDemand dmd = advBillable.getCurrentDemand();
        List<EgDemandDetails> details=new ArrayList<EgDemandDetails>(dmd.getEgDemandDetails());
        EgDemandDetails penaltyExistingDemandDetail=null;
  
        
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                AdvertisementTaxConstants.MODULE_NAME, AdvertisementTaxConstants.PENALTYCALCULATIONREQUIRED).get(0);
       
       

        if (!details.isEmpty()) {
            Collections.sort(details, new Comparator<EgDemandDetails>() {
                @Override
                public int compare(EgDemandDetails c1, EgDemandDetails c2) {
                    //should ensure that list doesn't contain null values!
                    return c1.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster().compareTo(c2.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster());
                }
               });
           }
        
        for (EgDemandDetails demandDetail : details) {
            if(demandDetail.getAmount().compareTo(BigDecimal.ZERO) >0){
                    BigDecimal creaditAmt=BigDecimal.ZERO;
                    creaditAmt = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
              
                    // If Amount- collected amount greather than zero, then send these demand details to collection.
                    if(creaditAmt.compareTo(BigDecimal.ZERO) > 0){
                        
                        if(AdvertisementTaxConstants.DEMANDREASON_PENALTY.equalsIgnoreCase(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()))
                        {
                            penaltyExistingDemandDetail=   demandDetail; 
                        }else
                        {
                            
                           EgBillDetails billdetail = createBillDetailObject(orderNo,BigDecimal.ZERO,creaditAmt,(demandDetail.getEgDemandReason().getGlcodeId().getGlcode()),demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()+" "+AdvertisementTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX);
                            orderNo++;
                            billDetailList.add(billdetail);
                        }
                    }
            }
    }
        if(appConfigValue!=null &&  "YES".equalsIgnoreCase(appConfigValue.getValue()))
        {
            BigDecimal penaltyAmount=    advertisementDemandService.checkPenaltyAmountByDemand(dmd);
            if(penaltyAmount.compareTo(BigDecimal.ZERO)>0)
            {
            
              // Update penalty to existing demand
               if(penaltyExistingDemandDetail==null){
                EgDemandReason demandReason= advertisementDemandService.getDemandReasonByCodeAndInstallment(AdvertisementTaxConstants.DEMANDREASON_PENALTY, advertisementDemandService.getCurrentInstallment());
                EgBillDetails billdetail = createBillDetailObject(orderNo,BigDecimal.ZERO,penaltyAmount,demandReason.getGlcodeId().getGlcode(),demandReason.getEgDemandReasonMaster().getReasonMaster()+" "+AdvertisementTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX);
                billDetailList.add(billdetail);   
              }else
              {
                  BigDecimal creaditAmt=penaltyExistingDemandDetail.getAmount().subtract(penaltyExistingDemandDetail.getAmtCollected());
                  EgBillDetails billdetail = createBillDetailObject(orderNo,BigDecimal.ZERO,creaditAmt.add(penaltyAmount),(penaltyExistingDemandDetail.getEgDemandReason().getGlcodeId().getGlcode()),penaltyExistingDemandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()+" "+AdvertisementTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX);
                  billDetailList.add(billdetail);
              }
            }
                //TODO: PENALTY GENERATION REQUIRED OR NOT ?
        }
        
    //TODO: IF LIST SIZE IS ZERO THEN RETURN NULL OR THROW EXCEPTION.
            return billDetailList;
    }
    private EgBillDetails createBillDetailObject(int orderNo,BigDecimal debitAmount,BigDecimal creditAmount,
            String glCodeForDemandDetail,String description) {
    
    EgBillDetails billdetail = new EgBillDetails();
    billdetail.setFunctionCode(null); //TODO ADD FUNCTIONCODE
    billdetail.setOrderNo(orderNo);
    billdetail.setCreateDate(new Date());
    billdetail.setModifiedDate(new Date());
    billdetail.setCrAmount(creditAmount);
    billdetail.setDrAmount(debitAmount);
    billdetail.setGlcode(glCodeForDemandDetail);
    billdetail.setDescription(description);
    billdetail.setAdditionalFlag(1);
    return billdetail;
} 
    @Override
    public void cancelBill() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public String getBillXML(Billable billObj) {
        String collectXML;
        collectXML = URLEncoder.encode(super.getBillXML(billObj));
          return collectXML;
      }
          
}
