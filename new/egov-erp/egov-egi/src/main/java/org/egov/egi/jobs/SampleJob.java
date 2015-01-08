package org.egov.egi.jobs;

import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;

public class SampleJob extends AbstractQuartzJob {

	@Override
	public void executeJob() {
		System.out.println("RUNNING EGOV KICKASS SCHEDULER");
	}

}
