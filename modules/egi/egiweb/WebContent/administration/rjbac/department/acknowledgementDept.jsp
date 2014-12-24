<%@ include file="/includes/taglibs.jsp" %>
<%String msg = (String)request.getAttribute("MESSAGE");%>
<html>	
	<head>
		<title>Acknowledgement</title>
		<script>
			function goBack() {
				document.forms[0].action = "<c:url value='/SetupDepartment.do?bool=VIEW'/>";
				document.forms[0].submit();
			}
		</script>
	</head>
	<body bgcolor="#FFFFFF">
		<html:form action="Department?" >
				<table align="center"  class="tableStyle">
					<tr><td colspan=4>&nbsp;</td></tr>
					<tr>
						<td align="center" class="tableheader" align="middle" width="728" height="6"><bean:message key="acknowledgementlabel"/>
							<bean:message key="acknowledgementlabel.ll"/>
						</td>
					</tr>					
					<tr>
						<td width="728" height="10">
					</tr>
					<tr>
						<td align="center" class="tablesubcaption" align="middle" width="728" height="50">
							<p align="center"><b><font="blue"><%= msg %></font></b></p>
						</td>
					</tr>
					<tr>
						<td width="728" height="10"  align="center">
							<input type="button"  value="  Back  " onclick="goBack()">
						</td>
					</tr>
				</table>
			</html:form>
	</body>
</html>