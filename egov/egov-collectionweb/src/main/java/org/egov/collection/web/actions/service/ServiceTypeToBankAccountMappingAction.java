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

import java.util.Collection;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.models.BankAccountServiceMap;
import org.egov.infstr.services.PersistenceService;


@ParentPackage("egov")
@Results({ @Result(name = ServiceTypeToBankAccountMappingAction.NEW, location = "serviceTypeToBankAccountMapping-new.jsp"),
        @Result(name = ServiceTypeToBankAccountMappingAction.EDIT, location = "serviceTypeToBankAccountMapping-edit.jsp"),
        @Result(name = ServiceTypeToBankAccountMappingAction.INDEX, location = "serviceTypeToBankAccountMapping-index.jsp") })
public class ServiceTypeToBankAccountMappingAction extends BaseFormAction{
	  private static final long serialVersionUID = 1L;
	    private PersistenceService<BankAccountServiceMap, Long> bankAccountServiceMap;
	    private Collection<BankAccountServiceMap> serviceTypeToBankAccountMappingList = null;
	    private BankAccountServiceMap bankAccountServiceInstance = new BankAccountServiceMap();
	    private Integer id;
	    private String bankName;
	    private String serviceDetails;
		private String branchName;
	    private String accountNumber;
	    private String serviceCategory;

		@Action(value = "/service/serviceTypeToBankAccountMapping-newform")
	    public String newform() {
			
	        addDropdownData("serviceCategoryList", bankAccountServiceMap.findAllByNamedQuery("SERVICE_CATEGORY_ALL"));
	        addDropdownData("serviceTypeList", bankAccountServiceMap.findAllByNamedQuery("GETSERVICETYPENOTMAPPED"));
	        addDropdownData("bankNameList", bankAccountServiceMap.findAllByNamedQuery("BANK_NAME_ATLEAST_ONE_BRANCH"));
	        addDropdownData("bankBranchList", bankAccountServiceMap.findAllByNamedQuery("BANKBRANCH_NAME_ATLEAST_ONE_BRANCH"));
	        addDropdownData("bankAcctNoList", bankAccountServiceMap.findAllByNamedQuery("BANKACCOUNT"));

	        return NEW;
	    }

	    @Action(value = "/service/serviceTypeToBankAccountMapping-list")
	    public String list() {
	    	serviceTypeToBankAccountMappingList = bankAccountServiceMap.findAll(CollectionConstants.SERVICETYPETOBANK_ID);
	        return INDEX;
	    }

	    @Action(value = "/service/serviceTypeToBankAccountMapping-edit")
	    public String edit() {
	    	bankAccountServiceInstance = bankAccountServiceMap.findByNamedQuery("SERVICETYPETOBANK_ID", id);
	        return EDIT;
	    }

	    @Action(value = "/service/serviceTypeToBankAccountMapping-save")
	    public String save() {
	    	bankAccountServiceMap.update(bankAccountServiceInstance);
	        return NEW;
	    }

	    @Action(value = "/service/serviceTypeToBankAccountMapping-create")
	    public String create() {
	    	System.out.print(bankName+serviceDetails+branchName+accountNumber+serviceCategory);
	    	
	    	bankAccountServiceMap.create(bankAccountServiceInstance);
	    	  return list();
	    }

	    @Override
	    public Object getModel() {
	        return bankAccountServiceInstance;
	    }

	    public PersistenceService<BankAccountServiceMap, Long> getBankAccountServiceMap() {
			return bankAccountServiceMap;
		}

		public void setBankAccountServiceMap(
				PersistenceService<BankAccountServiceMap, Long> bankAccountServiceMap) {
			this.bankAccountServiceMap = bankAccountServiceMap;
		}

		/**
	     * @return the ServiceTypeToBankAccountMappingList
	     */
	    public Collection<BankAccountServiceMap> getserviceTypeToBankAccountMappingList() {
	        return serviceTypeToBankAccountMappingList;
	    }

	    public void setserviceTypeToBankAccountMapping(final PersistenceService<BankAccountServiceMap, Long> BankAccountServiceMap) {
	        this.bankAccountServiceMap = bankAccountServiceMap;
	    }

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getBankName() {
			return bankName;
		}

		public void setBankName(String bankName) {
			this.bankName = bankName;
		}

		public String getServiceDetails() {
			return serviceDetails;
		}

		public void setServiceDetails(String serviceDetails) {
			this.serviceDetails = serviceDetails;
		}

		public String getBranchName() {
			return branchName;
		}

		public void setBranchName(String branchName) {
			this.branchName = branchName;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

		public String getServiceCategory() {
			return serviceCategory;
		}

		public void setServiceCategory(String serviceCategory) {
			this.serviceCategory = serviceCategory;
		}

	

	
		
}
