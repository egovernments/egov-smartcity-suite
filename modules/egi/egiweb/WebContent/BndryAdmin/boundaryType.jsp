<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryTypeServiceImpl"%>
<%@page import="org.egov.infstr.utils.*,org.egov.lib.admbndry.ejb.api.*,org.egov.lib.admbndry.*"%>
<%@ include file="/includes/taglibs.jsp"%>

<%
	String bndryId = request.getParameter("bndryId");
	BoundaryTypeService	btm = new BoundaryTypeServiceImpl();
	BoundaryType bt = btm.getBoundaryType(new Integer(bndryId));
	String type = request.getParameter("type");
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Boundary Type</title>
		<script>
		
		function parentChildRelation(obj) {
			var parentName = "<%=bt.getName()%>";
			if (parentName == obj.value) {
				alert("Child Boundary should not be same as Parent Boundary");
				document.getElementById('name').value = "";
				document.getElementById('name').focus();
				return false;
			}
		}
		var a = "";
		function checkWithChildAndParentBoudary(heirarchyType, boundaryName, boudaryId) {
			var type = "checkWithChildAndParentBoudary";
			var url = "../commons/Process.jsp?type=" + type + "&heirarchyType=" + heirarchyType + "&boundaryName=" + boundaryName + "&boudaryId=" + boudaryId + " ";
			var req2 = initiateRequest();
			req2.open("GET", url, false);
			req2.send(null);
			if (req2.status == 200) {
				var result = req2.responseText;
				result = result.split("/");
				if (result[0] != null && result[0] != "") {
					a = result[0];
				}
			}
			return a;
		}
		function validate(arg) {
			if (arg == "modify") {
				var boudaryId = "<%=bndryId%>";
				var heirarchyType = "<%=bt.getHeirarchyType().getId()%>";
				var bndryname = document.getElementById('name').value;
				var checking = checkWithChildAndParentBoudary(heirarchyType, bndryname, boudaryId);
				if (checking == "true") {
					alert("This Boundary Type is already exists for this Hierarchy Type");
					return false;
				}
				if (validateBoundryTypeForm(boundryTypeForm) == false) {
					return false;
				}
				document.forms[0].action = "<c:url value='/BoundryType.do?operation=edit'/>";
				document.forms[0].submit();
			}
			if (arg == "addchild") {
				if (validateBoundryTypeForm(boundryTypeForm) == false) {
					return false;
				}
				document.forms[0].action = "<c:url value='/BoundryType.do?operation=create'/>";
				document.forms[0].submit();
			}
			if (arg == "delete") {
				document.forms[0].action = "<c:url value='/BoundryType.do?operation=delete'/>";
				document.forms[0].submit();
			}
		}
		</script>
		<html:javascript formName="boundryTypeForm" />
	</head>
	<body>
		<html:form action="/BoundryType" >
			<table align="center" width="500">
				<tr>
					<td class="tableheader" align="center" height="23">
						Boundary Type
					</td>
				</tr>
				<tr>
					<td colspan="4">
						&nbsp;
					</td>
				</tr>
			</table>

				<%
					if ("view".equals(type)) {
				%>
				<table align="center" width="300">
				<tr>
					<td class="labelcell" height="23">
						Hierarchy Type
						<input type="hidden" name="heirarchyType" id="heirarchyType"
							value="<%=bt.getHeirarchyType().getId()%>" />
					</td>
					<td class="labelcell" align="left" height="23">
						<input type="text" name="hierarchyTypeName" id="hierarchyTypeName"
							value="<%=bt.getHeirarchyType().getName()%>" class="ControlText"
							readonly />
					</td>
				</tr>
				<tr>
					<td class="labelcell" height="23" class="labelcell">
						Boundary Type
					</td>
					<td class="labelcell" align="left" height="23">
						<html:text property="name" styleId="name"
							value="<%=bt.getName()%>" styleClass="ControlText"
							readonly="true" />
						<input type="hidden" name="parentName" value="null" />
					</td>
				</tr>
				<tr>
					<td class="labelcell" height="23">
						Boundary Type Local
					</td>
					<td class="labelcell" align="left" height="23">
						<html:text property="bndryTypeLocal"
							value="<%=bt.getBndryTypeLocal()%>" styleClass="ControlText"
							readonly="true" />
					</td>
				</tr>
				<tr>
					<td colspan="4">
						&nbsp;
					</td>
				</tr>
				</table>
				<table width="500"> 
					<tr>
						<td class="button2" align="center"  colspan="2" height="23">
						<input  type="button" value="Back" onclick="window.location = '<c:url value='/BndryAdmin/viewBndryType.jsp'/>';" />
					</td>
					</tr>
				</table>
				<%
					} else if ("modify".equals(type)) {
				%>
				<table align="center" width="300">
				<tr>
					<td class="labelcell" height="23">
						Hierarchy Type
						<input type="hidden" name="heirarchyType" id="heirarchyType"
							value="<%=bt.getHeirarchyType().getId()%>" />
					</td>
					<td class="labelcell" align="left" height="23">
						<input type="text" name="hierarchyTypeName" id="hierarchyTypeName"
							value="<%=bt.getHeirarchyType().getName()%>" class="ControlText"
							readonly />
					</td>
				</tr>
				<tr>
					<td class="labelcell" height="23">
						Boundary Type
						<font class="ErrorText">*</font>
					</td>
					<td class="labelcell" align="left" height="23">
						<input type="text" name="name" id="name" value="<%=bt.getName()%>" />
						<input type="hidden" name="parentName" id="parentName"
							value="<%=bt.getName()%>" />
				</tr>
				<tr>
					<td class="labelcell" height="23">
						Boundary Type Local
					</td>
					<td class="labelcell" align="left" height="23">
						<html:text property="bndryTypeLocal"
							value="<%=bt.getBndryTypeLocal()%>" styleClass="ControlText" />
					</td>
				</tr>
				<tr>
					<td colspan="4">
						&nbsp;
					</td>
				</tr>
				</table>
				<table width="500">
					<tr>
						<td class="button2" align="center" colspan="2" height="23">
							<input  type="button" value="Modify" onclick="validate('modify')" />
						</td>
					</tr>
				</table>
				<%
					} else if ("addchild".equals(type)) {
				%>
				<table align="center" width="300">
				<tr>
					<td class="labelcell" height="23">
						Hierarchy Type
						<input type="hidden" name="heirarchyType" id="heirarchyType"
							value="<%=bt.getHeirarchyType().getId()%>" />
					</td>
					<td class="labelcell" align="left" height="23">
						<input type="text" name="hierarchyTypeName" id="hierarchyTypeName"
							value="<%=bt.getHeirarchyType().getName()%>" class="ControlText"
							readonly />
					</td>
				</tr>
				<tr>
					<td class="labelcell" height="23">
						Parent Boundary Type
					</td>
					<td class="labelcell" align="left" height="23">
						<input type="text" value="<%=bt.getName()%>" class="ControlText"
							readonly="readonly" />
					</td>
				</tr>
				<tr>
					<td class="labelcell" height="23">
						Boundary Type
						<font class="ErrorText">*</font>
					</td>
					<td class="labelcell" align="left" height="23">
						<input type="text" name="name" id="name"
							onchange="return parentChildRelation(this);" />
						<input type="hidden" name="parentName" id="parentName"
							value="<%=bt.getName()%>" />
					</td>
				</tr>
				<tr>
					<td class="labelcell" height="23">
						Boundary Type Local
					</td>
					<td class="labelcell" align="left" height="23">
						<html:text property="bndryTypeLocal" styleClass="ControlText" />
					</td>
				</tr>
				<tr>
					<td colspan="4">
						&nbsp;
					</td>
				</tr>
				</table>
				<table width="500">
					<tr>
						<td class="button2" align="center" colspan="2" height="23">
							<input  type="button" value="Add Child" onclick="validate('addchild')" />
						</td>
					</tr>
				</table>
				<%
					} else if ("delete".equals(type)) {
				%>
				<table align="center" width="300">
				<tr>
					<td height="23" colspan="4">
						<font size="2"> Are you sure to delete <%=bt.getName()%> Boundary!!</font>
						<input type="hidden" name="heirarchyType" id="heirarchyType"
							value="<%=bt.getHeirarchyType().getId()%>" />
						<input type="hidden" name="name" value="<%=bt.getName()%>" />
						<input type="hidden" name="parentName" value="<%=bt.getName()%>" />
					</td>
				</tr>
				<tr>
					<td colspan="4">
						&nbsp;
					</td>
				</tr>
				</table>
				<table width="500">
					<tr>
						<td align="center" colspan="2" height="23" class="button2">
							<input  type="button" value="Delete" onclick="validate('delete')" />
						</td>
					</tr>
				</table>
				<%
					}
				%>
		</html:form>
	</body>
</html>