/**
 * 
 */
package org.egov.collection.services;


/**
 * JUnit Test cases for Receipt Header Service
 */
public class ChallanServiceTest { /* extends
		AbstractPersistenceServiceTest<Challan, Long> {
	private ChallanService challanService;
	private WorkflowService<Challan> challanWorkflowService;
	private CollectionObjectFactory objectFactory;
	private CollectionsUtil collectionsUtil;
	private Position position;

	@Before
	public void setupService() {

		objectFactory = new CollectionObjectFactory(session);
		position = objectFactory.createPosition();
		collectionsUtil = new CollectionsUtil(){
			public Position getPositionOfUser(User user) {
				return position;
			}
			public Position getPositionByName(String positionName) {
				return position;
			}
		};
		
		collectionsUtil.setPersistenceService(genericService);
		
		challanService = new ChallanService();
		challanService.setCollectionsUtil(collectionsUtil);
		challanWorkflowService = new SimpleWorkflowService<Challan>(
				challanService, Challan.class);
		challanService.setChallanWorkflowService(challanWorkflowService);
		
	}
	
	@Test
	public void testWorkflowTransition(){
		Challan challan = objectFactory.createChallan();
		Position position = objectFactory.createPosition();
		
		// create challan
		challanService.workflowtransition(
				challan, position, 
				CollectionConstants.WF_ACTION_NAME_NEW_CHALLAN, 
				"Test Comment - Challan Created.");
		
		State challanState = challan.getState();
		
		assertEquals(CollectionConstants.WF_STATE_CREATE_CHALLAN, challanState.getValue());
		assertEquals(CollectionConstants.WF_ACTION_NAME_CHECK_CHALLAN, challanState.getNextAction());
		assertEquals(position, challanState.getOwner());
		assertTrue(CollectionConstants.CHALLAN_CREATION_REMARKS.equals(challanState.getText1()));
		assertEquals(State.NEW, challanState.getPrevious().getValue());
		assertEquals("Challan Workflow Started", challanState.getPrevious().getText1());
		assertEquals(CollectionConstants.CHALLAN_STATUS_CODE_CREATED, challan.getStatus().getCode());
		
		// check challan
		challanService.workflowtransition(
				challan, position, 
				CollectionConstants.WF_ACTION_NAME_CHECK_CHALLAN, 
				"Test Comment - Challan Checked.");		
		challanState = challan.getState();
		
		assertEquals(CollectionConstants.WF_STATE_CHECK_CHALLAN, challanState.getValue());
		assertEquals(CollectionConstants.WF_ACTION_NAME_APPROVE_CHALLAN, challanState.getNextAction());
		assertEquals("Test Comment - Challan Checked.", challanState.getText1());
		assertEquals(CollectionConstants.WF_STATE_CREATE_CHALLAN, challanState.getPrevious().getValue());
		assertEquals(CollectionConstants.CHALLAN_STATUS_CODE_CHECKED, challan.getStatus().getCode());
		
		// approve challan
		challanService.workflowtransition(
				challan, position, 
				CollectionConstants.WF_ACTION_NAME_APPROVE_CHALLAN, 
				"Test Comment - Challan Approved.");		
		challanState = challan.getState();
		
		assertEquals(CollectionConstants.WF_STATE_APPROVE_CHALLAN, challanState.getValue());
		assertEquals(CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN, challanState.getNextAction());
		assertEquals(CollectionConstants.WF_STATE_CHECK_CHALLAN, challanState.getPrevious().getValue());
		assertEquals("Test Comment - Challan Approved.", challanState.getText1());
		assertEquals(CollectionConstants.CHALLAN_STATUS_CODE_APPROVED, challan.getStatus().getCode());
		
		// validate challan
		challanService.workflowtransition(challan, position, 
				CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN, "Test Comment - Challan Validated.");
		challanState = challan.getState();
		
		assertEquals(State.END, challanState.getValue());
		assertEquals(null, challanState.getNextAction());
		assertEquals(CollectionConstants.WF_STATE_VALIDATE_CHALLAN, challanState.getPrevious().getValue());
		assertEquals("End of challan worklow", challanState.getText1());
		assertEquals("Test Comment - Challan Validated.", challanState.getPrevious().getText1());
		assertEquals(CollectionConstants.CHALLAN_STATUS_CODE_VALIDATED, challan.getStatus().getCode());
		
		// modify challan
		challanService.workflowtransition(
				challan, position, 
				CollectionConstants.WF_ACTION_NAME_MODIFY_CHALLAN, 
				"Test Comment - Challan modified.");		
		challanState = challan.getState();
		
		assertEquals(CollectionConstants.WF_STATE_CREATE_CHALLAN, challanState.getValue());
		assertTrue(CollectionConstants.CHALLAN_CREATION_REMARKS.equals(challanState.getText1()));
		assertEquals(CollectionConstants.CHALLAN_STATUS_CODE_CREATED, challan.getStatus().getCode());
		
		// reject challan
		challanService.workflowtransition(
				challan, position, 
				CollectionConstants.WF_ACTION_NAME_REJECT_CHALLAN, 
				"Test Comment - Challan REJECTED.");		
		challanState = challan.getState();
		
		assertEquals(CollectionConstants.WF_STATE_REJECTED_CHALLAN, challanState.getValue());
		assertEquals("Test Comment - Challan REJECTED.", challanState.getText1());
		assertEquals(CollectionConstants.CHALLAN_STATUS_CODE_REJECTED, challan.getStatus().getCode());
		
		// cancel challan
		challan.setReasonForCancellation("test challan cancelled");
		challanService.workflowtransition(challan, position, 
				CollectionConstants.WF_ACTION_NAME_CANCEL_CHALLAN, "");
		challanState = challan.getState();
		
		assertEquals(State.END, challanState.getValue());
		assertEquals(null, challanState.getNextAction());
		assertEquals(CollectionConstants.WF_STATE_CANCEL_CHALLAN, challanState.getPrevious().getValue());
		assertEquals("End of challan worklow", challanState.getText1());
		assertEquals(challan.getReasonForCancellation(), challanState.getPrevious().getText1());
		assertEquals(CollectionConstants.CHALLAN_STATUS_CODE_CANCELLED, challan.getStatus().getCode()); 
		
		
	}*/
}
