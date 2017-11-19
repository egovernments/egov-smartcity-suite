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
package org.egov.egf.web.actions.masters;


import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.CoaCache;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.EgfAccountcodePurpose;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.masters.AccountCodePurpose;
import org.egov.services.voucher.GeneralLedgerService;
import org.egov.utils.Constants;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results({
    @Result(name = "detailed", location = "chartOfAccounts-detailed.jsp"),
    @Result(name = Constants.EDIT, location = "chartOfAccounts-edit.jsp"),
    @Result(name = Constants.VIEW, location = "chartOfAccounts-view.jsp"),
    @Result(name = Constants.VIEW_COA, location = "chartOfAccounts-viewCoa.jsp"),
    @Result(name = Constants.VIEW_MODIFY_COA, location = "chartOfAccounts-viewModifyCoa.jsp"),
    @Result(name = "new", location = "chartOfAccounts-new.jsp"),
    @Result(name = "detailed-editCode", location = "chartOfAccounts-editCode.jsp"),
    @Result(name = "detailed-viewCode", location = "chartOfAccounts-viewCode.jsp"),
    @Result(name = "generated-glcode", location = "chartOfAccounts-generated-glcode.jsp") })
public class ChartOfAccountsAction extends BaseFormAction {
    private static final long serialVersionUID = 3393565721493478018L;
    private static final long LONG_FOUR = 4l;
    private static final long LONG_TWO = 2l;
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    @Qualifier("chartOfAccountsService")
    private PersistenceService<CChartOfAccounts, Long> chartOfAccountsService;
    @Autowired
    @Qualifier("chartOfAccountDetailService")
    private PersistenceService<CChartOfAccountDetail, Long> chartOfAccountDetailService;
    CChartOfAccounts model = new CChartOfAccounts();
    List<String> accountDetailTypeList = new ArrayList<String>();
    List<Accountdetailtype> accountDetailType = new ArrayList<Accountdetailtype>();
    private static final Logger LOGGER = Logger.getLogger(ChartOfAccountsAction.class);
 
    boolean activeForPosting = false;
    boolean functionRequired = false;
    boolean budgetCheckRequired = false;
    Long coaId;
    Long parentId;
    @Autowired
    AppConfigValueService appConfigValuesService;
    @Autowired
    private ChartOfAccounts chartOfAccounts;
    String glCode = "";
    List<CChartOfAccounts> allChartOfAccounts;
    int majorCodeLength = 0;
    int minorCodeLength = 0;
    int subMinorCodeLength = 0;
    int detailedCodeLength = 0;
    EgfAccountcodePurpose accountcodePurpose;
    private String generatedGlcode;
    String newGlcode;
    String parentForDetailedCode = "";
    private final Map<Long, Integer> glCodeLengths = new HashMap<Long, Integer>();
    private boolean updateOnly = false;
    @Autowired
    private CoaCache coaCache;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    @Autowired
    private GeneralLedgerService generalLedgerService;

    @Override
    public Object getModel() {
        return model;
    }

    public ChartOfAccountsAction() {

        addRelatedEntity("purpose", AccountCodePurpose.class);
        addRelatedEntity("chartOfAccountDetails.detailTypeId",
                AccountCodePurpose.class);

    }

    @Override
    public void prepare() {
        super.prepare();
        populateChartOfAccounts();
        populateCodeLength();
        parentForDetailedCode = getAppConfigValueFor("EGF", "parent_for_detailcode");
        populateGlCodeLengths();
        allChartOfAccounts = chartOfAccountsService.findAllBy("from CChartOfAccounts where classification=?",
                Long.valueOf(parentForDetailedCode));
        if (model != null)
            if (accountcodePurpose != null && accountcodePurpose.getId() != null)
                accountcodePurpose = getPurposeCode(Integer.valueOf(accountcodePurpose.getId()));
        dropdownData.put("purposeList", persistenceService.findAllBy("from EgfAccountcodePurpose order by name"));
        dropdownData.put("accountDetailTypeList", persistenceService.findAllBy("from Accountdetailtype order by name"));
    }

    private void populateAccountDetailTypeList() {
        if (model.getChartOfAccountDetails() != null)
            for (final CChartOfAccountDetail entry : model.getChartOfAccountDetails())
                accountDetailTypeList.add(entry.getDetailTypeId().getId().toString());
    }

    void populateGlCodeLengths() {
        getGlCodeLengths().put(Constants.LONG_ONE, majorCodeLength);
        getGlCodeLengths().put(LONG_TWO, minorCodeLength - majorCodeLength);
        getGlCodeLengths().put(3l, subMinorCodeLength - minorCodeLength);
        if (parentForDetailedCode.equals("2"))
            getGlCodeLengths().put(LONG_FOUR, detailedCodeLength - minorCodeLength);
        else if (parentForDetailedCode.equals("3"))
            getGlCodeLengths().put(LONG_FOUR, detailedCodeLength - subMinorCodeLength);
    }

    private EgfAccountcodePurpose getPurposeCode(final Integer id) {
        return (EgfAccountcodePurpose) persistenceService.find("from EgfAccountcodePurpose where id=?", id);
    }

    private void populateChartOfAccounts() {
        if (model.getId() != null)
            model = chartOfAccountsService.findById(model.getId(), false);
    }

    @Override
    public String execute() throws Exception {
        return NEW;
    }

    @Action(value = "/masters/chartOfAccounts-view")
    public String view() throws Exception {
        populateAccountCodePurpose();
        populateAccountDetailTypeList();
        populateCoaRequiredFields();
        coaId = model.getId();
        return Constants.VIEW;
    }

    @Action(value = "/masters/chartOfAccounts-viewChartOfAccounts")
    public String viewChartOfAccounts() throws Exception {
        populateAccountCodePurpose();
        populateAccountDetailTypeList();
        populateCoaRequiredFields();
        coaId = model.getId();
        return Constants.VIEW_COA;
    }
    @Action(value = "/masters/chartOfAccounts-modifyChartOfAccounts")
    public String modifyChartOfAccounts() throws Exception {
        populateAccountCodePurpose();
        populateAccountDetailTypeList();
        populateCoaRequiredFields();
        coaId = model.getId();
        return Constants.VIEW_MODIFY_COA;
    }
    public boolean shouldAllowCreation() {
        return !Long.valueOf("4").equals(model.getClassification());
    }

    @Action(value = "/masters/chartOfAccounts-modify")
    public String modify() throws Exception {
        populateAccountDetailTypeList();
        populateCoaRequiredFields();
        populateAccountCodePurpose();
        return Constants.EDIT;
    }

    private void populateCoaRequiredFields() {
        activeForPosting = getIsActiveForPosting();
        functionRequired = getFunctionReqd();
        budgetCheckRequired = budgetCheckReq();
    }

    private void populateAccountCodePurpose() {
        if (model != null && model.getPurposeId() != null)
            accountcodePurpose = getPurposeCode(model.getPurposeId().intValue());
    }

    @Action(value = "/masters/chartOfAccounts-update")
    public String update() throws Exception {
        setPurposeOnCoa();
        updateOnly = true;
        populateAccountDetailType();
        model.setIsActiveForPosting(activeForPosting);
        model.setFunctionReqd(functionRequired);
        model.setBudgetCheckReq(budgetCheckRequired);
        chartOfAccountsService.persist(model);
        saveCoaDetails(model);
        addActionMessage(getText("chartOfAccount.modified.successfully"));
        clearCache();
        coaId = model.getId();
        return Constants.VIEW_MODIFY_COA;
    }

    private void setPurposeOnCoa() {
        if (accountcodePurpose != null && accountcodePurpose.getId() != null)
            model.setPurposeId(accountcodePurpose.getId().longValue());
        if (model.getPurposeId() != null && model.getPurposeId().compareTo(0l) == 0)
            model.setPurposeId(null);
    }

    private void populateAccountDetailType() {
        //persistenceService.setType(Accountdetailtype.class);
        for (final String row : accountDetailTypeList)
            accountDetailType.add((Accountdetailtype) persistenceService.find("from Accountdetailtype where id=?",
                    Integer.valueOf(row)));
    }

    void deleteAccountDetailType(final List<Accountdetailtype> accountDetailType, final CChartOfAccounts accounts) {
        String accountDetail = "";
        if (accounts.getChartOfAccountDetails() == null)
            return;
        chartOfAccountsService.getSession().flush();
        //persistenceService.setType(CChartOfAccountDetail.class);
        try {
            for (final Accountdetailtype row : accountDetailType) {
                final Iterator<CChartOfAccountDetail> iterator = accounts.getChartOfAccountDetails().iterator();
                while (iterator.hasNext()) {
                    final CChartOfAccountDetail next = iterator.next();
                    accountDetail = row.getName();
                    if (next == null || next.getDetailTypeId().getId().equals(row.getId())) {
                        iterator.remove();
                        chartOfAccountDetailService.delete(chartOfAccountDetailService.findById(next.getId(), false));
                        persistenceService.getSession().flush();
                    }
                }
            }
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            populateAccountDetailTypeList();
            final String message = accountDetail.concat(" ").concat(e.toString());
            throw new ValidationException(Arrays.asList(new ValidationError(message, message)));
        }
    }

    boolean hasReference(final Integer id, final String glCode) {
        final SQLQuery query = persistenceService.getSession().createSQLQuery(
                "select * from chartofaccounts c,generalledger gl,generalledgerdetail gd " +
                        "where c.glcode='" + glCode + "' and gl.glcodeid=c.id and gd.generalledgerid=gl.id and gd.DETAILTYPEID="
                        + id);
        final List list = query.list();
        if (list != null && list.size() > 0)
            return true;
        return false;
    }

    boolean validAddtition(final String glCode) {
        boolean flag=true;
        final StringBuffer strQuery = new StringBuffer();
        strQuery.append("select bd.billid from  eg_billdetails bd, chartofaccounts coa,  eg_billregistermis brm where coa.glcode = '"
                + glCode + "' and bd.glcodeid = coa.id and brm.billid = bd.billid and brm.voucherheaderid is null ");
        strQuery.append(" intersect SELECT br.id FROM eg_billregister br, eg_billdetails bd, chartofaccounts coa,egw_status  sts WHERE coa.glcode = '"
                + glCode + "' AND bd.glcodeid = coa.id AND br.id= bd.billid AND br.statusid=sts.id ");
        strQuery.append(" and sts.id not in (select id from egw_status where upper(moduletype) like '%BILL%' and upper(description) like '%CANCELLED%') ");
        final SQLQuery query = persistenceService.getSession().createSQLQuery(strQuery.toString());
        final List list = query.list();
        if (!list.isEmpty())
            flag = false;
        if (flag) {
            final List<CGeneralLedger> generalLedgerList = generalLedgerService.getGeneralLedgerByGlCode(glCode);
            if (!generalLedgerList.isEmpty())
                flag = false;
        }
        return flag;
    }

    boolean validAddtition(final String glCode, Integer accountDetailTypeId) {
        final List<CGeneralLedger> generalLedgerList = generalLedgerService.getGeneralLedgerByGlCode(glCode);
        if (!generalLedgerList.isEmpty())
            for (CGeneralLedger cg : generalLedgerList) {
                if (!cg.getGeneralLedgerDetails().isEmpty())
                    for (CGeneralLedgerDetail cgd : cg.getGeneralLedgerDetails()) {
                        if (accountDetailTypeId.equals(cgd.getDetailTypeId().getId()))
                            return false;
                    }
            }
        return true;
    }

    void saveCoaDetails(final CChartOfAccounts accounts) {
        final List<Accountdetailtype> rowsToBeDeleted = getAccountDetailTypeToBeDeleted(accountDetailType, accounts);
        final List<Accountdetailtype> rowsToBeAdded = getAccountDetailTypeToBeAdded(accountDetailType, accounts);
        String accountDetailTypeName = "";
        if (accounts.getChartOfAccountDetails().isEmpty() && !accountDetailType.isEmpty() && updateOnly) {
            if (!validAddtition(model.getGlcode())) {
                final String message = getText("chartOfAccount.accDetail.uncancelled.bills");
                throw new ValidationException(Arrays.asList(new ValidationError(message, message)));
            }
        }

        else if (accounts.getChartOfAccountDetails().size() == rowsToBeDeleted.size() && rowsToBeAdded.isEmpty())
            deleteAccountDetailType(rowsToBeDeleted, accounts);
        for (final Accountdetailtype entry : rowsToBeAdded) {
            if (!coaHasAccountdetailtype(entry, accounts)) {
                final CChartOfAccountDetail chartOfAccountDetail = new CChartOfAccountDetail();
                chartOfAccountDetail.setDetailTypeId(entry);
                chartOfAccountDetail.setGlCodeId(accounts);
                accounts.getChartOfAccountDetails().add(chartOfAccountDetail);
                chartOfAccountsService.persist(accounts);
            }
        }
        if (!accounts.getChartOfAccountDetails().isEmpty() && !rowsToBeDeleted.isEmpty()) {
            List<Accountdetailtype> accountDetailTypeToBeDeleted = new ArrayList<>();
            for (Accountdetailtype accountDtltyp : rowsToBeDeleted) {
                for (CChartOfAccountDetail coaDetail : accounts.getChartOfAccountDetails()) {
                    if (coaDetail.getDetailTypeId().getId() == accountDtltyp.getId()) {
                        if (!validAddtition(model.getGlcode(), accountDtltyp.getId()))
                            accountDetailTypeName = accountDetailTypeName.concat(accountDtltyp.getName() + ",");
                        else {
                            accountDetailTypeToBeDeleted.add(accountDtltyp);
                        }
                    }
                }
            }
            if (!accountDetailTypeToBeDeleted.isEmpty())
                deleteAccountDetailType(accountDetailTypeToBeDeleted, accounts);
            if (!accountDetailTypeName.isEmpty()) {

                final String message = getText("chartOfAccount.accDetail.bills",
                        new String[] { accountDetailTypeName, accountDetailTypeName });
                throw new ValidationException(Arrays.asList(new ValidationError(message, message)));
            }
        }

        chartOfAccountsService.getSession().flush();
    }

    List<Accountdetailtype> getAccountDetailTypeToBeDeleted(final List<Accountdetailtype> accountDetailType,
            final CChartOfAccounts accounts) {
        final List<Accountdetailtype> rowsToBeDeleted = new ArrayList<Accountdetailtype>();
        for (final CChartOfAccountDetail entry : accounts.getChartOfAccountDetails())
            if (accountDetailType != null && accountDetailType.isEmpty())
                rowsToBeDeleted.add(entry.getDetailTypeId());
            else if (!accountDetailTypeContains(accountDetailType, entry.getDetailTypeId()))
                rowsToBeDeleted.add(entry.getDetailTypeId());
        return rowsToBeDeleted;
    }

    List<Accountdetailtype> getAccountDetailTypeToBeAdded(final List<Accountdetailtype> accountDetailType,
            final CChartOfAccounts accounts) {
        final List<Accountdetailtype> rowsToBeAdded = new ArrayList<Accountdetailtype>();
        for (final Accountdetailtype row : accountDetailType)
            if (!coaHasAccountdetailtype(row, accounts))
                rowsToBeAdded.add(row);
        return rowsToBeAdded;
    }

    private boolean coaHasAccountdetailtype(final Accountdetailtype entry, final CChartOfAccounts accounts) {
        for (final CChartOfAccountDetail row : accounts.getChartOfAccountDetails())
            if (row.getDetailTypeId().getId().equals(entry.getId()))
                return true;
        return false;
    }

    private boolean accountDetailTypeContains(final List<Accountdetailtype> list, final Accountdetailtype entry) {
        for (final Accountdetailtype row : list)
            if (row.getId().equals(entry.getId()))
                return true;
        return false;
    }

    @Action(value = "/masters/chartOfAccounts-addNewCoa")
    public String addNewCoa() throws Exception {
        model = new CChartOfAccounts();
        if (parentId != null)
            model.setParentId(parentId);
        final CChartOfAccounts parent = chartOfAccountsService.findById(parentId, false);
        model.setType(parent.getType());
        setClassification(parent);
        final Long glCode = findNextGlCode(parent);
        if (parent.getClassification() == null || parent.getClassification() == 0)
            generatedGlcode = "";
        else
            generatedGlcode = parent.getGlcode();
        if (glCode == null) {
            Long classification=0l;
            if(glCodeLengths.get(parent.getClassification()+1)==0)
            {
                classification=parent.getClassification()+2;
            }else
            {
                classification=parent.getClassification()+1; 
            }
            populateGlcode(classification);
            newGlcode = model.getGlcode();
        } else {
            newGlcode = String.valueOf(glCode + 1);
            if (model.getClassification().equals(LONG_TWO))
                newGlcode = newGlcode.substring(majorCodeLength, newGlcode.length());
            else if (model.getClassification().equals(3l))
                newGlcode = newGlcode.substring(minorCodeLength, newGlcode.length());
            else if (model.getClassification().equals(LONG_FOUR))
                extractDetailCode();
        }
        return NEW;
    }

    private Long findNextGlCode(final CChartOfAccounts parentCoa) {
        final String glcode = (String) persistenceService.find("select max(glcode) from CChartOfAccounts where parentId=?",
                parentCoa.getId());
        return glcode != null ? Long.valueOf(glcode) : null;
    }

    void setClassification(final CChartOfAccounts parentCoa) {
        if (parentCoa.getClassification() == null)
            model.setClassification(Constants.LONG_ONE);
        else if (Constants.LONG_ZERO.equals(parentCoa.getClassification()))
            model.setClassification(Constants.LONG_ONE);
        else if (Constants.LONG_ONE.equals(parentCoa.getClassification()))
            model.setClassification(LONG_TWO);
        else if (parentCoa.getClassification().equals(LONG_TWO)) {
            if (parentForDetailedCode.equals("2"))
                model.setClassification(LONG_FOUR);
            else
                model.setClassification(3l);
        }
        else if (parentCoa.getClassification().equals(3l))
            model.setClassification(LONG_FOUR);
    }

    String getAppConfigValueFor(final String module, final String key) {
        return appConfigValuesService.getConfigValuesByModuleAndKey(module, key).get(0).getValue();
    }

    void populateCodeLength() {
        majorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF, "coa_majorcode_length"));
        minorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF, "coa_minorcode_length"));
        subMinorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF, "coa_subminorcode_length"));
        detailedCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF, "coa_detailcode_length"));
    }

    void populateGlcode(final Long classification) {
        
        model.setGlcode(StringUtils.leftPad("", glCodeLengths.get(classification), '0'));
    }

    @Action(value = "/masters/chartOfAccounts-save")
    public String save() throws Exception {
        if (generatedGlcode == null || newGlcode == null) {
            addActionMessage(getText("chartOfAccount.invalid.glcode"));
            return NEW;
        }
        final CChartOfAccounts coa = chartOfAccountsService.find("from CChartOfAccounts where glcode=?",
                generatedGlcode.concat(newGlcode));
        if (coa != null) {
            addActionMessage(getText("chartOfAccount.glcode.already.exists"));
            return NEW;
        }
        model.setGlcode(generatedGlcode.concat(newGlcode));
        if ("0".equals(model.getPurposeId()))
            model.setPurposeId(null);
        if (parentId != null) {
            final CChartOfAccounts parent = chartOfAccountsService.findById(parentId, false);
            model.setParentId(parentId);
            model.setType(parent.getType());
        }
        setPurposeOnCoa();
        model.setIsActiveForPosting(activeForPosting);
        model.setBudgetCheckReq(budgetCheckRequired);
        model.setFunctionReqd(functionRequired);
        populateAccountDetailType();
        model.setMajorCode(model.getGlcode().substring(0, majorCodeLength));
        model.setCreatedDate(DateUtils.now());
        chartOfAccountsService.persist(model);
        saveCoaDetails(model);
        addActionMessage(getText("chartOfAccount.saved.successfully"));
        clearCache();
       // reset();
        coaId = model.getId();
        return Constants.VIEW;
       
    }

    private void reset() {
        activeForPosting = false;
        budgetCheckRequired = false;
        functionRequired = false;
        generatedGlcode = "";
        newGlcode = "";
        model = new CChartOfAccounts();
    }

    public boolean isActiveForPosting() {
        return activeForPosting;
    }

    public boolean budgetCheckReq() {
        if (model != null && model.getBudgetCheckReq() != null && model.getBudgetCheckReq())
            return true;
        return false;
    }

    public boolean getFunctionReqd() {
        if (model != null && model.getFunctionReqd() != null && model.getFunctionReqd())
            return true;
        return false;
    }

    public boolean getIsActiveForPosting() {
        if (model != null && model.getIsActiveForPosting() != null && model.getIsActiveForPosting())
            return true;
        return false;
    }


    @Action(value = "/masters/chartOfAccounts-editDetailedCode")
    public String editDetailedCode() throws Exception {
        allChartOfAccounts = chartOfAccountsHibernateDAO.getDetailedCodesList();
        return "detailed-editCode";
    }

    @Action(value = "/masters/chartOfAccounts-viewDetailedCode")
    public String viewDetailedCode() throws Exception {
        allChartOfAccounts = chartOfAccountsHibernateDAO.getDetailedCodesList();
        return "detailed-viewCode";
    }

    @SkipValidation
    @Action(value = "/masters/chartOfAccounts-modifySearch")
    public String modifySearch() throws Exception {
        if (glCode != null) {
            model = chartOfAccountsService.find("from CChartOfAccounts where classification=4 and glcode=?",
                    glCode.split("-")[0]);
            if (model == null) {
                addActionMessage(getText("charOfAccount.no.record"));
                return editDetailedCode();
            }
            populateAccountDetailTypeList();
            populateCoaRequiredFields();
            populateAccountCodePurpose();
            return Constants.EDIT;
        } else {
            addActionMessage(getText("charOfAccount.no.record"));
            return editDetailedCode();
        }
    }

    @SkipValidation
    @Action(value = "/masters/chartOfAccounts-viewSearch")
    public String viewSearch() throws Exception {
        if (glCode != null) {
            model = chartOfAccountsService.find("from CChartOfAccounts where classification=4 and glcode=?",
                    glCode.split("-")[0]);
            if (model == null) {
                addActionMessage(getText("charOfAccount.no.record"));
                return viewDetailedCode();
            }
            coaId = model.getId();
            populateAccountDetailTypeList();
            populateCoaRequiredFields();
            populateAccountCodePurpose();
            return Constants.VIEW_COA;
        } else {
            addActionMessage(getText("charOfAccount.no.record"));
            return viewDetailedCode();
        }
    }

    @Action(value = "/masters/chartOfAccounts-addNew")
    public String addNew() throws Exception {
        populateCodeLength();
        model = new CChartOfAccounts();
        return "detailed";
    }

    @Action(value = "/masters/chartOfAccounts-create")
    public String create() throws Exception {
        if (glCode != null) {
            final CChartOfAccounts parent = chartOfAccountsService.find("from CChartOfAccounts where glcode=?",
                    glCode.split("-")[0]);
            if (parent == null) {
                addActionMessage(getText("chartOfAccount.no.data"));
                return addNew();
            }
            if (generatedGlcode == null || newGlcode == null) {
                addActionMessage(getText("chartOfAccount.invalid.glcode"));
                return "detailed";
            }
            final CChartOfAccounts coa = chartOfAccountsService.find("from CChartOfAccounts where glcode=?",
                    generatedGlcode.concat(newGlcode));
            if (coa != null) {
                addActionMessage(getText("chartOfAccount.glcode.already.exists"));
                return "detailed";
            }
            parentId = parent.getId();
            model.setParentId(parentId);
            model.setBudgetCheckReq(budgetCheckRequired);
            model.setFunctionReqd(functionRequired);
            model.setType(parent.getType());
            setClassification(parent);
            model.setGlcode(generatedGlcode.concat(newGlcode));
            model.setMajorCode(model.getGlcode().substring(0, majorCodeLength));
            setPurposeOnCoa();
            model.setIsActiveForPosting(activeForPosting);
            populateAccountDetailType();
            chartOfAccountsService.persist(model);
            saveCoaDetails(model);
            addActionMessage(getText("chartOfAccount.detailed.saved"));
        } else
            addActionMessage(getText("chartOfAccount.no.data"));
        clearCache();
        return Constants.VIEW_COA;
    }

    public void setGeneratedGlcode(final String generatedGlcode) {
        this.generatedGlcode = generatedGlcode;
    }

    public String getGeneratedGlcode() {
        return generatedGlcode;
    }

    public void setNewGlcode(final String newGlcode) {
        this.newGlcode = newGlcode;
    }

    public String getNewGlcode() {
        return newGlcode;
    }

    @Action(value = "/masters/chartOfAccounts-ajaxNextGlCode")
    public String ajaxNextGlCode() {
        final String parentGlcode = parameters.get("parentGlcode")[0];
        if (parentGlcode != null || !StringUtils.isBlank(parentGlcode)) {
            final CChartOfAccounts coa = chartOfAccountsService.find("from CChartOfAccounts where glcode=?", parentGlcode);
            final Long glCode = findNextGlCode(coa);
            if (glCode == null) {
                populateGlcode(coa.getClassification());
                newGlcode = model.getGlcode();
            } else {
                newGlcode = String.valueOf(glCode + 1);
                extractDetailCode();
            }
        }
        return "generated-glcode";
    }

    void extractDetailCode() {
        if (parentForDetailedCode.equals("2"))
            newGlcode = newGlcode.substring(minorCodeLength, newGlcode.length());
        else
            newGlcode = newGlcode.substring(subMinorCodeLength, newGlcode.length());
    }

    public Map<Long, Integer> getGlCodeLengths() {
        return glCodeLengths;
    }

    void clearCache() {
        try {
        	coaCache.reLoad();
        }catch(Exception e)
        {
        	LOGGER.error("Error while reloading coa cache");  
        }
    }

    public EgfAccountcodePurpose getAccountcodePurpose() {
        return accountcodePurpose;
    }

    public void setAccountcodePurpose(final EgfAccountcodePurpose purposeName) {
        accountcodePurpose = purposeName;
    }

    public List<CChartOfAccounts> getAllChartOfAccounts() {
        return allChartOfAccounts;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(final String glCode) {
        this.glCode = glCode;
    }

    public boolean isBudgetCheckRequired() {
        return budgetCheckRequired;
    }

    public boolean isFunctionRequired() {
        return functionRequired;
    }

    public void setBudgetCheckRequired(final boolean budgetCheckReq) {
        budgetCheckRequired = budgetCheckReq;
    }

    public void setFunctionRequired(final boolean functionReqd) {
        functionRequired = functionReqd;
    }

    public Long getCoaId() {
        return coaId;
    }

    public void setCoaId(final Long id) {
        coaId = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(final Long id) {
        parentId = id;
    }

    public void setActiveForPosting(final boolean activeForPosting) {
        this.activeForPosting = activeForPosting;
    }

    public List<String> getAccountDetailTypeList() {
        return accountDetailTypeList;
    }

    public void setAccountDetailTypeList(final List<String> accountDetailTypeList) {
        this.accountDetailTypeList = accountDetailTypeList;
    }

    public List<Accountdetailtype> getAccountDetailType() {
        return accountDetailType;
    }

    public void setAccountDetailType(final List<Accountdetailtype> accountDetailType) {
        this.accountDetailType = accountDetailType;
    }


    public PersistenceService<CChartOfAccounts, Long> getChartOfAccountsService() {
        return chartOfAccountsService;
    }

    public void setChartOfAccountsService(PersistenceService<CChartOfAccounts, Long> chartOfAccountsService) {
        this.chartOfAccountsService = chartOfAccountsService;
    }

    public void setModel(final CChartOfAccounts model) {
        this.model = model;
    }

}