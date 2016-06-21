package org.egov.council.repository;

import org.egov.council.entity.CouncilMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouncilMemberRepository extends JpaRepository<CouncilMember, Long> {

    CouncilMember findByName(String name);

    CouncilMember findById(Long id);

}