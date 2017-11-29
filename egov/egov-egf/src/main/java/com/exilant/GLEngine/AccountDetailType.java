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
//Source file: D:\\SUSHMA\\PROJECTS\\E-GOV\\ENGINEDESIGN\\com\\exilant\\GLEngine\\AccountDetailType.java

package com.exilant.GLEngine;

import java.io.Serializable;

public class AccountDetailType implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -4786588582684678013L;

    /**
     * Id for the AccountDetailType class
     */
    private int id;

    /**
     * Name of the detail type
     */
    private String name;

    /**
     * Description of the detail type
     */
    private String description;

    /**
     * Table name in the database to verify if the detail key is valid or not
     */
    private String tableName;

    /**
     * Similar to the table name, the column name in the table to validate the detail key
     */
    private String columnName;

    /**
     * String with which the GL engine compares for the valid detail entered by any client
     */
    private String attributeName;

    /**
     * Number of levels, if any for this detail type
     */
    private int nbrOfLevels;

    /**
     * @roseuid 42076049021D
     */
    public AccountDetailType()
    {

    }

    /**
     * Access method for the Id property.
     *
     * @return the current value of the Id property
     */
    public int getId()
    {
        return id;
    }

    /**
     * Sets the value of the Id property.
     *
     * @param aId the new value of the Id property
     */
    public void setId(final int aId)
    {
        id = aId;
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

    /**
     * Access method for the description property.
     *
     * @return the current value of the description property
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param aDescription the new value of the description property
     */
    public void setDescription(final String aDescription)
    {
        description = aDescription;
    }

    /**
     * Access method for the tableName property.
     *
     * @return the current value of the tableName property
     */
    public String getTableName()
    {
        return tableName;
    }

    /**
     * Sets the value of the tableName property.
     *
     * @param aTableName the new value of the tableName property
     */
    public void setTableName(final String aTableName)
    {
        tableName = aTableName;
    }

    /**
     * Access method for the columnName property.
     *
     * @return the current value of the columnName property
     */
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * Sets the value of the columnName property.
     *
     * @param aColumnName the new value of the columnName property
     */
    public void setColumnName(final String aColumnName)
    {
        columnName = aColumnName;
    }

    /**
     * Access method for the attributeName property.
     *
     * @return the current value of the attributeName property
     */
    public String getAttributeName()
    {
        return attributeName;
    }

    /**
     * Sets the value of the attributeName property.
     *
     * @param aAttributeName the new value of the attributeName property
     */
    public void setAttributeName(final String aAttributeName)
    {
        attributeName = aAttributeName;
    }

    /**
     * Access method for the nbrOfLevels property.
     *
     * @return the current value of the nbrOfLevels property
     */
    public int getNbrOfLevels()
    {
        return nbrOfLevels;
    }

    /**
     * Sets the value of the nbrOfLevels property.
     *
     * @param aNbrOfLevels the new value of the nbrOfLevels property
     */
    public void setNbrOfLevels(final int aNbrOfLevels)
    {
        nbrOfLevels = aNbrOfLevels;
    }
}
