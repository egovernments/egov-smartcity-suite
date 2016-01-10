/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.mrs.domain.entity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.mrs.domain.enums.ApplicationStatus;
import org.egov.mrs.masters.entity.Act;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egmrs_registration")
@SequenceGenerator(name = Registration.SEQ_REGISTRATION, sequenceName = Registration.SEQ_REGISTRATION, allocationSize = 1)
public class Registration extends StateAware {

    private static final long serialVersionUID = 6743094118312883758L;
    public static final String SEQ_REGISTRATION = "SEQ_EGMRS_REGISTRATION";

    @Id
    @GeneratedValue(generator = SEQ_REGISTRATION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String applicationNo;

    private String registrationNo;

    @NotNull
    private Date dateOfMarriage;

    @NotNull
    private Act marriageAct;

    @NotNull
    @SafeHtml
    @Length(max = 30)
    private String placeOfMarriage;

    @NotNull
    @Valid
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "husband")
    private Applicant husband = new Applicant();

    @NotNull
    @Valid
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wife")
    private Applicant wife = new Applicant();

    @NotNull
    @Valid
    @OneToMany(mappedBy = "registration")
    private List<Witness> witnesses = new LinkedList<Witness>();

    @NotNull
    @Valid
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priest")
    private Priest priest;

    private boolean coupleFromSamePlace;

    private byte[] memorandumOfMarriage;
    private byte[] courtFeeStamp;
    private byte[] affidavit;
    private byte[] marriageCard;

    @NotNull
    private String feeCriteria;

    @NotNull
    private String feePaid;
    
    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone")
    private Boundary zone;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "demand")
    private EgDemand demand;
    
    @NotNull
    @Length(max = 30)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    
    @Length(max = 256)
    private String rejectionReason;
    
    @Length(max = 256)
    private String remakrs;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(final String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(final String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public Date getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(final Date dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public Act getMarriageAct() {
        return marriageAct;
    }

    public void setMarriageAct(final Act marriageAct) {
        this.marriageAct = marriageAct;
    }

    public String getPlaceOfMarriage() {
        return placeOfMarriage;
    }

    public void setPlaceOfMarriage(final String placeOfMarriage) {
        this.placeOfMarriage = placeOfMarriage;
    }

    public Applicant getHusband() {
        return husband;
    }

    public void setHusband(final Applicant husband) {
        this.husband = husband;
    }

    public Applicant getWife() {
        return wife;
    }

    public void setWife(final Applicant wife) {
        this.wife = wife;
    }

    public Priest getPriest() {
        return priest;
    }

    public void setPriest(final Priest priest) {
        this.priest = priest;
    }

    public byte[] getMemorandumOfMarriage() {
        return memorandumOfMarriage;
    }

    public void setMemorandumOfMarriage(final byte[] memorandumOfMarriage) {
        this.memorandumOfMarriage = memorandumOfMarriage;
    }

    public byte[] getCourtFeeStamp() {
        return courtFeeStamp;
    }

    public void setCourtFeeStamp(final byte[] courtFeeStamp) {
        this.courtFeeStamp = courtFeeStamp;
    }

    public byte[] getAffidavit() {
        return affidavit;
    }

    public void setAffidavit(final byte[] affidavit) {
        this.affidavit = affidavit;
    }

    public byte[] getMarriageCard() {
        return marriageCard;
    }

    public void setMarriageCard(final byte[] marriageCard) {
        this.marriageCard = marriageCard;
    }

    public boolean isCoupleFromSamePlace() {
        return coupleFromSamePlace;
    }

    public void setCoupleFromSamePlace(final boolean coupleFromSamePlace) {
        this.coupleFromSamePlace = coupleFromSamePlace;
    }

    public String getFeeCriteria() {
        return feeCriteria;
    }

    public void setFeeCriteria(final String feeCriteria) {
        this.feeCriteria = feeCriteria;
    }

    public String getFeePaid() {
        return feePaid;
    }

    public void setFeePaid(final String feePaid) {
        this.feePaid = feePaid;
    }

    public List<Witness> getWitnesses() {
        return witnesses;
    }

    public void setWitnesses(final List<Witness> witnesses) {
        this.witnesses = witnesses;
    }

    public Boundary getZone() {
        return zone;
    }

    public void setZone(Boundary zone) {
        this.zone = zone;
    }
    

    @Override
    public String getStateDetails() {
        return null;
    }
}
