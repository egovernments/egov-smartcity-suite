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
//Source file: D:\\SUSHMA\\PROJECTS\\E-GOV\\ENGINEDESIGN\\com\\exilant\\GLEngine\\GLAccount.java

package com.exilant.GLEngine;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * a hashmap with key as id
 */
public class GLAccount implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 7369183258745701979L;

    /**
     * Id of the GLAccount
     */
    private long ID;

    /**
     * GLCode
     */
    private String glCode;

    /**
     * Name of the account code or GL code
     */
    private String name;

    private boolean isActiveForPosting;
    
    private Boolean functionRequired;
    
    private Long classification;

    private ArrayList glParameters = new ArrayList();

    /**
     * @roseuid 41E23F6F0202
     */
    public GLAccount()
    {

    }

    /**
     * Access method for the id property.
     *
     * @return the current value of the id property
     */
    public long getId()
    {
        return ID;
    }

    /**
     * Sets the value of the id property.
     *
     * @param aId the new value of the id property
     */
    public void setID(final int aId)
    {
        ID = aId;
    }

    /**
     * Access method for the code property.
     *
     * @return the current value of the code property
     */
    public String getCode()
    {
        return glCode;
    }

    /**
     * Sets the value of the code property.
     *
     * @param aCode the new value of the code property
     */
    public void setCode(final String aCode)
    {
        glCode = aCode;
    }

    /**
     * Access method for the name property.
     *
     * @return the current value of the name property
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param aName the new value of the name property
     */
    public void setName(final String aName)
    {
        name = aName;
    }

    public boolean isActiveForPosting()
    {
        return isActiveForPosting;
    }

    /**
     * Sets the value of the id property.
     *
     * @param aId the new value of the id property
     */
    public void setIsActiveForPosting(final boolean active)
    {
        isActiveForPosting = active;
    }

    public void setGLParameters(final ArrayList aglParameters)
    {
        glParameters = aglParameters;
    }

    public ArrayList getGLParameters()
    {
        return glParameters;
    }

    public boolean getIsActiveForPosting() {
        return isActiveForPosting;
    }

        public Boolean getFunctionRequired() {
                return functionRequired;
        }

        public void setFunctionRequired(Boolean functionRequired) {
                this.functionRequired = functionRequired;
        }

        public Long getClassification() {
                return classification;
        }

        public void setClassification(Long classification) {
                this.classification = classification;
        }

}
