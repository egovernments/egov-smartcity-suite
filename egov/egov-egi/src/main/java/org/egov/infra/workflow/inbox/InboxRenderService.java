/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infra.workflow.inbox;

import org.egov.infra.workflow.entity.StateAware;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface InboxRenderService<T extends StateAware> {
	
	String WFTYPE = "wfType";
	String SENDER = "sender";
	String OWNER = "owner";
	String FROMDATE = "fromDate";
	String TODATE = "toDate";
	String WFSTATE = "wfState";
	String IDENTIFIER = "identifier";
	String SEARCH_OP = "searchOp";
	String INBOX_RENDER_SERVICE_SUFFIX = "InboxRenderService";
	Character RENDER_Y = Character.valueOf('Y');
	Character GROUP_Y = Character.valueOf('Y');
	    
	/**
	 * Returns a list of workflow items that are assigned to the given position where the workflow
	 * has not ended.
	 * @param owner the owner
	 * @param userId the user id
	 * @return the assigned workflow items
	 */
	List<T> getAssignedWorkflowItems(Long userId, List<Long> owners);
	
	/**
	 * Returns a list of workflow items that are created by the given position and the state in
	 * 'NEW'.
	 * @param owner the owner
	 * @param userId the user id
	 * @return the draft workflow items
	 */
	List<T> getDraftWorkflowItems(Long userId, List<Long> owners);
	
	/**
	 * Gets the filtered workflow items.
	 * @param owner the owner
	 * @param userId the user id
	 * @param sender the sender
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the filtered workflow items
	 */
	List<T> getFilteredWorkflowItems(Long owner, Long userId, Long sender, Date fromDate, Date toDate);
	
	
	/**
	 * Returns a list of workflow items for the given criteria
	 * @param criteria Map of criteria fields<br>
	 * Mandatory key in criteria <br>
	 * 1] wfType {@link InboxRenderService#WFTYPE} the StateAware simple class name (without package structure) <br> 
	 * Other non mandatory keys in criteria <br>
	 * 1] sender {@link InboxRenderService#SENDER}: List of Positions that have created the workflow items <br>
	 * 2] owner {@link InboxRenderService#OWNER}: List of Positions to whom the workflow items are currently assigned <br>
	 * 3] fromDate {@link InboxRenderService#FROMDATE}: From date for fetching the workflow items (based on creation time stamp) <br>
	 * 4] toDate {@link InboxRenderService#TODATE}: To date for fetching the workflow items (based on creation time stamp) <br>
	 * 5] wfState {@link InboxRenderService#WFSTATE}: StateAware current state value <br>
	 * @return the workflow items
	 */
	List<T> getWorkflowItems(Map<String,Object> criteria);
	
	/**
	 * Populate all Workflow Items using the myLinkedId value.
	 * @param myLinkId the StateAware myLinkId property
	 * @return all StateAware
	 */
	List<T> getWorkflowItems(String myLinkId);
	
}
