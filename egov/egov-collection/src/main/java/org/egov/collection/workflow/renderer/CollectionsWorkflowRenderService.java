/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.collection.workflow.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.inbox.DefaultInboxRenderServiceImpl;
import org.egov.infstr.services.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Render service for collections workflow. Groups the receipt headers based on user + counter + service
 */
public class CollectionsWorkflowRenderService extends
        DefaultInboxRenderServiceImpl<ReceiptHeader> {

    @Autowired
    private PersistenceService persistenceService;

    public CollectionsWorkflowRenderService(PersistenceService<ReceiptHeader, Long> stateAwarePersistenceService) {
        super(stateAwarePersistenceService);
    }

    /**
     * TODO: Implement collections specific grouping logic
     */
    /**
     * @param allItems Workflow Items from which grouped items are to be created
     * @return Workflow items (receipt headers) grouped by service + counter + user
     */
    private List<ReceiptHeader> getGroupedWorkflowItems(List<ReceiptHeader> allItems) {
        List<ReceiptHeader> receiptHeaderPerGroup = new ArrayList<ReceiptHeader>();
        HashMap<String, Integer> assignedItems = new HashMap<String, Integer>();
        for (StateAware nextItem : allItems) {
            if (nextItem instanceof ReceiptHeader) {
                ReceiptHeader nextReceipt = (ReceiptHeader) nextItem;
                String groupingCriteria = nextReceipt.myLinkId();
                if (assignedItems.get(groupingCriteria) == null) {
                    // Group not created yet. Create it.
                    receiptHeaderPerGroup.add(nextReceipt);
                    assignedItems.put(groupingCriteria, 1);

                }
            }
        }
        return receiptHeaderPerGroup;
    }

    @Override
    public List<ReceiptHeader> getDraftWorkflowItems(Long userId, List<Long> owner) {
        return Collections.emptyList();
    }

    /**
     * TODO: Implement collections specific grouping logic
     */

    @Override
    public List<ReceiptHeader> getFilteredWorkflowItems(final Long owner, final Long userId, final Long sender,
            final Date fromDate,
            final Date toDate) {
        return Collections.emptyList();
    }

    /**
     * Returns the assigned work flow items for given user. For collections, one item is returned for every unique combination of
     * billing service, user and counter.
     *
     * @param owner The owner for whom the assigned work flow items are to be returned
     * @param userId The currently logged in user id (NEW items created by this user are excluded)
     * @param order The field on which the items are to be sorted
     * @return Assigned workflow items (receipt headers) grouped by service + counter + user
     */

    @Override
    public List<ReceiptHeader> getAssignedWorkflowItems(Long userId, List<Long> owner) {
        return getGroupedWorkflowItems(super.getAssignedWorkflowItems(userId, owner));
    }

    /**
     * Returns the work flow items for given filter criteria. For collections, one item is returned for every unique combination
     * of billing service, user and counter.
     *
     * @param criteria Filter criteria for fetching workflow items
     * @return Filtered workflow items (receipt headers) grouped by service + counter + user
     */

    @Override
    public List<ReceiptHeader> getWorkflowItems(final Map<String, Object> criteria) {
        return getGroupedWorkflowItems(super.getWorkflowItems(criteria));
    }

    @Override
    public List<ReceiptHeader> getWorkflowItems(String arg0) {
        String params[] = arg0.split(CollectionConstants.SEPARATOR_HYPHEN, -1);
        StringBuilder query = new StringBuilder(
                "select receipt from org.egov.collection.entity.ReceiptHeader receipt where 1=1 and receipt.status.code = ? "
                        +
                        "and receipt.createdBy.userName = ? and receipt.location.id = ? and receipt.service.code = ? order by receipt.createdDate desc");
        Object arguments[] = new Object[4];
        if (params[0].equals(CollectionConstants.WF_ACTION_SUBMIT))
            arguments[0] = CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED;
        else if (params[0].equals(CollectionConstants.WF_ACTION_APPROVE))
            arguments[0] = CollectionConstants.RECEIPT_STATUS_CODE_SUBMITTED;
        arguments[1] = params[2];
        arguments[2] = Integer.valueOf(params[3]);
        arguments[3] = params[1];
        return persistenceService.findAllBy(query.toString(),
                arguments);
    }

}
