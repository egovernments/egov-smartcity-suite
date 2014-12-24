<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<title><s:text name="page.title.workorder" /></title> 

<body onload="spillOverWorks();getDefaults();load();disableForm();"> 

<script src="<egov:url path='js/works.js'/>"></script>

<script>
function getDefaults() {
	roundOffEstimateAmount();
	populateDetails();
	<s:if test="%{'no'.equals(createdBySelection) && model.id==null}">
	populateDesignation1();
	</s:if>
		
}

function spillOverWorks(){
	<s:if test="%{isSpillOverWorks}">
		if(dom.get("manual_workflow")!=null){
			dom.get("manual_workflow").style.display='none';
		}
	</s:if>
}

function populateUser1(obj){
	var elem=document.getElementById('department');
	deptId=elem.options[elem.selectedIndex].value;
	populateengineerIncharge({desgId:obj.value,executingDepartment:deptId})
}
function populateUser2(obj){
	var elem=document.getElementById('department');
	deptId=elem.options[elem.selectedIndex].value;
	populateengineerIncharge2({desgId:obj.value,executingDepartment:deptId})
}
function setupPreparedByList(elem){
	deptId=elem.options[elem.selectedIndex].value;
    populatepreparedBy({executingDepartment:deptId});
}
function populateDesignation1(){	
	if(dom.get("department").value!="" && dom.get("department").value!="-1"){
		populateassignedTo1({departmentName:dom.get("department").options[dom.get("department").selectedIndex].text})
		<s:if test="%{'yes'.equals(assignedToRequiredOrNot)}">
		populateassignedTo2({departmentName:dom.get("department").options[dom.get("department").selectedIndex].text})
		</s:if>
	}
	else {removeAllOptions(dom.get("assignedTo1"));
	removeAllOptions(dom.get("engineerIncharge"));
	<s:if test="%{'yes'.equals(assignedToRequiredOrNot)}">
	removeAllOptions(dom.get("assignedTo2")); removeAllOptions(dom.get("engineerIncharge2"));
	</s:if>
	}
}
function roundOffEstimateAmount() {
	roundOffEmdAmountDeposited();	
}

function roundOffEmdAmountDeposited()
{
	document.workOrderForm.workOrderAmount.value=roundTo(document.workOrderForm.workOrderAmount.value);
	document.workOrderForm.emdAmountDeposited.value=roundTo(document.workOrderForm.emdAmountDeposited.value);
	document.workOrderForm.securityDeposit.value=roundTo(document.workOrderForm.securityDeposit.value);
}
function populateDetails()
{
	if(dom.get("workOrderDate").value=='') {
		 <s:if test="%{model.workOrderDate==null}">
			document.workOrderForm.workOrderDate.value=getCurrentDate();
		 </s:if>
	}
	<s:if test="%{createdBySelection.toLowerCase().equals('no')}">
		dom.get('department').disabled=true;
		dom.get('preparedBy').disabled=true;
	</s:if>
	<s:else>
		dom.get('department').disabled=false;
		dom.get('preparedBy').disabled=false;
		
	</s:else>
	<s:if test="%{editableDate.toLowerCase().equals('yes')}">
		dom.get('workOrderDate').disabled=false;
		dom.get("dateHref2").style.display='';
	</s:if>
	<s:else>
		dom.get('workOrderDate').disabled=true;
		dom.get("dateHref2").style.display='none';
		
	</s:else>
}
function showHeaderTab(){
  var hiddenid = document.forms[0].id.value;
  document.getElementById('workorder_header').style.display=''; 
  setCSSClasses('detailsTab','Last');
  setCSSClasses('headerTab','First Active'); 
  hideDetailsTab();
 
}

function validateFields(){
	clearMessage('workOrder_error');
	if(!checkDates()) {
		window.scroll(0,0);
		return false
	}
	var currentState = document.getElementById('currentState');

	if(currentState!=null && (currentState.value=='' || currentState.value=='NEW' || currentState.value=='REJECTED')) {
		if(dom.get("defectLiabilityPeriod").value<=0) {
	    	dom.get("workOrder_error").style.display='';     
			dom.get("workOrder_error").innerHTML='<s:text name="defectLiabilityPeriod.validate"/>';
			dom.get("defectLiabilityPeriod").focus();
			window.scroll(0,0);
			return false
      	}
	}
	if(dom.get("assignedTo1").value=='-1'){
    	dom.get("workOrder_error").style.display='';     
		dom.get("workOrder_error").innerHTML='<s:text name="allocatedTo.notselected"/>';
		window.scroll(0,0);
		return false
    }
    if(dom.get("assignedTo1").value!='-1' && dom.get("assignedTo2")!=null && dom.get("assignedTo2").value!='-1' && 
    dom.get("engineerIncharge").value!='-1' && dom.get("engineerIncharge2")!=null && dom.get("engineerIncharge2").value!='-1' && 
    dom.get("assignedTo2").value==dom.get("assignedTo1").value && dom.get("engineerIncharge").value==dom.get("engineerIncharge2").value){
    	dom.get("workOrder_error").style.display='';     
		dom.get("workOrder_error").innerHTML='<s:text name="same.allocatedTo.selected"/>';
		window.scroll(0,0);
		return false
    }
    if(dom.get("assignedTo1").value!='-1' && dom.get("engineerIncharge").value=='-1'){
    	dom.get("workOrder_error").style.display='';     
		dom.get("workOrder_error").innerHTML='<s:text name="engineerIncharge1.notselected"/>';
		window.scroll(0,0);
		return false
    }
    if(dom.get("assignedTo2")!=null && dom.get("assignedTo2").value!='-1' && dom.get("engineerIncharge2")!=null && dom.get("engineerIncharge2").value=='-1'){
    	dom.get("workOrder_error").style.display='';     
		dom.get("workOrder_error").innerHTML='<s:text name="engineerIncharge2.notselected"/>';
		window.scroll(0,0);
		return false
    }
    if(!checkDateFormat(dom.get("workOrderDate"))){
 		dom.get("workOrder_error").innerHTML='<s:text name="invalid.fieldvalue.workOrderDate"/>'; 
        dom.get("workOrder_error").style.display='';
        window.scroll(0,0);
	 	return false;
 	}
 	if(!checkDateFormat(dom.get("workCompletionDate"))){
  		dom.get("workOrder_error").innerHTML='<s:text name="invalid.fieldvalue.workCompletionDate"/>'; 
         dom.get("workOrder_error").style.display='';
         window.scroll(0,0);
 	 	return false;
 	}
 	<s:if test="%{noOfTenderResponses > 1}">
 	 if(woDataTable.getRecordSet().getLength()<1){
 	 	dom.get("workOrder_error").innerHTML='<s:text name="WorkOrder.activity.required"/>'; 
        dom.get("workOrder_error").style.display='';
        window.scroll(0,0);
	 	return false;
 	 }
	 else{
 	 	for(var i=0;i<woDataTable.getRecordSet().getLength();i++){
 	 	var recId=woDataTable.getRecord(i).getId();
 	 		if(!validateApprovedQtyNew(dom.get('approvedQuantity'+recId),recId,woDataTable.getColumn('approvedQuantity'))){
 	 			window.scroll(0,0);
	 			return false;
 	 		}
 	 	}
 	 }
 	 </s:if> 
 		 
 	if(dom.get("workOrderAmount").value<=0){
 	 	dom.get("workOrder_error").innerHTML='<s:text name="workorder.workOrderamount.zeroNotAllowed"/>'; 
        dom.get("workOrder_error").style.display='';
        window.scroll(0,0);
 	 	return false;
 	 }
 	 
 	if(dom.get("workOrder_error")!=null && (dom.get("workOrder_error").style.display!='none' && dom.get("workOrder_error").style.display=='')){
 	 	window.scroll(0,0);
 	 	return false;
	}
 	clearMessage('workOrder_error');
	links=document.forms[0].getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++) {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    
    if(errors) {
        dom.get("workOrder_error").style.display='';
    	document.getElementById("workOrder_error").innerHTML='<s:text name="sor.validate_x.message" />';
    	return;
    }

    dom.get("workOrder_error").style.display='none';     
	dom.get("workOrder_error").innerHTML='';
	enableSelect();
	return true;
}
function isvalidFormat(obj)
 {
 	if(!checkDateFormat(obj)){
 		dom.get("workOrder_error").innerHTML='<s:text name="invalid.fieldvalue.workOrderDate"/>'; 
        dom.get("workOrder_error").style.display='';
        window.scroll(0,0);
	 	return false;
 	}
 	dom.get("workOrder_error").style.display='none';
	dom.get("workOrder_error").innerHTML='';
	return true;
 }
function hideHeaderTab(){
  document.getElementById('workorder_header').style.display='none';
  }

function hideDetailsTab(){
  document.getElementById('workorder_details').style.display='none';
}

function showDetailsTab(){ 
  document.getElementById('workorder_details').style.display='';
  document.getElementById('detailsTab').setAttribute('class','Active');
  document.getElementById('detailsTab').setAttribute('className','Active');
  hideHeaderTab();
 
  setCSSClasses('detailsTab','Last Active ActiveLast');
  setCSSClasses('headerTab','First BeforeActive');
  
}

function setCSSClasses(id,classes){
    document.getElementById(id).setAttribute('class',classes);
    document.getElementById(id).setAttribute('className',classes);
}

function enableSelect(){    
   	for(i=0;i<document.workOrderForm.elements.length;i++){
      document.workOrderForm.elements[i].disabled=false;
	}
}

function validateCancel() {
	var msg='<s:text name="wo.cancel.confirm"/>';
	var estNo='<s:property value="model.workOrderNumber"/>'; 
	if(!confirmCancel(msg,estNo)) {
		return false;
	}
	else {
		return true;
	}
}

function validateForm(){
	if(!validateFields()){
	  return false;
	}
	return true;
}
function trim(str) {
        return str.replace(/^\s+|\s+$/g,"");
}

function validate(text){
	if(document.getElementById("actionName").value=='Cancel'){
		if(!validateCancel()){
			return false;
		}
	}
	if(!validateFields())
	  return false;
	if(text!='Approve' && text!='Reject' ){
		if(!validateWorkFlowApprover(text, "workOrder_error"))
			return false;
	}
	if(text=='Reject' ){
	var remarks=document.getElementById("approverComments").value;
		if(trim(remarks)==""){
			dom.get("workOrder_error").style.display='';
    		document.getElementById("workOrder_error").innerHTML='<s:text name="workorder.remarks.null" /><br>';
    		return false;
		}
	}
	enableSelect(); 
	return true;
}



function checkDates(){
	sdate = document.getElementById('workOrderDate').value;
	edate = document.getElementById('workCompletionDate').value;
	var woCompObj=document.getElementById('workCompletionDate');
	if(compareDate(formatDate6(sdate),formatDate6(edate)) == -1)
	{
		dom.get("workOrder_error").style.display='';
		document.getElementById("workOrder_error").innerHTML='<s:text name="workOrder.workCompletionDate.validate" /><br/>';
		woCompObj.focus();
		return false;
	}
	else {
		return true;
	} 
 }
 
 
function loadDesignationFromMatrix(){ 
	//var dept = dom.get('approverDepartment').value;
	var dept=dom.get('departmentName').value;
  		 var currentState = dom.get('currentState').value;
  		 var amountRule =  dom.get('amountRule').value;
	var additionalRuleValue =  dom.get('additionalRuleValue').value; 
	//var currentDesignation =  dom.get('currentDesignation').value;
	var pendingAction=document.getElementById('pendingActions').value;
	loadDesignationByDeptAndType('WorkOrder',dept,currentState,amountRule,additionalRuleValue,pendingAction); 
}

function populateApprover()
{
  getUsersByDesignationAndDept();
}

</script>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<div class="errorstyle" id="workOrder_error" style="display: none;"></div>
		<s:if test="%{hasActionMessages()}">
			<div class="messagestyle">
				<s:property value="%{workOrderNumber}"/>
				&nbsp;
				<s:actionmessage theme="simple" />
			</div>
		</s:if>


		<s:form theme="simple" name="workOrderForm">
		<s:if test="%{mode!='search'}">
		<s:token/>
		</s:if> 
			<s:push value="model"> 
				<s:hidden name="id" id="id"/>
				<s:hidden name="scriptName" id="scriptName" value="WorkOrder"></s:hidden>
				<s:hidden name="model.documentNumber" id="docNumber" />  
				<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
				<div class="formmainbox">
					<div class="insidecontent">
						<div class="rbroundbox2">
							<div class="rbtop2">
								<div></div>
							</div>
							<div class="rbcontent2">
							<div class="datewk">
									<div class="estimateno">
										<s:text name="workOrder.workorderNo" />
										:
										<s:if test="%{not workOrderNumber}">&lt; <s:text
												name="message.notAssigned" /> &gt;</s:if>
										<s:property value="workOrderNumber" />
									</div>
								</div>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td>
											<div id="header">
												<ul id="Tabs">
													<li id="headerTab" class="First Active">
														<a id="header_1" href="#" onclick="showHeaderTab();"><s:text
																name="workOrder.tab.header" />
														</a>
													</li>
													<li id="detailsTab" class="Last">
														<a id="header_4" href="#" onclick="showDetailsTab();"><s:text
																name="workorder.detailtab" />
														</a>
													</li>
												</ul>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											&nbsp;
										</td>
									</tr>
									<tr>
										<td>
											<div id="workorder_header">
												<%@ include file="workOrder-header.jsp"%>
											</div>
										</td>
									</tr>

									<tr>
									  <td>
									    <div id="workorder_details" style="display:none;">
									 	 <%@ include file="workOrder-details.jsp"%>
									 	 <%@ include file="workOrder-mSheet.jsp"%>
									    </div>
									  </td>
									</tr>
									 <s:if test="%{mode != 'view' && mode !='search' }" >
									<tr> 
									    <td>
									    <div id="manual_workflow">
									   		<c:set var="approverHeadCSS" value="headingwk" scope="request" />
						         			<c:set var="approverCSS" value="bluebox" scope="request" />
											<%@ include file="/commons/commonWorkflow.jsp"%>
									    </div>
 						         			<s:hidden name="departmentName" id="departmentName" value="%{departmentName}"/>
									    </td>
            						</tr>
            						</s:if>	 							 	
								 	 <tr>
						            	<td><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div></td>
						          	</tr>
								</table>
							</div>
							
							<div class="rbbot2">
								<div></div>
							</div>
						</div>
					</div>
				</div>
				<div class="buttonholderwk" id="buttons">
					<input type="hidden" name="actionName" id="actionName" />	
						
						<s:if test="%{(sourcepage=='inbox' || model.egwStatus==null || model.egwStatus.code=='NEW'  
						|| model.egwStatus.code=='REJECTED') && mode !='view' && mode !='search' || hasErrors() || hasActionMessages()}">
	 						<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
	 							<s:submit type="submit" cssClass="buttonfinal"
									value="Save" id="Save" name="Save"
									method="save"
									onclick="document.workOrderForm.actionName.value='Save';return validateForm();" />
	 						</s:if>
							<s:if test="%{isSpillOverWorks}">	
								<s:submit type="submit" cssClass="buttonfinal"
										value="Approve" id="Approve" name="Approve"
										method="save"
										onclick="document.workOrderForm.actionName.value='Approve';return validate('Approve');" />
								<s:if test="%{model.egwStatus.code=='NEW'}">
									<s:submit type="submit" cssClass="buttonfinal"
											value="Cancel" id="Cancel" name="Cancel"
											method="save"
											onclick="document.workOrderForm.actionName.value='Cancel';return validate('Cancel');" />
								
								</s:if>
							</s:if>
							<s:else>
	 						<s:iterator value="%{getValidActions()}" var="name">
								<s:if test="%{name!=''}">
										<s:submit type="submit" cssClass="buttonfinal"
											value="%{name}" id="%{name}" name="%{name}"
											method="save"
											onclick="document.workOrderForm.actionName.value='%{name}';return validate('%{name}');" />
								</s:if>
							</s:iterator>   
							</s:else>
						</s:if>
						
						<!--  s:submit type="submit" cssClass="buttonfinal" value="SAVE" id="save" name="save" method="save" /  -->
									
					
					<!--   Action buttons have to displayed only if the page is directed from the inbox   -->
						<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='workorder.close.confirm'/>');"/>
						<input type="submit" class="buttonadd" value="Upload Document" id="workOrderDocUploadButton" onclick="showDocumentManager();return false;" />
						<s:if test="%{id==null}"> 
						<s:if test="%{type=='workOrderForRC'}">
						<input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="button"
			  						 onclick="window.open('${pageContext.request.contextPath}/workorder/workOrder!newform.action?estimateRcId=<s:property value='%{estimateRcId}'/>&type=workOrderForRC','_self');"/>
						</s:if>
						<s:else>
			 				 <input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="button"
			  						 onclick="window.open('${pageContext.request.contextPath}/workorder/workOrder!newform.action?tenderRespId=<s:property value='%{tenderRespId}'/>','_self');"/>
			  			</s:else>
			 			</s:if>
	 			 <s:if test="%{model.id!=null}">
	  						 <input type="button" onclick="window.open('${pageContext.request.contextPath}/workorder/workOrder!viewWorkOrderPdf.action?id=<s:property value='%{model.id}'/>');" 
	  							 class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" />
	 			 </s:if>
	 			 <s:if test="%{model.id!=null && model.egwStatus.code=='APPROVED' && !workOrder.getWorkOrderEstimates().get(0).getEstimate().getIsSpillOverWorks()}">
  						 <input type="button" onclick="window.open('${pageContext.request.contextPath}/workorder/workOrder!viewWorkOrderIntimatationLetter.action?id=<s:property value='%{model.id}'/>');" 
  							 class="buttonpdf" value="WOI Letter" id="woipdfButton"  />
	 			 </s:if>
				</div>
			</s:push>
		</s:form>
		<SCRIPT type="text/javascript">
	
	    function disableForm(){
	    
	    <s:if test="%{sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED'}">
	  	dom.get("woDateImg").style.display='none';
		 </s:if>	
	    
	 <s:if test="%{(sourcepage=='inbox' && model.currentState.value=='CHECKED')}">
	         toggleFields(true,['approverComments']);  
	         showElements(['approverComments']);
	 </s:if>
	 <s:elseif test="%{sourcepage=='inbox' && model.currentState.value=='NEW'}">
	        toggleFields(false,[]);
	        showElements(['approverComments']);
	 </s:elseif>
	 <s:elseif test="%{sourcepage=='inbox' && model.currentState.value!='REJECTED' && model.currentState.value!='END'}">
	        toggleFields(true,['approverDepartment','approverDesignation','approverPositionId','approverComments']);
	 </s:elseif>
	 
	 <s:if test="%{sourcepage=='inbox'}">
		     <s:if test="%{model.currentState.value=='CREATED' || model.currentState.value=='CHECKED' || model.currentState.value=='RESUBMITTED'}">		       
				document.workOrderForm.closeButton.readonly=false;
				document.workOrderForm.closeButton.disabled=false;
				document.workOrderForm.pdfButton.readonly=false;
				document.workOrderForm.pdfButton.disabled=false;
				document.workOrderForm.workOrderDocUploadButton.readonly=false;
				document.workOrderForm.workOrderDocUploadButton.disabled=false;	
				if(dom.get('Approve')!=null)
					dom.get('Approve').disabled=false;
				if(dom.get('Forward')!=null)
					dom.get('Forward').disabled=false;
				dom.get('Reject').disabled=false;	
    			if(document.getElementById("woipdfButton")!=null){
			      	document.getElementById("woipdfButton").disabled=false;
					document.getElementById("woipdfButton").readonly=false;	
			    }
				
			 </s:if>			
		</s:if> 
		}
	function load(){
	<s:if test="%{mode=='search'}">
        for(var i=0;i<document.forms[0].length;i++) {
      		document.forms[0].elements[i].disabled =true;
      		dom.get("woDateImg").style.display='none';
      	}
      	document.workOrderForm.closeButton.readonly=false;
		document.workOrderForm.closeButton.disabled=false;	
		document.workOrderForm.pdfButton.readonly=false;
		document.workOrderForm.pdfButton.disabled=false; 
		if(document.getElementById("woipdfButton")!=null){
	      	document.getElementById("woipdfButton").disabled=false;
			document.getElementById("woipdfButton").readonly=false;	
    	}
      	$('workOrderDocUploadButton').hide();
      </s:if>	
    }
	</SCRIPT>
</body>
</html>
