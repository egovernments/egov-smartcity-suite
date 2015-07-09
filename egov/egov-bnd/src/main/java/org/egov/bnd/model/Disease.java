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
 * Created on May 26, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.bnd.model;

/**
 * @author administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class Disease {

    public Disease()

    {
        super();
    }

    private Long id;
    private String desc;
    private String descLocal;
    private String causeofdeathconst;
    private String diseasesCode;
    private Disease parent;

    public Disease getParent() {
        return parent;
    }

    public void setParent(final Disease parent) {
        this.parent = parent;
    }

    /**
     * @return Returns the causeofdeathconst.
     */
    public String getCauseofdeathconst() {
        return causeofdeathconst;
    }

    /**
     * @param causeofdeathconst
     *            The causeofdeathconst to set.
     */
    public void setCauseofdeathconst(final String causeofdeathconst) {
        this.causeofdeathconst = causeofdeathconst;
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
     * @return Returns the diseasesCode.
     */
    public String getDiseasesCode() {
        return diseasesCode;
    }

    /**
     * @param diseasesCode
     *            The diseasesCode to set.
     */
    public void setDiseasesCode(final String diseasesCode) {
        this.diseasesCode = diseasesCode;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(final Long id) {
        this.id = id;
    }
}
