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




public class BankRemittanceActionTest { /*extends AbstractPersistenceServiceTest<ReceiptHeader,Long> {

private BankRemittanceAction action;
private ReceiptHeaderService receiptService;
private CommonsManager commonsManager;
private PersistenceService service;
private FinancialsUtil financialsUtil;
private CollectionsUtil collectionsUtil; 
private UserManager userManager;
private EisManager eisManager;
private @Autowired AppConfigValuesDAO appConfigValuesDAO;
	
	public BankRemittanceActionTest() {
		this.type = ReceiptHeader.class;
	}
	
	@Before
	public void setUp() throws Exception {
		action = new BankRemittanceAction(){
			protected ReceiptHeader io=new ReceiptHeader();
			
			@Override
			protected void setValue(String relationshipName, Object relation) {
				try {
					Ognl.setValue(relationshipName, action.getModel(), relation);
				} catch (OgnlException e) {
					throw new RuntimeException(e);
				}
			}
			public Object getModel() {
				return io;
			}
		};
		genericDao = new GenericHibernateDaoFactory(){
			protected Session getCurrentSession(){
				return session;
			}
			public  AppConfigValuesDAO getAppConfigValuesDAO()
			{
				return new AppConfigValuesHibernateDAO(AppConfigValues.class,session);
			}
		};
		commonsManager=createMock(CommonsManager.class);
		service = createMock(PersistenceService.class);
		financialsUtil = createMock(FinancialsUtil.class);
		userManager = createMock(UserManager.class);
		eisManager = createMock(EisManager.class);
		collectionsUtil=new CollectionsUtil();
		
		collectionsUtil.setGenericDao(genericDao);
		collectionsUtil.setEisManager(eisManager);
		collectionsUtil.setUserManager(userManager);
		collectionsUtil.setScriptService(genericService);
		collectionsUtil.setPersistenceService(genericService);
		
		receiptService = new ReceiptHeaderService();
		receiptService.setType(ReceiptHeader.class);
		receiptService.setFinancialsUtil(financialsUtil);
		receiptService.setCommonsManager(commonsManager);
		receiptService.setPersistenceService(service);
		receiptService.setCollectionsUtil(collectionsUtil);
		
		action.setReceiptHeaderService(receiptService);
		action.setCollectionsUtil(collectionsUtil);
		action.setPersistenceService(genericService);
		
	}

	//@Test
	public void list()
	{		
		UserImpl user = (UserImpl) genericService.find(
				"from UserImpl U where U.userName =?",CollectionConstants.SUPER_USER_NAME);
		
		UserImpl citizenUser = (UserImpl) genericService.find(
				"from UserImpl U where U.userName =?",CollectionConstants.CITIZEN_USER_NAME);
		
		userManager.getUserByUserName(CollectionConstants.SUPER_USER_NAME);
		expectLastCall().andReturn(user);
		
		userManager.getUserByUserName(CollectionConstants.CITIZEN_USER_NAME);
		expectLastCall().andReturn(citizenUser);
		replay(userManager);
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("com.egov.user.LoginUserName", user.getUserName());
		action.setSession(sessionMap);
		
		assertEquals("new", action.list());
		verify(userManager);
	}
	
	@Test
	public void newform()
	{
		assertEquals("new", action.newform());
	}
	
	@Test
	public void edit()
	{
		assertEquals("edit", action.edit());
	}
	
	@Test
	public void create()
	{ 
		action.setServiceNameArray(new String[]{""});
		action.setTotalCashAmountArray(new String[]{""});
		action.setTotalChequeAmountArray(new String[]{""});
		action.setTotalCardAmountArray(new String[]{""});
		action.setTotalOnlineAmountArray(new String[]{""});
		action.setReceiptDateArray(new String[]{""});
		action.setAccountNumberMaster(null);
		assertEquals("index", action.create());
		assertEquals(true, action.getVoucherHeaderValues().isEmpty());
	}
	
	@Test
	public void save()
	{
		assertEquals("success", action.save());
	}
	
	@Test
	public void prepareAction() {
		action.prepare();
		assertEquals(true, action.getDropdownData().get("bankBranchList").isEmpty());
		assertEquals(true, action.getDropdownData().get("accountNumberList").isEmpty());
	
	}
*/}
