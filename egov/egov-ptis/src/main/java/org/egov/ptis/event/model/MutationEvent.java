/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */

package org.egov.ptis.event.model;

import java.math.BigInteger;

import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @author subhash
 *
 */
public class MutationEvent extends ApplicationEvent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final PropertyMutation propertyMutation;

    private BigInteger random;
    private String cityCode;
    private String districtCode;
    private String propertyType;
    private String ownerName;
    private String aadharNumber;
    private String mobileNumber;
    private String surveyNumber;
    private String ward;
    private String block;
    private String layoutNumber;
    private String doorNumber;

    public MutationEvent(final Object source, final PropertyMutation propertyMutation) {
        super(source);
        this.propertyMutation = propertyMutation;
    }

    public PropertyMutation getPropertyMutation() {
        return propertyMutation;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(final String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(final String districtCode) {
        this.districtCode = districtCode;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final String propertyType) {
        this.propertyType = propertyType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(final String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSurveyNumber() {
        return surveyNumber;
    }

    public void setSurveyNumber(final String surveyNumber) {
        this.surveyNumber = surveyNumber;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(final String ward) {
        this.ward = ward;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(final String block) {
        this.block = block;
    }

    public String getLayoutNumber() {
        return layoutNumber;
    }

    public void setLayoutNumber(final String layoutNumber) {
        this.layoutNumber = layoutNumber;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(final String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public BigInteger getRandom() {
        return random;
    }

    public void setRandom(BigInteger random) {
        this.random = random;
    }

}
