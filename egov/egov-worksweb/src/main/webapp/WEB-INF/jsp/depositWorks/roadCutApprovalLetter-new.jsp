<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
 <style type="text/css">
#container iframe {
width: 60%;
height: 615px;
border-color: #000000;
border: 1;
background:White;
align: center;
}
</style> 
<html>
<body onload="noBack();" onpageshow="if(event.persisted) noBack();" onunload="">
<head>
<title><s:text name="depositworks.roadcutApprovalLetter.title" /> </title>
</head>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>


<script type="text/javascript">
window.history.forward(1);
function noBack() {
	window.history.forward(); 
	setContractorName();
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
		if(!validateWorkFlowApprover(text)) { 
			return false;
		}
		 <s:if test="applicationDetails.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA ||(applicationDetails.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@REPAIRSANDMAINTENANCE && !applicationDetails.applicationRequest.isSchemeBased)">
			if(document.getElementById("rateContractId").value==''){
				alert('<s:text name="ratecontract.select.msg"/>');
				return false;
			}
		</s:if>
	}
	// Enable Fields				
	for(var i=0;i<document.forms[0].length;i++) {
  		document.forms[0].elements[i].disabled =false; 
  	}
  	return true;
}

function validateCancel() {
	var msg='<s:text name="dw.roadcutApprovalLetter.cancel.confirm"/>';
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
	loadDesignationByDeptAndType('RoadCutApprovalLetter',dept,currentState,amountRule,additionalRuleValue,pendingAction); 
}

function populateApprover() {
  getUsersByDesignationAndDept();
}		

function viewDocument(){
	  viewDocumentManager(dom.get("docNumber").value); 
}

function update(elemValue) {	
	if(elemValue!="" || elemValue!=null) {
		var a = elemValue.split("`~`");
		//alert(a);
		var contName=a[0];
		var rcNo=a[1];
		var validFr=a[2];
		var validTo=a[3];
		var rateContId = a[4];
		if(contName !=null && contName != '') {
		  dom.get("contractorName").value=contName;
		}
		if(contName !=null && contName != '') {
			  dom.get("rcNumber").value=rcNo;
			}
		if(contName !=null && contName != '') {
			  dom.get("validFrom").value=validFr;
			}
		if(contName !=null && contName != '') {
			  dom.get("validTo").value=validTo;
			}
		if(rateContId !=null && rateContId!='') {
			 dom.get("rateContractId").value=rateContId;
			}
	}
}

function setContractorName() {
	var obj = dom.get("contractorName");
	if(obj !=null) {
		var indexOfLastSlash = obj.value.lastIndexOf("/");
		if(indexOfLastSlash==1) {
			dom.get("contractorName").value="";
			}
		}
}

</script> 

<div class="errorstyle" id="roadcut_error" style="display: none;"></div>
<s:if test="%{hasErrors() && damageFeeEstAmount>receiptAmount}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
        <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />        
</s:if>
<s:else>
    <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
<s:form action="roadCutApprovalLetter" name="roadCutApprovalLetterForm" theme="simple">
	<s:if test="%{sourcepage!='search'}">
		<s:token /> 
	</s:if>

	<s:push value="model">
		<s:hidden name="sourcepage" id="sourcepage" />
		<s:hidden name="documentNumber" id="docNumber" />
		<s:hidden name="zoneIdsString" id="zoneIdsString" value="%{zoneIdsString}" /> 
		<s:hidden name="wardIdsString" id="wardIdsString" value="%{wardIdsString}" />  
  		<s:hidden name="forwardNotApplicable" id="forwardNotApplicable" />
		<s:hidden name="id" id="id" value="%{id}" />
		<s:hidden name="appDetailsId" id="appDetailsId" />  
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
								<td colspan="100%" class="headingwk">
									<div class="arrowiconwk">
										<img src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headplacer">
										<s:text
											name="depositworks.roadcutApprovalLetter.details.title" />
									</div>
								</td>
							</tr>
								<tr>
									<td width="11%" class="whiteboxwk"><s:text
											name="depositworks.applicationreq.number" />:</td>
									<td class="whitebox2wk" colspan="2"><a href="#"
										onclick="showApplicationRequestDetails(<s:property  value="applicationDetails.id"/>)"><s:property
												value="%{applicationDetails.applicationRequest.applicationNo}" /></a></td>
									<s:if
										test="applicationDetails.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA && (model.egwStatus==null || model.egwStatus.code=='REJECTED')  && (sourcepage !='search')">
										<s:hidden name="rateContractId" id="rateContractId"
											value="%{rateContractId}" />
										<td width="75%" class="whiteboxwk"><a href="#"
											onClick="window.open('${pageContext.request.contextPath}/rateContract/searchRateContract!newform.action?zoneIdsString=<s:property  value="zoneIdsString"/>&wardIdsString=<s:property  value="wardIdsString"/>','SearchRateContractWindow','width=900,height=700')">
												<s:text name="ratecontract.add.details" /><img
												id="addrowImg" name="addrowImg"
												src='${pageContext.request.contextPath}/images/addrow.gif'
												width="18" height="18" border="0" " />
										</a></td>
									</s:if>
									
									<s:if
										test="applicationDetails.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@REPAIRSANDMAINTENANCE && !applicationDetails.applicationRequest.isSchemeBased
										  		&& (model.egwStatus==null || model.egwStatus.code=='REJECTED')  && (sourcepage !='search')">
										<s:hidden name="rateContractId" id="rateContractId"
											value="%{rateContractId}" />
											<td width="75%" class="whiteboxwk"><a href="#"
											onClick="window.open('${pageContext.request.contextPath}/rateContract/searchRateContract!newform.action?zoneIdsString=<s:property  value="zoneIdsString"/>&wardIdsString=<s:property  value="wardIdsString"/>&repairAndMaintainance=true&isMunicipalFund=<s:property value="municipalFund"/>','SearchRateContractWindow','width=900,height=700')">
												<s:text name="ratecontract.add.details" /><img
												id="addrowImg" name="addrowImg"
												src='${pageContext.request.contextPath}/images/addrow.gif'
												width="18" height="18" border="0" " />
											</a></td>
									</s:if>
									<s:if
										test="applicationDetails.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT
										  		&& (model.egwStatus==null || model.egwStatus.code=='REJECTED')  && (sourcepage !='search')">
										<s:hidden name="rateContractId" id="rateContractId"
											value="%{rateContractId}" />
											<td width="75%" class="whiteboxwk"><a href="#"
											onClick="window.open('${pageContext.request.contextPath}/rateContract/searchRateContract!newform.action?zoneIdsString=<s:property  value="zoneIdsString"/>&wardIdsString=<s:property  value="wardIdsString"/>&isEmergencyCut=yes','SearchRateContractWindow','width=900,height=700')">
												<s:text name="ratecontract.add.details" /><img
												id="addrowImg" name="addrowImg"
												src='${pageContext.request.contextPath}/images/addrow.gif'
												width="18" height="18" border="0" " />
											</a></td>
									</s:if>
									<s:if
										test="applicationDetails.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA
										|| applicationDetails.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT 
										|| (applicationDetails.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@REPAIRSANDMAINTENANCE && !applicationDetails.applicationRequest.isSchemeBased)">
											<td width="75%" class="whiteboxwk"><a href="#"
												onClick="window.open('${pageContext.request.contextPath}/rateContract/rateContractAssignment!search.action?zoneIdsString=<s:property  value="zoneIdsString"/>&wardIdsString=<s:property  value="wardIdsString"/>','RateContractAssignmentWindow','width=900,height=700')">
													<s:text name="ratecontract.assignment.link.label" /><img
													id="addrowImg" name="addrowImg"
													src='${pageContext.request.contextPath}/image/book_open.png'
													width="18" height="18" border="0" " />
											</a></td>
									</s:if>
									<td width="100%" class="whiteboxwk">&nbsp;</td>
									</tr>

								<tr colspan="4"><td>&nbsp;</td></tr>
						</table>
						<s:if test="applicationDetails.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA
							|| applicationDetails.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT
							|| (applicationDetails.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@REPAIRSANDMAINTENANCE && !applicationDetails.applicationRequest.isSchemeBased) ">
							<table id="rateContractDetails">
								<tr>
									<td colspan="100%" class="headingwk">
										<div class="arrowiconwk">
											<img src="${pageContext.request.contextPath}/image/arrow.gif" />
										</div>
										<div class="headplacer">
											<s:text name="ratecontract.estimate.header" />
										</div>
									</td>
								</tr>
								<tr>
									<th class="pagetableth" width="30%"><s:text
											name="ratecontract.search.contractornamecode" />
									</td>
									<th class="pagetableth" width="30%"><s:text
											name="ratecontract.rcNo.label" />
									</td>
									<th class="pagetableth" width="30%"><s:text
											name="ratecontract.validfrom.label" />
									</td>
									<th class="pagetableth" width="30%"><s:text
											name="ratecontract.validto.label" />
									</td>
								</tr>
								<tr>
									<td class="whitebox4wk"><s:textfield id="contractorName" size="50"
											name="contractorName" readonly="true" value="%{applicationDetails.applicationRequest.rateContract.contractor.name} / %{applicationDetails.applicationRequest.rateContract.contractor.code}" /></td>
									<td class="whitebox4wk"><s:textfield id="rcNumber"
											name="rcNumber" readonly="true" value="%{applicationDetails.applicationRequest.rateContract.rcNumber}"/></td>
											<s:date name="applicationDetails.applicationRequest.rateContract.indent.startDate" var="startDateFormat" format="dd/MM/yyyy" />
									<td class="whitebox4wk"><s:textfield id="validFrom"
											name="validFrom"  value="%{startDateFormat}" readonly="true" /></td>
											<s:date name="applicationDetails.applicationRequest.rateContract.indent.startDate" var="endDateFormat" format="dd/MM/yyyy" />
									<td class="whitebox4wk"><s:textfield id="validTo"
											name="validTo" value="%{endDateFormat}" readonly="true" /></td>
								</tr>
							</table>
							</s:if>
							<div> &nbsp;</div>
						<div> &nbsp;</div>
						<div id="container" align="center">
						<s:if test="%{id!=null && id!=''}">
							<iframe id="report" onload="setLogo()"  name="report"
								src='${pageContext.request.contextPath}/depositWorks/roadCutApprovalLetter!ajaxPrint.action?id=<s:property value="id"/>'></iframe>
						</s:if>
						<s:else>
							<iframe id="report" onload="setLogo()"  name="report"
							src='${pageContext.request.contextPath}/depositWorks/roadCutApprovalLetter!ajaxPrint.action?appDetailsId=<s:property value="applicationDetails.id"/>'></iframe>
						</s:else>
						</div>
						<table width="100%" cellspacing="0" cellpadding="0" border="0">
						<tr colspan="4"><td>&nbsp;</td></tr>
						<tr>
							<td class="shadowwk" colspan="4"></td>
						</tr>
						<s:if test="%{sourcepage !='search'}">
						<tr>
							<td colspan="4">
								<div id="manual_workflow">
									<c:set var="approverHeadCSS" value="headingwk" scope="request" />
									<c:set var="approverCSS" value="bluebox" scope="request" />
									<s:hidden name="departmentName" id="departmentName"
										value="%{departmentName}" />
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
			<s:if test="%{hasErrors() || hasActionMessages() || (sourcepage=='inbox'  || model.egwStatus==null || model.egwStatus.code=='NEW'  
			|| model.egwStatus.code=='REJECTED')  && (sourcepage !='search')}">  
					 						
				<s:iterator value="%{getValidActions()}" var="name">
					<s:if test="%{name!=''}">
						<s:submit type="submit" cssClass="buttonfinal"
							value="%{name}" id="%{name}" name="%{name}"
							method="save"
							onclick="document.roadCutApprovalLetterForm.actionName.value='%{name}';return validateDataBeforeSubmit('%{name}');" />
					</s:if>
				</s:iterator>
				
				</s:if>
				<s:if test="%{sourcepage!='search'}">		
				<input type="button" class="buttonadd" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();return false;" />
				</s:if>
				<s:if test="%{sourcepage=='search'}">
					<input type="button" class="buttonadd" value="View Document"
						id="docViewButton" onclick="viewDocument();return false;" />
				</s:if>
				<s:if test="%{sourcepage=='search' && (model.egwStatus.code=='APPROVED' || model.egwStatus.code=='CANCELLED')}">			 
				<input type="button" onclick="window.open('${pageContext.request.contextPath}/depositWorks/roadCutApprovalLetter!viewRoadCutApprovalPDF.action?appDetailsId=<s:property  value="applicationDetails.id"/>');"
					class="buttonpdf" value="PRINT" id="pdfButton" name="pdfButton" /> 
					</s:if>
				<s:if test="%{(sourcepage=='inbox' || sourcepage=='search')}">
					<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue=<s:property value="state.id"/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"
						class="buttonfinal" value="Workflow History" id="history" name="History" />
				</s:if>
				<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
			</div>

		</div>
	</s:push>
</s:form>
</s:else>
</body>
</html>