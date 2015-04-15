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

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;

public class VisitorPath implements Path {
	private List path = null;

	@Override
	public boolean pathExists() {
		if (this.path == null) {
			return false;
		}

		return !this.path.isEmpty();
	}

	@Override
	public void buildPath(final BoundaryType sType, final BoundaryType tType) {
		if (tType == null || sType == null) {
			return;
		}

		this.path = new ArrayList();
		BoundaryType current = tType;
		while (!current.equals(sType)) {
			this.path.add(current);
			current = current.getParent();
		}
		this.path.add(sType);
	}

	@Override
	public void buildPathFromChild(final BoundaryType child) {
		if (child == null) {
			return;
		}
		BoundaryType parent = child;

		this.path = new ArrayList();
		while (parent != null) {
			this.path.add(parent);
			parent = parent.getParent();
			if (parent == null) {
				break;
			}
		}
	}

	@Override
	public boolean isTraversable(final Boundary bndry) {
		if (!this.pathExists()) {
			return true;
		}
		return this.path.contains(bndry.getBoundaryType());
	}
}
