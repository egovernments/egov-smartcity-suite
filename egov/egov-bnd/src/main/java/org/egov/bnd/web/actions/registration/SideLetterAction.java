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

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bnd.client.utils.BndRuleBook;
import org.egov.bnd.model.BirthRegistration;
import org.egov.bnd.model.CRelation;
import org.egov.bnd.model.RegistrationUnit;
import org.egov.bnd.model.SideLetter;
import org.egov.bnd.services.common.NumberGenerationService;
import org.egov.bnd.services.registration.BirthRegistrationService;
import org.egov.bnd.services.registration.SideLetterService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.utils.BndDateUtils;
import org.egov.bnd.web.actions.common.BndCommonAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.workflow.entity.StateAware;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Validations(

        requiredStrings = {
                @RequiredStringValidator(fieldName = "applicantName", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "citizenName", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "applicantAddress", type = ValidatorType.FIELD, message = "Required", key = "") },

                requiredFields = { @RequiredFieldValidator(fieldName = "applicationDate", type = ValidatorType.FIELD, message = "Required", key = "") })

@ParentPackage("egov")
@Namespace("/registration")
@Transactional(readOnly = true)
public class SideLetterAction extends BndCommonAction {

    private static final long serialVersionUID = 6279292161404254681L;
    private static final String STRUTS_RESULT_PRINT = "print";
    private SideLetter sideLetter = new SideLetter();
    private static final Logger LOGGER = Logger.getLogger(SideLetterAction.class);
    private SideLetterService sideLetterService;
    private NumberGenerationService numberGenerationService;
    private BirthRegistrationService birthRegistrationService;
    private final String[] relations = { "SON", "WIFE", "DAUGHTER" };
    private String formattedDateOfEvent;
    private Long birthId;
    private String sideLetterType;
    private String citizenNameAsPerRecord;

    public String getCitizenNameAsPerRecord() {
        return citizenNameAsPerRecord;
    }

    public void setCitizenNameAsPerRecord(final String citizenNameAsPerRecord) {
        this.citizenNameAsPerRecord = citizenNameAsPerRecord;
    }

    public SideLetterAction() {
        addRelatedEntity("applicantRelationType", CRelation.class);
        addRelatedEntity("modifiedBy", User.class);
        addRelatedEntity("createdBy", User.class);
        addRelatedEntity("birthReportId", BirthRegistration.class);
    }

    @Override
    public StateAware getModel() {
        return sideLetter;
    }

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("relationList", bndCommonService.getRelationTypesbyConstant(relations));
    }

    @Override
    @Transactional    
    public void prepareNewform() {
        LOGGER.debug("To Prepare a new form method");
        final RegistrationUnit regUnit = bndCommonService.getRegistrarByLoggedInUser().getRegUnitId();

        if (regUnit != null)
             if (regUnit.getRegUnitAddress() != null &&
             regUnit.getRegUnitAddress().getSubdistrict() != null)
             sideLetter.setTalukName(regUnit.getRegUnitAddress() != null ?
             regUnit.getRegUnitAddress().getSubdistrict()
             : "");
            
            if (regUnit.getRegUnitAddress() != null && regUnit.getRegUnitAddress().getDistrict() != null)
                sideLetter.setDistrictName(regUnit.getRegUnitAddress() != null ? regUnit.getRegUnitAddress()
                        .getDistrict() : "");

    }

    @Override
    @Transactional
    @SkipValidation
    @Action(value = "/sideLetter-newform", results = { @Result(name = NEW, type = "dispatcher") })
    public String newform() {
        LOGGER.debug("Inside new form method");
        final RegistrationUnit regUnit = bndCommonService.getRegistrarByLoggedInUser().getRegUnitId();
        final BirthRegistration birthreg = birthRegistrationService.getBirthRegistrationById(birthId);

        if (birthreg != null && birthreg.getStatus().getCode().equals(BndConstants.APPROVED)) {
            sideLetter.setBirthReportId(birthreg);
            final List<String> roleList = bndCommonService.getRoleNamesByPassingUserId(Long.valueOf(EgovThreadLocals
                    .getUserId()));
            if (roleList != null)
                if (roleList.contains(BndRuleBook.SUPERUSER) || regUnit != null
                && regUnit.getIsmainregunit().equals(Boolean.TRUE))
                    if (!BndDateUtils.isSideLetter(birthreg.getDateOfEvent()))
                        throw new EGOVRuntimeException(getMessage("sideletter.error"));
        } else
            throw new EGOVRuntimeException(getMessage("birthreg.error"));
        LOGGER.debug("Completed new form method");
        return NEW;

    }

    @Override
    @Transactional
    protected void saveOrUpdate() {
        LOGGER.debug("Started saveOrUpdate method");
        sideLetter.setEventType(BndConstants.BIRTH);
        sideLetter.setDateOfissue(new Date());
        if (sideLetter.getReferenceNumber() == null || "".equals(sideLetter.getReferenceNumber().trim()))
            sideLetter.setReferenceNumber(numberGenerationService.getSideLetterRefNumber(sideLetter));
        sideLetterService.save(sideLetter, null);
        setMode(VIEW);
        addDropdownData("relationList", bndCommonService.getRelationTypesbyConstant(relations));
        LOGGER.debug("Completed saveOrUpdate method");

    }

    @Override
    @Transactional
    public void prepareView() {
        LOGGER.debug("Started prepareView method");
        sideLetter = sideLetterService.getSidLetterId(idTemp);
        addDropdownData("relationList", bndCommonService.getRelationTypesbyConstant(relations));
        LOGGER.debug("Completed prepareView method");
    }

    @Override
    @Transactional
    public void prepareBeforeEdit() {
        LOGGER.debug("Started prepareBeforeEdit method");
        sideLetter = sideLetterService.getSidLetterId(idTemp);
        addDropdownData("relationList", bndCommonService.getRelationTypesbyConstant(relations));
        LOGGER.debug("Completed prepareBeforeEdit method");
    }

    @SkipValidation
    @Action(value = "/sideLetter-printSideLetter", results = { @Result(name = STRUTS_RESULT_PRINT, type = "dispatcher") })
    public String printSideLetter() {
        prepareBeforeEdit();

        // setFormattedDateOfEvent(DateUtils.formatDateInWords(sideLetter
        // .getBirthReportId().getDateOfEvent()));
        setSideLetterType(sideLetterType);
        final String firstName = sideLetter.getBirthReportId().getCitizen().getName();

        if (getSideLetterType().equals("withregistrationdetails")) {
            if (firstName.equalsIgnoreCase("NA"))
                setCitizenNameAsPerRecord("*********");
            else
                setCitizenNameAsPerRecord(sideLetter.getBirthReportId().getCitizenName().toString());
        } else if (sideLetter.getApplicantRelationType().getRelatedAsConst().equalsIgnoreCase("Son"))
            sideLetter.getApplicantRelationType().setDesc("S/O");
        else if (sideLetter.getApplicantRelationType().getRelatedAsConst().equalsIgnoreCase("Daughter"))
            sideLetter.getApplicantRelationType().setDesc("D/O");
        else
            sideLetter.getApplicantRelationType().setDesc("W/O");

        return STRUTS_RESULT_PRINT;
    }

    @Override
    protected String getMessage(final String text) {
        return getText(text);
    }

    public Long getBirthId() {
        return birthId;
    }

    public void setBirthId(final Long birthId) {
        this.birthId = birthId;
    }

    public String getSideLetterType() {
        return sideLetterType;
    }

    public void setSideLetterType(final String sideLetterType) {
        this.sideLetterType = sideLetterType;
    }

    public void setSideLetterService(final SideLetterService sideLetterService) {
        this.sideLetterService = sideLetterService;
    }

    public void setNumberGenerationService(final NumberGenerationService numberGenerationService) {
        this.numberGenerationService = numberGenerationService;
    }

    public SideLetter getSideLetter() {
        return sideLetter;
    }

    public void setSideLetter(final SideLetter sideLetter) {
        this.sideLetter = sideLetter;
    }

    public BirthRegistrationService getBirthRegistrationService() {
        return birthRegistrationService;
    }

    public void setBirthRegistrationService(final BirthRegistrationService birthRegistrationService) {
        this.birthRegistrationService = birthRegistrationService;
    }

    public String getFormattedDateOfEvent() {
        return formattedDateOfEvent;
    }

    public void setFormattedDateOfEvent(final String formattedDateOfEvent) {
        this.formattedDateOfEvent = formattedDateOfEvent;
    }

}
