/*
 * @(#)InboxComparator.java 3.0, 17 Jun, 2013 4:43:30 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.infstr.workflow.inbox;

import java.util.Comparator;
import java.util.Date;

import org.egov.infstr.models.StateAware;

public class InboxComparator implements Comparator<StateAware> {
	
	@Override
	public int compare(final StateAware first_stateAware, final StateAware second_stateAware) {
		int returnVal = 1;
		if (first_stateAware == null) {
			returnVal = second_stateAware == null ? 0 : -1;
		} else if (second_stateAware == null) {
			returnVal = 1;
		} else {
			final Date first_date = first_stateAware.getState().getCreatedDate();
			final Date second_date = second_stateAware.getState().getCreatedDate();
			if (first_date.after(second_date)) {
				returnVal = -1;
			} else if (first_date.equals(second_date)) {
				returnVal = 0;
			}
		}
		return returnVal;
	}
}
