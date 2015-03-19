/**
 * 
 */
package org.egov.eis.service;

import static org.junit.Assert.assertNotNull;

import org.egov.eis.EISAbstractSpringIntegrationTest;
import org.egov.pims.model.PersonalInformation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vaibhav.K
 *
 */
public class PersonalInformationServiceTest extends EISAbstractSpringIntegrationTest {

	@Autowired
	private PersonalInformationService personalInformationService;
	
	@Test
	public void getEmployeeByUserId() {		
		PersonalInformation emp = personalInformationService.getEmployeeByUserId(1l);		
		
		assertNotNull(emp);
	}
	
	@Test
	public void getEmployeeByEmpId() {
		PersonalInformation emp = personalInformationService.getEmployeeById(1);
		
		assertNotNull(emp);
	}
}
