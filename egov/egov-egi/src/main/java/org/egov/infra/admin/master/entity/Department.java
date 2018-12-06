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

import com.google.gson.annotations.Expose;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
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
import java.util.Objects;

import static org.egov.infra.admin.master.entity.Department.SEQ_DEPARTMENT;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_ALPHANUMERIC_WITH_SPACE;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_MASTER_DATA_CODE;
import static org.egov.infra.validation.constants.ValidationRegex.ALPHANUMERIC_WITH_SPACE;
import static org.egov.infra.validation.constants.ValidationRegex.MASTER_DATA_CODE;

@Entity
@Unique(fields = {"name", "code"}, enableDfltMsg = true)
@Table(name = "eg_department")
@SequenceGenerator(name = SEQ_DEPARTMENT, sequenceName = SEQ_DEPARTMENT, allocationSize = 1)
public class Department extends AbstractAuditable {

    protected static final String SEQ_DEPARTMENT = "SEQ_EG_DEPARTMENT";
    private static final long serialVersionUID = 7630238192598939863L;

    @Expose
    @Id
    @GeneratedValue(generator = SEQ_DEPARTMENT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Length(max = 64)
    @SafeHtml
    @Pattern(regexp = ALPHANUMERIC_WITH_SPACE, message = INVALID_ALPHANUMERIC_WITH_SPACE)
    private String name;

    @NotBlank
    @Length(max = 64)
    @SafeHtml
    @Column(updatable = false)
    @Pattern(regexp = MASTER_DATA_CODE, message = INVALID_MASTER_DATA_CODE)
    private String code;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Department))
            return false;
        Department that = (Department) other;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getCode(), that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCode());
    }
}
