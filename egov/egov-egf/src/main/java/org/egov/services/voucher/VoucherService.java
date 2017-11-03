/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
package org.egov.services.voucher;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFiscalPeriod;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgfRecordStatus;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.AccountdetailtypeHibernateDAO;
import org.egov.commons.dao.ChartOfAccountsDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.dao.FunctionDAO;
import org.egov.commons.service.ChartOfAccountDetailService;
import org.egov.commons.utils.EntityType;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dao.budget.BudgetDetailsHibernateDAO;
import org.egov.dao.voucher.VoucherHibernateDAO;
import org.egov.egf.autonumber.JVBillNumberGenerator;
import org.egov.egf.commons.EgovCommon;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.EmployeeView;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.entity.Script;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EGovConfig;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBillSubType;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.payment.Paymentheader;
import org.egov.model.voucher.VoucherDetails;
import org.egov.model.voucher.VoucherTypeBean;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.services.bills.EgBillRegisterService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.GLEngine.Transaxtion;
import com.exilant.GLEngine.TransaxtionParameter;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.transactions.VoucherTypeForULB;

@Service
public class VoucherService extends PersistenceService<CVoucherHeader, Long> {
    private static final Logger LOGGER = Logger.getLogger(VoucherService.class);
    @Autowired
    @Qualifier("persistenceService")
    protected PersistenceService persistenceService;
    @Autowired
    @Qualifier("eisCommonService")
    protected EisCommonService eisCommonService;
    @Autowired
    @Qualifier("budgetDetailsDAO")
    private BudgetDetailsDAO budgetDetailsDAO;
    private @Autowired AppConfigValueService appConfigValuesService;
    @Autowired
    @Qualifier("voucherHibDAO")
    private VoucherHibernateDAO voucherHibDAO;
    @Autowired
    private ChartOfAccountsDAO coaDAO;
    @Autowired
    private VoucherTypeForULB voucherTypeForULB;
    @Autowired
    private FunctionDAO functionDAO;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    @Qualifier("voucherHelper")
    private VoucherHelper voucherHelper;

    @Autowired
    @Qualifier("eGovernCommon")
    private EGovernCommon eGovernCommon;

    @Autowired
    @Qualifier("financialYearDAO")
    private FinancialYearHibernateDAO financialYearDAO;
    @Autowired
    private EgovCommon egovCommon;
    @Autowired
    private DesignationService designationService;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    private EisUtilService eisService;

    private EmployeeServiceOld employeeService;

    @Autowired
    private ScriptService scriptService;

    @Autowired
    @Qualifier("egBillRegisterService")
    private EgBillRegisterService egBillRegisterService;

    @Autowired
    private AccountdetailtypeHibernateDAO accountdetailtypeHibernateDAO;

    @Autowired
    @Qualifier("recordStatusPersistenceService")
    private PersistenceService recordStatusPersistenceService;
    
    @Autowired
    private ChartOfAccountDetailService chartOfAccountDetailService;

    public VoucherService(final Class<CVoucherHeader> voucherHeader) {
        super(voucherHeader);
    }

    public VoucherService() {
        super(CVoucherHeader.class);
    }

    public Boundary getBoundaryForUser(final CVoucherHeader rv) {
        return egovCommon.getBoundaryForUser(rv.getCreatedBy());
    }

    public String getEmployeeNameForPositionId(final Position pos) {
        final Assignment assignment = assignmentService.getAssignmentsForPosition(
                +pos.getId(), new Date()).get(0);
        return assignment.getEmployee().getName() + " ("
                + assignment.getDesignation().getName() + ")";
    }

    public Department getCurrentDepartment() {
        final Assignment assignment = eisCommonService
                .getLatestAssignmentForEmployeeByDate(
                        ApplicationThreadLocals.getUserId(), new Date());
        return assignment.getDepartment();
    }

    public Department getDepartmentForWfItem(final CVoucherHeader cv) {
        final Assignment assignment = eisCommonService
                .getLatestAssignmentForEmployeeByDate(
                        cv.getCreatedBy().getId(), new Date());
        return assignment.getDepartment();
    }

    public Department getTempDepartmentForWfItem(final CVoucherHeader cv,
            final Position position) {
        Department d = null;
        eisCommonService.getEmployeeByUserId(cv.getCreatedBy().getId());
        d = (Department) persistenceService
                .find("select v.deptId from EmployeeView v left join v.userMaster  as user where v.isPrimary=true and user.id=?",
                        ApplicationThreadLocals.getUserId());
        return d;
    }

    public Department getDepartmentForUser(final User user) {
        return egovCommon.getDepartmentForUser(user, eisCommonService,
                employeeService, persistenceService);
    }

    public PersonalInformation getEmpForCurrentUser() {
        return eisCommonService.getEmployeeByUserId(ApplicationThreadLocals
                .getUserId());
    }

    public Position getPositionForWfItem(final CVoucherHeader rv) {
        return eisCommonService.getPositionByUserId(rv.getCreatedBy().getId());
    }

    public boolean budgetaryCheck(final EgBillregister billregister) {
        final Map<String, Object> paramMap = new HashMap<>();
        CChartOfAccounts coa = null;
        boolean result = false;
        paramMap.put("asondate", billregister.getBilldate());
        if (billregister.getEgBillregistermis().getScheme() != null)
            paramMap.put("schemeid", billregister.getEgBillregistermis()
                    .getScheme().getId());
        if (billregister.getEgBillregistermis().getSubScheme() != null)
            paramMap.put("subschemeid", billregister.getEgBillregistermis()
                    .getSubScheme().getId());
        if (billregister.getEgBillregistermis().getFieldid() != null)
            paramMap.put("boundaryid", billregister.getEgBillregistermis()
                    .getFieldid().getId());
        if (billregister.getEgBillregistermis().getEgDepartment() != null)
            paramMap.put("deptid", billregister.getEgBillregistermis()
                    .getEgDepartment().getId());
        if (billregister.getEgBillregistermis().getFunctionaryid() != null)
            paramMap.put("functionaryid", billregister.getEgBillregistermis()
                    .getFunctionaryid().getId());
        if (billregister.getEgBillregistermis().getFund() != null)
            paramMap.put("fundid", billregister.getEgBillregistermis()
                    .getFund().getId());
        paramMap.put("mis.budgetcheckreq", billregister.getEgBillregistermis()
                .isBudgetCheckReq());
        for (final EgBilldetails detail : billregister.getEgBilldetailes()) {
            paramMap.put("debitAmt", detail.getDebitamount());
            paramMap.put("creditAmt", detail.getCreditamount());
            coa = (CChartOfAccounts) persistenceService.find(
                    " from CChartOfAccounts where id=?",
                    Long.valueOf(detail.getGlcodeid().toString()));
            paramMap.put("glcode", coa.getGlcode());
            if (detail.getFunctionid() != null)
                paramMap.put("functionid",
                        Long.valueOf(detail.getFunctionid().toString()));
            paramMap.put("bill", billregister);
            try {
                result = budgetDetailsDAO.budgetaryCheckForBill(paramMap);
            } catch (final ValidationException e) {
                throw new ValidationException("", "Budget Check failed for "
                        + coa.getGlcode());
            }

            if (!result)
                throw new ValidationException("", "Budget Check failed for "
                        + coa.getGlcode());
        }
        return result;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public Map<String, Object> getDesgByDeptAndTypeAndVoucherDate(
            final String type, final String scriptName, final Date vouDate,
            final Paymentheader paymentheader) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Voucher Service | getDesgUserByDeptAndDesgName | Start");
        final Map<String, Object> map = new HashMap<>();
        Designation designation;
        final Double grossAmount = getJVsGrassAmount(paymentheader);
        final List<String> list = (List<String>) scriptService.executeScript(
                scriptName, ScriptService.createContext("eisCommonServiceBean",
                        eisCommonService, "grossAmount", grossAmount, "userId",
                        ApplicationThreadLocals.getUserId().intValue(), "DATE",
                        new Date(), "type", type, "vouDate", vouDate.getTime(),
                        "paymentheader", paymentheader));
        Map<String, Object> desgFuncryMap;
        final List<Map<String, Object>> designationList = new ArrayList<>();
        for (final String desgFuncryName : list)
            if (desgFuncryName.trim().length() != 0
                    && !desgFuncryName.equalsIgnoreCase("END")) {
                desgFuncryMap = new HashMap<>();
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Designation and Functionary  Name  = "
                            + desgFuncryName);
                designation = designationService
                        .getDesignationByName(desgFuncryName
                                .substring(desgFuncryName.indexOf('-') + 1));
                desgFuncryMap.put("designationName",
                        desgFuncryName
                                .substring(0, desgFuncryName.indexOf('-'))
                                .split("~")[0]
                                + "-" + designation.getName());
                desgFuncryMap.put(
                        "designationId",
                        designation.getId()
                                + "-"
                                + desgFuncryName.substring(0,
                                        desgFuncryName.indexOf('-')));
                designationList.add(desgFuncryMap);
            } else if (desgFuncryName.equalsIgnoreCase("END"))
                map.put("wfitemstate", "END");
        map.put("designationList", designationList);
        return map;
    }

    public Double getJVsGrassAmount(final Paymentheader paymentheader) {
        if (paymentheader != null) {
            if (!paymentheader
                    .getVoucherheader()
                    .getName()
                    .equalsIgnoreCase(
                            FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK)) {
                final Paymentheader ph = (Paymentheader) persistenceService
                        .find("select ph from Paymentheader ph , Miscbilldetail misc ,CVoucherHeader vh,CGeneralLedger gl where misc.payVoucherHeader = ph.voucherheader and misc.billVoucherHeader = vh and gl.voucherHeaderId = vh and vh.status not in ("
                                + FinancialConstants.CANCELLEDVOUCHERSTATUS
                                + ") and gl.debitAmount > 0  and (gl.glcode like '210%' or gl.glcode like '460%') and ph = ? ",
                                paymentheader);
                if (ph != null)
                    return (double) 0;
                else {
                    final Double grossAmount = (Double) persistenceService
                            .find("select sum(gl.debitAmount) from Paymentheader ph , Miscbilldetail misc ,CVoucherHeader vh,CGeneralLedger gl where misc.payVoucherHeader = ph.voucherheader and misc.billVoucherHeader = vh and gl.voucherHeaderId = vh and vh.status not in ("
                                    + FinancialConstants.CANCELLEDVOUCHERSTATUS
                                    + ") and gl.debitAmount > 0 and ph = ?",
                                    paymentheader);

                    return grossAmount != null ? grossAmount : (double) 0;

                }
            } else {
                final Paymentheader ph = (Paymentheader) persistenceService
                        .find("select ph from Paymentheader ph ,CVoucherHeader vh,CGeneralLedger gl where ph.voucherheader = vh and gl.voucherHeaderId = vh and vh.status not in ("
                                + FinancialConstants.CANCELLEDVOUCHERSTATUS
                                + ") and gl.debitAmount > 0 and (gl.glcode like '210%' or gl.glcode like '460%') and ph = ? ",
                                paymentheader);
                if (ph != null)
                    return (double) 0;
                else {
                    final Double grossAmount = (Double) persistenceService
                            .find("select sum(gl.debitAmount) from Paymentheader ph ,CVoucherHeader vh,CGeneralLedger gl where ph.voucherheader = vh and gl.voucherHeaderId = vh and vh.status not in ("
                                    + FinancialConstants.CANCELLEDVOUCHERSTATUS
                                    + ") and gl.debitAmount > 0 and ph = ?",
                                    paymentheader);

                    return grossAmount != null ? grossAmount : (double) 0;

                }
            }
        } else
            return (double) 0;

    }

    public void createVoucherfromPreApprovedVoucher(final CVoucherHeader vh) {
        final List<AppConfigValues> appList = appConfigValuesService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "APPROVEDVOUCHERSTATUS");
        final String approvedVoucherStatus = appList.get(0).getValue();
        vh.setStatus(Integer.valueOf(approvedVoucherStatus));
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getJournalVouchers(
            final CVoucherHeader voucherHeader,
            final Map<String, Object> searchFilterMap)
            throws ApplicationException, ParseException {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VoucherService | getJournalVouchers | Start");
        final List<CVoucherHeader> vouchers = voucherHibDAO.getVoucherList(
                voucherHeader, searchFilterMap);
        Map<String, Object> voucherMap = null;
        final List<Map<String, Object>> voucherList = new ArrayList<>();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("voucherList size = " + voucherList.size());
        for (final CVoucherHeader voucherheader : vouchers) {
            voucherMap = new HashMap<>();
            BigDecimal amt = BigDecimal.ZERO;
            voucherMap.put("id", voucherheader.getId());
            voucherMap.put("vouchernumber", voucherheader.getVoucherNumber());
            voucherMap.put("type", voucherheader.getType());
            voucherMap.put("voucherdate", voucherheader.getVoucherDate());
            voucherMap.put("fundname", voucherheader.getFundId().getName());
            final Set<CGeneralLedger> vDetailSet = voucherheader
                    .getGeneralledger();
            for (final CGeneralLedger detail : vDetailSet)
                amt = amt.add(new BigDecimal(detail.getDebitAmount()));
            voucherMap.put("amount", amt);
            if (voucherheader.getStatus() != null)
                voucherMap
                        .put("status",
                                voucherheader.getStatus() == 0 ? voucherheader
                                        .getIsConfirmed() == 0 ? "UnConfirmed"
                                                : "Confirmed"
                                        : voucherheader.getStatus() == 1 ? "Reversed"
                                                : voucherheader.getStatus() == 2 ? "Reversal"
                                                        : "");

            voucherList.add(voucherMap);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Total number of vouchers = " + voucherList.size());
        return voucherList;

    }

    public Map<String, Object> getVoucherInfo(final Long voucherId) {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VoucherService | getVoucherDetails | Start");
        final Map<String, Object> voucherMap = new HashMap<>();
        final CVoucherHeader voucherHeader = (CVoucherHeader) persistenceService
                .find("from CVoucherHeader where id=?", voucherId);
        voucherMap.put(Constants.VOUCHERHEADER, voucherHeader);
        final List<CGeneralLedger> glList = voucherHibDAO
                .getGLInfo(voucherHeader.getId());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("total number of general ledger entry "
                    + glList.size());
        final List<VoucherDetails> billDetailslist = new ArrayList<>();
        final List<VoucherDetails> subLedgerlist = new ArrayList<>();
        VoucherDetails voucherDetail;
        VoucherDetails subLedgerDetail;
        BigDecimal crAmount;
        BigDecimal drAmount;
        try {
            for (final CGeneralLedger generalLedger : glList) {
                voucherDetail = new VoucherDetails();
                if (null != generalLedger.getFunctionId()) {
                    voucherDetail.setFunctionIdDetail(Long
                            .valueOf(generalLedger.getFunctionId().toString()));
                    voucherDetail.setFunctionDetail(functionDAO
                            .getFunctionById(
                                    Long.valueOf(generalLedger.getFunctionId()
                                            .toString()))
                            .getName());
                }
                voucherDetail.setGlcodeIdDetail(generalLedger.getGlcodeId()
                        .getId());
                voucherDetail.setGlcodeDetail(generalLedger.getGlcodeId()
                        .getGlcode());

                voucherDetail.setAccounthead(coaDAO
                        .findById(generalLedger.getGlcodeId().getId(), false)
                        .getName());
                drAmount = new BigDecimal(generalLedger.getDebitAmount());
                crAmount = new BigDecimal(generalLedger.getCreditAmount());
                voucherDetail.setDebitAmountDetail(drAmount.setScale(2,
                        BigDecimal.ROUND_HALF_UP));
                voucherDetail.setCreditAmountDetail(crAmount.setScale(2,
                        BigDecimal.ROUND_HALF_UP));
                billDetailslist.add(voucherDetail);

                final List<CGeneralLedgerDetail> gledgerDetailList = voucherHibDAO
                        .getGeneralledgerdetail(generalLedger.getId());

                for (final CGeneralLedgerDetail gledgerDetail : gledgerDetailList) {
                    if (chartOfAccountDetailService.getByGlcodeIdAndDetailTypeId(generalLedger.getGlcodeId().getId(),
                            gledgerDetail.getDetailTypeId().getId().intValue()) != null) {
                        subLedgerDetail = new VoucherDetails();
                        subLedgerDetail.setAmount(gledgerDetail.getAmount()
                                .setScale(2));
                        subLedgerDetail.setGlcode(coaDAO
                                .findById(generalLedger.getGlcodeId().getId(),
                                        false));
                        subLedgerDetail.setSubledgerCode(generalLedger
                                .getGlcodeId().getGlcode());
                        final Accountdetailtype accountdetailtype = voucherHibDAO
                                .getAccountDetailById(gledgerDetail
                                        .getDetailTypeId().getId());
                        subLedgerDetail.setDetailType(accountdetailtype);
                        subLedgerDetail.setDetailTypeName(accountdetailtype
                                .getName());
                        final EntityType entity = voucherHibDAO.getEntityInfo(
                                gledgerDetail.getDetailKeyId(),
                                accountdetailtype.getId());
                        subLedgerDetail.setDetailCode(entity.getCode());
                        subLedgerDetail.setDetailKeyId(gledgerDetail
                                .getDetailKeyId());
                        subLedgerDetail.setDetailKey(entity.getName());
                        subLedgerDetail.setFunctionDetail(generalLedger
                                .getFunctionId() != null ? generalLedger
                                        .getFunctionId().toString() : "0");
                        subLedgerlist.add(subLedgerDetail);
                    }
                }

            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured in VoucherSerive |getVoucherInfo "
                    + e);
        } catch (final Exception e) {
            LOGGER.error("Exception occured in VoucherSerive |getVoucherInfo "
                    + e);
        }

        voucherMap.put(Constants.GLDEATILLIST, billDetailslist);
        /**
         * create empty sub ledger row
         */
        if (subLedgerlist.isEmpty())
            subLedgerlist.add(new VoucherDetails());
        voucherMap.put("subLedgerDetail", subLedgerlist);
        return voucherMap;

    }

    @SuppressWarnings("deprecation")
    public CVoucherHeader updateVoucherHeader(final CVoucherHeader voucherHeader) {
        final String voucherNumType = voucherHeader.getType();
        return updateVoucherHeader(voucherHeader, voucherNumType);
    }

    @SuppressWarnings("deprecation")
    @Transactional
    public CVoucherHeader updateVoucherHeader(
            final CVoucherHeader voucherHeader,
            final VoucherTypeBean voucherTypeBean) {
        String voucherNumType = voucherTypeBean.getVoucherNumType();
        if (voucherTypeBean.getVoucherNumType() == null)
            voucherNumType = voucherHeader.getType();
        return updateVoucherHeader(voucherHeader, voucherNumType);
    }

    @Transactional
    public CVoucherHeader updateVoucherHeader(
            final CVoucherHeader voucherHeader, final String voucherNumType) {
        CVoucherHeader existingVH = null;
        try {
            if (voucherHeader.getId() != null && voucherHeader.getId() != -1)
                existingVH = find("from CVoucherHeader where id=?",
                        voucherHeader.getId());
            existingVH = getUpdatedVNumCGVN(existingVH, voucherHeader,
                    voucherNumType);
            existingVH.setFundId(voucherHeader.getFundId());
            existingVH.getVouchermis().setDepartmentid(
                    voucherHeader.getVouchermis().getDepartmentid());
            existingVH.getVouchermis().setFunction(
                    voucherHeader.getVouchermis().getFunction());
            existingVH.getVouchermis().setSchemeid(
                    voucherHeader.getVouchermis().getSchemeid());
            existingVH.getVouchermis().setSubschemeid(
                    voucherHeader.getVouchermis().getSubschemeid());
            existingVH.getVouchermis().setFunctionary(
                    voucherHeader.getVouchermis().getFunctionary());
            existingVH.getVouchermis().setDivisionid(
                    voucherHeader.getVouchermis().getDivisionid());
            existingVH.getVouchermis().setFundsource(
                    voucherHeader.getVouchermis().getFundsource());
            existingVH.setVoucherDate(voucherHeader.getVoucherDate());
            existingVH.setDescription(voucherHeader.getDescription());
            applyAuditing(existingVH);
            update(existingVH);
        } catch (final HibernateException e) {
            LOGGER.error(e);
            throw new HibernateException(
                    "Exception occured in voucher service while updating voucher header"
                            + e);
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e);

            throw new ApplicationRuntimeException(
                    "Exception occured in voucher service while updating voucher header"
                            + e);
        }
        return existingVH;
    }

    public CVoucherHeader getUpdatedVNumCGVN(final CVoucherHeader existingVH,
            final CVoucherHeader voucherHeader, String voucherNumType) {

        String autoVoucherType = null;
        if (voucherNumType.equalsIgnoreCase("Journal Voucher"))
            voucherNumType = "Journal";
        autoVoucherType = existingVH
                .getVoucherNumber()
                .substring(
                        Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH)
                                - Integer
                                        .parseInt(FinancialConstants.VOUCHERNO_TYPE_SUBLENGTH),
                        Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("autoVoucherType FOR MODIFIED VOUCHER :"
                    + autoVoucherType);

        final String vNumGenMode = voucherTypeForULB
                .readVoucherTypes(voucherNumType);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("new fund id :" + voucherHeader.getFundId().getId());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("old fund id :" + existingVH.getFundId().getId());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("new voucher date :" + voucherHeader.getVoucherDate());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("old voucher date :" + existingVH.getVoucherDate());
        // generating an new voucher number If fund or voucher date is modified.
        // change voucher number only if fund is changed
        // no need to change voucher number if onlydate is changed since
        // vouchernumber-sequence is for the year not for month
        try {
            final String fiscalPeriodIdStr = eGovernCommon
                    .getFiscalPeriod(DateUtils.getFormattedDate(voucherHeader.getVoucherDate(), "dd-MMM-yyyy"));
            if (null == fiscalPeriodIdStr)
                throw new ApplicationRuntimeException(
                        "Voucher Date not within an open period or Financial year not open for posting, fiscalPeriod := "
                                + fiscalPeriodIdStr);
            voucherHeader
                    .setFiscalPeriodId(Integer.parseInt(fiscalPeriodIdStr));
            if (!voucherHeader.getFundId().equals(existingVH.getFundId())) {

                final String strVoucherNumber = voucherHelper
                        .getGeneratedVoucherNumber(voucherHeader.getFundId()
                                .getId(), autoVoucherType, voucherHeader
                                        .getVoucherDate(),
                                vNumGenMode, voucherHeader
                                        .getVoucherNumber());
                existingVH.setVoucherNumber(strVoucherNumber);
                final String vType = voucherHeader.getFundId().getIdentifier()
                        .toString()
                        + "/" + getCgnType(voucherHeader.getType()) + "/CGVN";
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Voucher type  : " + vType);
                String eg_voucher = voucherHelper.getEg_Voucher(vType,
                        existingVH.getFiscalPeriodId().toString());
                for (int i = eg_voucher.length(); i < 10; i++)
                    eg_voucher = "0" + eg_voucher;
                existingVH.setCgvn(vType + eg_voucher);

            } else if (!voucherHeader.getVoucherDate().equals(
                    existingVH.getVoucherDate())) {
                // A financial Year can have multiple fiscalPeriod so comparing
                // previous and new financial year id
                final CFiscalPeriod fiscalPeriod = (CFiscalPeriod) persistenceService
                        .find(" from CFiscalPeriod where id=?",
                                Long.valueOf(fiscalPeriodIdStr));
                final String financialYearId = financialYearDAO
                        .getFinancialYearId(Constants.DDMMYYYYFORMAT2
                                .format(existingVH.getVoucherDate()));
                final CFinancialYear financialYear = financialYearDAO
                        .getFinancialYearById(Long.valueOf(financialYearId));
                if (existingVH.getFiscalPeriodId().equals(
                        voucherHeader.getFiscalPeriodId())
                        && fiscalPeriod.getcFinancialYear().getId().longValue() == financialYear
                                .getId().longValue()) {
                    final String vDate = Constants.DDMMYYYYFORMAT2
                            .format(voucherHeader.getVoucherDate());
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Voucher Number  : "
                                + existingVH.getVoucherNumber());
                    final String strTempVoucherNumber = existingVH
                            .getVoucherNumber();
                    String strVoucherNumber = "";
                    final String newDate[] = vDate.split("/");
                    final String voucherArr[] = strTempVoucherNumber.split("/");
                    strVoucherNumber = voucherArr[0] + "/" + voucherArr[1]
                            + "/" + voucherArr[2] + "/" + newDate[1] + "/"
                            + voucherArr[4];
                    existingVH.setVoucherNumber(strVoucherNumber);
                } else {
                    final String strVoucherNumber = voucherHelper
                            .getGeneratedVoucherNumber(voucherHeader
                                    .getFundId().getId(), autoVoucherType,
                                    voucherHeader.getVoucherDate(),
                                    vNumGenMode, voucherHeader
                                            .getVoucherNumber());
                    existingVH.setVoucherNumber(strVoucherNumber);
                    final String vType = voucherHeader.getFundId()
                            .getIdentifier().toString()
                            + "/"
                            + getCgnType(voucherHeader.getType())
                            + "/CGVN";
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Voucher type  : " + vType);
                    String eg_voucher = voucherHelper.getEg_Voucher(vType,
                            existingVH.getFiscalPeriodId().toString());
                    for (int i = eg_voucher.length(); i < 10; i++)
                        eg_voucher = "0" + eg_voucher;
                    existingVH.setCgvn(vType + eg_voucher);

                }
            }
            // If only the voucher number is modified then just appending the
            // manual voucher number
            else if ("Manual".equalsIgnoreCase(vNumGenMode)
                    && !existingVH
                            .getVoucherNumber()
                            .substring(
                                    Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH))
                            .equalsIgnoreCase(voucherHeader.getVoucherNumber())) {
                final String strVoucherNumber = voucherHelper
                        .getGeneratedVoucherNumber(voucherHeader.getFundId()
                                .getId(), autoVoucherType, voucherHeader
                                        .getVoucherDate(),
                                vNumGenMode, voucherHeader
                                        .getVoucherNumber());
                // existingVH.setVoucherNumber(existingVH.getVoucherNumber().substring(0,
                // Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH))+voucherHeader.getVoucherNumber());
                existingVH.setVoucherNumber(strVoucherNumber);

            }
            // conn.close();
        } catch (final Exception e) {
            LOGGER.error(e);
            throw new ApplicationRuntimeException(
                    "Exception occured while getting upadetd voucher number and cgvn number"
                            + e);
        }

        return existingVH;
    }

    @Transactional
    public void deleteGLDetailByVHId(final Object voucherHeaderId) {
        voucherHibDAO.deleteGLDetailByVHId(voucherHeaderId);
    }

    /**
     * Post into Voucher detail and create transaction list for posting into GL.
     *
     * @param billDetailslist
     * @param subLedgerlist
     * @param voucherHeader
     * @return
     */
    public List<Transaxtion> postInTransaction(
            final List<VoucherDetails> billDetailslist,
            final List<VoucherDetails> subLedgerlist,
            final CVoucherHeader voucherHeader) {
        final List<Transaxtion> transaxtionList = new ArrayList<>();
        final String accDetailFunc = "";
        String detailedFunc = "";
        Integer voucherLineId = 1;
        final List<String> repeatedglCodes = VoucherHelper
                .getRepeatedGlcodes(billDetailslist);
        try {
            for (final VoucherDetails accountDetails : billDetailslist) {
                final String glcodeId = accountDetails.getGlcodeIdDetail()
                        .toString();
                /*
                 * VoucherDetail voucherDetail = new VoucherDetail(); voucherDetail.setLineId(lineId++);
                 * voucherDetail.setVoucherHeaderId(voucherHeader); voucherDetail.setGlCode(accountDetails.getGlcodeDetail());
                 * voucherDetail .setAccountName(accountDetails.getAccounthead()); voucherDetail
                 * .setDebitAmount(accountDetails.getDebitAmountDetail()); voucherDetail
                 * .setCreditAmount(accountDetails.getCreditAmountDetail());
                 * voucherDetail.setNarration(voucherHeader.getDescription()); voucherHibDAO.postInVoucherDetail(voucherDetail);
                 */

                final Transaxtion transaction = new Transaxtion();
                transaction.setGlCode(accountDetails.getGlcodeDetail());
                transaction.setGlName(accountDetails.getAccounthead());
                transaction.setVoucherLineId(String.valueOf(voucherLineId++));
                transaction
                        .setVoucherHeaderId(voucherHeader.getId().toString());
                transaction.setCrAmount(accountDetails.getCreditAmountDetail()
                        .toString());
                transaction.setDrAmount(accountDetails.getDebitAmountDetail()
                        .toString());
                if (null != accountDetails.getFunctionIdDetail())
                    transaction.setFunctionId(accountDetails
                            .getFunctionIdDetail().toString());
                /*
                 * if(null!=voucherHeader.getIsRestrictedtoOneFunctionCenter() &&
                 * voucherHeader.getIsRestrictedtoOneFunctionCenter()){ if(null != voucherHeader.getFunctionId()){
                 * transaction.setFunctionId (voucherHeader.getFunctionId().toString()); } if(null !=
                 * voucherHeader.getVouchermis().getFunction()){ transaction.setFunctionId
                 * (voucherHeader.getVouchermis().getFunction ().getId().toString()); } }else{ accDetailFunc = accountDetails
                 * .getFunctionIdDetail()!=null?accountDetails.getFunctionIdDetail ().toString():"0"; }
                 */
                final ArrayList reqParams = new ArrayList();
                for (final VoucherDetails subledgerDetails : subLedgerlist) {

                    final String detailGlCode = subledgerDetails.getGlcode()
                            .getId().toString();
                    if (null != voucherHeader
                            .getIsRestrictedtoOneFunctionCenter()
                            && voucherHeader
                                    .getIsRestrictedtoOneFunctionCenter())
                        detailedFunc = voucherHeader.getVouchermis()
                                .getFunction().toString();
                    else
                        detailedFunc = subledgerDetails.getFunctionDetail();
                    final String detailtypeid = subledgerDetails
                            .getDetailType().getId().toString();
                    if (glcodeId.equals(detailGlCode)
                            && (repeatedglCodes.contains(glcodeId) ? accDetailFunc
                                    .equals(detailedFunc) : true)) {
                        final TransaxtionParameter reqData = new TransaxtionParameter();
                        final Accountdetailtype adt = accountdetailtypeHibernateDAO
                                .findById(Integer.valueOf(detailtypeid), false);
                        reqData.setDetailName(adt.getAttributename());
                        reqData.setGlcodeId(detailGlCode);
                        reqData.setDetailAmt(subledgerDetails.getAmount()
                                .toString());
                        reqData.setDetailKey(subledgerDetails.getDetailKeyId()
                                .toString());
                        reqData.setDetailTypeId(detailtypeid);
                        reqParams.add(reqData);
                    }

                }
                if (reqParams != null && !reqParams.isEmpty())
                    transaction.setTransaxtionParam(reqParams);
                transaxtionList.add(transaction);
            }
        } catch (final Exception e) {
            LOGGER.error("Exception occured while posting data into voucher detail and transaction");
            throw new ApplicationRuntimeException(
                    "Exception occured while posting data into voucher detail and transaction"
                            + e.getMessage());
        }

        return transaxtionList;

    }

    public void setVoucherHeaderDetails(final CVoucherHeader voucherHeader,
            final VoucherTypeBean voucherTypeBean) throws Exception {
        voucherHeader.setName(voucherTypeBean.getVoucherName());
        voucherHeader.setType(voucherTypeBean.getVoucherType());
        String vNumGenMode = null;
        if (null != voucherHeader.getType()
                && FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL
                        .equalsIgnoreCase(voucherHeader.getType()))
            vNumGenMode = voucherTypeForULB.readVoucherTypes("Journal");
        else
            vNumGenMode = voucherTypeForULB.readVoucherTypes(voucherTypeBean
                    .getVoucherNumType());
        final String autoVoucherType = EGovConfig.getProperty(
                FinancialConstants.APPLCONFIGNAME, voucherTypeBean
                        .getVoucherNumType().toLowerCase(),
                "",
                FinancialConstants.CATEGORYFORVNO);
        final String vocuherNumber = voucherHelper.getGeneratedVoucherNumber(
                voucherHeader.getFundId().getId(), autoVoucherType,
                voucherHeader.getVoucherDate(), vNumGenMode,
                voucherHeader.getVoucherNumber());
        voucherHeader.setVoucherNumber(vocuherNumber);

    }

    public CVoucherHeader postIntoVoucherHeader(
            final CVoucherHeader voucherHeader,
            final VoucherTypeBean voucherTypeBean) throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("start | insertIntoVoucherHeader");
        voucherHeader.setName(voucherTypeBean.getVoucherName());
        voucherHeader.setType(voucherTypeBean.getVoucherType());
        String vNumGenMode = null;
        if (null != voucherHeader.getType()
                && FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL
                        .equalsIgnoreCase(voucherHeader.getType()))
            vNumGenMode = voucherTypeForULB.readVoucherTypes("Journal");
        else
            vNumGenMode = voucherTypeForULB.readVoucherTypes(voucherTypeBean
                    .getVoucherNumType());
        final String autoVoucherType = EGovConfig.getProperty(
                FinancialConstants.APPLCONFIGNAME, voucherTypeBean
                        .getVoucherNumType().toLowerCase(),
                "",
                FinancialConstants.CATEGORYFORVNO);
        final String vocuherNumber = voucherHelper.getGeneratedVoucherNumber(
                voucherHeader.getFundId().getId(), autoVoucherType,
                voucherHeader.getVoucherDate(), vNumGenMode,
                voucherHeader.getVoucherNumber());
        voucherHeader.setVoucherNumber(vocuherNumber);
        /*
         * if("Auto".equalsIgnoreCase(vNumGenMode)){ if(LOGGER.isDebugEnabled()) LOGGER.debug("Generating auto voucher number");
         * String vDate = Constants.DDMMYYYYFORMAT2.format(voucherHeader.getVoucherDate()); //String vn1 =
         * getGeneratedVoucherNumber(voucherHeader.getFundId().getId(), autoVoucherType, voucherHeader.getVoucherDate());
         * voucherHeader .setVoucherNumber (cmImpl.getTxnNumber(voucherHeader.getFundId().getId(
         * ).toString(),autoVoucherType,vDate,conn)); } else { Query query=getSession ().createQuery(
         * "select f.identifier from Fund f where id=:fundId"); query.setInteger("fundId", voucherHeader.getFundId().getId());
         * String fundIdentifier = query.uniqueResult().toString(); //String vn2 = getFormattedManualVoucherNumber(fundIdentifier,
         * autoVoucherType, voucherHeader.getVoucherNumber()); voucherHeader.setVoucherNumber(new
         * StringBuffer().append(fundIdentifier).append(autoVoucherType). append(voucherHeader.getVoucherNumber()).toString()); }
         */
        try {
            final String vdt = Constants.DDMMYYYYFORMAT1.format(voucherHeader
                    .getVoucherDate());
            String fiscalPeriod = null;
            fiscalPeriod = eGovernCommon.getFiscalPeriod(vdt);
            if (null == fiscalPeriod)
                throw new ApplicationRuntimeException(
                        "Voucher Date not within an open period or Financial year not open for posting, fiscalPeriod := "
                                + fiscalPeriod);
            voucherHeader.setFiscalPeriodId(Integer.valueOf(fiscalPeriod));

            final String vType = voucherHeader.getFundId().getIdentifier()
                    + "/" + getCgnType(voucherHeader.getType()) + "/CGVN";
            String eg_voucher = eGovernCommon
                    .getEg_Voucher(vType, fiscalPeriod);

            for (int i = eg_voucher.length(); i < 10; i++)
                eg_voucher = "0" + eg_voucher;
            final String cgNum = vType + eg_voucher;
            voucherHeader.setCgvn(cgNum);
            voucherHeader.setEffectiveDate(new Date());
            if (!eGovernCommon
                    .isUniqueVN(voucherHeader.getVoucherNumber(), vdt))
                throw new ApplicationRuntimeException(
                        "Duplicate Voucher Number");
            voucherHeader.getVouchermis().setVoucherheaderid(voucherHeader);
            voucherHeader
                    .setStatus(FinancialConstants.PREAPPROVEDVOUCHERSTATUS);
            final List<AppConfigValues> appConfig = appConfigValuesService
                    .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "JournalVoucher_ConfirmonCreate");
            if (null != appConfig && !appConfig.isEmpty())
                for (final AppConfigValues appConfigVal : appConfig)
                    voucherHeader.setIsConfirmed(Integer.valueOf(appConfigVal
                            .getValue()));
            persist(voucherHeader);
            if (!voucherHeader.getType().equalsIgnoreCase(
                    FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL)) {
                final StringBuilder sourcePath = new StringBuilder();
                sourcePath
                        .append(voucherHeader.getVouchermis().getSourcePath())
                        .append(voucherHeader.getId().toString());
                voucherHeader.getVouchermis().setSourcePath(
                        sourcePath.toString());

                update(voucherHeader);
            }

        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e);
            throw new ApplicationRuntimeException(e.getMessage());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("End | insertIntoVoucherHeader");
        return voucherHeader;
    }

    enum voucherTypeEnum {
        JOURNALVOUCHER, CONTRA, RECEIPT, PAYMENT;
    }

    protected String getCgnType(String vType) {
        vType = vType.toUpperCase().replaceAll(" ", "");
        String cgnType = null;
        String typetoCheck = vType;
        if (vType.equalsIgnoreCase("JOURNAL VOUCHER"))
            typetoCheck = "JOURNALVOUCHER";

        switch (voucherTypeEnum.valueOf(typetoCheck.toUpperCase())) {
        case JOURNALVOUCHER:
            cgnType = "JVG";
            break;
        case CONTRA:
            cgnType = "CSL";
            break;
        case RECEIPT:
            cgnType = "MSR";
            break;
        case PAYMENT:
            cgnType = "DBP";
            break;
        }
        return cgnType;
    }

    public void insertIntoRecordStatus(final CVoucherHeader voucherHeader) {
        try {
            final EgfRecordStatus recordStatus = new EgfRecordStatus();
            final String code = EGovConfig.getProperty("egf_config.xml",
                    "confirmoncreate", "", voucherHeader.getType());
            if ("N".equalsIgnoreCase(code))
                recordStatus.setStatus(Integer.valueOf(1));
            else
                recordStatus.setStatus(Integer.valueOf(0));
            recordStatus.setUpdatedtime(new Date());
            recordStatus.setVoucherheader(voucherHeader);
            recordStatus.setRecordType(voucherHeader.getType());
            recordStatus.setUserid(ApplicationThreadLocals.getUserId()
                    .intValue());
            recordStatusPersistenceService.persist(recordStatus);
        } catch (final HibernateException he) {
            LOGGER.error(he.getMessage());
            throw new HibernateException(he);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage());
            throw new HibernateException(e);
        }

    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public Map<String, Object> getDesgByDeptAndType(final String type,
            final String scriptName) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Voucher Service | getDesgUserByDeptAndDesgName | Start");
        final Map<String, Object> map = new HashMap<>();
        final List<Map<String, Object>> designationList = new ArrayList<>();
        /*
         * for (String desgFuncryName : list) { if(desgFuncryName.trim().length()!=0 && !desgFuncryName.equalsIgnoreCase("END")){
         * desgFuncryMap = new HashMap<>(); if(LOGGER.isDebugEnabled()) LOGGER.debug(
         * "Designation and Functionary  Name  = "+ desgFuncryName); try { designation =new
         * DesignationMasterDAO().getDesignationByDesignationName (desgFuncryName.substring(desgFuncryName.indexOf('-')+1));
         * desgFuncryMap.put("designationName", designation.getName()); desgFuncryMap
         * .put("designationId",designation.getId()+"-"+desgFuncryName .substring(0,desgFuncryName.indexOf('-')));
         * designationList.add(desgFuncryMap); } catch (NoSuchObjectException e) { LOGGER.error(e); } }else if
         * (desgFuncryName.equalsIgnoreCase("END")) { map.put("wfitemstate", "END"); } }
         */
        map.put("wfitemstate", "END");
        map.put("designationList", designationList);
        return map;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public Map<String, Object> getDesgByDeptAndTypeAndvouDate(
            final String type, final String scriptName, final Date vouDate) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Voucher Service | getDesgUserByDeptAndDesgName | Start");
        final Map<String, Object> map = new HashMap<>();
        Designation designation = null;
        persistenceService.findAllByNamedQuery(Script.BY_NAME, scriptName).get(
                0);
        final List<String> list = null;/*
                                        * (List<String>) validScript.eval(Script .createContext( "eisCommonServiceBean",
                                        * eisCommonService,"userId", Integer.valueOf (ApplicationThreadLocals
                                        * .getUserId().trim()),"DATE",new Date() ,"type",type,"vouDate",vouDate.getTime ()));
                                        */
        Map<String, Object> desgFuncryMap;
        final List<Map<String, Object>> designationList = new ArrayList<>();
        for (final String desgFuncryName : list)
            if (desgFuncryName.trim().length() != 0
                    && !desgFuncryName.equalsIgnoreCase("END")) {
                desgFuncryMap = new HashMap<>();
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Designation and Functionary  Name  = "
                            + desgFuncryName);
                designation = designationService
                        .getDesignationByName(desgFuncryName
                                .substring(desgFuncryName.indexOf('-') + 1));
                desgFuncryMap.put("designationName", designation.getName());
                desgFuncryMap.put(
                        "designationId",
                        designation.getId()
                                + "-"
                                + desgFuncryName.substring(0,
                                        desgFuncryName.indexOf('-')));
                designationList.add(desgFuncryMap);
            } else if (desgFuncryName.equalsIgnoreCase("END"))
                map.put("wfitemstate", "END");
        map.put("designationList", designationList);
        return map;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public Map<String, Object> getDesgByDeptAndTypeAndVoudate(
            final String type, final String scriptName, final Date vouDate) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Voucher Service | getDesgUserByDeptAndDesgName | Start");
        final Map<String, Object> map = new HashMap<>();
        Designation designation = null;
        persistenceService.findAllByNamedQuery(Script.BY_NAME, scriptName).get(
                0);
        final List<String> list = null;/*
                                        * (List<String>) validScript.eval(Script .createContext( "eisCommonServiceBean",
                                        * eisCommonService,"userId", Integer.valueOf (ApplicationThreadLocals
                                        * .getUserId().trim()),"DATE",new Date() ,"type",type,"vouDate",vouDate.getTime ()));
                                        */
        Map<String, Object> desgFuncryMap;
        final List<Map<String, Object>> designationList = new ArrayList<>();
        for (final String desgFuncryName : list)
            if (desgFuncryName.trim().length() != 0
                    && !desgFuncryName.equalsIgnoreCase("END")) {
                desgFuncryMap = new HashMap<>();
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Designation and Functionary  Name  = "
                            + desgFuncryName);
                designation = designationService
                        .getDesignationByName(desgFuncryName
                                .substring(desgFuncryName.indexOf('-') + 1));
                desgFuncryMap.put("designationName", designation.getName());
                desgFuncryMap.put(
                        "designationId",
                        designation.getId()
                                + "-"
                                + desgFuncryName.substring(0,
                                        desgFuncryName.indexOf('-')));
                designationList.add(desgFuncryMap);
            } else if (desgFuncryName.equalsIgnoreCase("END"))
                map.put("wfitemstate", "END");
        map.put("designationList", designationList);
        return map;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public Map<String, Object> getDesgBYPassingWfItem(final String scriptName,
            final Object wfitem, final Integer deptId) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Voucher Service | getDesgUserByDeptAndDesgName | Start");
        final Map<String, Object> map = new HashMap<>();
        Designation designation = null;
        final Script validScript = (Script) persistenceService
                .findAllByNamedQuery(Script.BY_NAME, scriptName).get(0);
        final List<String> list = (List<String>) scriptService.executeScript(
                validScript, ScriptService.createContext(
                        "eisCommonServiceBean", eisCommonService, "userId",
                        ApplicationThreadLocals.getUserId().intValue(), "DATE",
                        new Date(), "wfitem", wfitem, "deptId",
                        deptId, "persistenceService", persistenceService));
        Map<String, Object> desgFuncryMap;
        List<Map<String, Object>> designationList = new ArrayList<>();
        for (final String desgFuncryName : list)
            if (desgFuncryName.trim().length() != 0
                    && !desgFuncryName.equalsIgnoreCase("END")
                    && !desgFuncryName
                            .equalsIgnoreCase("ANYFUNCTIONARY-ANYDESG")) {
                desgFuncryMap = new HashMap<>();
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Designation and Functionary  Name  = "
                            + desgFuncryName);
                designation = designationService
                        .getDesignationByName(desgFuncryName
                                .substring(desgFuncryName.indexOf('-') + 1));
                desgFuncryMap.put("designationName", designation.getName());
                desgFuncryMap.put(
                        "designationId",
                        designation.getId()
                                + "-"
                                + desgFuncryName.substring(0,
                                        desgFuncryName.indexOf('-')));
                designationList.add(desgFuncryMap);
                map.put("wfitemstate", desgFuncryName);
            } else if (desgFuncryName
                    .equalsIgnoreCase("ANYFUNCTIONARY-ANYDESG")) {
                designationList = getAllDesgByAndDept(deptId, desgFuncryName);
                map.put("wfitemstate", desgFuncryName);
            } else if (desgFuncryName.equalsIgnoreCase("END"))
                map.put("wfitemstate", desgFuncryName);
        map.put("designationList", designationList);
        return map;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllDesgByAndDept(final Integer deptId,
            final String desgfuncry) {
        Map<String, Object> desgMap;
        final List<Map<String, Object>> desgList = new ArrayList<>();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VoucherService | getAllDesgByFuncryAndDept | Start");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("department id = " + deptId);
        final StringBuilder query = new StringBuilder();
        query.append(
                "select DISTINCT desg.name,desg.id from Designation desg , EmployeeView ev where ")
                .append(" desg.id=ev.desigId.id and ev.deptId.id = ? and ev.userMaster is not null");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getAllDesgByFuncryAndDept Query : = "
                    + query.toString());
        final List<Object[]> list = persistenceService.findAllBy(
                query.toString(), deptId);
        for (final Object[] objects : list) {
            desgMap = new HashMap<>();
            desgMap.put("designationName", objects[0]);
            desgMap.put(
                    "designationId",
                    objects[1] + "-"
                            + desgfuncry.substring(0, desgfuncry.indexOf('-')));
            desgList.add(desgMap);

        }
        return desgList;

    }

    public List<EmployeeView> getUserByDeptAndDesgName(
            final String departmentId, final String designationId,
            final String functionaryId) {

        final HashMap<String, String> paramMap = new HashMap<>();

        paramMap.put("departmentId", departmentId);
        paramMap.put("designationId", designationId);
        paramMap.put("functionaryId", functionaryId);
        return eisService.getEmployeeInfoList(paramMap);
    }

    /**
     *
     * @description - creates the bill register object for the vouchers except expense JV.eisService
     * @param billDetailslist -having account details info.
     * @param subLedgerlist - having sub ledger details info.
     * @param voucherHeader - header and misc details info.
     * @param voucherTypeBean - voucher type info.
     * @param totalBillAmount - total bill amount.
     * @return - returns the created egbillregister object.
     * @throws Exception
     */
    @Transactional
    public EgBillregister createBillForVoucherSubType(
            final List<VoucherDetails> billDetailslist,
            final List<VoucherDetails> subLedgerlist,
            final CVoucherHeader voucherHeader,
            final VoucherTypeBean voucherTypeBean,
            final BigDecimal totalBillAmount) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VoucherService | createBillForVoucherSubType | Start");
        final EgBillregister egBillregister = new EgBillregister();
        // Fix it for basic financial type also
        try {
            egBillregister.setBillstatus("APPROVED");
            EgwStatus egwstatus = null;
            if ("Contractor Journal".equalsIgnoreCase(voucherTypeBean
                    .getVoucherName())) {
                egwstatus = (EgwStatus) persistenceService
                        .find("from EgwStatus where upper(moduletype)=? and upper(description)=?",
                                "CONTRACTORBILL", "APPROVED");
                egBillregister.setExpendituretype("Works");
            } else if ("Supplier Journal".equalsIgnoreCase(voucherTypeBean
                    .getVoucherName())) {
                egwstatus = (EgwStatus) persistenceService
                        .find("from EgwStatus where upper(moduletype)=? and upper(description)=?",
                                "SBILL", "APPROVED");
                egBillregister.setExpendituretype("Purchase");
            } else if ("Salary Journal".equalsIgnoreCase(voucherTypeBean
                    .getVoucherName())) {
                egwstatus = (EgwStatus) persistenceService
                        .find("from EgwStatus where upper(moduletype)=? and upper(description)=?",
                                "SALBILL", "APPROVED");
                egBillregister.setExpendituretype("Salary");
            } else if ("Expense Journal".equalsIgnoreCase(voucherTypeBean
                    .getVoucherName())) {
                egwstatus = (EgwStatus) persistenceService
                        .find("from EgwStatus where upper(moduletype)=? and upper(description)=?",
                                "EXPENSEBILL", "APPROVED");
                egBillregister.setExpendituretype("Expense");
            } else if ("Pension Journal".equalsIgnoreCase(voucherTypeBean
                    .getVoucherName())) {
                egwstatus = (EgwStatus) persistenceService
                        .find("from EgwStatus where upper(moduletype)=? and upper(description)=?",
                                "PENSIONBILL", "APPROVED");
                egBillregister.setExpendituretype("Pension");
            }
            egBillregister.setStatus(egwstatus);
            if (null != voucherTypeBean.getBillDate())
                egBillregister.setBilldate(voucherTypeBean.getBillDate());
            else
                egBillregister.setBilldate(voucherHeader.getVoucherDate());
            if (null != voucherHeader.getVouchermis().getDivisionid())
                egBillregister.setFieldid(new BigDecimal(voucherHeader
                        .getVouchermis().getDivisionid().getId().toString()));
            egBillregister.setNarration(voucherHeader.getDescription());
            egBillregister.setIsactive(true);
            egBillregister.setBilltype("Final Bill");
            egBillregister.setPassedamount(totalBillAmount);
            egBillregister.setBillamount(totalBillAmount);

            final EgBillregistermis egBillregistermis = new EgBillregistermis();
            egBillregistermis.setFund(voucherHeader.getFundId());
            egBillregistermis.setEgDepartment(voucherHeader.getVouchermis()
                    .getDepartmentid());
            egBillregistermis.setFunctionaryid(voucherHeader.getVouchermis()
                    .getFunctionary());
            egBillregistermis.setFunction(voucherHeader.getVouchermis()
                    .getFunction());
            egBillregistermis.setFundsource(voucherHeader.getVouchermis()
                    .getFundsource());
            egBillregistermis.setScheme(voucherHeader.getVouchermis()
                    .getSchemeid());
            egBillregistermis.setSubScheme(voucherHeader.getVouchermis()
                    .getSubschemeid());
            egBillregistermis.setNarration(voucherHeader.getDescription());
            egBillregistermis.setPartyBillDate(voucherTypeBean
                    .getPartyBillDate());
            egBillregistermis.setPayto(voucherTypeBean.getPartyName());
            egBillregistermis.setPartyBillNumber(voucherTypeBean
                    .getPartyBillNum());
            egBillregistermis.setFieldid(voucherHeader.getVouchermis()
                    .getDivisionid());
            if (voucherTypeBean.getVoucherNumType().equalsIgnoreCase(
                    "fixedassetjv")) {
                final EgBillSubType egBillSubType = (EgBillSubType) persistenceService
                        .find("from EgBillSubType where name=? and expenditureType=?",
                                "Fixed Asset", "Purchase");
                egBillregistermis.setEgBillSubType(egBillSubType);
            }
            egBillregistermis.setLastupdatedtime(new Date());
            egBillregistermis.setVoucherHeader(voucherHeader);
            egBillregister.setEgBillregistermis(egBillregistermis);
            if (null != voucherTypeBean.getBillNum()
                    && StringUtils.isNotEmpty(voucherTypeBean.getBillNum()))
                egBillregister.setBillnumber(voucherTypeBean.getBillNum());
            else {
                final JVBillNumberGenerator b = beanResolver
                        .getAutoNumberServiceFor(JVBillNumberGenerator.class);
                final String billNumber = b.getNextNumber(egBillregister);

                egBillregister.setBillnumber(billNumber);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("VoucherService | createBillForVoucherSubType | Bill number generated :="
                            + billNumber);
            }
            if (!isBillNumUnique(egBillregister.getBillnumber()))
                throw new ValidationException(
                        Arrays.asList(new ValidationError("bill number",
                                "Duplicate Bill Number : "
                                        + egBillregister.getBillnumber())));
            egBillregistermis.setEgBillregister(egBillregister);

            Set<EgBilldetails> egBilldetailes = new HashSet<>();
            egBilldetailes = prepareBillDetails(egBillregister,
                    billDetailslist, subLedgerlist, voucherHeader,
                    egBilldetailes);
            egBillregister.setEgBilldetailes(egBilldetailes);
            egBillRegisterService.applyAuditing(egBillregister);
            egBillRegisterService.persist(egBillregister);

            voucherHeader.getVouchermis().setSourcePath(
                    "/EGF/voucher/journalVoucherModify-beforeModify.action?voucherHeader.id="
                            + voucherHeader.getId());
            update(voucherHeader);
            persistenceService.getSession().flush();
        } catch (final ValidationException e) {
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getErrors().get(0)
                    .getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VoucherService | createBillForVoucherSubType | End | bill number : = "
                    + egBillregister.getBillnumber());
        return egBillregister;

    }

    /**
     * @description - update the bill register objects in the JV modify screen for voucher sub types.
     * @param billDetailslist - account detail info.
     * @param subLedgerlist - Bill Payee details info.
     * @param voucherHeader - bill register and mis info.
     * @param voucherTypeBean - different voucher sub type and bill type info.
     * @param totalBillAmount - total debit amount.
     * @return egBillregister - the updated bill register object.
     * @throws ValidationException
     */
    @Transactional
    public EgBillregister updateBillForVSubType(
            final List<VoucherDetails> billDetailslist,
            final List<VoucherDetails> subLedgerlist,
            final CVoucherHeader voucherHeader,
            final VoucherTypeBean voucherTypeBean,
            final BigDecimal totalBillAmount) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Voucher Service | updateBillForVSubType | Start");
        EgBillregister egBillregister = null;
        try {
            egBillregister = (EgBillregister) persistenceService
                    .find("from EgBillregister br where br.egBillregistermis.voucherHeader.id="
                            + voucherHeader.getId());
            final EgBillregistermis egBillregistermis = egBillregister
                    .getEgBillregistermis();
            if (null != voucherTypeBean.getBillDate())
                egBillregister.setBilldate(voucherTypeBean.getBillDate());
            else
                egBillregister.setBilldate(voucherHeader.getVoucherDate());
            if (null != voucherTypeBean.getBillNum()
                    && StringUtils.isNotEmpty(voucherTypeBean.getBillNum()))
                if (!voucherTypeBean.getBillNum().equalsIgnoreCase(
                        egBillregister.getBillnumber())) {
                    if (!isBillNumUnique(voucherTypeBean.getBillNum()))
                        throw new ValidationException(
                                Arrays.asList(new ValidationError(
                                        "bill number",
                                        "Duplicate Bill Number : "
                                                + voucherTypeBean.getBillNum())));
                    egBillregister.setBillnumber(voucherTypeBean.getBillNum());
                }

            if (null != voucherHeader.getVouchermis().getDivisionid())
                egBillregister.setFieldid(new BigDecimal(voucherHeader
                        .getVouchermis().getDivisionid().getId().toString()));
            egBillregister.setNarration(voucherHeader.getDescription());
            egBillregister.setPassedamount(totalBillAmount);
            egBillregister.setBillamount(totalBillAmount);

            egBillregistermis.setFund(voucherHeader.getFundId());
            egBillregistermis.setEgDepartment(voucherHeader.getVouchermis()
                    .getDepartmentid());
            egBillregistermis.setFunction(voucherHeader.getVouchermis()
                    .getFunction());
            egBillregistermis.setFunctionaryid(voucherHeader.getVouchermis()
                    .getFunctionary());
            egBillregistermis.setFundsource(voucherHeader.getVouchermis()
                    .getFundsource());
            egBillregistermis.setScheme(voucherHeader.getVouchermis()
                    .getSchemeid());
            egBillregistermis.setSubScheme(voucherHeader.getVouchermis()
                    .getSubschemeid());
            egBillregistermis.setNarration(voucherHeader.getDescription());
            egBillregistermis.setPartyBillDate(voucherTypeBean
                    .getPartyBillDate());
            egBillregistermis.setPayto(voucherTypeBean.getPartyName());
            egBillregistermis.setPartyBillNumber(voucherTypeBean
                    .getPartyBillNum());

            final Set<EgBilldetails> egBilldetailes = egBillregister
                    .getEgBilldetailes();
            egBilldetailes.clear();

            prepareBillDetails(egBillregister, billDetailslist, subLedgerlist,
                    voucherHeader, egBilldetailes);
            egBillRegisterService.update(egBillregister);

        } catch (final ValidationException e) {
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getErrors().get(0)
                    .getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Voucher Service | updateBillForVSubType | End");
        return egBillregister;
    }

    /**
     *
     * @param egBillregister
     * @param billDetailslist
     * @param subLedgerlist
     * @param voucherHeader
     * @return
     */
    private Set<EgBilldetails> prepareBillDetails(
            final EgBillregister egBillregister,
            final List<VoucherDetails> billDetailslist,
            final List<VoucherDetails> subLedgerlist,
            final CVoucherHeader voucherHeader,
            final Set<EgBilldetails> egBilldetailes) {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Voucher Service | prepareBillDetails | Start");
        for (final VoucherDetails accountDetail : billDetailslist) {

            final EgBilldetails egBilldetail = new EgBilldetails();
            egBilldetail.setEgBillregister(egBillregister);
            egBilldetail.setGlcodeid(new BigDecimal(accountDetail
                    .getGlcodeIdDetail().toString()));
            egBilldetail.setDebitamount(accountDetail.getDebitAmountDetail());
            egBilldetail.setCreditamount(accountDetail.getCreditAmountDetail());
            if (null != accountDetail.getFunctionIdDetail())
                egBilldetail.setFunctionid(new BigDecimal(accountDetail
                        .getFunctionIdDetail()));
            if (voucherHeader.getVouchermis().getFunction() != null
                    && !voucherHeader.getVouchermis().getFunction().equals("0"))
                egBilldetail.setFunctionid(new BigDecimal(voucherHeader
                        .getVouchermis().getFunction().getId()));
            egBilldetail.setNarration(voucherHeader.getDescription());
            Set<EgBillPayeedetails> egBillPaydetailes = null;

            for (final VoucherDetails subledgerDetail : subLedgerlist)
                if (accountDetail.getGlcodeIdDetail().equals(
                        subledgerDetail.getGlcode().getId())) {
                    if (null == egBillPaydetailes)
                        egBillPaydetailes = new HashSet<>();
                    final EgBillPayeedetails egBillPaydetail = new EgBillPayeedetails();
                    egBillPaydetail.setEgBilldetailsId(egBilldetail);
                    egBillPaydetail.setAccountDetailTypeId(subledgerDetail
                            .getDetailType().getId());
                    egBillPaydetail.setAccountDetailKeyId(subledgerDetail
                            .getDetailKeyId());
                    if (egBilldetail.getDebitamount()
                            .compareTo(BigDecimal.ZERO) > 0)
                        egBillPaydetail.setDebitAmount(subledgerDetail
                                .getAmount());
                    else
                        egBillPaydetail.setCreditAmount(subledgerDetail
                                .getAmount());
                    egBillPaydetail
                            .setNarration(voucherHeader.getDescription());
                    egBillPaydetail.setLastUpdatedTime(new Date());
                    egBillPaydetailes.add(egBillPaydetail);
                }
            egBilldetail.setEgBillPaydetailes(egBillPaydetailes);
            egBilldetail.setLastupdatedtime(new Date());
            egBilldetailes.add(egBilldetail);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Voucher Service | prepareBillDetails | End");
        return egBilldetailes;
    }

    private boolean isBillNumUnique(final String billNumber) {

        final String billNum = (String) persistenceService
                .find("select billnumber from EgBillregister where upper(billnumber)='"
                        + billNumber.toUpperCase() + "'");
        return billNum == null;
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public void setBudgetDetailsDAO(
            final BudgetDetailsHibernateDAO detailsHibernateDAO) {
        budgetDetailsDAO = detailsHibernateDAO;
    }

    public VoucherHibernateDAO getVoucherHibDAO() {
        return voucherHibDAO;
    }

    public void setVoucherHibDAO(final VoucherHibernateDAO voucherHibDAO) {
        this.voucherHibDAO = voucherHibDAO;
    }

    public void cancelVoucher(final CVoucherHeader voucher) {
        voucher.setStatus(4);
        update(voucher);

    }

    public void setPersistenceService(
            final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public Position getPositionForEmployee(final Employee emp) {
        return eisCommonService.getPrimaryAssignmentPositionForEmp(emp.getId());
    }

    public void setScriptService(final ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    public Integer getDefaultDepartment() {
        persistenceService.findAllByNamedQuery(Script.BY_NAME,
                "BudgetDetail.get.default.department").get(0);
        final String defaultDepartmentName = null;/*
                                                   * (String) script.eval(Script.createContext ("eisCommonServiceBean",
                                                   * eisCommonService, "userId",Integer .valueOf(ApplicationThreadLocals
                                                   * .getUserId().trim())));
                                                   */
        if (!"".equalsIgnoreCase(defaultDepartmentName)) {
            final Department dept = (Department) persistenceService.find(
                    "from Department where name=?", defaultDepartmentName);
            if (dept != null)
                return dept.getId().intValue();
        }
        return 0;
    }

    public FinancialYearHibernateDAO getFinancialYearDAO() {
        return financialYearDAO;
    }

    public void setFinancialYearDAO(
            final FinancialYearHibernateDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public void setEmployeeService(final EmployeeServiceOld employeeService) {
        this.employeeService = employeeService;
    }

    public void setEisService(final EisUtilService eisService) {
        this.eisService = eisService;
    }

    public VoucherHelper getVoucherHelper() {
        return voucherHelper;
    }

    public void setVoucherHelper(final VoucherHelper voucherHelper) {
        this.voucherHelper = voucherHelper;
    }

}