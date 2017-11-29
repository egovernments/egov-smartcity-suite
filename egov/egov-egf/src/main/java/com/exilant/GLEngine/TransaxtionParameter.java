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
//Source file: D:\\SUSHMA\\PROJECTS\\E-GOV\\ENGINEDESIGN\\com\\exilant\\GLEngine\\t

package com.exilant.GLEngine;

public class TransaxtionParameter
{

    /**
     * name of the detail key
     */
    protected String detailName;

    /**
     * value of the detail key
     */
    protected String detailKey;

    protected String detailAmt;

    protected String glcodeId;

    protected String tdsId;

    protected String detailTypeId;

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

    public void setDetailKey(final String aDetailKey)
    {
        detailKey = aDetailKey;
    }

    /**
     * Access method for the detailKey property.
     *
     * @return the current value of the detailKey property
     */
    public String getDetailTypeId()
    {
        return detailTypeId;
    }

    public void setDetailTypeId(final String aDetailType)
    {
        detailTypeId = aDetailType;
    }

    /**
     * Access methods for detail amount
     * @param aDetailKey
     */
    public String getDetailAmt()
    {
        return detailAmt;
    }

    /**
     * Access methods for set Detail amount
     * @param adetailAmt
     */
    public void setDetailAmt(final String adetailAmt)
    {
        detailAmt = adetailAmt;
    }

    /**
     * Access methods for GlCode Id
     * @param the current value of the glcodeId property
     */
    public String getGlcodeId()
    {
        return glcodeId;
    }

    /**
     * Access methods for set GlCode Id
     * @param aGlcodeId
     */
    public void setGlcodeId(final String aGlcodeId)
    {
        glcodeId = aGlcodeId;
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
