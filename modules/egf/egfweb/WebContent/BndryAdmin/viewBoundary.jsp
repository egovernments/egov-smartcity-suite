<%@page contentType="text/html" %>
<%@page import="java.util.*" %>
<%@ include file="/egovheader.jsp" %>

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
		
		//String parentBoundary = request.getParameter("parent");
		//System.out.println("********************parentBoundary 1: "+parentBoundary);
		String pbnum = request.getParameter("parentBndryNum");
		String topLevelBoundaryID =  request.getParameter("topLevelBoundaryID");
		String bndryTypeHeirarchyLevel = request.getParameter("BndryTypeHeirarchyLevel");
		String targetBoundaryNum = request.getParameter("TargetBoundaryNum");
		String targetBoundaryName = request.getParameter("TargetBoundaryName");
		String parentBoundary = request.getParameter("parentBoundaryName");
		
		System.out.println("********************pbnum : "+pbnum);
		System.out.println("********************topLevelBoundaryID: "+topLevelBoundaryID);
		System.out.println("********************bndryTypeHeirarchyLevel: "+bndryTypeHeirarchyLevel);
		System.out.println("********************targetBoundaryNum: "+targetBoundaryNum);
		System.out.println("********************targetBoundaryName "+targetBoundaryName);
		
		
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
					

			<tr>
				<td class="eGovTblContentSubHd" vAlign="center" align="middle" width="100%" colSpan="2" height="56">
				<p align="center">
					<b>View the properties of <%=targetBoundaryName%></b>
				</p>
				</td>
			</tr>
			
			<tr>
			<td class="eGovTblContent" width="50%" height="76">
			<p align="center">
				<b>Boundary Name </b><font class="ErrorText">*</font>
			</p>
			
			<td class="eGovTblContent" align="center" width="50%">	
			<input type="text" name="name" value="<%=targetBoundaryName%>" readonly>
			</td>
			</tr>
			
			<tr>
				<td class="eGovTblContent" width="50%" height="76">
				<p align="center">
					<b>Boundary Number </b><font class="ErrorText">*</font>
				</p>

				<td class="eGovTblContent" align="center" width="50%">	
				<input type="text" name="boundaryNum" value="<%=targetBoundaryNum%>" readonly>
				</td>
			</tr>
				
			<tr class="eGovTblContentSubHd" colSpan="2" >
			</tr>
			<tr bgColor="#dddddd">
				<td vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
					<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font>
				</td>
			</tr>
			<input type="hidden" name="parentBoundaryNum" value="<%=pbnum%>">
			
			<input type="hidden" name="boundaryNum" value="<%=targetBoundaryNum%>">
			
			<input type="hidden" name="TargetBoundaryNum" value="<%=targetBoundaryNum%>">
			<input type="hidden" name="topLevelBoundaryID" value="<%=topLevelBoundaryID%>">
			<input type="hidden" name="bndryTypeHeirarchyLevel" value="<%=bndryTypeHeirarchyLevel%>">

			
			<tr bgColor="#dddddd">
					<td vAlign="bottom" align="left" width="30%" colSpan="2" height="23">
					<p align="center">
					<%
						System.out.println("********************parentBoundary: "+parentBoundary);
					%>
					<input type=button value="Add Child Boundry" onclick="window.location = '../BndryAdmin/addUpdateDeleteBoundary.jsp?parent=<%=targetBoundaryName%>&parentBndryNum=<%=targetBoundaryNum%>&topLevelBoundaryID=<%=topLevelBoundaryID%>&BndryTypeHeirarchyLevel=<%=bndryTypeHeirarchyLevel%>&operation=create';"/>
					<input type=button value="Edit" onclick="window.location = '../BndryAdmin/addUpdateDeleteBoundary.jsp?topLevelBoundaryID=<%=topLevelBoundaryID%>&BndryTypeHeirarchyLevel=<%=bndryTypeHeirarchyLevel%>&TargetBoundaryNum=<%=targetBoundaryNum%>&TargetBoundaryName=<%=targetBoundaryName%>&parentBoundaryName=<%=parentBoundary%>&operation=edit';"/>
					<input type=button value="Delete" onclick="window.location ='../BndryAdmin/addUpdateDeleteBoundary.jsp?topLevelBoundaryID=<%=topLevelBoundaryID%>&BndryTypeHeirarchyLevel=<%=bndryTypeHeirarchyLevel%>&TargetBoundaryNum=<%=targetBoundaryNum%>&TargetBoundaryName=<%=targetBoundaryName%>&parentBoundaryName=<%=parentBoundary%>&operation=delete';"/>
					</p></td>
			</table>
			</td>
			</tr>
			</table>
		</html:form>
		<%@ include file="/egovfooter.jsp" %>
	</body>
	
</html>