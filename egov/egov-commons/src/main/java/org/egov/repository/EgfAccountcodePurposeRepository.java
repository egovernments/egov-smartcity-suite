package org.egov.repository;


import org.egov.commons.EgfAccountcodePurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface EgfAccountcodePurposeRepository extends JpaRepository<EgfAccountcodePurpose,Long> {

EgfAccountcodePurpose findByName(String name);

}
