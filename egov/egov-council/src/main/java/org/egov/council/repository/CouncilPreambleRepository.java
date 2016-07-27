package org.egov.council.repository;

import java.util.List;

import org.egov.council.entity.CouncilPreamble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouncilPreambleRepository extends
		JpaRepository<CouncilPreamble, Long> {

	CouncilPreamble findById(Long id);

	@Query("from CouncilPreamble where department.id=:department order by id desc")
	List<CouncilPreamble> findBydepartment(
			@Param("department") long department);
	
}
