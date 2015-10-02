package org.egov.tl.domain.repository;


import org.egov.tl.domain.entity.FeeMatrixDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface FeeMatrixDetailRepository extends JpaRepository<FeeMatrixDetail,Long> {

//FeeMatrixDetail findByName(String name);

}