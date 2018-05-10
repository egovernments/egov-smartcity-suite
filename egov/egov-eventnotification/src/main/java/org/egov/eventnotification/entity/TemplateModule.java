package org.egov.eventnotification.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "egevntnotification_module")
@SequenceGenerator(name = TemplateModule.SEQ_EGEVNTNOTIFICATION_MODULE, sequenceName = TemplateModule.SEQ_EGEVNTNOTIFICATION_MODULE, allocationSize = 1)
public class TemplateModule {

	private static final long serialVersionUID = 1L;
    public static final String SEQ_EGEVNTNOTIFICATION_MODULE = "seq_egevntnotification_module";
    
    @Id
    @GeneratedValue(generator = SEQ_EGEVNTNOTIFICATION_MODULE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Length(max = 100)
    private String name;

    @NotNull
    private Boolean active;

    
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
    
    
}
