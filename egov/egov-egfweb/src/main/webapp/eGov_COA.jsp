<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<%@ page import=" org.egov.lib.rrbac.services.RbacManager,
		org.egov.lib.rrbac.services.RbacManagerHome,
		org.egov.infstr.utils.ServiceLocator,
		org.egov.infstr.utils.HibernateUtil,
		 org.egov.lib.rrbac.model.Action;"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<title>eGovernance</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<%
System.out.println("actionid|||||||||||||||||||||||||||||||||||||||||||||||||="+request.getParameter("actionid"));
	String actionId=request.getParameter("actionid");
	if(actionId==null)
	{
		try
		{
			ServiceLocator serviceloc = ServiceLocator.getInstance();
			RbacManagerHome rbacMangrHome=null;
			RbacManager rbacManager=null;
			rbacMangrHome=(RbacManagerHome)serviceloc.getLocalHome("RbacManagerHome");
			rbacManager=rbacMangrHome.create();
			Action actobj= rbacManager.getActionByURL("/eGov_COA.jsp?window=left");
			actionId = actobj.getId().toString();
			System.out.println("actobj========="+actobj.getId());
		}catch(Exception e){actionId="553";}
	}
	if(actionId!=null)
		session.setAttribute("actionid",actionId);
		
		
%>

<frameset id="mainFrameset" rows="100%,*" cols="300,*" framespacing="0" border="0" frameborder="NO">
		<frame src="commonyui/egov/ChartOfaccountsMenuTree.jsp?eGovAppName=ChartOfAccounts&actionid=<%=actionId%>" name="leftFrame" id="leftFrame" scrolling="AUTO" >
		<frame src="index.jsp" name="mainFrame">
	</frameset>


<noframes>

<body >
</body></noframes>
</html>
