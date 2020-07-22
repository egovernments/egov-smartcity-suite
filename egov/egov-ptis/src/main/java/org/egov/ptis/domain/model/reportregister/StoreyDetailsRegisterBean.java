/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2020  eGovernments Foundation
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
package org.egov.ptis.domain.model.reportregister;

import java.math.BigDecimal;

public class StoreyDetailsRegisterBean {

    private Integer age;
    private String usage;
    private String occupation;
    private String constructionType;
    private BigDecimal plinthArea;
    private BigDecimal unitRate;
    private BigDecimal mrv;
    private BigDecimal buildingARVBeforeDepriciation;
    private BigDecimal buildingARV;
    private BigDecimal sitalARV;
    private BigDecimal totalNetARV;
    private BigDecimal depriciationValue;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getConstructionType() {
        return constructionType;
    }

    public void setConstructionType(String constructionType) {
        this.constructionType = constructionType;
    }

    public BigDecimal getPlinthArea() {
        return plinthArea;
    }

    public void setPlinthArea(BigDecimal plinthArea) {
        this.plinthArea = plinthArea;
    }

    public BigDecimal getUnitRate() {
        return unitRate;
    }

    public void setUnitRate(BigDecimal unitRate) {
        this.unitRate = unitRate;
    }

    public BigDecimal getMrv() {
        return mrv;
    }

    public void setMrv(BigDecimal mrv) {
        this.mrv = mrv;
    }

    public BigDecimal getBuildingARVBeforeDepriciation() {
        return buildingARVBeforeDepriciation;
    }

    public void setBuildingARVBeforeDepriciation(BigDecimal buildingARVBeforeDepriciation) {
        this.buildingARVBeforeDepriciation = buildingARVBeforeDepriciation;
    }

    public BigDecimal getBuildingARV() {
        return buildingARV;
    }

    public void setBuildingARV(BigDecimal buildingARV) {
        this.buildingARV = buildingARV;
    }

    public BigDecimal getSitalARV() {
        return sitalARV;
    }

    public void setSitalARV(BigDecimal sitalARV) {
        this.sitalARV = sitalARV;
    }

    public BigDecimal getTotalNetARV() {
        return totalNetARV;
    }

    public void setTotalNetARV(BigDecimal totalNetARV) {
        this.totalNetARV = totalNetARV;
    }

    public BigDecimal getDepriciationValue() {
        return depriciationValue;
    }

    public void setDepriciationValue(BigDecimal depriciationValue) {
        this.depriciationValue = depriciationValue;
    }

}
