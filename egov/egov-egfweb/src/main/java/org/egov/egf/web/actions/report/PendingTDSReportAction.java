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
package org.egov.egf.web.actions.report;


import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.commons.Vouchermis;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.utils.EntityType;
import org.egov.deduction.model.EgRemittanceDetail;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.model.TDSEntry;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.deduction.RemittanceBean;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.recoveries.Recovery;
import org.egov.services.deduction.RemitRecoveryService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.FlushMode;
import org.hibernate.query.Query;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;


@Results(value = {
        @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=DeductionDetailedReport.pdf" }),
        @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/xls", "contentDisposition", "no-cache;filename=DeductionDetailedReport.xls" }),
        @Result(name = "summary-PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream",
                "contentType", "application/pdf", "contentDisposition", "no-cache;filename=DeductionsRemittanceSummary.pdf" }),
        @Result(name = "summary-XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream",
                "contentType", "application/xls", "contentDisposition", "no-cache;filename=DeductionsRemittanceSummary.xls" }),
        @Result(name = "results", location = "pendingTDSReport-results.jsp"),
        @Result(name = "entities", location = "pendingTDSReport-entities.jsp"),
        @Result(name = "summaryForm", location = "pendingTDSReport-summaryForm.jsp"),
        @Result(name = "reportForm", location = "pendingTDSReport-reportForm.jsp"),
        @Result(name = "summaryResults", location = "pendingTDSReport-summaryResults.jsp")
})

@ParentPackage("egov")
public class PendingTDSReportAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = 4077974966135536959L;
    String jasperpath = "pendingTDSReport";
    String summaryJasperpath = "summaryTDSReport";
    private Date asOnDate = new Date();
    private Date fromDate;
    private InputStream inputStream;
    private ReportService reportService;
    private String partyName = "";
    private String type = "";
    private Integer detailKey;
    private boolean showRemittedEntries = false;
    private List<RemittanceBean> pendingTDS = new ArrayList<RemittanceBean>();
    private List<TDSEntry> remittedTDS = new ArrayList<TDSEntry>();
    private List<TDSEntry> inWorkflowTDS = new ArrayList<TDSEntry>();
    private Recovery recovery = new Recovery();
    private Fund fund = new Fund();
    private Department department = new Department();
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private EgovCommon egovCommon;
    private final List<EntityType> entitiesList = new ArrayList<EntityType>();
    private RemitRecoveryService remitRecoveryService;
    private FinancialYearHibernateDAO financialYearDAO;
    private String message = "";
    private String mode = "";
    private static Logger LOGGER = Logger.getLogger(PendingTDSReportAction.class);
    
    public void setFinancialYearDAO(final FinancialYearHibernateDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public void setRemitRecoveryService(final RemitRecoveryService remitRecoveryService) {
        this.remitRecoveryService = remitRecoveryService;
    }

    @Override
    public String execute() throws Exception {
        mode = "deduction";
        return "reportForm";
    }

    @Action(value = "/report/pendingTDSReport-summaryReport")
    public String summaryReport() throws Exception {
        return "summaryForm";
    }

    @Override
    public void prepare() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setHibernateFlushMode(FlushMode.MANUAL);
        super.prepare();
        addDropdownData("departmentList", persistenceService.findAllBy("from Department order by name"));
        addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));  

        addDropdownData("recoveryList",
                persistenceService.findAllBy(" from Recovery where isactive=true order by chartofaccounts.glcode"));
    }

    @Action(value = "/report/pendingTDSReport-ajaxLoadData")
    public String ajaxLoadData() {
        populateData();
        return "results";
    }

    @Action(value = "/report/pendingTDSReport-ajaxLoadSummaryData")
    public String ajaxLoadSummaryData() {
        populateSummaryData();
        return "summaryResults";
    }

    public void setAsOnDate(final Date startDate) {
        asOnDate = startDate;
    }

    public Date getAsOnDate() {
        return asOnDate;
    }

    public String getFormattedDate(final Date date) {
        return Constants.DDMMYYYYFORMAT2.format(date);
    }

    @Action(value = "/report/pendingTDSReport-exportPdf")
    public String exportPdf() throws JRException, IOException {
        generateReport();
        return "PDF";
    }

    @Action(value = "/report/pendingTDSReport-exportSummaryPdf")
    public String exportSummaryPdf() throws JRException, IOException {
        generateSummaryReport();
        return "summary-PDF";
    }

    private void generateReport() {
        populateData();
        final ReportRequest reportInput = new ReportRequest(jasperpath, pendingTDS, getParamMap());
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
    }

    private void generateSummaryReport() {
        populateSummaryData();
        final ReportRequest reportInput = new ReportRequest(summaryJasperpath, remittedTDS, getParamMap());
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
    }

    Map<String, Object> getParamMap() {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("remittedTDSJasper", this.getClass().getResourceAsStream("/reports/templates/remittedTDSReport.jasper"));
        paramMap.put("inWorkflowTDSJasper", this.getClass().getResourceAsStream("/reports/templates/inWorkflowTDSReport.jasper"));
        paramMap.put("inWorkflowTDS", inWorkflowTDS);
        if (showRemittedEntries)
            paramMap.put("remittedTDS", remittedTDS);
        else
            paramMap.put("remittedTDS", null);
        final String formatedAsOndate = Constants.DDMMYYYYFORMAT2.format(asOnDate);
        paramMap.put("asOnDate", formatedAsOndate);
        if (fromDate != null)
        {
            final String formatedFromDate = Constants.DDMMYYYYFORMAT2.format(fromDate);
            paramMap.put("fromDate", formatedFromDate);
            paramMap.put("heading", "Deduction detailed report for "+ recovery.getType() +" From " + formatedFromDate + "  to " + formatedAsOndate);
            paramMap.put("summaryheading", "Deductions remittance summary for "+ recovery.getType() +" From " + formatedFromDate + "  to " + formatedAsOndate);
            paramMap.put("fromDateText", "From Date :      " + formatedFromDate);
        } else{
            paramMap.put("heading", "Deduction detailed report for "+ recovery.getType() +" as on " + formatedAsOndate);
            paramMap.put("summaryheading", "Deductions remittance summary for "+ recovery.getType() +" as on " + formatedAsOndate);
        }
        fund = (Fund) persistenceService.find("from Fund where id=?1", fund.getId());
        paramMap.put("fundName", fund.getName());
        paramMap.put("partyName", partyName);
        if (department.getId() != null && department.getId() != -1) {
            department = (Department) persistenceService.find("from Department where id=?1", department.getId());
            paramMap.put("departmentName", department.getName());
        }
        recovery = (Recovery) persistenceService.find("from Recovery where id=?1", recovery.getId());
        paramMap.put("recoveryName", recovery.getRecoveryName());
        return paramMap;
    }

    @ReadOnly
    private void populateData() {
        validateFinYear();
        if (getFieldErrors().size() > 0)
            return;
        recovery = (Recovery) persistenceService.find("from Recovery where id=?1", recovery.getId());
        type = recovery.getType();
        final RemittanceBean remittanceBean = new RemittanceBean();
        remittanceBean.setRecoveryId(recovery.getId());
        if (fromDate != null)
            remittanceBean.setFromDate(Constants.DDMMYYYYFORMAT1.format(fromDate));
        pendingTDS = remitRecoveryService.getRecoveryDetailsForReport(remittanceBean, getVoucherHeader(), detailKey);
        final StringBuffer query1 = new StringBuffer(1000);
        List<EgRemittanceDetail> result1 = new ArrayList<EgRemittanceDetail>();
        int index = 1;
        List<Object> params = new ArrayList<>();
        query1.append("from EgRemittanceDetail where  egRemittanceGldtl.generalledgerdetail.generalLedgerId.glcodeId.id=?").append(index++)
                .append("and egRemittance.fund.id=?").append(index++).append(" and egRemittance.voucherheader.status = 5 and ")
                .append(" egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.status=0 and ")
                .append("egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.voucherDate <= ?").append(index++);
        params.add(recovery.getChartofaccounts().getId());
        params.add(fund.getId());
        params.add(asOnDate);
        if (fromDate != null) {
            query1.append(" and egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.voucherDate >= ?").append(index++);
            params.add(fromDate);
        }
        if (department.getId() != null && department.getId() != -1) {
            query1.append(" and egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.vouchermis.departmentid.id=?").append(index++);
            params.add(department.getId());
        }
        if (detailKey != null && detailKey != -1) {
            query1.append(" and egRemittanceGldtl.generalledgerdetail.detailkeyid=?").append(index++);
            params.add(detailKey);
        }
        query1.append(" order by egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.voucherNumber ");
        result1 = persistenceService.findAllBy(query1.toString(), params.toArray());
        Boolean createPartialRow1 = false;
        for (final EgRemittanceDetail entry : result1) {
            createPartialRow1 = false;
            for (final TDSEntry tdsExists : inWorkflowTDS)
                if (tdsExists.getEgRemittanceGlDtlId().intValue() == entry.getEgRemittanceGldtl().getId().intValue())
                    createPartialRow1 = true;
            TDSEntry tds = new TDSEntry();
            tds.setEgRemittanceGlDtlId(entry.getEgRemittanceGldtl().getId());
            if (!createPartialRow1)
                tds = createTds(entry);
            tds.setRemittedOn(Constants.DDMMYYYYFORMAT2.format(entry.getEgRemittance().getVoucherheader().getVoucherDate()));
            tds.setAmount(entry.getRemittedamt());
            if (entry.getEgRemittance().getVoucherheader() != null)
                tds.setPaymentVoucherNumber(entry.getEgRemittance().getVoucherheader().getVoucherNumber());
            final List<InstrumentVoucher> ivList = persistenceService.findAllBy(
                    new StringBuilder("from InstrumentVoucher where instrumentHeaderId.statusId.description in(?1,?2,?3)")
                            .append(" and voucherHeaderId=?4").toString(),
                    FinancialConstants.INSTRUMENT_DEPOSITED_STATUS, FinancialConstants.INSTRUMENT_CREATED_STATUS,
                    FinancialConstants.INSTRUMENT_RECONCILED_STATUS, entry.getEgRemittance().getVoucherheader());
            boolean isMultiple = false;
            for (final InstrumentVoucher iv : ivList)
            {
                if (entry.getRemittedamt().compareTo(iv.getInstrumentHeaderId().getInstrumentAmount()) != 0)
                    isMultiple = true;

                tds.setChequeNumber(iv.getInstrumentHeaderId().getInstrumentNumber());
                if (isMultiple)
                    tds.setChequeNumber(tds.getChequeNumber() + "-MULTIPLE");
                tds.setChequeAmount(iv.getInstrumentHeaderId().getInstrumentAmount());
                if (iv.getInstrumentHeaderId().getInstrumentDate() != null)
                    tds.setDrawnOn(Constants.DDMMYYYYFORMAT2.format(iv.getInstrumentHeaderId().getInstrumentDate()));
            }
            inWorkflowTDS.add(tds);
        }
        if (showRemittedEntries) {
            final StringBuffer query = new StringBuffer(1000);
            params = new ArrayList<>();
            index = 1;
            List<EgRemittanceDetail> result = new ArrayList<EgRemittanceDetail>();
            query.append("from EgRemittanceDetail where  egRemittanceGldtl.generalledgerdetail.generalLedgerId.glcodeId.id=?").append(index++)
                    .append(" and egRemittance.fund.id=?").append(index++).append(" and egRemittance.voucherheader.status = 0 and ")
                    .append(" egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.status=0 and ")
                    .append(" egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.voucherDate <= ?").append(index++);
            params.add(recovery.getChartofaccounts().getId());
            params.add(fund.getId());
            params.add(asOnDate);
            if (fromDate != null) {
                query.append(" and egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.voucherDate >= ?").append(index++);
                params.add(fromDate);
            }
            if (department.getId() != null && department.getId() != -1) {
                query.append(" and egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.vouchermis.departmentid.id=?").append(index++);
                params.add(department.getId());
            }
            if (detailKey != null && detailKey != -1) {
                query.append(" and egRemittanceGldtl.generalledgerdetail.detailkeyid=?").append(index++);
                params.add(detailKey);
            }
            query.append(" order by egRemittanceGldtl.generalledgerdetail.generalLedgerId.voucherHeaderId.voucherNumber ");
            result = persistenceService.findAllBy(query.toString(), params);

            Boolean createPartialRow = false;
            for (final EgRemittanceDetail entry : result) {
                createPartialRow = false;
                for (final TDSEntry tdsExists : remittedTDS)
                    if (tdsExists.getEgRemittanceGlDtlId().intValue() == entry.getEgRemittanceGldtl().getId().intValue())
                        createPartialRow = true;
                TDSEntry tds = new TDSEntry();
                tds.setEgRemittanceGlDtlId(entry.getEgRemittanceGldtl().getId());
                if (!createPartialRow)
                    tds = createTds(entry);
                tds.setRemittedOn(Constants.DDMMYYYYFORMAT2.format(entry.getEgRemittance().getVoucherheader().getVoucherDate()));
                tds.setAmount(entry.getRemittedamt());
                if (entry.getEgRemittance().getVoucherheader() != null)
                    tds.setPaymentVoucherNumber(entry.getEgRemittance().getVoucherheader().getVoucherNumber());
                final List<InstrumentVoucher> ivList = persistenceService.findAllBy(
                        new StringBuilder("from InstrumentVoucher where instrumentHeaderId.statusId.description in(?1,?2,?3)")
                                .append(" and voucherHeaderId=?4").toString(),
                        FinancialConstants.INSTRUMENT_DEPOSITED_STATUS, FinancialConstants.INSTRUMENT_CREATED_STATUS,
                        FinancialConstants.INSTRUMENT_RECONCILED_STATUS, entry.getEgRemittance().getVoucherheader());
                boolean isMultiple = false;
                for (final InstrumentVoucher iv : ivList)
                {
                    if (entry.getRemittedamt().compareTo(iv.getInstrumentHeaderId().getInstrumentAmount()) != 0)
                        isMultiple = true;

                    tds.setChequeNumber(iv.getInstrumentHeaderId().getInstrumentNumber());
                    if (isMultiple)
                        tds.setChequeNumber(tds.getChequeNumber() + "-MULTIPLE");
                    tds.setChequeAmount(iv.getInstrumentHeaderId().getInstrumentAmount());
                    if (iv.getInstrumentHeaderId().getInstrumentDate() != null)
                        tds.setDrawnOn(Constants.DDMMYYYYFORMAT2.format(iv.getInstrumentHeaderId().getInstrumentDate()));
                }
                remittedTDS.add(tds);
            }
           
        }
    }

    /**
     * show only pending TDSes
     */
    @ReadOnly
    private void populateSummaryData() {
        recovery = (Recovery) persistenceService.find("from Recovery where id=?1", Long.valueOf(recovery.getId()));
        type = recovery.getType();
        List<Object[]> result = new ArrayList<Object[]>();
        List<Object[]> resultTolDeduction = new ArrayList<Object[]>();
        try {
            StringBuffer qry = new StringBuffer("select vh.name,sum(erd.remittedamt),er.month")
                    .append(" from eg_remittance_detail erd, voucherheader vh1 right outer join eg_remittance er on vh1.id=er.paymentvhid,voucherheader vh,vouchermis mis,")
                    .append("generalledger gl, generalledgerdetail gld,fund f,eg_remittance_gldtl ergl")
                    .append(" where erd.remittancegldtlid= ergl.id and erd.remittanceid=er.id and gl.glcodeid=:glCodeId and vh.id=mis.voucherheaderid and vh1.status=0")
                    .append(" and ergl.gldtlid=gld.id and gl.id=gld.generalledgerid ")
                    .append(" and gl.voucherheaderid=vh.id and er.fundid=f.id and f.id=:fundId and vh.status=0 and vh.voucherDate <= to_date(:asOnDate,'dd/MM/yyyy') ")
                    .append(" and vh.voucherDate >= to_date(:startingDate, 'dd/MM/yyyy') ");
            if (department.getId() != null && department.getId() != -1)
                qry.append(" and mis.departmentid=:deptId");
            if (detailKey != null && detailKey != -1)
                qry.append(" and gld.detailkeyid=:detailKeyId");
            qry.append(" group by er.month,vh.name order by er.month,vh.name");

            if (LOGGER.isDebugEnabled())
                LOGGER.debug(qry.toString());
            Query nativeQuery = persistenceService.getSession().createNativeQuery(qry.toString())
                    .setParameter("glCodeId", Long.valueOf(recovery.getChartofaccounts().getId()), LongType.INSTANCE)
                    .setParameter("fundId", Long.valueOf(fund.getId()), LongType.INSTANCE)
                    .setParameter("asOnDate", asOnDate, DateType.INSTANCE)
                    .setParameter("startingDate", financialYearDAO.getFinancialYearByDate(asOnDate).getStartingDate(), DateType.INSTANCE);
            if (department.getId() != null && department.getId() != -1)
                nativeQuery.setParameter("deptId", Long.valueOf(department.getId()), LongType.INSTANCE);
            if (detailKey != null && detailKey != -1)
                nativeQuery.setParameter("detailKeyId", Integer.valueOf(detailKey), IntegerType.INSTANCE);

            result = nativeQuery.list();
            // Query to get total deduction
            StringBuffer qryTolDeduction = new StringBuffer("SELECT type,MONTH,SUM(gldtamt) FROM (SELECT DISTINCT er.month AS MONTH,ergl.gldtlamt AS gldtamt, ")
                    .append(" ergl.gldtlid as gldtlid,vh.name AS type")
                    .append(" FROM eg_remittance_detail erd,voucherheader vh1 RIGHT OUTER JOIN eg_remittance er ON ")
                    .append(" vh1.id=er.paymentvhid,voucherheader vh,vouchermis mis,generalledger gl,generalledgerdetail gld,fund f,eg_remittance_gldtl ergl ")
                    .append(" WHERE erd.remittancegldtlid= ergl.id AND erd.remittanceid=er.id  AND gl.glcodeid =:glCodeId AND vh.id =mis.voucherheaderid ")
                    .append(" AND vh1.status =0 AND f.id =:fundId AND vh.status =0 AND vh.voucherDate <= to_date(:asOnDate,'dd/MM/yyyy' ) and ")
                    .append(" vh.voucherDate >= to_date(:startingDate,'dd/MM/yyyy') ");
            if (department.getId() != null && department.getId() != -1)
                qryTolDeduction.append(" and mis.departmentid=:deptId");
            if (detailKey != null && detailKey != -1)
                qryTolDeduction.append(" and gld.detailkeyid=:detailKeyId");
            qryTolDeduction.append(" ) as temptable group by type,month");

            nativeQuery = persistenceService.getSession().createNativeQuery(qryTolDeduction.toString())
                    .setParameter("glCodeId", Long.valueOf(recovery.getChartofaccounts().getId()), LongType.INSTANCE)
                    .setParameter("fundId", Long.valueOf(fund.getId()), LongType.INSTANCE)
                    .setParameter("asOnDate", asOnDate, DateType.INSTANCE)
                    .setParameter("startingDate", financialYearDAO.getFinancialYearByDate(asOnDate).getStartingDate(), DateType.INSTANCE);
            if (department.getId() != null && department.getId() != -1)
                nativeQuery.setParameter("deptId", Long.valueOf(department.getId()), LongType.INSTANCE);
            if (detailKey != null && detailKey != -1)
                nativeQuery.setParameter("detailKeyId", Integer.valueOf(detailKey), IntegerType.INSTANCE);
            resultTolDeduction = nativeQuery.list();
        } catch (final ApplicationRuntimeException e) {
            message = e.getMessage();
            return;
        } catch (final Exception e) {
            message = e.getMessage();
            return;
        }
        for (final Object[] entry : result)
            for (final Object[] dedentry : resultTolDeduction) {
                final TDSEntry tds = new TDSEntry();
                final String monthChk = DateUtils.getAllMonthsWithFullNames().get(Integer.valueOf(entry[2].toString()) + 1);
                if (monthChk.equalsIgnoreCase(DateUtils.getAllMonthsWithFullNames().get(
                        Integer.valueOf(dedentry[1].toString()) + 1))
                        && dedentry[0].toString().equalsIgnoreCase(entry[0].toString())) {
                    tds.setNatureOfDeduction(entry[0].toString());
                    tds.setTotalRemitted(new BigDecimal(entry[1].toString()));
                    tds.setMonth(DateUtils.getAllMonthsWithFullNames().get(Integer.valueOf(entry[2].toString()) + 1));
                    final BigDecimal totDeduction = new BigDecimal(dedentry[2].toString());
                    tds.setTotalDeduction(totDeduction);
                    remittedTDS.add(tds);
                }
            }
    }

    private CVoucherHeader getVoucherHeader() {
        final CVoucherHeader voucherHeader = new CVoucherHeader();
        voucherHeader.setFundId(fund);
        final Vouchermis vouchermis = new Vouchermis();
        voucherHeader.setVouchermis(vouchermis);
        voucherHeader.getVouchermis().setDepartmentid(department);
        voucherHeader.setVoucherDate(asOnDate);
        return voucherHeader;
    }

    @Action(value = "/report/pendingTDSReport-ajaxLoadEntites")
    public String ajaxLoadEntites() throws ClassNotFoundException {
        if (parameters.containsKey("recoveryId") && parameters.get("recoveryId")[0] != null
                && !"".equals(parameters.get("recoveryId")[0])) {
            recovery = (Recovery) persistenceService.find("from Recovery where id=?1",
                    Long.valueOf(parameters.get("recoveryId")[0]));
            for (final CChartOfAccountDetail detail : recovery.getChartofaccounts().getChartOfAccountDetails())
                entitiesList.addAll(egovCommon.loadEntitesFor(detail.getDetailTypeId()));
        }
        return "entities";
    }

    private TDSEntry createTds(final EgRemittanceDetail entry) {
        final TDSEntry tds = new TDSEntry();
        if (entry.getEgRemittanceGldtl().getRecovery() != null)
            tds.setPartyCode(entry.getEgRemittanceGldtl().getRecovery().getEgPartytype().getCode());
        tds.setEgRemittanceGlDtlId(entry.getEgRemittanceGldtl().getId());
        tds.setNatureOfDeduction(entry.getEgRemittanceGldtl().getGeneralledgerdetail().getGeneralLedgerId().getVoucherHeaderId()
                .getName());
        tds.setVoucherNumber(entry.getEgRemittanceGldtl().getGeneralledgerdetail().getGeneralLedgerId().getVoucherHeaderId()
                .getVoucherNumber());
        tds.setVoucherDate(Constants.DDMMYYYYFORMAT2.format(entry.getEgRemittanceGldtl().getGeneralledgerdetail()
                .getGeneralLedgerId().getVoucherHeaderId().getVoucherDate()));
        final EntityType entityType = getEntity(entry);
        if (entityType != null) {
            tds.setPartyName(entityType.getName());
            tds.setPartyCode(entityType.getCode());
            tds.setPanNo(entityType.getPanno());
        }
        tds.setAmount(entry.getEgRemittanceGldtl().getGldtlamt());
        return tds;
    }

    private EntityType getEntity(final EgRemittanceDetail entry) {
        egovCommon.setPersistenceService(persistenceService);
        final Integer detailKeyId = entry.getEgRemittanceGldtl().getGeneralledgerdetail().getDetailKeyId().intValue();
        EntityType entityType = null;
        try {
            entityType = egovCommon.getEntityType(entry.getEgRemittanceGldtl().getGeneralledgerdetail().getDetailTypeId(),
                    detailKeyId);
        } catch (final ApplicationException e) {

        }
        return entityType;
    }

    @Action(value = "/report/pendingTDSReport-exportXls")
    public String exportXls() throws JRException, IOException {
        populateData();
        final ReportRequest reportInput = new ReportRequest(jasperpath, pendingTDS, getParamMap());
        reportInput.setReportFormat(ReportFormat.XLS);
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return "XLS";
    }

    @Action(value = "/report/pendingTDSReport-exportSummaryXls")
    public String exportSummaryXls() throws JRException, IOException {
        populateSummaryData();
        final ReportRequest reportInput = new ReportRequest(summaryJasperpath, remittedTDS, getParamMap());
        reportInput.setReportFormat(ReportFormat.XLS);
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return "summary-XLS";
    }

    public void validateFinYear()
    {
        if (fromDate != null)
        {
            Constants.DDMMYYYYFORMAT2.format(fromDate);
            if (financialYearDAO.isSameFinancialYear(fromDate, asOnDate))
                return;
            else
                addFieldError("fromDate", "Dates are not within same Financial Year");
        }

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

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public void setPartyName(final String partyName) {
        this.partyName = partyName;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setShowRemittedEntries(final boolean showRemittedEntries) {
        this.showRemittedEntries = showRemittedEntries;
    }

    public boolean getShowRemittedEntries() {
        return showRemittedEntries;
    }

    public boolean isShowRemittedEntries() {
        return showRemittedEntries;
    }

    public void setPendingTDS(final List<RemittanceBean> pendingTDS) {
        this.pendingTDS = pendingTDS;
    }

    public List<RemittanceBean> getPendingTDS() {
        return pendingTDS;
    }

    public void setRemittedTDS(final List<TDSEntry> remittedTDS) {
        this.remittedTDS = remittedTDS;
    }

    public List<TDSEntry> getRemittedTDS() {
        return remittedTDS;
    }

    public List<TDSEntry> getInWorkflowTDS() {
        return inWorkflowTDS;
    }

    public void setInWorkflowTDS(List<TDSEntry> inWorkflowTDS) {
        this.inWorkflowTDS = inWorkflowTDS;
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

    public void setDetailKey(final Integer detailKey) {
        this.detailKey = detailKey;
    }

    public Integer getDetailKey() {
        return detailKey;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}