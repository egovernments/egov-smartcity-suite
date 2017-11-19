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
package org.egov.collection.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.integration.pgi.DefaultPaymentResponse;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.entity.BankAccountServiceMap;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.entity.Location;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentAccountCodes;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.egov.model.instrument.InstrumentType;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.elasticsearch.repositories.RepositoryException;
import org.hibernate.Session;

/**
 * 
 */
public class CollectionObjectFactory {
	private final Session session;
	private PersistenceService service;

	public static final String MODULE_NAME_TESTRECEIPTHEADER = "TestReceiptHeader";
	public static final String MODULE_NAME_TESTINSTRUMENTSTATUS = "TestInstrumentStatus";
	public static final String MODULE_NAME_TESTCHALLANHEADER = "TestChallanHeader";
	
	public CollectionObjectFactory(){
		this.session = null;
	}

	public CollectionObjectFactory(Session session) {
		this.session = session;
	}
	
	public CollectionObjectFactory(Session session,PersistenceService service) {
		this.session = session;
		this.service = service;
	}
	
	public int getRandomNumber() {
		Random ran = new Random();
		return ran.nextInt();
	}

	public int getRandomNumber(int max) {
		Random ran = new Random();
		return ran.nextInt(max);
	}

	public ServiceDetails createUnsavedServiceDetails() {
		ServiceDetails service = new ServiceDetails();
		String serviceName = "!testSrvc#" + getRandomNumber(9999);
		service.setName(serviceName);
		service.setServiceUrl("testServiceURL");
		service.setServiceType("B");
		service.setCode("testCode");
		service.setVoucherCreation(Boolean.TRUE);
		service.setIsVoucherApproved(Boolean.TRUE);
		service.addBankAccountServiceMap(createBankAccountServiceMap(service));
		return service;
	}

	public BankAccountServiceMap createBankAccountServiceMap(ServiceDetails serviceDetails)
	{
		BankAccountServiceMap bankServ=new BankAccountServiceMap();
		bankServ.setServiceDetails(serviceDetails);
		bankServ.setBankAccountId(createBankAccount("$" + serviceDetails.getCode() + "%"));
		bankServ.setDeptId(createDept("testDeptName", "testDeptCode"));
		bankServ.setLastModifiedBy(createUser("system"));
		bankServ.setLastModifiedDate(new Date());
		bankServ.setCreatedBy(createUser("system"));
		bankServ.setCreatedDate(new Date());
		return  (bankServ);
	}
	
	public Bankaccount createBankAccount(String glCode) {
		Bankaccount bankaccount = new Bankaccount();
		bankaccount.setAccountnumber("123456789");
		bankaccount.setAccounttype("NATIONALISED BANKS");
		bankaccount.setIsactive(true);
		bankaccount.setFund(createFund("testFund"));
		bankaccount.setChartofaccounts(createCOA(this.getRandomNumber(99999999)+""));
		bankaccount.setBankbranch(createBankBranch());
		session.saveOrUpdate(bankaccount);
		return bankaccount;
	}

	public Bankbranch createBankBranch() {
		Bankbranch bankbranch = new Bankbranch();
		bankbranch.setBranchcode("10101");
		bankbranch.setBranchname("test  branch");
		bankbranch.setBranchaddress1("test branch address");
		bankbranch.setBranchcity("branch city");
		bankbranch.setIsactive(true);
		bankbranch.setBank(createBank());
		bankbranch.setCreatedDate(new Date());
		bankbranch.setCreatedBy(createUser("system"));
		bankbranch.setLastModifiedDate(new Date());
		bankbranch.setLastModifiedBy(createUser("system"));
		session.saveOrUpdate(bankbranch);
		return bankbranch;
	}

	public Bank createBank() {
		Bank bank = new Bank();
		bank.setCode("TEST" + getRandomNumber());
		bank.setName("Test Bank" + getRandomNumber());
		bank.setIsactive(true);
		bank.setCreatedDate(new Date());
		bank.setCreatedBy(createUser("system"));
		bank.setLastModifiedDate(new Date());
		bank.setLastModifiedBy(createUser("system"));
		session.saveOrUpdate(bank);
		return bank;
	}

	public ServiceDetails createServiceDetails() throws NumberFormatException {
		ServiceDetails service = createUnsavedServiceDetails();
		session.saveOrUpdate(service);
		return service;
	}

	public ServiceDetails createServiceDetails(String code) {
		ServiceDetails service = new ServiceDetails();
		service.setName("testServiceName");
		service.setServiceUrl("testServiceURL");
		service.setServiceType("B");
		service.setCode(code);
		service.addBankAccountServiceMap(createBankAccountServiceMap(service));
		session.saveOrUpdate(service);
		return service;
	}

	public ReceiptHeader createReceiptHeader(String receiptnumber) throws NumberFormatException {
		return createReceiptHeader(receiptnumber,
				CollectionConstants.RECEIPT_TYPE_BILL, "123456", "testCode",
				createUser("system"),null);
	}

	/**
	 * Creates a receipt header with given details
	 * 
	 * @param receiptnumber
	 *            Receipt number
	 * @param receiptType
	 *            Receipt type
	 * @param refNum
	 *            Receipt (bill) reference number
	 * @param statusCode
	 *            Receipt status code
	 * @return Receipt header created with given details
	 * @throws RepositoryException 
	 * @throws NumberFormatException 
	 */
	public ReceiptHeader createUnsavedReceiptHeader(String receiptnumber,
			char receiptType, String refNum, String statusCode, User user,EgwStatus status) throws NumberFormatException {
		ReceiptHeader receiptHeader = new ReceiptHeader();
		receiptHeader.setReceipttype(receiptType);
		if(receiptnumber != null){
			receiptHeader.setReceiptnumber(receiptnumber+getRandomNumber());
		}	
		receiptHeader.setReferencenumber(refNum);
		receiptHeader.setConsumerCode("10-10-111-20");
		receiptHeader.setService(createServiceDetails());
		receiptHeader.setCreatedDate(new Date());
		receiptHeader.setLastModifiedDate(new Date());
		receiptHeader.setCreatedBy(user);
		receiptHeader.setLastModifiedBy(user);
		receiptHeader.setIsReconciled(false);
		receiptHeader.setManualreceiptnumber("292929");
		if(status==null){
			receiptHeader.setStatus(createEgwStatus(statusCode,
				CollectionConstants.MODULE_NAME_RECEIPTHEADER));
		}
		else{
			receiptHeader.setStatus(status);
		}
		receiptHeader.setPaidBy("Test Payee");
		receiptHeader.setReceiptMisc(createUnSavedReceiptMisc(receiptHeader));
		receiptHeader.addReceiptDetail(createUnsavedReceiptDetail(createCOA("1100201"),
				BigDecimal.valueOf(100.00), BigDecimal.ZERO, createFunction("Test Function"), 1L,
				"testGLCodeDescription", receiptHeader,true));

		return receiptHeader;
	}

	/**
	 * Creates a receipt header with given details
	 * 
	 * @param receiptnumber
	 *            Receipt number
	 * @param receiptType
	 *            Receipt type
	 * @param refNum
	 *            Receipt (bill) reference number
	 * @param statusCode
	 *            Receipt status code
	 * @return Receipt header created with given details
	 * @throws RepositoryException 
	 * @throws NumberFormatException 
	 */
	public ReceiptHeader createReceiptHeader(String receiptnumber,
			char receiptType, String refNum, String statusCode, User user, EgwStatus status) throws NumberFormatException {
		ReceiptHeader receiptHeader = createUnsavedReceiptHeader(receiptnumber,
				receiptType, refNum, statusCode, user,status);
		session.saveOrUpdate(receiptHeader);
		return receiptHeader;
	}

	private Position createUnsavedPosition() {
		Position pos = new Position();
		pos.setName("TestPos");
		return pos;
	}

	public Position createPosition() {
		Position pos = createUnsavedPosition();
		session.saveOrUpdate(pos);
		return pos;
	}

	/*public State createState(User user) {
		State state = new State("ReceiptHeader",
				CollectionConstants.WF_STATE_RECEIPT_CREATED, createPosition(),
				"Test");
		state.setCreatedBy(user);
		state.setModifiedBy(user);
		state.setCreatedDate(new Date());
		state.setModifiedDate(new Date());
		state.setValue(CollectionConstants.WF_STATE_RECEIPT_CREATED);
		session.saveOrUpdate(state);
		return state;
	}
	
	public State createState(String type,String value) {
		State state = new State(type,
				value, createPosition(),
				"Test");
		User user = createUser("testUser");
		state.setCreatedBy(user);
		state.setModifiedBy(user);
		state.setCreatedDate(new Date());
		state.setModifiedDate(new Date());
		session.saveOrUpdate(state);
		return state;
	}*/

	/**
	 * Creates a receipt header along with associated instrument header attached
	 * to it
	 * 
	 * @param receiptNum
	 * @param receiptType
	 * @param statusCode
	 * @param refNum
	 * @param instrumentType
	 * @param instrumentNum
	 * @param instrumentAmount
	 * @param instrumentDate
	 * @param instrumentStatusCode
	 * @param glCode
	 * @param functionName
	 * @param userName
	 * @return Receipt header created using given details
	 * @throws RepositoryException 
	 * @throws NumberFormatException 
	 */
	public ReceiptHeader createReceiptHeaderWithInstrument(String receiptNum,
			char receiptType, String statusCode, String refNum,
			InstrumentType instrumentType, String instrumentNum,
			Double instrumentAmount, Date instrumentDate,
			String instrumentStatusCode, String glCode, String functionName,
			String userName) throws NumberFormatException {
		return createReceiptHeaderWithInstrument(receiptNum, receiptType,
				statusCode, refNum, instrumentType, instrumentNum,
				instrumentAmount, instrumentDate, instrumentStatusCode, glCode,
				functionName, userName, "testCounter");
	}

	/**
	 * Creates a receipt header along with associated instrument header attached
	 * to it
	 * 
	 * @param receiptNum
	 * @param receiptType
	 * @param statusCode
	 * @param refNum
	 * @param instrumentType
	 * @param instrumentNum
	 * @param instrumentAmount
	 * @param instrumentDate
	 * @param instrumentStatusCode
	 * @param glCode
	 * @param functionName
	 * @param userName
	 * @param counterName
	 * @return Receipt header created using given details
	 * @throws RepositoryException 
	 * @throws NumberFormatException 
	 */
	public ReceiptHeader createReceiptHeaderWithInstrument(String receiptNum,
			char receiptType, String statusCode, String refNum,
			InstrumentType instrumentType, String instrumentNum,
			Double instrumentAmount, Date instrumentDate,
			String instrumentStatusCode, String glCode, String functionName,
			String userName, String counterName) throws NumberFormatException {

		User user = createUser(userName);
		Location counter = createCounter(counterName);
		return createReceiptHeaderWithInstrument(receiptNum, receiptType,
				statusCode, refNum, instrumentType, instrumentNum,
				instrumentAmount, instrumentDate, instrumentStatusCode, glCode,
				functionName, user, counter);
	}

	/**
	 * Creates a receipt header along with associated instrument header attached
	 * to it
	 * 
	 * @param receiptNum
	 * @param receiptType
	 * @param statusCode
	 * @param refNum
	 * @param instrumentType
	 * @param instrumentNum
	 * @param instrumentAmount
	 * @param instrumentDate
	 * @param instrumentStatusCode
	 * @param glCode
	 * @param functionName
	 * @param user
	 * @param counter
	 * @return Receipt header created using given details
	 * @throws RepositoryException 
	 * @throws NumberFormatException 
	 */
	public ReceiptHeader createReceiptHeaderWithInstrument(String receiptNum,
			char receiptType, String statusCode, String refNum,
			InstrumentType instrumentType, String instrumentNum,
			Double instrumentAmount, Date instrumentDate,
			String instrumentStatusCode, String glCode, String functionName,
			User user, Location counter) throws NumberFormatException {
		// Create employee for user
		PersonalInformation emp = createPersonalInformation(user,
				createDept("testDepartment"));
		/*createEmployeePositionDetails("testDesignation", emp,
				createDept("testDept"));*/

		// Create chart of accounts
		CChartOfAccounts coaObj = createCOA(glCode);
		CFunction functionObj = createFunction(functionName);

		// Create basic receipt header
		ReceiptHeader receiptHeader = createUnsavedReceiptHeader(receiptNum,
				receiptType, refNum, statusCode, user,null);
		receiptHeader.setReceiptMisc(createUnSavedReceiptMisc(receiptHeader));

		// Add given instrument
		receiptHeader.addInstrument(createInstrumentHeader(instrumentType,
				instrumentNum, instrumentAmount, instrumentDate,
				instrumentStatusCode, user));

		// Add payee details
		//receiptHeader.setReceiptPayeeDetails(createReceiptPayeeDetails());

		// Add receipt detail
		/*ReceiptDetail receiptDetail = createUnsavedReceiptDetail(coaObj,
				BigDecimal.valueOf(instrumentAmount), BigDecimal.ZERO, functionObj, 1L,
				"testGLCodeDescription", receiptHeader,1L);
		receiptDetail.setCramountToBePaid(receiptDetail.getCramount());
		receiptHeader.addReceiptDetail(receiptDetail);*/

		// Set the location (counter)
		receiptHeader.setLocation(counter);

		// Create empty receipt voucher mapping
		ReceiptVoucher receiptVoucher = new ReceiptVoucher();
		receiptVoucher.setReceiptHeader(receiptHeader);
		receiptHeader.addReceiptVoucher(receiptVoucher);
		//receiptHeader.setState(this.createState("test","test"));
		// Save the receipt header
		session.saveOrUpdate(receiptHeader);

		return receiptHeader;
	}

	public ReceiptHeader createUnsavedReceiptHeader() throws NumberFormatException {
		ReceiptHeader receiptHeader = new ReceiptHeader();
		receiptHeader.setReceipttype('A');
		receiptHeader.setService(createServiceDetails());
		receiptHeader.setCreatedDate(new Date());
		receiptHeader.setIsReconciled(false);
		receiptHeader.setStatus(createEgwStatus(
				("testCodeRH" + getRandomNumber()).substring(0, 10),
				MODULE_NAME_TESTRECEIPTHEADER));
		receiptHeader.setReceiptnumber("testReceiptNumber"+getRandomNumber());
		receiptHeader.setPaidBy("Test Payee");
		receiptHeader.setOverrideAccountHeads(true);
		receiptHeader.setPartPaymentAllowed(true);
		receiptHeader.setCallbackForApportioning(false);
		receiptHeader.setConsumerCode("testConsumerCode");
		receiptHeader.setReceiptMisc(createUnSavedReceiptMisc(receiptHeader));
		receiptHeader.addReceiptDetail(createUnsavedReceiptDetail(createCOA("1100201"),
				BigDecimal.valueOf(100.00), BigDecimal.ZERO, createFunction("Test Function"), 1L,
				"testGLCodeDescription", receiptHeader,true));
		return receiptHeader;
	}
	
	public ReceiptHeader createUnsavedPendingReceiptHeader() throws NumberFormatException {
		ReceiptHeader receiptHeader = new ReceiptHeader();
		receiptHeader.setReceipttype('A');
		receiptHeader.setService(createServiceDetails());
		receiptHeader.setCreatedDate(new Date());
		receiptHeader.setIsReconciled(false);
		receiptHeader.setStatus(createEgwStatus(
				("testCodeRH" + getRandomNumber()).substring(0, 10),
				MODULE_NAME_TESTRECEIPTHEADER));
		//receiptHeader.setReceiptnumber(null);
		receiptHeader.setPaidBy("Test Payee");
		receiptHeader.setOverrideAccountHeads(true);
		receiptHeader.setPartPaymentAllowed(true);
		receiptHeader.setConsumerCode("testConsumerCode");
		receiptHeader.setReceiptMisc(createUnSavedReceiptMisc(receiptHeader));
		receiptHeader.addReceiptDetail(createUnsavedReceiptDetail(createCOA("1100201"),
				BigDecimal.valueOf(100.00), BigDecimal.ZERO, createFunction("Test Function"), 1L,
				"testGLCodeDescription", receiptHeader,true));
		return receiptHeader;
	}

	public CFunction createFunction(String name) {
		CFunction function = new CFunction();
		function.setName(name);
		function.setCode(name + getRandomNumber());
		function.setIsActive(true);
		function.setIsNotLeaf(false);
		///function.setLevel(1);
		function.setType(name);
		session.saveOrUpdate(function);
		return function;
	}

	public CFunction createFunction(String name, String code) {
		CFunction function = new CFunction();
		function.setName(name);
		function.setCode(code);
		function.setIsActive(true);
		function.setIsNotLeaf(false);
		//function.setLevel(1);
		function.setType(name);
		session.saveOrUpdate(function);
		return function;
	}

	public CChartOfAccounts createCOA(String glCode) {
		CChartOfAccounts account = new CChartOfAccounts();
		User user = createUser("testUser");
		Date date = new Date();
		account.setGlcode(glCode + getRandomNumber(9999));
		account.setName("testAccountName" + glCode);
		account.setIsActiveForPosting(true);
		account.setCreatedBy(user);
		account.setLastModifiedBy(user);
		account.setCreatedDate(date);
		account.setLastModifiedDate(date);
		account.setType('I');
		session.saveOrUpdate(account);
		return account;
	}

	public EgwStatus createUnsavedEgwStatus(String code, String moduleType) {
		EgwStatus status = new EgwStatus();
		status.setModuletype(moduleType);
		status.setLastmodifieddate(new Date());
		status.setCode(code);
		status.setDescription(code);
		return status;
	}

	public EgwStatus createEgwStatus(String code, String moduleType) {
		EgwStatus status = createUnsavedEgwStatus(code, moduleType);
		session.saveOrUpdate(status);
		return status;
	}

	public Location createCounter(String counterName) {
		Location counter = new Location();
		counter.setActive(true);
		counter.setName(counterName);
		counter.setDescription("testCounterDesc");
		session.saveOrUpdate(counter);
		return counter;
	}


	public User createUser(String userName) {
		User user = new User();
		user.setName(userName + getRandomNumber());
		//user.setFirstName(userName);
		user.setPassword("testpassword");
		//user.setPwdModifiedDate(new Date());
		user.setActive(true);
		session.saveOrUpdate(user);
		return user;
	}
	
	/*public User createUser(String userName, Department dept) {
		User user = new User();
		user.setName(userName + getRandomNumber());
		user.setFirstName(userName);
		user.setPassword("testpassword");
        user.setPwdModifiedDate(new Date());
		user.setIsActive(1);
		user.setDepartment(dept);
		session.saveOrUpdate(user);
		return user;
	}*/

	public InstrumentType createUnsavedInstrumentType(String type) {
		InstrumentType instrType = new InstrumentType();

		User user = createUser("system");
		Date date = new Date();
		instrType.setType(type + getRandomNumber());
		instrType.setCreatedBy(user);
		instrType.setCreatedDate(date);
		instrType.setModifiedBy(user);
		instrType.setModifiedDate(date);

		return instrType;
	}

	public InstrumentType createInstrumentType(String type) {
		InstrumentType instrType = createUnsavedInstrumentType(type);
		session.saveOrUpdate(instrType);
		return instrType;
	}

	public InstrumentType createUnsavedRegularInstrumentType(String type) {
		InstrumentType instrType = new InstrumentType();

		User user = createUser("system");
		Date date = new Date();
		instrType.setType(type);
		instrType.setCreatedBy(user);
		instrType.setCreatedDate(date);
		instrType.setModifiedBy(user);
		instrType.setModifiedDate(date);
		// session.saveOrUpdate(instrType);

		return instrType;
	}

	public InstrumentType createRegularInstrumentType(String type) {
		InstrumentType instrType = createUnsavedRegularInstrumentType(type);
		session.saveOrUpdate(instrType);
		return instrType;
	}

	public InstrumentHeader createUnsavedInstrumentHeader(
			InstrumentType instrumentType, EgwStatus status) {
		InstrumentHeader instrHdr = new InstrumentHeader();
		instrHdr.setInstrumentAmount(BigDecimal.valueOf(1000));
		instrHdr.setStatusId(status);
		instrHdr.setInstrumentType(instrumentType);

		User user = createUser("testUser");
		instrHdr.setCreatedBy(user);
		instrHdr.setCreatedDate(new Date());
		instrHdr.setCreatedDate(new Date());
		instrHdr.setModifiedBy(user);
		instrHdr.setModifiedDate(new Date());
		return instrHdr;
	}

	public InstrumentHeader createUnsavedInstrumentHeader(
			String instrumentType, String status) {
		InstrumentHeader instrHdr = new InstrumentHeader();
		instrHdr.setInstrumentAmount(BigDecimal.valueOf(1000));
		instrHdr.setStatusId(createEgwStatus(status,
				CollectionConstants.MODULE_NAME_RECEIPTHEADER));
		instrHdr.setInstrumentType(createUnsavedInstrumentType(instrumentType));

		User user = createUser("testUser");
		instrHdr.setCreatedBy(user);
		instrHdr.setCreatedDate(new Date());
		instrHdr.setCreatedDate(new Date());
		instrHdr.setModifiedBy(user);
		instrHdr.setModifiedDate(new Date());
		return instrHdr;
	}

	/**
	 * TODO: Check if this can be removed and the subsequent
	 * createInstrumentHeader method can be used in its place
	 * 
	 * @param instrumentType
	 * @return
	 */
	public InstrumentHeader createInstrumentHeader(
			InstrumentType instrumentType, EgwStatus status) {
		InstrumentHeader instrHdr = new InstrumentHeader();
		instrHdr.setInstrumentAmount(BigDecimal.valueOf(1000));
		instrHdr.setStatusId(status);
		instrHdr.setInstrumentType(instrumentType);
		
		User user = createUser("testUser");
		instrHdr.setCreatedBy(user);
		instrHdr.setCreatedDate(new Date());
		instrHdr.setCreatedDate(new Date());
		instrHdr.setModifiedBy(user);
		instrHdr.setModifiedDate(new Date());
		session.saveOrUpdate(instrHdr);
		return instrHdr;
	}
	
	/**
	 * TODO: Check if this can be removed and the subsequent
	 * createInstrumentHeader method can be used in its place
	 * 
	 * @param instrumentType
	 * @return
	 * @throws RepositoryException 
	 * @throws NumberFormatException 
	 */
	public InstrumentHeader createBankInstrumentHeader() throws NumberFormatException {
		InstrumentHeader instrHdr = new InstrumentHeader();
		instrHdr.setInstrumentAmount(BigDecimal.valueOf(1000));
		instrHdr.setStatusId(createEgwStatus("testStatus", MODULE_NAME_TESTRECEIPTHEADER));
		
		instrHdr.setInstrumentType((InstrumentType) service.find("from InstrumentType where type=?",
				CollectionConstants.INSTRUMENTTYPE_BANK));
		
		instrHdr.setBankAccountId(createBankAccount("testGLCode"));
		instrHdr.setBankId(instrHdr.getBankAccountId().getBankbranch().getBank());
		instrHdr.setBankBranchName(instrHdr.getBankAccountId().getBankbranch().getBranchname());
		instrHdr.setTransactionNumber("123456");//(BigDecimal.valueOf(1000));
		instrHdr.setIsPayCheque(CollectionConstants.ZERO_INT);
		instrHdr.setTransactionDate(new Date());
		
		User user = createUser("testUser");
		instrHdr.setCreatedBy(user);
		instrHdr.setCreatedDate(new Date());
		instrHdr.setCreatedDate(new Date());
		instrHdr.setModifiedBy(user);
		instrHdr.setModifiedDate(new Date());
		session.saveOrUpdate(instrHdr);
		return instrHdr;
	}

	/**
	 * TODO: Check if this can be removed and the subsequent
	 * createInstrumentHeader method can be used in its place
	 * 
	 * @param instrumentType
	 * @return
	 */
	/*
	 * public InstrumentHeader createUnSavedInstrumentHeader(InstrumentType
	 * instrumentType,EgwStatus status){ InstrumentHeader instrHdr = new
	 * InstrumentHeader(); instrHdr.setInstrumentAmount(new Double(1000));
	 * instrHdr.setStatusId(status); instrHdr.setInstrumentType(instrumentType);
	 * 
	 * User user = createUser("testUser"); instrHdr.setCreatedBy(user);
	 * instrHdr.setCreatedDate(new Date()); instrHdr.setCreatedDate(new Date());
	 * instrHdr.setModifiedBy(user); instrHdr.setModifiedDate(new Date());
	 * session.saveOrUpdate(instrHdr); return instrHdr; }
	 */

	/**
	 * Creates instrument header with given details
	 * 
	 * @param instrumentType
	 *            Instrument type object
	 * @param instrumentNum
	 *            Instrument number
	 * @param instrumentAmount
	 *            Instrument amount
	 * @param statusCode
	 *            Instrument status code
	 * @param user
	 *            User creating/modifying the object
	 * @return Instrument header object created using given details
	 */
	public InstrumentHeader createUnsavedInstrumentHeader(
			InstrumentType instrumentType, String instrumentNum,
			Double instrumentAmount, Date instrumentDate, String statusCode,
			User user) {
		InstrumentHeader instrHdr = new InstrumentHeader();
		instrHdr.setInstrumentNumber(instrumentNum);
		instrHdr.setInstrumentType(instrumentType);
		instrHdr.setInstrumentAmount(BigDecimal.valueOf(instrumentAmount));
		instrHdr.setInstrumentDate(instrumentDate);
		instrHdr.setStatusId(createEgwStatus(statusCode,
				CollectionConstants.MODULE_NAME_RECEIPTHEADER));
		instrHdr.setCreatedDate(new Date());
		instrHdr.setModifiedDate(new Date());
		instrHdr.setCreatedBy(user);
		instrHdr.setModifiedBy(user);
		return instrHdr;
	}

	public InstrumentHeader createInstrumentHeaderWithBankDetails(
			InstrumentType instrumentType, String instrumentNum,
			Double instrumentAmount, Date instrumentDate, EgwStatus status,
			Bank bank, String branchName, String isPayCheck) {
		InstrumentHeader instrHdr = createUnsavedInstrumentHeaderWithBankDetails(
				instrumentType, instrumentNum, instrumentAmount,
				instrumentDate, status, bank, branchName, isPayCheck);

		session.saveOrUpdate(instrHdr);
		return instrHdr;
	}

	public InstrumentHeader createUnsavedInstrumentHeaderWithBankDetails(
			InstrumentType instrumentType, String instrumentNum,
			Double instrumentAmount, Date instrumentDate, EgwStatus status,
			Bank bank, String branchName, String isPayCheck) {
		InstrumentHeader instrHdr = new InstrumentHeader();
		instrHdr.setInstrumentNumber(instrumentNum);
		instrHdr.setInstrumentType(instrumentType);
		instrHdr.setInstrumentAmount(BigDecimal.valueOf(instrumentAmount));
		instrHdr.setStatusId(status);
		instrHdr.setBankId(bank);
		instrHdr.setBankBranchName(branchName);
		instrHdr.setIsPayCheque(isPayCheck);
		instrHdr.setInstrumentDate(instrumentDate);

		User user = createUser("testUserInstrumentHdr");
		instrHdr.setCreatedBy(user);
		instrHdr.setModifiedBy(user);
		instrHdr.setCreatedDate(new Date());
		instrHdr.setModifiedDate(new Date());

		return instrHdr;
	}

	/**
	 * Creates instrument header with given details
	 * 
	 * @param instrumentType
	 *            Instrument type object
	 * @param instrumentNum
	 *            Instrument number
	 * @param instrumentAmount
	 *            Instrument amount
	 * @param statusCode
	 *            Instrument status code
	 * @param user
	 *            User creating/modifying the object
	 * @return Instrument header object created using given details
	 */
	public InstrumentHeader createInstrumentHeader(
			InstrumentType instrumentType, String instrumentNum,
			Double instrumentAmount, Date instrumentDate, String statusCode,
			User user) {
		InstrumentHeader instrumentHeader = createUnsavedInstrumentHeader(
				instrumentType, instrumentNum, instrumentAmount,
				instrumentDate, statusCode, user);
		session.saveOrUpdate(instrumentHeader);
		return instrumentHeader;
	}

	public CFinancialYear getFinancialYearForDate(Date date) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		Date startingDate;
		Date endingDate;
		Calendar start;
		Calendar end;
		CFinancialYear financialYear = new CFinancialYear();

		if (now.get(Calendar.MONTH) > Calendar.APRIL) {
			start = (Calendar) now.clone();
			start.set(now.get(Calendar.YEAR), Calendar.APRIL, 1);
			startingDate = start.getTime();
			end = (Calendar) now.clone();
			end.set(now.get(Calendar.YEAR) + 1, Calendar.MARCH, 31);
			endingDate = end.getTime();
		} else {
			start = (Calendar) now.clone();
			start.set(now.get(Calendar.YEAR) - 1, Calendar.APRIL, 1);
			startingDate = start.getTime();
			end = (Calendar) now.clone();
			end.set(now.get(Calendar.YEAR), Calendar.MARCH, 31);
			endingDate = end.getTime();
		}
		String finYrRange = start.get(Calendar.YEAR) + "_"
				+ String.valueOf(end.get(Calendar.YEAR)).substring(2);

		financialYear.setStartingDate(startingDate);
		financialYear.setEndingDate(endingDate);
		financialYear.setFinYearRange(finYrRange);
		session.saveOrUpdate(financialYear);
		return financialYear;
	}

	public CVoucherHeader createVoucher(String name) {
		CVoucherHeader voucher = new CVoucherHeader();
		voucher.setName("testVoucher" + name);
		voucher.setType("testType");
		voucher.setEffectiveDate(new Date());
		voucher.setVoucherDate(new Date());
		voucher.setFiscalPeriodId(1);
		voucher.setVoucherNumber("testVoucherNumber");
		voucher.setCgvn("testCGVN" + name );
		session.saveOrUpdate(voucher);
		return voucher;
	}

	public ReceiptVoucher createReceiptVoucher() throws NumberFormatException {
		ReceiptVoucher receiptVoucher = new ReceiptVoucher();
		ReceiptHeader receiptHeader = createReceiptHeader("testReceiptNumber");
		CVoucherHeader voucherHeader = createVoucher("testVoucher");
		receiptVoucher.setReceiptHeader(receiptHeader);
		receiptVoucher.setVoucherheader(voucherHeader);
		//receiptVoucher.setInternalrefno("123456/2009-10");
		session.saveOrUpdate(receiptVoucher);
		return receiptVoucher;
	}

	public ReceiptVoucher createReceiptVoucher(CVoucherHeader voucherHeader) throws NumberFormatException {
		ReceiptVoucher receiptVoucher = new ReceiptVoucher();

		//ReceiptPayeeDetails payee = new ReceiptPayeeDetails();
		ReceiptHeader receiptHeader1 = createUnsavedReceiptHeader();
		receiptHeader1.setReceiptnumber("testReceiptnumber1"+getRandomNumber());
		/*receiptHeader1.setReceiptPayeeDetails(payee);
		payee.addReceiptHeader(receiptHeader1);

		session.saveOrUpdate(payee);

		ReceiptHeader savedReceiptHeader1 = payee.getReceiptHeaders()
				.iterator().next();*/
		receiptVoucher.setReceiptHeader(receiptHeader1);
		receiptVoucher.setVoucherheader(voucherHeader);
		//receiptVoucher.setInternalrefno("123456/2009-10");
		session.saveOrUpdate(receiptVoucher);
		return receiptVoucher;
	}

	public ReceiptDetail createReceiptDetail() throws NumberFormatException {
		return createReceiptDetail(createCOA("1100201"), BigDecimal
				.valueOf(100.00), BigDecimal.valueOf(100.00),
				createFunction("Test Function"), 1L, "testGLDescription",
				createReceiptHeader("11111"));
	}

	/**
	 * Creates receipt detail with given details
	 * 
	 * @param glCode
	 *            GL Code
	 * @param crAmt
	 *            Credit amount
	 * @param drAmt
	 *            Debit amount
	 * @param function
	 *            Function
	 * @param orderNum
	 *            Order number
	 * @param receiptHeader
	 *            Receipt header
	 * @return The receipt detail object created using given details
	 */
	public ReceiptDetail createUnsavedReceiptDetail(CChartOfAccounts coa,
			BigDecimal crAmt, BigDecimal drAmt, CFunction function,
			long orderNum, String description, ReceiptHeader receiptHeader,Boolean isActualDemand) {
		ReceiptDetail receiptDetail = new ReceiptDetail();
		receiptDetail.setAccounthead(coa);
		receiptDetail.setCramount(crAmt);
		receiptDetail.setDramount(drAmt);
		receiptDetail.setFunction(function);
		receiptDetail.setOrdernumber(orderNum);
		receiptDetail.setDescription(description);
		receiptDetail.setReceiptHeader(receiptHeader);
		receiptDetail.setIsActualDemand(isActualDemand);
		receiptDetail.setCramountToBePaid(crAmt);
		return receiptDetail;
	}

	/**
	 * Creates receipt detail with given details
	 * 
	 * @param glCode
	 *            GL Code
	 * @param crAmt
	 *            Credit amount
	 * @param drAmt
	 *            Debit amount
	 * @param function
	 *            Function
	 * @param orderNum
	 *            Order number
	 * @param receiptHeader
	 *            Receipt header
	 * @return The receipt detail object created using given details
	 */
	public ReceiptDetail createReceiptDetail(CChartOfAccounts coa,
			BigDecimal crAmt, BigDecimal drAmt, CFunction function,
			long orderNum, String description, ReceiptHeader receiptHeader) {
		ReceiptDetail receiptDetail = createUnsavedReceiptDetail(coa, crAmt,
				drAmt, function, orderNum, description, receiptHeader,true);
		session.saveOrUpdate(receiptDetail);
		return receiptDetail;
	}

	public ReceiptMisc createUnSavedReceiptMisc(ReceiptHeader receiptHeader) {
		ReceiptMisc receiptMisc = new ReceiptMisc();
		Fund fund = createFund("testFund");
		Scheme scheme = createScheme("123", "testscheme", fund);
		receiptMisc.setBoundary(createBoundary());
		receiptMisc.setDepartment(createDept("testDeptName", "testDeptCode"));
		receiptMisc.setFund(fund);
		receiptMisc.setFundsource(createFundsource("testFSName", "testFSCode"));
		receiptMisc.setScheme(scheme);
		receiptMisc.setSubscheme(createSubScheme(scheme));
		receiptMisc.setReceiptHeader(receiptHeader);
		return receiptMisc;
	}

	public ReceiptMisc createReceiptMis() throws NumberFormatException {
		ReceiptMisc receiptMisc = new ReceiptMisc();
		Fund fund = createFund("001");
		Scheme scheme = createScheme("234", "testscheme1", fund);
		receiptMisc.setBoundary(createBoundary());
		receiptMisc.setDepartment(createDept("testDeptName", "testDeptCode"));
		receiptMisc.setFund(fund);
		receiptMisc.setFundsource(createFundsource("testFSName", "testFSCode"));
		receiptMisc.setScheme(scheme);
		receiptMisc.setSubscheme(createSubScheme(scheme));
		receiptMisc.setReceiptHeader(createReceiptHeader("11111"));
		receiptMisc.setIdFunctionary(createFunctionary());
		session.saveOrUpdate(receiptMisc);
		return receiptMisc;
	}

	public Functionary createFunctionary() {
		Functionary functionary = new Functionary();
		functionary.setCode(BigDecimal.valueOf(10102));
		functionary.setCreatetimestamp(new Date());
		functionary.setIsactive(true);
		functionary.setName("Test Functionary");
		functionary.setUpdatetimestamp(new Date());
		return functionary;
	}

	public Fundsource createFundsource(String name, String code) {
		Fundsource newFundsource = new Fundsource();
		Date date = new Date();
		newFundsource.setCode(code + getRandomNumber());
		newFundsource.setName(name + getRandomNumber());
		newFundsource.setLlevel(BigDecimal.valueOf(0));
		newFundsource.setIsactive(true);
		newFundsource.setCreatedDate(date);
		newFundsource.setLastModifiedDate(date);
		newFundsource.setIsnotleaf(true);
		session.saveOrUpdate(newFundsource);
		return newFundsource;

	}

	public Boundary createBoundary() {
		Boundary boundaryImpl = new Boundary();
		boundaryImpl.setBoundaryNum(Long.valueOf(123232));
		boundaryImpl.setName("Bangalore");
		boundaryImpl.setBoundaryType(createBoundaryType());
		boundaryImpl.setActive(true);
		session.saveOrUpdate(boundaryImpl);
		return boundaryImpl;
	}

	public BoundaryType createBoundaryType() {
		BoundaryType boundaryTypeImpl = new BoundaryType();
		boundaryTypeImpl.setHierarchy(Long.valueOf(1));
		boundaryTypeImpl.setName("karnataka");
		boundaryTypeImpl.setLastModifiedDate(new Date());
		/*boundaryTypeImpl.setHeirarchyType(createHierarchy("testHierarchyName",
				"testHierarchyCode"));*/
		session.saveOrUpdate(boundaryTypeImpl);
		return boundaryTypeImpl;
	}

	public HierarchyType createHierarchy(String name, String code) {

		HierarchyType hierarchyType = new HierarchyType();
		hierarchyType.setName(name + getRandomNumber());
		hierarchyType.setCode(code + getRandomNumber());
		session.saveOrUpdate(hierarchyType);
		return hierarchyType;
	}

	public Department createDept(String deptName, String deptCode) {
		Department empDept = new Department();
		empDept.setName(deptName + getRandomNumber());
		empDept.setLastModifiedDate(new Date());
		//empDept.setBillingLocation("0");
		empDept.setCode(deptCode + getRandomNumber());
		session.saveOrUpdate(empDept);
		return empDept;
	}

	public Department createDeptForCode(String deptCode) {
		Department empDept = new Department();
		empDept.setName(deptCode);
		empDept.setCode(deptCode);
		empDept.setLastModifiedDate(new Date());
		//empDept.setBillingLocation("0");
		session.saveOrUpdate(empDept);
		return empDept;
	}

	public Fund createFund(String code) {
		Fund fund = new Fund();
		fund.setIdentifier('O');
		fund.setName(code);
		fund.setCode(code + getRandomNumber());
		fund.setLlevel(BigDecimal.valueOf(0));
		fund.setIsactive(true);
		fund.setCreatedDate(new Date());
		session.saveOrUpdate(fund);
		return fund;
	}

	/*public ReceiptPayeeDetails createUnsavedReceiptPayeeDetails() {
		ReceiptPayeeDetails newReceiptPayeeDetails = new ReceiptPayeeDetails();
		newReceiptPayeeDetails.setPayeeAddress("Test Address");
		newReceiptPayeeDetails.setPayeename("Test Payee");
		ReceiptHeader receipt=createReceiptHeader("110001");
		receipt.setReceiptPayeeDetails(newReceiptPayeeDetails);
		newReceiptPayeeDetails.addReceiptHeader(receipt);
		return newReceiptPayeeDetails;
	}

	public ReceiptPayeeDetails createReceiptPayeeDetails() {
		ReceiptPayeeDetails payeeDetails = createUnsavedReceiptPayeeDetails();
		session.saveOrUpdate(payeeDetails);
		return payeeDetails;
	}
	
	public ReceiptPayeeDetails createPayeeForChallan(){
		ReceiptPayeeDetails payeeDetails = new ReceiptPayeeDetails();
		payeeDetails.setPayeeAddress("Test Address");
		payeeDetails.setPayeename("Test Payee");
		ReceiptHeader header = createReceiptHeaderForChallan();
		header.setReceiptPayeeDetails(payeeDetails);
		payeeDetails.addReceiptHeader(header);
		session.saveOrUpdate(payeeDetails);
		return payeeDetails;
		
	}*/

	public InstrumentType createInstrumentTypeWithAccountCode() {
		InstrumentType instrType = createUnsavedInstrumentType("testInstrumentType"
				+ getRandomNumber());
		User user = createUser("system");
		Date date = new Date();

		InstrumentAccountCodes instrAccountCode = new InstrumentAccountCodes();
		instrAccountCode.setCreatedBy(user);
		instrAccountCode.setCreatedDate(date);
		instrAccountCode.setModifiedBy(user);
		instrAccountCode.setModifiedDate(date);
		instrAccountCode.setInstrumentType(instrType);
		// session.saveOrUpdate(instrAccountCode);

		instrType.getInstrumentAccountCodes().add(instrAccountCode);
		session.saveOrUpdate(instrType);
		// session.flush();
		return instrType;
	}

	public InstrumentAccountCodes createInstrumentAccountCodesForInstrType(
			String instrType) {
		InstrumentAccountCodes instrAccountCode = new InstrumentAccountCodes();

		User user = createUser("system");
		Date date = new Date();

		instrAccountCode.setCreatedBy(user);
		instrAccountCode.setCreatedDate(date);
		instrAccountCode.setModifiedBy(user);
		instrAccountCode.setModifiedDate(date);

		InstrumentType type = createInstrumentType(instrType);
		type.getInstrumentAccountCodes().add(instrAccountCode);
		session.saveOrUpdate(type);
		instrAccountCode.setInstrumentType(type);
		session.saveOrUpdate(instrAccountCode);
		// session.saveOrUpdate(type);
		return instrAccountCode;// here
	}

	public InstrumentOtherDetails createInstrumentOtherDetails(
			InstrumentVoucher instrVoucher, Date statusDate) {
		InstrumentOtherDetails instrOtherDet = new InstrumentOtherDetails();
		instrOtherDet.setInstrumentHeaderId(instrVoucher
				.getInstrumentHeaderId());
		instrOtherDet.setPayinslipId(instrVoucher.getVoucherHeaderId());
		instrOtherDet.setInstrumentStatusDate(statusDate);
		User user = createUser("testUser");
		instrOtherDet.setCreatedBy(user);
		instrOtherDet.setModifiedBy(user);
		instrOtherDet.setCreatedDate(statusDate);
		instrOtherDet.setModifiedDate(statusDate);
		session.saveOrUpdate(instrOtherDet);
		return instrOtherDet;
	}

	/*
	 * public InstrumentVoucher
	 * createInstrumentVoucherForDishonoredInstruments(){ InstrumentVoucher
	 * instrVoucher=new InstrumentVoucher();
	 * 
	 * InstrumentHeader instrHdr = new InstrumentHeader();
	 * instrHdr.setInstrumentAmount(new Double(1000));
	 * instrHdr.setStatusId(createEgwStatus
	 * (CollectionConstants.INSTRUMENT_DISHONORED_STATUS
	 * ,CollectionConstants.MODULE_NAME_TESTRECEIPTHEADER));
	 * instrHdr.setInstrumentType
	 * (createInstrumentType("testInstrumentType"+getRandomNumber()));
	 * 
	 * User user = createUser("testUser"); Date now = new Date();
	 * instrHdr.setCreatedBy(user); instrHdr.setCreatedDate(now);
	 * instrHdr.setModifiedBy(user); instrHdr.setModifiedDate(now);
	 * session.saveOrUpdate(instrHdr);
	 * 
	 * CVoucherHeader voucher = createVoucher("testVoucher");
	 * 
	 * instrVoucher.setInstrumentHeaderId(instrHdr);
	 * instrVoucher.setVoucherHeaderId(voucher);
	 * instrVoucher.setCreatedBy(user); instrVoucher.setModifiedBy(user);
	 * instrVoucher.setCreatedDate(now); instrVoucher.setModifiedDate(now);
	 * 
	 * session.saveOrUpdate(instrVoucher);
	 * 
	 * return instrVoucher; }
	 */

	public InstrumentVoucher createInstrumentVoucher(
			EgwStatus instrumentStatus, InstrumentType instrumentType) {
		InstrumentVoucher instrVoucher = new InstrumentVoucher();

		InstrumentHeader instrHeader = createInstrumentHeader(instrumentType,
				instrumentStatus);
		CVoucherHeader voucher = createVoucher("testVoucher");
		User user = createUser("testUser");
		Date date = new Date();

		instrVoucher.setInstrumentHeaderId(instrHeader);
		instrVoucher.setVoucherHeaderId(voucher);
		instrVoucher.setCreatedBy(user);
		instrVoucher.setModifiedBy(user);
		instrVoucher.setCreatedDate(date);
		instrVoucher.setModifiedDate(date);

		session.saveOrUpdate(instrVoucher);

		return instrVoucher;
	}

	
	public ReceiptDetail createReceiptDetailWithoutHeader() {
		ReceiptDetail receiptDetail = new ReceiptDetail();
		receiptDetail.setAccounthead(createCOA("testGLCode"));
		receiptDetail.setCramount(BigDecimal.valueOf(10000));
		receiptDetail.setDramount(BigDecimal.valueOf(0));
		receiptDetail.setFunction(createFunction("testFunction"));
		receiptDetail.setFinancialYear(getFinancialYearForDate(new Date()));
		session.saveOrUpdate(receiptDetail);
		return receiptDetail;
	}

	/*public ReceiptPayeeDetails createReceiptPayeeWithoutHeader() {
		ReceiptPayeeDetails receiptPayee = new ReceiptPayeeDetails();
		receiptPayee.setPayeeAddress("Test Address");
		receiptPayee.setPayeename("Test Payee");
		return receiptPayee;
	}

	public ReceiptPayeeDetails createReceiptPayeeForBillingSystem() {
		ReceiptPayeeDetails receiptPayee = createReceiptPayeeWithoutHeader();
		ReceiptHeader receiptHeader = createReceiptHeader("testReceiptNo");
		EgwStatus status = createEgwStatus("testStatusCode",
				"TestInstrumentHeader");
		InstrumentType testInstrType = createInstrumentType("testInstrType");
		receiptHeader.addInstrument(createInstrumentHeader(testInstrType,
				status));
		//receiptHeader.addReceiptDetail(createReceiptDetailWithoutHeader());
		receiptPayee.addReceiptHeader(receiptHeader);
		receiptHeader.setReceiptPayeeDetails(receiptPayee);
		session.saveOrUpdate(receiptPayee);
		return receiptPayee;
	}*/

	/*public Assignment createAssignment(AssignmentPrd assignmentPrd,
			DesignationMaster empDsgn, Position position,Department department) {
	    Department dimpl = (Department) department;
		Assignment assignment = new Assignment();
		assignment.setAssignmentPrd(assignmentPrd);
		assignment.setDesigId(empDsgn);
		assignment.setPosition(position);
		assignment.setDeptId(dimpl);
		session.saveOrUpdate(assignment);
		return assignment;

	}*/

	public Position createPosition(Designation desig) {
		Position position = new Position();
		position.setName(desig.getName() + "pos");
		//position.setDeptDesigId(desig);
		session.saveOrUpdate(position);
		return position;
	}

	public Department createDept(String name) {
		Department d = new Department();
		d.setName(name + getRandomNumber());
		d.setCode(name + getRandomNumber());
		session.save(d);
		return d;
	}

	/*public AssignmentPrd createEmpAsgnmtPrd(PersonalInformation employee) {
		AssignmentPrd assignPeriod = new AssignmentPrd();
		assignPeriod.setEmployeeId(employee);
		assignPeriod.setFromDate(new Date("4/1/2005"));
		assignPeriod.setToDate(new Date("4/1/2099"));
		session.saveOrUpdate(assignPeriod);
		return assignPeriod;
	}*/

	public Designation createDesignation(int deptId,
			String designationName) {
		Designation designation = new Designation();
		//designation.setDeptId(deptId);
		designation.setName(designationName);
		designation.setDescription(designationName);
		session.saveOrUpdate(designation);
		return designation;
	}

	/*public Position createEmployeePositionDetails(String designation,
			PersonalInformation emp, Department dept) {
		AssignmentPrd ap = createEmpAsgnmtPrd(emp);
		DesignationMaster desig = createDesignation(dept.getId(), designation
				+ getRandomNumber());
		Position position = createPosition(desig);
		createAssignment(ap, desig, position,dept);

		return position;
	}*/

	public PersonalInformation createPersonalInformation(User user,
			Department dept) {
		PersonalInformation personalInformation = new PersonalInformation();
		personalInformation.setEmployeeFirstName(user.getName());
		Random ran = new Random();
		personalInformation.setEmployeeCode(ran.nextInt());
		personalInformation.setUserMaster(user);
		//personalInformation.setEgdeptMstr(dept);
		session.saveOrUpdate(personalInformation);
		return personalInformation;
	}

	public Accountdetailkey createAccountdetailkey(String keyname) {
		Accountdetailkey accountdetailkey = new Accountdetailkey();
		accountdetailkey.setAccountdetailtype(createAccountdetailtype("testAccountDetailTypeName"));
		accountdetailkey.setDetailkey(1);
		accountdetailkey.setDetailname(keyname);
		accountdetailkey.setGroupid(1);
		session.saveOrUpdate(accountdetailkey);
		return accountdetailkey;
	}

	public Accountdetailtype createAccountdetailtype(String name) {
		Accountdetailtype accountdetailtype = new Accountdetailtype();
		accountdetailtype.setName(name+getRandomNumber());
		accountdetailtype.setDescription(name);
		accountdetailtype.setAttributename(name+getRandomNumber());
		accountdetailtype.setNbroflevels(new BigDecimal(1));
		session.saveOrUpdate(accountdetailtype);
		return accountdetailtype;
	}

	public AccountPayeeDetail createAccountPayeeDetail() {
		return createAccountPayeeDetail(createAccountdetailtype("test"),
				createAccountdetailkey("test"), BigDecimal.valueOf(100.00),
				createReceiptDetailWithoutHeader());
	}

	public AccountPayeeDetail createUnsavedAccountPayeeDetail(
			Accountdetailtype accdetailtype, Accountdetailkey accdetailkey,
			BigDecimal amt, ReceiptDetail receiptDetail) {
		AccountPayeeDetail accpayeeDetail = new AccountPayeeDetail();
		accpayeeDetail.setAmount(amt);
		accpayeeDetail.setAccountDetailKey(accdetailkey);
		accpayeeDetail.setAccountDetailType(accdetailtype);
		receiptDetail.addAccountPayeeDetail(accpayeeDetail);
		accpayeeDetail.setReceiptDetail(receiptDetail);
		return accpayeeDetail;
	}

	public AccountPayeeDetail createAccountPayeeDetail(
			Accountdetailtype accdetailtype, Accountdetailkey accdetailkey,
			BigDecimal amt, ReceiptDetail receiptDetail) {
		AccountPayeeDetail accpayeeDetail = createUnsavedAccountPayeeDetail(
				accdetailtype, accdetailkey, amt, receiptDetail);
		session.saveOrUpdate(accpayeeDetail);
		return accpayeeDetail;
	}

	public Scheme createScheme(final String code, final String name, Fund fund) {
		Scheme scheme = new Scheme();
		scheme.setCode(code + getRandomNumber());
		scheme.setName(name + getRandomNumber());
		scheme.setFund(fund);
		scheme.setIsactive(true);
		session.saveOrUpdate(scheme);
		return scheme;
	}

	public SubScheme createSubScheme(Scheme scheme) {
		SubScheme subscheme = new SubScheme();
		subscheme.setCode("1234567" + getRandomNumber());
		subscheme.setName("subScheme" + getRandomNumber());
		subscheme.setValidfrom(new Date());
		subscheme.setValidto(new Date());
		subscheme.setScheme(scheme);
		subscheme.setIsactive(true);
		subscheme.setLastmodifieddate(new Date());
		session.saveOrUpdate(subscheme);
		return subscheme;
	}

	public ReceiptMisc createReceiptMisForMiscReceipt() throws NumberFormatException {
		ReceiptMisc receiptMisc = new ReceiptMisc();
		Fund fund = createFund("001");
		Scheme scheme = createScheme("234", "testscheme1", fund);
		receiptMisc.setBoundary(createBoundary());
		receiptMisc.setDepartment(createDept("testDeptName", "testDeptCode"));
		receiptMisc.setFund(fund);
		receiptMisc.setFundsource(createFundsource("testFSName", "testFSCode"));
		receiptMisc.setScheme(scheme);
		receiptMisc.setSubscheme(createSubScheme(scheme));
		receiptMisc.setReceiptHeader(createReceiptHeader("11111"));
		receiptMisc.setIdFunctionary(createSavedFunctionary());
		session.saveOrUpdate(receiptMisc);
		return receiptMisc;
	}

	public List<ReceiptDetailInfo> createBillCreditDetailslist() {
		List<ReceiptDetailInfo> billCreditDetailslist = new ArrayList<ReceiptDetailInfo>();
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		vd.setAccounthead("Surcharge on Stamp Duty for Transfer of Immovable Properties");
		vd.setCreditAmountDetail(new BigDecimal(100));
		vd.setDebitAmountDetail(new BigDecimal(0));
		vd.setAmount(new BigDecimal(0));
		vd.setGlcodeDetail("testGLCODE");
		vd.setGlcodeIdDetail(Long.valueOf(355));
		billCreditDetailslist.add(vd);
		return billCreditDetailslist;
	}

	public List<ReceiptDetailInfo> createEmptyBillRebateDetailslist() {
		List<ReceiptDetailInfo> billRebateDetailslist = new ArrayList<ReceiptDetailInfo>();
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		vd.setAccounthead("");
		vd.setCreditAmountDetail(new BigDecimal(0));
		vd.setDebitAmountDetail(new BigDecimal(0));
		vd.setAmount(new BigDecimal(0));
		vd.setGlcodeDetail("");
		billRebateDetailslist.add(vd);
		return billRebateDetailslist;
	}

	public List<ReceiptDetailInfo> createEmptySubLedgerlist() {
		List<ReceiptDetailInfo> subLedgerlist = new ArrayList<ReceiptDetailInfo>();
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		vd.setCreditAmountDetail(new BigDecimal(0));
		vd.setDebitAmountDetail(new BigDecimal(0));
		vd.setAmount(new BigDecimal(0));
		vd.setDetailCode("");
		vd.setDetailKey("");
		// vd.setDetailType(detailType);
		// vd.setGlcode(glcode);
		subLedgerlist.add(vd);
		return subLedgerlist;
	}

	public Functionary createSavedFunctionary() {
		Functionary functionary = new Functionary();
		functionary.setCode(BigDecimal.valueOf(10102));
		functionary.setCreatetimestamp(new Date());
		functionary.setIsactive(true);
		functionary.setName("Test Functionary");
		functionary.setUpdatetimestamp(new Date());
		session.saveOrUpdate(functionary);
		return functionary;
	}

	public CChartOfAccountDetail createCOADetail(CChartOfAccounts c,
			Accountdetailtype a) {
		CChartOfAccountDetail cChartOfAccountDetail = new CChartOfAccountDetail();
		cChartOfAccountDetail.setGlCodeId(c);
		cChartOfAccountDetail.setDetailTypeId(a);
		session.saveOrUpdate(cChartOfAccountDetail);
		return cChartOfAccountDetail;

	}

	public List<ReceiptDetailInfo> createBillRebateDetailslist() {
		List<ReceiptDetailInfo> billRebateDetailslist = new ArrayList<ReceiptDetailInfo>();
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		vd.setAccounthead("Leave Salary and Gratuity Fund");
		vd.setCreditAmountDetail(new BigDecimal(0));
		vd.setDebitAmountDetail(new BigDecimal(100));
		vd.setAmount(new BigDecimal(0));
		vd.setGlcodeDetail("testBRGLCode");
		vd.setGlcodeIdDetail(Long.valueOf(757));
		billRebateDetailslist.add(vd);
		return billRebateDetailslist;
	}

	public CChartOfAccounts createCOAForGLCode(String glcode) {
		CChartOfAccounts coa = new CChartOfAccounts();
		User user = createUser("testUser");
		Date date = new Date();
		// coa.setId(Long.valueOf(757));
		coa.setGlcode(glcode);
		coa.setName("testcoa");
		coa.setIsActiveForPosting(true);
		coa.setCreatedBy(user);
		coa.setLastModifiedBy(user);
		coa.setCreatedDate(date);
		coa.setLastModifiedDate(date);
		coa.setPurposeId((long) 4);
		coa.setType('I');
		session.saveOrUpdate(coa);
		return coa;
	}
	
	
	public List<ReceiptDetailInfo> createSubLedgerlist() {
		List<ReceiptDetailInfo> subLedgerlist = new ArrayList<ReceiptDetailInfo>();
		//Accountdetailtype accDetailType = createAccountdetailtype("testEmployee");
		CChartOfAccounts ccoa = createCOA("subLedGLCode");
		Accountdetailkey accDetKey1 = createAccountdetailkey("testDetailKeyName1");
		
		
		Accountdetailkey accDetKey2 = createAccountdetailkey("testDetailKeyName2");
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		vd.setCreditAmountDetail(new BigDecimal(0));
		vd.setDebitAmountDetail(new BigDecimal(0));
		vd.setAmount(new BigDecimal(50));
		vd.setDetailCode("102");
		vd.setDetailKey(accDetKey1.getDetailkey().toString());
		vd.setDetailKeyId(accDetKey1.getDetailkey());
		vd.setDetailType(accDetKey1.getAccountdetailtype());
		vd.setDetailTypeName("Employee");
		vd.setGlcode(ccoa);
		vd.setSubledgerCode("3117004");
		subLedgerlist.add(vd);
		
		ReceiptDetailInfo vd2 = new ReceiptDetailInfo();
		vd2.setCreditAmountDetail(new BigDecimal(0));
		vd2.setDebitAmountDetail(new BigDecimal(0));
		vd2.setAmount(new BigDecimal(50));
		vd2.setDetailCode("103");
		vd2.setDetailKey(accDetKey2.getDetailkey().toString());
		vd2.setDetailKeyId(accDetKey2.getDetailkey());
		vd2.setDetailType(accDetKey2.getAccountdetailtype());
		vd2.setDetailTypeName("Employee");
		vd2.setGlcode(ccoa);
		vd2.setSubledgerCode("3117004");
		subLedgerlist.add(vd2);
		
		return subLedgerlist;
	}
	
	public List<ReceiptDetailInfo> createSubLedgerlist(CChartOfAccounts ccoa,Accountdetailtype accDetailType) {
		List<ReceiptDetailInfo> subLedgerlist = new ArrayList<ReceiptDetailInfo>();
		//Accountdetailtype accDetailType = createAccountdetailtype("testEmployee");
		//CChartOfAccounts ccoa = createCOAForGLCode("3117004");
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		vd.setCreditAmountDetail(new BigDecimal(0));
		vd.setDebitAmountDetail(new BigDecimal(0));
		vd.setAmount(new BigDecimal(50));
		vd.setDetailCode("102");
		vd.setDetailKey("CHHIDDU PRASAD");
		vd.setDetailKeyId(3);
		vd.setDetailType(accDetailType);
		vd.setDetailTypeName("Employee");
		vd.setGlcode(ccoa);
		vd.setSubledgerCode("3117004");
		subLedgerlist.add(vd);
		ReceiptDetailInfo vd2 = new ReceiptDetailInfo();
		vd2.setCreditAmountDetail(new BigDecimal(0));
		vd2.setDebitAmountDetail(new BigDecimal(0));
		vd2.setAmount(new BigDecimal(50));
		vd2.setDetailCode("103");
		vd2.setDetailKey("RAM PRASAD");
		vd2.setDetailKeyId(4);
		vd2.setDetailType(accDetailType);
		vd2.setDetailTypeName("Employee");
		vd2.setGlcode(ccoa);
		vd2.setSubledgerCode("3117004");
		subLedgerlist.add(vd2);
		return subLedgerlist;
	}
	
	public OnlinePayment createOnlinePayment() throws NumberFormatException {
		OnlinePayment onlinePayment = new OnlinePayment();
		User user = createUser("testUser");

		onlinePayment.setCreatedDate(new Date());
		onlinePayment.setModifiedDate(new Date());
		onlinePayment.setCreatedBy(user);
		onlinePayment.setModifiedBy(user);
		onlinePayment.setService(this.createServiceDetails());
		onlinePayment.setTransactionAmount(BigDecimal.valueOf(1000));
		onlinePayment.setTransactionDate(new Date());
		onlinePayment.setTransactionNumber("2309319937");
		onlinePayment.setReceiptHeader(this.createReceiptHeader("101010"
				+ getRandomNumber()));
		
		onlinePayment.setStatus(this.createEgwStatus(
				("testCodeRH" + getRandomNumber()).substring(0, 10),
				MODULE_NAME_TESTRECEIPTHEADER));
		
		onlinePayment.getReceiptHeader().setOnlinePayment(onlinePayment);

		session.saveOrUpdate(onlinePayment);
		return onlinePayment;

	}
	
	public OnlinePayment createOnlinePayment(ReceiptHeader receiptHeader,
			String transNo, BigDecimal transAmt,EgwStatus status) throws NumberFormatException {
		OnlinePayment onlinePayment = new OnlinePayment();
		User user = createUser("testUser");

		onlinePayment.setCreatedDate(new Date());
		onlinePayment.setModifiedDate(new Date());
		onlinePayment.setCreatedBy(user);
		onlinePayment.setModifiedBy(user);

		onlinePayment.setService(this.createServiceDetails());

		onlinePayment.setTransactionAmount(transAmt);
		onlinePayment.setTransactionDate(new Date());
		onlinePayment.setTransactionNumber(transNo);
		onlinePayment.setStatus(status);
		receiptHeader.setOnlinePayment(onlinePayment);
		onlinePayment.setReceiptHeader(receiptHeader);

		session.saveOrUpdate(onlinePayment);

		return onlinePayment;
	}
	
	public DefaultPaymentResponse createPaytResponse(){
		DefaultPaymentResponse paytResponse = new DefaultPaymentResponse();

		paytResponse.setMerchantId("MerchantID");
		paytResponse.setCustomerId("customerId");
		paytResponse.setTxnReferenceNo("txnReferenceNo");
		paytResponse.setBankReferenceNo("bankReferenceNo");
		paytResponse.setTxnAmount(BigDecimal.valueOf(1000.0));
		paytResponse.setBankId(1);
		paytResponse.setBankMerchantId(1);
		paytResponse.setTxnType("txnType");
		paytResponse.setCurrencyName("currencyName");
		paytResponse.setItemCode("itemCode");
		paytResponse.setSecurityType("securityType");
		paytResponse.setSecurityId(1);
		paytResponse.setSecurityPassword("securityPassword");
		paytResponse.setTxnDate(new Date());
		paytResponse.setAuthStatus("authStatus");
		paytResponse.setSettlementType("settlementType");
		paytResponse.setReceiptId("10001");
		paytResponse.setAdditionalInfo2("additionalInfo2");
		paytResponse.setAdditionalInfo3("additionalInfo3");
		paytResponse.setPaytGatewayServiceCode("additionalInfo4");
		paytResponse.setBillingServiceCode("additionalInfo5");
		paytResponse.setAdditionalInfo6("additionalInfo6");
		paytResponse.setAdditionalInfo7("additionalInfo7");
		paytResponse.setErrorStatus("errorStatus");
		paytResponse.setErrorDescription("errorDescription");
		paytResponse.setChecksum("checksum");
		
		return paytResponse;
	}

	public List<Map<String, Object>> createMapForInstrumentHeader(
			List<InstrumentHeader> instrumentHeaderList) {
		List<Map<String, Object>> instrumentHeaderMapList = new ArrayList();
		if (instrumentHeaderList != null) {
			for (InstrumentHeader instrumentHeader : instrumentHeaderList) {
				Map<String, Object> instrumentHeaderMap = new HashMap();
				instrumentHeaderMap.put("Instrument number", instrumentHeader
						.getInstrumentNumber());
				instrumentHeaderMap.put("Instrument date", instrumentHeader
						.getInstrumentDate());
				instrumentHeaderMap.put("Instrument amount", instrumentHeader
						.getInstrumentAmount());
				instrumentHeaderMap.put("Instrument type", instrumentHeader
						.getInstrumentType().getType());
				instrumentHeaderMap.put("Is pay cheque", instrumentHeader
						.getIsPayCheque());
				if (instrumentHeader.getBankId() != null)
					instrumentHeaderMap.put("Bank code", instrumentHeader
							.getBankId().getCode());
				instrumentHeaderMap.put("Bank branch name", instrumentHeader
						.getBankBranchName());
				instrumentHeaderMap.put("Transaction number", instrumentHeader
						.getTransactionNumber());
				instrumentHeaderMap.put("Transaction date", instrumentHeader
						.getTransactionDate());
				instrumentHeaderMapList.add(instrumentHeaderMap);
			}
		}

		return instrumentHeaderMapList;
	}

	public List<Map<String, Object>> createMapForInstrumentVoucher(
			List<CVoucherHeader> voucherHeaderList,
			List<InstrumentHeader> instrumentHeaderList) {
		List<Map<String, Object>> instrumentVoucherList = new ArrayList<Map<String,Object>>();

		if (voucherHeaderList != null && instrumentHeaderList != null) {
			for (CVoucherHeader voucherHeader : voucherHeaderList) {
				for (InstrumentHeader instrumentHeader : instrumentHeaderList) {
					Map<String, Object> iVoucherMap = new HashMap();
					iVoucherMap
							.put(
									CollectionConstants.FINANCIAL_INSTRUMENTSERVICE_INSTRUMENTHEADEROBJECT,
									instrumentHeader);
					iVoucherMap
							.put(
									CollectionConstants.FINANCIAL_INSTRUMENTSERVICE_VOUCHERHEADEROBJECT,
									voucherHeader);
					instrumentVoucherList.add(iVoucherMap);
				}
			}
		}
		return instrumentVoucherList;
	}
	
	public Challan createChallan() throws NumberFormatException {
		Challan challan = createUnsavedChallan();
		session.saveOrUpdate(challan);
		return challan;
	}
	
	public ReceiptHeader createReceiptHeaderWithChallan() throws NumberFormatException {
		ReceiptHeader receiptHeader = createReceiptHeader("testReceiptNo");
		
		receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_CHALLAN);
		
		Challan challan = new Challan();
		User user = createUser("testUser");
		
		Date date = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,2);
		//cal.set(Calendar.HOUR, 23);
		//cal.set(Calendar.MINUTE, 59);
		//cal.set(Calendar.SECOND, 59);
		Date valid = cal.getTime();
		challan.setValidUpto(valid);
		
		challan.setCreatedDate(date);
		challan.setLastModifiedDate(date);
		
		challan.setCreatedBy(user);
		challan.setLastModifiedBy(user);
		challan.setChallanNumber("testChallanNo");
		challan.setChallanDate(date);
		challan.setService(createServiceDetails("testService"));
		challan.setStatus(this.createEgwStatus(
				("testCodeRH" + getRandomNumber()).substring(0, 10),
				MODULE_NAME_TESTCHALLANHEADER));
		challan.setVoucherHeader(createVoucher("testChallanVoucher"));
		
		challan.setReceiptHeader(receiptHeader);
		receiptHeader.setChallan(challan);
		session.saveOrUpdate(challan);
		session.saveOrUpdate(receiptHeader);
		
		return receiptHeader;
	}
	
	
	public Challan createUnsavedChallan() throws NumberFormatException {
		Challan challan = new Challan();
		User user = createUser("testUser");
		Date date = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,3);
		Date valid = cal.getTime();
		challan.setValidUpto(valid);
		
		
		challan.setCreatedDate(date);
		challan.setLastModifiedDate(date);
		//challan.setValidUpto(date);
		challan.setCreatedBy(user);
		challan.setLastModifiedBy(user);
		challan.setChallanNumber("testChallanNo");
		challan.setChallanDate(date);
		challan.setService(createServiceDetails("testService"));
		/*ReceiptPayeeDetails payee = createReceiptPayeeDetails();
		ReceiptHeader header = payee.getReceiptHeaders().iterator().next();
		header.setReceiptPayeeDetails(payee);
		challan.setReceiptHeader(header);*/ //TODO: Set the header appropriately
		
		challan.setStatus(this.createEgwStatus(
				("testCodeRH" + getRandomNumber()).substring(0, 10),
				MODULE_NAME_TESTCHALLANHEADER));
		

		return challan;
	}
	
	public ServiceDetails createUnsavedChallanServiceDetails() {
		ServiceDetails service = new ServiceDetails();
		BankAccountServiceMap tempB=new BankAccountServiceMap();
		String serviceName = "@testChallanSrvc$" + getRandomNumber(9999);
		service.setName(serviceName);
		service.setServiceUrl("testServiceURL");
		service.setServiceType("S");
		service.setCode("testCode");
		service.addBankAccountServiceMap(createBankAccountServiceMap(service));
		return service;
	}
	
	public ReceiptMisc createReceiptMisForChallan() throws NumberFormatException {
		ReceiptMisc receiptMisc = new ReceiptMisc();
		Fund fund = createFund("001");
		receiptMisc.setBoundary(createBoundary());
		receiptMisc.setDepartment(createDept("testDeptName", "testDeptCode"));
		receiptMisc.setFund(fund);
		receiptMisc.setReceiptHeader(createReceiptHeader("11111"));
		session.saveOrUpdate(receiptMisc);
		return receiptMisc;
	}
	
	public ReceiptDetailInfo createReceiptDetailInfo(BigDecimal creditAmt, BigDecimal debitAmt,String glCode){
		ReceiptDetailInfo receiptDetailInfo = new ReceiptDetailInfo();
		receiptDetailInfo.setCreditAmountDetail(creditAmt);
		receiptDetailInfo.setDebitAmountDetail(debitAmt);
		receiptDetailInfo.setAmount(new BigDecimal(0));
		receiptDetailInfo.setGlcodeDetail(glCode);
		receiptDetailInfo.setGlcodeIdDetail(Long.valueOf(355));
		receiptDetailInfo.setFunctionDetail("testFunction");
		receiptDetailInfo.setFunctionIdDetail(Long.valueOf(2));
		return receiptDetailInfo;
	}
	public List<ReceiptDetailInfo> createCreditDetailslist() {
		List<ReceiptDetailInfo> billCreditDetailslist = new ArrayList<ReceiptDetailInfo>();
		
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		vd.setAccounthead("Surcharge on Stamp Duty for Transfer of Immovable Properties");
		vd.setCreditAmountDetail(new BigDecimal(100));
		vd.setDebitAmountDetail(new BigDecimal(0));
		vd.setAmount(new BigDecimal(0));
		
		vd.setGlcodeDetail("testGLCODE");
		
		vd.setGlcodeIdDetail(Long.valueOf(355));
		vd.setFinancialYearId(Long.valueOf("4"));
		vd.setFunctionDetail("testFunction");
		vd.setFunctionIdDetail(Long.valueOf(2));
		billCreditDetailslist.add(vd);
		return billCreditDetailslist;
	}
	
	public List<ReceiptDetailInfo> createCreditDetailslist(CChartOfAccounts account) {
		List<ReceiptDetailInfo> billCreditDetailslist = new ArrayList<ReceiptDetailInfo>();
		
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		vd.setAccounthead(account.getName());
		vd.setCreditAmountDetail(new BigDecimal(100));
		vd.setDebitAmountDetail(new BigDecimal(0));
		vd.setAmount(new BigDecimal(0));
		
		vd.setGlcodeDetail(account.getGlcode());
		vd.setGlcodeIdDetail(account.getId());
		
		vd.setFinancialYearId(Long.valueOf("4"));
		vd.setFunctionDetail("testFunction");
		vd.setFunctionIdDetail(Long.valueOf(2));
		billCreditDetailslist.add(vd);
		return billCreditDetailslist;
	}

	public ReceiptHeader createReceiptHeaderForChallan() throws NumberFormatException{
		//ReceiptPayeeDetails payee = createReceiptPayeeDetails();
		ReceiptHeader receiptHeader = null ; //TODO: Fix the issue by getting ReceiptHeader //payee.getReceiptHeaders().iterator().next();
		//receiptHeader.setReceiptPayeeDetails(payee);
		// ReceiptHeader receiptHeader=new ReceiptHeader();
		receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_CHALLAN);
		receiptHeader.setReceiptnumber("1234"+getRandomNumber());
		receiptHeader.setReferencenumber("12");
		receiptHeader.setConsumerCode("10-10-111-20");
		receiptHeader.setService(createServiceDetails());
		receiptHeader.setCreatedDate(new Date());
		receiptHeader.setLastModifiedDate(new Date());
		User user=createUser("system");
		receiptHeader.setCreatedBy(user);
		receiptHeader.setLastModifiedBy(user);
		receiptHeader.setIsReconciled(false);
		receiptHeader.setStatus(createEgwStatus("testcode",
				CollectionConstants.MODULE_NAME_CHALLAN));
		receiptHeader.setPaidBy("Test Payee");
		receiptHeader.setReceiptMisc(createUnSavedReceiptMisc(receiptHeader));
		receiptHeader.addReceiptDetail(createReceiptDetailForChallan());
		session.saveOrUpdate(receiptHeader);
		return receiptHeader;
		
	}
	
	public ReceiptDetail createReceiptDetailForChallan() throws NumberFormatException{
		ReceiptDetail receiptDetail = createUnsavedReceiptDetail(createCOA("1100201"), BigDecimal
				.valueOf(100.00), BigDecimal.valueOf(100.00),
				createFunction("Test Function"), 1L, "testGLDescription",
				createReceiptHeader("11111"),true);
		receiptDetail.setFinancialYear(getFinancialYearForDate(new Date()));
		session.saveOrUpdate(receiptDetail);
		receiptDetail.addAccountPayeeDetail(createAccountPayeeDetail(createAccountdetailtype("test"),
				createAccountdetailkey("test"), BigDecimal.valueOf(100.00),receiptDetail));
		session.saveOrUpdate(receiptDetail);
		return receiptDetail;
	}
	public ServiceDetails createChallanServiceDetails() throws NumberFormatException {
		ServiceDetails service = new ServiceDetails();
		String serviceName = "@testChallanSrvc$" + getRandomNumber(9999);
		service.setName(serviceName);
		service.setServiceUrl("testServiceURL");
		service.setServiceType("S");
		service.setCode("testCode");
		session.saveOrUpdate(service);
		return service;
	}
}
