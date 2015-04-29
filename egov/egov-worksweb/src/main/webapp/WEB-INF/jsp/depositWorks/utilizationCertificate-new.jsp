<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
 <style type="text/css">
#container iframe {
width: 60%;
height: 630px;
border-color: #000000;
border: 1;
background:White;
align: center;
}
</style> 
<html>
<body onload="noBack();" onpageshow="if(event.persisted) noBack();" onunload="">
<head>
<title><s:text name="depositworks.utilizationCertificate.title" /> </title>
</head>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>
<script type="text/javascript">

window.history.forward(1);
function noBack() {
	window.history.forward(); 
	hideForward();
}
function setLogo() {
	try {
		document.getElementById('report').contentWindow.document.getElementsByTagName('img')[0].src = '/egi/images/<s:property value="logoforHTML"/>';
	} catch (e) {
	}
}


function hideForward()
{
	var hideForward = <s:property value="%{forwardNotApplicable}"/>;
	 if(hideForward==true && document.getElementById('Forward')!=null && document.getElementById('Reject')!=null) {
		 jQuery("#Forward").hide();
		 jQuery("#Reject").hide();
	 }
	 if(hideForward==true && document.getElementById('Approve')!=null)
		 jQuery("#Approve").hide();
}
function showApplicationRequestDetails(id){ 
	window.open("${pageContext.request.contextPath}/depositWorks/roadCut!view.action?mode=view&sourcepage=search&appDetailsId="+id,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function validateDataBeforeSubmit(text) {
	if(document.getElementById("actionName").value=='Cancel'){
		if(!validateCancel()){
		return false;
		}
	}

	
	if(text!='Approve' && text!='Reject' ){
		if(!validateWorkFlowApprover(text))
			return false;
	}

	// Enable Fields				
	for(var i=0;i<document.forms[0].length;i++) {
  		document.forms[0].elements[i].disabled =false; 
  	}
  	return true;
}

function validateCancel() {
	var msg='<s:text name="dw.utilizationCertificate.cancel.confirm"/>';
	var applNo='<s:property value="applicationDetails.applicationRequest.applicationNo"/>';
	if(!confirmCancel(msg,applNo)) {
		return false;
	}
	else {
		return true;
	}
}

function loadDesignationFromMatrix(){ 
	var dept=dom.get('departmentName').value;
	var currentState = dom.get('currentState').value;
	var amountRule =  dom.get('amountRule').value;
	var additionalRuleValue =  dom.get('additionalRuleValue').value; 
	var pendingAction=document.getElementById('pendingActions').value;
	loadDesignationByDeptAndType('UtilizationCertificate',dept,currentState,amountRule,additionalRuleValue,pendingAction); 
}

function populateApprover() {
  getUsersByDesignationAndDept();
}		

function viewDocument(){
	  viewDocumentManager(dom.get("docNumber").value); 
}

</script> 

<div class="errorstyle" id="utilizationCertificate_error" style="display: none;"></div>
<s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
</s:if>
<s:form action="utilizationCertificate" name="utilizationCertificateForm" theme="simple">
	<s:if test="%{sourcepage!='search'}">
		<s:token /> 
	</s:if>

	<s:push value="model">
		<s:hidden name="sourcepage" id="sourcepage" />
		<s:hidden name="documentNumber" id="docNumber" />  
		<s:hidden name="id" id="id" />
		<s:hidden name="appDetailsId" id="appDetailsId" />  
		<s:hidden name="forwardNotApplicable" id="forwardNotApplicable" />
		<input type="hidden" name="actionName" id="actionName"/>
		<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
		<div class="formmainbox">
			<div class="insidecontent">
				<div class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
					<div class="rbcontent2">

						<table width="100%" cellspacing="0" cellpadding="0" border="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk"> 
										<img src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headplacer">
										<s:text
											name="depositworks.utilizationCertificate.details.title" />
									</div>
								</td>
							</tr>
							<tr><td colspan="4">&nbsp;</td></tr>
							<tr>
								<td width="11%" class="whiteboxwk"><s:text name="depositworks.applicationreq.number" />:</td>
								<td width="21%" class="whitebox2wk"><a href="#" onclick="showApplicationRequestDetails('<s:property  value="applicationDetails.id"/>')">
										<s:property value="%{applicationDetails.applicationRequest.applicationNo}" /> </a></td>
								
							</tr>
							<tr><td colspan="4">&nbsp;</td></tr>
							
							<s:if test="%{utilizationDetails.keySet().size() != 0}">
							<tr align="center">
								<td colspan="4" align="center">
									<div align="center">
										<table width="60%" border="0" cellpadding="0" cellspacing="0" align="center">
											<tr>
												<td width="8%" class="tablesubheadwk">
													<s:text name="column.title.SLNo" />
												</td>
												<td width="20%" class="tablesubheadwk">
														<s:text name="dw.utilizationCertificate.estNo" />
												</td>
												<td width="20%" class="tablesubheadwk">
														<s:text name="dw.utilizationCertificate.estAmount" />
												</td>
												<td width="20%" class="tablesubheadwk">
														<s:text name="dw.utilizationCertificate.projectCode" />
												</td>
												<td width="20%" class="tablesubheadwk">
														<s:text name="dw.utilizationCertificate.expIncurred" />
												</td>
											</tr>
    										<s:iterator var="estimate" value="utilizationDetails.estimateList" status="status">  
												<tr>
													<td width="8%" class="whitebox3wka">
														<s:property value="#status.count" />
													</td>
													<td width="20%" class="whitebox3wka">
		    												<s:property value="#estimate.estimateNumber" /> 
													</td>
													<td width="20%" class="whiteboxamount" align="right">
													<s:text name="contractor.format.number" >
															<s:param name="rate" value='%{#estimate.estimateAmount}' />
														</s:text>
													</td>
													<td width="20%" class="whitebox3wka" >														
		    											<s:property value="#estimate.projectCode" />
													</td>
													<td width="20%" class="whiteboxamount" align="right" >
														<s:text name="contractor.format.number">
															<s:param name="rate" value='%{#estimate.expenseAmount}' />
														</s:text>
													</td>
												</tr>
											</s:iterator>
												<tr>
													<td width="8%" class="whitebox3wka">
														&nbsp;
													</td>
													<td width="20%" class="whitebox3wka">
		    												&nbsp;
													</td>
													<td width="20%" class="whitebox3wka">
		    												&nbsp;
													</td>
													<td width="20%" class="whitebox3wka" >  
														<b><s:text name="dw.utilizationCertificate.amountUtilized" /></b>
													</td>
													<td width="20%" class="whiteboxamount" align="right" > 
														<s:text name="contractor.format.number" >
															<s:param name="rate" value='%{utilizationDetails.totalUtilizedAmount}'/>
														</s:text>
													</td> 
												</tr>
										</table>
									</div>
								</td>
							</tr>
							</s:if>
							<tr><td colspan="4">&nbsp;</td></tr>
							<tr>
								<td class="shadowwk" colspan="4"></td>
							</tr>
						</table>
						<div> &nbsp;</div>
						<div> &nbsp;</div>
						<div id="container" align="center">
							<iframe id="report" onload="setLogo()" name="report"
								src='${pageContext.request.contextPath}/depositWorks/utilizationCertificate!exportHtml.action?appDetailsId=<s:property value="applicationDetails.id"/>'></iframe>
						</div>
						<table width="100%" cellspacing="0" cellpadding="0" border="0">
						<tr><td colspan="4">&nbsp;</td></tr>
						<tr>
							<td class="shadowwk" colspan="4"></td>
						</tr>
						<s:if test="%{sourcepage !='search'}">
							<tr>
								<td colspan="4">
									<div id="manual_workflow">
										<c:set var="approverHeadCSS" value="headingwk" scope="request" />
										<c:set var="approverCSS" value="bluebox" scope="request" />
										<s:hidden name="departmentName" id="departmentName" value="%{departmentName}" />
										<%@ include file="/commons/commonWorkflow.jsp"%>
									</div>
								</td>
							</tr>
						</s:if>
						</table>
					</div>  
				<div class="rbbot2"><div></div></div>
			</div>
			</div>
			<div class="buttonholderwk" id="buttons">
			<s:if test="%{(sourcepage=='inbox' || model.egwStatus==null || model.egwStatus.code=='NEW'  
			|| model.egwStatus.code=='REJECTED')  && (sourcepage !='search') || hasErrors() || hasActionMessages()}">  
					 						
				<s:iterator value="%{getValidActions()}" var="name">
					<s:if test="%{name!=''}">
						<s:submit type="submit" cssClass="buttonfinal"
							value="%{name}" id="%{name}" name="%{name}"
							method="save"
							onclick="document.utilizationCertificateForm.actionName.value='%{name}';return validateDataBeforeSubmit('%{name}');" />
					</s:if>
				</s:iterator>
				
				</s:if>
				<s:if test="%{sourcepage=='search' && model.egwStatus.code=='APPROVED'}">	
				<input type="button" onclick="window.open('${pageContext.request.contextPath}/depositWorks/utilizationCertificate!viewUtilizationCertificatePDF.action?appDetailsId=<s:property  value="applicationDetails.id"/>');"
					class="buttonpdf" value="PRINT" id="pdfButton" name="pdfButton" />
				</s:if>
				<s:if test="%{sourcepage!='search'}">
					<input type="submit" class="buttonadd" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();return false;" />
				</s:if>
				<s:if test="%{sourcepage=='search'}">
					<input type="button" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" />
				</s:if>
				
				<s:if test="%{(sourcepage=='inbox' || sourcepage=='search')}">
					<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue=<s:property value="state.id"/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"
						class="buttonfinal" value="Workflow History" id="history" name="History" />
				</s:if>		
								
				<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
			</div>

		</div>
	</s:push>
</s:form>
</body>
</html>
