package org.egov.model.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.egov.model.contra.TransactionSummary;
import org.egov.model.repository.TransactionSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
