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

import org.egov.bnd.utils.BndConstants;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.persistence.entity.Address;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BirthRegistration extends Registration implements Cloneable {
    private static final long serialVersionUID = -8427361865321338155L;
    private AttentionType typeAttention;
    private DeliveryMethod methodDelivery;
    private BigDecimal birthWeight;
    private BigDecimal pregDuration;
    private BirthRegistration parent;
    private Set<SideLetter> sideLetterList = new HashSet<SideLetter>(0);
    // Temporary variables
    private Address permanentCitizenAddress;
    private Address parentAddress;
    private Address motherResidenceAddress;

    private BndNameChange nameChange;

    public BndNameChange getNameChange() {
        return nameChange;
    }

    public void setNameChange(final BndNameChange nameChange) {
        this.nameChange = nameChange;
    }

    public BirthRegistration() {
        setCitizenBDDetails(new CitizenBDDetails());
        setAdoptionDetail(new AdoptionDetails());
    }

    public Address getMotherResidenceAddress() {
        return motherResidenceAddress;
    }

    public void setMotherResidenceAddress(final Address motherResidenceAddress) {
        this.motherResidenceAddress = motherResidenceAddress;
    }

    public Address getParentAddress() {
        return parentAddress;
    }

    public void setParentAddress(final Address parentAddress) {
        this.parentAddress = parentAddress;
    }

    public Address getPermanentCitizenAddress() {
        return permanentCitizenAddress;
    }

    public void setPermanentCitizenAddress(final Address permanentCitizenAddress) {
        this.permanentCitizenAddress = permanentCitizenAddress;
    }

    public Set<SideLetter> getSideLetterList() {
        return sideLetterList;
    }

    public void setSideLetterList(final Set<SideLetter> sideLetterList) {
        this.sideLetterList = sideLetterList;
    }

    /**
     * @return Returns the m_ageMomBirth.
     */

    public AttentionType getTypeAttention() {
        return typeAttention;
    }

    public void setTypeAttention(final AttentionType typeAttention) {
        this.typeAttention = typeAttention;
    }

    public DeliveryMethod getMethodDelivery() {
        return methodDelivery;
    }

    public void setMethodDelivery(final DeliveryMethod methodDelivery) {
        this.methodDelivery = methodDelivery;
    }

    public BigDecimal getBirthWeight() {
        return birthWeight;
    }

    public void setBirthWeight(final BigDecimal birthWeight) {
        this.birthWeight = birthWeight;
    }

    public BigDecimal getPregDuration() {
        return pregDuration;
    }

    public void setPregDuration(final BigDecimal pregDuration) {
        this.pregDuration = pregDuration;
    }

    public BirthRegistration getParent() {
        return parent;
    }

    public void setParent(final BirthRegistration parent) {
        this.parent = parent;
    }

    @Override
    public BirthRegistration clone() {
        try {
            return (BirthRegistration) super.clone();
        } catch (final CloneNotSupportedException ex) {
            ex.printStackTrace();
            throw new EGOVRuntimeException("Exception in creating a clone for BirthRegistration :"
                    + getRegistrationNo());
        }

    }

    public List<String> getDropdownActionList() {
        return BndConstants.SEARCHBIRTHDROPDOWNLIST;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ID  :").append(getId()).append(" ,Registration Date :").append(getRegistrationDate())
        .append(" ,Registration Number :").append(getRegistrationNo()).append(" ,BirthDate : ")
        .append(getDateOfEvent()).append(" ,Child : ").append(getCitizen()).append(" ,isAdopted : ")
        .append(getIsChildAdopted()).append(" ,Informant : ").append(getInformant());
        return builder.toString();
    }

    public List<String> getDropdownActionListStillBirth() {
        return BndConstants.SEARCHSTILLBIRTHDROPDOWNLIST;
    }

}
