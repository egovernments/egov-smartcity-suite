/**
 * 
 */
package org.egov.eis.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.egov.eis.EISAbstractSpringIntegrationTest;
import org.egov.infra.admin.master.entity.User;
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
        assertEquals("Zonal Officer",pos.getName());
        
    }
    
    @Test
    public void getUserForPosition() {
        User usr = eisCommonService.getUserForPositionId(1, new Date());
           assertEquals("egovernments",usr.getUsername());
    }

}
