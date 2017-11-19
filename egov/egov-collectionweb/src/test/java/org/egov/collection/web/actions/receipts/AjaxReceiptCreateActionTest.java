/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.collection.web.actions.receipts;


public class AjaxReceiptCreateActionTest {/*extends AbstractPersistenceServiceTest<ReceiptPayeeDetails,Long>{
	
	private AjaxReceiptCreateAction action;
	private Session session;
	private CollectionObjectFactory objectFactory;
	private Accountdetailtype employeeDetailType;	
	@Before
	public void setupAction(){
	
		action = new AjaxReceiptCreateAction();
		action.setPersistenceService(genericService);
		objectFactory = new CollectionObjectFactory(session);
		employeeDetailType=(Accountdetailtype)genericService.find("from Accountdetailtype where name=?", "Employee");
	}
	
	@Test
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
	}
	
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
*/}