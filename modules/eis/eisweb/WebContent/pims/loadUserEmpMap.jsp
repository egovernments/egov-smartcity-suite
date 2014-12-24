<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.Iterator"%>
<%@ page language="java" import="java.util.List"%>
<%@ page language="java" import="org.egov.lib.rjbac.user.UserImpl,org.egov.pims.client.*"%>



<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%
	List empMapUserId = (List)session.getAttribute("empUserNotMappedList");
	Iterator iterate = empMapUserId.iterator();
	int i=0;
	UserImpl entry = new UserImpl();
%>

<html>
	<head>
		<title>List of Users</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script type="text/javascript">
			function getClosed(obj)
			{
				var usr = confirm("Selected user name is "+obj);
				if(usr)
				{
						window.opener.document.pIMSForm.userFirstName.value = obj;
						window.close();
				}			 
			}
		</script>	
	</head>
	<body >
		<table align="center" border=1 >
			<tr>
				<td class="whiteboxwk">Sl No</td>
				<td class="whiteboxwk">UserName</td>
			</tr>
			<%
				while(iterate.hasNext())
				{
					entry=(UserImpl)iterate.next();
					i++;
					if((i%2)==0)
					{
			%>
			<tr>
				<td class="greyboxwk"><%=i%></td>
				<td class="greybox2wk"><A href="#" onclick="getClosed('<%=entry.getUserName()%>')"><%=entry.getUserName()%></A></td>
				
			</tr>
			<%	
					}
					else
					{
			%>
			<tr>
				<td class="whiteboxwk" ><%=i%></td>
				<td class="whitebox2wk"><A href="#" onclick="getClosed('<%=entry.getUserName()%>')"><%=entry.getUserName()%></A></td>
				
			</tr>
			<%	
					}	
				}
			%>
			
		</table>			
	</body>
</html>
