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
/**
 *
 */
package org.egov.egf.web.actions.voucher;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.contra.ContraBean;
import org.egov.model.voucher.VoucherDetails;
import org.egov.model.voucher.WorkflowBean;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EisUtilService;
import org.egov.services.financingsource.FinancingSourceService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;

public class BaseVoucherAction extends GenericWorkFlowAction {
    private static final long serialVersionUID = 1L;
    protected final String INVALIDPAGE = "invalidPage";
    private static final String FAILED = "Transaction failed";
    private static final String EXCEPTION_WHILE_SAVING_DATA = "Exception while saving data";
    private static final Logger LOGGER = Logger.getLogger(BaseVoucherAction.class);
    // sub class should add getter and setter this workflowBean
    public transient WorkflowBean workflowBean = new WorkflowBean();
    public transient CVoucherHeader voucherHeader = new CVoucherHeader();
    protected transient List<String> headerFields = new ArrayList<>();
    protected transient List<String> mandatoryFields = new ArrayList<>();
    protected String voucherNumManual;
    protected transient EisUtilService eisService;
    protected transient AssignmentService assignmentService;
   
    @Autowired
    private VoucherTypeForULB voucherTypeForULB;
    protected String reversalVoucherNumber;
    protected String reversalVoucherDate;
    private Integer voucherNumberPrefixLength;
    public static final String ZERO = "0";
    private FinancingSourceService financingSourceService;
    List<String> voucherTypes = VoucherHelper.VOUCHER_TYPES;
    @Autowired
    private  CreateVoucher createVoucher;
    Map<String, List<String>> voucherNames = VoucherHelper.VOUCHER_TYPE_NAMES;

    @Autowired
    private EgovMasterDataCaching masterDataCache;
    @Autowired
    protected AppConfigValueService appConfigValuesService;

    public BaseVoucherAction()
    {
        voucherHeader.setVouchermis(new Vouchermis());
        addRelatedEntity("vouchermis.departmentid", Department.class);
        addRelatedEntity("fundId", Fund.class);
        addRelatedEntity("vouchermis.schemeid", Scheme.class);
        addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
        addRelatedEntity("vouchermis.function", CFunction.class);
        addRelatedEntity("vouchermis.fundsource", Fundsource.class);
        addRelatedEntity("vouchermis.divisionid", Boundary.class);
        addRelatedEntity("vouchermis.functionary", Functionary.class);
    }

    @Override
    public StateAware getModel() {

        return voucherHeader;
    }

    @SkipValidation
    public String newform()
    {
        return NEW;

    }

    @Override
    public void prepare() {
        super.prepare();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside Prepare method");
        getHeaderMandateFields();
        if (headerFields.contains("department"))
            addDropdownData("departmentList", masterDataCache.get("egi-department"));
        if (headerFields.contains("functionary"))
            addDropdownData("functionaryList", masterDataCache.get("egi-functionary"));
        if (headerFields.contains("function"))
            addDropdownData("functionList", masterDataCache.get("egi-function"));
        if (headerFields.contains("fund"))
            addDropdownData("fundList", masterDataCache.get("egi-fund"));
        if (headerFields.contains("fundsource"))
            addDropdownData("fundsourceList", masterDataCache.get("egi-fundSource"));
        if (headerFields.contains("field"))
            addDropdownData("fieldList", masterDataCache.get("egi-ward"));
        if (headerFields.contains("scheme"))
            addDropdownData("schemeList", Collections.emptyList());
        if (headerFields.contains("subscheme"))
            addDropdownData("subschemeList", Collections.emptyList());

        // addDropdownData("typeList",
        // persistenceService.findAllBy(" select distinct vh.type from CVoucherHeader vh where vh.status!=4 order by vh.type"));
        addDropdownData("typeList", VoucherHelper.VOUCHER_TYPES);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of  MIS attributes are :" + headerFields.size());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of mandate MIS attributes are :" + mandatoryFields.size());
    }

    @Deprecated
    protected void getHeaderMandateFields() {
        final List<AppConfigValues> appConfigList = appConfigValuesService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "DEFAULTTXNMISATTRRIBUTES");

            for (final AppConfigValues appConfigVal : appConfigList) {
                final String value = appConfigVal.getValue();
                final String header = value.substring(0, value.indexOf('|'));
                headerFields.add(header);
                final String mandate = value.substring(value.indexOf('|') + 1);
                if (mandate.equalsIgnoreCase("M"))
                    mandatoryFields.add(header);
            }
        setOneFunctionCenterValue();
        mandatoryFields.add("voucherdate");
    }

    public void populateWorkflowBean() {
        workflowBean.setApproverPositionId(approverPositionId);
        workflowBean.setApproverComments(approverComments);
        workflowBean.setWorkFlowAction(workFlowAction);
        workflowBean.setCurrentState(currentState);
    }

    public boolean isOneFunctionCenter() {
        setOneFunctionCenterValue();
        return voucherHeader.getIsRestrictedtoOneFunctionCenter();
    }

    public void setOneFunctionCenterValue() {
        
        final List<AppConfigValues> appConfigValues = appConfigValuesService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,"ifRestrictedToOneFunctionCenter");
        if (appConfigValues == null || appConfigValues.isEmpty())
            throw new ValidationException("Error", "ifRestrictedToOneFunctionCenter is not defined");
        else
            voucherHeader.setIsRestrictedtoOneFunctionCenter(appConfigValues.get(0).getValue().equalsIgnoreCase("yes") ? true : false);
    }

    public boolean isBankBalanceMandatory()
    {
        
        final List<AppConfigValues> appConfigValues = appConfigValuesService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,"bank_balance_mandatory");
        if (appConfigValues == null)
            throw new ValidationException("", "bank_balance_mandatory parameter is not defined");
        return appConfigValues.get(0).getValue().equals("Y") ? true : false;
    }

    protected void loadSchemeSubscheme() {
        if (headerFields.contains("scheme") && null != voucherHeader.getFundId())
            addDropdownData("schemeList", getPersistenceService()
                    .findAllBy("from Scheme where fund=?", voucherHeader.getFundId()));
        if (headerFields.contains("subscheme") && voucherHeader.getVouchermis() != null
                && null != voucherHeader.getVouchermis().getSchemeid())
            addDropdownData(
                    "subschemeList",
                    getPersistenceService().findAllBy("from SubScheme where scheme.id=? and isActive=true order by name",
                            voucherHeader.getVouchermis().getSchemeid().getId()));
    }

    protected void loadFundSource() {
        if (headerFields.contains("fundsource") && null != voucherHeader.getVouchermis().getSubschemeid()) {
            final List<Fundsource> fundSourceList = financingSourceService.getFinancialSourceBasedOnSubScheme(voucherHeader
                    .getVouchermis().getSubschemeid().getId());
            addDropdownData("fundsourceList", fundSourceList);
        }
    }

    protected HashMap<String, Object> createHeaderAndMisDetails() throws ValidationException
    {
        final HashMap<String, Object> headerdetails = new HashMap<>();
        headerdetails.put(VoucherConstant.VOUCHERNAME, voucherHeader.getName());
        headerdetails.put(VoucherConstant.VOUCHERTYPE, voucherHeader.getType());
        headerdetails.put((String) VoucherConstant.VOUCHERSUBTYPE, voucherHeader.getVoucherSubType());
        headerdetails.put(VoucherConstant.VOUCHERNUMBER, voucherHeader.getVoucherNumber());
        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherHeader.getVoucherDate());
        headerdetails.put(VoucherConstant.DESCRIPTION, voucherHeader.getDescription());

        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            headerdetails.put(VoucherConstant.DEPARTMENTCODE, voucherHeader.getVouchermis().getDepartmentid().getCode());
        if (voucherHeader.getFundId() != null)
            headerdetails.put(VoucherConstant.FUNDCODE, voucherHeader.getFundId().getCode());
        if (voucherHeader.getVouchermis().getSchemeid() != null)
            headerdetails.put(VoucherConstant.SCHEMECODE, voucherHeader.getVouchermis().getSchemeid().getCode());
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            headerdetails.put(VoucherConstant.SUBSCHEMECODE, voucherHeader.getVouchermis().getSubschemeid().getCode());
        if (voucherHeader.getVouchermis().getFundsource() != null)
            headerdetails.put(VoucherConstant.FUNDSOURCECODE, voucherHeader.getVouchermis().getFundsource().getCode());
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            headerdetails.put(VoucherConstant.DIVISIONID, voucherHeader.getVouchermis().getDivisionid().getId());
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            headerdetails.put(VoucherConstant.FUNCTIONARYCODE, voucherHeader.getVouchermis().getFunctionary().getCode());
        if (voucherHeader.getVouchermis().getFunction() != null)
            headerdetails.put(VoucherConstant.FUNCTIONCODE, voucherHeader.getVouchermis().getFunction().getCode());
        return headerdetails;
    }

    @Deprecated
    protected void validateFields() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside Validate Method");

        checkMandatoryField("vouchernumber", voucherHeader.getVoucherNumber(), "voucher.field.vouchernumber");
        checkMandatoryField("voucherdate", voucherHeader.getVoucherDate(), "voucher.field.voucherdate");
        checkMandatoryField("fund", voucherHeader.getFundId(), "voucher.fund.mandatory");
        checkMandatoryField("function", voucherHeader.getVouchermis().getFunction(), "voucher.function.mandatory");
        checkMandatoryField("department", voucherHeader.getVouchermis().getDepartmentid(), "voucher.department.mandatory");
        checkMandatoryField("scheme", voucherHeader.getVouchermis().getSchemeid(), "voucher.scheme.mandatory");
        checkMandatoryField("subscheme", voucherHeader.getVouchermis().getSubschemeid(), "voucher.subscheme.mandatory");
        checkMandatoryField("functionary", voucherHeader.getVouchermis().getFunctionary(), "voucher.functionary.mandatory");
        checkMandatoryField("fundsource", voucherHeader.getVouchermis().getFundsource(), "voucher.fundsource.mandatory");
        checkMandatoryField("field", voucherHeader.getVouchermis().getDivisionid(), "voucher.field.mandatory");

    }

    protected void checkMandatoryField(final String fieldName, final Object value, final String errorKey) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Filed name :=" + fieldName + " Value = :" + value);

        if (mandatoryFields.contains(fieldName) && (value == null || StringUtils.isEmpty(value.toString())))
            throw new ValidationException(Arrays.asList(new ValidationError(errorKey, errorKey)));

    }

    public CVoucherHeader createVoucherAndledger(final List<VoucherDetails> billDetailslist,
            final List<VoucherDetails> subLedgerlist) {
        try {
            final HashMap<String, Object> headerDetails = createHeaderAndMisDetails();
            // update DirectBankPayment source path
            HashMap<String, Object> subledgertDetailMap = null;
            final List<HashMap<String, Object>> accountdetails = new ArrayList<>();
            final List<HashMap<String, Object>> subledgerDetails = new ArrayList<>();

            final Map<String, Object> glcodeMap = new HashMap<>();
            for (final VoucherDetails voucherDetail : billDetailslist)
            {
                HashMap<String, Object> detailMap = new HashMap<>();
                if (voucherDetail.getFunctionIdDetail() != null)
                    if (voucherHeader.getIsRestrictedtoOneFunctionCenter())
                        detailMap.put(VoucherConstant.FUNCTIONCODE, voucherHeader.getVouchermis().getFunction().getCode());
                    else if (null != voucherDetail.getFunctionIdDetail()) {
                        final CFunction function = (CFunction) persistenceService.getSession().load(CFunction.class,
                                voucherDetail.getFunctionIdDetail());
                        detailMap.put(VoucherConstant.FUNCTIONCODE, function.getCode());
                    } else if (null != voucherHeader.getVouchermis().getFunction())
                        detailMap.put(VoucherConstant.FUNCTIONCODE, voucherHeader.getVouchermis().getFunction().getCode());
                if (voucherDetail.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0) {

                    detailMap.put(VoucherConstant.DEBITAMOUNT, voucherDetail.getDebitAmountDetail().toString());
                    detailMap.put(VoucherConstant.CREDITAMOUNT, ZERO);
                    detailMap.put(VoucherConstant.GLCODE, voucherDetail.getGlcodeDetail());
                    accountdetails.add(detailMap);
                    glcodeMap.put(voucherDetail.getGlcodeDetail(), VoucherConstant.DEBIT);
                }
                else {
                    detailMap.put(VoucherConstant.CREDITAMOUNT, voucherDetail.getCreditAmountDetail().toString());
                    detailMap.put(VoucherConstant.DEBITAMOUNT, ZERO);
                    detailMap.put(VoucherConstant.GLCODE, voucherDetail.getGlcodeDetail());
                    accountdetails.add(detailMap);
                    glcodeMap.put(voucherDetail.getGlcodeDetail(), VoucherConstant.CREDIT);
                }

            }

            for (final VoucherDetails voucherDetail : subLedgerlist) {
                subledgertDetailMap = new HashMap<>();
                final String amountType = glcodeMap.get(voucherDetail.getSubledgerCode()) != null ? glcodeMap.get(
                        voucherDetail.getSubledgerCode()).toString() : null; // Debit or Credit.
                if (voucherDetail.getFunctionDetail() != null && !voucherDetail.getFunctionDetail().equalsIgnoreCase("")
                        && !voucherDetail.getFunctionDetail().equalsIgnoreCase("0")) {
                    final CFunction function = (CFunction) persistenceService.find("from CFunction where id = ?",
                            Long.parseLong(voucherDetail.getFunctionDetail()));
                    subledgertDetailMap.put(VoucherConstant.FUNCTIONCODE, function != null ? function.getCode() : "");
                }
                if (null != amountType && amountType.equalsIgnoreCase(VoucherConstant.DEBIT))
                    subledgertDetailMap.put(VoucherConstant.DEBITAMOUNT, voucherDetail.getAmount());
                else if (null != amountType)
                    subledgertDetailMap.put(VoucherConstant.CREDITAMOUNT, voucherDetail.getAmount());
                subledgertDetailMap.put(VoucherConstant.DETAILTYPEID, voucherDetail.getDetailType().getId());
                subledgertDetailMap.put(VoucherConstant.DETAILKEYID, voucherDetail.getDetailKeyId());
                subledgertDetailMap.put(VoucherConstant.GLCODE, voucherDetail.getSubledgerCode());

                subledgerDetails.add(subledgertDetailMap);
            }

            voucherHeader = createVoucher.createPreApprovedVoucher(headerDetails, accountdetails, subledgerDetails);

        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        } catch (final ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (final Exception e) {
            // handle engine exception
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Posted to Ledger " + voucherHeader.getId());
        return voucherHeader;

    }

    protected boolean validateData(final List<VoucherDetails> billDetailslist, final List<VoucherDetails> subLedgerList) {
        BigDecimal totalDrAmt = BigDecimal.ZERO;
        BigDecimal totalCrAmt = BigDecimal.ZERO;
        int index = 0;
        boolean isValFailed = false;
        final Map<String, List<String>> accCodeFuncMap = new HashMap<>();

        for (final VoucherDetails voucherDetails : billDetailslist) {
            index = index + 1;
            totalDrAmt = totalDrAmt.add(voucherDetails.getDebitAmountDetail() == null ? BigDecimal.ZERO : voucherDetails.getDebitAmountDetail());
            totalCrAmt = totalCrAmt.add(voucherDetails.getCreditAmountDetail() == null ? BigDecimal.ZERO : voucherDetails.getCreditAmountDetail());
           
             // to support same account code to be used multiple times.

            if (voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0 &&
                    voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && voucherDetails.getGlcodeDetail().trim().length() != 0) {
                addActionError(getText("journalvoucher.accdetail.amountZero", new String[] { voucherDetails.getGlcodeDetail() }));
                isValFailed = true;
            } else if (voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0 &&
                    voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) > 0) {
                addActionError(getText("journalvoucher.accdetail.amount", new String[] { voucherDetails.getGlcodeDetail() }));
                isValFailed = true;
            } else if ((voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0
                    || voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) > 0)
                    && voucherDetails.getGlcodeDetail().trim().length() == 0 || voucherDetails.getGlcodeIdDetail() == null) {
                addActionError(getText("journalvoucher.accdetail.accmissing", new String[] { Integer.toString(index) }));
                isValFailed = true;
            } else if (voucherDetails.getFunctionDetail() != null && !voucherDetails.getFunctionDetail().equalsIgnoreCase("")
                    && voucherDetails.getFunctionIdDetail() == null) {
                addActionError(getText("jv.funcMissing", new String[] { voucherDetails.getGlcodeDetail() }));
                isValFailed = true;
            }
            else {
                String functionId = null;
               
                functionId = voucherDetails.getFunctionIdDetail() != null ? voucherDetails.getFunctionIdDetail().toString() : "0";
                final List<String> existingFuncs = accCodeFuncMap.get(voucherDetails.getGlcodeDetail());
                if (null == existingFuncs) {
                    final List<String> list = new ArrayList<>();
                    list.add(functionId);
                    accCodeFuncMap.put(voucherDetails.getGlcodeDetail(), list);
                } else if (functionId.equals("0") || existingFuncs.contains("0")) {
                    addActionError(getText("jv.multiplecodes.funcMissing", new String[] { voucherDetails.getGlcodeDetail() }));
                    isValFailed = true;
                    break;
                } else if (existingFuncs.contains(functionId)) {
                    addActionError(getText("jv.multiplecodes.duplicateFunc",
                            new String[] { voucherDetails.getGlcodeDetail() }));
                    isValFailed = true;
                    break;

                } else {
                    existingFuncs.add(functionId);
                    accCodeFuncMap.put(voucherDetails.getGlcodeDetail(), existingFuncs);
                }
            }
        }
        if (totalDrAmt.compareTo(totalCrAmt) != 0 && !isValFailed) {
            addActionError(getText("journalvoucher.accdetail.drcrmatch"));
            isValFailed = true;
        }
        else if (!isValFailed)
            isValFailed = validateSubledgerDetails(billDetailslist, subLedgerList);
        return isValFailed;
    }

    /*
     * description - validate the total subledger data.
     */
    @SuppressWarnings("unchecked")
    protected boolean validateSubledgerDetails(final List<VoucherDetails> billDetailslist,
            final List<VoucherDetails> subLedgerlist) {

        Map<String, Object> accountDetailMap;
        final List<Map<String, Object>> subLegAccMap = new ArrayList<>(); // this list will contain the details
                                                                                             // about
        // the account code those are detail codes.
        final List<String> repeatedglCodes = VoucherHelper.getRepeatedGlcodes(billDetailslist);
        for (final VoucherDetails voucherDetails : billDetailslist) {
            final CChartOfAccountDetail chartOfAccountDetail = (CChartOfAccountDetail) getPersistenceService().find(
                    " from CChartOfAccountDetail" +
                            " where glCodeId=(select id from CChartOfAccounts where glcode=?)", voucherDetails.getGlcodeDetail());
            if (null != chartOfAccountDetail) {
                accountDetailMap = new HashMap<>();
                if (repeatedglCodes.contains(voucherDetails.getGlcodeIdDetail().toString()))
                    /*
                     * if(voucherHeader.getIsRestrictedtoOneFunctionCenter()){ accountDetailMap.put("glcodeId-funcId",
                     * voucherDetails.getGlcodeIdDetail()+'-'+voucherHeader.getFunctionId()); }else{
                     */
                    accountDetailMap.put("glcodeId-funcId",
                            voucherDetails.getGlcodeIdDetail().toString() + '-' + voucherDetails.getFunctionIdDetail().toString());
                // }
                else
                    accountDetailMap.put("glcodeId-funcId", voucherDetails.getGlcodeIdDetail().toString().concat("-0"));
                accountDetailMap.put("glcode", voucherDetails.getGlcodeDetail());
                if (voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0)
                    accountDetailMap.put("amount", voucherDetails.getCreditAmountDetail());
                else if (voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0)
                    accountDetailMap.put("amount", voucherDetails.getDebitAmountDetail());
                subLegAccMap.add(accountDetailMap);
            }
        }

        final Map<String, BigDecimal> subledAmtmap = new HashMap<String, BigDecimal>();
        final Map<String, String> subLedgerMap = new HashMap<String, String>();
        for (final VoucherDetails voucherDetails : subLedgerlist) {
            if (voucherDetails.getGlcode() == null) {
                addActionError(getText("journalvoucher.acccode.missing",
                        new String[] { voucherDetails.getSubledgerCode() }));
                return true;
            }
            if (voucherDetails.getGlcode().getId() != 0) {
                final String function = repeatedglCodes.contains(voucherDetails.getGlcode().getId().toString()) ? voucherDetails
                        .getFunctionDetail() : "0";
                // above is used to check if this account code is used multiple times in the account grid or not, if it used
                // multiple times
                // then take the function into consideration while calculating the total sl amount , else igone the function by
                // paasing function value=0
                if (null != subledAmtmap.get(voucherDetails.getGlcode().getId().toString() + '-' + function)) {
                    final BigDecimal debitTotalAmount = subledAmtmap.get(voucherDetails.getGlcode().getId().toString() + '-' + function)
                            .add(voucherDetails.getAmount());
                    subledAmtmap.put(voucherDetails.getGlcode().getId().toString() + '-' + function, debitTotalAmount);
                } else
                    subledAmtmap.put(voucherDetails.getGlcode().getId().toString() + '-' + function
                            , voucherDetails.getAmount());
                final StringBuffer subledgerDetailRow = new StringBuffer();
                if (voucherDetails.getDetailType().getId() == 0 || null == voucherDetails.getDetailKeyId()) {
                    addActionError(getText("journalvoucher.subledger.entrymissing",
                            new String[] { voucherDetails.getSubledgerCode() }));
                    return true;
                } else
                    subledgerDetailRow.append(voucherDetails.getGlcode().getId().toString()).
                            append(voucherDetails.getDetailType().getId().toString()).
                            append(voucherDetails.getDetailKeyId().toString()).
                            append(voucherDetails.getFunctionDetail());
                if (null == subLedgerMap.get(subledgerDetailRow.toString()))
                    subLedgerMap.put(subledgerDetailRow.toString(), subledgerDetailRow.toString());
                else {
                    addActionError(getText("journalvoucher.samesubledger.repeated"));
                    return true;
                }

            }
        }
        if (subLegAccMap.size() > 0)
            for (final Map<String, Object> map : subLegAccMap) {
                final String glcodeIdAndFuncId = map.get("glcodeId-funcId").toString();
                if (null == subledAmtmap.get(glcodeIdAndFuncId)) {
                    final String functionId = glcodeIdAndFuncId.split("-")[1];
                    if (functionId.equalsIgnoreCase("0"))
                        addActionError(getText("journalvoucher.subledger.entrymissing", new String[] { map.get("glcode")
                                .toString() }));
                    else {
                        final CFunction function = null;// (CFunction)persistenceService.get(CFunction.class,Long.valueOf(functionId));
                        addActionError(getText("journalvoucher.subledger.entrymissingFunc", new String[] {
                                map.get("glcode").toString(), function.getName() }));
                    }
                    return true;
                } else if (subledAmtmap.get(glcodeIdAndFuncId).compareTo(new BigDecimal(map.get("amount")
                        .toString())) != 0) {
                    final String functionId = glcodeIdAndFuncId.split("-")[1];
                    if (functionId.equalsIgnoreCase("0"))
                        addActionError(getText("journalvoucher.subledger.amtnotmatchinng", new String[] { map.get("glcode")
                                .toString() }));
                    else {
                        final CFunction function = (CFunction) persistenceService.find("from CFunction where id=?",
                                Long.valueOf(functionId));
                        addActionError(getText("journalvoucher.subledger.amtnotmatchinngFunc", new String[] {
                                map.get("glcode").toString(), function.getName() }));
                    }
                    return true;
                }
            }

        final StringBuffer fyQuery = new StringBuffer();
        fyQuery.append("from CFinancialYear where isActiveForPosting=true and startingDate <= '").
                append(Constants.DDMMYYYYFORMAT1.format(voucherHeader.getVoucherDate())).append("' AND endingDate >='")
                .append(Constants.DDMMYYYYFORMAT1.format(voucherHeader.getVoucherDate())).append("'");
        final List<CFinancialYear> list = persistenceService.findAllBy(fyQuery.toString());
        if (list.size() == 0) {
            addActionError(getText("journalvoucher.fYear.notActive"));
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    protected void loadBankAccountNumber(final ContraBean contraBean) {
        if (null != contraBean.getBankBranchId() && !contraBean.getBankBranchId().equalsIgnoreCase("-1")) {
            final int index1 = contraBean.getBankBranchId().indexOf('-');
            final Integer branchId = Integer.valueOf(contraBean.getBankBranchId().substring(index1 + 1,
                    contraBean.getBankBranchId().length()));
            final List<Bankaccount> bankAccountList = getPersistenceService().findAllBy(
                    "from Bankaccount ba where ba.bankbranch.id=? " +
                            "  and isactive=true order by id", branchId);
            addDropdownData("accNumList", bankAccountList);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Account number list size " + bankAccountList.size());
        }

    }

    @SuppressWarnings("unchecked")
    protected void loadBankAccountNumber(final String bankBranchId) {
        if (null != bankBranchId && !bankBranchId.equalsIgnoreCase("-1")) {
            final int index1 = bankBranchId.indexOf('-');
            final Integer branchId = Integer.valueOf(bankBranchId.substring(index1 + 1, bankBranchId.length()));
            final List<Bankaccount> bankAccountList = getPersistenceService().findAllBy(
                    "from Bankaccount ba where ba.bankbranch.id=? " +
                            "  and isactive=true order by id", branchId);
            addDropdownData("accNumList", bankAccountList);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Account number list size " + bankAccountList.size());
        }

    }

    @SuppressWarnings("unchecked")
    public void loadBankBranchForFund() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BaseVoucherAction | loadBankBranchForFund | Start");
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("FUND ID = " + voucherHeader.getFundId().getId());
            final List<Object[]> bankBranch = getPersistenceService()
                    .findAllBy(
                            "select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),bankBranch.branchname) as bankbranchname "
                                    +
                                    " FROM Bank bank,Bankbranch bankBranch,Bankaccount bankaccount "
                                    +
                                    " where  bank.isactive=true  and bankBranch.isactive=true and bank.id = bankBranch.bank.id and bankBranch.id = bankaccount.bankbranch.id"
                                    +
                                    " and bankaccount.fund.id=?", voucherHeader.getFundId().getId());

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankBranch.size());
            final List<Map<String, Object>> bankBranchList = new ArrayList<>();
            Map<String, Object> bankBrmap;
            for (final Object[] element : bankBranch) {
                bankBrmap = new HashMap<>();
                bankBrmap.put("bankBranchId", element[0].toString());
                bankBrmap.put("bankBranchName", element[1].toString());
                bankBranchList.add(bankBrmap);
            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank branch list size :" + bankBranchList.size());
            addDropdownData("bankList", bankBranchList);
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));

        }
    }

    public boolean isFieldMandatory(final String field) {
        return mandatoryFields.contains(field);
    }

    public boolean shouldShowHeaderField(final String field) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside shouldShowHeaderField menthod");
        if (field.equals("vouchernumber"))
        {
            String vNumGenMode;
            if (voucherHeader.getType() != null && voucherHeader.getType().equalsIgnoreCase("Journal Voucher"))
                vNumGenMode = voucherTypeForULB.readVoucherTypes("Journal");
            else
                vNumGenMode = voucherTypeForULB.readVoucherTypes(voucherHeader.getType());
            if (!"Auto".equalsIgnoreCase(vNumGenMode)) {
                mandatoryFields.add("vouchernumber");
                return true;
            } else
                return false;

        }
        else
        {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Header field contains = " + headerFields.contains(field));
            return headerFields.contains(field);
        }
    }

    public String getTransactionType()
    {
        return voucherHeader.getType();
    }

    public List<String> getHeaderFields() {
        return headerFields;
    }

    public void setHeaderFields(final List<String> headerFields) {
        this.headerFields = headerFields;
    }

    public List<String> getMandatoryFields() {
        return mandatoryFields;
    }

    public void setMandatoryFields(final List<String> mandatoryFields) {
        this.mandatoryFields = mandatoryFields;
    }

    public CVoucherHeader getVoucherHeader() {
        return voucherHeader;
    }

    public void setVoucherHeader(final CVoucherHeader voucherHeader) {
        this.voucherHeader = voucherHeader;
    }

    public String getVoucherNumManual() {
        return voucherNumManual;
    }

    public void setVoucherNumManual(final String voucherNumManual) {
        this.voucherNumManual = voucherNumManual;
    }

    protected void removeEmptyRowsAccoutDetail(final List list) {
        for (final Iterator<VoucherDetails> detail = list.iterator(); detail.hasNext();) {
            final VoucherDetails next = detail.next();
            if (next == null)
                detail.remove();
            else {
                if (next.getDebitAmountDetail() == null)
                    next.setDebitAmountDetail(BigDecimal.ZERO);
                if (next.getCreditAmountDetail() == null)
                    next.setCreditAmountDetail(BigDecimal.ZERO);
                if ((next.getGlcodeDetail() == null || StringUtils.isEmpty(next.getGlcodeDetail()))
                        && (next.getFunctionDetail() == null || StringUtils.isEmpty(next.getFunctionDetail()))
                        && next.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0
                        && next.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0)
                    detail.remove();
            }
        }
    }

    protected void removeEmptyRowsSubledger(final List<VoucherDetails> list) {
        for (final Iterator<VoucherDetails> detail = list.iterator(); detail.hasNext();) {
            final VoucherDetails next = detail.next();
            if (next != null && (next.getSubledgerCode() == null || next.getSubledgerCode().equals("")))
                detail.remove();
            else if (next == null)
                detail.remove();

        }

    }

    public void saveReverse(final String voucherName, final String type) {
        CVoucherHeader reversalVoucher = null;
        final HashMap<String, Object> reversalVoucherMap = new HashMap<>();
        reversalVoucherMap.put("Original voucher header id", voucherHeader.getId());
        reversalVoucherMap.put("Reversal voucher type", type);
        reversalVoucherMap.put("Reversal voucher name", voucherName);
        reversalVoucherMap.put("Reversal voucher date", getReversalVoucherDate());
        reversalVoucherMap.put("Reversal voucher number", getReversalVoucherNumber());
        final List<HashMap<String, Object>> reversalList = new ArrayList<>();
        reversalList.add(reversalVoucherMap);
        try {
            reversalVoucher = createVoucher.reverseVoucher(reversalList);
        } catch (final ValidationException e) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("ERROR in Reversing voucher" + e.getMessage());
            addActionError(getText(e.getErrors().get(0).getMessage()));
            return;
        } catch (final ApplicationRuntimeException e) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("ERROR " + e.getMessage());
            addActionError(getText(e.getMessage()));
            return;
        } catch (final ParseException e) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("ERROR " + e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError("invalid.date.format", "invalid.date.format")));
        }
        addActionMessage(getText("transaction.success") + reversalVoucher.getVoucherNumber());
        voucherHeader = reversalVoucher;
    }

    /**
     *
     */
    protected void loadDefalutDates() {
        final Date currDate = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            voucherHeader.setVoucherDate(sdf.parse(sdf.format(currDate)));
        } catch (final ParseException e) {
            LOGGER.error("Inside loadDefalutDates" + e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError("Exception while formatting voucher date",
                    "Transaction failed")));
        }
    }

    protected void resetVoucherHeader() {
        // required only for manual voucher number.
        if (null != parameters.get("voucherNumberPrefix")) {
            final String voucherNumeditPart = voucherHeader.getVoucherNumber();
            voucherHeader.setVoucherNumber(parameters.get("voucherNumberPrefix")[0] + voucherNumeditPart);
        }
    }

    public Integer getVoucherNumberPrefixLength() {
        voucherNumberPrefixLength = Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH);
        return voucherNumberPrefixLength;
    }

    public void setVoucherNumberPrefixLength(final Integer voucherNumberPrefixLength) {
        this.voucherNumberPrefixLength = voucherNumberPrefixLength;
    }

    public String getReversalVoucherNumber() {
        return reversalVoucherNumber;
    }

    public void setReversalVoucherNumber(final String reversalVoucherNumber) {
        this.reversalVoucherNumber = reversalVoucherNumber;
    }

    public String getReversalVoucherDate() {
        return reversalVoucherDate;
    }

    public void setReversalVoucherDate(final String reversalVoucherDate) {
        this.reversalVoucherDate = reversalVoucherDate;
    }

    public void setFinancingSourceService(
            final FinancingSourceService financingSourceService) {
        this.financingSourceService = financingSourceService;
    }

    protected Boolean validateOwner(final State state)
    {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("validating owner for user " + ApplicationThreadLocals.getUserId());
        List<Position> positionsForUser = null;
        positionsForUser = eisService.getPositionsForUser(ApplicationThreadLocals.getUserId(), new Date());
        if (positionsForUser.contains(state.getOwnerPosition()))
        {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Valid Owner :return true");
            return true;
        } else
        {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Invalid  Owner :return false");
            return false;
        }
    }

    public void setEisService(final EisUtilService eisService) {
        this.eisService = eisService;
    }

    public EisUtilService getEisService() {
        return eisService;
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

    public AssignmentService getAssignmentService() {
        return assignmentService;
    }

    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public FinancingSourceService getFinancingSourceService() {
        return financingSourceService;
    }

}