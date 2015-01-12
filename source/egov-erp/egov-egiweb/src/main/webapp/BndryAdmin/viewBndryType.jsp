<%@page import="java.util.ArrayList"%>
<%@page import="org.egov.infstr.utils.EgovMasterDataCaching"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	ArrayList hierarchyTypeList = (ArrayList) EgovMasterDataCaching
			.getInstance().get("egi-hierarchyType");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>View Boundary Type</title>
		<script>
		function goTo(arg) {
			if (document.getElementById('heirarchyType').options[document.getElementById('heirarchyType').selectedIndex].value == "0") {
				alert("Please select the Hierarchy Type !");
				document.getElementById('heirarchyType').focus();
				return false;
			}
			if (document.getElementById('name').options[document.getElementById('name').selectedIndex].value == 0) {
				alert("Please select the Boundary Type !");
				document.getElementById('name').focus();
				return false;
			}
			if (arg == "delete") {
				var result = uniqueCheckingBoolean("../commonyui/egov/uniqueCheckAjax.jsp", "EG_BOUNDARY_TYPE", "PARENT", "name", "no", "no");
				if (result == false) {
					alert("Boundary Type can't be deleted, found Child Boundary !");
					return false;
				}
				var result = uniqueCheckingBoolean("../commonyui/egov/uniqueCheckAjax.jsp", "EG_BOUNDARY", "ID_BNDRY_TYPE", "name", "no", "no");
				if (result == false) {
					alert("Boundary Type can't be deleted, its present in Boundary !");
					return false;
				}
			}
			if (arg == "addchild") {
				var result = uniqueCheckingBoolean("${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp", "EG_BOUNDARY_TYPE", "PARENT", "name", "no", "no");
				if (result == false) {
					alert("Child Boundary already exists");
					document.getElementById('name').focus();
					return false;
				}
			}
			var bndryId = document.getElementById('name').value;
			window.location = "<c:url value='/BndryAdmin/boundaryType.jsp?type="+arg+"&bndryId="+bndryId'/>;
		}		
		</script>
	</head>
	<body>
		<html:form action="/BoundryType">
			<table align="center" width="500">
				<tr>
					<td class="tableheader" align="center" height="23">
						View Boundary Type
					</td>
				</tr>
				<tr>
					<td colspan="4">
						&nbsp;
					</td>
				</tr>
			</table>

			<table align="center" width="300">
				<tr>
					<td class="labelcell"  height="23">
						Hierarchy Type <font class="ErrorText">*</font>
					</td>
					<td class="labelcell" align="left" height="23">
						<html:select styleId="heirarchyType" property="heirarchyType" onchange="loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'EG_BOUNDARY_TYPE', 'ID_BNDRY_TYPE', 'NAME', 'ID_HEIRARCHY_TYPE=#1 order by ID_BNDRY_TYPE', 'heirarchyType', 'name');">
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
					<td class="labelcell" align="left" height="23">
						<html:select styleId="name" property="name">
							<html:option value="0">Choose</html:option>
						</html:select>
						<input type="hidden" name="parentName" id="parentName" value="null" />
					</td>
				</tr>
			</table>
			<br/>
			<table width="500">
				<tr>
					<td align="center" height="23" colspan="2" class="button2">
						<input type="button"  value="View" onclick="goTo('view');" />
						<input type="button"  value="Modify" onclick="goTo('modify');" />
						<input type="button"  value="Delete" onclick="goTo('delete');" />
						<input type="button"  value="Add Child Boundary" onclick="goTo('addchild');" />
						<input type="hidden"  name="parentName" 	value="null" />
					</td>
				</tr>			
			</table>
		</html:form>
	</body>
</html>