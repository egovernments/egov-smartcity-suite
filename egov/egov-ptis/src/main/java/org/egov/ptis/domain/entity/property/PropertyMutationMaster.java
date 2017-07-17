/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */


package org.egov.ptis.domain.entity.property;

import java.io.Serializable;

public class PropertyMutationMaster implements Serializable {
    private static final long serialVersionUID = 8232626581214227934L;

    private Long id = null;
    private String mutationName = null;
    private String mutationDesc = null;
    private String type = null;
    private String code = null;
    private String orderId = null;

    /**
     * @return Returns the idMutation.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param idMutation The idMutation to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Returns the mutationDesc.
     */
    public String getMutationDesc() {
        return mutationDesc;
    }

    /**
     * @param mutationDesc The mutationDesc to set.
     */
    public void setMutationDesc(String mutationDesc) {
        this.mutationDesc = mutationDesc;
    }

    /**
     * @return Returns the mutationName.
     */
    public String getMutationName() {
        return mutationName;
    }

    /**
     * @param mutationName The mutationName to set.
     */
    public void setMutationName(String mutationName) {
        this.mutationName = mutationName;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
