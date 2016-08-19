package org.egov.works.mb.repository;

import java.util.Date;
import java.util.List;

import org.egov.works.mb.entity.MBDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MBDetailsRepository extends JpaRepository<MBDetails, Long> {
   
    @Query("select mbDetails.workOrderActivity.id,sum(mbDetails.quantity) from MBDetails mbDetails where mbDetails.mbHeader.egBillregister.id =:contractorBillId and mbDetails.mbHeader.egwStatus.code = :status group by mbDetails.workOrderActivity.id ")
    List<Object[]> getActivitiesByContractorBill(@Param("contractorBillId") Long contractorBillId , @Param("status") String status);
    
    @Query("select mbDetails from MBDetails mbDetails where mbDetails.mbHeader.egwStatus.code = :status and mbDetails.mbHeader.workOrderEstimate.id =:workOrderEstimateId and mbDetails.mbHeader.egBillregister.createdDate < :billCreatedDate and mbDetails.mbHeader.egBillregister.status.code = :status ")
    List<MBDetails> getActivitiesByContractorBillTillDate(@Param("workOrderEstimateId") Long workOrderEstimateId,@Param("status") String status,@Param("billCreatedDate") Date billCreatedDate);
}
