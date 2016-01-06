/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.adtax.workflow;


import java.util.Date;

import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.Position;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class ApplicationCommonWorkflow.
 */
public abstract class ApplicationWorkflowCustomImpl implements ApplicationWorkflowCustom {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationWorkflowCustomImpl.class);


    @Autowired
    private SecurityUtils securityUtils;


    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private AdvertisementPermitDetailService advertisementPermitDetailService;
    

    
    @Autowired
    private SimpleWorkflowService<AdvertisementPermitDetail> advertisementPermitDetailWorkflowService;
    @Autowired
    public ApplicationWorkflowCustomImpl() {

    }

    @Override
    public void createCommonWorkflowTransition(final AdvertisementPermitDetail advertisementPermitDetail,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if(LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        String currState = "";
        String natureOfwork="CREATEHOARDING";
         
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            WorkFlowMatrix wfmatrix = null;
            if (null == advertisementPermitDetail.getState()) {
                wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(), null,
                        null, additionalRule, currState, null);
                advertisementPermitDetail.transition().start().withSenderName(user.getUsername() + "::"+ user.getName()).withComments(approvalComent)
                .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
            } else if ("Approve".equalsIgnoreCase(workFlowAction)) {
                
                wfmatrix = advertisementPermitDetailWorkflowService.getWfMatrix(advertisementPermitDetail.getStateType(), null,
                        null, additionalRule, advertisementPermitDetail.getCurrentState().getValue(), null);
               
                if (wfmatrix.getNextAction().equalsIgnoreCase("END"))
                    advertisementPermitDetail.transition(true).end().withSenderName(user.getUsername() + "::"+ user.getName())
                    .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork);
            } 
        if(LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }
    
}
