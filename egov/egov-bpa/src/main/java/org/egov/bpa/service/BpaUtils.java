package org.egov.bpa.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.bpa.application.entity.BpaApplication;
import org.egov.bpa.application.workflow.BpaApplicationWorkflowCustomDefaultImpl;
import org.egov.bpa.utils.BpaConstants;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BpaUtils {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<BpaApplication> bpaApplicationWorkflowService;

    @Autowired
    private DesignationService designationService;

    public BpaApplicationWorkflowCustomDefaultImpl getInitialisedWorkFlowBean() {
        BpaApplicationWorkflowCustomDefaultImpl applicationWorkflowCustomDefaultImpl = null;
        if (null != context)
            applicationWorkflowCustomDefaultImpl = (BpaApplicationWorkflowCustomDefaultImpl) context
                    .getBean("bpaApplicationWorkflowCustomDefaultImpl");
        return applicationWorkflowCustomDefaultImpl;
    }

    public WorkFlowMatrix getWfMatrixByCurrentState(final BpaApplication application, final String currentState) {
        return bpaApplicationWorkflowService.getWfMatrix(application.getStateType(), null,
                null, BpaConstants.CREATE_ADDITIONAL_RULE_CREATE, currentState, null);
    }

    @Transactional(readOnly = true)
    public Long getUserPositionByZone(final String designation, final Long boundary) {
        final Boundary boundaryObj = getBoundaryById(boundary);
        final String[] designationarr = designation.split(",");
        List<Assignment> assignment = new ArrayList<>();
        for (final String desg : designationarr) {
            assignment = assignmentService.findByDepartmentDesignationAndBoundary(null,
                    designationService.getDesignationByName(desg).getId(), boundaryObj.getId());
            if (assignment.isEmpty()) {
                // Ward->Zone
                if (boundaryObj.getParent() != null && boundaryObj.getParent().getBoundaryType() != null
                        && boundaryObj.getParent().getBoundaryType().getName().equals(BpaConstants.BOUNDARY_TYPE_ZONE)) {
                    assignment = assignmentService.findByDeptDesgnAndParentAndActiveChildBoundaries(
                            null,
                            designationService.getDesignationByName(desg).getId(), boundaryObj.getParent().getId());
                    if (assignment.isEmpty() && boundaryObj.getParent() != null && boundaryObj.getParent().getParent() != null
                            && boundaryObj.getParent().getParent().getBoundaryType().getName()
                                    .equals(BpaConstants.BOUNDARY_TYPE_CITY))
                        assignment = assignmentService.findByDeptDesgnAndParentAndActiveChildBoundaries(null,
                                designationService.getDesignationByName(desg).getId(),
                                boundaryObj.getParent().getParent().getId());
                }
                // ward->City mapp
                if (assignment.isEmpty() && boundaryObj.getParent() != null && boundaryObj.getParent().getBoundaryType().getName()
                        .equals(BpaConstants.BOUNDARY_TYPE_CITY))
                    assignment = assignmentService.findByDeptDesgnAndParentAndActiveChildBoundaries(
                            null,
                            designationService.getDesignationByName(desg).getId(),
                            boundaryObj.getParent().getId());
            }
            if (!assignment.isEmpty())
                break;
        }
        return !assignment.isEmpty() ? assignment.get(0).getPosition().getId() : 0;
    }

    public Boundary getBoundaryById(final Long boundary) {

        return boundaryService.getBoundaryById(boundary);
    }

    @Transactional
    public void redirectToBpaWorkFlow(final BpaApplication application, final String currentState, final String remarks) {
        Long approvalPosition;
        final WorkFlowMatrix wfmatrix = getWfMatrixByCurrentState(application, currentState);
        final BpaApplicationWorkflowCustomDefaultImpl applicationWorkflowCustomDefaultImpl = getInitialisedWorkFlowBean();
        approvalPosition = getUserPositionByZone(wfmatrix.getNextDesignation(), application.getSiteDetail().get(0) != null
                ? application.getSiteDetail().get(0).getAdminBoundary().getId() : null);
        applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(application,
                approvalPosition, remarks,
                BpaConstants.CREATE_ADDITIONAL_RULE_CREATE, null);
    }

}
