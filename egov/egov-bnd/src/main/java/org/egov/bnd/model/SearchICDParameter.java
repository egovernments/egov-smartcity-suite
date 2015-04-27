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
package org.egov.bnd.model;

import java.io.Serializable;

public class SearchICDParameter implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3313490936745991429L;

    public SearchICDParameter() {
        super();
    }

    private String icdCode;
    private long majorGrp;
    private String subGrp;
    private String deathcause;

    /**
     * @return Returns the icdCode.
     */
    public String getICDCode() {
        return icdCode;
    }

    /**
     * @param icdCode
     *            The icdCode to set.
     */
    public void setICDCode(final String icdCode) {
        this.icdCode = icdCode;
    }

    /**
     * @return Returns the majorGrp.
     */
    public long getMajorgrp() {
        return majorGrp;
    }

    /**
     * @param majorGrp
     *            The majorGrp to set.
     */
    public void setMajorgrp(final long majorGrp) {
        this.majorGrp = majorGrp;
    }

    /**
     * @return Returns the subGrp.
     */
    public String getSubgrp() {
        return subGrp;
    }

    /**
     * @param subGrp
     *            The subGrp to set.
     */
    public void setSubgrp(final String subGrp) {
        this.subGrp = subGrp;
    }

    /**
     * @return Returns the deathcause.
     */
    public String getDeathcause() {
        return deathcause;
    }

    /**
     * @param deathcause
     *            The deathcause to set.
     */
    public void setDeathcause(final String deathcause) {
        this.deathcause = deathcause;
    }

}
