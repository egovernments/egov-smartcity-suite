package org.egov.commons.dao;

import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.EgiObjectFactory;
import org.egov.commons.service.CommonsServiceImpl;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.hibernate.HibernateException;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Malathi
 * 
 */
@Ignore
public class ChartofAccountsHibernateDAOTest extends EgovHibernateTest {

	private static final Logger LOGGER = Logger
			.getLogger(ChartofAccountsHibernateDAOTest.class);
	String msg = null;
	ChartOfAccountsDAO coa = null;
	public static PersistenceService<CChartOfAccounts, Long> cChartOfAccountsSer;
	private static PersistenceService<Accountdetailtype, Long> accountdetailtypeService;
	private static PersistenceService<CChartOfAccountDetail, Long> cChartOfAccountDetailService;
	private static CommonsServiceImpl comm = null;
	private EgiObjectFactory objectFactory;
	private MockHttpSession session;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		EGOVThreadLocals.setUserId("1");
		this.coa = new ChartOfAccountsHibernateDAO(CChartOfAccounts.class, null);
		this.session = new MockHttpSession();
		comm = new CommonsServiceImpl(null, null, null);
		this.objectFactory = new EgiObjectFactory();
	}

	public void testgetGlcode() throws Exception {
		LOGGER.info("\n" + ">>>>>>>>>>> Inside Testcase - testgetGlcode");
		final String minGlCode = "11001";
		final String maxGlCode = "11002";
		final String majGlCode = "";

		final List glCode = this.coa.getGlcode(minGlCode, maxGlCode, majGlCode);
		assertNotNull(glCode);
		LOGGER.info("list>>>>>>> " + glCode.size());
	}

	public void testGetAccountCodeByPurposeInvalid() {
		this.msg = "";
		try {
			this.coa.getAccountCodeByPurpose(Integer.valueOf(0));
		} catch (final EGOVException e) {
			this.msg = e.getMessage();
		}
		assertEquals("Error occurred while getting CCA using Purpose Id", this.msg);
	}

	public void testGetAccountCodeByPurposeInvalidpurpose() {
		try {
			this.coa.getAccountCodeByPurpose(Integer.valueOf(-1));
		} catch (final EGOVException e) {
			this.msg = e.getMessage();
		}
		assertEquals("Error occurred while getting CCA using Purpose Id", this.msg);
	}

	public void testGetAccountCodeByPurpose() throws Exception {
		final List accountCodeList = this.coa.getAccountCodeByPurpose(Integer
				.valueOf(26));
		LOGGER.info("accountCodeList for detail level" + accountCodeList.size());
		assertNotNull(accountCodeList);
	}

	public void testGetAccountCodeByPurposeMajorcodewise() throws Exception {
		final List accountCodeList = this.coa.getAccountCodeByPurpose(Integer
				.valueOf(2));
		LOGGER.info("accountCodeList for major code level"
				+ accountCodeList.size());
		assertNotNull(accountCodeList);
	}

	public void testGetAccountCodeByPurposeMinorcodewise() throws Exception {
		final List accountCodeList = this.coa.getAccountCodeByPurpose(Integer
				.valueOf(3));
		LOGGER.info("accountCodeList for minor code level"
				+ accountCodeList.size());
		assertNotNull(accountCodeList);
	}

	public void testGetNonControlCodeList() throws EGOVException {
		final List accountCodeList = this.coa.getNonControlCodeList();
		assertNotNull(accountCodeList);
	}

	/**
	 * @description - this method tests for,getting detailtype objects based on
	 *              the glcode.
	 * @throws EGOVException
	 */
	@Test
	public void testDetailTypeForValidGLCode() throws EGOVException {

		final String glcode = createChartOfAccountsWithDetailType();
		System.out.println("Start | testGetAccountdetailtypeListByGLCode");
		// testing by passing the glcode which is having detailtype object.
		// System.out.println("Testing by passing the correct glcode that have detailtype object ");
		final ChartOfAccountsHibernateDAO obj = new ChartOfAccountsHibernateDAO(
				CChartOfAccounts.class, new SessionFactory().getSession());
		final List<Accountdetailtype> list = obj
				.getAccountdetailtypeListByGLCode(glcode);
		if (null != list) {
			assertNotNull(list);
			assertTrue(list.size() > 0);
			System.out.println("Number of detailtype object for the glcode="
					+ glcode + " is =" + list.size());
		} else {
			System.out
					.println("There is no detailtype object for the supplied glcode: "
							+ glcode);
		}

	}

	/**
	 * @description - testing getAccountdetailtypeListByGLCode() method by
	 *              passing null in glcode.
	 * @throws EGOVException
	 */
	@Test
	public void testDetailTypeFornullGlcode() throws EGOVException {

		try {
			this.coa.getAccountdetailtypeListByGLCode(null);
			fail("should raise an  EGOVException");
		} catch (final EGOVException e) {
			assertEquals("glcode supplied by the client is  null ",
					e.getMessage());
		}
	}

	@Test
	public void testDetailTypeByName() throws Exception {

		final String glcode = createChartOfAccountsWithDetailType();
		System.out.println("Start | testDetailTypeByName");
		// testing by passing the glcode which is having detailtype object.
		// System.out.println("Testing by passing the correct glcode that have detailtype object ");
		final ChartOfAccountsHibernateDAO obj = new ChartOfAccountsHibernateDAO(
				CChartOfAccounts.class, new SessionFactory().getSession());
		final Accountdetailtype detailtype = obj.getAccountDetailTypeIdByName(
				glcode, "TestEmployee");
		System.out.println("testDetailTypeByName" + detailtype.getId());
		if (null != detailtype) {
			assertNotNull(detailtype);
		}

	}

	@Test
	public void testAllDetailedAccountCode() throws EGOVException {
		createChartOfAccountsWithDetailType();
		final ChartOfAccountsHibernateDAO obj = new ChartOfAccountsHibernateDAO(
				CChartOfAccounts.class, new SessionFactory().getSession());
		final List<CChartOfAccounts> list = obj.getDetailedAccountCodeList();
		System.out.println("testAllDetailedAccountCode" + list.size());
		if (null != list) {
			assertNotNull(list);
		}

	}

	/**
	 * @description - testing getAccountdetailtypeListByGLCode() method by
	 *              passing a invalid glcode.
	 * @throws EGOVException
	 */
	@Test
	public void testDetailTypeForInvalidGlcode() throws EGOVException {

		System.out.println("Testing by passing the incorrect glcode ");
		final String glcode = "3501002^^^^Testing";
		try {
			this.coa.getAccountdetailtypeListByGLCode(glcode);
			fail("should raise an  EGOVException");
		} catch (final EGOVException e) {
			assertEquals("glcode= " + glcode
					+ " is not present in ChartOfAccounts table",
					e.getMessage());
		}
	}

	/**
	 * @description - testing getAccountdetailtypeListByGLCode() method by
	 *              passing a glcode that does not have detail type objects.
	 * @throws EGOVException
	 */
	@Test
	public void testDetailTypeForGlcodeHavingNoDetailTypeEntry()
			throws EGOVException {
		final CChartOfAccounts c = createCOA("350178282", null);
		final List<Accountdetailtype> list = this.coa
				.getAccountdetailtypeListByGLCode(c.getGlcode());
		assertTrue(list.isEmpty());
	}

	public void testGetDetailTypeListByGlCode() throws EGOVException {
		System.out
				.println("Start | testGetDetailTypeListByGlCode | CommonsManager getDetailTypeListByGlCode()");
		final String glCode = createChartOfAccountsWithDetailType();
		final List<Accountdetailtype> list = comm
				.getDetailTypeListByGlCode(glCode);
		if (null != list) {
			assertNotNull(list);
			assertTrue(list.size() > 0);
			System.out.println("Number of detailtype object for the glcode="
					+ glCode + " is =" + list.size());
		} else {
			System.out
					.println("There is no detailtype object for the supplied glcode: "
							+ glCode);
		}
		System.out
				.println("End | testGetDetailTypeListByGlCode | CommonsManager getDetailTypeListByGlCode()");
	}

	protected String createChartOfAccountsWithDetailType()
			throws HibernateException {
		final CChartOfAccounts c = createCOA("35010021", null);
		final Accountdetailtype a1 = createAccountdetailtype("TestEmployee",
				"TestEmployee", "eg_employee", "Employee_id_test");
		final Accountdetailtype a2 = createAccountdetailtype("TestRelation",
				"TestRelation", "relation", "relation_id_test");
		createCOADetail(c, a1);
		createCOADetail(c, a2);
		return c.getGlcode();

	}

	protected CChartOfAccounts createCOA(final String glcode,
			final String purposeId) throws HibernateException {
		final CChartOfAccounts account = new CChartOfAccounts();
		cChartOfAccountsSer = new PersistenceService<CChartOfAccounts, Long>();
//		cChartOfAccountsSer.setSessionFactory((new SessionFactory()));
		cChartOfAccountsSer.setType(CChartOfAccounts.class);
		account.setGlcode(glcode);
		account.setName("COA Test");
		account.setIsActiveForPosting(1l);
		account.setCreatedDate(new Date());
		final PersistenceService persistenceService = new PersistenceService();
//		persistenceService.setSessionFactory(new SessionFactory());
		persistenceService.setType(UserImpl.class);
		account.setModifiedBy((User) persistenceService.findAll().get(0));
		account.setModifiedDate(new Date());
		account.setType('E');
		cChartOfAccountsSer.persist(account);
		return account;
	}

	protected Accountdetailtype createAccountdetailtype(final String name,
			final String desc, final String tableName,
			final String attributeName) throws HibernateException {
		final Accountdetailtype accountdetailtype = new Accountdetailtype();
		accountdetailtypeService = new PersistenceService<Accountdetailtype, Long>();
//		accountdetailtypeService.setSessionFactory(new SessionFactory());
		accountdetailtypeService.setType(Accountdetailtype.class);
		accountdetailtype.setName(name);
		accountdetailtype.setDescription(desc);
		accountdetailtype.setTablename(tableName);
		accountdetailtype.setColumnname("id");
		accountdetailtype.setAttributename(attributeName);
		accountdetailtype.setNbroflevels(new BigDecimal(1));
		accountdetailtype.setIsactive(true);
		accountdetailtype.setCreated(new Date());
		accountdetailtype.setModifiedby(new Long(1));
		accountdetailtype.setLastmodified(new Date());
		accountdetailtypeService.persist(accountdetailtype);
		return accountdetailtype;

	}

	protected CChartOfAccountDetail createCOADetail(final CChartOfAccounts c,
			final Accountdetailtype a) throws HibernateException {
		final CChartOfAccountDetail cChartOfAccountDetail = new CChartOfAccountDetail();
		cChartOfAccountDetailService = new PersistenceService<CChartOfAccountDetail, Long>();
//		cChartOfAccountDetailService.setSessionFactory(new SessionFactory());
		cChartOfAccountDetailService.setType(CChartOfAccountDetail.class);
		cChartOfAccountDetail.setGlCodeId(c);
		cChartOfAccountDetail.setDetailTypeId(a);
		cChartOfAccountDetailService.persist(cChartOfAccountDetail);
		return cChartOfAccountDetail;

	}

	public void testGetActiveAccountsForTypesTypeNull() {
		try {
			this.coa.getActiveAccountsForTypes(null);
		} catch (final ValidationException e) {
			assertEquals(
					"The supplied value for chartofaccount type  can not be null",
					e.getErrors().get(0).getMessage());
		}
	}

	public void testGetActiveAccountsForTypesTypeEmpty() {
		try {
			final char[] type = {};
			this.coa.getActiveAccountsForTypes(type);
		} catch (final ValidationException e) {
			assertEquals(
					"The supplied value for chartofaccount type  can not be empty",
					e.getErrors().get(0).getMessage());
		}
	}

	public void testGetActiveAccountsForTypes() {

		final char[] type = new char[5];
		type[0] = 'I';
		List<CChartOfAccounts> listChartOfAcc = this.coa
				.getActiveAccountsForTypes(type);
		LOGGER.debug("total number of chartofaccount objects  for type I :"
				+ listChartOfAcc.size());
		assertNotNull(listChartOfAcc);
		assertTrue(listChartOfAcc.size() > 0);
		type[1] = 'A';
		listChartOfAcc = this.coa.getActiveAccountsForTypes(type);
		LOGGER.debug("total number of chartofaccount objects  for type I ,A :"
				+ listChartOfAcc.size());
		assertNotNull(listChartOfAcc);
		assertTrue(listChartOfAcc.size() > 0);

	}

	public void testGetAccountCodeByListOfPurposeIdNull() {

		try {
			this.coa.getAccountCodeByListOfPurposeId(null);
		} catch (final ValidationException e) {
			assertEquals("the supplied purposeId  can not be null", e
					.getErrors().get(0).getMessage());
		}

	}

	public void testGetAccountCodeByListOfPurposeIdEmpty() {

		try {
			final Integer[] purposeId = {};
			this.coa.getAccountCodeByListOfPurposeId(purposeId);
		} catch (final ValidationException e) {
			assertEquals("the supplied purposeId  can not be empty", e
					.getErrors().get(0).getMessage());
		}

	}

	public void testGetAccountCodeByListOfPurposeId() {

		final Integer[] purposeId = { 2 };
		final List<CChartOfAccounts> listChartOfAcc = this.coa
				.getAccountCodeByListOfPurposeId(purposeId);
		LOGGER.debug("total number of chartofaccount objects : "
				+ listChartOfAcc.size());

	}

	@Test
	public void testGetListOfDetailCodeNull() {

		try {
			this.coa.getListOfDetailCode(null);
		} catch (final ValidationException e) {
			assertEquals("the glcode value supplied can not be null", e
					.getErrors().get(0).getMessage());
		}
	}

	@Test
	public void testGetListOfDetailCodeEmpty() {

		try {
			this.coa.getListOfDetailCode("   ");
		} catch (final ValidationException e) {
			assertEquals("the glcode value supplied can not be blank", e
					.getErrors().get(0).getMessage());
		}
	}

	@Test
	public void testGetListOfDetailCodeNotExist() {

		try {
			this.coa.getListOfDetailCode("BTR5U6");
		} catch (final ValidationException e) {
			assertEquals(
					"the glcode value supplied doesnot exist in the System", e
							.getErrors().get(0).getMessage());
		}
	}

	@Test
	public void testGetListOfDetailCode() {

		// Major ,Minor and subminor level glcode isactiveforposting value
		// should be ZERO.
		final CChartOfAccounts majorCOA = this.objectFactory.createCOA("A",
				"10", null, Long.valueOf(0), Long.valueOf(4),
				Character.valueOf('A'), "Testing");
		final CChartOfAccounts minorCOA = this.objectFactory.createCOA(
				majorCOA.getGlcode() + "50", "10", majorCOA.getId(),
				Long.valueOf(0), Long.valueOf(4), Character.valueOf('A'),
				"Testing");
		final CChartOfAccounts subminorCOA1 = this.objectFactory.createCOA(
				minorCOA.getGlcode() + "10", "10", minorCOA.getId(),
				Long.valueOf(0), Long.valueOf(4), Character.valueOf('A'),
				"Testing");
		final CChartOfAccounts detailCOA1 = this.objectFactory.createCOA(
				subminorCOA1.getGlcode() + "01", "10", subminorCOA1.getId(),
				Long.valueOf(1), Long.valueOf(4), Character.valueOf('A'),
				"Testing");
		final CChartOfAccounts detailCOA2 = this.objectFactory.createCOA(
				subminorCOA1.getGlcode() + "02", "10", subminorCOA1.getId(),
				Long.valueOf(1), Long.valueOf(4), Character.valueOf('A'),
				"Testing");

		final List<CChartOfAccounts> list1 = this.coa
				.getListOfDetailCode(detailCOA1.getGlcode());
		assertEquals(1, list1.size());

		final List<CChartOfAccounts> list2 = this.coa
				.getListOfDetailCode(detailCOA2.getGlcode());
		assertEquals(1, list2.size());

		final List<CChartOfAccounts> list3 = this.coa
				.getListOfDetailCode(subminorCOA1.getGlcode());
		assertEquals(2, list3.size());

		final List<CChartOfAccounts> list4 = this.coa
				.getListOfDetailCode(minorCOA.getGlcode());
		assertEquals(2, list4.size());

		final List<CChartOfAccounts> list5 = this.coa
				.getListOfDetailCode(majorCOA.getGlcode());
		assertEquals(2, list5.size());

	}

	@Test
	public void testGetBankChartofAccountCodeList() {
		createBankAccount();
		final ChartOfAccountsHibernateDAO coa = new ChartOfAccountsHibernateDAO(
				CChartOfAccounts.class, null);
//		HibernateUtil.getCurrentSession().clear();
		assertTrue(coa.getBankChartofAccountCodeList() != null);
	}

	private Bank createBank() {
		final PersistenceService bankService = new PersistenceService();
//		bankService.setSessionFactory(new SessionFactory());
		bankService.setType(Bank.class);
		final Bank bank = new Bank();
		bank.setCode("TestBank");
		bank.setCreated(new Date());
		bank.setIsactive(true);
		bank.setLastmodified(new Date());
		bank.setName("TestBank");
		bank.setNarration("");
		bank.setType("TestBank");
		bank.setModifiedby(new BigDecimal(getUser().getId()));
		return (Bank) bankService.persist(bank);
	}

	private Bankbranch createBankBranch() {
		final PersistenceService branchService = new PersistenceService();
//		branchService.setSessionFactory(new SessionFactory());
		branchService.setType(Bankbranch.class);
		final Bankbranch branch = new Bankbranch();
		branch.setBank(createBank());
		branch.setCreated(new Date());
		branch.setIsactive(true);
		branch.setLastmodified(new Date());
		branch.setBranchaddress1("Test");
		branch.setNarration("");
		branch.setBranchcode("123456");
		branch.setBranchname("test");
		branch.setModifiedby(new BigDecimal(getUser().getId()));
		return (Bankbranch) branchService.persist(branch);
	}

	private UserImpl getUser() {
		final PersistenceService persistenceService = new PersistenceService();
//		persistenceService.setSessionFactory(new SessionFactory());
		return ((UserImpl) persistenceService.findAllBy("from UserImpl").get(0));
	}

	private void createBankAccount() {
		final PersistenceService bankAccountService = new PersistenceService();
//		bankAccountService.setSessionFactory(new SessionFactory());
		bankAccountService.setType(Bankaccount.class);
		final Bankaccount bankaccount = new Bankaccount();
		bankaccount.setBankbranch(createBankBranch());
		bankaccount.setAccountnumber("123456789");
		bankaccount.setAccounttype("test");
		final PersistenceService persistenceService = new PersistenceService();
//		persistenceService.setSessionFactory(new SessionFactory());
		bankaccount.setChartofaccounts((CChartOfAccounts) persistenceService
				.findAllBy("from CChartOfAccounts").get(0));
		bankaccount.setCurrentbalance(BigDecimal.TEN);
		bankaccount.setIsactive(true);
		bankaccount.setCreated(new Date());
		bankaccount.setLastmodified(new Date());
		bankaccount.setModifiedby(new BigDecimal(getUser().getId()));
		bankAccountService.persist(bankaccount);
	}
}
