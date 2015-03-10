/**
 * 
 */
package org.egov.eis.service;

import java.util.List;

import org.egov.eis.repository.DesignationMasterRepository;
import org.egov.pims.commons.DesignationMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vaibhav.K
 *
 */

@Service
@Transactional(readOnly = true)
public class DesignationMasterService {
	
	private final DesignationMasterRepository designationMasterRepository;
	
	@Autowired
	public DesignationMasterService(final DesignationMasterRepository designationMasterRepository){
		this.designationMasterRepository=designationMasterRepository;
	}
	
	@Transactional
	public void createDesignation(DesignationMaster designation){
		designationMasterRepository.save(designation);
	}
	
	@Transactional
	public void updateDesignation(DesignationMaster designation){
		designationMasterRepository.save(designation);
	}
	
	@Transactional
	public void deleteDesignation(DesignationMaster designation){
		designationMasterRepository.delete(designation);
	}

	public DesignationMaster getDesignationByName(String desName){
		return designationMasterRepository.findByDesignationName(desName);
	}
	
	public DesignationMaster getDesignationById(final Integer desigId){
		return designationMasterRepository.findOne(desigId);
	}
	
	public List<DesignationMaster> getAllDesignations(){
		return designationMasterRepository.findAll();
	}
	
	public List<DesignationMaster> getAllDesignationsByNameLike(String name){
		return designationMasterRepository.findByDesignationNameContainingIgnoreCase(name);
	}
}
