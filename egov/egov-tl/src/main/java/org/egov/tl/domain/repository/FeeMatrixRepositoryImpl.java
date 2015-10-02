package org.egov.tl.domain.repository;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.tl.domain.entity.FeeMatrix;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository 
public class FeeMatrixRepositoryImpl implements FeeMatrixRepositoryCustom {

	@PersistenceContext
	EntityManager entiyManager;
	@Override
	@Transactional(readOnly=true)
	public FeeMatrix findByExample(FeeMatrix feeMatrix) {     
		Example feeMatrixExample=Example.create(feeMatrix);
		Criteria cr=entiyManager.unwrap(Session.class).createCriteria(FeeMatrix.class);
		cr.add(feeMatrixExample);
		List feeMatriList = cr.list();
		if(feeMatriList.size()>0)
		  return (FeeMatrix)feeMatriList.get(0);
		else
			return null;
		
	}
	


}