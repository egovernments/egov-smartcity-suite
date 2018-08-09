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
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgModules;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.egf.model.VoucherReportView;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.persistence.utils.Page;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infra.workflow.entity.State;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.payment.Paymentheader;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.egov.utils.VoucherHelper;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Results(value = {
        @Result(name = "PDF", type = "stream", location = "inputStream", params = {"inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=VoucherStatusReport.pdf"}),
        @Result(name = "search", location = "voucherStatusReport-search.jsp"),
        @Result(name = "XLS", type = "stream", location = "inputStream", params = {"inputName", "inputStream", "contentType",
                "application/xls", "contentDisposition", "no-cache;filename=VoucherStatusReport.xls"})
})
@ParentPackage("egov")
public class VoucherStatusReportAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private static final String JASPERPATH = "/reports/templates/voucherStatusReport.jasper";
    private static final String SCHEME = "scheme";
    private static final String SCHEME_LIST = "schemeList";
    private static final String SUBSCHEME = "subscheme";
    private final List<String> headerFields = new ArrayList<>();
    private final List<String> mandatoryFields = new ArrayList<>();
    private final transient Map<String, Object> paramMap = new HashMap<>();
    private transient CVoucherHeader voucherHeader = new CVoucherHeader();
    private Date fromDate = new Date();
    private Date toDate = null;
    private transient InputStream inputStream;
    private transient ReportHelper reportHelper;
    private transient List<Object> voucherReportList = new ArrayList<>();
    private transient List<String> voucherTypes = VoucherHelper.VOUCHER_TYPES;
    private transient Map<String, List<String>> voucherNames = VoucherHelper.VOUCHER_TYPE_NAMES;
    private transient Map<Integer, String> statusMap;
    private transient Map<String, String> nameMap;
    private Integer page = 1;
    private Integer pageSize = 30;
    private transient EgovPaginatedList pagedResults;
    private String countQry;
    private String modeOfPayment;
    @Autowired
    private transient AppConfigValueService appConfigValueService;
    @Autowired
    private transient FinancialYearDAO financialYearDAO;

    public VoucherStatusReportAction() {
        voucherHeader.setVouchermis(new Vouchermis());
        addRelatedEntity("vouchermis.departmentid", Department.class);
        addRelatedEntity("fundId", Fund.class);
        addRelatedEntity("vouchermis.schemeid", Scheme.class);
        addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
        addRelatedEntity("vouchermis.functionary", Functionary.class);
        addRelatedEntity("vouchermis.divisionid", Boundary.class);
        addRelatedEntity("fundsourceId", Fundsource.class);

    }

    @Override
    public Object getModel() {
        return voucherHeader;
    }

    public void finYearDate() {

        final String financialYearId = financialYearDAO.getCurrYearFiscalId();
        if (isBlank(financialYearId))
            fromDate = new Date();
        else
            fromDate = (Date) persistenceService.find("select startingDate  from CFinancialYear where id=?",
                    Long.parseLong(financialYearId));
        toDate = null;

    }

    @Override
    public void prepare() {
        super.prepare();
        getHeaderFields();
        loadDropDowns();
        statusMap = new HashMap<>();
        statusMap.put(FinancialConstants.CREATEDVOUCHERSTATUS, "Approved");
        statusMap.put(FinancialConstants.REVERSEDVOUCHERSTATUS, "Reversed");
        statusMap.put(FinancialConstants.REVERSALVOUCHERSTATUS, "Reversal");
        statusMap.put(FinancialConstants.CANCELLEDVOUCHERSTATUS, "Cancelled");
        statusMap.put(FinancialConstants.PREAPPROVEDVOUCHERSTATUS, "Preapproved");
    }

    private void loadDropDowns() {

        if (headerFields.contains("department"))
            addDropdownData("departmentList", persistenceService.findAllBy("from Department order by name"));
        if (headerFields.contains("functionary"))
            addDropdownData("functionaryList", persistenceService.findAllBy(" from Functionary where isactive=true order by name"));
        if (headerFields.contains("fund"))
            addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
        if (headerFields.contains("fundsource"))
            addDropdownData("fundsourceList",
                    persistenceService.findAllBy(" from Fundsource where isactive=true order by name"));
        if (headerFields.contains("field"))
            addDropdownData("fieldList",
                    persistenceService.findAllBy(" from Boundary b where lower(b.boundaryType.name)='ward' "));
        if (headerFields.contains(SCHEME))
            addDropdownData(SCHEME_LIST, Collections.emptyList());
        if (headerFields.contains(SUBSCHEME))
            addDropdownData("subschemeList", Collections.emptyList());
        addDropdownData("typeList", VoucherHelper.VOUCHER_TYPES);
        addDropdownData("modeOfPaymentList", persistenceService.findAllBy(" select DISTINCT upper(type) from Paymentheader "));
        nameMap = new LinkedHashMap<>();
    }

    protected void getHeaderFields() {
        final List<AppConfigValues> appConfigList = appConfigValueService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "DEFAULT_SEARCH_MISATTRRIBUTES");

        for (final AppConfigValues appConfigVal : appConfigList) {
            final String value = appConfigVal.getValue();
            final String header = value.substring(0, value.indexOf('|'));
            headerFields.add(header);
            final String mandate = value.substring(value.indexOf('|') + 1);
            if (mandate.equalsIgnoreCase("M"))
                mandatoryFields.add(header);
        }

    }

    public boolean shouldShowHeaderField(final String field) {
        return headerFields.contains(field);
    }

    public Map<Integer, String> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(final Map<Integer, String> statusMap) {
        this.statusMap = statusMap;
    }

    @SkipValidation
    @Action(value = "/report/voucherStatusReport-beforeSearch")
    public String beforeSearch() {
        voucherHeader.reset();
        finYearDate();
        return "search";
    }


    @ReadOnly
    @ValidationErrorPage(value = "search")
    @Action(value = "/report/voucherStatusReport-search")
    public String search() throws ApplicationException {
        List<Map<String, Object>> voucherList = new ArrayList<>();
        Map<String, Object> voucherMap;
        HashMap<Long, String> voucherIDOwnerNameMap = new HashMap<>();
        Long voucherHeaderId;
        String voucherOwner;
        final Query qry = voucherSearchQuery();
        final Long count = (Long) persistenceService.find(countQry);
        final Page resPage = new Page(qry, page, pageSize);
        pagedResults = new EgovPaginatedList(resPage, count.intValue());
        final List<CVoucherHeader> list = pagedResults.getList();
        for (final CVoucherHeader voucherheader : list) {
            voucherMap = new HashMap<>();
            Double amt = Double.valueOf(0);
            voucherHeaderId = voucherheader.getId();
            voucherMap.put("id", voucherHeaderId);
            voucherMap.put("vouchernumber", voucherheader.getVoucherNumber());
            voucherMap.put("type", voucherheader.getType());
            voucherMap.put("name", voucherheader.getName());
            voucherMap.put("voucherdate", voucherheader.getVoucherDate());
            voucherMap.put("deptName", voucherheader.getVouchermis().getDepartmentid().getName());
            for (final CGeneralLedger detail : voucherheader.getGeneralledger())
                amt = amt + detail.getDebitAmount();
            voucherMap.put("amount", amt);
            voucherMap.put("status", getVoucherStatus(voucherheader.getStatus()));
            voucherMap.put("source", getVoucherModule(voucherheader.getModuleId()));
            voucherOwner = getVoucherOwner(voucherheader);
            voucherMap.put("owner", voucherOwner);
            voucherIDOwnerNameMap.put(voucherHeaderId, voucherOwner);
            voucherList.add(voucherMap);
        }
        loadAjaxedData();
        pagedResults.setList(voucherList);
        return "search";
    }

    private void loadAjaxedData() {
        getVoucherNameMap(voucherHeader.getType());
        if (headerFields.contains(SCHEME))
            if (voucherHeader.getFundId() != null && voucherHeader.getFundId().getId() != -1) {
                final StringBuilder st = new StringBuilder(50);
                st.append("from Scheme where isactive=true and fund.id=");
                st.append(voucherHeader.getFundId().getId());
                dropdownData.put(SCHEME_LIST, persistenceService.findAllBy(st.toString()));
                st.delete(0, st.length() - 1);

            } else
                dropdownData.put(SCHEME_LIST, Collections.emptyList());
        if (headerFields.contains(SUBSCHEME))
            if (voucherHeader.getVouchermis() != null
                    && voucherHeader.getVouchermis().getSchemeid() != null
                    && voucherHeader.getVouchermis().getSchemeid().getId() != -1)
                dropdownData.put("subSchemeList", persistenceService.findAllBy(
                        "from SubScheme where isactive=true and scheme.id=?",
                        voucherHeader.getVouchermis().getSchemeid().getId()));
            else
                dropdownData.put("subSchemeList", Collections.emptyList());
    }

    public Map<String, String> getVoucherNameMap(final String type) {
        final List<Object> voucherNameList = persistenceService.findAllBy(
                "select  distinct name from  CVoucherHeader where type=?", type);
        nameMap = new LinkedHashMap<>();

        for (final Object voucherName : voucherNameList)
            nameMap.put((String) voucherName, (String) voucherName);
        return nameMap;
    }

    private Query voucherSearchQuery() {
        StringBuilder sql = new StringBuilder(500);

        if (modeOfPayment.equals("-1"))
            sql.append(" from CVoucherHeader vh where ");
        else
            sql.append(" from CVoucherHeader vh,Paymentheader ph where vh.id = ph.voucherheader.id and");

        if (voucherHeader.getFundId() != null && voucherHeader.getFundId().getId() != -1)
            sql.append("  vh.fundId=").append(voucherHeader.getFundId().getId());

        if (voucherHeader.getType() != null && !voucherHeader.getType().equals("-1"))
            sql.append(" and vh.type='").append(voucherHeader.getType()).append('\'');
        if (voucherHeader.getName() != null && !voucherHeader.getName().equalsIgnoreCase("-1")
                && !voucherHeader.getName().equalsIgnoreCase("0"))
            sql.append(" and vh.name='").append(voucherHeader.getName()).append('\'');
        if (fromDate != null)
            sql.append(" and vh.voucherDate>='").append(Constants.DDMMYYYYFORMAT1.format(fromDate)).append('\'');
        if (toDate != null)
            sql.append(" and vh.voucherDate<='").append(Constants.DDMMYYYYFORMAT1.format(toDate)).append('\'');
        if (voucherHeader.getStatus() != -1)
            sql.append(" and vh.status=").append(voucherHeader.getStatus());

        if (voucherHeader.getVouchermis().getDepartmentid() != null
                && voucherHeader.getVouchermis().getDepartmentid().getId() != -1)
            sql.append(" and vh.vouchermis.departmentid=").append(voucherHeader.getVouchermis().getDepartmentid().getId());

        if (voucherHeader.getVouchermis().getSchemeid() != null)
            sql.append(" and vh.vouchermis.schemeid=").append(voucherHeader.getVouchermis().getSchemeid().getId());
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            sql.append(" and vh.vouchermis.subschemeid=").append(voucherHeader.getVouchermis().getSubschemeid().getId());
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            sql.append(" and vh.vouchermis.functionary=").append(voucherHeader.getVouchermis().getFunctionary().getId());
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            sql.append(" and vh.vouchermis.divisionid=").append(voucherHeader.getVouchermis().getDivisionid().getId());
        if (!modeOfPayment.equals("-1"))
            sql.append(" and upper(ph.type) ='").append(getModeOfPayment()).append('\'');
        countQry = "select count(*) " + sql;
        return persistenceService.getSession()
                .createQuery(new StringBuilder().append("select vh ").append(sql)
                        .append(" order by vh.vouchermis.departmentid.name ,vh.voucherDate, vh.voucherNumber").toString());
    }

    private String getVoucherModule(final Integer vchrModuleId) throws ApplicationException {
        if (vchrModuleId == null)
            return "Internal";
        else {
            EgModules egModuleObj = (EgModules) persistenceService.find("from EgModules m where m.id=?", vchrModuleId);
            if (egModuleObj == null)
                throw new ApplicationException("INCORRECT MODULE ID");
            else
                return egModuleObj.getName();
        }
    }

    @Override
    public void validate() {
        if (fromDate == null)
            addFieldError("From Date", getText("Please enter From Date"));
        if (toDate == null)
            addFieldError("To Date", getText("Please enter To Date"));
        checkMandatoryField("fundId", "fund", voucherHeader.getFundId(), "voucher.fund.mandatory");
        checkMandatoryField("vouchermis.departmentid", "department", voucherHeader.getVouchermis().getDepartmentid(),
                "voucher.department.mandatory");
        checkMandatoryField("vouchermis.schemeid", SCHEME, voucherHeader.getVouchermis().getSchemeid(),
                "voucher.scheme.mandatory");
        checkMandatoryField("vouchermis.subschemeid", SUBSCHEME, voucherHeader.getVouchermis().getSubschemeid(),
                "voucher.subscheme.mandatory");
        checkMandatoryField("vouchermis.functionary", "functionary", voucherHeader.getVouchermis().getFunctionary(),
                "voucher.functionary.mandatory");
        checkMandatoryField("fundsourceId", "fundsource", voucherHeader.getVouchermis().getFundsource(),
                "voucher.fundsource.mandatory");
        checkMandatoryField("vouchermis.divisionId", "field", voucherHeader.getVouchermis().getDivisionid(),
                "voucher.field.mandatory");
    }

    private void populateData() throws ApplicationException {
        final List<CVoucherHeader> list = new ArrayList();
        list.addAll(voucherSearchQuery().list());
        BigDecimal amt = BigDecimal.ZERO;
        for (final CVoucherHeader cVchrHdr : list) {
            final VoucherReportView vhcrRptView = new VoucherReportView();
            vhcrRptView.setDeptName(cVchrHdr.getVouchermis().getDepartmentid().getName());
            vhcrRptView.setVoucherNumber(cVchrHdr.getVoucherNumber());
            vhcrRptView.setVoucherType(cVchrHdr.getType());
            vhcrRptView.setVoucherName(cVchrHdr.getName());
            vhcrRptView.setVoucherDate(cVchrHdr.getVoucherDate());
            vhcrRptView.setSource(getVoucherModule(cVchrHdr.getModuleId()));
            for (final CGeneralLedger detail : cVchrHdr.getGeneralledger())
                amt = amt.add(BigDecimal.valueOf(detail.getDebitAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
            vhcrRptView.setAmount(amt);
            vhcrRptView.setOwner(getVoucherOwner(cVchrHdr));
            vhcrRptView.setStatus(getVoucherStatus(cVchrHdr.getStatus()));
            voucherReportList.add(vhcrRptView);
            amt = BigDecimal.ZERO;
        }

        setParamMap();
    }

    protected void checkMandatoryField(final String objectName, final String fieldName, final Object value, final String errorKey) {
        if (mandatoryFields.contains(fieldName) && (value == null || value.equals(-1)))
            addFieldError(objectName, getText(errorKey));
    }

    public boolean isFieldMandatory(final String field) {
        return mandatoryFields.contains(field);
    }

    private String getVoucherStatus(final int status) {
        if (FinancialConstants.CREATEDVOUCHERSTATUS.equals(status))
            return "Approved";
        if (FinancialConstants.REVERSEDVOUCHERSTATUS.equals(status))
            return "Reversed";
        if (FinancialConstants.REVERSALVOUCHERSTATUS.equals(status))
            return "Reversal";
        if (FinancialConstants.CANCELLEDVOUCHERSTATUS.equals(status))
            return "Cancelled";
        if (FinancialConstants.PREAPPROVEDVOUCHERSTATUS.equals(status))
            return "Preapproved";
        return "";
    }

    private String getVoucherOwner(final CVoucherHeader voucherHeader) {
        final String dash = "-";
        final Integer voucherStatus = voucherHeader.getStatus();
        final String voucherType = voucherHeader.getType();
        State voucherState;
        if (voucherStatus.longValue() == FinancialConstants.CANCELLEDVOUCHERSTATUS.longValue()
                || voucherStatus.longValue() == FinancialConstants.CREATEDVOUCHERSTATUS.longValue())
            return dash;
        else if (voucherType.equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA)) {
            final ContraJournalVoucher contraJV = (ContraJournalVoucher) persistenceService.find(
                    "from ContraJournalVoucher cj where cj.voucherHeaderId=?", voucherHeader);
            if (contraJV == null)
                return dash;
            else
                voucherState = contraJV.getState();
            if (voucherState == null)
                return dash;
            else if (voucherState.getValue().equals("END"))
                return dash;
            else
                return getUserNameForPosition(voucherState.getOwnerPosition().getId().intValue());
        } else if (voucherType.equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL)) {
            voucherState = voucherHeader.getState();
            if (voucherState == null)
                return dash;
            else if (voucherState.getValue().equals("END"))
                return dash;
            else
                return getUserNameForPosition(voucherState.getOwnerPosition().getId().intValue());
        } else if (voucherType.equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT)) {
            final Paymentheader paymentHeader = (Paymentheader) persistenceService.find(
                    "from Paymentheader ph where ph.voucherheader=?", voucherHeader);
            if (paymentHeader == null)
                return dash;
            else
                voucherState = paymentHeader.getState();
            if (voucherState == null)
                return dash;
            else if (voucherState.getValue().equals("END"))
                return dash;
            else
                return getUserNameForPosition(voucherState.getOwnerPosition().getId().intValue());
        } else
            return dash;
    }

    private String getUserNameForPosition(final Integer posId) {
        final String query = "select emp.userName  from org.egov.eis.entity.EmployeeView emp where emp.position.id = ? ";
        return (String) persistenceService.find(query, posId.longValue());
    }

    public void setParamMap() {
        paramMap.put("fund", voucherHeader.getFundId().getName());
        if (voucherHeader.getVouchermis() != null && voucherHeader.getVouchermis().getDepartmentid() != null)
            paramMap.put("deptName", voucherHeader.getVouchermis().getDepartmentid().getName());
        paramMap.put("status", getVoucherStatus(voucherHeader.getStatus()));
        paramMap.put("toDate", toDate);
        paramMap.put("fromDate", fromDate);
        paramMap.put("voucherName", voucherHeader.getName());
        paramMap.put("voucherType", voucherHeader.getType());
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    @SkipValidation
    @Action(value = "/report/voucherStatusReport-generatePdf")
    public String generatePdf() throws IOException, JRException, ApplicationException {
        populateData();
        inputStream = reportHelper.exportPdf(inputStream, JASPERPATH, getParamMap(), voucherReportList);
        return "PDF";
    }

    @SkipValidation
    @Action(value = "/report/voucherStatusReport-generateXls")
    public String generateXls() throws IOException, JRException, ApplicationException {
        populateData();
        inputStream = reportHelper.exportXls(inputStream, JASPERPATH, getParamMap(), voucherReportList);
        return "XLS";
    }

    protected Map<String, Object> getParamMap() {
        return paramMap;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(final Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public EgovPaginatedList getPagedResults() {
        return pagedResults;
    }

    public void setPagedResults(final EgovPaginatedList pagedResults) {
        this.pagedResults = pagedResults;
    }

    public Map<String, String> getNameMap() {
        return nameMap;
    }

    public void setNameMap(final Map<String, String> nameMap) {
        this.nameMap = nameMap;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(final Integer page) {
        this.page = page;
    }

    public List<String> getVoucherTypes() {
        return voucherTypes;
    }

    public void setVoucherTypes(final List<String> voucherTypes) {
        this.voucherTypes = voucherTypes;
    }

    public Map<String, List<String>> getVoucherNames() {
        return voucherNames;
    }

    public void setVoucherNames(final Map<String, List<String>> voucherNames) {
        this.voucherNames = voucherNames;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(final String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public FinancialYearDAO getFinancialYearDAO() {
        return financialYearDAO;
    }

    public void setFinancialYearDAO(FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

}