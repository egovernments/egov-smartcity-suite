<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<div align="center">
	<br>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="5" width="5%">
				<div class="headingsmallbg">
					<span class="bold"><s:text name='approval.details.title' />
					</span>
				</div>
			</td>
		</tr>
		<tr>
			<td class="bluebox" width="6%">
				&nbsp;
			</td>
			<td class="bluebox" width="10%">
				<s:text name='approver.department' />
				<span class="mandatory">*</span>
			</td>
			<td class="bluebox" width="8%">
				<s:select name="workflowBean.departmentId" id="departmentId"
					list="workflowBean.departmentList" listKey="id"
					listValue="deptName" headerKey="-1" headerValue="----Choose----"
					value="%{workflowBean.departmentId}"
					onchange="populateDesignations()" />
			</td>
			<egov:ajaxdropdown id="designationId" fields="['Text','Value']"
				dropdownId="designationId"
				url="common/ajaxCommon!populateDesignationsByDept.action" />
			<td class="bluebox" width="8%">
				<s:text name='approver.designation' />
				<span class="mandatory">*</span>
			</td>
			<td class="bluebox" width="15%">
				<s:select name="workflowBean.designationId" id="designationId"
					list="workflowBean.designationList" listKey="designationId"
					listValue="designationName" headerKey="-1"
					headerValue="----Choose----" value="%{workflowBean.designationId}"
					onchange="populateUser()" />
			</td>
		</tr>
		<tr>
			<td class="greybox" width="5%">
				&nbsp;
			</td>
			<td class="greybox" width="10%">
				<s:text name='approver.position' />
				<span class="mandatory">*</span>
			</td>
			<td class="greybox" width="15%">
				<s:select id="approverUserId" name="workflowBean.approverUserId"
					list="workflowBean.appoverUserList" headerKey="-1"
					headerValue="----Choose----" listKey="id" listValue="userName"
					value="%{workflowBean.approverUserId}" />
				<egov:ajaxdropdown id="approverUserId" fields="['Text','Value']"
					dropdownId="approverUserId"
					url="common/ajaxCommon!populateUsersByDesignation.action" />
			</td>
			<td class="greybox" colspan="2" width="20%">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="bluebox" width="6%">
				&nbsp;
			</td>
			<td class="bluebox" width="10%">
				<s:text name='approver.comments' />
			</td>
			<td class="bluebox" width="8%">
				<s:textarea name="workflowBean.comments" id="comments" rows="3"
					cols="80" onblur="checkLength(this);" />
			</td>
			<td class="bluebox" width="15%" colspan="2"></td>
		</tr>
		<s:hidden name="workflowBean.actionName" id="workflowBean.actionName" />
	</table>
</div>

<script>
	designationIdFailureHandler = function() {
	}

	function populateDesignations() {
		populatedesignationId( {
			departmentId : document.getElementById("departmentId").value
		})
	}
	function populateUser() {
		populateapproverUserId( {
			designationId : document.getElementById("designationId").value
		})
	}
</script>
