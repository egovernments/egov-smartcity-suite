/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.admin.master.entity;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import static org.egov.infra.admin.master.entity.CrossHierarchy.SEQ_CROSSHIERARCHY;

@Entity
@Table(name = "EG_CROSSHIERARCHY")
@SequenceGenerator(name = SEQ_CROSSHIERARCHY, sequenceName = SEQ_CROSSHIERARCHY, allocationSize = 1)
public class CrossHierarchy extends AbstractPersistable<Long> {
    public static final String SEQ_CROSSHIERARCHY = "seq_eg_crosshierarchy";
    private static final long serialVersionUID = 5586809829548733921L;
    @Id
    @GeneratedValue(generator = SEQ_CROSSHIERARCHY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent")
    @Fetch(value = FetchMode.JOIN)
    private Boundary parent;

    @ManyToOne
    @JoinColumn(name = "child")
    @Fetch(value = FetchMode.JOIN)
    private Boundary child;

    @ManyToOne
    @JoinColumn(name = "parenttype")
    @Fetch(value = FetchMode.JOIN)
    private BoundaryType parentType;

    @ManyToOne
    @JoinColumn(name = "childtype")
    @Fetch(value = FetchMode.JOIN)
    private BoundaryType childType;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    public Boundary getParent() {
        return parent;
    }

    public void setParent(final Boundary parent) {
        this.parent = parent;
    }

    public Boundary getChild() {
        return child;
    }

    public void setChild(final Boundary child) {
        this.child = child;
    }

    public BoundaryType getParentType() {
        return parentType;
    }

    public void setParentType(BoundaryType parentType) {
        this.parentType = parentType;
    }

    public BoundaryType getChildType() {
        return childType;
    }

    public void setChildType(BoundaryType childType) {
        this.childType = childType;
    }

}
