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
package org.egov.collection.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractAuditable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Comparator;
import java.util.Objects;

@Entity
@Table(name = "EGCL_APPROVER_REMITTER")
@SequenceGenerator(name = ApproverRemitterMapping.SEQ_APPROVER_REMITTER, sequenceName = ApproverRemitterMapping.SEQ_APPROVER_REMITTER, allocationSize = 1)
@JsonIgnoreProperties({ "createdBy", "lastModifiedBy" })
public class ApproverRemitterMapping extends AbstractAuditable implements Comparator {
    public static final String SEQ_APPROVER_REMITTER = "SEQ_EGCL_APPROVER_REMITTER";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = SEQ_APPROVER_REMITTER, strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "isactive")
    @NotNull
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "approver")
    @NotNull
    private User approver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "remitter")
    @NotNull
    private User remitter;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(Long id) {
        this.id = id;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }

    public User getRemitter() {
        return remitter;
    }

    public void setRemitter(User remitter) {
        this.remitter = remitter;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @Deprecated As "Order is not defined for Mapping"
     * @return -1 (less-than)
     */
    @Deprecated
    @Override
    public int compare(Object o1, Object o2) {
        return -1;
    }

    /**
     * This equality check considers the following 1. Both are of same type. i.e. {@link ApproverRemitterMapping} 2. id of both
     * mapping are same 3. approver & remitter are same 4. approver are identical and both are active mapping
     * @param o the other object
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        boolean thisEffectivelyNull = getApprover() == null && getRemitter() == null;
        if (o == null && thisEffectivelyNull)
            return true;
        else if (thisEffectivelyNull)
            return false;
        if (!(o instanceof ApproverRemitterMapping))
            return false;

        ApproverRemitterMapping mapping = (ApproverRemitterMapping) o;
        return Objects.equals(getId(), mapping.getId()) ||
                (Objects.equals(getApprover(), mapping.getApprover())
                        && Objects.equals(getRemitter(), mapping.getRemitter()))
                || (Objects.equals(getApprover(), mapping.getApprover()) && isActive
                        && Objects.equals(getIsActive(), mapping.getIsActive()));

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}