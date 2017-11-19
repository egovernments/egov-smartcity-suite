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

package org.egov.infra.web.taglib;

/**
 * @author Manas
 */

import org.egov.infra.admin.master.entity.Department;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DeptRoleTag extends BodyTagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List name = new ArrayList();
	private List labels = new ArrayList();
	private List labelsList = new ArrayList();
	int editdeptid = 0;
	int editroleid = 0;
	private String colspan = "";

	public List getName() {
		return this.name;
	}

	public void setName(final List name) {
		this.name = name;
	}

	public String getColspan() {
		return this.colspan;
	}

	public void setColspan(final String colspan) {
		this.colspan = colspan;
	}

	public List getLabels() {
		return this.labels;
	}

	public void setLabels(final List labels) {
		this.labels = labels;
	}

	private String getScript(final String s) {
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
			final List deptList = this.getName();
			this.labelsList = this.getLabels();
			final Map deptMap = new TreeMap();
			final String colspn = this.getColspan();
			String deptRoleSelect = "<tr><td class=\"eGovTblContent\" width=\"40%\" height=\"23\" colspan=\"" + colspn + "\"> " + this.labelsList.get(0) + "<font class=\"ErrorText\">*</font></td> "
					+ "<td align=\"left\" width=\"40%\" height=\"23\" class=\"eGovTblContent\" colspan=\"" + colspn + "\"> " + "<select name=\"" + this.labelsList.get(2)
					+ "\" class=\"controlText\" onchange=\"populateRoleSelect()\">";
			final String str = "Choose";
			deptRoleSelect += "<option value=\"" + "" + "\">" + str;
			for (final Iterator deptItr = deptList.iterator(); deptItr.hasNext();) {
				final Department department = (Department) deptItr.next();
				// StringBuffer deptPopulateStr = new StringBuffer();
				// deptPopulateStr ="<html:select property=\""+"departmentId"+"\""+"styleClass=\""+"controlText"+"\">" +"\n"+"<\\html:select>" ;
				final Integer deptID = Integer.valueOf(department.getId().intValue());
				final String deptname = department.getName();
				deptMap.put(deptname, deptID);
			}

			if (this.labelsList.size() == 6) {
				if (this.labelsList.get(4) != null) {
					this.editdeptid = new Integer((String) this.labelsList.get(4));
				}
				if (this.labelsList.get(5) != null) {
					this.editroleid = new Integer((String) this.labelsList.get(5));
				}
			}
			for (final Iterator itr = deptMap.keySet().iterator(); itr.hasNext();) {
				final String dname = (String) itr.next();
				final int deptid = (Integer) deptMap.get(dname);
				deptRoleSelect += "<option " + (deptid == this.editdeptid && this.editdeptid != 0 ? "selected" : "") + " value=\"" + deptid + "\">" + dname;
				// deptRoleSelect += "<option "+(deptid==10?"selected":"")+"value=\""+(deptid==did?+"\"selected\""+":\"\")"+"\">" + dname ;
			}

			deptRoleSelect += "</select></td></tr>";

			deptRoleSelect += "<tr><td class=\"eGovTblContent\" width=\"40%\" height=\"23\" colspan=\"" + colspn + "\"> " + this.labelsList.get(1) + "<font class=\"ErrorText\">*</font></td> "
					+ "<td align=\"left\" width=\"40%\" height=\"23\" class=\"eGovTblContent\" colspan=\"" + colspn + "\"> " + "<select name=\"" + this.labelsList.get(3)
					+ "\" class=\"controlText\"></select></td></tr>";

			// String x = "<tr><td><html:text property=\""+ "roleName"+"\" />"+"</td></tr>";
			// String xx = "<tr><td><input type=\"text\" name=\"rolename\"></td></tr>";
			String deptRoleStr = "\nfunction populateRoleSelect()" + "\n{" + "\n var len = document.forms[0]." + this.labelsList.get(3) + ".options.length;" + "\n for(var i=0;i<=len;i++)" + "\n{"
					+ " \n\n document.forms[0]." + this.labelsList.get(3) + ".options[0] = null;" + "\n}\n";

			deptRoleStr += "if(document.forms[0]." + this.labelsList.get(2) + ".options[document.forms[0]." + this.labelsList.get(2) + ".selectedIndex].text == \"Choose\")\n{\n"
					+ "document.forms[0]." + this.labelsList.get(3) + ".options[0] = new Option(\"Choose\",\"\");\n}";

			for (final Iterator deptItr = deptList.iterator(); deptItr.hasNext();) {
				final Department department = (Department) deptItr.next();

				deptRoleStr += this.allRolesForDepartment(department.getName(), department.getId().intValue(), this.labelsList);
			}

			deptRoleStr += "}";
			final JspWriter out = this.pageContext.getOut();
			out.print(deptRoleSelect);
			out.print(this.getScript(deptRoleStr));
		} catch (final Exception ioe) {
			

		}

		return EVAL_PAGE;
	}

	private String allRolesForDepartment(final String dname, final int deptId, final List labelList) {

		final String deptStr = "department" + deptId + ".value";
		// labelsList =getLabels();
		final String s1 = "if(document.forms[0]." + this.labelsList.get(2) + ".options[document.forms[0]." + this.labelsList.get(2) + ".selectedIndex].text ==" + deptStr + ")\n{\n";
		String s2 = "";
		int i = 1;
		final Set roleSet = new TreeSet();
		final Map roleMap = new TreeMap();
		// FIXME no method supported in departmentService getAllRolesByDept set an alternative.
		// roleSet = departmentService.getAllRolesByDept(deptId);
		throw new UnsupportedOperationException("This method is missing implementation, Please check");
		/*s2 += "document.forms[0]." + this.labelsList.get(3) + ".options[0] = new Option(\"Choose\",\"\");";

		for (final Iterator roleItr = roleSet.iterator(); roleItr.hasNext();) {
			final Role role = (Role) roleItr.next();
			final String roleName = role.getRoleName();
			final Integer roleID = role.getId();
			roleMap.put(roleName, roleID);
		}
		for (final Iterator itr = roleMap.keySet().iterator(); itr.hasNext();) {
			final String rname = (String) itr.next();
			final int roleid = ((Integer) roleMap.get(rname)).intValue();

			s2 += "document.forms[0]." + this.labelsList.get(3) + ".options[" + i + "] = new Option(\"" + rname + "\"," + roleid + ","
					+ (roleid == this.editroleid && this.editroleid != 0 ? true : false) + "," + (roleid == this.editroleid && this.editroleid != 0 ? true : false) + ");\n";
			i++;
		}

		return s1 + s2 + "}\n";*/
	}

}
