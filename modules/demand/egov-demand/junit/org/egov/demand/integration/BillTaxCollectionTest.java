package org.egov.demand.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.egov.InvalidAccountHeadException;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.demand.dao.DCBDaoFactory;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.dao.EgBillDetailsDao;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgBill;
import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.models.ReceiptDetail;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.models.ReceiptMisc;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.commons.Module;
import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BillTaxCollectionTest extends TaxCollection{
	private  EgovHibernateTest egovHibernateTest;

	@Before
	public void setUp() throws Exception {
		egovHibernateTest = new EgovHibernateTest();
		egovHibernateTest.setUp();

	}
	@After
	public void tearDown() throws Exception {
		egovHibernateTest.tearDown();
	}


	@Test
	public void linkBillToReceiptWithInputNull() throws InvalidAccountHeadException
	{
		/*BillReceipt billRcpt = linkBillToReceipt(null);
		assertNull(billRcpt);*/
	}

	/*@Test
	public void linkBillToReceiptWithNullBillReceiptInfo() throws InvalidAccountHeadException
	{
		User user = createUser("egovernments");
		BillReceipt billRcpt =linkBillToReceipt(null);
		assertNull(billRcpt);
	}

	@Test(expected=InvalidAccountHeadException.class)
	public void linkBillToReceiptWithCompleteBillReceiptInfoObject() throws InvalidAccountHeadException
	{
		BillReceiptInfo billRcptInfo = new BillReceiptInfo(createReceiptHeaderWithWrongGlCode("receiptNumber"));
		User user = createUser("egovernments");
		BillReceipt billRcpt = linkBillToReceipt(billRcptInfo);
		assertEquals("receiptnumber",billRcpt.getReceiptNumber());
	}

	@Test
	public void updateBillDetailsWithInputNull() throws InvalidAccountHeadException
	{
		EgBill bill = updateBillDetails(null);
		assertNull(bill);
	}

	@Test
	public void updateBillDetailsWithCompleteBillReceiptInfoObject() throws InvalidAccountHeadException
	{
		BillReceiptInfo billRcptInfo = new BillReceiptInfo(createReceiptHeader("receiptNumber"));
		EgBill bill = updateBillDetails(billRcptInfo);
		assertEquals("billNo",bill.getBillNo());
	}

	@Test(expected=InvalidAccountHeadException.class)
	public void updateBillDetailsWithInvalidGlCode() throws InvalidAccountHeadException
	{
		BillReceiptInfo billRcptInfo = new BillReceiptInfo(createReceiptHeaderWithWrongGlCode("receiptNumber"));
		updateBillDetails(billRcptInfo);
	}

	@Test
	public void calculateTotalCollectedAmtWithNullInput() throws InvalidAccountHeadException
	{
		BigDecimal totalCollAmt = calculateTotalCollectedAmt(null, null);
		assertEquals(0,totalCollAmt);
	}

	@Test
	public void calculateTotalCollectedAmtWithNullBillReceiptInfoInput() throws InvalidAccountHeadException
	{
		EgBillDao billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
		EgBillDetailsDao billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		EgBill bill = (EgBill)billDao.findById(Long.valueOf(1), true);
		BigDecimal totalCollAmt = calculateTotalCollectedAmt(null, billDetDao.getBillDetailsByBill(bill));
		assertEquals(0,totalCollAmt);
	}

	@Test
	public void calculateTotalCollectedAmtWithNullBillDetailsListInput() throws InvalidAccountHeadException
	{
		BillReceiptInfo billRcptInfo = new BillReceiptInfo(createReceiptHeader("receiptNumber"));
		BigDecimal totalCollAmt = calculateTotalCollectedAmt(billRcptInfo, null);
		assertEquals(0,totalCollAmt);
	}

	@Test
	public void calculateTotalCollectedAmtWithValidInput() throws InvalidAccountHeadException
	{
		BillReceiptInfo billRcptInfo = new BillReceiptInfo(createReceiptHeader("receiptNumber"));
		EgBillDao billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
		EgBillDetailsDao billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		EgBill bill = (EgBill)billDao.findById(Long.valueOf(1), true);
		BigDecimal totalCollAmt = calculateTotalCollectedAmt(billRcptInfo, billDetDao.getBillDetailsByBill(bill));
		 assertSame(true,totalCollAmt.compareTo(BigDecimal.ZERO)>0);
	}

	@Test(expected=InvalidAccountHeadException.class)
	public void calculateTotalCollectedAmtWithInvalidGlCode() throws InvalidAccountHeadException
	{
		BillReceiptInfo billRcptInfo = new BillReceiptInfo(createReceiptHeaderWithWrongGlCode("receiptNumber"));
		EgBillDao billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
		EgBillDetailsDao billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		EgBill bill = (EgBill)billDao.findById(Long.valueOf(1), true);
		calculateTotalCollectedAmt(billRcptInfo, billDetDao.getBillDetailsByBill(bill));
	}


    public ReceiptHeader createReceiptHeader(String receiptnumber) {
        return createReceiptHeader(receiptnumber,
                CollectionConstants.RECEIPT_TYPE_BILL, "30", "testCode",
                createUser("egovernments"));
    }

    public ReceiptHeader createReceiptHeaderWithWrongGlCode(String receiptnumber) {
        return createReceiptHeaderWithWrongGlCode(receiptnumber,
                CollectionConstants.RECEIPT_TYPE_BILL, "30", "testCode",
                createUser("egovernments"));
    }

    public ReceiptHeader createReceiptHeader(String receiptnumber, char receiptType, String refNum, String statusCode, User user)
    {
        Set<ReceiptDetail> receiptDetails = new HashSet();
        ReceiptHeader receiptHeader = createUnsavedReceiptHeader(receiptnumber, receiptType, refNum, statusCode, user);
        receiptHeader.setReceiptMisc(createUnSavedReceiptMisc(receiptHeader));
        receiptDetails.add(createReceiptDetailWithoutHeader("431100100"));
        receiptDetails.add(createReceiptDetailWithoutHeader("431101300"));
        receiptHeader.setReceiptDetails(receiptDetails);
        return receiptHeader;
    }

    public ReceiptHeader createReceiptHeaderWithWrongGlCode(String receiptnumber, char receiptType, String refNum, String statusCode, User user)
    {
        Set<ReceiptDetail> receiptDetails = new HashSet();
        ReceiptHeader receiptHeader = createUnsavedReceiptHeader(receiptnumber, receiptType, refNum, statusCode, user);
        receiptHeader.setReceiptMisc(createUnSavedReceiptMisc(receiptHeader));
        receiptDetails.add(createReceiptDetailWithoutHeaderWithWrongGlCode());
        receiptHeader.setReceiptDetails(receiptDetails);
        return receiptHeader;
    }

    public UserImpl createUser(String userName) {
        UserImpl user = new UserImpl();
        user.setUserName(userName + getRandomNumber());
        user.setFirstName(userName);
        user.setPwd("testpassword");
        user.setIsActive(1);
        return user;
    }

    public ReceiptHeader createUnsavedReceiptHeader(String receiptnumber,
            char receiptType, String refNum, String statusCode, User user) {
        ReceiptHeader receiptHeader = new ReceiptHeader();
        receiptHeader.setReceipttype(receiptType);
        receiptHeader.setReceiptnumber(receiptnumber);
        receiptHeader.setReferencenumber(refNum);
        receiptHeader.setCreatedDate(new Date());
        receiptHeader.setModifiedDate(new Date());
        receiptHeader.setCreatedBy(user);
        receiptHeader.setModifiedBy(user);
        receiptHeader.setIsReconciled(false);
        receiptHeader.setEgwStatus(createEgwStatus(statusCode,CollectionConstants.MODULE_NAME_RECEIPTHEADER));
        receiptHeader.setReceiptDetails(null);
        return receiptHeader;
    }

    public ReceiptMisc createUnSavedReceiptMisc(ReceiptHeader rcptHeader)
    {
        ReceiptMisc rcptMisc = new ReceiptMisc();
        rcptMisc.setReceiptHeader(rcptHeader);

        return rcptMisc;
    }

    public ReceiptDetail createReceiptDetailWithoutHeader(String glCode) {
        ReceiptDetail receiptDetail = new ReceiptDetail();

        receiptDetail.setAccounthead(createCOA(glCode));
        receiptDetail.setCramount(BigDecimal.valueOf(100));
        receiptDetail.setDramount(BigDecimal.valueOf(20));
        receiptDetail.setOrdernumber(Long.valueOf(1));
        receiptDetail.setFunction(createFunction("testFunction"));
        return receiptDetail;
    }

    public ReceiptDetail createReceiptDetailWithoutHeaderWithWrongGlCode() {
        ReceiptDetail receiptDetail = new ReceiptDetail();

        receiptDetail.setAccounthead(createCOA("testGLCode"));
        receiptDetail.setCramount(BigDecimal.valueOf(100));
        receiptDetail.setDramount(BigDecimal.valueOf(20));
        receiptDetail.setOrdernumber(Long.valueOf(1));
        receiptDetail.setFunction(createFunction("testFunction"));
        return receiptDetail;
    }

    public CChartOfAccounts createCOA(String glcode)
    {
        CChartOfAccounts coa = new CChartOfAccounts();
        coa.setGlcode(glcode);
        coa.setName("chart");

        return coa;
    }

    public CFunction createFunction(String function)
    {
        CFunction func = new CFunction();
        func.setName(function);
        func.setCode("code");
        func.setIsActive(1);
        func.setType("type");

        return func;
    }

    public EgwStatus createEgwStatus(String testStatusCode, String TestInstrumentHeader)
    {
        EgwStatus status = new EgwStatus();
        status.setCode("testStatusCode");
        status.setDescription("description");
        status.setId(getRandomNumber());
        status.setModuletype("moduletype");
        status.setOrderId("1");

        return status;
    }

    public Integer getRandomNumber()
    {
        Random random = new Random();
        return random.nextInt();
    }*/

    @Override
    public void updateDemandDetails(BillReceiptInfo bri) {
        // TODO Auto-generated method stub

    }
    @Override
    public Module module() {
        return null;
    }
}
