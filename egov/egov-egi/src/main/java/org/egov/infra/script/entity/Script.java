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

package org.egov.infra.script.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.entity.component.Period;
import org.joda.time.DateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.script.CompiledScript;

import static org.egov.infra.script.entity.Script.SEQ_SCRIPT;

@Entity
@Table(name = "eg_script")
@SequenceGenerator(name = SEQ_SCRIPT, sequenceName = SEQ_SCRIPT, allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = Script.BY_NAME,
                query = "select s from Script s where s.name=:name and current_date between period.startDate and period.endDate"),
        @NamedQuery(name = Script.BY_NAME_AND_DATE,
                query = "select s from Script as s where s.name=:name and :date between period.startDate and period.endDate")
})
public class Script extends AbstractAuditable {
    public static final String SEQ_SCRIPT = "SEQ_EG_SCRIPT";
    public static final String BY_NAME = "Script.findByName";
    public static final String BY_NAME_AND_DATE = "Script.findByNameAndPeriod";
    private static final long serialVersionUID = -2464312999181924258L;
    @Id
    @GeneratedValue(generator = SEQ_SCRIPT, strategy = GenerationType.SEQUENCE)
    private Long id;
    private String type;
    private String script;
    private String name;
    @Embedded
    private Period period;

    @Transient
    private CompiledScript compiledScript;

    Script() {
    }

    public Script(final String name, final String type, final String script) {
        this(name, type, script, new DateTime(), new DateTime().plusYears(100));
    }

    public Script(final String name, final String type, final String script, final DateTime startDate, final DateTime endDate) {
        this.name = name;
        this.type = type;
        this.script = script;
        period = new Period(startDate.toDate(), endDate.toDate());
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getScript() {
        return script;
    }

    public void setScript(final String script) {
        this.script = script;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(final Period periods) {
        period = periods;
    }

    public CompiledScript getCompiledScript() {
        return compiledScript;
    }

    public void setCompiledScript(final CompiledScript compiledScript) {
        this.compiledScript = compiledScript;
    }

}
