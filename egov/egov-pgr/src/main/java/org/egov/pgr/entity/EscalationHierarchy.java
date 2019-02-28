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
package org.egov.pgr.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.pims.commons.Position;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.EAGER;
import static org.egov.pgr.entity.EscalationHierarchy.SEQ_ESCALATIONHIERARCHY;

@Entity
@Table(name = "egpgr_escalationhierarchy")
@SequenceGenerator(name = SEQ_ESCALATIONHIERARCHY, sequenceName = SEQ_ESCALATIONHIERARCHY, allocationSize = 1)
public class EscalationHierarchy extends AbstractAuditable {

    protected static final String SEQ_ESCALATIONHIERARCHY = "SEQ_EGPGR_ESCALATIONHIERARCHY";
    private static final long serialVersionUID = -6097906596110329202L;
    @Id
    @GeneratedValue(generator = SEQ_ESCALATIONHIERARCHY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = EAGER, optional = false)
    @JoinColumn(name = "fromposition", updatable = false)
    private Position fromPosition;

    @NotNull
    @ManyToOne(fetch = EAGER, optional = false)
    @JoinColumn(name = "toposition")
    private Position toPosition;

    @NotNull
    @ManyToOne(fetch = EAGER, optional = false)
    @JoinColumn(name = "grievancetype")
    private ComplaintType grievanceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Position getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(Position fromPosition) {
        this.fromPosition = fromPosition;
    }

    public Position getToPosition() {
        return toPosition;
    }

    public void setToPosition(Position toPosition) {
        this.toPosition = toPosition;
    }

    public ComplaintType getGrievanceType() {
        return grievanceType;
    }

    public void setGrievanceType(ComplaintType grievanceType) {
        this.grievanceType = grievanceType;
    }
}
