/*
 * @(#)CrossHeirarchy.java 3.0, 16 Jun, 2013 3:34:33 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

public interface CrossHeirarchy {

	Integer getId();

	Boundary getParent();

	Boundary getChild();
}
