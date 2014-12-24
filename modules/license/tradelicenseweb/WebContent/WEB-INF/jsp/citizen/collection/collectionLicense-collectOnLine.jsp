<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.egov.ptis.property.client.property.coc.*,java.util.*,org.egov.ptis.property.model.FloorIF.*,java.net.URLEncoder"%>

<html>
<head>
    <title>View License Details</title>
 
<script type="text/javascript">
  function loadOnStartup()
  {  
  	var form = document.createElement("form");
		form.setAttribute("method", "post");
		var collStr="";
        collStr="/collection/citizen/onlineReceipt!newform.action";
		form.setAttribute("action", collStr);
		var hiddenField = document.createElement("input");
		hiddenField.setAttribute("type","hidden");              
		hiddenField.setAttribute("name", "collectXML");
		hiddenField.setAttribute("value", "${collectXML}");
		form.appendChild(hiddenField);
		document.body.appendChild(form);
		var  win1 = window.open(collStr,'_self');
		form.submit();
  }
  
  
</script>   
</head>
<body onload = "loadOnStartup();">
</body>
</html>
