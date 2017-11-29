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


import com.opensymphony.xwork2.validator.annotations.Validation;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgPartytype;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.List;

@ParentPackage("egov")
@Validation()

@Results({
    @Result(name = PartyTypeAction.NEW, location = "partyType-" + PartyTypeAction.NEW + ".jsp"),
    @Result(name = "search", location = "partyType-search.jsp"),
    @Result(name = PartyTypeAction.EDIT, location = "partyType-" + PartyTypeAction.EDIT + ".jsp")
})
public class PartyTypeAction extends BaseFormAction {

    private static final long serialVersionUID = -1076021355881784888L;
    private EgPartytype partyType = new EgPartytype();
    private boolean close = false;
    private String showMode = "view";
    private List<EgPartytype> partyTypeList;
    private List<EgPartytype> partySearchList;
    private EgPartytype parentParty = null;
    protected static final String REQUIRED = "required";
    private String success = "";
    protected static final Logger LOGGER = Logger.getLogger(PartyTypeAction.class);

   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    @Override
    @SkipValidation
    public Object getModel() {
        return partyType;
    }

    @Override
    @SkipValidation
    public void prepare() {
        super.prepare();
        dropdownData.put("partyTypeList", persistenceService
                .findAllBy("from EgPartytype order by code"));
    }

    @SkipValidation
    @Action(value = "/masters/partyType-newform")
    public String newform() {
        return NEW;
    }

    @SkipValidation
    @ValidationErrorPage(NEW)
    @Action(value = "/masters/partyType-create")
    public String create() {
        validatemandatoryFields();
        try {
            if (partyType.getEgPartytype() != null && partyType.getEgPartytype().getId() != null)
                parentParty = (EgPartytype) persistenceService
                .find("from EgPartytype where id=?", partyType.getEgPartytype().getId());
            partyType.setEgPartytype(parentParty);
            partyType.setCode(partyType.getCode());
            partyType.setDescription(partyType.getDescription());

            masterDataCache.removeFromCache("egi-partyTypeMaster");
            masterDataCache.removeFromCache("egi-partyTypeAllChild");
            masterDataCache.removeFromCache("egi-typeOfWorkParent");
            masterDataCache.removeFromCache("egi-coaCodesForLiability");

            masterDataCache.removeFromCache("egi-tds");
            masterDataCache.removeFromCache("egi-tdsType");
            masterDataCache.removeFromCache("egi-recovery");
            masterDataCache.removeFromCache("egi-egwTypeOfWork");
            masterDataCache.removeFromCache("egi-egwSubTypeOfWork");

            //persistenceService.setType(EgPartytype.class);
            persistenceService.persist(partyType);
            persistenceService.getSession().flush();
            persistenceService.getSession().clear();
            setSuccess("yes");
        } catch (final Exception e) {
            setSuccess("no");
            LOGGER.error("Exception occurred in PartyTypeAction-create ", e);

            throw new ApplicationRuntimeException("Exception occurred in PartyTypeAction-create ", e);
        }
        return NEW;
    }

    @SkipValidation
    @ValidationErrorPage(EDIT)
    @Action(value = "/masters/partyType-edit")
    public String edit() {
        validatemandatoryFields();
        try {
            final EgPartytype partyOld = (EgPartytype) persistenceService.find("from EgPartytype where id=?", partyType.getId());

            partyOld.setCode(partyType.getCode());

            partyOld.setDescription(partyType.getDescription());
            if (partyType.getEgPartytype() != null && partyType.getEgPartytype().getId() != null)
                parentParty = (EgPartytype) persistenceService
                .find("from EgPartytype where id=?", partyType.getEgPartytype().getId());
            partyOld.setEgPartytype(parentParty);

            setPartyType(partyOld);

            masterDataCache.removeFromCache("egi-partyTypeMaster");
            masterDataCache.removeFromCache("egi-partyTypeAllChild");
            masterDataCache.removeFromCache("egi-typeOfWorkParent");
            masterDataCache.removeFromCache("egi-coaCodesForLiability");

            masterDataCache.removeFromCache("egi-tds");
            masterDataCache.removeFromCache("egi-tdsType");
            masterDataCache.removeFromCache("egi-recovery");
            masterDataCache.removeFromCache("egi-egwTypeOfWork");
            masterDataCache.removeFromCache("egi-egwSubTypeOfWork");

            //persistenceService.setType(EgPartytype.class);
            persistenceService.persist(partyType);
            // showMode = "view";
            setSuccess("yes");
        } catch (final Exception e) {
            setSuccess("no");
            LOGGER.error("Exception occurred in PartyTypeAction-edit ", e);

            throw new ApplicationRuntimeException("Exception occurred in PartyTypeAction-edit ", e);
        }
        showMode = "edit";
        return EDIT;
    }

    @SkipValidation
    @Action(value = "/masters/partyType-beforeSearch")
    public String beforeSearch() {
        return "search";
    }

    @SkipValidation
    @Action(value = "/masters/partyType-search")
    public String search() {
        final StringBuffer query = new StringBuffer();

        query.append("From EgPartytype where createdBy is not null ");
        if (!partyType.getCode().isEmpty())
            query.append(" and upper(code) like upper('%" + partyType.getCode() + "%')");
        if (!partyType.getDescription().isEmpty())
            query.append(" and upper(description) like upper('%" + partyType.getDescription() + "%')");
        if (partyType.getEgPartytype() != null && partyType.getEgPartytype().getId() != null)
            query.append(" and egPartytype =" + partyType.getEgPartytype());
        partySearchList = persistenceService.findAllBy(query.toString());

        // this.partySearchList = masterDataCache.get(query.toString());
        return "search";
    }

    @SkipValidation
    @Action(value = "/masters/partyType-beforeModify")
    public String beforeModify() {
        partyType = (EgPartytype) persistenceService.find("from EgPartytype where id=?", partyType.getId());

        return EDIT;
    }

    private void validatemandatoryFields() {
        if (partyType.getCode() == null || "".equals(partyType.getCode()))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "party.code.mandatory", getText("mandatory.party.code"))));
        if (partyType.getDescription() == null || "".equals(partyType.getDescription()))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "party.desc.mandatory", getText("mandatory.party.description"))));
        if (partyType.getCode() != null)
            if (getCheckCode())
                throw new ValidationException(Arrays.asList(new ValidationError(
                        "party.code.unique", getText("party.code.unique"))));
    }

    @SkipValidation
    public boolean getCheckCode() {
        EgPartytype pt = null;
        boolean isDuplicate = false;
        if (!partyType.getCode().equals("") && partyType.getId() != null)
            pt = (EgPartytype) persistenceService.find("from EgPartytype where code=? and id!=?",
                    partyType.getCode(), partyType.getId());
        else if (!partyType.getCode().equals(""))
            pt = (EgPartytype) persistenceService.find("from EgPartytype where code=?",
                    partyType.getCode());
        if (pt != null)
            isDuplicate = true;
        return isDuplicate;
    }

    public EgPartytype getPartyType() {
        return partyType;
    }

    public void setPartyType(final EgPartytype partyType) {
        this.partyType = partyType;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(final boolean close) {
        this.close = close;
    }

    public String getShowMode() {
        return showMode;
    }

    public void setShowMode(final String showMode) {
        this.showMode = showMode;
    }

    public List<EgPartytype> getPartyTypeList() {
        return partyTypeList;
    }

    public void setPartyTypeList(final List<EgPartytype> partyTypeList) {
        this.partyTypeList = partyTypeList;
    }

    public List<EgPartytype> getPartySearchList() {
        return partySearchList;
    }

    public void setPartySearchList(final List<EgPartytype> partySearchList) {
        this.partySearchList = partySearchList;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(final String success) {
        this.success = success;
    }

}