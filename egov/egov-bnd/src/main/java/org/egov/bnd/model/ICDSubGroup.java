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
 * Created on May 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.bnd.model;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class ICDSubGroup {

    private long id;
    private String subGrpConst;
    private String subGrpname;
    private String subGrpDesc;
    private ICDMajorGroup majorGroup;

    /**
     * @return Returns the id.
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return Returns the majorGroup.
     */
    public ICDMajorGroup getMajorGroup() {
        return majorGroup;
    }

    /**
     * @param majorGroup
     *            The majorGroup to set.
     */
    public void setMajorGroup(final ICDMajorGroup majorGroup) {
        this.majorGroup = majorGroup;
    }

    /**
     * @return Returns the subGrpConst.
     */
    public String getSubGrpConst() {
        return subGrpConst;
    }

    /**
     * @param subGrpConst
     *            The subGrpConst to set.
     */
    public void setSubGrpConst(final String subGrpConst) {
        this.subGrpConst = subGrpConst;
    }

    /**
     * @return Returns the subGrpDesc.
     */
    public String getSubGrpDesc() {
        return subGrpDesc;
    }

    /**
     * @param subGrpDesc
     *            The subGrpDesc to set.
     */
    public void setSubGrpDesc(final String subGrpDesc) {
        this.subGrpDesc = subGrpDesc;
    }

    /**
     * @return Returns the subGrpname.
     */
    public String getSubGrpname() {
        return subGrpname;
    }

    /**
     * @param subGrpname
     *            The subGrpname to set.
     */
    public void setSubGrpname(final String subGrpname) {
        this.subGrpname = subGrpname;
    }
}
