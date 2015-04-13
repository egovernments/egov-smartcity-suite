package org.egov.infra.admin.common.repository;

import org.egov.infra.admin.common.entity.Favourites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouritesRepository extends JpaRepository<Favourites, Long> {

}
