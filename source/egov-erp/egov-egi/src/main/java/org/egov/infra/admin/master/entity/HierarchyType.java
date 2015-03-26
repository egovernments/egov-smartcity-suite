package org.egov.infra.admin.master.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Future;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Represents the top classification for Boundary
 * 
 * @author nayeem
 * @see Boundary
 * @see BoundaryType
 */

@Entity
@Table(name = "eg_hierarchy_type", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "code" }))
public class HierarchyType extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -7131667806935923935L;

    @NotBlank
    @Length(max = 128)
    private String name;

    @NotBlank
    @Length(max = 50)
    private String code;

    @Column(name = "updatedtime")
    private Date updatedTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append("HierarchyType [")
                .append("name=").append(name)
                .append(", code=").append(code)
                .append("]").toString();
    }
}
