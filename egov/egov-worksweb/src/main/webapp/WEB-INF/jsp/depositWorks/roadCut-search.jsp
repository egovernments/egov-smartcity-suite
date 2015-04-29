<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>
<html>
<head>
	<title>
	<s:if test="%{sourcepage=='utilizationCertificate'}">
		<s:text name='depositworks.utilizationCertificate.title' />   
	</s:if> 
	<s:else>
		<s:text name='depositworks.roadcut.search.title' />
	</s:else>
	
	</title>
</head>

<script type="text/javascript">
function checkDepositCodeId(){
	var code = dom.get("depositCodeSearch").value;
	var id = dom.get("depositCodeId").value;
	if(code=="" && id!=""){
		document.getElementById("depositCodeId").value="";
	}
}

function gotoPage(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'applicationDetailsId');
	
	var showActions = getControlInBranch(currRow,'appDetailsActions');
	if(obj.value=='View'){	
		window.open("${pageContext.request.contextPath}/depositWorks/roadCut!view.action?mode=view&sourcepage=search&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,resizable=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Reject For Resubmit'){	
		window.open("${pageContext.request.contextPath}/depositWorks/roadCut!view.action?mode=rejectForResubmission&sourcepage=search&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,resizable=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Modify Application'){	
		window.open("${pageContext.request.contextPath}/depositWorks/roadCut!edit.action?mode=edit&sourcepage=search&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,resizable=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='View Technical Details'){	
		window.open("${pageContext.request.contextPath}/depositWorks/roadCut!view.action?mode=viewTechnicalDetails&sourcepage=search&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,resizable=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='View Abstract Estimate'){	
		window.open("${pageContext.request.contextPath}/depositWorks/roadCut!view.action?mode=viewAbstractEstimate&sourcepage=search&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,resizable=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Generate Feasibility Report'){	
		window.open("${pageContext.request.contextPath}/depositWorks/roadCut!view.action?sourcepage=genFeasibilityReport&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,resizable=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Generate damage fee communication letter'){	
		window.open("${pageContext.request.contextPath}/depositWorks/roadCut!viewDamageFeeCommunicationPDF.action?mode=view&sourcepage=damageFeePDF&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,resizable=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Generate Road-Cut Approval Letter'){

		 makeJSONCall(["receiptAmount","damageFeeEstAmount","appDetailsId"],'${pageContext.request.contextPath}/depositWorks/ajaxDepositWorks!getDamageFeeAndReceiptAmount.action',{appDetailsId:id.value},damageFeeAndRecieptAmountLoadHandler,damageFeeAndRecieptAmountLoadFailureHandler) ;			
		//window.open("${pageContext.request.contextPath}/depositWorks/roadCutApprovalLetter!newform.action?appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Print approval letter for road cut'){	
		window.open("${pageContext.request.contextPath}/depositWorks/roadCut!viewRoadCutApprovalPDF.action?mode=view&sourcepage=generateApprovalPDF&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,resizable=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Update road cut date'){	
		window.open("${pageContext.request.contextPath}/depositWorks/roadCut!view.action?mode=UpdateRoadCutDate&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Update road cut restoration date'){	
		window.open("${pageContext.request.contextPath}/depositWorks/roadCut!view.action?mode=UpdateRoadCutRestorationDate&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,resizable=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='View Road-Cut Approval Letter'){	
		window.open("${pageContext.request.contextPath}/depositWorks/roadCutApprovalLetter!view.action?sourcepage=search&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,resizable=yes,left=0,top=0,status=yes');
	}
}

damageFeeAndRecieptAmountLoadHandler = function(req,res){
	results=res.results;
	<s:if test="applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@OTHERS || applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@REPAIRSANDMAINTENANCE || applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT">
  	if(results[0].receiptAmount == 0) {
  		showMessage('roadCut_error','<s:text name="dw.receiptamount.validate.zero" />');
		window.scroll(0,0);
		return;
  	}
 	else if(eval(results[0].receiptAmount)<eval(results[0].damageFeeEstAmount)) {
  		showMessage('roadCut_error','<s:text name="dw.receiptamount.validate" />');
		window.scroll(0,0);
		return;
  	 }
 	else {
 		window.open("${pageContext.request.contextPath}/depositWorks/roadCutApprovalLetter!newform.action?appDetailsId="+results[0].appDetailsId,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
 	 	 }
 	 </s:if>
  	<s:else> {
  		window.open("${pageContext.request.contextPath}/depositWorks/roadCutApprovalLetter!newform.action?appDetailsId="+results[0].appDetailsId,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
 	}
 	</s:else>
}

damageFeeAndRecieptAmountLoadFailureHandler= function(){
	showMessage('roadCut_error','<s:text name="dw.receiptamount.error" />');
	window.scroll(0,0);
}


function gotoEditPage(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'applicationDetailsId');
	var applicationRequestStatus = getControlInBranch(currRow,'applRequestStatus');
	
	<s:if test="%{sourcepage!='utilizationCertificate'}">
		window.open("${pageContext.request.contextPath}/depositWorks/roadCut!edit.action?mode=edit&sourcepage=search&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	</s:if>
	<s:else>
		document.getElementById("appDetailsId").value=id.value;
		dom.get('serviceConn').value = getControlInBranch(currRow,'serviceConnection').value;
		if(applicationRequestStatus.value == 'UTILISATIONCERTIFICATECREATED' || applicationRequestStatus.value == 'UTILISATIONCERTIFICATEAPPROVED') {
			document.getElementById("viewUCButton").style.display='';
			document.getElementById("createUCButton").style.display='none';
		}
		else {
			document.getElementById("createUCButton").style.display='';
			document.getElementById("viewUCButton").style.display='none';
			
		}
			
	</s:else>
}


var wardSearchSelectionHandler = function(sType, arguments) { 
    var oData = arguments[2];
    dom.get("wardSearch").value=oData[0];
    dom.get("wardId").value = oData[1];
};

var wardSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="depositworks.roadcut.search.wardloading.failure"/>');
};

function generateParameter()
{
	var status = "";
	var search=document.getElementById("zoneId").value;
	status ="zoneId="+search;
	return status;
}
function clearHiddenWard(obj)
{
	if(obj.value=="")
	{
		document.getElementById("wardId").value="";
	}	
}

function clearHiddenDepositCode(obj)
{
	if(obj.value=="")
	{
		document.getElementById("depositCodeId").value="";
	}	
}

var depositCodeSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("depositCodeSearch").value=oData[0];
    dom.get("depositCodeId").value = oData[1];
};

var depositCodeSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="estimate.depositworks.search.depCodeloading.failure"/>');
};

function openEstimateScreen(){
	var id=dom.get('appDetailsId').value;
	var depositWorksCategory = dom.get('depositWorksCat').value;
	if(id==null || id==''){
		showMessage('roadCut_error','<s:text name="search.estimate.depositworks.create"/>');
		window.scroll(0,0);
	}
	else if(depositWorksCategory == '<s:property value="@org.egov.works.models.depositWorks.DepositWorksCategory@BPA" />') {
		clearMessage('roadCut_error')
		window.open("${pageContext.request.contextPath}/estimate/bpaAbstractEstimate!newform.action?appDetailsId="+id,'_self');
	}
	else{
		clearMessage('roadCut_error')
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!newform.action?sourcepage=roadCutDepositWorks&appDetailsId="+id,'_self');
	} 
}

function initAppDetailsId(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'applicationDetailsId');
	dom.get('appDetailsId').value=id.value;
	dom.get('depositWorksCat').value = getControlInBranch(currRow,'depositWorksCategory').value;
}


function createUC(){
	var appDetailsId = document.roadCutForm.appDetailsId.value;

	if(appDetailsId==null || appDetailsId==''){
		showMessage('roadCut_error','<s:text name="search.roadCut.utilization.create"/>');
		window.scroll(0,0);
	}
	else{
		var depositCategory = dom.get("depositWorksCategory").value;	
		if(depositCategory == 'BPA'){
			validateBPAAmount(appDetailsId);
		}
		else{
			clearMessage('roadCut_error')
			window.open("${pageContext.request.contextPath}/depositWorks/utilizationCertificate!newform.action?appDetailsId="+appDetailsId,'_self');
		}	
	}
}

function validateBPAAmount(appDetailsId){
	makeJSONCall(["Value","appDetailsId","bpaNumber","applicationNo"],'${pageContext.request.contextPath}/depositWorks/ajaxDepositWorks!validateForBPAAmount.action',{appDetailsId:appDetailsId},bpaAmountCheckSuccess,bpaAmountCheckFailure) ;
}

bpaAmountCheckSuccess = function(req,res){
	results=res.results;
	var checkResult='';
	var appDetailsId='';
	var bpaNo='';
	var appNo='';
	
	if(results != '') {
		checkResult =   results[0].Value;
		appDetailsId = results[0].appDetailsId;
		bpaNo = results[0].bpaNumber;
		appNo = results[0].applicationNo;
	}
	var serviceConnName = dom.get("serviceConn").value;
	if(checkResult != '' && checkResult=='yes'){
		window.open("${pageContext.request.contextPath}/depositWorks/utilizationCertificate!newform.action?appDetailsId="+appDetailsId,'_self');
	}	
	else {
		dom.get("roadCut_error").innerHTML='<s:text name="dw.bpa.utilizationCertificate.bpaAmount.validate.msg1" />'+bpaNo+' '+'<s:text name="dw.bpa.utilizationCertificate.bpaAmount.validate.msg2" />'+serviceConnName+' '+'<s:text name="dw.bpa.utilizationCertificate.bpaAmount.validate.msg3" />'+appNo+'<s:text name="dw.bpa.utilizationCertificate.bpaAmount.validate.msg4" />';
	    dom.get("roadCut_error").style.display='';
	    return false;
	}
	if(dom.get("roadCut_error").innerHTML=='<s:text name="dw.bpa.utilizationCertificate.bpaAmount.validate.msg1" />'+bpaNo+' '+'<s:text name="dw.bpa.utilizationCertificate.bpaAmount.validate.msg2" />'+serviceConnName+' '+'<s:text name="dw.bpa.utilizationCertificate.bpaAmount.validate.msg3" />'+appNo+'<s:text name="dw.bpa.utilizationCertificate.bpaAmount.validate.msg4" />')
	{
		dom.get("roadCut_error").innerHTML='';
	    dom.get("roadCut_error").style.display='none';
	}
		
}

bpaAmountCheckFailure= function(){
  dom.get("workOrder_error").style.display='';
	document.getElementById("workOrder_error").innerHTML='<s:text name="yrEnd.appr.verification.for.bill.failed" />';
}

function viewUC(){
	var appDetailsId = document.roadCutForm.appDetailsId.value;

	if(appDetailsId==null || appDetailsId==''){
		showMessage('roadCut_error','<s:text name="search.roadCut.utilization.view"/>');
		window.scroll(0,0);
	}
	else{
		clearMessage('roadCut_error')
		window.open("${pageContext.request.contextPath}/depositWorks/utilizationCertificate!view.action?sourcepage=search&appDetailsId="+appDetailsId,'_self');
	}
}

</script>

<body>
 <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
<div class="errorstyle" id="roadCut_error" style="display: none;"></div>
<s:form name="roadCutForm" action="roadCut" theme="simple" >
	<s:push value="model">
	<s:hidden name="mode" id="mode" />
	<s:hidden name="sourcepage" id="sourcepage" />
	<s:hidden name="appDetailsId" id="appDetailsId" value="%{appDetailsId}"  />
	<s:hidden name="depositWorksCat" id="depositWorksCat" value="%{depositWorksCat}"  />
	<s:hidden name="serviceConn" id="serviceConn" value="%{serviceConn}"  />
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">          
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
	        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
	            <div class="headplacer"><s:text name="depositworks.roadcut.search" /></div></td>
	        </tr>
	        
	        <tr>
				 <td width="11%" class="whiteboxwk"><s:text name="depositworks.roadcut.search.applicationno" />:</td>
		         <td width="21%" class="whitebox2wk"><s:textfield name="applicationRequest.applicationNo" value="%{applicationRequest.applicationNo}" id="applicationNo" cssClass="selectwk" /></td>				
				 <s:if test="%{mode!='edit' && mode!='showDWforEstimate' && sourcepage!='utilizationCertificate'}">
					 <td width="11%" class="whiteboxwk"><s:text name="depositworks.roadcut.search.applicationReqStatus" />:</td>
			         <td width="21%" class="whitebox2wk">
			         	<s:select id="appReqStatus" name="applicationRequest.egwStatus.code" cssClass="selectwk" 
			         	list="%{applicationRequestStatuses}" headerKey="-1" headerValue="ALL"
			         	listKey="code" listValue="description"  value="%{applicationRequest.egwStatus.code}" /> 
		         	</td>
		         </s:if>
		         <s:else>
		         	<td width="11%" class="whiteboxwk"></td>
		         	<td width="21%" class="whitebox2wk" ></td>
		         </s:else>
			</tr>
			<tr>
				 <td width="11%" class="greyboxwk"><s:text name="depositworks.roadcut.typeofcut" />:</td>
		         <td width="21%" class="greybox2wk">
					<s:select id="typeOfCut" name="applicationRequest.depositWorksType.id" cssClass="selectwk" 
						list="dropdownData.typesOfRoadCut" headerKey="-1" headerValue="%{getText('list.default.select')}"
						listKey="id" listValue="code"  value="%{applicationRequest.depositWorksType.id}" />
				 </td>				
		         <td width="11%" class="greyboxwk"><s:text name="depositworks.roadcut.schemebasedcut" />:</td>
		         <td width="21%" class="greybox2wk" >
		         	<s:radio name="applicationRequest.isSchemeBased" id="isSchemeBasedRadio" list="#{true:'Yes',false:'No'}" value="%{applicationRequest.isSchemeBased}"/>
		         </td>
			</tr>
			<tr>
				 <td width="11%" class="whiteboxwk"><s:text name="depositworks.zone" />:</td>
		         <td width="21%" class="whitebox2wk" >
		         	<s:select id="zoneId" name="zoneId" cssClass="selectwk" disabled="%{fDisabled}"
						list="dropdownData.zoneList" listKey="id" listValue='name+ " - " +parent.name' 
						headerKey="-1" headerValue="%{getText('list.default.select')}"
						value="%{zoneId}"  />	
		         </td>				
				 <td width="11%" class="whiteboxwk"><s:text name="depositworks.ward" />:</td>
		         <td width="21%" class="whitebox2wk" >
		         	<div class="yui-skin-sam">
	                <div id="wardSearch_autocomplete">
	                <div><s:textfield id="wardSearch" type="text" name="wardName" value="%{wardName}" onBlur="clearHiddenWard(this)" class="selectwk"/><s:hidden id="wardId" name="wardId" value="%{wardId}"/></div>
	                <span id="wardSearchResults"></span>
	                </div>
	                </div>
	                <egov:autocomplete name="wardSearch" width="20" field="wardSearch" url="ajaxDepositWorks!searchWardAjax.action?" paramsFunction="generateParameter" queryQuestionMark="false" results="wardSearchResults" handler="wardSearchSelectionHandler" forceSelectionHandler="wardSelectionEnforceHandler"/>
		         </td>
			</tr>

		   <tr> 
				<td width="11%" class="greyboxwk"><s:text name="depositworks.roadcut.search.fromdate" />:</td>
				<td width="21%" class="greybox2wk"><s:date name="fromDate" var="fromApplicationDate"	format="dd/MM/yyyy" />
										<s:textfield name="fromDate" id="fromDate"
											cssClass="selectwk" value="%{fromApplicationDate}"
											onfocus="javascript:vDateType='3';"
											onkeyup="DateFormat(this,this.value,event,false,'3')" />
										<a	href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
											onmouseover="window.status='Date Picker';return true;"
											onmouseout="window.status='';return true;"> <img
												src="${pageContext.request.contextPath}/image/calendar.png"
												alt="Calendar" width="16" height="16" border="0"
												align="absmiddle" />
										</a>
									<span id='errorfromDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
				</td>
				<td width="11%" class="greyboxwk"><s:text name="depositworks.roadcut.search.todate" />:</td>
				<td width="21%" class="greybox2wk"><s:date name="toDate" var="toApplicationDate"	format="dd/MM/yyyy" />
													<s:textfield name="toDate" id="toDate"
														value="%{toApplicationDate}" cssClass="selectwk"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a	href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
														onmouseover="window.status='Date Picker';return true;"
														onmouseout="window.status='';return true;"> <img
															src="${pageContext.request.contextPath}/image/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>
										<span id='errortoDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
				</td>	 
		   </tr>
		   <tr>
				<td width="11%" class="whiteboxwk">
					<s:text name="depositworks.roadcut.preparedBy" />:
				</td>
				<td class="whitebox2wk">
					<s:select headerKey="-1"
						headerValue="%{getText('list.default.select')}"
						name="empId" value="%{empId}" id="preparedBy"
						cssClass="selectwk" list="dropdownData.preparedByList"
						listKey="id" listValue="employeeName" />
	
				</td>
				<td class="whiteboxwk"><s:text name="estimate.depositworks.depositcode" />:</td>
				<td class="whitebox2wk">
				<div class="yui-skin-sam">
	                <div id="depositCodeSearch_autocomplete">
	                <div><s:textfield id="depositCodeSearch" type="text" name="depWrkCode" value="%{depWrkCode}" onBlur="clearHiddenDepositCode(this)" class="selectwk"/><s:hidden id="depositCodeId" name="depositCodeId" value="%{depositCodeId}"/></div>
	                <span id="depositCodeSearchResults"></span>
	                </div>
	                </div>
	                <egov:autocomplete name="depositCodeSearch" width="20" field="depositCodeSearch" url="ajaxDepositWorks!searchDepositCodeAjax.action?"  queryQuestionMark="false" results="depositCodeSearchResults" handler="depositCodeSearchSelectionHandler" forceSelectionHandler="depositCodeSelectionEnforceHandler"/>
				</td>
		   </tr>
		   <tr>
		   		<td width="11%" class="greyboxwk"><s:text name="depositworks.roadcut.application.type" />:</td>
		   		<td class="greybox2wk">
		   		<s:select id="applicationType" name="applicationType" headerKey="-1"
								headerValue="%{getText('list.default.select')}" cssClass="selectwk"
								list="dropdownData.applicationTypeList" listKey="value" listValue="label" />
		   			
	
				</td>
				<td colspan="2" class="greyboxwk">
		   </tr>
		   <tr>
	       		<td colspan="4" class="shadowwk"></td> 
	       </tr>
	       <tr>
	       		<td colspan="4">
	       			
	            </td>
	      	</tr>
				
		</table>
		</td>
        </tr>
	</table>
   </div><!-- end of rbroundbox2 -->
   <div class="rbbot2"><div></div></div>
   </div><!-- end of insidecontent -->
   </div><!-- end of formmainbox -->
   <div class="buttonholdersearch" align = "center">
      	<s:submit  cssClass="buttonfinal" value="SEARCH" id="saveButton" onclick="checkDepositCodeId();"	method="searchList" name="button" />
    </div>
   <div class="errorstyle" id="error_search" style="display: none;"></div>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">

		<tr><td><%@ include file='roadCut-searchresults.jsp'%></td></tr>
		
		<tr>
			<td>
				<div class="buttonholderwk">				
					<s:if test="%{sourcepage=='utilizationCertificate'}">  
						<input type="button" class="buttonadd"
							value="Create Utilization Certificate " id="createUCButton" 
							name="createUtilizationCertificate" onclick="createUC();" align="center" />
						<input type="button" class="buttonadd"
							value="View Utilization Certificate " id="viewUCButton" 
							name="viewUtilizationCertificate" onclick="viewUC();" align="center" />
						<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
					</s:if>					
				</div> 
			</td>
		</tr>

		
	</table>
   </s:push>
</s:form>
</body>
</html>