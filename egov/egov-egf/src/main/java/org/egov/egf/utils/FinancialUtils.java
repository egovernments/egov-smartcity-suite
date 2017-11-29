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
package org.egov.egf.utils;

import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.model.bills.DocumentUpload;
import org.egov.model.masters.AccountCodePurpose;
import org.egov.pims.commons.Position;
import org.egov.utils.FinancialConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author venki
 */

@Service
@Transactional(readOnly = true)
public class FinancialUtils {

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
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PositionMasterService positionMasterService;
    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private EisCommonService eisCommonService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private FileStoreService fileStoreService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

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

    public String getApproverDetails(final String workFlowAction, final State<Position> state, final Long id, final Long approvalPosition) {
        final Assignment currentUserAssignment = assignmentService.getPrimaryAssignmentForGivenRange(securityUtils
                .getCurrentUser().getId(), new Date(), new Date());

        Assignment assignObj = null;
        List<Assignment> asignList = null;
        if (approvalPosition != null)
            assignObj = assignmentService.getPrimaryAssignmentForPositon(approvalPosition);

        if (assignObj != null) {
            asignList = new ArrayList<>();
            asignList.add(assignObj);
        } else if (approvalPosition != null)
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());

        String nextDesign = "";
        if (asignList != null)
            nextDesign = !asignList.isEmpty() ? asignList.get(0).getDesignation().getName() : "";

        String approverDetails;
        if (!FinancialConstants.BUTTONREJECT.toString().equalsIgnoreCase(workFlowAction))
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
        } else
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

    public boolean isBillEditable(final State<Position> state) {
        boolean isEditable = false;
        if (state.getOwnerPosition() != null && state.getOwnerPosition().getDeptDesig() != null
                && state.getOwnerPosition().getDeptDesig().getDesignation() != null) {
            final String designationName = state.getOwnerPosition().getDeptDesig().getDesignation().getName();
            final AppConfig appConfig = appConfigService.getAppConfigByKeyName(FinancialConstants.BILL_EDIT_DESIGNATIONS);
            for (final AppConfigValues appConfigValues : appConfig.getConfValues())
                if (designationName.equals(appConfigValues.getValue()))
                    isEditable = true;

        }
        return isEditable;
    }

    public List<HashMap<String, Object>> getHistory(final State<Position> state, final List<StateHistory<Position>> history) {
        User user = null;
        final List<HashMap<String, Object>> historyTable = new ArrayList<>();
        final HashMap<String, Object> map = new HashMap<>(0);
        if (null != state) {
            if (!history.isEmpty() && history != null)
                Collections.reverse(history);
            for (final StateHistory<Position> stateHistory : history) {
                final HashMap<String, Object> workflowHistory = new HashMap<>(0);
                workflowHistory.put("date", stateHistory.getDateInfo());
                workflowHistory.put("comments", stateHistory.getComments());
                workflowHistory.put("updatedBy", stateHistory.getLastModifiedBy().getUsername() + "::"
                        + stateHistory.getLastModifiedBy().getName());
                workflowHistory.put("status", stateHistory.getValue());
                final Position owner = stateHistory.getOwnerPosition();
                user = stateHistory.getOwnerUser();
                if (null != user) {
                    workflowHistory.put("user", user.getUsername() + "::" + user.getName());
                    workflowHistory.put("department",
                            null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                                    .getDepartmentForUser(user.getId()).getName() : "");
                } else if (null != owner && null != owner.getDeptDesig()) {
                    user = eisCommonService.getUserForPosition(owner.getId(), new Date());
                    workflowHistory
                            .put("user", null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                    workflowHistory.put("department", null != owner.getDeptDesig().getDepartment() ? owner.getDeptDesig()
                            .getDepartment().getName() : "");
                }
                historyTable.add(workflowHistory);
            }
            map.put("date", state.getDateInfo());
            map.put("comments", state.getComments() != null ? state.getComments() : "");
            map.put("updatedBy", state.getLastModifiedBy().getUsername() + "::" + state.getLastModifiedBy().getName());
            map.put("status", state.getValue());
            final Position ownerPosition = state.getOwnerPosition();
            user = state.getOwnerUser();
            if (null != user) {
                map.put("user", user.getUsername() + "::" + user.getName());
                map.put("department", null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                        .getDepartmentForUser(user.getId()).getName() : "");
            } else if (null != ownerPosition && null != ownerPosition.getDeptDesig()) {
                user = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                map.put("user", null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                map.put("department", null != ownerPosition.getDeptDesig().getDepartment() ? ownerPosition
                        .getDeptDesig().getDepartment().getName() : "");
            }
            historyTable.add(map);
        }
        return historyTable;
    }

    public AccountCodePurpose getAccountCodePurposeById(final Long id) {

        return getCurrentSession().load(AccountCodePurpose.class, id);

    }

    public List<DocumentUpload> getDocumentDetails(final List<DocumentUpload> files, final Object object, final String objectType) {
        final List<DocumentUpload> documentDetailsList = new ArrayList<>();

        Long id;
        Method method;
        try {
            method = object.getClass().getMethod("getId", null);
            id = (Long) method.invoke(object, null);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new ApplicationRuntimeException("error.expense.bill.document.error", e);
        }

        for (DocumentUpload doc : files) {
            final DocumentUpload documentDetails = new DocumentUpload();
            documentDetails.setObjectId(id);
            documentDetails.setObjectType(objectType);
            documentDetails.setFileStore(fileStoreService.store(doc.getInputStream(), doc.getFileName(),
                    doc.getContentType(), FinancialConstants.FILESTORE_MODULECODE));
            documentDetailsList.add(documentDetails);

        }
        return documentDetailsList;
    }

}
