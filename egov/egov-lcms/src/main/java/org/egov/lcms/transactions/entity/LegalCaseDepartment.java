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
package org.egov.lcms.transactions.entity;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.pims.commons.Position;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "EGLC_LEGALCASE_DEPT")
@SequenceGenerator(name = LegalCaseDepartment.SEQ_EGLC_LEGALCASE_DEPT, sequenceName = LegalCaseDepartment.SEQ_EGLC_LEGALCASE_DEPT, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
    @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class LegalCaseDepartment extends AbstractAuditable {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_LEGALCASE_DEPT = "SEQ_EGLC_LEGALCASE_DEPT";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_LEGALCASE_DEPT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "legalcase", nullable = false)
    @Audited
    private LegalCase legalCase;

    @Temporal(TemporalType.DATE)
    @Column(name = "dateofreceiptofpwr")
    @Audited
    private Date dateofreceiptofpwr;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "DEPARTMENT")
    @NotAudited
    private Department department;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "POSITION")
    @NotAudited
    private Position position;

    @Column(name = "isprimarydepartment")
    @Audited
    private boolean isPrimaryDepartment;

    @OneToMany(mappedBy = "legalCaseDepartment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @NotAudited
    private List<Reminder> legalCaseReminders = new ArrayList<Reminder>(0);

    @Temporal(TemporalType.DATE)
    @Audited
    private Date assignOn;

    public boolean getIsPrimaryDepartment() {
        return isPrimaryDepartment;
    }

    public void setIsPrimaryDepartment(final boolean isPrimaryDepartment) {
        this.isPrimaryDepartment = isPrimaryDepartment;
    }

    public void setPrimaryDepartment(final boolean isPrimaryDepartment) {
        this.isPrimaryDepartment = isPrimaryDepartment;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getLegalCase() != null && !DateUtils.compareDates(getDateofreceiptofpwr(), legalCase.getCaseDate()))
            errors.add(new ValidationError("dateOfReceipt", "dateOfReceipt.less.casedate"));
        if (legalCase != null && !DateUtils.compareDates(getAssignOn(), legalCase.getCaseDate()))
            errors.add(new ValidationError("assignOnDate", "assignOn.less.casedate"));

        return errors;
    }

    public Date getDateofreceiptofpwr() {
        return dateofreceiptofpwr;
    }

    public void setDateofreceiptofpwr(final Date dateofreceiptofpwr) {
        this.dateofreceiptofpwr = dateofreceiptofpwr;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public void addReminder(final Reminder reminder) {
        getLegalCaseReminders().add(reminder);
    }

    public void removeReminder(final Reminder reminder) {
        getLegalCaseReminders().remove(reminder);
    }

    public Date getAssignOn() {
        return assignOn;
    }

    public void setAssignOn(final Date assignOn) {
        this.assignOn = assignOn;
    }

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

    public List<Reminder> getLegalCaseReminders() {
        return legalCaseReminders;
    }

    public void setLegalCaseReminders(final List<Reminder> legalCaseReminders) {
        this.legalCaseReminders = legalCaseReminders;
    }

}
