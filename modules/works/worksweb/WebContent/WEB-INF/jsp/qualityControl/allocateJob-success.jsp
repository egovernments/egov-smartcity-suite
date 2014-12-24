<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Allocate Job Number</title>
<body onload="refreshInbox();">
<script>
function refreshInbox(){
    var x=opener.top.opener;
    if(x==null){
        x=opener.top;
    }
    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}
</script> 

<table align="center">
<tr>
<td align="center">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>
</td>
</tr>
</table>
	
	