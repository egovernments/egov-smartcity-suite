package org.egov.erpcollection.web.actions.receipts;


import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import ognl.Ognl;
import ognl.OgnlException;

import org.egov.commons.service.CommonsManager;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.services.ReceiptHeaderService;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.erpcollection.util.FinancialsUtil;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.config.dao.AppConfigValuesHibernateDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.rjbac.user.ejb.api.UserManager;
import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.pims.service.EisManager;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;


public class BankRemittanceActionTest extends AbstractPersistenceServiceTest<ReceiptHeader,Long> {

private BankRemittanceAction action;
private ReceiptHeaderService receiptService;
private CommonsManager commonsManager;
private PersistenceService service;
private FinancialsUtil financialsUtil;
private CollectionsUtil collectionsUtil; 
private UserManager userManager;
private EisManager eisManager;
private GenericHibernateDaoFactory genericDao;
	
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
		receiptService.setSessionFactory(egovSessionFactory);
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
}
