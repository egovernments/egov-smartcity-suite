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
package org.egov.lib.admbndry;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infstr.utils.HibernateUtil;

public class BoundaryFinder implements BoundaryVisitor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BoundaryVisitor.class);

	private final List resultList = new ArrayList();
	private BoundaryType target = null;
	private Boundary boundary = null;
	private boolean middleBoundariesRequired = false;
	private final Path path = new VisitorPath();

	/**
	 * visits the given VisitableBoundary and gather information
	 */
	@Override
	public void visit(final VisitableBoundary vBoundary) {

		this.boundary = vBoundary.getBoundary();

		try {

			if (this.middleBoundariesRequired) {
				addToResult(this.boundary);
				return;
			}

			if (this.target != null) {
				if (this.boundary.getBoundaryType().equals(this.target)) {
					addToResult(this.boundary);
				}
				return;
			}

			if (this.boundary.isLeaf()) {
				addToResult(this.boundary);
				return;
			}

		} catch (final RuntimeException e) {
			LOGGER.error("Error occurred in visit", e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Error occurred in visit", e);
		}
	}

	/**
	 * adds the collected information to the resultList
	 */
	private void addToResult(final Boundary boundary) {
		this.resultList.add(boundary);
	}

	/**
	 * set targetBoundaryType, which this class should look for
	 * @param type
	 */
	public void setTargetBoundaryType(final BoundaryType type) {
		setTargetBoundaryType(type, false);
	}

	/**
	 * same as above, but can specify whether result should include the intermediate boundaries also
	 * @param type
	 * @param needIntermediates
	 */
	public void setTargetBoundaryType(final BoundaryType type, final boolean needIntermediates) {
		this.target = type;
		this.middleBoundariesRequired = needIntermediates;
		if (this.target != null) {
			this.path.buildPathFromChild(this.target);
		}
	}

	/**
	 * whether to visit the given boundary
	 */
	@Override
	public boolean visitNeeded(final Boundary boundary) {
		return this.path.isTraversable(boundary);
	}

	/**
	 * returns the resultList
	 */
	@Override
	public List getResult() {
		return this.resultList;
	}

	/**
	 * returns the path this class contain
	 */
	@Override
	public Path getPath() {
		return this.path;
	}

}
