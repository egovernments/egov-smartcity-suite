/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.application.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.bpa.application.entity.enums.GenderTitle;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_APPLICANT")
@SequenceGenerator(name = Applicant.SEQ_APPLICANT, sequenceName = Applicant.SEQ_APPLICANT, allocationSize = 1)
public class Applicant extends AbstractAuditable {

    private static final long serialVersionUID = 3078684328383202788L;
    public static final String SEQ_APPLICANT = "SEQ_EGBPA_Applicant";

    @Id
    @GeneratedValue(generator = SEQ_APPLICANT, strategy = GenerationType.SEQUENCE)
    private Long id;
    private String applicantName;
    private GenderTitle title;
    @Length(min = 1, max = 128)
    private String username;
    private BpaApplication application;
    @Length(min = 1, max = 128)
    private String gender;
    @Length(min = 1, max = 128)
    private String fatherorHusbandName;
    private Date dateofBirth;
    @Length(min = 1, max = 128)
    private String address;
    @Length(min = 1, max = 128)
    private String district;
    @Length(min = 1, max = 128)
    private String taluk;
    @Length(min = 1, max = 128)
    private String area;
    @Length(min = 1, max = 128)
    private String city;
    @Length(min = 1, max = 128)
    private String state;
    @Length(min = 1, max = 128)
    private String pinCode;
    @Length(min = 1, max = 128)
    private String mobileNumber;
    @Length(min = 1, max = 128)
    private String emailid;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    public GenderTitle getTitle() {
        return title;
    }

    public void setTitle(final GenderTitle title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public Date getDateofBirth() {
        return dateofBirth;
    }

    public void setDateofBirth(final Date dateofBirth) {
        this.dateofBirth = dateofBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(final String district) {
        this.district = district;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(final String taluk) {
        this.taluk = taluk;
    }

    public String getArea() {
        return area;
    }

    public void setArea(final String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(final String emailid) {
        this.emailid = emailid;
    }

    public String getFatherorHusbandName() {
        return fatherorHusbandName;
    }

    public void setFatherorHusbandName(final String fatherorHusbandName) {
        this.fatherorHusbandName = fatherorHusbandName;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(final String pinCode) {
        this.pinCode = pinCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

}
