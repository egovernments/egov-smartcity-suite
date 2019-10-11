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
package org.egov.ptis.domain.entity.property;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;
import org.egov.ptis.bean.demand.DemandDetail;

@Entity
@Table(name = "egpt_write_off")
@SequenceGenerator(name = WriteOff.SEQ_WRITEOFF, sequenceName = WriteOff.SEQ_WRITEOFF, allocationSize = 1)

public class WriteOff extends StateAware<Position> {

    private static final long serialVersionUID = 8839183407077692372L;

    public static final String SEQ_WRITEOFF = "SEQ_EGPT_WRITE_OFF";

    @Id
    @GeneratedValue(generator = SEQ_WRITEOFF, strategy = GenerationType.SEQUENCE)
    public Long id;

    @ManyToOne(targetEntity = PropertyImpl.class, cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(name = "property")
    private PropertyImpl property;

    @ManyToOne(targetEntity = BasicPropertyImpl.class, cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(name = "basicproperty", nullable = false)
    private BasicPropertyImpl basicProperty;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "egpt_write_off_docs", joinColumns = @JoinColumn(name = "writeOff"), inverseJoinColumns = @JoinColumn(name = "document"))
    private List<Document> documents = new ArrayList<>();

    @ManyToOne(targetEntity = PropertyMutationMaster.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "mutation_master_id")
    private PropertyMutationMaster writeOffType;

    @ManyToOne(targetEntity = WriteOffReasons.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_writeoff_reason")
    private WriteOffReasons writeOffReasons;

    @Transient
    private transient List<DemandDetail> demandDetailBeanList = new ArrayList<>();
    @Transient
    private transient List<Document> writeoffDocumentsProxy = new ArrayList<>();

    @Column(name = "applicationno")
    private String applicationNumber;

    @Column(name = "resolutionNo")
    private String resolutionNo;

    @Column(name = "resolutionDate")
    private String resolutionDate;

    @Column(name = "deactivate")
    private boolean propertyDeactivateFlag;

    @Column(name = "resolutionType")
    private String resolutionType;

    @Column(name = "fromInstallment")
    private String fromInstallment;

    @Column(name = "toInstallment")
    private String toInstallment;

    @Column(name = "comments")
    private String comments;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public PropertyImpl getProperty() {
        return property;
    }

    public void setProperty(PropertyImpl property) {
        this.property = property;
    }

    public BasicPropertyImpl getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(BasicPropertyImpl basicProperty) {
        this.basicProperty = basicProperty;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getResolutionNo() {
        return resolutionNo;
    }

    public void setResolutionNo(String resolutionNo) {
        this.resolutionNo = resolutionNo;
    }

    public String getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(String resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public List<DemandDetail> getDemandDetailBeanList() {
        return demandDetailBeanList;
    }

    public void setDemandDetailBeanList(List<DemandDetail> demandDetailBeanList) {
        this.demandDetailBeanList = demandDetailBeanList;
    }

    public boolean getPropertyDeactivateFlag() {
        return propertyDeactivateFlag;
    }

    public void setPropertyDeactivateFlag(boolean propertyDeactivateFlag) {
        this.propertyDeactivateFlag = propertyDeactivateFlag;
    }

    public WriteOffReasons getWriteOffReasons() {
        return writeOffReasons;
    }

    public void setWriteOffReasons(WriteOffReasons writeOffReasons) {
        this.writeOffReasons = writeOffReasons;
    }

    public Position getPositionById(Long approvalPosition) {
        return null;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public String getResolutionType() {
        return resolutionType;
    }

    public void setResolutionType(String resolutionType) {
        this.resolutionType = resolutionType;
    }

    public String getFromInstallment() {
        return fromInstallment;
    }

    public void setFromInstallment(String fromInstallment) {
        this.fromInstallment = fromInstallment;
    }

    public String getToInstallment() {
        return toInstallment;
    }

    public void setToInstallment(String toInstallment) {
        this.toInstallment = toInstallment;
    }

    public PropertyMutationMaster getWriteOffType() {
        return writeOffType;
    }

    public void setWriteOffType(PropertyMutationMaster writeOffType) {
        this.writeOffType = writeOffType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String getStateDetails() {
        return "Write Off" + " - " + this.getBasicProperty().getUpicNo();
    }

    public List<Document> getWriteoffDocumentsProxy() {
        return writeoffDocumentsProxy;
    }

    public void setWriteoffDocumentsProxy(List<Document> writeoffDocumentsProxy) {
        this.writeoffDocumentsProxy = writeoffDocumentsProxy;
    }
}
