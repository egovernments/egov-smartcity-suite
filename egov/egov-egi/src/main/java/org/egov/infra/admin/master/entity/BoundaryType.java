/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.admin.master.entity;

import com.google.common.base.Objects;
import com.google.gson.annotations.Expose;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Set;

import static org.egov.infra.admin.master.entity.BoundaryType.SEQ_BOUNDARY_TYPE;

@Entity
@Table(name = "EG_BOUNDARY_TYPE")
@SequenceGenerator(name = SEQ_BOUNDARY_TYPE, sequenceName = SEQ_BOUNDARY_TYPE, allocationSize = 1)
public class BoundaryType extends AbstractAuditable {

    public static final String SEQ_BOUNDARY_TYPE = "SEQ_EG_BOUNDARY_TYPE";
    private static final long serialVersionUID = 859229842367886336L;
    @Expose
    @Id
    @GeneratedValue(generator = SEQ_BOUNDARY_TYPE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @SafeHtml
    private String name;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "hierarchytype")
    private HierarchyType hierarchyType;

    @ManyToOne
    @JoinColumn(name = "parent")
    private BoundaryType parent;

    private Long hierarchy;

    @SafeHtml
    private String localName;

    @Transient
    private String parentName;

    @Transient
    private Set<BoundaryType> childBoundaryTypes;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public Long getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Long hierarchy) {
        this.hierarchy = hierarchy;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Set<BoundaryType> getChildBoundaryTypes() {
        return childBoundaryTypes;
    }

    public void setChildBoundaryTypes(Set<BoundaryType> childBoundaryTypes) {
        this.childBoundaryTypes = childBoundaryTypes;
    }

    public void addChildBoundaryType(BoundaryType boundaryType) {
        boundaryType.setParent(this);
        childBoundaryTypes.add(boundaryType);
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(final String localName) {
        this.localName = localName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BoundaryType))
            return false;
        BoundaryType that = (BoundaryType) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(hierarchyType, that.hierarchyType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, hierarchyType);
    }
}