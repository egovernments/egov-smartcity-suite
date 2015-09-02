package org.egov.tradelicense.domain.service.integration;

import java.util.Comparator;

import org.egov.demand.model.EgDemandDetails;

class DemandComparatorByOrderId implements Comparator<EgDemandDetails>
{
	public int compare(EgDemandDetails d1,EgDemandDetails d2)
	{
		return d1.getEgDemandReason().getEgDemandReasonMaster().getOrderId().compareTo
		(d2.getEgDemandReason().getEgDemandReasonMaster().getOrderId());
	}
}