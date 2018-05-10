package org.egov.eventnotification.entity;

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
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "egevntnotification_module_category_map")
@SequenceGenerator(name = ModuleCategoryMap.SEQ_EGEVNTNOTIFICATION_MODULE_CATEGORY, sequenceName = ModuleCategoryMap.SEQ_EGEVNTNOTIFICATION_MODULE_CATEGORY, allocationSize = 1)
public class ModuleCategoryMap {
	private static final long serialVersionUID = 1L;
    public static final String SEQ_EGEVNTNOTIFICATION_MODULE_CATEGORY = "seq_egevntnotification_module_category_map";
    
    @Id
    @GeneratedValue(generator = SEQ_EGEVNTNOTIFICATION_MODULE_CATEGORY, strategy = GenerationType.SEQUENCE)
    private Long id;
    

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "moduleid")
    private TemplateModule module;


    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryid")
    private ModuleCategory category;
    
    @NotNull
    @Column(name="attributes_available", nullable = false)
    private boolean attributesAvailable;

    
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TemplateModule getModule() {
		return module;
	}

	public void setModule(TemplateModule module) {
		this.module = module;
	}

	public ModuleCategory getCategory() {
		return category;
	}

	public void setCategory(ModuleCategory category) {
		this.category = category;
	}

	public boolean isAttributesAvailable() {
		return attributesAvailable;
	}

	public void setAttributesAvailable(boolean attributesAvailable) {
		this.attributesAvailable = attributesAvailable;
	}
    
    
    
	

}
