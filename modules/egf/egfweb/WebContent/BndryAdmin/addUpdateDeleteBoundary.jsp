<%@page contentType="text/html" %>
<%@page import="java.util.*" %>
<%@ include file="/egovheader.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Create Update Delete Boundry</title>
	<LINK rel="stylesheet" type="text/css" href="../css/egov.css">
	<html:javascript formName="boundryForm"/>
	
</head>
	
	<body bgcolor="#FFFFFF">
		<center>
<%
		String operation = request.getParameter("operation");
		String parentBoundary = request.getParameter("parent");
		String pbnum = request.getParameter("parentBndryNum");
		String topLevelBoundaryID =  request.getParameter("topLevelBoundaryID");
		String bndryTypeHeirarchyLevel = request.getParameter("BndryTypeHeirarchyLevel");
		String targetBoundaryNum = request.getParameter("TargetBoundaryNum");
		String targetBoundaryName = request.getParameter("TargetBoundaryName");
		String parentBoundaryName = request.getParameter("parentBoundaryName");
		System.out.println("--------------operation"+operation);
		System.out.println("--------------pbnum"+pbnum);
		if(operation == null)
			operation = "";
		session.setAttribute("operation",operation);			

%>
		<html:form  action="administration/Boundry" onsubmit="return validateBoundryForm(this);">
		<table class="eGovTblMain" border="1" width=746 bgcolor="#FFFFFF" borderColorDark="#000000" >
			<center>
			<tr>
			
				<td class="eGovTDMain" align="middle" colSpan="2"width="100%">
					
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
			<td class="eGovTblContentSubHd" vAlign="center" align="middle" width="100%" colSpan="2" height="56">
			<p align="center">
<%			if(parentBoundary==null || parentBoundary.trim().equalsIgnoreCase("null"))
			{
	
%>

				<b>Create a new Boundary</b>
				
				
<%			
			}
			else
			{
%>

			<b>Create a new Boundary under <%=parentBoundary%></b>
<%			
			}
%>
			</p>
			</td>
			</tr>
			<tr>
			<td class="eGovTblContent">
			<p align="center">
				<b>Boundary Name <b><font class="ErrorText">*</font>
				
			</p>

			<td class="eGovTblContent" align="center" width="50%" height="76">
			<input type="text" name="name">
			</td>
			</tr>
			<tr>
			<td class="eGovTblContent">
			<p align="center">
				<b>Boundary Number <b><font class="ErrorText">*</font>

			</p>

			<td class="eGovTblContent" align="center" width="50%" height="76">
			<input type="text" name="boundaryNum">
			</td>
			</tr>
			<input type="hidden" name="parentBoundaryNum" value="<%=pbnum%>">
			<input type="hidden" name="topLevelBoundaryID" value="<%=topLevelBoundaryID%>">
			<input type="hidden" name="bndryTypeHeirarchyLevel" value="<%=bndryTypeHeirarchyLevel%>">

			
			
			<tr class="eGovTblContentSubHd" colSpan="2" >
			</tr>
			<tr bgColor="#dddddd">
				<td vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
					<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font>
				</td>
			</tr>
<%
		}
		else if(operation.equals("delete"))
		{System.out.println("--------------operation1"+operation);
		
%>
			
			<tr>
			<td class="eGovTblContent" vAlign="center" align="middle" width="100%" colSpan="2" height="26">
			<p align="center">
<%			
			if(parentBoundaryName!=null && !parentBoundaryName.trim().equalsIgnoreCase("null"))
			{
%>
				<b>Are you sure you want to Delete   <font class="ErrorText" size = "3"><%=targetBoundaryName%></font>  Boundary under <font class="ErrorText" size = "3"><%=parentBoundaryName%></font></b>
<%
			}
			else
			{
%>
				<b>Are you sure you want to Delete   ---<font class="ErrorText" size = "3"><%=targetBoundaryName%></font>---  Boundary

<%
			}
%>
			</p><input type="hidden" name="name" value="<%=targetBoundaryName%>">
			</td>
			</tr>
			<input type="hidden" name="boundaryNum" value="<%=targetBoundaryNum%>">
			<input type="hidden" name="TargetBoundaryNum" value="<%=targetBoundaryNum%>">
			<input type="hidden" name="topLevelBoundaryID" value="<%=topLevelBoundaryID%>">
			<input type="hidden" name="bndryTypeHeirarchyLevel" value="<%=bndryTypeHeirarchyLevel%>">
			

<%
			
		}
		else if(operation.equals("edit"))
		{
%>
			<tr>
				<td class="eGovTblContentSubHd" vAlign="center" align="middle" width="100%" colSpan="2" height="56">
				<p align="center">
					<b>Change the properties of <%=targetBoundaryName%></b>
				</p>
				</td>
			</tr>
			
			<tr>
			<td class="eGovTblContent" width="50%" height="76">
			<p align="center">
				<b>Change Boundary Name </b><font class="ErrorText">*</font>
			</p>
			
			<td class="eGovTblContent" align="center" width="50%">	
			<input type="text" name="name" value="<%=targetBoundaryName%>">
			</td>
			</tr>
			
			<tr>
				<td class="eGovTblContent" width="50%" height="76">
				<p align="center">
					<b>Change Boundary Number </b><font class="ErrorText">*</font>
				</p>

				<td class="eGovTblContent" align="center" width="50%">	
				<input type="text" name="boundaryNum" value="<%=targetBoundaryNum%>">
				</td>
			</tr>
				
			<tr class="eGovTblContentSubHd" colSpan="2" >
			</tr>
			<tr bgColor="#dddddd">
				<td vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
					<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font>
				</td>
			</tr>
			<input type="hidden" name="parentBoundaryNum" value="<%=targetBoundaryNum%>">
			<input type="hidden" name="TargetBoundaryNum" value="<%=targetBoundaryNum%>">
			<input type="hidden" name="topLevelBoundaryID" value="<%=topLevelBoundaryID%>">
			<input type="hidden" name="bndryTypeHeirarchyLevel" value="<%=bndryTypeHeirarchyLevel%>">
<% 
		}

%>
			
			<tr bgColor="#dddddd">
					<td vAlign="bottom" align="left" width="100%" colSpan="2" height="23">
					<p align="center">
					<html:submit value="SubmitHere" /></td>
			</tr>
			</table>
			</td>
			</tr>
			</table>
		</html:form>
		<%@ include file="/egovfooter.jsp" %>
	</body>
	
</html>