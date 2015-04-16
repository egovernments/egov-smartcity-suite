<%@page contentType="text/html" %>
<%@page import="java.util.*" %>
<%@ include file="/includes/taglibs.jsp" %>

<html>
<head>
	<title>Add Update Delete BoundryType</title>
	<LINK rel="stylesheet" type="text/css" href="/egi/css/egov.css">
	<html:javascript formName="boundryTypeForm"/>

</head>

	<body bgcolor="#FFFFFF">
		<center>
<%
		String parentBoundaryType = "";
		parentBoundaryType = request.getParameter("parent");
		String targetBoundaryType = request.getParameter("target");
		String operation = request.getParameter("operation");
		if(operation == null)
			operation = "";
		session.setAttribute("operation",operation);

%>
		<html:form  action="/BoundryType" onsubmit="return validateBoundryTypeForm(this);">
		<table class="tableheader" border="1" width=746 bgcolor="#FFFFFF" borderColorDark="#000000" >
			<center>
			<tr>

				<td class="tableheader" align="middle" colSpan="2"width="100%">

				</td>

 			</tr>
			<tr>
				<td width="728" height="200">
					<table border="1" width="730">

<%
		if(operation.equals("create"))
		{
%>

			<tr>
			<td class="tableheader" vAlign="center" align="middle" width="100%" colSpan="2" height="56">
			<p align="center">
<%
				if(parentBoundaryType!=null && !parentBoundaryType.equalsIgnoreCase("null"))
				{
%>
				<b>Create a new Boundary Type under <%=parentBoundaryType%></b>

<%
				}
				else
				{
%>
				<b>Create a new Boundary Type</b>
<%
				}
%>
			</p>
			</td>
			</tr>
			<tr>
			<td class="tableheader">
			<p align="center">
				<b>Boundary Type Name <b><font class="ErrorText">*</font>

			</p>

			<td class="tableheader" align="center" width="50%" height="76">
			<input type="text" name="name">
			</td>
			</tr>
			<input type="hidden" name="parentName" value="<%=parentBoundaryType%>">

			<%-- Dumy field to make validation work! --%>
			<input type="text" name="changedBoundaryTypeName" value="aa" Style="visibility:hidden">

			<tr class="tableheader" colSpan="2" >
			</tr>
			<tr bgColor="#dddddd">
				<td vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
					<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font>
				</td>
			</tr>
<%
		}
		else if(operation.equals("delete"))
		{

%>

			<tr>
			<td class="tableheader" vAlign="center" align="middle" width="110%" colSpan="2" height="80">
			<p align="center">
				<b>Are you sure you want to delete --- <font class="ErrorText" size="3"><%=targetBoundaryType%> </font>--- Boundary type under ---<font class="ErrorText"size="3"><%=parentBoundaryType%></font>---</b>
			</p>
			</td>
			</tr>
			<input type="hidden" name="operation" value="delete">
			<input type="hidden" name="parentName" value="<%=parentBoundaryType%>">
			<input type="hidden" name="name" value="<%=targetBoundaryType%>">
<%
		}
		else if(operation.equals("edit"))
		{
%>
			<input type="hidden" name="operation" value="edit">
			<input type="hidden" name="parentName" value="<%=targetBoundaryType%>">
			<tr>
				<td class="tableheader" vAlign="center" align="middle" width="100%" colSpan="2" height="56">
				<p align="center">
					<b>Change the properties of <%=targetBoundaryType%></b>
				</p>
				</td>
			</tr>

			<tr>
			<td class="tableheader" width="50%" height="76">
			<p align="center">
				<b>Change Boundary Type Name </b><font class="ErrorText">*</font>
			</p>

			<td class="tableheader" align="center" width="50%">
			<input type="text" name="name" value="<%=targetBoundaryType%>">
			<%-- Dumy field to make validation work! --%>
			<input type="text" name="newBoundaryTypeName" value="aa" Style="visibility:hidden">
			</td>
			</tr>

			<tr class="tableheader" colSpan="2" >
			</tr>
			<tr bgColor="#dddddd">
				<td vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
					<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font>
				</td>
			</tr>
<%
		}

%>

			<tr bgColor="#dddddd">
					<td vAlign="bottom" align="left" width="100%" colSpan="2" height="23">
					<p align="center">
					<html:submit value="Submit" /></td>
			</tr>
			</table>
			</td>
			</tr>
			</table>
		</html:form>
		
	</body>

</html>