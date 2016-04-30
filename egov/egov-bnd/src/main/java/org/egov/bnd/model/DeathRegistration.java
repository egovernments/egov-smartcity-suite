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

import java.util.ArrayList;
import java.util.List;

public class DeathRegistration extends Registration implements Cloneable {

    private static final long serialVersionUID = 5492067132210150290L;
    private int age;
    private AgeType ageType;
    private String deathCertified;
    private String pregnancyRelated;
    private AttentionDeathType typeMedAttention;
    private List<CitizenAddiction> addictions = new ArrayList<CitizenAddiction>();// contains
    // CCitizenAddiction
    // objects
    private Address deceasedAddress;
    private Address permanentCitizenAddress;
    private Address informantAddress;
    private CrematoriumMaster crematorium;
    private BnDCitizen deceasedrelation;
    private Address deceasedUsualAddress;
    private AttentionDeathType typeAttention;
    private Disease causeOfDeathParent;
    private DeathRegistration parent;
    private CRelation relationType;
    private Boolean isRelative;
    private DeathRegistration deathHistory;

    public DeathRegistration getDeathHistory() {
        return deathHistory;
    }

    public void setDeathHistory(final DeathRegistration deathHistory) {
        this.deathHistory = deathHistory;
    }

    public Boolean getIsRelative() {
        return isRelative;
    }

    public void setIsRelative(final Boolean isRelative) {
        this.isRelative = isRelative;
    }

    public CRelation getRelationType() {
        return relationType;
    }

    public void setRelationType(final CRelation relationType) {
        this.relationType = relationType;
    }

    public CRelation getDeceasedrelationType() {
        return deceasedrelationType;
    }

    public void setDeceasedrelationType(final CRelation deceasedrelationType) {
        this.deceasedrelationType = deceasedrelationType;
    }

    private CRelation deceasedrelationType;

    // private DeathTxAgent deathAgent=null;

    public DeathRegistration getParent() {
        return parent;
    }

    public void setParent(final DeathRegistration parent) {
        this.parent = parent;
    }

    @Override
    public Disease getCauseOfDeathParent() {
        return causeOfDeathParent;
    }

    @Override
    public void setCauseOfDeathParent(final Disease causeOfDeathParent) {
        this.causeOfDeathParent = causeOfDeathParent;
    }

    public AttentionDeathType getTypeAttention() {
        return typeAttention;
    }

    public void setTypeAttention(final AttentionDeathType typeAttention) {
        this.typeAttention = typeAttention;
    }

    public Address getDeceasedUsualAddress() {
        return deceasedUsualAddress;
    }

    public void setDeceasedUsualAddress(final Address deceasedUsualAddress) {
        this.deceasedUsualAddress = deceasedUsualAddress;
    }

    public DeathRegistration() {
        setCitizenBDDetails(new CitizenBDDetails());

    }

    public BnDCitizen getDeceasedrelation() {
        return deceasedrelation;
    }

    public void setDeceasedrelation(final BnDCitizen deceasedrelation) {
        this.deceasedrelation = deceasedrelation;
    }

    /**
     * @return Returns the ageType.
     */
    public AgeType getAgeType() {
        return ageType;
    }

    /**
     * @param ageType
     *            The ageType to set.
     */
    public void setAgeType(final AgeType ageType) {
        this.ageType = ageType;
    }

    /**
     * @return Returns the deceasedAddress.
     */
    public Address getDeceasedAddress() {
        return deceasedAddress;
    }

    /**
     * @param deceasedAddress
     *            The deceasedAddress to set.
     */
    public void setDeceasedAddress(final Address deceasedAddress) {
        this.deceasedAddress = deceasedAddress;
    }

    /**
     * @return Returns the age of deceased
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age
     *            The age to set.
     */
    public void setAge(final int age) {
        this.age = age;
    }

    /**
     * @return Returns the deathCertified.
     */
    public String isDeathCertified() {
        return deathCertified;
    }

    /**
     * @param deathCertified
     *            The deathCertified to set.
     */
    public void setDeathCertified(final String deathCertified) {
        this.deathCertified = deathCertified;
    }

    /**
     * @return Returns the pregnancyRelated.
     */
    public String isPregnancyRelated() {
        return pregnancyRelated;
    }

    /**
     * @param pregnancyRelated
     *            The pregnancyRelated to set.
     */
    public void setPregnancyRelated(final String pregnancyRelated) {
        this.pregnancyRelated = pregnancyRelated;
    }

    /**
     * @return Returns the typeMedAttention.
     */
    public AttentionDeathType getTypeMedAttention() {
        return typeMedAttention;
    }

    /**
     * @param typeMedAttention
     *            The typeMedAttention to set.
     */
    public void setTypeMedAttention(final AttentionDeathType typeMedAttention) {
        this.typeMedAttention = typeMedAttention;
    }

    /**
     * @return Returns the addictions.
     */
    public List<CitizenAddiction> getAddictions() {
        return addictions;
    }

    /**
     * @param addictions
     *            The addictions to set.
     */
    public void setAddictions(final List<CitizenAddiction> addictions) {

        this.addictions = addictions;
    }

    public CrematoriumMaster getCrematorium() {
        return crematorium;
    }

    public void setCrematorium(final CrematoriumMaster crematorium) {
        this.crematorium = crematorium;
    }

    public Address getPermanentCitizenAddress() {
        return permanentCitizenAddress;
    }

    public void setPermanentCitizenAddress(final Address permanentCitizenAddress) {
        this.permanentCitizenAddress = permanentCitizenAddress;
    }

    @Override
    public Address getInformantAddress() {
        return informantAddress;
    }

    @Override
    public void setInformantAddress(final Address informantAddress) {
        this.informantAddress = informantAddress;
    }

    @Override
    public DeathRegistration clone() {
        try {
            return (DeathRegistration) super.clone();
        } catch (final CloneNotSupportedException ex) {
            ex.printStackTrace();
            throw new EGOVRuntimeException("Exception in creating a clone for DeathRegistration :"
                    + getRegistrationNo());
        }

    }

    public List<String> getDropdownActionList() {
        return BndConstants.SEARCHDEATHDROPDOWNLIST;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ID  :").append(getId()).append(" ,Registration Date :").append(getRegistrationDate())
        .append(" ,Registration Number :").append(getRegistrationNo()).append(" ,BirthDate : ")
        .append(getDateOfEvent()).append(" ,Deceased : ").append(getCitizen()).append(" ,Informant : ")
        .append(getInformant());
        return builder.toString();
    }

}
