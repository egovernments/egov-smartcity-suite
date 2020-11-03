/**
 * 
 */
package org.egov.tl.repository;

import org.egov.tl.entity.LicenseDemandVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Pabitra
 *
 */

@Repository
public interface LicenseDemandVoucherRepository extends JpaRepository<LicenseDemandVoucher, Long>{

}
