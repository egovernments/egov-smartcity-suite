<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
	The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
	var popupWindow = null;
	function openReassignWindow() {
		var appId;
		if ($("#registrationId").val()) {
			appId = $("#registrationId").val();
		} else if ($("#reIssueId").val()) {
			appId = $("#reIssueId").val();
		}

		var applicationtype = $("#stateType").val();

		popupWindow = window.open('/mrs/reassignmrs/'
				+appId + "/" + applicationtype, '_blank',
				'width=650, height=500, scrollbars=yes', false);
		jQuery('.loader-class').modal('show', {
			backdrop : 'static'
		});
	}

	function closeAllWindows() {
		popupWindow.close();
		window.opener.inboxloadmethod();
		self.close();
	}

	function closeChildWindow() {
		jQuery('.loader-class').modal('hide');
		popupWindow.close();
	}
	function validateWorkFlowApprover(name) {
		$("#workFlowAction").val(name);
		var rejectbutton = name;

		console.log('rejectbutton=' + rejectbutton);
		console.log('workFlorAction= ' + $("#workFlowAction").val());

		if (rejectbutton != null) {
			if (rejectbutton == 'Approve' || rejectbutton == 'Cancel ReIssue'
					|| rejectbutton == 'Cancel Registration') {
				$('#approvalDepartment').removeAttr('required');
				$('#approvalDesignation').removeAttr('required');
				$('#approvalPosition').removeAttr('required');
			} else if (rejectbutton == 'Forward') {
				$('#approvalDepartment').attr('required', 'required');
				$('#approvalDesignation').attr('required', 'required');
				$('#approvalPosition').attr('required', 'required');
				$('#approvalComent').removeAttr('required');

				if (!validateChecklists()) {
					return false;
				}
			}

			if (rejectbutton == 'Print Rejection Certificate') {
				$('#form-registration').prop(
						'action',
						'../certificate/rejection?registrationId='
								+ $('#registrationId').val());
			}

			if (rejectbutton == 'Reject') {
				$('#approvalComent').attr('required', 'required');
			} else if (rejectbutton == 'Approve') {
				$('#approvalComent').removeAttr('required');
			}
		}

		document.forms[0].submit;
		return true;
	}
</script>

<div class="buttonbottom" align="center">
	<table>
		<tr>
			<td><c:if
					test="${marriageRegistration.currentState.value == 'NEW' or reIssue.currentState.value == 'NEW'}">
					<c:if test="${isReassignEnabled}">
						<input type="hidden" name="stateType" id="stateType"
							value="${stateType}" />
						<input type="hidden" id="registrationId"
							value="${marriageRegistration.id}" />
						<input type="hidden" id="reIssueId"
							value="${reIssue.id}" />

						<input type="button" value="Reassign" class="btn btn-primary"
							id="Reassign" name="Reassign"
							onclick="return openReassignWindow();" />
					</c:if>
				</c:if> <c:forEach items="${validActionList}" var="validButtons">

					<form:button type="submit" id="${validButtons}"
						class="btn btn-primary" value="${validButtons}"
						onclick="return validateWorkFlowApprover('${validButtons}');">
						<c:out value="${validButtons}" />
					</form:button>
				</c:forEach> <input type="button" name="button2" id="button2" value="Close"
				class="btn btn-default" onclick="window.close();" /></td>
		</tr>
	</table>
</div>