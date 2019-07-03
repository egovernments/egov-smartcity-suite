package org.egov.ptis.domain.entity.property;

import javax.persistence.*;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egpt_writeoff_reason")
@SequenceGenerator(name = WriteOffReasons.SEQ_WRITE_OFF_REASON, sequenceName = WriteOffReasons.SEQ_WRITE_OFF_REASON, allocationSize = 1)
public class WriteOffReasons extends AbstractAuditable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	public static final String SEQ_WRITE_OFF_REASON = "SEQ_WRITE_OFF_REASON";

	@Id
	@GeneratedValue(generator = SEQ_WRITE_OFF_REASON, strategy = GenerationType.SEQUENCE)
	private Long id;

	@SafeHtml
	private String name;

	@SafeHtml
	private String type;
	private boolean isActive;
	private String code = null;
	private String orderId = null;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
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

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
