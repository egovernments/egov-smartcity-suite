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

package org.egov.pgr.entity;

import org.egov.infra.admin.master.entity.Boundary;
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

@Entity
@Table(name = "egpgr_router")
@SequenceGenerator(name = ComplaintRouter.SEQ_COMPLAINTROUTER, sequenceName = ComplaintRouter.SEQ_COMPLAINTROUTER, allocationSize = 1)
public class ComplaintRouter extends AbstractAuditable {

    public static final String SEQ_COMPLAINTROUTER = "SEQ_EGPGR_ROUTER";
    private static final long serialVersionUID = 5691022600220045218L;
    @Id
    @GeneratedValue(generator = SEQ_COMPLAINTROUTER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "complainttypeid")
    private ComplaintType complaintType;

    @ManyToOne
    @JoinColumn(name = "bndryid")
    private Boundary boundary;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "position")
    private Position position;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public ComplaintType getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(final ComplaintType complaintType) {
        this.complaintType = complaintType;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(final Boundary boundary) {
        this.boundary = boundary;
    }
}