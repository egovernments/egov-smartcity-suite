<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Road Cut Application</title>
<body >
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
			<s:if test="%{bpaSuccessMsg != ''}">
				<s:property value="%{bpaSuccessMsg}" escape="false"/>
			</s:if>
			<s:if test="%{bpaSuccessMsg == ''">
				<s:actionmessage theme="simple" />
			</s:if>
			<s:if test="%{emailMsg!=''}">
				<s:property value="%{emailMsg}"/>
			</s:if>
			<br>
			<s:if test="%{smsSuccessMsg!=''}">
				<s:property value="%{smsSuccessMsg}"/>
			</s:if>
		</div>
	</s:if>
</td>
</tr>
<tr><td>&nbsp;</td></tr>
<tr>
<td align="center">
<input type="button" class="buttonfinal" value="CLOSE"
		id="closeButton" name="button"
		onclick="window.close();" />
</td></tr>
</table>
</body>
</html>