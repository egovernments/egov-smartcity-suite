package org.egov.ptis.domain.entity.property;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egpt_wood_type")
@SequenceGenerator(name = WoodType.SEQ_WOODTYPE, sequenceName = WoodType.SEQ_WOODTYPE, allocationSize = 1)
public class WoodType extends AbstractAuditable {

	private static final long serialVersionUID = 1L;
	public static final String SEQ_WOODTYPE = "SEQ_EGPT_WALL_TYPE";
	
	@Id
	@GeneratedValue(generator = SEQ_WOODTYPE, strategy = GenerationType.SEQUENCE)
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
