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

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.persistence.entity.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "egpgr_complaintstatus_mapping")
@SequenceGenerator(name = ComplaintStatusMapping.SEQ_STATUSMAP, sequenceName = ComplaintStatusMapping.SEQ_STATUSMAP, allocationSize = 1)
public class ComplaintStatusMapping extends AbstractPersistable<Long> {

    public static final String SEQ_STATUSMAP = "SEQ_EGPGR_COMPLAINTSTATUS_MAPPING";
    private static final long serialVersionUID = -1671713502661376820L;
    @Id
    @GeneratedValue(generator = SEQ_STATUSMAP, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_status_id")
    private ComplaintStatus currentStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_status_id")
    private ComplaintStatus showStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Valid
    @JoinColumn(name = "role_id")
    private Role role;

    @NotNull
    private Integer orderNo;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    public ComplaintStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(final ComplaintStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public ComplaintStatus getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(final ComplaintStatus showStatus) {
        this.showStatus = showStatus;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(final Integer orderNo) {
        this.orderNo = orderNo;
    }

}
