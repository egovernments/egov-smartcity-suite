/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.adtax.entity;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EGADTAX_MSTR_PENALTYRATES")
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = AdvertisementPenaltyRates.SEQ_PENALTYRATES, sequenceName = AdvertisementPenaltyRates.SEQ_PENALTYRATES, allocationSize = 1)
public class AdvertisementPenaltyRates extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 6781333136218089016L;
    public static final String SEQ_PENALTYRATES = "SEQ_EGADTAX_PENALTYRATES";
    @Id
    @GeneratedValue(generator = SEQ_PENALTYRATES, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Audited
    private Double rangeFrom;
   
    @Audited
    private Double rangeTo;

    @Audited
    private Double percentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public Double getRangeFrom() {
        return rangeFrom;
    }

    public void setRangeFrom(Double rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    
    public Double getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(Double rangeTo) {
        this.rangeTo = rangeTo;
    }

    
    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

   
}