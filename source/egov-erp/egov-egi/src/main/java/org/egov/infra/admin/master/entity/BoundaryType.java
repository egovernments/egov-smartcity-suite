package org.egov.infra.admin.master.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Manasa Niranjan
 */

@Entity
@Table(name = "EG_BOUNDARY_TYPE")
public class BoundaryType extends AbstractAuditable<User, Long> {

    private static final long serialVersionUID = 7034114743461088247L;

    @NotBlank
    private String name;

    @ManyToOne
    @Valid
    @NotNull
    @JoinColumn(name = "hierarchytype")
    private HierarchyType hierarchyType;

    @ManyToOne
    @Valid
    @JoinColumn(name = "parent")
    private BoundaryType parent;

    private Integer hierarchy;

    @Transient
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(final String parentName) {
        this.parentName = parentName;
    }

    @Transient
    private Set childBoundaryTypes;

    public Set getChildBoundaryTypes() {
        return childBoundaryTypes;
    }

    public void setChildBoundaryTypes(final Set childBoundaryTypes) {
        this.childBoundaryTypes = childBoundaryTypes;
    }

    public void addChildBoundaryType(final BoundaryType boundaryType) {
        boundaryType.setParent(this);
        childBoundaryTypes.add(boundaryType);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public HierarchyType getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(final HierarchyType hierarchyType) {
        this.hierarchyType = hierarchyType;
    }

    public BoundaryType getParent() {
        return parent;
    }

    public void setParent(final BoundaryType parent) {
        this.parent = parent;
    }

    public Integer getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(final Integer hierarchy) {
        this.hierarchy = hierarchy;
    }

}