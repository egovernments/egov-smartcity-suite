package org.egov.assets.repository;


import org.egov.assets.model.CategoryPropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface CategoryPropertyTypeRepository extends JpaRepository<CategoryPropertyType,java.lang.Long> {

	CategoryPropertyType findByName(String name);
	@Override
	public void delete(Long id);

}