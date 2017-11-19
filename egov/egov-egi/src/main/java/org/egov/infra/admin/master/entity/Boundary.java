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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.google.gson.annotations.Expose;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.CompositeUnique;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.egov.infra.admin.master.entity.Boundary.SEQ_BOUNDARY;

@Entity
@CompositeUnique(fields = {"boundaryNum", "boundaryType"}, enableDfltMsg = true)
@Unique(fields = "code", enableDfltMsg = true)
@Table(name = "EG_BOUNDARY")
@NamedQuery(name = "Boundary.findBoundariesByBoundaryType",
        query = "select b from Boundary b where b.boundaryType.id = :boundaryTypeId")
@SequenceGenerator(name = SEQ_BOUNDARY, sequenceName = SEQ_BOUNDARY, allocationSize = 1)
public class Boundary extends AbstractAuditable {

    public static final String SEQ_BOUNDARY = "seq_eg_boundary";
    private static final long serialVersionUID = 3054956514161912026L;
    @Expose
    @Id
    @GeneratedValue(generator = SEQ_BOUNDARY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Length(max = 512)
    @SafeHtml
    @NotBlank
    private String name;

    @Length(max = 25)
    @SafeHtml
    @NotBlank
    private String code;

    private Long boundaryNum;

    @ManyToOne
    @JoinColumn(name = "boundaryType", updatable = false)
    private BoundaryType boundaryType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    @Fetch(value = FetchMode.SELECT)
    private Boundary parent;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent")
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private Set<Boundary> children = new HashSet<>();

    @DateFormat
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fromDate;

    private Date toDate;

    private boolean active;

    private Long bndryId;

    @SafeHtml
    private String localName;

    private Float longitude;

    private Float latitude;

    @Length(max = 32)
    private String materializedPath;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(final String boundaryNameLocal) {
        localName = boundaryNameLocal;
    }

    public Boundary getParent() {
        return parent;
    }

    public void setParent(final Boundary parent) {
        this.parent = parent;
    }

    public Set<Boundary> getChildren() {
        return children;
    }

    public void setChildren(final Set<Boundary> boundaries) {
        children = boundaries;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public boolean isLeaf() {
        return getChildren().isEmpty();
    }

    public BoundaryType getBoundaryType() {
        return boundaryType;
    }

    public void setBoundaryType(final BoundaryType boundaryType) {
        this.boundaryType = boundaryType;
    }

    public Long getBoundaryNum() {
        return boundaryNum;
    }

    public void setBoundaryNum(final Long number) {

        boundaryNum = number;
    }

    public boolean isRoot() {
        return getParent() == null;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public Long getBndryId() {
        return bndryId;
    }

    public void setBndryId(final Long bndryId) {
        this.bndryId = bndryId;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(final Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(final Float latitude) {
        this.latitude = latitude;
    }

    public String getMaterializedPath() {
        return materializedPath;
    }

    public void setMaterializedPath(final String materializedPath) {
        this.materializedPath = materializedPath;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Boundary))
            return false;
        Boundary boundary = (Boundary) other;
        return Objects.equal(boundaryNum, boundary.boundaryNum) &&
                Objects.equal(boundaryType, boundary.boundaryType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(boundaryNum, boundaryType);
    }

}
