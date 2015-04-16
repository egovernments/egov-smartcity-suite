
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/egov-authz.tld" prefix="egovAuthz" %>
<egovAuthz:authorize actionName="PermissionsAction">
<fieldset>
	<legend align="left"><b>Assign Access Permissions</b></legend>
	<div id="calender" class="cal"></div>
		<table border="0" cellpadding="4" cellspacing="0" width="100%">
			<tbody>
			<tr class="graybox">
			
				<td colspan="2" align="center"><a href="/egi/acl/accessPermissions.action?objectId=${id}&objectClass=${model.class.canonicalName}">Do You Want to Assign Access Permissions</a></td>
					</tr>
			</tbody>
		</table>
</fieldset>
</egovAuthz:authorize>
 