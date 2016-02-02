package org.egov.commons.repository;


import java.util.List;


import org.egov.commons.Accountdetailtype;
import org.egov.masters.model.AccountEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


@Repository 
public interface AccountEntityRepository extends JpaRepository<AccountEntity,Integer> {
	AccountEntity findByName(String name);
	AccountEntity findByCode(String Code);
	List<AccountEntity> findByAccountdetailtype(Accountdetailtype accountdetailtype);
	List<AccountEntity> findByAccountdetailtypeAndIsactive(Accountdetailtype accountdetailtype,boolean isactive);
	Page<AccountEntity> findByAccountdetailtypeAndNameContainingIgnoreCaseOrCodeContainingIgnoreCase(Accountdetailtype accountdetailtype, String name,String code,Pageable pageable);

	@Query("from AccountEntity  where accountdetailtype.id=:detailTypeId and ((upper(code) like upper(:filterkey) or upper(name) like upper(:filterkey))  and isactive=true)   order by code,name")
	List<AccountEntity> findBy20(@Param("detailTypeId") Integer typeId,@Param("filterkey")  String key);
}