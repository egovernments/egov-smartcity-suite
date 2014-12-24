<%@ include file="/includes/taglibs.jsp" %>

<%
String msg = (String)request.getAttribute("MESSAGE");
%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
		<title>Acknowledgement</title>
		<LINK rel="stylesheet" type="text/css" href="<c:url value="/css/egov.css" />">
	</head>
	<body bgcolor="#FFFFFF">

		<form name="ackForm">
			<table align="center" >
				<tr>
					<td colspan=4>&nbsp;</td>
				</tr>
				<tr>
					<td class="tableheader" align="center" width="728" height="23"><bean:message key="acknowledgementlabel"/>
						<bean:message key="acknowledgementlabel.ll"/>
					</td>
				</tr>
				<tr>
					<td  align="center" width="728" style="font-size:12px" height="50px">
						<p align="center"><b><font color="navy"><%= msg %></font></b></p>
					</td>
				</tr>
				<tr height="50px">
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="button2" align="center" width="728" height="23">
						<input type="button" value ="Close" onclick="javascript:window.close();">
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>