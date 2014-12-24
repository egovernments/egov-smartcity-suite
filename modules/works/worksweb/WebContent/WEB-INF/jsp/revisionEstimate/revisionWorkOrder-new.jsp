<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<title>Revision <s:text name="page.title.workorder" /></title> 

<body onload="spillOverWorks();"> 

<script src="<egov:url path='js/works.js'/>"></script>

<script>
function disableSelect(){
	if(dom.get('Approve')!=null){
		document.getElementById('Approve').style.display='none';
	}
	document.revisionWorkOrderForm.securityDeposit.readonly=true;
	document.revisionWorkOrderForm.securityDeposit.disabled=true;
		
	}  
function roundOffSecurityDepositAmount()
{	
	document.revisionWorkOrderForm.securityDeposit.value=roundTo(document.revisionWorkOrderForm.securityDeposit.value);
}

function spillOverWorks(){
	<s:if test="%{isSpillOverWorks}">
		if(dom.get("manual_workflow")!=null){
			dom.get("manual_workflow").style.display='none';
		}
	</s:if>
}

function showHeaderTab(){
  var hiddenid = document.forms[0].id.value;
  document.getElementById('revisionWorkOrder_header').style.display=''; 
  setCSSClasses('detailsTab','Last');
  setCSSClasses('headerTab','First Active'); 
  hideDetailsTab();
 
}
function hideHeaderTab(){
  document.getElementById('revisionWorkOrder_header').style.display='none';
  }

function hideDetailsTab(){
  document.getElementById('revisionWorkOrder_details').style.display='none';
}


function showDetailsTab(){ 
  document.getElementById('revisionWorkOrder_details').style.display='';
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

function showWorkOrderDetails(workOrderId){ 
	window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+workOrderId+"&mode=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}


function showRevisionWorkOrderDetails(workOrderId){
	var url = '${pageContext.request.contextPath}/revisionEstimate/revisionWorkOrder!view.action?revWorkOrderId='+workOrderId+"&sourcepage=search";
	window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function enableSelect(){
	for(i=0;i<document.workOrderForm.elements.length;i++){
      document.revisionWorkOrderForm.elements[i].disabled=false;
	}
}

function validateCancel() {
	var msg='<s:text name="revisionWorkOrder.cancel.confirm"/>';
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

function validate(text){
	if(document.getElementById("actionName").value=='Cancel'){
		if(!validateCancel()){
			return false;
		}
	}
	if(!validateFields())
	  return false;
	if(text!='Approve' && text!='Reject' ){
		if(!validateWorkFlowApprover(text))
			return false;
	}
	if(text=='Reject' ){
	var remarks=document.getElementById("approverComments").value;
		if(trim(remarks)==""){
			dom.get("revisionWorkOrder_error").style.display='';
    		document.getElementById("revisionWorkOrder_error").innerHTML='<s:text name="revisionWorkOrder.remarks.null" /><br>';
    		return false;
		}
	}
	enableSelect(); 
	return true;
}


function loadDesignationFromMatrix(){ 
	var dept=dom.get('departmentName').value;
	var currentState = dom.get('currentState').value;
	var amountRule =  dom.get('amountRule').value;
	var additionalRuleValue =  dom.get('additionalRuleValue').value;
	var pendingAction=document.getElementById('pendingActions').value;
	loadDesignationByDeptAndType('RevisionWorkOrder',dept,currentState,amountRule,additionalRuleValue,pendingAction); 
}

function populateApprover() {
  getUsersByDesignationAndDept();
}

function viewDocument(){
	  viewDocumentManager(dom.get("docNumber").value); 
}

</script>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<div class="errorstyle" id="revisionWorkOrder_error" style="display: none;"></div>
		<s:if test="%{hasActionMessages()}">
			<div class="messagestyle">
				<s:property value="%{workOrderNumber}"/>
				&nbsp;
				<s:actionmessage theme="simple" />  
			</div>
		</s:if>

		<s:form theme="simple" name="revisionWorkOrderForm">
		<s:if test="%{sourcepage!='search'}">
		<s:token/>
		</s:if>
			<s:push value="model">
				<s:hidden name="revWorkOrderId" id="revWorkOrderId" value="%{revWorkOrderId}" />
				<s:hidden name="sourcepage" id="sourcepage" value="%{sourcepage}" />
				<s:hidden id="revEstimateId" name="revEstimateId" value="%{revEstimateId}" />
				<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
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
										<s:text name="revisionWO.No" />
										:
										<s:if test="%{model.currentState!=''}">&lt;<s:text
												name="message.notAssigned" /> &gt;</s:if>
										<s:else>
											<s:property value="workOrderNumber" />
										</s:else>
									</div>
								</div>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td>
											<div id="header">
												<ul id="Tabs">
													<li id="headerTab" class="First Active">
														<a id="header_1" href="#" onclick="showHeaderTab();"><s:text
																name="revisionWO.tab.header" />
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
											<div id="revisionWorkOrder_header">
												<%@ include file="revisionWorkOrder-header.jsp"%>
											</div>
										</td>
									</tr>

									<tr>
									  <td>
									    <div id="revisionWorkOrder_details" style="display:none;">
									 	 	<%@ include file="revisionWorkOrder-details.jsp"%>
									    </div>
									  </td>
									</tr>
									 <s:if test="%{sourcepage !='search' }" >
									<tr> 
									    <td>
									    <div id="manual_workflow">
									   		<c:set var="approverHeadCSS" value="headingwk" scope="request" />
						         			<c:set var="approverCSS" value="bluebox" scope="request" />
						         			<s:hidden name="departmentName" id="departmentName" value="%{departmentName}"/>
											<%@ include file="/commons/commonWorkflow.jsp"%>
									    </div>
 						         			
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
	 						<!--<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
	 							<s:submit type="submit" cssClass="buttonfinal"
									value="Save" id="Save" name="Save"
									method="save"
									onclick="document.revisionWorkOrderForm.actionName.value='Save';return validateForm();" />
	 						</s:if>-->
	 						<s:if test="%{isSpillOverWorks}">	
								<s:submit type="submit" cssClass="buttonfinal"
										value="Approve" id="Approve" name="Approve"
										method="save"
										onclick="document.revisionWorkOrderForm.actionName.value='Approve';return validate('Approve');" />
								<s:if test="%{model.egwStatus.code=='NEW'}">
									<s:submit type="submit" cssClass="buttonfinal"
											value="Cancel" id="Cancel" name="Cancel"
											method="save"
											onclick="document.revisionWorkOrderForm.actionName.value='Cancel';return validate('Cancel');" />
								
								</s:if>
	 					</s:if>
	 					<s:else>
	 						<s:iterator value="%{getValidActions()}" var="name">
								<s:if test="%{name!=''}">
										<s:submit type="submit" cssClass="buttonfinal"
											value="%{name}" id="%{name}" name="%{name}"
											method="save"
											onclick="document.revisionWorkOrderForm.actionName.value='%{name}';return validate('%{name}');" />
								</s:if>
							</s:iterator>   
							</s:else>
							</s:if>
							<s:if test="%{sourcepage=='search'}">
							  	<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" /> 
							  </s:if>
							  <s:else>
							  	<input type="submit" class="buttonadd" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();return false;" />
							  </s:else>
					<!--   Action buttons have to displayed only if the page is directed from the inbox   -->
					 	<s:if test="%{sourcepage!='search'}">
						<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='re.workorder.close.confirm'/>');"/>
						</s:if>
						<s:else>
							<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
						</s:else>
					<s:if test="%{sourcepage=='inbox'}">
	  					<input type="button" class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" onclick="window.open('${pageContext.request.contextPath}/revisionEstimate/revisionWorkOrder!viewRevWorkOrderPdf.action?id=<s:property value= '%{revWorkOrderId}'/>');" />
	 			 </s:if>
				</div>
			</s:push>
		</s:form>
		<script>

		<s:if test="%{sourcepage=='search'}">	


			if(dom.get("pdfButton")!=null){
			dom.get("pdfButton").disabled=false;
		}  
			disableSelect();
		</s:if>

		</script>
</body>
</html>
