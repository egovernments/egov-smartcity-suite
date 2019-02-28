/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_MASTER_DATA_CODE;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_NAME_WITH_EXTRA_SPECIAL_CHARS;
import static org.egov.infra.validation.constants.ValidationRegex.MASTER_DATA_CODE;
import static org.egov.infra.validation.constants.ValidationRegex.NAME_WITH_EXTRA_SPECIAL_CHARS;
import static org.egov.tl.entity.LicenseCategory.SEQ_CATEGORY;

@Entity
@Table(name = "EGTL_MSTR_CATEGORY")
@Unique(fields = {"name", "code"}, enableDfltMsg = true)
@SequenceGenerator(name = SEQ_CATEGORY, sequenceName = SEQ_CATEGORY, allocationSize = 1)
public class LicenseCategory extends AbstractAuditable {
    protected static final String SEQ_CATEGORY = "SEQ_EGTL_MSTR_CATEGORY";
    private static final long serialVersionUID = 2997222319085575846L;

    @Id
    @GeneratedValue(generator = SEQ_CATEGORY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Length(max = 150)
    @SafeHtml
    @Pattern(regexp = NAME_WITH_EXTRA_SPECIAL_CHARS, message = INVALID_NAME_WITH_EXTRA_SPECIAL_CHARS)
    private String name;

    @NotBlank
    @Length(max = 5)
    @SafeHtml
    @Pattern(regexp = MASTER_DATA_CODE, message = INVALID_MASTER_DATA_CODE)
    @Column(updatable = false)
    private String code;

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
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof LicenseCategory))
            return false;
        LicenseCategory that = (LicenseCategory) obj;
        return Objects.equals(getCode(), that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
