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
package org.egov.infstr.client.delegate;

import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;

public class MoveBoundaryDelegate {

	private BoundaryDAO boundaryDAO; // Data Access Object for Boundary

	/**
	 * Used to get all the Child Boundary from a given Parent Boundary ID
	 * @param parentBoundaryId
	 * @return {@link List<Boundary>}
	 * @throws Exception
	 **/
	public List<Boundary> getChildBoundaries(final String parentBoundaryId) throws Exception {
		return this.boundaryDAO.getChildBoundaries(parentBoundaryId);
	}

	/**
	 * Used to get all the Parent Boundary from a given Child Boundary ID
	 * @param childBoundaryId
	 * @return {@link List<Boundary>}
	 * @throws Exception
	 **/
	public List<Boundary> getAllParentBoundaries(final String childBoundaryId) throws Exception {
		return this.boundaryDAO.getAllParentBoundaries(childBoundaryId);
	}

	/**
	 * Used to move a Child Boundary from its Parent to another Parent Boundary
	 * @param childBoundaryId
	 * @param parentBoundaryId
	 * @return {@link Boundary}
	 * @throws Exception
	 **/
	public Boundary moveBoundary(final String childBoundaryId, final String parentBoundaryId) throws Exception {
		final Boundary boundary = this.boundaryDAO.getBoundary(Long.valueOf(childBoundaryId));
		final Boundary parentBoundary = this.boundaryDAO.getBoundary(Long.valueOf(parentBoundaryId));
		boundary.setParent(parentBoundary);
		this.boundaryDAO.updateBoundary(boundary);
		return boundary;
	}

	/**
	 * BoundaryDAO to set
	 * @param boundaryDAO
	 **/
	public void setBoundaryDAO(final BoundaryDAO boundaryDAO) {
		this.boundaryDAO = boundaryDAO;
	}
}
