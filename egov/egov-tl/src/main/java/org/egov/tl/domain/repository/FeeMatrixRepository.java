package org.egov.tl.domain.repository;


import java.util.List;

import org.egov.tl.domain.entity.FeeMatrix;
import org.egov.tl.domain.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface FeeMatrixRepository extends JpaRepository<FeeMatrix,Long>,FeeMatrixRepositoryCustom {

	FeeMatrix findByUniqueNo(String uniqueNo);

	FeeMatrix findByUniqueNoLike(String uniqueNo);

	

}