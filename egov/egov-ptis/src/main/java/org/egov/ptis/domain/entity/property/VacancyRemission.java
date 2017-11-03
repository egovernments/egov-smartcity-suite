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
package org.egov.ptis.domain.entity.property;

import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "egpt_vacancy_remission")
@SequenceGenerator(name = VacancyRemission.SEQ_VACANCYREMISSION, sequenceName = VacancyRemission.SEQ_VACANCYREMISSION, allocationSize = 1)
public class VacancyRemission extends StateAware<Position> {

    public static final String SEQ_VACANCYREMISSION = "SEQ_EGPT_VACANCY_REMISSION";
    private static final long serialVersionUID = 3387659460257524470L;
    @Id
    @GeneratedValue(generator = SEQ_VACANCYREMISSION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "basicproperty", nullable = false)
    private BasicPropertyImpl basicProperty;

    @NotNull
    @Column(name = "vacancy_fromdate")
    private Date vacancyFromDate;

    @NotNull
    @Column(name = "vacancy_todate")
    private Date vacancyToDate;

    @NotNull
    @Column(name = "vacancy_comments")
    private String vacancyComments;

    @OrderBy("id")
    @OneToMany(mappedBy = "vacancyRemission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VacancyRemissionDetails> vacancyRemissionDetails = new LinkedList<>();

    @Column(name = "status")
    private String status;

    @Column(name = "applicationno")
    private String applicationNumber;

    @OrderBy("id")
    @OneToMany(mappedBy = "vacancyRemission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VacancyRemissionApproval> vacancyRemissionApproval = new LinkedList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "egpt_vacancy_remission_docs", joinColumns = @JoinColumn(name = "vacancyremission"), inverseJoinColumns = @JoinColumn(name = "document"))
    private List<Document> documents = new ArrayList<>();

    @Transient
    private String meesevaApplicationNumber;

    @Column(name = "source")
    private String source;

    @Override
    public String getStateDetails() {
        return "Vacancy Remission" + " - " + this.basicProperty.getUpicNo();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public BasicPropertyImpl getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(BasicPropertyImpl basicProperty) {
        this.basicProperty = basicProperty;
    }

    public Date getVacancyFromDate() {
        return vacancyFromDate;
    }

    public void setVacancyFromDate(Date vacancyFromDate) {
        this.vacancyFromDate = vacancyFromDate;
    }

    public Date getVacancyToDate() {
        return vacancyToDate;
    }

    public void setVacancyToDate(Date vacancyToDate) {
        this.vacancyToDate = vacancyToDate;
    }

    public String getVacancyComments() {
        return vacancyComments;
    }

    public void setVacancyComments(String vacancyComments) {
        this.vacancyComments = vacancyComments;
    }

    public List<VacancyRemissionDetails> getVacancyRemissionDetails() {
        return vacancyRemissionDetails;
    }

    public void setVacancyRemissionDetails(List<VacancyRemissionDetails> vacancyRemissionDetails) {
        this.vacancyRemissionDetails = vacancyRemissionDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<VacancyRemissionApproval> getVacancyRemissionApproval() {
        return vacancyRemissionApproval;
    }

    public void setVacancyRemissionApproval(List<VacancyRemissionApproval> vacancyRemissionApproval) {
        this.vacancyRemissionApproval = vacancyRemissionApproval;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getMeesevaApplicationNumber() {
        return meesevaApplicationNumber;
    }

    public void setMeesevaApplicationNumber(String meesevaApplicationNumber) {
        this.meesevaApplicationNumber = meesevaApplicationNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

}
