package org.egov.tl.domain.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.egov.tl.domain.entity.FeeMatrix;
import org.egov.tl.domain.entity.FeeMatrixDetail;
import org.egov.tl.domain.entity.License;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public class FeeMatrixDetailRepositoryImpl implements FeeMatrixDetailRepositoryCustom{
	@PersistenceContext
	private EntityManager entityManager;
	@Override
	public List<FeeMatrixDetail> findFeeList(License license) {
		CriteriaBuilder builder=entityManager.getCriteriaBuilder();
		CriteriaQuery<FeeMatrixDetail> createQuery = builder.createQuery(FeeMatrixDetail.class);
		Root<FeeMatrixDetail> from = createQuery.from(FeeMatrixDetail.class);
		
		// TODO Auto-generated method st
		return null;
	}
	
public	FeeMatrixDetail findFeeDetailList(FeeMatrix feeMatrix, Integer uom, Date appdate,long financialYearId)
	{
	
		FeeMatrixDetail fmd=null;
	String qlString="select fd from  FeeMatrixDetail fd  where fd.feeMatrix=:feeMatrix and :uom >=uomFrom and :uom <=uomTo and fd.feeMatrix.financialYear.id=:financialYearId "
			+ " order by fd.id desc";
	List l=	entityManager.createQuery(qlString).setParameter("feeMatrix", feeMatrix)
		.setParameter("uom", uom).setParameter("financialYearId", financialYearId).getResultList();
	
	
	
	if(!l.isEmpty())
	fmd=(FeeMatrixDetail)l.get(0);
	
	return fmd;
		
		
	

	}

}