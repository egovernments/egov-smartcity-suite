package org.egov.web.actions.masters;

import org.apache.struts2.convention.annotation.Action;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFunction;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.Validation;

@ParentPackage("egov")
@Validation()
public class FunctionAction extends BaseFormAction {
	
	private static final long serialVersionUID = -1076021355881784888L;
	private CFunction function = new CFunction();
	private boolean clearValues = false;
	private boolean close = false;
	private String showMode="view";
	private List<CFunction> functionList;
	private List<CFunction> funcSearchList = new ArrayList<CFunction>();
	private String success = "";
	protected static final Logger LOGGER = Logger.getLogger(FunctionAction.class);

	public Object getModel() {
		return function;
	}
	
	@Override
	public void prepare() {
		super.prepare();
		dropdownData.put("functionList", persistenceService
				.findAllBy("from CFunction where isActive=1 order by name"));
	}

	@SkipValidation
@Action(value="/masters/function-newform")
	public String newform() {
		return NEW;
	}
	
	@SkipValidation
	private int getParentIsNotLeaf(CFunction function) {
		int isNotLeaf = 0;

		if (function.getFunction() != null && function.getFunction().getId() != null) {
			List<CFunction> funcList = new ArrayList<CFunction>(persistenceService.findAllBy("from CFunction where parentId=?",
					function.getFunction().getId()));
			if (funcList.size() != 0) {
				isNotLeaf = 1;
			}
		}
		return isNotLeaf;
	}
	
	@ValidationErrorPage(NEW)
	@SuppressWarnings("unchecked")
	public String create() {
		StringBuffer funcNameStr = new StringBuffer();
		CFunction parentFunc = null;
		int parentLevel = 0;
		validatemandatoryFields_create();
		
		try {
			
			EgovMasterDataCaching.getInstance().removeFromCache("egi-function");
			function.setLevel(parentLevel);
			function.setCreated(new Date());
			function.setModifiedBy(getLoggedInUser());
			
			funcNameStr.append(function.getCode()).append("-").append(function.getName());

			function.setName(funcNameStr.toString());
			function.setIsNotLeaf(0);
			function.setFunction(parentFunc);
			if (function.getFunction() != null && function.getFunction().getId() != null) {
				parentFunc = (CFunction) persistenceService.find("from CFunction where id=?", function.getFunction()
						.getId());
				parentLevel = parentFunc.getLevel() + new Integer(1);
				parentFunc.setIsNotLeaf(1);
			} 
					
			persistenceService.setType(CFunction.class);
			persistenceService.persist(function);
			clearValues = true;
			setSuccess("yes");
		} catch (Exception e) {
			setSuccess("no");
			LOGGER.error("Exception occurred in FunctionAction-create ", e);
             
            throw new EGOVRuntimeException("Exception occurred in FunctionAction-create ", e);
		}
				return NEW;
	}
	
	@ValidationErrorPage(value = "edit")
	@SuppressWarnings("unchecked")
	public String edit() {
		StringBuffer funcNameStr = new StringBuffer();
		int parentLevel = 0;
		validatemandatoryFields();
		CFunction parentFunc = null;
		
		try {
			EgovMasterDataCaching.getInstance().removeFromCache("egi-function");
			CFunction funcOld = (CFunction) persistenceService.find("from CFunction where id=?", function.getId());
			if (function.getFunction() != null && function.getFunction().getId() != null) {
				parentFunc = (CFunction) persistenceService.find("from CFunction where id=?",
						function.getFunction().getId());
				parentLevel = parentFunc.getLevel() + new Integer(1);
			}
			//check if the old and the new parent function are not the same.
			if (funcOld.getFunction() != null && funcOld.getFunction().getId() != null && function.getFunction() != null
					&& function.getFunction().getId() != null) {
				if (!funcOld.getFunction().getId().equals(function.getFunction().getId())) {
					CFunction oldParentFunc = (CFunction) persistenceService.find("from CFunction where id=?", funcOld.getFunction()
							.getId());
					// setting the existing(old) parent function isNotLeaf value
					oldParentFunc.setIsNotLeaf(getParentIsNotLeaf(funcOld));
					persistenceService.setType(CFunction.class);
					persistenceService.update(oldParentFunc);
				}
			}
			funcOld.setLevel(parentLevel);
			
			//prefixing name with code (code-name)
			funcNameStr.append(function.getCode()).append("-").append(function.getFuncNameActual());
			funcOld.setName(funcNameStr.toString());
			funcOld.setCode(function.getCode());
			funcOld.setIsActive(function.isIsActive());
			funcOld.setModifiedBy(getLoggedInUser());
			funcOld.setFuncNameActual(function.getFuncNameActual());

			//Reading the parentFunc value at the start and then updating at the end due to StaleObjectException
			if (function.getFunction() != null && function.getFunction().getId() != null) {
				//setting the new parent function isNotLeaf value
				parentFunc.setIsNotLeaf(1);
			}	
			funcOld.setFunction(parentFunc);

			setFunction(funcOld);
			persistenceService.setType(CFunction.class);
			persistenceService.persist(function);
			setSuccess("yes");
		}  catch (Exception e) {
			setSuccess("no");
			LOGGER.error("Exception occurred in FunctionAction-edit ", e);
             
            throw new EGOVRuntimeException("Exception occurred in FunctionAction-edit ", e);
		}
		showMode = "edit";
		return EDIT;
	}

	@SkipValidation
@Action(value="/masters/function-beforeSearch")
	public String beforeSearch() {
		return "search";
	}
	
	@SuppressWarnings("unchecked")
	@SkipValidation
	public String search() {
		StringBuffer query = new StringBuffer();
		query.append("From CFunction");
		if (!function.getCode().equals("") && !function.getName().equals("")) {
			query.append(" where upper(code) like upper('%" + function.getCode()
					+ "%') and upper(name) like upper('%" + function.getName() + "%')");
		} else {
			if (!function.getCode().isEmpty())
				query.append(" where upper(code) like upper('%" + function.getCode() + "%')");
			if (!function.getName().isEmpty())
				query.append(" where upper(name) like upper('%" + function.getName() + "%')");
		}
		List<CFunction> fuList = persistenceService.findAllBy(query.toString());
		for (CFunction fu : fuList) {
			funcSearchList.add(fu);
		}
		
		return "search";
	}
	
	@SkipValidation
@Action(value="/masters/function-beforeModify")
	public String beforeModify() {
		function = (CFunction) persistenceService.find("from CFunction where id=?", function.getId());
	
		if (function.getName().contains("-")) {
			String funcName[] = function.getName().split("-");
			function.setFuncNameActual(funcName[1]);
	    }
		return EDIT;
	}

	@SkipValidation
	public boolean getCheckCode() {
		CFunction fn = null;
		boolean isDuplicate = false;
		if (!this.function.getCode().equals("") && this.function.getId() != null)
			fn = (CFunction) persistenceService.find("from CFunction where code=? and id!=?",
					this.function.getCode(), this.function.getId());
		else if (!this.function.getCode().equals(""))
			fn = (CFunction) persistenceService.find("from CFunction where code=?",
							this.function.getCode());
		if (fn != null) {
			isDuplicate = true;
		}
		return isDuplicate;
	}
	
	@SkipValidation
	private String getLoggedInUser() {
		Integer userId = (IntegerHibernateUtil.getCurrentSession().get("com.egov.user.LoginUserId");
		return userId.toString();
	}
	
	private void validatemandatoryFields() {
		if (function.getCode() == null || "".equals(function.getCode())) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"function.code.mandatory", getText("mandatory.function.code"))));
		}
		if (function.getFuncNameActual() == null || "".equals(function.getFuncNameActual())) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"function.name.mandatory", getText("mandatory.function.actualname"))));
		}
		if (function.getCode() != null) {
			if(getCheckCode()) {
				throw new ValidationException(Arrays.asList(new ValidationError(
						"Function.code.unique", getText("Function.code.unique"))));
			}
			if (function.getCode().contains("-")) {
				throw new ValidationException(Arrays.asList(new ValidationError(
						"validation.function.code", getText("validation.function.code"))));
			}
		}
	}
	
	private void validatemandatoryFields_create() {
		if (function.getCode() == null || "".equals(function.getCode())) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"function.code.mandatory", getText("mandatory.function.code"))));
		}
		if (function.getName() == null || "".equals(function.getName())) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"function.name.mandatory", getText("mandatory.function.name"))));
		}
		if (function.getCode() != null) {
			if(getCheckCode()) {
				throw new ValidationException(Arrays.asList(new ValidationError(
						"Function.code.unique", getText("Function.code.unique"))));
			}
			if (function.getCode().contains("-")) {
				throw new ValidationException(Arrays.asList(new ValidationError(
						"validation.function.code", getText("validation.function.code"))));
			}
		}
	}
	
	public CFunction getFunction() {
		return function;
	}

	public void setFunction(CFunction function) {
		this.function = function;
	}

	public String getShowMode() {
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

	public List<CFunction> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<CFunction> functionList) {
		this.functionList = functionList;
	}

	public List<CFunction> getFuncSearchList() {
		return funcSearchList;
	}

	public void setFuncSearchList(List<CFunction> funcSearchList) {
		this.funcSearchList = funcSearchList;
	}

	public void setClose(boolean close) {
		this.close = close;
	}
	
	public boolean isClose() {
		return close;
	}
	
	public void setClearValues(boolean clearValues) {
		this.clearValues = clearValues;
	}

	public boolean isClearValues() {
		return clearValues;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
	
}
