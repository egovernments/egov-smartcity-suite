package org.egov.works.web.actions.qualityControl;

import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.qualityControl.TestMaster;

public class AjaxTestMasterAction extends BaseFormAction {
	
	private static final String TESTNAMEUNIQUECHECK = "testNameUniqueCheck";
	private PersistenceService<TestMaster,Long> testMasterService;
	private String testName;
	private Long materialTypeId;
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String testNameUniqueCheck(){
		return TESTNAMEUNIQUECHECK;
	}
	
	
	public boolean getTestNameCheck(){
		TestMaster testMaster = null;
		boolean testNameexistsOrNot = false;
		testMaster =testMasterService.find("from TestMaster where materialType.id = ? and upper(testName) = ?",materialTypeId,testName.toUpperCase());
		if(testMaster!=null) {
			testNameexistsOrNot=true;
		}
		return testNameexistsOrNot;
	}

	public void setTestMasterService(
			PersistenceService<TestMaster, Long> testMasterService) {
		this.testMasterService = testMasterService;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public Long getMaterialTypeId() {
		return materialTypeId;
	}

	public void setMaterialTypeId(Long materialTypeId) {
		this.materialTypeId = materialTypeId;
	}
}