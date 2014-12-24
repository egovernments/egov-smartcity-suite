<%@ include file="/includes/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="nmc.title"/></title>
<script type="text/javascript"> 
<%@ page import="org.egov.erpcollection.web.constants.CollectionConstants" %>
function onBodyLoad(){

	var form = document.createElement("form");
	form.setAttribute("method", "POST");
	
	<s:iterator value="paymentRequest.requestParameters">
		<s:if test='key.equals("paymentGatewayURL")'>
			form.setAttribute("action", "<s:property value='value' />");			
		</s:if>
		<s:else>
			var hiddenField = document.createElement("input");
			hiddenField.setAttribute("type","hidden");              
			hiddenField.setAttribute("name",  "msg");
			hiddenField.setAttribute("value", "<s:property value='value' />");
			form.appendChild(hiddenField);	
		</s:else>
			
	</s:iterator> 

	document.body.appendChild(form);
	form.submit();

	
}

</script>
</head>
<body onLoad="onBodyLoad();">
</body>
</html>
