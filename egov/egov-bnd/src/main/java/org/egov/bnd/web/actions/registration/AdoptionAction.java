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
package org.egov.bnd.web.actions.registration;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bnd.model.BirthRegistration;
import org.egov.bnd.utils.BndConstants;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infstr.utils.DateUtils;
import org.springframework.transaction.annotation.Transactional;

@Validations(requiredStrings = {
        @RequiredStringValidator(fieldName = "adoptionDetail.adopteeMother.firstName", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "adoptionDetail.adopteeFather.firstName", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "adoptionDetail.adoptionNumber", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "adoptionDetail.affidavitNumber", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "adoptionDetail.adopteeAddress.streetRoadLine", message = "", key = BndConstants.REQUIRED) }, emails = {
        @EmailValidator(fieldName = "adoptionDetail.adopteeFather.emailAddress", key = BndConstants.INVALID, message = "", type = ValidatorType.FIELD),
        @EmailValidator(fieldName = "adoptionDetail.adopteeMother.emailAddress", key = BndConstants.INVALID, message = "", type = ValidatorType.FIELD) })
@Namespace("/registration")
@ParentPackage("egov")
@Transactional(readOnly = true)
public class AdoptionAction extends BirthRegistrationAction {

    private static final long serialVersionUID = -7482182954767992184L;
    private final Logger LOGGER = Logger.getLogger(AdoptionAction.class);
    private BirthRegistration birthRegistrationClone;

    @Override
    public BirthRegistration getModel() {
        return birthRegistration;
    }

    @Override
    public void prepareNewform() {
        prepareView();
    }

    @Override
    @SkipValidation
    @Action(value = "/adoption-newform", results = { @Result(name = NEW, type = "dispatcher") })
    public String newform() {
        LOGGER.debug("Load Adoption new form");
        return NEW;
    }

    @Override
    @Transactional
    public void prepareCreate() {
        LOGGER.debug("Start prepare create method");
        birthRegistrationClone = birthRegistrationService.getBirthRegistrationById(getIdTemp());
        if (birthRegistration.getAdoptionDetail().getAdoptionInstituteId() != null
                && birthRegistration.getAdoptionDetail().getAdoptionInstituteId() != -1)
            birthRegistration.getAdoptionDetail().setAdoptionInstitute(
                    bndCommonService.getAdoptionInstituteById(birthRegistration.getAdoptionDetail()
                            .getAdoptionInstituteId()));
        LOGGER.debug("End prepare create method");
    }

    @Override
    public void prepare() {
        super.prepare();
    }

    @Override
    @Transactional
    @Action(value = "/adoption-create", results = { @Result(name = NEW, type = "dispatcher") })
    public String create() {
        LOGGER.debug("Start create method");
        birthRegistration.getAdoptionDetail().getAdopteeFather().setGender(Gender.MALE);
        birthRegistration.getAdoptionDetail().getAdopteeMother().setGender(Gender.FEMALE);
        birthRegistrationClone.setIsChildAdopted(Boolean.TRUE);
        birthRegistrationClone.setAdoptionDetail(birthRegistrationService.saveAdoption(birthRegistration
                .getAdoptionDetail()));
        birthRegistrationService.update(birthRegistrationClone);
        birthRegistration = birthRegistrationClone.clone();
        setMode(VIEW);
        LOGGER.debug("End create method");
        return NEW;
    }

    @Override
    public void validate() {
        if (birthRegistration.getAdoptionDetail() != null
                && birthRegistration.getAdoptionDetail().getAdoptionDate() != null) {

            if (birthRegistration.getDateOfEvent() != null
                    && birthRegistration.getAdoptionDetail().getAdoptionDate()
                    .before(birthRegistration.getDateOfEvent()))
                addActionError(getMessage("adoption.date.birthdate.validate"));

            if (birthRegistration.getAdoptionDetail().getAdoptionDate().after(DateUtils.today()))
                addActionError(getMessage("adoptiondate.today.validate"));
        }
    }

}
