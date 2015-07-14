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
		/* 
		 Assuming this is not require for view --Mani
		enableFieldsForPropType();
		toggleFloorDetails();
   		setCorrCheckBox();
   		<s:if test="%{extra_field4 != 'Yes'}">
		    var btnPVR = document.getElementById("GeneratePrativrutta");
		    if (btnPVR != null) {
		    	btnPVR.disabled = false;
		    }
		</s:if> */
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
	  <div class="errorcss" id="jsValidationErrors" style="display:none;"></div>
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
					<%-- <s:if test="%{isApprPageReq}">
							<tr>
								<%@ include file="../workflow/property-workflow.jsp"%>
							</tr>
						</s:if> --%>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<%@ include file="../create/createPropertyView.jsp"%>
/font>
					</tr>
					<div id="loadingMask" style="display: none">
						<p align="center">
							<img src="/egi/resources/erp2//images/bar_loader.gif"> <span
								id="message"><p style="color: red">Please wait....</p></span>
						</p>
					</div>
					<div class="buttonbottom" align="center">
						<tr>
							<%-- <s:if
								test="%{model.state.value.endsWith(@org.egov.ptis.nmc.constants.PropertyTaxConstants@WF_STATE_NOTICE_GENERATION_PENDING)}">
								<s:if test="%{extra_field3!='Yes'}">
									<input type="button" name="GenerateNotice" id="GenerateNotice"
										value="Generate Notice" class="button"
										onclick="return generatenotice();" />
								</s:if>

								<s:if test="%{extra_field4!='Yes'}">
									<input type="button" name="GeneratePrativrutta"
										id="GeneratePrativrutta" value="Generate Prativrutta"
										class="button" onclick="return generatePrativrutta();" />
								</s:if>

							</s:if>
							<s:else>
									<td>
										<s:submit value="Approve" name="Approve" id='Create:Approve'
											cssClass="buttonsubmit" method="approve"
											onclick="return onSubmit('createProperty-approve.action');" />
									</td>									
									<s:if test="%{isApprPageReq}">
										<td>
											<s:submit value="Forward" name="Forward" id='Create:Forward'
												cssClass="buttonsubmit" method="forward"
												onclick="return onSubmit('createProperty-forward.action');" />
										</td>
									</s:if>																	
									<td>
										<s:submit value="Reject" name="Reject" id='Reject'
											cssClass="buttonsubmit" method="reject"
											 onclick="return onSubmit('createProperty-reject.action');"/>
									</td>	
									<!-- <td>
										<input type="button" name="PreviewPrativrutta"
											id="PreviewPrativrutta" value="Preview Prativrutta"
											class="button" onclick="return previewPrativrutta();" />
									</td> -->
							</s:else> --%>
							<s:if
								test="@org.egov.ptis.constants.PropertyTaxConstants@ASSISTANT_DESGN.equalsIgnoreCase(userDesgn)">
								<s:if
									test="@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_STEP_COMMISSIONER_APPROVED.equalsIgnoreCase(model.state.nextAction) ||
							 @org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_STEP_REVENUE_OFFICER_APPROVED.equalsIgnoreCase(model.state.nextAction)">
									<td><input type="button" name="GenerateNotice6"
										id="GenerateNotice6" value="Generate Notice"
										class="buttonsubmit" onclick="return generateNotice6();" /></td>
								</s:if>
							</s:if>
									<div id="loadingMask" style="display:none"><p align="center"><img src="/egi/resources/erp2//images/bar_loader.gif"> <span id="message"><p style="color: red">Please wait....</p></span></p></div>
											
						<tr>
							<%@ include file="../workflow/commonWorkflowMatrix-button.jsp" %>  
						</tr>  
						
				
					</div>
				</table>
			</s:push>
		</s:form>
	</div>
</body>
</html>
