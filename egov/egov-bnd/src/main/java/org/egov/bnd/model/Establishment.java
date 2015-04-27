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
 * Created on Apr 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.bnd.model;

/**
 * @author sahina This class is the POJO for Establishment. An establishment in
 *         a birth and death system is any official place where a birth/death
 *         event occurs
 * @hibernate.class table="EGBD_ESTABLISHMENTMASTER"
 **/
public class Establishment extends Place {

    /**
     *
     */
    private static final long serialVersionUID = 8765162420498256634L;
    private EstablishmentType type;
    // private Integer id;
    private String name;
    private String nameLocal;
    private Boolean isAuth;
    private RegistrationUnit regUnit;

    // private static Map typeEstablishments;

    /**
     * @hibernate.id name="id" generator-class="native"
     * @hibernate.column name="ESTABLISHMENTMASTERID"
     * @return Returns the id.
     */
    /*
     * public Integer getId() { return id; }
     *//**
     * @param id
     *            The id to set.
     */
    /*
     * public void setId(Integer id) { this.id = id; }
     */
    /**
     * @hibernate.property name="isAuth"
     * @hibernate.column name="ISAUTHBNDCERT"
     * @return Returns the isAuth as true if the establisment is authorized to
     *         certify birth and death
     */
    public Boolean getIsAuth() {
        return isAuth;
    }

    /**
     * @param isAuth
     *            The isAuth to set.
     */
    public void setIsAuth(final Boolean isAuth) {
        this.isAuth = isAuth;
    }

    /**
     * @hibernate.property name="name"
     * @hibernate.column name="ESTABLISHMENTNAMEDESC"
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @hibernate.property name="nameLocal"
     * @hibernate.column name="ESTABLISHMENTNAMEDESCLOCAL"
     * @return Returns the nameLocal.
     */
    public String getNameLocal() {
        return nameLocal;
    }

    /**
     * @param nameLocal
     *            The nameLocal to set.
     */
    public void setNameLocal(final String nameLocal) {
        this.nameLocal = nameLocal;
    }

    /**
     * @hibernate.many-to-one column="ESTABLISHMENTTYPEID"
     *                        class="com.egov.bnd.registration.client.CEstablishmentType"
     *                        cascade="none" unique="false"
     * @return Returns the type.
     */
    public EstablishmentType getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(final EstablishmentType type) {
        this.type = type;
    }

    /**
     * @return Returns the regUnit.
     */
    public RegistrationUnit getRegUnit() {
        return regUnit;
    }

    /**
     * @param regUnit
     *            The regUnit to set.
     */
    public void setRegUnit(final RegistrationUnit regUnit) {
        this.regUnit = regUnit;
    }
}
