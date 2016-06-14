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
package org.egov.lcms.masters.entity;

import javax.validation.constraints.Max;

import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

@Unique(fields = { "judgmentType", "code" }, id = "id", tableName = "EGLC_JUDGMENTTYPE_MASTER", columnName = {
		"JUDGMENTTYPE", "CODE" }, message = "masters.judgmentType.isunique")
public class JudgmentType extends BaseModel {
	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	@Required(message = "masters.code.null")
	@Length(max = 8, message = "masters.code.length")
	@OptionalPattern(regex = "[0-9A-Za-z-]*", message = "masters.code.alpha2")
	private String code;
	private Boolean active;

	public Boolean getActive() {
		return active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	@Required(message = "masters.judgmentType.null")
	@Length(max = 32, message = "masters.judgmentType.length")
	@OptionalPattern(regex = LcmsConstants.mixedChar, message = "masters.judgmentType.mixedChar")
	private String judgmentType;

	@Length(max = 128, message = "masters.description.length")
	private String description;
	@Max(value = 1000, message = "masters.orderNumber.length")
	private Long orderNumber;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * @return the judgmentType
	 */
	public String getJudgmentType() {
		return judgmentType;
	}

	/**
	 * @param judgmentType
	 *            the judgmentType to set
	 */
	public void setJudgmentType(final String judgmentType) {
		this.judgmentType = judgmentType;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the order_Number
	 */
	public Long getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param order_Number
	 *            the order_Number to set
	 */
	public void setOrderNumber(final Long orderNumber) {
		this.orderNumber = orderNumber;
	}
}
