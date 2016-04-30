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
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bnd.client.utils.BndRuleBook;
import org.egov.bnd.model.AttentionType;
import org.egov.bnd.model.CRelation;
import org.egov.bnd.model.CitizenBDDetails;
import org.egov.bnd.model.DeliveryMethod;
import org.egov.bnd.model.Disease;
import org.egov.bnd.model.Education;
import org.egov.bnd.model.Establishment;
import org.egov.bnd.model.PlaceType;
import org.egov.bnd.model.RegKeys;
import org.egov.bnd.model.Registrar;
import org.egov.bnd.model.Registration;
import org.egov.bnd.model.RegistrationUnit;
import org.egov.bnd.model.Religion;
import org.egov.bnd.model.StillBirthRegistration;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.utils.BndDateUtils;
import org.egov.bnd.utils.BndUtils;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.enums.AddressType;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Validations(requiredFields = {
        @RequiredFieldValidator(fieldName = "registrationDate", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "dateOfEvent", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "citizen.sex", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "placeTypeTemp", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "eventAddress.state", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "informantAddress.state", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "informantFlag", message = "", key = BndConstants.REQUIRED) }, requiredStrings = {
        @RequiredStringValidator(fieldName = "father.firstName", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "mother.firstName", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "eventAddress.streetAddress1", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "eventAddress.cityTownVillage", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "eventAddress.district", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "informantCitizen.firstName", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "informantAddress.streetAddress1", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "informantAddress.cityTownVillage", message = "", key = BndConstants.REQUIRED),
        @RequiredStringValidator(fieldName = "informantAddress.district", message = "", key = BndConstants.REQUIRED) }

        )
@ParentPackage("egov")
@Namespace("/registration")
@Transactional(readOnly = true)
public class StillBirthRegistrationAction extends RegistrationAction {

    @PersistenceContext
    private EntityManager entityManager;

    private static final long serialVersionUID = -6855607612064252745L;
    private static final Logger LOGGER = Logger.getLogger(StillBirthRegistrationAction.class);
    private StillBirthRegistration stillBirthRegistration = new StillBirthRegistration();

    public StillBirthRegistrationAction() {
        addRelatedEntity("registrationUnit", RegistrationUnit.class);
        addRelatedEntity("registrarId", Registrar.class);
        addRelatedEntity("status", EgwStatus.class);
        addRelatedEntity("modifiedBy", User.class);
        addRelatedEntity("createdBy", User.class);
        addRelatedEntity("citizenBDDetails", CitizenBDDetails.class);
        addRelatedEntity("citizenBDDetails.causeOfDeath", Disease.class);
        addRelatedEntity("causeOfDeathParent", Disease.class);
        addRelatedEntity("citizenBDDetails.religion", Religion.class);
        addRelatedEntity("citizenBDDetails.motherEducation", Education.class);
        addRelatedEntity("typeAttention", AttentionType.class);
        addRelatedEntity("methodDelivery", DeliveryMethod.class);
        addRelatedEntity("establishment", Establishment.class);
        addRelatedEntity("placeType", PlaceType.class);
    }

    @Override
    public Registration getModel() {
        return stillBirthRegistration;
    }

    @Override
    public void prepare() {
        super.prepare();
        buildHospitalList();
        setDifferentActionsForRole();
    }

    private void buildPrepareNewForm() {
        stillBirthRegistration.setRegistrarId(bndCommonService.getRegistrarByLoggedInUser());
        if (stillBirthRegistration.getRegistrarId() != null)
            stillBirthRegistration.setRegistrationUnit(stillBirthRegistration.getRegistrarId().getRegUnitId());
    }

    private void buildHospitalList() {
        if (stillBirthRegistration.getEstablishment() != null
                && stillBirthRegistration.getRegistrarId().getRegUnitId() != null)
            addDropdownData(
                    "hospitalList",
                    bndCommonService.getHospitalByTypeAndUnit(stillBirthRegistration.getEstablishment().getType()
                            .getId(), stillBirthRegistration.getRegistrarId().getRegUnitId().getId()));
        else
            addDropdownData("hospitalList", Collections.EMPTY_LIST);

        if (stillBirthRegistration.getCitizenBDDetails() != null
                && stillBirthRegistration.getCitizenBDDetails().getCauseOfDeath() != null
                && stillBirthRegistration.getCitizenBDDetails().getCauseOfDeath().getParent() != null)
            addDropdownData(
                    "diseaseList",
                    bndCommonService.getCauseofDeathbyParentId(stillBirthRegistration.getCitizenBDDetails()
                            .getCauseOfDeath().getParent().getId()));
        else
            addDropdownData("diseaseList", Collections.EMPTY_LIST);
    }

    public void prepareNewOnlineform() {
        buildPrepareNewForm();
    }

    @SkipValidation
    @Action(value = "/stillBirthRegistration-newform", results = { @Result(name = NEW, type = "dispatcher") })
    public String newOnlineform() {
        LOGGER.debug("New Still Birth Registration Online form");
        stillBirthRegistration.setRegistrationDate(DateUtils.today());
        buildNewBirthForm(stillBirthRegistration);
        registrationMode = "online";
        return NEW;
    }

    public void prepareNewOfflineForm() {
        buildPrepareNewForm();
    }

    @SkipValidation
    @Action(value = "/stillBirthRegistration-newOfflineForm", results = { @Result(name = NEW, type = "dispatcher") })
    public String newOfflineForm() {
        LOGGER.debug("New still Birth Registration Offline form");
        buildNewBirthForm(stillBirthRegistration);
        registrationMode = OFFLINE;
        setMode(OFFLINE);
        return NEW;
    }

    @Override
    public void prepareCreate() {
        getDetailsAddressTypeAndRelation();
    }

    @Override
    @Transactional
    @Action(value = "/stillBirthRegistration-create", results = { @Result(name = NEW, type = "dispatcher") })
    public String create() {
        if (workFlowType != null && !"".equals(workFlowType) && BndConstants.SCRIPT_SAVE.equals(workFlowType))
            stillBirthRegistration.setStatus(bndCommonService.getStatusByModuleAndCode(
                    BndConstants.STILLBIRTHREGISTRATION, BndConstants.CREATED));
        stillBirthRegistration.setPlaceType(bndCommonService.getPlaceType(placeTypeTemp));
        saveOrUpdate();
        mode = VIEW;
        return NEW;
    }

    @Override
    @Transactional
    protected void saveOrUpdate() {
        buildRegistration();
        if (stillBirthRegistration.getRegistrationNo() == null
                || "".equals(stillBirthRegistration.getRegistrationNo().trim()))
            stillBirthRegistration.setRegistrationNo(numberGenerationService.getStillBirthRegistrationNumber(
                    stillBirthRegistration, birthHospitalUserFlag));
        stillBirthRegistration.setAdditionalRule(getAdditionalRule());
        birthRegistrationService.save(stillBirthRegistration, workFlowType);
        stillBirthRegistration.setPlaceType(bndCommonService.getPlaceType(placeTypeTemp));
        LOGGER.debug("Still Birth Registration Record ::::::::::" + stillBirthRegistration);
        mode = VIEW;
    }

    @SkipValidation
    @Override
    @Action(value = "/stillBirthRegistration-beforeEdit", results = { @Result(name = NEW, type = "dispatcher") })
    public String beforeEdit() {
        if (mode != null)
            mode = getMode();
        else
            mode = EDIT;

        return NEW;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void prepareEdit() {
        stillBirthRegistration = (StillBirthRegistration) birthRegistrationService.getBirthRegistrationById(idTemp);
        // stillBirthRegistration =
        // (StillBirthRegistration)birthRegistrationService.merge(stillBirthRegistration);
        fatherAddressSet = (Set<Address>) stillBirthRegistration.getCitizen()
                .getRelatedPerson(BndConstants.FATHER.toUpperCase()).getAddress();
        motherAddressSet = (Set<Address>) stillBirthRegistration.getCitizen()
                .getRelatedPerson(BndConstants.MOTHER.toUpperCase()).getAddress();
        informantAddressSet.add(stillBirthRegistration.getInformant().getRelatedAddress(BndConstants.PRESENTADDRESS));
        citizenAddressSet = (Set<Address>) stillBirthRegistration.getCitizen().getAddress();
        oldRelations = stillBirthRegistration.getCitizen().getRelations();
        fatherAddressSet.removeAll(informantAddressSet);
        motherAddressSet.removeAll(informantAddressSet);
        getDetailsAddressTypeAndRelation();
    }

    private void buildRegistration() {
        // stillBirthRegistration.setPlaceType(bndCommonService.getPlaceType(placeTypeTemp));
        stillBirthRegistration.setIsCitizenKnown(Boolean.FALSE);
        buildCitizenAddress();
        buildCitizenRelation(stillBirthRegistration);
        buildInfomantDetails(stillBirthRegistration);
        buildAddress(stillBirthRegistration);
        // stillBirthRegistration.setStatus(birthStatusFlag==1?bndCommonService.getStatusByModuleAndCode(BndConstants.STILLBIRTHREGISTRATION,BndConstants.APPROVED)
        // :bndCommonService.getStatusByModuleAndCode(BndConstants.STILLBIRTHREGISTRATION,BndConstants.CREATED));
        stillBirthRegistration.getCitizenBDDetails().setIsStillBirth('Y');
        stillBirthRegistration.setIsStillBirth('Y');
        stillBirthRegistration.setAdoptionDetail(null);
        LOGGER.info("Mother Education-------:::" + stillBirthRegistration.getCitizenBDDetails().getMotherEducation());
        if (stillBirthRegistration.getCitizenBDDetails().getCauseOfDeath() == null)
            stillBirthRegistration.getCitizenBDDetails()
            .setCauseOfDeath(stillBirthRegistration.getCauseOfDeathParent());
    }

    @Override
    @Transactional
    @ValidationErrorPage(NEW)
    @Action(value = "/stillBirthRegistration-edit", results = { @Result(name = NEW, type = "dispatcher") })
    public String edit() {
        if (getMode().equals(LOCK)) {
            birthRegistrationService.buildAdoptionDetial(stillBirthRegistration);
            buildRegistration();
            stillBirthRegistration.setPlaceType(bndCommonService.getPlaceType(placeTypeTemp));
            stillBirthRegistration.setStatus(bndCommonService.getStatusByModuleAndCode(
                    BndConstants.STILLBIRTHREGISTRATION, BndConstants.LOCK));
            final EgwSatuschange change = new EgwSatuschange();
            change.setFromstatus(bndCommonService.getStatusByModuleAndCode(BndConstants.STILLBIRTHREGISTRATION,
                    BndConstants.APPROVED).getId());
            change.setTostatus(bndCommonService.getStatusByModuleAndCode(BndConstants.STILLBIRTHREGISTRATION,
                    BndConstants.LOCK).getId());
            change.setModuleid(stillBirthRegistration.getId().intValue());
            change.setModuletype(BndConstants.STILLBIRTHREGISTRATIONMODULE);
            // change.setCreatedby(EgovThreadLocals.getUserId());
            // TODO egifix-hibernateutil
            // HibernateUtil.getCurrentSession().persist(change);
            entityManager.persist(change);
            birthRegistrationService.save(stillBirthRegistration, workFlowType);
        } else if (getMode().equals(UNLOCK)) {
            birthRegistrationService.buildAdoptionDetial(stillBirthRegistration);
            buildRegistration();
            stillBirthRegistration.setPlaceType(bndCommonService.getPlaceType(placeTypeTemp));
            stillBirthRegistration.setStatus(bndCommonService.getStatusByModuleAndCode(
                    BndConstants.STILLBIRTHREGISTRATION, BndConstants.APPROVED));
            final EgwSatuschange change = new EgwSatuschange();
            change.setFromstatus(bndCommonService.getStatusByModuleAndCode(BndConstants.STILLBIRTHREGISTRATION,
                    BndConstants.LOCK).getId());
            change.setTostatus(bndCommonService.getStatusByModuleAndCode(BndConstants.STILLBIRTHREGISTRATION,
                    BndConstants.APPROVED).getId());
            change.setModuleid(stillBirthRegistration.getId().intValue());
            change.setModuletype(BndConstants.STILLBIRTHREGISTRATIONMODULE);
//             change.setCreatedby(EgovThreadLocals.getUserId());
            // TODO egifix-hibernateutil
            // HibernateUtil.getCurrentSession().persist(change);
            entityManager.persist(change);
            birthRegistrationService.save(stillBirthRegistration, workFlowType);
        } else {
            saveOrUpdate();
            if (BndConstants.APPROVED.equalsIgnoreCase(stillBirthRegistration.getStatus().getCode())
                    && stillBirthRegistration.getRegistrationNo().contains(BndConstants.HOSPITALSTILLBIRTHSUFFIX))
                numberGenerationService.reGenerateStillBirthRegistrationNumber(stillBirthRegistration);
            if (remarksHistory != null && !"".equals(remarksHistory))
                bndCommonService.saveHistory(stillBirthRegistration, BndConstants.STILLBIRTHREGISTRATION,
                        remarksHistory);
        }
        mode = VIEW;
        return NEW;
    }

    private void buildCitizenAddress() {
        stillBirthRegistration.getCitizen().setName(BndConstants.STILLBIRTH);
        if (placeTypeTemp != null && BndConstants.NOTSTATED.equals(placeTypeTemp))
            stillBirthRegistration.setEventAddress(null);
        else if (placeTypeTemp != null && BndConstants.HOSPTIAL.equals(placeTypeTemp))
            stillBirthRegistration.setEventAddress(stillBirthRegistration.getEstablishment().getAddress());
        else stillBirthRegistration.getEventAddress().setType(eventAddressType);
         stillBirthRegistration.getMotherResidenceAddress().setType(usualAddressType);
        buildNewAddressFromOldAddress(motherAddressSet, stillBirthRegistration.getMotherResidenceAddress());
        stillBirthRegistration.getInformantAddress().setType(AddressType.CORRESPONDENCE);
    }

    @Override
    public void validate() {

        if (stillBirthRegistration.getDateOfEvent() != null
                && stillBirthRegistration.getDateOfEvent().after(DateUtils.today()))
            addActionError(getMessage("dateOfEvent.today.validate"));

        if (stillBirthRegistration.getRegistrationDate() != null && stillBirthRegistration.getDateOfEvent() != null
                && stillBirthRegistration.getDateOfEvent().after(stillBirthRegistration.getRegistrationDate()))
            addActionError(getMessage("dateOfEvent.registration.validate"));

        if (placeTypeTemp != null && !EMPTY.equals(placeTypeTemp)
                && BndConstants.HOSPTIAL.equalsIgnoreCase(placeTypeTemp)
                && stillBirthRegistration.getEstablishment() == null)
            addActionError(getMessage("hospitalname.required"));
        // For Offline mode validating registration number....
        if (OFFLINE.equals(mode) && stillBirthRegistration.getRegistrationNo() != null
                && !EMPTY.equals(stillBirthRegistration.getRegistrationNo().trim()))
            if (stillBirthRegistration.getRegistrationUnit() != null
            && birthRegistrationService.checkUniqueRegistrationNumber(
                    stillBirthRegistration.getRegistrationUnit().getId(),
                    stillBirthRegistration.getId(),
                    stillBirthRegistration.getRegistrationNo(),
                    BndConstants.REGISTRATIONDATE.equalsIgnoreCase(numberGenKey) ? stillBirthRegistration
                            .getRegistrationDate() : stillBirthRegistration.getDateOfEvent(),
                            BndConstants.STILLBIRTHNUM))
                addActionError(getMessage("registration.number.exists"));

            else if (BndUtils.isNum(stillBirthRegistration.getRegistrationNo().trim())) {
                final int eventYear = BndConstants.REGISTRATIONDATE.equalsIgnoreCase(numberGenKey) ? BndDateUtils
                        .getCurrentYear(stillBirthRegistration.getRegistrationDate()) : BndDateUtils
                        .getCurrentYear(stillBirthRegistration.getDateOfEvent());
                        final RegKeys regNumRange = regKeyService.getRegKeyByRegUnitAndDate(stillBirthRegistration
                                .getRegistrationUnit(), eventYear, numberGenerationService.buildObjectType(
                                        stillBirthRegistration, eventYear, BndConstants.STILLBIRTHNUM));
                        if (regNumRange != null) {
                            final Integer regNumber = Integer.valueOf(stillBirthRegistration.getRegistrationNo());
                            if (regNumber >= regNumRange.getMinValue())
                                addActionError(getMessage("regNumber.minvalue.validate").concat(" ").concat(
                                        regNumRange.getMinValue().toString()));
                        }
            }

    }

    /**
     * This method is to prepare a view method
     */

    @Override
    public void prepareView() {
        beforeViewAndModify();
    }

    private void beforeViewAndModify() {
        stillBirthRegistration = (StillBirthRegistration) birthRegistrationService.getBirthRegistrationById(idTemp);
        stillBirthRegistration.setFather(stillBirthRegistration.getCitizen().getRelatedPerson(
                BndConstants.FATHER.toUpperCase()));
        stillBirthRegistration.setMother(stillBirthRegistration.getCitizen().getRelatedPerson(
                BndConstants.MOTHER.toUpperCase()));

        if (stillBirthRegistration.getInformant() != null) {
            final CRelation relation = stillBirthRegistration.getCitizen().getRelation(
                    stillBirthRegistration.getInformant());
            if (relation != null)
                informantFlag = relation.getDesc();
            informantCitizen = stillBirthRegistration.getInformant();
            stillBirthRegistration.setInformantAddress(stillBirthRegistration.getInformant().getRelatedAddress(
                    BndConstants.PRESENTADDRESS));
        }
        stillBirthRegistration.setMotherResidenceAddress(stillBirthRegistration.getMother().getRelatedAddress(
                BndConstants.USUALADDRESS));
        placeTypeTemp = stillBirthRegistration.getPlaceType().getDesc();

        if (stillBirthRegistration.getCitizenBDDetails().getCauseOfDeath() != null)
            if (stillBirthRegistration.getCitizenBDDetails().getCauseOfDeath().getParent() != null) {
                stillBirthRegistration.setCauseOfDeathParent(stillBirthRegistration.getCitizenBDDetails()
                        .getCauseOfDeath().getParent());
                addDropdownData(
                        "diseaseList",
                        bndCommonService.getCauseofDeathbyParentId(stillBirthRegistration.getCitizenBDDetails()
                                .getCauseOfDeath().getParent().getId()));
            } else
                stillBirthRegistration.setCauseOfDeathParent(stillBirthRegistration.getCitizenBDDetails()
                        .getCauseOfDeath());

    }

    /**
     * This method is to prepare a modify method
     */

    @Override
    public void prepareBeforeEdit() {
        beforeViewAndModify();
        setUpRegistrationDetailsForValidation(stillBirthRegistration);
    }

    /**
     * This method is to prepare a inbox method
     */

    @Override
    public void prepareInbox() {
        beforeViewAndModify();
        setUpRegistrationDetailsForValidation(stillBirthRegistration);
    }

    @Override
    protected String getPendingActions() {
        return stillBirthRegistration == null ? "" : stillBirthRegistration.getCurrentState() == null ? ""
                : stillBirthRegistration.getCurrentState().getNextAction();
    }

    @Override
    @Transactional
    public String getAdditionalRule() {

        final Long userId = stillBirthRegistration.getCreatedBy() == null ? Long.valueOf(EgovThreadLocals.getUserId())
                : stillBirthRegistration.getCreatedBy().getId();
        final List<String> roleList = bndCommonService.getRoleNamesByPassingUserId(userId);
        return BndRuleBook.HOSPITALUSER.equalsIgnoreCase(BndRuleBook.getInstance().getHighestPrivilegedRole(roleList)) ? BndRuleBook.HOSPITALUSER
                : "ALL";

    }

    @Override
    public String getNextAction() {
        final WorkFlowMatrix wfMatrix = null;
        // TODO egifix-state
        /*
         * if (getModel().getId() != null) { if (getModel().getCurrentState() !=
         * null) wfMatrix =
         * customizedWorkFlowService.getWfMatrix(getModel().getStateType(),
         * getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
         * getModel().getCurrentState().getValue(), getPendingActions()); else
         * wfMatrix =
         * customizedWorkFlowService.getWfMatrix(getModel().getStateType(),
         * getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
         * State.NEW, getPendingActions()); } else wfMatrix =
         * customizedWorkFlowService.getWfMatrix(getModel().getStateType(),
         * getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
         * State.NEW, getPendingActions());
         */
        return wfMatrix == null ? "" : wfMatrix.getNextAction();

    }

}
