/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

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