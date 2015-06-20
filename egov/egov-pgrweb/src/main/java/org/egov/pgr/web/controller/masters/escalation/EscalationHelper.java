package org.egov.pgr.web.controller.masters.escalation;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pims.commons.Position;

public class EscalationHelper {
    private Position fromPosition;
    private Position toPosition;
    private ComplaintType complaintType;
    
    public Position getFromPosition() {
        return fromPosition;
    }
    public void setFromPosition(Position fromPosition) {
        this.fromPosition = fromPosition;
    }
    public Position getToPosition() {
        return toPosition;
    }
    public void setToPosition(Position toPosition) {
        this.toPosition = toPosition;
    }
    public ComplaintType getComplaintType() {
        return complaintType;
    }
    public void setComplaintType(ComplaintType complaintType) {
        this.complaintType = complaintType;
    }
}
