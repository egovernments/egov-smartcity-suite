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
package org.egov.bnd.model;

import org.egov.bnd.utils.BndConstants;
import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.workflow.entity.StateAware;

import java.util.ArrayList;
import java.util.Date;

import static org.apache.commons.lang.StringUtils.isBlank;

public class Registration extends StateAware {

    private static final long serialVersionUID = -419639557628367259L;
    private Long id;
    private BnDCitizen citizen = null;
    private Date dateOfEvent;
    private char sex;
    private PlaceType placeType;
    private Address eventAddress;
    private Address regnAddress;
    private Establishment establishment;
    private BnDCitizen informant = null;
    private String registrationNo;
    private RegistrationUnit registrationUnit;
    private Date registrationDate;
    // have a place type with constants for town/village
    // should be part of address
    private Boolean isTown;
    private Integer addressEventID = null;
    private String remarks;
    private ArrayList revisionHistory = null;
    private CitizenBDDetails citizenBDDetails;
    private Registrar registrarId;
    private String glcNumber;
    private EgwStatus status; /*
     * Hospital registrar appoved status -- added by
     * pradeep
     */
    // private Map relations; //contains relationid as key and CCitizenRelation
    // object as value
    private String bndType = null;
    private String hospitalRegistrationNo; // Using this variable to save the
    // Hospital Temparary register no.
    private char isHistory = 'N';
    private Boolean isChildAdopted = Boolean.FALSE;
    private Boolean isCitizenKnown = Boolean.TRUE;
    private String isCertIssued = "N";
    private AdoptionDetails adoptionDetail;
    private String documentid;
    private Address informantAddress;
    private BnDCitizen mother;
    private BnDCitizen father;
    private Disease causeOfDeathParent;
    private String stateName;

    public String getAdditionalRule() {
        return additionalRule;
    }

    public void setAdditionalRule(final String additionalRule) {
        this.additionalRule = additionalRule;
    }

    private String additionalRule;

    public Disease getCauseOfDeathParent() {
        return causeOfDeathParent;
    }

    public void setCauseOfDeathParent(final Disease causeOfDeathParent) {
        this.causeOfDeathParent = causeOfDeathParent;
    }

    public Address getInformantAddress() {
        return informantAddress;
    }

    public void setInformantAddress(final Address informantAddress) {
        this.informantAddress = informantAddress;
    }

    public Boolean getIsCitizenKnown() {
        return isCitizenKnown;
    }

    public void setIsCitizenKnown(final Boolean isCitizenKnown) {
        this.isCitizenKnown = isCitizenKnown;
    }

    public Boolean getIsChildAdopted() {
        return isChildAdopted;
    }

    public void setIsChildAdopted(final Boolean isChildAdopted) {
        this.isChildAdopted = isChildAdopted;
    }

    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(final String documentid) {
        this.documentid = documentid;
    }

    public AdoptionDetails getAdoptionDetail() {
        return adoptionDetail;
    }

    public void setAdoptionDetail(final AdoptionDetails adoptionDetail) {
        this.adoptionDetail = adoptionDetail;
    }

    /**
     * @return Returns the isCertIssued.
     */
    public String getIsCertIssued() {
        return isCertIssued;
    }

    /**
     * @param isCertIssued
     *            The isCertIssued to set.
     */
    public void setIsCertIssued(final String isCertIssued) {
        this.isCertIssued = isCertIssued;
    }

    /**
     * @return Returns the isHistory.
     */
    public char getIsHistory() {
        return isHistory;
    }

    /**
     * @param isHistory
     *            The isHistory to set.
     */
    public void setIsHistory(final char isHistory) {
        this.isHistory = isHistory;
    }

    /**
     * @return Returns the bndType.
     */
    public String getBndType() {
        return bndType;
    }

    /**
     * @param bndType
     *            The bndType to set.
     */
    public void setBndType(final String bndType) {
        this.bndType = bndType;
    }

    public BnDCitizen getCitizen() {
        return citizen;
    }

    /**
     * @param citizen
     *            The citizen to set.
     */
    public void setCitizen(final BnDCitizen citizen) {
        this.citizen = citizen;
    }

    /**
     * @return Returns the eventAddress.
     */
    public Address getEventAddress() {
        return eventAddress;
    }

    /**
     * @param eventAddress
     *            The eventAddress to set.
     */
    public void setEventAddress(final Address eventAddress) {
        this.eventAddress = eventAddress;
    }

    /**
     * @return Returns the regnAddress.
     */
    public Address getRegnAddress() {
        return regnAddress;
    }

    /**
     * @param regnAddress
     *            The regnAddress to set.
     */
    public void setRegnAddress(final Address regnAddress) {
        this.regnAddress = regnAddress;
    }

    /**
     * @return Returns the placeType.
     */
    public PlaceType getPlaceType() {
        return placeType;
    }

    /**
     * @param placeType
     *            The placeType to set.
     */
    public void setPlaceType(final PlaceType placeType) {
        this.placeType = placeType;
    }

    /**
     * @return Returns the dateOfEvent.
     */
    public Date getDateOfEvent() {
        return dateOfEvent;
    }

    /**
     * @param dateOfEvent
     *            The dateOfEvent to set.
     */
    public void setDateOfEvent(final Date dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    /**
     * @return Returns the informant.
     */
    public BnDCitizen getInformant() {
        return informant;
    }

    /**
     * @param informant
     *            The informant to set.
     */
    public void setInformant(final BnDCitizen informant) {
        this.informant = informant;
    }

    /**
     * @return Returns the isTown.
     */
    public Boolean getIsTown() {
        return isTown;
    }

    /**
     * @param isTown
     *            The isTown to set.
     */
    public void setIsTown(final Boolean isTown) {
        this.isTown = isTown;
    }

    /**
     * @return Returns the establishment.
     */
    public Establishment getEstablishment() {
        return establishment;
    }

    /**
     * @param establishment
     *            The establishment to set.
     */
    public void setEstablishment(final Establishment establishment) {
        this.establishment = establishment;
    }

    /**
     * @return Returns the registrationDate.
     */
    public Date getRegistrationDate() {
        return registrationDate;
    }

    /**
     * @param registrationDate
     *            The registrationDate to set.
     */
    public void setRegistrationDate(final Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * @return Returns the registrationNo.
     */
    public String getRegistrationNo() {
        return registrationNo;
    }

    /**
     * @param registrationNo
     *            The registrationNo to set.
     */
    public void setRegistrationNo(final String registrationNo) {
        this.registrationNo = registrationNo;
    }

    /**
     * @return Returns the registrationUnit.
     */
    public RegistrationUnit getRegistrationUnit() {
        return registrationUnit;
    }

    /**
     * @param registrationUnit
     *            The registrationUnit to set.
     */
    public void setRegistrationUnit(final RegistrationUnit registrationUnit) {
        this.registrationUnit = registrationUnit;
    }

    /**
     * @return Returns the remarks.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     *            The remarks to set.
     */
    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return Returns the revisionHistory.
     */
    public ArrayList getRevisionHistory() {
        return revisionHistory;
    }

    /**
     * @param revisionHistory
     *            The revisionHistory to set.
     */
    public void setRevisionHistory(final ArrayList revisionHistory) {
        this.revisionHistory = revisionHistory;
    }

    /**
     * @return Returns the sex.
     */
    public char getSex() {
        return sex;
    }

    /**
     * @param sex
     *            The sex to set.
     */
    public void setSex(final char sex) {
        this.sex = sex;
    }

    /**
     * @return Returns the registrarId.
     */
    public Registrar getRegistrarId() {
        return registrarId;
    }

    /**
     * @param registrarId
     *            The registrarId to set.
     */
    public void setRegistrarId(final Registrar registrarId) {
        this.registrarId = registrarId;
    }

    /*
     * @return the relation associated with the informant object May not be
     * needed. Can use CCitizen.getRelation instead
     */
    public CRelation getInformantRelation() {
        return getCitizen().getRelation(getInformant());
        // return null;
    }

    /*
     * this method is not required. Use citizen.addRelation to update the
     * relation list public void setInformantRelation(CRelation rel) { //TODO:
     * update the relation map for the object with informant }
     */
    /**
     * @return Returns the citizenBDDetails.
     */
    public CitizenBDDetails getCitizenBDDetails() {
        return citizenBDDetails;
    }

    /**
     * @param citizenBDDetails
     *            The citizenBDDetails to set.
     */
    public void setCitizenBDDetails(final CitizenBDDetails citizenBDDetails) {
        this.citizenBDDetails = citizenBDDetails;
    }

    /**
     * @return Returns the addressEventID. used to delete event address id if
     *         its changed from hospital to home
     */
    public Integer getAddressEventID() {
        return addressEventID;
    }

    /**
     * @param addressEventID
     *            The addressEventID to set.
     */
    public void setAddressEventID(final Integer addressEventID) {
        this.addressEventID = addressEventID;
    }

    /**
     * @return Returns the glcNumber.
     */
    public String getGlcNumber() {
        return glcNumber;
    }

    /**
     * @param glcNumber
     *            The glcNumber to set.
     */
    public void setGlcNumber(final String glcNumber) {
        this.glcNumber = glcNumber;
    }

    /**
     * @return Returns the hospitalRegistrationNo.
     */
    public String getHospitalRegistrationNo() {
        return hospitalRegistrationNo;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public BnDCitizen getMother() {
        return mother;
    }

    public void setMother(final BnDCitizen mother) {
        this.mother = mother;
    }

    public BnDCitizen getFather() {
        return father;
    }

    public void setFather(final BnDCitizen father) {
        this.father = father;
    }

    /**
     * @param hospitalRegistrationNo
     *            The hospitalRegistrationNo to set.
     */
    public void setHospitalRegistrationNo(final String hospitalRegistrationNo) {
        this.hospitalRegistrationNo = hospitalRegistrationNo;
    }

    @Override
    public String getStateDetails() {
        // TODO Auto-generated method stub
        return null;
    }

    public StringBuffer getPlaceOfEventAddress() {
        Address address = new Address();
        if (getEventAddress() != null && !"".equals(getEventAddress()))
            address = getEventAddress();

        final StringBuffer placeOfEventAddress = new StringBuffer();
        if (getPlaceType() != null && !"".equals(getPlaceType())) {
            if (getPlaceType().getDesc().equals(BndConstants.PLACETYPEHOSPITAL)) {
                placeOfEventAddress.append("HOSPITAL:" + " ");
                placeOfEventAddress.append(getEstablishment().getName() + " ");
            } else if (getPlaceType().getDesc().equals(BndConstants.PLACETYPEHOUSE))
                placeOfEventAddress.append("HOUSE:" + " ");
            else if (getPlaceType().getDesc().equals(BndConstants.PLACETYPEOTHERS))
                placeOfEventAddress.append("OTHERS:" + " ");
            else if (getPlaceType().getDesc().equals(BndConstants.PLACETYPENOTSTATED))
                placeOfEventAddress.append("NOT STATED");

            if (address != null && !"".equals(address))
                placeOfEventAddress.append(getAddressValues(address));
        }
        return placeOfEventAddress;
    }

    public String getAddressValues(final Address addres) {
        final StringBuilder address = new StringBuilder();
        address.append(isBlank(addres.getHouseNoBldgApt()) ? "" : addres.getHouseNoBldgApt() + ",\n");
        // TODO streetAddres1, streetAddress2, block and taluk are changed in
        // Address
        // address.append(isBlank(addres.getStreetAddress1()) ? "" :
        // addres.getStreetAddress1()+",\n");
        // address.append(isBlank(addres.getStreetAddress2()) ? "" :
        // addres.getStreetAddress2()+",\n");
        // address.append(isBlank(addres.getBlock()) ? "" :
        // addres.getBlock()+",\n");
        address.append(isBlank(addres.getAreaLocalitySector()) ? "" : addres.getAreaLocalitySector() + "\n");
        address.append(isBlank(addres.getCityTownVillage()) ? "" : addres.getCityTownVillage() + ",\n");
        // address.append(isBlank(addres.getTaluk()) ? "" : addres.getTaluk()
        // +",\n");
        address.append(isBlank(addres.getDistrict()) ? "" : addres.getDistrict() + ",\n");
        address.append(isBlank(addres.getState()) ? "" : getStateName() + ",\n");
        address.append(addres.getPinCode() == null ? "" : "Pin : " + addres.getPinCode());
        return address.toString();
    }

    public StringBuffer getCitizenName() {
        final StringBuffer citizenName = getFullName(citizen);
        return citizenName;
    }

    public StringBuffer getFatherFullName() {
        StringBuffer citizenName = new StringBuffer();
        if (getIsChildAdopted()) {
            if (getAdoptionDetail() != null && !"".equals(getAdoptionDetail())
                    && getAdoptionDetail().getAdopteeFather() != null
                    && !"".equals(getAdoptionDetail().getAdopteeFather()))
                citizenName = getFullName(getAdoptionDetail().getAdopteeFather());
        } else
            for (final CitizenRelation citRel : citizen.getRelations())
                if (citRel.getRelatedAs().getDesc().equals(BndConstants.FATHER))
                    citizenName = getFullName(citRel.getPerson());
        return citizenName;
    }

    public StringBuffer getMotherFullName() {
        StringBuffer citizenName = new StringBuffer();
        if (getIsChildAdopted()) {
            if (getAdoptionDetail() != null && !"".equals(getAdoptionDetail())
                    && getAdoptionDetail().getAdopteeMother() != null
                    && !"".equals(getAdoptionDetail().getAdopteeMother()))
                citizenName = getFullName(getAdoptionDetail().getAdopteeMother());
        } else
            for (final CitizenRelation citRel : citizen.getRelations())
                if (citRel.getRelatedAs().getDesc().equals(BndConstants.MOTHER))
                    citizenName = getFullName(citRel.getPerson());
        return citizenName;
    }

    private StringBuffer getFullName(final BnDCitizen person) {
        final StringBuffer citizenName = new StringBuffer();

        // TODO egifix
        /*
         * if(person.getFirstName()!=null && !"".equals(person.getFirstName())){
         * citizenName.append(person.getFirstName()); citizenName.append(" "); }
         * if(person.getMiddleName()!=null &&
         * !"".equals(person.getMiddleName())){
         * citizenName.append(person.getMiddleName()); citizenName.append(" ");
         * } if(person.getLastName()!=null && !"".equals(person.getLastName())){
         * citizenName.append(person.getLastName()); }
         */

        return citizenName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(final String stateName) {
        this.stateName = stateName;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
