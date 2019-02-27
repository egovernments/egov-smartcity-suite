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
package org.egov.egf.web.actions.voucher;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.egf.commons.VoucherSearchUtil;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.services.payment.PaymentService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@ParentPackage("egov")
@Results({
        @Result(name = CancelVoucherAction.SEARCH, location = "cancelVoucher-search.jsp")
})
public class CancelVoucherAction extends BaseFormAction {
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    private static final long serialVersionUID = -8065315728701853083L;
    private static final Logger LOGGER = Logger.getLogger(CancelVoucherAction.class);
    public static final Locale LOCALE = new Locale("en", "IN");
    public static final SimpleDateFormat DDMMYYYYFORMATS = new SimpleDateFormat("dd/MM/yyyy", LOCALE);
    private final List<String> headerFields = new ArrayList<String>();
    private final List<String> mandatoryFields = new ArrayList<String>();
    private CVoucherHeader voucherHeader = new CVoucherHeader();
    private Map<String, String> nameMap;
    private VoucherSearchUtil voucherSearchUtil;
    private PaymentService paymentService;
    private Date fromDate;
    private Date toDate;
    private Fund fundId;
    private Long[] selectedVhs;
    protected static final String SEARCH = "search";
    Integer loggedInUser;
    public List<CVoucherHeader> voucherSearchList = new ArrayList<CVoucherHeader>();
    private PersistenceService<CVoucherHeader, Long> cVoucherHeaderPersistanceService;
    List<CVoucherHeader> voucherList;
    List<String> voucherTypes = VoucherHelper.VOUCHER_TYPES;
    Map<String, List<String>> voucherNames = VoucherHelper.VOUCHER_TYPE_NAMES;
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private AppConfigValueService appConfigValueService;
    @Autowired
    private VoucherService voucherService;

    public CancelVoucherAction() {
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

    @Override
    public void prepare() {

        loggedInUser = ApplicationThreadLocals.getUserId().intValue();
        super.prepare();
        getHeaderFields();
        loadDropDowns();
    }

    @SkipValidation
    @Action(value = "/voucher/cancelVoucher-beforeSearch")
    public String beforeSearch() {
        voucherHeader.reset();
        setFromDate(null);
        setToDate(null);
        return SEARCH;
    }

    @ValidationErrorPage(value = SEARCH)
    @Action(value = "/voucher/cancelVoucher-search")
    public String search() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("...Searching for voucher of type " + voucherHeader.getType());
        voucherSearchList = getVouchersForCancellation();
        return SEARCH;
    }

    private boolean isSuperUser() {
        final Query queryFnd = persistenceService.getSession().createNativeQuery(new StringBuilder(" SELECT usrr.USERID")
                .append(" FROM EG_USERROLE usrr,  EG_ROLE r")
                .append(" WHERE usrr.ROLEID=r.ID and usrr.userid = :userId AND lower(r.NAME)=:name").toString());
        final List<Object> superUserList = queryFnd.setParameter("userId", loggedInUser, IntegerType.INSTANCE)
                .setParameter("name", FinancialConstants.SUPERUSER, StringType.INSTANCE)
                .list();
        if (superUserList != null && superUserList.size() > 0)
            return true;
        else
            return false;
    }

    @SuppressWarnings("unchecked")
    private List<CVoucherHeader> getVouchersForCancellation() {
        StringBuffer voucheerWithNoPayment= new StringBuffer();
        StringBuffer allPayment = new StringBuffer();
        StringBuffer noChequePaymentQry = new StringBuffer();
        String contraVoucherQry;
        String filterQry = "";
        final boolean validateFinancialYearForPosting = voucherSearchUtil.validateFinancialYearForPosting(fromDate, toDate);
        if (!validateFinancialYearForPosting)
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "Financial Year  Not active for Posting(either year or date within selected date range)",
                    "Financial Year  Not active for Posting(either year or date within selected date range)")));

        Map<String, Map<String, Object>> filterQueryMap = voucherSearchUtil.voucherFilterQuery(voucherHeader, fromDate, toDate, "");
        Map.Entry<String, Map<String, Object>> filterQueryEntry = filterQueryMap.entrySet().iterator().next();
        String queryString = filterQueryEntry.getKey();
        Map<String, Object> params = filterQueryEntry.getValue();

        //final String filter = voucherSearchUtil.voucherFilterQuery(voucherHeader, fromDate, toDate, "");
        final String userCond = "";
        voucherList = new ArrayList<CVoucherHeader>();
        final List<CVoucherHeader> toBeRemovedList = new ArrayList<CVoucherHeader>();
        filterQry = queryString.concat(userCond).concat(" order by vh.voucherNumber");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("......Searching voucher for cancelllation...");
        if (voucherHeader.getType().equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL)) {
            // Voucher for which payment is not generated
            voucheerWithNoPayment.append(" from CVoucherHeader vh where vh not in ( select billVoucherHeader from Miscbilldetail) ")
                                    .append(" and vh.status in (:createStatus) and (vh.isConfirmed != 1 or vh.isConfirmed is null)");

            Query noPayQuery = persistenceService.getSession().createQuery(voucheerWithNoPayment.append(filterQry).toString())
                    .setParameter("createStatus",Long.valueOf(FinancialConstants.CREATEDVOUCHERSTATUS),LongType.INSTANCE);
            params.entrySet().forEach(entry -> noPayQuery.setParameter(entry.getKey(), entry.getValue()));
            voucherList.addAll(noPayQuery.list());

            // Filters vouchers for which payments are generated and are in cancelled state
            allPayment.append("select distinct(vh) from  Miscbilldetail misc left join misc.billVoucherHeader vh where misc.billVoucherHeader is not null")
                    .append(" and (vh.isConfirmed != 1  or  vh.isConfirmed  is null ) and vh.status in (:createStatus)");

            Query allPayQuery = persistenceService.getSession().createQuery(allPayment.append(filterQry).toString())
                    .setParameter("createStatus",Long.valueOf(FinancialConstants.CREATEDVOUCHERSTATUS),LongType.INSTANCE);
            params.entrySet().forEach(entry -> allPayQuery.setParameter(entry.getKey(), entry.getValue()));
            voucherList.addAll(allPayQuery.list());

            // editModeQuery3 :-check for voucher for for which payments are active 0,5 this will be removed from above two list
            final StringBuffer editModeQuery3 = new StringBuffer(" select misc.billVoucherHeader.id from CVoucherHeader ph, Miscbilldetail misc,CVoucherHeader vh  where ")
                    .append(" misc.payVoucherHeader=ph and   misc.billVoucherHeader is not null and misc.billVoucherHeader=vh ")
                    .append(" and ph.status  in (:createdVoucherStatus , :preApprovedStatus) ")
                    .append(" and (vh.isConfirmed != 1 or  vh.isConfirmed is null) ");
            Query query3 = persistenceService.getSession().createQuery(editModeQuery3.append(filterQry).toString())
                    .setParameter("createdVoucherStatus",Long.valueOf(FinancialConstants.CREATEDVOUCHERSTATUS), LongType.INSTANCE)
                    .setParameter("preApprovedStatus",Long.valueOf(FinancialConstants.PREAPPROVEDVOUCHERSTATUS),LongType.INSTANCE);
            params.entrySet().forEach(entry -> query3.setParameter(entry.getKey(), entry.getValue()));
            final List<Long> vouchersHavingActivePayments = query3.list();

            // If remittance payment is there and are in cancelled state
            final StringBuffer uncancelledRemittances = new StringBuffer(" SELECT distinct(vh.id) FROM EgRemittanceDetail r, EgRemittanceGldtl rgd, Generalledgerdetail gld, ")
                    .append(" CGeneralLedger gl, EgRemittance rd, CVoucherHeader vh ,Vouchermis billmis, CVoucherHeader remittedvh  WHERE ")
                    .append(" r.egRemittanceGldtl=rgd AND rgd.generalledgerdetail=gld AND gld.generalledger=gl AND r.egRemittance=rd AND")
                    .append(" rd.voucherheader=remittedvh AND gl.voucherHeaderId =vh  AND ")
                    .append(" remittedvh =billmis.voucherheaderid and remittedvh.status!=:cancelledStatus");
            Query uncancelledRemQuery = persistenceService.getSession().createQuery(uncancelledRemittances.append(queryString).append(userCond).toString())
                    .setParameter("cancelledStatus",Long.valueOf(FinancialConstants.CANCELLEDVOUCHERSTATUS),LongType.INSTANCE);
            params.entrySet().forEach(entry -> uncancelledRemQuery.setParameter(entry.getKey(), entry.getValue()));
            final List<Long> remittanceBillVhIdList = uncancelledRemQuery.list();

            remittanceBillVhIdList.addAll(vouchersHavingActivePayments);

            // If remmittacnce payment is generated remove the voucher from the list
            if (voucherList != null && voucherList.size() != 0 && remittanceBillVhIdList != null
                    && remittanceBillVhIdList.size() != 0) {
                for (int i = 0; i < voucherList.size(); i++)
                    if (remittanceBillVhIdList.contains(voucherList.get(i).getId()))
                        toBeRemovedList.add(voucherList.get(i));
                for (final CVoucherHeader vh : toBeRemovedList)
                    voucherList.remove(vh);
            }
        } else if (voucherHeader.getType().equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT)) {
            final String qryStr = queryString;
            String filterQuerySql = "";
            String misTab = "";
            String VoucherMisJoin = "";
            if (qryStr.contains(" and vh.vouchermis")) {
                misTab = ", vouchermis mis ";
                VoucherMisJoin = " and vh.id=mis.voucherheaderid";
                filterQuerySql = qryStr.replace("and vh.vouchermis.", " and mis.");
            } else
                filterQuerySql = queryString;
            // BPVs for which no Cheque is issued
            noChequePaymentQry = noChequePaymentQry.append("from CVoucherHeader vh where vh.status not in (:preApprovedStatus , :cancelledStatus )")
                        .append(queryString)
                        .append("  and not Exists(select 'true' from InstrumentVoucher iv where iv.voucherHeaderId=vh.id) order by vh.voucherNumber ");
            Query noChkPayQry = persistenceService.getSession().createQuery(noChequePaymentQry.toString())
                    .setParameter("preApprovedStatus",Long.valueOf(FinancialConstants.PREAPPROVEDVOUCHERSTATUS),LongType.INSTANCE)
                    .setParameter("cancelledStatus",Long.valueOf(FinancialConstants.CANCELLEDVOUCHERSTATUS),LongType.INSTANCE);
            params.entrySet().forEach(entry -> noChkPayQry.setParameter(entry.getKey(), entry.getValue()));
            voucherList.addAll(noChkPayQry.list());

            filterQueryMap = voucherFilterSqlQuery(voucherHeader, fromDate, toDate, "");
            filterQueryEntry = filterQueryMap.entrySet().iterator().next();
            queryString = filterQueryEntry.getKey();
            params = filterQueryEntry.getValue();
            misTab = "";
            VoucherMisJoin = "";
            if (queryString.contains(" and vh.vouchermis")) {
                misTab = ", Vouchermis mis ";
                VoucherMisJoin = " and vh.id=mis.voucherheaderid";
                filterQuerySql = queryString.replace("and vh.vouchermis.", " and mis.");
            } else
                filterQuerySql = queryString;

            StringBuffer queryString1 = new StringBuffer("SELECT distinct vh.id FROM egw_status status")
                    .append(misTab)
                    .append(", voucherheader vh ")
                    .append(" LEFT JOIN EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID")
                    .append(" LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID INNER JOIN (SELECT MAX(iv1.instrumentheaderid) AS maxihid,")
                    .append(" iv1.voucherheaderid AS iv1vhid FROM egf_instrumentvoucher iv1 GROUP BY iv1.voucherheaderid)as INST ON maxihid=IH.ID ")
                    .append(" WHERE	IV.VOUCHERHEADERID  IS NOT NULL	AND status.description IN (:instrumentCancelledStatus , :instrumentSurrenderedStatus, ")
                    .append(" :instrumentSurrReassignStatus ) and status.id=ih.id_status and vh.status not in (:preApprovedStatus , :cancelledVoucStatus ) ")
                    .append(VoucherMisJoin).append(filterQuerySql);
            // Query for cancelling BPVs for which cheque is assigned and cancelled
            final Query query1 = persistenceService.getSession()
                    .createNativeQuery(queryString1.toString())
                    .setParameter("instrumentCancelledStatus",FinancialConstants.INSTRUMENT_CANCELLED_STATUS,StringType.INSTANCE)
                    .setParameter("instrumentSurrenderedStatus",FinancialConstants.INSTRUMENT_SURRENDERED_STATUS,StringType.INSTANCE)
                    .setParameter("instrumentSurrReassignStatus",FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS,StringType.INSTANCE)
                    .setParameter("preApprovedStatus",Long.valueOf(FinancialConstants.PREAPPROVEDVOUCHERSTATUS),LongType.INSTANCE)
                    .setParameter("cancelledVoucStatus",Long.valueOf(FinancialConstants.CANCELLEDVOUCHERSTATUS),LongType.INSTANCE);
            params.entrySet().forEach(entry -> query1.setParameter(entry.getKey(), entry.getValue()));
            final List<BigInteger> list = query1.list();

            for (final BigInteger b : list)
                voucherList.add((CVoucherHeader) persistenceService.find("from CVoucherHeader  where id=?1", b.longValue()));
        } else if (voucherHeader.getType().equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA)) {
            contraVoucherQry = "from CVoucherHeader vh where vh.status =:vhStatus and (vh.isConfirmed != 1 or vh.isConfirmed is null) ";
            Query qry1 = persistenceService.getSession().createQuery(contraVoucherQry.concat(filterQry));
            qry1.setParameter("vhStatus", Integer.valueOf(FinancialConstants.CREATEDVOUCHERSTATUS), IntegerType.INSTANCE);
            params.entrySet().forEach(entry -> qry1.setParameter(entry.getKey(), entry.getValue()));
            voucherList.addAll(qry1.list());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("......No of voucher found in search for is cancellation ..." + voucherList.size());
        return voucherList;
    }

    @SkipValidation
    public void validateBeforeCancel(final CVoucherHeader voucherObj) {
        try {
            financialYearDAO.getFinancialYearByDate(voucherObj.getVoucherDate());
        } catch (final Exception e) {
            addActionError("Voucher Cancellation failed for " + voucherObj.getVoucherNumber());
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        }
    }

    @SuppressWarnings("unchecked")
    @ValidationErrorPage(value = SEARCH)
    @SkipValidation
    @Action(value = "/voucher/cancelVoucher-update")
    public String update() {
        final Map<String, Object> map = voucherService.cancelVouchers(selectedVhs, loggedInUser, voucherHeader);
        ((List<String>) map.get("voucherNumbers")).forEach(rec -> addActionMessage(getText("cancel.voucher.failure", new String[] {rec})));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Cancel Voucher | CancelVoucher | Vouchers Cancelled ");
        if (!map.get("voucherId").equals(""))
            addActionMessage(getText("Vouchers Cancelled Succesfully"));
        return SEARCH;
    }

    private void loadDropDowns() {

        if (headerFields.contains("department"))
            addDropdownData("departmentList", persistenceService.findAllBy("from Department order by name"));
        if (headerFields.contains("functionary"))
            addDropdownData("functionaryList",
                    persistenceService.findAllBy(" from Functionary where isactive=true order by name"));
        if (headerFields.contains("fund"))
            addDropdownData("fundList",
                    persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
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
        // persistenceService.findAllBy(" select distinct vh.type from CVoucherHeader vh order by vh.type")); //where
        // vh.status!=4
        addDropdownData("typeList", VoucherHelper.VOUCHER_TYPES);
        nameMap = new LinkedHashMap<String, String>();
    }

    @Override
    public void validate() {
        if (fromDate == null)
            addFieldError("From Date", getText("Please Enter From Date"));
        if (toDate == null)
            addFieldError("To Date", getText("Please Enter To Date"));
        if (voucherHeader.getType() == null || voucherHeader.getType().equals("-1"))
            addFieldError("Voucher Type", getText("Please Select Voucher Type"));
        if (voucherHeader.getName() == null || voucherHeader.getName().equals("-1") || voucherHeader.getName().equals("0"))
            addFieldError("Voucher Type", getText("Please Select Voucher Name"));
        int checKDate = 0;
        if (fromDate != null && toDate != null)
            checKDate = fromDate.compareTo(toDate);
        if (checKDate > 0)
            addFieldError("To Date", getText("Please Enter To Date Greater than From Date"));
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

    protected void getHeaderFields() {
        final List<AppConfigValues> appConfigList = appConfigValueService.getConfigValuesByModuleAndKey("EGF",
                "DEFAULT_SEARCH_MISATTRRIBUTES");

        for (final AppConfigValues appConfigVal : appConfigList) {
            final String value = appConfigVal.getValue();
            final String header = value.substring(0, value.indexOf('|'));
            headerFields.add(header);
            final String mandate = value.substring(value.indexOf('|') + 1);
            if (mandate.equalsIgnoreCase("M"))
                mandatoryFields.add(header);
        }

    }

    protected void checkMandatoryField(final String objectName, final String fieldName, final Object value,
            final String errorKey) {
        if (mandatoryFields.contains(fieldName) && (value == null || value.equals(-1)))
            addFieldError(objectName, getText(errorKey));
    }

    private Map<String, Map<String, Object>> voucherFilterSqlQuery(final CVoucherHeader voucherHeader, final Date fromDate, final Date toDate, final String mode) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("");
        if (!voucherHeader.getType().equals("-1")) {
            sql.append(" and vh.type = :vhType");
            params.put("vhType", voucherHeader.getType());
        }

        if (voucherHeader.getName() != null
                && !voucherHeader.getName().equalsIgnoreCase("0") && !voucherHeader.getName().equalsIgnoreCase("-1")) {
            sql.append(" and vh.name = :vhName");
            params.put("vhName", voucherHeader.getName());
        }
        if (voucherHeader.getVoucherNumber() != null
                && !voucherHeader.getVoucherNumber().equals("")) {
            sql.append(" and vh.voucherNumber like '%:voucherNumber%'");
            params.put("voucherNumber", voucherHeader.getVoucherNumber());
        }
        if (fromDate != null) {
            sql.append(" and vh.voucherDate >= :fromDate");
            params.put("fromDate", fromDate);
        }
        if (toDate != null) {
            sql.append(" and vh.voucherDate <= :toDate");
            params.put("toDate", toDate);
        }
        if (voucherHeader.getFundId() != null) {
            sql.append(" and vh.fundId = :fundId");
            params.put("fundId", voucherHeader.getFundId().getId());
        }
        if (voucherHeader.getVouchermis().getFundsource() != null) {
            sql.append(" and vh.fundsourceId = :fundSourceId");
            params.put("fundSourceId", voucherHeader.getVouchermis().getFundsource().getId());
        }
        if (voucherHeader.getVouchermis().getDepartmentid() != null) {
            sql.append(" and vh.vouchermis.departmentid = :deptId");
            params.put("deptId", voucherHeader.getVouchermis().getDepartmentid().getId());
        }
        if (voucherHeader.getVouchermis().getSchemeid() != null) {
            sql.append(" and vh.vouchermis.schemeid = :schemeId");
            params.put("schemeId", voucherHeader.getVouchermis().getSchemeid().getId());
        }
        if (voucherHeader.getVouchermis().getSubschemeid() != null) {
            sql.append(" and vh.vouchermis.subschemeid = :subSchemeId");
            params.put("subSchemeId", voucherHeader.getVouchermis().getSubschemeid().getId());
        }
        if (voucherHeader.getVouchermis().getFunctionary() != null) {
            sql.append(" and vh.vouchermis.functionaryid = :functionary");
            params.put("functionary", voucherHeader.getVouchermis().getFunctionary().getId());
        }
        if (voucherHeader.getVouchermis().getDivisionid() != null) {
            sql.append(" and vh.vouchermis.divisionid = :divisionId");
            params.put("divisionId", voucherHeader.getVouchermis().getDivisionid().getId());
        }
        if (voucherHeader.getModuleId() != null) {
            // -2 For vouchers created from the financial module. -2 is set in
            // populateSourceMap() of VoucherSearchAction.
            if (voucherHeader.getModuleId() == -2)
                sql.append(" and vh.moduleId is null ");
            else {
                sql.append(" and vh.moduleId = :moduleId");
                params.put("moduleId", voucherHeader.getModuleId());
            }
        }
        map.put(sql.toString(), params);
        return map;
    }

    public boolean isFieldMandatory(final String field) {
        return mandatoryFields.contains(field);
    }

    public boolean shouldShowHeaderField(final String field) {
        return headerFields.contains(field);
    }

    public CVoucherHeader getVoucherHeader() {
        return voucherHeader;
    }

    public void setVoucherHeader(final CVoucherHeader voucherHeader) {
        this.voucherHeader = voucherHeader;
    }

    public Map<String, String> getNameMap() {
        return nameMap;
    }

    public void setNameMap(final Map<String, String> nameMap) {
        this.nameMap = nameMap;
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

    public VoucherSearchUtil getVoucherSearchUtil() {
        return voucherSearchUtil;
    }

    public void setVoucherSearchUtil(final VoucherSearchUtil voucherSearchUtil) {
        this.voucherSearchUtil = voucherSearchUtil;
    }

    public List<CVoucherHeader> getVoucherSearchList() {
        return voucherSearchList;
    }

    public void setVoucherSearchList(final List<CVoucherHeader> voucherSearchList) {
        this.voucherSearchList = voucherSearchList;
    }

    public Long[] getSelectedVhs() {
        return selectedVhs;
    }

    public void setSelectedVhs(final Long[] selectedVhs) {
        this.selectedVhs = selectedVhs;
    }

    public List<CVoucherHeader> getVoucherList() {
        return voucherList;
    }

    public void setVoucherList(final List<CVoucherHeader> voucherList) {
        this.voucherList = voucherList;
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

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

}