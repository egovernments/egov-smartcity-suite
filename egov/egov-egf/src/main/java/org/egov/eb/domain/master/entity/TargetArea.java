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
package org.egov.eb.domain.master.entity;

import java.util.HashSet;
import java.util.Set;

import org.egov.infstr.models.BaseModel;
import org.egov.pims.commons.Position;

public class TargetArea extends BaseModel{

	private String name;
	private String code;
	private boolean isActive;
	private Position position;
	private Set<TargetAreaMappings> targetAreaMappings = new HashSet<TargetAreaMappings>(0);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Set<TargetAreaMappings> getTargetAreaMappings() {
		return targetAreaMappings;
	}

	public void setTargetAreaMappings(Set<TargetAreaMappings> targetAreaMappings) {
		this.targetAreaMappings = targetAreaMappings;
	}

	/*
     * FIXME PHOENIX use HashCodeBuilder from apache commons 
     * override equals method as well with EqualsBuilder
     * 
     * @Override
	public int hashCode() {
		int seedValue = HashCodeUtil.SEED;

		seedValue = HashCodeUtil.hash(seedValue, this.code);
		seedValue = HashCodeUtil.hash(seedValue, this.isActive);
		seedValue = HashCodeUtil.hash(seedValue, this.name);
		seedValue = HashCodeUtil.hash(seedValue, this.position);
		
		return seedValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		TargetArea other = (TargetArea) obj;
		
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		
		if (isActive != other.isActive)
			return false;
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		
		return true;
	}*/
	
	//FIXME PHOENIX remove entity ref from toString
	@Override
	public String toString() {
		return new StringBuilder().append("TargetArea [")
				.append("id=").append(id)
				.append(", name=").append(name)
				.append(", code=").append(code)
				.append(", isActive").append(isActive)
				.append(", position").append(position)
				.append("]").toString();
	}
}

