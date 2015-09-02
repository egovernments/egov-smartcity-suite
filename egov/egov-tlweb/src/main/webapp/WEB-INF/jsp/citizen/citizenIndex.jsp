<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Citizen Index Page</title>
</head>
<body>
	<div class="container">
		<div class="mainhead">Citizen Access</div>
		<table align="center">
			<tbody>
				<br>
				<br>
				<tr>
					<td align="left"><a
						href='${pageContext.request.contextPath}/citizen/search/searchCitizenLicense!newForm.action'
						target="_blank"><img src="../images/searchicon.gif" />  Search Trade License</a></td>
				</tr>
				<tr>
				</tr>
				<tr>
					<td align="left"><a
						href='${pageContext.request.contextPath}/citizen/newapp/newCitizenTradeLicense!newForm.action'
						target="_blank"> <img src="../images/application_edit.png" /> Apply for Trade License</a></td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>
