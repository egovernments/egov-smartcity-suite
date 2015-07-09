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
 * Created on Apr 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.bnd.model;

import java.io.Serializable;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class CRelation implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2127330741797578929L;
    private Integer id;
    private String relatedAsConst;
    private String desc;
    private String descLocal;

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

    /**
     * @return Returns the relatedAsConst.
     */
    public String getRelatedAsConst() {
        return relatedAsConst;
    }

    /**
     * @param relatedAsConst
     *            The relatedAsConst to set.
     */
    public void setRelatedAsConst(final String relatedAsConst) {
        this.relatedAsConst = relatedAsConst;
    }

    /*
     * public boolean equals(Object obj) {
     * System.out.println("CRelation:in equals::::::::::"); if(obj == null)
     * return false; CRelation tr = (CRelation)obj; if(this == tr) return true;
     * if(this.getId() != null && tr.getId() != null) {
     * if(this.getId().equals(tr.getId())) return true; else return false; }
     * if(this.getRelatedAsConst() != null && tr.getRelatedAsConst() != null) {
     * if(this.getRelatedAsConst().equals(tr.getRelatedAsConst())) { return
     * true; } else return false; } else return false; } public int hashCode() {
     * System.out.println("CRelation:in hashcode::::::::::"); int hashcode = 0;
     * if(this.getId() != null) { hashcode += this.getId().hashCode(); }
     * if(this.getRelatedAsConst() != null) { hashcode +=
     * this.getRelatedAsConst().hashCode(); } return hashcode; }
     */

}
