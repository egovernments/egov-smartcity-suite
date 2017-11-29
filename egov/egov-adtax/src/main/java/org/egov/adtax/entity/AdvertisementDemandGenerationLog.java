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

package org.egov.adtax.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.adtax.entity.enums.ProcessStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;

@Entity
@Table(name = "egadtax_demandgenerationlog")
@SequenceGenerator(name = AdvertisementDemandGenerationLog.SEQ, sequenceName = AdvertisementDemandGenerationLog.SEQ, allocationSize = 1)
@Unique(fields = { "installmentYear" }, enableDfltMsg = true)
public class AdvertisementDemandGenerationLog extends AbstractAuditable {

    private static final long serialVersionUID = -2733572195063280873L;

    public static final String SEQ = "seq_egadtax_demandgenerationlog";

    @Id
    @GeneratedValue(generator = SEQ, strategy = GenerationType.SEQUENCE)
    private Long id;

    private String installmentYear;

    @Enumerated(EnumType.STRING)
    private ProcessStatus executionStatus;

    @Enumerated(EnumType.STRING)
    private ProcessStatus demandGenerationStatus;

    @OneToMany(mappedBy = "demandGenerationLog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AdvertisementDemandGenerationLogDetail> details = new ArrayList<>();

    public AdvertisementDemandGenerationLog() {
        // default constructor
    }

    public AdvertisementDemandGenerationLog(final String installmentYear) {
        this.installmentYear = installmentYear;
        executionStatus = ProcessStatus.COMPLETED;
        demandGenerationStatus = ProcessStatus.COMPLETED;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getInstallmentYear() {
        return installmentYear;
    }

    public void setInstallmentYear(final String installmentYear) {
        this.installmentYear = installmentYear;
    }

    public ProcessStatus getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(final ProcessStatus executionStatus) {
        this.executionStatus = executionStatus;
    }

    public ProcessStatus getDemandGenerationStatus() {
        return demandGenerationStatus;
    }

    public void setDemandGenerationStatus(final ProcessStatus demandGenerationStatus) {
        this.demandGenerationStatus = demandGenerationStatus;
    }

    public List<AdvertisementDemandGenerationLogDetail> getDetails() {

        return details;
    }

    public void setDetails(final List<AdvertisementDemandGenerationLogDetail> details) {
        this.details = details;
    }

    public void addDetails(final AdvertisementDemandGenerationLogDetail logDetail) {
        getDetails().add(logDetail);

    }

}
