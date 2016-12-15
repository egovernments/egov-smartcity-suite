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
package org.egov.demand.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.collection.handler.BillCollectXmlHandler;
import org.egov.collection.integration.models.BillAccountDetails;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.collection.integration.models.BillDetails;
import org.egov.collection.integration.models.BillInfo.COLLECTIONTYPE;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.models.BillPayeeDetails;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;

public class DemandUtils {

    public static final Logger LOGGER = Logger.getLogger(DemandUtils.class);

    /**
     * This is used to post the bill Collection details to the collection system
     * in an xml format
     * 
     * @param org.egov.demand.model.EgBill
     *            bill(The bill object)
     * @param java.lang.String
     *            displayMsg
     * @return String xmlData(it contains the complete bill collection data in
     *         xml format) .
     */

    public String generateBillXML(EgBill bill, String displayMsg) {
        String xmlData = "";
        BillCollectXmlHandler handler = new BillCollectXmlHandler();
        try {
            if (bill != null && displayMsg != null) {
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
                if (bill.getCollModesNotAllowed() != null) {
                    String[] collModes = bill.getCollModesNotAllowed().split(",");
                    for (String coll : collModes) {
                        collModesList.add(coll);
                    }
                }
                billInfoImpl = new BillInfoImpl(bill.getServiceCode(), bill.getFundCode(), bill.getFunctionaryCode(),
                        bill.getFundSourceCode(), bill.getDepartmentCode(), displayMsg, bill.getCitizenName(),
                        bill.getPartPaymentAllowed(), bill.getOverrideAccountHeadsAllowed(), collModesList,
                        COLLECTIONTYPE.F);
                billPayeeDet = new BillPayeeDetails(bill.getCitizenName(), bill.getCitizenAddress(), bill.getEmailId());
                billDetails = new BillDetails(bill.getId().toString(), bill.getCreateDate(), bill.getConsumerId(),bill.getConsumerType(),
                        bill.getBoundaryNum().toString(), bill.getBoundaryType(), bill.getDescription(),
                        bill.getTotalAmount(), bill.getMinAmtPayable());
                billPayeeDetList.add(billPayeeDet);
                billInfoImpl.setPayees(billPayeeDetList);
                billInfoImpl.setCallbackForApportioning(bill.getCallBackForApportion());
                boolean isActualDemand = false;

                for (EgBillDetails egBillDet : bill.getEgBillDetails()) {
                    isActualDemand = egBillDet.getAdditionalFlag() == 1 ? true : false;
                    billAccDetails = new BillAccountDetails(egBillDet.getGlcode(), egBillDet.getOrderNo(),
                            egBillDet.getCrAmount(), egBillDet.getDrAmount(), egBillDet.getFunctionCode(),
                            egBillDet.getDescription(), isActualDemand,egBillDet.getPurpose()!=null?PURPOSE.valueOf(egBillDet.getPurpose()):PURPOSE.OTHERS);
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
