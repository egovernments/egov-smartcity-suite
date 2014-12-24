package org.egov.demand.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.erpcollection.integration.models.BillAccountDetails;
import org.egov.erpcollection.integration.models.BillDetails;
import org.egov.erpcollection.integration.models.BillInfoImpl;
import org.egov.erpcollection.integration.models.BillPayeeDetails;
import org.egov.erpcollection.integration.models.BillInfo.COLLECTIONTYPE;
import org.egov.erpcollection.web.handler.BillCollectXmlHandler;

public class DemandUtils {

	public static final Logger LOGGER=Logger.getLogger(DemandUtils.class);
	
	/**
	 * This is used to post the bill Collection details to the collection system in an xml format
	 * 
	 *@param org.egov.demand.model.EgBill bill(The bill object)
	 *@param java.lang.String displayMsg
	 *
	 *@return String xmlData(it contains the complete bill collection data in xml format) .
	 *
	 */

 
	public String generateBillXML(EgBill bill, String displayMsg) {
		String xmlData = "";
		BillCollectXmlHandler handler = new BillCollectXmlHandler();
		try {
			if(bill!=null && displayMsg!=null)
			{
			LOGGER.info(" before preparing Bill XML xmlData===" + xmlData);
			xmlData = handler.toXML(prepareBillInfoXml(bill, displayMsg));
			LOGGER.info("xmlData===" + xmlData);
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in postBillCollectionDetails", ex);
		}
		return xmlData;
	}
	
	
	public BillInfoImpl prepareBillInfoXml(EgBill bill, String displayMsg) {
		List<BillPayeeDetails> billPayeeDetList = new ArrayList<BillPayeeDetails>();
		BillDetails billDetails = null;
		BillAccountDetails billAccDetails = null;
		List<String> collModesList = new ArrayList<String>();
		BillPayeeDetails billPayeeDet = null;
		BillInfoImpl billInfoImpl = null;
		try {
			if (bill != null) {
				if(bill.getCollModesNotAllowed()!=null){
					String[] collModes = bill.getCollModesNotAllowed().split(",");
					for (String coll : collModes) {
						collModesList.add(coll);
					}
				}
				billInfoImpl = new BillInfoImpl(bill.getServiceCode(), bill.getFundCode(), bill
						.getFunctionaryCode(), bill.getFundSourceCode(), bill.getDepartmentCode(),
						displayMsg, bill.getCitizenName(),bill.getPartPaymentAllowed(), bill
								.getOverrideAccountHeadsAllowed(), collModesList,COLLECTIONTYPE.F);
				billPayeeDet = new BillPayeeDetails(bill.getCitizenName(), bill.getCitizenAddress());
				billDetails = new BillDetails(bill.getId().toString(), bill.getCreateTimeStamp(),
						bill.getConsumerId(), bill.getBoundaryNum().toString(), bill
								.getBoundaryType(), bill.getDescription(), bill.getTotalAmount(),
						bill.getMinAmtPayable());
				billPayeeDetList.add(billPayeeDet);
				billInfoImpl.setPayees(billPayeeDetList);
				billInfoImpl.setCallbackForApportioning(bill.getCallBackForApportion());

				for (EgBillDetails egBillDet : bill.getEgBillDetails()) {
					billAccDetails = new BillAccountDetails(egBillDet.getGlcode(), egBillDet
							.getOrderNo(), egBillDet.getCrAmount(), egBillDet.getDrAmount(),
							egBillDet.getFunctionCode(), egBillDet.getDescription(), 
							egBillDet.getAdditionalFlag());
					billDetails.addBillAccountDetails(billAccDetails);
				}
				billPayeeDet.addBillDetails(billDetails);
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in prepareBillInfoXml method", ex);
		}
		return billInfoImpl;
	}

}
