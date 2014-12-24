<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="org.egov.utils.Constants;" %>
<div class="buttonbottom" >
<table border="0" width="75%" cellspacing="0" align="center"><tr></tr>
			<tr>
				<td>
				<s:hidden name="button" id="button"/>
				<s:submit  cssClass="buttonsubmit" name="Save_New"  id="Save_New"   value="Save & New"  onclick="document.getElementById('button').value='Save_New';return validate();" method="create" />
				</td>
				<td>
				<s:submit  cssClass="buttonsubmit" name="Save_View"  id="Save_View" value="Save & View"   onclick="document.getElementById('button').value='Save_View';return validate();" method="create" />
				</td>
				<td>
				<s:submit type="submit" cssClass="buttonsubmit" id="Save_Close"  name="Save_Close" value="Save & Close"   onclick="document.getElementById('button').value='Save_Close';return validate();" method="create" />
				</td>
				<td>
				<s:submit name="Cancel" type="submit" cssClass="button" id="Cancel"  value="Cancel"  onclick="document.getElementById('button').value='';return true;" method="newform" />
				</td>
				<td>
				<input type="button" id="closeButton" value="Close" onclick="javascript:window.close()" class="button"/>   
				</td>
			</tr>
</table>
</div>