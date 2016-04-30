package org.egov.ptis.domain.entity.property;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "egpt_exemption_reason")
@SequenceGenerator(name = TaxExeptionReason.SEQ_TAX_EXEMPTION_REASON, sequenceName = TaxExeptionReason.SEQ_TAX_EXEMPTION_REASON, allocationSize = 1)
public class TaxExeptionReason extends AbstractAuditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEQ_TAX_EXEMPTION_REASON = "SEQ_EGPT_EXEMPTION_REASON";

	@Id
	@GeneratedValue(generator = SEQ_TAX_EXEMPTION_REASON, strategy = GenerationType.SEQUENCE)
	private Long id;

	@SafeHtml
	private String name;

	@SafeHtml
	private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

}
