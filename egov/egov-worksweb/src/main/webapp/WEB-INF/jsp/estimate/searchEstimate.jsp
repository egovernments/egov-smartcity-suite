<!-- -------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ include file="/includes/taglibs.jsp" %> 

<%@ page import="java.util.*" %>
<script src="<egov:url path='resources/js/works.js'/>"></script>

<html>
	<head>
		<s:if test="%{source=='financialdetail'}">
			<title><s:text name='page.title.financial.detail' />
			</title>
		</s:if>
		<s:elseif test="%{source=='technical sanction'}">
			<title><s:text name='page.title.Technical.Sanction' />
			</title>
		</s:elseif>
		<s:elseif test="%{source=='Financial Sanction'}">
			<title><s:text name='page.title.Financial.Sanction' />
			</title>
		</s:elseif>
		<s:elseif test="%{source=='AdministrativeSanction'}">
			<title><s:text name='page.title.Admin.Sanction' />
			</title>
		</s:elseif>
		<s:elseif test="%{source=='SearchEstimateforWO'}">
			<title><s:text name='page.title.Estimate.WorkOrder' />
			</title>
		</s:elseif>
		<s:elseif test="%{source=='cancelMilestone'}">
			<title><s:text name='cancelMS.title' />
			</title>
		</s:elseif>
		<s:else>
			<title><s:text name='page.title.search.estimate' />
			</title>
		</s:else>
	</head>
	<script type="text/javascript">
	var fundSourceIds = new Array();

    function returnBackToParent() { 
	var value = new Array();
	var wind=window.dialogArguments;
	var len=document.forms[0].selectedEstimate.length; 
	var j=0;
	if(len >0){
		for (i = 0; i < len; i++){
			if(document.forms[0].selectedEstimate[i].checked){
				value[j] = document.forms[0].abEstId[i].value;
				fundSourceIds[j] = document.forms[0].fundSourceId[i].value;
				j++;
			}
		}
	}
	else{
		if(document.forms[0].selectedEstimate.checked){
			value[j] = dom.get('abEstId').value;
		}
	}
	if(value.length>0)
	{
		var wind;
		var j,fundSourceId1;
		var data = new Array();
		wind=window.dialogArguments;
	
		validateForRCEstimates(value);
	}
	else{
		dom.get("searchEstimate_error").innerHTML='Please Select any one of the estimates'; 
        dom.get("searchEstimate_error").style.display='';
		return false;
	 }
	 dom.get("searchEstimate_error").style.display='none';
	 dom.get("searchEstimate_error").innerHTML='';
	return true;
}

    function validateForRCEstimates(estimateIds){
    	makeJSONCall(["Value","estimateNum","estimateIds"],'${pageContext.request.contextPath}/estimate/ajaxEstimate!checkIfMultipleRCEstimatesSelected.action',{estimateIds:estimateIds},validateRCEstimateSuccess,validateRCEstimateFailure) ;
    }

    validateRCEstimateSuccess = function(req,res){
		results=res.results;
		
		var checkResult='';
		var estimateNum='';
		var data = new Array();
		var wind=window.dialogArguments;
		var fundSourceId1;
		
		if(results != '') {
			checkResult =   results[0].Value;
			estimateNum =   results[0].estimateNum;
		}
		
		if(checkResult != '' && checkResult=='yes'){
			dom.get("searchEstimate_error").innerHTML='<s:text name="wp.rcEstimate.check.msg1" />'+estimateNum+' '+'<s:text name="wp.rcEstimate.check.msg2" />';
		    dom.get("searchEstimate_error").style.display='';
		    return false;
		}
		else{
			if(fundSourceIds.length>0){
				fundSourceId1=fundSourceIds[0];
				for(j=1;j<fundSourceIds.length;j++){
					if(fundSourceId1!=fundSourceIds[j]){
							dom.get("searchEstimate_error").innerHTML='<s:text name="wp.estimate.different.fund.source.not.allowed"/>'; 
					        dom.get("searchEstimate_error").style.display='';
							return false;
					}
					
				}
			}
			
			if(wind==undefined){
					wind=window.opener;
					data=results[0].estimateIds;
					window.opener.update(data);
				}
				else{
					wind=window.dialogArguments;
					wind.result=value;
				}
				window.close();
			}
	}
 
 validateRCEstimateFailure= function(){
	    dom.get("searchEstimate_error").style.display='';
		document.getElementById("searchEstimate_error").innerHTML='<s:text name="wp.rcEstimate.check.failure" />';
	}    
function enableCreateTrackMilestone(){
		dom.get("trackMilestoneButton").style.visibility='visible';
		dom.get("createMilestoneButton").style.visibility='visible';
		dom.get("viewMilestoneButton").style.visibility='hidden';
}

function disableCreateTrackMilestone(){
		dom.get("trackMilestoneButton").style.visibility='hidden';
		dom.get("createMilestoneButton").style.visibility='hidden';
		dom.get("viewMilestoneButton").style.visibility='visible';
}

function enableprintButton(){
document.getElementById('printButton').style.display='';
document.getElementById('printButton').style.visibility='visible';
}     
function disableSelect(){
	document.getElementById('status').disabled=true
}
function disableDept(){
	document.getElementById('status').disabled=true
	dom.get('executingDepartment').disabled=true 
}
function enableSelect(){
	if(document.getElementById('status')!=null){
	document.getElementById('status').disabled=false
	}
	dom.get('executingDepartment').disabled=false
}


function addFinancial(){
	document.getElementById('status').disabled=false;
	var id = document.techSanctionEstimatesForm.estimateId.value;
	if(id==null || id==''){
		showMessage('error_search','Please Choose An Estimate Before Adding Financial Details');
	}
	else{
		clearMessage('error_search')
		window.open('${pageContext.request.contextPath}/estimate/financialDetail.action?estimateId='+id,'_self');
	} 
}

function createNegotiation(){
	document.getElementById('status').disabled=false;
	var id = document.techSanctionEstimatesForm.estimateId.value;
	

	if(id==null || id==''){
		showMessage('error_search','<s:text name="search.estimate.negotiation.create"/>');
		disableSelect();
	}
	else{
		clearMessage('error_search')
		window.open('${pageContext.request.contextPath}/tender/tenderNegotiation!newform.action?tenderSource=estimate&estimateId='+id,'_self');
	}
}

function createWO(){
 	var id = document.techSanctionEstimatesForm.estimateId.value;
	if(id==null || id==''){
		showMessage('error_search','<s:text name="search.estimate.WorkOrder.create"/>');
		disableSelect();
	}
	else{
		 clearMessage('error_search');
	}
}

function viewMilestone(){
	clearMessage('error_search');
	var woEstimateId=document.getElementById('woEstimateId').value;
	var milestoneId = document.getElementById('milestoneId').value;
	if(woEstimateId==null || woEstimateId==''){
		showMessage('error_search','<s:text name="search.estimate.milestone.view"/>');
	}
	else{
		clearMessage('error_search');
		window.open('${pageContext.request.contextPath}/milestone/trackMilestone!view.action?woEstimateId='+woEstimateId+'&milestoneId='+milestoneId+'&mode=view','_self');
	}
}


function createMilestone(){
	clearMessage('error_search');
	var woEstimateId=document.getElementById('woEstimateId').value;
	if(woEstimateId==null || woEstimateId==''){
		showMessage('error_search','<s:text name="search.estimate.milestone.create"/>');
	}
	else{
		clearMessage('error_search');
		window.open('${pageContext.request.contextPath}/milestone/milestone!newform.action?woEstimateId='+woEstimateId,'_self');
	}
}

function trackMilestone(){
	clearMessage('error_search');
	var woEstimateId=document.getElementById('woEstimateId').value;
	if(woEstimateId==null || woEstimateId==''){
		showMessage('error_search','<s:text name="search.estimate.milestone.create"/>');
	}
	else{
			clearMessage('error_search');
			if(dom.get("isWrkOrdrWrkCommenced").value!="" && dom.get("isWrkOrdrWrkCommenced").value == 'false'){
				showMessage('error_search','<s:text name="search.estimate.trackmilestone.validate"/>');
				window.scroll(0,0);
			}
			else if(dom.get("isWrkOrdrWrkCommenced").value!="" && dom.get("isWrkOrdrWrkCommenced").value == 'true')
				window.open('${pageContext.request.contextPath}/milestone/trackMilestone!newform.action?woEstimateId='+woEstimateId,'_self');
		}
}

function populateUser1(obj){
	var elem=document.getElementById('executingDepartment');
	deptId=elem.options[elem.selectedIndex].value;
	populateengineerIncharge({desgId:obj.value,executingDepartment:deptId})
}
function populateUser2(obj){
	var elem=document.getElementById('executingDepartment');
	deptId=elem.options[elem.selectedIndex].value;
	populateengineerIncharge2({desgId:obj.value,executingDepartment:deptId})
}

function populateDesignation1(){	
	if(dom.get("executingDepartment").value!="" && dom.get("executingDepartment").value!="-1"){
		populateassignedTo1({departmentName:dom.get("executingDepartment").options[dom.get("executingDepartment").selectedIndex].text})
		populateassignedTo2({departmentName:dom.get("executingDepartment").options[dom.get("executingDepartment").selectedIndex].text})
	}
	else {removeAllOptions(dom.get("assignedTo1"));
	removeAllOptions(dom.get("engineerIncharge"));
	<s:if test="%{'yes'.equals(assignedToRequiredOrNot)}">
	removeAllOptions(dom.get("assignedTo2")); removeAllOptions(dom.get("engineerIncharge2"));
	</s:if>
	}
}

function validateSearch()
{
	if(dom.get('source').value=="searchEstimateForMilestone" || dom.get('source').value=="viewMilestone" 
		|| dom.get('source').value=="cancelMilestone"){
		if(dom.get('executingDepartment').value==-1){
			dom.get("searchEstimate_error").innerHTML='<s:text name="search.execDept.null"/>'; 
        	dom.get("searchEstimate_error").style.display='';
        	return false;
		}
		if(dom.get('source').value=="cancelMilestone" && dom.get('checkWO').checked 
				&& document.getElementById('workOrderNumberSearch').value=="") {
			dom.get("searchEstimate_error").innerHTML='<s:text name="estimate.search.workOrdNum.null"/>'; 
        	dom.get("searchEstimate_error").style.display='';
        	return false;
		}
	 }
	 else{
		if(dom.get('type').value==-1 && dom.get('status').value==-1 && 
		dom.get('location').value=="" && dom.get('executingDepartment').value==-1 && 
		dom.get('fromDate').value=="" && dom.get('toDate').value==""  &&  dom.get('preparedBy').value==-1 &&
		dom.get('parentCategory').value==-1 && dom.get('category').value==-1 && dom.get('description').value=="")
		{
			dom.get("searchEstimate_error").innerHTML='Please Select any one of the Search Parameters'; 
        	dom.get("searchEstimate_error").style.display='';
        	return false;
	 	}
	 }
	 dom.get("searchEstimate_error").style.display='none';
	 dom.get("searchEstimate_error").innerHTML='';
	hideResult(); 
	return true;
}	

function setupSubTypes(elem){
	categoryId=elem.options[elem.selectedIndex].value;
    populatecategory({category:categoryId});
}

function setupPreparedByList(elem){
 	<s:if test="%{!('wp').equals(source)}">
    deptId=elem.options[elem.selectedIndex].value;
    populatepreparedBy({executingDepartment:deptId});
    </s:if>
}

function checkPrint(){

	if(dom.get('type').value==-1 && dom.get('status').value==-1 && 
	dom.get('location').value=="" && dom.get('executingDepartment').value==-1 && 
	dom.get('fromDate').value=="" && dom.get('toDate').value==""  &&  dom.get('preparedBy').value==-1 &&
	dom.get('parentCategory').value==-1 && dom.get('category').value==-1 && dom.get('description').value=="")
	{
		dom.get("searchEstimate_error").innerHTML='Please search the Estimate Before Printing'; 
        dom.get("searchEstimate_error").style.display='';
       	return false;
	 }
	 dom.get("searchEstimate_error").style.display='none';
	 dom.get("searchEstimate_error").innerHTML='';
	 openPrint();
}

function sortBy() {
	if($F('selectedorder') == 'false')
		$('selectedorder').value =true;
	else
		$('selectedorder').value =false;
	
	this.document.techSanctionEstimatesForm.action = '${pageContext.request.contextPath}/estimate/searchEstimate!search.action';
	this.document.techSanctionEstimatesForm.submit();
}

function openPrint(){
		var actionUrl = '${pageContext.request.contextPath}/estimate/searchEstimate!printpage.action?decorate=false';
		var params    = $('techSanctionEstimatesForm').serialize();
		
	 	var ajaxCall = new Ajax.Request(actionUrl,
		{
				parameters:params,
				method:'post',
			 	onSuccess:function(transport){
					var printWin = window.open("","printSpecial","menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes");
					printWin.document.open();
					printWin.document.write(transport.responseText);
					printWin.document.close();
					printWin.print();
			   }
			 	
		});
 //window.open(actionUrl+'?'+params,'mywindow',"height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
 
}


function saveCancelApprovedMS() {
	var value = new Array();
	var cancellationReason = document.techSanctionEstimatesForm.cancellationReason.value; 
	var cancelRemarks = document.techSanctionEstimatesForm.cancelRemarks.value; 
	
	var milestonecheck = document.getElementsByName("msCheck");
	var projClose = document.getElementsByName("projClosed");
	var workOrderNum = document.getElementsByName("workOrderNum");
	var projectCodeMS = document.getElementsByName("projectCodeMS");
	var isTmsExist = document.getElementsByName("isTmsExist");
	var woeid = document.getElementsByName("woEstimateId");
	var hcheck = document.getElementById("hcheck");
	var len=milestonecheck.length; 
	
	var j=0;
	var projClosed_projCodes="";
	var trackMS_projCodes="";
	if(len >0){
		for (var i = 0; i < len; i++){
			if(milestonecheck[i].checked){
				value[j] = woeid[i].value;
				if(projClose[i].value=='CLOSED') {
					projClosed_projCodes = projClosed_projCodes + projectCodeMS[i].value + ",";
				}
				if(isTmsExist!=null && isTmsExist[i]!=undefined && isTmsExist[i].value=="true") {
					trackMS_projCodes = trackMS_projCodes + projectCodeMS[i].value + ",";
				}
				j++;
			}
		}
		if(len>=1 && j==0) {
			dom.get("searchEstimate_error").style.display='';
			document.getElementById("searchEstimate_error").innerHTML='<s:text name="ms.cancel.not.selected" />';
			window.scroll(0,0);
			return false;
		} 
		else if(len>1 && j>1 && !dom.get('checkWO').checked) {
			dom.get("searchEstimate_error").style.display='';
			document.getElementById("searchEstimate_error").innerHTML='<s:text name="ms.cancel.not.select.morethanone" />';
			window.scroll(0,0);
			return false;
		}
		if(projClosed_projCodes!="") {
			dom.get("searchEstimate_error").style.display='';
			document.getElementById("searchEstimate_error").innerHTML='<s:text name="cancelMS.project.closed.message1"/> '+projClosed_projCodes+' <s:text name="cancelMS.project.closed.message2"/>';
			window.scroll(0,0);
			return false;
		}

		if(cancellationReason=='Select'){  
			dom.get("searchEstimate_error").style.display='';
			document.getElementById("searchEstimate_error").innerHTML='<s:text name="validate.cancel.ms.reason"/>';
			window.scroll(0,0);
			return false;
		}	
		if(cancellationReason=='OTHER' && cancelRemarks == ''){  
			dom.get("searchEstimate_error").style.display='';
			document.getElementById("searchEstimate_error").innerHTML='<s:text name="validate.cancel.ms.remarks"/>';
			window.scroll(0,0);
			return false;
		}
		
		if(trackMS_projCodes!="") {
			var msg='<s:text name="ms.cancel.confirm.trackmilestone1"/>'+trackMS_projCodes+' <s:text name="ms.cancel.confirm.trackmilestone2"/> ?';
			if(!confirm(msg)) {
				return false;
			}
		}
			
		if(trackMS_projCodes=="") {
			var cancelConfirmMsg='<s:text name="ms.cancel.final.confirm"/> ?';
			if(!confirm(cancelConfirmMsg)) {
				return false;
			}
		}
		doLoadingMask();
		window.open('${pageContext.request.contextPath}/estimate/searchEstimate!cancelApprdMilestones.action?workOrdEstIds='+value+'&cancelRemarks='+cancelRemarks+'&cancellationReason='+cancellationReason,'_self');
	}
}
	
function toggleCancelMSRemarks(obj) { 
	if(obj.value=='OTHER') {
		document.getElementById("cancelMSRemarksDtls").style.display='';
	}
	else {
		document.getElementById("cancelMSRemarksDtls").style.display='none';
		dom.get("cancelRemarks").value='';
	}	
}   

function toggleSelectAll(mileStoneObj) {
	  mileStoneCheckbox = document.getElementsByName('msCheck');
	  for(var i=0, n=mileStoneCheckbox.length;i<n;i++) {
		  mileStoneCheckbox[i].checked = mileStoneObj.checked;
	  }
	}

var projectCodeSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
}


function enableWONumber() {
	if(dom.get('source').value=="cancelMilestone") {
		var isWorkOrder = document.getElementById("checkWO");
		if(isWorkOrder.checked) {
			document.getElementById('cancelWO1Dtls').style.display='';
			document.getElementById('cancelWO2Dtls').style.display='';
		} else {
			document.getElementById('cancelWO1Dtls').style.display='none';
			document.getElementById('cancelWO2Dtls').style.display='none';
			document.getElementById('workOrderNumberSearch').value="";
		}
	}
}

function setupInnerStatuses(){
	if(dom.get('source').value=="viewMilestone") {
		var statusName = document.getElementById("milestoneStatus").value;
		
		if(statusName=='Milestone Created' || statusName=='Milestone Tracked') {
			document.getElementById('msStatusLbl').style.display='';
			document.getElementById('msStatusVals').style.display='';
			document.getElementById('msStatMain').colSpan="0";
			
		} else {
			document.getElementById('msStatusLbl').style.display='none';
			document.getElementById('msStatusVals').style.display='none';
			document.getElementById('msStatMain').colSpan="3";
		}
	}
}

var workOrderNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

var estimateNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};
function loadWaitingImage() {
	if(document.getElementById('loading')){
		document.getElementById('loading').style.display='block';
	}
}

function clearWaitingImage(){
	if(document.getElementById('loading')){
		document.getElementById('loading').style.display='none';
	}
}
function hideResult()
{
	dom.get("resultRow").style.display='none';
}
wardSearchSelectionHandler = function(sType, arguments) { 
    var oData = arguments[2];
    dom.get("wardSearch").value=oData[0];
    dom.get("wardID").value = oData[1];
}

var wardSelectionEnforceHandler = function(sType, arguments) {
    warn('improperWardSelection');
}

function clearHiddenWardId(obj)
{
	if(obj.value=="")
	{
		document.getElementById("wardID").value="";
	}	
}

function jurisdictionSearchParameters(){
	return "isBoundaryHistory=true";
}

</script>

	<body onload="enableWONumber();setupInnerStatuses();">
		<div class="errorstyle" id="searchEstimate_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="techSanctionEstimatesForm" action="searchEstimate!search.action"
			id="techSanctionEstimatesForm" theme="simple"
			onsubmit="enableSelect()">
			<s:hidden name="selectedorder" id="selectedorder" />
			<s:hidden name="loginUserDeptName" id="loginUserDeptName"/>
			 
			<div class="formmainbox">
				<div class="insidecontent">
					<div id="printContent" class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										&nbsp;
									</td>
								</tr>
								
								<s:if
									test="%{estimateOrWpSearchReq!=null && estimateOrWpSearchReq =='both'}">
									<tr>
										<td>
											<%@ include file="../tender/estimateOrWpSearch.jsp"%>
										</td>
										<script>
 				document.forms[0].tenderForEst.checked=true;
			</script>
									</tr>
								</s:if>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td colspan="4" class="headingwk">
													<div class="arrowiconwk">
														<img
															src="${pageContext.request.contextPath}/image/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name="page.subheader.search.estimate" />
													</div>
												</td>
											</tr>
											<s:if test="%{source=='createNegotiationNew'}">
												<tr>
													<td width="11%" class="greyboxwk">
														<s:text name="estimate.search.estimateStatus" />
														:
													</td>
													<td width="21%" class="greybox2wk">
														<s:select id="status" name="status" headerKey="-1"
															headerValue="ALL" cssClass="selectwk"
															list="%{estimateStatuses}" listKey="code"
															listValue="description" />

													</td>
													<script>disableSelect();</script>
													<td width="15%" class="greyboxwk">
														<s:text name="estimate.executing.department" />
														:
													</td>
													<td width="53%" class="greybox2wk">
														<s:select headerKey="-1"
															headerValue="%{getText('estimate.default.select')}"
															name="execDept" id="executingDepartment"
															cssClass="selectwk"
															list="dropdownData.executingDepartmentList" listKey="id"
															listValue="deptName" value="%{execDept}" />
													</td>
													<s:if test="%{negoCreatedBy=='no'}">
														<script>disableDept();</script>
													</s:if>

													<s:if test="%{source=='wp'}">
														<script>disableDept();</script>
													</s:if>
												</tr>
											</s:if>
											<s:elseif test="%{source=='SearchEstimateforWO'}">
												<tr>
													<td width="11%" class="greyboxwk">
														<s:text name="search.estimate.negotiationStatus" />
														:
													</td>
													<td width="21%" class="greybox2wk" id="statuslabl">
														<s:textfield name="status" value="%{status}"
															id="negotiationStatus" cssClass="selectwk"
															readonly="true" />
													</td>
													<td width="15%" class="greyboxwk">
														<s:text name="estimate.executing.department" />
														:
													</td>
													<td width="53%" class="greybox2wk">
														<s:select headerKey="-1"
															headerValue="%{getText('estimate.default.select')}"
															name="execDept" id="executingDepartment"
															cssClass="selectwk"
															list="dropdownData.executingDepartmentList" listKey="id"
															listValue="deptName" value="%{execDept}" />
													</td>
													<s:if test="%{negoCreatedBy=='no'}">
														<script>disableDept();</script>
													</s:if>
												</tr>
											</s:elseif>
											<s:else>
												<tr>
													<td width="8%" class="greyboxwk">
														<s:if test="%{source=='searchEstimateForMilestone' || source=='viewMilestone' 
															|| source=='cancelMilestone'}">
															<s:text name="estimate.search.projectcode" />
														</s:if>
														<s:else>
															<s:text name="estimate.search.estimateStatus" />
														</s:else>
														:
													</td>
													
													<td width="15%" class="greybox2wk">
														<s:if test="%{source=='searchEstimateForMilestone' || source=='viewMilestone'}">
															<s:textfield name="projCode"
															value="%{projCode}" id="projCode"
															cssClass="selectwk" />
														</s:if>
														<s:elseif test="%{source=='cancelMilestone'}">
															
								        						<div class="yui-skin-sam">
								        							<div id="projectCodeSearch_autocomplete">
							                							<div> 
								        									<s:textfield id="projectCodeSearch" name="projCode" value="%{projCode}" cssClass="selectwk" />
								        								</div>
								        								<span id="projectCodeSearchResults"></span>
								        							</div>		
								        						</div>
								        						<egov:autocomplete name="projectCodeSearch" width="15" field="projectCodeSearch" 
								        							url="../milestone/ajaxMilestone!searchProjectCodeForMileStone.action?" queryQuestionMark="false" 
								        							results="projectCodeSearchResults"  handler="projectCodeSearchSelectionHandler" queryLength="3" />
														</s:elseif>
														<s:else>
															<s:select id="status" name="status" headerKey="-1"
																headerValue="ALL" cssClass="selectwk"
																list="%{estimateStatuses}" listKey="code"
																listValue="description" />
														</s:else>	
													</td>

													<s:if test="%{source=='financialdetail'}">
														<script>disableSelect();</script>
													</s:if>
													<s:if test="%{source=='technical sanction'}">
														<script>disableSelect();</script>
													</s:if>
													<s:if test="%{source=='Financial Sanction'}">
														<script>disableSelect();</script>
													</s:if>
													<s:if test="%{source=='AdministrativeSanction'}">
														<script>disableSelect();</script>
													</s:if>
													<s:if test="%{source=='cancelEstimate'}">
														<script>disableSelect();</script>
													</s:if>
													<td width="15%" class="greyboxwk">
														<s:if test="%{source=='searchEstimateForMilestone' || source=='viewMilestone' 
															|| source=='cancelMilestone'}">
															<span class="mandatory">*</span>
														</s:if>
														<s:text name="estimate.executing.department" />
														:
													</td>
													<td width="53%" class="greybox2wk">
														<s:if test="%{source=='cancelMilestone'}">
															<s:select headerKey="-1"
																headerValue="%{getText('estimate.default.select')}"
																name="execDept" id="executingDepartment"
																cssClass="selectwk"
																list="dropdownData.executingDepartmentList" listKey="id"
																listValue="deptName" value="%{execDept}"
																onChange="populateDesignation1();" />
															<egov:ajaxdropdown id="assignedTo1"fields="['Text','Value']" 
																dropdownId="assignedTo1" url="workorder/ajaxWorkOrder!getDesignationByDeptId.action" />
															<egov:ajaxdropdown id="assignedTo2"fields="['Text','Value']" 
																dropdownId="assignedTo2" url="workorder/ajaxWorkOrder!getDesignationByDeptId.action" />
														</s:if>
														<s:else>															
															<s:select headerKey="-1"
																headerValue="%{getText('estimate.default.select')}"
																name="execDept" id="executingDepartment"
																cssClass="selectwk"
																list="dropdownData.executingDepartmentList" listKey="id"
																listValue="deptName" value="%{execDept}"
																onChange="setupPreparedByList(this);" />
															<egov:ajaxdropdown id="preparedBy"
																fields="['Text','Value','Designation']"
																dropdownId='preparedBy' optionAttributes='Designation'
																url='estimate/ajaxEstimate!usersInExecutingDepartment.action' />
														</s:else>
													</td>
													<s:if test="%{source=='wp'}">
														<script>disableDept();</script>
													</s:if>
												</tr>
											</s:else>
											<tr>
												<td width="11%" class="whiteboxwk">
													<s:text name="estimate.search.estimateNo" />
													:
												</td>
												<td width="21%" class="whitebox2wk">
												  <s:if test="%{source=='cancelMilestone'}">
					        						<div class="yui-skin-sam">
					        							<div id="estimateNumberSearch_autocomplete">
				                							<div>
					        									<s:textfield id="estimateNumberSearch" name="estimatenumber" 
					        										value="%{estimatenumber}" cssClass="selectwk" />
					        								</div>
					        								<span id="estimateNumberSearchResults"></span>
					        							</div>	
					        						</div>
					        						<egov:autocomplete name="estimateNumberSearch" width="20" 
					        							field="estimateNumberSearch" url="../milestone/ajaxMilestone!searchEstimateForMileStone.action?" 
					        							queryQuestionMark="false" results="estimateNumberSearchResults" 
					        							handler="estimateNumberSearchSelectionHandler" queryLength="3"/>
					        					  </s:if>
					        					   <s:elseif test="%{source=='cancelEstimate'}">
					        						<div class="yui-skin-sam">
					        							<div id="estimateNumberSearch_autocomplete">
				                							<div>
					        									<s:textfield id="estimateNumberSearch" name="estimatenumber" 
					        										value="%{estimatenumber}" cssClass="selectwk" />
					        								</div>
					        								<span id="estimateNumberSearchResults"></span>
					        							</div>	
					        						</div>
					        						<egov:autocomplete name="estimateNumberSearch" width="20" 
					        							field="estimateNumberSearch" url="ajaxEstimate!searchEstimateNumber.action?" 
					        							queryQuestionMark="false" results="estimateNumberSearchResults" 
					        							handler="estimateNumberSearchSelectionHandler" queryLength="3"/>
					        					  </s:elseif>
					        					  <s:else>
													<s:textfield name="estimatenumber" value="%{estimatenumber}" id="location"
														cssClass="selectwk" />
												  </s:else>
												</td>
												<td width="15%" class="whiteboxwk">
													<s:text name="estimate.work.nature" />
													:
												</td>
												<td width="53%" class="whitebox2wk">
													<s:select headerKey="-1" headerValue="ALL"
														name="expenditureType" id="type" cssClass="selectwk"
														list="dropdownData.typeList" listKey="id" listValue="name"
														value="%{expenditureType}" />
												</td>
												<s:hidden name="expenditureTypeid"
													value="%{expenditureType}"></s:hidden>
											</tr>
											<tr>
												<td class="greyboxwk">
													Estimate Date From:
												</td>
												<td class="greybox2wk">
													<s:date name="fromDate" var="fromDateFormat"
														format="dd/MM/yyyy" />
													<s:textfield name="fromDate" id="fromDate"
														cssClass="selectwk" value="%{fromDateFormat}"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a
														href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
														onmouseover="window.status='Date Picker';return true;"
														onmouseout="window.status='';return true;"> <img
															src="${pageContext.request.contextPath}/image/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>

												</td>
												<td width="17%" class="greyboxwk">
													Estimate Date To:
												</td>
												<td width="17%" class="greybox2wk">
													<s:date name="toDate" var="toDateFormat"
														format="dd/MM/yyyy" />
													<s:textfield name="toDate" id="toDate"
														value="%{toDateFormat}" cssClass="selectwk"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a
														href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
														onmouseover="window.status='Date Picker';return true;"
														onmouseout="window.status='';return true;"> <img
															src="${pageContext.request.contextPath}/image/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>
												</td>
											</tr>
											<tr>
												<td class="whiteboxwk">
													<s:text name="estimate.work.type" />
													:
												</td>
												<td class="whitebox2wk">
													<s:select headerKey="-1" headerValue="ALL"
														name="parentCategory" id="parentCategory"
														cssClass="selectwk" list="dropdownData.parentCategoryList"
														listKey="id" listValue="description"
														value="%{parentCategory.id}"
														onChange="setupSubTypes(this);" />
													<egov:ajaxdropdown id="categoryDropdown"
														fields="['Text','Value']" dropdownId='category'
														url='estimate/ajaxEstimate!subcategories.action'
														selectedValue="%{category.id}" />
												</td>

												<td class="whiteboxwk">
													<s:text name="estimate.work.subtype" />
													:
												</td>
												<td class="whitebox2wk">
													<s:select headerKey="-1" headerValue="ALL" name="category"
														value="%{category.id}" id="category" cssClass="selectwk"
														list="dropdownData.categoryList" listKey="id"
														listValue="description" />
												</td>
											</tr>
											<s:hidden name="wpdate" id="wpdate" value="%{wpdate}"></s:hidden>
											<s:if test="%{source!='createNegotiationNew' && source!='SearchEstimateforWO' 
												&& source!='searchEstimateForMilestone' && source!='viewMilestone' 
												&& source!='cancelMilestone'}">
												<tr>
													<td class="greyboxwk">
														<s:text name="estimate.preparedBy" />
														:
													</td>
													<td class="greybox2wk">
														<s:select headerKey="-1"
															headerValue="%{getText('estimate.default.select')}"
															name="empId" value="%{empId}" id="preparedBy"
															cssClass="selectwk" list="dropdownData.preparedByList"
															listKey="id" listValue="employeeName" />

													</td>

													<td class="greyboxwk">
														<s:text name="estimate.description" />
														:
													</td>
													<td class="greybox2wk">
														<s:textfield name="description" value="%{description}"
															id="description" cssClass="selectwk" />
													</td>
												</tr>
											</s:if>
											<s:elseif test="%{source=='searchEstimateForMilestone' || source=='viewMilestone' 
													|| source=='cancelMilestone'}">
    											<tr>
   													<td class="greyboxwk">
   														<s:text name="milestone.search.allocated.to1"/> :
													</td>
													<td class="greybox2wk">
														<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="assignedTo1" 
	         												id="assignedTo1" value="%{assignedTo1}" cssClass="selectwk" 
	         												list="dropdownData.assignedToList" listKey="designationId" listValue="designationName" onchange="populateUser1(this)"/>
	         											<egov:ajaxdropdown id="engineerIncharge" fields="['Text','Value']" dropdownId='engineerIncharge' 
		        	  										url='workorder/ajaxWorkOrder!getUsersForDesg.action'/>
													</td>	
        											<td class="greyboxwk">
        												<s:text name="milestone.search.user1"/> :
        											</td>
        											<td class="greybox2wk">
        												<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="engineerIncharge" 
	         												id="engineerIncharge"  cssClass="selectwk" 
	         												list="dropdownData.assignedUserList1" listKey="id" listValue="employeeName" value="%{engineerIncharge}"/>
        											</td>
   												</tr>
											   <tr>
   													<td class="whiteboxwk">
   			 											<s:text name="milestone.search.allocated.to2"/> :
													</td>
													<td class="whitebox2wk">
														<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="assignedTo2" 
													        id="assignedTo2" value="%{assignedTo2}" cssClass="selectwk" 
	         												list="dropdownData.assignedToList" listKey="designationId" listValue="designationName" onchange="populateUser2(this)"/>
	         											<egov:ajaxdropdown id="engineerIncharge2" fields="['Text','Value']" dropdownId='engineerIncharge2' 
		        	  										url='workorder/ajaxWorkOrder!getUsersForDesg.action'/>
													</td>	
        											<td class="whiteboxwk">
        												<s:text name="milestone.search.user2"/> :
        											</td>
        											<td class="whitebox2wk">
        												<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="engineerIncharge2" 
	         												id="engineerIncharge2"  cssClass="selectwk" 
	         												list="dropdownData.assignedUserList2" listKey="id" listValue="employeeName" value="%{engineerIncharge2}"/>
        											</td>

   												</tr>
   												<s:if test="%{source=='searchEstimateForMilestone' || source=='viewMilestone'}">
	   												<tr>
	   													<td class="greyboxwk">
	   															<s:text name="estimate.search.workOrderNo" />
	   													</td>
	   															<td class="greybox2wk" >
			   															<s:textfield name="workOrderNo"
																			value="%{workOrderNo}" id="workOrderNo"
																			cssClass="selectwk" />
			   													</td>
			   													<td class="greyboxwk" width="11%"><s:text name="label.preparedBy" /> : </td>
																<td class="greybox2wk" width="21%">
																					<s:select headerKey="-1" 
																						headerValue="%{getText('estimate.default.select')}"
																						name="empId" value="%{empId}" id="preparedBy"
																						cssClass="selectwk" list="dropdownData.preparedByList"
																						listKey="id" listValue="employeeName" />
																</td>
													</tr>
	   											</s:if>
	   											<s:if test="%{source=='viewMilestone'}">	
	   													<tr>
	   													
		   													<td width="11%" class="whiteboxwk"><s:text name="estimate.ward" />:</td>
																<td width="21%" class="whitebox2wk">
																
																<div class="yui-skin-sam">
																<div id="wardSearch_autocomplete">
																<div><s:textfield id="wardSearch" type="text" name="wardName" value="%{ward.parent?(ward.parent?ward.name+'('+ward.parent.name+')':''):(ward.name?ward.name:'')}" onBlur="clearHiddenWardId(this)" class="selectwk"/><s:hidden id="wardID" name="ward" value="%{ward.id}"/></div>
																<span id="wardSearchResults"></span>
																</div>
																</div>
																<egov:autocomplete name="wardSearch" width="20" field="wardSearch" url="wardSearch!searchAjax.action?" queryQuestionMark="false" results="wardSearchResults" handler="wardSearchSelectionHandler" forceSelectionHandler="wardSelectionEnforceHandler"  paramsFunction="jurisdictionSearchParameters" queryLength="3" />
																<span class='warning' id="improperWardSelectionWarning"></span>
																
																</td> 
																<td class="whiteboxwk" colspan="2"></td>
																																
	   													</tr>
	   													
	   													<tr>
	   														<td width="15%" class="greyboxwk"><s:text name="milestone.work.status" /> :</td>
															<td id="msStatMain" class="greybox2wk" colspan="3">
															
																	<s:select id="milestoneStatus" name="milestoneStatus" headerKey="-1"
																			headerValue="ALL" cssClass="selectwk"
																			list="dropdownData.msStatusList" onchange="setupInnerStatuses(this);" />
															</td>
															<td id="msStatusLbl" class="greyboxwk" style="display:none"></td>        
															<td id="msStatusVals" class="greybox2wk" style="display:none" >
																
																<s:select id="status2" name="status2" headerKey="-1"
																			headerValue="ALL" cssClass="selectwk"
																			list="dropdownData.statusList" />
									         				</td>   
	   													</tr>
	   												</s:if>
	   												
												
												<s:if test="%{source=='cancelMilestone'}">
													<tr>
	   													<td class="greyboxwk">
	   															<s:text name="milestone.search.status" /> :
	   													</td>
	   													<td class="greybox2wk" colspan="3">
	   															<s:textfield name="status"
																	value="%{status}" id="status"
																	cssClass="selectwk" readonly="true"/>
	   													
	   													</td>
	   												</tr>
	   												<tr>
														<td class="whiteboxwk">
															<s:text name="estimate.search.workOrderFlag" /> :
														</td>
	   													<td class="whitebox2wk">
   															<s:checkbox name="checkWO" id="checkWO" 
																onClick="enableWONumber(this);" />
	   													</td>
	   													<td class="whiteboxwk">
	   													<span id="cancelWO1Dtls">
	   														<span class="mandatory">*</span>
		   													<s:text name="estimate.search.workOrderNo" /> :
		   												</span>
		   												</td>
		   												
		   												<td class="whitebox2wk">
		   												<span id="cancelWO2Dtls">
		   												<div class="yui-skin-sam">
						        							<div id="workOrderNumberSearch_autocomplete">
						        								<div>
		   															<s:textfield name="workOrderNo" value="%{workOrderNo}" 
		   																id="workOrderNumberSearch" cssClass="selectwk" />
		   														</div>
		   														<span id="workOrderNumberSearchResults"></span>
		   													</div>
		   												</div>
		   												<egov:autocomplete name="workOrderNumberSearch" width="20" 
		   													field="workOrderNumberSearch" url="../milestone/ajaxMilestone!searchWorkOrdNumForMileStone.action?" 
		   													queryQuestionMark="false" results="workOrderNumberSearchResults" 
		   													handler="workOrderNumberSearchSelectionHandler" queryLength="3" />
		   												</span>
		   												</td>
	   												</tr>	   													
												</s:if>
												
											</s:elseif>
											<s:if test="%{source=='cancelEstimate'}">
												<tr>
													<td width="8%" class="whiteboxwk">														
														<s:text name="estimate.search.projectcode" />
													</td>
													<td class="whitebox2wk" colspan="3">		
						        						<div class="yui-skin-sam">
						        							<div id="projectCodeSearch_autocomplete">
					                							<div> 
						        									<s:textfield id="projectCodeSearch" name="projCode" value="%{projCode}" cssClass="selectwk" />
						        								</div>
						        								<span id="projectCodeSearchResults"></span>
						        							</div>		
						        						</div>
						        						<egov:autocomplete name="projectCodeSearch" field="projectCodeSearch" 
						        							url="ajaxEstimate!searchProjectCodes.action?" queryQuestionMark="false" 
						        							results="projectCodeSearchResults"  handler="projectCodeSearchSelectionHandler" queryLength="3" />
													</td>
												</tr>
											</s:if>
											
											<tr>
												<td colspan="4" class="shadowwk"></td>
											</tr>
											
											<tr>
												<td colspan="4">
													<div  class="buttonholderwk" align="center">
														<s:submit cssClass="buttonadd" value="SEARCH"
															id="saveButton" name="button"
															onClick="return validateSearch();enableSelect()" method="search"/>
														<input type="button" class="buttonfinal" style="display:none;"
															clcssClass="buttonadd" value="PRINT" id="printButton"
															name="button" onclick="return checkPrint()" />
													</div>
												</td>
											</tr>
											<div class="errorstyle" id="error_search"
												style="display: none;"></div>

											<s:hidden id="estimateId" name="estimateId" />
											<s:hidden id="source" name="source" />
											<s:hidden id="estimateNumber" name="estimateNumber" /> 
											<s:hidden id="pcStatus" name="pcStatus" /> 
											<s:hidden id="pcCode" name="pcCode" />
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">
												<tr>
													
												</tr>
												<tr><td colspan="4">&nbsp;</td></tr>
												<tr id="resultRow" ><td colspan="4">												
													<s:if test="%{source=='financialdetail'}">
														<tr>
															<td><%@ include file='estimate-list.jsp'%></td>
														</tr>
														<tr>
															<td>
																<div class="buttonholderwk">
																	<input type="button" class="buttonadd"
																		value="Update Financial Details " id="addButton"
																		name="updateFinancialDetailButton"
																		onclick="addFinancial();" align="center" />
																</div>
															</td>
														</tr>
													</s:if>
													<s:elseif test="%{source=='wp'}">
														<%@ include file='estimateWP-list.jsp'%>
													</s:elseif>
													<s:elseif
														test="%{source=='createNegotiationNew' || source=='SearchEstimateforWO'}">
														<tr>
															<td><%@ include file='estimateNewSearch-list.jsp'%></td>
														</tr>
														<tr>
															<td>
																<div class="buttonholderwk">
																	<s:if test="%{source=='createNegotiationNew'}">
																		<input type="button" class="buttonadd"
																			value="Create Negotiation Statement " id="addButton"
																			name="createNegotiationButton"
																			onclick="createNegotiation();" align="center" />
																	</s:if>
																	<s:elseif test="%{source=='SearchEstimateforWO'}">
																		<input type="button" class="buttonadd"
																			value="Create Work Order " id="addButton"
																			name="createWorkOrderButton" onclick="createWO();"
																			align="center" />
																	</s:elseif>
	
																</div>
															</td>
														</tr>
													</s:elseif>
													<s:elseif test="%{source=='searchEstimateForMilestone' || source=='viewMilestone'}" >
														<tr>
															<%@ include file='estimateListForMilestone.jsp'%>
														</tr>
														<tr>
															<td>&nbsp;</td>
														</tr>
														<s:if test="%{searchResult.fullListSize != 0 && !hasErrors()}">
															<tr>
																<td>
																	<div class="buttonholderwk">
																		<input type="button" class="buttonadd"
																			value="Create Milestone" id="createMilestoneButton"
																			name="createMilestoneButton"
																			onclick="createMilestone();" align="center" />
																		<input type="button" class="buttonadd"
																			value="Track Milestone " id="trackMilestoneButton"
																			name="trackMilestoneButton" onclick="trackMilestone();"
																			align="center" />
																		<input type="button" class="buttonadd"
																			value="View Milestone " id="viewMilestoneButton"
																			name="viewMilestoneButton" onclick="viewMilestone();"
																			align="center" />
																	</div>
																</td>
															</tr>
																<s:if test="%{source=='viewMilestone'}">
																	<script>disableCreateTrackMilestone();</script>
																</s:if>
																<s:else>
																	<script>enableCreateTrackMilestone();</script>
																</s:else>
														</s:if>
													</s:elseif>
													
													<s:elseif test="%{source=='cancelMilestone'}">
														<script type="text/javascript">
																	window.history.forward(1);
														</script>
														<tr>
														<%@ include file='estimateListForCancelMilestone.jsp'%>
														</tr>
														<tr>
															<td>&nbsp;</td>
														</tr>
														<tr>												   
														  <td align="left" class="whitebox2wk">
															<b><span class="mandatory">*</span><s:text name="cancellation.reason" />:</b>
															<s:select id="cancellationReason" name="cancellationReason" cssClass="selectwk" 
																list="#{'Select':'Select','DATA ENTRY MISTAKE':'DATA ENTRY MISTAKE','OTHER':'OTHER'}" 
																onChange="toggleCancelMSRemarks(this)" />
															<span id="cancelMSRemarksDtls" style="display:none">
															<b><span class="mandatory">*</span><s:text name="cancel.remarks" />:</b>
															<s:textarea id="cancelRemarks" name="cancelRemarks" rows="2" cols="35" />
															</span> 
														  </td>
														</tr> 
														<tr>
															<td colspan="4">
																<div class="buttonholderwk">
																<input type="button" class="buttonadd" value="Cancel MileStone" 
																	id="cancelMSButton" name="cancelMS" onclick="saveCancelApprovedMS()"
																	align="center" />
																<input type="button" class="buttonfinal" value="CLOSE"
																id="closeButton" name="button" onclick="window.close();" />
																</div>
															</td>
														</tr>
													</s:elseif>
								
													<s:else>
													<script>enableprintButton();</script>
													<%@ include file='estimate-list.jsp'%>
													</s:else>
												</td>
												</tr>																							
											</table>
											
										</table>
									</td>
								</tr>
							</table>
						</div>

						<!-- end of rbroundbox2 -->
						<div class="rbbot2">
							<div></div>
						</div>
					</div>
					<!-- end of insidecontent -->
				</div>
				<!-- end of formmainbox -->
		</s:form>
	</body>
</html>
