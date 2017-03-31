package org.egov.bpa.application.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.admin.master.entity.User;
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

    /*
     * @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
     * @JoinTable(name = "egwtr_documents", joinColumns = @JoinColumn(name = "applicationdocumentsid"), inverseJoinColumns
     * = @JoinColumn(name = "filestoreid")) private Set<FileStoreMapper> supportDocs = Collections.emptySet();
     */

    private transient MultipartFile[] files;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "checklistDetail", nullable = false)
    private CheckListDetail checklistDetail;
    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name = "application", nullable = false)
    private Application application;
    @Temporal(value = TemporalType.DATE)
    private Date submissionDate;
    private Boolean issubmitted;

    @ManyToOne(cascade = CascadeType.ALL)
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

    public void setCreateduser(User createduser) {
        this.createduser = createduser;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(final Application application) {
        this.application = application;
    }

    public CheckListDetail getChecklistDetail() {
        return checklistDetail;
    }

    public void setChecklistDetail(CheckListDetail checklistDetail) {
        this.checklistDetail = checklistDetail;
    }

}
