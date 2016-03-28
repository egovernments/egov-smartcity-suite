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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.services.masters.SubSchemeService;
import org.hibernate.exception.ConstraintViolationException;


import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")

@Results({
    @Result(name = SubSchemeAction.NEW, location = "subScheme-new.jsp"),
    @Result(name = SubSchemeAction.SEARCH, location = "subScheme-search.jsp"),
    @Result(name = SubSchemeAction.VIEW, location = "subScheme-view.jsp") })
public class SubSchemeAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = -3712472100095261379L;
    private SubScheme subScheme = new SubScheme();
    private boolean isActive = false;
    private boolean clearValues = false;
    private int fundId;
    private static final String REQUIRED = "required";
    private int schemeId;
    private Long subSchemeId;
    private List<SubScheme> subSchemeList;
    public static final String SEARCH = "search";
    public static final String VIEW = "view";
    private String showMode;
    private SubSchemeService subSchemeService;

    @Override
    public Object getModel() {
        return subScheme;
    }

    public SubSchemeAction() {
        addRelatedEntity("scheme", Scheme.class, "name");
        addRelatedEntity("department", Department.class, "name");
        addRelatedEntity("createdBy", User.class);
    }

    @Override
    public void prepare() {
        super.prepare();
        setupDropdownDataExcluding();
        dropdownData.put("schemeList", persistenceService.findAllBy("from Scheme where isactive=true order by name"));

    }

    @SkipValidation
    @Action(value = "/masters/subScheme-newForm")
    public String newForm() {
        showMode = "new";
        return NEW;

    }

    @Validations(requiredFields = { @RequiredFieldValidator(fieldName = "scheme", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "code", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "name", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "validfrom", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "validto", message = "", key = REQUIRED) })
    @ValidationErrorPage(value = NEW)
    @Action(value = "/masters/subScheme-create")
    public String save() {
        if (isActive)
            subScheme.setIsactive(true);
        else
            subScheme.setIsactive(false);

        subScheme.setCreatedDate(new Date());
        subScheme.setCreatedBy(getLoggedInUser());
        subScheme.setLastmodifieddate(new Date());
        try {
            subSchemeService.persist(subScheme);
            subSchemeService.getSession().flush();
        } catch (final ValidationException e) {
            throw e;
        } catch (final ConstraintViolationException e) {
            addActionError(getText("duplicate.subscheme"));
            return NEW;
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator",
                    "An error occured contact Administrator")));
        }
        clearValues = true;
        addActionMessage(getText("subscheme.saved.successfully"));
        showMode = "";
        return NEW;
    }

    @Action(value = "/masters/subScheme-edit")
    public String edit() {
        if (subSchemeId != null)
            subScheme.setId(subSchemeId.intValue());
        if (isActive)
            subScheme.setIsactive(true);
        else
            subScheme.setIsactive(false);

        subScheme.setLastModifiedBy(getLoggedInUser());
        subScheme.setLastmodifieddate(new Date());
        try {
            subSchemeService.persist(subScheme);
            subSchemeService.getSession().flush();
        } catch (final ValidationException e) {
            throw e;
        } catch (final ConstraintViolationException e) {
            throw new ValidationException(Arrays.asList(new ValidationError("duplicate.subscheme", "duplicate.subscheme")));
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator",
                    "An error occured contact Administrator")));
        }
        clearValues = true;
        addActionMessage(getText("subscheme.modified.successfully"));
        showMode = "";
        return NEW;
    }

    @SkipValidation
    @Action(value = "/masters/subScheme-beforeEdit")
    public String beforeEdit()
    {
        subScheme = (SubScheme) persistenceService.find("from SubScheme where id=?", subScheme.getId());
        if (subScheme!=null && subScheme.getIsactive())
            isActive = true;
        return NEW;
    }

    @SkipValidation
    @Action(value = "/masters/subScheme-beforeSearch")
    public String beforeSearch() {

        dropdownData.put("fundList", persistenceService
                .findAllBy("from Fund where isActive=true order by name"));
        dropdownData.put("schemeList", Collections.emptyList());
        dropdownData.put("subSchemeList", Collections.emptyList());
        fundId = 0;
        return SEARCH;
    }

    @SkipValidation
    @Action(value = "/masters/subScheme-search")
    public String search() {
        final StringBuffer query = new StringBuffer(500);
        new StringBuffer(100);
        query.append("From SubScheme s ");
        if (fundId != 0) {
            query.append("where s.scheme.fund.id= " + fundId);
            // params.append(""+fundId);

            if (schemeId != -1) {
                query.append("and  s.scheme.id= " + schemeId);
                // params.append(","+schemeId);

                if (subScheme.getId() != -1)
                    query.append("and s.id=" + subScheme.getId());
                // params.append(","+subSchemeId);
            }
        }
        loadDropDowns();
        subSchemeList = persistenceService.findAllBy(query.toString());
        return SEARCH;
    }

    @SkipValidation
    @Action(value = "/masters/subScheme-viewSubScheme")
    public String viewSubScheme() {
        subScheme = (SubScheme) persistenceService.find("from SubScheme where id=?", subScheme.getId());
        return VIEW;
    }

    private void loadDropDowns() {

        dropdownData.put("fundList", persistenceService
                .findAllBy("from Fund where isActive=true order by name"));
        final StringBuffer st = new StringBuffer();

        if (fundId != 0) {

            st.append("from Scheme where isactive=true and fund.id=");
            st.append(fundId);
            dropdownData.put("schemeList", persistenceService.findAllBy(st
                    .toString()));
            st.delete(0, st.length() - 1);

        } else
            dropdownData.put("schemeList", Collections.emptyList());
        if (schemeId != -1)
            dropdownData.put("subSchemeList",
                    persistenceService.findAllBy("from SubScheme where isactive=true and scheme.id=?", schemeId));
        else
            dropdownData.put("subSchemeList", Collections.emptyList());

    }

    private User getLoggedInUser() {
        return (User) persistenceService.getSession().load(User.class, EgovThreadLocals.getUserId());
    }

    public void setFundId(final int fundId) {
        this.fundId = fundId;
    }

    public int getFundId() {
        return fundId;
    }

    public void setSchemeId(final int schemeId) {
        this.schemeId = schemeId;
    }

    public int getSchemeId() {
        return schemeId;
    }

    public void setSubSchemeList(final List<SubScheme> subSchemeList) {
        this.subSchemeList = subSchemeList;
    }

    public List<SubScheme> getSubSchemeList() {
        return subSchemeList;
    }

    public void setShowMode(final String showMode) {
        this.showMode = showMode;
    }

    public String getShowMode() {
        return showMode;
    }

    public SubSchemeService getSubSchemeService() {
        return subSchemeService;
    }

    public void setSubSchemeService(final SubSchemeService subSchemeService) {
        this.subSchemeService = subSchemeService;
    }

    public void setSubScheme(final SubScheme subScheme) {
        this.subScheme = subScheme;
    }

    public SubScheme getSubScheme() {
        return subScheme;
    }

    public void setIsActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setClearValues(final boolean clearValues) {
        this.clearValues = clearValues;
    }

    public boolean isClearValues() {
        return clearValues;
    }

    public Long getSubSchemeId() {
        return subSchemeId;
    }

    public void setSubSchemeId(final Long subSchemeId) {
        this.subSchemeId = subSchemeId;
    }

}
