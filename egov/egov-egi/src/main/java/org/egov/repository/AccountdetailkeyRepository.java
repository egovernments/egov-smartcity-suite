package org.egov.repository;


import org.egov.commons.Accountdetailkey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface AccountdetailkeyRepository extends JpaRepository<Accountdetailkey,Long> {

//Accountdetailkey findByName(String name);

}
