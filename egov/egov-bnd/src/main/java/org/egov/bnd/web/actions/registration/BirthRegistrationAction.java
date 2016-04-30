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
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bnd.client.utils.BndRuleBook;
import org.egov.bnd.model.*;
import org.egov.bnd.services.reports.PaymentReportService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.utils.BndDateUtils;
import org.egov.bnd.utils.BndUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.enums.AddressType;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Validations(requiredFields = {
        @RequiredFieldValidator(fieldName = "registrationDate", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "dateOfEvent", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "citizen.sex", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "placeTypeTemp", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "permanentCitizenAddress.state", message = BndConstants.REQUIRED, key = ""),
        @RequiredFieldValidator(fieldName = "informantAddress.state", message = BndConstants.REQUIRED, key = ""),
        @RequiredFieldValidator(fieldName = "eventAddress.state", message = BndConstants.REQUIRED, key = ""),
        @RequiredFieldValidator(fieldName = "parentAddress.state", message = BndConstants.REQUIRED, key = "") }, requiredStrings = {
        @RequiredStringValidator(fieldName = "citizen.firstName", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "father.firstName", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "mother.firstName", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "eventAddress.streetRoadLine", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "eventAddress.cityTownVillage", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "eventAddress.district", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "informantCitizen.firstName", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "informantAddress.streetRoadLine", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "informantAddress.cityTownVillage", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "informantAddress.district", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "permanentCitizenAddress.streetRoadLine", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "permanentCitizenAddress.cityTownVillage", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "permanentCitizenAddress.district", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "parentAddress.streetRoadLine", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "parentAddress.cityTownVillage", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "parentAddress.district", message = "", key = BndConstants.REQUIRED) }, emails = {
        @EmailValidator(fieldName = "mother.emailAddress", key = BndConstants.INVALID, message = "", type = ValidatorType.FIELD),
        @EmailValidator(fieldName = "father.emailAddress", key = BndConstants.INVALID, message = "", type = ValidatorType.FIELD) })
//@Namespace("/registration")
@ParentPackage("egov")
@Transactional
public class BirthRegistrationAction extends RegistrationAction {

    private static final long serialVersionUID = 849449050374228871L;
    private static final Logger LOGGER = Logger.getLogger(BirthRegistrationAction.class);
    protected BirthRegistration birthRegistration = new BirthRegistration();
    private Boolean updateNameFlag = Boolean.FALSE;

    public static final String PRINTBIRTH = "printbirth";
    private InputStream birthCertificatePDF;

    private Boolean receiptNoFlag = Boolean.TRUE;

    public void setPaymentReportService(final PaymentReportService paymentReportService) {
    }

    public Boolean getReceiptNoFlag() {
        return receiptNoFlag;
    }

    public void setReceiptNoFlag(final Boolean receiptNoFlag) {
        this.receiptNoFlag = receiptNoFlag;
    }

    public Boolean getUpdateNameFlag() {
        return updateNameFlag;
    }

    public void setUpdateNameFlag(final Boolean updateNameFlag) {
        this.updateNameFlag = updateNameFlag;
    }

    public BirthRegistrationAction() {
        addRelatedEntity("registrationUnit", RegistrationUnit.class);
        addRelatedEntity("registrarId", Registrar.class);
        addRelatedEntity("status", EgwStatus.class);
        addRelatedEntity("modifiedBy", User.class);
        addRelatedEntity("createdBy", User.class);
        addRelatedEntity("citizenBDDetails", CitizenBDDetails.class);
        addRelatedEntity("citizenBDDetails.religion", Religion.class);
        addRelatedEntity("citizenBDDetails.fatherEducation", Education.class);
        addRelatedEntity("citizenBDDetails.motherEducation", Education.class);
        addRelatedEntity("citizenBDDetails.fatherOccupation", Occupation.class);
        addRelatedEntity("citizenBDDetails.motherOccupation", Occupation.class);
        addRelatedEntity("typeAttention", AttentionType.class);
        addRelatedEntity("methodDelivery", DeliveryMethod.class);
        addRelatedEntity("establishment", Establishment.class);
        addRelatedEntity("placeType", PlaceType.class);
        addRelatedEntity("adoptionDetail", AdoptionDetails.class);
        addRelatedEntity("citizen.newcitizenname", BndNameChange.class);

    }

    @Override
    public BirthRegistration getModel() {
        return birthRegistration;
    }

    @Override
    public void prepare() {
        super.prepare();
        buildHospitalList();
        setDifferentActionsForRole();
    }

    private void buildHospitalList() {
        if (birthRegistration.getEstablishment() != null && birthRegistration.getRegistrarId().getRegUnitId() != null)
            addDropdownData("hospitalList", bndCommonService.getHospitalByTypeAndUnit(birthRegistration
                    .getEstablishment().getType().getId(), birthRegistration.getRegistrarId().getRegUnitId().getId()));
        else
            addDropdownData("hospitalList", Collections.EMPTY_LIST);
    }

    @Override
    public void prepareNewform() {
        buildPrepareNewForm();
    }

    public void prepareNewOfflineForm() {
        buildPrepareNewForm();
    }

    @Transactional
    private void buildPrepareNewForm() {
        birthRegistration.setRegistrarId(bndCommonService.getRegistrarByLoggedInUser());
        if (birthRegistration.getRegistrarId() != null)
            birthRegistration.setRegistrationUnit(birthRegistration.getRegistrarId().getRegUnitId());
    }

    @Override
    @SkipValidation
    @Transactional    
    @Action(value = "/registration/birthRegistration-newform", results = { @Result(name = NEW, location = "/WEB-INF/jsp/registration/birthRegistration-new.jsp") })
    public String newform() {
        LOGGER.info("New Birth Registration Online form");
        birthRegistration.setRegistrationDate(DateUtils.today());
        buildNewBirthForm(birthRegistration);
        registrationMode = "online";
        return NEW;
    }

    @SkipValidation
    @Transactional
    @Action(value = "/birthRegistration-newOfflineForm", results = { @Result(name = NEW, type = "dispatcher") })
    public String newOfflineForm() {
        LOGGER.debug("New Birth Registration Offline form");
        buildNewBirthForm(birthRegistration);
        registrationMode = OFFLINE;
        mode = OFFLINE;
        return NEW;
    }

    @Override
    public void prepareCreate() {
        getDetailsAddressTypeAndRelation();
    }

    @Override
    @Transactional
    @Action(value = "/birthRegistration-create", results = { @Result(name = NEW, type = "dispatcher") })
    public String create() {
        if (workFlowType != null && !"".equals(workFlowType) && BndConstants.SCRIPT_SAVE.equals(workFlowType))
            birthRegistration.setStatus(bndCommonService.getStatusByModuleAndCode(BndConstants.BIRTHREGISTRATION,
                    BndConstants.CREATED));
        birthRegistration.setPlaceType(bndCommonService.getPlaceType(placeTypeTemp));
        saveOrUpdate();
        mode = VIEW;
        return NEW;
    }

    /**
     * This method is called to save birthRegistration for create as well as
     * modify mode
     */

    @Override
    @Transactional
    protected void saveOrUpdate() {
        birthRegistrationService.buildAdoptionDetial(birthRegistration);
        buildRegistration();
        if (birthRegistration.getRegistrationNo() == null || "".equals(birthRegistration.getRegistrationNo().trim()))
            birthRegistration.setRegistrationNo(numberGenerationService.getBirthRegistrationNumber(birthRegistration,
                    birthHospitalUserFlag));
        // Setting additional rule for work flow
        birthRegistration.setAdditionalRule(getAdditionalRule());
        birthRegistrationService.save(birthRegistration, workFlowType);
        birthRegistration.setPlaceType(bndCommonService.getPlaceType(placeTypeTemp));
        LOGGER.debug("Birth Registration Record ::::::::::" + birthRegistration);
        mode = VIEW;
    }

    /**
     * This method will build citizen object with address details
     */

    private void buildCitizenAddress() {
        LOGGER.debug("Start Build citizen Address");
        if (permanentAddressFlag != null && permanentAddressFlag == 1) {
            birthRegistration.getPermanentCitizenAddress().setType(AddressType.PERMANENT);
            buildNewAddressFromOldAddress(citizenAddressSet, birthRegistration.getPermanentCitizenAddress());
        } else
            birthRegistration.setPermanentCitizenAddress(null);

        if (placeTypeTemp != null && BndConstants.NOTSTATED.equals(placeTypeTemp))
            birthRegistration.setEventAddress(null);
        else
            
            birthRegistration.getEventAddress().setType(AddressType.EVENTADDRESS);

            if (parentAddressFlag != null && parentAddressFlag == 1) {
                birthRegistration.getParentAddress().setType(AddressType.CORRESPONDENCE);
                buildNewAddressFromOldAddress(fatherAddressSet, birthRegistration.getParentAddress());
                buildNewAddressFromOldAddress(motherAddressSet, birthRegistration.getParentAddress());
            } else
                birthRegistration.setParentAddress(null);

       
         birthRegistration.getMotherResidenceAddress().setType(AddressType.USUALADDRESS);
        buildNewAddressFromOldAddress(motherAddressSet, birthRegistration.getMotherResidenceAddress());
        birthRegistration.getInformantAddress().setType(AddressType.PRESENTADDRESS);
        LOGGER.debug("End Build citizen Address");
    }

    private void buildRegistration() {
        birthRegistration.setIsCitizenKnown(nameOfchildFlag == 1 ? Boolean.TRUE : Boolean.FALSE);
        buildCitizenAddress();
        buildCitizenRelation(birthRegistration);
        buildInfomantDetails(birthRegistration);
        buildAddress(birthRegistration);
    }

    @Override
    public void validate() {
        if (birthRegistration.getDateOfEvent() != null && birthRegistration.getDateOfEvent().after(DateUtils.today()))
            addActionError(getMessage("dateOfEvent.today.validate"));

        if (birthRegistration.getRegistrationDate() != null && birthRegistration.getDateOfEvent() != null
                && birthRegistration.getDateOfEvent().after(birthRegistration.getRegistrationDate()))
            addActionError(getMessage("dateOfEvent.registration.validate"));

        if (placeTypeTemp != null && !EMPTY.equals(placeTypeTemp)
                && BndConstants.HOSPTIAL.equalsIgnoreCase(placeTypeTemp)
                && birthRegistration.getEstablishment() == null)
            addActionError(getMessage("hospitalname.required"));

        if (placeTypeTemp != null && !EMPTY.equals(placeTypeTemp)
                && !BndConstants.HOSPTIAL.equalsIgnoreCase(placeTypeTemp)
                && (informantFlag == null || "".equals(informantFlag)))
            addActionError(getMessage("informat.relations.child.required"));

        // validating date of adoption date against today's date and date of
        // event .
        if (birthRegistration.getIsChildAdopted() != null && birthRegistration.getIsChildAdopted())
            if (birthRegistration.getAdoptionDetail() != null
            && birthRegistration.getAdoptionDetail().getAdoptionDate() != null) {

                if (birthRegistration.getDateOfEvent() != null
                        && birthRegistration.getAdoptionDetail().getAdoptionDate()
                        .before(birthRegistration.getDateOfEvent()))
                    addActionError(getMessage("adoption.date.birthdate.validate"));

                if (birthRegistration.getAdoptionDetail().getAdoptionDate().after(DateUtils.today()))
                    addActionError(getMessage("adoptiondate.today.validate"));
            }

        // For Offline mode validating registration number....
        if (OFFLINE.equals(mode) && birthRegistration.getRegistrationNo() != null
                && !EMPTY.equals(birthRegistration.getRegistrationNo().trim()))
            if (birthRegistration.getRegistrationUnit() != null
            && birthRegistrationService.checkUniqueRegistrationNumber(
                    birthRegistration.getRegistrationUnit().getId(),
                    birthRegistration.getId(),
                    birthRegistration.getRegistrationNo(),
                    BndConstants.REGISTRATIONDATE.equalsIgnoreCase(numberGenKey) ? birthRegistration
                            .getRegistrationDate() : birthRegistration.getDateOfEvent(), BndConstants.BIRTH))
                addActionError(getMessage("registration.number.exists"));

            else if (BndUtils.isNum(birthRegistration.getRegistrationNo().trim())) {
                final int eventYear = BndConstants.REGISTRATIONDATE.equalsIgnoreCase(numberGenKey) ? BndDateUtils
                        .getCurrentYear(birthRegistration.getRegistrationDate()) : BndDateUtils
                        .getCurrentYear(birthRegistration.getDateOfEvent());
                        final RegKeys regNumRange = regKeyService.getRegKeyByRegUnitAndDate(
                                birthRegistration.getRegistrationUnit(), eventYear,
                                numberGenerationService.buildObjectType(birthRegistration, eventYear, BndConstants.BIRTH));
                        if (regNumRange != null) {
                            final Integer regNumber = Integer.valueOf(birthRegistration.getRegistrationNo());
                            if (regNumber >= regNumRange.getMinValue())
                                addActionError(getMessage("regNumber.minvalue.validate").concat(" ").concat(
                                        regNumRange.getMinValue().toString()));
                        }
            }

    }

    @Override
    public void prepareView() {
        beforeViewAndModify();
    }

    /**
     * This method builds birthRegistration object for view and beforeEdit
     */

    private void beforeViewAndModify() {
        birthRegistration = birthRegistrationService.getBirthRegistrationById(idTemp);
        birthRegistration.setFather(birthRegistration.getCitizen().getRelatedPerson(BndConstants.FATHER.toUpperCase()));
        birthRegistration.setMother(birthRegistration.getCitizen().getRelatedPerson(BndConstants.MOTHER.toUpperCase()));
        birthRegistration.setPermanentCitizenAddress(birthRegistration.getCitizen().getRelatedAddress(
                BndConstants.PERMANENTADDRESS));
        permanentAddressFlag = birthRegistration.getPermanentCitizenAddress() == null ? 0 : 1;
        birthRegistration.setParentAddress(birthRegistration.getFather().getRelatedAddress(
                BndConstants.CORRESPONDINGADDRESS));
        parentAddressFlag = birthRegistration.getParentAddress() == null ? 0 : 1;

        if (birthRegistration.getInformant() != null) {
            final CRelation relation = birthRegistration.getCitizen().getRelation(birthRegistration.getInformant());
            if (relation != null)
                informantFlag = relation.getDesc();
            birthRegistration.setInformantAddress(birthRegistration.getInformant().getRelatedAddress(
                    BndConstants.PRESENTADDRESS));
            informantCitizen = birthRegistration.getInformant();
        }

        birthRegistration.setMotherResidenceAddress(birthRegistration.getMother().getRelatedAddress(
                BndConstants.USUALADDRESS));
        placeTypeTemp = birthRegistration.getPlaceType().getDesc();
        nameOfchildFlag = birthRegistration.getIsCitizenKnown() ? 1 : 0;
    }

    /**
     * This method is to prepare edit method
     */

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public void prepareEdit() {
        birthRegistration = birthRegistrationService.getBirthRegistrationById(idTemp);
        setUpRegistrationDetailsForValidation(birthRegistration);
        // birthRegistration =
        // birthRegistrationService.merge(birthRegistration);
        fatherAddressSet = (Set<Address>) birthRegistration.getCitizen()
                .getRelatedPerson(BndConstants.FATHER.toUpperCase()).getAddress();
        motherAddressSet = (Set<Address>) birthRegistration.getCitizen()
                .getRelatedPerson(BndConstants.MOTHER.toUpperCase()).getAddress();
        informantAddressSet.add(birthRegistration.getInformant().getRelatedAddress(BndConstants.PRESENTADDRESS));
        citizenAddressSet = (Set<Address>) birthRegistration.getCitizen().getAddress();
        oldRelations = birthRegistration.getCitizen().getRelations();
        fatherAddressSet.removeAll(informantAddressSet);
        motherAddressSet.removeAll(informantAddressSet);
        getDetailsAddressTypeAndRelation();

    }

    @Override
    @SkipValidation
    @Transactional
    @Action(value = "/birthRegistration-beforeEdit", results = { @Result(name = NEW, type = "dispatcher") })
    public String beforeEdit() {
        if (mode != null) {
            mode = getMode();
            if (NAMEINCLUSION.equals(getMode())) {
                if (birthRegistration.getCitizen().getName().equalsIgnoreCase("NA")
                        && birthRegistrationService.issueFreeCertificate(birthRegistration.getId().longValue(),
                                getRoleNameByLoginUserId()))
                    setReceiptNoFlag(Boolean.FALSE);
                if (!bndCommonService.getAppconfigValue(BndConstants.BNDMODULE, BndConstants.UPDATENAME, "0"))
                    setUpdateNameFlag(Boolean.FALSE);
                else
                    setUpdateNameFlag(Boolean.TRUE);
            }

        } else
            mode = EDIT;

        return NEW;
    }

    /**
     * This method is called to update birth registration
     */

    @Override
    @Transactional
    @ValidationErrorPage(NEW)
    @Action(value = "/birthRegistration-edit", results = { @Result(name = NEW, type = "dispatcher") })
    public String edit() {

        if (getMode().equals(LOCK)) {
            birthRegistrationService.buildAdoptionDetial(birthRegistration);
            buildRegistration();
            birthRegistration.setPlaceType(bndCommonService.getPlaceType(placeTypeTemp));
            birthRegistration.setStatus(bndCommonService.getStatusByModuleAndCode(BndConstants.BIRTHREGISTRATION,
                    BndConstants.LOCK));
            birthRegistrationService.save(birthRegistration, workFlowType);
        } else if (getMode().equals(UNLOCK)) {
            birthRegistrationService.buildAdoptionDetial(birthRegistration);
            buildRegistration();
            birthRegistration.setPlaceType(bndCommonService.getPlaceType(placeTypeTemp));
            birthRegistration.setStatus(bndCommonService.getStatusByModuleAndCode(BndConstants.BIRTHREGISTRATION,
                    BndConstants.APPROVED));
            birthRegistrationService.save(birthRegistration, workFlowType);
        } else if (getMode().equals(NAMEINCLUSION)) {
            birthRegistrationService.buildAdoptionDetial(birthRegistration);
            buildRegistration();
            birthRegistration.setPlaceType(bndCommonService.getPlaceType(placeTypeTemp));
            birthRegistration.getNameChange().setCitizen(birthRegistration.getCitizen());
            // TODO egifix
            /*
             * birthRegistration.getNameChange().setOldfirstname(birthRegistration
             * .getCitizen().getFirstName());
             * birthRegistration.getNameChange().setOldmiddlename
             * (birthRegistration.getCitizen().getMiddleName());
             * birthRegistration
             * .getNameChange().setOldlastname(birthRegistration
             * .getCitizen().getLastName());
             */
            birthRegistration.getNameChange().setLastModifiedBy(
                    bndCommonService.getUserByPassingUserId(EgovThreadLocals.getUserId()));
            birthRegistration.getNameChange().setLastUpatedTimestamp(new Date());
            // TODO egifix
            /*
             * birthRegistration.getCitizen().setFirstName(birthRegistration.
             * getNameChange().getFirstName());
             * birthRegistration.getCitizen().setMiddleName
             * (birthRegistration.getNameChange().getMiddleName());
             * birthRegistration
             * .getCitizen().setLastName(birthRegistration.getNameChange
             * ().getLastName());
             */
            birthRegistration.setIsCitizenKnown(Boolean.TRUE);

            birthRegistration.getCitizen().getNewcitizenname().add(birthRegistration.getNameChange());
            if (getNameOfchildFlag() == 0)
                setNameOfchildFlag(1);
            birthRegistrationService.save(birthRegistration, workFlowType);

        } else {
            saveOrUpdate();
            if (birthRegistration.getStatus() != null
                    && BndConstants.APPROVED.equalsIgnoreCase(birthRegistration.getStatus().getCode())
                    && birthRegistration.getRegistrationNo().contains(BndConstants.HOSPITALBIRTHSUFFIX)) {
                birthRegistration.setHospitalRegistrationNo(birthRegistration.getRegistrationNo());
                numberGenerationService.reGenerateBirthRegistrationNumber(birthRegistration);
            }
            if (remarksHistory != null && !"".equals(remarksHistory))
                bndCommonService.saveHistory(birthRegistration, birthRegistration.getClass().getSimpleName(),
                        remarksHistory);
        }
        mode = VIEW;
        return NEW;
    }

    @Override
    public void prepareBeforeEdit() {
        beforeViewAndModify();
        setUpRegistrationDetailsForValidation(birthRegistration);
    }

    @Override
    public void prepareInbox() {
        beforeViewAndModify();
        setUpRegistrationDetailsForValidation(birthRegistration);
    }

    /**
     * Required for workflow
     */

    @Override
    protected String getPendingActions() {
        return birthRegistration == null ? "" : birthRegistration.getCurrentState() == null ? "" : birthRegistration
                .getCurrentState().getNextAction();
    }

    /**
     * Required for workflow
     */

    @Override
    @Transactional
    public String getAdditionalRule() {
        final Long userId = birthRegistration.getCreatedBy() == null ? Long.valueOf(EgovThreadLocals.getUserId())
                : birthRegistration.getCreatedBy().getId();
        final List<String> roleList = bndCommonService.getRoleNamesByPassingUserId(userId);
        return BndRuleBook.HOSPITALUSER.equalsIgnoreCase(BndRuleBook.getInstance().getHighestPrivilegedRole(roleList)) ? BndRuleBook.HOSPITALUSER
                : "ALL";

    }

    /**
     * Required for workflow
     */

    @Override
    public String getNextAction() {
        WorkFlowMatrix wfMatrix = null;
        if (getModel().getId() != null)
            if (getModel().getCurrentState() != null)
                wfMatrix = customizedWorkFlowService.getWfMatrix(getModel().getStateType(), getWorkFlowDepartment(),
                        getAmountRule(), getAdditionalRule(), getModel().getCurrentState().getValue(),
                        getPendingActions());
        // else
        /*
         * wfMatrix =
         * customizedWorkFlowService.getWfMatrix(getModel().getStateType(),
         * getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
         * State.NEW, getPendingActions());
         */
        // else
        // TODO egifix-state
        /*
         * wfMatrix =
         * customizedWorkFlowService.getWfMatrix(getModel().getStateType(),
         * getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
         * State.NEW, getPendingActions());
         */
        return wfMatrix == null ? "" : wfMatrix.getNextAction();

    }

    @SkipValidation
    public String print() {

        reportId = generateCertificateService.generateCertificate(idTemp, BndConstants.SEARCHBIRTH,
                getRoleNameByLoginUserId(), BndConstants.BIRTHREGISTRATION_TEMPLATE, getSession());
        /*
         * ReportOutput reportOutput =
         * generateCertificateService.generateCertificateBirthDeathNA(idTemp,
         * BndConstants.SEARCHBIRTH,getRoleNameByLoginUserId(),
         * BndConstants.BIRTHREGISTRATION_TEMPLATE, getSession()); if
         * (reportOutput != null && reportOutput.getReportOutputData() != null)
         * birthCertificatePDF = new
         * ByteArrayInputStream(reportOutput.getReportOutputData()); return
         * PRINTBIRTH;
         */

        /*
         * List<BirthRegistration> list=new ArrayList<BirthRegistration>();
         * birthRegistration =
         * birthRegistrationService.getBirthRegistrationById(idTemp);
         * list.add(birthRegistration); Integer userId =
         * birthRegistration.getCreatedBy
         * ()==null?Integer.valueOf(EgovThreadLocals
         * .getUserId()):birthRegistration.getCreatedBy().getId(); List<String>
         * roleList=bndCommonService.getRoleNamesByPassingUserId(userId); String
         * roleName
         * =BndRuleBook.getInstance().getHighestPrivilegedRole(roleList);
         * registrationMapObject =
         * bndCommonService.mapForReportGeneration(birthRegistration,roleName);
         * ReportRequest reportInput = new
         * ReportRequest(BndConstants.BIRTHREGISTRATION_TEMPLATE
         * ,list,registrationMapObject); ReportOutput reportOutput =
         * reportService.createReport(reportInput); reportId =
         * addingReportToSession(reportOutput);
         */

        return "report";
    }

    public InputStream getBirthCertificatePDF() {
        return birthCertificatePDF;
    }

    public void setBirthCertificatePDF(final InputStream birthCertificatePDF) {
        this.birthCertificatePDF = birthCertificatePDF;
    }

}
