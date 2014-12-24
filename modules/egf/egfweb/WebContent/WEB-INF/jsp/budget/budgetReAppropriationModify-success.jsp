<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ page import="org.egov.budget.model.*"%>
<html>  
<head>  
    <title> <s:text name="budgetReAppropriation.modify"/></title>
</head>  
<body>  
	<div class="formmainbox"><div class="subheadnew"><s:text name="budgetReAppropriation.modify"/></div></div>
	<s:actionmessage theme="simple"/>
	<s:actionerror/>  
	<s:fielderror />  
	<div class="buttonbottom">
		<input type="button" value="Close" onclick="javascript: self.close()" class="button"/>
	</div>
</body>
</html>