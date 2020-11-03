package org.egov.tl.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egtl_notice")
@SequenceGenerator(name = LicenseNotice.SEQUENCE, sequenceName = LicenseNotice.SEQUENCE, allocationSize = 1)
public class LicenseNotice implements Serializable {

	private static final long serialVersionUID = 1L;

    public static final String SEQUENCE = "SEQ_EGTL_NOTICE";

    @Id
    @GeneratedValue(generator = SEQUENCE, strategy = GenerationType.SEQUENCE)
    private Long id;
        
    @SafeHtml
    private String noticeNo;

    @Temporal(value = TemporalType.DATE)
    private Date noticeDate;
    
    @SafeHtml
    private String noticeType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="licenseid", nullable = false)
    private TradeLicense licenseId;
        
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "filestore", nullable = false)
    private FileStoreMapper fileStore;
    
    @SafeHtml
    private String applicationNumber;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public Date getNoticeDate() {
		return noticeDate;
	}

	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public TradeLicense getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(TradeLicense licenseId) {
		this.licenseId = licenseId;
	}

	public FileStoreMapper getFileStore() {
		return fileStore;
	}

	public void setFileStore(FileStoreMapper fileStore) {
		this.fileStore = fileStore;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
    
    
}
