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
package org.egov.egf.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pims.commons.Position;
import org.egov.utils.FinancialConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author venki
 *
 */

@Service
@Transactional(readOnly = true)
public class FinancialUtils {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    public static final Map<String, String> VOUCHER_SUBTYPES = new HashMap<String, String>() {
        private static final long serialVersionUID = -2168753508482839041L;

        {
            put(FinancialConstants.JOURNALVOUCHER_NAME_GENERAL, FinancialConstants.GENERAL);
            put(FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS, FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS);
            put(FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE, FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE);
            put(FinancialConstants.STANDARD_SUBTYPE_FIXED_ASSET, FinancialConstants.STANDARD_SUBTYPE_FIXED_ASSET);
            put(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT, FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
        }
    };

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public EgwStatus getStatusByModuleAndCode(final String moduleType, final String code) {
        return egwStatusHibernateDAO.getStatusByModuleAndCode(moduleType, code);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<EgwStatus> getStatusByModule(final String moduleType) {
        return egwStatusHibernateDAO.getStatusByModule(moduleType);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public EgwStatus getStatusById(final Integer id) {
        return egwStatusHibernateDAO.findById(id, true);
    }

    public String getApproverDetails(final EgwStatus status, final State state, final Long id, final Long approvalPosition) {
        final Assignment currentUserAssignment = assignmentService.getPrimaryAssignmentForGivenRange(securityUtils
                .getCurrentUser().getId(), new Date(), new Date());

        Assignment assignObj = null;
        List<Assignment> asignList = null;
        if (approvalPosition != null)
            assignObj = assignmentService.getPrimaryAssignmentForPositon(approvalPosition);

        if (assignObj != null) {
            asignList = new ArrayList<>();
            asignList.add(assignObj);
        } else if (assignObj == null && approvalPosition != null)
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());

        String nextDesign = "";
        if (asignList != null)
            nextDesign = !asignList.isEmpty() ? asignList.get(0).getDesignation().getName() : "";

        String approverDetails;
        if (!status.getCode().equals(FinancialConstants.WORKFLOW_STATUS_CODE_REJECTED.toString()))
            approverDetails = id + ","
                    + getApproverName(approvalPosition) + ","
                    + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : "") + ","
                    + (nextDesign != null ? nextDesign : "");
        else
            approverDetails = id + ","
                    + getApproverName(state.getOwnerPosition().getId()) + ","
                    + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : "") + ","
                    + (nextDesign != null ? state.getOwnerPosition().getDeptDesig().getDesignation().getName() : "");
        return approverDetails;
    }

    public String getApproverName(final Long approvalPosition) {
        Assignment assignment = null;
        List<Assignment> asignList = null;
        if (approvalPosition != null)
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPosition, new Date());
        if (assignment != null) {
            asignList = new ArrayList<>();
            asignList.add(assignment);
        } else if (assignment == null)
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());
        return !asignList.isEmpty() ? asignList.get(0).getEmployee().getName() : "";
    }

    public Long getApproverPosition(final String designationName, final State state, final Long createdById) {
        final Set<StateHistory> stateHistoryList = state.getHistory();
        Long approverPosition = 0l;
        final String[] desgnArray = designationName != null ? designationName.split(",") : null;
        if (stateHistoryList != null && !stateHistoryList.isEmpty()) {
            for (final StateHistory stateHistory : stateHistoryList)
                if (stateHistory.getOwnerPosition() != null) {
                    final List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(
                            stateHistory.getOwnerPosition().getId(), new Date());
                    for (final Assignment assgn : assignmentList)
                        if (desgnArray != null)
                            for (final String str : desgnArray)
                                if (assgn.getDesignation().getName().equalsIgnoreCase(str)) {
                                    approverPosition = stateHistory.getOwnerPosition().getId();
                                    break;
                                }
                }
            if (approverPosition == 0) {
                final State stateObj = state;
                final List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(stateObj
                        .getOwnerPosition().getId(), new Date());
                for (final Assignment assgn : assignmentList)
                    if (desgnArray != null)
                        for (final String str : desgnArray)
                            if (assgn.getDesignation().getName().equalsIgnoreCase(str)) {
                                approverPosition = stateObj.getOwnerPosition().getId();
                                break;
                            }
            }
        } else {
            final Position posObjToClerk = positionMasterService.getCurrentPositionForUser(createdById);
            approverPosition = posObjToClerk.getId();
        }
        return approverPosition;
    }

}
