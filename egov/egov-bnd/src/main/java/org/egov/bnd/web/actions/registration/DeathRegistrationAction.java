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
import org.egov.bnd.model.*;
import org.egov.bnd.services.registration.DeathRegistrationService;
import org.egov.bnd.utils.BndConstants;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.enums.AddressType;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Validations(

        requiredStrings = {
                @RequiredStringValidator(fieldName = "placeTypeTemp", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "citizen.firstName", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "deceasedAddress.streetRoadLine", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "deceasedAddress.cityTownVillage", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "deceasedAddress.district", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "deceasedAddress.state", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "permanentCitizenAddress.streetRoadLine", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "permanentCitizenAddress.cityTownVillage", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "permanentCitizenAddress.district", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "permanentCitizenAddress.state", type = ValidatorType.FIELD, message = "Required"),
                @RequiredStringValidator(fieldName = "mother.firstName", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "eventAddress.streetRoadLine", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "eventAddress.cityTownVillage", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "eventAddress.district", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "eventAddress.state", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "informantCitizen.firstName", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "informantAddress.streetRoadLine", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "informantAddress.cityTownVillage", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "informantAddress.district", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredStringValidator(fieldName = "informantAddress.state", type = ValidatorType.FIELD, message = "Required", key = "")

        },

        requiredFields = {
                @RequiredFieldValidator(fieldName = "registrationDate", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredFieldValidator(fieldName = "dateOfEvent", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredFieldValidator(fieldName = "citizen.sex", type = ValidatorType.FIELD, message = "Required", key = ""),
                @RequiredFieldValidator(fieldName = "ageType", type = ValidatorType.FIELD, message = "Required", key = "")
        })
@Transactional(readOnly = true)
@Namespace("/registration")
@ParentPackage("egov")
public class DeathRegistrationAction extends RegistrationAction {

	@PersistenceContext
	private EntityManager entityManager;
	
    private static final long serialVersionUID = 1412469733689024605L;
    private DeathRegistration deathRegistration = new DeathRegistration();
    private Integer nameOfdeceasedFlag;
    // private Integer permanentAddressFlag;
    private Integer deceasedAddressFlag;
    private Integer deathDelayedFlag;
    private Integer deathHospitalUserFlag;
    private Integer deathHospitalRegistrarFlag;
    private List<String> actionList;
    private Integer deathStatisticsInfoFlag;
    private DeathRegistrationService deathRegistrationService;
    private Integer deathStatusFlag;
    private List<CitizenRelation> relationList = new ArrayList<CitizenRelation>();
    private List<Addiction> addictionList = new ArrayList<Addiction>();
    protected Set<Address> husbandAddressSet = new HashSet<Address>(0);
    private static final Logger LOGGER = Logger.getLogger(DeathRegistrationAction.class);
    public static final String PRINTDEATH = "printdeath";
    private InputStream deathCertificatePDF;

    public DeathRegistrationAction() {
        addRelatedEntity("registrationUnit", RegistrationUnit.class);
        addRelatedEntity("registrarId", Registrar.class);
        addRelatedEntity("status", EgwStatus.class);
        addRelatedEntity("modifiedBy", User.class);
        addRelatedEntity("createdBy", User.class);
        addRelatedEntity("citizenBDDetails", CitizenBDDetails.class);
        addRelatedEntity("citizenBDDetails.religion", Religion.class);
        addRelatedEntity("citizenBDDetails.causeOfDeath", Disease.class);
        addRelatedEntity("causeOfDeathParent", Disease.class);
        addRelatedEntity("crematorium", CrematoriumMaster.class);
        addRelatedEntity("addictions", CitizenAddiction.class);
        addRelatedEntity("establishment", Establishment.class);
        addRelatedEntity("placeType", PlaceType.class);
        addRelatedEntity("ageType", AgeType.class);
        addRelatedEntity("citizenBDDetails.occupation", Occupation.class);
        addRelatedEntity("typeMedAttention", AttentionDeathType.class);
        addRelatedEntity("relationType", CRelation.class);
        addRelatedEntity("deceasedrelationType", CRelation.class);
        addRelatedEntity("establishment", Establishment.class);
        addRelatedEntity("placeType", PlaceType.class);
        addRelatedEntity("deathHistory.placeType", PlaceType.class);
        // addRelatedEntity("deathHistory.establishment",Establishment.class);
    }

    @Override
    public void prepare() {
        super.prepare();
        buildHospitalDiseaseList();
        setDifferentActionsForRole();
    }

    private void buildHospitalDiseaseList() {
        LOGGER.debug("Started buildHospitalDiseaseList method");
        addDropdownData("relationList",
                bndCommonService.findAllBy("from CRelation where relatedAsConst IN('HUSBAND', 'FATHER')"));

        if (deathRegistration.getEstablishment() != null && deathRegistration.getRegistrarId() != null
                && deathRegistration.getRegistrarId().getRegUnitId() != null)
            addDropdownData("hospitalList", bndCommonService.getHospitalByTypeAndUnit(deathRegistration
                    .getEstablishment().getType().getId(), deathRegistration.getRegistrarId().getRegUnitId().getId()));
        else
            addDropdownData("hospitalList", Collections.EMPTY_LIST);

        if (deathRegistration.getCitizenBDDetails() != null
                && deathRegistration.getCitizenBDDetails().getCauseOfDeath() != null
                && deathRegistration.getCitizenBDDetails().getCauseOfDeath().getParent() != null)
            addDropdownData(
                    "diseaseList",
                    bndCommonService.getCauseofDeathbyParentId(deathRegistration.getCitizenBDDetails()
                            .getCauseOfDeath().getParent().getId()));
        else
            addDropdownData("diseaseList", Collections.EMPTY_LIST);
        LOGGER.debug("Completed buildHospitalDiseaseList method");
    }

    @SkipValidation
    @Override
    @Action(value = "/deathRegistration-beforeEdit", results = { @Result(name = NEW, type = "dispatcher") })
    public String beforeEdit() {
        if (mode != null)
            mode = getMode();
        else
            mode = EDIT;

        return NEW;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    @Action(value = "/deathRegistration-prepareEdit", results = { @Result(name = NEW, type = "dispatcher") })
    public void prepareEdit() {
        LOGGER.debug("Started prepareEdit method");
        deathRegistration = deathRegistrationService.getDeathRegistrationById(idTemp);
        deathRegistration = deathRegistrationService.merge(deathRegistration);
        citizenAddressSet = (Set<Address>) deathRegistration.getCitizen().getAddress();
        informantAddressSet = (Set<Address>) deathRegistration.getInformant().getAddress();
        if (deathRegistration.getCitizen().getRelatedPerson(BndConstants.FATHER.toUpperCase()) != null)
            fatherAddressSet = (Set<Address>) deathRegistration.getCitizen()
            .getRelatedPerson(BndConstants.FATHER.toUpperCase()).getAddress();
        if (deathRegistration.getCitizen().getRelatedPerson(BndConstants.MOTHER.toUpperCase()) != null)
            motherAddressSet = (Set<Address>) deathRegistration.getCitizen()
            .getRelatedPerson(BndConstants.MOTHER.toUpperCase()).getAddress();
        if (deathRegistration.getCitizen().getRelatedPerson(BndConstants.HUSBAND.toUpperCase()) != null)
            husbandAddressSet = (Set<Address>) deathRegistration.getCitizen()
            .getRelatedPerson(BndConstants.HUSBAND.toUpperCase()).getAddress();
        oldRelations = deathRegistration.getCitizen().getRelations();
        LOGGER.debug("Completed prepareEdit method");
    }

    @Override
    @Transactional
    @ValidationErrorPage(NEW)
    @Action(value = "/deathRegistration-edit", results = { @Result(name = NEW, type = "dispatcher") })
    public String edit() {
        LOGGER.debug("Started edit method");
        if (getMode().equals(LOCK)) {

            buildDeathRegistration();
            deathRegistration.setStatus(bndCommonService.getStatusByModuleAndCode(BndConstants.DEATHREGISTRATION,
                    BndConstants.LOCK));
            // TODO Audit is not required
            final EgwSatuschange change = new EgwSatuschange();
            change.setFromstatus(bndCommonService.getStatusByModuleAndCode(BndConstants.DEATHREGISTRATION,
                    BndConstants.APPROVED).getId());
            change.setTostatus(bndCommonService.getStatusByModuleAndCode(BndConstants.DEATHREGISTRATION,
                    BndConstants.LOCK).getId());
            change.setModuleid(deathRegistration.getId().intValue());
            change.setModuletype(BndConstants.DEATHREGISTRATIONMODULE);
            // change.setCreatedby(EgovThreadLocals.getUserId());
            entityManager.persist(change);
            deathRegistrationService.save(deathRegistration, workFlowType, addictionList);
        } else if (getMode().equals(UNLOCK)) {

            buildDeathRegistration();
            deathRegistration.setStatus(bndCommonService.getStatusByModuleAndCode(BndConstants.DEATHREGISTRATION,
                    BndConstants.APPROVED));
            final EgwSatuschange change = new EgwSatuschange();
            change.setFromstatus(bndCommonService.getStatusByModuleAndCode(BndConstants.DEATHREGISTRATION,
                    BndConstants.LOCK).getId());
            change.setTostatus(bndCommonService.getStatusByModuleAndCode(BndConstants.DEATHREGISTRATION,
                    BndConstants.APPROVED).getId());
            change.setModuleid(deathRegistration.getId().intValue());
            change.setModuletype(BndConstants.DEATHREGISTRATIONMODULE);
            // change.setCreatedby(EgovThreadLocals.getUserId());
            entityManager.persist(change);
            deathRegistrationService.save(deathRegistration, workFlowType, addictionList);
        } else {
            saveOrUpdate();
            if (deathRegistration.getStatus().getCode().equalsIgnoreCase(BndConstants.APPROVED)
                    && deathRegistration.getRegistrationNo().contains(BndConstants.HOSPITALDEATHSUFFIX)) {
                deathRegistration.setHospitalRegistrationNo(deathRegistration.getRegistrationNo());
                numberGenerationService.reGenerateDeathRegistrationNumber(deathRegistration);
            }
            if (remarksHistory != null && !"".equals(remarksHistory))
                bndCommonService.saveHistory(deathRegistration, deathRegistration.getClass().getSimpleName(),
                        remarksHistory);
        }
        mode = VIEW;
        LOGGER.debug("Completed edit method");
        return NEW;

    }

    private void buildPrepareNewForm() {
        LOGGER.debug("Started buildPrepareNewForm method");
        deathRegistration.setRegistrarId(bndCommonService.getRegistrarByLoggedInUser());
        if (deathRegistration.getRegistrarId() != null)
            deathRegistration.setRegistrationUnit(deathRegistration.getRegistrarId().getRegUnitId());
        LOGGER.debug("Completed buildPrepareNewForm method");
    }

    @Override
    public DeathRegistration getModel() {
        return deathRegistration;
    }

    public void prepareNewOnlineform() {
        buildPrepareNewForm();
    }

    @Transactional
    @SkipValidation
    @Action(value = "/deathRegistration-newOnlineform", results = { @Result(name = NEW, type = "dispatcher") })
    public String newOnlineform()
    {
        LOGGER.info("Started Death Registration Online form");
        deathRegistration.setRegistrationDate(DateUtils.today());
        buildNewdeathRegistration();
        setRegistrationMode("online");
        LOGGER.debug("Completed  Death Registration Online method");
        return NEW;
    }

    @SuppressWarnings("unchecked")
    private void buildNewdeathRegistration() {
        LOGGER.debug("Started buildNewdeathRegistration method");
        if (deathRegistration.getRegistrarId() == null)
            throw new EGOVRuntimeException(getMessage("user.registrar.error"));

        if (deathRegistration.getRegistrarId() != null && deathRegistration.getRegistrarId().getRegUnitId() == null)
            throw new EGOVRuntimeException(getMessage("user.registrationunit.error"));

        for (final Addiction addictiontemp : (List<Addiction>) bndCommonService.findAllBy("from Addiction"))
            addictionList.add(addictiontemp);
        nameOfdeceasedFlag = 1;
        permanentAddressFlag = 1;
        parentAddressFlag = 1;
        deceasedAddressFlag = 1;

        if (deathHospitalUserFlag == 1 || deathHospitalRegistrarFlag == 1)
            buildNewFormForHospital(deathRegistration);
        LOGGER.debug("Completed buildNewdeathRegistration method");
    }

    public void prepareNewOfflineForm() {
        buildPrepareNewForm();
    }

    @Transactional
    @SkipValidation
    @Action(value = "/deathRegistration-newOfflineForm", results = { @Result(name = NEW, type = "dispatcher") })
    public String newOfflineForm() {

        LOGGER.info("New newOfflineForm form");
        buildNewdeathRegistration();
        setRegistrationMode("offline");
        setMode(OFFLINE);
        LOGGER.debug("Completed newOfflineForm method");
        return NEW;
    }

    
    @Override
    @Transactional
    @Action(value = "/deathRegistration-create", results = { @Result(name = NEW, type = "dispatcher") })
    public String create() {
        if (workFlowType != null && !"".equals(workFlowType) && BndConstants.SCRIPT_SAVE.equals(workFlowType))
            deathRegistration.setStatus(bndCommonService.getStatusByModuleAndCode(BndConstants.DEATHREGISTRATION,
                    BndConstants.CREATED));
        saveOrUpdate();
        mode = VIEW;
        return NEW;
    }

    @Override
    @Transactional
    protected void saveOrUpdate() {
        LOGGER.debug("Started saveOrUpdate method");
        buildDeathRegistration();
        if (deathRegistration.getRegistrationNo() == null || "".equals(deathRegistration.getRegistrationNo().trim()))
            deathRegistration.setRegistrationNo(numberGenerationService.getDeathRegistrationNumber(deathRegistration,
                    deathHospitalUserFlag));
        deathRegistration.setAdditionalRule(getAdditionalRule());

        deathRegistrationService.save(deathRegistration, workFlowType, addictionList);
        setMode(VIEW);
        setHospitalDisease(deathRegistration);
        LOGGER.debug("Completed saveOrUpdate method");
    }

    @Override
    protected void setDifferentActionsForRole() {
        LOGGER.debug("Started setDifferentActionsForRole method");
        final BndRuleBook ruleBook = BndRuleBook.getInstance();
        final List<String> roleList = bndCommonService.getRoleNamesByPassingUserId(Long.valueOf(EgovThreadLocals
                .getUserId()));
        actionList = ruleBook.getDeathActionsByRoles(roleList);
        deathStatusFlag = actionList.contains(BndRuleBook.DEATHSTATUSAPPROVED) ? 1 : 0;
        deathStatisticsInfoFlag = actionList.contains(BndRuleBook.DEATHSTATISTICALINFO) ? 1 : 0;
        deathDelayedFlag = actionList.contains(BndRuleBook.DEATHDELAYEDREG) ? 1 : 0;
        final String role = ruleBook.getHighestPrivilegedRole(roleList);
        deathHospitalRegistrarFlag = BndRuleBook.HOSPITALREGISTRAR.equalsIgnoreCase(role) ? 1 : 0;
        deathHospitalUserFlag = BndRuleBook.HOSPITALUSER.equalsIgnoreCase(role) ? 1 : 0;
        if (deathHospitalUserFlag == 1 || deathHospitalRegistrarFlag == 1)
            gracePeriod = bndCommonService.getAppconfigValueResult(BndConstants.BNDMODULE, BndConstants.GRACEPERIODKEY,
                    "0");
        numberGenKey = bndCommonService.getAppconfigValueResult(BndConstants.BNDMODULE, BndConstants.NUMBERGENKEY,
                BndConstants.REGISTRATIONDATE);
        LOGGER.debug("Completed setDifferentActionsForRole method");
    }

    private void buildInfomantDetailsforModify(final DeathRegistration deathRegistration) {
        LOGGER.debug("Started buildInfomantDetailsforModify method");
       
          deathRegistration.getInformantAddress().setType(bndCommonService.getAddressType(BndConstants.PRESENTADDRESS));
         
        buildNewAddressFromOldAddress(informantAddressSet, deathRegistration.getInformantAddress());

        if (deathRegistration.getRelationType() != null
                && BndConstants.FATHER.toUpperCase().equals(deathRegistration.getRelationType().getRelatedAsConst())
                && deathRegistration.getCitizen().getRelatedPerson(BndConstants.FATHER.toUpperCase()) != null) {
            deathRegistration.setInformant(deathRegistration.getCitizen().getRelatedPerson(
                    BndConstants.FATHER.toUpperCase()));
            fatherAddressSet.addAll(informantAddressSet);

        } else if (deathRegistration.getRelationType() != null
                && BndConstants.HUSBAND.toUpperCase().equals(deathRegistration.getRelationType().getRelatedAsConst())
                && deathRegistration.getCitizen().getRelatedPerson(BndConstants.HUSBAND.toUpperCase()) != null) {
            deathRegistration.setInformant(deathRegistration.getCitizen().getRelatedPerson(
                    BndConstants.HUSBAND.toUpperCase()));
            husbandAddressSet.addAll(informantAddressSet);
        }

        else if (deathRegistration.getRelationType() != null
                && BndConstants.MOTHER.toUpperCase().equals(deathRegistration.getRelationType().getRelatedAsConst())) {
            deathRegistration.setInformant(deathRegistration.getCitizen().getRelatedPerson(
                    BndConstants.MOTHER.toUpperCase()));
            motherAddressSet.addAll(informantAddressSet);
        } else if (deathRegistration.getRelationType() != null && deathRegistration.getInformant() != null
                && !deathRegistration.getPlaceType().getDesc().equals(BndConstants.HOSPTIAL)) {
            informantCitizen.getAddress().addAll(informantAddressSet);
            deathRegistration.getInformant().updateCitizenName(informantCitizen);
            deathRegistration.setInformant(informantCitizen);
        }
        LOGGER.debug("Completed buildInfomantDetailsforModify method");
    }

    private void buildDeathRegistration() {
        LOGGER.debug("Started buildDeathRegistration method");
        deathRegistration.setPlaceType(bndCommonService.getPlaceType(placeTypeTemp));
        deathRegistration.setIsCitizenKnown(nameOfdeceasedFlag == 1 ? Boolean.TRUE : Boolean.FALSE);
        if (deathRegistration.getId() != null) {
            buildInfomantDetailsforModify(deathRegistration);
            buildCitizenRelationforModify();
            buildAddressforModify();

        } else {
            buildAddress();
            buildCitizenRelation();
        }

        if (deathRegistration.getRegistrationUnit() != null
                && deathRegistration.getRegistrationUnit().getRegUnitConst() != null)
            deathRegistration.setGlcNumber(deathRegistration.getRegistrationUnit().getRegUnitConst());
        if (deathRegistration.getDeceasedrelationType() != null)
            deathRegistration.setIsRelative(Boolean.TRUE);
        else
            deathRegistration.setIsRelative(Boolean.FALSE);
        if (deathRegistration.getCrematorium() == null || deathRegistration.getCrematorium().getId() == -1)
            deathRegistration.setCrematorium(null);

        if (deathRegistration.getTypeAttention() == null || deathRegistration.getTypeAttention().getId() == -1)
            deathRegistration.setTypeAttention(null);

        if (deathRegistration.getCauseOfDeathParent() != null
                && deathRegistration.getCauseOfDeathParent().getId() != -1)
            // TODO:Check this condition
            if (deathRegistration.getCitizenBDDetails().getCauseOfDeath() != null
            && deathRegistration.getCitizenBDDetails().getCauseOfDeath().getId() != -1)
                ;
            else
                deathRegistration.getCitizenBDDetails().setCauseOfDeath(deathRegistration.getCauseOfDeathParent());
        LOGGER.debug("Completed buildDeathRegistration method");

    }

    private void buildAddress() {
        LOGGER.debug("Started buildAddress method");

        if (deceasedAddressFlag != null && deceasedAddressFlag == 1){
             deathRegistration.getDeceasedAddress().setType(bndCommonService.getAddressType(BndConstants.DECEASEDADDRESS));
            deathRegistration.getCitizen().addAddress(deathRegistration.getDeceasedAddress());
        }
        else
            deathRegistration.setDeceasedAddress(null);

        if (permanentAddressFlag != null && permanentAddressFlag == 1) {
            deathRegistration.getPermanentCitizenAddress().setType(AddressType.PERMANENT);
            deathRegistration.getCitizen().addAddress(deathRegistration.getPermanentCitizenAddress());
        } else
            deathRegistration.setPermanentCitizenAddress(null);

        if (placeTypeTemp != null && BndConstants.NOTSTATED.equals(placeTypeTemp))
            deathRegistration.setEventAddress(null);
        else if (placeTypeTemp != null && BndConstants.HOSPTIAL.equals(placeTypeTemp)) {
            deathRegistration.setEventAddress(deathRegistration.getEstablishment().getAddress());
            deathRegistration.getCitizen().addAddress(deathRegistration.getEventAddress());
        } else
             deathRegistration.getEventAddress().setType(bndCommonService.getAddressType(BndConstants.EVENTADDRESS));
             deathRegistration.getCitizen().addAddress(deathRegistration.getEventAddress());

        if (deathRegistration.getDeceasedUsualAddress() != null
                && deathRegistration.getDeceasedUsualAddress().getCityTownVillage() != null
                && deathRegistration.getDeceasedUsualAddress().getState() != "-1"
                && !deathRegistration.getDeceasedUsualAddress().getCityTownVillage().equals("")
                && !deathRegistration.getDeceasedUsualAddress().getDistrict().equals(""))
            // TODO egifix-addresstype
            /*
             * deathRegistration.getDeceasedUsualAddress().setAddTypeMaster(
             * bndCommonService.getAddressType(BndConstants.USUALADDRESS));
             */
            deathRegistration.getCitizen().addAddress(deathRegistration.getDeceasedUsualAddress());
        else
            deathRegistration.setDeceasedUsualAddress(null);

        LOGGER.debug("Completed buildAddress method");
    }

    private void buildAddressforModify() {
        LOGGER.debug("Started buildAddressforModify method");
        if (permanentAddressFlag != null && permanentAddressFlag == 1) {
            deathRegistration.getCitizen().getRelations().size();
            deathRegistration.getPermanentCitizenAddress().setType(AddressType.PERMANENT);
            buildNewAddressFromOldAddress(citizenAddressSet, deathRegistration.getPermanentCitizenAddress());
        } else
            deathRegistration.setPermanentCitizenAddress(null);

        if (deceasedAddressFlag != null && deceasedAddressFlag == 1){
             deathRegistration.getDeceasedAddress().setType(
              bndCommonService.getAddressType(BndConstants.DECEASEDADDRESS));
            buildNewAddressFromOldAddress(citizenAddressSet, deathRegistration.getDeceasedAddress());
        }
        else
            deathRegistration.setDeceasedAddress(null);

        if (placeTypeTemp != null && BndConstants.NOTSTATED.equals(placeTypeTemp))
            deathRegistration.setEventAddress(null);
        else if (placeTypeTemp != null && BndConstants.HOSPTIAL.equals(placeTypeTemp)) {
            deathRegistration.getEventAddress();
            deathRegistration.setEventAddress(deathRegistration.getEstablishment().getAddress());
        } else
          
              deathRegistration.getEventAddress().setType(
              bndCommonService.getAddressType(BndConstants.EVENTADDRESS));
             

            if (deathRegistration.getDeceasedUsualAddress() != null)
            
                 deathRegistration.getDeceasedUsualAddress().setType(
                 bndCommonService.getAddressType(BndConstants.USUALADDRESS));
                
                buildNewAddressFromOldAddress(citizenAddressSet, deathRegistration.getDeceasedUsualAddress());
        LOGGER.debug("Completed buildAddressforModify method");
    }

    private void buildCitizenRelation() {

        deathRegistration.getCitizen().getRelations().clear();
        deathRegistration.getCitizen().getRelations().addAll(getRelationshipdetail());

    }

    private List<CitizenRelation> getRelationshipdetail() {
        LOGGER.debug("Started getRelationshipdetail method");
        final List<CitizenRelation> relations = new ArrayList<CitizenRelation>();
        if (deathRegistration.getRelationType() != null) {

            if (deathRegistration.getRelationType().getRelatedAsConst().equals(BndConstants.MOTHER.toUpperCase())) {
                deathRegistration.getInformantAddress().setType(AddressType.CORRESPONDENCE);
                deathRegistration.getMother().addAddress(deathRegistration.getInformantAddress());
                deathRegistration.setInformant(deathRegistration.getMother());
            }

            else if (deathRegistration.getDeceasedrelationType() != null
                    && deathRegistration.getRelationType().equals(deathRegistration.getDeceasedrelationType())) {
                deathRegistration.getInformantAddress().setType(AddressType.CORRESPONDENCE);
                deathRegistration.getDeceasedrelation().addAddress(deathRegistration.getInformantAddress());
                deathRegistration.setInformant(deathRegistration.getDeceasedrelation());
                relations.add(bndCommonService.populateCitizenRelation(deathRegistration.getCitizen(),
                        deathRegistration.getDeceasedrelation(), deathRegistration.getDeceasedrelationType()
                        .getRelatedAsConst()));

            } else {
                deathRegistration.getInformantAddress().setType(AddressType.CORRESPONDENCE);
                deathRegistration.setInformant(getInformantCitizen());
                deathRegistration.getInformant().addAddress(deathRegistration.getInformantAddress());
                relations.add(bndCommonService.populateCitizenRelation(deathRegistration.getCitizen(),
                        deathRegistration.getInformant(), deathRegistration.getRelationType().getRelatedAsConst()));
            }
        } else if (deathRegistration.getPlaceType().getDesc().equals(BndConstants.HOSPTIAL)) {
            deathRegistration.getInformantAddress().setType(AddressType.CORRESPONDENCE);
            deathRegistration.setInformant(getInformantCitizen());
            deathRegistration.getInformant().addAddress(deathRegistration.getInformantAddress());

        } else {
            deathRegistration.setInformant(null);
            deathRegistration.setInformantAddress(null);
        }

        if (deathRegistration.getDeceasedrelationType() != null && deathRegistration.getRelationType() != null
                && !deathRegistration.getRelationType().equals(deathRegistration.getDeceasedrelationType()))
            relations.add(bndCommonService.populateCitizenRelation(deathRegistration.getCitizen(), deathRegistration
                    .getDeceasedrelation(), deathRegistration.getDeceasedrelationType().getRelatedAsConst()));

        relations.add(bndCommonService.populateCitizenRelation(deathRegistration.getCitizen(),
                deathRegistration.getMother(), BndConstants.MOTHER.toUpperCase()));
        LOGGER.debug("Completed getRelationshipdetail method");
        return relations;
    }

    private void buildCitizenRelationforModify() {

        LOGGER.debug("Started buildCitizenRelationforModify method");
        final List<CitizenRelation> newRelations = new ArrayList<CitizenRelation>();
        Boolean informantFlagLoc = Boolean.TRUE;
        Boolean relationFlagLoc = Boolean.TRUE;
        final Iterator<CitizenRelation> citRelationIterator = oldRelations.iterator();

        while (citRelationIterator.hasNext()) {

            final CitizenRelation relation = citRelationIterator.next();
            if (relation.getCit().getId().equals(deathRegistration.getCitizen().getId()))
                if (relation.getPerson().getId().equals(deathRegistration.getMother().getId())) {
                    relation.getPerson().updateCitizenName(deathRegistration.getMother());
                    newRelations.add(relation);
                } else if (deathRegistration.getDeceasedrelationType() != null
                        && relation.getPerson().getId().equals(deathRegistration.getDeceasedrelation().getId())) {
                    relation.setRelatedAs(deathRegistration.getDeceasedrelationType());
                    relation.getPerson().updateCitizenName(deathRegistration.getDeceasedrelation());
                    newRelations.add(relation);
                    relationFlagLoc = Boolean.FALSE;
                } else if (deathRegistration.getRelationType() != null
                        && deathRegistration.getRelationType().getRelatedAsConst() != null
                        && !deathRegistration.getRelationType().equals(deathRegistration.getDeceasedrelationType())
                        && !BndConstants.MOTHER.toUpperCase().equals(
                                deathRegistration.getRelationType().getRelatedAsConst()))
                    if (relation.getPerson().getId().equals(deathRegistration.getInformant().getId())) {
                        relation.getPerson().updateCitizenName(getInformantCitizen());
                        newRelations.add(relation);
                        informantFlagLoc = Boolean.FALSE;
                    }
        }
        if (informantFlagLoc && deathRegistration.getRelationType() != null
                && !deathRegistration.getRelationType().equals(deathRegistration.getDeceasedrelationType())
                && !BndConstants.MOTHER.toUpperCase().equals(deathRegistration.getRelationType().getRelatedAsConst()))
            newRelations.add(bndCommonService.populateCitizenRelation(deathRegistration.getCitizen(),
                    deathRegistration.getInformant(), deathRegistration.getRelationType().getRelatedAsConst()));

        if (deathRegistration.getRelationType() != null
                && relationFlagLoc
                && relationFlagLoc != null
                && deathRegistration.getDeceasedrelationType() != null
                && !deathRegistration.getRelationType().getId()
                .equals(deathRegistration.getDeceasedrelationType().getId())
                && !BndConstants.MOTHER.equals(deathRegistration.getRelationType().getRelatedAsConst()))
            newRelations.add(bndCommonService.populateCitizenRelation(deathRegistration.getCitizen(), deathRegistration
                    .getDeceasedrelation(), deathRegistration.getDeceasedrelationType().getRelatedAsConst()));
        deathRegistration.getCitizen().getRelations().clear();
        deathRegistration.getCitizen().getRelations().addAll(newRelations);
        LOGGER.debug("Completed buildCitizenRelationforModify method");
    }

    public void setHospitalDisease(final DeathRegistration deathreg) {

        LOGGER.debug("Started setHospitalDisease method");
        if (deathreg.getPlaceType().getDesc().equals(BndConstants.HOSPTIAL))
            addDropdownData(
                    "hospitalList",
                    bndCommonService.getHospitalByTypeAndUnit(deathreg.getEstablishment().getType().getId(), deathreg
                            .getRegistrarId().getRegUnitId().getId()));
        if (deathreg.getCitizenBDDetails().getCauseOfDeath() != null)
            if (deathreg.getCitizenBDDetails().getCauseOfDeath().getParent() != null) {
                deathreg.setCauseOfDeathParent(deathreg.getCitizenBDDetails().getCauseOfDeath().getParent());
                addDropdownData(
                        "diseaseList",
                        bndCommonService.getCauseofDeathbyParentId(deathreg.getCitizenBDDetails().getCauseOfDeath()
                                .getParent().getId()));
            } else
                deathreg.setCauseOfDeathParent(deathreg.getCitizenBDDetails().getCauseOfDeath());
        LOGGER.debug("Completed setHospitalDisease method");
    }

    @Override
    public void prepareView() {
        buildDeathRegistrationForViewModify();
    }

    @Override
    public void prepareBeforeEdit() {
        LOGGER.debug("Started prepareBeforeEdit method");
        buildDeathRegistrationForViewModify();
        setUpDeathRegistrationDetailsForValidation(deathRegistration);
        LOGGER.debug("Completed prepareBeforeEdit method");
    }

    @Override
    public void prepareInbox() {
        LOGGER.debug("Started prepareInbox method");
        buildDeathRegistrationForViewModify();
        setUpDeathRegistrationDetailsForValidation(deathRegistration);
        LOGGER.debug("Completed prepareInbox method");
    }

    @SuppressWarnings("unchecked")
    private void buildDeathRegistrationForViewModify() {
        LOGGER.debug("Started buildDeathRegistrationForViewModify method");

        deathRegistration = deathRegistrationService.getDeathRegistrationById(idTemp);
        deathRegistration.setMother(deathRegistration.getCitizen().getRelatedPerson(BndConstants.MOTHER.toUpperCase()));
        deathRegistration.setPermanentCitizenAddress(deathRegistration.getCitizen().getRelatedAddress(
                BndConstants.PERMANENTADDRESS));
        setPermanentAddressFlag(deathRegistration.getPermanentCitizenAddress() == null ? 0 : 1);
        deathRegistration.setDeceasedAddress(deathRegistration.getCitizen().getRelatedAddress(
                BndConstants.DECEASEDADDRESS));
        setDeceasedAddressFlag(deathRegistration.getDeceasedAddress() == null ? 0 : 1);
        if (deathRegistration.getEventAddress() == null)
            deathRegistration.setEventAddress(deathRegistration.getCitizen().getRelatedAddress(
                    BndConstants.EVENTADDRESS));
        if (deathRegistration.getInformant() != null) {
            deathRegistration.setRelationType(deathRegistration.getCitizen().getRelation(
                    deathRegistration.getInformant()));
            setInformantCitizen(deathRegistration.getInformant());
            deathRegistration.setInformantAddress(deathRegistration.getInformant().getRelatedAddress(
                    BndConstants.PRESENTADDRESS));
        }
        deathRegistration.setDeceasedUsualAddress(deathRegistration.getCitizen().getRelatedAddress(
                BndConstants.USUALADDRESS));

        getRelativeDetail(deathRegistration);

        if (deathRegistration.getAddictions() != null && !deathRegistration.getAddictions().isEmpty()) {
            for (final CitizenAddiction citaddiction : deathRegistration.getAddictions()) {
                final Addiction addiction = new Addiction();
                addiction.setDesc(citaddiction.getAddictedBy().getDesc());
                addiction.setNoOfYears(citaddiction.getNoOfYears());
                addictionList.add(addiction);
            }
            for (final Addiction addictiontemp : (List<Addiction>) bndCommonService.findAllBy("from Addiction")) {
                Boolean ispresent = false;
                for (final Addiction addiction : addictionList)
                    if (addiction.getDesc().equals(addictiontemp.getDesc())) {
                        ispresent = true;
                        break;
                    }
                if (!ispresent)
                    addictionList.add(addictiontemp);
            }

        } else
            for (final Addiction addictiontemp : (List<Addiction>) bndCommonService.findAllBy("from Addiction"))
                addictionList.add(addictiontemp);
        setPlaceTypeTemp(deathRegistration.getPlaceType().getDesc());
        setNameOfdeceasedFlag(deathRegistration.getCitizen().getName().contentEquals("NA") ? 0 : 1);
        setHospitalDisease(deathRegistration);
        LOGGER.debug("Completed buildDeathRegistrationForViewModify method");
    }

    public void getRelativeDetail(final DeathRegistration deathRegistration) {

        LOGGER.debug("Started getRelativeDetail method");
        relationList = deathRegistrationService.getRelativeDetails(deathRegistration);
        if (!relationList.isEmpty()) {
            if (relationList.size() == 1 && deathRegistration.getIsRelative()) {
                deathRegistration.setDeceasedrelationType(relationList.get(0).getRelatedAs());
                deathRegistration.setDeceasedrelation(relationList.get(0).getPerson());

            } else
                for (final CitizenRelation relation : relationList)
                    if (!deathRegistration.getInformant().getId().equals(relation.getPerson().getId()))
                        if (deathRegistration.getIsRelative()) {
                            deathRegistration.setDeceasedrelationType(relation.getRelatedAs());
                            deathRegistration.setDeceasedrelation(relation.getPerson());
                            break;
                        }
        } else
            deathRegistration.setDeceasedrelation(null);
        LOGGER.debug("Completed getRelativeDetail method");

    }

    protected void setUpDeathRegistrationDetailsForValidation(final DeathRegistration registration) {
        LOGGER.debug("Started setUpDeathRegistrationDetailsForValidation method");
        registration.setDeathHistory(registration.clone());
        placeTypeTempDeathHistory = placeTypeTemp;
        nameOfdeceasedFlagDeathHistory = nameOfdeceasedFlag;
        permanentAddressFlagDeathHistory = permanentAddressFlag;
        deceasedAddressFlagDeathHistory = deceasedAddressFlag;
        informantCitizenDeathHistory = informantCitizen;
        LOGGER.debug("Completed setUpDeathRegistrationDetailsForValidation method");
    }

    @Override
    protected String getPendingActions() {
        return deathRegistration == null ? "" : deathRegistration.getCurrentState() == null ? "" : deathRegistration
                .getCurrentState().getNextAction();
    }

    @Override
    public String getAdditionalRule() {
        final Long userId = deathRegistration.getCreatedBy() == null ? Long.valueOf(EgovThreadLocals.getUserId())
                : deathRegistration.getCreatedBy().getId();
        final List<String> roleList = bndCommonService.getRoleNamesByPassingUserId(userId);
        return BndRuleBook.HOSPITALUSER.equalsIgnoreCase(BndRuleBook.getInstance().getHighestPrivilegedRole(roleList)) ? BndRuleBook.HOSPITALUSER
                : "ALL";

    }

    @Override
    public String getNextAction() {
        LOGGER.debug("Completed buildHospitalDiseaseList method");
        WorkFlowMatrix wfMatrix = null;
        if (getModel().getId() != null) {
            if (getModel().getCurrentState() != null)
                wfMatrix = customizedWorkFlowService.getWfMatrix(getModel().getStateType(), getWorkFlowDepartment(),
                        getAmountRule(), getAdditionalRule(), getModel().getCurrentState().getValue(),
                        getPendingActions());
            // else
            // TODO egifix-state
            /*
             * wfMatrix =
             * customizedWorkFlowService.getWfMatrix(getModel().getStateType(),
             * getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
             * State.NEW, getPendingActions());
             */
        } else
            // TODO egifix-state
            /*
             * wfMatrix =
             * customizedWorkFlowService.getWfMatrix(getModel().getStateType(),
             * getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
             * State.NEW, getPendingActions());
             */
            LOGGER.debug("Completed buildHospitalDiseaseList method");
        return wfMatrix == null ? "" : wfMatrix.getNextAction();

    }

    public DeathRegistration getDeathRegistration() {
        return deathRegistration;
    }

    public void setDeathRegistration(final DeathRegistration deathRegistration) {
        this.deathRegistration = deathRegistration;
    }

    public Integer getNameOfdeceasedFlag() {
        return nameOfdeceasedFlag;
    }

    public void setNameOfdeceasedFlag(final Integer nameOfdeceasedFlag) {
        this.nameOfdeceasedFlag = nameOfdeceasedFlag;
    }

    public Integer getDeceasedAddressFlag() {
        return deceasedAddressFlag;
    }

    public void setDeceasedAddressFlag(final Integer deceasedAddressFlag) {
        this.deceasedAddressFlag = deceasedAddressFlag;
    }

    public DeathRegistrationService getDeathRegistrationService() {
        return deathRegistrationService;
    }

    public void setDeathRegistrationService(final DeathRegistrationService deathRegistrationService) {
        this.deathRegistrationService = deathRegistrationService;
    }

    public List<Addiction> getAddictionList() {
        return addictionList;
    }

    public void setAddictionList(final List<Addiction> addictionList) {
        this.addictionList = addictionList;
    }

    public Integer getDeathDelayedFlag() {
        return deathDelayedFlag;
    }

    public void setDeathDelayedFlag(final Integer deathDelayedFlag) {
        this.deathDelayedFlag = deathDelayedFlag;
    }

    public Integer getDeathHospitalUserFlag() {
        return deathHospitalUserFlag;
    }

    public void setDeathHospitalUserFlag(final Integer deathHospitalUserFlag) {
        this.deathHospitalUserFlag = deathHospitalUserFlag;
    }

    public Integer getDeathStatisticsInfoFlag() {
        return deathStatisticsInfoFlag;
    }

    public void setDeathStatisticsInfoFlag(final Integer deathStatisticsInfoFlag) {
        this.deathStatisticsInfoFlag = deathStatisticsInfoFlag;
    }

    public Integer getDeathStatusFlag() {
        return deathStatusFlag;
    }

    public void setDeathStatusFlag(final Integer deathStatusFlag) {
        this.deathStatusFlag = deathStatusFlag;
    }

    public Integer getDeathHospitalRegistrarFlag() {
        return deathHospitalRegistrarFlag;
    }

    @SkipValidation
    public String print() {

        reportId = generateCertificateService.generateCertificate(idTemp, BndConstants.SEARCHDEATH,
                getRoleNameByLoginUserId(), BndConstants.DEATHREGISTRATION_TEMPLATE, getSession());

        /*
         * ReportOutput reportOutput =
         * generateCertificateService.generateCertificateBirthDeathNA(idTemp,
         * BndConstants.SEARCHDEATH,getRoleNameByLoginUserId(),
         * BndConstants.DEATHREGISTRATION_TEMPLATE, getSession()); if
         * (reportOutput != null && reportOutput.getReportOutputData() != null)
         * deathCertificatePDF = new
         * ByteArrayInputStream(reportOutput.getReportOutputData()); return
         * PRINTDEATH;
         */

        /*
         * List<DeathRegistration> list=new ArrayList<DeathRegistration>();
         * deathRegistration =
         * deathRegistrationService.getDeathRegistrationById(idTemp);
         * list.add(deathRegistration); Integer userId =
         * deathRegistration.getCreatedBy
         * ()==null?Integer.valueOf(EgovThreadLocals
         * .getUserId()):deathRegistration.getCreatedBy().getId(); List<String>
         * roleList=bndCommonService.getRoleNamesByPassingUserId(userId); String
         * roleName
         * =BndRuleBook.getInstance().getHighestPrivilegedRole(roleList);
         * registrationMapObject =
         * mapForReportGeneration(deathRegistration,roleName); ReportRequest
         * reportInput = new
         * ReportRequest(BndConstants.DEATHREGISTRATION_TEMPLATE
         * ,list,registrationMapObject); ReportOutput reportOutput =
         * reportService.createReport(reportInput); reportId =
         * addingReportToSession(reportOutput);
         */

        return "report";
    }

    protected Integer addingReportToSession(final ReportOutput reportOutput) {
        return ReportViewerUtil.addReportToSession(reportOutput, getSession());
    }

    public InputStream getDeathCertificatePDF() {
        return deathCertificatePDF;
    }

    public void setDeathCertificatePDF(final InputStream deathCertificatePDF) {
        this.deathCertificatePDF = deathCertificatePDF;
    }

}
