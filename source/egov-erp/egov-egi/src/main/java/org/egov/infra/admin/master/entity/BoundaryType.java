package org.egov.infra.admin.master.entity;

import java.util.Set;

import javax.persistence.Column;
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
 * 
 * @author Manasa Niranjan
 */

@Entity
@Table(name = "EG_BOUNDARY_TYPE")
public class BoundaryType extends AbstractAuditable<User, Long>{

	private static final long serialVersionUID = 7034114743461088247L;
	
	@NotBlank
	private String name;
	
	@ManyToOne
	@Valid
	@NotNull
	@JoinColumn(name = "hierarchytype_id")
	private HierarchyType hierarchyType;

	@Column(name="bndryname_local")
	private String bndryNameLocal;
	
	@ManyToOne
	@Valid
	@JoinColumn(name = "parent_id")
	private BoundaryType parent;
	
	
	private Integer hierarchy;
	
	@Transient
	private Set childBndryTypes;
	
	
	public Set getChildBndryTypes() {
		return childBndryTypes;
	}

	public void setChildBndryTypes(Set childBndryTypes) {
		this.childBndryTypes = childBndryTypes;
	}

	public void addChildBoundaryType(BoundaryType bndryType) {
		bndryType.setParent(this);
		this.childBndryTypes.add(bndryType);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HierarchyType getHierarchyType() {
		return hierarchyType;
	}

	public void setHierarchyType(HierarchyType hierarchyType) {
		this.hierarchyType = hierarchyType;
	}
	
	public BoundaryType getParent() {
		return parent;
	}

	public void setParent(BoundaryType parent) {
		this.parent = parent;
	}

	public String getBndryNameLocal() {
		return bndryNameLocal;
	}

	public void setBndryNameLocal(String bndryNameLocal) {
		this.bndryNameLocal = bndryNameLocal;
	}

	public Integer getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Integer hierarchy) {
		this.hierarchy = hierarchy;
	}

}
