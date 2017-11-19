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

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.services.masters.SchemeService;
import org.egov.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ParentPackage("egov")
@Results({
    @Result(name = SchemeAction.NEW, location = "scheme-new.jsp"),
    @Result(name = SchemeAction.SEARCH, location = "scheme-search.jsp"),
    @Result(name = SchemeAction.VIEW, location = "scheme-view.jsp"),
    @Result(name = SchemeAction.UNIQUECHECKFIELD, location = "scheme-fieldUniqueCheck.jsp") })
public class SchemeAction extends BaseFormAction {

    private static final long serialVersionUID = 5697760395477552986L;
    private Scheme scheme = new Scheme();
    private String mode;
    private static final String REQUIRED = "required";
    public static final String UNIQUECHECKFIELD = "fieldUniqueCheck";
    private boolean uniqueCode = false;
    public static final String SEARCH = "search";
    public static final String VIEW = "view";
    private static final Logger LOGGER = Logger.getLogger(SchemeAction.class);
    List<Scheme> schemeList;
    @Autowired
    @Qualifier("schemeService")
    private SchemeService schemeService;
    @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    @Override
    public Object getModel() {
        return scheme;
    }

    public SchemeAction() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside Scheme Action Constructor");
        addRelatedEntity("fund", Fund.class);
    }

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("fundDropDownList", masterDataCache.get("egi-fund"));

    }

    @SkipValidation
    @Action(value = "/masters/scheme-newForm")
    public String newForm() {
        scheme.reset();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("..Inside NewForm method..");
        mode = NEW;
        return NEW;
    }

    @SkipValidation
    @Action(value = "/masters/scheme-beforeSearch")
    public String beforeSearch() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Scheme Mode=" + mode);
        return SEARCH;
    }

    @SkipValidation
    @Action(value = "/masters/scheme-beforeEdit")
    public String beforeEdit() {
        scheme = (Scheme) persistenceService.find("from Scheme where id=?", scheme.getId());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("..Inside Before Edit Method..");
        mode = EDIT;
        return NEW;
    }

    @SkipValidation
    @Action(value = "/masters/scheme-beforeView")
    public String beforeView() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("..Inside Before View Method..");
        scheme = (Scheme) persistenceService.find("from Scheme where id=?", scheme.getId());
        mode = VIEW;
        return VIEW;
    }

    @SkipValidation
    @Action(value = "/masters/scheme-search")
    public String search() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside Search |Search scheme Action Starts");
        final StringBuffer query = new StringBuffer();
        schemeList = new ArrayList<Scheme>();
        query.append("From Scheme scheme");

        if (scheme.getFund().getId() != null)
            query.append(" where scheme.fund=" + scheme.getFund().getId());
        if (scheme.getValidfrom() != null && scheme.getValidto() != null)
            query.append(" and scheme.validfrom>='" + Constants.DDMMYYYYFORMAT1.format(scheme.getValidfrom()) + "'")
            .append("and scheme.validto<='" + Constants.DDMMYYYYFORMAT1.format(scheme.getValidto()) + "'");
        else if (scheme.getValidfrom() != null)
            query.append(" and scheme.validfrom>='" + Constants.DDMMYYYYFORMAT1.format(scheme.getValidfrom()) + "'");
        else if (scheme.getValidto() != null)
            query.append("and scheme.validto<='" + Constants.DDMMYYYYFORMAT1.format(scheme.getValidto()) + "'");
        query.append("order by scheme.name");
        schemeList = persistenceService.findAllBy(query.toString());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Scheme List Size is" + schemeList.size());
        return SEARCH;
    }

    @Validations(requiredFields = { @RequiredFieldValidator(fieldName = "fund", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "code", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "name", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "validfrom", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "validto", message = "", key = REQUIRED) })
    @ValidationErrorPage(value = NEW)
    @SuppressWarnings("unchecked")
    @Action(value = "/masters/scheme-edit")
    public String edit() {
        try {
        	scheme.setLastModifiedDate(new Date());
        	scheme.setLastModifiedBy((User)schemeService.getSession().load(User.class, ApplicationThreadLocals.getUserId()));
            schemeService.persist(scheme);
        } catch (final ValidationException e) {
            LOGGER.error("ValidationException in creating Scheme" + e.getMessage());
            throw e;
        } catch (final Exception e) {
            LOGGER.error("Exception while creating Scheme" + e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator",
                    "An error occured contact Administrator")));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(".................................Scheme Modified Successfully......................");
        addActionMessage(getText("Scheme Modified Successfully"));
        mode = EDIT;
        return NEW;
    }

    @ValidationErrorPage(value = NEW)
    @Action(value = "/masters/scheme-create")
    public String create() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("............................Creating New Scheme method.......................");

        try {
        	scheme.setCreatedDate(new Date());
        	scheme.setCreatedBy((User)schemeService.getSession().load(User.class, ApplicationThreadLocals.getUserId()));
            schemeService.persist(scheme);
        } catch (final ValidationException e) {
            LOGGER.error("ValidationException in create Scheme" + e.getMessage());
            throw e;
        } catch (final Exception e) {
            LOGGER.error("Exception while creating Scheme" + e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator",
                    "An error occured contact Administrator")));
        }

        addActionMessage(getText("Scheme Created Successfully"));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("SchemeAction  | Scheme Created successfully");
        mode = "";
        return NEW;
    }

    @SkipValidation
    public boolean getCheckField() {
        Scheme scheme_validate = null;
        boolean isDuplicate = false;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("......Scheme Unique check Begins......");
        if (uniqueCode) {
            if (!scheme.getCode().equals("") && scheme.getId() != null)
                scheme_validate = (Scheme) persistenceService.find("from Scheme where lower(code)=? and id!=?",
                        scheme.getCode().toLowerCase(), scheme.getId());
            else if (!scheme.getCode().equals(""))
                scheme_validate = (Scheme) persistenceService.find("from Scheme where lower(code)=?", scheme.getCode().toLowerCase());
            uniqueCode = false;
        } else if (!scheme.getName().equals("") && scheme.getId() != null)
            scheme_validate = (Scheme) persistenceService.find("from Scheme where lower(name)=? and id!=?", scheme.getName().toLowerCase(),
                    scheme.getId());
        else if (!scheme.getName().equals(""))
            scheme_validate = (Scheme) persistenceService.find("from Scheme where lower(name)=?", scheme.getName().toLowerCase());
        if (scheme_validate != null)
            isDuplicate = true;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("......Scheme Unique check processed......");
        return isDuplicate;
    }

    @SkipValidation
    @Action(value = "/masters/scheme-codeUniqueCheck")
    public String codeUniqueCheck() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("......Scheme Unique check for code......");
        uniqueCode = true;
        return UNIQUECHECKFIELD;
    }

    @SkipValidation
    @Action(value = "/masters/scheme-nameUniqueCheck")
    public String nameUniqueCheck() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("......Scheme Unique check for Name......");
        return UNIQUECHECKFIELD;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(final Scheme scheme) {
        this.scheme = scheme;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public List<Scheme> getSchemeList() {
        return schemeList;
    }

    public void setSchemeList(final List<Scheme> schemeList) {
        this.schemeList = schemeList;
    }

    public SchemeService getSchemeService() {
        return schemeService;
    }

    public void setSchemeService(final SchemeService schemeService) {
        this.schemeService = schemeService;
    }

}
