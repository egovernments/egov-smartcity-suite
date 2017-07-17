<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>
	<s:if test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn)}">
		<s:text name='ModProp.title' />
	</s:if>
	<s:elseif test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)}">
		<s:text name='BifurProp.title' />
	</s:elseif>
</title>

<link href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" rel="stylesheet" type="text/css" />
<script src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>

<script type="text/javascript">
	jQuery.noConflict();
	jQuery("#loadingMask").remove();
	jQuery(function($) {
		try {
			jQuery(".datepicker").datepicker({
				format : "dd/mm/yyyy",
				autoclose:true
			});
		} catch (e) {
			console.warn("No Date Picker " + e);
		}

	});

	function onSubmit() {
		var actionName = document.getElementById('workFlowAction').value;
		var action = null;
		if (actionName == 'Forward') {
			action = 'modifyProperty-forward.action';
		} else if (actionName == 'Approve') {
			action = 'modifyProperty-approve.action';
		} else if (actionName == 'Reject') {
			action = 'modifyProperty-reject.action';
		}
		document.forms[0].action = action;
		document.forms[0].submit;
		return true;
	}

	function loadOnStartUp() {
		enableFieldsForPropType();
		enableOrDisableSiteOwnerDetails(jQuery('input[name="propertyDetail.structure"]'));
		enableOrDisableBPADetails(jQuery('input[name="propertyDetail.buildingPlanDetailsChecked"]'));
		toggleFloorDetails();
	}

</script>
</head>

<body onload="loadOnStartUp();">
	<div align="left" class="errortext">
		<s:actionerror />
	</div>
	<!-- Area for error display -->
	<div class="errorcss" id="jsValidationErrors" style="display:none;"></div>
	<s:form name="ModifyPropertyForm" action="modifyProperty" enctype="multipart/form-data" method="post"
		theme="simple" validate="true">
		<s:push value="model">
			<s:token />
			<div class="formmainbox">
				<div class="headingbg" id="modPropHdr">
					<s:if test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn)}">
						<s:text name='ModProp.title' />
					</s:if>
					<s:elseif test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)}">
						<s:text name='BifurProp.title' />
					</s:elseif>
				</div>
				<%@ include file="modifyPropertyDataEntryForm.jsp"%>
				<s:hidden name="modelId" id="modelId" value="%{modelId}" />
				<s:hidden id="indexNumber" name="indexNumber" value="%{indexNumber}" />
				<s:hidden id="modifyRsn" name="modifyRsn" value="%{modifyRsn}" />
				<s:hidden id="ownerName" name="ownerName" value="%{ownerName}" />
				<s:hidden id="propAddress" name="propAddress" value="%{propAddress}" />
                <s:if test="%{propertyByEmployee == true}">
				<%@ include file="../workflow/commonWorkflowMatrix.jsp"%>
				<div class="buttonbottom" align="center">
					<%@ include file="../workflow/commonWorkflowMatrix-button.jsp" %>
				</div>
				</s:if>
				<s:else>
				<div class="buttonbottom" align="center">
					<%@ include file="../workflow/commonWorkflowMatrix-button.jsp" %>
				</div>
				</s:else>
			</div>
		</s:push>
	</s:form>
</body>
</html>
