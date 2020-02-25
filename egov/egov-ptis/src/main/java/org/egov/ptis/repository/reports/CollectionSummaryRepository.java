package org.egov.ptis.repository.reports;

import org.egov.ptis.domain.entity.property.CollectionSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface CollectionSummaryRepository
        extends JpaRepository<CollectionSummary, Long>, JpaSpecificationExecutor<CollectionSummary> {

    Page<CollectionSummary> findAll(Pageable pageable);

}
