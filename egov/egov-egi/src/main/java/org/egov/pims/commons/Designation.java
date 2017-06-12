/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
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

package org.egov.pims.commons;

import static org.egov.pims.commons.Designation.SEQ_DESIGNATION;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.egov.commons.CChartOfAccounts;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.validation.regex.Constants;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "eg_designation")
@Unique(fields = { "name", "code" }, enableDfltMsg = true)
@SequenceGenerator(name = SEQ_DESIGNATION, sequenceName = SEQ_DESIGNATION, allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "getDesignationForListOfDesgNames", query = "from Designation where trim(upper(name)) in(:param_0)"),
        @NamedQuery(name = "getDesignationForActiveAssignmentsByListOfDesgNames", query = "select distinct A.designation from  Assignment A where A.fromDate<=current_date and A.toDate>=current_date and trim(upper(A.designation.name)) in(:param_0)") })
public class Designation extends AbstractAuditable {

    public static final String SEQ_DESIGNATION = "SEQ_EG_DESIGNATION";
    private static final long serialVersionUID = -3775503109625394145L;
    @Id
    @GeneratedValue(generator = SEQ_DESIGNATION, strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotBlank
    @SafeHtml
    @Pattern(regexp = Constants.ALLTYPESOFALPHABETS_WITHMIXEDCHAR, message = "Name should contain letters with space and (-,_)")
    private String name;
    @NotBlank
    @SafeHtml
    // @Pattern(regexp = Constants.ALLTYPESOFALPHABETS_WITHMIXEDCHAR, message =
    // "Name should contain letters with space and (-,_)")
    private String code;
    @SafeHtml
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chartofaccounts")
    private CChartOfAccounts chartOfAccounts;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "egeis_desig_rolemapping", joinColumns = @JoinColumn(name = "designationid"), inverseJoinColumns = @JoinColumn(name = "roleid"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public CChartOfAccounts getChartOfAccounts() {
        return chartOfAccounts;
    }

    public void setChartOfAccounts(final CChartOfAccounts chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

}