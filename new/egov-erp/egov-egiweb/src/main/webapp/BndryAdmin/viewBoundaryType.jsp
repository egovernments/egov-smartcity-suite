<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>



<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Add Update Delete BoundryType</title>


</head>

	<body bgcolor="#FFFFFF">
		<center>
<%
		String parentBoundaryType = "";
		parentBoundaryType = request.getParameter("parent");
		String targetBoundaryType = request.getParameter("target");
		String hasLeaf = request.getParameter("hasLeaf");
		if(hasLeaf == null)
			hasLeaf = "";
		//session.setAttribute("operation",operation);

%>
		<table class="tableStyle" border="1" width=746 bgcolor="#FFFFFF" borderColorDark="#000000" >
			<center>
			<tr>

				<td class="tableheader" align="middle" colSpan="2"width="100%">

				</td>

 			</tr>
			<tr>
				<td width="728" height="200">
					<table border="1" width="730">

			<input type="hidden" name="parentName" value="<%=targetBoundaryType%>">
			<tr>
				<td class="tableheader" vAlign="center" align="middle" width="100%" colSpan="2" height="56">
				<p align="center">
					<b>View properties of <%=targetBoundaryType%></b>
				</p>
				</td>
			</tr>

			<tr>
			<td class="labelcell" width="50%" height="76">
			<p align="center">
				<b>Boundary Type Name </b><font class="ErrorText">*</font>
			</p>

			<td class="labelcell" align="center" width="50%">
			<input type="text" name="name" value="<%=targetBoundaryType%>" readonly>
			<%-- Dumy field to make validation work! --%>

			</td>
			</tr>

			<tr class="labelcellforsingletd" colSpan="2" >
			</tr>
			<tr bgColor="#dddddd">
				<td vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
					<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font>
				</td>
			</tr>

			<tr bgColor="#dddddd">
					<td class="button2" vAlign="bottom" align="left" width="30%" colSpan="2" height="23">
					<p align="center">
<%
					if(hasLeaf.equalsIgnoreCase("false")){

%>
						<input type=button value="Add Child BoundryType" onclick="window.location = '/BndryAdmin/addUpdateDeleteBoundaryType.jsp?parent=<%=targetBoundaryType%>&operation=create';"/>
						<input type=button value="Delete" onclick="window.location = '/BndryAdmin/addUpdateDeleteBoundaryType.jsp?parent=<%=parentBoundaryType%>&operation=delete&target=<%=targetBoundaryType%>';"/></p></td>
<%
					}
%>
					<input type=button value="Edit" onclick="window.location = '/BndryAdmin/addUpdateDeleteBoundaryType.jsp?parent=<%=parentBoundaryType%>&operation=edit&target=<%=targetBoundaryType%>';"/>

			</tr>
			</table>
			</td>
			</tr>
			</table>
	</body>

</html>