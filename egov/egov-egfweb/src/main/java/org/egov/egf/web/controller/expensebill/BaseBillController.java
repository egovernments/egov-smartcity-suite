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
package org.egov.egf.web.controller.expensebill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.service.AccountdetailtypeService;
import org.egov.commons.service.ChartOfAccountDetailService;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.billsubtype.service.EgBillSubTypeService;
import org.egov.egf.expensebill.service.ExpenseBillService;
import org.egov.egf.web.controller.voucher.BaseVoucherController;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBillSubType;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 * @author venki
 *
 */

@Controller
public abstract class BaseBillController extends BaseVoucherController {

    @Autowired
    private EgBillSubTypeService egBillSubTypeService;

    @Autowired
    private AccountdetailtypeService accountdetailtypeService;

    @Autowired
    @Qualifier("chartOfAccountsService")
    private ChartOfAccountsService chartOfAccountsService;

    @Autowired
    private ExpenseBillService expenseBillService;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    
    public BaseBillController(final AppConfigValueService appConfigValuesService) {
        super(appConfigValuesService);
    }

    @Override
    protected void setDropDownValues(final Model model) {
        super.setDropDownValues(model);
        model.addAttribute("billNumberGenerationAuto", expenseBillService.isBillNumberGenerationAuto());
        model.addAttribute("billSubTypes", getBillSubTypes());
        model.addAttribute("subLedgerTypes", accountdetailtypeService.findAll());
    }

    public List<EgBillSubType> getBillSubTypes() {
        return egBillSubTypeService.getByExpenditureType(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
    }

    protected void validateBillNumber(final EgBillregister egBillregister, final BindingResult resultBinder) {
        if (!expenseBillService.isBillNumberGenerationAuto() && egBillregister.getId() == null)
            if (!expenseBillService.isBillNumUnique(egBillregister.getBillnumber()))
                resultBinder.reject("msg.expense.bill.duplicate.bill.number",
                        new String[] { egBillregister.getBillnumber() }, null);
    }

    protected void validateLedgerAndSubledger(final EgBillregister egBillregister, final BindingResult resultBinder) {
        BigDecimal totalDrAmt = BigDecimal.ZERO;
        BigDecimal totalCrAmt = BigDecimal.ZERO;
        for (final EgBilldetails details : egBillregister.getEgBilldetailes()) {
            if (details.getDebitamount() != null)
                totalDrAmt = totalDrAmt.add(details.getDebitamount());
            if (details.getCreditamount() != null)
                totalCrAmt = totalCrAmt.add(details.getCreditamount());
            if (details.getGlcodeid() == null)
                resultBinder.reject("msg.expense.bill.accdetail.accmissing", new String[] {}, null);

            if (details.getDebitamount() != null && details.getCreditamount() != null
                    && details.getDebitamount().equals(BigDecimal.ZERO) && details.getCreditamount().equals(BigDecimal.ZERO)
                    && details.getGlcodeid() != null)
                resultBinder.reject("msg.expense.bill.accdetail.amountzero",
                        new String[] { details.getChartOfAccounts().getGlcode() }, null);

            if (details.getDebitamount() != null && details.getCreditamount() != null
                    && details.getDebitamount().compareTo(BigDecimal.ZERO) == 1
                    && details.getCreditamount().compareTo(BigDecimal.ZERO) == 1)
                resultBinder.reject("msg.expense.bill.accdetail.amount",
                        new String[] { details.getChartOfAccounts().getGlcode() }, null);
        }
        if (totalDrAmt.compareTo(totalCrAmt) != 0)
            resultBinder.reject("msg.expense.bill.accdetail.drcrmatch", new String[] {}, null);
        validateSubledgerDetails(egBillregister, resultBinder);
    }

    protected void validateSubledgerDetails(final EgBillregister egBillregister, final BindingResult resultBinder) {
        Boolean check;
        BigDecimal detailAmt;
        BigDecimal payeeDetailAmt;
        for (final EgBilldetails details : egBillregister.getEgBilldetailes()) {

            detailAmt = BigDecimal.ZERO;
            payeeDetailAmt = BigDecimal.ZERO;

            if (details.getDebitamount() != null && details.getDebitamount().compareTo(BigDecimal.ZERO) == 1)
                detailAmt = details.getDebitamount();
            else if (details.getCreditamount() != null &&
                    details.getCreditamount().compareTo(BigDecimal.ZERO) == 1)
                detailAmt = details.getCreditamount();

            for (final EgBillPayeedetails payeeDetails : details.getEgBillPaydetailes()) {

                if (payeeDetails.getDebitAmount() != null && payeeDetails.getCreditAmount() != null
                        && payeeDetails.getDebitAmount().equals(BigDecimal.ZERO)
                        && payeeDetails.getCreditAmount().equals(BigDecimal.ZERO))
                    resultBinder.reject("msg.expense.bill.subledger.amountzero",
                            new String[] { details.getChartOfAccounts().getGlcode() }, null);

                if (payeeDetails.getDebitAmount() != null && payeeDetails.getCreditAmount() != null
                        && payeeDetails.getDebitAmount().compareTo(BigDecimal.ZERO) == 1
                        && payeeDetails.getCreditAmount().compareTo(BigDecimal.ZERO) == 1)
                    resultBinder.reject("msg.expense.bill.subledger.amount",
                            new String[] { details.getChartOfAccounts().getGlcode() }, null);

                if (payeeDetails.getDebitAmount() != null && payeeDetails.getDebitAmount().compareTo(BigDecimal.ZERO) == 1)
                    payeeDetailAmt = payeeDetailAmt.add(payeeDetails.getDebitAmount());
                else if (payeeDetails.getCreditAmount() != null && payeeDetails.getCreditAmount().compareTo(BigDecimal.ZERO) == 1)
                    payeeDetailAmt = payeeDetailAmt.add(payeeDetails.getCreditAmount());

                check = false;
                for (final CChartOfAccountDetail coaDetails : details.getChartOfAccounts().getChartOfAccountDetails())
                    if (payeeDetails.getAccountDetailTypeId() == coaDetails.getDetailTypeId().getId())
                        check = true;
                if (!check)
                    resultBinder.reject("msg.expense.bill.subledger.mismatch",
                            new String[] { details.getChartOfAccounts().getGlcode() }, null);

            }

            if (detailAmt.compareTo(payeeDetailAmt) != 0 && !details.getEgBillPaydetailes().isEmpty())
                resultBinder.reject("msg.expense.bill.subledger.amtnotmatchinng",
                        new String[] { details.getChartOfAccounts().getGlcode() }, null);
        }
    }

    @SuppressWarnings("unchecked")
    protected void populateBillDetails(final EgBillregister egBillregister) {
        egBillregister.getEgBilldetailes().clear();
        egBillregister.getEgBilldetailes().addAll(egBillregister.getBillDetails());
        for (final EgBilldetails details : egBillregister.getEgBilldetailes()) {
            if (egBillregister.getEgBillregistermis().getFunction() != null)
                details.setFunctionid(BigDecimal.valueOf(egBillregister.getEgBillregistermis().getFunction().getId()));
            details.setEgBillregister(egBillregister);
            details.setLastupdatedtime(new Date());
            details.setChartOfAccounts(chartOfAccountsService.findById(details.getGlcodeid().longValue(), false));
        }
        if (!egBillregister.getBillPayeedetails().isEmpty())
            populateBillPayeeDetails(egBillregister);
    }

    protected void populateBillPayeeDetails(final EgBillregister egBillregister) {
        EgBillPayeedetails payeeDetail;
        for (final EgBilldetails details : egBillregister.getEgBilldetailes())
            for (final EgBillPayeedetails payeeDetails : egBillregister.getBillPayeedetails())
                if (details.getGlcodeid().equals(payeeDetails.getEgBilldetailsId().getGlcodeid())) {
                    payeeDetail = new EgBillPayeedetails();
                    payeeDetail.setEgBilldetailsId(details);
                    payeeDetail.setAccountDetailTypeId(payeeDetails.getAccountDetailTypeId());
                    payeeDetail.setAccountDetailKeyId(payeeDetails.getAccountDetailKeyId());
                    payeeDetail.setDebitAmount(payeeDetails.getDebitAmount());
                    payeeDetail.setCreditAmount(payeeDetails.getCreditAmount());
                    payeeDetail.setLastUpdatedTime(new Date());
                    details.getEgBillPaydetailes().add(payeeDetail);
                }
    }
    
    protected void prepareBillDetailsForView(final EgBillregister egBillregister) {
        for (final EgBilldetails details : egBillregister.getBillDetails()) {
            details.setChartOfAccounts(chartOfAccountsService.findById(details.getGlcodeid().longValue(), false));
            egBillregister.getBillPayeedetails().addAll(details.getEgBillPaydetailes());
        }
        for (final EgBillPayeedetails payeeDetails : egBillregister.getBillPayeedetails()) {
            payeeDetails.getEgBilldetailsId().setChartOfAccounts(
                    chartOfAccountsService.findById(payeeDetails.getEgBilldetailsId().getGlcodeid().longValue(), false));
            final Accountdetailtype detailType = accountdetailtypeService.findOne(payeeDetails.getAccountDetailTypeId());
            EntityType entity = null;
            String dataType = "";
            try {
                final String table = detailType.getFullQualifiedName();
                final Class<?> service = Class.forName(table);
                final String tableName = service.getSimpleName();
                final java.lang.reflect.Method method = service.getMethod("getId");

                dataType = method.getReturnType().getSimpleName();
                if ("Long".equals(dataType))
                    entity = (EntityType) persistenceService.find("from " + tableName + " where id=? order by name",
                            payeeDetails.getAccountDetailKeyId()
                                    .longValue());
                else
                    entity = (EntityType) persistenceService.find("from " + tableName + " where id=? order by name",
                            payeeDetails.getAccountDetailKeyId());
            } catch (final Exception e) {
                throw new ApplicationRuntimeException(e.getMessage());
            }
            payeeDetails.setDetailTypeName(detailType.getName());
            payeeDetails.setDetailKeyName(entity.getName());

        }
    }

}