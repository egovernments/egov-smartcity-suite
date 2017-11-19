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

<html>
<title><s:text name="contractoradvance.advancerequisition.title" /></title>
<body class="yui-skin-sam" onload="setDefaults();noBack();" onpageshow="if(event.persisted) noBack();" onunload=""> 
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script> 

<script>
window.history.forward(1);
function noBack() {
	window.history.forward(); 
}

function setDefaults() {
	setRequisitionDate();
	document.getElementById('advancePaidId').innerHTML = roundTo(document.getElementById('advancePaidId').innerHTML);
	<s:if test="%{sourcepage!='search'}">
		populateDesignation();
	</s:if>
}

function setRequisitionDate(){
	var advanceRequisitionDate = document.contractorAdvanceRequisitionForm.advanceRequisitionDate.value;
	if(advanceRequisitionDate == '') {
		document.contractorAdvanceRequisitionForm.advanceRequisitionDate.value = '<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	}
	document.contractorAdvanceRequisitionForm.hiddenAdvanceRequisitionDate.value = document.contractorAdvanceRequisitionForm.advanceRequisitionDate.value
}
	
function showEstimateDetails(estimateId){ 
	window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+estimateId+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function showWorkOrderDetails(workOrderId) {
	window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+workOrderId+"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function enableFields(){
	for(i=0;i<document.contractorAdvanceRequisitionForm.elements.length;i++){
	        document.contractorAdvanceRequisitionForm.elements[i].disabled=false; 
	} 
}

function validateInputBeforeSubmit() {

	advanceRequisitionDate = document.contractorAdvanceRequisitionForm.advanceRequisitionDate.value;
	woApprovedDate = '<s:date name="workOrderEstimate.workOrder.approvedDate" format="dd/MM/yyyy"/>';
	currentDate = '<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	if(advanceRequisitionDate == '') {
		dom.get("contractorAdvanceRequisition_error").innerHTML='<s:text name="advancerequisition.date.required"/>'; 
        dom.get("contractorAdvanceRequisition_error").style.display='';
        window.scroll(0,0);
		return false;
	}	 

	if(compareDate(advanceRequisitionDate,woApprovedDate) == 1 ) {
		dom.get("contractorAdvanceRequisition_error").innerHTML='<s:text name="advancerequisition.date.lessthan.workorder.approveddate" />';
	    dom.get("contractorAdvanceRequisition_error").style.display='';
	    window.scroll(0,0);
	    return false;
	}

	if(compareDate(advanceRequisitionDate,currentDate) == -1 ){
		dom.get("contractorAdvanceRequisition_error").innerHTML='<s:text name="advancerequisition.validate.date.greaterthan.currentDate" />'; 
       	dom.get("contractorAdvanceRequisition_error").style.display='';
       	window.scroll(0,0);
		return false;
	 }	
	
	advanceAccountCode = document.contractorAdvanceRequisitionForm.advanceAccountCode.value;
	if(advanceAccountCode == '' || advanceAccountCode == 0 || advanceAccountCode == -1) {
		dom.get("contractorAdvanceRequisition_error").innerHTML='<s:text name="advancerequisition.advanceaccountcode.required"/>'; 
        dom.get("contractorAdvanceRequisition_error").style.display='';
        window.scroll(0,0);
		return false;
	}	 

	advanceRequisitionAmount = document.contractorAdvanceRequisitionForm.advanceRequisitionAmount.value;
	if(advanceRequisitionAmount == '') {
		dom.get("contractorAdvanceRequisition_error").innerHTML='<s:text name="advancerequisition.amount.required"/>'; 
        dom.get("contractorAdvanceRequisition_error").style.display='';
        window.scroll(0,0);
		return false;
	}

	if(advanceRequisitionAmount <= 0) {
		dom.get("contractorAdvanceRequisition_error").innerHTML='<s:text name="advancerequisition.valid.amount.greaterthan.zero"/>'; 
        dom.get("contractorAdvanceRequisition_error").style.display='';
        window.scroll(0,0);
		return false;
	}		

	drawingOfficer = document.contractorAdvanceRequisitionForm.drawingOfficerNameSearch.value;
	if(drawingOfficer == '') {
		dom.get("contractorAdvanceRequisition_error").innerHTML='<s:text name="advancerequisition.drawingofficer.required"/>'; 
        dom.get("contractorAdvanceRequisition_error").style.display='';
        window.scroll(0,0);
		return false;
	}	
	
	dom.get("contractorAdvanceRequisition_error").style.display="none";
	return true;	
}

function roundOffAmount(obj) {
	obj.value=roundTo(obj.value);
}

function validateForm(text){
	if(!validateInputBeforeSubmit()){
		return false;
	}
	if(text =='cancel'){
		if(!validateCancel()){
			return false;
		}
	}
	if(text!='approve' && text!='reject' && text!='save'){
		if(!validateWorkFlowApprover(text))
			return false;
	}
	enableFields();
	return true;  
}

function validateWorkFlowApprover(name){
	document.getElementById('approver_error').style.display ='none';
	if(name != 'reject' && name != 'cancel' && name!='save') {
	 	<s:if test="%{model.currentState==null || ((model.currentState.nextAction!='Pending for Approval') && !(model.currentState.value=='REJECTED' && model.currentState.nextAction=='Pending for Verification') )}" >
		if(null != document.getElementById("designationId") && document.getElementById("designationId").value == -1){
			document.getElementById('approver_error').style.display ='';	
			document.getElementById('approver_error').innerHTML ="";
			document.getElementById('approver_error').innerHTML ='<s:text name="workflow.approver.designation.null"/>';
			return false;
		}
		if(null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
			document.getElementById('approver_error').style.display ='';	
			document.getElementById('approver_error').innerHTML ="";
			document.getElementById('approver_error').innerHTML ='<s:text name="workflow.approver.null"/>';
			return false;
		}
	 	</s:if>
	}
	if(name=='revert') {
		<s:if test="%{model.currentState.nextAction!='Pending for Verification'}" >
			if(null != document.getElementById("designationId") && document.getElementById("designationId").value == -1){
				document.getElementById('approver_error').style.display ='';	
				document.getElementById('approver_error').innerHTML ="";
				document.getElementById('approver_error').innerHTML ='<s:text name="workflow.approver.designation.null"/>';
				return false;
			}
			if(null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
				document.getElementById('approver_error').style.display ='';	
				document.getElementById('approver_error').innerHTML ="";
				document.getElementById('approver_error').innerHTML ='<s:text name="workflow.approver.null"/>';
				return false;
			}
		</s:if>
	}	
	return true; 
}

var drawingOfficerNameSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
    dom.get("drawingOfficerId").value = oData[1];
};

function clearDrawingOfficerDetails(obj) {
	if(obj.value == "") {
		document.getElementById("drawingOfficerId").value = "";
	}	
}

function validateCancel() {
	var msg='<s:text name="advancerequisition.cancel.confirm"/>';
	var arfNumber='<s:property value="advanceRequisitionNumber"/>'; 
	if(!confirmCancel(msg,arfNumber)) {
		return false;
	}
	else {
		return true;
	}
}

function validateARFDateSelection(obj) {
	if(obj.value != "") {
		advanceRequisitionDate = document.contractorAdvanceRequisitionForm.advanceRequisitionDate.value;
		if(advanceRequisitionDate == '') {
			dom.get("contractorAdvanceRequisition_error").innerHTML='<s:text name="advancerequisition.date.required"/>'; 
	        dom.get("contractorAdvanceRequisition_error").style.display='';
	        window.scroll(0,0);
			return false;
		}	
	}
	dom.get("contractorAdvanceRequisition_error").style.display='none';
	return true; 
}

function drawingOfficerSearchParameters(){
	advanceRequisitionDate = document.contractorAdvanceRequisitionForm.advanceRequisitionDate.value;
	if(advanceRequisitionDate != ""){
	   	return "advanceRequisitionDate="+advanceRequisitionDate;
	}
}

var previousARFDate;
function dateChangeValidation() {
	drawingOfficerNameSearch = document.contractorAdvanceRequisitionForm.drawingOfficerNameSearch.value;
	if(drawingOfficerNameSearch != '') {
		var advanceRequisitionDate = document.contractorAdvanceRequisitionForm.advanceRequisitionDate.value;
		var id = document.contractorAdvanceRequisitionForm.id.value;
		var hiddenAdvanceRequisitionDate = document.contractorAdvanceRequisitionForm.hiddenAdvanceRequisitionDate.value;
		if(hiddenAdvanceRequisitionDate != ''){
			previousARFDate = hiddenAdvanceRequisitionDate;
		}
		else{
			previousARFDate = advanceRequisitionDate;
		}
		
		if(trim(advanceRequisitionDate) !='' && hiddenAdvanceRequisitionDate != '' && hiddenAdvanceRequisitionDate != advanceRequisitionDate){
			popup('popUpDiv');		
		}
		
		else if(id != '' && id!=undefined && advanceRequisitionDate != '' && hiddenAdvanceRequisitionDate != advanceRequisitionDate){
			popup('popUpDiv');
		}
	}
	document.contractorAdvanceRequisitionForm.hiddenAdvanceRequisitionDate.value=document.contractorAdvanceRequisitionForm.advanceRequisitionDate.value;
}

function resetDrawingOfficerDetails() {
	document.contractorAdvanceRequisitionForm.drawingOfficerId.value = "";
	document.contractorAdvanceRequisitionForm.drawingOfficerNameSearch.value = "";
}

function resetPreviousDate() {
	document.contractorAdvanceRequisitionForm.advanceRequisitionDate.value=previousARFDate;
	document.contractorAdvanceRequisitionForm.hiddenAdvanceRequisitionDate.value=previousARFDate;
}

</script>

    <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
     <s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	<s:property value="%{advanceRequisitionNumber}"/> &nbsp; <s:actionmessage theme="simple"/>
        </div>
    </s:if>
<s:form  action="contractorAdvanceRequisition" theme="simple" name="contractorAdvanceRequisitionForm" >
<div class="errorstyle" id="contractorAdvanceRequisition_error" style="display:none;"></div>
<div id="blanket" style="display:none;"></div>
<div id="popUpDiv" style="display:none;" ><s:text name="advancerequisition.dateChange.warning"/>(<a href="#" onclick="popup('popUpDiv');resetDrawingOfficerDetails();">Yes</a>/<a href="#" onclick="popup('popUpDiv');resetPreviousDate();">No</a>)?</div>

<s:if test="%{sourcepage!='search'}">
 	<s:token/>
</s:if>
<s:push value="model">
<s:if test="%{model.advanceRequisitionNumber != null}">
	<s:hidden name="id" value="%{id}" id="id" />
</s:if>
<s:hidden id="workOrderEstimateId" name="workOrderEstimateId" value="%{workOrderEstimateId}" />
<s:hidden id="sourcepage" name="sourcepage" value="%{sourcepage}" />
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
		<div class="datewk">
		<div class="estimateno">
			<s:text name="advancerequisition.requisitionnumber" />:
			<s:if test="%{not advanceRequisitionNumber}">&lt; 
				<s:text	name="message.notAssigned" /> &gt;
			</s:if>
			<s:property value="advanceRequisitionNumber" />
		</div>
		</div>
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          
          <tr>
            <td>
 				<table width="100%"  border="0" cellspacing="0" cellpadding="0">
	 				<tr>
	                	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
	                	<div class="headplacer"><s:text name="estimate.header" />:</div></td>
	                </tr>
					<tr>
						<td width="25%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="advancerequisition.date" />:</td>
						<td width="25%" class="whitebox2wk">
							<s:date name="advanceRequisitionDate" var="advanceRequisitionDateFormat" format="dd/MM/yyyy"/>
							<s:textfield name="advanceRequisitionDate" value="%{advanceRequisitionDateFormat}" id="advanceRequisitionDate" cssClass="selectwk" onBlur="dateChangeValidation(this)" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
	                    	<a href="javascript:show_calendar('forms[0].advanceRequisitionDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
	                    		<img id="arfDateImage" src="/egworks/resources/erp2/images/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
	                    	</a>
	                    	<input type='hidden' id='hiddenAdvanceRequisitionDate' name='hiddenAdvanceRequisitionDate'/>
						</td>
						<td class="whiteboxwk"><s:text name="advancerequisition.estimatenumber" />:</td>  
						<td class="whitebox2wk" > 
							<a href="#" id="estNumb" onclick="showEstimateDetails(<s:property  value="%{workOrderEstimate.estimate.id}"/>)"><s:property  value="%{workOrderEstimate.estimate.estimateNumber}"   /></a>
						</td>

					</tr>
					<tr>
						<td class="greyboxwk"> <s:text name="advancerequisition.fund" />:</td>
						<td class="greybox2wk"><s:property  value="%{workOrderEstimate.estimate.financialDetails[0].fund.name}" /></td>
						
						<td class="greyboxwk"><s:text name="advancerequisition.function" />:</td>
						<td class="greybox2wk"><s:property  value="%{workOrderEstimate.estimate.financialDetails[0].function.name}" /></td>
					</tr>
					<tr>
						<td class="whiteboxwk"> <s:text name="advancerequisition.budgethead" />:</td>
						<td class="whitebox2wk" colspan="3"><s:property  value="%{workOrderEstimate.estimate.financialDetails[0].budgetGroup.name}" /></td>
					</tr>
					<tr>
						<td class="greyboxwk"> <s:text name="advancerequisition.userdepartment" />:</td>
						<td class="greybox2wk"><s:property  value="%{workOrderEstimate.estimate.userDepartment.deptName}" /></td>
						
						<td class="greyboxwk"><s:text name="advancerequisition.executingdepartment" />:</td>
						<td class="greybox2wk"><s:property  value="%{workOrderEstimate.estimate.executingDepartment.deptName}" /></td>
					</tr>
					<tr>
						<td class="whiteboxwk"> <s:text name="advancerequisition.workordernumber" />:</td>
						<td class="whitebox2wk"> 
							<a href="#" id="woNumb" onclick="showWorkOrderDetails(<s:property  value="%{workOrderEstimate.workOrder.id}"/>)">
								<s:property  value="%{workOrderEstimate.workOrder.workOrderNumber}"  />
							</a>
						</td>
						
						<td class="whiteboxwk"> <s:text name="advancerequisition.contractor" />:</td>
						<td class="whitebox2wk"> <s:property  value="%{workOrderEstimate.workOrder.contractor.code+' - '+workOrderEstimate.workOrder.contractor.name}" /></td>
					</tr>
					
					<tr>
						<td class="greyboxwk"><span class="mandatory">*</span><s:text name="advancerequisition.advancecoa" />:</td>
						<td class="greybox2wk" colspan="3">
							<s:select headerKey="-1" headerValue="%{getText('default.select')}" name="advanceAccountCode" id="advanceAccountCode" 
        						cssClass="selectwk" list="dropdownData.advanceAccountCodeList" listKey="id" listValue="glcode+' - '+name" />
        				</td>
					</tr>
					 <tr>
				   		<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="advancerequisition.advanceamount"/> :</td>
						<td class="whitebox2wk">
							<s:textfield name="advanceRequisitionAmount" value="%{advanceRequisitionAmount}" id="advanceRequisitionAmount" cssClass="selectamountwk" onblur="roundOffAmount(this)"/>
						</td>	
				        <td class="whiteboxwk"><s:text name="advancerequisition.advancepaid"/> :</td>
				        <td class="whitebox2wk"><span id="advancePaidId" style="text-align:right;"><s:property value="%{advancePaid}" /></span></td>
				   </tr>
				   
				   <tr>
						<td class="greyboxwk"><span class="mandatory">*</span><s:text name="advancerequisition.drawingofficer" />:</td>
						<td class="greybox2wk">
							<div class="yui-skin-sam">
       							<div id="drawingOfficerNameSearch_autocomplete">
              							<div> 
       									<s:textfield id="drawingOfficerNameSearch" name="drawingOfficerName" value="%{drawingOfficer.code+' - '+drawingOfficer.name}" 
       									cssClass="selectwk" onBlur="clearDrawingOfficerDetails(this)" onkeypress="if(event.keyCode==13) return false;return validateARFDateSelection(this);"/> 
       									<s:hidden id="drawingOfficerId" name="drawingOfficer" value="%{drawingOfficer.id}"/>
       								</div>
       								<span id="drawingOfficerNameSearchResults"></span>
       							</div>		
       						</div>
       						<egov:autocomplete name="drawingOfficerNameSearch" width="40" field="drawingOfficerNameSearch" url="ajaxContractorAdvance!searchDrawingOfficer.action?" queryQuestionMark="false" results="drawingOfficerNameSearchResults" handler="drawingOfficerNameSearchSelectionHandler" paramsFunction="drawingOfficerSearchParameters" queryLength="3" />
						</td>
						
						<td class="greyboxwk"><s:text name="advancerequisition.remarks" />:</td>
						<td class="greybox2wk"><s:textarea name="narration" cols="40" rows="4" cssClass="selectwk" id="narration" /></td>
					</tr>
				</table>   
            </td> 
          </tr>   
          		
			<tr>
         		<td colspan="12" class="shadowwk"> </td>               
         	</tr>
         	<tr><td>&nbsp;</td></tr>
		</table>
		
		</div>
    
            </td>
          </tr>
          
         <s:if test="%{sourcepage!='search'}" >        
	 	 <tr> 
		    <td>
		    	<div id="manual_workflow">
		    		<s:hidden name="scriptName" id="scriptName" value="ContractorAdvanceRequisition"></s:hidden>
					<s:hidden name="workflowFunctionaryId" id="workflowFunctionaryId" ></s:hidden>
         			<%@ include file="../workflow/workflow.jsp"%> 
  				</div>
 		    </td>
            </tr>
          </s:if>       
                  
           <tr>
            <td><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div></td>
          </tr>
        </table>    

<div class="rbbot2"><div></div></div>
</div>     
	
</div>
</div>
</div>

<div id="button_submit" class="buttonholderwk">
	<s:hidden name="actionName"  id="actionName"/>
	<s:if test="%{((sourcepage=='inbox' || model.status==null || model.status.code=='NEW'  
						|| model.status.code=='REJECTED') || hasErrors() || hasActionMessages()) }">
		<s:iterator value="%{validActions}">
		<s:if test="%{description!=''}">
			<s:if test="%{description == 'CANCEL' && model.id != null}">
				<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" 
						 method="cancel" onclick="enableFields();
						 document.contractorAdvanceRequisitionForm.actionName.value='%{name}';return validateCancel();"/>
			</s:if>
			<s:else>
				
				<s:submit type="submit" cssClass="buttonfinal"
					value="%{description}" id="%{name}" name="%{name}"
					method="save"
					onclick="document.contractorAdvanceRequisitionForm.actionName.value='%{name}';
					return validateForm('%{name}');" />
			</s:else>
		</s:if>
	 	</s:iterator>
	</s:if>

	  <s:if test="%{sourcepage!='search'}">
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="return confirmClose('<s:text name='advancerequisition.form.close.confirm'/>')"/>
	</s:if>
	<s:else>
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
	</s:else>
</div>
</s:push>
</s:form>
<script>
<s:if test="%{sourcepage=='search' || sourcepage=='inbox'}">
	load();
</s:if>
function load() { 
	<s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && model.currentState.value=='REJECTED' && model.currentState.nextAction=='Pending for Verification' )}">
		toggleFields(true,['departmentid','designationId','approverUserId','approverComments']);		 
		document.getElementById('arfDateImage').style.display='none';
		<s:if test="%{sourcepage=='inbox'}">
			hideElements(['workflowDetials']); 
			showElements(['approverCommentsRow']) 
		</s:if>
	</s:if>
	<s:elseif test="%{sourcepage=='inbox' && model.status.code!='REJECTED' && model.status.code!='NEW'}">
		toggleFields(true,['departmentid','designationId','approverUserId','approverComments']);
		document.getElementById('arfDateImage').style.display='none';
		showElements(['approverCommentsRow']);
	</s:elseif>
	<s:elseif test="%{sourcepage=='inbox' && model.currentState.value=='REJECTED' && model.currentState.nextAction!=null && model.currentState.nextAction!=''}">
		toggleFields(true,['departmentid','designationId','approverUserId','approverComments']);
		document.getElementById('arfDateImage').style.display='none';		
	</s:elseif> 
	<s:elseif test="%{sourcepage=='inbox' && model.currentState.value=='REJECTED' && (model.currentState.nextAction==null || model.currentState.nextAction=='')}">
		toggleForSelectedFields(true,[]);
		showElements(['approverCommentsRow']);
	</s:elseif>
	<s:if test="%{sourcepage=='search' || sourcepage=='inbox'}">
		enabledivChilderns("button_submit");
	</s:if>
}
</script>
</body>
</html>
