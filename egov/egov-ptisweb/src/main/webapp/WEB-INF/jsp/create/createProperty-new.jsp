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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title><s:if test="mode=='create' || mode=='edit'">
		<s:text name='NewProp.title' />
	</s:if></title>
<sx:head />
<script	src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"type="text/javascript"></script>
<link href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" rel="stylesheet" type="text/css" />
<script	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>" type="text/javascript"></script>
<script	src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>" type="text/javascript"></script>
</head>

<body onload="loadOnStartUp();">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="property_error_area">
			<div class="errortext">
				<s:actionerror />
			</div>
		</div>
	</s:if>
 <s:form name="CreatePropertyForm" action="createProperty-create"
		theme="simple" enctype="multipart/form-data">
		<s:push value="model">
			<s:token />
			<s:hidden name="mode" id="mode" value="%{mode}" />
			<s:hidden name="modelId" id="modelId" value="%{modelId}" />
			<div class="formmainbox">
				<div class="headingbg">
					<s:text name="CreatePropertyHeader" />
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<%@  include file="createPropertyForm.jsp"%>
					</tr>
					<s:if test="%{!documentTypes.isEmpty()}">
						<tr>
							<%@ include file="../common/DocumentUploadForm.jsp"%>
						</tr>
					</s:if>
					<s:if test="%{state != null}">
						<tr>
							<%@ include file="../common/workflowHistoryView.jsp"%>
						<tr>					
					</s:if>
					<s:if test="%{propertyByEmployee == true}">
						<tr>
							<%@ include file="../workflow/commonWorkflowMatrix.jsp"%>
						</tr>
						<tr>
							<font size="2"><div align="left" class="mandatory1">
									&nbsp;&nbsp;
									<s:text name="mandtryFlds" />
								</div></font>
						</tr>
						<tr>
							<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
						</tr>
					</s:if>
					<s:else>
						<tr>
							<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
						</tr>
					</s:else>

				</table>
			</div>
		</s:push>
	</s:form>
	<script type="text/javascript">

jQuery.noConflict();
jQuery("#loadingMask").remove();
jQuery(function ($) {
	try { 
		$(".datepicker").datepicker({
			format: "dd/mm/yyyy"
		}); 
		reInitializeDateOnChangeEvent();
		}catch(e) {
		console.warn("No Date Picker "+ e);
	}
});

function reInitializeDateOnChangeEvent(){

	jQuery(".datepicker").on('changeDate', function(ev){
		jQuery(this).datepicker('hide');
	}); 
	
}

function loadOnStartUp() {
	document.getElementById('assessmentRow').style.display="none";
	enableCorresAddr();
	enableAppartnaumtLandDetails();
	makeMandatory();
	document.getElementById("appurtenantRow").style.display = "none";
	enableOrDisableSiteOwnerDetails(jQuery('input[name="propertyDetail.structure"]'));
	enableOrDisableBPADetails(jQuery('input[name="propertyDetail.buildingPlanDetailsChecked"]'));
	var appartunentLand = jQuery('input[name="propertyDetail.appurtenantLandChecked"]');
	if (jQuery(appartunentLand).is(":checked")) {
		enableAppartnaumtLandDetails();
	}
	var category = '<s:property value="%{propertyDetail.categoryType}"/>';
	document.forms[0].propTypeCategoryId.options[document.forms[0].propTypeCategoryId.selectedIndex].value = category;
	toggleFloorDetails();
     var aadhartextboxes = jQuery('.txtaadhar');
     console.log(aadhartextboxes);
     aadhartextboxes.each(function() {
	   	if(jQuery(this).val())
	   	{
	   		  getAadharDetails(this);
	   	}
	 });
     populateBoundaries();
     loadDesignationFromMatrix();
}

function onSubmit() { 
	jQuery('#gender, #guardianRelation').removeAttr('disabled');
	document.forms[0].action = 'createProperty-create.action';
	<s:if test="mode=='edit'">
	document.forms[0].action = 'createProperty-forward.action';
	</s:if>
	document.forms[0].submit;
   return true;
}

</script>
<script src="<c:url value='/resources/global/js/egov/inbox.js' context='/egi'/>"></script>
</body>
</html>
