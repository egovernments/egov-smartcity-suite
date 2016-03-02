<!-------------------------------------------------------------------------------
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
------------------------------------------------------------------------------->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>

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
			<s:elseif test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(modifyRsn)}">
		                <s:text name='GenRevPetition.title' />
	         </s:elseif>
		</title>
		<sx:head />
		<link
			href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"
			rel="stylesheet" type="text/css" />
		<script
			src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
		<script type="text/javascript">
			jQuery.noConflict();
			jQuery("#loadingMask").remove();
			jQuery(function($) {
				try {
					$(".datepicker").datepicker({
						format : "dd/mm/yyyy",
						autoclose:true
					});
					reInitializeDateOnChangeEvent();
				} catch (e) {
					console.warn("No Date Picker " + e);
				}
			});


			function onSubmit() {
				var actionName = document.getElementById('workFlowAction').value;
				var nextAction = '<s:property value="%{model.state.nextAction}"/>'; 
				var action = null;
				var userDesg = '<s:property value="%{userDesgn}"/>';
				var state = '<s:property value="%{model.state.value}"/>';
				if (actionName == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_STEP_FORWARD}"/>') {
					if (userDesg == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@JUNIOR_ASSISTANT}"/>' 
						|| userDesg == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@SENIOR_ASSISTANT}"/>'
						|| (nextAction != null && nextAction == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING}"/>')
						|| state == 'Alter:Rejected') {
						action = 'modifyProperty-forward.action';
					} else {
						action = 'modifyProperty-forwardView.action';
					}
				} else if (actionName == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_STEP_APPROVE}"/>') {
					action = 'modifyProperty-approve.action';
				} else if (actionName == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_STEP_REJECT}"/>') {
					action = 'modifyProperty-reject.action';
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
			
			function loadOnStartUp() {
				enableFieldsForPropTypeView();
				enableAppartnaumtLandDetailsView();
				enableOrDisableSiteOwnerDetails(jQuery('input[name="propertyDetail.structure"]'));
				enableOrDisableBPADetails(jQuery('input[name="propertyDetail.buildingPlanDetailsChecked"]'));
				toggleFloorDetailsView();
				showHideFirmName();
				showHideLengthBreadth();
			}

			function enableDisableFirmName(obj){ 
				var selIndex = obj.selectedIndex;
				var selText = obj.options[selIndex].text; 
				var rIndex = getRow(obj).rowIndex;
				var tbl = document.getElementById('floorDetails');
				var firmval=getControlInBranch(tbl.rows[rIndex],'firmName'); 
				if(selText!=null && selText=='<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@NATURE_OF_USAGE_RESIDENCE}"/>'){
					if(firmval.value!=null && firmval.value!="") 
						firmval.value="";
					firmval.readOnly = true;      
				} else{
					firmval.readOnly = false;
				}
			}  

			function showHideLengthBreadth(){
				var tbl=document.getElementById("floorDetails");
		        var tabLength = (tbl.rows.length)-1;
		        for(var i=1;i<=tabLength;i++){
		             enableDisableLengthBreadth(getControlInBranch(tbl.rows[i],'unstructuredLand'));
		        }
			}

			function showHideFirmName(){
				var tbl=document.getElementById("floorDetails");
		        var tabLength = (tbl.rows.length)-1;
		        for(var i=1;i<=tabLength;i++){
		             enableDisableFirmName(getControlInBranch(tbl.rows[i],'floorUsage'));
		        }
			}

			function calculatePlintArea(obj){ 
				var rIndex = getRow(obj).rowIndex;
				var tbl = document.getElementById('floorDetails');
				var builtUpArea=getControlInBranch(tbl.rows[rIndex],'builtUpArea');
				if(getControlInBranch(tbl.rows[rIndex],'unstructuredLand').value=='true'){
					if(obj.value!=null && obj.value!=""){
						var buildLength=getControlInBranch(tbl.rows[rIndex],'builtUpArealength');
						var buildbreadth=getControlInBranch(tbl.rows[rIndex],'builtUpAreabreadth');
						  
						if(buildLength.value!=null && buildLength.value!="" && buildbreadth.value!=null && buildbreadth.value!=""){
							builtUpArea.value= roundoff(eval(buildLength.value * buildbreadth.value));
							trim(builtUpArea,builtUpArea.value);
							checkForTwoDecimals(builtUpArea,'Assessable Area');
							checkZero(builtUpArea,'Assessable Area');
						}
						else
							builtUpArea.value="";
					}else
						builtUpArea.value="";
				}
			}
			
			function enableDisableLengthBreadth(obj){ 
				var selIndex = obj.selectedIndex;
				if(obj.value=='true'){
						obj.value='true';
						obj.options[selIndex].selected = true;
				}
				else{
					obj.value='false';
					obj.options[selIndex].selected = true;
				}
				
				if(selIndex != undefined){
					var selText = obj.options[selIndex].text; 
					var rIndex = getRow(obj).rowIndex;
					var tbl = document.getElementById('floorDetails');
					var buildLength=getControlInBranch(tbl.rows[rIndex],'builtUpArealength');
					var buildbreadth=getControlInBranch(tbl.rows[rIndex],'builtUpAreabreadth');  
					var builtUpArea=getControlInBranch(tbl.rows[rIndex],'builtUpArea');
					if(selText!=null && selText=='No'){
						buildLength.value="";
						buildLength.readOnly = true;      
						buildbreadth.value="";
						buildbreadth.readOnly = true;
						builtUpArea.readOnly = false;
					} else{
						buildLength.readOnly = false; 
						buildbreadth.readOnly = false;
						builtUpArea.readOnly = true;
					}
				}
			}

			function enableAppartnaumtLandDetailsView() {
				if (document.forms[0].appurtenantLandChecked.checked == true) {
					jQuery('tr.vacantlanddetaills').show();
					jQuery('#appurtenantRow').show();
					jQuery('tr.floordetails').show();
					jQuery('tr.extentSite').hide();
				} else {
					enableFieldsForPropTypeView();
				}
			}

			function enableFieldsForPropTypeView() {
				var propType = '<s:property value="%{model.propertyDetail.propertyTypeMaster.type}"/>';
				if (propType != "select") {
					//onChangeOfPropertyTypeFromMixedToOthers(propType);
					if (propType == "Vacant Land") {
						jQuery('tr.floordetails').hide();
						jQuery('tr.vacantlanddetaills').show();
						jQuery('tr.construction').hide();
						jQuery('tr.amenities').hide();
						jQuery('#appurtenantRow').hide();
						jQuery('tr.extentSite').hide();
						jQuery('tr.appurtenant').hide();
						jQuery('tr.superStructureRow').hide();
						jQuery('tr.bpddetailsheader').hide();
						jQuery('tr.bpddetails').hide();
						jQuery("#apartment").prop('selectedIndex', 0);
						jQuery('td.apartmentRow').hide();
					} else {
						jQuery('tr.floordetails').show();
						jQuery('tr.vacantlanddetaills').hide();
						jQuery('tr.construction').show();
						jQuery('tr.amenities').show();
						jQuery('#appurtenantRow').hide();
						jQuery('tr.extentSite').show();
						jQuery('tr.appurtenant').show();
						jQuery('tr.superStructureRow').show();
						jQuery('tr.bpddetailsheader').show();
						jQuery('tr.bpddetails').show();
						jQuery('td.apartmentRow').show();
					}
				}
			}

			function toggleFloorDetailsView() {
				var propType = '<s:property value="%{model.propertyDetail.propertyTypeMaster.type}"/>';
				if (propType == "Vacant Land") {
					jQuery('tr.floordetails').hide();
				} else {
					jQuery('tr.floordetails').show();
				}
				if (propType == "Apartments") {
					bootbox.alert("Please select Apartment/Complex Name");
				}
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
			
			function generateNotice(){
				var noticeType = '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@NOTICE_TYPE_SPECIAL_NOTICE}"/>';
			   	document.ModifyPropertyForm.action="../notice/propertyTaxNotice-generateNotice.action?basicPropId=<s:property value='%{basicProp.id}'/>&noticeType="+noticeType+"&noticeMode=modify";
				document.ModifyPropertyForm.submit();
			}
			  
			function generateBill(){
				doLoadingMask();
				document.ModifyPropertyForm.action="../bills/billGeneration!generateBill.action?indexNumber=<s:property value='%{basicProp.upicNo}'/>";
				document.ModifyPropertyForm.submit();
				undoLoadingMask();
			}				

</script>
<script src="<c:url value='/resources/global/js/egov/inbox.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/javascript/helper.js' context='/ptis'/>"></script>
	</head>
	<body onload="loadOnStartUp();">
		<div align="left" class="errortext">
			<s:actionerror />
		</div>
		<s:if test="%{hasActionMessages()}">
			<div id="actionMessages" class="messagestyle" align="center">
				<s:actionmessage theme="simple" />
			</div>
			<div class="blankspace">
				&nbsp;
			</div>
		</s:if>
		<!-- Area for error display -->
		<div class="errorstyle" id="property_error_area"
			style="display: none;"></div>
		<s:form name="ModifyPropertyForm" action="modifyProperty" enctype="multipart/form-data"
			theme="simple" validate="true">
			<s:push value="model">
			<s:token/>
				<s:hidden name="modifyRsn" value="%{modifyRsn}" />
				<div class="formmainbox">
					<div class="headingbg" id="modPropHdr">
						<s:if test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn)}">
							<s:text name='ModProp.title' />
						</s:if>
						<s:elseif test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)}">
							<s:text name='BifurProp.title' />
						</s:elseif>
						<s:elseif test="%{@org.egov.ptis.constants.PropertyTaxConstants@PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(modifyRsn)}">
		                    <s:text name='GenRevPetition.title' />
	                    </s:elseif> 
					</div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<s:if test="%{(model.state.nextAction!=null && 
						@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING.equalsIgnoreCase(model.state.nextAction)) ||
						((@org.egov.ptis.constants.PropertyTaxConstants@JUNIOR_ASSISTANT.equalsIgnoreCase(userDesgn) ||
						@org.egov.ptis.constants.PropertyTaxConstants@SENIOR_ASSISTANT.equalsIgnoreCase(userDesgn))
							&& !model.state.value.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_DIGITALLY_SIGNED))}">
						<tr>
							<%@ include file="../modify/modifyPropertyForm.jsp"%>
						</tr> 
					</s:if>
					<s:elseif test="%{model.state.nextAction.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_COMMISSIONER_APPROVAL_PENDING) ||
					        model.state.value.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_COMMISSIONER_APPROVED) ||
							@org.egov.ptis.constants.PropertyTaxConstants@REVENUE_OFFICER_DESGN.equalsIgnoreCase(userDesgn) ||
							@org.egov.ptis.constants.PropertyTaxConstants@BILL_COLLECTOR_DESGN.equalsIgnoreCase(userDesgn) ||
							((@org.egov.ptis.constants.PropertyTaxConstants@JUNIOR_ASSISTANT.equalsIgnoreCase(userDesgn) || 
							@org.egov.ptis.constants.PropertyTaxConstants@SENIOR_ASSISTANT.equalsIgnoreCase(userDesgn))
							&& model.state.value.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_DIGITALLY_SIGNED)) }">
						<tr>
							<%@ include file="../modify/modifyPropertyView.jsp"%>
						</tr>
					</s:elseif>
					<s:if test="%{state != null}">   
						<tr>
							<%@ include file="../common/workflowHistoryView.jsp"%>
						<tr>					
					</s:if> 
					<s:if test="%{(!(model.state.nextAction.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_COMMISSIONER_APPROVAL_PENDING)
					     || model.state.value.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_COMMISSIONER_APPROVED)) ||
						((@org.egov.ptis.constants.PropertyTaxConstants@JUNIOR_ASSISTANT.equalsIgnoreCase(userDesgn) || 
							@org.egov.ptis.constants.PropertyTaxConstants@SENIOR_ASSISTANT.equalsIgnoreCase(userDesgn))
							&& model.state.value.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_DIGITALLY_SIGNED)))}">
						<tr>
							 <%@ include file="../workflow/commonWorkflowMatrix.jsp"%>
						</tr>
					</s:if>
					<s:if test="%{model.state.nextAction.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_COMMISSIONER_APPROVAL_PENDING) ||
					    model.state.value.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_COMMISSIONER_APPROVED)}">
						<div id="workflowCommentsDiv" align="center">
					         <table width="100%">
								<tr>
						        	<td width="10%" class="bluebox">&nbsp;</td>
						           	<td width="20%" class="bluebox">&nbsp;</td>
						           	<td class="bluebox" width="13%">Approver Remarks: </td>
						           	<td class="bluebox"> 
						           		<textarea id="approverComments" name="approverComments" rows="2" cols="35" ></textarea>  
						           	</td>
						           	<td class="bluebox">&nbsp;</td>
						           	<td width="10%" class="bluebox">&nbsp;</td>
						           	<td class="bluebox">&nbsp;</td>
						           	</tr>
					         </table>
					  </div>   
					</s:if>
					<s:hidden name="modelId" id="modelId" value="%{modelId}" />
					<tr>
						<font size="2"><div align="left" class="mandatory">
								<s:text name="mandtryFlds" />
							</div> </font>
					</tr>
					<div class="buttonbottom" align="center">
						<%@ include file="../workflow/commonWorkflowMatrix-button.jsp" %>
					</div>
					</table>
				</div>
			</s:push>
		</s:form>
	</body>
</html>
