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
package org.egov.egf.web.actions.bill;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.web.actions.voucher.VoucherSearchAction;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results({
    @Result(name = Constants.VIEW, location = "billView-" + Constants.VIEW + ".jsp")
})
public class BillViewAction extends BaseFormAction
{
    private static final Logger LOGGER = Logger.getLogger(VoucherSearchAction.class);
    private static final long serialVersionUID = 1L;
    EgBillregister egBillRegister = new EgBillregister();
    List<Map<String, Object>> billDetailsList = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> subledgerList = new ArrayList<Map<String, Object>>();
    @Autowired
    private EgovCommon egovCommon;
    public List<Map<String, Object>> getSubledgerList() {
        return subledgerList;
    }

    public void setSubledgerList(final List<Map<String, Object>> subledgerList) {
        this.subledgerList = subledgerList;
    }

    public List<Map<String, Object>> getBillDetailsList() {
        return billDetailsList;
    }

    public void setBillDetailsList(final List<Map<String, Object>> billDetailsList) {
        this.billDetailsList = billDetailsList;
    }

    public EgBillregister getEgBillRegister() {
        return egBillRegister;
    }

    public void setEgBillRegister(final EgBillregister egBillRegister) {
        this.egBillRegister = egBillRegister;
    }

    public final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE);

    @Override
    public Object getModel() {
        return egBillRegister;
    }

    @Override
    public void prepare()
    {
        super.prepare();
    }

    @Action(value = "/bill/billView-view")
    public String view() throws ApplicationException, ParseException
    {
        loadBillDetails();
        return Constants.VIEW;
    }

    private void loadBillDetails() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("-----------Start of loadBillDetails()-----------");
        Map<String, Object> temp = null;
        Map<String, Object> subLedgerTemp = null;
        if (egBillRegister.getEgBilldetailes() != null && egBillRegister.getEgBilldetailes().size() != 0)
        {
            final List<EgBilldetails> billDetList = persistenceService.findAllBy(
                    " from EgBilldetails where egBillregister.id=? ",
                    egBillRegister.getId());
            for (final EgBilldetails billDetail : billDetList)
            {
                final CChartOfAccounts coa = (CChartOfAccounts) persistenceService.find(" from CChartOfAccounts where id=?  ",
                        billDetail.getGlcodeid().longValue());
                temp = new HashMap<String, Object>();
                if (billDetail.getFunctionid() != null)
                {
                    final CFunction function = (CFunction) getPersistenceService().find("from CFunction where id=?",
                            billDetail.getFunctionid().longValue());
                    temp.put(Constants.FUNCTION, function.getName());
                } else
                    temp.put(Constants.FUNCTION, "");

                temp.put("glcode", coa.getGlcode());
                temp.put("accountHead", coa.getName());
                temp.put("debitAmount", billDetail.getDebitamount() == null ? 0 : billDetail.getDebitamount().longValue());
                temp.put("creditAmount", billDetail.getCreditamount() == null ? 0 : billDetail.getCreditamount().longValue());

                billDetailsList.add(temp);

                for (final EgBillPayeedetails payeeDetails : billDetail.getEgBillPaydetailes())
                {
                    final Accountdetailtype detailtype = (Accountdetailtype) persistenceService.find(
                            " from Accountdetailtype where id=?", payeeDetails.getAccountDetailTypeId());
                    subLedgerTemp = new HashMap<String, Object>();
                    try {
                        subLedgerTemp = getAccountDetails(detailtype, payeeDetails.getAccountDetailKeyId(), subLedgerTemp);
                    } catch (final ApplicationException e) {
                        final List<ValidationError> errors = new ArrayList<ValidationError>();
                        errors.add(new ValidationError("exp", e.getMessage()));
                        throw new ValidationException(errors);
                    }
                    subLedgerTemp.put(Constants.FUNCTION, temp.get(Constants.FUNCTION));
                    subLedgerTemp.put("glcode", coa.getGlcode());
                    if (payeeDetails.getDebitAmount() != null && payeeDetails.getDebitAmount().longValue() != 0)
                        subLedgerTemp.put("amount", payeeDetails.getDebitAmount().longValue());
                    else
                        subLedgerTemp.put("amount", payeeDetails.getCreditAmount().longValue());
                    subledgerList.add(subLedgerTemp);
                }
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("-----------End of loadBillDetails()-----------");
    }

    public Map<String, Object> getAccountDetails(final Accountdetailtype detailtype, final Integer detailkeyid,
            final Map<String, Object> tempMap) throws ApplicationException
            {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("-----------Start of getAccountDetails()-----------");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("-----------detailtype::" + detailtype.getId());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("-----------detailkeyid::" + detailkeyid);
        egovCommon.setPersistenceService(persistenceService);
        final EntityType entityType = egovCommon.getEntityType(detailtype, detailkeyid);
        tempMap.put(Constants.DETAILKEY, entityType.getName());
        tempMap.put(Constants.DETAILTYPE_NAME, detailtype.getName());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("-----------End of loadBillDetails()-----------");
        return tempMap;
            }

    public void setBillId(final long billId) {
        egBillRegister = (EgBillregister) persistenceService.find(" from EgBillregister where id = ?", billId);
    }

}
