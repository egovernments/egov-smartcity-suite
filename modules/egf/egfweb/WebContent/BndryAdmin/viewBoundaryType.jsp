<%@page contentType="text/html" %>
<%@page import="java.util.*" %>
<%@ include file="/egovheader.jsp" %>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Add Update Delete BoundryType</title>
	<LINK rel="stylesheet" type="text/css" href="../css/egov.css">
	
	
</head>
	
	<body bgcolor="#FFFFFF">
		<center>
<%
		String parentBoundaryType = "";
		parentBoundaryType = request.getParameter("parent");
		String targetBoundaryType = request.getParameter("target");
		/*String operation = request.getParameter("operation");
		if(operation == null)
			operation = "";*/
		//session.setAttribute("operation",operation);			

%>
		<html:form >
		<table class="eGovTblMain" border="1" width=746 bgcolor="#FFFFFF" borderColorDark="#000000" >
			<center>
			<tr>
			
				<td class="eGovTDMain" align="middle" colSpan="2"width="100%">
					
				</td>	
				
 			</tr>	
			<tr>
				<td width="728" height="200">
					<table border="1" width="730">
					
			<input type="hidden" name="parentName" value="<%=targetBoundaryType%>">
			<tr>
				<td class="eGovTblContentSubHd" vAlign="center" align="middle" width="100%" colSpan="2" height="56">
				<p align="center">
					<b>View properties of <%=targetBoundaryType%></b>
				</p>
				</td>
			</tr>
			
			<tr>
			<td class="eGovTblContent" width="50%" height="76">
			<p align="center">
				<b>Boundary Type Name </b><font class="ErrorText">*</font>
			</p>
			
			<td class="eGovTblContent" align="center" width="50%">	
			<input type="text" name="name" value="<%=targetBoundaryType%>" readonly>
			<%-- Dumy field to make validation work! --%>
			
			</td>
			</tr>
				
			<tr class="eGovTblContentSubHd" colSpan="2" >
			</tr>
			<tr bgColor="#dddddd">
				<td vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
					<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font>
				</td>
			</tr>
		
			<tr bgColor="#dddddd">
					<td vAlign="bottom" align="left" width="30%" colSpan="2" height="23">
					<p align="center">
					<input type=button value="Add Child BoundryType" onclick="window.location = '../BndryAdmin/addUpdateDeleteBoundaryType.jsp?parent=<%=targetBoundaryType%>&operation=create';"/>
					<input type=button value="Edit" onclick="window.location = '../BndryAdmin/addUpdateDeleteBoundaryType.jsp?parent=<%=parentBoundaryType%>&operation=edit&target=<%=targetBoundaryType%>';"/>
					<input type=button value="Delete" DISABLED onclick="window.location = '../BndryAdmin/addUpdateDeleteBoundaryType.jsp?parent=<%=parentBoundaryType%>&operation=delete&target=<%=targetBoundaryType%>';"/></p></td>
			</tr>
			</table>
			</td>
			</tr>
			</table>
		</html:form>
		<%@ include file="/egovfooter.jsp" %>
	</body>
	
</html>