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
 * Created on May 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.bnd.model;

import java.math.BigDecimal;

/**
 * @author Manas kumar TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class Addiction {
    private Integer id;
    private String addictionConst;
    private String desc;
    private String descLocal;
    private BigDecimal noOfYears;

    public BigDecimal getNoOfYears() {
        return noOfYears;
    }

    public void setNoOfYears(final BigDecimal noOfYears) {
        this.noOfYears = noOfYears;
    }

    /**
     * @return Returns the addictionConst.
     */
    public String getAddictionConst() {
        return addictionConst;
    }

    /**
     * @param addictionConst
     *            The addictionConst to set.
     */
    public void setAddictionConst(final String addictionConst) {
        this.addictionConst = addictionConst;
    }

    /**
     * @return Returns the desc.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc
     *            The desc to set.
     */
    public void setDesc(final String desc) {
        this.desc = desc;
    }

    /**
     * @return Returns the descLocal.
     */
    public String getDescLocal() {
        return descLocal;
    }

    /**
     * @param descLocal
     *            The descLocal to set.
     */
    public void setDescLocal(final String descLocal) {
        this.descLocal = descLocal;
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

    @Override
    public boolean equals(final Object obj) {
        System.out.println("CAddiction:in equals::::::::::");
        if (obj == null)
            return false;
        final Addiction tr = (Addiction) obj;

        if (this == tr)
            return true;

        if (getId() != null && tr.getId() != null)
            if (getId().equals(tr.getId()))
                return true;
            else
                return false;

        if (getAddictionConst() != null && tr.getAddictionConst() != null) {
            if (getAddictionConst().equals(tr.getAddictionConst()))
                return true;
            else
                return false;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        System.out.println("CAddiction:in hashcode::::::::::");
        int hashcode = 0;
        if (getId() != null)
            hashcode += getId().hashCode();
        if (getAddictionConst() != null)
            hashcode += getAddictionConst().hashCode();
        return hashcode;
    }

}
