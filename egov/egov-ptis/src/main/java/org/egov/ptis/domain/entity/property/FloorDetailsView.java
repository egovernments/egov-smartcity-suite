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
 *      1) All versions of this program, verbatim or modified must carry this 
 *         Legal Notice.
 * 
 *      2) Any misrepresentation of the origin of the material is prohibited. It 
 *         is required that all modified versions of this material be marked in 
 *         reasonable ways as different from the original version.
 * 
 *      3) This license does not grant any rights to any user of the program 
 *         with regards to rights under trademark law for use of the trade names 
 *         or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.entity.property;

import java.io.Serializable;
import java.math.BigDecimal;

public class FloorDetailsView implements Serializable {

    private Integer basicPropertyId;
    private Integer propertyId;
    private String propertyUsage;
    private PropertyTypeMaster propertyTypeMaster;
    private String floorNo;
    private BigDecimal sitalArea;
    private BigDecimal builtUpArea;
    private String classification;
    private PropertyOccupation propertyOccupation;


    public Integer getBasicPropertyId() {
        return basicPropertyId;
    }

    public void setBasicPropertyId(Integer basicPropertyId) {
        this.basicPropertyId = basicPropertyId;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyUsage() {
        return propertyUsage;
    }

    public void setPropertyUsage(String propertyUsage) {
        this.propertyUsage = propertyUsage;
    }

    public PropertyTypeMaster getPropertyTypeMaster() {
        return propertyTypeMaster;
    }

    public void setPropertyTypeMaster(PropertyTypeMaster propertyTypeMaster) {
        this.propertyTypeMaster = propertyTypeMaster;
    }

    public String getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(String floorNo) {
        this.floorNo = floorNo;
    }

    public BigDecimal getSitalArea() {
        return sitalArea;
    }

    public void setSitalArea(BigDecimal sitalArea) {
        this.sitalArea = sitalArea;
    }

    public BigDecimal getBuiltUpArea() {
        return builtUpArea;
    }

    public void setBuiltUpArea(BigDecimal builtUpArea) {
        this.builtUpArea = builtUpArea;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public PropertyOccupation getPropertyOccupation() {
        return propertyOccupation;
    }

    public void setPropertyOccupation(PropertyOccupation propertyOccupation) {
        this.propertyOccupation = propertyOccupation;
    }

}
