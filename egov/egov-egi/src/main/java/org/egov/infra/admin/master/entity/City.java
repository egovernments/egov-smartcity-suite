/* eGov suite of products aim to improve the internal efficiency,transparency,
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

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Unique(id = "id", tableName = "eg_city", fields = { "domainURL" }, columnName = { "domainURL" }, enableDfltMsg = true)
@Table(name = "eg_city")
@NamedQuery(name = City.QUERY_CITY_BY_URL, query = "Select cw FROM City cw WHERE cw.domainURL=:domainURL")
@SequenceGenerator(name = City.SEQ_CITY, sequenceName = City.SEQ_CITY, allocationSize = 1)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class City extends AbstractAuditable {

    private static final long serialVersionUID = -6267923687226233397L;
    public static final String SEQ_CITY = "SEQ_EG_CITY";
    public static final String QUERY_CITY_BY_URL = "CITY_BY_URL";

    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_CITY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "boundary", updatable = false)
    @NotAudited
    private Boundary boundary;

    @SafeHtml
    @NotBlank
    private String name;

    @SafeHtml
    @NotBlank
    private String localName;

    private boolean active;

    @SafeHtml
    @NotBlank
    private String domainURL;

    @SafeHtml
    @NotBlank
    private String recaptchaPK;

    @SafeHtml
    @NotBlank
    private String recaptchaPub;

    @SafeHtml
    @NotBlank
    private String code;

    @SafeHtml
    @NotBlank
    private String districtCode;

    @SafeHtml
    @NotBlank
    private String districtName;

    private Float longitude;

    private Float latitude;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "preferences")
    @NotAudited
    private CityPreferences preferences;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(final Boundary boundary) {
        this.boundary = boundary;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(final String localName) {
        this.localName = localName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public String getDomainURL() {
        return domainURL;
    }

    public void setDomainURL(final String domainURL) {
        this.domainURL = domainURL;
    }

    public String getRecaptchaPK() {
        return recaptchaPK;
    }

    public void setRecaptchaPK(final String recaptchaPK) {
        this.recaptchaPK = recaptchaPK;
    }

    public String getRecaptchaPub() {
        return recaptchaPub;
    }

    public void setRecaptchaPub(final String recaptchaPub) {
        this.recaptchaPub = recaptchaPub;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(final Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(final Float latitude) {
        this.latitude = latitude;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(final String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public CityPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(final CityPreferences preferences) {
        this.preferences = preferences;
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> cityPrefs = new HashMap<>();
        cityPrefs.put("cityBoundaryId", getBoundary().getId().toString());
        cityPrefs.put("cityurl", getDomainURL());
        cityPrefs.put("cityname", getName());
        final CityPreferences cityPreferences = getPreferences();
        if (cityPreferences == null)
            cityPrefs.put("citylogo", "/resources/global/images/logo@2x.png");
        else {
            cityPrefs.put("citylogo", cityPreferences.logoExist()
                    ? String.format("/downloadfile/logo?fileStoreId=%s&moduleName=%s", cityPreferences.getLogo().getFileStoreId(), getCode()) : "");
            cityPrefs.put("cityKmlFileStoreId", cityPreferences.kmlExist() ? cityPreferences.getGisKML().getFileStoreId() : "");
            cityPrefs.put("cityShapeFileStoreId", cityPreferences.shapeExist() ? cityPreferences.getGisShape().getFileStoreId() : "");
        }

        cityPrefs.put("citynamelocal", getLocalName());
        cityPrefs.put("cityCode", getCode());
        cityPrefs.put("cityRecaptchaPK", getRecaptchaPK());
        cityPrefs.put("cityRecaptchaPub", getRecaptchaPub());
        cityPrefs.put("citylat", getLatitude());
        cityPrefs.put("citylng", getLongitude());
        cityPrefs.put("cityCode", getCode());
        return cityPrefs;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (domainURL == null ? 0 : domainURL.hashCode());
        result = prime * result + (id == null ? 0 : id.hashCode());
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
        final City other = (City) obj;
        if (domainURL == null) {
            if (other.domainURL != null)
                return false;
        } else if (!domainURL.equals(other.domainURL))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
