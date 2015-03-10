/**
 * 
 */
package org.egov.eis.service;

import java.util.List;

import org.egov.eis.repository.PositionMasterRepository;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	public Position getPositionById(final Integer posId){
		return positionMasterRepository.findOne(posId);
	}
	
	public List<Position> getAllPositions(){
		return positionMasterRepository.findAll();
	}
	
	public List<Position> getAllPositionsByNameLike(String name){
		return positionMasterRepository.findByNameContainingIgnoreCase(name);
	}
	
}
