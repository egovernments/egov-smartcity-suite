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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.tl.entity.enums.ProcessStatus;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static org.egov.tl.entity.DemandGenerationLogDetail.SEQ_DEMANDGENERATIONLOGDETAIL;

@Entity
@Table(name = "egtl_demandgenerationlogdetail")
@SequenceGenerator(name = SEQ_DEMANDGENERATIONLOGDETAIL, sequenceName = SEQ_DEMANDGENERATIONLOGDETAIL, allocationSize = 1)
@JsonIgnoreProperties({"createdBy", "lastModifiedBy"})
public class DemandGenerationLogDetail extends AbstractPersistable<Long> {

    protected static final String SEQ_DEMANDGENERATIONLOGDETAIL = "seq_egtl_demandgenerationlogdetail";
    private static final long serialVersionUID = 3192204759105538672L;

    @Id
    @GeneratedValue(generator = SEQ_DEMANDGENERATIONLOGDETAIL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @SafeHtml
    @NotBlank
    @Length(max = 50)
    private String licenseNumber;

    @NotNull
    private Long licenseId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProcessStatus status;

    @SafeHtml
    @NotBlank
    @Length(max = 1000)
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demandGenerationLog", nullable = false)
    @JsonIgnore
    private DemandGenerationLog demandGenerationLog;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(final String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Long getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(final Long licenseId) {
        this.licenseId = licenseId;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(final ProcessStatus status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(final String detail) {
        this.detail = detail;
    }

    public DemandGenerationLog getDemandGenerationLog() {
        return demandGenerationLog;
    }

    public void setDemandGenerationLog(final DemandGenerationLog demandGenerationLog) {
        this.demandGenerationLog = demandGenerationLog;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof DemandGenerationLogDetail))
            return false;
        DemandGenerationLogDetail that = (DemandGenerationLogDetail) other;
        return Objects.equals(getLicenseId(), that.getLicenseId()) &&
                Objects.equals(getDemandGenerationLog(), that.getDemandGenerationLog());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLicenseId(), getDemandGenerationLog());
    }
}
