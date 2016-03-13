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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFunction;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.validator.annotations.Validation;

@ParentPackage("egov")
@Validation()
@Results({
    @Result(name = FunctionAction.NEW, location = "function-" + FunctionAction.NEW + ".jsp"),
    @Result(name = "search", location = "function-search.jsp"),
    @Result(name = FunctionAction.EDIT, location = "function-" + FunctionAction.EDIT + ".jsp")
})
public class FunctionAction extends BaseFormAction {

    private static final long serialVersionUID = -1076021355881784888L;
    private CFunction function = new CFunction();
    private boolean clearValues = false;
    private boolean close = false;
    private String showMode = "view";
    private List<CFunction> functionList;
    private List<CFunction> funcSearchList = new ArrayList<CFunction>();
    private String success = "";
    protected static final Logger LOGGER = Logger.getLogger(FunctionAction.class);

    @Override
    public Object getModel() {
        return function;
    }

    @Override
    public void prepare() {
        super.prepare();
        dropdownData.put("functionList", persistenceService
                .findAllBy("from CFunction where isActive=true order by name"));
    }

    @SkipValidation
    @Action(value = "/masters/function-newform")
    public String newform() {
        return NEW;
    }

    @SkipValidation
    private Boolean getParentIsNotLeaf(final CFunction function) {
        Boolean isNotLeaf=false;

        if (function.getFunction() != null && function.getFunction().getId() != null) {
            final List<CFunction> funcList = new ArrayList<CFunction>(persistenceService.findAllBy(
                    "from CFunction where parentId=?",
                    function.getFunction().getId()));
            if (funcList.size() != 0)
                isNotLeaf = true;
        }
        return isNotLeaf;
    }

    @ValidationErrorPage(NEW)
    @SuppressWarnings("unchecked")
    @Action(value = "/masters/function-create")
    public String create() {
        final StringBuffer funcNameStr = new StringBuffer();
        CFunction parentFunc = null;
        int parentLevel = 0;
        validatemandatoryFields_create();

        try {

            EgovMasterDataCaching.removeFromCache("egi-function");
            function.setLevel(parentLevel);
            function.setCreated(new Date());
            function.setModifiedBy(getLoggedInUser());

            funcNameStr.append(function.getCode()).append("-").append(function.getName());

            function.setName(funcNameStr.toString());
            function.setIsNotLeaf(false);
            function.setFunction(parentFunc);
            if (function.getFunction() != null && function.getFunction().getId() != null) {
                parentFunc = (CFunction) persistenceService.find("from CFunction where id=?", function.getFunction()
                        .getId());
                parentLevel = parentFunc.getLevel() + new Integer(1);
                parentFunc.setIsNotLeaf(true);
            }

            //persistenceService.setType(CFunction.class);
            persistenceService.persist(function);
            clearValues = true;
            setSuccess("yes");
        } catch (final Exception e) {
            setSuccess("no");
            LOGGER.error("Exception occurred in FunctionAction-create ", e);

            throw new ApplicationRuntimeException("Exception occurred in FunctionAction-create ", e);
        }
        return NEW;
    }

    @ValidationErrorPage(value = "edit")
    @SuppressWarnings("unchecked")
    @Action(value = "/masters/function-edit")
    public String edit() {
        final StringBuffer funcNameStr = new StringBuffer();
        int parentLevel = 0;
        validatemandatoryFields();
        CFunction parentFunc = null;

        try {
            EgovMasterDataCaching.removeFromCache("egi-function");
            final CFunction funcOld = (CFunction) persistenceService.find("from CFunction where id=?", function.getId());
            if (function.getFunction() != null && function.getFunction().getId() != null) {
                parentFunc = (CFunction) persistenceService.find("from CFunction where id=?",
                        function.getFunction().getId());
                parentLevel = parentFunc.getLevel() + new Integer(1);
            }
            // check if the old and the new parent function are not the same.
            if (funcOld.getFunction() != null && funcOld.getFunction().getId() != null && function.getFunction() != null
                    && function.getFunction().getId() != null)
                if (!funcOld.getFunction().getId().equals(function.getFunction().getId())) {
                    final CFunction oldParentFunc = (CFunction) persistenceService.find("from CFunction where id=?", funcOld
                            .getFunction()
                            .getId());
                    // setting the existing(old) parent function isNotLeaf value
                    oldParentFunc.setIsNotLeaf(getParentIsNotLeaf(funcOld));
                    //persistenceService.setType(CFunction.class);
                    persistenceService.update(oldParentFunc);
                }
            funcOld.setLevel(parentLevel);

            // prefixing name with code (code-name)
            funcNameStr.append(function.getCode()).append("-").append(function.getFuncNameActual());
            funcOld.setName(funcNameStr.toString());
            funcOld.setCode(function.getCode());
            funcOld.setIsActive(function.getIsActive());
            funcOld.setModifiedBy(getLoggedInUser());
            funcOld.setFuncNameActual(function.getFuncNameActual());

            // Reading the parentFunc value at the start and then updating at the end due to StaleObjectException
            if (function.getFunction() != null && function.getFunction().getId() != null)
                // setting the new parent function isNotLeaf value
                parentFunc.setIsNotLeaf(true);
            funcOld.setFunction(parentFunc);

            setFunction(funcOld);
            //persistenceService.setType(CFunction.class);
            persistenceService.persist(function);
            setSuccess("yes");
        } catch (final Exception e) {
            setSuccess("no");
            LOGGER.error("Exception occurred in FunctionAction-edit ", e);

            throw new ApplicationRuntimeException("Exception occurred in FunctionAction-edit ", e);
        }
        showMode = "edit";
        return EDIT;
    }

    @SkipValidation
    @Action(value = "/masters/function-beforeSearch")
    public String beforeSearch() {
        return "search";
    }

    @SuppressWarnings("unchecked")
    @SkipValidation
    @Action(value = "/masters/function-search")
    public String search() {
        final StringBuffer query = new StringBuffer();
        query.append("From CFunction");
        if (!function.getCode().equals("") && !function.getName().equals(""))
            query.append(" where upper(code) like upper('%" + function.getCode()
                    + "%') and upper(name) like upper('%" + function.getName() + "%')");
        else {
            if (!function.getCode().isEmpty())
                query.append(" where upper(code) like upper('%" + function.getCode() + "%')");
            if (!function.getName().isEmpty())
                query.append(" where upper(name) like upper('%" + function.getName() + "%')");
        }
        final List<CFunction> fuList = persistenceService.findAllBy(query.toString()+" order by id");
        for (final CFunction fu : fuList)
            funcSearchList.add(fu);

        return "search";
    }

    @SkipValidation
    @Action(value = "/masters/function-beforeModify")
    public String beforeModify() {
        function = (CFunction) persistenceService.find("from CFunction where id=?", function.getId());

        if (function.getName().contains("-")) {
            final String funcName[] = function.getName().split("-");
            function.setFuncNameActual(funcName[1]);
        }
        return EDIT;
    }

    @SkipValidation
    public boolean getCheckCode() {
        CFunction fn = null;
        boolean isDuplicate = false;
        if (!function.getCode().equals("") && function.getId() != null)
            fn = (CFunction) persistenceService.find("from CFunction where code=? and id!=?",
                    function.getCode(), function.getId());
        else if (!function.getCode().equals(""))
            fn = (CFunction) persistenceService.find("from CFunction where code=?",
                    function.getCode());
        if (fn != null)
            isDuplicate = true;
        return isDuplicate;
    }

    @SkipValidation
    private String getLoggedInUser() {
        final Long userId = EgovThreadLocals.getUserId();
        return userId.toString();
    }

    private void validatemandatoryFields() {
        if (function.getCode() == null || "".equals(function.getCode()))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "function.code.mandatory", getText("mandatory.function.code"))));
        if (function.getFuncNameActual() == null || "".equals(function.getFuncNameActual()))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "function.name.mandatory", getText("mandatory.function.actualname"))));
        if (function.getCode() != null) {
            if (getCheckCode())
                throw new ValidationException(Arrays.asList(new ValidationError(
                        "Function.code.unique", getText("Function.code.unique"))));
            if (function.getCode().contains("-"))
                throw new ValidationException(Arrays.asList(new ValidationError(
                        "validation.function.code", getText("validation.function.code"))));
        }
    }

    private void validatemandatoryFields_create() {
        if (function.getCode() == null || "".equals(function.getCode()))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "function.code.mandatory", getText("mandatory.function.code"))));
        if (function.getName() == null || "".equals(function.getName()))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "function.name.mandatory", getText("mandatory.function.name"))));
        if (function.getCode() != null) {
            if (getCheckCode())
                throw new ValidationException(Arrays.asList(new ValidationError(
                        "Function.code.unique", getText("Function.code.unique"))));
            if (function.getCode().contains("-"))
                throw new ValidationException(Arrays.asList(new ValidationError(
                        "validation.function.code", getText("validation.function.code"))));
        }
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public String getShowMode() {
        return showMode;
    }

    public void setShowMode(final String showMode) {
        this.showMode = showMode;
    }

    public List<CFunction> getFunctionList() {
        return functionList;
    }

    public void setFunctionList(final List<CFunction> functionList) {
        this.functionList = functionList;
    }

    public List<CFunction> getFuncSearchList() {
        return funcSearchList;
    }

    public void setFuncSearchList(final List<CFunction> funcSearchList) {
        this.funcSearchList = funcSearchList;
    }

    public void setClose(final boolean close) {
        this.close = close;
    }

    public boolean isClose() {
        return close;
    }

    public void setClearValues(final boolean clearValues) {
        this.clearValues = clearValues;
    }

    public boolean isClearValues() {
        return clearValues;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(final String success) {
        this.success = success;
    }

}
