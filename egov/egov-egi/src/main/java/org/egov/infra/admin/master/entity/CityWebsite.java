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
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Unique(id = "id", tableName = "eg_city_website", fields = { "cityBaseURL" }, columnName = {
        "citybaseurl" }, enableDfltMsg = true)
@Table(name = "eg_city_website")
@NamedQuery(name = CityWebsite.QUERY_CITY_BY_URL, query = "Select cw FROM CityWebsite cw WHERE cw.cityBaseURL=:url")
@SequenceGenerator(name = CityWebsite.SEQ_CITY_WEBSITE, sequenceName = CityWebsite.SEQ_CITY_WEBSITE, allocationSize = 1)
public class CityWebsite extends AbstractAuditable {

    private static final long serialVersionUID = -6267923687226233397L;
    public static final String SEQ_CITY_WEBSITE = "SEQ_EG_CITY_WEBSITE";
    public static final String QUERY_CITY_BY_URL = "CITY_BY_URL";

    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_CITY_WEBSITE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "boundary")
    private Boundary boundary;

    @SafeHtml
    @NotBlank
    private String cityName;

    @SafeHtml
    @NotBlank
    private String cityNameLocal;

    private boolean active;

    @SafeHtml
    @NotBlank
    private String cityBaseURL;

    @SafeHtml
    @NotBlank
    private String logo;
    
    @NotBlank
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(final Boundary boundary) {
        this.boundary = boundary;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName;
    }

    public String getCityNameLocal() {
        return cityNameLocal;
    }

    public void setCityNameLocal(final String cityNameLocal) {
        this.cityNameLocal = cityNameLocal;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public String getCityBaseURL() {
        return cityBaseURL;
    }

    public void setCityBaseURL(final String cityBaseURL) {
        this.cityBaseURL = cityBaseURL;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(final String logo) {
        this.logo = logo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (cityBaseURL == null ? 0 : cityBaseURL.hashCode());
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
        final CityWebsite other = (CityWebsite) obj;
        if (cityBaseURL == null) {
            if (other.cityBaseURL != null)
                return false;
        } else if (!cityBaseURL.equals(other.cityBaseURL))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
