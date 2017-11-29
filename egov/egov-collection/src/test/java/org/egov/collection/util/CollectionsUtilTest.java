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

/**
 * 
 */
package org.egov.collection.util;


/**
 * JUnit tests for collections utilities
 */
public class CollectionsUtilTest{ /* extends AbstractPersistenceServiceTest {
	private CollectionsUtil collectionsUtil;
	private CollectionObjectFactory objectFactory;
	
	@Before
	public void setUp() {
		objectFactory = new CollectionObjectFactory(session);
		
		PersistenceService<Script, Long> scriptService = new PersistenceService<Script, Long>();

		collectionsUtil = new CollectionsUtil();
		collectionsUtil.setGenericDao(new GenericHibernateDaoFactory());
		collectionsUtil.setScriptService(scriptService);
	}
	
	@Test
	public void testGetDept() {
		String testUserName = "test.user";
		String testDeptName = "test.deptname";
		Department testDept = objectFactory.createDept(testDeptName);
		UserImpl testUser = objectFactory.createUser(testUserName, testDept);
		
		PersonalInformation emp = objectFactory.createPersonalInformation(testUser, testDept);
		AssignmentPrd ap = objectFactory.createEmpAsgnmtPrd(emp);
		DesignationMaster desig = objectFactory.createDesignation(testDept.getId(), "testDesignation"+objectFactory.getRandomNumber());
		Position position = objectFactory.createPosition(desig);
		Assignment assignment=objectFactory.createAssignment(ap, desig, position,testDept);
		
		
		EisManager eisManagerMock = EasyMock.createMock(EisManager.class);
		collectionsUtil.setEisManager(eisManagerMock);
		
		UserManager userManagerMock = EasyMock.createMock(UserManager.class);
		collectionsUtil.setUserManager(userManagerMock);
		
		EasyMock.expect(eisManagerMock.getEmpForUserId(testUser.getId())).andReturn(emp);
		EasyMock.expect(eisManagerMock.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class))).andReturn(assignment);
		EasyMock.expect(eisManagerMock.getEmpForUserId(testUser.getId())).andReturn(emp);
		EasyMock.expect(eisManagerMock.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class))).andReturn(assignment);
		EasyMock.replay(eisManagerMock);
		
		EasyMock.expect(userManagerMock.getUserByUserName(testUserName)).andReturn(testUser);
		EasyMock.replay(userManagerMock);
		
		Department dept = collectionsUtil.getDepartmentOfUser(testUser);
		Assert.assertEquals(testDept, dept);
		
		Map<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put(CollectionConstants.SESSION_VAR_LOGIN_USER_NAME, testUserName);
		
		dept = collectionsUtil.getDepartmentOfLoggedInUser(sessionMap);
		Assert.assertEquals(testDept, dept);
		
		EasyMock.verify(eisManagerMock);
		EasyMock.verify(userManagerMock);
	}
	
	@Test
	public void testCheckChallanValidity(){
		Challan challan = new Challan();
		
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Date challanDate=new Date();
		Date validUpto = new Date();
		start.setTime(challanDate) ;
		
		end.setTime(validUpto);
		
		// T <= current date <= T
		challan.setValidUpto(end.getTime());
		challan.setChallanDate(start.getTime());
		assertTrue(collectionsUtil.checkChallanValidity(challan));
		
		// T-1 <= current date <= T
		start.add(Calendar.DATE,-1);
		challan.setChallanDate(start.getTime());
		assertTrue(collectionsUtil.checkChallanValidity(challan));
		
		// T <= current date <= T+3
		start.add(Calendar.DATE,1);
		challan.setChallanDate(start.getTime());
		end.add(Calendar.DATE,3);
		validUpto=end.getTime();
		challan.setValidUpto(validUpto);
		assertTrue(collectionsUtil.checkChallanValidity(challan));
		
		// T+1 < current date < T+3
		start.add(Calendar.DATE,1);
		challan.setChallanDate(start.getTime());
		assertFalse(collectionsUtil.checkChallanValidity(challan));
		
		// T-3 < current date < T-1
		start.add(Calendar.DATE,-3);
		challan.setChallanDate(start.getTime());
		end.add(Calendar.DATE,-4);
		validUpto=end.getTime();
		challan.setValidUpto(validUpto);
		assertFalse(collectionsUtil.checkChallanValidity(challan));
		
		// T-2 < current date < T-1
		start.add(Calendar.DATE,-3);
		challan.setChallanDate(start.getTime());
		end.add(Calendar.DATE,-4);
		validUpto=end.getTime();
		challan.setValidUpto(validUpto);
		assertFalse(collectionsUtil.checkChallanValidity(challan));
	}*/
}
