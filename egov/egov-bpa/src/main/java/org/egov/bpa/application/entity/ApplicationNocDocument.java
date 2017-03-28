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
import javax.validation.constraints.NotNull;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "egbpa_NOC_Document")
@SequenceGenerator(name = ApplicationNocDocument.SEQ_APPLICATIONDOCUMENT, sequenceName = ApplicationNocDocument.SEQ_APPLICATIONDOCUMENT, allocationSize = 1)
public class ApplicationNocDocument extends AbstractAuditable {

	private static final long serialVersionUID = -4555037259173138199L;
	public static final String SEQ_APPLICATIONDOCUMENT= "seq_egbpa_NOC_Document";

	@Id
	@GeneratedValue(generator = SEQ_APPLICATIONDOCUMENT, strategy = GenerationType.SEQUENCE)
	private Long id;

	/*
	 * @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	 * 
	 * @JoinTable(name = "egwtr_documents", joinColumns = @JoinColumn(name =
	 * "applicationdocumentsid"), inverseJoinColumns = @JoinColumn(name =
	 * "filestoreid")) private Set<FileStoreMapper> supportDocs =
	 * Collections.emptySet();
	 */

	private transient MultipartFile[] files;
	@ManyToOne
	@NotNull
	@JoinColumn(name = "checklistcode", nullable = false)
	private CheckListDetail checklist;
	@ManyToOne(cascade = CascadeType.ALL)
	@NotNull
	@JoinColumn(name = "application", nullable = false)
	private Application application;
	private Date submissionDate;
	private Boolean issubmitted;
	@ManyToOne(cascade = CascadeType.ALL)
	private User createdusercode;
	@Length(min = 1, max = 128)
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

	public CheckListDetail getChecklist() {
		return checklist;
	}

	public void setChecklist(CheckListDetail checklist) {
		this.checklist = checklist;
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

	public User getCreatedusercode() {
		return createdusercode;
	}

	public void setCreatedusercode(final User createdusercode) {
		this.createdusercode = createdusercode;
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

}
