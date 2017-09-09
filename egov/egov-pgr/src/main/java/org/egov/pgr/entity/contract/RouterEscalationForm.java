/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.entity.contract;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "pgr_router_escalation_view")
public class RouterEscalationForm {

    @Id
    private Long id;

    @Column(name = "ctid")
    private Long complainttype;

    private Long categoryid;

    @Column(name = "bndryid")
    private Long boundary;

    @Column(name = "routerpos")
    private Long position;

    private Long esclvl1;
    private Long esclvl2;
    private Long esclvl3;

    private String ctcode;
    private String ctname;
    private String categoryname;
    private String bndryname;
    private String routerposname;
    private String esclvl1posname;
    private String esclvl2posname;
    private String esclvl3posname;
    private Boolean active;

    public Long getComplainttype() {
        return complainttype;
    }

    public void setComplainttype(final Long complainttype) {
        this.complainttype = complainttype;
    }

    public Long getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(final Long categoryid) {
        this.categoryid = categoryid;
    }

    public Long getBoundary() {
        return boundary;
    }

    public void setBoundary(final Long boundary) {
        this.boundary = boundary;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(final Long position) {
        this.position = position;
    }

    public String getCtname() {
        return ctname;
    }

    public void setCtname(final String ctname) {
        this.ctname = ctname;
    }

    public String getBndryname() {
        return bndryname;
    }

    public void setBndryname(final String bndryname) {
        this.bndryname = bndryname;
    }

    public String getRouterposname() {
        return routerposname;
    }

    public void setRouterposname(final String routerposname) {
        this.routerposname = routerposname;
    }

    public String getEsclvl1posname() {
        return esclvl1posname;
    }

    public void setEsclvl1posname(final String esclvl1posname) {
        this.esclvl1posname = esclvl1posname;
    }

    public String getEsclvl2posname() {
        return esclvl2posname;
    }

    public void setEsclvl2posname(final String esclvl2posname) {
        this.esclvl2posname = esclvl2posname;
    }

    public String getEsclvl3posname() {
        return esclvl3posname;
    }

    public void setEsclvl3posname(final String esclvl3posname) {
        this.esclvl3posname = esclvl3posname;
    }

    public Long getEsclvl1() {
        return esclvl1;
    }

    public void setEsclvl1(final Long esclvl1) {
        this.esclvl1 = esclvl1;
    }

    public Long getEsclvl2() {
        return esclvl2;
    }

    public void setEsclvl2(final Long esclvl2) {
        this.esclvl2 = esclvl2;
    }

    public Long getEsclvl3() {
        return esclvl3;
    }

    public void setEsclvl3(final Long esclvl3) {
        this.esclvl3 = esclvl3;
    }

    public String getCtcode() {
        return ctcode;
    }

    public void setCtcode(final String ctcode) {
        this.ctcode = ctcode;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(final String categoryname) {
        this.categoryname = categoryname;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}