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

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import javax.servlet.jsp.JspWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Manas TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class DepartmentUserTag extends RequestContextAwareTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List deptCollection = new ArrayList();
	private List labels = new ArrayList();
	private List labelsList = new ArrayList();
	private final Logger logger = Logger.getLogger(this.getClass());

	public DepartmentUserTag()
	{
		super();
	}

	public List getDeptCollection()
	{
		return this.deptCollection;
	}

	public void setDeptCollection(List deptCollection)
	{
		this.deptCollection = deptCollection;
	}

	public List getLabels()
	{
		return this.labels;
	}

	public void setLabels(List labels)
	{
		this.labels = labels;
	}

	private String getScript(String s)
	{
		final String lscriptStr1 = "<SCRIPT>";
		final String lscriptStr2 = "</SCRIPT>";
		final String lresutantStr = lscriptStr1 + s + lscriptStr2;
		return lresutantStr;
	}

	@Override
	public int doStartTagInternal() throws javax.servlet.jsp.JspTagException
	{
		return SKIP_BODY;
	}

	@Override
	public int doEndTag()
	{

		try
		{

			final javax.servlet.http.HttpSession session = this.pageContext.getSession();
			final String cityid = (String) session.getAttribute("org.egov.topBndryID");
			this.logger.info("cityid from session:::" + cityid);
			final List deptList = this.getDeptCollection();
			this.labelsList = this.getLabels();
			final Map deptMap = new TreeMap();

			String deptUserSelectStr = "<tr><td class=\"eGovTblContent\" width=\"40%\" height=\"23\" > " + this.labelsList.get(0) + "<font class=\"ErrorText\">*</font></td> " +
					"<td align=\"left\" width=\"40%\" height=\"23\" class=\"eGovTblContent\" >" +
					"<select name=\"" + this.labelsList.get(2) + "\" class=\"controlText\" onchange=\"populateUserSelect()\">";
			final String str = "Choose";
			deptUserSelectStr += "<option value=\"" + "" + "\">" + str;
			for (final Iterator deptItr = deptList.iterator(); deptItr.hasNext();)
			{
				final Department department = (Department) deptItr.next();
				// StringBuffer deptPopulateStr = new StringBuffer();
				// deptPopulateStr ="<html:select property=\""+"departmentId"+"\""+"styleClass=\""+"controlText"+"\">" +"\n"+"<\\html:select>" ;
				final Integer deptID = Integer.valueOf(department.getId().intValue());
				final String deptname = department.getName();
				deptMap.put(deptname, deptID);
			}
			for (final Iterator itr = deptMap.keySet().iterator(); itr.hasNext();)
			{
				final String dname = (String) itr.next();
				final int deptid = (Integer) deptMap.get(dname);
				deptUserSelectStr += "<option value=\"" + deptid + "\">" + dname;
			}

			deptUserSelectStr += "</select></td></tr>";

			deptUserSelectStr += "<tr><td class=\"eGovTblContent\" width=\"40%\" height=\"23\" >" + this.labelsList.get(1) + "<font class=\"ErrorText\">*</font></td> " +
					"<td align=\"left\" width=\"40%\" height=\"23\" class=\"eGovTblContent\" >" +
					"<select name=\"" + this.labelsList.get(3) + "\" class=\"controlText\"></select></td></tr>";

			// String x = "<tr><td><html:text property=\""+ "roleName"+"\" />"+"</td></tr>";
			// String xx = "<tr><td><input type=\"text\" name=\"rolename\"></td></tr>";
			String deptUserStr = "\nfunction populateUserSelect()" +
					"\n{" +
					"\n var len = document.forms[0]." + this.labelsList.get(3) + ".options.length;" +
					"\n for(var i=0;i<=len;i++)" +
					"\n{" +
					" \n\n document.forms[0]." + this.labelsList.get(3) + ".options[0] = null;" +
					"\n}\n";

			deptUserStr += "if(document.forms[0]." + this.labelsList.get(2) + ".options[document.forms[0]." + this.labelsList.get(2) + ".selectedIndex].text == \"Choose\")\n{\n" +
					"document.forms[0]." + this.labelsList.get(3) + ".options[0] = new Option(\"Choose\",\"\");\n}";

			for (final Iterator deptItr = deptList.iterator(); deptItr.hasNext();)
			{
				final Department department = (Department) deptItr.next();

				deptUserStr += this.allUsersForDepartment(department, this.labelsList, cityid);
			}

			deptUserStr += "}";
			final JspWriter out = this.pageContext.getOut();
			out.print(deptUserSelectStr);
			out.print(this.getScript(deptUserStr));
		} catch (final Exception ioe)
		{
			

		}

		return EVAL_PAGE;
	}

	private String allUsersForDepartment(Department department, List labelList, String cityid)
	{

		// labelsList =getLabels();
		final String s1 = "if(document.forms[0]." + this.labelsList.get(2) + ".options[document.forms[0]." + this.labelsList.get(2) + ".selectedIndex].text == \"" + department.getName()
				+ "\")\n{\n";
		String s2 = "";
		int i = 1;

		List userList = new ArrayList();
		final Map userMap = new TreeMap();
		//userList = ((DepartmentService) getRequestContext().getWebApplicationContext().getBean("departmentService")).getAllUsersByDept(department, new Integer(cityid).intValue());
		s2 += "document.forms[0]." + this.labelsList.get(3) + ".options[0] = new Option(\"Choose\",\"\");";

		for (final Iterator userItr = userList.iterator(); userItr.hasNext();)
		{
			final User user = (User) userItr.next();
			final String userName = user.getUsername();
			final Long userId = user.getId();
			userMap.put(userName, userId);
		}
		// logger.info("userMap::"+userMap);
		for (final Iterator itr = userMap.keySet().iterator(); itr.hasNext();)
		{
			final String usrName = (String) itr.next();
			final int usrId = ((Integer) userMap.get(usrName)).intValue();

			s2 += "document.forms[0]." + this.labelsList.get(3) + ".options[" + i + "] = new Option(\"" + usrName + "\"," + usrId + ");\n";
			i++;
		}

		// logger.info("in allUsersForDepartment()  ::" + s1+s2+"}\n");
		return s1 + s2 + "}\n";
	}

}
