<%@page import="org.egov.lib.admbndry.HeirarchyType"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@ include file="/includes/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<title>HIERARCHY</title>
		<link rel="stylesheet" type="text/css" 	href="<c:url value='/css/egov.css' />" />
		<script>
			
		function goToCreate() {
			document.forms[0].action = "<c:url value='/BndryAdmin/createHierarchy.jsp'/>";
			document.forms[0].submit();
		}
		function goToView() {
			if (document.getElementById('hierarchyTypeid').value == "0") {
				alert("Select a Hierarchy Type");
				return false;
			}
			document.forms[0].action = "<c:url value='/SetupHierarchyType.do?bool=VIEW'/>";
			document.forms[0].submit();
		}
		function goToModify() {
			if (document.getElementById('hierarchyTypeid').value == "0") {
				alert("Select a Hierarchy Type");
				return false;
			}
			document.forms[0].action = "<c:url value='/SetupHierarchyType.do?bool=UPDATE'/>";
			document.forms[0].submit();
		}
		function goToDelete() {
			if (document.getElementById('hierarchyTypeid').value == "0") {
				alert("Select a Hierarchy Type");
				return false;
			}
			var result = uniqueCheckingBoolean("${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp", "EG_BOUNDARY_TYPE", "ID_HEIRARCHY_TYPE", "hierarchyTypeid", "no", "no");
			if (result == false) {
				alert("Unable to Delete\n\nThis hierarchy type contains existing boundaries.");
				return false;
			} else {
				document.forms[0].action = "<c:url value='/CreateHierarchy.do?bool=DELETE'/>";
				document.forms[0].submit();
			}
		}		
		</script>
	</head>

	<body>
		<html:form action="CreateHierarchy?bool=CREATE">
		<table align="center" width="500">
				<tr>
					<td class="tableheader" align="center" width="500" height="23">
						Hierarchy Types
					</td>
				</tr>
				<tr>
					<td colspan=4>
						&nbsp;
					</td>
				</tr>
			</table>

			<table align="center" width="250" >
				<tr>
					<td class="labelcell" width="25%" height="23">
						HierarchyType
						<font class="ErrorText"></font>
					</td>
					<td class="labelcell" align="left">
						<html:select styleId="hierarchyTypeid" property="hierarchyTypeid">
							<html:option value="0">--Choose--</html:option>
							<%
								Set hierarchySet = (HashSet) request.getSession().getAttribute("hierarchySet");
								for (Iterator itr = hierarchySet.iterator(); itr.hasNext();) {
									HeirarchyType heirarchyType = (HeirarchyType) itr.next();
									String hierarchyTypeid = heirarchyType.getId().toString();
							%>
							<html:option value="<%=hierarchyTypeid%>"><%=heirarchyType.getName()%></html:option>
							<%
								}
							%>
						</html:select>
					</td>
				</tr>
				</table>
				<br/>
				<table align="center" width="500"> 
					<tr>
						<td align="right" class="button2" width="200px">
							<input type="button" value="Create" onclick="goToCreate()"/>&nbsp;
						</td>
						<td align="center" class="button2" width="50px">
							<input type="button" value="View"	onclick="goToView()"/>&nbsp;
						</td>
						<td align="center" class="button2" width="50px">
							<input type="button" value="Modify" onclick="goToModify()"/>
						</td>
						<td align="left" class="button2" width="200">
							&nbsp;&nbsp;<input type="button" value="Delete" onclick="goToDelete()"/>
						</td>
					</tr>
				</table>
			</html:form>
	</body>
</html>