package org.egov.works.mb.repository;

import java.util.Date;
import java.util.List;

import org.egov.works.mb.entity.MBDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MBDetailsRepository extends JpaRepository<MBDetails, Long>, RevisionRepository<MBDetails, Long, Integer> {

    @Query("select mbDetails.workOrderActivity.id,sum(mbDetails.quantity) from MBDetails mbDetails where mbDetails.mbHeader.egBillregister.id =:contractorBillId and mbDetails.mbHeader.egwStatus.code = :status group by mbDetails.workOrderActivity.id ")
    List<Object[]> getActivitiesByContractorBill(@Param("contractorBillId") Long contractorBillId,
            @Param("status") String status);

    @Query("select mbDetails from MBDetails mbDetails where mbDetails.mbHeader.egwStatus.code = :status and mbDetails.mbHeader.workOrderEstimate.id =:workOrderEstimateId and mbDetails.mbHeader.egBillregister.createdDate < :billCreatedDate and mbDetails.mbHeader.egBillregister.status.code = :status ")
    List<MBDetails> getActivitiesByContractorBillTillDate(@Param("workOrderEstimateId") Long workOrderEstimateId,
            @Param("status") String status, @Param("billCreatedDate") Date billCreatedDate);

    @Query("select distinct(mbdetails) from MBDetails as mbdetails where mbdetails.workOrderActivity.id =:woaId and mbHeader.egwStatus.code =:status")
    List<MBDetails> getMBDetailsByWorkOrderActivity(@Param("woaId") Long woaId, @Param("status") String status);

    @Query("select mbDetails.workOrderActivity.activity.id,sum(mbDetails.quantity) from MBDetails mbDetails where mbHeader.egwStatus.code!='CANCELLED' and mbDetails.workOrderActivity.activity.id in (:activityIdList) group by mbDetails.workOrderActivity.activity.id")
    List<Object[]> getMBActivitiesForRevisionEstimate(@Param("activityIdList") List<Long> activityIdList);

    @Query("select mbd from MBDetails mbd where mbd.mbHeader.egwStatus.code !=:status and mbd.workOrderActivity.activity.id =:activityId and exists (select a from Activity a where a.parent.id = :activityId and a.abstractEstimate.id=:revisionEstimateId and mbd.workOrderActivity.activity.id = a.parent.id)")
    MBDetails getMBDetailsForREActivity(@Param("activityId") Long activityId,
            @Param("revisionEstimateId") Long revisionEstimateId, @Param("status") String status);

    List<MBDetails> findByMbHeader_Id(Long mbId);
}
