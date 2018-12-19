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

package org.egov.tl.entity;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.hibernate.annotations.Immutable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_ALPHABETS_WITH_SPACE;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_MASTER_DATA_CODE;
import static org.egov.infra.validation.constants.ValidationRegex.ALPHABETS_WITH_SPACE;
import static org.egov.infra.validation.constants.ValidationRegex.MASTER_DATA_CODE;

@Entity
@Table(name = "EGTL_MSTR_STATUS")
@SequenceGenerator(name = LicenseStatus.SEQ_STATUS, sequenceName = LicenseStatus.SEQ_STATUS, allocationSize = 1)
@Immutable
public class LicenseStatus extends AbstractPersistable<Long> {

    public static final String SEQ_STATUS = "SEQ_EGTL_MSTR_STATUS";
    private static final long serialVersionUID = 22395010799520683L;

    @Id
    @GeneratedValue(generator = SEQ_STATUS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Length(max = 256)
    @SafeHtml
    @Column(name = "STATUS_NAME")
    @Pattern(regexp = ALPHABETS_WITH_SPACE, message = INVALID_ALPHABETS_WITH_SPACE)
    private String name;

    @NotBlank
    @Length(max = 32)
    @SafeHtml
    @Column(name = "CODE")
    @Pattern(regexp = MASTER_DATA_CODE, message = INVALID_MASTER_DATA_CODE)
    private String statusCode;

    @Column(name = "IS_ACTIVE")
    private boolean active;

    @Column(name = "ORDER_ID")
    @Positive
    private Integer orderId;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(final Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof LicenseStatus))
            return false;
        LicenseStatus that = (LicenseStatus) other;
        return getName().equals(that.getName());

    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
