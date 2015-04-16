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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.infra.persistence.validator.annotation.Unique;


@Entity
@Unique(
        id = "id", 
        tableName = "eg_city_website", 
        fields = { "cityBaseURL" }, 
        columnName = { "citybaseurl" }
)
@Table(name = "eg_city_website")
@NamedQuery(name="CITY_BY_URL",query="Select cw FROM CityWebsite cw WHERE cw.cityBaseURL=:url")
public class CityWebsite extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -6267923687226233397L;

    public static final String QUERY_CITY_BY_URL = "CITY_BY_URL";

    @ManyToOne
    @JoinColumn(name = "boundary")
    private Boundary boundary;

    private String cityName;
    private String cityNameLocal;
    private boolean isActive;
    private String cityBaseURL;
    private String logo;

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityNameLocal() {
        return cityNameLocal;
    }

    public void setCityNameLocal(String cityNameLocal) {
        this.cityNameLocal = cityNameLocal;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getCityBaseURL() {
        return cityBaseURL;
    }

    public void setCityBaseURL(String cityBaseURL) {
        this.cityBaseURL = cityBaseURL;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
