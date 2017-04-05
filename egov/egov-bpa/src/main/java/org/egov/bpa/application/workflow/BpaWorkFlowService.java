package org.egov.bpa.application.workflow;

import java.util.Date;
import java.util.List;

import org.egov.bpa.application.entity.BpaApplication;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;

public class BpaWorkFlowService {

    @Autowired
    protected AssignmentService assignmentService;

    public Assignment getWorkFlowInitiator(BpaApplication application) {
        Assignment wfInitiator = null;
        List<Assignment> assignment;
        if (application != null) {
            if (application.getState() != null
                    && application.getState().getInitiatorPosition() != null) {
                wfInitiator = getUserAssignmentByPassingPositionAndUser(application
                        .getCreatedBy(), application.getState().getInitiatorPosition());

                if (wfInitiator == null) {
                    assignment = assignmentService
                            .getAssignmentsForPosition(application.getState().getInitiatorPosition().getId(),
                                    new Date());
                    wfInitiator = getActiveAssignment(assignment);
                }
            } else {

                wfInitiator = assignmentService.getPrimaryAssignmentForUser(application
                        .getCreatedBy().getId());

            }
        }
        return wfInitiator;
    }

    private Assignment getActiveAssignment(List<Assignment> assignment) {
        Assignment wfInitiator = null;
        for (final Assignment assign : assignment)
            if (assign.getEmployee().isActive()) {
                wfInitiator = assign;
                break;
            }
        return wfInitiator;
    }

    public boolean validateUserHasSamePositionAsInitiator(Long userId, Position position) {

        Boolean userHasSamePosition = false;

        if (userId != null && position != null) {
            List<Assignment> assignmentList = assignmentService.findByEmployeeAndGivenDate(userId, new Date());
            for (final Assignment assignment : assignmentList) {
                if (position.getId() == assignment.getPosition().getId()) {
                    userHasSamePosition = true;
                }
            }
        }
        return userHasSamePosition;
    }

    private Assignment getUserAssignmentByPassingPositionAndUser(final User user, Position position) {

        Assignment wfInitiatorAssignment = null;

        if (user != null && position != null) {
            List<Assignment> assignmentList = assignmentService.findByEmployeeAndGivenDate(user.getId(), new Date());
            for (final Assignment assignment : assignmentList) {
                if (position.getId() == assignment.getPosition().getId()) {
                    wfInitiatorAssignment = assignment;
                }
            }
        }

        return wfInitiatorAssignment;
    }
}
