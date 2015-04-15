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
package org.egov.infstr.client.taglib;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class TagGreViewReports extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TagGreViewReports() {
		super();
	}

	private String getScript(final StringBuffer s) {
		final String lscriptStr1 = "<SCRIPT>";
		final String lscriptStr2 = "</SCRIPT>";
		final String lresutantStr = lscriptStr1 + s + lscriptStr2;
		return lresutantStr;
	}

	@Override
	public int doStartTag() throws javax.servlet.jsp.JspTagException {
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() {
		try {
			final javax.servlet.http.HttpSession gSession = this.pageContext.getSession();
			gSession.getAttribute("org.egov.topBndryID");
			// FIXME ConstructStringBndryDept is obsolate use some other way
			// final StringBuffer finalString = new StringBuffer(ConstructStringBndryDept.getFnString(toplevelID));
			final StringBuffer finalString = new StringBuffer("");
			final JspWriter out = this.pageContext.getOut();
			out.print(this.getScript(finalString));
		} catch (final Exception ioe) {
			

		}

		return EVAL_PAGE;
	}
}
