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
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "egpgr_mv_ageing_report_view")
public class AgeingReportForm {

    @Id
    private Long id;

    private String department;

    private String status;

    private String boundary;

    private Date createddate;

    private Long greater30;

    private Long btw10to30;

    private Long btw5to10;

    private Long btw2to5;

    private Long lsthn2;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(final String department) {
        this.department = department;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(final String boundary) {
        this.boundary = boundary;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(final Date createddate) {
        this.createddate = createddate;
    }

    public Long getGreater30() {
        return greater30;
    }

    public void setGreater30(final Long greater30) {
        this.greater30 = greater30;
    }

    public Long getBtw10to30() {
        return btw10to30;
    }

    public void setBtw10to30(final Long btw10to30) {
        this.btw10to30 = btw10to30;
    }

    public Long getBtw5to10() {
        return btw5to10;
    }

    public void setBtw5to10(final Long btw5to10) {
        this.btw5to10 = btw5to10;
    }

    public Long getBtw2to5() {
        return btw2to5;
    }

    public void setBtw2to5(final Long btw2to5) {
        this.btw2to5 = btw2to5;
    }

    public Long getLsthn2() {
        return lsthn2;
    }

    public void setLsthn2(final Long lsthn2) {
        this.lsthn2 = lsthn2;
    }

    public BigInteger getTotal() {
        return BigInteger.valueOf(greater30).add(BigInteger.valueOf(btw10to30)).add(BigInteger.valueOf(btw5to10))
                .add(BigInteger.valueOf(btw2to5)).add(BigInteger.valueOf(lsthn2));
    }

    public AgeingReportForm(final String name, final Long greater30, final Long btw10to30, final Long btw5to10,
            final Long btw2to5, final Long lsthn2) {
        department = name;
        boundary = name;
        this.greater30 = greater30;
        this.btw10to30 = btw10to30;
        this.btw5to10 = btw5to10;
        this.btw2to5 = btw2to5;
        this.lsthn2 = lsthn2;

    }

    public AgeingReportForm(final Long count) {

    }

}