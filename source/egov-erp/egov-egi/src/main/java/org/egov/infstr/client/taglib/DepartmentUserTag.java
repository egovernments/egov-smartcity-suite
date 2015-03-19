/*
 * Created on Jul 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.infstr.client.taglib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.web.utils.ERPWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Manas TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class DepartmentUserTag extends BodyTagSupport {

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
	public int doStartTag() throws javax.servlet.jsp.JspTagException
	{
		System.out.println("In doStartTag() " + this.getDeptCollection());
		return SKIP_BODY;
	}

	@Override
	public int doEndTag()
	{
		System.out.println("In doEndTag() " + this.getDeptCollection());

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
				final Integer deptID = department.getId();
				final String deptname = department.getDeptName();
				deptMap.put(deptname, deptID);
			}
			for (final Iterator itr = deptMap.keySet().iterator(); itr.hasNext();)
			{
				final String dname = (String) itr.next();
				final int deptid = (Integer) deptMap.get(dname);
				deptUserSelectStr += "<option value=\"" + deptid + "\">" + dname;
			}

			deptUserSelectStr += "</select></td></tr>";

			// System.out.println("deptUserSelectStr  ::" + deptUserSelectStr);
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
			// System.out.println("deptUserStr" + deptUserStr);
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
		final String s1 = "if(document.forms[0]." + this.labelsList.get(2) + ".options[document.forms[0]." + this.labelsList.get(2) + ".selectedIndex].text == \"" + department.getDeptName()
				+ "\")\n{\n";
		String s2 = "";
		int i = 1;

		List userList = new ArrayList();
		final Map userMap = new TreeMap();
		final ApplicationContextBeanProvider provider = new ApplicationContextBeanProvider();
		provider.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(ERPWebApplicationContext.getServletContext()));
		userList = ((DepartmentService) provider.getBean("departmentService")).getAllUsersByDept(department, new Integer(cityid).intValue());
		s2 += "document.forms[0]." + this.labelsList.get(3) + ".options[0] = new Option(\"Choose\",\"\");";

		for (final Iterator userItr = userList.iterator(); userItr.hasNext();)
		{
			final User user = (User) userItr.next();
			final String userName = user.getUserName();
			final Long userId = user.getId();
			userMap.put(userName, userId);
		}
		// logger.info("userMap::"+userMap);
		for (final Iterator itr = userMap.keySet().iterator(); itr.hasNext();)
		{
			final String usrName = (String) itr.next();
			final int usrId = ((Integer) userMap.get(usrName)).intValue();
			// System.out.println("roleName::" + rname );

			s2 += "document.forms[0]." + this.labelsList.get(3) + ".options[" + i + "] = new Option(\"" + usrName + "\"," + usrId + ");\n";
			i++;
		}

		// logger.info("in allUsersForDepartment()  ::" + s1+s2+"}\n");
		return s1 + s2 + "}\n";
	}

}
