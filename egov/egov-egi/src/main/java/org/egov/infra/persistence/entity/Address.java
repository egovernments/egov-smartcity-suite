/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.infra.persistence.entity;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.enums.AddressType;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;

import static org.egov.infra.persistence.entity.Address.SEQ_ADDRESS;

@Entity
@Table(name = "eg_address")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = SEQ_ADDRESS, sequenceName = SEQ_ADDRESS, allocationSize = 1)
@Cacheable
public abstract class Address extends AbstractPersistable<Long> {

    public static final String SEQ_ADDRESS = "seq_eg_address";
    private static final long serialVersionUID = 4842889134725565148L;
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_ADDRESS)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User user;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AddressType type;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
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

    public AddressType getType() {
        return type;
    }

    public void setType(final AddressType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        result = prime * result + (type == null ? 0 : type.hashCode());
        result = prime * result + (user == null ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Address other = (Address) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (type != other.type)
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(houseNoBldgApt))
            builder.append(StringUtils.trim(houseNoBldgApt)).append(", ");
        if (StringUtils.isNotBlank(areaLocalitySector))
            builder.append(StringUtils.trim(areaLocalitySector)).append(", ");
        if (StringUtils.isNotBlank(streetRoadLine))
            builder.append(StringUtils.trim(streetRoadLine)).append(", ");
        if (StringUtils.isNotBlank(landmark))
            builder.append(StringUtils.trim(landmark)).append(", ");
        if (StringUtils.isNotBlank(cityTownVillage))
            builder.append(StringUtils.trim(cityTownVillage)).append(", ");
        if (StringUtils.isNotBlank(postOffice))
            builder.append(StringUtils.trim(postOffice)).append(", ");
        if (StringUtils.isNotBlank(subdistrict))
            builder.append(StringUtils.trim(subdistrict)).append(", ");
        if (StringUtils.isNotBlank(district))
            builder.append(StringUtils.trim(district)).append(", ");
        if (StringUtils.isNotBlank(state))
            builder.append(StringUtils.trim(state)).append(", ");
        if (StringUtils.isNotBlank(country))
            builder.append(StringUtils.trim(country)).append(", ");
        if (StringUtils.isNotBlank(pinCode))
            builder.append("PIN : ").append(pinCode);
        return builder.toString();
    }
}