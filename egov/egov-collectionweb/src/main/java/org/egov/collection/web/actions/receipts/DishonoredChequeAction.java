/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.collection.web.actions.receipts;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.collection.constants.CollectionConstants;
import org.egov.commons.dao.BankBranchHibernateDAO;
import org.egov.commons.dao.BankaccountHibernateDAO;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.springframework.beans.factory.annotation.Autowired;

import com.exilant.eGov.src.domain.BankBranch;

@Results({ @Result(name = BankRemittanceAction.NEW, location = "dishonoredCheque-new.jsp"),
  @Result(name = BankRemittanceAction.INDEX, location = "dishonoredCheque-index.jsp"),
  @Result(name = "accountList", location = "dishonoredCheque-accountList.jsp")})
@ParentPackage("egov")
public class DishonoredChequeAction extends BaseFormAction {

    private static final long serialVersionUID = 2871716607884152080L;
    private static final Logger LOGGER = Logger.getLogger(DishonoredChequeAction.class);

    private List bankBranchList;
    @Autowired
    private BankBranchHibernateDAO bankBranchHibernateDAO;
    @Autowired
    private BankaccountHibernateDAO bankaccountHibernateDAO;
    private String bankBranch;
    private List accountNumberList;
    private Map instrumentModesMap;
    private String chequeNumber;
    private Date chequeDate;
    

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData(CollectionConstants.DROPDOWN_DATA_BANKBRANCH_LIST, bankBranchHibernateDAO.getAllBankBranchs());
        addDropdownData(CollectionConstants.DROPDOWN_DATA_ACCOUNT_NO_LIST, new ArrayList());
        this.instrumentModesMap = CollectionConstants.INSTRUMENT_MODES_MAP;
    }
    
    @Action(value = "/receipts/dishonoredCheque-getAccountNumbers")
    public String getAccountNumbers() {
        try {
             accountNumberList = bankaccountHibernateDAO.getBankAccountByBankBranch(Integer.valueOf(bankBranch));
        } catch (final Exception ex) {
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);
        }
        return "accountList";
    }
    
    @Action(value = "/receipts/dishonoredCheque-getDetails")
    public String getDetails() {
        
        return NEW;
        
    }
    
    @SkipValidation
    @Action(value = "/receipts/dishonoredCheque-newForm")
    public String newForm() {
            return NEW;
    }

    public List getBankBranchList() {
        return bankBranchList;
    }

    public void setBankBranchList(final List bankBranchList) {
        this.bankBranchList = bankBranchList;
    }

    public Map getInstrumentModesMap() {
        return instrumentModesMap;
    }

    public void setInstrumentModesMap(Map instrumentModesList) {
        this.instrumentModesMap = instrumentModesList;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public List getAccountNumberList() {
        return accountNumberList;
    }

    public void setAccountNumberList(List accountNumberList) {
        this.accountNumberList = accountNumberList;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public Date getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(Date chequeDate) {
        this.chequeDate = chequeDate;
    }

}
