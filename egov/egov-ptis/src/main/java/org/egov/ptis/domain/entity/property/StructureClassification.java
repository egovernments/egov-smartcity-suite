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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
/*
 * StructureClassification.java Created on Oct 20, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import org.egov.commons.Installment;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Each deployment groups number of CostructionTypeSets and gives a common name.
 * a For example: A particular ConstructionTypeSet might be composed of the
 * following ConstructionType objects: TeakWood, MarbleFloor, RCCRoof. A name is
 * given to the StructuralClassification. For example the name given to the
 * above grouping can be Pucca. RoseWood, GraniteFloor and RCCRoof might also
 * share the same name. A factor is normally associated with the Structural
 * Classification which is used to calculate the demand of the Property.
 * </p>
 * 
 * @author Gayathri Joshi
 * @version 2.00
 * @see org.egov.ptis.domain.entity.property.ConstructionType
 * @since 2.00
 * @author srikanth Change Log : Added additional field
 *         constrTypeCode,orderId,fromDate,todate
 */
public class StructureClassification extends BaseModel {

    private String typeName;
    private String description;
    private String constrTypeCode;
    private Integer orderId;
    private Integer floorNum;
    private Integer number;
    private Float factor;
    private Installment startInstallment;
    private char isHistory;
    private Date fromDate;
    private Date toDate;
    private Boolean isActive;

    /**
     * @return Returns if the given Object is equal to PropertyStatus
     */
    public boolean equals(Object that) {

        if (that == null)
            return false;

        if (this == that)
            return true;

        if (that.getClass() != this.getClass())
            return false;
        final StructureClassification thatStrCls = (StructureClassification) that;

        if (this.getId() != null && thatStrCls.getId() != null) {
            if (getId().equals(thatStrCls.getId()))
                return true;
            else
                return false;
        } else if (this.getTypeName() != null && thatStrCls.getTypeName() != null) {
            if (getTypeName().equals(thatStrCls.getTypeName()))
                return true;
            else
                return false;
        } else if (this.getNumber() != null && thatStrCls.getNumber() != null) {
            if (getNumber().equals(thatStrCls.getNumber()))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    /**
     * @return Returns the hashCode
     */
    public int hashCode() {
        int hashCode = 0;
        if (this.getId() != null)
            hashCode += this.getId().hashCode();

        if (this.getTypeName() != null)
            hashCode += this.getTypeName().hashCode();

        if (this.getNumber() != null)
            hashCode += this.getNumber().hashCode();
        return hashCode;
    }

    /**
     * @return Returns the boolean after validating the current object
     */
    public List<ValidationError> validate() {
        List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        if (getTypeName() == null) {
            ValidationError ve = new ValidationError("StrucClass.TypeName.Null",
                    "In StructureClassification Attribute 'Type Name' is Not Set, Please Check !!");
            validationErrors.add(ve);
        }
        if (getNumber() == null || getNumber() == 0) {
            ValidationError ve = new ValidationError("StrucClass.Number.Null",
                    "In StructureClassification Attribute 'Number' is Not Set OR is Zero, Please Check !!");
            validationErrors.add(ve);
        }

        return validationErrors;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("").append(id).append("|").append(constrTypeCode).append("|").append(factor);
        return sb.toString();
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConstrTypeCode() {
        return constrTypeCode;
    }

    public void setConstrTypeCode(String constrTypeCode) {
        this.constrTypeCode = constrTypeCode;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(Integer floorNum) {
        this.floorNum = floorNum;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Float getFactor() {
        return factor;
    }

    public void setFactor(Float factor) {
        this.factor = factor;
    }

    public Installment getStartInstallment() {
        return startInstallment;
    }

    public void setStartInstallment(Installment startInstallment) {
        this.startInstallment = startInstallment;
    }

    public char getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(char isHistory) {
        this.isHistory = isHistory;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
