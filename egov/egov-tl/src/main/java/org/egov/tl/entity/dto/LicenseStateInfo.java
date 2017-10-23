package org.egov.tl.entity.dto;

/**
 * Created by jayashree on 10/10/17.
 */
public class LicenseStateInfo {
    private Long wfMatrixRef;
    private Long rejectionPosition;

    public Long getWfMatrixRef() {
        return wfMatrixRef;
    }

    public void setWfMatrixRef(Long wfMatrixRef) {
        this.wfMatrixRef = wfMatrixRef;
    }

    public Long getRejectionPosition() {
        return rejectionPosition;
    }

    public void setRejectionPosition(Long rejectionPosition) {
        this.rejectionPosition = rejectionPosition;
    }
}
