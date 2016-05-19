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
package org.egov.egf.web.actions.report;


import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.web.actions.voucher.VoucherReport;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.NumberToWord;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBillregistermis;
import org.egov.services.budget.BudgetAppropriationService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.hibernate.FlushMode;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Results(value = {
		
		@Result(name = ExpenseJournalVoucherPrintAction.PRINT ,location="expenseJournalVoucherPrint-print.jsp"),
        @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=ExpenseJournalVoucherReport.pdf" }),
                @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                        "application/xls", "contentDisposition", "no-cache;filename=ExpenseJournalVoucherReport.xls" }),
                        @Result(name = "HTML", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                        "text/html" })
})

@ParentPackage("egov")
public class ExpenseJournalVoucherPrintAction extends BaseFormAction {
    String jasperpath = "/reports/templates/expenseJournalVoucherReport.jasper";
    private static final long serialVersionUID = 1L;
    static final String PRINT = "print";
    private CVoucherHeader voucher = new CVoucherHeader();
    List<Object> voucherReportList = new ArrayList<Object>();
    InputStream inputStream;
    ReportHelper reportHelper;
    Long id;
    EgBillregistermis billRegistermis;
    List<EgBillPayeedetails> billPayeeDetails = new ArrayList<EgBillPayeedetails>();
    private static final String ACCDETAILTYPEQUERY = " from Accountdetailtype where id=?";
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private EgovCommon egovCommon;

    public Long getId() {
        return id;
    }

    public void setBudgetAppropriationService(final BudgetAppropriationService budgetAppropriationService) {
    }

    public void setReportHelper(final ReportHelper helper) {
        reportHelper = helper;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<Object> getVoucherReportList() {
        return voucherReportList;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public String execute() {
        return print();
    }

    @Action(value = "/report/expenseJournalVoucherPrint-ajaxPrint")
    public String ajaxPrint() {
        return exportHtml();
    }

    @Override
    public Object getModel() {
        return voucher;
    }

    @Action(value = "/report/expenseJournalVoucherPrint-print")
    public String print() {
        return PRINT;
    }

    private void populateVoucher() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        if (!StringUtils.isBlank(parameters.get("id")[0])) {
            final Long id = Long.valueOf(parameters.get("id")[0]);
            final CVoucherHeader voucherHeader = (CVoucherHeader) persistenceService.getSession().get(CVoucherHeader.class, id);
            if (voucherHeader != null) {
                voucher = voucherHeader;
                billRegistermis = (EgBillregistermis) persistenceService.find("from EgBillregistermis where voucherHeader.id=?",
                        voucherHeader.getId());
                if (billRegistermis != null)
                    billPayeeDetails = persistenceService.findAllBy(
                            "from EgBillPayeedetails where egBilldetailsId.egBillregister.id=?", billRegistermis
                            .getEgBillregister().getId());
                generateVoucherReportList();
            }
        }
    }

    private void generateVoucherReportList() {
        if (voucher != null) {
            for (final CGeneralLedger vd : voucher.getGeneralledger())
                if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(vd.getCreditAmount()))==0) {
                    final VoucherReport voucherReport = new VoucherReport(persistenceService, Integer.valueOf(voucher.getId()
                            .toString()), vd, egovCommon);
                    if (billRegistermis != null)
                        voucherReport.setDepartment(billRegistermis.getEgDepartment());
                    voucherReportList.add(voucherReport);
                }
            for (final CGeneralLedger vd : voucher.getGeneralledger())
                if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(vd.getDebitAmount()))==0) {
                    final VoucherReport voucherReport = new VoucherReport(persistenceService, Integer.valueOf(voucher.getId()
                            .toString()), vd, egovCommon);
                    if (billRegistermis != null)
                        voucherReport.setDepartment(billRegistermis.getEgDepartment());
                    voucherReportList.add(voucherReport);
                }
        }
    }

    private String getUlbName() {
        final SQLQuery query = persistenceService.getSession().createSQLQuery("select name from companydetail");
        final List<String> result = query.list();
        if (result != null)
            return result.get(0);
        return "";
    }

    public String exportPdf() throws JRException, IOException {
        populateVoucher();
        inputStream = reportHelper.exportPdf(inputStream, jasperpath, getParamMap(), voucherReportList);
        return "PDF";
    }

    public String exportHtml() {
        populateVoucher();
        inputStream = reportHelper.exportHtml(inputStream, jasperpath, getParamMap(), voucherReportList, "px");
        return "HTML";
    }

    public String exportXls() throws JRException, IOException {
        populateVoucher();
        inputStream = reportHelper.exportXls(inputStream, jasperpath, getParamMap(), voucherReportList);
        return "XLS";
    }

    protected Map<String, Object> getParamMap() {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("voucherNumber", getVoucherNumber());
        paramMap.put("voucherDate", getVoucherDate());
        paramMap.put("voucherDescription", getVoucherDescription());
        if (voucher != null && voucher.getState() != null)
            loadInboxHistoryData(voucher.getStateHistory(), paramMap);
        if (billRegistermis != null) {
            paramMap.put("billDate", Constants.DDMMYYYYFORMAT2.format(billRegistermis.getEgBillregister().getBilldate()));
            paramMap.put("partyBillNumber", billRegistermis.getPartyBillNumber());
            paramMap.put("serviceOrder", billRegistermis.getNarration());
            paramMap.put("partyName", billRegistermis.getPayto());
            paramMap.put("billNumber", billRegistermis.getEgBillregister().getBillnumber());
            final BigDecimal billamount = billRegistermis.getEgBillregister().getBillamount();
            final String amountInFigures = billamount == null ? " " : billamount.setScale(2).toPlainString();
            final String amountInWords = billamount == null ? " " : NumberToWord.convertToWord(billamount.toPlainString());
            paramMap.put("certificate", getText("ejv.report.text", new String[] { amountInFigures, amountInWords }));
        }
        paramMap.put("ulbName", ReportUtil.getCityName());

        return paramMap;
    }

    public Map<String, Object> getAccountDetails(final Integer detailtypeid, final Integer detailkeyid,
            final Map<String, Object> tempMap) throws ApplicationException {
        final Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY, detailtypeid);
        tempMap.put("detailtype", detailtype.getName());
        tempMap.put("detailtypeid", detailtype.getId());
        tempMap.put("detailkeyid", detailkeyid);
        egovCommon.setPersistenceService(persistenceService);
        final EntityType entityType = egovCommon.getEntityType(detailtype, detailkeyid);
        tempMap.put(Constants.DETAILKEY, entityType.getName());
        tempMap.put(Constants.DETAILCODE, entityType.getCode());
        return tempMap;
    }

    private String getVoucherNumber() {
        return voucher == null || voucher.getVoucherNumber() == null ? "" : voucher.getVoucherNumber();
    }

    private String getVoucherDescription() {
        return voucher == null || voucher.getDescription() == null ? "" : voucher.getDescription();
    }

    private String getVoucherDate() {
        return voucher == null || voucher.getVoucherDate() == null ? "" : DateUtils.getDefaultFormattedDate(voucher
                .getVoucherDate());
    }

    void loadInboxHistoryData(final List<StateHistory> stateHistory, final Map<String, Object> paramMap) throws ApplicationRuntimeException {
        final List<String> history = new ArrayList<String>();
        final List<String> workFlowDate = new ArrayList<String>();
        for (final StateHistory historyState : stateHistory)
            if (!"NEW".equalsIgnoreCase(historyState.getValue())) {
                history.add(historyState.getSenderName());
                workFlowDate.add(Constants.DDMMYYYYFORMAT2.format(historyState.getLastModifiedDate()));
            }
        for (int i = 0; i < history.size(); i++) {
            paramMap.put("workFlow_" + i, history.get(i));
            paramMap.put("workFlowDate_" + i, workFlowDate.get(i));
        }
    }

}