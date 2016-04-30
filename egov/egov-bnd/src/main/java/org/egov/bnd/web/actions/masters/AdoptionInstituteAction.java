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
package org.egov.bnd.web.actions.masters;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bnd.model.AdoptionInstitute;
import org.egov.bnd.services.common.BndCommonService;
import org.egov.bnd.services.masters.AdoptionInstituteService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.web.actions.common.BndCommonAction;
import org.springframework.transaction.annotation.Transactional;

@Validations(requiredFields = {
        @RequiredFieldValidator(fieldName = "institutionCode", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "institutionName", message = "", key = BndConstants.REQUIRED) })
@Namespace("/masters")
@ParentPackage("egov")
@Transactional(readOnly = true)
public class AdoptionInstituteAction extends BndCommonAction {

    private static final long serialVersionUID = 9100077427657947811L;
    AdoptionInstitute adoptionInstitute = new AdoptionInstitute();
    private AdoptionInstituteService adoptionInstituteService;
    private BndCommonService bndCommonService;

    @Override
    public String getMode() {
        return mode;
    }

    @Override
    public void setMode(final String mode) {
        this.mode = mode;
    }

    public AdoptionInstituteAction() {
    }

    public AdoptionInstituteService getAdoptionInstituteService() {
        return adoptionInstituteService;
    }

    public void setAdoptionInstituteService(final AdoptionInstituteService adoptionInstituteService) {
        this.adoptionInstituteService = adoptionInstituteService;
    }

    public BndCommonService getBndCommonService() {
        return bndCommonService;
    }

    @Override
    public void setBndCommonService(final BndCommonService bndCommonService) {
        this.bndCommonService = bndCommonService;
    }

    @Override
    public void prepare() {
        super.prepare();

    }

    @Override
    public AdoptionInstitute getModel() {
        return adoptionInstitute;
    }

    @Override
    @SkipValidation
    @Action(value = "/adoptionInstitute-newform", results = { @Result(name = NEW, type = "dispatcher") })
    public String newform() {
        return NEW;
    }

    @Override
    @Transactional
    @Action(value = "/adoptionInstitute-create", results = { @Result(name = NEW, type = "dispatcher") })
    public String create() {
        adoptionInstituteService.save(adoptionInstitute);
        mode = VIEW;
        return NEW;
    }

    @Override
    protected String getMessage(final String key) {
        return getText(key);
    }

    @Override
    @Transactional
    public void validate() {
        if (adoptionInstitute.getInstitutionCode() != null && !"".equals(adoptionInstitute.getInstitutionCode())) {
            final boolean isCodeAlreadyExist = adoptionInstituteService
                    .checkUniqueAdoptionInstituteCode(adoptionInstitute.getInstitutionCode());
            if (isCodeAlreadyExist)
                addFieldError("codeAlreadyExist", getMessage("adoption.institutionCode.alreadyExist"));
        }
        if (adoptionInstitute.getInstitutionName() != null && !"".equals(adoptionInstitute.getInstitutionName())) {
            final boolean isNameAlreadyExist = adoptionInstituteService
                    .checkUniqueAdoptionInstituteName(adoptionInstitute.getInstitutionName());
            if (isNameAlreadyExist)
                addFieldError("codeAlreadyExist", getMessage("adoption.institutionName.alreadyExist"));
        }
    }

}
