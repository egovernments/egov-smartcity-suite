

<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryServiceImpl"%>
<%@ page import= "org.egov.lib.rjbac.dept.ejb.api.*,
		org.egov.lib.rjbac.dept.*,
		org.egov.lib.admbndry.ejb.api.*,
		org.egov.lib.admbndry.*,
		org.egov.infstr.utils.*,
 		java.sql.Date,
 		java.text.SimpleDateFormat,
 		java.util.*,
 		javax.naming.*,
 		java.text.ParsePosition,
 		org.egov.exceptions.*"
%>

<%
	
	BoundaryServiceImpl boundary = new BoundaryServiceImpl();
	String mainCityid = (String)session.getAttribute("org.egov.topBndryID");
	System.out.println("mainCityid --> :"+mainCityid);

	String urlID = "";
	String img ="";
	String img1 ="";
	String cmcName ="";

	//System.out.println("************************** Session"+HibernateUtil.getCurrentSession());
	//HibernateUtil.beginTransaction();
	//System.out.println("************************** Session"+HibernateUtil.getCurrentSession());
	String toplevelbndryName = boundary.getBoundaryNameForID(new Integer(mainCityid));
	//String toplevelbndryName = "Ramanagara";
	urlID = toplevelbndryName + ".url";
	String required = EGovConfig.getProperty("LOGO_REQUIRED","", "PGR");
	System.out.println("required>>>>>>>>>>>>>>>>>"+required);


%>
		<tr>
		<td bgColor="#FFFFFF" width="2"> </td>
		<%
			if(required.equals("YES"))
			{
				System.out.println("required124124>>>>>>>>>>>>>>>>>"+required);
		%>
		<td vAlign="center"  align="left"  width="107">
		<p align="center">
		<!--<a  id="imaID" name="imaID" href=<bean:message key="<%=urlID%>"/>
		<img src="/images/logo-1.jpg" width="70" height="70" border="0">
		</a>-->
		<img src="/images/logo-1.jpg" width="70" height="70" border="0">
		</td>
		<%
			}
			else
			{
		%>
		<td vAlign="center"  align="center"  width="252">
				<p align="center">

		</td>
		<%
			}

		%>

		<!--<td  width="500"  align="center">
		<img border="0" src="${pageContext.request.contextPath}/images/delhi-headerTitle.gif" ><font size = "6" id="header_font" ><br>
		</font>
		</td>-->
		<td  width="600"  align="center"><font size = "6" id="header_font">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;eGov Payroll System
				</font>
		</td>


		<%
			if(required.equals("YES"))
			{

		%>
		<!--<td align="middle"  width="124">
				<a id="orgID" name="orgID" href="//www.egovernments.org"><img src="${pageContext.request.contextPath}/images/egovlogo-pwr-trans.gif" border="0" width="102" height="102"align="right"   id="logoID"> </a></td>
		<td bgColor="#FFFFFF" height="102" width="1"> </td>-->
		<td align="middle"  width="250">
		<a id="orgID" name="orgID" href="//www.egovernments.org"><img src="${pageContext.request.contextPath}/images/egovlogo-pwr-trans.gif" width="70" height="70" border="0" align="right"   id="logoID"> </a></td>
		<td bgColor="#FFFFFF" height="102" width="1"> </td>

		<%
			}
			//HibernateUtil.closeSession();
		%>

		</tr>


