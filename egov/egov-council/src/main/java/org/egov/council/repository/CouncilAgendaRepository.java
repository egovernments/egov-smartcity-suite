package org.egov.council.repository;

import org.egov.council.entity.CouncilAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouncilAgendaRepository extends JpaRepository<CouncilAgenda, Long> {

    CouncilAgenda findByAgendaNumber(String agendaNumber);

    CouncilAgenda findById(Long id);

}