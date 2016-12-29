/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.entity.dto;

import java.math.BigInteger;

/**
 * Created by jayashree on 19/12/16.
 */
public class RouterEscalationForm {
    private Long complainttype;
    private Long category;
    private Long boundary;
    private Long position;

    private String ctname;
    private String bndryname;
    private String routerposname;
    private String esclvl1posname;
    private String esclvl2posname;
    private String esclvl3posname;

    public Long getComplainttype() {
        return complainttype;
    }

    public void setComplainttype(Long complainttype) {
        this.complainttype = complainttype;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public Long getBoundary() {
        return boundary;
    }

    public void setBoundary(Long boundary) {
        this.boundary = boundary;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getCtname() {
        return ctname;
    }

    public void setCtname(String ctname) {
        this.ctname = ctname;
    }

    public String getBndryname() {
        return bndryname;
    }

    public void setBndryname(String bndryname) {
        this.bndryname = bndryname;
    }

    public String getRouterposname() {
        return routerposname;
    }

    public void setRouterposname(String routerposname) {
        this.routerposname = routerposname;
    }

    public String getEsclvl1posname() {
        return esclvl1posname;
    }

    public void setEsclvl1posname(String esclvl1posname) {
        this.esclvl1posname = esclvl1posname;
    }

    public String getEsclvl2posname() {
        return esclvl2posname;
    }

    public void setEsclvl2posname(String esclvl2posname) {
        this.esclvl2posname = esclvl2posname;
    }

    public String getEsclvl3posname() {
        return esclvl3posname;
    }

    public void setEsclvl3posname(String esclvl3posname) {
        this.esclvl3posname = esclvl3posname;
    }
}
