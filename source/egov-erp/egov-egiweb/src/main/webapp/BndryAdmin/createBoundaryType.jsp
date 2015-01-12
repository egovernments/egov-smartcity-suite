<%@page import="java.util.List"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% 
List hierarchyTypeList=(List) request.getAttribute("hierarchyTypeList");
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Create Boundary Type</title>
		<script>
			
		function validation() {
			if (document.getElementById('heirarchyType').options[document.getElementById('heirarchyType').selectedIndex].value == "0") {
				alert("Please select the Hierarchy Type!");
				document.getElementById('heirarchyType').focus();
				return false;
			}
			if (document.getElementById('name').value == "") {
				alert("Please enter the Boundary Type!");
				document.getElementById('name').focus();
				return false;
			}
			document.forms[0].action = "<c:url value='/BoundryType.do?operation=create'/>";
			document.forms[0].submit();
			return true;
		}

		</script>
	</head>

	<body>
		<html:form action="/BoundryType"> 
			<table align="center" width="500">
				<tr>
					<td class="tableheader" align="center" height="23">
						Create Boundary Type
					</td>
				</tr>
				<tr>
					<td colspan="4">
						&nbsp;
					</td>
				</tr>
			</table>

			<table align="center" width="300">
				<%
					if (hierarchyTypeList != null && hierarchyTypeList.size() != 0) {
				%>
				<tr>
					<td class="labelcell" height="23">
						Hierarchy Type
						<font class="ErrorText">*</font>
					</td>
					<td class="labelcell" align="left">
						<html:select styleId="heirarchyType" property="heirarchyType">
							<html:option value="0">Choose</html:option>
							<c:forEach var="hierarchyTypeObj" items="<%=hierarchyTypeList%>">
								<html:option value="${hierarchyTypeObj.id}">${hierarchyTypeObj.name}</html:option>
							</c:forEach>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="labelcell" height="23">
						Boundary Type
						<font class="ErrorText">*</font>
					</td>
					<td class="labelcell" align="left" >
						<html:text property="name" styleId="name" styleClass="ControlText" />
						<input type="hidden" name="parentName" id="parentName" value="null" />
					</td>
				</tr>
				<tr>
					<td class="labelcell" height="23">
						Boundary Type Local
					</td>
					<td class="labelcell" align="left">
						<html:text property="bndryTypeLocal" styleId="bndryTypeLocal" styleClass="ControlText" />
					</td>
				</tr>
			</table>
			<br/>
			<table align="center" width="500">
				<tr>
					<td align="center" class="button" colspan="2">
						<input type="button"  value="Create"	 onclick="return validation();" />
					</td>
				</tr>
			</table>
			<%
				} else {
				%>
			<br/>
			<table align="center" width="500">
				<tr>
					<td align="center" colspan="2">
						For all the Hierarchy Types, the Root Boundary Type already exists
					</td>
				</tr>
			</table>
				<%
					}
				%>

		</html:form>
	</body>
</html>