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

import org.egov.infra.persistence.entity.Address;
import org.egov.infstr.models.BaseModel;

import java.util.Date;

public class AdoptionDetails extends BaseModel {
    private static final long serialVersionUID = -8079098623497302378L;
    private Date adoptionDate;
    private BnDCitizen adopteeMother;
    private BnDCitizen adopteeFather;
    private Address adopteeAddress;
    private String remarks;
    private String courtOrderNumber;
    private String adoptionNumber;
    private String affidavitNumber;
    private AdoptionInstitute adoptionInstitute;
    // Temporary variable
    private Long adoptionInstituteId;

    public Long getAdoptionInstituteId() {
        return adoptionInstituteId;
    }

    public void setAdoptionInstituteId(final Long adoptionInstituteId) {
        this.adoptionInstituteId = adoptionInstituteId;
    }

    public AdoptionInstitute getAdoptionInstitute() {
        return adoptionInstitute;
    }

    public void setAdoptionInstitute(final AdoptionInstitute adoptionInstitute) {
        this.adoptionInstitute = adoptionInstitute;
    }

    public String getAdoptionNumber() {
        return adoptionNumber;
    }

    public void setAdoptionNumber(final String adoptionNumber) {
        this.adoptionNumber = adoptionNumber;
    }

    public String getAffidavitNumber() {
        return affidavitNumber;
    }

    public void setAffidavitNumber(final String affidavitNumber) {
        this.affidavitNumber = affidavitNumber;
    }

    public Date getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(final Date adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public BnDCitizen getAdopteeMother() {
        return adopteeMother;
    }

    public void setAdopteeMother(final BnDCitizen adopteeMother) {
        this.adopteeMother = adopteeMother;
    }

    public BnDCitizen getAdopteeFather() {
        return adopteeFather;
    }

    public void setAdopteeFather(final BnDCitizen adopteeFather) {
        this.adopteeFather = adopteeFather;
    }

    public Address getAdopteeAddress() {
        return adopteeAddress;
    }

    public void setAdopteeAddress(final Address adopteeAddress) {
        this.adopteeAddress = adopteeAddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getCourtOrderNumber() {
        return courtOrderNumber;
    }

    public void setCourtOrderNumber(final String courtOrderNumber) {
        this.courtOrderNumber = courtOrderNumber;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ID  :").append(id).append(" Adoption Date :").append(adoptionDate == null ? " " : adoptionDate)
                .append(" Adoptee Father :").append(adopteeFather).append(" Adoptee Mother : ").append(adopteeMother)
        .append(" Adoption Number : ").append(adoptionNumber).append(" Affitavit Number : ")
        .append(affidavitNumber);
        return builder.toString();
    }

}
