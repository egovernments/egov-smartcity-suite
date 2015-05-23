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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.search.domain.Searchable;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Unique(id = "id", tableName = "eg_boundary", fields = { "name" }, columnName = { "name" },enableDfltMsg=true)
@Table(name = "EG_BOUNDARY")
@Searchable
@NamedQuery(name = "Boundary.findBoundariesByBoundaryType", query = "select b from Boundary b where b.boundaryType.id = :boundaryTypeId")
public class Boundary extends AbstractAuditable<User, Long> {

	private static final long serialVersionUID = -224771387323975327L;

	@Length(max = 512)
	@Searchable(name = "name")
	private String name;

	private Long boundaryNum;

	@Valid
	@ManyToOne
	@JoinColumn(name = "boundaryType", updatable = false)
	private BoundaryType boundaryType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent")
	@Fetch(value = FetchMode.SELECT)
	private Boundary parent;

	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "parent")
	@Fetch(value = FetchMode.SELECT)
	private Set<Boundary> children = new HashSet<Boundary>();

	@DateFormat
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date fromDate;

	private Date toDate;

	private boolean isHistory;
	private Long bndryId;
	private String localName;

	private Float longitude;
	private Float latitude;

	@Length(max = 32)
	private String materializedPath;

	/*
	 * @Transient
	 * 
	 * @Searchable(name = "detail") private JSONObject boundaryJson;
	 */

	@Transient
	private Long parentBoundaryNum;

	@Transient
	private Long hierarchyTypeId;

	@Transient
	private Long boundaryTypeId;

	@Valid
	@Transient
	private CityWebsite cityWebsite = new CityWebsite();

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

	public String getName() {
		return name;
	}

	public void setName(final String name) {

		this.name = name;
	}

	public boolean isLeaf() {
		return getChildren().isEmpty();
	}

	public BoundaryType getBoundaryType() {
		return boundaryType;
	}

	public Long getBoundaryNum() {
		return boundaryNum;
	}

	public void addChild(final Boundary boundary) {
		boundary.setParent(this);
		children.add(boundary);
	}

	public void deleteChild(final Boundary boundary) {
		children.remove(boundary);
	}

	public void addChildren(final Set<Boundary> boundaries) {
		children.addAll(boundaries);

	}

	public void deleteChildren(final Set<Boundary> boundaries) {
		children.removeAll(boundaries);
	}

	public boolean isRoot() {
		return getParent() == null ? true : false;
	}

	public void setBoundaryType(final BoundaryType boundaryType) {
		this.boundaryType = boundaryType;
	}

	public void setBoundaryNum(final Long number) {

		boundaryNum = number;
	}

	public void setChildren(final Set<Boundary> boundaries) {
		children = boundaries;

	}

	public Long getParentBoundaryNum() {
		return parentBoundaryNum;
	}

	public void setParentBoundaryNum(final Long parentBoundaryNum) {
		this.parentBoundaryNum = parentBoundaryNum;
	}

	public Long getHierarchyTypeId() {
		return hierarchyTypeId;
	}

	public void setHierarchyTypeId(final Long hierarchyTypeId) {
		this.hierarchyTypeId = hierarchyTypeId;
	}

	public Long getBoundaryTypeId() {
		return boundaryTypeId;
	}

	public void setBoundaryTypeId(final Long boundaryTypeId) {
		this.boundaryTypeId = boundaryTypeId;
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

	public boolean isHistory() {
		return isHistory;
	}

	public void setHistory(final boolean isHistory) {
		this.isHistory = isHistory;
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

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Boundary))
			return false;
		final Boundary other = (Boundary) obj;

		if (getId() != null && getId().equals(other.getId()))
			return true;
		// Boundary number can be null
		if (getBoundaryNum() != null
				&& !getBoundaryNum().equals(other.getBoundaryNum()))
			return false;
		if (!getBoundaryType().equals(other.getBoundaryType()))
			return false;
		if (getName() != null && !getName().equals(other.getName()))
			return false;
		if (getLocalName() != null
				&& !getLocalName().equals(other.getLocalName()))
			return false;
		if (getParent() != null && !getParent().equals(other.getParent()))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int hashCode = 0;

		// assumes boundary number is never null.
		if (getBoundaryNum() != null)
			hashCode = hashCode + getBoundaryNum().hashCode();

		// assumes boundary name is never null.
		if (getName() != null)
			hashCode = hashCode + getName().hashCode();

		// assumes boundary type name is never null.
		if (getBoundaryType() != null && getBoundaryType().getName() != null)
			hashCode = hashCode + getBoundaryType().getName().hashCode();

		// assumes top boundary id is never null.
		final Long i = getTopLevelBndryId(this);
		if (i != null)
			hashCode = hashCode + i.hashCode();

		return hashCode;

	}

	private Long getTopLevelBndryId(final Boundary bn) {
		Boundary localBndry = bn;
		if (localBndry == null)
			return null;

		while (localBndry.getParent() != null)
			localBndry = localBndry.getParent();

		return localBndry.getId();
	}

	public String getMaterializedPath() {
		return materializedPath;
	}

	public void setMaterializedPath(final String materializedPath) {
		this.materializedPath = materializedPath;
	}

	public CityWebsite getCityWebsite() {
		return cityWebsite;
	}

	public void setCityWebsite(final CityWebsite cityWebsite) {
		this.cityWebsite = cityWebsite;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("Boundary [id=").append(getId())
				.append(", boundaryType=").append(boundaryType)
				.append(", name: ").append(name).append(", number=")
				.append(boundaryNum).append(", parentBoundaryNum=")
				.append(parentBoundaryNum).append(", isHistory=")
				.append(isHistory).append(", materializedPath: ")
				.append(materializedPath).append("]").toString();
	}

	/*
	 * public JSONObject getBoundaryJson() { final Map<String, Object> map = new
	 * HashMap<>(); addNameAndValue(map); return new JSONObject(map); }
	 * 
	 * public void addNameAndValue(final Map<String, Object> map) {
	 * map.put(getBoundaryType().getName(), getName()); if (getParent() != null)
	 * getParent().addNameAndValue(map); }
	 */
}
