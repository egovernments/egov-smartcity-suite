<%@ include file="/includes/taglibs.jsp"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>View Hierarchy</title>
		<script>
		function goBack() {
			document.forms[0].action = "<c:url value='/SetupHierarchyType.do?bool=VIEW'/>";
			document.forms[0].submit();
		}
		</script>
	</head>

	<body>
		<html:form action="CreateHierarchy?bool=UPDATE">
			<table align="center" width="500">
				<tr>
					<td class="tableheader" align="center"  height="23">
						View Hierarchy
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
					<td class="labelcell" height="23">
						Hierarchy Name
					</td>
					<td class="labelcell" align="left" >
						<html:text property="name" readonly="true" styleClass="ControlText" />
					</td>
				</tr>
				<tr>
					<td class="labelcell" >
						Hierarchy Code
					</td>
					<td class="labelcell" align="left" >
						<html:text property="code" readonly="true" styleClass="ControlText" />
					</td>
				</tr>
				</table>
				<br/>
				<table width="500" align="center">
					<tr>
						<td align="center" class="button2">
							<input type="button"  value="Back" onclick="goBack()"/>
						</td>
					</tr>
				</table>
			
		</html:form>
	</body>
</html>