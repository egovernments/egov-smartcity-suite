/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.model.service;


import org.egov.model.contra.TransactionSummary;
import org.egov.model.repository.TransactionSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


@Service 
@Transactional(readOnly = true)
public class TransactionSummaryService  {

	private final TransactionSummaryRepository transactionSummaryRepository;
	@PersistenceContext
private EntityManager entityManager;

	@Autowired
public TransactionSummaryService(final TransactionSummaryRepository transactionSummaryRepository) {
	 this.transactionSummaryRepository = transactionSummaryRepository;
  }

	 @Transactional
	 public TransactionSummary create(final TransactionSummary transactionSummary) {
	return transactionSummaryRepository.save(transactionSummary);
  } 
	 @Transactional
	 public TransactionSummary update(final TransactionSummary transactionSummary) {
	return transactionSummaryRepository.save(transactionSummary);
	  } 
	public List<TransactionSummary> findAll() {
	 return transactionSummaryRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	   }
	public TransactionSummary findOne(Long id){
	return transactionSummaryRepository.findOne(id);
	}
	@Transactional
	public void delete(final TransactionSummary transactionSummary) {
		transactionSummaryRepository.delete(transactionSummary);
	}

	public List<TransactionSummary> searchTransactionsForNonSubledger(Long finYear,
			Long fund, Long functn, Long department,Long glcodeId) {
		TypedQuery<TransactionSummary> query = entityManager.createQuery("select ts from TransactionSummary ts where ts.financialyear.id=:finYear and ts.fund.id=:fund and ts.functionid.id=:functn and ts.departmentid.id=:department and ts.glcodeid.id=:glcodeId and ts.glcodeid.id not in (select glCodeId.id from CChartOfAccountDetail ) ", TransactionSummary.class);
		query.setParameter("finYear", finYear);
		query.setParameter("fund", fund.intValue());
		query.setParameter("functn", functn);
		query.setParameter("department", department);
		query.setParameter("glcodeId", glcodeId);
		return query.getResultList();
	}
	
	public List<TransactionSummary> searchTransactionsForSubledger(Long finYear,
                Long fund, Long functn, Long department,Long glcodeId,Integer accountDetailTypeId,Integer accountDetailKeyId) {
        TypedQuery<TransactionSummary> query = entityManager.createQuery("select ts from TransactionSummary ts where ts.financialyear.id=:finYear and ts.fund.id=:fund and ts.functionid.id=:functn and ts.departmentid.id=:department and ts.glcodeid.id=:glcodeId and ts.accountdetailkey=:accountDetailKeyId and ts.accountdetailtype.id =:accountDetailTypeId ", TransactionSummary.class);
        query.setParameter("finYear", finYear);
        query.setParameter("fund", fund.intValue());
        query.setParameter("functn", functn);
        query.setParameter("department", department);
        query.setParameter("glcodeId", glcodeId);
        query.setParameter("accountDetailKeyId", accountDetailKeyId);
        query.setParameter("accountDetailTypeId", accountDetailTypeId);
        return query.getResultList();
}
	
	public TransactionSummary getTransactionSummary(Long glcodeId, Long accountDetailTypeId, Integer accountDetailKey) {
		TypedQuery<TransactionSummary> query = entityManager.createQuery("from TransactionSummary where glcodeid.id=:glcodeid and accountdetailtype.id=:accountdetailtype and accountdetailkey=:accountdetailkey", TransactionSummary.class);
		query.setParameter("glcodeid", glcodeId);
		query.setParameter("accountdetailtype", accountDetailTypeId.intValue());
		query.setParameter("accountdetailkey", accountDetailKey);
		List<TransactionSummary>  summaries = query.getResultList();
		if(!summaries.isEmpty())
			return summaries.get(0);
		else
			return null;
	}
}
