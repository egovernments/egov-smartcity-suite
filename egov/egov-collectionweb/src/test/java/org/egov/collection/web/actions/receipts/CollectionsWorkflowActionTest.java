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


/**
 * Test class for the submit collections action
 */
public class CollectionsWorkflowActionTest {/* extends
		AbstractPersistenceServiceTest<ReceiptHeader, Long> {
	private CollectionsWorkflowAction action;
	private static final String functionName = "SUBMIT-COLLECTION-FUNC";
	private static final String glCode1 = "#$";
	private static final String glCode2 = "$%";
	private final Double instrumentAmount = Double.valueOf("1000");
	private final Date instrumentDate = new Date();
	private static final String instrumentStatusCode = "testCode";
	private static final String userName = "testUser";
	private static final String counterName = "testCounter";
	private ReceiptHeader receiptToBeSubmitted;
	private ReceiptHeader receiptToBeApproved;
	private FinancialsUtil financialsUtil;
	private CollectionsUtil collectionsUtil;
	private static ClassPathXmlApplicationContext context;
	private CollectionObjectFactory objectFactory;
	private EisCommonsManager eisCommonsManagerMock;
	WorkflowService<ReceiptHeader> receiptWorkflowServiceMock;
	EgwStatus statusSubmitted, statusApproved, statusToBeSubmitted;
	private AuditEventService auditEventService;

	*//**
	 * Creates the receipts used for testing
	 *//*
	private void createReceipts() {
		receiptToBeSubmitted = objectFactory
				.createReceiptHeaderWithInstrument(
						"RCPT-1111",
						CollectionConstants.RECEIPT_TYPE_BILL,
						CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED,
						"BILL-1111",
						financialsUtil
								.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH),
						null, instrumentAmount, instrumentDate,
						instrumentStatusCode, glCode1, functionName, userName,
						counterName);

		receiptToBeApproved = objectFactory
				.createReceiptHeaderWithInstrument(
						"RCPT-2222",
						CollectionConstants.RECEIPT_TYPE_BILL,
						CollectionConstants.RECEIPT_STATUS_CODE_SUBMITTED,
						"BILL-2222",
						financialsUtil
								.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH),
						null, instrumentAmount, instrumentDate,
						instrumentStatusCode, glCode2, functionName, userName,
						counterName);
	}

	*//**
	 * Prepares the action object to be tested
	 *//*
	@SuppressWarnings("unchecked")
	private void prepareAction() {
		action = new CollectionsWorkflowAction();
		action.setPersistenceService(genericService);
		action.setCollectionsUtil(collectionsUtil);

		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put(CollectionConstants.SESSION_VAR_LOGIN_USER_NAME,
				receiptToBeSubmitted.getCreatedBy().getUserName());
		sessionMap.put(CollectionConstants.SESSION_VAR_LOGIN_USER_COUNTERID, receiptToBeSubmitted.getLocation().getId().toString());
		action.setSession(sessionMap);

		ReceiptHeaderService receiptHeaderService = (ReceiptHeaderService) context
				.getBean("receiptHeaderService");
		action.setReceiptHeaderService(receiptHeaderService);

		receiptWorkflowServiceMock = EasyMock.createMock(WorkflowService.class);
		action.setReceiptWorkflowService(receiptWorkflowServiceMock);

		action.prepare();
	}

	*//**
	 * Registers/creates the EJBs required for the workflow python script
	 *//*
	private void initBeans() throws Exception {
		if (context == null) {
			context = new ClassPathXmlApplicationContext(
					"erpCollectionsAppContextTest.xml");
		}

		financialsUtil = (FinancialsUtil) context.getBean("financialsUtil");
		collectionsUtil = (CollectionsUtil) context.getBean("collectionsUtil");

		eisCommonsManagerMock = EasyMock.createMock(EisCommonsManager.class);
		auditEventService = new AuditEventService();
		ApplicationThreadLocals.setUserId("1");
		genericService.setType(AuditEvent.class);
		auditEventService.setAuditEventPersistenceService(genericService);
		collectionsUtil.setAuditEventService(auditEventService);
		collectionsUtil.setEisCommonsManager(eisCommonsManagerMock);
	}

	*//**
	 * Initializes the status cache in CollectionsUtil class for the statuses
	 * that will be referred from the workflow python script. We are doing this
	 * because the db query from CollectionsUtil is failing if called from the
	 * python script (only from the JUnit test case). Initializing the cache
	 * here ensures that the db query is never fired.
	 *//*
	private void initStatusCache() {
		statusSubmitted = collectionsUtil
				.getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_SUBMITTED);
		statusApproved = collectionsUtil
				.getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_APPROVED);
		statusToBeSubmitted = collectionsUtil
				.getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
	}

	@Before
	public void setupAction() throws Exception {
		objectFactory = new CollectionObjectFactory(session);
		initBeans();
		createReceipts();
		prepareAction();
		initStatusCache();
	}

	@Test
	public void testGetModel() {
		assertTrue(action.getModel() == null);
	}

	@Test
	public void testListSubmit() {
		action.setWfAction(CollectionConstants.WF_ACTION_SUBMIT);
		
		InstrumentType instrTypeCheque=financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CHEQUE);
		InstrumentHeader chequeInstrument=objectFactory.createInstrumentHeader(instrTypeCheque, objectFactory.createEgwStatus("testCode", "testModuleType"));
		receiptToBeSubmitted.addInstrument(chequeInstrument);
		// Get list of receipts to be submitted
		action.listWorkflow();
		List<ReceiptHeader> receiptsToBeSubmitted = action.getReceiptHeaders();
		assertNotNull(receiptsToBeSubmitted);
		assertTrue(receiptsToBeSubmitted.size() > 0);

		String referenceNum = receiptToBeSubmitted.getReferencenumber();
		assertTrue("Receipt with reference number [" + referenceNum
				+ "] not fetched in list of receipts to be submitted ["
				+ receiptsToBeSubmitted + "]", containsReceipt(
				receiptsToBeSubmitted, referenceNum));

		assertFalse(action.getAllowPartialSelection());
	}
	
	@Test
	public void testSetInboxItemDetails(){
		action.setInboxItemDetails("testWfAction-testServiceCode-testUserName-01");
		
		assertEquals(action.getServiceCode(),"testServiceCode");
		assertEquals(action.getUserName(),"testUserName");
		assertEquals(action.getCounterId(),1);
	}

	@Test
	public void testListApprove() {
		action.setWfAction(CollectionConstants.WF_ACTION_APPROVE);
		// Get list of receipts to be submitted
		action.listWorkflow();
		List<ReceiptHeader> receiptsToBeApproved = action.getReceiptHeaders();
		assertNotNull(receiptsToBeApproved);

		String referenceNum = receiptToBeApproved.getReferencenumber();
		assertTrue("Receipt with reference number [" + referenceNum
				+ "] not fetched in list of receipts to be approved!",
				containsReceipt(receiptsToBeApproved, referenceNum));

		assertTrue(action.getIsApproveAction());
	}

	//@Test
	public void testSubmit() {
		EasyMock.expect(
				receiptWorkflowServiceMock.transition(EasyMock
						.isA(String.class), EasyMock.isA(StateAware.class),
						EasyMock.isA(String.class))).andAnswer(
				new IAnswer<ReceiptHeader>() {

					@Override
					public ReceiptHeader answer() throws Throwable {
						receiptToBeSubmitted.setStatus(statusSubmitted);
						return receiptToBeSubmitted;
					}
				});
		EasyMock.replay(receiptWorkflowServiceMock);

		Long receiptId = receiptToBeSubmitted.getId();

		// Set receipts to be submitted
		Long receiptsToBeSubmitted[] = new Long[1];
		receiptsToBeSubmitted[0] = receiptId;
		action.setReceiptIds(receiptsToBeSubmitted);
		action.setRemarks("Submit Collections Test");

		// Submit the collection
		action.submitCollections();

		EasyMock.verify(receiptWorkflowServiceMock);
		assertTrue(receiptToBeSubmitted.getStatus().getCode().equals(
				CollectionConstants.RECEIPT_STATUS_CODE_SUBMITTED));
		assertTrue(action.getIsSubmitAction());
	}

	@Test
	public void testApprove() {
		EasyMock.expect(
				receiptWorkflowServiceMock.transition(EasyMock
						.isA(String.class), EasyMock.isA(StateAware.class),
						EasyMock.isA(String.class))).andAnswer(
				new IAnswer<ReceiptHeader>() {

					@Override
					public ReceiptHeader answer() throws Throwable {
						receiptToBeApproved.setStatus(statusApproved);
						return receiptToBeApproved;
					}
				});
		EasyMock.expect(
				receiptWorkflowServiceMock.end(EasyMock.isA(StateAware.class),
						EasyMock.isA(Position.class), EasyMock
								.isA(String.class))).andReturn(
				receiptToBeApproved);

		EasyMock.replay(receiptWorkflowServiceMock);

		Long receiptId = receiptToBeApproved.getId();

		// Set receipts to be submitted
		Long receiptsToBeApproved[] = new Long[1];
		receiptsToBeApproved[0] = receiptId;
		action.setReceiptIds(receiptsToBeApproved);
		action.setRemarks("Approve Collections Test");

		EasyMock.expect(
				eisCommonsManagerMock.getCurrentPositionByUser((User) EasyMock
						.isA(UserImpl.class))).andReturn(
				objectFactory.createPosition());
		EasyMock.replay(eisCommonsManagerMock);
		// Approve the collection
		action.approveCollections();
		EasyMock.verify(eisCommonsManagerMock);
		
		EasyMock.verify(receiptWorkflowServiceMock);
		assertTrue(receiptToBeApproved.getStatus().getCode().equals(
				CollectionConstants.RECEIPT_STATUS_CODE_APPROVED));
		assertTrue(action.getIsApproveAction());
	}

	//@Test
	public void testReject() {
		EasyMock.expect(
				receiptWorkflowServiceMock.transition(EasyMock
						.isA(String.class), EasyMock.isA(StateAware.class),
						EasyMock.isA(String.class))).andAnswer(
				new IAnswer<ReceiptHeader>() {

					@Override
					public ReceiptHeader answer() throws Throwable {
						receiptToBeApproved.setStatus(statusToBeSubmitted);
						return receiptToBeApproved;
					}
				});
		EasyMock.replay(receiptWorkflowServiceMock);

		Long receiptId = receiptToBeApproved.getId();

		// Set receipts to be submitted
		Long receiptsToBeRejected[] = new Long[1];
		receiptsToBeRejected[0] = receiptId;
		action.setReceiptIds(receiptsToBeRejected);
		action.setRemarks("Reject Collections Test");

		// Reject the collection
		action.rejectCollections();

		EasyMock.verify(receiptWorkflowServiceMock);
		assertTrue(receiptToBeApproved.getStatus().getCode().equals(
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED));
		assertTrue(action.getIsRejectAction());
	}

	*//**
	 * Checks whether the given list of receipts contains a receipt with given
	 * reference number.
	 * 
	 * @param receiptHeaders
	 *            List of receipt headers
	 * @param receiptNumber
	 *            Receipt number to be checked
	 * @return true if the list contains a receipt with given receipt number,
	 *         else false.
	 *//*
	private boolean containsReceipt(List<ReceiptHeader> receiptHeaders,
			String referenceNumber) {
		for (ReceiptHeader receiptHeader : receiptHeaders) {
			String refNum = receiptHeader.getReferencenumber();
			if (refNum != null && refNum.equals(referenceNumber)) {
				// Receipt with given number found. Return true.
				return true;
			}
		}

		// If we reach here, it means the list doesn't contain a receipt with
		// given number. Return false.
		return false;
	}
*/}
