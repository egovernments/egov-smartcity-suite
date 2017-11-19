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
	<title><s:text name="page.title.workorder" /></title> 

<body onload="getDefaults();load();disableForm();noBack();" onpageshow="if(event.persisted) noBack();" onunload="" > 

<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>

<script>
window.history.forward(1);
function noBack() {
	window.history.forward(); 
}
function getDefaults() {
	roundOffEstimateAmount();
	populateDetails();
	 <s:if test="%{mode!='search'}">
		populateDesignation();
	 </s:if>
	<s:if test="%{'no'.equals(createdBySelection) && model.id==null}">
	populateDesignation1();
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
    populatepreparedBy({executingDepartment:deptId,employeeCode:dom.get("loggedInUserEmployeeCode").value});
}

function populatepreparedBy(params){
	makeJSONCall(['Text','Value','Designation'],'/egworks/estimate/ajaxEstimate!usersInExecutingDepartment.action',params,preparedBySuccessHandler,preparedByFailureHandler) ;
}

preparedBySuccessHandler=function(req,res){ 
	enablePreparedBy(); 
	preparedByDropdown=dom.get("preparedBy");
	var resLength =res.results.length+1;
	var dropDownLength = preparedByDropdown.length;
	for(i=0;i<res.results.length;i++){
	preparedByDropdown.options[i+1]=new Option(res.results[i].Text,res.results[i].Value);
	if(res.results[i].Value=='null') preparedBy.Dropdown.selectedIndex = i;
		preparedByDropdown.options[i+1].Designation=res.results[i].Designation;
	}
	while(dropDownLength>resLength)
	{
		preparedByDropdown.options[res.results.length+1] = null;
		dropDownLength=dropDownLength-1;
	}
	document.getElementById('preparedBy').value='<s:property value="%{empId}" />';
	if(res.results.length == 1) {
		disablePreparedBy();
	}
}
preparedByFailureHandler=function(){
	alert('Unable to load preparedBy');
}
function disablePreparedBy(){
	document.getElementById('preparedBy').disabled = true;
}

function enablePreparedBy(){
	document.getElementById('preparedBy').disabled = false;
}
	
function validateWOAllocatedUsers(){
	if(dom.get("assignedTo1").value!='-1' && dom.get("assignedTo2").value!='-1' && 
		    dom.get("engineerIncharge").value!='-1' && dom.get("engineerIncharge2").value!='-1' && 
		    dom.get("assignedTo2").value==dom.get("assignedTo1").value && dom.get("engineerIncharge").value==dom.get("engineerIncharge2").value){
		    	dom.get("workOrder_error").style.display='';     
				dom.get("workOrder_error").innerHTML='<s:text name="same.allocatedTo.selected"/>';
				return false;
	}
	else{
			dom.get("workOrder_error").innerHTML='';
			dom.get("workOrder_error").style.display="none";
			return true;
		}
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
	if(document.workOrderForm.emdAmountDeposited != null)
		document.workOrderForm.emdAmountDeposited.value=roundTo(document.workOrderForm.emdAmountDeposited.value);
	if(document.workOrderForm.securityDeposit != null)
		document.workOrderForm.securityDeposit.value=roundTo(document.workOrderForm.securityDeposit.value);
	document.workOrderForm.labourWelfareFund.value=roundTo(document.workOrderForm.labourWelfareFund.value);
}
function roundOffDLP()
{
	document.workOrderForm.defectLiabilityPeriod.value=roundTo(document.workOrderForm.defectLiabilityPeriod.value);
}
function populateDetails()
{
	if(dom.get("workOrderDate").value=='') {
		 <s:if test="%{model.workOrderDate==null}">
			document.workOrderForm.workOrderDate.value=getCurrentDate();
		 </s:if>
	}
	<s:if test="%{createdBySelection.toLowerCase().equals('no')}">
		<s:if test="%{dropdownData.departmentList.size==1}" >
			if(dom.get('defaultDepartmentId').value!='')
			{
				dom.get('department').value=dom.get('defaultDepartmentId').value;
				dom.get('department').disabled=true;
			}	
		</s:if>
		<s:if test="%{dropdownData.preparedByList.size==1}" >
			if(dom.get('defaultPreparedBy').value!='')
			{
				dom.get('preparedBy').value=dom.get('defaultPreparedBy').value;
				dom.get('preparedBy').disabled=true;
			}	
		</s:if>	
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

function validateContractPeriod(){

	if(dom.get("contractPeriod").value==''){
    	dom.get("workOrder_error").style.display='';     
		dom.get("workOrder_error").innerHTML='<s:text name="contractPeriod.null"/>';
		window.scroll(0,0);
		return false;
    }
	if(dom.get("contractPeriod").value<=0){
    	dom.get("workOrder_error").style.display='';     
		dom.get("workOrder_error").innerHTML='<s:text name="contractPeriod.greater.than.zero"/>';
		window.scroll(0,0);
		return false;
    }
    return true;
}
	
	function validateFields(text){

	var cutOffDate = '<s:date name="contractPeriodCutOffDate" format="dd/MM/yyyy"/>';
	var woCreatedDate = '<s:date name="createdDate" format="dd/MM/yyyy"/>';

	<s:if test="%{id==null}">
		if(!validateContractPeriod())
			return false;
	</s:if>

	<s:if test="%{model.egwStatus!=null && (model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED')}">
		if(woCreatedDate >= cutOffDate){
			if(!validateContractPeriod())
				return false;
		}
	</s:if>

  	if(text!="cancel"){
  		if(dom.get("defectLiabilityPeriod").value<=0) {
	    	dom.get("workOrder_error").style.display=''; 
	    	<s:if test="%{model.id==null || (model.id!=null && (model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED'))}">    
				dom.get("workOrder_error").innerHTML='<s:text name="defectLiabilityPeriod.validate"/>';
			</s:if>
			<s:else>
				dom.get("workOrder_error").innerHTML='<s:text name="defectLiabilityPeriod.workflow.validate"/>';
			</s:else>
			window.scroll(0,0);
			return false;
	  	}
  	}
	
  	if(dom.get("assignedTo1").value=='-1'){
    	dom.get("workOrder_error").style.display='';     
		dom.get("workOrder_error").innerHTML='<s:text name="allocatedTo.notselected"/>';
		window.scroll(0,0);
		return false
    }
	   
    if(dom.get("assignedTo1").value!='-1' && dom.get("engineerIncharge").value=='-1'){
    	dom.get("workOrder_error").style.display='';     
		dom.get("workOrder_error").innerHTML='<s:text name="engineerIncharge1.notselected"/>';
		window.scroll(0,0);
		return false
    }
    if(dom.get("assignedTo2").value!='-1' && dom.get("engineerIncharge2").value=='-1'){
    	dom.get("workOrder_error").style.display='';     
		dom.get("workOrder_error").innerHTML='<s:text name="engineerIncharge2.notselected"/>';
		window.scroll(0,0);
		return false
    }
    if(dom.get("workOrderDate").value==""){
    	dom.get("workOrder_error").innerHTML='<s:text name="workorder.date.null"/>'; 
        dom.get("workOrder_error").style.display='';
        window.scroll(0,0);
	 	return false;
    }
    if(dom.get("preparedBy").value=='-1'){
    	dom.get("workOrder_error").innerHTML='<s:text name="workorder.preparedBy.null"/>';  
        dom.get("workOrder_error").style.display='';
        window.scroll(0,0);
	 	return false;
	}
	<s:if test="%{!isRCEstimate}">
	    if(dom.get("emdAmountDeposited").value=='0.00'){
	    	dom.get("workOrder_error").innerHTML='<s:text name="workOrder.emdAmount.invalid"/>';  
	        dom.get("workOrder_error").style.display='';
	        window.scroll(0,0);
		 	return false;
		}
	</s:if>
		
	if(!checkDateFormat(dom.get("workOrderDate"))){
 		dom.get("workOrder_error").innerHTML='<s:text name="invalid.fieldvalue.workOrderDate"/>'; 
        dom.get("workOrder_error").style.display='';
        window.scroll(0,0);
	 	return false;
 	}
	if(text!="cancel"){
		if(!validateWOAllocatedUsers()){
			return false;
		}
		
		 	 <s:if test="%{tenderResponse != null && tenderResponse.tenderResponseContractors.size > 1}">
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
		 	 clearMessage('workOrder_error')
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
	 }
    dom.get("workOrder_error").style.display='none';     
	dom.get("workOrder_error").innerHTML='';
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
function validate(obj,text){
	if(obj!="cancel"){
		if(!validateUser(text))
			return false;
	}
	if(text!="reject"){
		if(!validateFields(text))
			  return false;
	}
	
	if(obj=="cancel"){
	   if(!validateCancel())
	      return false;
	}
	enableSelect();
	return true;
}
 function isNumberKey(evt)
      {
         var charCode = (evt.which) ? evt.which : event.keyCode
         if (charCode > 31 && (charCode < 48 || charCode > 57))
            return false;

         return true;
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
									    </div>
									  </td>
									</tr>
									<tr> 
									    <td>
									    <s:if test="%{mode!='search'}">
										    <div id="manual_workflow">
										   	<%@ include file="../workflow/workflow.jsp"%> 	 
										    </div>
									    </s:if>
									    </td>
            						</tr>								 	
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
					<s:if test="%{((sourcepage=='inbox' || model.egwStatus==null) || hasErrors())}">
										
						<s:iterator value="%{validActions}">
							<s:if test="%{description!=''}">
								<s:if test="%{description=='CANCEL'}">
									<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" 
										name="%{name}" method="cancel" 
										onclick="document.workOrderForm.actionName.value='%{name}';return validate('cancel','%{name}');"/>
							  	</s:if>								
								<s:else>
									<s:submit type="submit" cssClass="buttonfinal"
										value="%{description}" id="%{name}" name="%{name}"
										method="save"
										onclick="document.workOrderForm.actionName.value='%{name}';return validate('noncancel','%{name}');" />
								</s:else>
							</s:if>
						</s:iterator>
						</s:if>
						<!--  s:submit type="submit" cssClass="buttonfinal" value="SAVE" id="save" name="save" method="save" /  -->
									
					
					<!--   Action buttons have to displayed only if the page is directed from the inbox   -->
					<s:if test="%{mode!='search'}">
							<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='workorder.close.confirm'/>');"/>
						</s:if>
						<s:else>
							<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
						</s:else>
						<input type="submit" class="buttonadd" value="Upload Document" id="workOrderDocUploadButton" onclick="showDocumentManager();return false;" />
						<s:if test="%{id==null}"> 
			 				 <input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="button"
			  						 onclick="window.open('${pageContext.request.contextPath}/workorder/workOrder!newform.action?tenderRespId=<s:property value='%{tenderRespId}'/>','_self');"/>
			 			</s:if>
			 		
			 	<s:if test="%{isWorkCommenced}">
	 			 	<input type="button" class="buttonpdf" value="Work Notice" id="workNoticeButton" name="workNoticeButton" onclick="showWorkOrderNotice();"/>
	 			 </s:if>
	 			 <s:if test="%{model.id!=null}">
	  						 <input type="button" onclick="window.open('${pageContext.request.contextPath}/workorder/workOrder!viewWorkOrderPdf.action?id=<s:property value='%{model.id}'/>');" 
	  							 class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" />
	 			 </s:if>
				</div>
			</s:push>
		</s:form>
		<SCRIPT type="text/javascript">
			function showWorkOrderNotice(){
				var url = "${pageContext.request.contextPath}/workorder/workOrder!viewWorkOrderNotice.action?id="+<s:property value='%{model.id}'/>;
				window.open(url, "", "width=800 px, height=800 px,,scrollbars=yes,status=yes");
			}
		
	    function disableForm(){
	 <s:if test="%{(sourcepage=='inbox' && model.egwStatus!=null && model.egwStatus.code=='CHECKED')}">
	        toggleFields(true,['departmentid','designationId','approverUserId','approverComments']);
	      	hideElements(['workflowDetials']);
	      	showElements(['approverCommentsRow']);
	      	links=document.workOrderForm.getElementsByTagName("a"); 
	        disableLinks(links,['aprdDatelnk']);
	     	if(woDataTable)
	        	woDataTable.removeListener('cellClickEvent');
				
	 </s:if>
	 <s:elseif test="%{sourcepage=='inbox' && model.egwStatus!=null && model.egwStatus.code=='NEW'}">
	        toggleFields(false,[]);
	        showElements(['approverCommentsRow']);
	 </s:elseif>
	 <s:elseif test="%{sourcepage=='inbox' 
		 && model.egwStatus!=null && model.egwStatus.code!='REJECTED' && model.egwStatus.code!='END'}">
	        toggleFields(true,['departmentid','designationId','approverUserId','approverComments']);
	        links=document.workOrderForm.getElementsByTagName("a"); 
	        disableLinks(links,['aprdDatelnk']);
	        showElements(['approverCommentsRow']);
	     	if(woDataTable)
	        	woDataTable.removeListener('cellClickEvent');
				
	 </s:elseif>
	 
	 <s:if test="%{sourcepage=='inbox'}">
		     <s:if test="%{model.egwStatus!=null 
			     && (model.egwStatus.code=='CREATED' || model.egwStatus.code=='CHECKED' || model.egwStatus.code=='RESUBMITTED')}">		       
				document.workOrderForm.approve.readonly=false;
				document.workOrderForm.approve.disabled=false;
				document.workOrderForm.reject.readonly=false;
				document.workOrderForm.reject.disabled=false;
				document.workOrderForm.closeButton.readonly=false;
				document.workOrderForm.closeButton.disabled=false;
				document.workOrderForm.pdfButton.readonly=false;
				document.workOrderForm.pdfButton.disabled=false;
				document.workOrderForm.workOrderDocUploadButton.readonly=false;
				document.workOrderForm.workOrderDocUploadButton.disabled=false;		
				
			 </s:if>			
		</s:if> 
		}
	function load(){
	<s:if test="%{mode=='search'}">
        for(var i=0;i<document.forms[0].length;i++) {
      		document.forms[0].elements[i].disabled =true;
      	}
    var isWorkCommenced = '<s:property value="%{isWorkCommenced}"/>';
      	document.workOrderForm.closeButton.readonly=false;
		document.workOrderForm.closeButton.disabled=false;	
		document.workOrderForm.pdfButton.readonly=false;
		document.workOrderForm.pdfButton.disabled=false;
		<s:if test="%{isWorkCommenced}">
			document.workOrderForm.workNoticeButton.readonly=false;
			document.workOrderForm.workNoticeButton.disabled=false;
		</s:if>	
      	$('workOrderDocUploadButton').hide();
      	links=document.workOrderForm.getElementsByTagName("a"); 
	        disableLinks(links,['aprdDatelnk']);
     	if(woDataTable)
        	woDataTable.removeListener('cellClickEvent');
      </s:if>	
    }
	
	</SCRIPT>
</body>
</html>
