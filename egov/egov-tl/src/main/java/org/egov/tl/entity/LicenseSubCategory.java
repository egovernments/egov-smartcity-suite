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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_MASTER_DATA_CODE;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_NAME_WITH_EXTRA_SPECIAL_CHARS;
import static org.egov.infra.validation.constants.ValidationRegex.MASTER_DATA_CODE;
import static org.egov.infra.validation.constants.ValidationRegex.NAME_WITH_EXTRA_SPECIAL_CHARS;
import static org.egov.tl.entity.LicenseSubCategory.SEQ_SUBCATEGORY;

@Entity
@Table(name = "EGTL_MSTR_SUB_CATEGORY")
@SequenceGenerator(name = SEQ_SUBCATEGORY, sequenceName = SEQ_SUBCATEGORY, allocationSize = 1)
@Unique(fields = {"code", "name"}, enableDfltMsg = true)
public class LicenseSubCategory extends AbstractAuditable {

    protected static final String SEQ_SUBCATEGORY = "SEQ_EGTL_MSTR_SUB_CATEGORY";
    private static final long serialVersionUID = 4137779539190266766L;

    @Id
    @GeneratedValue(generator = SEQ_SUBCATEGORY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Length(max = 5)
    @SafeHtml
    @Column(updatable = false)
    @Pattern(regexp = MASTER_DATA_CODE, message = INVALID_MASTER_DATA_CODE)
    private String code;

    @NotBlank
    @Length(max = 150)
    @SafeHtml
    @Pattern(regexp = NAME_WITH_EXTRA_SPECIAL_CHARS, message = INVALID_NAME_WITH_EXTRA_SPECIAL_CHARS)
    private String name;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "ID_CATEGORY", updatable = false)
    private LicenseCategory category;

    @OneToMany(mappedBy = "subCategory", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @Valid
    private List<LicenseSubCategoryDetails> licenseSubCategoryDetails = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public LicenseCategory getCategory() {
        return category;
    }

    public void setCategory(LicenseCategory category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LicenseSubCategoryDetails> getLicenseSubCategoryDetails() {
        return licenseSubCategoryDetails;
    }

    public void setLicenseSubCategoryDetails(List<LicenseSubCategoryDetails> licenseSubCategoryDetails) {
        this.licenseSubCategoryDetails = licenseSubCategoryDetails;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof LicenseSubCategory))
            return false;
        LicenseSubCategory that = (LicenseSubCategory) obj;
        return Objects.equals(getCode(), that.getCode()) &&
                Objects.equals(getCategory(), that.getCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getCategory());
    }
}
