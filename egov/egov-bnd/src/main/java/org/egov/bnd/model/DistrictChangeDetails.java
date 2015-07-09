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
/*
 * Created on Jul 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.bnd.model;

import java.util.Date;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class DistrictChangeDetails {

    private Integer id;
    private String oldDistrict;
    private String oldDistrictLocal;
    private String newDistrict;
    private String newDistrictLocal;
    private String govtNotificationNo;
    private String govtNotificationLocal;
    private Date effectiveDate;

    private Integer updatedBy;
    private Date updatedDate;

    private RegistrationUnit regUnit;

    private char isHistory;

    /**
     * @return Returns the updatedDate.
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     * @param updatedDate
     *            The updatedDate to set.
     */
    public void setUpdatedDate(final Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * default constructor. initializes an Address object too.
     */
    public DistrictChangeDetails() {
        super();
    }

    /**
     * @return Returns the id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * @return Returns the oldDistrict.
     */
    public String getOldDistrict() {
        return oldDistrict;
    }

    /**
     * @param oldDistrict
     *            The oldDistrict to set.
     */
    public void setOldDistrict(final String oldDistrict) {
        this.oldDistrict = oldDistrict;
    }

    /**
     * @return Returns the oldDistrictLocal.
     */
    public String getOldDistrictLocal() {
        return oldDistrictLocal;
    }

    /**
     * @param oldDistrictLocal
     *            The oldDistrictLocal to set.
     */
    public void setOldDistrictLocal(final String oldDistrictLocal) {
        this.oldDistrictLocal = oldDistrictLocal;
    }

    /**
     * @return Returns the newDistrict.
     */
    public String getNewDistrict() {
        return newDistrict;
    }

    /**
     * @param newDistrict
     *            The newDistrict to set.
     */
    public void setNewDistrict(final String newDistrict) {
        this.newDistrict = newDistrict;
    }

    /**
     * @return Returns the newDistrictLocal.
     */
    public String getNewDistrictLocal() {
        return newDistrictLocal;
    }

    /**
     * @param newDistrict
     *            The newDistrict to set.
     */
    public void setNewDistrictLocal(final String newDistrictLocal) {
        this.newDistrictLocal = newDistrictLocal;
    }

    /**
     * @return Returns the govtNotificationNo.
     */
    public String getGovtNotificationNo() {
        return govtNotificationNo;
    }

    /**
     * @param govtNotificationNo
     *            The govtNotificationNo to set.
     */
    public void setGovtNotificationNo(final String govtNotificationNo) {
        this.govtNotificationNo = govtNotificationNo;
    }

    /**
     * @return Returns the govtNotificationLocal.
     */
    public String getGovtNotificationLocal() {
        return govtNotificationLocal;
    }

    /**
     * @param govtNotificationNo
     *            The govtNotificationLocal to set.
     */
    public void setGovtNotificationLocal(final String govtNotificationLocal) {
        this.govtNotificationLocal = govtNotificationLocal;
    }

    /**
     * @return Returns the effectiveDate.
     */
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * @param regUnitDescLocal
     *            The regUnitDescLocal to set.
     */
    public void setEffectiveDate(final Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * @return Returns the updatedBy.
     */
    public Integer getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @param updatedBy
     *            The updatedBy to set.
     */
    public void setUpdatedBy(final Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public RegistrationUnit getRegUnit() {
        // TODO Auto-generated method stub
        return regUnit;
    }

    public void setRegUnit(final RegistrationUnit regUnit) {
        // TODO Auto-generated method stub
        this.regUnit = regUnit;
    }

    public char getIsHistory() {
        // TODO Auto-generated method stub
        return isHistory;
    }

    public void setIsHistory(final char isHistory) {
        this.isHistory = isHistory;
    }

}
