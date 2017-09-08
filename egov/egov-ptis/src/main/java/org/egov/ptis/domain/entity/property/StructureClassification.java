/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.ptis.domain.entity.property;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.commons.Installment;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.validation.exception.ValidationError;
import org.hibernate.envers.Audited;

/**
 * <p>
 * Each deployment groups number of CostructionTypeSets and gives a common name.
 * a For example: A particular ConstructionTypeSet might be composed of the
 * following ConstructionType objects: TeakWood, MarbleFloor, RCCRoof. A name is
 * given to the StructuralClassification. For example the name given to the
 * above grouping can be Pucca. RoseWood, GraniteFloor and RCCRoof might also
 * share the same name. A factor is normally associated with the Structural
 * Classification which is used to calculate the demand of the Property.
 * </p>
 *
 * @author Gayathri Joshi
 * @version 2.00
 * @see org.egov.ptis.domain.entity.property.ConstructionType
 * @since 2.00
 * @author srikanth Change Log : Added additional field
 *         constrTypeCode,orderId,fromDate,todate
 */
@Entity
@Table(name = "EGPT_STRUC_CL")
@Unique(columnName = { "CODE", "CONSTR_TYPE"}, fields = { "constrTypeCode", "typeName" }, enableDfltMsg = true)
@SequenceGenerator(name = StructureClassification.SEQ_STRUCTURE_CLASSIFICATION, sequenceName = StructureClassification.SEQ_STRUCTURE_CLASSIFICATION, allocationSize = 1)
public class StructureClassification extends AbstractAuditable {

	private static final long serialVersionUID = 1L;
	public static final String SEQ_STRUCTURE_CLASSIFICATION = "SEQ_EGPT_STRUC_CL";

	@Id
	@GeneratedValue(generator = SEQ_STRUCTURE_CLASSIFICATION, strategy = GenerationType.SEQUENCE)
	private Long id;

	@Audited
	@Column(name = "CONSTR_TYPE")
	private String typeName;

	@Audited
	@Column(name = "CONSTR_DESCR")
	private String description;

	@Audited
	@Column(name = "CODE")
	private String constrTypeCode;

	@Column(name = "ORDER_ID")
	private Integer orderId;

	@Column(name = "FLOOR_NUM")
	private Integer floorNum;

	@Column(name = "CONSTR_NUM")
	private Integer number;

	@Column(name = "CONSTR_FACTOR")
	private Float factor;

	@ManyToOne
	@JoinColumn(name = "ID_INSTALLMENT")
	private Installment startInstallment;

	@Column(name = "IS_HISTORY")
	private char isHistory;

	@Column(name = "FROM_DATE")
	private Date fromDate;

	@Column(name = "TO_DATE")
	private Date toDate;
	
	@Audited
	@Column(name = "ISACTIVE")
	private Boolean isActive;

	/**
	 * @return Returns if the given Object is equal to PropertyStatus
	 */
	@Override
	public boolean equals(final Object that) {

		if (that == null)
			return false;

		if (this == that)
			return true;

		if (that.getClass() != this.getClass())
			return false;
		final StructureClassification thatStrCls = (StructureClassification) that;

		if (getId() != null && thatStrCls.getId() != null) {
			if (getId().equals(thatStrCls.getId()))
				return true;
			else
				return false;
		} else if (getTypeName() != null && thatStrCls.getTypeName() != null) {
			if (getTypeName().equals(thatStrCls.getTypeName()))
				return true;
			else
				return false;
		} else if (getNumber() != null && thatStrCls.getNumber() != null) {
			if (getNumber().equals(thatStrCls.getNumber()))
				return true;
			else
				return false;
		} else
			return false;
	}

	/**
	 * @return Returns the hashCode
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		if (getId() != null)
			hashCode += getId().hashCode();

		if (getTypeName() != null)
			hashCode += getTypeName().hashCode();

		if (getNumber() != null)
			hashCode += getNumber().hashCode();
		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public List<ValidationError> validate() {
		final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if (getTypeName() == null) {
			final ValidationError ve = new ValidationError("StrucClass.TypeName.Null",
					"In StructureClassification Attribute 'Type Name' is Not Set, Please Check !!");
			validationErrors.add(ve);
		}
		if (getNumber() == null || getNumber() == 0) {
			final ValidationError ve = new ValidationError("StrucClass.Number.Null",
					"In StructureClassification Attribute 'Number' is Not Set OR is Zero, Please Check !!");
			validationErrors.add(ve);
		}

		return validationErrors;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("").append(id).append("|").append(constrTypeCode).append("|").append(factor);
		return sb.toString();
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(final String typeName) {
		this.typeName = typeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getConstrTypeCode() {
		return constrTypeCode;
	}

	public void setConstrTypeCode(final String constrTypeCode) {
		this.constrTypeCode = constrTypeCode.toUpperCase();
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(final Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getFloorNum() {
		return floorNum;
	}

	public void setFloorNum(final Integer floorNum) {
		this.floorNum = floorNum;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(final Integer number) {
		this.number = number;
	}

	public Float getFactor() {
		return factor;
	}

	public void setFactor(final Float factor) {
		this.factor = factor;
	}

	public Installment getStartInstallment() {
		return startInstallment;
	}

	public void setStartInstallment(final Installment startInstallment) {
		this.startInstallment = startInstallment;
	}

	public char getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(final char isHistory) {
		this.isHistory = isHistory;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(final Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

}
