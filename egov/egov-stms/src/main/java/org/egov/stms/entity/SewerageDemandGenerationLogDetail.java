/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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

package org.egov.stms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.stms.masters.entity.enums.SewerageProcessStatus;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;

@Entity
@Table(name = "egswtax_demandgenerationlogdetail")
@SequenceGenerator(name = SewerageDemandGenerationLogDetail.SEQ, sequenceName = SewerageDemandGenerationLogDetail.SEQ, allocationSize = 1)
public class SewerageDemandGenerationLogDetail extends AbstractPersistable<Long> {

    public static final String SEQ = "seq_egswtax_demandgenerationlogdetail";
    private static final long serialVersionUID = 3192204759105538672L;

    @Id
    @GeneratedValue(generator = SEQ, strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "applicationdetails", nullable = false)
    private SewerageApplicationDetails sewerageApplicationDetails;

    @Enumerated(EnumType.STRING)
    private SewerageProcessStatus status;

    private String detail;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "demandGenerationLog", nullable = false)
    private SewerageDemandGenerationLog demandGenerationLog;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public SewerageApplicationDetails getSewerageApplicationDetails() {
        return sewerageApplicationDetails;
    }

    public void setSewerageApplicationDetails(SewerageApplicationDetails sewerageApplicationDetails) {
        this.sewerageApplicationDetails = sewerageApplicationDetails;
    }

    public SewerageProcessStatus getStatus() {
        return status;
    }

    public void setStatus(final SewerageProcessStatus status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(final String detail) {
        this.detail = detail;
    }

    public SewerageDemandGenerationLog getDemandGenerationLog() {
        return demandGenerationLog;
    }

    public void setDemandGenerationLog(final SewerageDemandGenerationLog demandGenerationLog) {
        this.demandGenerationLog = demandGenerationLog;
    }

}
