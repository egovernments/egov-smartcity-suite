/*
 * @(#)VisitorPath.java 3.0, 16 Jun, 2013 3:48:45 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import java.util.ArrayList;
import java.util.List;

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
