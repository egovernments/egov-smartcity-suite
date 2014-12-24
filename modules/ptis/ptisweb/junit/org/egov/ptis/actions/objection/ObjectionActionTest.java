package org.egov.ptis.actions.objection;


import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.egov.demand.model.DepreciationMaster;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.infstr.utils.ServiceLocator;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsManager;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisManager;
import org.egov.ptis.actions.view.ViewPropertyAction;
import org.egov.ptis.domain.entity.objection.Hearing;
import org.egov.ptis.domain.entity.objection.Objection;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.WorkflowBean;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.NMCObjectFactory;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.hibernate.mapping.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;

public class ObjectionActionTest extends AbstractPersistenceServiceTest{

	private ObjectionAction objectionAction;
	private NMCObjectFactory objectFactory;
	private WorkflowBean workflowBean;
	private static final ServiceLocator SERVICELOCATOR = ServiceLocator.getInstance();
	private PersistenceService persistenceService;
	
	@Before
	public void setUp() throws Exception {
		  objectFactory = new NMCObjectFactory(session, service);
		  objectionAction = new ObjectionAction();
		  EGOVThreadLocals.setUserId("1");
		  workflowBean = new WorkflowBean();
		  objectionAction.setWorkflowBean(workflowBean);
		 
			persistenceService = new PersistenceService();
			persistenceService.setSessionFactory(new SessionFactory()); 
			objectionAction.setPersistenceService(persistenceService);
			
	}
	@Test
	public void testGetModel(){
		assertTrue( objectionAction.getModel() instanceof Objection);
	}
	
	@Test
	public void testNewform(){
		 Property property  = getProperty();
		
		 objectionAction.setPropertyId(property.getBasicProperty().getUpicNo());
        ViewPropertyAction viewPropertyAction = Mockito.mock(ViewPropertyAction.class);
        Mockito.when(viewPropertyAction.viewForm()).thenReturn("view");
        Mockito.when(viewPropertyAction.getBasicProperty()).thenReturn(property.getBasicProperty());
		objectionAction.setViewPropertyAction(viewPropertyAction);
		PropertyTaxUtil propertyTaxUtil = Mockito.mock(PropertyTaxUtil.class);
		Mockito.when(propertyTaxUtil.getDepartmentsForLoggedInUser(new HashMap<String, Object>())).thenReturn(Collections.EMPTY_LIST);
		objectionAction.setPropertyTaxUtil(propertyTaxUtil);
        //objectionAction.newForm();
       
	}
	
	
	@Test
	public void testCreate(){
		
		Objection objection = getObjection();
		objectionAction.setObjection(objection);
		objectionAction.setSequenceGenerator(new SequenceGenerator());
	  
	    PersistenceService<Objection, Long> objectionService = Mockito.mock(PersistenceService.class);
        Mockito.when(objectionService.persist(objection)).thenReturn(objection);
        objectionAction.setObjectionService(objectionService);
        EisCommonsManager eisCommonsManager = Mockito.mock(EisCommonsManager.class);
        Mockito.when(eisCommonsManager.getPositionByUserId(Integer.valueOf(1))).thenReturn(new Position());
        objectionAction.setEisCommonsManager(eisCommonsManager);
        WorkflowBean workflowBean = new WorkflowBean();
        workflowBean.setActionName("SAVE");
        objectionAction.setWorkflowBean(workflowBean);
        WorkflowService<Objection> objectionWorkflowService = Mockito.mock(WorkflowService.class);
        Mockito.when(objectionWorkflowService.start(objection, new Position())).thenReturn(objection);
        objectionAction.setObjectionWorkflowService(objectionWorkflowService);
        EisManager eisManager = Mockito.mock(EisManager.class);
        PersonalInformation pi =  new PersonalInformation();
        pi.setEmployeeFirstName("");
        Mockito.when(eisManager.getEmployeeforPosition(Mockito.any(Position.class))).thenReturn(pi);
        objectionAction.setEisManager(eisManager);
        
        PropertyTaxUtil propertyTaxUtil = Mockito.mock(PropertyTaxUtil.class);
        Mockito.when(propertyTaxUtil.getDepartmentsForLoggedInUser((java.util.Map<String, Object>) Mockito.any(Map.class))).
        						thenReturn(Mockito.any(List.class));
        objectionAction.setPropertyTaxUtil(propertyTaxUtil);
        
		objectionAction.create();
		
	}
	
	@Test
	public void testAddHearingDate(){
		Objection objection = getObjection();
		Hearing hearing = new Hearing();
		hearing.setPlannedHearingDt(new Date());
		hearing.setInspectionRequired(Boolean.TRUE);
		List<Hearing> hearings = new ArrayList<Hearing>();
		hearings.add(hearing);
		objection.setRecievedOn(new Date());
		objection.setHearings(hearings);
		objectionAction.setObjection(objection);
		objectionAction.addHearingDate();
	}
	@Test
	public void testRecordHearingDetails(){
		Objection objection = getObjection();
		Hearing hearing = new Hearing();
		hearing.setInspectionRequired(Boolean.TRUE);
		List<Hearing> hearings = new ArrayList<Hearing>();
		hearings.add(hearing);
		objection.setHearings(hearings);
		objectionAction.setObjection(objection);
		objectionAction.setSequenceGenerator(new SequenceGenerator());
		
		objectionAction.recordHearingDetails();
	}
	
	@Test
	public void testRecordInspectionDetails(){
		Objection objection = getObjection();
		objectionAction.setObjection(objection);
		objectionAction.recordInspectionDetails();
	}
	
	@Test
	public void testRecordObjectionOutcome(){
		Objection objection = getObjection();
		objection.setObjectionRejected(Boolean.FALSE);
		User user = new UserImpl();
		user.setId(-1);
		objection.setCreatedBy(user);
		objectionAction.setObjection(objection);
		WorkflowBean wfBean = new WorkflowBean();
		wfBean.setActionName("Approve");
		objectionAction.setWorkflowBean(wfBean);
		
		PropertyService propService = Mockito.mock(PropertyService.class);
		objectionAction.setPropService(propService);
		
		EisCommonsManager eisCommonsManager = Mockito.mock(EisCommonsManager.class);
	    Mockito.when(eisCommonsManager.getPositionByUserId(Integer.valueOf(1))).thenReturn(new Position());
	    objectionAction.setEisCommonsManager(eisCommonsManager);
		
	    EisManager eisManager = Mockito.mock(EisManager.class);
        PersonalInformation pi =  new PersonalInformation();
        pi.setEmployeeFirstName("");
        Mockito.when(eisManager.getEmpForUserId(Mockito.any(Integer.class))).thenReturn(pi);
        objectionAction.setEisManager(eisManager);
        PersistenceService<Objection, Long> objectionService = Mockito.mock(PersistenceService.class);
        Mockito.when(objectionService.update(objection)).thenReturn(objection);
        objectionAction.setObjectionService(objectionService);
		objectionAction.recordObjectionOutcome();
	}
	/**
	 * @return
	 */
	private Objection getObjection() {
		Objection objection = new Objection();
		objection.setDocNumberObjection("100");
		objection.setRecievedBy("test");
		objection.setRecievedOn(new Date());
		objection.setDetails("test");
		Property property  = getProperty();
		objection.setBasicProperty(property.getBasicProperty());
		
		return objection;
	}
	
	/**
	 * @param category
	 */
	private Property getProperty() {
		Category category = objectFactory.createCategory("1.25");
		DepreciationMaster depreciationMaster = objectFactory.getDepreciationMaster("A", "1");
        Property property = (Property) objectFactory.createProperty(NMCPTISConstants.PROPTYPE_RESD, category
                .getCatBoundaries().iterator().next().getBndry(), "SELFOCC", "Owner", "1", "1.25", "1", "UFD1",
                "Residential houses", depreciationMaster, "205.7", new Date(), new Date());
      
       
        return property;
	}
}
