package org.egov.erpcollection.web.actions.receipts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.erpcollection.models.ReceiptPayeeDetails;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.models.AbstractPersistenceServiceTest;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

public class AjaxReceiptCreateActionTest extends AbstractPersistenceServiceTest<ReceiptPayeeDetails,Long>{
	
	private AjaxReceiptCreateAction action;
	private Session session;
	private CollectionObjectFactory objectFactory;
	private Accountdetailtype employeeDetailType;	
	@Before
	public void setupAction(){
	
		action = new AjaxReceiptCreateAction();
		action.setPersistenceService(genericService);
		session=HibernateUtil.getCurrentSession();
		objectFactory = new CollectionObjectFactory(session);
		employeeDetailType=(Accountdetailtype)genericService.find("from Accountdetailtype where name=?", "Employee");
	}
	
/*	@Test
	public void testajaxLoadSchemes() throws Exception{
		Fund fund=objectFactory.createFund("01010");
		objectFactory.createScheme("2020", "testscheme", fund);
		action.setFundId(fund.getId());
		String result = action.ajaxLoadSchemes();
		assertEquals("schemeList",result);
	}

	@Test
	public void testajaxLoadSubSchemes() throws Exception{
		Fund fund=objectFactory.createFund("01010");
		Scheme scheme=objectFactory.createScheme("2020", "testscheme", fund);
		objectFactory.createSubScheme(scheme);
		action.setSchemeId(scheme.getId());
		String result = action.ajaxLoadSubSchemes();
		assertEquals("subSchemeList",result);
	}*/
	
	@Test
	public void testjaxValidateDetailCode()throws Exception{
		Map<String, String[]> map = new HashMap<String, String[]>();
		if(employeeDetailType!=null){
			map.put("detailtypeid", new String[]{employeeDetailType.getId().toString()});
		}
		else{
			map.put("detailtypeid", new String[]{"1"});
		}
		map.put("index", new String[]{"0"});
		map.put("code", new String[]{"103"});
		map.put("codeorname", new String[]{"code"});
		action.setParameters(map);
		action.ajaxValidateDetailCode();
		assertNotNull(action.getValue());
	}
	
	@Test
	public void testAjaxValidateDetailCodeWithEmptyEntity()throws Exception{
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("detailtypeid", new String[]{"3"});
		map.put("index", new String[]{"0"});
		map.put("code", new String[]{"0"});
		map.put("codeorname", new String[]{"code"});
		action.setParameters(map);
		action.ajaxValidateDetailCode();
		assertNotNull(action.getValue());
	}
	
	@Test
	public void testAjaxValidateDetailCodeInvalidAccDetailtype()throws Exception{
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("detailtypeid", new String[]{"-1"});
		map.put("index", new String[]{"0"});
		map.put("code", new String[]{"0"});
		map.put("codeorname", new String[]{"code"});
		action.setParameters(map);
		action.ajaxValidateDetailCode();
		assertEquals(0+"~"+"error"+"#", action.getValue());
	}
	
	@Test
	public void testgetDetailType() throws Exception{
		 CChartOfAccounts chartOfAccounts= objectFactory.createCOA("1010100");
		 Accountdetailtype accountDetailType = objectFactory.createAccountdetailtype("TestAccountDetailType");
		 objectFactory.createCOADetail(chartOfAccounts, accountDetailType);
		 Map<String, String[]> map = new HashMap<String, String[]>();
		 map.put("accountCode", new String[]{chartOfAccounts.getGlcode()});
		 map.put("index", new String[]{"0"});
		 map.put("selectedDetailType", new String[]{"1"});
		 map.put("onload", new String[]{"true"});
		 action.setParameters(map);
		 action.getDetailType();
		 assertNotNull(action.getValue());
	}
	
	@Test
	public void testgetDetailTypewithInvalidData() throws Exception{
		 CChartOfAccounts chartOfAccounts= objectFactory.createCOA("1010100");
		 Map<String, String[]> map = new HashMap<String, String[]>();
		 map.put("accountCode", new String[]{chartOfAccounts.getGlcode()});
		 map.put("index", new String[]{"0"});
		 map.put("selectedDetailType", new String[]{"1"});
		 map.put("onload", new String[]{"true"});
		 action.setParameters(map);
		 action.getDetailType();
		 assertNotNull(action.getValue());
	}
	
	@Test
	public void testGetDetailCode() throws Exception{
		 CChartOfAccounts chartOfAccounts= objectFactory.createCOA("1010100");
		 Accountdetailtype accountDetailType = objectFactory.createAccountdetailtype("TestAccountDetailType");
		 objectFactory.createCOADetail(chartOfAccounts, accountDetailType);
		 Map<String, String[]> map = new HashMap<String, String[]>();
		 map.put("accountCodes", new String[]{chartOfAccounts.getGlcode()});
		 action.setParameters(map);
		 action.getDetailCode();
		 assertNotNull(action.getValue());
	}
	
	@Test
	public void testGetAccountForNoService() throws Exception{
		objectFactory.createUnsavedChallanServiceDetails();
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("serviceId", new String[]{"123"});
		action.setParameters(map);
		action.getAccountForService();
		assertNotNull(action.getValue());
	}
	
	@Test
	public void testGetAccountForService() throws Exception{
		ServiceDetails sd=objectFactory.createChallanServiceDetails();
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("serviceId", new String[]{sd.getId().toString()});
		action.setParameters(map);
		action.getAccountForService();
		assertNotNull(action.getValue());
	}
	
	@Test
	public void testGetMISdetailsForService() throws Exception{
		ServiceDetails sd=objectFactory.createChallanServiceDetails();
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("serviceId", new String[]{sd.getId().toString()});
		action.setParameters(map);
		action.getMISdetailsForService();
		assertNotNull(action.getValue());
	}
	
	@Test
	public void testGetCode() throws Exception{
		
		Map<String, String[]> map = new HashMap<String, String[]>();
		if(employeeDetailType!=null){
			map.put("detailTypeId", new String[]{employeeDetailType.getId().toString()});
		}
		else{
			map.put("detailTypeId", new String[]{"1"});
		}
		map.put("filterKey", new String[]{"1"});
		action.setParameters(map);
		action.getCode();
		String result=action.getValue();
		assertNotNull(result);
	}
	
	@Test
	public void testGetCodeInvalidDetailTypeId() throws Exception{
		
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("detailTypeId",new String[]{"123"});
		map.put("filterKey", new String[]{"1"});
		action.setParameters(map);
		action.getCode();
		String result=action.getValue();
		assertEquals(result,"");
	}
	
	@Test
	public void testGetCodeNoEntities() throws Exception{
		
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("detailTypeId",new String[]{"3"});
		map.put("filterKey", new String[]{"1"});
		action.setParameters(map);
		action.getCode();
		String result=action.getValue();
		assertEquals(result,"");
	}
	
	@Test
	public void testGetModel(){
		assertNull(action.getModel());
	}
	
	@Test
	public void testGetCodeNew() throws Exception{
		Map<String, String[]> map = new HashMap<String, String[]>();
		
		Accountdetailtype accDetType = objectFactory.createAccountdetailtype("testAccDetTypeName");
		//map.put("detailTypeId",new String[]{accDetType.getId().toString()});
		map.put("detailTypeId",new String[]{"0"});
		map.put("filterKey", new String[]{"1"});
		action.setParameters(map);
		assertEquals("result", action.getCodeNew());
		
		map.put("detailTypeId",new String[]{"0"});
		action.setParameters(map);
		
	}
}