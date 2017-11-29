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

package org.egov.mrs.domain.entity;

import org.egov.infra.persistence.entity.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * Holds information of applicant's proofs which are attached
 *
 * @author nayeem
 *
 */

@Entity
@Table(name = "egmrs_identityproof")
@SequenceGenerator(name = IdentityProof.SEQ_IDENTITYPROOF, sequenceName = IdentityProof.SEQ_IDENTITYPROOF, allocationSize = 1)
public class IdentityProof extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 6406891900509939754L;

    public static final String SEQ_IDENTITYPROOF = "SEQ_EGMRS_IDENTITYPROOF";

    @Id
    @GeneratedValue(generator = SEQ_IDENTITYPROOF, strategy = GenerationType.SEQUENCE)
    private Long id;

    private boolean photograph;
    private boolean deaceasedDeathCertificate;
    private boolean divorceCertificate;
    private boolean schoolLeavingCertificate;
    private boolean birthCertificate;
    private boolean passport;
    private boolean rationCard;
    private boolean msebBill;
    private boolean telephoneBill;
    private boolean aadhar;
    private boolean notaryAffidavit;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public boolean getPhotograph() {
        return photograph;
    }

    public void setPhotograph(final boolean photograph) {
        this.photograph = photograph;
    }

    public boolean getDeaceasedDeathCertificate() {
        return deaceasedDeathCertificate;
    }

    public void setDeaceasedDeathCertificate(final boolean deaceasedDeathCertificate) {
        this.deaceasedDeathCertificate = deaceasedDeathCertificate;
    }

    public boolean getDivorceCertificate() {
        return divorceCertificate;
    }

    public void setDivorceCertificate(final boolean divorceCertificate) {
        this.divorceCertificate = divorceCertificate;
    }

    public boolean getSchoolLeavingCertificate() {
        return schoolLeavingCertificate;
    }

    public void setSchoolLeavingCertificate(final boolean schoolLeavingCertificate) {
        this.schoolLeavingCertificate = schoolLeavingCertificate;
    }

    public boolean getBirthCertificate() {
        return birthCertificate;
    }

    public void setBirthCertificate(final boolean birthCertificate) {
        this.birthCertificate = birthCertificate;
    }

    public boolean getPassport() {
        return passport;
    }

    public void setPassport(final boolean passport) {
        this.passport = passport;
    }

    public boolean getRationCard() {
        return rationCard;
    }

    public void setRationCard(final boolean rationCard) {
        this.rationCard = rationCard;
    }

    public boolean getMsebBill() {
        return msebBill;
    }

    public void setMsebBill(final boolean msebBill) {
        this.msebBill = msebBill;
    }

    public boolean getTelephoneBill() {
        return telephoneBill;
    }

    public void setTelephoneBill(final boolean telephoneBill) {
        this.telephoneBill = telephoneBill;
    }

    public boolean isAadhar() {
        return aadhar;
    }

    public void setAadhar(boolean aadhar) {
        this.aadhar = aadhar;
    }

    public boolean isNotaryAffidavit() {
        return notaryAffidavit;
    }

    public void setNotaryAffidavit(boolean notaryAffidavit) {
        this.notaryAffidavit = notaryAffidavit;
    }
}
