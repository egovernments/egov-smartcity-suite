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

package org.egov.tl.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.tl.entity.enums.ProcessStatus;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "egtl_demandgenerationlog")
@SequenceGenerator(name = DemandGenerationLog.SEQ, sequenceName = DemandGenerationLog.SEQ, allocationSize = 1)
@Unique(fields = {"installmentYear"}, enableDfltMsg = true)
public class DemandGenerationLog extends AbstractAuditable {

    public static final String SEQ = "seq_egtl_demandgenerationlog";
    private static final long serialVersionUID = 3323170307345697375L;

    @Id
    @GeneratedValue(generator = SEQ, strategy = GenerationType.SEQUENCE)
    private Long id;

    private String installmentYear;

    @Enumerated(EnumType.STRING)
    private ProcessStatus executionStatus;

    @Enumerated(EnumType.STRING)
    private ProcessStatus demandGenerationStatus;

    @OneToMany(mappedBy = "demandGenerationLog", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<DemandGenerationLogDetail> details = new ArrayList<>();

    protected DemandGenerationLog() {
        //for hibernate
    }

    public DemandGenerationLog(String installmentYear) {
        this.installmentYear = installmentYear;
        this.executionStatus = ProcessStatus.INPROGRESS;
        this.demandGenerationStatus = ProcessStatus.INCOMPLETE;
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

    public List<DemandGenerationLogDetail> getDetails() {
        return details;
    }

    public void setDetails(final List<DemandGenerationLogDetail> details) {
        this.details = details;
    }

}