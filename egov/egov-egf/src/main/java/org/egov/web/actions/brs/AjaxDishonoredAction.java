package org.egov.web.actions.brs;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Bankaccount;
import org.egov.infra.web.struts.actions.BaseFormAction;

import java.util.Collections;
import java.util.List;

@ParentPackage("egov")
@Results({ @Result(name = AjaxDishonoredAction.ACCOUNTCODES, location = "ajaxDishonored-accountCodes.jsp") })
public class AjaxDishonoredAction extends BaseFormAction {

    /**
     *
     */
    private static final long serialVersionUID = -768679255839298625L;
    private String bankBranchId; // Set by Ajax call
    private List<Bankaccount> bankAccountList;
    public static final String ACCOUNTCODES = "accountCodes";

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Action(value = "/brs/ajaxDishonored-populateAccountCodes")
    public String populateAccountCodes() {
        if (bankBranchId != "-1" && bankBranchId != null && bankBranchId != "") {
            final String id[] = bankBranchId.split("-");
            final String branchId = id[1];
            bankAccountList = persistenceService.findAllBy("select ba from Bankaccount ba "
                    + "where ba.bankbranch.id=? and ba.isactive=true order by ba.id", Integer.parseInt(branchId));
        }
        else
            bankAccountList = Collections.emptyList();
        return ACCOUNTCODES;
    }

    public String getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(final String bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public List<Bankaccount> getBankAccountList() {
        return bankAccountList;
    }

    public void setBankAccountList(final List<Bankaccount> bankAccountList) {
        this.bankAccountList = bankAccountList;
    }

}
