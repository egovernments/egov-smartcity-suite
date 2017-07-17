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


package org.egov.ptis.domain.entity.property;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class reperesents the IOnfor,ation which is required to create a unique
 * Identification number for the Proeprty.
 *
 * @author Neetu
 * @version 2.00
 */

public class PropertyID extends BaseModel {
    private BasicProperty basicProperty;
    private Boundary zone;
    private Boundary ward;
    private Integer colonyId;
    private String doorNum;
    private Integer assUnit;
    private Boundary area;
    private Boundary locality;
    private Boundary Street;
    private Boundary frontBndryStreet;
    private Boundary backBndryStreet;
    private Boundary leftBndryStreet;
    private Boundary rightBndryStreet;
    private Boundary bndryStreetForDmdCalc;
    private String northBoundary;
    private String southBoundary;
    private String eastBoundary;
    private String westBoundary;
    private Boundary electionBoundary;

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PropertyID))
            return false;
        if (!super.equals(o))
            return false;
        final PropertyID that = (PropertyID) o;
        return Objects.equals(basicProperty, that.basicProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), basicProperty);
    }

    @Override
    public List<ValidationError> validate() {
        return new ArrayList<>();
    }

    /**
     * @return Returns the boolean after validating the current object
     */

    public boolean validatePropertID() {
        if (getBasicProperty() == null) {
            throw new ApplicationRuntimeException("PropertyID.validate : BasicProperty is not set, Please Check !!");
        }
        return true;
    }

    public BasicProperty getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(BasicProperty basicProperty) {
        this.basicProperty = basicProperty;
    }

    public Boundary getZone() {
        return zone;
    }

    public void setZone(Boundary zone) {
        this.zone = zone;
    }

    public Boundary getWard() {
        return ward;
    }

    public void setWard(Boundary ward) {
        this.ward = ward;
    }

    public Integer getColonyId() {
        return colonyId;
    }

    public void setColonyId(Integer colonyId) {
        this.colonyId = colonyId;
    }

    public String getDoorNum() {
        return doorNum;
    }

    public void setDoorNum(String doorNum) {
        this.doorNum = doorNum;
    }

    public Integer getAssUnit() {
        return assUnit;
    }

    public void setAssUnit(Integer assUnit) {
        this.assUnit = assUnit;
    }

    public Boundary getArea() {
        return area;
    }

    public void setArea(Boundary area) {
        this.area = area;
    }

    public Boundary getLocality() {
        return locality;
    }

    public void setLocality(Boundary locality) {
        this.locality = locality;
    }

    public Boundary getStreet() {
        return Street;
    }

    public void setStreet(Boundary street) {
        Street = street;
    }

    public Boundary getFrontBndryStreet() {
        return frontBndryStreet;
    }

    public void setFrontBndryStreet(Boundary frontBndryStreet) {
        this.frontBndryStreet = frontBndryStreet;
    }

    public Boundary getBackBndryStreet() {
        return backBndryStreet;
    }

    public void setBackBndryStreet(Boundary backBndryStreet) {
        this.backBndryStreet = backBndryStreet;
    }

    public Boundary getLeftBndryStreet() {
        return leftBndryStreet;
    }

    public void setLeftBndryStreet(Boundary leftBndryStreet) {
        this.leftBndryStreet = leftBndryStreet;
    }

    public Boundary getRightBndryStreet() {
        return rightBndryStreet;
    }

    public void setRightBndryStreet(Boundary rightBndryStreet) {
        this.rightBndryStreet = rightBndryStreet;
    }

    public Boundary getBndryStreetForDmdCalc() {
        return bndryStreetForDmdCalc;
    }

    public void setBndryStreetForDmdCalc(Boundary bndryStreetForDmdCalc) {
        this.bndryStreetForDmdCalc = bndryStreetForDmdCalc;
    }

    public String getNorthBoundary() {
        return northBoundary;
    }

    public void setNorthBoundary(String northBoundary) {
        this.northBoundary = northBoundary;
    }

    public String getSouthBoundary() {
        return southBoundary;
    }

    public void setSouthBoundary(String southBoundary) {
        this.southBoundary = southBoundary;
    }

    public String getEastBoundary() {
        return eastBoundary;
    }

    public void setEastBoundary(String eastBoundary) {
        this.eastBoundary = eastBoundary;
    }

    public String getWestBoundary() {
        return westBoundary;
    }

    public void setWestBoundary(String westBoundary) {
        this.westBoundary = westBoundary;
    }


    public Boundary getElectionBoundary() {
        return electionBoundary;
    }

    public void setElectionBoundary(Boundary electionBoundary) {
        this.electionBoundary = electionBoundary;
    }

    @Override
    public String toString() {
        StringBuilder objStr = new StringBuilder();

        objStr.append("Id: ").append(getId()).append("|BasicProperty: ");
        objStr = (getBasicProperty() != null) ? objStr.append(getBasicProperty().getUpicNo()) : objStr.append("");
        objStr = (getZone() != null) ? objStr.append("|Zone: ").append(getZone().getName()) : objStr.append("");
        objStr = (getWard() != null) ? objStr.append("|Ward: ").append(getWard().getName()) : objStr.append("");
        objStr.append("|Colony: ").append(getColonyId());
        objStr = (getArea() != null) ? objStr.append("|Area: ").append(getArea().getName()) : objStr.append("");
        objStr = (getLocality() != null) ? objStr.append("|Locality: ").append(getLocality().getName()) : objStr
                .append("");
        objStr = (getStreet() != null) ? objStr.append("|Street: ").append(getStreet().getName()) : objStr.append("");

        return objStr.toString();
    }
}
