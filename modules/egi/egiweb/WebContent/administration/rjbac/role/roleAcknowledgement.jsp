<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<title>Acknowledgement</title>
		<link rel="stylesheet" type="text/css" href="/egi/css/egov.css"/>
		<script>
			function goBack() {
				document.forms[0].action = "/egi/SetupRole.do?bool=VIEW";
				document.forms[0].submit();
			}
		</script>
	</head>
		<body bgcolor="#FFFFFF">
			<html:form action="CreateRole?bool=CREATE" >
				<table align="center"  class="tableStyle">
					<tr>
						<td colspan=4>&nbsp;</td>
					</tr>
					<tr>
						<td align="center" class="tableheader" align="middle" width="728" height="6"><bean:message key="acknowledgementlabel"/>
							<bean:message key="acknowledgementlabel.ll"/>
						</td>
					</tr>				
					<tr>
						<td align="center" class="tablesubcaption" align="middle" width="728" height="50">
							<p align="center"><b><font="blue"><%=request.getAttribute("MESSAGE")%></font></b></p>
						</td>
					</tr>
					<tr>
						<td width="728" calss="button2" height="10" align="center">
							<input type="button"  value="  Back  " onclick="goBack()">
						</td>
					</tr>
				</table>
			</html:form>
		</body>
</html>