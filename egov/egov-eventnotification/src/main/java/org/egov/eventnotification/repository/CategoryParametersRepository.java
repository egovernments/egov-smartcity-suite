package org.egov.eventnotification.repository;

import java.util.List;

import org.egov.eventnotification.entity.CategoryParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryParametersRepository extends JpaRepository<CategoryParameters, Long> {

    List<CategoryParameters> findByModuleCategoryId(Long id);

}
