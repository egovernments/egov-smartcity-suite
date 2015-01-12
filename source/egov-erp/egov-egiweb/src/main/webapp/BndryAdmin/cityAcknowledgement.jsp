<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
		<title>Acknowledgement Page</title>

	<script>

function goBack()
{

document.forms("hierarchyForm").action = "/SetupCity.do";
document.forms("hierarchyForm").submit();
}

</script>
	</head>

	<body bgcolor="#FFFFFF">
<html:form action="CreateHierarchy?bool=CREATE" >


				<table align="center"  class="tableStyle">

				<tr><td colspan=4>&nbsp;</td></tr>
				<tr>
					<td align="center" class="tableheader" align="middle" width="728" height="6"><bean:message key="acknowledgementlabel"/>
						<bean:message key="acknowledgementlabel.ll"/>
					</td>
				</tr>
				<%
				   String msg = (String)request.getAttribute("MESSAGE");%>
				<tr>
				<td width="728" height="10">

				<tr>
					<td align="center" class="tablesubcaption" align="middle" width="728" height="50">
						<p align="center"><b><font="blue"><%= msg %></font></b></p>
					</td>


				</tr>
			<tr>

		<td width="728" height="10" align="center"><input type="button" class="button2" value="Back" onclick="goBack()"></td>


		</tr>

			</table>

		</html:form>
	</body>
</html>