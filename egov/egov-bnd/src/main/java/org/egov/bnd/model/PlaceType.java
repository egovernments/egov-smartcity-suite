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
 * Created on Apr 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.bnd.model;

/**
 * @author sahina This class is the POJO for Place type used in CPlace
 * @hibernate.class table="EGBD_PLACETYPE"
 * @hibernate.cache usage="read-only"
 */
public class PlaceType {

    private Integer id = null;
    private String desc;
    private String descLocal;
    private String typeConst;

    /**
     * @hibernate.property name="desc"
     * @hibernate.column name="PLACETYPEDESC"
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
     * @hibernate.property name="descLocal"
     * @hibernate.column name="PLACETYPEDESCLOCAL"
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
     * @hibernate.id name="id" generator-class="native"
     * @hibernate.column name="PLACEID"
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
     * @hibernate.property name="typeConst"
     * @hibernate.column name="PLACETYPECONST"
     * @return Returns the typeConst.
     */
    public String getTypeConst() {
        return typeConst;
    }

    /**
     * @param typeConst
     *            The typeConst to set.
     */
    public void setTypeConst(final String typeConst) {
        this.typeConst = typeConst;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ID  :").append(id).append(" Description :").append(desc).append(" Type Constant :")
        .append(typeConst);
        return builder.toString();
    }

}
