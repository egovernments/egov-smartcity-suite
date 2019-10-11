package org.egov.ptis.domain.entity.property;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "egpt_writeoff_reason")
@SequenceGenerator(name = WriteOffReasons.SEQ_WRITE_OFF_REASON, sequenceName = WriteOffReasons.SEQ_WRITE_OFF_REASON, allocationSize = 1)
public class WriteOffReasons implements Serializable {

    /**
    *
    */
    private static final long serialVersionUID = 1L;

    public static final String SEQ_WRITE_OFF_REASON = "SEQ_WRITE_OFF_REASON";

    @Id
    @GeneratedValue(generator = SEQ_WRITE_OFF_REASON, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "isactive")
    private boolean isActive;

    @Column(name = "code")
    private String code;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "description")
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
