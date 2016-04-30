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
package org.egov.bnd.web.actions.common;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.bnd.client.utils.BndRuleBook;
import org.egov.bnd.model.City;
import org.egov.bnd.model.Disease;
import org.egov.bnd.model.Establishment;
import org.egov.bnd.model.RegKeys;
import org.egov.bnd.model.Registrar;
import org.egov.bnd.model.Registration;
import org.egov.bnd.model.RegistrationUnit;
import org.egov.bnd.services.common.BndCommonService;
import org.egov.bnd.services.masters.AdoptionInstituteService;
import org.egov.bnd.services.masters.EstablishmentService;
import org.egov.bnd.services.masters.RegKeyService;
import org.egov.bnd.services.masters.RegistrarService;
import org.egov.bnd.services.masters.RegistrationUnitService;
import org.egov.bnd.services.registration.BirthRegistrationService;
import org.egov.bnd.services.registration.DeathRegistrationService;
import org.egov.bnd.services.reports.PaymentReportService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.utils.BndDateUtils;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Results({ @Result(name = AjaxCommonAction.AJAX_RESULT, type = "stream", location = "returnStream", params = {
        "contentType", "text/plain" }) })
@SuppressWarnings("unchecked" )
@Transactional(readOnly = true)
@Namespace("/common")
public class AjaxCommonAction extends BaseFormAction {

    private static final long serialVersionUID = 1129396596379121136L;
    public static final String AJAX_RESULT = "ajaxResult";
    public static final String BIRTHREGNOCHECK = "birthRegNoCheck";
    public static final String DEATHREGNOCHECK = "deathRegNoCheck";
    public static final String EMPTYSTRING = "";
    public static final String ADDSTRING = "+";
    public static final String HYPEN = "-";
    public static final String ADOPTIONINSTITUTECODECHECK = "adoptionInstituteCodeCheck";
    public static final String ADOPTIONINSTITUTENAMECHECK = "adoptionInstituteNameCheck";
    public static final String RECEIPTNUMBERCHECK = "validateReceiptNumber";
    public static final String USERIDCHECK = "useIdCheck";
    public static final String HOSPITALNAMECHECK = "hospitalNameCheck";
    public static final String REGUNITDESCCHECK = "regUnitDescCheck";
    public static final String REGUNITCONSTCHECK = "regUnitConstCheck";
    private String returnStream = EMPTYSTRING;
    private List<Establishment> hospitalList = Collections.emptyList();
    private List<Disease> diseaseList = Collections.emptyList();
    private BndCommonService bndCommonService;
    private BirthRegistrationService birthRegistrationService;
    private DeathRegistrationService deathRegistrationService;
    private AdoptionInstituteService adoptionInstituteService;
    private EstablishmentService establishmentService;
    private RegistrationUnitService registrationUnitService;
    private RegistrarService registrarService;
    private RegKeyService regKeyService;
    private PaymentReportService paymentReportService;
    private String addressType;
    private Integer hospitalType;
    private Long regUnit;
    private Integer establishmentId;
    private String eventyear;
    private Long parentDeathCauseId;
    private Long birthRegid;
    private Long deathRegid;
    private String regNo;
    private Date birthDate;
    private Date deathDate;
    private Long regId;
    private String registrationType;
    private String arguments;
    // private List<Taluk> talukList = Collections.emptyList();
    private Integer talukId;
    private List<City> cityList = Collections.emptyList();
    private String institutionCode;
    private String institutionName;
    private String name;
    private String regUnitDesc;
    private List<Role> roleList = Collections.EMPTY_LIST;
    private Long userId;
    private String regUnitConst;
    private String role;
    private Long id;
    private String receiptNum;
    private final SimpleDateFormat sf = new SimpleDateFormat("yyyy");
    private Boolean hospitalRegistered;

    
    @Action(value = "/ajaxCommon-dummyform", results = { @Result(name = "dummy", type = "dispatcher") })
    public String dummyform() {
        return "dummy";
    }
    
    public Boolean getHospitalRegistered() {
        return hospitalRegistered;
    }

    public void setHospitalRegistered(final Boolean hospitalRegistered) {
        this.hospitalRegistered = hospitalRegistered;
    }

    public String getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(final String receiptNum) {
        this.receiptNum = receiptNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public String getRegUnitDesc() {
        return regUnitDesc;
    }

    public void setRegUnitDesc(final String regUnitDesc) {
        this.regUnitDesc = regUnitDesc;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(final String institutionName) {
        this.institutionName = institutionName;
    }

    public RegKeyService getRegKeyService() {
        return regKeyService;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(final String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public void setTalukId(final Integer talukId) {
        this.talukId = talukId;
    }

    public void setDistrictId(final Integer districtId) {
    }

    public Long getRegId() {
        return regId;
    }

    public void setRegId(final Long regId) {
        this.regId = regId;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(final String registrationType) {
        this.registrationType = registrationType;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(final String arguments) {
        this.arguments = arguments;
    }

    public void setDeathDate(final Date deathDate) {
        this.deathDate = deathDate;
    }

    public void setBirthDate(final Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setBirthRegid(final Long birthRegid) {
        this.birthRegid = birthRegid;
    }

    public void setRegNo(final String regNo) {
        this.regNo = regNo;
    }

    public Long getParentDeathCauseId() {
        return parentDeathCauseId;
    }

    public void setParentDeathCauseId(final Long parentDeathCauseId) {
        this.parentDeathCauseId = parentDeathCauseId;
    }

    public void setEstablishmentId(final Integer establishmentId) {
        this.establishmentId = establishmentId;
    }

    public List<Establishment> getHospitalList() {
        return hospitalList;
    }

    // TODO egifix
    /*
     * public List<Taluk> getTalukList() { return talukList; } public String
     * getTalukNameByDistrict() { talukList =
     * bndCommonService.getTalukByDistrictId(districtId); return "talukNames"; }
     */

    @Transactional
    public String getCityNameByTaluk() {
        cityList = bndCommonService.getCityByTalukId(talukId);
        return "cityNames";
    }

    @Transactional
    public String getRoleNamesByUserID() {
        final List<String> appConfigBnDRoleList = bndCommonService.getAppconfigActualValue(BndConstants.BNDMODULE,
                BndConstants.BND_ROLE);
        roleList = bndCommonService.getRoleNamesByUserId(userId, appConfigBnDRoleList);
        return "roleNames";
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setHospitalType(final Integer hospitalType) {
        this.hospitalType = hospitalType;
    }

    public void setAddressType(final String addressType) {
        this.addressType = addressType;
    }

    @Override
    public Object getModel() {
        return null;
    }

    @Transactional
    public String populateDefaultAddress() {
        final Address address = bndCommonService.getDefaultAddress(addressType);
        populateAddressString(address);
        return AJAX_RESULT;
    }

    private Address address;

    public Address getAddress() {
        return address;
    }

    @Transactional
    public String populateAddressByEstablishmentJson() {

        final Establishment establishment = bndCommonService.getEstablishmentById(establishmentId);
        address = establishment == null ? null : establishment.getAddress();
        if (getEventyear() != null && getEventyear().equals(sf.format(new Date()))) {
            if (establishment.getIsAuth())
                setHospitalRegistered(true);
            else
                setHospitalRegistered(false);
        } else
            setHospitalRegistered(false);
        return "establishmentAddress";
    }

    @Transactional
    public String populateAddressByEstablishment() {

        final Establishment establishment = bndCommonService.getEstablishmentById(establishmentId);
        final Address address = establishment == null ? null : establishment.getAddress();

        if (getEventyear() != null && getEventyear().equals(sf.format(new Date()))) {
            if (establishment.getIsAuth()) {
                returnStream = new String();
                returnStream = "HOSPITALREGISTERED";
            } else
                populateAddressString(address);
        } else
            populateAddressString(address);
        return AJAX_RESULT;
    }

    public void populateAddressString(final Address address) {
        StringBuffer addressString = new StringBuffer(EMPTYSTRING);
        if (address != null) {
            //addressString = addressString.append(address.getId());
            addressString = addressString.append(ADDSTRING);
            // TODO egifix
            /*
             * addressString = addressString.append(address.getStreetAddress1()
             * == null ? EMPTYSTRING : address .getStreetAddress1());
             * addressString = addressString.append(ADDSTRING); addressString =
             * addressString.append(address.getStreetAddress2() == null ?
             * EMPTYSTRING : address .getStreetAddress2()); addressString =
             * addressString.append(ADDSTRING); addressString =
             * addressString.append(address.getTaluk() == null ? EMPTYSTRING :
             * address.getTaluk()); addressString =
             * addressString.append(ADDSTRING);
             */

            addressString = addressString.append(address.getCityTownVillage() == null ? EMPTYSTRING : address
                    .getCityTownVillage());
            addressString = addressString.append(ADDSTRING);
            addressString = addressString.append(address.getPinCode() == null ? EMPTYSTRING : address.getPinCode());
            addressString = addressString.append(ADDSTRING);
            addressString = addressString.append(address.getDistrict() == null ? EMPTYSTRING : address.getDistrict());
            addressString = addressString.append(ADDSTRING);
            addressString = addressString.append(address.getState());
        } else {
            addressString = addressString.append(EMPTYSTRING);
            addressString = addressString.append(ADDSTRING);
            addressString = addressString.append(EMPTYSTRING);
            addressString = addressString.append(ADDSTRING);
            addressString = addressString.append(EMPTYSTRING);
            addressString = addressString.append(ADDSTRING);
            addressString = addressString.append(EMPTYSTRING);
            addressString = addressString.append(ADDSTRING);
            addressString = addressString.append(EMPTYSTRING);
            addressString = addressString.append(ADDSTRING);
            addressString = addressString.append(EMPTYSTRING);
            addressString = addressString.append(ADDSTRING);
            addressString = addressString.append(EMPTYSTRING);
            addressString = addressString.append(ADDSTRING);
            addressString = addressString.append(EMPTYSTRING);
        }
        returnStream = addressString.toString();
    }

    public String uniqueBirthRegNumberCheck() {
        return BIRTHREGNOCHECK;
    }

    public String uniqueAdoptionInstitutecodeCheck() {

        return ADOPTIONINSTITUTECODECHECK;
    }

    public String uniqueHospitalNameCheck() {
        return HOSPITALNAMECHECK;
    }

    // to check uniquness of registration unit desc
    public String uniqueRegUnitDescCheck() {
        return REGUNITDESCCHECK;
    }

    @Transactional
    public Boolean getRegUnitDescUniqueCheck() {
        return registrationUnitService.checkUniqueRegUnitDesc(regUnitDesc);
    }

    // to check uniquness of registration unit const (LCN no)
    public String uniqueRegUnitConstCheck() {
        return REGUNITCONSTCHECK;
    }

    @Transactional
    public Boolean getRegUnitConstUniqueCheck() {
        return registrationUnitService.checkUniqueRegUnitConst(regUnitConst);
    }

    public String uniqueAdoptionInstitutenameCheck() {
        return ADOPTIONINSTITUTENAMECHECK;

    }

    public String uniqueUserIdCheck() {
        return USERIDCHECK;
    }

    @Transactional
    public Boolean getAdoptionInstituteCodeUniqueCheck() {
        return adoptionInstituteService.checkUniqueAdoptionInstituteCode(institutionCode);
    }

    @Transactional
    public Boolean getHospitalNameUniqueCheck() {
        return establishmentService.checkUniqueHospitalName(name);
    }

    @Transactional
    public Boolean getAdoptionInstituteNameUniqueCheck() {
        return adoptionInstituteService.checkUniqueAdoptionInstituteName(institutionName);
    }

    @Transactional
    public Boolean getUserIdUniqueCheck() {
        return registrarService.checkUniqueUserId(userId);
    }

    public String uniqueDeathRegNumberCheck() {
        return DEATHREGNOCHECK;
    }

    @Transactional
    public String populateRegistrarDetails() {

        if (userId != null && !"".equals(userId)) {
            final Registrar registrar = bndCommonService.getRegistrarByUserId(userId);
            if (registrar == null)
                returnStream = EMPTYSTRING + ADDSTRING + EMPTYSTRING;
            else {
                final StringBuffer returnString = new StringBuffer(EMPTYSTRING);
                returnString.append(registrar.getRegUnitId().getId());
                returnString.append(ADDSTRING);
                if (registrar.getEstablishment() != null && !EMPTYSTRING.equals(registrar.getEstablishment()))
                    returnString.append(registrar.getEstablishment().getId());
                returnStream = returnString.toString();
            }

        }
        return AJAX_RESULT;
    }

    private RegKeys regNumRange;
    private Boolean regNoUniqueCheck;
    private static final String REGNUMRANGE = "regnumRange";

    public RegKeys getRegNumRange() {
        return regNumRange;
    }

    public Boolean getRegNoUniqueCheck() {
        return regNoUniqueCheck;
    }

    @Transactional
    public String populateBirthRegNumRange() {
        regNoUniqueCheck = birthRegistrationService.checkUniqueRegistrationNumber(regUnit, birthRegid, regNo,
                birthDate, BndConstants.BIRTH);
        if (regUnit != null && regUnit != -1 && !regNoUniqueCheck) {
            final RegistrationUnit regUnitObj = bndCommonService.getRegistrationUnitById(regUnit);
            final StringBuffer objectType = new StringBuffer(EMPTYSTRING);
            objectType.append(BndConstants.BIRTH);
            objectType.append(HYPEN);
            objectType.append(regUnitObj.getRegUnitConst());
            objectType.append(HYPEN);
            objectType.append(BndDateUtils.getCurrentYear(birthDate));
            regNumRange = regKeyService.getRegKeyByRegUnitAndDate(regUnitObj, BndDateUtils.getCurrentYear(birthDate),
                    objectType.toString());
        }
        return REGNUMRANGE;
    }

    @Transactional
    public String populateStillBirthNumRange() {
        regNoUniqueCheck = birthRegistrationService.checkUniqueRegistrationNumber(regUnit, birthRegid, regNo,
                birthDate, BndConstants.STILLBIRTH);
        if (regUnit != null && regUnit != -1 && !regNoUniqueCheck) {
            final RegistrationUnit regUnitObj = bndCommonService.getRegistrationUnitById(regUnit);
            final StringBuffer objectType = new StringBuffer(EMPTYSTRING);
            objectType.append(BndConstants.STILLBIRTHNUM);
            objectType.append(HYPEN);
            objectType.append(regUnitObj.getRegUnitConst());
            objectType.append(HYPEN);
            objectType.append(BndDateUtils.getCurrentYear(birthDate));
            regNumRange = regKeyService.getRegKeyByRegUnitAndDate(regUnitObj, BndDateUtils.getCurrentYear(birthDate),
                    objectType.toString());
        }
        return REGNUMRANGE;
    }

    @Transactional
    public Boolean getDeathRegNoUniqueCheck() {
        return deathRegistrationService.checkUniqueDeathRegistrationNumber(regUnit, deathRegid, regNo, deathDate);
    }

    @Transactional
    public String populateDeathRegNumRange() {
        regNoUniqueCheck = deathRegistrationService.checkUniqueDeathRegistrationNumber(regUnit, deathRegid, regNo,
                deathDate);
        if (regUnit != null && regUnit != -1 && !regNoUniqueCheck) {
            final RegistrationUnit regUnitObj = bndCommonService.getRegistrationUnitById(regUnit);
            final StringBuffer objectType = new StringBuffer(EMPTYSTRING);
            objectType.append(BndConstants.DEATH);
            objectType.append(HYPEN);
            objectType.append(regUnitObj.getRegUnitConst());
            objectType.append(HYPEN);
            objectType.append(BndDateUtils.getCurrentYear(deathDate));
            regNumRange = regKeyService.getRegKeyByRegUnitAndDate(regUnitObj, BndDateUtils.getCurrentYear(deathDate),
                    objectType.toString());

        }
        return REGNUMRANGE;
    }

    @Transactional
    public String ajaxLoadActions() {
        String message = "noAuthority";
        Registration reg = null;
        if (registrationType.equals(BndConstants.SEARCHDEATH))
            reg = deathRegistrationService.getDeathRegistrationById(regId);
        else
            reg = birthRegistrationService.getBirthRegistrationById(regId);
        final Boolean lockStatusFlag = reg.getStatus().getCode().equals(BndConstants.LOCK) ? Boolean.TRUE
                : Boolean.FALSE;
        final Registrar registrar = bndCommonService.getRegistrarByLoggedInUser();
        final BndRuleBook ruleBook = BndRuleBook.getInstance();
        final List<String> actionList = ruleBook.getSearchActionsByRoles(
                bndCommonService.getRoleNamesByPassingUserId(Long.valueOf(EgovThreadLocals.getUserId())), registrar,
                reg);

        if (!lockStatusFlag)
            if (arguments.equals(BndConstants.VIEW) && actionList.contains(BndConstants.VIEW))
                message = "performView";
            else if (arguments.equals(BndConstants.SIDELETTER) && actionList.contains(BndConstants.SIDELETTER)
                    && registrationType.equals(BndConstants.SEARCHBIRTH)) {
                if (BndDateUtils.isSideLetter(reg.getDateOfEvent()))
                    message = "performSideLetter";
            }

            else if (arguments.equals(BndConstants.UPDATE) && actionList.contains(BndConstants.UPDATE))
                message = "performUpdate";
            else if (arguments.contains("Adoption") && actionList.contains(BndConstants.CHILDADOPTION)) {
                if (reg.getIsChildAdopted())
                    message = "childAdopted";
                else
                    message = "performAdoption";

            } else if (arguments.equals(BndConstants.CERTIFICATEGENERATIONFORBIRTH)
                    && actionList.contains(BndConstants.CERTIFICATEGENERATIONFORBIRTH))
                message = "performCertificate";
            else if (arguments.equals(BndConstants.CERTIFICATEGENERATION)
                    && actionList.contains(BndConstants.CERTIFICATEGENERATION))
                message = "performCertificate";
            else if (arguments.equals(BndConstants.NAMEINCLUSION) && actionList.contains(BndConstants.NAMEINCLUSION))
                message = "performNameInclusion";
        if (arguments.equals(BndConstants.LOCKRECORD) && actionList.contains(BndConstants.LOCKRECORD)) {
            if (reg.getStatus().getCode().equals(BndConstants.APPROVED))
                message = "performLock";
            else if (lockStatusFlag)
                message = "alreadyLocked";
        } else if (arguments.equals(BndConstants.UNLOCKRECORD) && actionList.contains(BndConstants.UNLOCKRECORD)) {
            if (lockStatusFlag)
                message = "performUnlock";
            if (reg.getStatus().getCode().equals(BndConstants.APPROVED))
                message = "notLocked";
        }

        returnStream = message;
        return AJAX_RESULT;
    }

    public void setRegKeyService(final RegKeyService regKeyService) {
        this.regKeyService = regKeyService;
    }

    @Transactional
    public String getHospitalNameByType() {
        hospitalList = bndCommonService.getHospitalByTypeAndUnit(hospitalType, regUnit);
        return "hospitalNames";
    }

    @Transactional
    public String getHospitalNameByRole() {
        hospitalList = bndCommonService.getHospitalByRoleAndUnit(role, regUnit);
        return "hospitalNames";
    }

    public InputStream getReturnStream() {
        return new ByteArrayInputStream(returnStream.getBytes());

    }

    public void setReturnStream(final String xmlStream) {
        returnStream = xmlStream;
    }

    public void setBndCommonService(final BndCommonService bndCommonService) {
        this.bndCommonService = bndCommonService;
    }

    public void setBirthRegistrationService(final BirthRegistrationService birthRegistrationService) {
        this.birthRegistrationService = birthRegistrationService;
    }

    @Transactional
    public String getDeathcauseByParent() {
        diseaseList = bndCommonService.getCauseofDeathbyParentId(parentDeathCauseId);
        return "deathCause";
    }

    public void setAdoptionInstituteService(final AdoptionInstituteService adoptionInstituteService) {
        this.adoptionInstituteService = adoptionInstituteService;
    }

    public AdoptionInstituteService getAdoptionInstituteService() {
        return adoptionInstituteService;
    }

    public PaymentReportService getPaymentReportService() {
        return paymentReportService;
    }

    public void setPaymentReportService(final PaymentReportService paymentReportService) {
        this.paymentReportService = paymentReportService;
    }

    public EstablishmentService getEstablishmentService() {
        return establishmentService;
    }

    public void setEstablishmentService(final EstablishmentService establishmentService) {
        this.establishmentService = establishmentService;
    }

    public RegistrationUnitService getRegistrationUnitService() {
        return registrationUnitService;
    }

    public void setRegistrationUnitService(final RegistrationUnitService registrationUnitService) {
        this.registrationUnitService = registrationUnitService;
    }

    public RegistrarService getRegistrarService() {
        return registrarService;
    }

    public void setRegistrarService(final RegistrarService registrarService) {
        this.registrarService = registrarService;
    }

    public List<Disease> getDiseaseList() {
        return diseaseList;
    }

    public void setDiseaseList(final List<Disease> diseaseList) {
        this.diseaseList = diseaseList;
    }

    public DeathRegistrationService getDeathRegistrationService() {
        return deathRegistrationService;
    }

    public void setDeathRegistrationService(final DeathRegistrationService deathRegistrationService) {
        this.deathRegistrationService = deathRegistrationService;
    }

    public Long getRegUnit() {
        return regUnit;
    }

    public void setRegUnit(final Long regUnit) {
        this.regUnit = regUnit;
    }

    public String getRegUnitConst() {
        return regUnitConst;
    }

    public void setRegUnitConst(final String regUnitConst) {
        this.regUnitConst = regUnitConst;
    }

    public String uniqueValidateReceiptNumber() {
        return RECEIPTNUMBERCHECK;

    }

    @Transactional
    public Boolean getValidateReceiptNumber() {
        return paymentReportService.validateReceiptNumber(id, receiptNum);

    }

    public String getEventyear() {
        return eventyear;
    }

    public void setEventyear(final String eventyear) {
        this.eventyear = eventyear;
    }
}
