package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Vouchermis;
import org.egov.repository.VouchermisRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class VouchermisService  {

	private final VouchermisRepository vouchermisRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public VouchermisService(final VouchermisRepository vouchermisRepository) {
	 this.vouchermisRepository = vouchermisRepository;
  }

	 @Transactional
	 public Vouchermis create(final Vouchermis vouchermis) {
	return vouchermisRepository.save(vouchermis);
  } 
	 @Transactional
	 public Vouchermis update(final Vouchermis vouchermis) {
	return vouchermisRepository.save(vouchermis);
	  } 
	public List<Vouchermis> findAll() {
	 return vouchermisRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public Vouchermis findOne(Long id){
	return vouchermisRepository.findOne(id);
	}
}
