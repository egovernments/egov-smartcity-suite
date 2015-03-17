/**
 * 
 */
package org.egov.eis.service;

import static org.junit.Assert.assertNotNull;

import org.egov.eis.EISAbstractSpringIntegrationTest;
import org.egov.pims.commons.Position;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vaibhav.K
 *
 */
public class EisCommonServiceTest extends EISAbstractSpringIntegrationTest{
    
    @Autowired
    private EisCommonService eisCommonService;
    
    @Test
    public void getSuperiorPosByObjectAndPos() {
        Position pos = eisCommonService.getSuperiorPositionByObjectTypeAndPositionFrom(7, 22);
        assertNotNull("EXECUTIVE ENGINEER",pos.getName());
        
    }

}
