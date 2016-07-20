package org.egov.works.abstractestimate.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Required;
import org.hibernate.validator.constraints.Length;

/**
 * Created by mani on 16/6/16.
 */
@Entity
@Table(name="EGW_MEASUREMENTSHEET")
@SequenceGenerator(name = MeasurementSheet.SEQ, sequenceName = MeasurementSheet.SEQ, allocationSize = 1)

public class MeasurementSheet extends AbstractAuditable {
    public static final String SEQ = "SEQ_EGW_MEASUREMENTSHEET";


    @Id
    @GeneratedValue(generator = MeasurementSheet.SEQ, strategy = GenerationType.SEQUENCE)
    private Long id;
    private Integer slNo;
    private char identifier;
    @Length(max = 1024, message = "estimate.measurementSheet.remarks.length")
    private String remarks;

    @Max(9999)
    private Long no;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal depthOrHeight;
    @Required
    private BigDecimal quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activityid")
    private Activity activity;


    @Override
    public Long getId() {
        return id;
    }

    public BigDecimal getLength() {
		return length;
	}

	@Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(Integer slNo) {
        this.slNo = slNo;
    }

    public char getIdentifier() {
        return identifier;
    }

    public void setIdentifier(char identifier) {
        this.identifier = identifier;
    }

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getDepthOrHeight() {
        return depthOrHeight;
    }

    public void setDepthOrHeight(BigDecimal depthOrHeight) {
        this.depthOrHeight = depthOrHeight;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }





}
