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
/* Static Model */
package org.egov.bnd.model;

//import com.egov.infstr.client.*;

/**
 * @author Administrator This class is the POJO for AttentionTypes at Delivery
 * @hibernate.class table="EGBD_ATTENTIONATDELIVERY"
 * @hibernate.cache usage="read-only"
 */
public class AttentionType {

    public AttentionType()

    {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    private Integer m_attentionId;
    private Integer id;
    private String m_attentionDesc;
    private String m_attentionDescLocal;

    /**
     * @hibernate.property name="attentionDesc"
     * @hibernate.column name="TYPEOFATTENTIONDESC"
     * @return Returns the m_attentionDesc.
     */
    public String getAttentionDesc() {
        return m_attentionDesc;
    }

    /**
     * @param desc
     *            The m_attentionDesc to set.
     */
    public void setAttentionDesc(final String desc) {
        m_attentionDesc = desc;
    }

    /**
     * @hibernate.property name="attentionDescLocal"
     * @hibernate.column name="TYPEOFATTENTIONDESCLOCAL"
     * @return Returns the m_attentionDescLocal.
     */
    public String getAttentionDescLocal() {
        return m_attentionDescLocal;
    }

    /**
     * @param descLocal
     *            The m_attentionDescLocal to set.
     */
    public void setAttentionDescLocal(final String descLocal) {
        m_attentionDescLocal = descLocal;
    }

    /**
     * @hibernate.id name="attentionId"
     * @hibernate.column name="ATTENTIONATDELIVERYMASTERID"
     * @return Returns the m_attentionId.
     */
    public Integer getAttentionId() {
        return m_attentionId;
    }

    /**
     * @param id
     *            The m_attentionId to set.
     */
    public void setAttentionId(final Integer id) {
        m_attentionId = id;
    }
}
