/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.bnd.model;

import org.egov.commons.EgwStatus;
import org.egov.infra.workflow.entity.StateAware;

import java.math.BigDecimal;

public class NonAvailability extends StateAware {
    private static final long serialVersionUID = 4204662679487898122L;
    private Long id;
    private String registrationNo;
    private String eventType;
    // private Transaction transaction ;
    private Integer no_Of_copies;
    private String remarks;
    private Integer yearOfEvent;
    private String citizenName;
    private CRelation citizenRelationType;
    private String applicantName;
    private CRelation applicantRelationType;
    private EgwStatus status;
    private String applicantRelationName;
    private String citizenRelationName;
    private String acknowledgeNumber;
    private String talukName;
    private String districtName;
    private String stateName;
    private BigDecimal cost;
    private BigDecimal totalFee;

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(final BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(final BigDecimal cost) {
        this.cost = cost;
    }

    public String getTalukName() {
        return talukName;
    }

    public void setTalukName(final String talukName) {
        this.talukName = talukName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(final String stateName) {
        this.stateName = stateName;
    }

    public String getApplicantRelationName() {
        return applicantRelationName;
    }

    public void setApplicantRelationName(final String applicantRelationName) {
        this.applicantRelationName = applicantRelationName;
    }

    public String getCitizenRelationName() {
        return citizenRelationName;
    }

    public void setCitizenRelationName(final String citizenRelationName) {
        this.citizenRelationName = citizenRelationName;
    }

    /*
     * public Long getId() { return id; } public void setId(Long id) { this.id =
     * id; }
     */

    public Integer getYearOfEvent() {
        return yearOfEvent;
    }

    public void setYearOfEvent(final Integer yearOfEvent) {
        this.yearOfEvent = yearOfEvent;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(final String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }

    /*
     * public Transaction getTransaction() { return transaction; } public void
     * setTransaction(Transaction transaction) { this.transaction = transaction;
     * }
     */
    public Integer getNo_Of_copies() {
        return no_Of_copies;
    }

    public void setNo_Of_copies(final Integer noOfCopies) {
        no_Of_copies = noOfCopies;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getCitizenName() {
        return citizenName;
    }

    public void setCitizenName(final String citizenName) {
        this.citizenName = citizenName;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    public String getAcknowledgeNumber() {
        return acknowledgeNumber;
    }

    public void setAcknowledgeNumber(final String acknowledgeNumber) {
        this.acknowledgeNumber = acknowledgeNumber;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public CRelation getCitizenRelationType() {
        return citizenRelationType;
    }

    public void setCitizenRelationType(final CRelation citizenRelationType) {
        this.citizenRelationType = citizenRelationType;
    }

    public CRelation getApplicantRelationType() {
        return applicantRelationType;
    }

    public void setApplicantRelationType(final CRelation applicantRelationType) {
        this.applicantRelationType = applicantRelationType;
    }

    @Override
    public String getStateDetails() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {

        final StringBuffer obj = new StringBuffer();
        obj.append("OBJECT=" + getClass());
        obj.append("ID=" + id);
        obj.append("registrationNo=" + registrationNo);
        obj.append("eventType=" + eventType);
        obj.append("remarks=" + remarks);
        obj.append("applicantName=" + applicantName);
        obj.append("applicantRelationName=" + applicantRelationName);
        obj.append("citizenName=" + citizenName);
        obj.append("citizenRelationName=" + citizenRelationName);
        obj.append("applicantRelationType="
                + (applicantRelationType != null ? applicantRelationType.getRelatedAsConst() : null));
        obj.append("citizenRelationType="
                + (citizenRelationType != null ? citizenRelationType.getRelatedAsConst() : null));
        obj.append("cost=" + cost);
        obj.append("yearOfEvent=" + yearOfEvent);
        obj.append("no_Of_copies=" + no_Of_copies);
        obj.append("totalFee=" + totalFee);
        obj.append("stateName=" + stateName);
        obj.append("talukName=" + talukName);
        obj.append("stateName=" + stateName);

        return obj.toString();
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
       this.id = id;  
    }

}
