package org.egov.erpcollection.scheduler;

import java.text.ParseException;

import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.Before;
import org.junit.Test;

public class OnlinePaymentUnknownStatusTrackerJobTest extends AbstractPersistenceServiceTest  {
	private OnlinePaymentUnknownStatusTrackerJob delegate;
	
	@Before
	public void setupService(){

		delegate = new OnlinePaymentUnknownStatusTrackerJob();
		delegate.setPersistenceService(genericService);
	}
	
	@Test
	public void testExecuteJob() throws ParseException {
		delegate.executeJob();
		
		// code to test has to be written after story is complete
	}
	
}
