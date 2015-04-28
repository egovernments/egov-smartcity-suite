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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.web.actions.masters;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.masters.model.AccountEntity;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.Validation;

@ParentPackage("egov")
@Validation()
public class UserDefinedCodesAction extends BaseFormAction{

	private static final long serialVersionUID = 1706040004784954089L;
	private AccountEntity accEntity = new AccountEntity();
	private Accountdetailkey accDetKey = new Accountdetailkey();
	private AccountEntity accEntOld = new AccountEntity();
	private boolean clearValues = false;
	private boolean close = false;
	private String showMode="view";
	private List<AccountEntity> userDefCodeList;
	private List<AccountEntity> searchList;
	protected static final Logger LOGGER = Logger.getLogger(UserDefinedCodesAction.class);
	private String success = "";
	
	@SkipValidation
	public Object getModel() {
		return accEntity;
	}

	@Override
	public void prepare() {
		super.prepare();
		dropdownData.put("userDefCodeList", persistenceService
				.findAllBy("from Accountdetailtype where upper(tablename) = upper('accountEntityMaster') order by name"));
	}
	
	@SkipValidation
@Action(value="/masters/userDefinedCodes-newform")
	public String newform() {
		return NEW;
	}
	
	@ValidationErrorPage(NEW)
	@SuppressWarnings("unchecked")
	public String create() {
		validatemandatoryFields();
		
		try {
			if(accEntity.getIsactive() == null) {
				accEntity.setIsactive(false);
			}
			accEntity.setCreated(new Date());
			accEntity.setLastmodified(new Date());
			accEntity.setModifiedby(getLoggedInUser());
			persistenceService.setType(AccountEntity.class);
			persistenceService.persist(accEntity);
			createOrModifyAccDetKey(accEntity);
			setSuccess("yes");
		} catch (Exception e) {
			setSuccess("no");
        	LOGGER.error("Exception occurred in UserDefinedCodesAction-create ", e);
             
            throw new EGOVRuntimeException("Exception occurred in UserDefinedCodesAction-create ", e);
		}
		
		clearValues = true;
		return NEW;
	}
	
	@ValidationErrorPage(EDIT)
	@SuppressWarnings("unchecked")
	public String edit() {
		validatemandatoryFields();
		
		try {
			accEntOld = (AccountEntity) persistenceService.find(
					"from AccountEntity where id=?", accEntity.getId());
			accDetKey = (Accountdetailkey) persistenceService.find(
					"from Accountdetailkey where detailkey=? and accountdetailtype=?",
					accEntity.getId(), accEntOld.getAccountdetailtype());
			createOrModifyAccDetKey(accEntity);
			
			accEntOld.setLastmodified(new Date());
			accEntOld.setModifiedby(getLoggedInUser());
			Accountdetailtype  adt =(Accountdetailtype)  persistenceService.find("from Accountdetailtype where id=?", accEntity.getAccountdetailtype().getId());
			accEntOld.setAccountdetailtype(adt);
			accEntOld.setName(accEntity.getName());
			accEntOld.setCode(accEntity.getCode());
			accEntOld.setNarration(accEntity.getNarration());
			if (accEntity.getIsactive() == null) {
				accEntOld.setIsactive(false);
			} else {
				accEntOld.setIsactive(accEntity.getIsactive());
			}
			setAccEntity(accEntOld);
			persistenceService.setType(AccountEntity.class);
			persistenceService.persist(accEntity);
			setSuccess("yes");
		} catch (Exception e) {
			setSuccess("no");
        	LOGGER.error("Exception occurred in UserDefinedCodesAction-edit ", e);
             
            throw new EGOVRuntimeException("Exception occurred in UserDefinedCodesAction-edit ", e);
		}
		
		showMode = "view";
		return EDIT;
	}
	
	@SuppressWarnings("unchecked")
	private void createOrModifyAccDetKey(AccountEntity accountentity) {
		Accountdetailtype  adt =(Accountdetailtype)  persistenceService.find("from Accountdetailtype where id=?", accountentity.getAccountdetailtype().getId());
		accDetKey.setAccountdetailtype(adt);
		accDetKey.setDetailkey(accountentity.getId());
		accDetKey.setDetailname(adt.getAttributename());
		accDetKey.setGroupid(1);
		
		persistenceService.setType(Accountdetailkey.class);
		persistenceService.persist(accDetKey);
	}
	
	@SuppressWarnings("unchecked")
	@SkipValidation
	public String search() {
		StringBuffer query = new StringBuffer();
		query.append("From AccountEntity where ");
		if (!accEntity.getAccountdetailtype().getId().equals(0))
			query.append("accountdetailtype.id = "+ accEntity.getAccountdetailtype().getId());
		if (!accEntity.getCode().isEmpty())
			query.append(" and upper(code) like upper('%" + accEntity.getCode() + "%')");
		if (!accEntity.getName().isEmpty())
			query.append(" and upper(name) like upper('%" + accEntity.getName() + "%')");

		this.searchList = persistenceService.findAllBy(query.toString());
		return "search";
	}
	
	@SkipValidation
@Action(value="/masters/userDefinedCodes-beforeModify")
	public String beforeModify() {
		accEntity = (AccountEntity) persistenceService.find("from AccountEntity where id=?", accEntity.getId());
		return EDIT;
	}

	@SkipValidation
	private User getLoggedInUser() {
		/*Integer userId = (Integer)getSession.get("com.egov.user.LoginUserId");
        UserService userManager = null;
		User user = (User) userManager.getUserById(userId.longValue());
		return user;*/
		return null;
	}
	
	@SkipValidation
	public boolean getCheckCode() {
		AccountEntity ae = null;
		boolean isDuplicate = false;
		if (!this.accEntity.getCode().equals("") && this.accEntity.getId() != null)
			ae = (AccountEntity) persistenceService.find("from AccountEntity where code=? and id!=?",
					this.accEntity.getCode(), this.accEntity.getId());
		else if (!this.accEntity.getCode().equals(""))
			ae = (AccountEntity) persistenceService.find("from AccountEntity where code=?",
							this.accEntity.getCode());
		if (ae != null) {
			isDuplicate = true;
		}
		return isDuplicate;
	}
	
	private void validatemandatoryFields() {
		if (accEntity.getAccountdetailtype().getId().equals(0)
				|| accEntity.getAccountdetailtype().getId().equals("---- Choose ----")) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"UserDefinedCode.subcodefor.mandatory",	getText("mandatory.UserDefinedCode.subcodefor"))));
		}
		if (accEntity.getCode() == null || "".equals(accEntity.getCode()))
			throw new ValidationException(Arrays.asList(new ValidationError(
					"UserDefinedCode.code.mandatory", getText("mandatory.UserDefinedCode.code"))));
		if (accEntity.getName() == null || "".equals(accEntity.getName()))
			throw new ValidationException(Arrays.asList(new ValidationError(
					"UserDefinedCode.name.mandatory", getText("mandatory.UserDefinedCode.name"))));
		if(getCheckCode()) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"UserDefinedCode.code.unique", getText("UserDefinedCode.code.unique"))));
		}
	}

	@SkipValidation
@Action(value="/masters/userDefinedCodes-beforeSearch")
	public String beforeSearch() {
		return "search";
	}
	
	public AccountEntity getAccEntity() {
		return accEntity;
	}

	public void setAccEntity(AccountEntity accEntity) {
		this.accEntity = accEntity;
	}

	public boolean isClearValues() {
		return clearValues;
	}

	public void setClearValues(boolean clearValues) {
		this.clearValues = clearValues;
	}

	public boolean isClose() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}

	public String getShowMode() {
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

	public List<AccountEntity> getUserDefCodeList() {
		return userDefCodeList;
	}

	public void setUserDefCodeList(List<AccountEntity> userDefCodeList) {
		this.userDefCodeList = userDefCodeList;
	}

	public List<AccountEntity> getSearchList() {
		return searchList;
	}

	public void setSearchList(List<AccountEntity> searchList) {
		this.searchList = searchList;
	}

	public Accountdetailkey getAccDetKey() {
		return accDetKey;
	}

	public void setAccDetKey(Accountdetailkey accDetKey) {
		this.accDetKey = accDetKey;
	}

	public AccountEntity getAccEntOld() {
		return accEntOld;
	}

	public void setAccEntOld(AccountEntity accEntOld) {
		this.accEntOld = accEntOld;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

}
