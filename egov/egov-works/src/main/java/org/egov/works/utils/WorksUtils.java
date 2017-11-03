/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
package org.egov.works.utils;

import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pims.commons.Position;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.enums.LineEstimateStatus;
import org.egov.works.lineestimate.repository.DocumentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class WorksUtils {

    @Autowired
    private DocumentDetailsRepository documentDetailsRepository;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private DepartmentService departmentService;

    public void persistDocuments(final List<DocumentDetails> documentDetailsList) {
        if (documentDetailsList != null && !documentDetailsList.isEmpty())
            for (final DocumentDetails doc : documentDetailsList)
                documentDetailsRepository.save(doc);
    }

    public List<DocumentDetails> getDocumentDetails(final MultipartFile[] files, final Object object, final String objectType)
            throws IOException {
        final List<DocumentDetails> documentDetailsList = new ArrayList<>();

        Long id = null;
        Method method = null;
        try {
            method = object.getClass().getMethod("getId", null);
            id = (Long) method.invoke(object, null);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new ApplicationRuntimeException("lineestimate.document.error", e);
        }

        if (files != null && files.length > 0)
            for (int i = 0; i < files.length; i++)
                if (!files[i].isEmpty()) {
                    final DocumentDetails documentDetails = new DocumentDetails();
                    documentDetails.setObjectId(id);
                    documentDetails.setObjectType(objectType);
                    documentDetails.setFileStore(fileStoreService.store(files[i].getInputStream(), files[i].getOriginalFilename(),
                            files[i].getContentType(), WorksConstants.FILESTORE_MODULECODE));
                    documentDetailsList.add(documentDetails);
                }
        return documentDetailsList;
    }

    public List<DocumentDetails> findByObjectIdAndObjectType(final Long objectId, final String objectType) {
        return documentDetailsRepository.findByObjectIdAndObjectType(objectId, objectType);
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
            final Position posObjToClerk = positionMasterService
                    .getCurrentPositionForUser(createdById);
            approverPosition = posObjToClerk.getId();
        }
        return approverPosition;
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

    public String getPathVars(final EgwStatus status, final State<Position> state, final Long id, final Long approvalPosition) {
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

        String pathVars = "";
        if (!status.getCode().equals(LineEstimateStatus.REJECTED.toString()))
            pathVars = id + ","
                    + getApproverName(approvalPosition) + ","
                    + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : "") + ","
                    + (nextDesign != null ? nextDesign : "");
        else
            pathVars = id + ","
                    + getApproverName(state.getOwnerPosition().getId()) + ","
                    + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : "") + ","
                    + (nextDesign != null ? state.getOwnerPosition().getDeptDesig().getDesignation().getName() : "");
        return pathVars;
    }

    public String getUserDesignation(final User user) {
        List<Assignment> assignmentList = assignmentService.findByEmployeeAndGivenDate(user != null ? user.getId() : null, new Date());
        if (!assignmentList.isEmpty())
            return assignmentList.get(0).getDesignation().getName();
        return null;
    }

    public Long getDefaultDepartmentId() {
        final String defaultApproverDept = worksApplicationProperties.getDefaultApproverDepartment();
        if (defaultApproverDept != null) {
            final Department approverDepartment = departmentService.getDepartmentByName(defaultApproverDept);
            if (approverDepartment != null)
                return approverDepartment.getId();
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public EgwStatus getStatusByModuleAndCode(final String moduleType, final String code) {
        return egwStatusHibernateDAO.getStatusByModuleAndCode(moduleType, code);
    }
}
