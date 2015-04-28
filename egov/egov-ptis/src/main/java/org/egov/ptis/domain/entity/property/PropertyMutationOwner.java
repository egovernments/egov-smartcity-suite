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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
/*
 * Created on Aug 13, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.ptis.domain.entity.property;

/**
 * @author Admin
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class PropertyMutationOwner {
	private Integer mutationOwnerId = null;
	private Integer ownerId = null;
	private PropertyMutation propertyMutation = null;
	private Integer idPropMutation = null;

	/**
	 * @return Returns the idPropMutation.
	 */
	public Integer getIdPropMutation() {
		return idPropMutation;
	}

	/**
	 * @param idPropMutation
	 *            The idPropMutation to set.
	 */
	public void setIdPropMutation(Integer idPropMutation) {
		this.idPropMutation = idPropMutation;
	}

	/**
	 * @return Returns the mutationOwnerId.
	 */
	public Integer getMutationOwnerId() {
		return mutationOwnerId;
	}

	/**
	 * @param mutationOwnerId
	 *            The mutationOwnerId to set.
	 */
	public void setMutationOwnerId(Integer mutationOwnerId) {
		this.mutationOwnerId = mutationOwnerId;
	}

	/**
	 * @return Returns the ownerId.
	 */
	public Integer getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId
	 *            The ownerId to set.
	 */
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return Returns the propertyMutation.
	 */
	public PropertyMutation getPropertyMutation() {
		return propertyMutation;
	}

	/**
	 * @param propertyMutation
	 *            The propertyMutation to set.
	 */
	public void setPropertyMutation(PropertyMutation propertyMutation) {
		this.propertyMutation = propertyMutation;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getMutationOwnerId()).append("|OwnerId: ").append(getOwnerId()).append(
				"|PropertyMutation: ").append(getPropertyMutation()).append("|PropMutationId: ").append(
				getIdPropMutation());

		return objStr.toString();
	}
}
