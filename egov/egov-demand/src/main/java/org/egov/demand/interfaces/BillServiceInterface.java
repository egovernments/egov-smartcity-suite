/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.demand.interfaces;

import org.apache.log4j.Logger;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.utils.DemandUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * This is an abstract which will be overridden by individual Bill service
 * (PropertyTax, Professional Tax etc).
 *
 * @author Satyam
 */

public abstract class BillServiceInterface {

    public static final Logger LOGGER = Logger.getLogger(BillServiceInterface.class);

    @Autowired
    private EgBillDao egBillDAO;

    public String getBillXML(Billable billObj) {
        if (billObj == null)
            throw new ApplicationRuntimeException("Billable object can't be null");
        return generateXML(generateBill(billObj));
    }

    public String generateXML(EgBill bill) {
        if (bill == null)
            throw new ApplicationRuntimeException("Bill object can't be null");
        return DemandUtils.generateBillXML(bill, bill.getDisplayMessage());
    }

    public final EgBill generateBill(Billable billObj) {
        EgBill bill = new EgBill();
        bill.setBillNo(billObj.getReferenceNumber());
        bill.setBoundaryNum(billObj.getBoundaryNum().intValue());
        bill.setTransanctionReferenceNumber(billObj.getTransanctionReferenceNumber());
        bill.setBoundaryType(billObj.getBoundaryType());
        bill.setCitizenAddress(billObj.getBillAddress());
        bill.setCitizenName(billObj.getBillPayee());
        bill.setCollModesNotAllowed(billObj.getCollModesNotAllowed());
        bill.setDepartmentCode(billObj.getDepartmentCode());
        bill.setEgBillType(billObj.getBillType());
        bill.setFunctionaryCode(billObj.getFunctionaryCode());
        bill.setFundCode(billObj.getFundCode());
        bill.setFundSourceCode(billObj.getFundSourceCode());
        bill.setIssueDate(new Date());
        bill.setLastDate(billObj.getBillLastDueDate());
        bill.setModule(billObj.getModule());
        bill.setOverrideAccountHeadsAllowed(billObj.getOverrideAccountHeadsAllowed());
        bill.setPartPaymentAllowed(billObj.getPartPaymentAllowed());
        bill.setServiceCode(billObj.getServiceCode());
        bill.setIs_Cancelled("N");
        bill.setIs_History("N");
        bill.setModifiedDate(new Date());
        bill.setTotalAmount(billObj.getTotalAmount());
        bill.setUserId(billObj.getUserId());
        bill.setCreateDate(new Date());
        EgDemand currentDemand = billObj.getCurrentDemand();
        bill.setEgDemand(currentDemand);
        bill.setDescription(billObj.getDescription());
        bill.setDisplayMessage(billObj.getDisplayMessage());
        bill.setEmailId(billObj.getEmailId());

        if (currentDemand != null && currentDemand.getMinAmtPayable() != null) {
            bill.setMinAmtPayable(currentDemand.getMinAmtPayable());
        } else {
            bill.setMinAmtPayable(BigDecimal.ZERO);
        }

        // Get it from the concrete implementation
        List<EgBillDetails> bd = getBilldetails(billObj);
        for (EgBillDetails billdetails : bd) {
            bill.addEgBillDetails(billdetails);
            billdetails.setEgBill(bill);
        }

        bill.setConsumerId(billObj.getConsumerId());
        bill.setConsumerType(billObj.getConsumerType());
        bill.setCallBackForApportion(billObj.isCallbackForApportion());
        egBillDAO.create(bill);
        return bill;
    }

    public abstract List<EgBillDetails> getBilldetails(Billable billObj);

    public abstract void cancelBill();
}