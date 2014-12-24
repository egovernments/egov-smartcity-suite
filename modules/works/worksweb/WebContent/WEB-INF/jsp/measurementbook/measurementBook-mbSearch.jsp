<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>

<html>
	<head>
	    <title><s:text name="measurementbook.title.mb.search" /></title>
	</head>
<style>
.ui-button
{
	position: absolute;
	height: 2.0em;
}
</style>
<script>
function validateAndSubmit() {
   if(dom.get('mbStatus').value==-1 && dom.get('contractorId').value==-1 && dom.get('workOrderNumber').value==""
   && dom.get('mbRefNo').value=="" &&  dom.get('fromDate').value==""  &&  dom.get('toDate').value==""){
	 	dom.get("mbSearch_error").style.display="block";
		document.getElementById("mbSearch_error").innerHTML='<s:text name="measurementbook.search.mandatory.error" />'; 
       	return false; 
	 }
	else if(!dom.get('fromDate').value==""  && !validateDateFormat(document.mbSearchForm.fromDate)) {	
			dom.get('errorfromDate').style.display='none';
			dom.get("mbSearch_error").style.display='block';
			document.getElementById("mbSearch_error").innerHTML='<s:text name="invalid.fieldvalue.mbDate" />'; 
			document.mbSearchForm.fromDate.focus();
		   	return false;
	}
	else if(!dom.get('toDate').value==""  && !validateDateFormat(document.mbSearchForm.toDate)) {	
			dom.get('errortoDate').style.display='none';
			dom.get("mbSearch_error").style.display='block';
			document.getElementById("mbSearch_error").innerHTML='<s:text name="invalid.fieldvalue.mbDate" />';
			document.mbSearchForm.toDate.focus();
		   	return false;
	}
	else{
			dom.get("mbSearch_error").style.display='none';		
	}
	document.getElementById('contractorId').disabled=false;
	document.mbSearchForm.action='${pageContext.request.contextPath}/measurementbook/measurementBook!searchMB.action';
 	document.mbSearchForm.submit();
}

function setupWorkOrderDetails(){
    workorder_no = document.getElementById('workOrderNumber').value;
    makeJSONCall(["xWoId","xContractorId"], 
    	'${pageContext.request.contextPath}/measurementbook/ajaxMeasurementBook!workOrderDetails.action',
    	{workOrderNumber:workorder_no},myWOSuccessHandler,myWOFailureHandler) ;
}

myWOSuccessHandler = function(req,res){
    var resultdatas=res.results;
    if(resultdatas[0].xContractorId!=''){
		document.getElementById('contractorId').value=resultdatas[0].xContractorId;
		document.getElementById('contractorId').disabled=true;
	}else{
		document.getElementById('contractorId').value=-1;
		document.getElementById('contractorId').disabled=false;
	}
}

myWOFailureHandler= function(){
	// No Action
}

function gotoPage(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'mbookId');
	var measurementBookStateId=getControlInBranch(currRow,'mbookStateId');	
    var document_Number = getControlInBranch(currRow,'docNo');
	
	var mbDate = getControlInBranch(currRow,'mbookDate');
	var mBookNo = getControlInBranch(currRow,'mbNo');
	
	var showActions = getControlInBranch(currRow,'showMBActions');
	if(showActions[1]!=null && obj.value==showActions[1].value){	
		window.open("${pageContext.request.contextPath}/measurementbook/measurementBook!edit.action?id="+id.value+"&mode=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(showActions[2]!=null && obj.value==showActions[2].value){	
	
	window.open("${pageContext.request.contextPath}/measurementbook/measurementBookPDF.action?measurementBookId="+id.value+
		"&mode=search",'');
	}
	if(showActions[3]!=null && obj.value==showActions[3].value){
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+
		measurementBookStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(showActions[4]!=null && obj.value==showActions[4].value){  
		viewDocumentManager(document_Number.value);return false;
	}
}	

function onBodyLoad(){
	if(document.getElementById('workOrderNumber').value!=''){
		document.getElementById('contractorId').disabled=true;
	}
	else{
		document.getElementById('contractorId').disabled=false;
	}
	
}

function disableSelect(){
	document.getElementById('mbStatus').disabled=true
}

function enableSelect(){
	document.getElementById('mbStatus').disabled=false
}

function setMBId(elem){
	var currRow=getRow(elem);
	if(getControlInBranch(currRow,'billId')!=null)
		dom.get("billRegisterId").value = getControlInBranch(currRow,'billId').value;
	dom.get("revEstimateNumber").value = getControlInBranch(currRow,'revEstNo').value;
	if(getControlInBranch(currRow,'billNo')!=null)
		dom.get("billNumber").value = getControlInBranch(currRow,'billNo').value;
	dom.get("mbRefNumber").value = getControlInBranch(currRow,'mbNo').value;
	dom.get("mbId").value = elem.value; 
}

function cancelMeasurementBook(){
	var id = document.mbSearchForm.mbId.value; 
	var cancelRemarks = document.mbSearchForm.cancelRemarks.value; 
	var billId = document.mbSearchForm.billRegisterId.value;
	var billNumber = document.mbSearchForm.billNumber.value;
	var revEstno = document.mbSearchForm.revEstimateNumber.value;
	if(billId != ''){
		dom.get("mbSearch_error").style.display='';
		document.getElementById("mbSearch_error").innerHTML='<s:text name="cencelMb.bill.created.message"/>'+billNumber;
		window.scroll(0,0);
		return false;
	}
	if(revEstno != ''){
		dom.get("mbSearch_error").style.display='';
		document.getElementById("mbSearch_error").innerHTML='<s:text name="cencelMb.re.created.message"/>'+revEstno;
		window.scroll(0,0);
		return false;
	}	
	if(id!=''){		
		if(validateCancel())
			window.open('${pageContext.request.contextPath}/measurementbook/measurementBook!cancelApprovedMB.action?mbId='+id+'&sourcepage=cancelMB&cancelRemarks='+cancelRemarks,'_self');
		else
			return false;
		}		
	else{
		dom.get("mbSearch_error").style.display='';
		document.getElementById("mbSearch_error").innerHTML='<s:text name="mb.not.selected" />';
		window.scroll(0,0);
		return false;
	  }
	  dom.get("mbSearch_error").style.display='none';
	  document.getElementById("mbSearch_error").innerHTML='';
	  if(dom.get("errorstyle")){
	  	dom.get("errorstyle").style.display='none';
	 	dom.get("errorstyle").innerHTML='';
	}
}	

function validateCancel() {
	var msg='<s:text name="measurementbook.cancel.confirm"/>';
	var mbNo=dom.get("mbRefNumber").value; 
	if(!confirmCancel(msg,mbNo)) {
		return false;
	}
	else {
		return true;
	}
}

</script>

	<body onload="onBodyLoad();init();">
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<div class="messagestyle">
				<s:actionmessage theme="simple" />
			</div>
		</s:if>
		<s:form action="measurementBook" theme="simple"	name="mbSearchForm"  onsubmit="if(!validateDateFormat(document.mbSearchForm.mbDate)){return false;}; enableSelect()">
			<div class="errorstyle" id="mbSearch_error" style="display:none;"></div>
			<s:hidden id="sourcepage" name="sourcepage" />
			<s:hidden id="mbId" name="mbId" />
			<s:hidden id="mbRefNumber" name="mbRefNumber" />
			<s:hidden id="billRegisterId" name="billRegisterId" />
			<s:hidden id="billNumber" name="billNumber" />
			<s:hidden id="revEstimateNumber" name="revEstimateNumber" />
			<div class="navibarshadowwk">
			</div>
			<div class="formmainbox">
			<div class="insidecontent">
			<div class="rbroundbox2">
			<div class="rbtop2"><div></div>
			</div>
			<div class="rbcontent2">
			<table id="formTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table id="mbSearchTable" width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headplacer">
										<s:text name='title.search.criteria' />
									</div>
								</td>
							</tr>
							<tr>
								<td width="11%" class="whiteboxwk">
									<s:text name="measurementbook.search.status" />:
								</td>
								<td width="21%" class="whitebox2wk">
									<s:select id="mbStatus" name="mbStatus" cssClass="selectwk" list="%{mbStatusList}" value="%{mbStatus}" 
									headerKey="-1" headerValue="ALL" listKey="code" listValue="description"/>
								</td>
								<s:if test="%{sourcepage=='cancelMB'}">
										<script>disableSelect();</script>
									<td width="11%" class="whiteboxwk">
										<s:text name="measurementbook.search.estNO" />:
									</td>
									<td width="21%" class="whitebox2wk">
										<s:textfield id="estimateNo" name="estimateNo" value="%{estimateNo}" cssClass="selectboldwk" />
									</td>
								</s:if>									
							</tr>
							<tr>								
								<td width="11%" class="greyboxwk">
									<s:text name="measurementbook.search.woNo" />:
								</td>
								<td width="21%" class="greybox2wk">
									<s:textfield id="workOrderNumber" name="workorderNo" value="%{workorderNo}" cssClass="selectboldwk" onchange="setupWorkOrderDetails();"/>
								</td>
								<td width="11%" class="greyboxwk">
									<s:text name="measurementbook.search.refno" />:
								</td>
								<td width="21%" class="greybox2wk">
									<s:textfield name="mbRefNo" value="%{mbRefNo}" id="mbRefNo" cssClass="selectwk"/>
								</td>								
								
							</tr>	
							<tr>
								<td width="15%" class="whiteboxwk">
									<s:text name="measurementbook.search.fromdate" />:
								</td>
								<td class="whitebox2wk">
									<s:date name="fromDate" var="fromMBDateFormat"
										format="dd/MM/yyyy" />
									<s:textfield name="fromDate" id="fromDate"
										cssClass="selectwk" value="%{fromMBDateFormat}"
										onfocus="javascript:vDateType='3';"
										onkeyup="DateFormat(this,this.value,event,false,'3')" />
									<a href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
										onmouseover="window.status='Date Picker';return true;"
										onmouseout="window.status='';return true;"> <img
											src="${pageContext.request.contextPath}/image/calendar.png"
											alt="Calendar" width="16" height="16" border="0"
											align="absmiddle" />
									</a>
									<span id='errorfromDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
								</td>
												
								<td width="17%" class="whiteboxwk">
										<s:text name="measurementbook.search.todate" />:
									</td>
									<td width="17%" class="whitebox2wk">
										<s:date name="toDate" var="toMBDateFormat"
											format="dd/MM/yyyy" />
										<s:textfield name="toDate" id="toDate"
											value="%{toMBDateFormat}" cssClass="selectwk"
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
										<span id='errortoDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
									</td>	
								</tr>
								<tr>
									<td width="11%" class="greyboxwk">
										<s:text name="measurementbook.search.contractor" />:
									</td>
									<td width="31%" class="greybox2wk" colspan="3">
										<s:select id="contractorId" name="contractorId"
																cssClass="selectwk"
																list="%{contractorForApprovedWorkOrder}"
																headerKey="-1"															
																headerValue=" "
																value="%{contractorId}"/>	
									</td>								
								</tr>
								
								<tr>
									<td class="shadowwk" colspan="4"/>
								</tr>
									
							<tr>
								<td colspan="4">
									<div class="buttonholdersearch">
										<input type="submit" class="buttonadd" value="Search" id="searchButton" 
											onclick="return validateAndSubmit();enableSelect()" />
										<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button"
											onclick="window.close();" />
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="4">
								<div>
									<table width="100%" border="0" cellspacing="0"
										cellpadding="0">
										<tr>
											<td colspan="7" class="headingwk">
												<div class="arrowiconwk">
													<img
														src="${pageContext.request.contextPath}/image/arrow.gif" />
												</div>
												<div class="headplacer">
													<s:text name="title.search.result" />
												</div>
											</td>
										</tr>
													
												</table>
												</div>
								<%@ include file='mbSearch-list.jsp'%>
																</td>
															</tr>
											<s:if test="%{sourcepage.equals('cancelMB')}"> 
												<tr>												   
												  <td align="left" class="whitebox2wk">
													<b><span class="mandatory">*</span><s:text name="cancel.remarks" />:</b>&nbsp;&nbsp;<s:select id="cancelRemarks" name="cancelRemarks" cssClass="selectwk" list="#{'DATA ENTRY MISTAKE':'DATA ENTRY MISTAKE','OTHER':'OTHER'}" />
												  </td>	
												</tr>
												<tr>
													<td colspan="4">
														<div class="buttonholderwk">
														<input type="button" class="buttonadd"
															value="Cancel MB" id="addButton"
															name="cancelMB" onclick="cancelMeasurementBook()"
															align="center" />
														</div>
													</td>
												</tr>
												</s:if>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="shadowwk"></td>
				</tr>
			</table>
			</div>
			<div class="rbbot2"><div></div>
			</div>
			</div>
			</div>
			</div>
		</s:form>
		<script type="text/javascript">
			
						
		</script>
	</body>
</html>
