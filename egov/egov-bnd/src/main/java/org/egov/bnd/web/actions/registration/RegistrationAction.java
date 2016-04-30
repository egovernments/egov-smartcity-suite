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

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.bnd.client.utils.BndRuleBook;
import org.egov.bnd.model.BirthRegistration;
import org.egov.bnd.model.BnDCitizen;
import org.egov.bnd.model.CRelation;
import org.egov.bnd.model.CitizenRelation;
import org.egov.bnd.model.Registrar;
import org.egov.bnd.model.Registration;
import org.egov.bnd.services.common.NumberGenerationService;
import org.egov.bnd.services.registration.BirthRegistrationService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.utils.SexType;
import org.egov.bnd.web.actions.common.BndCommonAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.enums.AddressType;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.EgovThreadLocals;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Namespace("/registration")
@ParentPackage("egov")
@Transactional(readOnly = true)
public class RegistrationAction extends BndCommonAction {

    private static final long serialVersionUID = -8898085487142981653L;
    protected BirthRegistrationService birthRegistrationService;
    protected NumberGenerationService numberGenerationService;
    protected String roleName;
//    protected State defaultState;
    protected static final String WHITESPACE = " ";
    protected static final String OFFLINE = "offline";
    protected Set<Address> citizenAddressSet = new HashSet<Address>(0);
    protected Set<Address> fatherAddressSet = new HashSet<Address>(0);
    protected Set<Address> informantAddressSet = new HashSet<Address>(0);
    protected Set<Address> motherAddressSet = new HashSet<Address>(0);
    protected List<CitizenRelation> oldRelations = new ArrayList<CitizenRelation>(0);
    protected String workFlowType;

    // 1 means approve and 0 means create
    // Temporary variables to control role wise validation

    // This flag value decides whether registration will be created in approve
    // mode or in created mode
    protected Integer birthStatusFlag;
    // This flag value decides whether delayed registration is allowed or not
    protected Integer birthDelayedFlag;
    // This flag value decides whether Adoption details is allowed or not
    protected Integer birthAdoptionFlag;
    // This flag value decides whether statistical info is allowed or not
    protected Integer birthStatisticsInfoFlag;
    // This flag value decides whether user is a hospital user or not.
    protected Integer birthHospitalUserFlag;
    // This flag value decides whether user is hospital registrar or not.
    protected Integer birthHospitalRegistrarFlag;
    protected List<String> actionList;
    protected BnDCitizen informantCitizen;

    // Temporary variables
    protected Integer nameOfchildFlag;
    protected Integer permanentAddressFlag;
    protected Integer parentAddressFlag;
    protected String informantFlag;
    protected String placeTypeTemp;

    // Temporary variables for Death
    protected Integer nameOfdeceasedFlag;
    protected String relationType;
    protected Integer deceasedAddressFlag;

    // Temparary variables to save history
    protected Integer nameOfchildFlagBirthHistory;
    protected Integer permanentAddressFlagBirthHistory;
    protected Integer parentAddressFlagBirthHistory;
    protected String informantFlagBirthHistory;
    protected String relationTypeHistory;
    protected String placeTypeTempBirthHistory;
    protected BnDCitizen informantCitizenBirthHistory;
    protected String remarksHistory;

    // Temparary variables to save Death history
    protected Integer nameOfdeceasedFlagDeathHistory;
    protected Integer deceasedAddressFlagDeathHistory;
    protected Integer permanentAddressFlagDeathHistory;
    protected String placeTypeTempDeathHistory;
    protected BnDCitizen informantCitizenDeathHistory;

    protected AddressType usualAddressType; 
    protected AddressType eventAddressType;
    protected AddressType permanentAddressType;
    protected AddressType presentAddressType; 
    protected AddressType correspondingAddressType;
     
    protected CRelation fatherRelation;
    protected CRelation motherRelation;
    protected CRelation otherRelation;
    protected String gracePeriod;
    protected String registrationMode;
    protected String numberGenKey;

    /* for certificate */
    protected ReportService reportService;
    protected Integer reportId = -1;
    protected HashMap<String, Object> registrationMapObject = new HashMap<String, Object>();

    
    @Action(value = "/registratin-dummy", results = { @Result(name = "dummy", type = "dispatcher") })
    public String dummy() {
        return "dummy";
    }
    
    @Override
    public void prepare() {
        super.prepare();
        //persistenceService.findAllBy("from RegistrationUnit order by regUnitDesc")
        addDropdownData("registrationUnitList", Collections.EMPTY_LIST);
        addDropdownData("sexTypeList", Arrays.asList(SexType.values()));
        addDropdownData("diseaseList", Collections.EMPTY_LIST);
        
//         defaultState = bndCommonService.getStateByStateConstant(BndConstants.NASTATECONST);
    }

    @Transactional
    protected void setDifferentActionsForRole() {
        final BndRuleBook ruleBook = BndRuleBook.getInstance();
        final List<String> roleList = bndCommonService.getRoleNamesByPassingUserId(Long.valueOf(EgovThreadLocals
                .getUserId()));
        final Registrar registrar = bndCommonService.getRegistrarByLoggedInUser();
        final String role = ruleBook.getHighestPrivilegedRole(roleList);

        // If registration unit is main registration unit then assuming that
        // role is headquarterregistrar
        if (!roleList.isEmpty() && BndRuleBook.REGISTRAR.equalsIgnoreCase(role) && registrar != null
                && registrar.getRegUnitId() != null && registrar.getRegUnitId().getIsmainregunit() != null
                && registrar.getRegUnitId().getIsmainregunit())
            roleList.add(BndRuleBook.HEADQUATERREGISTRAR);

        actionList = ruleBook.getBirthActionsByRoles(roleList);

        // Setting all the flag values based on actions
        birthStatusFlag = actionList.contains(BndRuleBook.BIRTHSTATUSAPPROVED) ? 1 : 0;
        birthDelayedFlag = actionList.contains(BndRuleBook.BIRTHDELAYEDREG) ? 1 : 0;
        birthAdoptionFlag = actionList.contains(BndRuleBook.BIRTHADOPTIONDETAILS) ? 1 : 0;
        birthStatisticsInfoFlag = actionList.contains(BndRuleBook.BIRTHSTATISTICALINFO) ? 1 : 0;
        birthHospitalRegistrarFlag = BndRuleBook.HOSPITALREGISTRAR.equalsIgnoreCase(role) ? 1 : 0;
        birthHospitalUserFlag = BndRuleBook.HOSPITALUSER.equalsIgnoreCase(role) ? 1 : 0;

        if (birthHospitalUserFlag == 1 || birthHospitalRegistrarFlag == 1)
            gracePeriod = bndCommonService.getAppconfigValueResult(BndConstants.BNDMODULE, BndConstants.GRACEPERIODKEY,
                    "0");

        /*numberGenKey = bndCommonService.getAppconfigValueResult(BndConstants.BNDMODULE, BndConstants.NUMBERGENKEY,
                BndConstants.REGISTRATIONDATE);*/

    }

    @Transactional
    protected void getDetailsAddressTypeAndRelation() {
          usualAddressType =
          bndCommonService.getAddressType(BndConstants.USUALADDRESS) ;
          eventAddressType =
          bndCommonService.getAddressType(BndConstants.EVENTADDRESS);
          permanentAddressType =
          bndCommonService.getAddressType(BndConstants.PERMANENTADDRESS);
          presentAddressType =
          bndCommonService.getAddressType(BndConstants.PRESENTADDRESS);
          correspondingAddressType =
          bndCommonService.getAddressType(BndConstants.CORRESPONDINGADDRESS);
         
        fatherRelation = bndCommonService.getCRelationByRelatedConstant(BndConstants.FATHER.toUpperCase());
        motherRelation = bndCommonService.getCRelationByRelatedConstant(BndConstants.MOTHER.toUpperCase());
        otherRelation = bndCommonService.getCRelationByRelatedConstant(BndConstants.OTHER.toUpperCase());
    }

    protected void buildNewAddressFromOldAddress(final Set<Address> addressSet, final Address newAddress) {
        if (addressSet.isEmpty())
            addressSet.add(newAddress);
        /*else
           for (final Address address : addressSet)
               
                 * TODO PHOENIX
                 * if (address.getId().equals(newAddress.getId())) {
                  
                      address.setStreetRoadLine(newAddress.getStreetRoadLine());
                      address.setSubdistrict(newAddress.getSubdistrict());
                     
                    address.setCityTownVillage(newAddress.getCityTownVillage());
                    address.setPinCode(newAddress.getPinCode());
                    address.setDistrict(newAddress.getDistrict());
                    address.setState(newAddress.getState());
                    address.setType(newAddress.getType());
                    break;
                }*/
    }

    /**
     * This to load the form with hospital details.
     *
     * @param registration
     */
    @Transactional
    protected void buildNewFormForHospital(final Registration registration) {
        if (registration.getRegistrarId() == null)
            throw new EGOVRuntimeException(getMessage("user.registrar.error"));

        if (registration.getRegistrarId().getEstablishment() == null)
            throw new EGOVRuntimeException(getMessage("Hospital.registrar.error"));

        registration.setEstablishment(registration.getRegistrarId().getEstablishment());
        if (informantCitizen == null)
            informantCitizen = new BnDCitizen();
        informantCitizen.setName(registration.getEstablishment() == null ? "" : registration.getEstablishment()
                .getName());
        placeTypeTemp = BndConstants.HOSPTIAL;
        registration.setPlaceType(bndCommonService.getPlaceType(placeTypeTemp));

        /*
         * FIXME  PHOENIX
         * if (registration.getEventAddress() == null)
            registration.setEventAddress(new Address());
        if (registration.getInformantAddress() == null)
            registration.setInformantAddress(new Address());*/

        copyAddressDetails(registration.getEstablishment().getAddress(), registration.getEventAddress());
        copyAddressDetails(registration.getEstablishment().getAddress(), registration.getInformantAddress());
        informantFlag = BndConstants.OTHER;
        dropdownData.remove("hospitalList");
        addDropdownData("hospitalList", bndCommonService.getHospitalByTypeAndUnit(registration.getEstablishment()
                .getType().getId(), registration.getRegistrarId().getRegUnitId().getId()));

    }

    /**
     * @param addressNew
     * @param newAddress
     */

    private void copyAddressDetails(final Address oldAddress, final Address newAddress) {
      
          newAddress.setStreetRoadLine(oldAddress.getStreetRoadLine());
         newAddress.setSubdistrict(oldAddress.getSubdistrict());
        newAddress.setCityTownVillage(oldAddress.getCityTownVillage());
        newAddress.setPinCode(oldAddress.getPinCode());
        newAddress.setDistrict(oldAddress.getDistrict());
        newAddress.setState(oldAddress.getState());
    }

    /**
     * This API is to build citizen relation
     */

    protected void buildCitizenRelation(final BirthRegistration birthRegistration) {
        final List<CitizenRelation> newRelations = new ArrayList<CitizenRelation>();
        Boolean informantFlagLoc = Boolean.TRUE;
        // create mode
        if (oldRelations.isEmpty()) {
            birthRegistration.getFather().setGender(Gender.MALE);
            birthRegistration.getMother().setGender(Gender.FEMALE);
            newRelations.add(bndCommonService.populateCitizenRelation(birthRegistration.getCitizen(),
                    birthRegistration.getFather(), fatherRelation));
            newRelations.add(bndCommonService.populateCitizenRelation(birthRegistration.getCitizen(),
                    birthRegistration.getMother(), motherRelation));
            if (BndConstants.OTHER.equals(informantFlag)) {
                birthRegistration.setInformant(informantCitizen);
                newRelations.add(bndCommonService.populateCitizenRelation(birthRegistration.getCitizen(),
                        birthRegistration.getInformant(), otherRelation));
                informantFlagLoc = Boolean.FALSE;
            }
        }

        // modify mode
        final Iterator<CitizenRelation> citRelationIterator = oldRelations.iterator();

        while (citRelationIterator.hasNext()) {
            final CitizenRelation relation = citRelationIterator.next();
            if (relation.getCit().getId().equals(birthRegistration.getCitizen().getId()))
                if (relation.getPerson().getId().equals(birthRegistration.getFather().getId())) {
                    relation.getPerson().updateCitizenName(birthRegistration.getFather());
                    newRelations.add(relation);
                }

                else if (relation.getPerson().getId().equals(birthRegistration.getMother().getId())) {
                    relation.getPerson().updateCitizenName(birthRegistration.getMother());
                    newRelations.add(relation);
                }

                else if (informantFlag != null && !"".equals(informantFlag)
                        && !BndConstants.FATHER.equals(informantFlag) && !BndConstants.MOTHER.equals(informantFlag))
                    if (relation.getPerson().getId().equals(birthRegistration.getInformant().getId())) {
                        newRelations.add(relation);
                        informantFlagLoc = Boolean.FALSE;
                    }

        }
        if (informantFlagLoc && informantFlag != null && !"".equals(informantFlag)
                && !BndConstants.FATHER.equals(informantFlag) && !BndConstants.MOTHER.equals(informantFlag))
            newRelations.add(bndCommonService.populateCitizenRelation(birthRegistration.getCitizen(),
                    birthRegistration.getInformant(), otherRelation));

        birthRegistration.getCitizen().getRelations().clear();
        birthRegistration.getCitizen().getRelations().addAll(newRelations);

    }

    /**
     * This API is to build informant details
     */

    protected void buildInfomantDetails(final BirthRegistration birthRegistration) {
        if (informantFlag != null && !"".equals(informantFlag)) {
            buildNewAddressFromOldAddress(informantAddressSet, birthRegistration.getInformantAddress());
            if (BndConstants.FATHER.equals(informantFlag)) {
                birthRegistration.setInformant(birthRegistration.getCitizen().getRelatedPerson(
                        BndConstants.FATHER.toUpperCase()));
                fatherAddressSet.addAll(informantAddressSet);
            } else if (BndConstants.MOTHER.equals(informantFlag)) {
                birthRegistration.setInformant(birthRegistration.getCitizen().getRelatedPerson(
                        BndConstants.MOTHER.toUpperCase()));
                motherAddressSet.addAll(informantAddressSet);
            } else if (birthRegistration.getInformant() == null)
                birthRegistration.setInformant(informantCitizen);
            else
                birthRegistration.getInformant().updateCitizenName(informantCitizen);
        } else {
            birthRegistration.setInformant(null);
            birthRegistration.setInformantAddress(null);
        }

    }

    protected void buildAddress(final Registration birthRegistration) {
        // birthRegistration.getCitizen().getAddress().clear();
        birthRegistration.getCitizen().getAddress().addAll(citizenAddressSet);
        birthRegistration.getFather().getAddress().clear();
        birthRegistration.getFather().getAddress().addAll(fatherAddressSet);
        birthRegistration.getMother().getAddress().clear();
        birthRegistration.getMother().getAddress().addAll(motherAddressSet);
        if (birthRegistration.getInformant() != null && !BndConstants.FATHER.equals(informantFlag)
                && !BndConstants.MOTHER.equals(informantFlag)) {
            birthRegistration.getInformant().getAddress().clear();
            birthRegistration.getInformant().getAddress().addAll(informantAddressSet);
        }
    }

    protected void buildNewBirthForm(final Registration registration) {
        /*if (registration.getRegistrarId() == null)
            throw new EGOVRuntimeException(getMessage("user.registrar.error"));

        if (registration.getRegistrarId() != null && registration.getRegistrarId().getRegUnitId() == null)
            throw new EGOVRuntimeException(getMessage("user.registrationunit.error"));*/

        nameOfchildFlag = 1;
        permanentAddressFlag = 1;
        parentAddressFlag = 1;
        if (birthHospitalUserFlag == 1 || birthHospitalRegistrarFlag == 1)
            buildNewFormForHospital(registration);
    }

    protected void setUpRegistrationDetailsForValidation(final BirthRegistration registration) {
        birthHistory = registration.clone();
        informantFlagBirthHistory = informantFlag;
        placeTypeTempBirthHistory = placeTypeTemp;
        nameOfchildFlagBirthHistory = nameOfchildFlag;
        permanentAddressFlagBirthHistory = permanentAddressFlag;
        parentAddressFlagBirthHistory = parentAddressFlag;
        informantCitizenBirthHistory = informantCitizen;
    }

    public Map<Integer, String> getChildrenList() {
        return BndConstants.CHILDRENMAP;
    }

    public Integer getBirthHospitalRegistrarFlag() {
        return birthHospitalRegistrarFlag;
    }

    public String getRoleName() {
        return roleName;
    }

    public Map<Integer, String> getMotherAgeList() {
        return BndConstants.MOTHERAGEMAP;
    }

    public Map<Integer, String> getPregDurationList() {
        return BndConstants.PREGNANTDURATIONMAP;
    }

    public Map<Integer, String> getOptionMap() {
        return BndConstants.OPTIONMAP;
    }

    public List<String> getInformantRelationList() {
        return BndConstants.INFORMANTRELATIONLIST;
    }

    public Map<Integer, String> getDecisionMap() {
        return BndConstants.DECISIONNMAP;
    }

    public void setBirthRegistrationService(final BirthRegistrationService birthRegistrationService) {
        this.birthRegistrationService = birthRegistrationService;
    }

    public void setNumberGenerationService(final NumberGenerationService numberGenerationService) {
        this.numberGenerationService = numberGenerationService;
    }

    public Integer getNameOfchildFlag() {
        return nameOfchildFlag;
    }

    public void setNameOfchildFlag(final Integer nameOfchildFlag) {
        this.nameOfchildFlag = nameOfchildFlag;
    }

    public Integer getPermanentAddressFlag() {
        return permanentAddressFlag;
    }

    public void setPermanentAddressFlag(final Integer permanentAddressFlag) {
        this.permanentAddressFlag = permanentAddressFlag;
    }

    public Integer getParentAddressFlag() {
        return parentAddressFlag;
    }

    public void setParentAddressFlag(final Integer parentAddressFlag) {
        this.parentAddressFlag = parentAddressFlag;
    }

    public String getInformantFlag() {
        return informantFlag;
    }

    public void setInformantFlag(final String informantFlag) {
        this.informantFlag = informantFlag;
    }

    public String getPlaceTypeTemp() {
        return placeTypeTemp;
    }

    public void setPlaceTypeTemp(final String placeTypeTemp) {
        this.placeTypeTemp = placeTypeTemp;
    }

    public Integer getBirthStatusFlag() {
        return birthStatusFlag;
    }

    public Integer getBirthDelayedFlag() {
        return birthDelayedFlag;
    }

    public Integer getBirthAdoptionFlag() {
        return birthAdoptionFlag;
    }

    public Integer getBirthStatisticsInfoFlag() {
        return birthStatisticsInfoFlag;
    }

    public Integer getBirthHospitalUserFlag() {
        return birthHospitalUserFlag;
    }

    public BnDCitizen getInformantCitizen() {
        return informantCitizen;
    }

    public void setInformantCitizen(final BnDCitizen informantCitizen) {
        this.informantCitizen = informantCitizen;
    }

    public BnDCitizen getInformantCitizenBirthHistory() {
        return informantCitizenBirthHistory;
    }

    public void setInformantCitizenBirthHistory(final BnDCitizen informantCitizenBirthHistory) {
        this.informantCitizenBirthHistory = informantCitizenBirthHistory;
    }

    public Integer getNameOfchildFlagBirthHistory() {
        return nameOfchildFlagBirthHistory;
    }

    public void setNameOfchildFlagBirthHistory(final Integer nameOfchildFlagBirthHistory) {
        this.nameOfchildFlagBirthHistory = nameOfchildFlagBirthHistory;
    }

    public Integer getPermanentAddressFlagBirthHistory() {
        return permanentAddressFlagBirthHistory;
    }

    public void setPermanentAddressFlagBirthHistory(final Integer permanentAddressFlagBirthHistory) {
        this.permanentAddressFlagBirthHistory = permanentAddressFlagBirthHistory;
    }

    public Integer getParentAddressFlagBirthHistory() {
        return parentAddressFlagBirthHistory;
    }

    public void setParentAddressFlagBirthHistory(final Integer parentAddressFlagBirthHistory) {
        this.parentAddressFlagBirthHistory = parentAddressFlagBirthHistory;
    }

    public String getInformantFlagBirthHistory() {
        return informantFlagBirthHistory;
    }

    public void setInformantFlagBirthHistory(final String informantFlagBirthHistory) {
        this.informantFlagBirthHistory = informantFlagBirthHistory;
    }

    public String getPlaceTypeTempBirthHistory() {
        return placeTypeTempBirthHistory;
    }

    public void setPlaceTypeTempBirthHistory(final String placeTypeTempBirthHistory) {
        this.placeTypeTempBirthHistory = placeTypeTempBirthHistory;
    }

    public BirthRegistration getBirthHistory() {
        return birthHistory;
    }

    public void setBirthHistory(final BirthRegistration birthHistory) {
        this.birthHistory = birthHistory;
    }

    protected BirthRegistration birthHistory = new BirthRegistration();

    public String getWorkFlowType() {
        return workFlowType;
    }

    public void setWorkFlowType(final String workFlowType) {
        this.workFlowType = workFlowType;
    }

    public String getRegistrationMode() {
        return registrationMode;
    }

    public void setRegistrationMode(final String registrationMode) {
        this.registrationMode = registrationMode;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public void setReportId(final Integer reportId) {
        this.reportId = reportId;
    }

    public HashMap<String, Object> getRegistrationMapObject() {
        return registrationMapObject;
    }

    public void setRegistrationMapObject(final HashMap<String, Object> registrationMapObject) {
        this.registrationMapObject = registrationMapObject;
    }

    public Integer getNameOfdeceasedFlagDeathHistory() {
        return nameOfdeceasedFlagDeathHistory;
    }

    public void setNameOfdeceasedFlagDeathHistory(final Integer nameOfdeceasedFlagDeathHistory) {
        this.nameOfdeceasedFlagDeathHistory = nameOfdeceasedFlagDeathHistory;
    }

    public Integer getDeceasedAddressFlagDeathHistory() {
        return deceasedAddressFlagDeathHistory;
    }

    public void setDeceasedAddressFlagDeathHistory(final Integer deceasedAddressFlagDeathHistory) {
        this.deceasedAddressFlagDeathHistory = deceasedAddressFlagDeathHistory;
    }

    public Integer getPermanentAddressFlagDeathHistory() {
        return permanentAddressFlagDeathHistory;
    }

    public void setPermanentAddressFlagDeathHistory(final Integer permanentAddressFlagDeathHistory) {
        this.permanentAddressFlagDeathHistory = permanentAddressFlagDeathHistory;
    }

    public String getPlaceTypeTempDeathHistory() {
        return placeTypeTempDeathHistory;
    }

    public void setPlaceTypeTempDeathHistory(final String placeTypeTempDeathHistory) {
        this.placeTypeTempDeathHistory = placeTypeTempDeathHistory;
    }

    public BnDCitizen getInformantCitizenDeathHistory() {
        return informantCitizenDeathHistory;
    }

    public void setInformantCitizenDeathHistory(final BnDCitizen informantCitizenDeathHistory) {
        this.informantCitizenDeathHistory = informantCitizenDeathHistory;
    }

    public void setRemarksHistory(final String remarksHistory) {
        this.remarksHistory = remarksHistory;
    }

    public String getGracePeriod() {
        return gracePeriod;
    }

    public String getNumberGenKey() {
        return numberGenKey;
    }

}
