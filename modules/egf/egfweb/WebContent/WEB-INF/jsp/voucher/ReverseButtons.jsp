<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="org.egov.utils.Constants;" %>

<table border="0" width="30%" align="center"><tr></tr>
			<tr>
				<td>
				<s:hidden name="button" id="button"/>
				
				<s:submit type="submit" cssClass="buttonsubmit" id="Save_View"  name="Save_View"  value="Reverse & View"   onclick="enableAll();document.getElementById('button').value='Reverse_View'; return validateReverse()" method="reverse" />
				</td>
				<td>
				<s:submit type="submit" cssClass="buttonsubmit" id="Save_Close"  name="Save_Close"  value="Reverse & Close"   onclick="enableAll();document.getElementById('button').value='Reverse_Close';return validateReverse()" method="reverse" />
				</td>
				<td>
				<input type="button" id="Close" value="Close" onclick="javascript:window.close()" class="button"/>
				</td>
			</tr>
</table>