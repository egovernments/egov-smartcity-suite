/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.*;
import org.egov.commons.service.EntityTypeService;
import org.egov.commons.service.RelationService;
import org.egov.commons.utils.BankAccountType;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.masters.model.LoanGrantBean;
import org.egov.eis.entity.DrawingOfficer;
import org.egov.eis.entity.EmployeeView;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.masters.model.AccountEntity;
import org.egov.model.bills.EgBillSubType;
import org.egov.model.bills.EgBillregister;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.voucher.CommonBean;
import org.egov.pims.model.PersonalInformation;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.financingsource.FinancingSourceService;
import org.egov.services.instrument.InstrumentService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import com.exilant.eGov.src.domain.Bank;

@Results({
        @Result(name = "bankAccountByBranch", location = "common-bankAccountByBranch.jsp"),
        @Result(name = "branch", location = "common-branch.jsp"),
        @Result(name = "users", location = "common-users.jsp"),
        @Result(name = "arfNoSearchResults", location = "common-arfNoSearchResults.jsp"),
        @Result(name = "bankAccNum", location = "common-bankAccNum.jsp"),
        @Result(name = "bankAccNum-bankName", location = "common-bankAccNum-bankName.jsp"),
        @Result(name = Constants.FUNDSOURCE, location = "common-" + Constants.FUNDSOURCE + ".jsp"),
        @Result(name = "workflowHistory", location = "common-workflowHistory.jsp"),
        @Result(name = "searchAccountCodes", location = "common-searchAccountCodes.jsp"),
        @Result(name = "entities", location = "common-entities.jsp"),
        @Result(name = "bankByFund", location = "common-bankByFund.jsp"),
        @Result(name = "result", location = "common-result.jsp"),
        @Result(name = "branchesByBank", location = "common-branchesByBank.jsp"),
        @Result(name = "bank", location = "common-bank.jsp"),
        @Result(name = "glCodes", location = "common-glCodes.jsp"),
        @Result(name = "subLedgerType", location = "common-subLedgerType.jsp"),
        @Result(name = "checkList", location = "common-checkList.jsp"),
        @Result(name = "projectCodesBy20", location = "common-projectCodesBy20.jsp"),
        @Result(name = "bankAccNumAndType", location = "common-bankAccNumAndType.jsp"),
        @Result(name = "schemes", location = "common-schemes.jsp"),
        @Result(name = "subSchemeBy20", location = "common-subSchemeBy20.jsp"),
        @Result(name = "voucherNames", location = "common-voucherNames.jsp"),
        @Result(name = "rtgsNumbers", location = "common-rtgsNumbers.jsp"),
        @Result(name = Constants.SUBSCHEMES, location = "common-" + Constants.SUBSCHEMES + ".jsp"),
        @Result(name = "drawingOffcer", location = "common-drawingOffcer.jsp"),
        @Result(name = "searchResult", location = "common-searchResult.jsp"),
        @Result(name = "projectcodes", location = "common-projectcodes.jsp"),
        @Result(name = "functionCodes", location = "common-functionCodes.jsp"),
        @Result(name = "AJAX_RESULT", location = "common-AJAX_RESULT.jsp"),
        @Result(name = "accountcodes", location = "common-accountcodes.jsp"),
        @Result(name = "instrument", location = "common-instrument.jsp"),
        @Result(name = "desg", location = "common-desg.jsp"),
        @Result(name = "COA", location = "common-COA.jsp"),
        @Result(name = "process", location = "common-process.jsp"),
        @Result(name = "schemeBy20", location = "common-schemeBy20.jsp"),
        @Result(name = "yearCode", location = "common-yearCode.jsp"),
        @Result(name = "estimateBudgetDetails", location = "common-estimateBudgetDetails.jsp")
})
public class CommonAction extends BaseFormAction {

    private static final Logger LOGGER = Logger.getLogger(CommonAction.class);
    private static final long serialVersionUID = 1L;
    private static final String RTGSNUMBERSQUERY = "SELECT ih.id, ih.transactionNumber FROM InstrumentHeader ih, InstrumentVoucher iv, "
            + "Paymentheader ph WHERE ih.isPayCheque ='1' AND ih.bankAccountId.id = ? AND ih.statusId.description in ('New')" +
            " AND ih.statusId.moduletype='Instrument' AND iv.instrumentHeaderId = ih.id and ih.bankAccountId is not null " +
            "AND iv.voucherHeaderId     = ph.voucherheader AND ph.bankaccount = ih.bankAccountId AND ph.type = '"
            + FinancialConstants.MODEOFPAYMENT_RTGS + "' " + "GROUP BY ih.transactionNumber,ih.id order by ih.id desc";
    private Integer fundId;
    private Integer schemeId;
    private Integer department;
    private Integer bankId;
    private List<Map<String, Object>> bankBranchList;
    private Integer branchId;
    private Long departmentId;
    private Long bankaccountId;
    private String rtgsNumber;
    private String chequeNumber;
    private List<Bankaccount> accNumList;
    private List<DrawingOfficer> drawingList;
    private String value;
    private List<Scheme> schemeList;
    private List<SubScheme> subSchemes;
    private List<Bankbranch> branchList;
    private List<Bank> bankList;
    private List<InstrumentHeader> instrumentHeaderList;
    private String type;
    private ArrayList<Map<String, String>> nameList;
    private InstrumentService instrumentService;
    private List<String> detailCodes = new ArrayList<String>();
    private List<User> userList;
    private Integer designationId;
    private VoucherService voucherService;
    private String functionaryName;
    private EgovCommon egovCommon;
    private List<CChartOfAccounts> accountCodesForDetailTypeList;
    private List<EntityType> entitiesList;
    private List<String> numberList;
    private Integer accountDetailType;
    private Integer billSubtypeId;
    private String billType;
    private String searchType;
    private List<BudgetDetail> budgetDetailList;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    private List<AppConfigValues> checkList;
    private String accountDetailTypeName;
    private String typeOfAccount;
    private Date asOnDate;
    private String scriptName;
    private Long recoveryId;
    private Integer subSchemeId;
    private List<Fundsource> fundSouceList;
    private List<Map<String, Object>> designationList;
    private String startsWith;
    private FinancingSourceService financingSourceService;
    private String defaultDepartment;
    private Long billRegisterId;
    private Long billVhId;
    private String returnStream = "";
    private List<LoanGrantBean> projectCodeList;
    private List<String> projectCodeStringList;
    private List<CChartOfAccounts> accountCodesList;
    private String stateId;
    private String serialNo;
    private static final String ARF_NUMBER_SEARCH_RESULTS = "arfNoSearchResults";
    public static final String ARF_STATUS_APPROVED = "APPROVED";
    public static final String ARF_TYPE = "Contractor";
    private String query;
    private List<String> arfNumberSearchList = new LinkedList<String>();
    private String billSubType;
    private String glCode;
    private String function;
    private List<CChartOfAccounts> glCodesList;
    private List<CFunction> functionCodesList;
    private List<Accountdetailtype> subLedgerTypeList;
    private List<CChartOfAccounts> coaList;
    private StringBuffer result;
    private Long vouchHeaderId;
    private String glcodeParam;
    private String accountId;
    private String functionName;
    private Integer bankaccount;
    private List<CFinancialYear> yearCodeList;
    private Long functionId;
    @Autowired
    private BudgetDetailService budgetDetailService;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(final String serialNo) {
        this.serialNo = serialNo;
    }

    public InputStream getReturnStream() {
        final ByteArrayInputStream is = new ByteArrayInputStream(returnStream.getBytes());
        return is;
    }

    public Long getBillRegisterId() {
        return billRegisterId;
    }

    public void setBillRegisterId(final Long billRegisterId) {
        this.billRegisterId = billRegisterId;
    }

    public void setRelationService(final RelationService relationService) {
    }

    public List<String> getNumberList() {
        return numberList;
    }

    public void setNumberList(final List<String> numberList) {
        this.numberList = numberList;
    }

    public CommonAction() {
    }

    @Override
    public Object getModel() {

        return null;
    }

    public List<Bank> getBankList() {
        return bankList;
    }

    public void setBankList(final List<Bank> bankList) {
        this.bankList = bankList;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadSchemes")
    public String ajaxLoadSchemes() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadSchemes...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Fund Id received is : " + fundId);
        if (null == fundId)
            schemeList = getPersistenceService().findAllBy(
                    " from Scheme where fund.id=? and isActive=true order by name", -1);
        else
            schemeList = getPersistenceService()
                    .findAllBy(" from Scheme where fund.id=? and isactive=true order by name", fundId);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Scheme List size : " + schemeList.size());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadSchemes.");
        return "schemes";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadSchemeBy20")
    public String ajaxLoadSchemeBy20() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadSchemeBy20...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Fund Id received is :  " + fundId + "   and Startswith   :" + startsWith);
        startsWith = "%" + startsWith + "%";
        schemeList = new ArrayList<Scheme>();
        final String qry = "from Scheme  where upper(code) like upper(?) or upper(name) like upper(?) and isactive=true ";
        if (null != fundId && fundId != -1)
            schemeList.addAll(getPersistenceService().findPageBy(qry + " and fund.id=(?) order by code,name ", 0,
                    20, startsWith, startsWith, fundId).getList());
        else
            schemeList.addAll(getPersistenceService().findPageBy(qry + " order by code,name  ", 0, 20, startsWith,
                    startsWith).getList());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Scheme List size : " + schemeList.size());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadSchemeBy20.");
        return "schemeBy20";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadSubSchemes")
    public String ajaxLoadSubSchemes() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadSubSchemes...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Scheme Id received is : " + schemeId);
        if (null != schemeId && schemeId != -1) {
            subSchemes = getPersistenceService().findAllBy("from SubScheme where scheme.id=? and isActive=true order by name",
                    schemeId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Subscheme List size : " + subSchemes.size());
        } else
            subSchemes = Collections.EMPTY_LIST;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadSubSchemes.");

        return Constants.SUBSCHEMES;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadSubSchemeBy20")
    public String ajaxLoadSubSchemeBy20() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadSubSchemeBy20...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("schemeId Id received is :  " + schemeId + "   and Startswith   :" + startsWith);
        startsWith = "%" + startsWith + "%";
        subSchemes = new ArrayList<SubScheme>();
        final String qry = "from SubScheme  where upper(code) like upper(?) or upper(name) like upper(?) and isactive=true ";
        if (null != schemeId)
            subSchemes.addAll(getPersistenceService().findPageBy(qry + " and scheme.id=(?) order by code,name",
                    0, 20, startsWith, startsWith, schemeId).getList());
        else
            subSchemes.addAll(getPersistenceService().findPageBy(qry + " order by code,name ", 0, 20,
                    startsWith, startsWith).getList());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Scheme List size : " + subSchemes.size());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadSubSchemeBy20.");
        return "subSchemeBy20";
    }

    @SuppressWarnings("unchecked")
    // @Deprecated
    @Action(value = "/voucher/common-ajaxLoadBanks")
    public String ajaxLoadBanks() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBanks...");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadBanks");

        try {
            final List<Object[]> bankBranch = getPersistenceService()
                    .findAllBy(
                            "select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),bankBranch.branchname) as bankbranchname "
                                    +
                                    " FROM Bank bank,Bankbranch bankBranch,Bankaccount bankaccount "
                                    +
                                    " where  bank.isactive=true  and bankBranch.isactive=true and bankaccount.isactive=true  and bank.id = bankBranch.bank.id and bankBranch.id = bankaccount.bankbranch.id"
                                    +
                                    " and bankaccount.fund.id=? order by 2",
                            fundId);

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankBranch.size());
            bankBranchList = new ArrayList<Map<String, Object>>();
            Map<String, Object> bankBrmap;
            for (final Object[] element : bankBranch) {
                bankBrmap = new HashMap<String, Object>();
                bankBrmap.put("bankBranchId", element[0].toString());
                bankBrmap.put("bankBranchName", element[1].toString());
                bankBranchList.add(bankBrmap);
            }

        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));

        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBanks.");
        return "bank";

    }

    @SuppressWarnings("unchecked")
    // @Deprecated
    @Action(value = "/voucher/common-ajaxLoadAllBanks")
    public String ajaxLoadAllBanks() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadAllBanks...");
        String fundChk = "";
        List<Object[]> bankBranch;
        final StringBuffer bankQuery = new StringBuffer();
        if (fundId != null)
            // ajaxLoadBanks();
            fundChk = " and bankaccount.fund.id=?";
        try {
            bankQuery
                    .append("select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' ')")
                    .append(",bankBranch.branchname) as bankbranchname FROM Bank bank,Bankbranch bankBranch,Bankaccount bankaccount ")
                    .append(" where  bank.isactive=true  and bankBranch.isactive=true and bankaccount.isactive=true  and bank.id = bankBranch.bank.id ")
                    .append("and bankBranch.id = bankaccount.bankbranch.id");
            if (fundId != null)
                bankBranch = getPersistenceService().findAllBy(
                        bankQuery.append(fundChk).toString() + " order by 2", fundId);
            else
                bankBranch = getPersistenceService().findAllBy(bankQuery.toString() + " order by 2");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankBranch.size());
            bankBranchList = new ArrayList<Map<String, Object>>();
            Map<String, Object> bankBrmap;
            for (final Object[] element : bankBranch) {
                bankBrmap = new HashMap<String, Object>();
                bankBrmap.put("bankBranchId", element[0].toString());
                bankBrmap.put("bankBranchName", element[1].toString());
                bankBranchList.add(bankBrmap);
            }

        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));

        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadAllBanks.");
        return "bank";

    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadAllBanksByFund")
    public String ajaxLoadAllBanksByFund() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadAllBanksByFund...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadAllBanksByFund");
        try {
            if (fundId != null)
                bankList = getPersistenceService()
                        .findAllBy(
                                "select distinct b from Bank b,Bankbranch bb , Bankaccount ba  where bb.bank=b and ba.bankbranch =bb and ba.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and ba.fund.id=?",
                                fundId);
            else
                bankList = getPersistenceService()
                        .findAllBy(
                                "select distinct b from Bank b,Bankbranch bb , Bankaccount ba  where bb.bank=b and ba.bankbranch =bb and ba.type in ('RECEIPTS_PAYMENTS','PAYMENTS')");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size =  " + bankList.size());
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting bank  " + e.getMessage(), new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting bank  " + e.getMessage(), new HibernateException(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadAllBanksByFund.");
        return "bankByFund";

    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBanksByFundAndType")
    public String ajaxLoadBanksByFundAndType() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBanksByFundAndType...");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadBanks");
        int index = 0;
        String[] strArray = null;
        final StringBuffer query = new StringBuffer();
        query.append(
                "select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),bankBranch.branchname) as bankbranchname ")
                .append("FROM Bank bank,Bankbranch bankBranch,Bankaccount bankaccount where  bank.isactive=true  and bankBranch.isactive=true and ")
                .append(" bankaccount.isactive=true and bank.id = bankBranch.bank.id and bankBranch.id = bankaccount.bankbranch.id ");
        if (fundId != null)
            query.append("and bankaccount.fund.id=? and bankaccount.type in(");
        else
            query.append("and bankaccount.type in(");
        if (typeOfAccount.indexOf(",") != -1) {
            strArray = typeOfAccount.split(",");
            for (final String type : strArray) {
                index++;
                query.append("'").append(type).append("'");
                if (strArray.length > index)
                    query.append(",");

            }
        } else
            query.append("'").append(typeOfAccount).append("'");
        query.append(") order by 2 ");
        try {
            List<Object[]> bankBranch = null;
            if (fundId != null)
                bankBranch = getPersistenceService().findAllBy(query.toString(), fundId);
            else
                bankBranch = getPersistenceService().findAllBy(query.toString());

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankBranch.size());
            bankBranchList = new ArrayList<Map<String, Object>>();
            Map<String, Object> bankBrmap;
            for (final Object[] element : bankBranch) {
                bankBrmap = new HashMap<String, Object>();
                bankBrmap.put("bankBranchId", element[0].toString());
                bankBrmap.put("bankBranchName", element[1].toString());
                bankBranchList.add(bankBrmap);
            }

        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));

        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBanksByFundAndType.");
        return "bank";

    }

    @SuppressWarnings("unchecked")
    @Deprecated
    @Action(value = "/voucher/common-ajaxLoadAccNum")
    public String ajaxLoadAccNum() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadAccNum...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadAccNum");
        try {

            accNumList = getPersistenceService()
                    .findAllBy(
                            "from Bankaccount ba where ba.bankbranch.id=? and ba.bankbranch.bank.id=? and isactive=true order by ba.chartofaccounts.glcode",
                            branchId, bankId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank account Number list size =  " + accNumList.size());
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting bank account numbers " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting bank account numbers " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadAccNum.");
        return "bankAccNum";
    }

    @Action(value = "/voucher/common-ajaxLoadBankAccountsByBranch")
    public String ajaxLoadBankAccountsByBranch() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadAccNum...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadAccNum");
        try {

            accNumList = getPersistenceService()
                    .findAllBy(
                            "from Bankaccount ba where ba.bankbranch.id=? and isactive=true order by ba.chartofaccounts.glcode",
                            branchId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank account Number list size =  " + accNumList.size());
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting bank account numbers " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting bank account numbers " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadAccNum.");
        return "bankAccountByBranch";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBankBranchFromBank")
    public String ajaxLoadBankBranchFromBank() {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBankBranchFromBank...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadBankBranchFromBank");
        try {
            if (fundId != null)
                branchList = getPersistenceService()
                        .findAllBy(
                                "select distinct bb from Bankbranch bb , Bankaccount ba  where ba.bankbranch =bb and ba.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and bb.bank.id=? and bb.isactive=true and ba.fund.id=?",
                                bankId, fundId);
            else
                branchList = getPersistenceService()
                        .findAllBy(
                                "select distinct bb from Bankbranch bb , Bankaccount ba  where ba.bankbranch =bb and ba.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and bb.bank.id=? and bb.isactive=true",
                                bankId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank Branch Number list size =  " + branchList.size());
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting bank branch " + e.getMessage(), new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting bank branch " + e.getMessage(), new HibernateException(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBankBranchFromBank.");
        return "branch";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBankAccFromBranch")
    public String ajaxLoadBankAccFromBranch() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBankAccFromBranch...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadBankAccFromBranch");
        try {
            if (fundId != null)
                accNumList = getPersistenceService()
                        .findAllBy(
                                "from Bankaccount ba where ba.bankbranch.id=? and ba.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and ba.isactive=true and ba.fund.id=?",
                                branchId, fundId);
            else
                accNumList = getPersistenceService()
                        .findAllBy(
                                "from Bankaccount ba where ba.bankbranch.id=? and ba.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and ba.isactive=true",
                                branchId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank Account Number list size =  " + accNumList.size());
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting bank account " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting bank account " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBankAccFromBranch.");
        return "bankAccNum";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadRTGSChequeFromBankAcc")
    public String ajaxLoadRTGSChequeFromBankAcc() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadRTGSChequeFromBankAcc...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadRTGSChequeFromBankAcc");
        List<Object[]> resultList = new ArrayList<Object[]>();
        new ArrayList<Object>();
        // rtgsNumber=;
        instrumentHeaderList = new ArrayList<InstrumentHeader>();
        /*
         * if(!StringUtils.isEmpty(query)) { strquery=
         * "select appReq from ApplicationRequest appReq where upper(appReq.applicationNo) like '%'||?||'%' ";
         * params.add(query.toUpperCase()); if(!StringUtils.isEmpty(citizenId)) { strquery = strquery +
         * " and appReq.citizenDetails.id=? "; params.add(Long.parseLong(citizenId)); } applicationRequest =
         * getPersistenceService().findAllBy(strquery,params.toArray()); }
         */
        /*
         * try { queryStr= " FROM InstrumentHeader ih, InstrumentVoucher iv, Paymentheader ph "+
         * "WHERE ih.isPayCheque ='1' AND ih.bankAccountId.id = ? AND ih.statusId.description in ('New')" +
         * " AND ih.statusId.moduletype='Instrument' AND iv.instrumentHeaderId = ih.id and ih.bankAccountId is not null "+
         * "AND iv.voucherHeaderId     = ph.voucherheader AND ph.bankaccount = ih.bankAccountId AND ph.type = '"+
         * FinancialConstants.MODEOFPAYMENT_RTGS+"' "+" AND upper(ih.transactionNumber) like '%'||?||'%' "+
         * "GROUP BY ih.transactionNumber,ih.id"; params.add(bankaccountId); params.add(rtgsNumber.toUpperCase());
         * instrumentHeaderList= getPersistenceService().findAllBy(queryStr,params.toArray()); /*for(Object[] obj:resultList){
         * InstrumentHeader ih = new InstrumentHeader(); ih = (InstrumentHeader) persistenceService.find(
         * "from InstrumentHeader where id=?", (Long)obj[0]); instrumentHeaderList.add(ih); }
         */
        // instrumentHeaderList=new ArrayList<InstrumentHeader>();
        try {
            final Calendar calendar = Calendar.getInstance();
            calendar.get(Calendar.DATE);
            calendar.add(Calendar.DATE, -7);
            calendar.get(Calendar.DATE);
            final Date date = calendar.getTime();
            final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
            final String date1 = sdf.format(date);

            resultList = getPersistenceService()
                    .findAllBy(RTGSNUMBERSQUERY, bankaccountId);
            for (final Object[] obj : resultList) {
                InstrumentHeader ih = new InstrumentHeader();
                ih = (InstrumentHeader) persistenceService.find("from InstrumentHeader where id=?", (Long) obj[0]);

                instrumentHeaderList.add(ih);
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting bank account " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting bank account " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadRTGSChequeFromBankAcc.");
        return "instrument";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadAccountNumbers")
    public String ajaxLoadAccountNumbers() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadAccountNumbers...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadAccountNumbers");
        try {
            if (fundId != null && fundId != -1 && fundId != 0)
                accNumList = getPersistenceService()
                        .findAllBy(
                                "from Bankaccount ba where ba.bankbranch.id=? and fund.id=? and isactive=true order by ba.chartofaccounts.glcode",
                                branchId, fundId);
            else
                accNumList = getPersistenceService().findAllBy(
                        "from Bankaccount ba where ba.bankbranch.id=? and isactive=true order by ba.chartofaccounts.glcode",
                        branchId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank account Number list size =  " + accNumList.size());
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting bank account numbers " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting bank account numbers " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadAccountNumbers.");
        return "bankAccNum";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadDrawingOfficers")
    public String ajaxLoadDrawingOfficers() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadDrawingOfficers...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadDrawingOfficers");
        try {
            if (departmentId != null && departmentId != -1 && departmentId != 0)
                drawingList = getPersistenceService()
                        .findAllBy(
                                "select do from DrawingOfficer do,Department dept,DepartmentDOMapping ddm where ddm.department.id = dept.id and ddm.drawingOfficer.id = do.id and dept.id = ?",
                                departmentId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Drawing officers  list size =  " + drawingList.size());
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting Drawing officers " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting Drawing officers " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadDrawingOfficers.");
        return "drawingOffcer";
    }

    @Action(value = "/voucher/common-ajaxLoadAccNumAndType")
    public String ajaxLoadAccNumAndType() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadAccNumAndType...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadAccNum");
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("typeOfAccount in  ajaxLoadBankAccounts method >>>>>>>" + typeOfAccount);
            if (typeOfAccount != null && !typeOfAccount.equals("")) {
                if (typeOfAccount.indexOf(",") != -1) {
                    final String[] strArray = typeOfAccount.split(",");
                    if (fundId != null)
                        accNumList = getPersistenceService()
                                .findAllBy(
                                        "from Bankaccount ba where ba.bankbranch.id=? and ba.fund.id=? and ba.bankbranch.bank.id=? and isactive=true and type in (?, ?) order by ba.chartofaccounts.glcode",
                                        branchId, fundId, bankId, BankAccountType.valueOf(strArray[0].toUpperCase()),
                                        BankAccountType.valueOf(strArray[1].toUpperCase()));
                    else
                        accNumList = getPersistenceService()
                                .findAllBy(
                                        "from Bankaccount ba where ba.bankbranch.id=? and  ba.bankbranch.bank.id=? and isactive=true and type in (?, ?) order by ba.chartofaccounts.glcode",
                                        branchId, bankId, BankAccountType.valueOf(strArray[0]),
                                        BankAccountType.valueOf(strArray[1]));
                } else if (fundId != null)
                    accNumList = getPersistenceService()
                            .findAllBy(
                                    "from Bankaccount ba where ba.bankbranch.id=? and ba.fund.id=? and ba.bankbranch.bank.id=? and isactive=true and type in (?) order by ba.chartofaccounts.glcode",
                                    branchId, fundId, bankId, typeOfAccount);
                else
                    accNumList = getPersistenceService()
                            .findAllBy(
                                    "from Bankaccount ba where ba.bankbranch.id=?  and ba.bankbranch.bank.id=? and isactive=true and type in (?) order by ba.chartofaccounts.glcode",
                                    branchId, bankId, typeOfAccount);
            } else if (fundId != null)
                accNumList = getPersistenceService()
                        .findAllBy(
                                "from Bankaccount ba where ba.bankbranch.id=? and ba.fund.id=? and ba.bankbranch.bank.id=? and isactive=true order by ba.chartofaccounts.glcode",
                                branchId, fundId, bankId);
            else
                accNumList = getPersistenceService()
                        .findAllBy(
                                "from Bankaccount ba where ba.bankbranch.id=?  and ba.bankbranch.bank.id=? and isactive=true order by ba.chartofaccounts.glcode",
                                branchId, bankId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank account Number list size =  " + accNumList.size());
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting bank account numbers " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting bank account numbers " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadAccNumAndType.");
        return "bankAccNumAndType";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-loadAccNumNarration")
    public String loadAccNumNarration() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting loadAccNumNarration...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | loadAccNumNarration");
        try {
            value = "";
            final String accountNumId = parameters.get("accnum")[0];
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank account number id received = " + accountNumId);
            value = (String) getPersistenceService().find("select narration from Bankaccount where id=?",
                    Long.valueOf(accountNumId));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Naration value = " + value);
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting bank account narration " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting bank account narration " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed loadAccNumNarration.");
        return "result";
    }

    @Action(value = "/voucher/common-loadAccNumNarrationAndFund")
    public String loadAccNumNarrationAndFund() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting loadAccNumNarrationAndFund...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | loadAccNumNarration");
        try {
            value = "";
            final String accountNumId = parameters.get("accnum")[0];
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank account number id received = " + accountNumId);
            value = (String) getPersistenceService().find(
                    "select concat(concat(narration,'-'),fund.id) from Bankaccount where id=?", Integer.valueOf(accountNumId));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Naration value = " + value);
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting bank account narration " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting bank account narration " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed loadAccNumNarrationAndFund.");
        return "result";
    }

    @Action(value = "/voucher/common-getDetailType")
    public String getDetailType() throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getDetailType...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getDetailType method");
        value = "";
        final String accountCode = parameters.get("accountCode")[0];
        final String index = parameters.get("index")[0];
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Account code selected is : = " + accountCode);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("index is : = " + index);
        final List<Accountdetailtype> list = getPersistenceService()
                .findAllBy(
                        " from Accountdetailtype"
                                +
                                " where id in (select detailTypeId from CChartOfAccountDetail where glCodeId=(select id from CChartOfAccounts where glcode=?))  ",
                        accountCode);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" list :" + list);
        for (final Accountdetailtype accountdetailtype : list)
            value = value + index + "~" + accountdetailtype.getDescription() + "~" + accountdetailtype.getId().toString() + "#";
        if (!value.equals(""))
            value = value.substring(0, value.length() - 1);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("The Detail type Id is :" + value);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getDetailType.");
        return "result";
    }

    @Action(value = "/voucher/common-ajaxLoadBankBranch")
    public String ajaxLoadBankBranch() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBankBranch...");
        try {
            branchList = persistenceService
                    .findAllBy(
                            "from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund.id=? ) and br.isactive=true order by br.bank.name asc",
                            fundId);
        } catch (final Exception e) {
            LOGGER.error("Exception while loading ajaxLoadBankBranch=" + e.getMessage());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBankBranch.");
        return "branch";
    }

    @Action(value = "/voucher/common-ajaxLoadBankBranchesByBank")
    public String ajaxLoadBankBranchesByBank() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBankBranch...");
        try {
            branchList = persistenceService
                    .findAllBy(
                            "select distinct bb from Bankbranch bb,Bankaccount ba where bb.bank.id=? and ba.bankbranch=bb and bb.isactive=true",
                            bankId);
        } catch (final Exception e) {
            LOGGER.error("Exception while loading ajaxLoadBankBranch=" + e.getMessage());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBankBranch.");
        return "branchesByBank";
    }

    @Action(value = "/voucher/common-ajaxLoadBankAccounts")
    public String ajaxLoadBankAccounts() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBankAccounts...");
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("typeOfAccount in  ajaxLoadBankAccounts method >>>>>>>" + typeOfAccount);
            if (billSubType != null && !billSubType.equalsIgnoreCase("")) {
                String bankAccount = null;
                try {
                    final List<AppConfigValues> configValues = appConfigValuesService.getConfigValuesByModuleAndKey(
                            FinancialConstants.MODULE_NAME_APPCONFIG,
                            FinancialConstants.EB_VOUCHER_PROPERTY_BANKACCOUNT);

                    for (final AppConfigValues appConfigVal : configValues)
                        bankAccount = appConfigVal.getValue();
                } catch (final Exception e) {
                    throw new ApplicationRuntimeException(
                            "Appconfig value for EB Voucher propartys is not defined in the system");
                }
                accNumList = persistenceService
                        .findAllBy(
                                " from Bankaccount where accountnumber=? and isactive=true order by chartofaccounts.glcode ",
                                bankAccount);
            } else if (typeOfAccount != null && !typeOfAccount.equals("")) {
                if (typeOfAccount.indexOf(",") != -1) {
                    final String[] strArray = typeOfAccount.split(",");
                    if (fundId != null && fundId != -1 && fundId != 0)
                        accNumList = persistenceService
                                .findAllBy(
                                        " from Bankaccount where fund.id=? and bankbranch.id=? and isactive=true  and type in (?,?) order by chartofaccounts.glcode ",
                                        fundId, branchId, BankAccountType.valueOf(strArray[0]),
                                        BankAccountType.valueOf(strArray[1]));
                    else
                        accNumList = persistenceService
                                .findAllBy(
                                        " from Bankaccount where  bankbranch.id=? and isactive=true  and type in (?,?) order by chartofaccounts.glcode ",
                                        fundId, branchId, BankAccountType.valueOf(strArray[0]),
                                        BankAccountType.valueOf(strArray[1]));
                } else if (fundId != null && fundId != -1 && fundId != 0)
                    accNumList = persistenceService
                            .findAllBy(
                                    " from Bankaccount where fund.id=? and bankbranch.id=? and isactive=true  and type in (?) order by chartofaccounts.glcode ",
                                    fundId, branchId, typeOfAccount);
                else
                    accNumList = persistenceService
                            .findAllBy(
                                    " from Bankaccount where  bankbranch.id=? and isactive=true  and type in (?) order by chartofaccounts.glcode ",
                                    fundId, branchId, typeOfAccount);
            } else if (fundId != null && fundId != -1 && fundId != 0)
                accNumList = persistenceService
                        .findAllBy(
                                " from Bankaccount where fund.id=? and bankbranch.id=? and isactive=true order by chartofaccounts.glcode",
                                fundId, branchId);
            else
                accNumList = persistenceService.findAllBy(
                        " from Bankaccount where  bankbranch.id=? and isactive=true order by chartofaccounts.glcode",
                        fundId, branchId);
        } catch (final Exception e) {
            LOGGER.error("Exception while loading ajaxLoadBankAccounts=" + e.getMessage());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBankAccounts.");
        return "bankAccNum";
    }

    @Action(value = "/voucher/common-ajaxLoadBankAccountsBySubscheme")
    public String ajaxLoadBankAccountsBySubscheme() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBankAccountsBySubscheme...");

        final SubScheme subScheme = (SubScheme) persistenceService.find("from SubScheme where id = " + subSchemeId);
        fundId = subScheme.getScheme().getFund().getId();
        final String[] strArray = typeOfAccount.split(",");
        accNumList = persistenceService.findAllBy(
                " from Bankaccount where fund.id=? and isactive=true  and type in (?,?) order by chartofaccounts.glcode ",
                fundId,
                strArray[0], strArray[1]);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBankAccountsBySubscheme.");
        return "bankAccNum";
    }

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(final Integer fundId) {
        this.fundId = fundId;
    }

    public String ajaxValidateDetailCode() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxValidateDetailCode...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside ajaxValidateDetailCode method");
        final String code = parameters.get("code")[0];
        final String index = parameters.get("index")[0];
        try {

            final Accountdetailtype adt = (Accountdetailtype) getPersistenceService().find(" from Accountdetailtype where id=?",
                    Integer.valueOf(parameters.get("detailtypeid")[0]));
            if (adt == null) {
                value = index + "~" + ERROR;
                return "result";
            }
            if (adt.getTablename().equalsIgnoreCase("EG_EMPLOYEE")) {
                final PersonalInformation information = (PersonalInformation) getPersistenceService().find(
                        " from PersonalInformation where employeeCode=? and isActive=true", code);
                if (information == null)
                    value = index + "~" + ERROR;
                else
                    value = index + "~" + information.getIdPersonalInformation() + "~" + information.getEmployeeFirstName();
            } else if (adt.getTablename().equalsIgnoreCase("RELATION")) {
                final Relation relation = (Relation) getPersistenceService().find(
                        " from Relation where code=? and isactive=true",
                        code);
                if (relation == null)
                    value = index + "~" + ERROR;
                else
                    value = index + "~" + relation.getId() + "~" + relation.getName();
            } else if (adt.getTablename().equalsIgnoreCase("ACCOUNTENTITYMASTER")) {
                final AccountEntity accountEntity = (AccountEntity) getPersistenceService().find(
                        " from AccountEntity where code=? and isactive=true ", code);
                if (accountEntity == null)
                    value = index + "~" + ERROR;
                else
                    value = index + "~" + accountEntity.getId() + "~" + accountEntity.getCode();
            }
        } catch (final HibernateException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Exception occuerd while getting detail code " + e.getMessage());
            value = index + "~" + ERROR;
        } catch (final Exception e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Exception occuerd while getting detail code " + e.getMessage());
            value = index + "~" + ERROR;
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxValidateDetailCode.");
        return "result";
    }

    @Action(value = "/voucher/common-getDetailCode")
    public String getDetailCode() throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getDetailCode...");
        value = "";
        final String arr[] = parameters.get("accountCodes")[0].split(",");
        final List<String> list = Arrays.asList(arr); // remove duplicate account codes.
        final Set<String> set = new HashSet<String>(list);
        final String[] accountCodes = new String[set.size()];
        set.toArray(accountCodes);
        for (final String accountCode : accountCodes) {

            final CChartOfAccountDetail chartOfAccountDetail = (CChartOfAccountDetail) getPersistenceService().find(
                    " from CChartOfAccountDetail" +
                            " where glCodeId=(select id from CChartOfAccounts where glcode=?)",
                    accountCode);

            if (null != chartOfAccountDetail)
                if (value.trim().length() != 0)
                    value = value + "~" + accountCode + "~" + chartOfAccountDetail.getGlCodeId().getId().toString();
                else
                    value = accountCode + "~" + chartOfAccountDetail.getGlCodeId().getId().toString();

        } /*
           * if(values.trim().length()!=0){ values=index+"~"+values; }
           */
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("The account Detail  codes are :" + value);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getDetailCode.");
        return "result";
    }

    public String ajaxGetDetailCode() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxGetDetailCode...");
        final String index = parameters.get("index")[0];
        try {
            final Accountdetailtype adt = (Accountdetailtype) getPersistenceService().find(" from Accountdetailtype where id=?",
                    Integer.valueOf(parameters.get("detailtypeid")[0]));
            if (adt == null) {
                value = index + "~" + ERROR;
                return "result";
            }
            if (adt.getTablename().equalsIgnoreCase("EG_EMPLOYEE")) {
                final List<PersonalInformation> information = getPersistenceService().findAllBy(
                        "from PersonalInformation where isActive=true order by employeeCode");
                if (information == null)
                    value = index + "~" + ERROR;
                else
                    for (final PersonalInformation personalInformation : information)
                        detailCodes.add(personalInformation.getIdPersonalInformation() + "-"
                                + personalInformation.getEmployeeFirstName());
            } else if (adt.getTablename().equalsIgnoreCase("RELATION")) {
                final List<Relation> relation = getPersistenceService().findAllBy(
                        "from Relation where isactive=true order by code");
                if (relation == null)
                    value = index + "~" + ERROR;
                else
                    for (final Relation rel : relation)
                        detailCodes.add(rel.getId() + "-" + rel.getName());
            } else if (adt.getTablename().equalsIgnoreCase("ACCOUNTENTITYMASTER")) {
                final List<AccountEntity> accountEntity = getPersistenceService().findAllBy(
                        " from AccountEntity where isactive=true order by code");
                if (accountEntity == null)
                    value = index + "~" + ERROR;
                else
                    for (final AccountEntity rel : accountEntity)
                        detailCodes.add(rel.getId() + "-" + rel.getCode());
            }
        } catch (final Exception e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Exception occuerd while getting detail code " + e.getMessage());
            value = index + "~" + ERROR;
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxGetDetailCode.");
        return "detailedCodes";
    }

    @Deprecated
    @Action(value = "/voucher/common-ajaxLoadVoucherNames")
    public String ajaxLoadVoucherNames() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepare...");
        final List<Object> voucherNameList = getPersistenceService().findAllBy(
                "select  distinct name from  CVoucherHeader where type=?", type);
        nameList = new ArrayList<Map<String, String>>();
        Map<String, String> voucherNamesMap;
        for (final Object voucherName : voucherNameList) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("..................................................................." + (String) voucherName);
            voucherNamesMap = new LinkedHashMap<String, String>();
            voucherNamesMap.put("key", (String) voucherName);
            voucherNamesMap.put("val", (String) voucherName);
            nameList.add(voucherNamesMap);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadVoucherNames.");
        return "voucherNames";

    }

    @Action(value = "/voucher/common-ajaxValidateChequeNumber")
    public String ajaxValidateChequeNumber() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxValidateChequeNumber...");
        final String index = parameters.get("index")[0];
        value = instrumentService.isChequeNumberValid(chequeNumber, bankaccountId, departmentId.intValue(), serialNo) == true ? index
                + "~true" : index + "~false";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxValidateChequeNumber.");
        return "result";
    }

    public String ajaxValidateRtgsNumber() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxValidateRtgsNumber...");
        final String index = parameters.get("index")[0];
        value = instrumentService.isRtgsNumberValid(chequeNumber, bankaccountId) == true ? index + "~true" : index + "~false";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxValidateRtgsNumber.");
        return "result";
    }

    @Action(value = "/voucher/common-ajaxValidateReassignSurrenderChequeNumber")
    public String ajaxValidateReassignSurrenderChequeNumber() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxValidateReassignSurrenderChequeNumber...");
        final String index = parameters.get("index")[0];
        value = instrumentService.isReassigningChequeNumberValid(chequeNumber, bankaccountId, departmentId.intValue(), serialNo) == true
                ? index
                        + "~true"
                : index + "~false";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxValidateReassignSurrenderChequeNumber.");
        return "result";
    }

    @Action(value = "/voucher/common-ajaxLoadUser")
    public String ajaxLoadUser() throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadUser...");
        userList = new ArrayList<User>();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadUserByDesg | Start");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Functionar received : = " + functionaryName);
        String functionaryId = null;
        if (!"ANYFUNCTIONARY".equalsIgnoreCase(functionaryName)) {
            final Functionary functionary = (Functionary) persistenceService.find("from Functionary where name='"
                    + functionaryName
                    + "'");
            functionaryId = functionary != null ? functionary.getId().toString() : null;
        }
        if (departmentId != -1 && designationId != -1 && null != functionaryName && functionaryName.trim().length() != 0) {
            final List<EmployeeView> empInfoList = voucherService.getUserByDeptAndDesgName(departmentId.toString(),
                    designationId.toString(), functionaryId);
            for (final EmployeeView employeeView : empInfoList)
                userList.add(employeeView.getEmployee());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadUser.");
        return "users";
    }

    public String ajaxHodForDept() throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxHodForDept...");
        userList = new ArrayList<User>();

        final List<PersonalInformation> listOfPI = null;// new EisUtilService().getAllHodEmpByDept(departmentId);
        for (final PersonalInformation personalInformation : listOfPI)
            userList.add(personalInformation.getUserMaster());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxHodForDept.");
        return "users";
    }

    @Action(value = "/voucher/common-ajaxLoadCodesOfDetailType")
    public String ajaxLoadCodesOfDetailType() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadCodesOfDetailType...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Detail type id  : " + accountDetailType);
        if (null == accountDetailType)
            accountCodesForDetailTypeList = egovCommon.getAllAccountCodesForAccountDetailType(-1);
        else
            accountCodesForDetailTypeList = egovCommon.getAllAccountCodesForAccountDetailType(accountDetailType);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Scheme List size : " + accountCodesForDetailTypeList.size());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadCodesOfDetailType.");
        return "accountcodes";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadEntites")
    public String ajaxLoadEntites() throws ClassNotFoundException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadEntites...");
        if (accountDetailType == null)
            entitiesList = new ArrayList<EntityType>();
        else {
            final Accountdetailtype detailType = (Accountdetailtype) persistenceService.find(
                    "from Accountdetailtype where id=? order by name", accountDetailType);
            final String table = detailType.getFullQualifiedName();
            final Class<?> service = Class.forName(table);
            String simpleName = service.getSimpleName();
            simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + "Service";

            final WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext
                    .getServletContext());
            final EntityTypeService entityService = (EntityTypeService) wac.getBean(simpleName);
            entitiesList = (List<EntityType>) entityService.getAllActiveEntities(accountDetailType);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadEntites.");
        return "entities";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadEntitesBy20")
    public String ajaxLoadEntitesBy20() throws ClassNotFoundException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadEntitesBy20...");
        if (accountDetailType == null || accountDetailType == 0)
            entitiesList = new ArrayList<EntityType>();
        else {
            final Accountdetailtype detailType = (Accountdetailtype) persistenceService.find(
                    "from Accountdetailtype where id=? order by name", accountDetailType);
            final String table = detailType.getFullQualifiedName();
            final Class<?> service = Class.forName(table);
            String simpleName = service.getSimpleName();
            simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + "Service";

            final WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext
                    .getServletContext());
            final EntityTypeService entityService = (EntityTypeService) wac.getBean(simpleName);
            entitiesList = (List<EntityType>) entityService.filterActiveEntities(startsWith, 20, detailType.getId());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadEntitesBy20.");
        return "entities";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadRTGSNumberBy20")
    public String ajaxLoadRTGSNumberBy20() throws ClassNotFoundException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadRTGSNumberBy20...");
        if (bankaccountId == null || bankaccountId == 0)
            numberList = new ArrayList<String>();
        else
            numberList = persistenceService
                    .findAllBy(
                            "SELECT ih.transactionNumber FROM InstrumentHeader ih where  ih.bankAccountId.id =? and ih.instrumentType.id=5 and upper(transactionNumber) like upper(?)",
                            bankaccountId, "%" + rtgsNumber + "%");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadRTGSNumberBy20.");
        return "rtgsNumbers";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadRTGSNumberByAccountId")
    public String ajaxLoadRTGSNumberByAccountId() throws ClassNotFoundException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadRTGSNumberBy20...");
        if (bankaccountId == null || bankaccountId == 0)
            numberList = new ArrayList<String>();
        else
            numberList = persistenceService
                    .findAllBy(
                            "SELECT ih.transactionNumber FROM InstrumentHeader ih where  ih.bankAccountId.id =?  and upper(transactionNumber) like upper(?)",
                            bankaccountId, rtgsNumber + "%");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadRTGSNumberBy20.");
        return "rtgsNumbers";
    }

    @Action(value = "/voucher/common-ajaxLoadCheckList")
    public String ajaxLoadCheckList() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadCheckList...");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("..............................................................................ajaxLoadCheckList");
        final EgBillSubType egBillSubType = (EgBillSubType) persistenceService.find("from EgBillSubType where id=?",
                billSubtypeId.longValue());
        checkList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF", egBillSubType.getName());
        if (checkList.size() == 0)
            checkList = appConfigValuesService
                    .getConfigValuesByModuleAndKey("EGF", FinancialConstants.CBILL_DEFAULTCHECKLISTNAME);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadCheckList.");
        return "checkList";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-searchEntites")
    public String searchEntites() throws ClassNotFoundException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting searchEntites...");
        searchType = "EntitySearch";
        if (accountDetailType == null)
            entitiesList = new ArrayList<EntityType>();
        else {
            final Accountdetailtype detailType = (Accountdetailtype) persistenceService.find(
                    "from Accountdetailtype where id=? order by name", accountDetailType);
            final String table = detailType.getFullQualifiedName();
            accountDetailTypeName = detailType.getName();
            try {
                final Class<?> service = Class.forName(table);
                String simpleName = service.getSimpleName();
                simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + "Service";

                final WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext
                        .getServletContext());
                EntityTypeService entityService = null;

                entityService = (EntityTypeService) wac.getBean(simpleName);
                entitiesList = (List<EntityType>) entityService.getAllActiveEntities(accountDetailType);
            } catch (final Exception e) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Service Not Available Exception : " + e.getMessage());
                entitiesList = new ArrayList<EntityType>();
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed searchEntites.");
        return "searchResult";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-searchAccountCodes")
    public String searchAccountCodes() throws ClassNotFoundException {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting accountCodes...");

        accountCodesList = persistenceService
                .findAllBy(
                        "select coa from CChartOfAccounts coa, CChartOfAccountDetail cod WHERE coa.id = cod.glCodeId AND  coa.classification = 4 order by coa.glcode asc");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed accountCodes.");
        return "searchAccountCodes";
    }

    @Action(value = "/voucher/common-ajaxLoadBanksWithAssignedRTGS")
    public String ajaxLoadBanksWithAssignedRTGS() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBanksWithAssignedCheques...");
        try {
            final String vouchersWithNewInstrumentsQuery = "select voucherheaderid from egf_instrumentvoucher eiv,egf_instrumentheader ih,"
                    +
                    " egw_status egws where eiv.instrumentheaderid=ih.id and egws.id=ih.id_status and egws.moduletype='Instrument' and egws.description='New' and "
                    +
                    " ih.transactionNumber is not null";
            StringBuffer queryString = new StringBuffer();
            queryString = queryString
                    .append("select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),"
                            +
                            "bankBranch.branchname) as bankbranchname from  voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount, ")
                    .append(" paymentheader ph where  ")
                    .append(" ph.voucherheaderid=vh.id and vh.id  in (" + vouchersWithNewInstrumentsQuery.toString()
                            + ") and bank.isactive=true  and bankBranch.isactive=true ")
                    .append(" and  bank.id = bankBranch.bankid and bankBranch.id = bankaccount.BRANCHID and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and vh.voucherdate <= :date")
                    .append(" and ph.bankaccountnumberid=bankaccount.id  and bankaccount.isactive=true order by 2");
            final List<Object[]> bankBranch = persistenceService.getSession().createSQLQuery(queryString.toString())
                    .setParameter("date", getAsOnDate())
                    .list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankBranch.size());
            bankBranchList = new ArrayList<Map<String, Object>>();
            Map<String, Object> bankBrmap;
            for (final Object[] element : bankBranch) {
                bankBrmap = new HashMap<String, Object>();
                bankBrmap.put("bankBranchId", element[0].toString());
                bankBrmap.put("bankBranchName", element[1].toString());
                bankBranchList.add(bankBrmap);
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBanksWithAssignedCheques.");
        return "bank";
    }

    /**
     * This method will load the bank and branch for which there are cheqeues assigned and the cheque status is "NEW"
     */
    @Action(value = "/voucher/common-ajaxLoadBanksWithAssignedCheques")
    public String ajaxLoadBanksWithAssignedCheques() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBanksWithAssignedCheques...");
        try {
            final String vouchersWithNewInstrumentsQuery = "select voucherheaderid from egf_instrumentvoucher eiv,egf_instrumentheader ih,"
                    +
                    " egw_status egws where eiv.instrumentheaderid=ih.id and egws.id=ih.id_status and egws.moduletype='Instrument' and egws.description='New'  ";
            StringBuffer queryString = new StringBuffer();
            queryString = queryString
                    .append("select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),"
                            +
                            "bankBranch.branchname) as bankbranchname from  voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount, ")
                    .append(" paymentheader ph where  ")
                    .append(" ph.voucherheaderid=vh.id and vh.id  in (" + vouchersWithNewInstrumentsQuery.toString()
                            + ") and bank.isactive=true  and bankBranch.isactive=true ")
                    .append(" and  bank.id = bankBranch.bankid and bankBranch.id = bankaccount.BRANCHID and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and vh.voucherdate <= :date")
                    .append(" and ph.bankaccountnumberid=bankaccount.id  and bankaccount.isactive=true order by 2");
            final List<Object[]> bankBranch = persistenceService.getSession().createSQLQuery(queryString.toString())
                    .setParameter("date", getAsOnDate())
                    .list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankBranch.size());
            bankBranchList = new ArrayList<Map<String, Object>>();
            Map<String, Object> bankBrmap;
            for (final Object[] element : bankBranch) {
                bankBrmap = new HashMap<String, Object>();
                bankBrmap.put("bankBranchId", element[0].toString());
                bankBrmap.put("bankBranchName", element[1].toString());
                bankBranchList.add(bankBrmap);
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBanksWithAssignedCheques.");
        return "bank";
    }

    /**
     * This method is to get the list of bank accounts for a particular bank branch for which there are cheques assigned in "NEW"
     * status.
     * @return
     */
    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBanksAccountsWithAssignedRTGS")
    public String ajaxLoadBanksAccountsWithAssignedRTGS() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBanksAccountsWithAssignedCheques...");
        try {
            accNumList = new ArrayList<Bankaccount>();
            StringBuffer queryString = new StringBuffer();
            queryString = queryString
                    .append("select bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode "
                            +
                            " from  voucherheader vh,chartofaccounts coa,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,paymentheader ph,  "
                            +
                            "egf_instrumentvoucher eiv,egf_instrumentheader ih,egw_status egws ")
                    .append("where ph.voucherheaderid=vh.id and coa.id=bankaccount.glcodeid and vh.id=eiv.VOUCHERHEADERID and ")
                    .append("  eiv.instrumentheaderid=ih.id and egws.id=ih.id_status and egws.moduletype='Instrument' and egws.description='New' and ih.transactionNumber is not null")
                    .append("and ih.instrumenttype=(select id from egf_instrumenttype where upper(type)='CHEQUE') and ispaycheque='1' ")
                    .append(" and bank.isactive=true  and bankBranch.isactive=true and bankaccount.isactive=true ")
                    .append(" and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.branchid="
                            + branchId
                            + "  and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and vh.voucherdate <= :date");

            queryString = queryString.append(" and ph.bankaccountnumberid=bankaccount.id  order by vh.voucherdate desc");
            final List<Object[]> bankAccounts = persistenceService.getSession().createSQLQuery(queryString.toString())
                    .setDate("date", getAsOnDate())
                    .list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankAccounts.size());
            final List<String> addedBanks = new ArrayList<String>();
            for (final Object[] account : bankAccounts) {
                final String accountNumberAndType = account[0].toString() + "-" + account[1].toString();
                if (!addedBanks.contains(accountNumberAndType)) {
                    final Bankaccount bankaccount = new Bankaccount();
                    bankaccount.setAccountnumber(account[0].toString());
                    bankaccount.setAccounttype(account[1].toString());
                    bankaccount.setId(Long.valueOf(account[2].toString()));
                    final CChartOfAccounts chartofaccounts = new CChartOfAccounts();
                    chartofaccounts.setGlcode(account[3].toString());
                    bankaccount.setChartofaccounts(chartofaccounts);
                    addedBanks.add(accountNumberAndType);
                    accNumList.add(bankaccount);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBanksAccountsWithAssignedCheques.");
        return "bankAccNum";
    }

    /**
     * This method is to get the list of bank accounts for a particular bank branch for which there are cheques assigned in "NEW"
     * status.
     * @return
     */
    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBanksAccountsWithAssignedCheques")
    public String ajaxLoadBanksAccountsWithAssignedCheques() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBanksAccountsWithAssignedCheques...");
        try {
            accNumList = new ArrayList<Bankaccount>();
            StringBuffer queryString = new StringBuffer();
            queryString = queryString
                    .append("select bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode ,bank.name as bankName"
                            +
                            " from  voucherheader vh,chartofaccounts coa,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,paymentheader ph,  "
                            +
                            "egf_instrumentvoucher eiv,egf_instrumentheader ih,egw_status egws ")
                    .append("where ph.voucherheaderid=vh.id and coa.id=bankaccount.glcodeid and vh.id=eiv.VOUCHERHEADERID and ")
                    .append("  eiv.instrumentheaderid=ih.id and egws.id=ih.id_status and egws.moduletype='Instrument' and egws.description='New' ")
                    .append("and ih.instrumenttype=(select id from egf_instrumenttype where upper(type)=:type) and ispaycheque='1' ")
                    .append(" and bank.isactive=true  and bankBranch.isactive=true and bankaccount.isactive=true ")
                    .append(" and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.branchid="
                            + branchId
                            + "  and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and vh.voucherdate <= :date");

            queryString = queryString.append(" and ph.bankaccountnumberid=bankaccount.id  order by vh.voucherdate desc");
            if (type == null || type.equalsIgnoreCase(""))
                type = "CHEQUE";
            final List<Object[]> bankAccounts = persistenceService.getSession().createSQLQuery(queryString.toString())
                    .setDate("date", getAsOnDate())
                    .setString("type", type)
                    .list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankAccounts.size());
            final List<String> addedBanks = new ArrayList<String>();
            for (final Object[] account : bankAccounts) {
                final String accountNumberAndType = account[0] != null ? account[0].toString()
                        : "" + "-" + account[4] != null ? account[4].toString() : "";
                if (!addedBanks.contains(accountNumberAndType)) {
                    final Bankaccount bankaccount = new Bankaccount();
                    bankaccount.setAccountnumber(account[0] != null ? account[0].toString() : "");
                    // bankaccount.setAccounttype(account[1]!=null?account[1].toString():"");
                    bankaccount.setId(Long.valueOf(account[2] != null ? account[2].toString() : ""));
                    final CChartOfAccounts chartofaccounts = new CChartOfAccounts();
                    chartofaccounts.setGlcode(account[3] != null ? account[3].toString() : "");
                    final Bankbranch branch = new Bankbranch();
                    final Bank bank = new Bank();
                    bank.setName(account[4].toString());
                    branch.setBank(bank);
                    bankaccount.setBankbranch(branch);
                    bankaccount.setChartofaccounts(chartofaccounts);
                    addedBanks.add(accountNumberAndType);
                    accNumList.add(bankaccount);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBanksAccountsWithAssignedCheques.");
        return "bankAccNum";
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(final Integer branchId) {
        this.branchId = branchId;
    }

    public List<Bankaccount> getAccNumList() {
        return accNumList;
    }

    public void setAccNumList(final List<Bankaccount> accNumList) {
        this.accNumList = accNumList;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public Integer getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(final Integer schemeId) {
        this.schemeId = schemeId;
    }

    public List<Scheme> getSchemeList() {
        return schemeList;
    }

    public void setSchemeList(final List<Scheme> schemeList) {
        this.schemeList = schemeList;
    }

    public List<SubScheme> getSubSchemes() {
        return subSchemes;
    }

    public void setSubSchemes(final List<SubScheme> subSchemes) {
        this.subSchemes = subSchemes;
    }

    public List<Map<String, Object>> getBankBranchList() {
        return bankBranchList;
    }

    public void setBankBranchList(final List<Map<String, Object>> bankBranchList) {
        this.bankBranchList = bankBranchList;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(final Integer bankId) {
        this.bankId = bankId;
    }

    public List<Bankbranch> getBranchList() {
        return branchList;
    }

    public void setBranchList(final List<Bankbranch> branchList) {
        this.branchList = branchList;
    }

    public Long getBankaccountId() {
        return bankaccountId;
    }

    public void setBankaccountId(final Long bankaccountId) {
        this.bankaccountId = bankaccountId;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(final String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public void setInstrumentService(final InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Long departmentId) {
        this.departmentId = departmentId;
    }

    public void setDetailCodes(final List<String> detailCodes) {
        this.detailCodes = detailCodes;
    }

    public List<String> getDetailCodes() {
        return detailCodes;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(final List<User> userList) {
        this.userList = userList;
    }

    public void setDesignationId(final Integer designationId) {
        this.designationId = designationId;
    }

    public void setFunctionaryName(final String functionaryName) {
        this.functionaryName = functionaryName;
    }

    public EgovCommon getEgovCommon() {
        return egovCommon;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public List<CChartOfAccounts> getAccountCodesForDetailTypeList() {
        return accountCodesForDetailTypeList;
    }

    public void setAccountCodesForDetailTypeList(final List<CChartOfAccounts> accountCodesForDetailTypeList) {
        this.accountCodesForDetailTypeList = accountCodesForDetailTypeList;
    }

    public List<DrawingOfficer> getDrawingList() {
        return drawingList;
    }

    public void setDrawingList(final List<DrawingOfficer> drawingList) {
        this.drawingList = drawingList;
    }

    public List<EntityType> getEntitiesList() {
        return entitiesList;
    }

    public void setEntitiesList(final List<EntityType> entitiesList) {
        this.entitiesList = entitiesList;
    }

    public Integer getAccountDetailType() {
        return accountDetailType;
    }

    public void setAccountDetailType(final Integer accountDetailType) {
        this.accountDetailType = accountDetailType;
    }

    public Integer getBillSubtypeId() {
        return billSubtypeId;
    }

    public void setBillSubtypeId(final Integer billSubtypeId) {
        this.billSubtypeId = billSubtypeId;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(final String billType) {
        this.billType = billType;
    }

    public List<AppConfigValues> getCheckList() {
        return checkList;
    }

    public void setCheckList(final List<AppConfigValues> checkList) {
        this.checkList = checkList;
    }

    public VoucherService getVoucherService() {
        return voucherService;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public List getNameList() {
        return nameList;
    }

    public void setNameList(final List nameList) {
        this.nameList = (ArrayList<Map<String, String>>) nameList;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(final String searchType) {
        this.searchType = searchType;
    }

    public String getAccountDetailTypeName() {
        return accountDetailTypeName;
    }

    public void setAccountDetailTypeName(final String accountDetailTypeName) {
        this.accountDetailTypeName = accountDetailTypeName;
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }

    public void setTypeOfAccount(final String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBanksWithApprovedPayments")
    public String ajaxLoadBanksWithApprovedPayments() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBanksWithApprovedPayments...");
        try {
            StringBuffer queryString = new StringBuffer();
            // query to fetch vouchers for which no cheque has been assigned
            queryString = queryString
                    .append("select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),"
                            +
                            " bankBranch.branchname) as bankbranchname " +
                            " from Bank bank,  Bankbranch bankBranch,   Bankaccount bankaccount where bankaccount.id in ( " +
                            " select DISTINCT ph.bankaccountnumberid from " +
                            " paymentheader ph,egf_instrumentvoucher iv right outer join voucherheader vh on " +
                            " vh.id =iv.VOUCHERHEADERID where ph.voucherheaderid=vh.id  and " +
                            " vh.status=0  and " +
                            " ph.voucherheaderid=vh.id  " +
                            " and iv.VOUCHERHEADERID is null ");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and vh.fundid=" + fundId.longValue());
            queryString = queryString.append(" and vh.name NOT IN ( '" + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                    + "','" + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY + "')");
            queryString = queryString.append("and vh.voucherdate <= :date1 )");
            queryString = queryString
                    .append(" AND bank.id = bankBranch.bankid AND bank.isactive=true AND bankBranch.isactive=true " +
                            "AND bankaccount.type IN ('RECEIPTS_PAYMENTS','PAYMENTS') AND bankBranch.id = bankaccount.branchid");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());

            // query to fetch vouchers for which cheque has been assigned and surrendered
            queryString = queryString
                    .append(" union select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),"
                            +
                            "bankBranch.branchname) as bankbranchname "
                            +
                            " from Bank bank,  Bankbranch bankBranch,   Bankaccount bankaccount where bankaccount.id in ( "
                            +
                            " select DISTINCT ph.bankaccountnumberid from "
                            +
                            " egf_instrumentvoucher iv,voucherheader vh,"
                            +
                            " paymentheader ph,egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, "
                            +
                            "(select bankid,bankaccountid,instrumentnumber,max(id) as id from egf_instrumentheader group by bankid,bankaccountid,"
                            +
                            "instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber "
                            +
                            " and max_rec.id=ih1.id) ih where ph.voucherheaderid=vh.id " +
                            " and vh.status=0  and ph.voucherheaderid=vh.id " +
                            " and iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and " +
                            "ih.id_status=egws.id and egws.description in  ('Surrendered','Surrender_For_Reassign')");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and vh.fundid=" + fundId.longValue());

            queryString = queryString.append("  and vh.voucherdate <= :date2 " +
                    " and vh.name NOT IN ( '" + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE + "','"
                    + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY + "' ) ) ");
            queryString = queryString
                    .append(" AND bank.id = bankBranch.bankid AND bank.isactive=true AND bankBranch.isactive=true " +
                            "AND bankaccount.type IN ('RECEIPTS_PAYMENTS','PAYMENTS') AND bankBranch.id = bankaccount.branchid");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank check dates are  " + getAsOnDate() + queryString.toString());
            final List<Object[]> bankBranch = persistenceService.getSession().createSQLQuery(queryString.toString())
                    .setDate("date1", getAsOnDate())
                    .setDate("date2", getAsOnDate())
                    .list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankBranch.size());
            bankBranchList = new ArrayList<Map<String, Object>>();
            final List<String> addedBanks = new ArrayList<String>();
            for (final Object[] account : bankBranch) {
                final String bankBranchName = account[1].toString();
                if (!addedBanks.contains(bankBranchName)) {
                    addedBanks.add(bankBranchName);
                    final Map<String, Object> bankBrmap = new HashMap<String, Object>();
                    bankBrmap.put("bankBranchId", account[0].toString());
                    bankBrmap.put("bankBranchName", bankBranchName);
                    bankBranchList.add(bankBrmap);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBanksWithApprovedPayments.");
        return "bank";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBanksWithRtgsPayments")
    public String ajaxLoadBanksWithRtgsPayments() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBanksWithApprovedPayments...");
        try {
            StringBuffer queryString = new StringBuffer();
            // query to fetch vouchers for which no cheque has been assigned
            queryString = queryString
                    .append("select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),"
                            +
                            "bankBranch.branchname) as bankbranchname "
                            +
                            "from voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,"
                            +
                            "generalledger gl,paymentheader ph,egf_instrumentvoucher iv right outer join voucherheader vh1 on "
                            +
                            "vh1.id =iv.VOUCHERHEADERID,egw_status egws where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and "
                            +
                            "vmis.departmentid= d.id and vh.status=0 and gl.voucherheaderid=vh.id and "
                            +
                            "ph.voucherheaderid=vh.id and bank.isactive=true  and bankBranch.isactive=true and bank.id = bankBranch.bankid and "
                            +
                            "bankBranch.id = bankaccount.branchid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS')" +
                            " and  vh1.id=vh.id and iv.VOUCHERHEADERID is null ");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            if (departmentId != null && departmentId != 0 && departmentId != -1)
                queryString = queryString.append(" and vmis.departmentid=" + departmentId);
            queryString = queryString
                    .append(" and gl.debitamount!=0 and gl.debitamount is not null and vh.voucherdate <= :date1 " +
                            " and ph.bankaccountnumberid=bankaccount.id and vh.type='"
                            + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and  vh.name NOT IN ( '"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE + "','"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY + "' ) ");
            // query to fetch vouchers for which cheque has been assigned and surrendered
            queryString = queryString
                    .append(" union select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),"
                            +
                            "bankBranch.branchname) as bankbranchname from egf_instrumentvoucher iv,voucherheader vh,"
                            +
                            "Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,generalledger gl,"
                            +
                            "paymentheader ph,egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, "
                            +
                            "(select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader group by bankid,bankaccountid,"
                            +
                            "instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber "
                            +
                            "and max_rec.lastmodifieddate=ih1.lastmodifieddate) ih where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and "
                            +
                            "vmis.departmentid= d.id and vh.status=0 and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id "
                            +
                            "and bank.isactive=true  and bankBranch.isactive=true and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and "
                            +
                            "bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and "
                            +
                            "ih.id_status=egws.id and egws.description in  ('Surrendered','Surrender_For_Reassign')");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            if (departmentId != null && departmentId != 0 && departmentId != -1)
                queryString = queryString.append(" and vmis.departmentid=" + departmentId);
            queryString = queryString
                    .append(" and gl.debitamount!=0 and gl.debitamount is not null and vh.voucherdate <= :date2 " +
                            " and ph.bankaccountnumberid=bankaccount.id and vh.type='"
                            + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and vh.name NOT IN ( '"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE + "','"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY + "' ) order by 2 ");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank check dates are  " + getAsOnDate());
            final List<Object[]> bankBranch = persistenceService.getSession().createSQLQuery(queryString.toString())
                    .setDate("date1", getAsOnDate())
                    .setDate("date2", getAsOnDate())
                    .list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankBranch.size());
            bankBranchList = new ArrayList<Map<String, Object>>();
            final List<String> addedBanks = new ArrayList<String>();
            for (final Object[] account : bankBranch) {
                final String bankBranchName = account[1].toString();
                if (!addedBanks.contains(bankBranchName)) {
                    addedBanks.add(bankBranchName);
                    final Map<String, Object> bankBrmap = new HashMap<String, Object>();
                    bankBrmap.put("bankBranchId", account[0].toString());
                    bankBrmap.put("bankBranchName", bankBranchName);
                    bankBranchList.add(bankBrmap);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBanksWithApprovedPayments.");
        return "bank";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBanksWithApprovedSalaryPayments")
    public String ajaxLoadBanksWithApprovedSalaryPayments() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBanksWithApprovedSalaryPayments...");
        try {
            StringBuffer queryString = new StringBuffer();
            // query to fetch vouchers for which no cheque has been assigned
            queryString = queryString
                    .append("select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),"
                            +
                            "bankBranch.branchname) as bankbranchname "
                            +
                            "from voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,"
                            +
                            "generalledger gl,paymentheader ph,egf_instrumentvoucher iv right outer join voucherheader vh1 on "
                            +
                            "vh1.id =iv.VOUCHERHEADERID,egw_status egws where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and "
                            +
                            "vmis.departmentid= d.id and vh.status=0 and gl.voucherheaderid=vh.id and "
                            +
                            "ph.voucherheaderid=vh.id and bank.isactive=true  and bankBranch.isactive=true and bank.id = bankBranch.bankid and "
                            +
                            "bankBranch.id = bankaccount.branchid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS')" +
                            " and  vh1.id=vh.id and iv.VOUCHERHEADERID is null ");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            if (departmentId != null && departmentId != 0 && departmentId != -1)
                queryString = queryString.append(" and vmis.departmentid=" + departmentId);
            queryString = queryString
                    .append(" and gl.debitamount!=0 and gl.debitamount is not null and vh.voucherdate <= :date1 " +
                            " and ph.bankaccountnumberid=bankaccount.id and vh.type='"
                            + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and vh.name='"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY + "'  ");
            // query to fetch vouchers for which cheque has been assigned and surrendered
            queryString = queryString
                    .append(" union select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),"
                            +
                            "bankBranch.branchname) as bankbranchname from egf_instrumentvoucher iv,voucherheader vh,"
                            +
                            "Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,generalledger gl,"
                            +
                            "paymentheader ph,egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, "
                            +
                            "(select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader group by bankid,bankaccountid,"
                            +
                            "instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber "
                            +
                            "and max_rec.lastmodifieddate=ih1.lastmodifieddate) ih where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and "
                            +
                            "vmis.departmentid= d.id and vh.status=0 and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id "
                            +
                            "and bank.isactive=true  and bankBranch.isactive=true and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and "
                            +
                            "bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and "
                            +
                            "ih.id_status=egws.id and egws.description in  ('Surrendered','Surrender_For_Reassign')");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            if (departmentId != null && departmentId != 0 && departmentId != -1)
                queryString = queryString.append(" and vmis.departmentid=" + departmentId);
            queryString = queryString
                    .append(" and gl.debitamount!=0 and gl.debitamount is not null and vh.voucherdate <= :date2 " +
                            " and ph.bankaccountnumberid=bankaccount.id and vh.type='"
                            + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and vh.name='"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY + "' order by 2  ");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank check dates are  " + getAsOnDate());
            final List<Object[]> bankBranch = persistenceService.getSession().createSQLQuery(queryString.toString())
                    .setDate("date1", getAsOnDate())
                    .setDate("date2", getAsOnDate())
                    .list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankBranch.size());
            bankBranchList = new ArrayList<Map<String, Object>>();
            final List<String> addedBanks = new ArrayList<String>();
            for (final Object[] account : bankBranch) {
                final String bankBranchName = account[1].toString();
                if (!addedBanks.contains(bankBranchName)) {
                    addedBanks.add(bankBranchName);
                    final Map<String, Object> bankBrmap = new HashMap<String, Object>();
                    bankBrmap.put("bankBranchId", account[0].toString());
                    bankBrmap.put("bankBranchName", bankBranchName);
                    bankBranchList.add(bankBrmap);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBanksWithApprovedSalaryPayments.");
        return "bank";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBanksWithApprovedPensionPayments")
    public String ajaxLoadBanksWithApprovedPensionPayments() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBanksWithApprovedSalaryPayments...");
        try {
            StringBuffer queryString = new StringBuffer();
            // query to fetch vouchers for which no cheque has been assigned
            queryString = queryString
                    .append("select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),"
                            +
                            "bankBranch.branchname) as bankbranchname "
                            +
                            "from voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,"
                            +
                            "generalledger gl,paymentheader ph,egf_instrumentvoucher iv right outer join voucherheader vh1 on "
                            +
                            "vh1.id =iv.VOUCHERHEADERID,egw_status egws where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and "
                            +
                            "vmis.departmentid= d.id and vh.status=0 and gl.voucherheaderid=vh.id and "
                            +
                            "ph.voucherheaderid=vh.id and bank.isactive=true  and bankBranch.isactive=true and bank.id = bankBranch.bankid and "
                            +
                            "bankBranch.id = bankaccount.branchid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS')" +
                            " and  vh1.id=vh.id and iv.VOUCHERHEADERID is null ");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            if (departmentId != null && departmentId != 0 && departmentId != -1)
                queryString = queryString.append(" and vmis.departmentid=" + departmentId);
            queryString = queryString
                    .append(" and gl.debitamount!=0 and gl.debitamount is not null and vh.voucherdate <= :date1 " +
                            " and ph.bankaccountnumberid=bankaccount.id and vh.type='"
                            + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and vh.name='"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_PENSION + "'  ");
            // query to fetch vouchers for which cheque has been assigned and surrendered
            queryString = queryString
                    .append(" union select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),"
                            +
                            "bankBranch.branchname) as bankbranchname from egf_instrumentvoucher iv,voucherheader vh,"
                            +
                            "Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,generalledger gl,"
                            +
                            "paymentheader ph,egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, "
                            +
                            "(select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader group by bankid,bankaccountid,"
                            +
                            "instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber "
                            +
                            "and max_rec.lastmodifieddate=ih1.lastmodifieddate) ih where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and "
                            +
                            "vmis.departmentid= d.id and vh.status=0 and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id "
                            +
                            "and bank.isactive=true  and bankBranch.isactive=true and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and "
                            +
                            "bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and "
                            +
                            "ih.id_status=egws.id and egws.description in  ('Surrendered','Surrender_For_Reassign')");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            if (departmentId != null && departmentId != 0 && departmentId != -1)
                queryString = queryString.append(" and vmis.departmentid=" + departmentId);
            queryString = queryString
                    .append(" and gl.debitamount!=0 and gl.debitamount is not null and vh.voucherdate <= :date2 " +
                            " and ph.bankaccountnumberid=bankaccount.id and vh.type='"
                            + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and vh.name='"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_PENSION + "' order by 2  ");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank check dates are  " + getAsOnDate());
            final List<Object[]> bankBranch = persistenceService.getSession().createSQLQuery(queryString.toString())
                    .setDate("date1", getAsOnDate())
                    .setDate("date2", getAsOnDate())
                    .list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankBranch.size());
            bankBranchList = new ArrayList<Map<String, Object>>();
            final List<String> addedBanks = new ArrayList<String>();
            for (final Object[] account : bankBranch) {
                final String bankBranchName = account[1].toString();
                if (!addedBanks.contains(bankBranchName)) {
                    addedBanks.add(bankBranchName);
                    final Map<String, Object> bankBrmap = new HashMap<String, Object>();
                    bankBrmap.put("bankBranchId", account[0].toString());
                    bankBrmap.put("bankBranchName", bankBranchName);
                    bankBranchList.add(bankBrmap);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBanksWithApprovedPensionPayments.");
        return "bank";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBanksWithApprovedRemittances")
    public String ajaxLoadBanksWithApprovedRemittances() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBanksWithApprovedRemittances...");
        try {
            StringBuffer queryString = new StringBuffer();
            queryString = queryString
                    .append("select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),"
                            +
                            "bankBranch.branchname) as bankbranchname "
                            +
                            "from voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,eg_remittance rem,"
                            +
                            "generalledger gl left outer join function f on gl.functionid=f.id,paymentheader ph,"
                            +
                            "egf_instrumentvoucher iv right outer join voucherheader vh1 on vh1.id =iv.VOUCHERHEADERID,egw_status egws "
                            +
                            "where  ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and vmis.departmentid= d.id and vh.status=0 "
                            +
                            "and rem.paymentvhid=vh.id ");
            if (recoveryId != null && recoveryId != 0)
                queryString = queryString.append(" and rem.tdsid==" + recoveryId);
            queryString = queryString.append(
                    " and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id and bank.isactive=true  and bankBranch.isactive=true "
                            +
                            "and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS')"
                            +
                            " and  vh1.id=vh.id and iv.VOUCHERHEADERID is null ");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            queryString = queryString
                    .append(" and gl.debitamount!=0 and gl.debitamount is not null and vh.voucherdate <= :date1")
                    .append(" and ph.bankaccountnumberid=bankaccount.id and ph.type='" + FinancialConstants.MODEOFPAYMENT_CASH
                            + "'  and vh.type='" + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and vh.name='"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE + "' ");

            queryString
                    .append(" union select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),"
                            +
                            "bankBranch.branchname) as bankbranchname from egf_instrumentvoucher iv,voucherheader vh,eg_remittance rem,"
                            +
                            "Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,generalledger gl left outer join function f on "
                            +
                            "gl.functionid=f.id,paymentheader ph, egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, "
                            +
                            "(select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader group by bankid,bankaccountid,"
                            +
                            "instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber "
                            +
                            "and max_rec.lastmodifieddate=ih1.lastmodifieddate) ih where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and "
                            +
                            "vmis.departmentid= d.id and vh.status=0 and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id "
                            +
                            "and bank.isactive=true  and bankBranch.isactive=true and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and "
                            +
                            "bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and "
                            +
                            " rem.paymentvhid=vh.id ");
            if (recoveryId != null && recoveryId != 0)
                queryString = queryString.append(" and rem.tdsid==" + recoveryId);
            queryString = queryString
                    .append(" and ih.id_status=egws.id and egws.description in ('Surrendered','Surrender_For_Reassign') ");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            queryString = queryString
                    .append(" and gl.debitamount!=0 and gl.debitamount is not null and vh.voucherdate <= :date2")
                    .append(" and ph.bankaccountnumberid=bankaccount.id and ph.type='" + FinancialConstants.MODEOFPAYMENT_CASH
                            + "' and vh.type='" + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and vh.name='"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE + "' order by 2 ");
            final List<Object[]> bankBranch = persistenceService.getSession().createSQLQuery(queryString.toString())
                    .setDate("date1", getAsOnDate())
                    .setDate("date2", getAsOnDate())
                    .list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankBranch.size());
            bankBranchList = new ArrayList<>();
            final List<String> addedBanks = new ArrayList<>();
            for (final Object[] account : bankBranch) {
                final String bankBranchName = account[1].toString();
                if (!addedBanks.contains(bankBranchName)) {
                    addedBanks.add(bankBranchName);
                    final Map<String, Object> bankBrmap = new HashMap<>();
                    bankBrmap.put("bankBranchId", account[0].toString());
                    bankBrmap.put("bankBranchName", bankBranchName);
                    bankBranchList.add(bankBrmap);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBanksWithApprovedRemittances.");
        return "bank";
    }

    /**
     * @param voucherStatusKey - The appconfig key which gives the voucher workflow status
     * @param asOnDate
     * @param fundId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBanksWithPaymentInWorkFlow")
    public String ajaxLoadBanksWithPaymentInWorkFlow() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBanksWithPaymentInWorkFlow...");
        try {
            final String voucherStatusKey = parameters.get("voucherStatusKey")[0];
            final List<AppConfigValues> appConfig = appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF,
                    voucherStatusKey);
            if (appConfig == null || appConfig.isEmpty())
                throw new ValidationException("", "VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is not defined in AppConfig");

            String appConfigValue = "";
            boolean condtitionalAppConfigIsPresent = false;
            String designationName = null;
            String functionaryName = null;
            String stateWithoutCondition = "";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Before appConfig Checking  -----");
            for (final AppConfigValues app : appConfig) {
                appConfigValue = app.getValue();
                if (appConfigValue.contains(FinancialConstants.DELIMITER_FOR_VOUCHER_STATUS_TO_CHECK_BANK_BALANCE)) {
                    condtitionalAppConfigIsPresent = true;
                    final String[] array = appConfigValue
                            .split(FinancialConstants.DELIMITER_FOR_VOUCHER_STATUS_TO_CHECK_BANK_BALANCE);
                    if (array.length != 2)
                        throw new ValidationException("", "VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is invalid");
                    // Order assumed is first is designation Name, second functionary Name
                    designationName = array[0];
                    functionaryName = array[1];
                } else
                    stateWithoutCondition = appConfigValue;
            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("After appConfig Checking  -----");
            List<Bankaccount> bankAccounts = null;
            if (condtitionalAppConfigIsPresent) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("condtitionalAppConfigIsPresent -----");
                final String ownerIdList = getCommaSeperatedListForDesignationNameAndFunctionaryName(designationName,
                        functionaryName);
                bankAccounts = persistenceService.findAllBy("select p.bankaccount" +
                        " from Paymentheader p where p.voucherheader.voucherDate<='" + Constants.DDMMYYYYFORMAT2.format(asOnDate)
                        + "' and p.state.type='Paymentheader' " +
                        " and p.bankaccount.bankbranch.bank.isactive=true  and p.bankaccount.bankbranch.isactive=true " +
                        "and p.bankaccount.fund.id=? and p.state.owner in (" + ownerIdList
                        + ") order by p.bankaccount.bankbranch.bank.name,p.bankaccount.bankbranch.branchname", fundId);
            } else
                bankAccounts = persistenceService.findAllBy("select p.bankaccount" +
                        " from Paymentheader p where p.voucherheader.voucherDate<='" + Constants.DDMMYYYYFORMAT2.format(asOnDate)
                        + "' and p.state.type='Paymentheader' " +
                        " and p.bankaccount.bankbranch.bank.isactive=true  and p.bankaccount.bankbranch.isactive=true " +
                        "and p.bankaccount.fund.id=? and p.state.value like '" + stateWithoutCondition
                        + "' order by p.bankaccount.bankbranch.bank.name,p.bankaccount.bankbranch.branchname", fundId);

            bankBranchList = new ArrayList<Map<String, Object>>();
            final List<String> addedBanks = new ArrayList<String>();
            for (final Bankaccount account : bankAccounts) {
                final String bankBranchName = account.getBankbranch().getBank().getName() + "-"
                        + account.getBankbranch().getBranchname();
                if (!addedBanks.contains(bankBranchName)) {
                    addedBanks.add(bankBranchName);
                    final Map<String, Object> bankBrmap = new HashMap<String, Object>();
                    bankBrmap.put("bankBranchId", account.getBankbranch().getBank().getId() + "-"
                            + account.getBankbranch().getId());
                    bankBrmap.put("bankBranchName", bankBranchName);
                    bankBranchList.add(bankBrmap);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBanksWithPaymentInWorkFlow.");
        return "bank";
    }

    private String getCommaSeperatedListForDesignationNameAndFunctionaryName(final String designationName,
            final String functionaryName) {
        final String qrySQL = "select pos_id from eg_eis_employeeinfo empinfo, eg_designation desg, functionary func   " +
                " where empinfo.functionary_id=func.id and empinfo.DESIGNATIONID=desg.DESIGNATIONID " +
                " and empinfo.isactive=true   " +
                " and desg.DESIGNATION_NAME like '" + designationName + "' and func.NAME like '" + functionaryName + "' ";
        final Query query = persistenceService.getSession().createSQLQuery(qrySQL);
        final List<BigDecimal> result = query.list();
        if (result == null || result.isEmpty())
            throw new ValidationException("", "No employee with functionary -" + functionaryName + " and designation - "
                    + designationName);
        final StringBuffer returnListSB = new StringBuffer();
        String commaSeperatedList = "";
        for (final BigDecimal posId : result)
            returnListSB.append(posId.toString() + ",");
        commaSeperatedList = returnListSB.substring(0, returnListSB.length() - 1);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Commo seperated  list - " + commaSeperatedList);
        return commaSeperatedList;
    }

    /**
     * @param voucherStatusKey - The appconfig key which gives the voucher workflow status
     * @param asOnDate
     * @param fundId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBankAccountsWithPaymentInWorkFlow")
    public String ajaxLoadBankAccountsWithPaymentInWorkFlow() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBankAccountsWithPaymentInWorkFlow...");
        try {
            accNumList = new ArrayList<Bankaccount>();
            final String voucherStatusKey = parameters.get("voucherStatusKey")[0];
            final List<AppConfigValues> appConfig = appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF,
                    voucherStatusKey);
            if (appConfig == null || appConfig.isEmpty())
                throw new ValidationException("", "VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is not defined in AppConfig");

            String appConfigValue = "";
            boolean condtitionalAppConfigIsPresent = false;
            String designationName = null;
            String functionaryName = null;
            String stateWithoutCondition = "";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Starting app Config checking");

            for (final AppConfigValues app : appConfig) {
                appConfigValue = app.getValue();
                if (appConfigValue.contains(FinancialConstants.DELIMITER_FOR_VOUCHER_STATUS_TO_CHECK_BANK_BALANCE)) {
                    condtitionalAppConfigIsPresent = true;
                    final String[] array = appConfigValue
                            .split(FinancialConstants.DELIMITER_FOR_VOUCHER_STATUS_TO_CHECK_BANK_BALANCE);
                    if (array.length != 2)
                        throw new ValidationException("", "VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is invalid");
                    // Order assumed is first is designation Name, second functionary Name
                    designationName = array[0];
                    functionaryName = array[1];
                } else
                    stateWithoutCondition = appConfigValue;
            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Finished app Config checking");
            List<Bankaccount> bankAccounts = null;
            if (condtitionalAppConfigIsPresent) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("condtitionalAppConfigIsPresent ....");
                final String ownerIdList = getCommaSeperatedListForDesignationNameAndFunctionaryName(designationName,
                        functionaryName);
                bankAccounts = persistenceService
                        .findAllBy(
                                "select p.bankaccount"
                                        +
                                        " from Paymentheader p where p.voucherheader.voucherDate<='"
                                        + Constants.DDMMYYYYFORMAT2.format(asOnDate)
                                        + "' and p.state.type='Paymentheader' "
                                        +
                                        " and p.bankaccount.isactive=true  and p.bankaccount.bankbranch.isactive=true and  p.bankaccount.bankbranch.id=?"
                                        +
                                        "and p.bankaccount.fund.id=? and p.state.owner in  ( " + ownerIdList
                                        + ") order by p.bankaccount.bankbranch.bank.name,p.bankaccount.bankbranch.branchname",
                                branchId, fundId);
            } else
                bankAccounts = persistenceService
                        .findAllBy(
                                "select p.bankaccount"
                                        +
                                        " from Paymentheader p where p.voucherheader.voucherDate<='"
                                        + Constants.DDMMYYYYFORMAT2.format(asOnDate)
                                        + "' and p.state.type='Paymentheader' "
                                        +
                                        " and p.bankaccount.isactive=true  and p.bankaccount.bankbranch.isactive=true and  p.bankaccount.bankbranch.id=?"
                                        +
                                        "and p.bankaccount.fund.id=? and p.state.value like '" + stateWithoutCondition
                                        + "' order by p.bankaccount.bankbranch.bank.name,p.bankaccount.bankbranch.branchname",
                                branchId, fundId);

            final List<String> addedBanks = new ArrayList<String>();
            for (final Bankaccount account : bankAccounts) {
                final String bankBranchName = account.getAccountnumber() + "-" + account.getAccounttype();
                if (!addedBanks.contains(bankBranchName)) {
                    addedBanks.add(bankBranchName);
                    accNumList.add(account);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bankaccount dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bankaccount dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBankAccountsWithPaymentInWorkFlow.");
        return "bankAccNum";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBankAccountsWithApprovedPayments")
    public String ajaxLoadBankAccountsWithApprovedPayments() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBankAccountsWithApprovedPayments...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting | ajaxLoadBankAccountsWithApprovedPayments ");
        try {
            accNumList = new ArrayList<Bankaccount>();
            StringBuffer queryString = new StringBuffer();
            // query to fetch vouchers for which no cheque has been assigned
            queryString = queryString
                    .append("SELECT  bankaccount.accountnumber AS accountnumber,  bank.name AS bankName,"
                            +
                            " CAST(bankaccount.id AS INTEGER) AS id, coa.glcode AS glCode  FROM chartofaccounts coa, bankaccount bankaccount ,bankbranch branch,bank bank "
                            +
                            " WHERE bankaccount.ID IN (SELECT DISTINCT PH.bankaccountnumberid  "
                            +
                            " FROM   paymentheader ph,  voucherheader vh left OUTER JOIN egf_instrumentvoucher iv ON vh.id =iv.VOUCHERHEADERID"
                            +
                            " WHERE ph.voucherheaderid  =vh.id AND vh.status=0 AND VH.FUNDID="
                            + fundId
                            + " AND ph.voucherheaderid    =vh.id"
                            +
                            " AND iv.VOUCHERHEADERID   IS NULL AND vh.name NOT          IN ( 'Remittance Payment','Salary Bill Payment' ))"
                            +
                            " AND coa.id = bankaccount.glcodeid AND bankaccount.type     IN ('RECEIPTS_PAYMENTS','PAYMENTS')"
                            +
                            " AND bankaccount.fundid    =" + fundId.longValue()
                            + " AND bankaccount.branchid = branch.id and branch.bankid = bank.id and  bankaccount.branchid  ="
                            + branchId
                            + " and bankaccount.isactive=true ");
            // queryString =
            // queryString.append(" and ph.bankaccountnumberid=bankaccount.id and
            // vh.type='"+FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT+"' and vh.name NOT IN (
            // '"+FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE+"','"+FinancialConstants.PAYMENTVOUCHER_NAME_SALARY+"' ) ");
            // query to fetch vouchers for which cheque has been assigned and surrendered
            queryString
                    .append(" union select bankaccount.accountnumber as accountnumber,bank.name as bankName,cast(bankaccount.id as integer) as id,coa.glcode as glCode "
                            +
                            " from chartofaccounts coa, "
                            +
                            " Bankaccount bankaccount  ,bankbranch branch,bank bank "
                            +
                            " where bankaccount.branchid = branch.id and branch.bankid = bank.id and  bankaccount.id in(SELECT DISTINCT PH.bankaccountnumberid  from  "
                            +
                            " egf_instrumentvoucher iv,voucherheader vh,"
                            +
                            " paymentheader ph,egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, "
                            +
                            " (select bankid,bankaccountid,instrumentnumber,max(id) as id from egf_instrumentheader group by bankid,bankaccountid,"
                            +
                            " instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber "
                            +
                            " and max_rec.id=ih1.id) ih where ph.voucherheaderid=vh.id  and "
                            +
                            " vh.fundid="
                            + fundId.longValue()
                            +
                            " and vh.status=0 and  ph.voucherheaderid=vh.id and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id "
                            +
                            " and ph.bankaccountnumberid=bankaccount.id  and vh.type='"
                            + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                            + "'"
                            +
                            " and vh.name NOT IN ( '"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                            + "','"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY
                            + "' )"
                            +
                            " and ih.id_status=egws.id and egws.description in ('Surrendered','Surrender_For_Reassign') )"
                            +
                            " and coa.id=bankaccount.glcodeid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS')  and bankaccount.branchid="
                            + branchId);
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());

            final List<Object[]> bankAccounts = persistenceService.getSession().createSQLQuery(queryString.toString())
                    .list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank accont list size is " + bankAccounts.size() + "and Query is " + queryString.toString());
            final List<String> addedBanks = new ArrayList<String>();
            for (final Object[] account : bankAccounts) {
                final String accountNumberAndType = account[0].toString() + "-" + account[1].toString();
                if (!addedBanks.contains(accountNumberAndType)) {
                    final Bankaccount bankaccount = new Bankaccount();
                    bankaccount.setAccountnumber(account[0].toString());
                    // bankaccount.setAccounttype(account[1].toString());
                    final CChartOfAccounts chartofaccounts = new CChartOfAccounts();
                    final Bankbranch branch = new Bankbranch();
                    final Bank bank = new Bank();
                    bank.setName(account[1].toString());
                    branch.setBank(bank);
                    bankaccount.setBankbranch(branch);
                    chartofaccounts.setGlcode(account[3].toString());
                    bankaccount.setChartofaccounts(chartofaccounts);
                    bankaccount.setId(Long.valueOf(account[2].toString()));
                    addedBanks.add(accountNumberAndType);
                    accNumList.add(bankaccount);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Done | ajaxLoadBankAccountsWithApprovedPayments ");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBankAccountsWithApprovedPayments.");
        return "bankAccNum";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBankAccountsWithApprovedSalaryPayments")
    public String ajaxLoadBankAccountsWithApprovedSalaryPayments() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBankAccountsWithApprovedSalaryPayments...");
        try {
            accNumList = new ArrayList<Bankaccount>();
            StringBuffer queryString = new StringBuffer();
            // query to fetch vouchers for which no cheque has been assigned
            queryString = queryString
                    .append("select distinct bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode "
                            +
                            "from chartofaccounts coa,voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,"
                            +
                            "generalledger gl ,paymentheader ph,"
                            +
                            "egf_instrumentvoucher iv right outer join voucherheader vh1 on vh1.id =iv.VOUCHERHEADERID  "
                            +
                            "where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and vmis.departmentid= d.id and vh.status=0 "
                            +
                            " and coa.id=bankaccount.glcodeid and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id and bank.isactive=true  and bankBranch.isactive=true "
                            +
                            "and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and bankaccount.branchid="
                            + branchId +
                            " and  vh1.id=vh.id and iv.VOUCHERHEADERID is null");

            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            if (departmentId != null && departmentId != 0 && departmentId != -1)
                queryString = queryString.append(" and vmis.departmentid=" + departmentId);
            queryString = queryString.append(" and ph.bankaccountnumberid=bankaccount.id  and vh.type='"
                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and vh.name='"
                    + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY + "'");
            // query to fetch vouchers for which cheque has been assigned and surrendered
            queryString
                    .append(" union select bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode "
                            +
                            " from chartofaccounts coa,egf_instrumentvoucher iv,voucherheader vh,"
                            +
                            "Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,generalledger gl,"
                            +
                            "paymentheader ph,egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, "
                            +
                            "(select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader group by bankid,bankaccountid,"
                            +
                            "instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber "
                            +
                            "and max_rec.lastmodifieddate=ih1.lastmodifieddate) ih where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and "
                            +
                            "vmis.departmentid= d.id and coa.id=bankaccount.glcodeid and vh.status=0 and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id "
                            +
                            "and bank.isactive=true  and bankBranch.isactive=true and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and "
                            +
                            "bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and bankaccount.branchid="
                            + branchId +
                            " and ih.id_status=egws.id and egws.description in ('Surrendered','Surrender_For_Reassign')");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            if (departmentId != null && departmentId != 0 && departmentId != -1)
                queryString = queryString.append(" and vmis.departmentid=" + departmentId);
            queryString = queryString.append(" and ph.bankaccountnumberid=bankaccount.id  and vh.type='"
                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and vh.name='"
                    + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY + "' order by 4 ");

            final List<Object[]> bankAccounts = persistenceService.getSession().createSQLQuery(queryString.toString())
                    .list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank accont list size is " + bankAccounts.size());
            final List<String> addedBanks = new ArrayList<String>();
            for (final Object[] account : bankAccounts) {
                final String accountNumberAndType = account[0].toString() + "-" + account[1].toString();
                if (!addedBanks.contains(accountNumberAndType)) {
                    final Bankaccount bankaccount = new Bankaccount();
                    bankaccount.setAccountnumber(account[0].toString());
                    bankaccount.setAccounttype(account[1].toString());
                    final CChartOfAccounts chartofaccounts = new CChartOfAccounts();
                    chartofaccounts.setGlcode(account[3].toString());
                    bankaccount.setChartofaccounts(chartofaccounts);
                    bankaccount.setId(Long.valueOf(account[2].toString()));
                    addedBanks.add(accountNumberAndType);
                    accNumList.add(bankaccount);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBankAccountsWithApprovedSalaryPayments.");
        return "bankAccNum";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBankAccountsWithApprovedPensionPayments")
    public String ajaxLoadBankAccountsWithApprovedPensionPayments() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBankAccountsWithApprovedPensionPayments...");
        try {
            accNumList = new ArrayList<Bankaccount>();
            StringBuffer queryString = new StringBuffer();
            // query to fetch vouchers for which no cheque has been assigned
            queryString = queryString
                    .append("select distinct bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode "
                            +
                            "from chartofaccounts coa,voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,"
                            +
                            "generalledger gl ,paymentheader ph,"
                            +
                            "egf_instrumentvoucher iv right outer join voucherheader vh1 on vh1.id =iv.VOUCHERHEADERID  "
                            +
                            "where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and vmis.departmentid= d.id and vh.status=0 "
                            +
                            " and coa.id=bankaccount.glcodeid and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id and bank.isactive=true  and bankBranch.isactive=true "
                            +
                            "and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and bankaccount.branchid="
                            + branchId +
                            " and  vh1.id=vh.id and iv.VOUCHERHEADERID is null");

            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            if (departmentId != null && departmentId != 0 && departmentId != -1)
                queryString = queryString.append(" and vmis.departmentid=" + departmentId);
            queryString = queryString.append(" and ph.bankaccountnumberid=bankaccount.id  and vh.type='"
                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and vh.name='"
                    + FinancialConstants.PAYMENTVOUCHER_NAME_PENSION + "'");
            // query to fetch vouchers for which cheque has been assigned and surrendered
            queryString
                    .append(" union select bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode "
                            +
                            " from chartofaccounts coa,egf_instrumentvoucher iv,voucherheader vh,"
                            +
                            "Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,generalledger gl,"
                            +
                            "paymentheader ph,egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, "
                            +
                            "(select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader group by bankid,bankaccountid,"
                            +
                            "instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber "
                            +
                            "and max_rec.lastmodifieddate=ih1.lastmodifieddate) ih where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and "
                            +
                            "vmis.departmentid= d.id and coa.id=bankaccount.glcodeid and vh.status=0 and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id "
                            +
                            "and bank.isactive=true  and bankBranch.isactive=true and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and "
                            +
                            "bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and bankaccount.branchid="
                            + branchId +
                            " and ih.id_status=egws.id and egws.description in ('Surrendered','Surrender_For_Reassign')");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            if (departmentId != null && departmentId != 0 && departmentId != -1)
                queryString = queryString.append(" and vmis.departmentid=" + departmentId);
            queryString = queryString.append(" and ph.bankaccountnumberid=bankaccount.id  and vh.type='"
                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and vh.name='"
                    + FinancialConstants.PAYMENTVOUCHER_NAME_PENSION + "' order by 4 ");

            final List<Object[]> bankAccounts = persistenceService.getSession().createSQLQuery(queryString.toString())
                    .list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank accont list size is " + bankAccounts.size());
            final List<String> addedBanks = new ArrayList<String>();
            for (final Object[] account : bankAccounts) {
                final String accountNumberAndType = account[0].toString() + "-" + account[1].toString();
                if (!addedBanks.contains(accountNumberAndType)) {
                    final Bankaccount bankaccount = new Bankaccount();
                    bankaccount.setAccountnumber(account[0].toString());
                    bankaccount.setAccounttype(account[1].toString());
                    final CChartOfAccounts chartofaccounts = new CChartOfAccounts();
                    chartofaccounts.setGlcode(account[3].toString());
                    bankaccount.setChartofaccounts(chartofaccounts);
                    bankaccount.setId(Long.valueOf(account[2].toString()));
                    addedBanks.add(accountNumberAndType);
                    accNumList.add(bankaccount);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBankAccountsWithApprovedPensionPayments.");
        return "bankAccNum";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBankAccountsWithApprovedRemittances")
    public String ajaxLoadBankAccountsWithApprovedRemittances() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBankAccountsWithApprovedRemittances...");
        try {
            accNumList = new ArrayList<Bankaccount>();
            StringBuffer queryString = new StringBuffer();
            queryString = queryString
                    .append("select distinct bankaccount.accountnumber as accountnumber,bank.name as bankName,cast(bankaccount.id as integer) as id,coa.glcode as glCode "
                            +
                            "from chartofaccounts coa,voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,EG_REMITTANCE rem ,"
                            +
                            "generalledger gl left outer join function f on gl.functionid=f.id,paymentheader ph,"
                            +
                            "egf_instrumentvoucher iv right outer join voucherheader vh1 on vh1.id =iv.VOUCHERHEADERID  "
                            +
                            "where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid and vmis.departmentid= d.id and vh.status=0 "
                            +
                            "and rem.paymentvhid=vh.id ");
            if (recoveryId != null && recoveryId != 0)
                queryString = queryString.append(" and rem.tdsid=" + recoveryId);
            queryString = queryString.append(
                    " and coa.id=bankaccount.glcodeid and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id and bank.isactive=true  and bankBranch.isactive=true "
                            +
                            " and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and bankaccount.branchid="
                            + branchId +
                            " and  vh1.id=vh.id and iv.VOUCHERHEADERID is null");

            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            queryString = queryString.append(" and ph.bankaccountnumberid=bankaccount.id and ph.type='"
                    + FinancialConstants.MODEOFPAYMENT_CASH + "' and vh.type='"
                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and vh.name='"
                    + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE + "'");

            queryString
                    .append(" union select bankaccount.accountnumber as accountnumber,bank.name as bankName,cast(bankaccount.id as integer) as id,coa.glcode as glCode "
                            +
                            " from chartofaccounts coa,egf_instrumentvoucher iv,voucherheader vh,"
                            +
                            "Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,vouchermis vmis, eg_department d,eg_remittance rem ,generalledger gl left outer join function f on "
                            +
                            "gl.functionid=f.id,paymentheader ph,egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, "
                            +
                            "(select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader group by bankid,bankaccountid,"
                            +
                            " instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber "
                            +
                            " and max_rec.lastmodifieddate=ih1.lastmodifieddate) ih where ph.voucherheaderid=vh.id and vh.id= vmis.voucherheaderid  "
                            +
                            " and rem.paymentvhid=vh.id ");
            if (recoveryId != null && recoveryId != 0)
                queryString = queryString.append(" and rem.tdsid=" + recoveryId);
            queryString = queryString.append(
                    " and vmis.departmentid= d.id and coa.id=bankaccount.glcodeid and vh.status=0 and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id "
                            +
                            " and bank.isactive=true  and bankBranch.isactive=true and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.branchid="
                            + branchId
                            +
                            " and  bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and "
                            +
                            "ih.id_status=egws.id and egws.description in ('Surrendered','Surrender_For_Reassign')");
            if (fundId != null && fundId != 0 && fundId != -1)
                queryString = queryString.append(" and bankaccount.fundid=" + fundId.longValue());
            queryString = queryString.append(" and ph.bankaccountnumberid=bankaccount.id and  ph.type='"
                    + FinancialConstants.MODEOFPAYMENT_CASH + "' and vh.type='"
                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT + "' and vh.name='"
                    + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE + "' order by 4 ");

            final List<Object[]> bankAccounts = persistenceService.getSession().createSQLQuery(queryString.toString())
                    .list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankAccounts.size());
            final List<String> addedBanks = new ArrayList<String>();
            for (final Object[] account : bankAccounts) {
                final String accountNumberAndType = account[0].toString() + "-" + account[1].toString();
                if (!addedBanks.contains(accountNumberAndType)) {
                    final Bankaccount bankaccount = new Bankaccount();
                    bankaccount.setAccountnumber(account[0].toString());
                    bankaccount.setAccounttype(account[1].toString());
                    final CChartOfAccounts chartofaccounts = new CChartOfAccounts();
                    chartofaccounts.setGlcode(account[3].toString());
                    final Bankbranch branch = new Bankbranch();
                    final Bank bank = new Bank();
                    bank.setName(account[1].toString());
                    branch.setBank(bank);
                    bankaccount.setBankbranch(branch);
                    bankaccount.setChartofaccounts(chartofaccounts);
                    bankaccount.setId(Long.valueOf(account[2].toString()));
                    addedBanks.add(accountNumberAndType);
                    accNumList.add(bankaccount);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBankAccountsWithApprovedRemittances.");
        return "bankAccNum";
    }

    public void setAsOnDate(final Date asOnDate) {
        this.asOnDate = asOnDate;
    }

    public Date getAsOnDate() {
        return asOnDate;
    }

    @Action(value = "/voucher/common-ajaxLoadDesg")
    public String ajaxLoadDesg() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadDesg...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadDesg | Start ");

        Map<String, Object> map = null;
        if (getBillRegisterId() != null) {
            final EgBillregister cbill = (EgBillregister) persistenceService.find(" from EgBillregister where id=?",
                    getBillRegisterId());
            map = voucherService.getDesgBYPassingWfItem(scriptName, cbill, departmentId.intValue());
        } else
            map = voucherService.getDesgBYPassingWfItem(scriptName, null, departmentId.intValue());

        designationList = (List<Map<String, Object>>) map.get("designationList");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadDesg | End ");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadDesg.");
        return "desg";
    }

    public String ajaxLoadDefaultDepartment() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadDefaultDepartment...");
        defaultDepartment = voucherService.getDefaultDepartment().toString();
        return "defaultDepartment";
    }

    @Action(value = "/voucher/common-ajaxLoadFundSource")
    public String ajaxLoadFundSource() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadFundSource...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | subscheme id received = " + subSchemeId);
        if (null != subSchemeId)
            fundSouceList = financingSourceService.getFinancialSourceBasedOnSubScheme(subSchemeId);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadFundSource.");
        return Constants.FUNDSOURCE;
    }

    @Action(value = "/voucher/common-ajaxLoadProjectCodesForSubScheme")
    public String ajaxLoadProjectCodesForSubScheme() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadProjectCodesForSubScheme...");
        final String sql = "select pc.id as id,pc.code as code,pc.name as name from egw_projectcode pc,egf_subscheme_project ssp where  pc.id=ssp.projectcodeid and ssp.subschemeid="
                + subSchemeId;
        final SQLQuery pcQuery = persistenceService.getSession().createSQLQuery(sql);
        pcQuery.addScalar("id", LongType.INSTANCE)
                .addScalar("code")
                .addScalar("name")
                .setResultTransformer(Transformers.aliasToBean(LoanGrantBean.class));
        projectCodeList = pcQuery.list();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadProjectCodesForSubScheme.");
        return "projectcodes";
    }

    @Action(value = "/voucher/common-ajaxLoadUnmappedProjectCodesBy20")
    public String ajaxLoadUnmappedProjectCodesBy20() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadUnmappedProjectCodesBy20...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("schemeId Id received is :  " + subSchemeId + "   and Startswith   :" + startsWith);
        projectCodeStringList = new ArrayList<String>();
        startsWith = "%" + startsWith + "%";
        final String qry = " select * from (SELECT pc.code || '`-`'|| pc.description|| '`~`' ||pc.id  FROM egw_projectcode pc LEFT OUTER JOIN "
                +
                " egf_subscheme_project sp ON pc.id = sp.projectcodeid WHERE sp.projectcodeid IS NULL and pc.code like '"
                + startsWith + "' " +
                " ORDER BY pc.code) where rownum<=20";
        if (null == subSchemeId) {

        } else
            projectCodeStringList = persistenceService.getSession().createSQLQuery(qry).list();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Scheme List size : " + projectCodeStringList.size());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadUnmappedProjectCodesBy20.");
        return "projectCodesBy20";
    }

    @Action(value = "/voucher/common-ajaxLoadDocumentNoAndDate")
    public String ajaxLoadDocumentNoAndDate() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadDocumentNoAndDate...");
        if (billVhId != null && billVhId != 0) {
            final CVoucherHeader vh = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", billVhId);
            if (vh != null) {
                final EgBillregister bill = (EgBillregister) persistenceService.find(
                        "select mis.egBillregister from EgBillregistermis  mis where mis.voucherHeader=?", vh);
                if (bill != null) {
                    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    final String billDateStr = sdf.format(bill.getBilldate());
                    returnStream = bill.getBillnumber() + "$" + billDateStr;
                }
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadDocumentNoAndDate.");
        return "AJAX_RESULT";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadChequeNoAndDate")
    public String ajaxLoadChequeNoAndDate() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadChequeNoAndDate...");
        if (billVhId != null && billVhId.intValue() != 0) {
            final String instrumentRelatedQry = "select NVL(ih.id,0), NVL( NVL(ih.instrumentnumber, ih.transactionnumber),0), "
                    +
                    " TO_CHAR(NVL(ih.instrumentdate, ih.transactiondate),'dd/mm/yyyy'), "
                    +
                    " NVL( ih.instrumentamount,0),NVL(ba.id,0),NVL(ba.accountnumber,0),NVL(bb.branchname ||'-' || b.name,0) "
                    +
                    " from egf_instrumentvoucher iv, egf_instrumentheader ih LEFT OUTER JOIN bankaccount ba ON ih.bankaccountid= ba.id 	"
                    +
                    " left outer JOIN bankbranch bb on ba.branchid= bb.id LEFT OUTER JOIN bank b ON b.id= bb.bankid  " +
                    " where iv.instrumentheaderid=ih.id and iv.voucherheaderid=" + billVhId;
            final String voucherDescriptionQry = "select NVL(vh.description,0) from voucherheader vh  where vh.id= " + billVhId;
            final String fundingAgencyQry = "select  nvl( fa.id,0),nvl(fa.name,0) from "
                    +
                    " generalledger g  LEFT OUTER JOIN generalledgerdetail gd on gd.generalledgerid= g.id, "
                    +
                    " egf_fundingagency fa where gd.detailtypeid=(select id from accountdetailtype where accountdetailtype.name='FundingAgency' ) "
                    +
                    " and fa.id= gd.detailkeyid and g.voucherheaderid= " + billVhId;
            final List<Object[]> resultList1 = persistenceService.getSession().createSQLQuery(instrumentRelatedQry).list();
            final List<Object[]> resultList2 = persistenceService.getSession().createSQLQuery(voucherDescriptionQry).list();
            final List<Object[]> resultList3 = persistenceService.getSession().createSQLQuery(fundingAgencyQry).list();
            String instrumentResult;
            if (resultList1.size() == 0)
                instrumentResult = "0$0$-$0$0$0$-";
            else
                instrumentResult = resultList1.get(0)[0] + "$" + resultList1.get(0)[1] + "$"
                        + (resultList1.get(0)[2] == null ? "-" : resultList1.get(0)[2]) +
                        "$" + resultList1.get(0)[3] + "$" + resultList1.get(0)[4] + "$" + resultList1.get(0)[5] + "$"
                        + resultList1.get(0)[6];
            final String voucherDescResult = resultList2.size() == 0 ? "$0" : "$" + resultList2.get(0);
            final String fundingAgencyResult = resultList3.size() == 0 ? "$0$0" : "$" + resultList3.get(0)[0] + "$" + resultList3
                    .get(0)[1];
            returnStream = instrumentResult + voucherDescResult + fundingAgencyResult;
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadChequeNoAndDate.");
        return "AJAX_RESULT";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadVoucherAmount")
    public String ajaxLoadVoucherAmount() {
        final String chequeAmtQry = "select ih.instrumentamount, ih.id from egf_instrumentheader ih, egf_instrumentvoucher" +
                " iv where ih.id= iv.instrumentheaderid and iv.voucherheaderid=?";
        final List<Object[]> resultList2 = persistenceService.getSession().createSQLQuery(chequeAmtQry).setLong(0, billVhId)
                .list();
        String chqAmtResult;
        if (resultList2.size() == 0)
            chqAmtResult = "0$0";
        else
            chqAmtResult = resultList2.get(0)[0] + "$" + resultList2.get(0)[1];
        new CommonBean();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadFundingAgencyAmount...");
        if (billVhId != null && billVhId.intValue() != 0) {
            final String grantAMountQry = "select sum(g.debitAmount) as accountBalance from generalledger g" +
                    " where g.voucherheaderid=? ";
            // List<Object[]> resultList1=
            final Query qry = persistenceService.getSession().createSQLQuery(grantAMountQry)
                    .addScalar("accountBalance", BigDecimalType.INSTANCE);
            qry.setLong(0, billVhId);
            qry.setResultTransformer(Transformers.aliasToBean(CommonBean.class));
            final List<CommonBean> resultList1 = qry.list();
            String grantAmountResult;
            if (resultList1.size() == 0)
                grantAmountResult = "0$0";
            else
                grantAmountResult = resultList1.get(0).getAccountBalance().toString();
            if (resultList2.size() == 0)
                returnStream = grantAmountResult;
            else
                returnStream = chqAmtResult;
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadFundingAgencyAmount.");
        return "AJAX_RESULT";
    }

    @SuppressWarnings("unchecked")
    public String ajaxLoadGrantAmountSubledger() {
        if (billVhId != null && billVhId.intValue() != 0) {
            String amount = null;
            String subLedger = null;
            String amtSubledger = null;
            final Accountdetailtype detType = (Accountdetailtype) persistenceService
                    .find("from Accountdetailtype where name='Commercial Tax Officer'");
            final List<CGeneralLedger> glList = persistenceService.findAllBy(
                    "from CGeneralLedger where voucherHeaderId.id=?", billVhId);
            if (detType != null)
                for (final CGeneralLedger gl : glList) {
                    final Set<CGeneralLedgerDetail> generalLedgerDetails = gl.getGeneralLedgerDetails();
                    for (final CGeneralLedgerDetail gld : generalLedgerDetails)
                        if (detType.getId().toString().equals(gld.getDetailTypeId().getId().toString())) {
                            amount = gld.getAmount().toString();

                            final AccountEntity entity = (AccountEntity) persistenceService.find(
                                    "from AccountEntity where id=? and accountdetailtype=?", gld.getDetailKeyId(), detType);
                            subLedger = entity.getName();

                        }
                }
            if (amount == null && subLedger == null)
                amtSubledger = "0$0";
            else
                amtSubledger = amount + "$" + subLedger;
            returnStream = amtSubledger;
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadGrantAmountSubledger.");
        return "AJAX_RESULT";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBranchAccountNumbers")
    public String ajaxLoadBranchAccountNumbers() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBranchAccountNumbers...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadBranchAccountNumbers");
        try {
            accNumList = getPersistenceService()
                    .findAllBy(
                            "from Bankaccount ba where ba.bankbranch.id=? and isactive=true order by ba.chartofaccounts.glcode",
                            branchId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank account Number list size =  " + accNumList.size());
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting bank account numbers " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting bank account numbers " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        }
        final StringBuffer accountNumbers = new StringBuffer(256);
        for (final Bankaccount acc : accNumList)
            accountNumbers.append(acc.getChartofaccounts().getGlcode() + "-" + acc.getAccountnumber() + "~" + acc.getId() + "$");
        returnStream = accountNumbers.toString();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBranchAccountNumbers.");
        return "AJAX_RESULT";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxloadcoa")
    public String ajaxLoadCOA() {
        String query = "";

        if (glCode == null) {

            coaList = (List<CChartOfAccounts>) persistenceService
                    .findAllBy("from CChartOfAccounts  where parentId is null order by glcode asc");

            // query=" SELECT '' AS \"type\", ID AS \"chartOfAccounts_ID\", name AS \"chartOfAccounts_name\", parentId AS
            // \"chartOfAccounts_parentId\", glcode AS \"chartOfAccounts_glCode\" FROM chartOfAccounts where parentId is null
            // order by id asc";

        } else {
            coaList = (List<CChartOfAccounts>) persistenceService.findAllBy(
                    "from CChartOfAccounts where parentId=? order by glcode ",
                    Long.valueOf(glCode));
            // query=" SELECT '' AS \"type\", ID AS \"chartOfAccounts_ID\", name AS \"chartOfAccounts_name\", parentId AS
            // \"chartOfAccounts_parentId\", glcode AS \"chartOfAccounts_glCode\" FROM chartOfAccounts where parentId ="+glCode+"
            // order by id asc";
        }
        result = new StringBuffer();
        StringBuffer type = new StringBuffer();
        StringBuffer chartOfAccounts_ID = new StringBuffer();
        StringBuffer chartOfAccounts_name = new StringBuffer();
        StringBuffer chartOfAccounts_parentId = new StringBuffer();
        StringBuffer chartOfAccounts_glCode = new StringBuffer();
        int i = 0;
        for (CChartOfAccounts cc : coaList) {
            if (i > 0) {
                type.append("+");
                chartOfAccounts_ID.append("+");
                chartOfAccounts_name.append("+");
                chartOfAccounts_parentId.append("+");
                chartOfAccounts_glCode.append("+");

            }
            i++;
            type.append("null");
            chartOfAccounts_ID.append(cc.getId());
            chartOfAccounts_name.append(cc.getName());
            chartOfAccounts_parentId.append(cc.getParentId());
            chartOfAccounts_glCode.append(cc.getGlcode());

        }
        result.append(type.toString() + "^" + chartOfAccounts_ID.toString() + "^" + chartOfAccounts_name + "^" +
                chartOfAccounts_parentId.toString() + "^" + chartOfAccounts_glCode.toString() + "^");
        return "COA";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadGLreportCodes")
    public String ajaxLoadGLreportCodes() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadGLreportCodes...");
        if (glCode == null)
            glCodesList = new ArrayList<CChartOfAccounts>();
        else
            glCodesList = persistenceService
                    .findAllBy(
                            "select ca from CChartOfAccounts ca where"
                                    +
                                    " ca.glcode not in(select glcode from CChartOfAccounts where glcode like '47%' and glcode not like '471%' and glcode !='4741')"
                                    +
                                    " and ca.glcode not in (select glcode from CChartOfAccounts where glcode = '471%') " +
                                    " and ca.isActiveForPosting=true and ca.classification=4  and ca.glcode like ?",
                            glCode + "%");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadGLreportCodes.");
        return "glCodes";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadSLreportCodes")
    public String ajaxLoadSLreportCodes() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadSLreportCodes...");
        if (glCode == null)
            glCodesList = new ArrayList<CChartOfAccounts>();
        else
            glCodesList = persistenceService.findAllBy(
                    "select DISTINCT coa from CChartOfAccounts coa,CChartOfAccountDetail cod  where " +
                            " coa = cod.glCodeId and coa.classification=4 and coa.glcode like ?",
                    glCode + "%");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadSLreportCodes.");
        return "glCodes";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadFunctionCodes")
    public String ajaxLoadFunctionCodes() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadFunctionCodes...");
        if (function == null)
            functionCodesList = new ArrayList<CFunction>();
        else {
            String funCodeName = "%" + function.toLowerCase() + "%";
            functionCodesList = persistenceService.findAllBy("select f from CFunction f where" +
                    " isActive = true and isNotLeaf = false and lower(name) like ? or lower(code) like ? ", funCodeName,
                    funCodeName);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadFunctionCodes.");
        return "functionCodes";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadSubLedgerTypesByGlCode")
    public String ajaxLoadSubLedgerTypesByGlCode() {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadSubLedgerTypesByGlCode...");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CommonAction | ajaxLoadSubLedgerTypesByGlCode");
        try {
            if (glCode != null)
                subLedgerTypeList = getPersistenceService()
                        .findAllBy(
                                "select distinct adt from Accountdetailtype adt, CChartOfAccountDetail cad where cad.glCodeId.glcode = ? and cad.detailTypeId = adt ",
                                glCode);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Sub Ledger Type list size =  " + subLedgerTypeList.size());
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting Sub Ledger Type " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting Sub Ledger Type " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadSubLedgerTypesByGlCode.");
        return "subLedgerType";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-showHistory")
    public String showHistory() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("stateId=" + stateId);
        return "workflowHistory";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxGetAllCoaCodes")
    public String ajaxGetAllCoaCodes() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxGetAllCoaCodes...");

        coaList = persistenceService.findAllBy(
                " from CChartOfAccounts where classification=4 and isactiveforposting = true order by glcode ");
        // String
        // query="select glcode||'`-`'||name||'`~`'||ID as \"code\" from chartofaccounts where classification=4 and
        // isactiveforposting = true order by glcode ";*/

        result = new StringBuffer();
        for (CChartOfAccounts cc : coaList) {
            result.append(cc.getGlcode() + "`-`");
            result.append(cc.getName() + "`~`");
            result.append(cc.getId() + "+");
        }
        result.append("^");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxGetAllCoaCodes.");
        return "process";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxGetAllCoaCodesExceptCashBank")
    public String ajaxGetAllCoaCodesExceptCashBank() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxGetAllCoaCodesExceptCashBank...");

        coaList = persistenceService
                .findAllBy(
                        " FROM CChartOfAccounts   WHERE classification = 4 AND "
                                + "isactiveforposting = true AND parentid  not in(select id  from CChartOfAccounts where purposeid in "
                                + "( SELECT id FROM AccountCodePurpose  WHERE UPPER (NAME) = UPPER ('Cash In Hand') OR UPPER (NAME) = UPPER ('Bank Codes')"
                                + " OR UPPER (NAME) = UPPER ('Cheque In Hand')) ) "
                                + " and id  not in(select id  from CChartOfAccounts where purposeid in ( SELECT id FROM AccountCodePurpose WHERE "
                                + "	UPPER (NAME) = UPPER ('Cash In Hand') OR UPPER (NAME) = UPPER ('Bank Codes') OR UPPER (NAME) = UPPER ('Cheque In Hand')) )"
                                + "	and glcode not like '471%' ORDER BY glcode  ");

        result = new StringBuffer();
        for (CChartOfAccounts cc : coaList) {
            result.append(cc.getGlcode() + "`-`");
            result.append(cc.getName() + "`~`");
            result.append(cc.getId() + "+");
        }
        result.append("^");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxGetAllCoaCodesExceptCashBank.");
        return "process";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxGetAllAssetCodes")
    public String ajaxGetAllAssetCodes() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxGetAllAssetCodes...");

        coaList = persistenceService.findAllBy(
                " from CChartOfAccounts where classification=4 and isactiveforposting = true and type = 'A' order by glcode  ");
        // String
        // query="select glcode||'`-`'||name|| '`-`' || ID as \"code\" from chartofaccounts where classification=4 and
        // isactiveforposting = true and type = 'A' order by glcode ";

        result = new StringBuffer();
        for (CChartOfAccounts cc : coaList) {
            result.append(cc.getGlcode() + "`-`");
            result.append(cc.getName() + "`-`");
            result.append(cc.getId() + "+");
        }
        result.append("^");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxGetAllAssetCodes.");
        return "process";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxGetAllLiabCodes")
    public String ajaxGetAllLiabCodes() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxGetAllLiabCodes...");

        coaList = persistenceService.findAllBy(
                " from CChartOfAccounts where classification=4 and isactiveforposting = true and type = 'L' order by glcode  ");
        // String
        // query="select glcode||'`-`'||name|| '`-`' || ID as \"code\" from chartofaccounts where classification=4 and
        // isactiveforposting = true and type = 'L' order by glcode ";

        result = new StringBuffer();
        for (CChartOfAccounts cc : coaList) {
            result.append(cc.getGlcode() + "`-`");
            result.append(cc.getName() + "`-`");
            result.append(cc.getId() + "+");
        }
        result.append("^");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxGetAllLiabCodes.");
        return "process";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxGetAllFunctionName")
    public String ajaxGetAllFunctionName() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxGetAllFunctionName...");

        functionCodesList = persistenceService
                .findAllBy("select f from CFunction f where  isactive = true AND isnotleaf=false order by name");
        // String
        // query="select code||'`-`'||name||'`~`'||id as \"code\" from function where isactive = true AND isnotleaf=false order by
        // name ";

        result = new StringBuffer();
        for (CFunction cf : functionCodesList) {
            result.append(cf.getCode() + "`-`");
            result.append(cf.getName() + "`~`");
            result.append(cf.getId() + "+");
        }
        result.append("^");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxGetAllFunctionName.");
        return "process";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxGetAllBankName")
    public String ajaxGetAllBankName() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxGetAllBankName...");

        bankList = persistenceService.findAllBy("from Bank where  isactive = true order by name");
        // String query="select name||'`-`'||id as \"code\" from bank where isactive = true order by name ";
        result = new StringBuffer();
        for (Bank b : bankList) {
            result.append(b.getName() + "`-`");
            result.append(b.getId() + "+");
        }
        result.append("^");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxGetAllBankName.");
        return "process";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxGetAllCoaNames")
    public String ajaxGetAllCoaNames() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxGetAllCoaNames...");

        coaList = persistenceService.findAllBy(
                " from CChartOfAccounts where classification=4 and isactiveforposting = true order by glcode ");
        // final String
        // query="select name||'`-`'||glcode||'`-`'||ID as \"code\" from chartofaccounts where classification=4 and
        // isactiveforposting = true order by glcode ";

        result = new StringBuffer();
        for (CChartOfAccounts cc : coaList) {
            result.append(cc.getName() + "`-`");
            result.append(cc.getGlcode() + "`-`");
            result.append(cc.getId() + "+");
        }
        result.append("^");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxGetAllCoaNames.");
        return "process";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxCoaDetailCode")
    public String ajaxCoaDetailCode() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxCoaDetailCode...");
        if (glCode == null)
            glCodesList = new ArrayList<CChartOfAccounts>();
        else {
            String codeName = "%" + glCode + "%";
            glCodesList = persistenceService
                    .findAllBy("select glcode from CChartOfAccounts ca where  classification=4 and isactiveforposting = true "
                            + "and glcode like ? order by glcode", codeName);
        }
        // String query="select glcode as \"code\" from chartofaccounts where classification=4 and isactiveforposting = true
        // and glcode like '"+accountCode+"'|| '%' order by glcode ";

        result = new StringBuffer();
        for (CChartOfAccounts cc : coaList) {
            result.append(cc.getGlcode() + "+");
        }
        result.append("^");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxCoaDetailCode.");
        return "process";
    }

    @Action(value = "/voucher/common-ajaxLoadEstimateBudgetDetailsByFundId")
    public String ajaxLoadEstimateBudgetDetailsByFundId() {
        if (fundId != null && fundId != 0)
            budgetDetailList = budgetDetailService.getDepartmentFromBudgetDetailByFundId(fundId);
        return "estimateBudgetDetails";
    }

    @Action(value = "/voucher/common-ajaxLoadEstimateBudgetDetailsByDepartmentId")
    public String ajaxLoadEstimateBudgetDetailsByDepartmentId() {
        if (departmentId != null && departmentId != 0)
            budgetDetailList = budgetDetailService.getFunctionFromBudgetDetailByDepartmentId(departmentId);
        return "estimateBudgetDetails";
    }

    @Action(value = "/voucher/common-ajaxLoadEstimateBudgetDetailsByFuncId")
    public String ajaxLoadEstimateBudgetDetailsByFuncId() {
        if (functionId != null && functionId != 0)
            budgetDetailList = budgetDetailService.getBudgetDetailByFunctionId(functionId);
        return "estimateBudgetDetails";
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(final String stateId) {
        this.stateId = stateId;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(final String scriptName) {
        this.scriptName = scriptName;
    }

    public List<Map<String, Object>> getDesignationList() {
        return designationList;
    }

    public void setDesignationList(final List<Map<String, Object>> designationList) {
        this.designationList = designationList;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public void setStartsWith(final String startsWith) {
        this.startsWith = startsWith;
    }

    public Long getRecoveryId() {
        return recoveryId;
    }

    public void setRecoveryId(final Long recoveryId) {
        this.recoveryId = recoveryId;
    }

    public void setSubSchemeId(final Integer subSchemeId) {
        this.subSchemeId = subSchemeId;
    }

    public Integer getSubSchemeId() {
        return subSchemeId;
    }

    public List<Fundsource> getFundSouceList() {
        return fundSouceList;
    }

    public void setFundSouceList(final List<Fundsource> fundSouceList) {
        this.fundSouceList = fundSouceList;
    }

    public List<CChartOfAccounts> getAccountCodesList() {
        return accountCodesList;
    }

    public void setAccountCodesList(final List<CChartOfAccounts> accountCodesList) {
        this.accountCodesList = accountCodesList;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(final Integer department) {
        this.department = department;
    }

    public void setFinancingSourceService(
            final FinancingSourceService financingSourceService) {
        this.financingSourceService = financingSourceService;
    }

    public void setDefaultDepartment(final String defaultDepartment) {
        this.defaultDepartment = defaultDepartment;
    }

    public String getDefaultDepartment() {
        return defaultDepartment;
    }

    public Long getBillVhId() {
        return billVhId;
    }

    public void setBillVhId(final Long billVhId) {
        this.billVhId = billVhId;
    }

    public List<LoanGrantBean> getProjectCodeList() {
        return projectCodeList;
    }

    public void setProjectCodeList(final List<LoanGrantBean> projectCodeList) {
        this.projectCodeList = projectCodeList;
    }

    public List<String> getProjectCodeStringList() {
        return projectCodeStringList;
    }

    public void setProjectCodeStringList(final List<String> projectCodeStringList) {
        this.projectCodeStringList = projectCodeStringList;
    }

    public List<InstrumentHeader> getInstrumentHeaderList() {
        return instrumentHeaderList;
    }

    public void setInstrumentHeaderList(final List<InstrumentHeader> instrumentHeaderList) {
        this.instrumentHeaderList = instrumentHeaderList;
    }

    public String getRtgsNumber() {
        return rtgsNumber;
    }

    public void setRtgsNumber(final String rtgsNumber) {
        this.rtgsNumber = rtgsNumber;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBanksWithPayGenAndRTGSNotAssigned")
    public String ajaxLoadBanksWithPayGenAndRTGSNotAssigned() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBanksWithPayGenAndRTGSNotAssigned...");
        List<Object[]> bankBranch;
        final StringBuffer bankQuery = new StringBuffer();
        try {
            bankQuery
                    .append("SELECT DISTINCT CONCAT(CONCAT(bank.id,'-'),bankBranch.id) AS bankbranchid,CONCAT(CONCAT(bank.name,' '),"
                            +
                            " bankBranch.branchname) AS bankbranchname	FROM voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,"
                            +
                            " paymentheader ph,egf_instrumentvoucher iv right outer join voucherheader vh1 ON  vh1.id =iv.VOUCHERHEADERID"
                            +
                            " WHERE ph.voucherheaderid=vh.id AND vh.status=0  AND bank.isactive=true  AND bankBranch.isactive=true AND bank.id = bankBranch.bankid"
                            +
                            " AND bankBranch.id = bankaccount.branchid  AND  bankaccount.TYPE IN ('RECEIPTS_PAYMENTS','PAYMENTS')"
                            +
                            " AND  vh1.id=vh.id AND iv.VOUCHERHEADERID IS NULL	 AND ph.type = '"
                            + FinancialConstants.MODEOFPAYMENT_RTGS
                            + "' "
                            +
                            " AND vh.name = '"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                            + "' "
                            +
                            " UNION"
                            +
                            " SELECT DISTINCT CONCAT(CONCAT(bank.id,'-'),bankBranch.id) AS bankbranchid,CONCAT(CONCAT(bank.name,' '),"
                            +
                            " bankBranch.branchname) AS bankbranchname"
                            +
                            " FROM egf_instrumentvoucher iv,voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,"
                            +
                            " vouchermis vmis,"
                            +
                            " paymentheader ph,egw_status egws,(SELECT ih1.id,ih1.id_status FROM egf_instrumentheader ih1,"
                            +
                            " (SELECT bankid,bankaccountid,instrumentnumber,MAX(lastmodifieddate) AS lastmodifieddate FROM egf_instrumentheader GROUP BY bankid,bankaccountid,"
                            +
                            " instrumentnumber) max_rec WHERE max_rec.bankid=ih1.bankid AND max_rec.bankaccountid=ih1.bankaccountid AND max_rec.instrumentnumber=ih1.instrumentnumber"
                            +
                            " AND max_rec.lastmodifieddate=ih1.lastmodifieddate) ih WHERE ph.voucherheaderid=vh.id AND vh.id= vmis.voucherheaderid"
                            +
                            " AND vh.status=0  AND ph.voucherheaderid=vh.id 	AND bank.isactive=true "
                            +
                            " AND bankBranch.isactive=true AND bank.id = bankBranch.bankid AND bankBranch.id = bankaccount.branchid "
                            +
                            " AND bankaccount.TYPE IN ('RECEIPTS_PAYMENTS','PAYMENTS') AND  iv.voucherheaderid=vh.id AND iv.instrumentheaderid=ih.id "
                            +
                            " AND ih.id_status=egws.id AND egws.description IN  ('Surrendered','Surrender_For_Reassign')" +
                            " AND  ph.type = '" + FinancialConstants.MODEOFPAYMENT_RTGS + "' " +
                            " AND vh.name = '" + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE + "' " +
                            "  order by 2 ");

            bankBranch = persistenceService.getSession().createSQLQuery(bankQuery.toString()).list();

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank list size is " + bankBranch.size());
            bankBranchList = new ArrayList<Map<String, Object>>();
            Map<String, Object> bankBrmap;
            for (final Object[] element : bankBranch) {
                bankBrmap = new HashMap<String, Object>();
                bankBrmap.put("bankBranchId", element[0].toString());
                bankBrmap.put("bankBranchName", element[1].toString());
                bankBranchList.add(bankBrmap);
            }

        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));

        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBanksWithPayGenAndRTGSNotAssigned.");
        return "bank";

    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxLoadBankAccountsWithPayGenAndRTGSNotAssigned")
    public String ajaxLoadBankAccountsWithPayGenAndRTGSNotAssigned() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting ajaxLoadBankAccountsWithPayGenAndRTGSNotAssigned...");
        try {
            accNumList = new ArrayList<Bankaccount>();
            String bankaccountFundQuery = "";
            String voucherheaderFundQuery = "";
            if (fundId != null && fundId != 0 && fundId != -1) {
                bankaccountFundQuery = " and bankaccount.fundid=" + fundId.longValue();
                voucherheaderFundQuery = "  AND VH.FUNDID=" + fundId;
            }
            StringBuffer queryString = new StringBuffer();
            // query to fetch vouchers for which RTGS not assigned
            queryString = queryString
                    .append("SELECT  bankaccount.accountnumber AS accountnumber,  bankaccount.accounttype AS accounttype,"
                            +
                            " CAST(bankaccount.id AS INTEGER) AS id, coa.glcode AS glCode  FROM chartofaccounts coa, bankaccount bankaccount"
                            +
                            " WHERE bankaccount.ID IN (SELECT DISTINCT PH.bankaccountnumberid  "
                            +
                            " FROM   paymentheader ph,  voucherheader vh left OUTER JOIN egf_instrumentvoucher iv ON vh.id =iv.VOUCHERHEADERID"
                            +
                            " WHERE ph.voucherheaderid  =vh.id AND vh.status=0 "
                            + voucherheaderFundQuery
                            + " AND ph.voucherheaderid    =vh.id"
                            +
                            " AND iv.VOUCHERHEADERID   IS NULL "
                            +
                            " AND vh.name = '"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                            + "' "
                            +
                            " AND ph.type = '"
                            + FinancialConstants.MODEOFPAYMENT_RTGS
                            + "' "
                            +
                            " AND coa.id                =bankaccount.glcodeid AND bankaccount.type     IN ('RECEIPTS_PAYMENTS','PAYMENTS'))"
                            +
                            bankaccountFundQuery +
                            " AND bankaccount.branchid  =" + branchId + " and bankaccount.isactive=true ");
            // query to fetch vouchers for which cheque has been assigned and surrendered
            queryString
                    .append(" union select bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode "
                            +
                            " from chartofaccounts coa, "
                            +
                            " Bankaccount bankaccount"
                            +
                            " where bankaccount.id in(SELECT DISTINCT PH.bankaccountnumberid  from  "
                            +
                            " egf_instrumentvoucher iv,voucherheader vh,"
                            +
                            " paymentheader ph,egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, "
                            +
                            " (select bankid,bankaccountid,instrumentnumber,max(id) as id from egf_instrumentheader group by bankid,bankaccountid,"
                            +
                            " instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber "
                            +
                            " and max_rec.id=ih1.id) ih where ph.voucherheaderid=vh.id   "
                            +
                            voucherheaderFundQuery
                            +
                            " and vh.status=0 and  ph.voucherheaderid=vh.id and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id "
                            +
                            " and ph.bankaccountnumberid=bankaccount.id  and vh.type='"
                            + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                            + "'"
                            +
                            " AND vh.name = '"
                            + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                            + "' "
                            +
                            " AND ph.type = '"
                            + FinancialConstants.MODEOFPAYMENT_RTGS
                            + "' "
                            +
                            " and ih.id_status=egws.id and egws.description in ('Surrendered','Surrender_For_Reassign') )"
                            +
                            " and coa.id=bankaccount.glcodeid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS')  and bankaccount.branchid="
                            + branchId);

            queryString = queryString.append(bankaccountFundQuery);

            final List<Object[]> bankAccounts = persistenceService.getSession().createSQLQuery(queryString.toString()).list();

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bank accont list size is " + bankAccounts.size() + "and Query is " + queryString.toString());
            final List<String> addedBanks = new ArrayList<String>();
            for (final Object[] account : bankAccounts) {
                final String accountNumberAndType = account[0].toString() + "-" + account[1].toString();
                if (!addedBanks.contains(accountNumberAndType)) {
                    final Bankaccount bankaccount = new Bankaccount();
                    bankaccount.setAccountnumber(account[0].toString());
                    bankaccount.setAccounttype(account[1].toString());
                    final CChartOfAccounts chartofaccounts = new CChartOfAccounts();
                    chartofaccounts.setGlcode(account[3].toString());
                    bankaccount.setChartofaccounts(chartofaccounts);
                    bankaccount.setId(Long.valueOf(account[2].toString()));
                    addedBanks.add(accountNumberAndType);
                    accNumList.add(bankaccount);
                }
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data for bank dropdown " + e.getMessage(),
                    new Exception(e.getMessage()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed ajaxLoadBankAccountsWithPayGenAndRTGSNotAssigned.");
        return "bankAccNum";
    }

    /*
     * Autocomplete for ARF Nos where Advance Payment not created and ARF type='Contractor'
     */
    @Action(value = "/voucher/common-searchARFNumbers")
    public String searchARFNumbers() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select distinct(arf.advanceRequisitionNumber) from EgAdvanceRequisition arf where arf.status.code = ? "
                    +
                    " and arf.arftype = ? and NOT EXISTS (select 1 from CVoucherHeader vh where vh.id=arf.egAdvanceReqMises.voucherheader.id and arf.egAdvanceReqMises.voucherheader.status<>4) "
                    +
                    " and UPPER(arf.advanceRequisitionNumber) like '%'||?||'%' order by arf.advanceRequisitionNumber";
            params.add(ARF_STATUS_APPROVED);
            params.add(ARF_TYPE);
            params.add(query.toUpperCase());

            arfNumberSearchList = persistenceService.findAllBy(strquery, params.toArray());
        }
        return ARF_NUMBER_SEARCH_RESULTS;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/common-ajaxYearCode")
    public String ajaxYearCode() {
        try {
            if (bankaccount != null && departmentId != null) {
                yearCodeList = persistenceService
                        .findAllBy(
                                "select  DISTINCT fs from  AccountCheques ac,CFinancialYear fs,ChequeDeptMapping cd  where ac.serialNo = fs.id and  bankAccountId=?"
                                        + " and ac.id=cd.accountCheque and cd.allotedTo.id=?"
                                        + " order by fs.id desc ",
                                bankaccount, departmentId);
            }
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting year code " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting year code " + e.getMessage(),
                    new HibernateException(e.getMessage()));
        }
        return "yearCode";
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public List<String> getArfNumberSearchList() {
        return arfNumberSearchList;
    }

    public String getBillSubType() {
        return billSubType;
    }

    public void setBillSubType(final String billSubType) {
        this.billSubType = billSubType;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(final String glCode) {
        this.glCode = glCode;
    }

    public List<CChartOfAccounts> getGlCodesList() {
        return glCodesList;
    }

    public void setGlCodesList(final List<CChartOfAccounts> glCodesList) {
        this.glCodesList = glCodesList;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(final String function) {
        this.function = function;
    }

    public List<CFunction> getFunctionCodesList() {
        return functionCodesList;
    }

    public void setFunctionCodesList(final List<CFunction> functionCodesList) {
        this.functionCodesList = functionCodesList;
    }

    public List<Accountdetailtype> getSubLedgerTypeList() {
        return subLedgerTypeList;
    }

    public void setSubLedgerTypeList(final List<Accountdetailtype> subLedgerTypeList) {
        this.subLedgerTypeList = subLedgerTypeList;
    }

    public AppConfigValueService getAppConfigValuesService() {
        return appConfigValuesService;
    }

    public void setAppConfigValuesService(final AppConfigValueService appConfigValuesService) {
        this.appConfigValuesService = appConfigValuesService;
    }

    public List<CChartOfAccounts> getCoaList() {
        return coaList;
    }

    public void setCoaList(List<CChartOfAccounts> coaList) {
        this.coaList = coaList;
    }

    public StringBuffer getResult() {
        return result;
    }

    public void setResult(StringBuffer result) {
        this.result = result;
    }

    public Long getVouchHeaderId() {
        return vouchHeaderId;
    }

    public void setVouchHeaderId(Long vouchHeaderId) {
        this.vouchHeaderId = vouchHeaderId;
    }

    public String getGlcodeParam() {
        return glcodeParam;
    }

    public void setGlcodeParam(String glcodeParam) {
        this.glcodeParam = glcodeParam;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Integer getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(Integer bankaccount) {
        this.bankaccount = bankaccount;
    }

    public List<CFinancialYear> getYearCodeList() {
        return yearCodeList;
    }

    public void setYearCodeList(List<CFinancialYear> yearCodeList) {
        this.yearCodeList = yearCodeList;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public List<BudgetDetail> getBudgetDetailList() {
        return budgetDetailList;
    }

    public void setBudgetDetailList(List<BudgetDetail> budgetDetailList) {
        this.budgetDetailList = budgetDetailList;
    }

}