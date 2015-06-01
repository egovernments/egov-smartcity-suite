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

import org.apache.struts2.convention.annotation.Action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.commons.EgPartytype;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Validation()
@Transactional(readOnly=true)
public class ContractTypeAction extends BaseFormAction{

	private static final long serialVersionUID = -8067645108656316667L;
	private EgwTypeOfWork typeOfWork = new EgwTypeOfWork();
	private EgwTypeOfWork parentTypeOfWk = null;
	private EgPartytype appliedParty = null;
	private boolean close = false;
	private String showMode = "view";
	private List<EgwTypeOfWork> typeOfWorkList;
	private List<EgPartytype> partyTypeList;
	private List<EgwTypeOfWork> typeOfWkSearchList;
	protected static final String REQUIRED = "required";
	private String success = "";
	protected static final Logger LOGGER = Logger.getLogger(ContractTypeAction.class);
	private boolean duplicateCode = false;
	
	public Object getModel() {
		return typeOfWork;
	}

	@Override
	@SkipValidation
	public void prepare() {
		super.prepare();
		dropdownData.put("partyTypeList", persistenceService.findAllBy("from EgPartytype order by code"));
		dropdownData.put("typeOfWorkList", persistenceService.findAllBy("from EgwTypeOfWork order by code"));
	}

	@SkipValidation
@Action(value="/masters/contractType-newform")
	public String newform() {
		//typeOfWork.reset();
		return NEW;
	}
	
	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "code", message = "Please Enter Code", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "description", message = "Please Enter Description", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "typeOfWork.egPartytype.id", message = "Please Select Applied To", key = REQUIRED) 
	})
	@SkipValidation
	@ValidationErrorPage(NEW)
	public String create() {
		try {
			if (typeOfWork.getCode() != null) {
				if(getCheckCode()) {
					addActionError(getText("typeofwork.code.unique"));
					return NEW;
				}
			}
			if (typeOfWork.getParentid() != null && typeOfWork.getParentid().getId() != null) {
				parentTypeOfWk = (EgwTypeOfWork) persistenceService.find("from EgwTypeOfWork where id=?", typeOfWork
						.getParentid().getId());
			}
			if (typeOfWork.getEgPartytype() != null && typeOfWork.getEgPartytype().getId() != null) {
				appliedParty = (EgPartytype) persistenceService.find("from EgPartytype where id=?", typeOfWork.getEgPartytype()
						.getId());
			}
			typeOfWork.setParentid(parentTypeOfWk);
			typeOfWork.setEgPartytype(appliedParty);
			typeOfWork.setCreatedby(getLoggedInUser());
			typeOfWork.setCreateddate(new Date());

			EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeMaster");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeAllChild");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-typeOfWorkParent");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-coaCodesForLiability");

			EgovMasterDataCaching.getInstance().removeFromCache("egi-tds");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-tdsType");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-recovery");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-egwTypeOfWork");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-egwSubTypeOfWork");

			persistenceService.setType(EgwTypeOfWork.class);
			persistenceService.persist(typeOfWork);
			HibernateUtil.getCurrentSession().flush();
			HibernateUtil.getCurrentSession().clear();
			setSuccess("yes");
		} catch (Exception e) {
			setSuccess("no");
			LOGGER.error("Exception occurred in ContractTypeAction-create ", e);
			 
			throw new EGOVRuntimeException("Exception occurred in ContractTypeAction-create ", e);
		}
	//	typeOfWork.reset();
		return NEW;
	}
	
	@SkipValidation
	@ValidationErrorPage(EDIT)
	public String edit() {
		try {
			EgwTypeOfWork typeOfWkOld = (EgwTypeOfWork) persistenceService.find("from EgwTypeOfWork where id=?",
					typeOfWork.getId());

			if (typeOfWork.getCode() != null) {
				if(getCheckCode()) {
					addActionError(getText("typeofwork.code.unique"));
					return EDIT;
				}
			}
			typeOfWkOld.setCode(typeOfWork.getCode());
			typeOfWkOld.setDescription(typeOfWork.getDescription());
			if (typeOfWork.getParentid() != null && typeOfWork.getParentid().getId() != null) {
				parentTypeOfWk = (EgwTypeOfWork) persistenceService.find("from EgwTypeOfWork where id=?", typeOfWork
						.getParentid().getId());
			}
			if (typeOfWork.getEgPartytype() != null && typeOfWork.getEgPartytype().getId() != null) {
				appliedParty = (EgPartytype) persistenceService.find("from EgPartytype where id=?", typeOfWork.getEgPartytype()
						.getId());
			}
			typeOfWkOld.setParentid(parentTypeOfWk);
			typeOfWkOld.setEgPartytype(appliedParty);
			typeOfWkOld.setLastmodifieddate(new Date());
			typeOfWkOld.setLastmodifiedby(getLoggedInUser());

			setTypeOfWork(typeOfWkOld);

			EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeMaster");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeAllChild");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-typeOfWorkParent");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-coaCodesForLiability");

			EgovMasterDataCaching.getInstance().removeFromCache("egi-tds");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-tdsType");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-recovery");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-egwTypeOfWork");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-egwSubTypeOfWork");

			persistenceService.setType(EgwTypeOfWork.class);
			persistenceService.persist(typeOfWork);
			showMode = "view";
			setSuccess("yes");
		} catch (Exception e) {
			setSuccess("no");
			LOGGER.error("Exception occurred in ContractTypeAction-edit ", e);
			 
			throw new EGOVRuntimeException("Exception occurred in ContractTypeAction-edit ", e);
		}
		return EDIT;
	}
	
	@SkipValidation
@Action(value="/masters/contractType-beforeSearch")
	public String beforeSearch() {
		return "search";
	}
	
	@SkipValidation
	public String search() {
		StringBuffer query = new StringBuffer();
		query.append("From EgwTypeOfWork where createdBy is not null ");
		if (!typeOfWork.getCode().isEmpty()) {
			query.append(" and upper(code) like upper('%" + typeOfWork.getCode() + "%')");
		}
		if (!typeOfWork.getDescription().isEmpty()) {
			query.append(" and upper(description) like upper('%" + typeOfWork.getDescription() + "%')");
		}
		if (typeOfWork.getEgPartytype() != null && typeOfWork.getEgPartytype().getId() != null) {
			query.append(" and egPartytype =" + typeOfWork.getEgPartytype());
		}
		if (typeOfWork.getParentid() != null && typeOfWork.getParentid().getId() != null) {
			query.append(" and parentid = " + typeOfWork.getParentid());
		}
		this.typeOfWorkList = persistenceService.findAllBy(query.toString());
		
		return "search";
	}
	
	@SkipValidation
@Action(value="/masters/contractType-beforeModify")
	public String beforeModify() {
		typeOfWork = (EgwTypeOfWork) persistenceService.find("from EgwTypeOfWork where id=?", typeOfWork.getId());
		return EDIT;
	}
	
	@SkipValidation
	public boolean getCheckCode() {
		EgwTypeOfWork tow = null;
		if (!this.typeOfWork.getCode().equals("") && this.typeOfWork.getId() != null)
			tow = (EgwTypeOfWork) persistenceService.find("from EgwTypeOfWork where code=? and id!=?",
					this.typeOfWork.getCode(), this.typeOfWork.getId());
		else if (!this.typeOfWork.getCode().equals(""))
			tow = (EgwTypeOfWork) persistenceService.find("from EgwTypeOfWork where code=?",
							this.typeOfWork.getCode());
		if (tow != null) {
			duplicateCode = true;
		}
		return duplicateCode;
	}
	
	@SkipValidation
	private Integer getLoggedInUser() {
		Integer userId = (Integer)getSession().get("com.egov.user.LoginUserId");
		return userId;
	}
	
	public EgwTypeOfWork getTypeOfWork() {
		return typeOfWork;
	}

	public void setTypeOfWork(EgwTypeOfWork typeOfWork) {
		this.typeOfWork = typeOfWork;
	}

	public String getShowMode() {
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

	public List<EgwTypeOfWork> getTypeOfWorkList() {
		return typeOfWorkList;
	}

	public void setTypeOfWorkList(List<EgwTypeOfWork> typeOfWorkList) {
		this.typeOfWorkList = typeOfWorkList;
	}

	public List<EgwTypeOfWork> getTypeOfWkSearchList() {
		return typeOfWkSearchList;
	}

	public void setTypeOfWkSearchList(List<EgwTypeOfWork> typeOfWkSearchList) {
		this.typeOfWkSearchList = typeOfWkSearchList;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setClose(boolean close) {
		this.close = close;
	}
	
	public boolean isClose() {
		return close;
	}

	public EgwTypeOfWork getParentTypeOfWk() {
		return parentTypeOfWk;
	}

	public void setParentTypeOfWk(EgwTypeOfWork parentTypeOfWk) {
		this.parentTypeOfWk = parentTypeOfWk;
	}

	public EgPartytype getAppliedParty() {
		return appliedParty;
	}

	public void setAppliedParty(EgPartytype appliedParty) {
		this.appliedParty = appliedParty;
	}

	public List<EgPartytype> getPartyTypeList() {
		return partyTypeList;
	}

	public void setPartyTypeList(List<EgPartytype> partyTypeList) {
		this.partyTypeList = partyTypeList;
	}
	
	public void setDuplicateCode(boolean duplicateCode) {
		this.duplicateCode = duplicateCode;
	}
	
	public boolean isDuplicateCode() {
		return duplicateCode;
	}
}
