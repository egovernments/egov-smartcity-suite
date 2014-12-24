/*
 * @(#)DemandComparatorByOrderId.java 3.0, 29 Jul, 2013 1:24:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.service.integration;

import java.util.Comparator;

import org.egov.demand.model.EgDemandDetails;

class DemandComparatorByOrderId implements Comparator<EgDemandDetails> {
	@Override
	public int compare(final EgDemandDetails d1, final EgDemandDetails d2) {
		return d1.getEgDemandReason().getEgDemandReasonMaster().getOrderId().compareTo(d2.getEgDemandReason().getEgDemandReasonMaster().getOrderId());
	}
}