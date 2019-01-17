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

import com.exilant.GLEngine.GeneralLedgerBean;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFunction;
import org.egov.commons.repository.FunctionRepository;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@ParentPackage("egov")
@Results({
        @Result(name = "result", location = "journalBookReport-result.jsp"),
        @Result(name = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH, location = "journalBookReport-"
                + FinancialConstants.STRUTS_RESULT_PAGE_SEARCH + ".jsp")
})
public class JournalBookReportAction extends BaseFormAction {

    private static final long serialVersionUID = -7540296344209825345L;
    private static final Logger LOGGER = Logger.getLogger(JournalBookReportAction.class);
    private GeneralLedgerBean journalBookReport = new GeneralLedgerBean();
    protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private List<GeneralLedgerBean> journalBookDisplayList = new ArrayList<GeneralLedgerBean>();

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private FunctionRepository functionRepository;
    String heading = "";

    public JournalBookReportAction() {
        super();
    }

    @Override
    public Object getModel() {
        return journalBookReport;
    }

    public void prepareNewForm() {
        super.prepare();
        addDropdownData("fundList",
                persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
        addDropdownData("fundsourceList",
                persistenceService.findAllBy(" from Fundsource where isactive=true order by name"));
        addDropdownData("departmentList", persistenceService.findAllBy("from Department order by name"));
        addDropdownData("functionList", functionRepository.findByIsActiveAndIsNotLeaf(true,false));

        addDropdownData("voucherNameList", VoucherHelper.VOUCHER_TYPE_NAMES.get(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside  Prepare ........");

    }

    @SkipValidation
    @Action(value = "/report/journalBookReport-newForm")
    public String newForm() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("..Inside NewForm method..");
        return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
    }

    @Validations(requiredFields = {
            @RequiredFieldValidator(fieldName = "fund_id", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "startDate", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "endDate", message = "", key = FinancialConstants.REQUIRED) })
    @ValidationErrorPage(value = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
    @SkipValidation
    @Action(value = "/report/journalBookReport-ajaxSearch")
    @ReadOnly
    public String ajaxSearch() throws TaskFailedException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("JournalBookAction | Search | start");
        journalBookReport.setUlbName(ReportUtil.getCityName());
        prepareResultList();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("JournalBookAction | list | End");
        if (journalBookDisplayList.isEmpty() && journalBookDisplayList == null)
        {
            addActionMessage("No records found.");
        }
        heading = getGLHeading();
        prepareNewForm();
        return "result";
    }

    private void prepareResultList() {
        final Map.Entry<String, Map<String, Object>> queryMapEntry = getQuery().entrySet().iterator().next();
        String voucherDate = "", voucherNumber = "", voucherName = "", narration = "";
        final Query query = persistenceService.getSession().createNativeQuery(queryMapEntry.getKey())
                .addScalar("voucherdate", StringType.INSTANCE)
                .addScalar("vouchernumber", StringType.INSTANCE)
                .addScalar("code", StringType.INSTANCE)
                .addScalar("accName", StringType.INSTANCE)
                .addScalar("narration", StringType.INSTANCE)
                .addScalar("debitamount", StringType.INSTANCE)
                .addScalar("creditamount", StringType.INSTANCE)
                .addScalar("voucherName", StringType.INSTANCE)
                .addScalar("vhId", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(GeneralLedgerBean.class));
        queryMapEntry.getValue().entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        journalBookDisplayList = query.list();
        for (GeneralLedgerBean bean : journalBookDisplayList) {
            bean.setDebitamount(new BigDecimal(bean.getDebitamount()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            bean.setCreditamount(new BigDecimal(bean.getCreditamount()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            if (voucherDate != null && !voucherDate.equalsIgnoreCase("") && voucherDate.equalsIgnoreCase(bean.getVoucherdate())
                    && voucherNumber.equalsIgnoreCase(bean.getVouchernumber())) {
                bean.setVoucherdate("");
            } else {
                voucherDate = bean.getVoucherdate();
            }
            if (voucherName != null && !voucherName.equalsIgnoreCase("") && voucherName.equalsIgnoreCase(bean.getVoucherName())
                    && voucherNumber.equalsIgnoreCase(bean.getVouchernumber())) {
                bean.setVoucherName("");
            } else {
                voucherName = bean.getVoucherName();
            }
            if (voucherNumber != null && !voucherNumber.equalsIgnoreCase("")
                    && voucherNumber.equalsIgnoreCase(bean.getVouchernumber())) {
                bean.setVouchernumber("");
            } else {
                voucherNumber = bean.getVouchernumber();
            }

            if (narration != null && !narration.equalsIgnoreCase("") && narration.equalsIgnoreCase(bean.getNarration())) {
                bean.setNarration("");
            } else {
                narration = bean.getNarration();
            }

        }
    }

    private Map<String, Map<String, Object>> getQuery() {
        final Map<String, Map<String, Object>> queryMap = new HashMap<>();
        final Map<String, Object> params = new HashMap<>();
        Date startDate = null;
        Date endDate = null;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            startDate = sdf.parse(journalBookReport.getStartDate());
            endDate = sdf.parse(journalBookReport.getEndDate());
        } catch (ParseException e) {

        }
        StringBuilder subQuery = new StringBuilder();
        if (journalBookReport.getFund_id() != null && !journalBookReport.getFund_id().equals("")) {
            subQuery.append(" and f.id=:fundId ");
            params.put("fundId", Long.valueOf(journalBookReport.getFund_id()));
        }
        if (journalBookReport.getVoucher_name() != null && !journalBookReport.getVoucher_name().equals("")) {
            subQuery.append(" and vh.Name=:voucherName ");
            params.put("voucherName", journalBookReport.getVoucher_name());
        }
        if (journalBookReport.getDept_name() != null && !journalBookReport.getDept_name().equals("")) {
            subQuery.append(" and vmis.departmentid=:departmentName");
            params.put("departmentName", journalBookReport.getDept_name());
        }
        if (journalBookReport.getFunctionId() != null && !journalBookReport.getFunctionId().equals("")) {
            subQuery.append(" and vmis.functionid =:functionId");
            params.put("functionId", journalBookReport.getFunctionId());
        }

        StringBuilder query = new StringBuilder("SELECT TO_CHAR(vh.voucherdate,'dd-Mon-yyyy') AS voucherdate,vh.vouchernumber AS vouchernumber,f.name AS fund, ")
                            .append(" gl.glcode AS code,coa.name AS accName,vh.description AS narration,vh.isconfirmed AS isconfirmed, ")
                .append("gl.debitamount AS debitamount, gl.creditamount AS creditamount,vh.name AS voucherName,vh.id AS vhId ")
                .append(" FROM voucherheader vh, generalledger gl,fund f,function fn ,vouchermis vmis,chartofaccounts coa")
                .append(" WHERE vh.id = gl.voucherheaderid AND gl.glcodeid = coa.id AND vh.fundid = f.id")
                .append(" AND vmis.functionid = fn.id AND vmis.voucherheaderid=vh.id AND vh.status NOT IN (4,5)")
                .append(subQuery.toString())
                .append(" and vh.voucherdate >=:startDate ")
                .append(" and vh.voucherdate<=:endDate ");
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        queryMap.put(query.toString(), params);
        return queryMap;
    }

    private String getGLHeading() {

        String heading = "";
        heading = "Journal Book Report under " + journalBookReport.getFundName() + " from " + journalBookReport.getStartDate()
                + " to " + journalBookReport.getEndDate();
        Department dept = new Department();
        CFunction function = new CFunction();
        if (checkNullandEmpty(journalBookReport.getDept_name())) {
            dept = (Department) persistenceService.find("from Department where  id = ?1",
                    Long.parseLong(journalBookReport.getDept_name()));
            heading = heading + " and Department : " + dept.getName();
        }
        if (checkNullandEmpty(journalBookReport.getFunctionId())) {
            function = (CFunction) persistenceService.find("from CFunction where  id = ?1",
                    Long.parseLong(journalBookReport.getFunctionId()));
            heading = heading + " and Financing Source :" + function.getName();
        }
        if (checkNullandEmpty(journalBookReport.getVoucher_name()))
            heading = heading + " and Voucher Type Name :" + journalBookReport.getVoucher_name();
        return heading;
    }

    private boolean checkNullandEmpty(final String column)
    {
        if (column != null && !column.isEmpty())
            return true;
        else
            return false;

    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(final String heading) {
        this.heading = heading;
    }

    public GeneralLedgerBean getJournalBookReport() {
        return journalBookReport;
    }

    public void setJournalBookReport(final GeneralLedgerBean journalBookReport) {
        this.journalBookReport = journalBookReport;
    }

    public List<GeneralLedgerBean> getJournalBookDisplayList() {
        return journalBookDisplayList;
    }

    public void setJournalBookDisplayList(List<GeneralLedgerBean> journalBookDisplayList) {
        this.journalBookDisplayList = journalBookDisplayList;
    }

}