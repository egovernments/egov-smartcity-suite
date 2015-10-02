package org.egov.tl.domain.repository;


import org.egov.tl.domain.entity.FeeMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface FeeMatrixRepositoryCustom  {
	FeeMatrix findByExample(FeeMatrix feeMatrix);

}