/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.collection.workflow.renderer;

import org.egov.collection.entity.ReceiptHeader;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.inbox.DefaultInboxRenderServiceImpl;
import org.egov.infstr.services.PersistenceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Render service for collections workflow. Groups the receipt headers based on user + counter + service
 */
public class CollectionsWorkflowRenderService extends DefaultInboxRenderServiceImpl<ReceiptHeader> {

    public CollectionsWorkflowRenderService(final PersistenceService<ReceiptHeader, Long> stateAwarePersistenceService) {
        super(stateAwarePersistenceService);
    }

    /**
     * @param allItems Workflow Items from which grouped items are to be created
     * @return Workflow items (receipt headers) grouped by service + counter + user
     */
    private List<ReceiptHeader> getGroupedWorkflowItems(final List<ReceiptHeader> allItems) {
        final List<ReceiptHeader> receiptHeaderPerGroup = new ArrayList<>();
        final HashMap<String, Integer> assignedItems = new HashMap<>();
        for (final StateAware nextItem : allItems)
            if (nextItem instanceof ReceiptHeader) {
                final ReceiptHeader nextReceipt = (ReceiptHeader) nextItem;
                String groupingCriteria = "";
                if (nextReceipt.getReceipttype() == 'B')
                    groupingCriteria = nextReceipt.myLinkId();
                else
                    groupingCriteria = nextReceipt.myLinkIdForChallanMisc();
                if (assignedItems.get(groupingCriteria) == null) {
                    // Group not created yet. Create it.
                    receiptHeaderPerGroup.add(nextReceipt);
                    assignedItems.put(groupingCriteria, 1);

                }
            }
        return receiptHeaderPerGroup;
    }

    @Override
    public List<ReceiptHeader> getDraftWorkflowItems(final List<Long> owner) {
        return getGroupedWorkflowItems(super.getDraftWorkflowItems(owner));
    }

    /**
     * Returns the assigned work flow items for given user. For collections, one item is returned for every unique combination of
     * billing service, user and counter.
     *
     * @param owner The owner for whom the assigned work flow items are to be returned
     * @return Assigned workflow items (receipt headers) grouped by service + counter + user
     */

    @Override
    public List<ReceiptHeader> getAssignedWorkflowItems(final List<Long> owner) {
        return getGroupedWorkflowItems(super.getAssignedWorkflowItems(owner));
    }
}
