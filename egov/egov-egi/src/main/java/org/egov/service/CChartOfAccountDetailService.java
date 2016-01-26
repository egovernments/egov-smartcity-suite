package org.egov.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CChartOfAccountDetail;
import org.egov.repository.CChartOfAccountDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class CChartOfAccountDetailService  {

	private final CChartOfAccountDetailRepository cChartOfAccountDetailRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public CChartOfAccountDetailService(final CChartOfAccountDetailRepository cChartOfAccountDetailRepository) {
	 this.cChartOfAccountDetailRepository = cChartOfAccountDetailRepository;
  }

	 @Transactional
	 public CChartOfAccountDetail create(final CChartOfAccountDetail cChartOfAccountDetail) {
	return cChartOfAccountDetailRepository.save(cChartOfAccountDetail);
  } 
	 @Transactional
	 public CChartOfAccountDetail update(final CChartOfAccountDetail cChartOfAccountDetail) {
	return cChartOfAccountDetailRepository.save(cChartOfAccountDetail);
	  } 
	public List<CChartOfAccountDetail> findAll() {
	 return cChartOfAccountDetailRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public CChartOfAccountDetail findOne(Long id){
	return cChartOfAccountDetailRepository.findOne(id);
	}
}
