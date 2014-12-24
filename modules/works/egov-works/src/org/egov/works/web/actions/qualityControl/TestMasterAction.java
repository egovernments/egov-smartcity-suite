package org.egov.works.web.actions.qualityControl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.ValidationException;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.qualityControl.MaterialType;
import org.egov.works.models.qualityControl.TestMaster;
import org.egov.works.models.qualityControl.TestRate;
import org.egov.works.utils.WorksConstants;

@ParentPackage("egov")
public class TestMasterAction extends SearchFormAction{  
	
	private static final Logger LOGGER = Logger.getLogger(TestMasterAction.class);
	private String sourcepage;
	private Long materialTypeId;
	private Long id;
	private TestMaster testMaster = new TestMaster() ;
	private MaterialType materialType = new MaterialType() ;
	private PersistenceService<MaterialType,Integer> materialTypeService;
	private List<TestRate> testMasterRates = new LinkedList<TestRate>();
	private PersistenceService<TestMaster,Long> testMasterService;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return testMaster;
	}
	
	public void prepare() 
	{
		super.prepare();
		if(materialTypeId!=null){
			materialType=materialTypeService.find(" from MaterialType where id=?", getMaterialTypeId());
		}
		if(materialType!=null) 
			testMaster.setMaterialType(materialType);
		if (id != null) {
			testMaster=testMasterService.findById(id, false);
			materialTypeId=testMaster.getMaterialType().getId();
		}
	}
	
	@SkipValidation
	public String newform(){  
		return NEW;  
	} 
	
	public String edit(){ 
		return EDIT;
	} 
	
	@ValidationErrorPage(value=NEW)
	public String save()
	{
		try{
			populateRates();
			testMasterService.persist(testMaster);
		}
		catch (ValidationException exception) {
			LOGGER.error("Error in TestMasterAction save--"+exception.getStackTrace());
			exception.printStackTrace(); 
			throw exception;
		}
			addActionMessage("Test Master was Created successfully");
		return SUCCESS; 
	}
	
	 protected void populateRates() { 
		 testMaster.getTestRates().clear();
		 for(TestRate rate: testMasterRates) {
			 if (validRate(rate)) {				 
				 testMaster.addRate(rate);
			 }
		 }
	 }
	 
	 @SkipValidation
	public String searchTestMaster(){
		search();
		return "searchResults";
	}
	 
	 public SearchQuery prepareQuery(String sortField, String sortOrder) {
			List<Object> paramList = new ArrayList<Object>();
			StringBuilder dynQuery = new StringBuilder(800);
			dynQuery.append( " from TestMaster tm where tm.materialType != null  ") ;
			if(StringUtils.isNotBlank(materialTypeId.toString()))
			{
				dynQuery.append(" and tm.materialType.id = ?");
				paramList.add(materialTypeId);
			}
			setPageSize(WorksConstants.PAGE_SIZE);
			String searchQuery=dynQuery.toString();
			String countQuery = " select count(*)  " + dynQuery;
			return new SearchQueryHQL(searchQuery, countQuery, paramList);
	 }

	 protected boolean validRate(TestRate rate){		
		 if (rate != null){			 
			 return true;
		 }
		 return false;
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

	public TestMaster getTestMaster() {
		return testMaster;
	}

	public void setTestMaster(TestMaster testMaster) {
		this.testMaster = testMaster;
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

	public void setTestMasterService(
			PersistenceService<TestMaster, Long> testMasterService) {
		this.testMasterService = testMasterService;
	}

	public List<TestRate> getTestMasterRates() {
		return testMasterRates;
	}

	public void setTestMasterRates(List<TestRate> testMasterRates) {
		this.testMasterRates = testMasterRates;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}