/*
 * @(#)RulesDefinition.java 3.0, 14 Jun, 2013 5:09:25 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.authrule;

import java.util.Set;

import org.egov.infra.admin.master.entity.Boundary;

public class RulesDefinition {
	public boolean jurisCompare(final Boundary userBoundary, final Boundary objectBoundary) {
		boolean authorized = false;
		final Long userBndryHeir = userBoundary.getBoundaryType().getHierarchy();
		final Long objBndryHeir = objectBoundary.getBoundaryType().getHierarchy();
		if (!userBoundary.getBoundaryType().getHierarchyType().getCode().equalsIgnoreCase("ADMIN") || !objectBoundary.getBoundaryType().getHierarchyType().getCode().equalsIgnoreCase("ADMIN")) {
			return authorized;
		}
		if (userBndryHeir == 1) { // Top boundary type heirarchy
			authorized = true;
		} else if (userBndryHeir == objBndryHeir) {
			if (userBoundary.equals(objectBoundary)) {
				authorized = true;
			} else {
				authorized = false;
			}
		} else if (userBndryHeir < objBndryHeir) {
			if (objBndryHeir - userBndryHeir == 1) {
				final Set<Boundary> childBndryList = userBoundary.getChildren();
				if (childBndryList.contains(objectBoundary)) {
					authorized = true;
				} else {
					authorized = false;
				}
			}
			if (objBndryHeir - userBndryHeir == 2) {
				final Set<Boundary> childBndryList = userBoundary.getChildren();
				for (final Boundary childBndry : childBndryList) {
					if (childBndry.getChildren() != null) {
						if (childBndry.getChildren().contains(objectBoundary)) {
							authorized = true;
							break;
						}
					}
				}
			}
		}
		return authorized;
	}
}
