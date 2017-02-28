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
	src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/javascript/amalgamation.js?rnd=${app_release_no}' context='/ptis'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/javascript/helper.js' context='/ptis'/>"></script>

<script type="text/javascript">
	function onSubmit() {
		var actionName = jQuery('#workFlowAction').val();
		var action;
		if (actionName == 'Forward') {
			action = 'amalgamation-forwardModify.action';
		} else if (actionName == 'Approve') {
			action = 'amalgamation-approve.action';
		} else if (actionName == 'Reject') {
			action = 'amalgamation-reject.action';
		}
		jQuery('.amalgamation-form').find('input').removeAttr('readonly');
		jQuery('.amalgamation-form').find('select').removeAttr('disabled');
		enableOccupancyDate();
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
	<div class="errorcss" id="jsValidationErrors" style="display: none;"></div>
	<s:form name="AmalgamationForm" action="amalgamation"
		enctype="multipart/form-data" method="post" theme="simple"
		validate="true">
		<s:push value="model">
			<s:token />
			<s:hidden id="mode" name="mode" value="%{mode}" />
			<s:hidden name="modelId" id="modelId" value="%{modelId}" />
			<s:hidden id="indexNumber" name="indexNumber" value="%{indexNumber}" />
			<s:hidden id="modifyRsn" name="modifyRsn" value="%{modifyRsn}" />
			<div class="page-container" id="page-container">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary amalgamation-form"
							data-collapsed="0">
							<%@ include file="amalgamationForm.jsp"%>
							<div class="panel-body custom-form">
								<s:if test="%{propertyByEmployee == true}">
									<%@ include file="../workflow/common-workflowmatrix-new.jsp"%>
									<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
								</s:if>
								<s:else>
									<tr>
										<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
									</tr>
								</s:else>
							</div>
						</div>
						<!-- Amalgamation-form -->
					</div>

				</div>
			</div>
		</s:push>
	</s:form>
</body>

</html>
