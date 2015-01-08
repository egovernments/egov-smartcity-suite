/*
 * @(#)GenericJob.java 3.0, 18 Jun, 2013 2:57:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.scheduler;

import java.io.Serializable;

import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;

/**
 * This interface basically servers as a generic Interface for egov scheduler job 
 * to wrap any kind of scheduler framework like quartz
 * see {@link AbstractQuartzJob}. 
 **/
public interface GenericJob extends Serializable {
	void executeJob();
}
