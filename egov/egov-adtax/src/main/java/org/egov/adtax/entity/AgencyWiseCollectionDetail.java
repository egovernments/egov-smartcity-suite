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

import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.persistence.entity.AbstractPersistable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "EGADTAX_AGENCYWISECOLLECTION_DETAIL")
@SequenceGenerator(name = AgencyWiseCollectionDetail.SEQ_AGENCYWISEHOARDINGCOLLECTIONDTL, sequenceName = AgencyWiseCollectionDetail.SEQ_AGENCYWISEHOARDINGCOLLECTIONDTL, allocationSize = 1)
public class AgencyWiseCollectionDetail extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -5269599651436939067L;

    public static final String SEQ_AGENCYWISEHOARDINGCOLLECTIONDTL = "SEQ_EGADTAX_AGENCYWISECOLLECTIONDTL";

    @Id
    @GeneratedValue(generator = SEQ_AGENCYWISEHOARDINGCOLLECTIONDTL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "agencywisecollection", nullable = false)
    private AgencyWiseCollection agencyWiseCollection;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "demand", nullable = false)
    private EgDemand demand;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "demandDetail")
    private EgDemandDetails demandDetail;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "demandreason", nullable = false)
    private EgDemandReason demandreason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AgencyWiseCollection getAgencyWiseCollection() {
        return agencyWiseCollection;
    }

    public void setAgencyWiseCollection(AgencyWiseCollection agencyWiseCollection) {
        this.agencyWiseCollection = agencyWiseCollection;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public EgDemand getDemand() {
        return demand;
    }

    public void setDemand(EgDemand demand) {
        this.demand = demand;
    }

    public EgDemandDetails getDemandDetail() {
        return demandDetail;
    }

    public void setDemandDetail(EgDemandDetails demandDetail) {
        this.demandDetail = demandDetail;
    }

    public EgDemandReason getDemandreason() {
        return demandreason;
    }

    public void setDemandreason(EgDemandReason demandreason) {
        this.demandreason = demandreason;
    }

}