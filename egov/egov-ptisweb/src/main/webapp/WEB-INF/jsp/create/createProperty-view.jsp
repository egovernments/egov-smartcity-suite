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
<%@ page import="org.egov.ptis.constants.PropertyTaxConstants"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title><s:text name='NewProp.title' /></title>
<sx:head />
<!-- <script type="text/javascript" src="/ptis/javascript/unitRentAgreement.js"></script> -->

<script type="text/javascript">
		jQuery.noConflict();
		jQuery("#loadingMask").remove();
	  function loadOnStartUp() {
		//enableFieldsForPropType();
		//toggleFloorDetails();
   		//setCorrCheckBox();
   		var propType = '<s:property value="%{propertyDetail.propertyTypeMaster.type}"/>';
		enableFieldsForPropTypeView(propType);
		var appurtenantLandChecked = '<s:property value="%{propertyDetail.appurtenantLandChecked}"/>';
		if(appurtenantLandChecked == null) {
			jQuery('#appurtenantRow').hide();
			}
		var buildingPlanDetailsChecked = '<s:property value="%{propertyDetail.buildingPlanDetailsChecked}"/>';
		alert("buildingPlanDetailsChecked"+buildingPlanDetailsChecked)
		if(buildingPlanDetailsChecked == null) {
			alert("hide buildingPlanDetailsChecked");
			jQuery('tr.bpddetails').hide();
			}
		var structure = '<s:property value="%{propertyDetail.structure}"/>';
		alert(structure);
		if(structure == 'false') {
			alert("hide site owner")
			jQuery('td.siteowner').hide();
			}
   		
	}
 function setCorrCheckBox(){
    
     <s:if test="%{isAddressCheck()}">
			document.getElementById("chkIsCorrIsDiff").checked=true;
	</s:if>
   }

 function onSubmit(action,obj) {
	 document.getElementById('workflowBean.actionName').value = obj.id;
		document.forms[0].action = action;
		document.forms[0].submit;
	   return true;
	}
	
 function onSubmit() {
	 var department = jQuery('#approverDepartment').val();
	 var designation = jQuery('#approverDesignation').val();
	 var approver = jQuery('#approverPositionId').val();
	 if(approverPositionId == -1) {
		 alert("Please select approver details");
		 return false;
		 }
	 	 document.forms[0].action = 'createProperty-forward.action';
	     document.forms[0].submit;
	 	   return true;
	 	}  
	 	
 	function generateNotice6(){
	   	document.CreatePropertyForm.action="../notice/propertyTaxNotice-generateNotice.action?basicPropId=<s:property value='%{basicProp.id}'/>&noticeType=Notice6&noticeMode=create";
		document.CreatePropertyForm.submit();
	}
	
  	function loadDesignationFromMatrix() {
  		var e = dom.get('approverDepartment');
  		var dept = e.options[e.selectedIndex].text;
  			var currentState = dom.get('currentState').value;
  			var amountRule="";
  		var pendingAction=document.getElementById('pendingActions').value;
  		loadDesignationByDeptAndType('PropertyImpl',dept,currentState,amountRule,"",pendingAction); 
  	}
  	function populateApprover() {
  		getUsersByDesignationAndDept();
  	}  
</script>
</head>

<body onload="loadOnStartUp();">
	<div align="left">
		<s:actionerror />
	</div>
	<s:if test="%{hasActionMessages()}">
		<div id="actionMessages" class="messagestyle" align="center">
			<s:actionmessage theme="simple" />
		</div>
		<div class="blankspace">&nbsp;</div>
	</s:if>
	<!-- Area for error display -->
	<div class="errorcss" id="jsValidationErrors" style="display: none;"></div>
	<div class="errorstyle" id="property_error_area" style="display: none;"></div>
	<div class="formmainbox">
		<s:form name="CreatePropertyForm" action="createProperty"
			theme="simple" validate="true">
			<s:token />
			<!-- The mode value is used in floorform.jsp file to stop from remmoving the rent agreement header icon -->
			<s:hidden name="mode" value="view" />
			<s:push value="model">


				<div class="headingbg">
					<s:text name="CreatePropertyHeader" />
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<%@ include file="../create/createPropertyView.jsp"%>
					</tr>
					<tr>
						<%@ include file="../workflow/commonWorkflowMatrix.jsp"%>
					</tr>
					<s:hidden name="modelId" id="modelId" value="%{modelId}" />
					<tr>
						<font size="2"><div align="left" class="mandatory">
								<s:text name="mandtryFlds" />
							</div> </font>
					</tr>

					<tr>
						<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
					</tr>

				</table>
			</s:push>
		</s:form>
	</div>
</body>
</html>
