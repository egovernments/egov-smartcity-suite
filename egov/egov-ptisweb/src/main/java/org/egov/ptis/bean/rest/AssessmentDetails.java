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
package org.egov.ptis.bean.rest;

import java.io.Serializable;
import java.util.Set;

/**
 * The AssessmentDetails class is used to contain assessment details such as
 * property id, owner details, boundary details, and block details.
 * 
 * @author ranjit
 *
 */
@SuppressWarnings("serial")
public class AssessmentDetails implements Serializable {
	
	private String propertyID;
	private Set<OwnerName> ownerNames;
	private BoundaryDetails boundaryDetails;
	private String propertyAddress;
	private PropertyDetails propertyDetails;
	private ErrorDetails errorDetails;
	
	public String getPropertyID() {
		return propertyID;
	}
	public void setPropertyID(String propertyID) {
		this.propertyID = propertyID;
	}
	public Set<OwnerName> getOwnerNames() {
		return ownerNames;
	}
	public void setOwnerNames(Set<OwnerName> ownerNames) {
		this.ownerNames = ownerNames;
	}
	public BoundaryDetails getBoundaryDetails() {
		return boundaryDetails;
	}
	public void setBoundaryDetails(BoundaryDetails boundaryDetails) {
		this.boundaryDetails = boundaryDetails;
	}
	public PropertyDetails getPropertyDetails() {
		return propertyDetails;
	}
	public void setPropertyDetails(PropertyDetails propertyDetails) {
		this.propertyDetails = propertyDetails;
	}
	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(ErrorDetails errorDetails) {
		this.errorDetails = errorDetails;
	}
	public String getPropertyAddress() {
		return propertyAddress;
	}
	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}
	@Override
	public String toString() {
		return "AssessmentDetails [propertyID=" + propertyID + ", ownerNames=" + ownerNames + ", boundaryDetails="
				+ boundaryDetails + ", propertyAddress=" + propertyAddress + ", propertyDetails=" + propertyDetails
				+ ", errorDetails=" + errorDetails + "]";
	}
	
}
