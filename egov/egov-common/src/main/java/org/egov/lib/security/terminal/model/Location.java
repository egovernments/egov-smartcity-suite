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
package org.egov.lib.security.terminal.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Location implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String desc;
	private Integer isActive;
	private Integer isLocation;
	private Location locationId;
	private Date createdDate;
	private Timestamp lastModifiedDate;
	private Set<LocationIPMap> locationIPMapSet = new HashSet<LocationIPMap>();

	/**
	 * Gets the created date.
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the desc.
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Sets the desc.
	 * @param desc the new desc
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * Gets the last modified date.
	 * @return the last modified date
	 */
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * Sets the last modified date.
	 * @param lastModifiedDate the new last modified date
	 */
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * Gets the id.
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the location id.
	 * @return the location id
	 */
	public Location getLocationId() {
		return locationId;
	}

	/**
	 * Sets the location id.
	 * @param counter the new location id
	 */
	public void setLocationId(Location counter) {
		this.locationId = counter;
	}

	/**
	 * Gets the checks if is active.
	 * @return the checks if is active
	 */
	public Integer getIsActive() {
		return isActive;
	}

	/**
	 * Sets the checks if is active.
	 * @param isActive the new checks if is active
	 */
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * Gets the location ip map set.
	 * @return the locationIPMapSet
	 */
	public Set<LocationIPMap> getLocationIPMapSet() {
		return locationIPMapSet;
	}

	/**
	 * Sets the location ip map set.
	 * @param locationIPMapSet the locationIPMapSet to set
	 */
	public void setLocationIPMapSet(Set<LocationIPMap> locationIPMapSet) {
		this.locationIPMapSet = locationIPMapSet;
	}

	/**
	 * Gets the checks if is location.
	 * @return the isLocation
	 */
	public Integer getIsLocation() {
		return isLocation;
	}

	/**
	 * Sets the checks if is location.
	 * @param isLocation the isLocation to set
	 */
	public void setIsLocation(Integer isLocation) {
		this.isLocation = isLocation;
	}

	/**
	 * Adds the location ip map.
	 * @param locationIPMap the location ip map
	 */
	public void addLocationIPMap(Set locationIPMap) {
		getLocationIPMapSet().addAll(locationIPMap);
	}

}
