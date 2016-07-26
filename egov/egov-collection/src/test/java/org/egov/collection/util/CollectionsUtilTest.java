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
