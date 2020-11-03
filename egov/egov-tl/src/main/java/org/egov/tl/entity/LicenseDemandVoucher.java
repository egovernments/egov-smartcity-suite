package org.egov.tl.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.commons.CVoucherHeader;


/**
 * @author Pabitra
 *
 */

@Entity
@Table(name = "EGTL_DEMAND_VOUCHER")
@SequenceGenerator(name = LicenseDemandVoucher.SEQ_EGTL_DEMAND_VOUCHER, sequenceName = LicenseDemandVoucher.SEQ_EGTL_DEMAND_VOUCHER, allocationSize = 1)
public class LicenseDemandVoucher {

	private static final long serialVersionUID = 1L;
    public static final String SEQ_EGTL_DEMAND_VOUCHER = "SEQ_EGTL_DEMAND_VOUCHER";
    
    @Id
    @GeneratedValue(generator = SEQ_EGTL_DEMAND_VOUCHER, strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "licensedetail", updatable = false)
    private TradeLicense licensedetail;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "voucherHeader", updatable = false)
    private CVoucherHeader voucherHeader;
    
    @Column(name = "createdby")
    private Long createdBy;
    
    @Column(name = "lastmodifiedby")
    private Long lastModifiedBy;
    
    @Column(name = "createddate")
    private Date createdDate;
    
    @Column(name = "lastmodifieddate")
    private Date lastModifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TradeLicense getLicensedetail() {
		return licensedetail;
	}

	public void setLicensedetail(TradeLicense licensedetail) {
		this.licensedetail = licensedetail;
	}

	public CVoucherHeader getVoucherHeader() {
		return voucherHeader;
	}

	public void setVoucherHeader(CVoucherHeader voucherHeader) {
		this.voucherHeader = voucherHeader;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}	
	
}
