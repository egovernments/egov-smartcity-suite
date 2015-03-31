package org.egov.pims.commons;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractAuditable;

@Entity
@Table(name = "eg_position")
public class Position extends AbstractAuditable<User, Long> {
    private static final long serialVersionUID = -7237503685614187960L;

    @Column(name="name",unique=true)
    private String name;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="deptDesig")
    private DeptDesig deptDesig;
    private boolean isPostOutsourced;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public DeptDesig getDeptDesigId() {
        return deptDesig;
    }

    public void setDeptDesigId(final DeptDesig deptDesigId) {
        deptDesig = deptDesigId;
    }

    public boolean isPostOutsourced() {
        return isPostOutsourced;
    }

    public void setPostOutsourced(final boolean isPostOutsourced) {
        this.isPostOutsourced = isPostOutsourced;
    }

}
