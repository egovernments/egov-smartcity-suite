package org.egov.commons.repository;


import java.util.List;

import org.egov.commons.Accountdetailtype;
import org.egov.masters.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository 
public interface AccountEntityRepository extends JpaRepository<AccountEntity,Integer> {
	AccountEntity findByName(String name);
	AccountEntity findByCode(String Code);
	List<AccountEntity> findByAccountdetailtype(Accountdetailtype accountdetailtype);

}