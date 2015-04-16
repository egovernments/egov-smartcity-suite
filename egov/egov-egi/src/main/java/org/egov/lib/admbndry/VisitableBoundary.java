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

import java.util.Iterator;

import org.egov.infra.admin.master.entity.Boundary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisitableBoundary {

	private static final Logger LOGGER = LoggerFactory.getLogger(VisitableBoundary.class);
	private Boundary bndry = null;
	private VisitableBoundary sBnd = null;

	/**
	 * construct a visitable boundary by passing a boundary
	 */
	public VisitableBoundary(final Boundary boundary) {
		this.bndry = boundary;
	}

	/**
	 * accepts boundary visitors to query the class allow the visitor to visit in the following way - Depth First and breadth later for clarity see the numbers/chars in the tree - the 1 / \ 2 8 / | | \ 3 5 9 B / / \ | | \ 4 6 7 A C D
	 */
	public void accept(final BoundaryVisitor visitor) {
		if (!visitor.visitNeeded(this.getBoundary())) {
			return;
		}

		visitor.visit(this);

		final Iterator iter = this.bndry.getChildren().iterator();

		try {
			while (iter.hasNext()) {
				this.sBnd = new VisitableBoundary((Boundary) iter.next());
				this.sBnd.accept(visitor);
			}
		} catch (final ClassCastException cce) {
			LOGGER.warn("Error occurred while coverting the child boundaries into Boundary");

		} catch (final NullPointerException nule) {
			LOGGER.warn("Children of the currentBoundary seems to be null");
		}
	}

	/**
	 * returns the boundary contained
	 */
	public Boundary getBoundary() {
		return this.bndry;
	}
}
