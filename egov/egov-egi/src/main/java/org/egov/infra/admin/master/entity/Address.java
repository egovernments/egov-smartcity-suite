/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infra.admin.master.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.admin.master.entity.enums.AddressType;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name="eg_address")
public class Address extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 4842889134725565148L;
    
    @SafeHtml
    private String identityBy;
    
    @SafeHtml
    private String identityType;
    
    @SafeHtml
    private String houseNoBldgApt;
    
    @SafeHtml
    private String streetRoadLine;
    
    @SafeHtml
    private String landmark;
    
    @SafeHtml
    private String areaLocalitySector;
    
    @SafeHtml
    private String cityTownVillage;
    
    @SafeHtml
    private String district;
    
    @SafeHtml
    private String subdistrict;
    
    @SafeHtml
    private String postOffice;
    
    @SafeHtml
    private String state;
    
    @SafeHtml
    private String country;
    
    @SafeHtml
    private String pinCode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private AddressType type;
    
    public String getIdentityBy() {
        return identityBy;
    }

    public void setIdentityBy(final String identityBy) {
        this.identityBy = identityBy;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(final String identityType) {
        this.identityType = identityType;
    }

    public String getHouseNoBldgApt() {
        return houseNoBldgApt;
    }

    public void setHouseNoBldgApt(final String houseNoBldgApt) {
        this.houseNoBldgApt = houseNoBldgApt;
    }

    public String getStreetRoadLine() {
        return streetRoadLine;
    }

    public void setStreetRoadLine(final String streetRoadLine) {
        this.streetRoadLine = streetRoadLine;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(final String landmark) {
        this.landmark = landmark;
    }

    public String getAreaLocalitySector() {
        return areaLocalitySector;
    }

    public void setAreaLocalitySector(final String areaLocalitySector) {
        this.areaLocalitySector = areaLocalitySector;
    }

    public String getCityTownVillage() {
        return cityTownVillage;
    }

    public void setCityTownVillage(final String cityTownVillage) {
        this.cityTownVillage = cityTownVillage;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(final String district) {
        this.district = district;
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(final String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public String getPostOffice() {
        return postOffice;
    }

    public void setPostOffice(final String postOffice) {
        this.postOffice = postOffice;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(final String pinCode) {
        this.pinCode = pinCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public AddressType getType() {
        return type;
    }

    public void setType(final AddressType type) {
        this.type = type;
    }
}