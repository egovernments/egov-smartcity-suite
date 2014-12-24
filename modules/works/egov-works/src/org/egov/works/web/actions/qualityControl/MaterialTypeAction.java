package org.egov.works.web.actions.qualityControl;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.qualityControl.MaterialType;

@ParentPackage("egov")
public class MaterialTypeAction extends BaseFormAction{
	
	private MaterialType materialType = new MaterialType() ;
	private static final Boolean ISACTIVE=true;
	private static final Logger LOGGER = Logger.getLogger(MaterialTypeAction.class);
	private PersistenceService<MaterialType,Integer> materialTypeService;
	private String sourcepage;
	private Long materialTypeId;
	private Integer id;
	private Boolean isMTActive;
	
	@Override
	public Object getModel() {
		return materialType;
	}
	 
	public void prepare()
	{
		super.prepare();
		if(sourcepage!=null && (sourcepage.equalsIgnoreCase("view") || sourcepage.equalsIgnoreCase("edit"))){
			if(materialTypeId!=null)
				materialType=materialTypeService.find(" from MaterialType where id=?", getMaterialTypeId());
		} 
	}
	
	public MaterialTypeAction(){
		super();
	}
	
	@SkipValidation
	public String newform(){  
		return NEW;  
	} 
	
	@SkipValidation
	public String view(){  
		return NEW;   
	}
	
	@SkipValidation
	public String edit(){  
		return NEW;    
	} 

	@ValidationErrorPage(value=NEW)
	public String save()
	{
		try{
			if(isMTActive != null && isMTActive.TRUE)
				materialType.setIsActive(ISACTIVE);
			else
				materialType.setIsActive(false); 
			materialTypeService.persist(materialType);   
		}
		catch (ValidationException exception) {
			LOGGER.error("Error in MaterTypeAction save--"+exception.getStackTrace());
			exception.printStackTrace(); 
			throw exception;
		}
		if(sourcepage!=null && sourcepage.equalsIgnoreCase("edit"))
			addActionMessage("Material Type was modified successfully");
		else
			addActionMessage("Material Type was saved successfully");
		return SUCCESS; 
	}

	public MaterialType getMaterialType() { 
		return materialType; 
	}

	public void setMaterialType(MaterialType materialType) { 
		this.materialType = materialType;
	}

	public void setMaterialTypeService(
			PersistenceService<MaterialType, Integer> materialTypeService) {
		this.materialTypeService = materialTypeService;
	}

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}

	public Long getMaterialTypeId() {
		return materialTypeId;
	}

	public void setMaterialTypeId(Long materialTypeId) {
		this.materialTypeId = materialTypeId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsMTActive() {
		return isMTActive;
	}

	public void setIsMTActive(Boolean isMTActive) {
		this.isMTActive = isMTActive;
	}
	
}