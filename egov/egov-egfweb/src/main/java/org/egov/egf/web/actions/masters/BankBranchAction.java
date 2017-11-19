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

import com.google.gson.GsonBuilder;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Bank;
import org.egov.commons.Bankbranch;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParentPackage("egov")
public class BankBranchAction extends JQueryGridActionSupport {
    private static final long serialVersionUID = 1L;
    private String mode;
    private Integer bankId;
    private PersistenceService<Bankbranch, Integer> bankBranchService;
    private boolean isActive;

    @Override
    public String execute() {
        if ("CRUD".equals(mode))
            try {
                if (oper.equals(ADD))
                    addBankBranch();
                else if (oper.equals(EDIT))
                    editBankBranch();
                else if (oper.equals(DELETE))
                    deleteBankBranch();
                sendAJAXResponse(SUCCESS);
            } catch (final RuntimeException e) {
                sendAJAXResponse("failed");
                throw new ApplicationRuntimeException("Error occurred in Bank Branch.", e);
            }
        else if ("LIST_BRANCH".equals(mode))
            listAllBankBranches();
        else if ("CHECK_UNQ_MICR".equals(mode))
            sendAJAXResponse(String.valueOf(checkIsUniqueMicr()));
        else if ("CHECK_BANK_ACC".equals(mode))
            sendAJAXResponse(String.valueOf(checkBankAccountsExists()));
        return null;
    }

    private void addBankBranch() {
        final Bank bank = bankBranchService.getSession().load(Bank.class, bankId);
        final Date currentDate = new Date();
        final Bankbranch bankBranch = new Bankbranch();
        bankBranch.setBank(bank);
        bankBranch.setCreatedDate(currentDate);
        bankBranch.setCreatedBy(bankBranchService.getSession().load(User.class, ApplicationThreadLocals.getUserId()));
        populateBankBranchDetail(bankBranch);
        bankBranchService.persist(bankBranch);
    }

    private void editBankBranch() {
        final Bankbranch bankBranch = bankBranchService.getSession().get(Bankbranch.class, id);
        populateBankBranchDetail(bankBranch);
        bankBranchService.update(bankBranch);
    }

    private void deleteBankBranch() {
        final Bankbranch bankBranch = bankBranchService.getSession().load(Bankbranch.class, id);
        bankBranchService.delete(bankBranch);
    }

    private void populateBankBranchDetail(final Bankbranch bankBranch) {
        final HttpServletRequest request = ServletActionContext.getRequest();
        bankBranch.setLastModifiedDate(new Date());
        bankBranch.setLastModifiedBy(bankBranchService.getSession().load(User.class, ApplicationThreadLocals.getUserId()));
        bankBranch.setBranchcode(request.getParameter("branchcode"));
        bankBranch.setBranchname(request.getParameter("branchname"));
        bankBranch.setBranchaddress1(request.getParameter("branchaddress1"));
        bankBranch.setIsactive("Y".equals(request.getParameter("isActive")));
        isActive = bankBranch.getIsactive();
        bankBranch.setBranchaddress2(request.getParameter("branchaddress2"));
        bankBranch.setBranchcity(request.getParameter("branchcity"));
        bankBranch.setBranchstate(request.getParameter("branchstate"));
        bankBranch.setBranchpin(request.getParameter("branchpin"));
        bankBranch.setBranchphone(request.getParameter("branchphone"));
        bankBranch.setBranchfax(request.getParameter("branchfax"));
        bankBranch.setContactperson(request.getParameter("contactperson"));
        bankBranch.setNarration(request.getParameter("narration"));
        if (org.apache.commons.lang.StringUtils.isNotBlank(request.getParameter("branchMICR")))
            bankBranch.setBranchMICR(BigDecimal.valueOf(Long.valueOf(request.getParameter("branchMICR"))).toString());
    }

    private void listAllBankBranches() {
        final List<Bankbranch> bankBranches = getPagedResult(Bankbranch.class, "bank.id", bankId).getList();
        final List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

        for (final Bankbranch bankbranch : bankBranches)
            try {
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", bankbranch.getId());
                jsonObject.put("branchname", bankbranch.getBranchname());
                jsonObject.put("branchcode", bankbranch.getBranchcode());
                jsonObject.put("branchMICR", bankbranch.getBranchMICR());
                jsonObject.put("branchaddress1", bankbranch.getBranchaddress1());
                jsonObject.put("contactperson", bankbranch.getContactperson());
                jsonObject.put("branchphone", bankbranch.getBranchphone());
                jsonObject.put("narration", bankbranch.getNarration());
                jsonObject.put("isActive", bankbranch.getIsactive() ? "Y" : "N");
                jsonObjects.add(jsonObject);
            } catch (final JSONException e) {
                sendAJAXResponse("error");
            }
        final String jsonString = new GsonBuilder().create().toJson(jsonObjects);
        sendAJAXResponse(constructJqGridResponse(jsonString));
    }

    private boolean checkBankAccountsExists() {
        Bankbranch branch = null;
        if (id != null)
            branch = bankBranchService.find("from Bankbranch where id=?", id);
        return branch != null && branch.isAccountsExist();
    }

    private boolean checkIsUniqueMicr() {
        boolean isUnique = true;
        final String branchMICR = ServletActionContext.getRequest().getParameter("branchMICR");
        if (branchMICR != null && id != null)
            isUnique = null == bankBranchService.find("from Bankbranch where branchMICR=? and id!=?", branchMICR, id);
        else if (branchMICR != null)
            isUnique = null == bankBranchService.find("from Bankbranch where branchMICR=?", branchMICR);
        return isUnique;
    }

    public PersistenceService<Bankbranch, Integer> getBankBranchService() {
        return bankBranchService;
    }

    public void setBankBranchService(final PersistenceService<Bankbranch, Integer> bankBranchService) {
        this.bankBranchService = bankBranchService;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public void setBankId(final Integer bankId) {
        this.bankId = bankId;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(final boolean isActive) {
        this.isActive = isActive;
    }

}