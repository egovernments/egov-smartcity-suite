/**
 * 
 */
package org.egov.pgr.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.egov.eis.entity.DesignationMasterBuilder;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeBuilder;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.entity.EscalationBuilder;
import org.egov.pgr.repository.EscalationRepository;
import org.egov.pims.commons.DesignationMaster;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


/**
 * @author Vaibhav.K
 *
 */
public class EscalationServiceTest {    
   
    private EscalationService escalationService;
    
    @Mock
    private EscalationRepository escalationRepository;
    
    DesignationMaster designation;
    ComplaintType compType;
    Escalation escalation;
    
    @Before
    public void before()
    {
        initMocks(this);
       escalationService=new EscalationService(escalationRepository) ;
       sampleEscalation();
    }
    
    private void sampleEscalation() {
        designation = new DesignationMasterBuilder().withName("test-desig").withId(1).build();
        compType = new ComplaintTypeBuilder().withDefaults().build();
        escalation = new EscalationBuilder().withDesignation(designation).withComplaintType(compType).withHrs(23).build();
        escalationService.create(escalation);
    }
    
    @Test
    public void createEscalation() {
        assertNotNull(escalation);
    }
    
    @Test
    public void getNoOfHrs() {
        when(escalationRepository.findByDesignationAndComplaintType(designation.getDesignationId(),compType.getId())).thenReturn((escalation));
    
        Integer hrsToResolve = escalationService.getHrsToResolve(designation.getDesignationId(),compType.getId());
        assertEquals(hrsToResolve, Integer.valueOf(23));
    }

    
}
