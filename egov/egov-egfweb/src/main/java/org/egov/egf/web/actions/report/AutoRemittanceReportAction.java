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

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.model.AutoRemittanceBeanReport;
import org.egov.eis.entity.DrawingOfficer;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.recoveries.Recovery;
import org.egov.services.deduction.RemitRecoveryService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Results(value = {
        @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=AutoRemittanceReport.pdf" }),
        @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/xls", "contentDisposition", "no-cache;filename=AutoRemittanceReport.xls" }),
        @Result(name = "summary-PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream",
                "contentType", "application/pdf", "contentDisposition", "no-cache;filename=AutoRemittanceCOCLevel.pdf" }),
        @Result(name = "summary-XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream",
                "contentType", "application/xls", "contentDisposition", "no-cache;filename=AutoRemittanceReportCOCLevel.xls" })
})
@ParentPackage("egov")
public class AutoRemittanceReportAction extends BaseFormAction {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    /**
     *
     */
    private static final long serialVersionUID = -6786924278481362059L;
    String deptLevelJasperpath = "AutoRemittanceReport";
    String cocLevelJasperpath = "AutoRemittanceCOCLevelReport";
    private InputStream inputStream;
    private ReportService reportService;
    private Date paymentVoucherFromDate;
    private Date paymentVoucherToDate;
    private Date rtgsAssignedFromDate;
    private Date rtgsAssignedToDate;
    private Recovery recovery = new Recovery();
    private Fund fund = new Fund();
    private Department department = new Department();
    private final List<EntityType> entitiesList = new ArrayList<EntityType>();
    private RemitRecoveryService remitRecoveryService;
    private Bank bank;
    private Bankbranch bankbranch;
    private Bankaccount bankaccount;
    private String instrumentNumber;
    private String level;
    private DrawingOfficer drawingOfficer;
    private String supplierCode;
    private String contractorCode;
    private String accountNumber;
    private BigDecimal remittedAmountTotal = new BigDecimal("0");
    private List<AutoRemittanceBeanReport> autoRemittance = new ArrayList<AutoRemittanceBeanReport>();
    private final Map<String, Object> map = new HashMap<String, Object>();
    Map<AutoRemittanceBeanReport, List<AutoRemittanceBeanReport>> autoremittanceMap = new HashMap<AutoRemittanceBeanReport, List<AutoRemittanceBeanReport>>();
    private List<AutoRemittanceCOCLevelBeanReport> coaAbstract = new ArrayList<AutoRemittanceCOCLevelBeanReport>(0);
    private List<AutoRemittanceCOCLevelBeanReport> remittanceList = new ArrayList<AutoRemittanceCOCLevelBeanReport>(0);

    private static Logger LOGGER = Logger.getLogger(AutoRemittanceReportAction.class);

    public void setFinancialYearDAO(final FinancialYearHibernateDAO financialYearDAO) {
    }

    public void setRemitRecoveryService(final RemitRecoveryService remitRecoveryService) {
        this.remitRecoveryService = remitRecoveryService;
    }

    @Override
    public String execute() throws Exception {
        return "reportForm";
    }

    @Action(value = "/report/autoRemittanceReport-newform")
    public String newform() throws Exception {
        return "reportForm";
    }

    @Override
    public void prepare() {
        // persistenceService.getSession().setDefaultReadOnly(true);
        // persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        super.prepare();
        addDropdownData("departmentList", persistenceService.findAllBy("from Department order by deptName"));
        addDropdownData("fundList",
                persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
        addDropdownData("recoveryList",
                persistenceService.findAllBy(" from Recovery where isactive=true order by chartofaccounts.glcode"));
        addDropdownData("bankList", Collections.EMPTY_LIST);
        addDropdownData("bankBranchList", Collections.EMPTY_LIST);
        addDropdownData("bankAccountList", Collections.EMPTY_LIST);
        addDropdownData("accNumList", Collections.EMPTY_LIST);
        addDropdownData("drawingList", Collections.EMPTY_LIST);

    }

    @Action(value = "/report/autoRemittanceReport-ajaxLoadData")
    public String ajaxLoadData() {

        populateData();
        boolean addList = false;
        List<AutoRemittanceBeanReport> autoremitEntry = new ArrayList<AutoRemittanceBeanReport>();
        new AutoRemittanceBeanReport();
        if (level.equals("atcoc"))
        {
            populateCOCLevelSummaryData();
            for (final AutoRemittanceBeanReport autoRemit : autoRemittance) {
                AutoRemittanceBeanReport autoremitKey = new AutoRemittanceBeanReport();
                if (autoremittanceMap.isEmpty()) {
                    autoremitEntry = new ArrayList<AutoRemittanceBeanReport>();
                    autoremitKey.setRemittanceCOA(autoRemit.getRemittanceCOA());
                    autoremitKey.setDepartment(autoRemit.getDepartment());
                    autoremitKey.setDrawingOfficer(autoRemit.getDrawingOfficer());
                    autoremitKey.setBankbranchAccount(autoRemit.getBankbranchAccount());
                    autoremitEntry.add(autoRemit);
                    autoremitKey.setRemittedAmountSubtotal(autoRemit.getRemittedAmount());
                    autoremittanceMap.put(autoremitKey, autoremitEntry);
                } else {
                    final Set<AutoRemittanceBeanReport> autoRemitKeySet = autoremittanceMap.keySet();
                    final java.util.Iterator keySetitr = autoRemitKeySet.iterator();
                    while (keySetitr.hasNext()) {
                        final AutoRemittanceBeanReport autormt = (AutoRemittanceBeanReport) keySetitr.next();
                        addList = false;
                        if (autormt.getRemittanceCOA().equals(autoRemit.getRemittanceCOA()) &&
                                autormt.getDepartment().equals(autoRemit.getDepartment()) &&
                                autormt.getDrawingOfficer().equals(autoRemit.getDrawingOfficer()) &&
                                autormt.getBankbranchAccount().equals(autoRemit.getBankbranchAccount())) {
                            autormt.setRemittedAmountSubtotal(autormt.getRemittedAmountSubtotal().add(
                                    autoRemit.getRemittedAmount()));
                            autoremitKey = autormt;
                            addList = false;
                            break;
                        } else
                            addList = true;
                    }
                    if (!addList) {
                        autoremittanceMap.get(autoremitKey).add(autoRemit);
                    }
                    else {
                        autoremitEntry = new ArrayList<AutoRemittanceBeanReport>();
                        autoremitKey.setRemittanceCOA(autoRemit.getRemittanceCOA());
                        autoremitKey.setDrawingOfficer(autoRemit.getDrawingOfficer());
                        autoremitKey.setDepartment(autoRemit.getDepartment());
                        autoremitKey.setBankbranchAccount(autoRemit.getBankbranchAccount());
                        autoremitKey.setRemittedAmountSubtotal(autoRemit.getRemittedAmount());
                        autoremitEntry.add(autoRemit);
                        autoremittanceMap.put(autoremitKey, autoremitEntry);
                    }
                }
                remittedAmountTotal = remittedAmountTotal.add(autoRemit.getRemittedAmount());
            }
        } else
            for (final AutoRemittanceBeanReport autoRemit : autoRemittance) {
                AutoRemittanceBeanReport autoremitKey = new AutoRemittanceBeanReport();
                if (autoremittanceMap.isEmpty()) {
                    autoremitEntry = new ArrayList<AutoRemittanceBeanReport>();
                    autoremitKey.setRemittanceCOA(autoRemit.getRemittanceCOA());
                    autoremitKey.setFundName(autoRemit.getFundName());
                    autoremitKey.setBankbranchAccount(autoRemit.getBankbranchAccount());
                    autoremitEntry.add(autoRemit);
                    autoremitKey.setRemittedAmountSubtotal(autoRemit.getRemittedAmount());
                    autoremittanceMap.put(autoremitKey, autoremitEntry);
                } else {
                    final Set<AutoRemittanceBeanReport> autoRemitKeySet = autoremittanceMap.keySet();
                    final java.util.Iterator keySetitr = autoRemitKeySet.iterator();
                    while (keySetitr.hasNext()) {
                        final AutoRemittanceBeanReport autormt = (AutoRemittanceBeanReport) keySetitr.next();
                        addList = false;
                        if (autormt.getRemittanceCOA().equals(autoRemit.getRemittanceCOA()) &&
                                autormt.getFundName().equals(autoRemit.getFundName()) &&
                                autormt.getBankbranchAccount().equals(autoRemit.getBankbranchAccount())) {
                            autormt.setRemittedAmountSubtotal(autormt.getRemittedAmountSubtotal().add(
                                    autoRemit.getRemittedAmount()));
                            autoremitKey = autormt;
                            addList = false;
                            break;
                        } else
                            addList = true;
                    }
                    if (!addList) {
                        autoremittanceMap.get(autoremitKey).add(autoRemit);
                    }
                    else {
                        autoremitEntry = new ArrayList<AutoRemittanceBeanReport>();
                        autoremitKey.setRemittanceCOA(autoRemit.getRemittanceCOA());
                        autoremitKey.setFundName(autoRemit.getFundName());
                        autoremitKey.setBankbranchAccount(autoRemit.getBankbranchAccount());
                        autoremitKey.setRemittedAmountSubtotal(autoRemit.getRemittedAmount());
                        autoremitEntry.add(autoRemit);
                        autoremittanceMap.put(autoremitKey, autoremitEntry);
                    }
                }
                remittedAmountTotal = remittedAmountTotal.add(autoRemit.getRemittedAmount());
            }
        getSession().put("autoremittanceMap", autoremittanceMap);
        return "results";
    }

    @Action(value = "/report/autoRemittanceReport-exportXls")
    public String exportXls() throws JRException, IOException {
        populateData();
        if (level.equals("atcoc"))
        {
            final StringBuffer finyearQuery = new StringBuffer();
            final Date currentDate = new Date();
            finyearQuery.append("from CFinancialYear where  startingDate <= '")
                    .append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("' AND endingDate >='")
                    .append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("'");
            final CFinancialYear financialyear = (CFinancialYear) persistenceService.find(finyearQuery.toString());
            if (null == paymentVoucherFromDate)
                paymentVoucherFromDate = financialyear.getStartingDate();
            if (null == paymentVoucherToDate)
                paymentVoucherToDate = financialyear.getEndingDate();
            map.put("autoremittanceList", autoRemittance);
            populateCOCLevelSummaryData();
            final ReportRequest reportInput = new ReportRequest(cocLevelJasperpath, map, getParamMap());
            reportInput.setReportFormat(ReportFormat.XLS);
            final ReportOutput reportOutput = reportService.createReport(reportInput);
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
            return "summary-XLS";
        }
        else
        {
            final ReportRequest reportInput = new ReportRequest(deptLevelJasperpath, autoRemittance, getParamMap());
            reportInput.setReportFormat(ReportFormat.XLS);
            final ReportOutput reportOutput = reportService.createReport(reportInput);
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
            return "XLS";
        }
    }

    @Action(value = "/report/autoRemittanceReport-exportPdf")
    public String exportPdf()
    {
        populateData();
        if (level.equals("atcoc"))
        {
            final StringBuffer finyearQuery = new StringBuffer();
            final Date currentDate = new Date();
            finyearQuery.append("from CFinancialYear where  startingDate <= '")
                    .append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("' AND endingDate >='")
                    .append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("'");
            final CFinancialYear financialyear = (CFinancialYear) persistenceService.find(finyearQuery.toString());
            if (null == paymentVoucherFromDate)
                paymentVoucherFromDate = financialyear.getStartingDate();
            if (null == paymentVoucherToDate)
                paymentVoucherToDate = financialyear.getEndingDate();
            map.put("autoremittanceList", autoRemittance);
            populateCOCLevelSummaryData();
            final ReportRequest reportInput = new ReportRequest(cocLevelJasperpath, map, getParamMap());
            final ReportOutput reportOutput = reportService.createReport(reportInput);
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
            return "summary-PDF";
        }
        else {
            final ReportRequest reportInput = new ReportRequest(deptLevelJasperpath, autoRemittance, getParamMap());
            final ReportOutput reportOutput = reportService.createReport(reportInput);
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
            return "PDF";
        }

    }

    Map<String, Object> getParamMap() {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        final StringBuffer header = new StringBuffer("");
        if (level.equals("atcoc"))
        {
            header.append("Summary of remittance for the date range ");
            header.append(Constants.DDMMYYYYFORMAT2.format(paymentVoucherFromDate) + "  to  "
                    + Constants.DDMMYYYYFORMAT2.format(paymentVoucherToDate));
            final StringBuffer detailheader = new StringBuffer("Auto remittance payment report for ");
            recovery = (Recovery) persistenceService.find("from Recovery  where id =?", recovery.getId());
            detailheader.append(recovery.getType() + " - " + recovery.getRecoveryName());
            paramMap.put("detailheader", detailheader.toString());
        }
        else
        {
            header.append(" Auto remittance payment report for ");
            department = (Department) persistenceService.find("from Department where id=?", department.getId());
            header.append(department.getName() + " department ");
        }

        paramMap.put("header", header.toString());
        if (null != recovery && null != recovery.getId() && recovery.getId() != -1)
        {
            recovery = (Recovery) persistenceService.find("from Recovery  where id =?", recovery.getId());
            paramMap.put("remittanceCOA", recovery.getType());
        }
        if (null != paymentVoucherFromDate)
        {
            final String formatedDate = Constants.DDMMYYYYFORMAT2.format(paymentVoucherFromDate);
            paramMap.put("payVoucherFromDate", formatedDate);
        }
        if (null != paymentVoucherToDate)
        {
            final String formatedDate = Constants.DDMMYYYYFORMAT2.format(paymentVoucherToDate);
            paramMap.put("payVoucherToDate", formatedDate);
        }
        if (null != fund && null != fund.getId() && fund.getId() != -1)
        {
            fund = (Fund) persistenceService.find("from Fund where id=?", fund.getId());
            paramMap.put("fund", fund.getName());
        }
        if (null != drawingOfficer && null != drawingOfficer.getId() && drawingOfficer.getId() != -1)
        {
            drawingOfficer = (DrawingOfficer) persistenceService.find("from DrawingOfficer where id=?", drawingOfficer.getId());
            paramMap.put("drawingOfficer", drawingOfficer.getName());
        }
        if (null != rtgsAssignedFromDate)
        {
            final String formatedDate = Constants.DDMMYYYYFORMAT2.format(rtgsAssignedFromDate);
            paramMap.put("rtgsFromDate", formatedDate);
        }
        if (null != rtgsAssignedToDate)
        {
            final String formatedDate = Constants.DDMMYYYYFORMAT2.format(rtgsAssignedToDate);
            paramMap.put("rtgsToDate", formatedDate);
        }
        if (null != instrumentNumber)
            paramMap.put("rtgsNum", instrumentNumber);
        if (null != bank && null != bank.getId() && bank.getId() != -1)
        {
            bank = (Bank) persistenceService.find("from Bank where id = ?", bank.getId());
            paramMap.put("bank", bank.getName());
        }
        if (null != supplierCode && !supplierCode.isEmpty())
            paramMap.put("supplierName", supplierCode);
        if (null != contractorCode && !contractorCode.isEmpty())
            paramMap.put("contractorName", contractorCode);
        if (null != bankbranch && null != bankbranch.getId() && bankbranch.getId() != -1)
        {
            bankbranch = (Bankbranch) persistenceService.find("from Bankbranch where id =?", bankbranch.getId());
            paramMap.put("bankBranch", bankbranch.getBranchname());

        }
        if (null != bankaccount && null != bankaccount.getId() && bankaccount.getId() != -1)
        {
            bankaccount = (Bankaccount) persistenceService.find("from Bankaccount where id =?", bankbranch.getId());
            paramMap.put("bankAccountNum", bankaccount.getAccountnumber());
        }
        final Date currentDate = new Date();
        final String reportRunDate = Constants.DDMMYYYYFORMAT2.format(currentDate);
        paramMap.put("reportRunDate", reportRunDate);
        return paramMap;
    }

    private void populateData()
    {
        final StringBuffer query = new StringBuffer("");
        final Date currentDate = new Date();
        final StringBuffer finyearQuery = new StringBuffer();

        finyearQuery.append("from CFinancialYear where  startingDate <= '").append(Constants.DDMMYYYYFORMAT1.format(currentDate))
                .append("' AND endingDate >='").append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("'");
        final CFinancialYear financialyear = (CFinancialYear) persistenceService.find(finyearQuery.toString());

        if (level.equals("atcoc"))
            query.append("SELECT CONCAT(CONCAT(coa.GLCODE ,' - ') ,coa.NAME) AS remittanceCOA,"
                    +
                    "      dept.DEPT_NAME  AS department,CONCAT(	CONCAT(DO.NAME,'/') , DO.TAN) AS drawingOfficer , "
                    +
                    " CONCAT(CONCAT( CONCAT(CONCAT(bank.NAME, '  '),bnkbranch.BRANCHNAME), ' - '), bnkacc.ACCOUNTNUMBER) AS bankbranchAccount,"
                    +
                    "  vh.VOUCHERNUMBER AS remittancePaymentNo, CONCAT(CONCAT(ih.INSTRUMENTNUMBER ,'/'),ih.INSTRUMENTDATE ) rtgsNoDate,"
                    +
                    " ih.INSTRUMENTAMOUNT AS rtgsamount, remdt.ID AS remittanceDTId ,vh.id as paymentVoucherId "
                    +
                    " FROM EG_REMITTANCE rem, EG_REMITTANCE_DETAIL remdt,EG_REMITTANCE_GLDTL remgltl,"
                    +
                    " EGF_INSTRUMENTHEADER ih,EGF_INSTRUMENTVOUCHER iv, VOUCHERHEADER vh,TDS TDS,PAYMENTHEADER ph,BANKACCOUNT bnkacc,"
                    +
                    " GENERALLEDGER gl, GENERALLEDGERDETAIL gld, chartofaccounts coa, fund fund,bank bank,bankbranch bnkbranch,EG_DEPARTMENT dept,EG_DRAWINGOFFICER DO "
                    +
                    " WHERE rem.id = remdt.REMITTANCEID AND remdt.REMITTANCEGLDTLID =remgltl.ID"
                    +
                    " AND   rem.paymentvhid = iv.voucherheaderid AND  iv.instrumentheaderid = ih.ID AND  iv.voucherheaderid= vh.id "
                    +
                    " AND rem.tdsid = TDS.id AND 	 fund.id= vh.fundid "
                    +
                    " AND TDS.REMITTANCE_MODE='A' AND vh.status=0 "
                    +
                    " AND ph.VOUCHERHEADERID = vh.id  "
                    +
                    " AND ph.BANKACCOUNTNUMBERID = bnkacc.ID AND gl.VOUCHERHEADERID= vh.id AND gld.GENERALLEDGERID=gl.id AND  dept.ID_DEPT = vh.departmentid "
                    +
                    " AND DO.ID =ph.DRAWINGOFFICER_ID	 AND ph.DRAWINGOFFICER_ID IS NOT NULL AND rem.paymentvhid IS  NOT  NULL " +
                    " AND ih.ID_STATUS= (SELECT  id  FROM EGW_STATUS WHERE moduletype='Instrument' AND code='New') " +
                    " AND bnkacc.BRANCHID=bnkbranch.ID  AND bank.id =bnkbranch.BANKID   AND coa.id= tds.GLCODEID ");
        else
            query.append("SELECT CONCAT(CONCAT(coa.GLCODE ,' - ') ,coa.NAME) AS remittanceCOA,"
                    +
                    " fund.NAME AS fundName, CONCAT(CONCAT( CONCAT(CONCAT(bank.NAME, '  '),bnkbranch.BRANCHNAME), ' - '), bnkacc.ACCOUNTNUMBER) AS bankbranchAccount,"
                    +
                    "  vh.VOUCHERNUMBER AS remittancePaymentNo, CONCAT(CONCAT(ih.INSTRUMENTNUMBER ,'/'),ih.INSTRUMENTDATE ) rtgsNoDate,"
                    +
                    " ih.INSTRUMENTAMOUNT AS rtgsamount, remdt.ID AS remittanceDTId ,vh.id as paymentVoucherId "
                    +
                    " FROM EG_REMITTANCE rem, EG_REMITTANCE_DETAIL remdt,EG_REMITTANCE_GLDTL remgltl,"
                    +
                    " EGF_INSTRUMENTHEADER ih,EGF_INSTRUMENTVOUCHER iv, VOUCHERHEADER vh,TDS TDS,PAYMENTHEADER ph,BANKACCOUNT bnkacc,"
                    +
                    " GENERALLEDGER gl, GENERALLEDGERDETAIL gld, chartofaccounts coa, fund fund,bank bank,bankbranch bnkbranch "
                    +
                    " WHERE rem.id = remdt.REMITTANCEID AND remdt.REMITTANCEGLDTLID =remgltl.ID"
                    +
                    " AND   rem.paymentvhid = iv.voucherheaderid AND  iv.instrumentheaderid = ih.ID AND  iv.voucherheaderid= vh.id "
                    +
                    " AND rem.tdsid = TDS.id AND 	 fund.id= vh.fundid "
                    +
                    " AND TDS.REMITTANCE_MODE='A' AND vh.status=0 "
                    +
                    " AND ph.VOUCHERHEADERID = vh.id  "
                    +
                    " AND ph.BANKACCOUNTNUMBERID = bnkacc.ID AND gl.VOUCHERHEADERID= vh.id AND gld.GENERALLEDGERID=gl.id AND  rem.paymentvhid IS  NOT  NULL "
                    +
                    " AND ih.ID_STATUS= (SELECT  id  FROM EGW_STATUS WHERE moduletype='Instrument' AND code='New') " +
                    " AND bnkacc.BRANCHID=bnkbranch.ID  AND bank.id =bnkbranch.BANKID   AND coa.id= tds.GLCODEID ");
        if (null != department && null != department.getId() && department.getId() != -1)
            query.append(" AND vh.DEPARTMENTID = " + department.getId());
        if (null != recovery && null != recovery.getId() && recovery.getId() != -1)
            query.append(" AND  TDS.id = " + recovery.getId());

        if (level.equals("atcoc"))
        {
            if (null != paymentVoucherFromDate)
                query.append(" AND vh.voucherdate >= '" + Constants.DDMMYYYYFORMAT1.format(paymentVoucherFromDate) + "'");
            else
                query.append(" AND vh.voucherdate >= '" + Constants.DDMMYYYYFORMAT1.format(financialyear.getStartingDate()) + "'");
            if (null != paymentVoucherToDate)
                query.append(" AND vh.voucherdate <= '" + Constants.DDMMYYYYFORMAT1.format(paymentVoucherToDate) + "'");
            else
                query.append(" AND vh.voucherdate <= '" + Constants.DDMMYYYYFORMAT1.format(financialyear.getEndingDate()) + "'");
        }
        else
        {
            if (null != paymentVoucherFromDate)
                query.append(" AND vh.voucherdate >= '" + Constants.DDMMYYYYFORMAT1.format(paymentVoucherFromDate) + "'");
            if (null != paymentVoucherToDate)
                query.append(" AND vh.voucherdate <= '" + Constants.DDMMYYYYFORMAT1.format(paymentVoucherToDate) + "'");
        }
        if (null != fund && null != fund.getId() && fund.getId() != -1)
            query.append(" AND vh.fundid= " + fund.getId());
        if (null != drawingOfficer && null != drawingOfficer.getId() && drawingOfficer.getId() != -1)
            query.append(" AND ph.DRAWINGOFFICER_ID =" + drawingOfficer.getId());
        if (null != rtgsAssignedFromDate)
            query.append(" AND ih.INSTRUMENTDATE >= '" + Constants.DDMMYYYYFORMAT1.format(rtgsAssignedFromDate) + "'");
        if (null != rtgsAssignedToDate)
        {
            query.append(" AND ih.INSTRUMENTDATE <= '" + Constants.DDMMYYYYFORMAT1.format(rtgsAssignedToDate) + "'");
            query.append(rtgsAssignedToDate + "'");
        }
        if (null != instrumentNumber)
            query.append(" AND ih.INSTRUMENTNUMBER = '" + instrumentNumber + "'");
        if (null != bank && null != bank.getId() && bank.getId() != -1)
            query.append("AND bank.id = " + bank.getId());
        if (null != supplierCode && !supplierCode.isEmpty())
            query.append(" AND ( gld.DETAILKEYID = " + new Integer(supplierCode)
                    + " AND gld.DETAILTYPEID=(SELECT id FROM accountdetailtype WHERE name='Creditor'))");
        if (null != contractorCode && !contractorCode.isEmpty())
            query.append(" AND ( gld.DETAILKEYID = " + new Integer(contractorCode)
                    + " AND gld.DETAILTYPEID=(SELECT id FROM accountdetailtype WHERE name='contractor'))");
        if (null != bankbranch && null != bankbranch.getId() && bankbranch.getId() != -1)
            query.append("AND bnkacc.BRANCHID = " + bankbranch.getId());
        if (null != bankaccount && null != bankaccount.getId() && bankaccount.getId() != -1)
            query.append(" AND bnkacc.id = " + bankaccount.getId());
        if (level.equals("atcoc"))
            query.append("  GROUP BY coa.GLCODE ,coa.NAME,dept.DEPT_NAME, DO.NAME, DO.TAN,");
        else
            query.append("  GROUP BY coa.GLCODE ,coa.NAME, fund.NAME ,");

        query.append(" bank.NAME,bnkbranch.BRANCHNAME, bnkacc.ACCOUNTNUMBER, vh.VOUCHERNUMBER ,ih.INSTRUMENTNUMBER ,ih.INSTRUMENTDATE,"
                +
                " ih.INSTRUMENTAMOUNT,remdt.ID,vh.id ");

        if (level.equals("atcoc"))
            query.append(" order by  coa.GLCODE ,coa.NAME,dept.DEPT_NAME, DO.NAME, DO.TAN,");
        else
            query.append(" order by  coa.GLCODE ,coa.NAME, fund.NAME ,");

        query.append(" bank.NAME,bnkbranch.BRANCHNAME, bnkacc.ACCOUNTNUMBER, vh.VOUCHERNUMBER ,ih.INSTRUMENTNUMBER ,ih.INSTRUMENTDATE,"
                +
                " ih.INSTRUMENTAMOUNT,remdt.ID ");

        final Session session = persistenceService.getSession();
        Query sqlQuery = null;
        if (level.equals("atcoc"))
            sqlQuery = session.createSQLQuery(query.toString())
                    .addScalar("remittanceCOA").addScalar("department").addScalar("drawingOfficer")
                    .addScalar("bankbranchAccount")
                    .addScalar("remittancePaymentNo").addScalar("rtgsNoDate")
                    .addScalar("rtgsAmount").addScalar("remittanceDTId").addScalar("paymentVoucherId")
                    .setResultTransformer(Transformers.aliasToBean(AutoRemittanceBeanReport.class));
        else
            sqlQuery = session.createSQLQuery(query.toString())
                    .addScalar("remittanceCOA").addScalar("fundName").addScalar("bankbranchAccount")
                    .addScalar("remittancePaymentNo").addScalar("rtgsNoDate")
                    .addScalar("rtgsAmount").addScalar("remittanceDTId").addScalar("paymentVoucherId")
                    .setResultTransformer(Transformers.aliasToBean(AutoRemittanceBeanReport.class));
        autoRemittance = remitRecoveryService.populateAutoRemittanceDetailbySQL(sqlQuery);

    }

    public void populateCOCLevelSummaryData()
    {
        final StringBuffer queryString1 = new StringBuffer("SELECT (SUM(case when  glcode = "
                + FinancialConstants.INCOMETAX_CAPITAL +
                "then  rmtAmt  else (case when GLCODE = " + FinancialConstants.INCOMETAX_REVENUE
                + "  then RMTAMT else NULL end)  end)) AS  incomeTaxRemittedAmt," +
                " (SUM(case when glcode =  " + FinancialConstants.SALESTAX_CAPITAL + "  then rmtAmt else " +
                "  (case when GLCODE = " + FinancialConstants.SALESTAX_REVENUE
                + "  then RMTAMT else NULL end) )) AS  salesTaxRemittedAmt," +
                " (SUM(case when  glcode = " + FinancialConstants.MWGWF_MAINTENANCE + " THEN rmtAmt else " +
                "  (case when GLCODE =  " + FinancialConstants.MWGWF_CAPITAL
                + "  then RMTAMT else  NULL end)end)) AS  mwgwfRemittedAmt," +
                " (SUM(case when GLCODE = " + FinancialConstants.SERVICETAX_REVENUE
                + " then RMTAMT else NULL end  ))AS serviceTaxRemittedAmt," +
                " SUM(rmtamt) AS grandTotal FROM( SELECT * FROM (" +
                " SELECT remdt.REMITTEDAMT AS rmtAmt,tds.TYPE  AS glcode" +
                " FROM tds tds, eg_remittance rem, eg_remittance_detail remdt,eg_remittance_gldtl remgltl, voucherheader vh " +
                " WHERE rem.id=remdt.REMITTANCEID" +
                " AND remdt.REMITTANCEGLDTLID = remgltl.id" +
                " AND tds.id=rem.TDSID" +
                " AND vh.status=0 " +
                " AND tds.REMITTANCE_MODE ='A'" +
                " AND rem.PAYMENTVHID =vh.id " +
                " AND tds.TYPE IN ("
                + FinancialConstants.INCOMETAX_CAPITAL + ","
                + FinancialConstants.INCOMETAX_REVENUE + ","
                + FinancialConstants.SALESTAX_CAPITAL + ","
                + FinancialConstants.SALESTAX_REVENUE + ","
                + FinancialConstants.MWGWF_MAINTENANCE + ","
                + FinancialConstants.MWGWF_CAPITAL + ","
                + FinancialConstants.SERVICETAX_REVENUE + ")");
        final Date currentDate = new Date();
        final StringBuffer finyearQuery = new StringBuffer();

        finyearQuery.append("from CFinancialYear where  startingDate <= '").append(Constants.DDMMYYYYFORMAT1.format(currentDate))
                .append("' AND endingDate >='").append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("'");
        final CFinancialYear financialyear = (CFinancialYear) persistenceService.find(finyearQuery.toString());

        if (null != paymentVoucherFromDate)
            queryString1.append(" AND vh.voucherdate >= '" + Constants.DDMMYYYYFORMAT1.format(paymentVoucherFromDate) + "'");
        else
            queryString1.append(" AND vh.voucherdate >= '" + Constants.DDMMYYYYFORMAT1.format(financialyear.getStartingDate())
                    + "'");
        if (null != paymentVoucherToDate)
            queryString1.append(" AND vh.voucherdate <= '" + Constants.DDMMYYYYFORMAT1.format(paymentVoucherToDate) + "'");
        else
            queryString1.append(" AND vh.voucherdate <= '" + Constants.DDMMYYYYFORMAT1.format(financialyear.getEndingDate())
                    + "'");

        queryString1.append(" )) ");
        final Session session = persistenceService.getSession();
        final Query sqlQuery = session.createSQLQuery(queryString1.toString())
                .addScalar("incomeTaxRemittedAmt").addScalar("salesTaxRemittedAmt").addScalar("mwgwfRemittedAmt")
                .addScalar("serviceTaxRemittedAmt").addScalar("grandTotal")
                .setResultTransformer(Transformers.aliasToBean(AutoRemittanceCOCLevelBeanReport.class));
        coaAbstract = sqlQuery.list();
        map.put("coaAbstratct", coaAbstract);

        final StringBuffer queryString2 = new StringBuffer(" SELECT departmentCode," +
                " (SUM(case when  glcode = " + FinancialConstants.INCOMETAX_CAPITAL + " then rmtAmt else " +
                "   (case when GLCODE =" + FinancialConstants.INCOMETAX_REVENUE
                + " then RMTAMT else NULL end) end)) AS  incomeTaxRemittedAmt," +
                " (SUM(case when  glcode = " + FinancialConstants.SALESTAX_CAPITAL + " then rmtAmt else " +
                "  (case when GLCODE=" + FinancialConstants.SALESTAX_REVENUE
                + "  then RMTAMT else  NULL end) end)) AS  salesTaxRemittedAmt," +
                " (SUM(case when  glcode= " + FinancialConstants.MWGWF_MAINTENANCE + " then rmtAmt else " +
                " (case when GLCODE = " + FinancialConstants.MWGWF_CAPITAL
                + " then RMTAMT else NULL end )end)) AS  mwgwfRemittedAmt," +
                " (SUM(case when GLCODE=" + FinancialConstants.SERVICETAX_REVENUE
                + " then RMTAMT else NULL end  ))AS serviceTaxRemittedAmt, " +
                " SUM(rmtamt) AS departmentTotal FROM(" +
                "  SELECT * FROM (" +
                " SELECT dept.DEPT_code  departmentcode, remdt.REMITTEDAMT AS rmtAmt, tds.TYPE  AS glcode" +
                " FROM tds tds, eg_remittance rem, eg_remittance_detail remdt,eg_remittance_gldtl remgltl, voucherheader vh," +
                " eg_department dept" +
                " WHERE rem.id=remdt.REMITTANCEID" +
                " AND remdt.REMITTANCEGLDTLID = remgltl.id" +
                " AND tds.id=rem.TDSID" +
                " AND dept.ID_DEPT = vh.DEPARTMENTID" +
                " AND tds.REMITTANCE_MODE ='A'" +
                " AND vh.status=0" +
                "  AND rem.PAYMENTVHID =vh.id" +
                " AND tds.TYPE IN ("
                + FinancialConstants.INCOMETAX_CAPITAL + ","
                + FinancialConstants.INCOMETAX_REVENUE + ","
                + FinancialConstants.SALESTAX_CAPITAL + ","
                + FinancialConstants.SALESTAX_REVENUE + ","
                + FinancialConstants.MWGWF_MAINTENANCE + ","
                + FinancialConstants.MWGWF_CAPITAL + ","
                + FinancialConstants.SERVICETAX_REVENUE +
                "  )");

        if (null != paymentVoucherFromDate)
            queryString2.append(" AND vh.voucherdate >= '" + Constants.DDMMYYYYFORMAT1.format(paymentVoucherFromDate) + "'");
        else
            queryString2.append(" AND vh.voucherdate >= '" + Constants.DDMMYYYYFORMAT1.format(financialyear.getStartingDate())
                    + "'");
        if (null != paymentVoucherToDate)
            queryString2.append(" AND vh.voucherdate <= '" + Constants.DDMMYYYYFORMAT1.format(paymentVoucherToDate) + "'");
        else
            queryString2.append(" AND vh.voucherdate <= '" + Constants.DDMMYYYYFORMAT1.format(financialyear.getEndingDate())
                    + "'");
        queryString2.append(" ))GROUP BY departmentcode  ORDER BY departmentcode ");

        final Query sqlQuery2 = session.createSQLQuery(queryString2.toString())
                .addScalar("departmentCode")
                .addScalar("incomeTaxRemittedAmt").addScalar("salesTaxRemittedAmt").addScalar("mwgwfRemittedAmt")
                .addScalar("serviceTaxRemittedAmt").addScalar("departmentTotal")
                .setResultTransformer(Transformers.aliasToBean(AutoRemittanceCOCLevelBeanReport.class));
        remittanceList = sqlQuery2.list();
        map.put("summarryList", remittanceList);

    }

    public String getFormattedDate(final Date date) {
        return Constants.DDMMYYYYFORMAT2.format(date);
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public void setRecovery(final Recovery recovery) {
        this.recovery = recovery;
    }

    public Recovery getRecovery() {
        return recovery;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public Fund getFund() {
        return fund;
    }

    public List<EntityType> getEntitiesList() {
        return entitiesList;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(final Bank bank) {
        this.bank = bank;
    }

    public Bankbranch getBankbranch() {
        return bankbranch;
    }

    public void setBankbranch(final Bankbranch bankbranch) {
        this.bankbranch = bankbranch;
    }

    public Bankaccount getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(final Bankaccount bankaccount) {
        this.bankaccount = bankaccount;
    }

    public Date getPaymentVoucherFromDate() {
        return paymentVoucherFromDate;
    }

    public void setPaymentVoucherFromDate(final Date paymentVoucherFromDate) {
        this.paymentVoucherFromDate = paymentVoucherFromDate;
    }

    public Date getPaymentVoucherToDate() {
        return paymentVoucherToDate;
    }

    public void setPaymentVoucherToDate(final Date paymentVoucherToDate) {
        this.paymentVoucherToDate = paymentVoucherToDate;
    }

    public Date getRtgsAssignedFromDate() {
        return rtgsAssignedFromDate;
    }

    public void setRtgsAssignedFromDate(final Date rtgsAssignedFromDate) {
        this.rtgsAssignedFromDate = rtgsAssignedFromDate;
    }

    public Date getRtgsAssignedToDate() {
        return rtgsAssignedToDate;
    }

    public void setRtgsAssignedToDate(final Date rtgsAssignedToDate) {
        this.rtgsAssignedToDate = rtgsAssignedToDate;
    }

    public String getInstrumentNumber() {
        return instrumentNumber;
    }

    public void setInstrumentNumber(final String instrumentNumber) {
        this.instrumentNumber = instrumentNumber;
    }

    public List<AutoRemittanceBeanReport> getAutoRemittance() {
        return autoRemittance;
    }

    public void setAutoRemittance(final List<AutoRemittanceBeanReport> autoRemittance) {
        this.autoRemittance = autoRemittance;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(final String level) {
        this.level = level;
    }

    public DrawingOfficer getDrawingOfficer() {
        return drawingOfficer;
    }

    public void setDrawingOfficer(final DrawingOfficer drawingOfficer) {
        this.drawingOfficer = drawingOfficer;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(final String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(final String contractorCode) {
        this.contractorCode = contractorCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Map<AutoRemittanceBeanReport, List<AutoRemittanceBeanReport>> getAutoremittanceMap() {
        return autoremittanceMap;
    }

    public void setAutoremittanceMap(
            final Map<AutoRemittanceBeanReport, List<AutoRemittanceBeanReport>> autoremittanceMap) {
        this.autoremittanceMap = autoremittanceMap;
    }

    public BigDecimal getRemittedAmountTotal() {
        return remittedAmountTotal;
    }

    public void setRemittedAmountTotal(final BigDecimal remittedAmountTotal) {
        this.remittedAmountTotal = remittedAmountTotal;
    }

    public List<AutoRemittanceCOCLevelBeanReport> getCoaAbstract() {
        return coaAbstract;
    }

    public void setCoaAbstract(final List<AutoRemittanceCOCLevelBeanReport> coaAbstract) {
        this.coaAbstract = coaAbstract;
    }

    public List<AutoRemittanceCOCLevelBeanReport> getRemittanceList() {
        return remittanceList;
    }

    public void setRemittanceList(
            final List<AutoRemittanceCOCLevelBeanReport> remittanceList) {
        this.remittanceList = remittanceList;
    }

}