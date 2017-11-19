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

package org.egov.ptis.domain.entity.transactions;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;

@Entity
@Table(name = "egpt_assessment_transactions")
@SequenceGenerator(name = AssessmentTransactions.SEQ_ASSESSMENT_TRANSACTIONS, sequenceName = AssessmentTransactions.SEQ_ASSESSMENT_TRANSACTIONS, allocationSize = 1)
public class AssessmentTransactions extends AbstractAuditable {

    /**
     * 
     */
    private static final long serialVersionUID = -7715514846393024158L;
    protected static final String SEQ_ASSESSMENT_TRANSACTIONS = "SEQ_EGPT_ASSESSMENT_TRANSACTIONS";

    @Id
    @GeneratedValue(generator = SEQ_ASSESSMENT_TRANSACTIONS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "basicproperty")
    private BasicPropertyImpl basicProperty;

    @Column(name = "property")
    private PropertyImpl property;

    @Column(name = "transactiontype")
    private String transactionType;
    private Boundary zone;
    private Boundary ward;
    private Boundary street;
    private Boundary block;
    private Boundary locality;

    @Column(name = "electionward")
    private Boundary electionWard;

    @Column(name = "ownersname")
    private String ownerName;

    @Column(name = "doorno")
    private String doorNo;

    @Column(name = "propertytype")
    private PropertyTypeMaster propertyType;
    private String usage;

    @Column(name = "tax_effectivedate")
    private Date taxEffectiveDate;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public BasicPropertyImpl getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(BasicPropertyImpl basicProperty) {
        this.basicProperty = basicProperty;
    }

    public PropertyImpl getProperty() {
        return property;
    }

    public void setProperty(PropertyImpl property) {
        this.property = property;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Boundary getZone() {
        return zone;
    }

    public void setZone(Boundary zone) {
        this.zone = zone;
    }

    public Boundary getWard() {
        return ward;
    }

    public void setWard(Boundary ward) {
        this.ward = ward;
    }

    public Boundary getStreet() {
        return street;
    }

    public void setStreet(Boundary street) {
        this.street = street;
    }

    public Boundary getBlock() {
        return block;
    }

    public void setBlock(Boundary block) {
        this.block = block;
    }

    public Boundary getLocality() {
        return locality;
    }

    public void setLocality(Boundary locality) {
        this.locality = locality;
    }

    public Boundary getElectionWard() {
        return electionWard;
    }

    public void setElectionWard(Boundary electionWard) {
        this.electionWard = electionWard;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public PropertyTypeMaster getPropertytype() {
        return propertyType;
    }

    public void setPropertytype(PropertyTypeMaster propertytype) {
        this.propertyType = propertytype;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public Date getTaxEffectivedate() {
        return taxEffectiveDate;
    }

    public void setTaxEffectivedate(Date taxEffectivedate) {
        this.taxEffectiveDate = taxEffectivedate;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

}
