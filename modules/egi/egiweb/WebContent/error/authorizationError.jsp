<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html> 
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    	 <link rel="stylesheet" type="text/css" media="all" href="/egi/css/egov.css" />
 	<title>Authorization Error Page</title> 
</head>

<body>
<table class="tableStyle">
<tr>
<td>
<DIV id=main><DIV id=m2><DIV id=m3><div align="center">
 <form>
  <table  class="tableStyle" border=1  width="754" summary>
    <tbody>
	<tr>
		<td class="tableheader" align="middle" width="728" height="30">
			<p align="center"><b><font="blue"><bean:message key="${AuthRuleErrMsgKey}"/></font></b></p>
		</td>
	</tr>
     </tbody> 
     </div></div></div></td></tr> 
    </table>
</form>
</body>

</html>