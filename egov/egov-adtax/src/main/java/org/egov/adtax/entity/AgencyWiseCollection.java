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
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EGADTAX_AGENCYWISECOLLECTION")
@SequenceGenerator(name = AgencyWiseCollection.SEQ_AGENCYWISEHOARDINGCOLLECTION, sequenceName = AgencyWiseCollection.SEQ_AGENCYWISEHOARDINGCOLLECTION, allocationSize = 1)
public class AgencyWiseCollection extends AbstractAuditable {

    private static final long serialVersionUID = -6000069398066347498L;

    public static final String SEQ_AGENCYWISEHOARDINGCOLLECTION = "SEQ_EGADTAX_AGENCYWISECOLLECTION";

    @Id
    @GeneratedValue(generator = SEQ_AGENCYWISEHOARDINGCOLLECTION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "agency", nullable = false)
    private Agency agency;

    @NotNull
    @SafeHtml
    @Length(max = 25)
    private String billNumber;

    private Boolean demandUpdated = false;
    private Boolean amountCollected=false;
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "agencyWiseCollection", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AgencyWiseCollectionDetail> agencyWiseCollectionDetails = new HashSet<AgencyWiseCollectionDetail>(0);

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "agencyWiseDemand", nullable = false)
    private EgDemand agencyWiseDemand;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Boolean getDemandUpdated() {
        return demandUpdated;
    }

    public void setDemandUpdated(Boolean demandUpdated) {
        this.demandUpdated = demandUpdated;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Set<AgencyWiseCollectionDetail> getAgencyWiseCollectionDetails() {
        return agencyWiseCollectionDetails;
    }

    public void setAgencyWiseCollectionDetails(Set<AgencyWiseCollectionDetail> agencyWiseCollectionDetails) {
        this.agencyWiseCollectionDetails = agencyWiseCollectionDetails;
    }

    public EgDemand getAgencyWiseDemand() {
        return agencyWiseDemand;
    }

    public void setAgencyWiseDemand(EgDemand agencyWiseDemand) {
        this.agencyWiseDemand = agencyWiseDemand;
    }

    public Boolean getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(Boolean amountCollected) {
        this.amountCollected = amountCollected;
    }

}