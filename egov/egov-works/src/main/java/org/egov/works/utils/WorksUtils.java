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
package org.egov.works.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pims.commons.Position;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.enums.LineEstimateStatus;
import org.egov.works.lineestimate.repository.DocumentDetailsRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private FileStoreMapperRepository fileStoreMapperRepository;

    public void persistDocuments(final List<DocumentDetails> documentDetailsList) {
        if (documentDetailsList != null && !documentDetailsList.isEmpty())
            for (final DocumentDetails doc : documentDetailsList)
                documentDetailsRepository.save(doc);
    }

    public List<DocumentDetails> getDocumentDetails(final MultipartFile[] files, final Object object,
            final String objectType) throws IOException {
        final List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();

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
                    documentDetails.setFileStore(
                            fileStoreService.store(files[i].getInputStream(), files[i].getOriginalFilename(),
                                    files[i].getContentType(), WorksConstants.FILESTORE_MODULECODE));
                    documentDetailsList.add(documentDetails);
                }
        return documentDetailsList;
    }

    public void deleteDocuments(final Long documentId) {
        documentDetailsRepository.delete(documentId);
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
                    final List<Assignment> assignmentList = assignmentService
                            .getAssignmentsForPosition(stateHistory.getOwnerPosition().getId(), new Date());
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
                final List<Assignment> assignmentList = assignmentService
                        .getAssignmentsForPosition(stateObj.getOwnerPosition().getId(), new Date());
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

    public String getApproverName(final Long approvalPosition) {
        Assignment assignment = null;
        List<Assignment> asignList = null;
        if (approvalPosition != null)
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPosition, new Date());
        if (assignment != null) {
            asignList = new ArrayList<Assignment>();
            asignList.add(assignment);
        } else if (assignment == null)
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());
        return !asignList.isEmpty() ? asignList.get(0).getEmployee().getName() : "";
    }

    public String getPathVars(final EgwStatus status, final State state, final Long id, final Long approvalPosition) {
        final Assignment currentUserAssignment = assignmentService
                .getPrimaryAssignmentForGivenRange(securityUtils.getCurrentUser().getId(), new Date(), new Date());

        Assignment assignObj = null;
        List<Assignment> asignList = null;
        if (approvalPosition != null)
            assignObj = assignmentService.getPrimaryAssignmentForPositon(approvalPosition);

        if (assignObj != null) {
            asignList = new ArrayList<Assignment>();
            asignList.add(assignObj);
        } else if (assignObj == null && approvalPosition != null)
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());

        String nextDesign = "";
        if (asignList != null)
            nextDesign = !asignList.isEmpty() ? asignList.get(0).getDesignation().getName() : "";

        String pathVars = "";
        if (!status.getCode().equalsIgnoreCase(LineEstimateStatus.REJECTED.toString()))
            pathVars = id + "," + getApproverName(approvalPosition) + ","
                    + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : "") + ","
                    + (nextDesign != null ? nextDesign : "");
        else
            pathVars = id + "," + getApproverName(state.getOwnerPosition().getId()) + ","
                    + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : "") + ","
                    + (nextDesign != null ? state.getOwnerPosition().getDeptDesig().getDesignation().getName() : "");
        return pathVars;
    }

    public String getUserDesignation(final User user) {
        List<Assignment> assignmentList = new ArrayList<Assignment>();
        assignmentList = assignmentService.findByEmployeeAndGivenDate(user != null ? user.getId() : null, new Date());
        if (!assignmentList.isEmpty())
            return assignmentList.get(0).getDesignation().getName();
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public EgwStatus getStatusByModuleAndCode(final String moduleType, final String code) {
        return egwStatusHibernateDAO.getStatusByModuleAndCode(moduleType, code);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<EgwStatus> getStatusByModule(final String moduleType) {
        return egwStatusHibernateDAO.getStatusByModule(moduleType);
    }

    public String getExceptionalUOMS() {
        final List<AppConfigValues> exceptionalUomValues = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_EXCEPTIONALUOMS);
        String exceptionaluoms = "";
        for (final AppConfigValues appVal : exceptionalUomValues)
            exceptionaluoms = exceptionaluoms + appVal.getValue() + ":";
        return exceptionaluoms;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public EgwStatus getStatusById(final Integer id) {
        return egwStatusHibernateDAO.findById(id, true);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public CFinancialYear getFinancialYearByDate(final Date asOnDate) {
        return financialYearHibernateDAO.getFinYearByDate(asOnDate);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public CFinancialYear getActiveForPostingFinancialYearByDate(final Date asOnDate) {
        return financialYearHibernateDAO.getFinancialYearByDate(asOnDate);
    }

    public List<Hashtable<String, Object>> getWorkFlowHistory(final State state, final List<StateHistory> history) {
        User user = null;
        Assignment assignment = null;
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        final List<Hashtable<String, Object>> historyTable = new ArrayList<Hashtable<String, Object>>();
        final Hashtable<String, Object> map = new Hashtable<String, Object>(0);
        if (null != state) {
            for (final StateHistory stateHistory : history)
                if (!stateHistory.getValue().equals(WorksConstants.NEW)) {
                    final Hashtable<String, Object> HistoryMap = new Hashtable<String, Object>(0);
                    HistoryMap.put("date", sdf.format(stateHistory.getDateInfo()));
                    HistoryMap.put("comments", stateHistory.getComments());
                    if (StringUtils.isNotBlank(stateHistory.getNextAction()))
                        HistoryMap.put("status", stateHistory.getValue() + "-" + stateHistory.getNextAction());
                    else
                        HistoryMap.put("status", stateHistory.getValue());
                    user = stateHistory.getLastModifiedBy();
                    if (null != user) {
                        assignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
                        HistoryMap.put("user", user.getName());
                        HistoryMap.put("designation", assignment.getDesignation().getName());
                    }
                    historyTable.add(HistoryMap);
                }
            if (!state.getValue().equals(WorksConstants.NEW)) {
                map.put("date", sdf.format(state.getDateInfo()));
                map.put("comments", state.getComments() != null ? state.getComments() : "");
                if (StringUtils.isNotBlank(state.getNextAction()))
                    map.put("status", state.getValue() + "-" + state.getNextAction());
                else
                    map.put("status", state.getValue());
                user = state.getLastModifiedBy();
                if (null != user) {
                    assignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
                    map.put("user", user.getName());
                    map.put("designation", assignment.getDesignation().getName());
                }
                historyTable.add(map);
            }
        }
        return historyTable;
    }

    @SuppressWarnings("unchecked")
    public List<FileStoreMapper> getLatestSorRateUploadFiles(final String name) {
        return entityManager.unwrap(Session.class)
                .createQuery("from FileStoreMapper where fileName like :name order by id desc ")
                .setString("name", "%" + name + "%")
                .setMaxResults(5)
                .list();
    }

    public Date getCutOffDate() {
        Date cutOffDate = null;
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        final List<AppConfigValues> cutOffDateAppConfig = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_CUTOFFDATEFORLEGACYDATAENTRY);
        if (cutOffDateAppConfig != null && !cutOffDateAppConfig.isEmpty()) {
            final AppConfigValues appConfigValue = cutOffDateAppConfig.get(0);
            try {
                cutOffDate = formatter.parse(appConfigValue.getValue());
            } catch (final ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return cutOffDate;
    }

    public List<Hashtable<String, Object>> getHistory(final State state, final List<StateHistory> history) {
        User user = null;
        final List<Hashtable<String, Object>> historyTable = new ArrayList<Hashtable<String, Object>>();
        final Hashtable<String, Object> map = new Hashtable<String, Object>(0);
        if (null != state) {
            for (final StateHistory stateHistory : history) {
                final Hashtable<String, Object> HistoryMap = new Hashtable<String, Object>(0);
                HistoryMap.put("date", stateHistory.getDateInfo());
                HistoryMap.put("comments", stateHistory.getComments());
                HistoryMap.put("updatedBy", stateHistory.getLastModifiedBy().getUsername() + "::"
                        + stateHistory.getLastModifiedBy().getName());
                HistoryMap.put("status", stateHistory.getValue());
                final Position owner = stateHistory.getOwnerPosition();
                user = stateHistory.getOwnerUser();
                if (null != user) {
                    HistoryMap.put("user", user.getUsername() + "::" + user.getName());
                    HistoryMap.put("department", null != eisCommonService.getDepartmentForUser(user.getId())
                            ? eisCommonService.getDepartmentForUser(user.getId()).getName() : "");
                } else if (null != owner && null != owner.getDeptDesig()) {
                    user = eisCommonService.getUserForPosition(owner.getId(), new Date());
                    HistoryMap.put("user",
                            null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                    HistoryMap.put("department", null != owner.getDeptDesig().getDepartment()
                            ? owner.getDeptDesig().getDepartment().getName() : "");
                }
                historyTable.add(HistoryMap);
            }
            map.put("date", state.getDateInfo());
            map.put("comments", state.getComments() != null ? state.getComments() : "");
            map.put("updatedBy", state.getLastModifiedBy().getUsername() + "::" + state.getLastModifiedBy().getName());
            map.put("status", state.getValue());
            final Position ownerPosition = state.getOwnerPosition();
            user = state.getOwnerUser();
            if (null != user) {
                map.put("user", user.getUsername() + "::" + user.getName());
                map.put("department", null != eisCommonService.getDepartmentForUser(user.getId())
                        ? eisCommonService.getDepartmentForUser(user.getId()).getName() : "");
            } else if (null != ownerPosition && null != ownerPosition.getDeptDesig()) {
                user = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                map.put("user", null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                map.put("department", null != ownerPosition.getDeptDesig().getDepartment()
                        ? ownerPosition.getDeptDesig().getDepartment().getName() : "");
            }
            historyTable.add(map);
        }
        return historyTable;
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

    public List<Department> getUserDepartments(final User currentUser) {
        final List<Assignment> assignments = assignmentService.findByEmployeeAndGivenDate(currentUser.getId(),
                new Date());
        final List<Department> uniqueDepartmentList = new ArrayList<Department>();
        Department prevDepartment = new Department();
        final Iterator iterator = assignments.iterator();
        while (iterator.hasNext()) {
            final Assignment assignment = (Assignment) iterator.next();
            if (!assignment.getDepartment().getName().equals(prevDepartment.getName()))
                uniqueDepartmentList.add(assignment.getDepartment());
            prevDepartment = assignment.getDepartment();
        }
        return uniqueDepartmentList;
    }
    
    public List<FileStoreMapper> saveDocuments(MultipartFile[] files) throws IOException {
        final List<FileStoreMapper> fileStoreMappers = new ArrayList<>();
        if (files != null && files.length > 0)
            for (int i = 0; i < files.length; i++)
                if (!files[i].isEmpty()) {
                    final FileStoreMapper fileStoreMapper = fileStoreService.store(files[i].getInputStream(),
                            files[i].getOriginalFilename(),
                            files[i].getContentType(), WorksConstants.FILESTORE_MODULECODE);
                    fileStoreMapperRepository.save(fileStoreMapper);
                    fileStoreMappers.add(fileStoreMapper);
                }
        return fileStoreMappers;
    }
}
