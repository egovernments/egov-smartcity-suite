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


import com.opensymphony.xwork2.validator.annotations.Validation;

import org.apache.log4j.Logger;
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
import org.egov.egf.web.actions.voucher.VoucherSearchAction;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.persistence.utils.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.payment.Paymentheader;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.egov.utils.VoucherHelper;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Results(value = {
        @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=VoucherStatusReport.pdf" }),
        @Result(name = "search", location = "voucherStatusReport-search.jsp"),
        @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/xls", "contentDisposition", "no-cache;filename=VoucherStatusReport.xls" })
})
@ParentPackage("egov")
@Validation
public class VoucherStatusReportAction extends BaseFormAction
{
    private static final Logger LOGGER = Logger.getLogger(VoucherSearchAction.class);
    public List<Map<String, Object>> voucherList;
    private static final long serialVersionUID = 1L;
    public CVoucherHeader voucherHeader = new CVoucherHeader();
    public final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE);
    public Date fromDate = new Date();
    public Date toDate = null;
    private final List<String> headerFields = new ArrayList<String>();
    private final List<String> mandatoryFields = new ArrayList<String>();
    InputStream inputStream;
    private static final String JASPERPATH = "/reports/templates/voucherStatusReport.jasper";
    ReportHelper reportHelper;
    List<Object> voucherReportList = new ArrayList<Object>();
    List<CVoucherHeader> voucherDisplayList = new ArrayList<CVoucherHeader>();
    private Map<Integer, String> statusMap;
    private final Map<String, Object> paramMap = new HashMap<String, Object>();
    private Map<String, String> nameMap;
    private HashMap<Long, String> voucherIDOwnerNameMap;
    private Integer page = 1;
    private Integer pageSize = 30;
    private EgovPaginatedList pagedResults;
    private String countQry;
    private String modeOfPayment;
   @Autowired 
    private AppConfigValueService appConfigValueService;
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private FinancialYearDAO financialYearDAO;

    List<String> voucherTypes = VoucherHelper.VOUCHER_TYPES;
    Map<String, List<String>> voucherNames = VoucherHelper.VOUCHER_TYPE_NAMES;

    @Override
    public Object getModel() {
        return voucherHeader;
    }

    public VoucherStatusReportAction()
    {
        voucherHeader.setVouchermis(new Vouchermis());
        addRelatedEntity("vouchermis.departmentid", Department.class);
        addRelatedEntity("fundId", Fund.class);
        addRelatedEntity("vouchermis.schemeid", Scheme.class);
        addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
        addRelatedEntity("vouchermis.functionary", Functionary.class);
        addRelatedEntity("vouchermis.divisionid", Boundary.class);
        addRelatedEntity("fundsourceId", Fundsource.class);

    }

    public void finYearDate() {

        final String financialYearId = financialYearDAO.getCurrYearFiscalId();
        if (financialYearId == null || financialYearId.equals(""))
            fromDate = new Date();
        else
            fromDate = (Date) persistenceService.find("select startingDate  from CFinancialYear where id=?",
                    Long.parseLong(financialYearId));
        toDate = null;

    }

    @Override
    public void prepare()
    {
        super.prepare();
        getHeaderFields();
        loadDropDowns();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of  MIS attributes are :" + headerFields.size());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of mandate MIS attributes are :" + mandatoryFields.size());
        statusMap = new HashMap<Integer, String>();
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
        if (headerFields.contains("scheme"))
            addDropdownData("schemeList", Collections.EMPTY_LIST);
        if (headerFields.contains("subscheme"))
            addDropdownData("subschemeList", Collections.EMPTY_LIST);
        // addDropdownData("typeList",
        // persistenceService.findAllBy(" select distinct vh.type from CVoucherHeader vh  order by vh.type")); //where
        // vh.status!=4
        addDropdownData("typeList", VoucherHelper.VOUCHER_TYPES);
        addDropdownData("modeOfPaymentList", persistenceService.findAllBy(" select DISTINCT upper(type) from Paymentheader "));
        nameMap = new LinkedHashMap<String, String>();
    }

    protected void getHeaderFields()
    {
        final List<AppConfigValues> appConfigList = appConfigValueService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "DEFAULT_SEARCH_MISATTRRIBUTES");

            for (final AppConfigValues appConfigVal : appConfigList)
            {
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
    public String beforeSearch()
    {
        voucherHeader.reset();
        finYearDate();

        /*
         * try { Thread.sleep(30000); } catch (InterruptedException e) {   }
         */
        //
        // LOGGER.errorpersistenceService.getSession().getFlushMode());
        return "search";
    }


    @ReadOnly
    @ValidationErrorPage(value = "search")
    @Action(value = "/report/voucherStatusReport-search")
    public String search() throws ApplicationException, ParseException
    {
        voucherList = new ArrayList<Map<String, Object>>();
        Map<String, Object> voucherMap = null;
        voucherIDOwnerNameMap = new HashMap<Long, String>();
        Long voucherHeaderId;
        String voucherOwner;
        final Query qry = voucherSearchQuery();
        final Long count = (Long) persistenceService.find(countQry);
        final Page resPage = new Page(qry, page, pageSize);
        pagedResults = new EgovPaginatedList(resPage, count.intValue());
        final List<CVoucherHeader> list = pagedResults != null ? pagedResults.getList() : null;
        for (final CVoucherHeader voucherheader : list)
        {
            voucherMap = new HashMap<String, Object>();
            Double amt = new Double(0);
            voucherHeaderId = voucherheader.getId();
            voucherMap.put("id", voucherHeaderId);
            voucherMap.put("vouchernumber", voucherheader.getVoucherNumber());
            voucherMap.put("type", voucherheader.getType());
            voucherMap.put("name", voucherheader.getName());
            voucherMap.put("voucherdate", voucherheader.getVoucherDate());
            voucherMap.put("deptName", voucherheader.getVouchermis().getDepartmentid().getName());
            for (final CGeneralLedger detail : voucherheader.getGeneralledger())
                amt = amt+detail.getDebitAmount();
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
        if (headerFields.contains("scheme"))
            if (voucherHeader.getFundId() != null && voucherHeader.getFundId().getId() != -1) {
                final StringBuffer st = new StringBuffer();
                st.append("from Scheme where isactive=true and fund.id=");
                st.append(voucherHeader.getFundId().getId());
                dropdownData.put("schemeList", persistenceService.findAllBy(st.toString()));
                st.delete(0, st.length() - 1);

            } else
                dropdownData.put("schemeList", Collections.emptyList());
        if (headerFields.contains("subscheme"))
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
        nameMap = new LinkedHashMap<String, String>();

        for (final Object voucherName : voucherNameList)
            nameMap.put((String) voucherName, (String) voucherName);
        return nameMap;
    }

    private Query voucherSearchQuery() {
        String sql = "";

        if (!modeOfPayment.equals("-1"))
            sql = sql + " from CVoucherHeader vh,Paymentheader ph where vh.id = ph.voucherheader.id and";
        else
            sql = sql + " from CVoucherHeader vh where ";

        if (voucherHeader.getFundId() != null && voucherHeader.getFundId().getId() != -1)
            sql = sql + "  vh.fundId=" + voucherHeader.getFundId().getId();

        if (voucherHeader.getType() != null && !voucherHeader.getType().equals("-1"))
            sql = sql + " and vh.type='" + voucherHeader.getType() + "'";
        if (voucherHeader.getName() != null && !voucherHeader.getName().equalsIgnoreCase("-1") && !voucherHeader.getName().equalsIgnoreCase("0"))
            sql = sql + " and vh.name='" + voucherHeader.getName() + "'";
        if (fromDate != null)
            sql = sql + " and vh.voucherDate>='" + Constants.DDMMYYYYFORMAT1.format(fromDate) + "'";
        if (toDate != null)
            sql = sql + " and vh.voucherDate<='" + Constants.DDMMYYYYFORMAT1.format(toDate) + "'";
        if (voucherHeader.getStatus() != -1)
            sql = sql + " and vh.status=" + voucherHeader.getStatus();

        if (voucherHeader.getVouchermis().getDepartmentid() != null
                && voucherHeader.getVouchermis().getDepartmentid().getId() != -1)
            sql = sql + " and vh.vouchermis.departmentid=" + voucherHeader.getVouchermis().getDepartmentid().getId();

        if (voucherHeader.getVouchermis().getSchemeid() != null)
            sql = sql + " and vh.vouchermis.schemeid=" + voucherHeader.getVouchermis().getSchemeid().getId();
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            sql = sql + " and vh.vouchermis.subschemeid=" + voucherHeader.getVouchermis().getSubschemeid().getId();
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            sql = sql + " and vh.vouchermis.functionary=" + voucherHeader.getVouchermis().getFunctionary().getId();
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            sql = sql + " and vh.vouchermis.divisionid=" + voucherHeader.getVouchermis().getDivisionid().getId();
        if (!modeOfPayment.equals("-1"))
            sql = sql + " and upper(ph.type) ='" + getModeOfPayment() + "'";
        countQry = "select count(*) " + sql;
        sql = "select vh " + sql + " order by vh.vouchermis.departmentid.name ,vh.voucherDate, vh.voucherNumber";
        final Query query = persistenceService.getSession().createQuery(sql);
        return query;
    }

    private String getVoucherModule(final Integer vchrModuleId) throws ApplicationException
    {
        if (vchrModuleId == null)
            return "Internal";
        else
        {
            EgModules egModuleObj;
            egModuleObj = (EgModules) persistenceService.find("from EgModules m where m.id=?", vchrModuleId);
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
        checkMandatoryField("vouchermis.schemeid", "scheme", voucherHeader.getVouchermis().getSchemeid(),
                "voucher.scheme.mandatory");
        checkMandatoryField("vouchermis.subschemeid", "subscheme", voucherHeader.getVouchermis().getSubschemeid(),
                "voucher.subscheme.mandatory");
        checkMandatoryField("vouchermis.functionary", "functionary", voucherHeader.getVouchermis().getFunctionary(),
                "voucher.functionary.mandatory");
        checkMandatoryField("fundsourceId", "fundsource", voucherHeader.getVouchermis().getFundsource(),
                "voucher.fundsource.mandatory");
        checkMandatoryField("vouchermis.divisionId", "field", voucherHeader.getVouchermis().getDivisionid(),
                "voucher.field.mandatory");
    }

    @SuppressWarnings("unchecked")
    private void populateData() throws ParseException, ApplicationException {
        final List<CVoucherHeader> list = new ArrayList();
        list.addAll(voucherSearchQuery().list());
        BigDecimal amt = BigDecimal.ZERO;
        for (final CVoucherHeader cVchrHdr : list)
        {
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

    protected void checkMandatoryField(final String objectName, final String fieldName, final Object value, final String errorKey)
    {
        if (mandatoryFields.contains(fieldName) && (value == null || value.equals(-1)))
            addFieldError(objectName, getText(errorKey));
    }

    public boolean isFieldMandatory(final String field) {
        return mandatoryFields.contains(field);
    }

    private String getVoucherStatus(final int status)
    {
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

    private String getVoucherOwner(final CVoucherHeader voucherHeader)
    {
        final String dash = "-";
        final Integer voucherStatus = voucherHeader.getStatus();
        final String voucherType = voucherHeader.getType();
        State voucherState = null;
        if (voucherStatus.longValue() == FinancialConstants.CANCELLEDVOUCHERSTATUS.longValue()
                || voucherStatus.longValue() == FinancialConstants.CREATEDVOUCHERSTATUS.longValue())
            return dash;
        else if (voucherType.equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA))
        {
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
        }
        else if (voucherType.equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL))
        {
            voucherState = voucherHeader.getState();
            if (voucherState == null)
                return dash;
            else if (voucherState.getValue().equals("END"))
                return dash;
            else
                return getUserNameForPosition(voucherState.getOwnerPosition().getId().intValue());
        }
        else if (voucherType.equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT))
        {
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
        }
        
        //no need to find owner of receipt
       /* else if (voucherType.equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT))
        { 
            final ReceiptVoucher receiptVoucher = (ReceiptVoucher) persistenceService.find(
                    "from ReceiptVoucher rv where rv.voucherHeader=?", voucherHeader);
            if (receiptVoucher == null || receiptVoucher.getState() == null)
            {
                voucherState = voucherHeader.getState();
                if (voucherState == null)
                    return dash;
                else if (voucherState.getValue().equals("END"))
                    return dash;
                else
                    return getUserNameForPosition(voucherState.getOwnerPosition().getId().intValue());
            }
            else if (voucherState.getValue().equals("END"))
                return dash;
            else
                return getUserNameForPosition(receiptVoucher.getState().getOwnerPosition().getId().intValue());
        }*/
        else
            return dash;
    }

    private String getUserNameForPosition(final Integer posId) {
        final String query = "select emp.userName  from org.egov.eis.entity.EmployeeView emp where emp.position.id = ? ";
        final String userName = (String) persistenceService.find(query, posId.longValue());
        return userName;
    }

    public void setParamMap()
    {
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
    public String generatePdf() throws Exception {
        populateData();
        inputStream = reportHelper.exportPdf(inputStream, JASPERPATH, getParamMap(), voucherReportList);
        return "PDF";
    }

    @SkipValidation
    @Action(value = "/report/voucherStatusReport-generateXls")
    public String generateXls() throws Exception {
        populateData();
        inputStream = reportHelper.exportXls(inputStream, JASPERPATH, getParamMap(), voucherReportList);
        return "XLS";
    }

    protected Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setPagedResults(final EgovPaginatedList pagedResults) {
        this.pagedResults = pagedResults;
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