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

<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<html>
<head>
<link
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/javascript/objection.js' context='/ptis'/>"></script>
<title>
<s:if test="%{wfType.equals(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NAME_GRP)}">
<s:text name="objection.GRPView.title" />
</s:if>
<s:else>
<s:text name="objectionView.title" />
</s:else>
</title>
<script type="text/javascript">


	jQuery.noConflict();
	jQuery(document).ready(function()
			{	
		var designation = '<s:property value="%{currentDesignation}"/>';
		var state =  '<s:property value="%{objection.egwStatus.code}"/>';
		var value= '<s:property value="%{objection.currentState.nextAction}"/>'
		if(designation == 'Commissioner' && state!='CREATED') {
			jQuery('#Forward').hide();
		}else if(value=='Print Endoresement'){
			jQuery('#Forward').hide();
		}
					}); 
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

	var propType ;
	var appurtenantLandChecked;
	function loadOnStartUp() {
		propType = '<s:property value="%{objection.basicProperty.property.propertyDetail.propertyTypeMaster.type}"/>';
		appurtenantLandChecked = '<s:property value="%{objection.basicProperty.property.propertyDetail.appurtenantLandChecked}"/>';
		enableFieldsForPropTypeView(propType, appurtenantLandChecked);
		enableAppartnaumtLandDetailsView();
		enableOrDisableSiteOwnerDetails(jQuery('input[name="property.propertyDetail.structure"]'));
		//toggleFloorDetailsView();
		<s:if test="(objection.egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_COMPLETED))" >
			showHideFirmName();
			showHideLengthBreadth();
		</s:if>
		
		loadDesignationFromMatrix();
		
	}
	function loadDesignationFromMatrix() {
  		var e = dom.get('approverDepartment');
  		var dept = e.options[e.selectedIndex].text;
  			var currentState = dom.get('currentState').value;
  			var amountRule="";
  		var pendingAction=document.getElementById('pendingActions').value;
  		loadDesignationByDeptAndType('RevisionPetition',dept,currentState,amountRule,"",pendingAction); 
  	}
	function enableAppartnaumtLandDetailsView() {
		if (document.forms[0].appurtenantLandChecked != null && document.forms[0].appurtenantLandChecked.checked == true) {
			jQuery('tr.vacantlanddetaills').show();
			jQuery('#appurtenantRow').show();
			jQuery('tr.floordetails').show();
			jQuery('tr.extentSite').hide();
		} else {
			appurtenantLandChecked = '<s:property value="%{objection.basicProperty.property.propertyDetail.appurtenantLandChecked}"/>';
			enableFieldsForPropTypeView(propType, appurtenantLandChecked);
		}
	}
	function toggleFloorDetailsView() {
		var propType = '<s:property value="%{property.propertyDetail.propertyTypeMaster.type}"/>';
		if (propType == "Vacant Land") {
			jQuery('tr.floordetails').hide();
		} else {
			jQuery('tr.floordetails').show();
		}
		if (propType == "Apartments") {
			bootbox.alert("Please select Apartment/Complex Name");
		}
	}

	function onSubmit() {

		var actionName = document.getElementById('workFlowAction').value;
		var action = null;
		var userDesg = '<s:property value="%{userDesgn}"/>';
		var statusModuleType = '<s:property value="%{model.egwStatus.moduletype}"/>';
		var statusCode = '<s:property value="%{model.egwStatus.code}"/>';
		var state = '<s:property value="%{model.state.value}"/>';

		if (actionName == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_STEP_FORWARD}"/>') {

			if (statusCode == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_CREATED}"/>') {
				if (state == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@RP_CREATED}"/>'
						|| state == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@GRP_CREATED}"/>') {
					if (validateRecordObjections()) {
						action = 'revPetition.action';
					} else
						return false;

				} else if (validateHearingDate()) {
					action = 'revPetition-addHearingDate.action';
				} else
					return false;
			} else if (statusCode == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_FIXED}"/>') {
				action = 'revPetition-generateHearingNotice.action';

			} else if (statusCode == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_RECORD_GENERATEHEARINGNOTICE}"/>') {
				if (validateRecordHearing()) {
					action = 'revPetition-recordHearingDetails.action';
				} else
					return false;
			} else if (statusCode == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_COMPLETED}"/>') {
				if (validateRecordInspection()) {
					action = 'revPetition-recordInspectionDetails.action';
				} else
					return false;
			} else if (statusCode == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_INSPECTION_COMPLETED}"/>') {
				action = 'revPetition-validateInspectionDetails.action';

			} else if (statusCode == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_INSPECTION_VERIFY}"/>') {
				//if (validateObjectionOutcome()) {
					action = 'revPetition-recordObjectionOutcome.action';
				/* } else
					return false;
 				*/
			}else if(statusCode == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_ACCEPTED}"/>'){
				action = 'revPetition-recordObjectionOutcome.action';				
			}
			
		} else if (actionName == 'Print HearingNotice') {
			url = "/ptis/revPetition/revPetition-printHearingNotice.action?objectionId="
					+ document.getElementById("model.id").value;
			window.open(url, 'printHearingNotice', 'width=1000,height=400');
			return false;
		} else if (actionName == 'Save') {

			if (statusCode == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_RECORD_GENERATEHEARINGNOTICE}"/>') {
				if (validateRecordHearing()) {
					action = 'revPetition-recordHearingDetails.action';
				} else
					return false;
			} else if (statusCode == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_COMPLETED}"/>') {
				if (validateRecordInspection()) {
					action = 'revPetition-recordInspectionDetails.action';
				} else
					return false;
			}
		} else if (actionName == 'Print Endoresement') {
			action = 'revPetition-generateEnodresementNotice.action';
		} else if (actionName == 'Print Special Notice' || actionName == 'Sign') {
			action = 'revPetition-generateSpecialNotice.action?actionType='+actionName+'&objectionId='+document.getElementById("model.id").value;
		} else if (actionName == 'Preview' ) {
			var params = [
	   			'height='+screen.height,
	   		    'width='+screen.width,
	   		    'fullscreen=yes' 
	   		].join(',');
			
			window.open('revPetition-generateSpecialNotice.action?actionType='
							+ actionName + '&objectionId='
							+ document.getElementById("model.id").value,
							'NoticeWindow', params);
			return false;
		} else if (actionName == 'Reject Inspection') {
			action = 'revPetition-rejectInspectionDetails.action';
		} else if (actionName == 'Reject') {
			action = 'revPetition-reject.action';
		} else if (actionName == 'Approve' || actionName == 'Forward') {
			if (statusCode == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_INSPECTION_VERIFY}"/>') {
				//if (validateObjectionOutcome()) {
					/*  if(document.getElementById('approverPositionId').value=="-1") {
					        bootbox.alert("Please Select the Approver ");
							return false;
					    } */
					action = 'revPetition-recordObjectionOutcome.action?objectionId='
						+ document.getElementById("model.id").value;
				/* } else
					return false; */

			}
		}

		document.forms[0].action = action;
		document.forms[0].submit;
		return true;

		return true;
	}

	function showHideFirmName(){
		var tbl=document.getElementById("floorDetails");
        var tabLength = (tbl.rows.length)-1;
        var usageId ;
        if(tabLength==1){
        	enableDisableFirmName(getControlInBranch(tbl.rows[1],'floorUsage'));
        }else{
        	for(var i=0;i<tabLength;i++){
            	if(i==0){
            		enableDisableFirmName(getControlInBranch(tbl.rows[1],'floorUsage'));
                }else{
                	usageId = 'floorUsage'+(i-1);
                	enableDisableFirmName(getControlInBranch(tbl.rows[i+1],usageId));
                }
            }
        }
        
	}

	function enableDisableFirmName(obj){ 
		var selIndex = obj.selectedIndex;
		if(selIndex != undefined){
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
	} 


	function showHideLengthBreadth(){
		var tbl=document.getElementById("floorDetails");
        var tabLength = (tbl.rows.length)-1;
        if(tabLength==1){
            enableDisableLengthBreadth(getControlInBranch(tbl.rows[1],'unstructuredLand'));
        }else{
        	for(var i=0;i<tabLength;i++){
                	enableDisableLengthBreadth(getControlInBranch(tbl.rows[i+1],'unstructuredLand'));
                } 
            }
	} 

	function calculatePlintArea(obj){ 
		var rIndex = getRow(obj).rowIndex;
		var tbl = document.getElementById('floorDetails');
		var builtUpArea=getControlInBranch(tbl.rows[rIndex],'builtUpArea');
		var unstructureLand = getControlInBranch(tbl.rows[rIndex],'unstructuredLand');
		if(unstructureLand.options[unstructureLand.selectedIndex].text=='No'){
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
				buildLength.readOnly = false;      
				buildbreadth.readOnly = false;
				builtUpArea.readOnly = true;
			} else{
				buildLength.value="";
				buildLength.readOnly = true;
				buildbreadth.value="";
				buildbreadth.readOnly = true;
				builtUpArea.readOnly = false;
			}
		}
	}


		
</script>
<script
        src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
	<script src="<cdn:url value='/resources/javascript/helper.js' context='/ptis'/>"></script>
<link href="<cdn:url value='/resources/css/headertab.css'/>"
	rel="stylesheet" type="text/css" />
</head>
<body onload="loadOnStartUp();">
	<s:form action="revPetition-view" method="post"
		name="objectionViewForm" theme="simple">
		<s:push value="model">
			<s:if test="%{hasActionMessages()}">
				<div class="messagestyle">
					<s:actionmessage theme="simple" />
				</div>
			</s:if>
			<div class="errorstyle" id="lblError" style="display: none;"></div>
			<s:actionerror />
			<s:fielderror />
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><div id="header">
							<ul id="Tabs">
								<li id="propertyHeaderTab" class="First Active"><a
									id="header_1" href="#" onclick="showPropertyHeaderTab();"><s:text
											name="propDet"></s:text></a></li>
								<li id="objectionDetailTab" class=""><a id="header_2"
									href="#" onclick="showObjectionHeaderTab();">
									<s:if test="%{wfType.equals(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NAME_GRP)}">
									<s:text	name="objection.grp.details.heading"></s:text>
											</s:if>
											<s:else>
									<s:text
											name="objection.details.heading"></s:text>
											</s:else>
											</a></li>
								<%-- 				<li id="approvalTab" class="Last"><a id="header_3" href="#" onclick="showApprovalTab();"><s:text name="approval.details.title"></s:text></a></li>
 --%>
							</ul>
						</div></td>
				</tr>

				<tr>
					<td>
						<div id="property_header">
     						<s:if test="property!=null">
								<s:if
									test="(egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) 
							&& ( egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_INSPECTION_COMPLETED) ||
							egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_INSPECTION_VERIFY) ||
							egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_REJECTED) ||
							egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_ACCEPTED) ))
							">

									<jsp:include page="modifyPropertyViewForRevPetition.jsp" />

								</s:if>
								<s:else>
									<jsp:include page="modifyPropertyForObjectionForm.jsp" />
								</s:else>

							</s:if>
							<s:else>
								<jsp:include page="../view/viewProperty.jsp" />
							</s:else>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div id="objection_header" style="display: none;">

							<jsp:include page="objectionDetailsCommonView.jsp" />

							<s:if
								test="egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_CREATED) &&
        		state.value.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@GRP_RP_CREATED)">
								<jsp:include page="recordRevisionPetition.jsp" />
        		</s:if>
							<s:elseif
								test="egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) && 
						egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_CREATED)">
								<jsp:include page="addHearingDate.jsp" />
					</s:elseif>
							<s:elseif
								test="egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) 
							&& egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_FIXED)">
							</s:elseif>
							<s:elseif
								test="egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) 
						&& egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_RECORD_GENERATEHEARINGNOTICE)">
								<jsp:include page="recordHearingDetails.jsp" />
							</s:elseif>

							<s:elseif
								test="egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) 
							&& egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_COMPLETED)
							">
							    <div class="formmainbox" style="box-shadow:none;">
								<jsp:include page="recordInspecationDetails.jsp" />
								</div>
							</s:elseif>
							<s:elseif
								test="(egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) 
							&& egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_INSPECTION_COMPLETED))
							">

							</s:elseif>
							<s:if test="%{!documentTypes.isEmpty()}">

								<%@ include file="../common/DocumentUploadView.jsp"%>

							</s:if>
						</div>
					</td>
				</tr>
				<s:if test="%{state != null}">
					<tr>
						<%@ include file="../common/workflowHistoryView.jsp"%>
					</tr>
				</s:if>
				<tr>
					<td>
						<div id="approval_header" class="formmainbox" style="box-shadow:none;">
							<div id="wfHistoryDiv">
								<%--    <jsp:include page="../workflow/workflowHistory.jsp"/>
	  		 --%>

								<s:if
									test="egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_INSPECTION_COMPLETED)  ||
       					  	egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_FIXED)">
									<%-- <jsp:include page="../workflow/revisionPetition-workflow.jsp"/> --%>
									<jsp:include page="../workflow/commonWorkflowMatrix.jsp" />
								</s:if>
								<s:elseif
									test="egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_COMPLETED)
							">
									<%-- <jsp:include page="../workflow/revisionPetition-workflow.jsp"/> --%>
									<jsp:include page="../workflow/commonWorkflowMatrix.jsp" />
								</s:elseif>
								<s:elseif
									test="egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_CREATED)
							&& state.value.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@GRP_RP_CREATED)">
									<%-- <jsp:include page="../workflow/revisionPetition-workflow.jsp"/> --%>
									<jsp:include page="../workflow/commonWorkflowMatrix.jsp" />
								</s:elseif>
								<s:elseif test="%{egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_INSPECTION_VERIFY) && currentDesignation != null && !@org.egov.ptis.constants.PropertyTaxConstants@COMMISSIONER_DESGN.equalsIgnoreCase(currentDesignation.toUpperCase())}">
					   <jsp:include page="../workflow/commonWorkflowMatrix.jsp" />
					   </s:elseif>
								<s:elseif
									test="egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_ACCEPTED) && currentDesignation != null && !@org.egov.ptis.constants.PropertyTaxConstants@COMMISSIONER_DESGN.equalsIgnoreCase(currentDesignation.toUpperCase())">
									<s:if
										test="objection.currentState.nextAction.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_PRINT_ENDORSEMENT) || objection.currentState.nextAction.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_STEP_PRINT_NOTICE)">
									</s:if>
									<s:else>
										<jsp:include page="../workflow/commonWorkflowMatrix.jsp" />
									</s:else>
								</s:elseif>
								<s:else>
									<div align="center">
										<br>
										<%--  <table width="100%" border="0" cellspacing="0" cellpadding="0" >
       			 	<tr>
						<td class="bluebox" width="6%">&nbsp;</td>
				    	<td class="bluebox" width="10%"><s:text name='approver.comments'/></td>
						<td class="bluebox" width="8%"><s:textarea name="workflowBean.comments" id="comments"rows="3" cols="80" onblur="checkLength(this);" /></td>
						<td class="bluebox" width="15%" colspan="2"></td>
					</tr>	
       			 		
       			 		<s:hidden name="workflowBean.actionName" id="workflowBean.actionName"/>
       			 		</table>        
       			 --%>
       			                    <%-- <jsp:include page="../workflow/commonWorkflowMatrix.jsp" /> --%>
									</div>
								</s:else>
							</div>
						</div>
					</td>
				</tr>
			</table>
		
			<div class="buttonbottom" align="center">

				<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>

			</div>
			<s:hidden name="model.id" id="model.id" />
			<s:hidden name="egwStatus.code" id="egwStatuscode"
				value="%{egwStatus.code}" />
			<%-- 	<s:hidden name="model.property" id="model.property"/>
		 --%>
		 <s:hidden name="wfType" id="wfType" value="%{wfType}" />		</s:push>
	</s:form>

</body>
</html>
