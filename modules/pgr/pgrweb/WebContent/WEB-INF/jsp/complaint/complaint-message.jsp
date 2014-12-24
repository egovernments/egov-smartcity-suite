<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name="recoveryMsg" /></title>
  </head>
  
  <body onload="refreshInbox()">
 <s:push value="model">
  <div class="formmainbox">
 <s:if test="%{hasActionMessages()}">
			<font  style='color: green ; font-weight:bold '> 
     					<s:actionmessage/>
   				</font>
		</s:if>

	</div>
	
	<div class="buttonbottom" align="center">
	  	<table>
		<tr>
		    	<td><input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/></td>
		  
		</tr>             
		</table></div>   
</s:push>

<script >
 function refreshInbox() {
       if(opener && opener.top.document.getElementById('inboxframe'))
		opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}

</script>
  </body>

</html>