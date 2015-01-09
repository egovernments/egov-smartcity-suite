<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="s"    uri="/WEB-INF/struts-tags.tld" %>  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>File Download - Error</title>
<link href="/egi/css/docmgmt/documentManager.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="container">		
<div class="mainhead">Download - Error</div>


  
  <table id="box2" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
  <td width="90%"  align="center">An error has occurred. Please try again or contact system administrator if the problem persists.</td>

  </tr>
  <tr>
  <td width="90%" > 
	<s:actionerror/>  
	<s:fielderror />  
</td>
    </tr>

</table>
</div>
<div class="urlcontainer"> eGovernments Foundations &copy; All rights reserved</div>

</body>
</html>
 