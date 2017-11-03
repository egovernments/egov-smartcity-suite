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
package org.egov.council.entity;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Unique(fields = "agendaNumber", enableDfltMsg = true)
@Table(name = "egcncl_agenda")
@SequenceGenerator(name = CouncilAgenda.SEQ_AGENDA, sequenceName = CouncilAgenda.SEQ_AGENDA, allocationSize = 1)
public class CouncilAgenda extends StateAware<Position> {

    public static final String SEQ_AGENDA = "seq_egcncl_agenda";
    private static final long serialVersionUID = 6941145759682765506L;
    @Id
    @GeneratedValue(generator = SEQ_AGENDA, strategy = GenerationType.SEQUENCE)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "committeeType")
    private CommitteeType committeeType;


    @Column(name = "agendaNumber")
    private String agendaNumber;


    @ManyToOne
    @JoinColumn(name = "status")
    private EgwStatus status;

    @Transient
    private Date fromDate;

    @Transient
    private Date toDate;

    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("order")
    private List<CouncilAgendaDetails> agendaDetails = new ArrayList<>();

    @Transient
    @OrderBy("order")
    private List<CouncilAgendaDetails> councilAgendaDetailsForUpdate = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommitteeType getCommitteeType() {
        return committeeType;
    }

    public void setCommitteeType(CommitteeType committeeType) {
        this.committeeType = committeeType;
    }

    public String getAgendaNumber() {
        return agendaNumber;
    }

    public void setAgendaNumber(String agendaNumber) {
        this.agendaNumber = agendaNumber;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(EgwStatus status) {
        this.status = status;
    }

    public List<CouncilAgendaDetails> getAgendaDetails() {
        return agendaDetails;
    }

    public void setAgendaDetails(List<CouncilAgendaDetails> agendaDetails) {
        this.agendaDetails = agendaDetails;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    @Override
    public String getStateDetails() {
        return String.format("Agenda Number %s ", agendaNumber);

    }

    public List<CouncilAgendaDetails> getCouncilAgendaDetailsForUpdate() {
        return councilAgendaDetailsForUpdate;
    }

    public void setCouncilAgendaDetailsForUpdate(
            List<CouncilAgendaDetails> councilAgendaDetailsForUpdate) {
        this.councilAgendaDetailsForUpdate = councilAgendaDetailsForUpdate;
    }

}
