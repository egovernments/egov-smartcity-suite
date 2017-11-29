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
//Source file: D:\\SUSHMA\\PROJECTS\\E-GOV\\ENGINEDESIGN\\com\\exilant\\GLEngine\\Transaxtion.java

package com.exilant.GLEngine;

import java.util.ArrayList;

public class Transaxtion
{

    /**
     * gl Code whose transaction is to be recorded
     */
    protected String glCode;

    /**
     * Name of the gl Code
     */
    protected String glName;

    /**
     * Amount to be debited from the glCode
     */
    protected String drAmount;

    /**
     * Amount to be credited for this GL Code
     */
    protected String crAmount;

    /**
     * Collection of Transaxtion class
     */

    protected String voucherLineId;
    protected String voucherDetailId;

    protected String voucherHeaderId;

    protected String narration = "";

    protected ArrayList transaxtionParameters;

    protected String functionId;

    protected String functionaryId;
    protected String schemeId;
    protected String subSchemeId;
    protected String field;
    protected String asOnDate;
    protected Long billId;

    /**
     * @return the billId
     */
    public Long getBillId() {
        return billId;
    }

    /**
     * @param billId the billId to set
     */
    public void setBillId(final Long billId) {
        this.billId = billId;
    }

    /**
     * @return Returns the asOnDate.
     */
    public String getAsOnDate() {
        return asOnDate;
    }

    /**
     * @param asOnDate The asOnDate to set.
     */
    public void setAsOnDate(final String asOnDate) {
        this.asOnDate = asOnDate;
    }

    /**
     * @return Returns the field.
     */
    public String getField() {
        return field;
    }

    /**
     * @param field The field to set.
     */
    public void setField(final String field) {
        this.field = field;
    }

    /**
     * @return Returns the functionaryId.
     */
    public String getFunctionaryId() {
        return functionaryId;
    }

    /**
     * @param functionaryId The functionaryId to set.
     */
    public void setFunctionaryId(final String functionaryId) {
        this.functionaryId = functionaryId;
    }

    /**
     * @return Returns the schemeId.
     */
    public String getSchemeId() {
        return schemeId;
    }

    /**
     * @param schemeId The schemeId to set.
     */
    public void setSchemeId(final String schemeId) {
        this.schemeId = schemeId;
    }

    /**
     * @return Returns the subSchemeId.
     */
    public String getSubSchemeId() {
        return subSchemeId;
    }

    /**
     * @param subSchemeId The subSchemeId to set.
     */
    public void setSubSchemeId(final String subSchemeId) {
        this.subSchemeId = subSchemeId;
    }

    // protected String functionId1;
    /**
     * @roseuid 41E26E230326
     */
    public Transaxtion()
    {

    }

    /**
     * Access method for the glCode property.
     *
     * @return the current value of the glCode property
     */
    public String getGlCode()
    {
        return glCode;
    }

    /**
     * Sets the value of the glCode property.
     *
     * @param aGlCode the new value of the glCode property
     */
    public void setGlCode(final String aGlCode)
    {
        glCode = aGlCode;
    }

    /**
     * Access method for the glName property.
     *
     * @return the current value of the glName property
     */
    public String getGlName()
    {
        return glName;
    }

    /**
     * Sets the value of the glName property.
     *
     * @param aGlName the new value of the glName property
     */
    public void setGlName(final String aGlName)
    {
        glName = aGlName;
    }

    /**
     * Access method for the drAmount property.
     *
     * @return the current value of the drAmount property
     */
    public String getDrAmount()
    {
        return drAmount;
    }

    /**
     * Sets the value of the drAmount property.
     *
     * @param aDrAmount the new value of the drAmount property
     */
    public void setDrAmount(final String aDrAmount)
    {
        drAmount = aDrAmount;
    }

    /**
     * Access method for the crAmount property.
     *
     * @return the current value of the crAmount property
     */
    public String getCrAmount()
    {
        return crAmount;
    }

    /**
     * Sets the value of the crAmount property.
     *
     * @param aCrAmount the new value of the crAmount property
     */
    public void setCrAmount(final String aCrAmount)
    {
        crAmount = aCrAmount;
    }

    public ArrayList getTransaxtionParam()
    {
        return transaxtionParameters;
    }

    public String getVoucherDetailId()
    {
        return voucherDetailId;
    }

    public void setVoucherDetailId(final String avoucherDetailId)
    {
        voucherDetailId = avoucherDetailId;
    }

    public String getVoucherLineId()
    {
        return voucherLineId;
    }

    public String getVoucherHeaderId()
    {
        return voucherHeaderId;
    }

    public String getNarration()
    {
        return narration;
    }

    public void setVoucherLineId(final String avoucherLineId)
    {
        voucherLineId = avoucherLineId;
    }

    public void setTransaxtionParam(final ArrayList aTrnPrm)
    {
        transaxtionParameters = aTrnPrm;
    }

    public void setVoucherHeaderId(final String avoucherHeaderId)
    {
        voucherHeaderId = avoucherHeaderId;
    }

    public void setNarration(final String aNarration)
    {
        narration = aNarration;
    }

    public String getFunctionId()
    {
        return functionId;
    }

    public void setFunctionId(final String afunctionId)
    {
        functionId = afunctionId;
    }
    /*
     * public String getfunctionId1() { return functionId1; } public void setFunctionId1(String afunctionId) { functionId1 =
     * afunctionId; }
     */
}
