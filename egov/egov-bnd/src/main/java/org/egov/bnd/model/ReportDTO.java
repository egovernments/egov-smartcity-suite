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
/*
 * Created on Apr 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.bnd.model;

import org.egov.infra.persistence.entity.Address;

import java.util.Date;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class ReportDTO {

    private Integer id;
    private String regnNo;
    private Date dateOfEvent;
    // private char sex;
    private String sex;
    private Address eventAddress;
    private PlaceType placeType;
    private String firstName;
    private String lastName;
    private String middleName;
    private String motherName;
    private String fatherName;
    private String hospitalName;
    private String informantName;
    private int reportType;
    private String regunitdesc;
    private String regunitid;
    private Date registrationDate;
    private String authHospital;
    private Integer establishMasterID;
    private int adoptionDataAlreadyExist = 0; // Default value 0 means doesn't
    // exist.

    public Integer getEstablishMasterID() {
        return establishMasterID;
    }

    public void setEstablishMasterID(final Integer establishMasterID) {
        this.establishMasterID = establishMasterID;
    }

    public int getAdoptionDataAlreadyExist() {
        return adoptionDataAlreadyExist;
    }

    public void setAdoptionDataAlreadyExist(final int adoptionDataAlreadyExist) {
        this.adoptionDataAlreadyExist = adoptionDataAlreadyExist;
    }

    /**
     * @return Returns the regunitid.
     */
    public String getRegunitid() {
        return regunitid;
    }

    /**
     * @param regunitid
     *            The regunitid to set.
     */
    public void setRegunitid(final String regunitid) {
        this.regunitid = regunitid;
    }

    /**
     * @return Returns the regunitdesc.
     */
    public String getRegunitdesc() {
        return regunitdesc;
    }

    /**
     * @param regunitdesc
     *            The regunitdesc to set.
     */
    public void setRegunitdesc(final String regunitdesc) {
        this.regunitdesc = regunitdesc;
    }

    /**
     * @return Returns the reportType.
     */
    public int getReportType() {
        return reportType;
    }

    /**
     * @param reportType
     *            The reportType to set.
     */
    public void setReportType(final int reportType) {
        this.reportType = reportType;
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
     * @return Returns the fatherName.
     */
    public String getFatherName() {
        return fatherName;
    }

    /**
     * @param fatherName
     *            The fatherName to set.
     */
    public void setFatherName(final String fatherName) {
        this.fatherName = fatherName;
    }

    /**
     * @return Returns the firstName.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            The firstName to set.
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return Returns the id.
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
     * @return Returns the informantName.
     */
    public String getInformantName() {
        return informantName;
    }

    /**
     * @param informantName
     *            The informantName to set.
     */
    public void setInformantName(final String informantName) {
        this.informantName = informantName;
    }

    /**
     * @return Returns the lastName.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            The lastName to set.
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return Returns the middleName.
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @param middleName
     *            The middleName to set.
     */
    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return Returns the motherName.
     */
    public String getMotherName() {
        return motherName;
    }

    /**
     * @param motherName
     *            The motherName to set.
     */
    public void setMotherName(final String motherName) {
        this.motherName = motherName;
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
     * @return Returns the regnNo.
     */
    public String getRegnNo() {
        return regnNo;
    }

    /**
     * @param regnNo
     *            The regnNo to set.
     */
    public void setRegnNo(final String regnNo) {
        this.regnNo = regnNo;
    }

    /**
     * @return Returns the sex.
     */
    /*
     * public char getSex() { return sex; } /**
     * @param sex The sex to set.
     */
    /*
     * public void setSex(char sex) { this.sex = sex; }
     */
    /**
     * @return Returns the sex.
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex
     *            The sex to set.
     */
    public void setSex(final String sex) {
        this.sex = sex;
    }

    /**
     * @return Returns the hospitalName.
     */
    public String getHospitalName() {
        return hospitalName;
    }

    /**
     * @param hospitalName
     *            The hospitalName to set.
     */
    public void setHospitalName(final String hospitalName) {
        this.hospitalName = hospitalName;
    }

    /**
     * @return Returns the dateOfEvent.
     */
    public Date getRegistrationDate() {
        return registrationDate;
    }

    /**
     * @param dateOfEvent
     *            The dateOfEvent to set.
     */
    public void setRegistrationDate(final Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * @param authHospital
     *            The authHospital to set.
     */
    public void setAuthHospital(final String authHospital) {
        this.authHospital = authHospital;
    }

    /**
     * @return Returns the authHospital.
     */
    public String getAuthHospital() {
        return authHospital;
    }
}
