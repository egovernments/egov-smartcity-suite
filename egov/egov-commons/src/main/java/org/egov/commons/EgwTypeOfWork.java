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
package org.egov.commons;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;

@Entity
@Table(name = "EGW_TYPEOFWORK")
@Unique(id = "id", tableName = "EGW_TYPEOFWORK", columnName = { "code" }, fields = { "code" }, enableDfltMsg = true)
@SequenceGenerator(name = EgwTypeOfWork.SEQ_EGW_TYPEOFWORK, sequenceName = EgwTypeOfWork.SEQ_EGW_TYPEOFWORK, allocationSize = 1)
public class EgwTypeOfWork extends AbstractAuditable {

    private static final long serialVersionUID = 5516485821231001698L;

    public static final String SEQ_EGW_TYPEOFWORK = "SEQ_EGW_TYPEOFWORK";

    @Id
    @GeneratedValue(generator = SEQ_EGW_TYPEOFWORK, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentid")
    private EgwTypeOfWork parentid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partytypeid")
    private EgPartytype egPartytype;

    private String description;

    private boolean active = true;

    /**
     * @return the id
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return the parentid
     */
    public EgwTypeOfWork getParentid() {
        return parentid;
    }

    /**
     * @param parentid
     *            the parentid to set
     */
    public void setParentid(final EgwTypeOfWork parentid) {
        this.parentid = parentid;
    }

    /**
     * @return the egPartytype
     */
    public EgPartytype getEgPartytype() {
        return egPartytype;
    }

    /**
     * @param egPartytype
     *            the egPartytype to set
     */
    public void setEgPartytype(final EgPartytype egPartytype) {
        this.egPartytype = egPartytype;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

}
