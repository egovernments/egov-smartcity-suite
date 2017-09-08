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

import org.egov.collection.handler.BillInfoMarshaller;
import org.egov.collection.integration.models.BillAccountDetails;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.collection.integration.models.BillDetails;
import org.egov.collection.integration.models.BillInfo.COLLECTIONTYPE;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.models.BillPayeeDetails;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class DemandUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemandUtils.class);

    private DemandUtils() {
        //static API's only
    }

    /**
     * This is used to post the bill Collection details to the collection system
     * in an xml format
     *
     * @param bill(The bill object)
     * @param displayMsg
     * @return String xmlData(it contains the complete bill collection data in
     * xml format) .
     */

    public static String generateBillXML(EgBill bill, String displayMsg) {
        String xmlData = "";
        try {
            if (bill != null && displayMsg != null) {
                xmlData = BillInfoMarshaller.toXML(prepareBillInfoXml(bill, displayMsg));
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Generated Bill XML \r\n {}", xmlData);
            }
        } catch (Exception ex) {
            LOGGER.error("Exception in postBillCollectionDetails", ex);
        }
        return xmlData;
    }

    public static BillInfoImpl prepareBillInfoXml(EgBill bill, String displayMsg) {
        BillInfoImpl billInfo = null;
        try {
            if (bill != null) {
                List<String> collectionModes = new ArrayList<>();
                if (isNotBlank(bill.getCollModesNotAllowed())) {
                    String[] collModes = bill.getCollModesNotAllowed().split(",");
                    for (String collectionMode : collModes) {
                        collectionModes.add(collectionMode);
                    }
                }
                billInfo = new BillInfoImpl(bill.getServiceCode(), bill.getFundCode(), bill.getFunctionaryCode(),
                        bill.getFundSourceCode(), bill.getDepartmentCode(), displayMsg, bill.getCitizenName(),
                        bill.getPartPaymentAllowed(), bill.getOverrideAccountHeadsAllowed(), collectionModes,
                        COLLECTIONTYPE.F);
                BillPayeeDetails billPayeeDetails = new BillPayeeDetails(bill.getCitizenName(), bill.getCitizenAddress(), bill.getEmailId());
                BillDetails billDetails = new BillDetails(bill.getId().toString(), bill.getCreateDate(), bill.getConsumerId(), bill.getConsumerType(),
                        bill.getBoundaryNum().toString(), bill.getBoundaryType(), bill.getDescription(),
                        bill.getTotalAmount(), bill.getMinAmtPayable());
                List<BillPayeeDetails> billPayeeDetList = new ArrayList<>();
                billPayeeDetList.add(billPayeeDetails);
                billInfo.setPayees(billPayeeDetList);
                billInfo.setCallbackForApportioning(bill.getCallBackForApportion());
                for (EgBillDetails egBillDet : bill.getEgBillDetails()) {
                    boolean isActualDemand = egBillDet.getAdditionalFlag() == 1;
                    BillAccountDetails billAccDetails = new BillAccountDetails(egBillDet.getGlcode(), egBillDet.getOrderNo(),
                            egBillDet.getCrAmount(), egBillDet.getDrAmount(), egBillDet.getFunctionCode(),
                            egBillDet.getDescription(), isActualDemand, egBillDet.getPurpose() != null ? PURPOSE.valueOf(egBillDet.getPurpose()) : PURPOSE.OTHERS);
                    billDetails.addBillAccountDetails(billAccDetails);
                }
                billPayeeDetails.addBillDetails(billDetails);
            }
        } catch (Exception ex) {
            LOGGER.error("Error occurred while preparing Bill Details", ex);
        }
        return billInfo;
    }

}
