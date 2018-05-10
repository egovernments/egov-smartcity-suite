package org.egov.eventnotification.repository;

import java.util.List;

import org.egov.eventnotification.entity.CategoryParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryParametersRepository extends JpaRepository<CategoryParameters, Long> {
	
	@Query("select p from CategoryParameters p where p.moduleCategory.id = :category_id")
    public List<CategoryParameters> getParametersForCategory(@Param("category_id") Long categoryId);
	

}
