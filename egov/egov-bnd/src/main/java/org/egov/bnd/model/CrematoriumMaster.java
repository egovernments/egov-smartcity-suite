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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CrematoriumMaster implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 700029770529384638L;
    private Integer id;
    private RegistrationUnit registrationUnit;
    private String crematoriumconst;
    private String crematoriumdesc;
    private Date createddate;
    private Integer creamatoriumtype;
    private Set<DeathRegistration> deathRegistrations = new HashSet<DeathRegistration>(0);

    public Integer getCreamatoriumtype() {
        return creamatoriumtype;
    }

    public void setCreamatoriumtype(final Integer creamatoriumtype) {
        this.creamatoriumtype = creamatoriumtype;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(final Date createddate) {
        this.createddate = createddate;
    }

    public String getCrematoriumconst() {
        return crematoriumconst;
    }

    public void setCrematoriumconst(final String crematoriumconst) {
        this.crematoriumconst = crematoriumconst;
    }

    public String getCrematoriumdesc() {
        return crematoriumdesc;
    }

    public void setCrematoriumdesc(final String crematoriumdesc) {
        this.crematoriumdesc = crematoriumdesc;
    }

    public Set<DeathRegistration> getDeathRegistrations() {
        return deathRegistrations;
    }

    public void setDeathRegistrations(final Set<DeathRegistration> deathRegistrations) {
        this.deathRegistrations = deathRegistrations;
    }

    public void addDeathRegistrations(final DeathRegistration deathRegistration) {
        if (deathRegistration != null)
            getDeathRegistrations().add(deathRegistration);
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public RegistrationUnit getRegistrationUnit() {
        return registrationUnit;
    }

    public void setRegistrationUnit(final RegistrationUnit registrationUnit) {
        this.registrationUnit = registrationUnit;
    }

}
