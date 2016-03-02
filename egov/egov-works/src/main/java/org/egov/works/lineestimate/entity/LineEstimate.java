/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.lineestimate.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.model.budget.BudgetGroup;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "EGW_LINEESTIMATE")
@Unique(id = "id", tableName = "EGW_LINEESTIMATE", columnName = { "lineestimatenumber" }, fields = {
        "lineEstimateNumber" }, enableDfltMsg = true)
@SequenceGenerator(name = LineEstimate.SEQ_EGW_LINEESTIMATE, sequenceName = LineEstimate.SEQ_EGW_LINEESTIMATE, allocationSize = 1)
public class LineEstimate extends StateAware {

    private static final long serialVersionUID = -366602348464540736L;

    public static final String SEQ_EGW_LINEESTIMATE = "SEQ_EGW_LINEESTIMATE";

    @Id
    @GeneratedValue(generator = SEQ_EGW_LINEESTIMATE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @SafeHtml
    @Length(max = 50)
    @Column(unique = true)
    private String lineEstimateNumber;

    @NotNull
    @SafeHtml
    @Length(max = 256)
    private String subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund", nullable = false)
    private Fund fund;

    @NotNull
    @SafeHtml
    @Length(max = 1024)
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function", nullable = false)
    private CFunction function;

    @NotNull
    @SafeHtml
    @Length(max = 1024)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budgethead", nullable = false)
    private BudgetGroup budgetHead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme")
    private Scheme scheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscheme")
    private SubScheme subScheme;

    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date lineEstimateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executingdepartment", nullable = false)
    private Department executingDepartment;

    @OneToMany(mappedBy = "lineEstimate", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH }, orphanRemoval = true, targetEntity = LineEstimateDetails.class)
    private final List<LineEstimateDetails> lineEstimateDetails = new ArrayList<LineEstimateDetails>(0);

    @OneToMany(mappedBy = "objectId", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, targetEntity = DocumentDetails.class)
    private final List<DocumentDetails> documentDetails = new ArrayList<DocumentDetails>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private EgwStatus status;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getLineEstimateNumber() {
        return lineEstimateNumber;
    }

    public void setLineEstimateNumber(final String lineEstimateNumber) {
        this.lineEstimateNumber = lineEstimateNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BudgetGroup getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(final BudgetGroup budgetHead) {
        this.budgetHead = budgetHead;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(final Scheme scheme) {
        this.scheme = scheme;
    }

    public SubScheme getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(final SubScheme subScheme) {
        this.subScheme = subScheme;
    }

    public Date getLineEstimateDate() {
        return lineEstimateDate;
    }

    public void setLineEstimateDate(final Date lineEstimateDate) {
        this.lineEstimateDate = lineEstimateDate;
    }

    public Department getExecutingDepartment() {
        return executingDepartment;
    }

    public void setExecutingDepartment(final Department executingDepartment) {
        this.executingDepartment = executingDepartment;
    }

    public List<LineEstimateDetails> getLineEstimateDetails() {
        return lineEstimateDetails;
    }

    public void setLineEstimateDetails(final List<LineEstimateDetails> lineEstimateDetails) {
        this.lineEstimateDetails.clear();
        if (lineEstimateDetails != null)
            this.lineEstimateDetails.addAll(lineEstimateDetails);
    }

    public List<DocumentDetails> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(final List<DocumentDetails> documentDetails) {
        this.documentDetails.clear();
        if (documentDetails != null)
            this.documentDetails.addAll(documentDetails);
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    @Override
    public String getStateDetails() {
        return null;
    }
}