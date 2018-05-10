package org.egov.eventnotification.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "egevntnotification_parameters")
@SequenceGenerator(name = CategoryParameters.SEQ_EGEVNTNOTIFICATION_PARAMETERS, sequenceName = CategoryParameters.SEQ_EGEVNTNOTIFICATION_PARAMETERS, allocationSize = 1)
public class CategoryParameters {
	
    public static final String SEQ_EGEVNTNOTIFICATION_PARAMETERS = "seq_egevntnotification_parameters";
    
    @Id
    @GeneratedValue(generator = SEQ_EGEVNTNOTIFICATION_PARAMETERS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Length(max = 100)
    private String name;

    @NotNull
    private Boolean active;
    
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private ModuleCategory moduleCategory;

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

	public ModuleCategory getModuleCategory() {
		return moduleCategory;
	}

	public void setModuleCategory(ModuleCategory moduleCategory) {
		this.moduleCategory = moduleCategory;
	} 
	
}
