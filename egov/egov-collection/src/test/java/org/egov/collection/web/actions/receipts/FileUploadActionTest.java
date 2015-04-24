package org.egov.erpcollection.web.actions.receipts;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.easymock.EasyMock;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.service.CommonsManager;
import org.egov.erpcollection.models.Challan;
import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.erpcollection.models.ReceiptDetail;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.models.ReceiptPayeeDetails;
import org.egov.erpcollection.models.ReceiptVoucher;
import org.egov.erpcollection.services.ChallanService;
import org.egov.erpcollection.services.ReceiptHeaderService;
import org.egov.erpcollection.services.ReceiptService;
import org.egov.erpcollection.util.CollectionCommon;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.erpcollection.util.FinancialsUtil;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentType;
import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.pims.commons.Position;
import org.egov.services.instrument.InstrumentService;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;



public class FileUploadActionTest extends AbstractPersistenceServiceTest<ReceiptPayeeDetails,Long>{
	private FileUploadAction action;
	private CollectionObjectFactory objectFactory;
	private ReceiptService receiptService;
	private ReceiptHeaderService receiptHeaderService;
	
	private CollectionsUtil collectionsUtil;
	private CommonsManager commonsManager;
	private FinancialsUtil financialsUtil;
	private InstrumentService instrumentService;
	private CollectionCommon collectionCommon;
	WorkflowService<ReceiptHeader> receiptWorkflowService;
	private ChallanService challanService;
	
	
	@Before
	public void setupAction(){
		
		instrumentService = createMock(InstrumentService.class);
		
		financialsUtil = new FinancialsUtil(){
			public CVoucherHeader getReversalVoucher(
					List<HashMap<String, Object>> paramList){
				return new CVoucherHeader();
			}
			public InstrumentType getInstrumentTypeByType(String type){
				return (InstrumentType) genericService.find("from InstrumentType  where type=? and isActive=1",type);
			}
		};
		financialsUtil.setInstrumentService(instrumentService);
		
		
		ScriptService scriptExecutionService = new ScriptService(2, 5, 10, 30);
		scriptExecutionService.setSessionFactory(egovSessionFactory);
		
		commonsManager = createMock(CommonsManager.class);
		challanService=new ChallanService(){
			public void workflowtransition(Challan challan,Position nextPosition, 
					String actionName, String remarks){
			}
			public Session getSession() {
				return session;
			}
		};
				
		collectionsUtil=createMock(CollectionsUtil.class);
		receiptService = createMock(ReceiptService.class);
		receiptHeaderService=createMock(ReceiptHeaderService.class);
		
		receiptWorkflowService = new SimpleWorkflowService<ReceiptHeader>(
				receiptHeaderService, ReceiptHeader.class){
			public ReceiptHeader transition(final String actionName, final StateAware stateAwareItem, final String comment){
				return (ReceiptHeader)stateAwareItem;
			}
			public ReceiptHeader end (final StateAware stateAwareItem, final Position owner, final String comment){
				return (ReceiptHeader)stateAwareItem;
			}
		};
		
			
		collectionCommon = createMock(CollectionCommon.class);
		receiptHeaderService=createMock(ReceiptHeaderService.class);
		action = new FileUploadAction(){
			protected void readColumn(){
			}
		};
		action.setCollectionsUtil(collectionsUtil);
		action.setPersistenceService(genericService);
		action.setReceiptPayeeDetailsService(receiptService);
		action.setReceiptHeaderService(receiptHeaderService);
		action.setCommonsManager(commonsManager);
		action.setCollectionCommon(collectionCommon);
		action.setChallanService(challanService);
		//action.setReceiptWorkflowService(receiptWorkflowService);
		action.setBeanProvider(null);
		
		action.setFinancialsUtil(financialsUtil);
		
		objectFactory = new CollectionObjectFactory(session,genericService);
		
	}
	
	@Test
	public void testGetModel(){
		assertNull(action.getModel());
	}
	
	@Test
	public void testExecute() throws Exception{
		assertEquals(action.execute(),BaseFormAction.INDEX);
	}
	
	@Test
	public void testEdit() {
		assertEquals(action.edit(),BaseFormAction.EDIT);
	}
	
	@Test
	public void testNewForm() throws Exception{
		assertEquals(action.newform(),BaseFormAction.NEW);
	}
	
	@Test
	public void testCreate(){
		assertEquals(action.create(),Action.SUCCESS);
	}
	
	@Test
	public void testSave(){
		
		List<String[]> inputList = new ArrayList<String[]>();
		
		String[] inputArray1 = new String[22];
		
		Fund fund = objectFactory.createFund("testFund");
		Department dept = objectFactory.createDept("testDept");
		CFunction func = objectFactory.createFunction("testFunc");
		CChartOfAccounts account = objectFactory.createCOA("");
		Bankbranch bankBranch=objectFactory.createBankBranch();
		
		inputArray1[0]="1";
		inputArray1[1]="14-APR-2010";
		inputArray1[2]="Mrs.ABC";
		inputArray1[3]="XYZ";
		inputArray1[4]="Narration for File Upload Test";
		inputArray1[5]=fund.getName();
		inputArray1[6]=dept.getDeptName();
		inputArray1[7]=func.getCode()+".0";
		inputArray1[8]=account.getGlcode()+".0";
		inputArray1[9]="1000";

		inputArray1[10]="";
		inputArray1[11]="";
		inputArray1[12]="";
		inputArray1[13]="R/1";
		inputArray1[14]="14-AUG-2010";
		inputArray1[15]="Cheque";
		inputArray1[16]="600";
		inputArray1[17]="123456";
		inputArray1[18]="14-AUG-2010";
		inputArray1[19]=bankBranch.getBank().getName();
		inputArray1[20]=bankBranch.getBranchname();
		inputArray1[21]="1";
		
		inputList.add(inputArray1);
		
		String[] inputArray2 = new String[22];
		inputArray2[0]="1";
		inputArray2[1]="14-APR-2010";
		inputArray2[9]=null;
		inputArray2[15]="dd";
		inputArray2[16]="400";
		inputArray2[17]="098765";
		inputArray2[18]="14-AUG-2010";
		inputArray2[19]=bankBranch.getBank().getName();
		inputArray2[20]=bankBranch.getBranchname();
		inputArray2[21]="2";
		
		inputList.add(inputArray2);
		
		action.setInputList(inputList);
		
		UserImpl user = objectFactory.createUser("egovernments",dept);
		
		EgwStatus status = (EgwStatus) genericService.findByNamedQuery(
				CollectionConstants.QUERY_STATUS_BY_MODULE_AND_CODE, 
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_PENDING);
		
		
		EasyMock.expect(collectionsUtil.getLoggedInUser(null)).andReturn(user).anyTimes();
		EasyMock.expect(collectionsUtil.getFinancialYearforDate(EasyMock.isA(Date.class))).andReturn(new CFinancialYear()).anyTimes();
		EasyMock.expect(collectionsUtil.getEgwStatusForModuleAndCode(EasyMock.isA(String.class),EasyMock.isA(String.class))).andReturn(status).anyTimes();
		EasyMock.expect(collectionsUtil.getPositionOfUser(EasyMock.isA(UserImpl.class))).andReturn(null).anyTimes();
		EasyMock.expect(collectionsUtil.getLocationOfUser(null)).andReturn(null).anyTimes();
		replay(collectionsUtil);
		
		EasyMock.expect(commonsManager.getCChartOfAccountsByGlCode(EasyMock.isA(String.class))).andReturn(account).anyTimes();
		EasyMock.expect(commonsManager.getFinancialYearById(EasyMock.isA(Long.class))).andReturn(new CFinancialYear()).anyTimes();
		replay(commonsManager);
		
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeDetails();
		ReceiptHeader receipt = payee.getReceiptHeaders().iterator().next();
		
		EasyMock.expect(receiptService.persistChallan(EasyMock.isA(ReceiptPayeeDetails.class))).andReturn(payee);
		EasyMock.expect(receiptService.getSession()).andReturn(session);
		EasyMock.expect(receiptService.createInstrument(EasyMock.isA(List.class))).andReturn(new ArrayList<InstrumentHeader>());
		receiptService.setReceiptNumber(EasyMock.isA(ReceiptHeader.class));
		expectLastCall();
		EasyMock.expect(receiptService.persist(EasyMock.isA(ReceiptPayeeDetails.class))).andReturn(payee);
		receiptService.updateInstrument(EasyMock.isA(List.class),EasyMock.isA(List.class));
		expectLastCall();
		replay(receiptService);
		
		ReceiptDetail receiptDetail = objectFactory.createReceiptDetail();
		collectionCommon.addDebitAccountHeadDetails(
				EasyMock.isA(BigDecimal.class),EasyMock.isA(ReceiptHeader.class),
				EasyMock.isA(BigDecimal.class),EasyMock.isA(BigDecimal.class),
				EasyMock.isA(String.class));
		expectLastCall().andReturn(receiptDetail);
		replay(collectionCommon);
		
		receiptHeaderService.startWorkflow(EasyMock.isA(List.class),EasyMock.isA(Boolean.class));
		expectLastCall();
		replay(receiptHeaderService);
		
		action.setTestMode(true);
		assertEquals(action.save(),Action.SUCCESS);
		assertEquals(action.getSource(),"upload");
		assertEquals(action.getSuccessNo(),1);
	}
	
	@Test
	public void testFileValidateWithInvalidData(){
		List<String[]> inputList = new ArrayList<String[]>();
		
		String[] inputArray1 = new String[22];
		
		inputArray1[0]="1";
		inputArray1[1]="14/09/2010";
		inputArray1[2]="Mrs.ABC";
		inputArray1[3]="XYZ";
		inputArray1[4]="Narration for File Upload Test";
		inputArray1[5]="dummyFundName";
		inputArray1[6]="dummyDeptName";
		inputArray1[7]="dummyFuncName";
		inputArray1[8]="1";
		inputArray1[9]="1000";

		inputArray1[10]="";
		inputArray1[11]="";
		inputArray1[12]="";
		inputArray1[13]="R/1";
		inputArray1[14]="14/09/2010";
		inputArray1[15]="Cheque";
		inputArray1[16]="1000";
		inputArray1[17]="1234567";
		inputArray1[18]="14/09/2010";
		inputArray1[19]="dummyBankName";
		inputArray1[20]="dummyBankBranchName";
		inputArray1[21]="1";
		
		inputList.add(inputArray1);
		
		
		String[] inputArray2 = new String[22];
		inputArray2[0]="1";
		inputArray2[1]="14/09/2010";
		inputArray2[9]="";
		inputArray2[15]="dd";
		inputArray2[16]="400";
		inputArray2[17]="";
		inputArray2[18]="";
		inputArray2[19]="";
		inputArray2[20]="dummyBankBranchName2";
		inputArray2[21]="2";
		
		inputList.add(inputArray2);
		
		String[] inputArray3 = new String[22];
		inputArray3[0]="1";
		inputArray3[1]="14/09/2010";
		inputArray3[9]="";
		inputArray3[15]="dd";
		inputArray3[16]="";
		inputArray3[17]="098765";
		inputArray3[18]="14-AUG-2010";
		inputArray3[19]="";
		inputArray3[20]="";
		inputArray3[21]="3";
		
		inputList.add(inputArray3);
		
		action.setInputList(inputList);
		
		assertEquals(action.fileValidate(),Action.SUCCESS);
		assertEquals(action.getErrorRowMap().get(1),"Invalid Challan Date[ 14/09/2010 ], Incorrect value for fund[ dummyFundName ], Incorrect value for Department[ dummyDeptName ], Incorrect value for Function[ dummyFuncName ], Incorrect value for Account Code [ 1 ], Invalid Receipt Date Format[ 14/09/2010 ], Invalid Cheque/DD Number[ 1234567 ], Invalid Cheque/DD Date[ 14/09/2010 ], Incorrect value for Bank Name[ dummyBankName ], Sum of Cheque/DD amounts do not tally with the total amount");
		assertEquals(action.getErrorRowMap().get(2),"Cheque/DD Number is null/empty, Cheque/DD Date is null/empty, Cheque/DD Bank Name is null/empty");
		assertEquals(action.getErrorRowMap().get(3),"Cheque/DD Payment Amount is null/empty, Cheque/DD Bank Name is null/empty");
	}
	
	//@Test
	public void testFileValidateWithEmptyData(){
		List<String[]> inputList = new ArrayList<String[]>();
		
		String[] inputArray1 = new String[22];
		
		inputArray1[0]="";
		inputArray1[1]="";
		inputArray1[2]="Mrs.ABC";
		inputArray1[3]="XYZ";
		inputArray1[4]="Narration for File Upload Test";
		inputArray1[5]="";
		inputArray1[6]="";
		inputArray1[7]="";
		inputArray1[8]="";
		inputArray1[9]="";

		inputArray1[10]="";
		inputArray1[11]="";
		inputArray1[12]="";
		inputArray1[13]="";
		inputArray1[14]="";
		inputArray1[15]="";
		inputArray1[16]="";
		inputArray1[17]="";
		inputArray1[18]="";
		inputArray1[19]="";
		inputArray1[20]="dummyBankBranchName";
		inputArray1[21]="1";
		
		inputList.add(inputArray1);
		
		action.setInputList(inputList);
		
		assertEquals(action.fileValidate(),Action.SUCCESS);
		assertEquals(action.getErrorRowMap().get(1),"Challan Number is null/Empty, Challan Date is null/Empty, Invalid Challan Date[  ], Fund is null/Empty, Department is null/Empty, Account Code is null/Empty, Account head Amount is null/Empty, Receipt number is null/Empty, Receipt date is null/Empty, Invalid Receipt Date Format[  ], Payment Mode is null/Empty, Payment Amount is null/Empty");
	}
	
	@Test
	public void testCreateVouchers(){
		ReceiptHeader approved = objectFactory.createReceiptHeader("testReceiptNo");
		EgwStatus status = (EgwStatus) genericService.findByNamedQuery(
				CollectionConstants.QUERY_STATUS_BY_MODULE_AND_CODE,
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_APPROVED);
		approved.setStatus(status);
		session.saveOrUpdate(status);
		
		ReceiptVoucher approvedWithVoucher = objectFactory.createReceiptVoucher();
		
		receiptHeaderService.createVoucherForReceipt(EasyMock.isA(ReceiptHeader.class),EasyMock.isA(Boolean.class));
		expectLastCall().andReturn(approvedWithVoucher.getVoucherheader());
		replay(receiptHeaderService);
		
		receiptService.updateInstrument(EasyMock.isA(List.class),EasyMock.isA(List.class));
		expectLastCall();
		replay(receiptService);
		
		assertEquals(action.createVouchers(), "vouchercreationresult");
		assertEquals(action.getSuccessNo(), 1);
		assertTrue(action.getErrorReceiptList().isEmpty());
	}
}
