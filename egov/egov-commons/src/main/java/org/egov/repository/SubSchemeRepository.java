package org.egov.repository;


import org.egov.commons.SubScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface SubSchemeRepository extends JpaRepository<SubScheme,Long> {

SubScheme findByName(String name);

SubScheme findByCode(String code);

}
