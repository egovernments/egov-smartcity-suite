<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<html>
	<head>
		<title>Trade License Objection Success</title>
	</head>
	<body onload="refreshInbox()">
		<s:form action="editTradeLicense" theme="simple">
			<span class="mandatory"> <s:actionmessage /> </span>
			<br />
			<input type="button" value="Close" onclick="javascript:window.close()" class="button" />
		</s:form>
	</body>
</html>