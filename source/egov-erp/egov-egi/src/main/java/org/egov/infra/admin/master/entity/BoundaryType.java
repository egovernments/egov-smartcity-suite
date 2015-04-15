/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
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
import org.egov.search.domain.Searchable;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Manasa Niranjan
 */

@Entity
@Table(name = "EG_BOUNDARY_TYPE")
@Searchable
public class BoundaryType extends AbstractAuditable<User, Long> {

    private static final long serialVersionUID = 7034114743461088247L;

    @NotBlank
    @Searchable(name="name")
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

    private Long hierarchy;
    
    private String localName;

    @Transient
    private String parentName;

    @Transient
    private Set childBoundaryTypes;
    

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

    public Long getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(final Long hierarchy) {
        this.hierarchy = hierarchy;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(final String parentName) {
        this.parentName = parentName;
    }
    
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
    
    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }
}