/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.collection.web.actions.service;

import java.util.Collections;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Bankaccount;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.models.BankAccountServiceMap;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({
    @Result(name = ServiceTypeToBankAccountMappingAction.NEW, location = "serviceTypeToBankAccountMapping-new.jsp"),
    @Result(name = ServiceTypeToBankAccountMappingAction.SUCCESS, location = "serviceTypeToBankAccountMapping-success.jsp"),
        @Result(name = ServiceTypeToBankAccountMappingAction.EDIT, location = "serviceTypeToBankAccountMapping-edit.jsp"),
        @Result(name = ServiceTypeToBankAccountMappingAction.INDEX, location = "serviceTypeToBankAccountMapping-index.jsp") })
public class ServiceTypeToBankAccountMappingAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private PersistenceService<BankAccountServiceMap, Long> bankAccountService;
    private BankAccountServiceMap bankAccountServiceMap = new BankAccountServiceMap();
    @Autowired
    private BankHibernateDAO bankHibernateDAO;
    
    public ServiceTypeToBankAccountMappingAction() {
        addRelatedEntity("serviceDetails", ServiceDetails.class);
        addRelatedEntity("bankAccountId", Bankaccount.class);
    }

    @Action(value = "/service/serviceTypeToBankAccountMapping-newform")
    public String newform() {
        populateLists();
        return NEW;
    }

    private void populateLists() {
        addDropdownData("serviceCategoryList", persistenceService.findAllByNamedQuery("SERVICE_CATEGORY_ALL"));
        addDropdownData("serviceTypeList", Collections.EMPTY_LIST);
        addDropdownData("bankNameList", bankHibernateDAO.getAllBankHavingBranchAndAccounts());
        addDropdownData("bankBranchList", Collections.EMPTY_LIST);
        addDropdownData("accountNumberList", Collections.EMPTY_LIST);
    }

    @Action(value = "/service/serviceTypeToBankAccountMapping-list")
    public String list() {
        // serviceTypeToBankAccountMappingList =
        // bankAccountService.findAll(CollectionConstants.SERVICETYPETOBANK_ID);
        return INDEX;
    }

    @Action(value = "/service/serviceTypeToBankAccountMapping-edit")
    public String edit() {
        // bankAccountServiceInstance =
        // bankAccountService.findByNamedQuery("SERVICETYPETOBANK_ID", id);
        return EDIT;
    }

    @Action(value = "/service/serviceTypeToBankAccountMapping-create")
    public String create() {
        bankAccountService.persist(bankAccountServiceMap);
        BankAccountServiceMap serviceMap = ((BankAccountServiceMap) getModel());
        addActionMessage(getText("service.master.successmessage.create", new String[] { serviceMap.getServiceDetails().getName(),
                serviceMap.getBankAccountId().getBankbranch().getBank().getName(), serviceMap.getBankAccountId().getAccountnumber()}));
        return SUCCESS;
    }

    @Override
    public Object getModel() {
        return bankAccountServiceMap;
    }

    public PersistenceService<BankAccountServiceMap, Long> getBankAccountService() {
        return bankAccountService;
    }

    public void setBankAccountService(final PersistenceService<BankAccountServiceMap, Long> bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
}
