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
package org.egov.bpa.application.entity;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "egbpa_application_document")
@SequenceGenerator(name = ApplicationDocument.SEQ_APPLICATIONDOCUMENT, sequenceName = ApplicationDocument.SEQ_APPLICATIONDOCUMENT, allocationSize = 1)
public class ApplicationDocument extends AbstractAuditable {

    private static final long serialVersionUID = -4555037259173138199L;
    public static final String SEQ_APPLICATIONDOCUMENT = "seq_egbpa_application_document";

    @Id
    @GeneratedValue(generator = SEQ_APPLICATIONDOCUMENT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "egbpa_documents", joinColumns = @JoinColumn(name = "applicationdocumentid"), inverseJoinColumns = @JoinColumn(name = "filestoreid"))
    private Set<FileStoreMapper> supportDocs = Collections.emptySet();

    private transient MultipartFile[] files;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "checklistDetail", nullable = false)
    private CheckListDetail checklistDetail;
    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name = "application", nullable = false)
    private BpaApplication application;
    @Temporal(value = TemporalType.DATE)
    private Date submissionDate;
    private Boolean issubmitted;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "createduser")
    private User createduser;
    @Length(min = 1, max = 256)
    private String remarks;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(final MultipartFile[] files) {
        this.files = files;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(final Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Boolean getIssubmitted() {
        return issubmitted;
    }

    public void setIssubmitted(final Boolean issubmitted) {
        this.issubmitted = issubmitted;
    }

    public User getCreateduser() {
        return createduser;
    }

    public void setCreateduser(final User createduser) {
        this.createduser = createduser;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public BpaApplication getApplication() {
        return application;
    }

    public void setApplication(final BpaApplication application) {
        this.application = application;
    }

    public CheckListDetail getChecklistDetail() {
        return checklistDetail;
    }

    public void setChecklistDetail(final CheckListDetail checklistDetail) {
        this.checklistDetail = checklistDetail;
    }

    public Set<FileStoreMapper> getSupportDocs() {
        return supportDocs;
    }

    public void setSupportDocs(final Set<FileStoreMapper> supportDocs) {
        this.supportDocs = supportDocs;
    }

}
