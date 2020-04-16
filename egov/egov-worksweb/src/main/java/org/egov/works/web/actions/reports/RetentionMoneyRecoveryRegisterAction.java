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
package org.egov.works.web.actions.reports;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CChartOfAccounts;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.model.budget.BudgetGroup;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.services.ContractorBillService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("deprecation")
@ParentPackage("egov")
@Results({
        @Result(name = RetentionMoneyRecoveryRegisterAction.EXPORTPDF, type = "stream", location = "pdfInputStream", params = {
                "inputName", "pdfInputStream", "contentType", "application/pdf", "contentDisposition",
                "no-cache;filename=RetentionMoneyRecoveryRegister.pdf" }),
        @Result(name = RetentionMoneyRecoveryRegisterAction.EXPORTEXCEL, type = "stream", location = "excelInputStream", params = {
                "inputName", "excelInputStream", "contentType", "application/xls", "contentDisposition",
                "no-cache;filename=RetentionMoneyRecoveryRegister.xls" }) })
public class RetentionMoneyRecoveryRegisterAction extends SearchFormAction {

    private static final long serialVersionUID = 3137793754124318372L;

    private String estimateNumber;
    private String projectCode;
    private String contractorCodeName;
    private Date billDateFrom;
    private Date billDateTo;
    private List<Object> paramList;
    private List<Object> paramListCountQuery;
    private String reportSubTitle;
    private Long billDepartment;
    public static final String EXPORTPDF = "exportPdf";
    public static final String EXPORTEXCEL = "exportExcel";
    private ReportService reportService;
    private InputStream pdfInputStream;
    private InputStream excelInputStream;
    @Autowired
    private DepartmentService departmentService;
    private List<Long> depositCOA;
    private List<Long> budgetHeads;
    private final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private String subHeaderBudgetHeads;
    private String subHeaderDepositCOA;
    private BigDecimal retentionMoneyAmountFrom = null;
    private BigDecimal retentionMoneyAmountTo = null;
    private ContractorBillService contractorBillService;
    private String billType;
    public Integer retentionMoneyRefPeriod;

    @PersistenceContext
    private EntityManager entityManager;

    public RetentionMoneyRecoveryRegisterAction() {
    }

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public String execute() {
        return INDEX;
    }

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("billDepartmentList", departmentService.getAllDepartments());
        addDropdownData("billTypeList", contractorBillService.getBillType());
        addDropdownData(
                "budgetHeadList",
                entityManager.createQuery(new StringBuffer("select distinct(bg)")
                        .append(" from FinancialDetail fd , BudgetGroup bg")
                        .append(" where fd.abstractEstimate.egwStatus.code = :estimateStatus and fd.abstractEstimate.projectCode.id")
                        .append(" in (select bpd.accountDetailKeyId from EgBillPayeedetails bpd where bpd.accountDetailTypeId = ")
                        .append(" (select id from Accountdetailtype where name='PROJECTCODE')")
                        .append(" and bpd.egBilldetailsId.egBillregister.status.code = :billRegisterStatus ")
                        .append(" and expendituretype='Works' ) and bg=fd.budgetGroup order by bg.name ").toString(),
                        BudgetGroup.class)
                        .setParameter("estimateStatus", AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString())
                        .setParameter("billRegisterStatus", ContractorBillRegister.BillStatus.APPROVED.toString())
                        .getResultList());

        addDropdownData(
                "depositCOAList",
                entityManager.createQuery(new StringBuffer("select distinct(fd.coa)")
                        .append(" from FinancialDetail fd")
                        .append(" where fd.abstractEstimate.egwStatus.code = :estimateStatus and fd.abstractEstimate.projectCode.id")
                        .append(" in (select bpd.accountDetailKeyId from EgBillPayeedetails bpd where bpd.accountDetailTypeId = ")
                        .append(" (select id from Accountdetailtype where name='PROJECTCODE')")
                        .append(" and bpd.egBilldetailsId.egBillregister.status.code = :billRegisterStatus ")
                        .append(" and expendituretype='Works') order by glcode ").toString(), CChartOfAccounts.class)
                        .setParameter("estimateStatus", AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString())
                        .setParameter("billRegisterStatus", ContractorBillRegister.BillStatus.APPROVED.toString())
                        .getResultList());
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
        final StringBuffer query = getSelectQuery().append(getSearchQuery());
        final String countQry = new StringBuffer(" select count(*) from (").append(query).append(")").toString();
        final String orderByClause = " ORDER BY \"Bill Date\"";

        setPageSize(30);
        return new SearchQuerySQL(query.append(orderByClause).toString(), countQry, paramList);
    }

    @Override
    public String search() {
        return "search";
    }

    @SuppressWarnings("unchecked")
    public String searchList() {
        boolean isError = false;
        if (billDateFrom != null && billDateTo == null) {
            addFieldError("billDateTo", getText("search.billDateTo.null"));
            isError = true;
        }
        if (billDateTo != null && billDateFrom == null) {
            addFieldError("billDateFrom", getText("search.billDateFrom.null"));
            isError = true;
        }
        if (billDateFrom != null && billDateTo != null && !DateUtils.compareDates(getToDate(), getFromDate())) {
            addFieldError("billDateFrom", getText("greaterthan.billDateTo.billDateFrom"));
            isError = true;
        }
        if (billDateTo != null && !DateUtils.compareDates(new Date(), getToDate())) {
            addFieldError("billDateTo", getText("greaterthan.billDateTo.currentdate"));
            isError = true;
        }
        if (isError)
            return EDIT;

        super.search();
        if (searchResult != null && searchResult.getList() != null && !searchResult.getList().isEmpty()) {
            final List<RetentionMoneyRecoveryRegisterBean> resultList = setBeanValues(searchResult.getList());
            searchResult.getList().clear();
            searchResult.getList().addAll(resultList);
        }
        return "search";
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<RetentionMoneyRecoveryRegisterBean> setBeanValues(final List searchList) {
        final List<RetentionMoneyRecoveryRegisterBean> resultList = new ArrayList<>();
        for (final Object[] object : (List<Object[]>) searchList) {
            final RetentionMoneyRecoveryRegisterBean retentionMoneyRecoveryRegisterBean = new RetentionMoneyRecoveryRegisterBean();
            if (object[0] != null)
                retentionMoneyRecoveryRegisterBean.setBillDepartment(object[0].toString());

            if (object[1] != null)
                retentionMoneyRecoveryRegisterBean.setContractorCode(object[1].toString());

            if (object[2] != null)
                retentionMoneyRecoveryRegisterBean.setContractorName(object[2].toString());

            if (object[3] != null)
                retentionMoneyRecoveryRegisterBean.setProjectCode(object[3].toString());

            if (object[11] != null && Integer.valueOf(object[11].toString()) > 1)
                retentionMoneyRecoveryRegisterBean
                        .setProjectName(getText("retentionMoneyRecoveryRegister.multiple.label"));
            else if (object[4] != null)
                retentionMoneyRecoveryRegisterBean.setProjectName(object[4].toString());

            if (object[5] != null)
                retentionMoneyRecoveryRegisterBean.setBillNumber(object[5].toString());

            if (object[6] != null)
                retentionMoneyRecoveryRegisterBean.setBillType(object[6].toString());

            if (object[7] != null)
                retentionMoneyRecoveryRegisterBean.setBillDate(formatter.format(object[7]));

            if (object[8] != null)
                retentionMoneyRecoveryRegisterBean.setVoucherNumber(object[8].toString());
            else
                retentionMoneyRecoveryRegisterBean.setVoucherNumber("NA");

            if (object[9] != null)
                retentionMoneyRecoveryRegisterBean.setBillAmount(new BigDecimal(object[9].toString()));

            if (object[10] != null)
                retentionMoneyRecoveryRegisterBean.setRetentionMoneyRecoveredAmount(new BigDecimal(object[10]
                        .toString()));

            if (object[13] != null)
                if (object[13].toString().equalsIgnoreCase("1"))
                    retentionMoneyRecoveryRegisterBean.setBillType(WorksConstants.FINAL_BILL);

            if (object[12] != null) {
                // If the isFinalBill flag is not set for the project code, then
                // set the refund date based on the bill status
                if (object[13].toString().equalsIgnoreCase("0")) {
                    if (object[6].toString().equalsIgnoreCase("Part Bill"))
                        retentionMoneyRecoveryRegisterBean.setRefundDate("NA");
                    else
                        retentionMoneyRecoveryRegisterBean.setRefundDate(formatter.format(object[12]));
                } else
                    retentionMoneyRecoveryRegisterBean.setRefundDate(formatter.format(object[12]));
            } else
                retentionMoneyRecoveryRegisterBean.setRefundDate("NA");

            resultList.add(retentionMoneyRecoveryRegisterBean);
        }
        return resultList;

    }

    private StringBuffer getSelectQuery() {
        return new StringBuffer("select \"Bill Department\", \"Contractor Code\",\"Contractor Name\",")
                .append(" RTrim(xmlagg(xmlelement(a,\"Project Code\" || ', ').extract('//text()')),', ') as \"Project Code\",")
                .append(" RTrim(xmlagg(xmlelement(a,\"Project Name\" || ',').extract('//text()')),',') as \"Project Name\",")
                .append(" \"Bill Number\", \"Bill Type\", \"Bill Date\", \"Voucher Number\", \"Bill Amount\", ")
                .append(" \"Retention money recovered\", count(\"Project Code\"),\"Refund Date\",\"PC Flag\" ");
    }

    @SuppressWarnings("unchecked")
    private String getSearchQuery() {
        paramList = new ArrayList<>();
        final StringBuffer titleBuffer = new StringBuffer();

        titleBuffer.append(getText("retentionMoneyRecoveryRegister.title.report"));

        final StringBuffer query1 = new StringBuffer(" (SELECT dp.name AS \"Bill Department\", cont.code AS \"Contractor Code\",")
                .append(" cont.name AS \"Contractor Name\", pc.code AS \"Project Code\", pc.name as \"Project Name\",")
                .append(" br.billnumber AS \"Bill Number\", br.billtype AS \"Bill Type\", br.billdate AS \"Bill Date\",")
                .append(" vh.vouchernumber AS \"Voucher Number\", br.billamount AS \"Bill Amount\",")
                .append(" bd.creditamount AS \"Retention money recovered\",(pcmis.work_completion_date+(365*pcmis.defect_liability_period))")
                .append(" AS \"Refund Date\", pc.is_final_bill AS \"PC Flag\" ")
                .append(" FROM EG_DEPARTMENT dp, eg_billregister br, eg_billregistermis bmis LEFT OUTER JOIN VOUCHERHEADER vh")
                .append(" ON vh.id=bmis.voucherheaderid and vh.status=0, eg_billdetails bd, eg_billpayeedetails bpd,")
                .append(" egw_contractor cont, egw_projectcode pc left outer join egw_projectcodemis pcmis on pcmis.projectcode_id = pc.id ")
                .append(" WHERE dp.id_dept = bmis.departmentid AND br.id = bd.billid AND bmis.billid = br.id AND bd.id = bpd.billdetailid ")
                .append(" AND cont.id = bpd.ACCOUNTDETAILKEYID AND bpd.ACCOUNTDETAILTYPEID=(select id from accountdetailtype")
                .append(" where name='contractor') AND bd.creditamount > 0 AND br.EXPENDITURETYPE = 'Works' AND br.STATUSID IN")
                .append(" (select id from egw_status where code='APPROVED' and moduletype='CONTRACTORBILL') ")
                .append(" and bd.glcodeid in(select coa1.id from chartofaccounts coa1 ")
                .append(" where coa1.purposeid = (select id from egf_accountcode_purpose where name = 'RETENTION_MONEY'))")
                .append(" and pc.id in(select bpd1.accountdetailkeyid from eg_billpayeedetails bpd1,eg_billdetails bd1 where ")
                .append(" bpd1.ACCOUNTDETAILTYPEID=(select id from accountdetailtype where name='PROJECTCODE')")
                .append(" AND bd1.id = bpd1.billdetailid and bd1.debitamount>0 and bd1.billid=br.id) and not exists")
                .append(" (select cbr.id from egw_contractorbill cbr where cbr.id = br.id ) ");

        int index = 1;

        Map<String, Object> whereClauseMap = formSearchConditionsQuery(index);
        String whereClauseBfr = whereClauseMap.get("whereClause").toString();
        String estimateStartQueryCondition = whereClauseMap.get("estimateQryCondition").toString();
        List<Object> params = (List<Object>) whereClauseMap.get("params");
        paramList.addAll(params);

        index = (int) whereClauseMap.get("params");

        query1.append(whereClauseBfr).append(estimateStartQueryCondition);

        if (retentionMoneyRefPeriod != null && retentionMoneyRefPeriod != -1) {
            final Date currentDate = new Date();
            final Long period = retentionMoneyRefPeriod * 24 * 3600 * 1000L;
            final Date toDate = new Date(currentDate.getTime() + period.longValue());
            query1.append(" and (br.billtype = ?").append(index++).append(" OR pc.is_final_bill = 1) ");
            paramList.add(WorksConstants.FINAL_BILL);
            query1.append(" and (pcmis.work_completion_date+(365*pcmis.defect_liability_period)) between ?")
                    .append(index++).append(" and ?").append(index++);
            paramList.add(DateUtils.getFormattedDate(currentDate, "dd-MMM-yyyy"));
            paramList.add(DateUtils.getFormattedDate(toDate, "dd-MMM-yyyy"));
        }
        query1.append(")");

        final StringBuffer query2 = new StringBuffer(
                "(SELECT dp.dept_name AS \"Bill Department\", cont.code AS \"Contractor Code\",")
                        .append(" cont.name AS \"Contractor Name\", pc.code AS \"Project Code\", pc.name as \"Project Name\",")
                        .append(" br.billnumber AS \"Bill Number\", br.billtype AS \"Bill Type\", br.billdate AS \"Bill Date\",")
                        .append(" vh.vouchernumber AS \"Voucher Number\", br.billamount AS \"Bill Amount\",")
                        .append(" bd.creditamount AS \"Retention money recovered\",(woe.work_completion_date+ (365*wo.defect_liability_period))")
                        .append(" as \"Refund Date\", pc.is_final_bill AS \"PC Flag\" FROM EG_DEPARTMENT dp, eg_billregister br,")
                        .append(" eg_billregistermis bmis LEFT OUTER JOIN VOUCHERHEADER vh ON vh.id=bmis.voucherheaderid and vh.status=0,")
                        .append(" eg_billdetails bd, eg_billpayeedetails bpd, egw_contractor cont, egw_projectcode pc,egw_work_order wo")
                        .append(" left outer join egw_workorder_estimate woe on woe.workorder_id = wo.id, egw_mb_header mbh ")
                        .append(" WHERE dp.id = bmis.departmentid AND br.id = bd.billid AND bmis.billid = br.id AND bd.id = bpd.billdetailid ")
                        .append(" AND cont.id = bpd.ACCOUNTDETAILKEYID AND mbh.WORKORDER_ESTIMATE_ID = woe.id AND mbh.billregister_id = br.id")
                        .append(" AND bpd.ACCOUNTDETAILTYPEID=(select id from accountdetailtype where name='contractor') ")
                        .append(" AND bd.creditamount > 0 AND br.EXPENDITURETYPE = 'Works' AND br.STATUSID IN (select id from egw_status")
                        .append(" where code='APPROVED' and moduletype='CONTRACTORBILL') ")
                        .append(" and bd.glcodeid in(select coa1.id from chartofaccounts coa1 ")
                        .append(" where coa1.purposeid = (select id from egf_accountcode_purpose where name = 'RETENTION_MONEY'))")
                        .append(" and pc.id in(select bpd1.accountdetailkeyid from eg_billpayeedetails bpd1,eg_billdetails bd1 where ")
                        .append(" bpd1.ACCOUNTDETAILTYPEID=(select id from accountdetailtype where name='PROJECTCODE')")
                        .append(" AND bd1.id = bpd1.billdetailid and bd1.debitamount>0 and bd1.billid=br.id)");

        whereClauseMap = formSearchConditionsQuery(index);
        whereClauseBfr = whereClauseMap.get("whereClause").toString();
        estimateStartQueryCondition = whereClauseMap.get("estimateQryCondition").toString();
        params = (List<Object>) whereClauseMap.get("params");
        paramList.addAll(params);

        query2.append(whereClauseBfr).append(estimateStartQueryCondition);

        if (retentionMoneyRefPeriod != null && retentionMoneyRefPeriod != -1) {
            final Date currentDate = new Date();
            final Long period = new Long(retentionMoneyRefPeriod) * new Long(24) * new Long(3600) * new Long(1000);
            final Date toDate = new Date(currentDate.getTime() + period.longValue());
            query2.append(" and br.billtype = ?").append(index++);
            paramList.add(WorksConstants.FINAL_BILL);
            query2.append(" and (woe.work_completion_date + (365*wo.defect_liability_period)) between ?")
                    .append(" and ?").append(index++);
            paramList.add(DateUtils.getFormattedDate(currentDate, "dd-MMM-yyyy"));
            paramList.add(DateUtils.getFormattedDate(toDate, "dd-MMM-yyyy"));
        }
        query2.append(")");

        final StringBuffer unionQuery = new StringBuffer("FROM (").append(query1).append(" UNION ").append(query2);
        final StringBuffer groupByQuery = new StringBuffer(
                " ) GROUP BY \"Bill Department\", \"Contractor Code\",\"Contractor Name\",")
                        .append("\"Bill Number\", \"Bill Type\", \"Bill Date\", \"Voucher Number\", \"Bill Amount\",")
                        .append(" \"Retention money recovered\", \"Refund Date\",\"PC Flag\" ");
        reportSubTitle = titleBuffer.append(whereClauseMap.get("title").toString()).toString();
        return unionQuery.append(groupByQuery).toString();
    }

    private Map<String, Object> formSearchConditionsQuery(int index) {

        final StringBuilder dynQuery = new StringBuilder(800);
        final StringBuffer titleBuffer = new StringBuffer();
        final Map<String, Object> searchCriteria = new HashMap<>();
        final List<Object> params = new ArrayList<>();

        if (StringUtils.isNotBlank(projectCode)) {
            dynQuery.append(" and pc.code like '%'||?").append(index++)
                    .append("||'%' ");
            params.add(projectCode);
            titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.title.projectcode")).append(projectCode);
        }

        if (StringUtils.isNotBlank(contractorCodeName)) {
            titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.title.contractorcodeorname"))
                    .append(contractorCodeName);
            final String[] contractorDetails = contractorCodeName.split("~");
            if (contractorDetails.length > 1) {
                dynQuery.append(" and (upper(cont.code) like '%'||?").append(index++)
                        .append("||'%' or upper(cont.name) like '%'||?").append(index++)
                        .append("||'%') ");
                params.add(contractorDetails[0].toUpperCase());
                params.add(contractorDetails[1].toUpperCase());
            } else {
                dynQuery.append(" and (upper(cont.code) like '%'||?").append(index++)
                        .append("||'%' or upper(cont.name) like '%'||?").append(index++)
                        .append("||'%') ");
                params.add(contractorDetails[0].toUpperCase());
                params.add(contractorDetails[0].toUpperCase());
            }
        }

        if (billDepartment != null && billDepartment != 0 && billDepartment != -1) {
            dynQuery.append(" and bmis.departmentid = ?").append(index++);
            params.add(billDepartment);
            final Department department = departmentService.getDepartmentById(billDepartment);
            titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.title.billdepartment"))
                    .append(department.getName());
        }

        if (billDateFrom != null) {
            dynQuery.append(" and br.billdate >= ?").append(index++);
            params.add(DateUtils.getFormattedDate(billDateFrom, "dd-MMM-yyyy"));
            // paramList.add(DateUtils.getFormattedDate(billDateFrom,"dd-MMM-yyyy"));
        }

        if (billDateTo != null) {
            dynQuery.append(" and br.billdate <= ?").append(index++);
            params.add(DateUtils.getFormattedDate(billDateTo, "dd-MMM-yyyy"));
            // paramList.add(DateUtils.getFormattedDate(billDateTo,"dd-MMM-yyyy"));
        }
        if (billDateFrom != null && billDateTo != null)
            titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.title.date"))
                    .append(DateUtils.getFormattedDate(billDateFrom, "dd/MM/yyyy")).append(" to ")
                    .append(DateUtils.getFormattedDate(billDateTo, "dd/MM/yyyy"));

        if (StringUtils.isNotBlank(billType)) {
            dynQuery.append(" and br.billtype = ?").append(index++);
            params.add(billType);
            titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.billType")).append(billType);
        }
        if (retentionMoneyAmountFrom != null && retentionMoneyAmountTo != null
                && !retentionMoneyAmountFrom.equals(BigDecimal.ZERO) && !retentionMoneyAmountTo.equals(BigDecimal.ZERO)) {
            dynQuery.append(" and bd.creditamount between ?").append(index++)
                    .append(" and ?").append(index++);
            params.add(retentionMoneyAmountFrom);
            params.add(retentionMoneyAmountTo);
            titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.report.retentionmoney.amount.range.lebel"))
                    .append(String.format("%.2f", retentionMoneyAmountFrom)).append(" - ")
                    .append(String.format("%.2f", retentionMoneyAmountTo));
        }

        if (retentionMoneyRefPeriod != null && retentionMoneyRefPeriod != -1) {
            titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.title.refundDueForPayable"));

            if (retentionMoneyRefPeriod == 30)
                titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.refundDueForPayable.1month"));
            if (retentionMoneyRefPeriod == 60)
                titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.refundDueForPayable.2month"));
            if (retentionMoneyRefPeriod == 90)
                titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.refundDueForPayable.3month"));
            if (retentionMoneyRefPeriod == 180)
                titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.refundDueForPayable.6month"));
            if (retentionMoneyRefPeriod == 365)
                titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.refundDueForPayable.1year"));
        }

        final StringBuffer estimateStartQueryCondition = new StringBuffer();
        if (StringUtils.isNotBlank(estimateNumber) || budgetHeads != null && !budgetHeads.isEmpty()
                || depositCOA != null && !depositCOA.isEmpty()) {
            estimateStartQueryCondition.append(" and pc.id in (select est.projectcode_id from egw_abstractestimate est")
                    .append(" where est.parentid is null and est.status_id = (select id from egw_status where code='ADMIN_SANCTIONED'")
                    .append(" and moduletype='AbstractEstimate') ");

            if (StringUtils.isNotBlank(estimateNumber)) {
                estimateStartQueryCondition.append(" and est.estimate_number like '%'||?").append(index++)
                        .append("||'%'");
                params.add(estimateNumber);
                titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.title.estimatenumber"))
                        .append(estimateNumber);
            }

            if (budgetHeads != null && !budgetHeads.isEmpty() && budgetHeads.get(0) != null && budgetHeads.get(0) != -1
                    && depositCOA != null && !depositCOA.isEmpty() && depositCOA.get(0) != null
                    && depositCOA.get(0) != -1) {
                estimateStartQueryCondition.append(" and est.id in (select abstractestimate_id from egw_financialdetail")
                        .append(" where ").append(getInSubQuery(new ArrayList<Object>(budgetHeads), " BUDGETGROUP_ID "));
                estimateStartQueryCondition.append(" or ").append(getInSubQuery(new ArrayList<Object>(depositCOA), " COA_ID "))
                        .append(")");
                titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.title.budgethead"))
                        .append(subHeaderBudgetHeads);
                titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.title.depositcoa"))
                        .append(subHeaderDepositCOA);
            } else if (budgetHeads != null && !budgetHeads.isEmpty() && budgetHeads.get(0) != null
                    && budgetHeads.get(0) != -1) {
                estimateStartQueryCondition.append(" and est.id in (select abstractestimate_id from egw_financialdetail")
                        .append(" where ").append(getInSubQuery(new ArrayList<Object>(budgetHeads), " BUDGETGROUP_ID "))
                        .append(")");
                titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.title.budgethead"))
                        .append(subHeaderBudgetHeads);
            } else if (depositCOA != null && !depositCOA.isEmpty() && depositCOA.get(0) != null
                    && depositCOA.get(0) != -1) {
                estimateStartQueryCondition.append(" and est.id in (select abstractestimate_id from egw_financialdetail")
                        .append(" where ").append(getInSubQuery(new ArrayList<Object>(depositCOA), " COA_ID "))
                        .append(")");
                titleBuffer.append(" ").append(getText("retentionMoneyRecoveryRegister.title.depositcoa"))
                        .append(subHeaderDepositCOA);
            }

            estimateStartQueryCondition.append(")");
        }

        searchCriteria.put("whereClause", dynQuery.toString());
        searchCriteria.put("params", params);
        searchCriteria.put("title", titleBuffer.toString());
        searchCriteria.put("estimateQryCondition", estimateStartQueryCondition);
        searchCriteria.put("index", index);
        return searchCriteria;
    }

    public String getInSubQuery(final List<Object> idList, final String param) {
        final StringBuffer inClause = new StringBuffer("");
        if (idList != null && idList.size() > 0 && param != null) {
            final int size = idList.size();
            inClause.append(" (").append(param).append(" in ( ");
            for (int i = 0; i < size; i++) {
                if (i % 1000 == 0 && i != 0)
                    inClause.append(") or ").append(param).append(" in (").append(idList.get(i).toString());
                else
                    inClause.append(idList.get(i).toString());
                if (i == size - 1)
                    inClause.append(")) ");
                else if (i % 1000 != 999)
                    inClause.append(",");
            }
        }
        return inClause.toString();
    }

    @SuppressWarnings("unchecked")
    private List<RetentionMoneyRecoveryRegisterBean> getReportData() {
        final String orderByClause = " ORDER BY \"Bill Date\"";
        final String reportQuery = getSelectQuery().append(getSearchQuery()).append(orderByClause).toString();
        final Query sqlQuery = entityManager.createNativeQuery(reportQuery);
        int count = 1;
        for (final Object param : paramList) {
            sqlQuery.setParameter(count, param);
            count++;
        }
        final List<Object[]> resultList = sqlQuery.getResultList();
        final List<RetentionMoneyRecoveryRegisterBean> reportList = setBeanValues(resultList);
        return reportList;
    }

    public String exportToPdf() {
        final Map<String, Object> reportParams = new HashMap<>();
        final List<RetentionMoneyRecoveryRegisterBean> reportData = getReportData();
        reportParams.put("reportSubTitle", reportSubTitle);
        final ReportRequest reportRequest = new ReportRequest("RetentionMoneyRecoveryRegister", reportData,
                reportParams);
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return EXPORTPDF;
    }

    public String exportToExcel() {
        final Map<String, Object> reportParams = new HashMap<>();
        final List<RetentionMoneyRecoveryRegisterBean> reportData = getReportData();
        reportParams.put("reportSubTitle", reportSubTitle);
        final ReportRequest reportRequest = new ReportRequest("RetentionMoneyRecoveryRegister", reportData,
                reportParams);
        reportRequest.setReportFormat(ReportFormat.XLS);
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            excelInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return EXPORTEXCEL;
    }

    public InputStream getPdfInputStream() {
        return pdfInputStream;
    }

    public InputStream getExcelInputStream() {
        return excelInputStream;
    }

    public Date getFromDate() {
        return billDateFrom;
    }

    public void setFromDate(final Date fromDate) {
        billDateFrom = fromDate;
    }

    public Date getToDate() {
        return billDateTo;
    }

    public void setToDate(final Date toDate) {
        billDateTo = toDate;
    }

    public String getReportSubTitle() {
        return reportSubTitle;
    }

    public void setReportSubTitle(final String reportSubTitle) {
        this.reportSubTitle = reportSubTitle;
    }

    public List<Object> getParamList() {
        return paramList;
    }

    public void setParamList(final List<Object> paramList) {
        this.paramList = paramList;
    }

    public Long getUserDept() {
        return billDepartment;
    }

    public void setUserDept(final Long userDept) {
        billDepartment = userDept;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public void setDepartmentService(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public List<Long> getDepositCOA() {
        return depositCOA;
    }

    public void setDepositCOA(final List<Long> depositCOA) {
        this.depositCOA = depositCOA;
    }

    public List<Long> getBudgetHeads() {
        return budgetHeads;
    }

    public void setBudgetHeads(final List<Long> budgetHeads) {
        this.budgetHeads = budgetHeads;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(final String projectCode) {
        this.projectCode = projectCode;
    }

    public String getContractorCodeName() {
        return contractorCodeName;
    }

    public void setContractorCodeName(final String contractorCodeName) {
        this.contractorCodeName = contractorCodeName;
    }

    public Date getBillDateFrom() {
        return billDateFrom;
    }

    public void setBillDateFrom(final Date billDateFrom) {
        this.billDateFrom = billDateFrom;
    }

    public Date getBillDateTo() {
        return billDateTo;
    }

    public void setBillDateTo(final Date billDateTo) {
        this.billDateTo = billDateTo;
    }

    public Long getBillDepartment() {
        return billDepartment;
    }

    public void setBillDepartment(final Long billDepartment) {
        this.billDepartment = billDepartment;
    }

    public List<Object> getParamListCountQuery() {
        return paramListCountQuery;
    }

    public void setParamListCountQuery(final List<Object> paramListCountQuery) {
        this.paramListCountQuery = paramListCountQuery;
    }

    public String getSubHeaderBudgetHeads() {
        return subHeaderBudgetHeads;
    }

    public void setSubHeaderBudgetHeads(final String subHeaderBudgetHeads) {
        this.subHeaderBudgetHeads = subHeaderBudgetHeads;
    }

    public String getSubHeaderDepositCOA() {
        return subHeaderDepositCOA;
    }

    public void setSubHeaderDepositCOA(final String subHeaderDepositCOA) {
        this.subHeaderDepositCOA = subHeaderDepositCOA;
    }

    public BigDecimal getRetentionMoneyAmountFrom() {
        return retentionMoneyAmountFrom;
    }

    public void setRetentionMoneyAmountFrom(final BigDecimal retentionMoneyAmountFrom) {
        this.retentionMoneyAmountFrom = retentionMoneyAmountFrom;
    }

    public BigDecimal getRetentionMoneyAmountTo() {
        return retentionMoneyAmountTo;
    }

    public void setRetentionMoneyAmountTo(final BigDecimal retentionMoneyAmountTo) {
        this.retentionMoneyAmountTo = retentionMoneyAmountTo;
    }

    public void setContractorBillService(final ContractorBillService contractorBillService) {
        this.contractorBillService = contractorBillService;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(final String billType) {
        this.billType = billType;
    }

    public Integer getRetentionMoneyRefPeriod() {
        return retentionMoneyRefPeriod;
    }

    public void setRetentionMoneyRefPeriod(final Integer retentionMoneyRefPeriod) {
        this.retentionMoneyRefPeriod = retentionMoneyRefPeriod;
    }

}