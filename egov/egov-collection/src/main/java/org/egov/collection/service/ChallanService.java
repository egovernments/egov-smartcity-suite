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
package org.egov.collection.service;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.Challan;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Provides services related to receipt header
 */
@Transactional
public class ChallanService extends PersistenceService<Challan, Long> {

    private static final Logger LOGGER = Logger.getLogger(ChallanService.class);

    @Autowired
    private CollectionsUtil collectionsUtil;

    public ChallanService() {
        super(Challan.class);
    }

    public ChallanService(Class<Challan> type) {
        super(type);
    }
    /**
     * This method performs the Challan workflow transition. The challan status
     * is updated and transitioned to the next state. At the end of the
     * transition the challan will be available in the inbox of the user of the
     * position specified.
     *
     * @param challan
     *            the <code>Challan</code> instance which has to under go the
     *            workflow transition
     * @param nextPosition
     *            the position of the user to whom the challan must next be
     *            assigned to.
     * @param actionName
     *            a <code>String</code> representing the state to which the
     *            challan has to transition.
     * @param remarks
     *            a <code>String</code> representing the remarks for the current
     *            action
     * @throws ApplicationRuntimeException
     */
    public void workflowtransition(final Challan challan, final Position position, final String actionName,
            final String remarks) throws ApplicationRuntimeException {
        // to initiate the workflow

        if (challan.getState() == null && CollectionConstants.WF_ACTION_NAME_NEW_CHALLAN.equals(actionName)) {
            challan.setStatus(collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_CHALLAN,
                    CollectionConstants.CHALLAN_STATUS_CODE_CREATED));
            challan.transition().start().withComments(CollectionConstants.CHALLAN_CREATION_REMARKS)
                    .withStateValue(CollectionConstants.WF_STATE_CREATE_CHALLAN).withOwner(position)
                    .withSenderName(challan.getCreatedBy().getUsername() + "::" + challan.getCreatedBy().getName())
            .withDateInfo(new Date());
        }
        if (CollectionConstants.WF_ACTION_NAME_MODIFY_CHALLAN.equals(actionName)) {
            challan.transition().progressWithStateCopy().withComments(CollectionConstants.CHALLAN_CREATION_REMARKS)
            .withStateValue(CollectionConstants.WF_STATE_CREATE_CHALLAN).withOwner(position)
            .withSenderName(challan.getCreatedBy().getUsername() + "::" + challan.getCreatedBy().getName())
            .withDateInfo(new Date());
            LOGGER.debug("Challan Workflow Started.");

        }

        if (CollectionConstants.WF_ACTION_NAME_APPROVE_CHALLAN.equals(actionName)) {
            challan.setStatus(collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_CHALLAN,
                    CollectionConstants.CHALLAN_STATUS_CODE_APPROVED));
            challan.transition().progressWithStateCopy().withComments(remarks).withStateValue(CollectionConstants.WF_STATE_APPROVE_CHALLAN)
            .withOwner(position)
                    .withSenderName(challan.getCreatedBy().getUsername() + "::" + challan.getCreatedBy().getName())
            .withDateInfo(new Date());
        }
        if (CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN.equals(actionName))
            challan.setStatus(collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_CHALLAN,
                    CollectionConstants.CHALLAN_STATUS_CODE_VALIDATED));

        // on reject, the challan has to go to inbox of the creator
        if (CollectionConstants.WF_ACTION_NAME_REJECT_CHALLAN.equals(actionName)) {

            challan.setStatus(collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_CHALLAN,
                    CollectionConstants.CHALLAN_STATUS_CODE_REJECTED));
            // the next action can be modify or cancel challan
            challan.transition().progressWithStateCopy().withComments(remarks)
                    .withStateValue(CollectionConstants.WF_STATE_REJECTED_CHALLAN)
            .withOwner(collectionsUtil.getPositionOfUser(challan.getCreatedBy()))
                    .withSenderName(challan.getCreatedBy().getUsername() + "::" + challan.getCreatedBy().getName())
            .withDateInfo(new Date());
        }

        if (CollectionConstants.WF_ACTION_NAME_CANCEL_CHALLAN.equals(actionName)) {
            challan.setStatus(collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_CHALLAN,
                    CollectionConstants.CHALLAN_STATUS_CODE_CANCELLED));
            challan.transition().end().withComments(remarks)
                    .withStateValue(CollectionConstants.WF_STATE_CANCEL_CHALLAN)
                    .withSenderName(challan.getCreatedBy().getUsername() + "::" + challan.getCreatedBy().getName())
            .withDateInfo(new Date());
        }
        // persist(challan);

        if (CollectionConstants.WF_ACTION_NAME_CANCEL_CHALLAN.equals(actionName)
                || (CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN.equals(actionName) && challan.getState() != null )) {
            challan.transition().end().withComments("End of challan worklow")
                    .withStateValue(CollectionConstants.WF_STATE_END)
            .withSenderName(challan.getCreatedBy().getUsername() + "::" + challan.getCreatedBy().getName())
                    .withDateInfo(new Date());
            LOGGER.debug("End of Challan Workflow.");
        }
        
        if (challan.getState() == null &&
                CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN.equals(actionName)){
            challan.transition().start().end().withComments("End of challan worklow")
                    .withStateValue(CollectionConstants.WF_STATE_END)
            .withSenderName(challan.getCreatedBy().getUsername() + "::" + challan.getCreatedBy().getName())
                    .withDateInfo(new Date());
            LOGGER.debug("End of Challan Workflow.");
        }
        persist(challan);
    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }
}
