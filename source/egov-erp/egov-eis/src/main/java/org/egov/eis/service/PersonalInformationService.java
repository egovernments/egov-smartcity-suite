/**
 * 
 */
package org.egov.eis.service;

import org.egov.eis.repository.PersonalInformationRepository;
import org.egov.pims.model.PersonalInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vaibhav.K
 *
 */
@Service
@Transactional(readOnly = true)
public class PersonalInformationService {
	
	private final PersonalInformationRepository repository;
	
	@Autowired
	public PersonalInformationService(final PersonalInformationRepository repository) {
		this.repository = repository;
	}
	
	public PersonalInformation getEmployeeByUserId(Long userId) {
		return repository.getPersonalInformationByUserId(userId);
	}
	
	public PersonalInformation getEmployeeById(Integer empId) {
		return repository.findByIdPersonalInformation(empId);
	}
	
	public void createEmployee(PersonalInformation personalInformation) {
	    repository.saveAndFlush(personalInformation); 
	}
	
	

}
