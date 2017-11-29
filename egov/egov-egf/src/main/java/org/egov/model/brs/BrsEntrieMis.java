/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.model.brs;

import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.entity.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "BANKENTRIES_MIS")
@SequenceGenerator(name = BrsEntrieMis.SEQ_BANKENTRIES_MIS, sequenceName = BrsEntrieMis.SEQ_BANKENTRIES_MIS, allocationSize = 1)
public class BrsEntrieMis extends AbstractPersistable<Long>
{
    private static final long serialVersionUID = 8924403787977830824L;

    public static final String SEQ_BANKENTRIES_MIS = "SEQ_BANKENTRIES_MIS";

    @Id
    @GeneratedValue(generator = SEQ_BANKENTRIES_MIS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bankentriesid")
    private BrsEntries bankentries;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundId")
    private Fund fund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentid")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "functionid")
    private CFunction function;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundsourceid")
    private Fundsource fundsource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "divisionid")
    private Boundary division;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schemeid")
    private Scheme scheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subschemeid")
    private SubScheme subscheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "functionaryid")
    private Functionary functionary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BrsEntries getBankentries() {
        return bankentries;
    }

    public void setBankentries(BrsEntries bankentries) {
        this.bankentries = bankentries;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(CFunction function) {
        this.function = function;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Fundsource getFundsource() {
        return fundsource;
    }

    public void setFundsource(Fundsource fundsource) {
        this.fundsource = fundsource;
    }

    public Boundary getDivision() {
        return division;
    }

    public void setDivision(Boundary division) {
        this.division = division;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public SubScheme getSubscheme() {
        return subscheme;
    }

    public void setSubscheme(SubScheme subscheme) {
        this.subscheme = subscheme;
    }

    public Functionary getFunctionary() {
        return functionary;
    }

    public void setFunctionary(Functionary functionary) {
        this.functionary = functionary;
    }

}
