package org.egov.erpcollection.scheduler;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.erpcollection.models.OnlinePayment;
import org.egov.erpcollection.web.constants.CollectionConstants;
// import org.egov.infstr.scheduler.EgovJob;
import org.egov.infstr.services.PersistenceService;

// TODO REWRITE. SEE http://118.102.160.130:8000/projects/erp_3_0/cards/96
public class OnlinePaymentUnknownStatusTrackerJob  // implements EgovJob 
{
	private static final Logger LOGGER = Logger.getLogger(
			OnlinePaymentUnknownStatusTrackerJob.class);
	
	protected PersistenceService persistenceService;

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	@SuppressWarnings("unchecked")
	// @Override
	public void executeJob() {
		LOGGER.debug("Executing job to track online payments with UNKNOWN " +
				"transanction statuses");
		
		final List<OnlinePayment> unknownTransList = 
			persistenceService.findAllByNamedQuery(
				CollectionConstants.QUERY_ONLINERECEIPTS_BY_STATUSCODE, 
				CollectionConstants.ONLINEPAYMENT_STATUS_DESC_PENDING);
		
		LOGGER.debug("Retrieved online payments with unknown statuses : " 
				+ unknownTransList);
	}
}
