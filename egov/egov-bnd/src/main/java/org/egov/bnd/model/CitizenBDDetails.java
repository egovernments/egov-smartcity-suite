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
/* Static Model */
package org.egov.bnd.model;

import java.io.Serializable;

/**
 * @author Manas This class is the POJO for CitizenBirthnDeathDetails. An
 *         CitizenBirthnDeathDetails in a birth and death system keeps track of
 *         details regarding citizen's birth/death.
 * @hibernate.class table="EGBD_CITIZENBDDETAILS"
 **/
public class CitizenBDDetails implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1204644696158738756L;

    public CitizenBDDetails()

    {
        super();
    }

    private Integer id;
    private Integer ageMomMarriage;
    private Integer ageMomBirth;
    private Integer noOfChildren;
    private Religion religion = null;
    private Education fatherEducation = null;
    private Education motherEducation = null;
    private Occupation fatherOccupation = null;
    private Occupation motherOccupation = null;
    private Occupation occupation = null;
    private Character isStillBirth = 'N';
    // private CICDClassification causeOfDeath;
    private Disease causeOfDeath;
    private Occupation informantOccupation = null;

    // private String nationality ;
    /**
     * @return Returns the nationality.
     */
    /*
     * public String getNationality() { return nationality; }
     */
    /**
     * @param nationality
     *            The nationality to set.
     */
    /*
     * public void setNationality(String nationality) { this.nationality =
     * nationality; }
     */
    /**
     * @return Returns the causeOfDeath.
     */
    public Disease getCauseOfDeath() {

        return causeOfDeath;
    }

    /**
     * @param causeOfDeath
     *            The causeOfDeath to set.
     */
    public void setCauseOfDeath(final Disease causeOfDeath) {

        this.causeOfDeath = causeOfDeath;

    }

    private long citizenId;

    /**
     * @return Returns the id.
     * @hibernate.id name="id" generator-class="native"
     * @hibernate.column name="EGBD_CITIZENDETAILSID"
     */
    public Integer getId() {

        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(final Integer id) {

        this.id = id;
    }

    /**
     * @return Returns the fatherEducation.
     * @hibernate.many-to-one column="FATHEREDUMASTERID"
     *                        class="com.egov.citizen.client.CEducation"
     *                        cascade="none" unique="false"
     */
    public Education getFatherEducation() {
        return fatherEducation;
    }

    /**
     * @param fatherEducation
     *            The fatherEducation to set.
     */
    public void setFatherEducation(final Education fatherEducation) {
        this.fatherEducation = fatherEducation;
    }

    /**
     * @return Returns the fatherOccupation.
     * @hibernate.many-to-one column="FATHEROCCMASTERID"
     *                        class="com.egov.citizen.client.COccupation"
     *                        cascade="none" unique="false"
     */
    public Occupation getFatherOccupation() {
        return fatherOccupation;
    }

    /**
     * @param fatherOccupation
     *            The fatherOccupation to set.
     */
    public void setFatherOccupation(final Occupation fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    /**
     * @return Returns the motherEducation.
     * @hibernate.many-to-one column="MOTHEREDUMASTERID"
     *                        class="com.egov.citizen.client.CEducation"
     *                        cascade="none" unique="false"
     */
    public Education getMotherEducation() {
        return motherEducation;
    }

    /**
     * @param motherEducation
     *            The motherEducation to set.
     */
    public void setMotherEducation(final Education motherEducation) {
        this.motherEducation = motherEducation;
    }

    /**
     * @return Returns the motherOccupation.
     * @hibernate.many-to-one column="MOTHEROCCMASTERID"
     *                        class="com.egov.citizen.client.COccupation"
     *                        cascade="none" unique="false"
     */
    public Occupation getMotherOccupation() {
        return motherOccupation;
    }

    /**
     * @param motherOccupation
     *            The motherOccupation to set.
     */
    public void setMotherOccupation(final Occupation motherOccupation) {
        this.motherOccupation = motherOccupation;
    }

    /**
     * @return Returns the occupation.
     * @hibernate.many-to-one column="OCCUPATIONID"
     *                        class="com.egov.citizen.client.COccupation"
     *                        cascade="none" unique="false"
     */
    public Occupation getOccupation() {
        return occupation;
    }

    /**
     * @param occupation
     *            The occupation to set.
     */
    public void setOccupation(final Occupation occupation) {
        this.occupation = occupation;
    }

    /**
     * @return Returns the ageMomBirth.
     * @hibernate.property name="ageMomBirth"
     * @hibernate.column name="MOTHERAGEATCHILDBIRTH"
     */
    public Integer getAgeMomBirth() {
        return ageMomBirth;
    }

    /**
     * @param ageMomBirth
     *            The ageMomBirth to set.
     */
    public void setAgeMomBirth(final Integer ageMomBirth) {
        this.ageMomBirth = ageMomBirth;
    }

    /**
     * @return Returns the ageMomMarriage.
     * @hibernate.property name="ageMomMarriage"
     * @hibernate.column name="MOTHERAGEATMARRIAGE"
     */
    public Integer getAgeMomMarriage() {
        return ageMomMarriage;
    }

    /**
     * @param ageMomMarriage
     *            The ageMomMarriage to set.
     */
    public void setAgeMomMarriage(final Integer ageMomMarriage) {
        this.ageMomMarriage = ageMomMarriage;
    }

    /**
     * @return Returns the noOfChildren.
     * @hibernate.property name="noOfChildren"
     * @hibernate.column name="NOOFCHILDREN"
     */
    public Integer getNoOfChildren() {
        return noOfChildren;
    }

    /**
     * @param noOfChildren
     *            The noOfChildren to set.
     */
    public void setNoOfChildren(final Integer noOfChildren) {
        this.noOfChildren = noOfChildren;
    }

    /**
     * @return Returns the religion.
     * @hibernate.many-to-one column="RELIGIONMASTERID"
     *                        class="com.egov.citizen.client.CReligion"
     *                        cascade="none" unique="false"
     */
    public Religion getReligion() {
        return religion;
    }

    /**
     * @param religion
     *            The religion to set.
     */
    public void setReligion(final Religion religion) {
        this.religion = religion;
    }

    /**
     * @return Returns the citizenId.
     */
    public long getCitizenId() {
        return citizenId;
    }

    /**
     * @param citizenId
     *            The citizenId to set.
     */
    public void setCitizenId(final long citizenId) {
        this.citizenId = citizenId;
    }

    /**
     * @param Returns
     *            the isStillBirth
     */
    public char getIsStillBirth() {
        return isStillBirth;
    }

    /**
     * @param isStillBirth
     *            The isStillBirth to set.
     */
    public void setIsStillBirth(final Character isStillBirth) {
        this.isStillBirth = isStillBirth;
    }

    /**
     * @return Returns the informantOccupation.
     */
    public Occupation getInformantOccupation() {
        return informantOccupation;
    }

    /**
     * @param informantOccupation
     *            The informantOccupation to set.
     */
    public void setInformantOccupation(final Occupation informantOccupation) {
        this.informantOccupation = informantOccupation;
    }
}
