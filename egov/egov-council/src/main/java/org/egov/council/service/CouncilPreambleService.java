package org.egov.council.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.council.entity.CouncilPreamble;
import org.egov.council.repository.CouncilPreambleRepository;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilPreambleService {

	private final CouncilPreambleRepository CouncilPreambleRepository;
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
    protected AutonumberServiceBeanResolver autonumberServiceBeanResolver;
	
	public Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	@Autowired
	public CouncilPreambleService(
			final CouncilPreambleRepository CouncilPreambleRepository) {
		this.CouncilPreambleRepository = CouncilPreambleRepository;
	}

	@Transactional
	public CouncilPreamble create(final CouncilPreamble councilPreamble) {
		return CouncilPreambleRepository.save(councilPreamble);
	}

	@Transactional
	public CouncilPreamble update(final CouncilPreamble CouncilPreamble) {
		return CouncilPreambleRepository.save(CouncilPreamble);
	}

	public CouncilPreamble findOne(Long id) {
		return CouncilPreambleRepository.findById(id);
	}
	
	public List<CouncilPreamble> search(CouncilPreamble councilPreamble) {
		final Criteria criteria = getCurrentSession().createCriteria(
				CouncilPreamble.class);
		if (null != councilPreamble.getDepartment())
			criteria.add(Restrictions.eq("department",
					councilPreamble.getDepartment()));
		return criteria.list();
	
		/*if(null != councilPreamble.getDepartment())
		return	CouncilPreambleRepository.findBydepartment(councilPreamble.getDepartment().getId());
		else
			return CouncilPreambleRepository.findAll();*/
	}
	
	
}