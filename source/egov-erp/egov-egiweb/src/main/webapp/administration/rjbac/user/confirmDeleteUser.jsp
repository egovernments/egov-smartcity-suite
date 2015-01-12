<%@page contentType="text/html" %>
<%@page import="org.egov.lib.rjbac.user.*" %>

<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>Delete User Confirm Page</title>
		<LINK rel="stylesheet" type="text/css" href="../../../css/egov.css">
	</head>
	<body bgcolor="#FFFFFF">
	
		<html:form action="DeleteUpdateUser" >
			<center>
			<table class="eGovTblMain" width="734" border="1">
				<tr>
						<td class="eGovTDMain" align="middle" width="728" height="6">
						Confirm Deletion 
						</td>
				</tr>
				<tr>
					<td width="728" height="100">
					<table border="1" width="730">
						<%
							//Gets the User from the session
							String 	userIdStr	=	"";
							User 	user 		= 	null;
							if((User)session.getAttribute("USER")!= null)
							{
								user		=	(User)session.getAttribute("USER");
								userIdStr	=	user.getId().toString();
							}
						%>	
						<tr>
							<td class="eGovTblContentSubHd" vAlign="center" align="middle" width="100%" height="30">
							<p align="center">Are You Sure Want to Delete USER <%= user.getUserName()%></td>
						</tr>
						
						<tr bgColor="#dddddd">
							<td vAlign="bottom" align="left" width="100%" height="23">
							<p align="center">
								<input type=button  value="DELETE" onclick="window.location = '/DeleteUpdateUser.do?bool=ConfirmDelete&userid=<%=userIdStr%>';">
							</td>
						</tr>
					</table>
				</tr>
			</table>
			</center>
		</html:form>
					

	</body>
</html>