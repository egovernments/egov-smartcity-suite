/**
 * 
 */
package org.egov.eis.service;

import static org.junit.Assert.*;

import java.util.List;

import org.egov.eis.EISAbstractSpringIntegrationTest;
import org.egov.eis.entity.DesignationMasterBuilder;
import org.egov.pims.commons.DesignationMaster;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vaibhav.K
 *
 */

public class DesignationMasterServiceTest extends EISAbstractSpringIntegrationTest{
	
	@Autowired	
	private DesignationMasterService designationMasterService;
	
	private DesignationMaster designation;
	
	private void sampleDesignation()	{
		designation = new DesignationMasterBuilder().withName("ASSISTANT1").withDescription("ASSISTANT1").build();
		designationMasterService.createDesignation(designation);
	}
	
	@Test
	public void createDesignation()	{
		sampleDesignation();		
		
		assertEquals("ASSISTANT1",designationMasterService.getDesignationByName("ASSISTANT1").getDesignationName());
	}
	
	@Test
	public void updateDesignation()	{
		sampleDesignation();
		designation.setDesignationDescription("FORTESTING");
		designationMasterService.updateDesignation(designation);
		
		assertEquals("FORTESTING", designation.getDesignationDescription());
	}
	
	@Test
	public void getDesignationsContainingName()	{
		sampleDesignation();
		List<DesignationMaster> desigList = designationMasterService.getAllDesignationsByNameLike("ASSISTANT");
		
		assertNotNull(desigList);
	}
	
	@Test
	public void getAllDesignations() {
		sampleDesignation();
		List<DesignationMaster> desigList = designationMasterService.getAllDesignations();
		
		assertNotNull(desigList);
	}

}
