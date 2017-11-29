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
//Source file: D:\\SUSHMA\\PROJECTS\\E-GOV\\ENGINEDESIGN\\com\\exilant\\GLEngine\\GLParameters.java

package com.exilant.GLEngine;

import java.io.Serializable;

public class GLParameter implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 2875426573880846321L;
    /**
     * if this detail is required for posting?
     */

    private int glcodeId;
    /**
     * Name of the detail the GL code requires
     */
    private int detailId;

    private String detailName;

    /**
     * The value of the detail for that detail name
     */
    private String detailKey;

    private String detailAmt;

    private String tdsId;

    public int getDetailId()
    {
        return detailId;
    }

    /**
     * Sets the value of the detailName property.
     *
     * @param aDetailName the new value of the detailName property
     */
    public void setDetailId(final int aDetailId)
    {
        detailId = aDetailId;
    }

    /**
     * Access method for the detailName property.
     *
     * @return the current value of the detailName property
     */
    public String getDetailName()
    {
        return detailName;
    }

    /**
     * Sets the value of the detailName property.
     *
     * @param aDetailName the new value of the detailName property
     */
    public void setDetailName(final String aDetailName)
    {
        detailName = aDetailName;
    }

    /**
     * Access method for the detailKey property.
     *
     * @return the current value of the detailKey property
     */
    public String getDetailKey()
    {
        return detailKey;
    }

    /**
     * Sets the value of the detailKey property.
     *
     * @param aDetailKey the new value of the detailKey property
     */
    public void setDetailKey(final String aDetailKey)
    {
        detailKey = aDetailKey;
    }

    /**
     * Access method for the detailAmt property.
     *
     * @return the current value of the detailKey property
     */
    public String getDetailAmt()
    {
        return detailAmt;
    }

    /**
     * Sets the value of the detailAmt property.
     *
     * @param aDetailAmt the new value of the detailAmt property
     */
    public void setDetailAmt(final String aDetailAmt)
    {
        detailAmt = aDetailAmt;
    }

    /**
     * @return Returns the glcodeId.
     */
    public int getGlcodeId()
    {
        return glcodeId;
    }

    /**
     * @param glcodeId The glcodeId to set.
     */
    public void setGlcodeId(final int glcodeId)
    {
        this.glcodeId = glcodeId;
    }

    /**
     * @return the tdsId
     */
    public String getTdsId()
    {
        return tdsId;
    }

    /**
     * @param tdsId the tdsId to set
     */
    public void setTdsId(final String tdsId)
    {
        this.tdsId = tdsId;
    }
}
