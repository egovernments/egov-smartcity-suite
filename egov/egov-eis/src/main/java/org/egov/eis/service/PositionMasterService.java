/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.eis.service;

import java.util.List;

import org.egov.eis.repository.PositionMasterRepository;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vaibhav.K
 *
 */
@Service
@Transactional(readOnly = true)
public class PositionMasterService {

	private final PositionMasterRepository positionMasterRepository;
	
	@Autowired
	public PositionMasterService(final PositionMasterRepository positionMasterRepository) {
		this.positionMasterRepository = positionMasterRepository;
	}
	
	@Transactional
	public void createPosition(Position position){
		positionMasterRepository.save(position);
	}
	
	@Transactional
	public void updatePosition(Position position){
		positionMasterRepository.save(position);
	}
	
	@Transactional
	public void deletePosition(Position position){
		positionMasterRepository.delete(position);
	}

	public Position getPositionByName(String name){
		return positionMasterRepository.findByName(name);
	}
	
	public Position getPositionById(final Long posId){
		return positionMasterRepository.findOne(posId);
	}
	
	public List<Position> getAllPositions(){
		return positionMasterRepository.findAll();
	}
	
	public List<Position> getAllPositionsByNameLike(String name){
		return positionMasterRepository.findByNameContainingIgnoreCase(name);
	}

	public List<Position> getAllPositionsByDeptDesigId(Long deptDesigId){
		return positionMasterRepository.findAllByDeptDesig_Id(deptDesigId);
	}
	
	public boolean validatePosition(Position position) {
		if (position != null && position.getName() != null) {
			List<Position> positionList = positionMasterRepository
					.findByNameContainingIgnoreCase(position.getName());
			if (positionList.size() > 0)
				return false;
		}
		return true;
	}

	public Page<Position> getPageOfPositions(Integer pageNumber,
			Integer pageSize, Long departmentId, Long designationId) {
		final Pageable pageable = new PageRequest(pageNumber - 1, pageSize,
				Sort.Direction.ASC, "name");
		if (departmentId != 0 && designationId != 0)
			return positionMasterRepository
					.findPositionBydepartmentAndDesignation(departmentId,
							designationId, pageable);
		else if (departmentId != 0 )
			return positionMasterRepository
					.findPositionBydepartment(departmentId,
							 pageable);
		else if (designationId != 0 )
			return positionMasterRepository
					.findPositionByDesignation(
							designationId, pageable);
		else
			return positionMasterRepository.findPositionByAll(pageable);
	}

	public List<Position> findByNameContainingIgnoreCase(String positionName) {
		return positionMasterRepository
				.findByNameContainingIgnoreCase(positionName);
	}

	public Integer getTotalOutSourcedPosts(Long departmentId,
			Long designationId) {
		
		if (departmentId != 0 && designationId != 0)
			return positionMasterRepository
					.getTotalOutSourcedPostsByDepartmentAndDesignation(departmentId,
							designationId);
		else if (designationId != 0 )
			return positionMasterRepository.getTotalOutSourcedPostsByDesignation(designationId);
		else if (departmentId != 0 )
			return positionMasterRepository.getTotalOutSourcedPostsByDepartment(departmentId);
		else
			return positionMasterRepository.getTotalOutSourcedPosts();
		
	}
	public Integer getTotalSanctionedPosts(Long departmentId,
			Long designationId) {
		
		if (departmentId != 0 && designationId != 0)
			return positionMasterRepository
					.getTotalSanctionedPostsByDepartmentAndDesignation(departmentId,
							designationId);
		else if (designationId != 0 )
			return positionMasterRepository.getTotalSanctionedPostsByDesignation(designationId);
		else if (departmentId != 0 )
			return positionMasterRepository.getTotalSanctionedPostsByDepartment(departmentId);
		else
			return positionMasterRepository.getTotalSanctionedPosts();
		
	}
}
