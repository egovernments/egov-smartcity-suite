<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<%@ include file="/includes/taglibs.jsp" %>

<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
<html>
	<head>
		<title><s:text name='page.title.search.workorder' /></title>
	</head>
	
<script type="text/javascript">
jQuery("#loadingMask").remove();
 function validateAndSubmit(){
   if($F('status') == -1 && $F('contractorId') == -1 && $F('deptId') == -1
						&& $F('workOrderNumberSearch').blank()
						&& $F('fromDate').blank()
						&& $F('toDate').blank()
						&&$F('estimateNumberSearch').blank()
						&& $F('wpNumber').blank()
						&& $F('tenderFileNumber').blank()){
		
						var bool=false;
										
	if(!bool){
	$('workOrder_error').show();					
  	$('mandatory_error').show();
  	$('mandatory_length_error').hide();
  	return false;
  	}
  }
  
 
   if(!$F('workOrderNumberSearch').blank()  && $F('workOrderNumberSearch').length < 3){
		  	$('workOrder_error').show();
		  	$('mandatory_length_error').show();
		  	$('mandatory_error').hide();
		     return false;
	 }
	    $('workOrder_error').hide();
	    jQuery(".commontopyellowbg").prepend('<div id="loadingMask" style="display:none;overflow:none;scroll:none;" ><img src="/egi/images/bar_loader.gif"> <span id="message">Please wait....</span></div>')
		doLoadingMask();
	    document.workOrderForm.action='${pageContext.request.contextPath}/workorder/workOrder!searchWorkOrderDetails.action';
    	document.workOrderForm.submit();
    }
 function gotoPage(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'workorderId');
    var workOrderStateId=getControlInBranch(currRow,'workOrderStateId');	
	var docNumber = getControlInBranch(currRow,'docNumber');
	var appDate = getControlInBranch(currRow,'appDate');
	var objNo = getControlInBranch(currRow,'objNo');
	var showActions = getControlInBranch(currRow,'showActions');
	if(showActions[1]!=null && obj.value==showActions[1].value){	
		window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+id.value+
		"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(showActions[2]!=null && obj.value==showActions[2].value){	
	
	window.open("${pageContext.request.contextPath}/workorder/workOrder!viewWorkOrderPdf.action?id="+id.value+
		"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');	
	}
	if(showActions[3]!=null && obj.value==showActions[3].value){
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+
		workOrderStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(showActions[4]!=null && obj.value==showActions[4].value){ 
		viewDocumentManager(docNumber.value);return false;
	}
	if(showActions[5]!=null && obj.value==showActions[5].value){ 
	window.open("${pageContext.request.contextPath}/tender/setStatus!edit.action?objectType=WorkOrder&objId="+
	id.value+"&setStatus="+dom.get('setStatus').value+"&appDate="+appDate.value+"&objNo="+objNo.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}		
	if(showActions[6]!=null && obj.value==showActions[6].value){
		var url = "${pageContext.request.contextPath}/workorder/workOrder!viewWorkOrderNotice.action?id="+id.value+"&mode=search";
		window.open(url, "", "width=980, height=650,scrollbars=yes,status=yes");
	}
}	
 function setworkorderId(elem){
	var currRow=getRow(elem);
	dom.get("workOrderId").value = elem.value;
	dom.get("woNumber").value = getControlInBranch(currRow,'objNo').value;
	dom.get("depWorksCategory").value = getControlInBranch(currRow,'dwCategory').value;
	dom.get("roadCutStartDate").value = getControlInBranch(currRow,'roadCutDate').value;
}

function gotoMB(){
var id = document.workOrderForm.workOrderId.value;
var category = dom.get("depWorksCategory").value;
var roadCutStDate = dom.get("roadCutStartDate").value;
if(id!=''){
	if(category =='BPA' && roadCutStDate == ''){
		dom.get("workOrder_error").innerHTML='<s:text name="dw.bpa.roadCut.startDate.check.MB" />';
	    dom.get("workOrder_error").style.display='';
	    return false;
	}
	else{
		window.open('${pageContext.request.contextPath}/measurementbook/measurementBook!newform.action?workOrderId='+id,'_self');
	}
}
else{
	dom.get("workOrder_error").style.display='';
	document.getElementById("workOrder_error").innerHTML='<s:text name="workorder.not.selected" />';
	return false;
  }
  dom.get("workOrder_error").style.display='none';
  document.getElementById("workOrder_error").innerHTML='';
}

function gotoBill(){
var id = document.workOrderForm.workOrderId.value;
if(id!=''){
	validateARFInWorkflow(id);
}
else{
	dom.get("workOrder_error").style.display='';
	document.getElementById("workOrder_error").innerHTML='<s:text name="contractorBill.workorder.not.selected" />';
	return false;
  }
  dom.get("workOrder_error").style.display='none';
  document.getElementById("workOrder_error").innerHTML='';
}

function validateARFInWorkflow(workOrderId) {
	makeJSONCall(["Value","EstimateNo","ARFNo","Owner","WOId"],'${pageContext.request.contextPath}/workorder/ajaxWorkOrder!advanceRequisitionInWorkflowCheck.action',{workOrderId:workOrderId},arfInWorkflowCheckSuccess,arfInWorkflowCheckFailure) ;
}

arfInWorkflowCheckSuccess = function(req,res){
	  results=res.results;
	  var checkResult='';
	  if(results != '') {
		checkResult = results[0].Value;
		workOrderId = results[0].WOId;
		estimateNo = results[0].EstimateNo;
		arfNo = results[0].ARFNo;
		owner = results[0].Owner;
	  }
  	if(checkResult != '' && checkResult=='valid'){
  		validateTrackMlsForBillCreation(workOrderId);
	}	
	else {
		dom.get("workOrder_error").innerHTML='<s:text name="arf.workflow.verification.for.bill.msg1" />'+arfNo+' <s:text name="arf.workflow.verification.for.bill.msg2" />'+estimateNo+' <s:text name="arf.workflow.verification.for.bill.msg3" />'+owner+' <s:text name="arf.workflow.verification.for.bill.msg4" />';
	    dom.get("workOrder_error").style.display='';
	    return false;
	}
	if(dom.get("workOrder_error").innerHTML='<s:text name="arf.workflow.verification.for.bill.msg1" />'+arfNo+' <s:text name="arf.workflow.verification.for.bill.msg2" />'+estimateNo+' <s:text name="arf.workflow.verification.for.bill.msg3" />'+owner+' <s:text name="arf.workflow.verification.for.bill.msg4" />')
	{
		dom.get("workOrder_error").innerHTML='';
	    dom.get("workOrder_error").style.display='none';
	}		
}

arfInWorkflowCheckFailure= function(){
    dom.get("workOrder_error").style.display='';
	document.getElementById("workOrder_error").innerHTML='<s:text name="arf.workflow.ajax.verification.for.bill.failed" />';
}

function validateTrackMlsForBillCreation(workOrderId)
{
	makeJSONCall(["Value","woId"],'${pageContext.request.contextPath}/workorder/ajaxWorkOrder!trackMilestoneForBillCreationCheck.action',{workOrderId:workOrderId},trackMlsCheckSuccess,trackMlsCheckFailuer) ;
}

function validateEstYearEndAppr(workOrderId)
{
	makeJSONCall(["Value","woId","estNo"],'${pageContext.request.contextPath}/workorder/ajaxWorkOrder!yearEndApprForBillCreationCheck.action',{workOrderId:workOrderId},yearEndApprCheckSuccess,yearEndApprCheckFailure) ;
}

function disableSelect(){
	document.getElementById('status').disabled=true
}

function enableSelect(){
	document.getElementById('status').disabled=false
}

function getApprovedMBs(workOrderId){
    makeJSONCall(["mbRefNo"],'${pageContext.request.contextPath}/workorder/ajaxWorkOrder!getApprovedMBsForWorkOrder.action',{workOrderId:workOrderId},mbLoadHandler,mbLoadFailureHandler) ;
}

mbLoadHandler = function(req,res){
  results=res.results;
  var mbRefNo=''
  if(results != '') {
  	for(var i=0; i<results.length;i++) {
  	if(mbRefNo!='')
		mbRefNo=mbRefNo+', MB#:'+results[i].mbRefNo;
	else
		mbRefNo=results[i].mbRefNo;
	}
  }
  	
	if(mbRefNo != ''){
		dom.get("workOrder_error").style.display='';
		document.getElementById("workOrder_error").innerHTML='<s:text name="cencelWO.bill.created.message"/>'+mbRefNo;
		window.scroll(0,0);
		return false;
	}	
	else {
		saveCancelWO();
	}	
}

mbLoadFailureHandler= function(){
    dom.get("workOrder_error").style.display='';
	document.getElementById("workOrder_error").innerHTML='<s:text name="cancel.wo.mb.ajax.failure"/>';
}

trackMlsCheckSuccess = function(req,res){
  results=res.results;
  var checkResult='';
  if(results != '') {
	checkResult =   results[0].Value;
	wordId =   results[0].woId;
  }
  	if(checkResult != '' && checkResult=='valid'){
		validateEstYearEndAppr(wordId);
	}	
	else {
		dom.get("workOrder_error").innerHTML='<s:text name="contactor.part.bill.milestone.ajaxmsg" />';
	    dom.get("workOrder_error").style.display='';
	    return false;
	}
	if(dom.get("workOrder_error").innerHTML=='<s:text name="contactor.part.bill.milestone.ajaxmsg" />')
	{
		dom.get("workOrder_error").innerHTML='';
	    dom.get("workOrder_error").style.display='none';
	}		
}

yearEndApprCheckSuccess = function(req,res){
	  results=res.results;
	  var checkResult='';
	  if(results != '') {
		checkResult =   results[0].Value;
		workOrderId =   results[0].woId;
		estNum =   results[0].estNo;
	  }
		if(checkResult != '' && checkResult=='valid'){
			window.open('${pageContext.request.contextPath}/contractorBill/contractorBill!newform.action?workOrderId='+workOrderId	,'_self');
		}	
		else {
			dom.get("workOrder_error").innerHTML='<s:text name="yrEnd.appr.verification.for.bill.msg1" />'+estNum+'<s:text name="yrEnd.appr.verification.for.bill.msg2" />';
		    dom.get("workOrder_error").style.display='';
		    return false;
		}
		if(dom.get("workOrder_error").innerHTML=='<s:text name="yrEnd.appr.verification.for.bill.msg1" />'+estNum+'<s:text name="yrEnd.appr.verification.for.bill.msg2" />')
		{
			dom.get("workOrder_error").innerHTML='';
		    dom.get("workOrder_error").style.display='none';
		}		
}

trackMlsCheckFailuer= function(){
    dom.get("workOrder_error").style.display='';
	document.getElementById("workOrder_error").innerHTML='<s:text name="milestone.ajax.verification.for.bill.failed" />';
}

yearEndApprCheckFailure= function(){
    dom.get("workOrder_error").style.display='';
	document.getElementById("workOrder_error").innerHTML='<s:text name="yrEnd.appr.verification.for.bill.failed" />';
}

function checkValidMileStonesForWO() {
	var woId = document.workOrderForm.workOrderId.value; 	
	 <s:iterator value="workOrderList" status="row_status1">	
		if('<s:property value="%{id}"/>'==woId){
			<s:iterator value="workOrderEstimates" status="row_status2">			
				<s:iterator value="milestone" status="row_status3">	
				var mileStoneStatus = '<s:property value="%{egwStatus.code}"/>';
				if(mileStoneStatus != 'CANCELLED'){		
					dom.get("workOrder_error").style.display='';
					document.getElementById("workOrder_error").innerHTML='<s:text name="cencelWO.milestone.created.message"/>';
					window.scroll(0,0);
					return false;							
				}				
				</s:iterator>
			</s:iterator>
		}
	</s:iterator>
	return true;	
}

function checkValidREsForWO() {
	var woId = document.workOrderForm.workOrderId.value; 
	var revsionEstimateNo = '';	
	 <s:iterator value="workOrderList" status="row_status1">	
		if('<s:property value="%{id}"/>'==woId){
			<s:iterator value="revisionWOs" status="row_status2">			
				<s:iterator value="workOrderEstimates" status="row_status3">	
				var revsionEstimateStatus = '<s:property value="%{estimate.egwStatus.code}"/>';
				if(revsionEstimateStatus != 'CANCELLED'){
				  	if(revsionEstimateNo!='')
				  		revsionEstimateNo=revsionEstimateNo+', RE#:'+'<s:property value="%{estimate.estimateNumber}"/>';
					else
						revsionEstimateNo='<s:property value="%{estimate.estimateNumber}"/>';
				}				
				</s:iterator>
			</s:iterator>
		}
	</s:iterator>
	if(revsionEstimateNo != '') {
		dom.get("workOrder_error").style.display='';
		document.getElementById("workOrder_error").innerHTML='<s:text name="cencelWO.re.created.message1"/> '+revsionEstimateNo+' <s:text name="cencelWO.re.created.message2"/>';
		window.scroll(0,0);
		return false;	
	}
	else		
		return true;	
}

function checkValidARFsForWO() {
	var woId = document.workOrderForm.workOrderId.value; 
	var arfNo = '';	
	var estimateNo = '';	
	 <s:iterator value="workOrderList" status="row_status1">	
		if('<s:property value="%{id}"/>'==woId){
			<s:iterator value="workOrderEstimates" status="row_status2">			
				<s:iterator value="contractorAdvanceRequisitions" status="row_status3">	
				var arfStatus = '<s:property value="%{status.code}"/>';
				var estimateStatus = '<s:property value="%{estimate.egwStatus.code}"/>';
				if(arfStatus != 'CANCELLED') {
				  	if(arfNo != '')
				  		arfNo = arfNo+', ARF#:'+'<s:property value="%{advanceRequisitionNumber}"/>';
					else
						arfNo = '<s:property value="%{advanceRequisitionNumber}"/>';
				}	
				
				if(estimateStatus != 'CANCELLED') {
				  	if(estimateNo != '') 
				  		estimateNo = estimateNo+', '+'<s:property value="%{estimate.estimateNumber}"/>';
					else
						estimateNo = '<s:property value="%{estimate.estimateNumber}"/>';
				}							
				</s:iterator>
			</s:iterator>
		}
	</s:iterator>
	if(arfNo != '') {
		dom.get("workOrder_error").style.display='';
		document.getElementById("workOrder_error").innerHTML='<s:text name="cancelWO.arf.created.message1"/> '+arfNo+' <s:text name="cancelWO.arf.created.message2"/> '+estimateNo+' <s:text name="cancelWO.arf.created.message3"/>';
		window.scroll(0,0);
		return false;	
	}
	else		
		return true;	
}

function saveCancelWO() {
	var id = document.workOrderForm.workOrderId.value; 	
	var cancellationReason = document.workOrderForm.cancellationReason.value; 
	var cancelRemarks = document.workOrderForm.cancelRemarks.value; 
	if(cancellationReason=='OTHER' && cancelRemarks == ''){
		dom.get("workOrder_error").style.display='';
		document.getElementById("workOrder_error").innerHTML='<s:text name="validate.cancel.wo.remarks"/>';
		window.scroll(0,0);
		return false;
	}	
	if(validateCancel()){
		doLoadingMask();
		window.open('${pageContext.request.contextPath}/workorder/workOrder!cancelApprovedWO.action?sourcepage=cancelWO&workOrderId='+id+'&cancelRemarks='+cancelRemarks+'&cancellationReason='+cancellationReason,'_self');
	}
	else
		return false;	
}

function cancelWorkOrder(){
	var id = document.workOrderForm.workOrderId.value;
	 	
	if(id!=''){	
		if(!checkValidMileStonesForWO())
			return false;	
		if(!checkValidREsForWO())
			return false;
		if(!checkValidARFsForWO())
			return false;
		getApprovedMBs(id);					
	}		
	else{
		dom.get("workOrder_error").style.display='';
		document.getElementById("workOrder_error").innerHTML='<s:text name="wo.cancel.not.selected" />';
		window.scroll(0,0);
		return false;
	  }
	  dom.get("workOrder_error").style.display='none';
	  document.getElementById("workOrder_error").innerHTML='';
	  if(dom.get("errorstyle")){
	  	dom.get("errorstyle").style.display='none';
	 	dom.get("errorstyle").innerHTML='';
	}
	 
}	

function validateCancel() {
	var msg='<s:text name="wo.cancel.confirm"/>';
	var woNo=dom.get("woNumber").value; 
	if(!confirmCancel(msg,woNo)) {
		return false;
	}
	else {
		return true;
	}
}

function toggleCancelRemarks(obj) { 
	if(obj.value=='OTHER') {
		document.getElementById("cancelRemarksDtls").style.display='';
	}
	else {
		document.getElementById("cancelRemarksDtls").style.display='none';
		dom.get("cancelRemarks").value='';
	}	
}

var estimateNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

var workOrderNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

</script>

	<body>
		<div class="errorstyle" id="searchEstimate_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="workOrderForm" 
			id="workOrderForm" theme="simple"
			onsubmit="return validateAndSubmit();enableSelect();">
			
			<div class="errorstyle" id="workOrder_error" style="display: none;">
			<span id="mandatory_error" style="display: none;"><s:text name="workorder.serach.mandatory.error" /></span>
			<span id="mandatory_length_error" style="display: none;"><s:text name="workorder.serach.mandatory.length.error" /></span>
		    </div>
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
								<s:hidden name="setStatus" id="setStatus" value="%{setStatus}" />
								<s:hidden name="sourcepage" id="sourcepage" value="%{sourcepage}" />
								<s:hidden id="workOrderId" name="workOrderId" />
								<s:hidden id="woNumber" name="woNumber" />
								<s:hidden id="depWorksCategory" name="depWorksCategory" />
								<s:hidden id="roadCutStartDate" name="roadCutStartDate" />
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td colspan="4" class="headingwk">
													<div class="arrowiconwk">
														<img
															src="/egworks/resources/erp2/images/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name="page.subheader.search.workorder" />
													</div>
												</td>
											</tr>
											
											<tr>
												<td class="whiteboxwk">
													<s:text name="workorder.search.status" />:
												</td>
												<td class="whitebox2wk">
												<s:if test="%{sourcepage.equals('searchWOForMBCreation') || sourcepage.equals('searchWOForBillCreation')}">
													<s:select id="status" name="status" 
															cssClass="selectwk"
															list="%{workOrderStatusesForMBCreation}" value="%{status}"
															listKey="code" listValue="description"
															/>
												</s:if>
												<s:elseif test="%{sourcepage.equals('cancelWO') }">												
													<s:select id="status" name="status" headerKey="-1"
															headerValue="--- Select ---" cssClass="selectwk"
															list="%{workOrderStatuses}" value="%{woStatus}"
															listKey="code" listValue="description"
															/>
															<script>disableSelect();														
																window.history.forward(1);
															</script>
												</s:elseif>
												<s:else>
													<s:select id="status" name="status" headerKey="-1"
															headerValue="--- Select ---" cssClass="selectwk"
															list="%{workOrderStatuses}" value="%{status}"
															listKey="code" listValue="description"
															/>
												</s:else>
												</td>
												<td class="whiteboxwk">
													<s:text name="workorder.search.workordernumber" />:
												</td>
												<s:if test="%{sourcepage.equals('cancelWO')}">	
													<td class="whitebox2wk" >
						        						<div class="yui-skin-sam">
						        							<div id="workOrderNumberSearch_autocomplete">
					                							<div> 
						        									<s:textfield id="workOrderNumberSearch" name="workOrderNumber" value="%{workOrderNumber}" cssClass="selectwk" />
						        								</div>
						        								<span id="workOrderNumberSearchResults"></span>
						        							</div>		
						        						</div>
						        						<egov:autocomplete name="workOrderNumberSearch" width="20" field="workOrderNumberSearch" url="ajaxWorkOrder!searchWorkOrderNumber.action?" queryQuestionMark="false" results="workOrderNumberSearchResults" handler="workOrderNumberSearchSelectionHandler" queryLength="3" />
							         				</td>
						         				</s:if>
						         				<s:else>						         				
											        <td class="whitebox2wk">
													 <s:textfield name="workOrderNumber"
														value="%{workOrderNumber}" id="workOrderNumberSearch"
														cssClass="selectwk" />
													</td>
						         				</s:else>
												
											</tr>
											<tr>
												<td class="greyboxwk">
													<s:text name="workorder.search.workorderFromdate" />:
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
															src="/egworks/resources/erp2/images/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>

												</td>
												<td class="greyboxwk">
													<s:text name="workorder.search.workorderTodate" />:
												</td>
												<td class="greybox2wk">
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
															src="/egworks/resources/erp2/images/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>
												</td>
											</tr>
											
											<tr>
												<td class="whiteboxwk">
													<s:text name="workorder.search.contractor" />:
												</td>
												
													<td colspan="3" class="whitebox2wk">
														<s:select id="contractorId" name="contractor"
															cssClass="selectwk"
															list="%{contractorForApprovedWorkOrder}"  headerKey="-1"
															headerValue="--- Select ---" value="%{contractor.id}" />
													</td>
												
											</tr>
											<s:if
													test="%{wOCreationForEstimateOrWP.toLowerCase().equals('workspackage') 
													|| wOCreationForEstimateOrWP.toLowerCase().equals('both')}">
												
											<tr>
													<td class="greyboxwk">
														<s:text name="tenderNegotiation.worksPackageNo" />
														:
													</td>

													<td class="greybox2wk">
														<s:textfield name="wpNumber" id="wpNumber"
															cssClass="selectboldwk" />
													</td>

													<td class="greyboxwk">
														<s:text name="estimate.tenderfilenumber" />
														:
													</td>
													<td class="greybox2wk">
														<s:textfield name="tenderFileNumber" id="tenderFileNumber"
															cssClass="selectboldwk" />
													</td>
											</tr>
											</s:if>
											<tr>
											<td class="whiteboxwk">
													<s:text name="workOrder.executingDepartment" />
													:
												</td>
												<td class="whitebox2wk">
												<s:if test="%{sourcepage.equals('searchWOForMBCreation') || sourcepage.equals('searchWOForBillCreation')}">
												<s:if test="%{dropdownData.deptListForMB.size()==1}">
												<s:select id="deptId" name="deptId"
														cssClass="selectwk" list="%{dropdownData.deptListForMB}"
														listKey="id" listValue="deptName"  />
												</s:if>
												<s:else>
												<s:select id="deptId" name="deptId"
														cssClass="selectwk" list="%{dropdownData.deptListForMB}"
														listKey="id" listValue="deptName" />
												</s:else>
												</s:if>
												<s:else>
												<s:select id="deptId" name="deptId"
														cssClass="selectwk" list="%{dropdownData.deptListForSearch}"
														listKey="id" listValue="deptName" headerKey="-1"
														headerValue="--- Select ---" />
												</s:else>
												</td>													
												<td class="whiteboxwk">
													<s:text name="estimate.number" />
													:
												</td>
												<s:if test="%{sourcepage.equals('cancelWO')}">	
												<td class="whitebox2wk" >
					        						<div class="yui-skin-sam">
					        							<div id="estimateNumberSearch_autocomplete">
				                							<div>
					        									<s:textfield id="estimateNumberSearch" name="estimateNumber" value="%{estimateNumber}" cssClass="selectwk" />
					        								</div>
					        								<span id="estimateNumberSearchResults"></span>
					        							</div>	
					        						</div>
					        						<egov:autocomplete name="estimateNumberSearch" width="20" field="estimateNumberSearch" url="ajaxWorkOrder!searchEstimateNumber.action?" queryQuestionMark="false" results="estimateNumberSearchResults" handler="estimateNumberSearchSelectionHandler" queryLength="3"/>
						         				</td>
						         				</s:if>
						         				<s:else>		
												<td class="whitebox2wk">
													<s:textfield name="estimateNumber" id="estimateNumberSearch" cssClass="selectboldwk" />
												</td>
												</s:else>
											</tr>
											<tr>
												<td colspan="4" class="shadowwk"></td>
											</tr>
											
											<tr>
												<td colspan="4">
													<div  class="buttonholderwk" align="center">
														<s:submit  cssClass="buttonadd" value="SEARCH"
															id="saveButton" name="button" onclick="enableSelect()"
															/>
														<input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="button" onclick="window.close();" />
													</div>
												</td>
											</tr>
											
											

										</table>
									</td>
							</tr>
							<tr>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
			
										<tr><td><%@ include file='woSearch-list.jsp'%></td></tr>
								</table>
							</tr>
							</table>
							<s:if test="%{sourcepage.equals('cancelWO')}"> 
								<tr>												   
								  <td align="left" class="whitebox2wk">
									<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="mandatory">*</span><s:text name="cancellation.reason" />:</b>&nbsp;&nbsp;
									<s:select id="cancellationReason" name="cancellationReason" cssClass="selectwk" list="#{'DATA ENTRY MISTAKE':'DATA ENTRY MISTAKE','OTHER':'OTHER'}" onChange="toggleCancelRemarks(this)" />
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span id="cancelRemarksDtls" style="display:none"><b><span class="mandatory">*</span><s:text name="cancel.remarks" />:</b>&nbsp;&nbsp;
									<s:textarea id="cancelRemarks" name="cancelRemarks" rows="2" cols="35" />
									</span> 
								  </td>
								</tr> 
								<tr>
									<td colspan="4">
										<div class="buttonholderwk">
										<input type="button" class="buttonadd"
										value="Cancel Work Order" id="addButton"
										name="cancelWO" onclick="cancelWorkOrder()"
										align="center" />
										</div>
									</td>
								</tr>
							</s:if>
							<s:if test="%{sourcepage.equals('searchWOForMBCreation')}">
							<P align="center">
								<input type="button" class="buttonadd"
									value="Create Abstract M Book" id="addButton"
									name="createWorkOrderButton" onclick="gotoMB()"
									align="center" />
							</P>
							</s:if>
							<s:if test="%{sourcepage.equals('searchWOForBillCreation')}">
							<P align="center">
								<input type="button" class="buttonadd"
									value="Create Contractor Bill" id="addButton"
									name="createContractorBillButton" onclick="gotoBill()"
									align="center" />
							</P>
							</s:if>
							
						</div>
						<!-- end of rbroundbox2 -->
						
					</div>
					<!-- end of insidecontent -->
				</div>
				<!-- end of formmainbox -->
		</s:form>
	</body>
</html>
