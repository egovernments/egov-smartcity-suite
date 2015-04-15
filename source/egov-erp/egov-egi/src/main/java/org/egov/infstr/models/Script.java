/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr.models;

import java.util.Date;

import javax.script.CompiledScript;

import org.egov.commons.Period;
import org.egov.infstr.utils.DateUtils;

public class Script extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final String BY_NAME = "SCRIPT";
	public static final String QRY_SCRIPT_BY_NAME_DATE = "EGI_SCRIPT_BY_NAME_DATE";
	private String type;
	private String script;
	private String name;
	private Period period;
	private CompiledScript compiledScript;

	Script() {
		//FOR Hibernate
	}

	/**
	 * Creates a script with the given name,type and script body, valid between 01/01/1900 and 01/01/2100
	 * @param name
	 * @param type
	 * @param script
	 */
	public Script(String name, String type, String script) {
		this(name, type, script, DateUtils.createDate(1900), DateUtils.createDate(2100));
	}

	public Script(String name, String type, String script, Date startDate, Date endDate) {
		this.name = name;
		this.type = type;
		this.script = script;
		this.period = new Period(startDate, endDate);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period periods) {
		this.period = periods;
	}

	/**
	 * @return the <code>CompiledScript</code> object for this script
	 */
	public CompiledScript getCompiledScript() {
		return compiledScript;
	}

	/**
	 * @param compiledScript the <code>CompiledScript</code> object to set
	 */
	public void setCompiledScript(CompiledScript compiledScript) {
		this.compiledScript = compiledScript;
	}
}
