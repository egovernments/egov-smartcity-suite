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

package org.egov.ptis.domain.entity.property.view;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "EGPT_VIEW_CURRENT_FLOOR_DETAIL")
public class FloorDetailsInfo implements Serializable {

	private static final long serialVersionUID = -2878836256360657101L;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "basicPropertyId")
	private PropertyMVInfo propMatView;

	private Integer propertyId;

	@Id
	private Integer floorId;

	@Column(name = "NATUREOFUSAGE")
	private String propertyUsage;

	@Column(name = "PROPERTYTYPE")
	private Integer propertyTypeMaster;

	private String floorNo;

	private BigDecimal plinthArea;

	private BigDecimal builtUpArea;

	private String classification;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "occupation")
	private PropertyOccupation propertyOccupation;

	public PropertyMVInfo getPropMatView() {
		return propMatView;
	}

	public void setPropMatView(final PropertyMVInfo propMatView) {
		this.propMatView = propMatView;
	}

	public Integer getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(final Integer propertyId) {
		this.propertyId = propertyId;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(final Integer floorId) {
		this.floorId = floorId;
	}

	public String getPropertyUsage() {
		return propertyUsage;
	}

	public void setPropertyUsage(final String propertyUsage) {
		this.propertyUsage = propertyUsage;
	}

	public Integer getPropertyTypeMaster() {
		return propertyTypeMaster;
	}

	public void setPropertyTypeMaster(final Integer propertyTypeMaster) {
		this.propertyTypeMaster = propertyTypeMaster;
	}

	public String getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(final String floorNo) {
		this.floorNo = floorNo;
	}

	public BigDecimal getPlinthArea() {
		return plinthArea;
	}

	public void setPlinthArea(final BigDecimal plinthArea) {
		this.plinthArea = plinthArea;
	}

	public BigDecimal getBuiltUpArea() {
		return builtUpArea;
	}

	public void setBuiltUpArea(final BigDecimal builtUpArea) {
		this.builtUpArea = builtUpArea;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(final String classification) {
		this.classification = classification;
	}

	public PropertyOccupation getPropertyOccupation() {
		return propertyOccupation;
	}

	public void setPropertyOccupation(final PropertyOccupation propertyOccupation) {
		this.propertyOccupation = propertyOccupation;
	}

}
