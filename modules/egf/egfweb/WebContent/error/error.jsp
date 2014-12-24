<html>
	<head>
		<title>Error</title>
		<LINK rel="stylesheet" type="text/css" href="../css/egov.css">
	</head>
<body bgcolor="#FFFFFF">


<center>
<%@ include file="/egovheader.jsp" %>
 <html:form>
  <table border=1 class="eGovTblMain" width="754" summary>
    <tbody>
      <tr>
        <td class="eGovTblContentSubHd" align="middle" height="27" width="772">
          <p align="center">An error has occurred! Please try again or contact the administrator if the problem persists<bean:message key="error.generic"/>
          <bean:message key="error.generic.ll"/></p>
        </td>
      </tr>
	</tbody>
  </table>
</center>
</html:form>
 <%@ include file = "/egovfooter.jsp" %>
</body>

</html>