<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    
    
    <title>Result Page</title>

	
<script type="text/javascript">
function closeAndRefreshParent()
{
	var target="<%=(request.getAttribute("alertMessage"))%>";

	 if(target!=null && target != "null")
	 {
		alert("<%=request.getAttribute("alertMessage")%>");
		<%	request.setAttribute("alertMessage",null);	%>
	 }
  
	refreshInbox();
	window.close();

}

 function refreshInbox()
    {
    	if(opener.top.document.getElementById('inboxframe')!=null)
    	{    	
    		if(opener.top.document.getElementById('inboxframe').contentWindow.name!=null && opener.top.document.getElementById('inboxframe').contentWindow.name=="inboxframe")
    		{    		
    		opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
    		}
    	
    	}
    }



</script>
  </head>
  
  <body onload="closeAndRefreshParent()">
  
  </body>
</html:html>
