
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title><s:text name="page.title.asset.capitalisation.details" /></title>
<body>
<script>
	
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
<tr>
<td align="center">
<input type="button" class="buttonfinal" value="CLOSE"
		id="closeButton" name="button"
		onclick="window.close();" />
</td></tr>
</table>
		
				
</body>
</html>
