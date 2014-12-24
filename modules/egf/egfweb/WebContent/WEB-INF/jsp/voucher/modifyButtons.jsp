<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="org.egov.utils.Constants;" %>
<div class="buttonbottom" >
<table border="0" width="30%" align="center"><tr></tr>
			<tr>
				<td>
				<s:hidden name="button" id="button"/>
				<s:submit type="submit" cssClass="buttonsubmit" name="Save & View"  value="Save & View"   onclick="document.getElementById('button').value='Save_View';return validate()" method="edit" />
				</td>
				<td>
				<s:submit type="submit" cssClass="buttonsubmit" name="Save & Close"  value="Save & Close"   onclick="document.getElementById('button').value='Save_Close';return validate()" method="edit" />
				</td>
				<td>
				<input type="button" id="closeButton" value="Close" onclick="javascript:window.close()" class="button"/>
				</td>
			</tr>
</table>
</div>