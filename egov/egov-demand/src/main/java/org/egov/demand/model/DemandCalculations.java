/*
 * DemandCalculations.java Created on Mar 07, 2006
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

 package org.egov.demand.model;

 /**
  * This is the interface for All Demand Calculations, to be extended to customise to specific clients.
  *
  * @author Gayathri Joshi
  * @version 1.00
  * @see
  * @see
  * @since   1.00
  */
 public interface DemandCalculations
 {
 	public abstract DemandCalculations createDemandCalculations();
 }
