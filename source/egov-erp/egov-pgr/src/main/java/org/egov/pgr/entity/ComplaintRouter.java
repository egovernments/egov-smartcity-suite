package org.egov.pgr.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.pims.commons.Position;

@Entity
@Table(name = "pgr_router")
public class ComplaintRouter extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 5691022600220045218L;

    @ManyToOne
    @Valid
    @JoinColumn(name = "complainttypeid")
    private ComplaintType complaintType;

    @ManyToOne
    @Valid
    @JoinColumn(name = "bndryid")
    private Boundary boundary;

    @ManyToOne
    @Valid
    @NotNull
    @JoinColumn(name = "position")
    private Position position;

    public ComplaintType getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(final ComplaintType complaintType) {
        this.complaintType = complaintType;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(final Boundary boundary) {
        this.boundary = boundary;
    }
}
