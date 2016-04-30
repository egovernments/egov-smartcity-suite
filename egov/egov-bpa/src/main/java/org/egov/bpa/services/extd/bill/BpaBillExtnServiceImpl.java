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
package org.egov.bpa.services.extd.bill;

import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.commons.Installment;
import org.egov.demand.interfaces.BillServiceInterface;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.PersistenceService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


@SuppressWarnings("unchecked")
public class BpaBillExtnServiceImpl extends BillServiceInterface{
	private PersistenceService persistenceService;
	private BpaCommonExtnService bpaCommonExtnService;	

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}
	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonService) {
		this.bpaCommonExtnService = bpaCommonService;
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	 @Override
	  public String getBillXML(Billable billObj) {
	        if (billObj == null) {
	            throw new EGOVRuntimeException("Exception in getBillXML....Billable is null");
	        }
	        return super.getBillXML(billObj);
	    }
		
	@Override
	public List<EgBillDetails> getBilldetails(Billable billObj) {
		List<EgBillDetails> billDetailList = new ArrayList<EgBillDetails>();
	   	int orderNo = 1;
    	BpaBillableExtn bpaBillable = (BpaBillableExtn)billObj;
    	EgDemand dmd = bpaBillable.getCurrentDemand();
    	if (dmd == null) {
    		throw new EGOVRuntimeException("Exception in getBilldetails....Demand is null");
    	}
    	
    	List<EgDemandDetails> details=new ArrayList<EgDemandDetails>(dmd.getEgDemandDetails());
    	
    	
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
    			BigDecimal creaditAmt=BigDecimal.ZERO,debitAmt = BigDecimal.ZERO;
    			creaditAmt = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
    			// If Amount- collected amount greather than zero, then send these demand details to collection.
    			if(creaditAmt.compareTo(BigDecimal.ZERO) > 0){
    				String glCodeForDemandDetail=(demandDetail.getEgDemandReason().getGlcodeId().getGlcode());
    				// Installments of demand detail is used and sending for collection. 
    				Installment instlment=  demandDetail.getEgDemandReason().getEgInstallmentMaster();
    				EgBillDetails billdetail = createBillDetailObject(orderNo,debitAmt,creaditAmt,glCodeForDemandDetail,getReceiptDetailDescription(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()+" "+BpaConstants.COLL_RECEIPTDETAIL_DESC_PREFIX,instlment));
    				orderNo++;
    				billDetailList.add(billdetail);   
    			}
    		}
    	}
    	//TODO: IF LIST SIZE IS ZERO THEN RETURN NULL OR THROW EXCEPTION.
		return billDetailList;
	}
	private EgBillDetails createBillDetailObject(int orderNo,BigDecimal debitAmount,BigDecimal creditAmount,
			String glCodeForDemandDetail,String description) {
		
		EgBillDetails billdetail = new EgBillDetails();
		billdetail.setFunctionCode(getFunctionCode()); //TODO ADD FUNCTIONCODE
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
	
	private String getFunctionCode() {
		 String fundCode=(bpaCommonExtnService.getAppconfigValueResult(BpaConstants.BPAMODULENAME, BpaConstants.FUNCTION_CODE, null));
	        if(fundCode!=null && !fundCode.equals(""))
	          return (fundCode);
	        else return null;
	}
	//Formatting the description for bill details. 
		private String getReceiptDetailDescription(String reasonType,Installment instlment) {
		//	return reasonType+(instlment!=null? " "+instlment.getDescription():"");
			return reasonType;//Not appending financial year details in receipt/collection screen.
		}
		
		
	@Override
	public void cancelBill() {
		// TODO Auto-generated method stub
		
	}

}
