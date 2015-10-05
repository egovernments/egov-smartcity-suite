package org.egov.tl.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.hibernate.validator.constraints.Length;
@Entity
@Table(name="egtl_feematrix_detail")
@SequenceGenerator(name=FeeMatrixDetail.SEQ,sequenceName=FeeMatrixDetail.SEQ)
public class FeeMatrixDetail extends AbstractPersistable<Long> {
	public static final String SEQ="seq_egtl_feematrix_detail";
    private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(generator = SEQ, strategy = GenerationType.SEQUENCE)
	 private Long id;
	
	@ManyToOne
    @JoinColumn(name = "feeMatrix", nullable = false)
	private FeeMatrix feeMatrix;

    private Integer uomFrom;

    private Integer uomTo;
    
    private Double percentage;
    @Temporal(value = TemporalType.DATE)
    private Date fromDate;
  
    private BigDecimal amount;
	public Long getId() {
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getUomFrom() {
		return uomFrom;
	}
	public void setUomFrom(Integer uomFrom) {
		this.uomFrom = uomFrom;
	}
	public Integer getUomTo() {
		return uomTo;
	}
	public void setUomTo(Integer uomTo) {
		this.uomTo = uomTo;
	}
	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public FeeMatrix getFeeMatrix() {
		return feeMatrix;
	}
	public void setFeeMatrix(FeeMatrix feeMatrix) {
		this.feeMatrix = feeMatrix;
	}
	
}
