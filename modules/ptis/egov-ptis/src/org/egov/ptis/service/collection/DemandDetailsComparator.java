package org.egov.ptis.service.collection;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;

import java.util.Comparator;

import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.ptis.nmc.constants.NMCPTISConstants;

/**
 * The Class DemandDetailsComparator. Compares the demand details using the id
 * in order to list the details in ascending order
 */
public class DemandDetailsComparator implements Comparator<EgDemandDetails> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(EgDemandDetails o1, EgDemandDetails o2) {
		EgDemandReason dmndRsnMaster1 = o1.getEgDemandReason();
		EgDemandReason dmndRsnMaster2 = o2.getEgDemandReason();
		if (o1 == null || o2 == null) {
			return 1;
		} else {
			if (dmndRsnMaster1.getEgDemandReasonMaster().getCode().equals(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
				return -1;
			} else if (dmndRsnMaster2.getEgDemandReasonMaster().getCode().equals(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
				return 1;
			} else if (dmndRsnMaster1.getEgInstallmentMaster().getInstallmentYear().after(
					dmndRsnMaster2.getEgInstallmentMaster().getInstallmentYear())) {
				return 1;
			} else if (dmndRsnMaster1.getEgInstallmentMaster().getInstallmentYear().before(
					dmndRsnMaster2.getEgInstallmentMaster().getInstallmentYear())) {
				return -1;
			}
		}
		return 0;
	}

}
