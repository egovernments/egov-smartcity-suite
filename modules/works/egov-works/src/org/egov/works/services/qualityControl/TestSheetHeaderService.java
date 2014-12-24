package org.egov.works.services.qualityControl;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsServiceImpl;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.qualityControl.TestSheetHeader;
import org.egov.works.models.qualityControl.TestSheetNumberGenerator;

public class TestSheetHeaderService extends PersistenceService<TestSheetHeader,Long>{
	private static final Logger logger = Logger.getLogger(TestSheetHeaderService.class);
	private TestSheetNumberGenerator testSheetNumberGenerator;
	private CommonsServiceImpl commonsService;
	
	public TestSheetHeaderService(){
		setType(TestSheetHeader.class);
	}
	
	public void setTestSheetNumber(TestSheetHeader entity){
		CFinancialYear financialYear=commonsService.getFinancialYearByDate(entity.getTestSheetDate());
		if(entity.getTestSheetNumber()==null){ 
			entity.setTestSheetNumber(testSheetNumberGenerator.getTestSheetNumber(entity, financialYear));
		}
	}

	public void setCommonsService(CommonsServiceImpl commonsService) {
		this.commonsService = commonsService;
	}

	public void setTestSheetNumberGenerator(
			TestSheetNumberGenerator testSheetNumberGenerator) {
		this.testSheetNumberGenerator = testSheetNumberGenerator;
	}
}
