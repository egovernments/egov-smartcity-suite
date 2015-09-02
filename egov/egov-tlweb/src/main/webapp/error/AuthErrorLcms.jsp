<%@ include file="/includes/taglibs.jsp" %>
<html> 
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    	<title>Authorization Error Page</title> 
    	<link href="<egov:url path='/css/security.css'/>" rel="stylesheet" type="text/css" />
</head>
<body>
 <table align="center" cellpadding="0" cellspacing="0" class="main"> 
    <tbody>
	<tr>
	<td>
	<img src="../images/secure4.JPG" />
	</td>
	<td>
	<p> <fmt:setBundle basename="lcmsApplicationResources" />
			 <fmt:message key="${AuthRuleErrMsgKey}" /></p>
	</td>
	</tr>
     </tbody> 
    
    </table>

</body>

</html>