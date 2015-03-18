package org.egov.pgr.repository;

import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.entity.ComplaintType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRouterRepository extends JpaRepository<ComplaintRouter, Long> {

    public ComplaintRouter findByComplaintTypeAndBoundary(ComplaintType complaintType, BoundaryImpl location);

    @Query("select cr from ComplaintRouter cr where cr.complaintType=:complaintType and cr.boundary is null")
    public ComplaintRouter findByOnlyComplaintType(@Param("complaintType") ComplaintType complaintType);

    public ComplaintRouter findByBoundary(BoundaryImpl location);

    @Query("select cr from ComplaintRouter cr where   cr.boundary.parent is null")
    public ComplaintRouter findGrievanceOfficer();

}
