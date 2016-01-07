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
@Table(name = "egpt_floor_type")
@SequenceGenerator(name = FloorType.SEQ_FLOORTYPE, sequenceName = FloorType.SEQ_FLOORTYPE, allocationSize = 1)
public class FloorType extends AbstractAuditable {

	private static final long serialVersionUID = 1L;
	public static final String SEQ_FLOORTYPE = "SEQ_EGPT_FLOOR_TYPE";
	
	@Id
	@GeneratedValue(generator = SEQ_FLOORTYPE, strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@SafeHtml
	private String name;
	
	@SafeHtml
	private String code;

	public  String getName() {
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
