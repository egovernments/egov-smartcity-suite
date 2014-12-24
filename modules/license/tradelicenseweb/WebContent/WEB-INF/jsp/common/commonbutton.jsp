<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<table align="center">
	<tr class="buttonbottom" id="buttondiv" style="align: middle">
		<td>
			<s:submit type="submit" cssClass="buttonsubmit" value="Approve" id="Approve" method="create" onclick="return validateForm(this);" />
		</td>
		<td>
			<s:submit type="submit" cssClass="buttonsubmit" value="Forward" id="Forward" method="create" onclick="return validateForm(this);" />
		</td>
		<td>
			<input type="button" value="Close" id="closeButton" onclick="javascript:window.close()" class="button" />
		</td>
	</tr>
</table>
