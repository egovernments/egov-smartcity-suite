<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~      accountability and the service delivery of the government  organizations.
  ~
  ~       Copyright (C) 2016  eGovernments Foundation
  ~
  ~       The updated version of eGov suite of products as by eGovernments Foundation
  ~       is available at http://www.egovernments.org
  ~
  ~       This program is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       any later version.
  ~
  ~       This program is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU General Public License for more details.
  ~
  ~       You should have received a copy of the GNU General Public License
  ~       along with this program. If not, see http://www.gnu.org/licenses/ or
  ~       http://www.gnu.org/licenses/gpl.html .
  ~
  ~       In addition to the terms of the GPL license to be adhered to in using this
  ~       program, the following additional terms are to be complied with:
  ~
  ~           1) All versions of this program, verbatim or modified must carry this
  ~              Legal Notice.
  ~
  ~           2) Any misrepresentation of the origin of the material is prohibited. It
  ~              is required that all modified versions of this material be marked in
  ~              reasonable ways as different from the original version.
  ~
  ~           3) This license does not grant any rights to any user of the program
  ~              with regards to rights under trademark law for use of the trade names
  ~              or trademarks of eGovernments Foundation.
  ~
  ~     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title><s:text name='lbl.amalgamation.title' /></title>
<link
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"
	rel="stylesheet" type="text/css" />
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/javascript/amalgamation.js?rnd=${app_release_no}' context='/ptis'/>"></script>
<script
	src="<cdn:url value='/resources/javascript/helper.js' context='/ptis'/>"></script>
<script type="text/javascript">
function onSubmit() {
	var actionName = jQuery('#workFlowAction').val();
	var nextAction = '<s:property value="%{model.state.nextAction}"/>'; 
	var action = null;
	var userDesg = '<s:property value="%{userDesgn}"/>';
	var state = '<s:property value="%{model.state.value}"/>';
	if (actionName == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_STEP_FORWARD}"/>') {
		if ((nextAction != null && nextAction == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_ASSISTANT_APPROVAL_PENDING}"/>')
			|| (nextAction != null && nextAction == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING}"/>')
			|| state == 'Alter:Rejected') {
			jQuery('.amalgamation-form').find('input').removeAttr('readonly');
			jQuery('.amalgamation-form').find('select').removeAttr('disabled');
			enableOccupancyDate();
			action = 'amalgamation-forwardModify.action';
		} else {
			action = 'amalgamation-forwardView.action';
		}
	} else if (actionName == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_STEP_APPROVE}"/>') {
		action = 'amalgamation-approve.action';
	} else if (actionName == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_STEP_REJECT}"/>') {
		action = 'amalgamation-reject.action';
	} else if (actionName == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_STEP_NOTICE_GENERATE}"/>'
			|| actionName == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_STEP_SIGN}"/>'){
		var noticeType = '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@NOTICE_TYPE_SPECIAL_NOTICE}"/>';
		action = '../notice/propertyTaxNotice-generateNotice.action?basicPropId=<s:property value='%{basicProp.id}'/>&noticeType='+noticeType+'&noticeMode=modify&actionType='+actionName;
	} else if (actionName == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_STEP_PREVIEW}"/>') {
		var params = [
   			'height='+screen.height,
   		    'width='+screen.width,
   		    'fullscreen=yes' 
   		].join(',');
		var noticeType = '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@NOTICE_TYPE_SPECIAL_NOTICE}"/>';
		window.open("../notice/propertyTaxNotice-generateNotice.action?basicPropId=<s:property value='%{basicProp.id}'/>&noticeType="+noticeType+"&noticeMode=modify&actionType="+actionName, 'NoticeWindow', params);
		return false;
	}
	document.forms[0].action = action; 
	document.forms[0].submit;
	return true;
}
	
</script>
</head>
<body>
	<div align="left" class="errortext">
		<s:actionerror />
	</div>
	<s:if test="%{hasActionMessages()}">
		<div id="actionMessages" class="messagestyle" align="center">
			<s:actionmessage theme="simple" />
		</div>
		<div class="blankspace">&nbsp;</div>
	</s:if>
	<div class="errorstyle" id="property_error_area" style="display: none;"></div>
	<s:form name="AmalgamationForm" action="amalgamation"
		enctype="multipart/form-data" method="post" theme="simple"
		validate="true">
		<s:push value="model">
			<s:token />
			<s:hidden id="mode" name="mode" value="%{mode}" />
			<s:hidden name="modifyRsn" value="%{modifyRsn}" />
			<s:hidden name="modelId" id="modelId" value="%{modelId}" />
			<s:hidden id="indexNumber" name="indexNumber" value="%{indexNumber}" />

				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary amalgamation-form"
							data-collapsed="0">
							<s:if
								test="%{(model.state.nextAction!=null && 
						@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING.equalsIgnoreCase(model.state.nextAction)) ||
						@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_ASSISTANT_APPROVAL_PENDING.equalsIgnoreCase(model.state.nextAction)}">
								<tr>
									<%@ include file="amalgamationForm.jsp"%>
								</tr>
							</s:if>
							<s:elseif
								test="%{model.state.nextAction.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_COMMISSIONER_APPROVAL_PENDING) ||
					        model.state.value.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_COMMISSIONER_APPROVED) ||
							userDesignationList.toUpperCase().contains(@org.egov.ptis.constants.PropertyTaxConstants@REVENUE_OFFICER_DESGN.toUpperCase()) ||
							userDesignationList.toUpperCase().contains(@org.egov.ptis.constants.PropertyTaxConstants@TAX_COLLECTOR_DESGN.toUpperCase()) ||
							userDesignationList.toUpperCase().contains(@org.egov.ptis.constants.PropertyTaxConstants@BILL_COLLECTOR_DESGN.toUpperCase()) ||
							((userDesignationList.toUpperCase().contains(@org.egov.ptis.constants.PropertyTaxConstants@JUNIOR_ASSISTANT.toUpperCase()) || 
							userDesignationList.toUpperCase().contains(@org.egov.ptis.constants.PropertyTaxConstants@SENIOR_ASSISTANT.toUpperCase()))
							&& model.state.value.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_DIGITALLY_SIGNED))}">
								<tr>
									<%@ include file="amalgamationView.jsp"%>
									<%@ include file="../common/DocumentUploadView_new.jsp"%>
								</tr>
							</s:elseif>
							<s:if test="%{state != null}">
								<tr>
									<%@ include file="../common/workflowHistoryView_new.jsp"%>
								<tr>
							</s:if>
							<s:if
								test="%{(currentDesignation != null && !@org.egov.ptis.constants.PropertyTaxConstants@COMMISSIONER_DESGN.equalsIgnoreCase(currentDesignation.toUpperCase())) ||
					   model.state.value.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_REJECTED) ||
					   model.state.value.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NEW)}">
								<tr>
									<%@ include file="../workflow/commonWorkflowMatrix.jsp"%>
								</tr>
							</s:if>
							<s:if
								test="%{currentDesignation != null && currentDesignation.toUpperCase().equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@COMMISSIONER_DESGN)}">
								<s:if test="%{!endorsementNotices.isEmpty()}">
									<jsp:include page="../workflow/endorsement_history.jsp"/>
								</s:if>
								<div id="workflowCommentsDiv" align="center">
									<s:hidden id="currentDesignation" name="currentDesignation"
										value="%{currentDesignation}" />
									<table width="100%">
										<tr>
											<td width="10%" class="bluebox">&nbsp;</td>
											<td width="20%" class="bluebox">&nbsp;</td>
											<td class="bluebox" width="13%">Remarks:</td>
											<td class="bluebox"><textarea id="approverComments" class="form-control"
													name="approverComments" rows="2" cols="35"></textarea></td>
											<td class="bluebox">&nbsp;</td>
											<td width="10%" class="bluebox">&nbsp;</td>
											<td class="bluebox">&nbsp;</td>
										</tr>
									</table>
								</div>
							</s:if>

							<div class="panel-body custom-form">
								<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
							</div>
						</div>
						<!-- Amalgamation form ends -->
					</div>
				</div>

		</s:push>
	</s:form>
</body>
</html>
