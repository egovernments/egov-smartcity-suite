<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Acknowledgement Page</title>
		<script>
		function goBack() {
			document.forms[0].action = "<c:url value='/BndryAdmin/viewBndryType.jsp'/>";
			document.forms[0].submit();
		}

		</script>
	</head>
	<body>
		<html:form action="/BoundryType">
			<table align="center" width="500">
				<tr>
					<td colspan="4">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td align="center" class="tableHeader">
						<bean:message key="acknowledgementlabel" />
						<bean:message key="acknowledgementlabel.ll" />
					</td>
				</tr>
				<%
					String msg = (String) session.getAttribute("MESSAGE");
					session.removeAttribute("MESSAGE");
				%>
				<tr>
					<td align="center" width="500" >
						<p align="center">
							<b><font color="blue"><%=msg%></font> </b>
						</p>
					</td>
				</tr>
				</table>
				<br/>
				<table width="500">
				<tr>
					<td  height="23" align="center" class="button2">
						<input type="button"  value="Back" onclick="goBack()" />
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html>