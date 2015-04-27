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
 * Created on Apr 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.bnd.model;

//import com.egov.infstr.client.*;
/**
 * @author administrator
 * @hibernate.class table="EGBD_EDUCATIONMASTER"
 * @hibernate.cache usage="read-only"
 */
public class Education {

    private Integer id;
    private String eduConst;
    private String eduDesc;
    private String eduDescLocal;

    /**
     * @hibernate.property name="eduConst"
     * @hibernate.column name="EDUCATIONLEVELCONST"
     * @return Returns the eduConst.
     */
    public String getEduConst() {
        return eduConst;
    }

    /**
     * @param eduConst
     *            The eduConst to set.
     */
    public void setEduConst(final String eduConst) {
        this.eduConst = eduConst;
    }

    /**
     * @hibernate.property name="eduDesc"
     * @hibernate.column name="EDUCATIONLEVELDESC"
     * @return Returns the eduDesc.
     */
    public String getEduDesc() {
        return eduDesc;
    }

    /**
     * @param eduDesc
     *            The eduDesc to set.
     */
    public void setEduDesc(final String eduDesc) {
        this.eduDesc = eduDesc;
    }

    /**
     * @hibernate.property name="eduDescLocal"
     * @hibernate.column name="EDUCATIONLEVELDESCLOCAL"
     * @return Returns the eduDescLocal.
     */
    public String getEduDescLocal() {
        return eduDescLocal;
    }

    /**
     * @param eduDescLocal
     *            The eduDescLocal to set.
     */
    public void setEduDescLocal(final String eduDescLocal) {
        this.eduDescLocal = eduDescLocal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

}
