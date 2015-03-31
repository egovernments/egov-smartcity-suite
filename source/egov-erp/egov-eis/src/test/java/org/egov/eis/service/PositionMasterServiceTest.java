/**
 * 
 */
package org.egov.eis.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.egov.eis.EISAbstractSpringIntegrationTest;
import org.egov.eis.entity.PositionBuilder;
import org.egov.pims.commons.Position;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vaibhav.K
 *
 */
public class PositionMasterServiceTest extends EISAbstractSpringIntegrationTest{
	
	@Autowired
	private PositionMasterService positionMasterService;
	
	private Position position;
	
	private void samplePosition() {
		position = new PositionBuilder().withName("TEST_ACC_ASSISTANT").build();
		positionMasterService.createPosition(position);
	}
	
	@Test
	public void createPosition() {
		samplePosition();
		
		assertEquals("TEST_ACC_ASSISTANT",position.getName());
	}
	
	@Test
	public void updatePosition() {
		samplePosition();
		position.setPostOutsourced(false);
		positionMasterService.updatePosition(position);
		
		assertEquals(false,position.isPostOutsourced());
		
	}
	
	@Test
	public void getPositionsContainigName() {
		samplePosition();
		List<Position> posList = positionMasterService.getAllPositionsByNameLike("ASSISTANT");
		
		assertNotNull(posList);
	}
	
	@Test
	public void getAllPositions() {
		samplePosition();
		List<Position> posList = positionMasterService.getAllPositions();
		
		assertNotNull(posList);
	}

}
