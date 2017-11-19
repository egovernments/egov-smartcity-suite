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

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.egf.masters.model.FundingAgency;
import org.egov.egf.masters.model.LoanGrantBean;
import org.egov.egf.masters.model.LoanGrantDetail;
import org.egov.egf.masters.model.LoanGrantHeader;
import org.egov.egf.masters.model.LoanGrantReceiptDetail;
import org.egov.egf.masters.model.SchemeBankaccount;
import org.egov.egf.masters.model.SubSchemeProject;
import org.egov.egf.web.actions.masters.loangrant.LoanGrantBaseAction;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.services.masters.BankService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Results({
    @Result(name = LoanGrantAction.NEW, location = "loanGrant-" + LoanGrantAction.NEW + ".jsp"),
    @Result(name = "search", location = "loanGrant-search.jsp"),
    @Result(name = "new", location = "loanGrant-new.jsp"),
    @Result(name = LoanGrantAction.EDIT, location = "loanGrant-" + LoanGrantAction.EDIT + ".jsp"),
    @Result(name = "codeUniqueCheckCode", location = "loanGrant-codeUniqueCheckCode.jsp"),
    @Result(name = "view", location = "loanGrant-view.jsp")
})
public class LoanGrantAction extends LoanGrantBaseAction {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
    private static final long serialVersionUID = -5126017690888146473L;

    private static final String VIEW = "view";
    private LoanGrantHeader loanGrantHeader;
    private List<LoanGrantDetail> sanctionedAmountLGDetails;
    private List<LoanGrantDetail> unsanctionedAmountLGDetails;
    private List<LoanGrantDetail> revisedAmountLGDetails;
    private List<FundingAgency> fundingAgencyList;
    private List<LoanGrantBean> projectCodeList;
    private Integer bankaccount;
    private Integer bank_branch;
    private String mode;
    private Map<String, String> bankBranchMap;
    private List<LoanGrantHeader> loanGrantHeaderList;
    private Query query;
    private static final String SANCTIONEDTYPE = "sanctioned";
    private static final String UNSANCTIONEDTYPE = "unsanctioned";
    private static final String REVISEDTYPE = "revised";

    @Autowired
    @Qualifier("bankService")
    private BankService bankService;

    public LoanGrantAction() {
        super();
        loanGrantHeader = new LoanGrantHeader();
    }

    @Override
    public void prepare()
    {
        super.prepare();

    }

    public void prepareNewForm()
    {
        addDropdownData("bankaccountList", Collections.EMPTY_LIST);
    }

    @SuppressWarnings("unchecked")
    public void prepareBeforeEdit()
    {

        loanGrantHeader = (LoanGrantHeader) persistenceService.find("from LoanGrantHeader where id=?",
                ((LoanGrantHeader) getModel()).getId());
        final SchemeBankaccount account = (SchemeBankaccount) persistenceService.find("from SchemeBankaccount where subScheme=?",
                loanGrantHeader.getSubScheme());
        setFundId(loanGrantHeader.getSubScheme().getScheme().getFund().getId());
        setBank_branch(account.getBankAccount().getBankbranch().getId());
        final List<Bankaccount> accNumList = persistenceService.findAllBy(
                "from Bankaccount ba where ba.bankbranch.id=? and fund.id=? and isactive=true order by ba.chartofaccounts.glcode",
                bank_branch, fundId);
        setBankaccount(account.getBankAccount().getId().intValue());

        addDropdownData("bankaccountList", accNumList);
        final List<Bankbranch> branchList = persistenceService
                .findAllBy(
                        "from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund.id=? ) and br.isactive=true order by br.bank.name asc",
                        fundId);
        addDropdownData("bankbranchList", branchList);
        fundingAgencyList = new ArrayList<FundingAgency>();
        fundingAgencyList.addAll(persistenceService.findAllBy(" from FundingAgency where isActive=true order by name"));
        schemeId = loanGrantHeader.getSubScheme().getScheme().getId();
        subSchemeId = loanGrantHeader.getSubScheme().getId();
        projectCodeList = new ArrayList<LoanGrantBean>();
        final String strQuery = "select pc.id as id , pc.code as code, pc.name as name from egw_projectcode pc," +
                " egf_subscheme_project sp where pc.id= sp.projectcodeid and sp.subschemeid=" + subSchemeId;
        query = persistenceService.getSession().createSQLQuery(strQuery)
                .addScalar("id", LongType.INSTANCE).addScalar("code").addScalar("name")
                .setResultTransformer(Transformers.aliasToBean(LoanGrantBean.class));
        projectCodeList = query.list();
        final List<LoanGrantDetail> lgDetailList = loanGrantHeader.getDetailList();
        if (lgDetailList != null && lgDetailList.size() != 0)
        {
            boolean sanctionedDetailsPresent, unsanctionedDetailsPresent, revisedDetailsPresent;
            sanctionedDetailsPresent = unsanctionedDetailsPresent = revisedDetailsPresent = false;
            // Assumption is lgDetailList is atleast of size 1.
            for (final LoanGrantDetail lgDetail : lgDetailList)
            {
                if (lgDetail.getPatternType().equalsIgnoreCase(SANCTIONEDTYPE))
                    sanctionedDetailsPresent = true;
                if (lgDetail.getPatternType().equalsIgnoreCase(UNSANCTIONEDTYPE))
                    unsanctionedDetailsPresent = true;
                if (lgDetail.getPatternType().equalsIgnoreCase(REVISEDTYPE))
                    revisedDetailsPresent = true;
            }
            if (!sanctionedDetailsPresent)
            {
                sanctionedAmountLGDetails = new ArrayList<LoanGrantDetail>();
                sanctionedAmountLGDetails.add(new LoanGrantDetail());
            }
            if (!unsanctionedDetailsPresent)
            {
                unsanctionedAmountLGDetails = new ArrayList<LoanGrantDetail>();
                unsanctionedAmountLGDetails.add(new LoanGrantDetail());
            }
            if (!revisedDetailsPresent)
            {
                revisedAmountLGDetails = new ArrayList<LoanGrantDetail>();
                revisedAmountLGDetails.add(new LoanGrantDetail());
            }
        }
    }

    public void prepareBeforeView()
    {
        prepareBeforeEdit();
    }

    @Override
    public Object getModel() {

        return loanGrantHeader;
    }

    @SkipValidation
    @Action(value = "/masters/loanGrant-beforeSearch")
    public String beforeSearch()
    {
        return "search";
    }

    @SuppressWarnings("unchecked")
    @SkipValidation
    @Action(value = "/masters/loanGrant-search")
    public String search()
    {
        loanGrantHeaderList = new ArrayList<LoanGrantHeader>();
        if (schemeId != null && subSchemeId == null)
            loanGrantHeaderList.addAll(persistenceService.findAllBy(
                    "from LoanGrantHeader where subScheme.scheme.id=? order by subScheme.name ", schemeId));
        if (schemeId != null && subSchemeId != null)
            loanGrantHeaderList.addAll(persistenceService.findAllBy(
                    "from LoanGrantHeader where subScheme.id=? order by subScheme.name ", subSchemeId));
        return "search";
    }

    @SkipValidation
    @Action(value = "/masters/loanGrant-beforeView")
    public String beforeView()
    {
        beforeEdit();
        return VIEW;
    }

    @SkipValidation
    @Action(value = "/masters/loanGrant-beforeEdit")
    public String beforeEdit() {
        return EDIT;
    }

    @SuppressWarnings("unchecked")
    @SkipValidation
    @Action(value = "/masters/loanGrant-newForm")
    public String newForm()
    {
        projectCodeList = new ArrayList<LoanGrantBean>();
        projectCodeList.add(new LoanGrantBean());

        setBankBranchMap(new HashMap<String, String>());
        addDropdownData("bankaccountList", Collections.EMPTY_LIST);
        //persistenceService.setType(LoanGrantDetail.class);
        sanctionedAmountLGDetails = new ArrayList<LoanGrantDetail>();
        sanctionedAmountLGDetails.add(new LoanGrantDetail());
        unsanctionedAmountLGDetails = new ArrayList<LoanGrantDetail>();
        unsanctionedAmountLGDetails.add(new LoanGrantDetail());
        revisedAmountLGDetails = new ArrayList<LoanGrantDetail>();
        revisedAmountLGDetails.add(new LoanGrantDetail());
        //persistenceService.setType(FundingAgency.class);
        fundingAgencyList = new ArrayList<FundingAgency>();
        fundingAgencyList.addAll(persistenceService.findAllBy(" from FundingAgency where isActive=true order by name"));
        loanGrantHeader.getReceiptList().add(new LoanGrantReceiptDetail());
        return "new";
    }

    @SkipValidation
    @Action(value = "/masters/loanGrant-codeUniqueCheckCode")
    public String codeUniqueCheckCode() {
        return "codeUniqueCheckCode";
    }

    @SkipValidation
    public boolean getCodeCheckCode() {
        LoanGrantHeader header = null;
        boolean isDuplicate = false;
        if (subSchemeId != null && loanGrantHeader.getId() != null)
            header = (LoanGrantHeader) persistenceService.find("from LoanGrantHeader where subScheme.id=? and id!=?",
                    subSchemeId, loanGrantHeader.getId());
        else if (subSchemeId != null)
            header = (LoanGrantHeader) persistenceService.find("from LoanGrantHeader where subScheme.id=?", subSchemeId);
        if (header != null)
            isDuplicate = true;
        return isDuplicate;
    }

    @Override
    public void validate() {
        if (schemeId == null || schemeId == -1)
            addFieldError("schemeId", getText("masters.loangrant.scheme.mandatory"));
        if (subSchemeId == null || subSchemeId == -1)
            addFieldError("subSchemeId", getText("masters.loangrant.subscheme.mandatory"));
        if (bankaccount == null || bankaccount == -1)
            addFieldError("bankaccount", getText("masters.loangrant.bankaccount.mandatory"));
        if (loanGrantHeader.getAmendmentDate() == null)
            addFieldError("amendmentDate", getText("masters.loangrant.amendmentdate.mandatory"));
        if (loanGrantHeader.getAmendmentNo() == null)
            addFieldError("amendmentNo", getText("masters.loangrant.amendmentno.mandatory"));
        if (loanGrantHeader.getCouncilResDate() == null)
            addFieldError("councilResDate", getText("masters.loangrant.councilresdate.mandatory"));
        if (loanGrantHeader.getCouncilResNo() == null)
            addFieldError("councilResNo", getText("masters.loangrant.councilresno.mandatory"));
        if (loanGrantHeader.getGovtOrderDate() == null)
            addFieldError("govtOrderDate", getText("masters.loangrant.govtorderdate.mandatory"));
        if (loanGrantHeader.getGovtOrderNo() == null)
            addFieldError("govtOrderNo", getText("masters.loangrant.govtorderno.mandatory"));
        if (loanGrantHeader.getProjectCost() == null || loanGrantHeader.getProjectCost().compareTo(BigDecimal.ZERO)==0)
            addFieldError("projectCost", getText("masters.loangrant.projectcost.mandatory"));
        if (loanGrantHeader.getProjectCost() != null && loanGrantHeader.getSanctionedCost() != null
                && loanGrantHeader.getSanctionedCost().compareTo(loanGrantHeader.getProjectCost()) > 0)
            addFieldError("projectCost", getText("masters.loangrant.validate.projectcost"));
        if (loanGrantHeader.getProjectCost() != null && loanGrantHeader.getRevisedCost() != null
                && loanGrantHeader.getRevisedCost().compareTo(loanGrantHeader.getProjectCost()) < 0)
            addFieldError("revisedCost", getText("masters.loangrant.validate.revisedcost"));
        if (getCodeCheckCode())
            addActionError(getText("loangrant.subscheme.already.exists"));
    }

    @ValidationErrorPage(NEW)
    @SuppressWarnings("unchecked")
    @Action(value = "/masters/loanGrant-save")
    public String save()
    {
        if (!getFieldErrors().isEmpty())
            return NEW;
        try {
            final Scheme scheme = (Scheme) persistenceService.find(" from Scheme where id=?", getSchemeId());
            final SubScheme subScheme = (SubScheme) persistenceService.find(" from SubScheme where id=?", getSubSchemeId());
            loanGrantHeader.setSubScheme(subScheme);
            if (loanGrantHeader.getRevisedCost() == null)
                loanGrantHeader.setRevisedCost(BigDecimal.ZERO);
            SubSchemeProject subSchemeProject;
            //persistenceService.setType(SubSchemeProject.class);
            for (final LoanGrantBean bean : projectCodeList)
            {
                subSchemeProject = new SubSchemeProject();
                subSchemeProject.setSubScheme(subScheme);
                subSchemeProject.setProjectCode(bean.getId());
                persistenceService.persist(subSchemeProject);
            }
            final Bankaccount bankaccObj = (Bankaccount) persistenceService.find(" from Bankaccount where id=?", bankaccount);
            final SchemeBankaccount schemeBankaccount = new SchemeBankaccount();
            schemeBankaccount.setBankAccount(bankaccObj);
            schemeBankaccount.setScheme(scheme);
            schemeBankaccount.setSubScheme(subScheme);
            //persistenceService.setType(SchemeBankaccount.class);
            persistenceService.persist(schemeBankaccount);
            createDetailAndReceiptList();
            //persistenceService.setType(LoanGrantHeader.class);
            persistenceService.persist(loanGrantHeader);
        } catch (final ValidationException e) {
            throw e;
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator",
                    "An error occured contact Administrator")));
        }
        return "result";
    }

    @ValidationErrorPage(EDIT)
    @SuppressWarnings("unchecked")
    @Action(value = "/masters/loanGrant-update")
    public String update()
    {
        if (!getFieldErrors().isEmpty())
            return EDIT;
        try {
            final SubScheme subScheme = (SubScheme) persistenceService.find(" from SubScheme where id=?", getSubSchemeId());
            loanGrantHeader.setSubScheme(subScheme);
            final User user = (User) persistenceService.find("from User where id=?", ApplicationThreadLocals.getUserId());
            final Date currDate = new Date();
            loanGrantHeader.setCreatedBy(user);
            loanGrantHeader.setModifiedBy(user);
            loanGrantHeader.setCreatedDate(currDate);
            loanGrantHeader.setModifiedDate(currDate);
            if (loanGrantHeader.getRevisedCost() == null)
                loanGrantHeader.setRevisedCost(BigDecimal.ZERO);
            createDetailAndReceiptList();
            for (final LoanGrantDetail lgDetail : loanGrantHeader.getDetailList())
                if (lgDetail.getId() != null)
                {
                    lgDetail.setCreatedBy(user);
                    lgDetail.setModifiedBy(user);
                    lgDetail.setCreatedDate(currDate);
                    lgDetail.setModifiedDate(currDate);
                }
            for (final LoanGrantReceiptDetail lgRecptDetail : loanGrantHeader.getReceiptList())
                if (lgRecptDetail.getId() != null)
                {
                    lgRecptDetail.setCreatedBy(user);
                    lgRecptDetail.setModifiedBy(user);
                    lgRecptDetail.setCreatedDate(currDate);
                    lgRecptDetail.setModifiedDate(currDate);
                }
            query = persistenceService.getSession().createSQLQuery(
                    "delete from egf_subscheme_project where subschemeid= " + getSubSchemeId());
            query.executeUpdate();
            SubSchemeProject subSchemeProject;
            //persistenceService.setType(SubSchemeProject.class);
            for (final LoanGrantBean bean : projectCodeList)
            {
                subSchemeProject = new SubSchemeProject();
                subSchemeProject.setSubScheme(subScheme);
                subSchemeProject.setProjectCode(bean.getId());
                persistenceService.persist(subSchemeProject);
            }
            final SchemeBankaccount schemeBankaccount = (SchemeBankaccount) persistenceService.find(
                    "from SchemeBankaccount where scheme.id=?", getSchemeId());
            final Bankaccount accountObj = (Bankaccount) persistenceService.find("from Bankaccount where id=?", bankaccount);
            schemeBankaccount.setBankAccount(accountObj);
            //persistenceService.setType(LoanGrantHeader.class);
            persistenceService.persist(loanGrantHeader);
        } catch (final ValidationException e) {
            prepareBeforeEdit();
            throw e;
        } catch (final Exception e) {
            prepareBeforeEdit();
            throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator",
                    "An error occured contact Administrator")));

        }

        return "result";
    }

    @SkipValidation
    private void createDetailAndReceiptList()
    {
        for (final LoanGrantDetail detail : sanctionedAmountLGDetails)
            if (detail == null || detail.getFundingAgency() == null || detail.getFundingAgency().getId() == -1
            || (detail.getLoanAmount() == null || detail.getLoanAmount().compareTo(BigDecimal.ZERO)==0)
            && (detail.getGrantAmount() == null || detail.getGrantAmount().compareTo(BigDecimal.ZERO)==0))
                continue;
            else
            {
                if (detail.getLoanAmount() == null)
                    detail.setLoanAmount(BigDecimal.ZERO);
                if (detail.getGrantAmount() == null)
                    detail.setGrantAmount(BigDecimal.ZERO);
                detail.setPatternType(SANCTIONEDTYPE);
                detail.setHeader(loanGrantHeader);
                loanGrantHeader.getDetailList().add(detail);
            }
        for (final LoanGrantDetail detail : unsanctionedAmountLGDetails)
            if (detail == null || detail.getFundingAgency() == null || detail.getFundingAgency().getId() == -1
            || (detail.getLoanAmount() == null || detail.getLoanAmount().compareTo(BigDecimal.ZERO)==0)
            && (detail.getGrantAmount() == null || detail.getGrantAmount().compareTo(BigDecimal.ZERO)==0))
                continue;
            else
            {
                if (detail.getLoanAmount() == null)
                    detail.setLoanAmount(BigDecimal.ZERO);
                if (detail.getGrantAmount() == null)
                    detail.setGrantAmount(BigDecimal.ZERO);
                detail.setPatternType(UNSANCTIONEDTYPE);
                detail.setHeader(loanGrantHeader);
                loanGrantHeader.getDetailList().add(detail);
            }
        for (final LoanGrantDetail detail : revisedAmountLGDetails)
            if (detail == null || detail.getFundingAgency() == null || detail.getFundingAgency().getId() == -1
            || (detail.getLoanAmount() == null || detail.getLoanAmount().compareTo(BigDecimal.ZERO)==0)
            && (detail.getGrantAmount() == null || detail.getGrantAmount().compareTo(BigDecimal.ZERO)==0))
                continue;
            else
            {
                if (detail.getLoanAmount() == null)
                    detail.setLoanAmount(BigDecimal.ZERO);
                if (detail.getGrantAmount() == null)
                    detail.setGrantAmount(BigDecimal.ZERO);
                detail.setPatternType(REVISEDTYPE);
                detail.setHeader(loanGrantHeader);
                loanGrantHeader.getDetailList().add(detail);
            }
        final List<LoanGrantReceiptDetail> newReceiptList = new ArrayList<LoanGrantReceiptDetail>();
        for (final LoanGrantReceiptDetail receiptDetail : loanGrantHeader.getReceiptList())
            if (receiptDetail.getVoucherHeader() == null || receiptDetail.getVoucherHeader().getId() == null)
                continue;
            else
            {
                if (receiptDetail.getBankaccount() != null && receiptDetail.getBankaccount().getId() == null)
                    receiptDetail.setBankaccount(null);
                if (receiptDetail.getInstrumentHeader() != null && receiptDetail.getInstrumentHeader().getId() == null)
                    receiptDetail.setInstrumentHeader(null);
                if (receiptDetail.getFundingAgency() != null && receiptDetail.getFundingAgency().getId() == null)
                    receiptDetail.setFundingAgency(null);
                receiptDetail.setLoanGrantHeader(loanGrantHeader);
                newReceiptList.add(receiptDetail);
            }
        loanGrantHeader.setReceiptList(newReceiptList);
    }

    @SkipValidation
    public void loadBanks()
    {
        addDropdownData("bankBranchList", bankService.getAllBankAndBranchName(getFundId()));
    }

    public void setLoanGrantHeader(final LoanGrantHeader loanGrantHeader) {
        this.loanGrantHeader = loanGrantHeader;
    }

    public LoanGrantHeader getLoanGrantHeader() {
        return loanGrantHeader;
    }

    public List<LoanGrantHeader> getLoanGrantHeaderList() {
        return loanGrantHeaderList;
    }

    public void setUnsanctionedAmountLGDetails(
            final List<LoanGrantDetail> unsanctionedAmountLGDetails) {
        this.unsanctionedAmountLGDetails = unsanctionedAmountLGDetails;
    }

    public List<LoanGrantDetail> getUnsanctionedAmountLGDetails() {
        return unsanctionedAmountLGDetails;
    }

    public void setRevisedAmountLGDetails(final List<LoanGrantDetail> revisedAmountLGDetails) {
        this.revisedAmountLGDetails = revisedAmountLGDetails;
    }

    public List<LoanGrantDetail> getRevisedAmountLGDetails() {
        return revisedAmountLGDetails;
    }

    public void setSanctionedAmountLGDetails(
            final List<LoanGrantDetail> sanctionedAmountLGDetails) {
        this.sanctionedAmountLGDetails = sanctionedAmountLGDetails;
    }

    public List<LoanGrantDetail> getSanctionedAmountLGDetails() {
        return sanctionedAmountLGDetails;
    }

    public List<LoanGrantBean> getProjectCodeList() {
        return projectCodeList;
    }

    public void setProjectCodeList(final List<LoanGrantBean> projectCodeList) {
        this.projectCodeList = projectCodeList;
    }

    public void setBankBranchMap(final Map<String, String> bankBranchMap) {
        this.bankBranchMap = bankBranchMap;
    }

    public Map<String, String> getBankBranchMap() {
        return bankBranchMap;
    }

    public void setFundingAgencyList(final List<FundingAgency> fundingAgencyList) {
        this.fundingAgencyList = fundingAgencyList;
    }

    public List<FundingAgency> getFundingAgencyList() {
        return fundingAgencyList;
    }

    public void setBankaccount(final Integer bankaccount) {
        this.bankaccount = bankaccount;
    }

    public Integer getBankaccount() {
        return bankaccount;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public Integer getBank_branch() {
        return bank_branch;
    }

    public void setBank_branch(final Integer bank_branch) {
        this.bank_branch = bank_branch;
    }
}