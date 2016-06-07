/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.pgr.service;

import org.egov.eis.entity.DesignationBuilder;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeBuilder;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.entity.EscalationBuilder;
import org.egov.pgr.repository.EscalationRepository;
import org.egov.pims.commons.Designation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


/**
 * @author Vaibhav.K
 *
 */
public class EscalationServiceTest {    
   
    private EscalationService escalationService;
    
    @Mock
    private EscalationRepository escalationRepository;
    
    Designation designation;
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
        designation = new DesignationBuilder().withName("test-desig").withId(1l).build();
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
        when(escalationRepository.findByDesignationAndComplaintType(designation.getId(),compType.getId())).thenReturn((escalation));
    
        Integer hrsToResolve = escalationService.getHrsToResolve(designation.getId(),compType.getId());
        assertEquals(hrsToResolve, Integer.valueOf(23));
    }

    
}
