package org.egov.tl.repository;

import java.util.Date;
import java.util.List;

import org.egov.tl.entity.LicenseNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface LicenseNoticeRepository extends JpaRepository<LicenseNotice, Long>{

	@Query("select ln from LicenseNotice ln where ln.noticeDate between :noticeFromDate and :noticeToDate")
	List<LicenseNotice> getNoticeByDateRange(@Param("noticeFromDate") Date noticeFromDate, @Param("noticeToDate") Date noticeToDate);
}
