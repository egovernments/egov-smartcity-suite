<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<html>
	<title><s:text name="tenderNegotiation.list" />
	</title>
	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>

	<script type="text/javascript">	 
function setworkorderId(obj){
	var currRow=getRow(obj);
	dom.get("tenderRespId").value=getControlInBranch(currRow,'tenderResponseId').value;
	dom.get("tenderRespContrId").value=getControlInBranch(currRow,'tenderResponseContractorsId').value; 
}

function gotoWorkorder(){
var id = document.tenderNegotiation.tenderRespId.value;
var tndrRespContrId = document.tenderNegotiation.tenderRespContrId.value; 
if(id!='' && tndrRespContrId!=''){
	window.open('${pageContext.request.contextPath}/workorder/workOrder!newform.action?tenderRespId='+id+"&tenderRespContrId="+tndrRespContrId,'_self');
}
else{
	dom.get("negotiationDate_error").style.display=''; 
	document.getElementById("negotiationDate_error").innerHTML='<s:text name="tenderResponse.not.selected" />';  
	return false;
  }
  dom.get("negotiationDate_error").style.display='none';
  document.getElementById("negotiationDate_error").innerHTML='';
}

function checkDate(obj){
	if(!validateDateFormat(obj)) {
    	dom.get("negotiationDate_error").style.display='block'; 
		document.getElementById("negotiationDate_error").innerHTML='<s:text name="tenderResponse.negotiationDate.invalid" />';
		document.tenderNegotiation.fromDate.focus();
    	return;
	}
	else {
		dom.get("negotiationDate_error").style.display='none';
		dom.get("negotiation_error").style.display='none';
	}
}
	 
	function validate(){
	   var status=dom.get('status');
	   var wpNumber=dom.get('wpNumber');
	   var tenderFileNumber=dom.get('tenderFileNumber');
	   var departmentId=dom.get('departmentId');
	   var estimateNumber=dom.get('estimateNumber');
	   var fromDate=dom.get('fromDate');
	   var negotiationNumber=dom.get('negotiationNumber');
	   var toDate=dom.get('toDate');
	  if(tenderFileNumber!=null && tenderFileNumber.value!="" && tenderFileNumber.value.length < 4){
		  	dom.get("negotiationDate_error").style.display='block';
			document.getElementById("negotiationDate_error").innerHTML='<s:text name="tenderResponse.tenderFileNumber.length" />';
		    return false;
	   }else if(wpNumber!=null &&  dom.get('wpNumber').value!="" && wpNumber.value.length < 4 ){
	        dom.get("negotiationDate_error").style.display='block';
			document.getElementById("negotiationDate_error").innerHTML='<s:text name="tenderResponse.wpNumber.length" />';
			return false;
	   }else if(estimateNumber!=null  && dom.get('estimateNumber').value!="" && estimateNumber.value.length<4){
	        dom.get("negotiationDate_error").style.display='block';
			document.getElementById("negotiationDate_error").innerHTML='<s:text name="tenderResponse.estimateNumber.length" />';
			return false;
	   }else if(negotiationNumber.value!="" && negotiationNumber.value.length<4){
	      dom.get("negotiationDate_error").style.display='block';
		  document.getElementById("negotiationDate_error").innerHTML='<s:text name="tenderResponse.negotiationNumber.length" />';
		  return false;
	   }
	   if(fromDate.value!='' && toDate.value==''){
	   	  dom.get("negotiationDate_error").style.display='block';
		  document.getElementById("negotiationDate_error").innerHTML='<s:text name="search.endDate.null" />';
		  return false;
	   }
	   else if(fromDate.value=='' && toDate.value!=''){
	   	  dom.get("negotiationDate_error").style.display='block';
		  document.getElementById("negotiationDate_error").innerHTML='<s:text name="search.startDate.null" />';
		  return false;
	   }
	   else if(compareDate(formatDate6(fromDate.value),formatDate6(toDate.value)) == -1 ){
	  	  dom.get("negotiationDate_error").style.display='block';
		  document.getElementById("negotiationDate_error").innerHTML='<s:text name="greaterthan.endDate.fromDate" />';
		  return false;
	   }
	   
	   if(wpNumber!=null && estimateNumber!=null && wpNumber.value!="" && estimateNumber.value!=""){
	   dom.get("negotiationDate_error").style.display='block';
	   document.getElementById("negotiationDate_error").innerHTML='Please enter either Estimate number or works package number';
	   return false;
	   }
	   
	  if(status.value=='-1' && departmentId.value=='-1' && fromDate.value=="" && toDate.value=='' && negotiationNumber.value=="" && 
	  dom.get("projectCode").value==""){
	     var iserror='true';
	      <s:if test="%{tenderCretedBy.toLowerCase().equals('estimate')}">
	         if(estimateNumber.value==""){
	    		iserror='false';
	    		<s:if test="%{sourcepage.equals('searchNegotiationForWO')}">
		  			if(dom.get('contractorId')!=null && dom.get('contractorId').value=="-1" ){
		   				iserror='false';
		  			 }else iserror='true';
		  		</s:if>
			}
		  </s:if>
		  <s:if test="%{tenderCretedBy.toLowerCase().equals('workspackage')}">
	         if(tenderFileNumber.value=="" && wpNumber.value==""){
	    		iserror='false';
	    		<s:if test="%{sourcepage.equals('searchNegotiationForWO')}">
		  			if(dom.get('contractorId')!=null && dom.get('contractorId').value=="-1" ){
		   				iserror='false';
		  			 }else iserror='true';
		  		</s:if>
	    	}
		  </s:if>
		  <s:if test="%{tenderCretedBy.toLowerCase().equals('both')}">
		   if(estimateNumber.value=="" && tenderFileNumber.value=="" && wpNumber.value==""){
		   		iserror='false';
		   		<s:if test="%{sourcepage.equals('searchNegotiationForWO')}">
		  			if(dom.get('contractorId')!=null && dom.get('contractorId').value=="-1"){
		   				iserror='false';
		  			 }else iserror='true';
		  		</s:if>
		   }
		  </s:if>
		 
		  if(iserror=='false'){
		  		dom.get("negotiationDate_error").style.display='block';
				document.getElementById("negotiationDate_error").innerHTML='Please select at least one  search criteria';
				return false;
		  }
		}
		dom.get("negotiationDate_error").style.display='none';
		document.getElementById("negotiationDate_error").innerHTML='';
		document.tenderNegotiation.action='${pageContext.request.contextPath}/tender/tenderNegotiation!search.action?mode=searchaction';						
	    document.tenderNegotiation.submit();		
	}
	
function gotoPage(obj){
	var currRow=getRow(obj);
	var tndrRespContrId = getControlInBranch(currRow,'tenderResponseContractorsId'); 
	var worksPckgId =getControlInBranch(currRow,'worksPckgId');
	var tenderHdrId=getControlInBranch(currRow,'tenderHdrId')
	var abstractEstmId=getControlInBranch(currRow,'abstractEstId');
	var tndrStateId=getControlInBranch(currRow,'tenderStateId');	
	var docNumber = getControlInBranch(currRow,'docNumber');
	var appDate = getControlInBranch(currRow,'appDate');
	var objNo = getControlInBranch(currRow,'objNo');
	var showActions = getControlInBranch(currRow,'showActions');
	var id=getControlInBranch(currRow,'tenderResponseId'); 
	var cName=getControlInBranch(currRow,'contractorName'); 
	
	
	if(showActions[1]!=null && obj.value==showActions[1].value){	
	if(abstractEstmId.value!="")		
		window.open("${pageContext.request.contextPath}/tender/tenderNegotiation!edit.action?id="+id.value+
		"&estimateId="+abstractEstmId.value+"&tenderHeader.id="+tenderHdrId.value+"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	else if(worksPckgId.value!="")
		window.open("${pageContext.request.contextPath}/tender/tenderNegotiation!edit.action?id="+id.value+
		"&worksPackageId="+worksPckgId.value+"&tenderHeader.id="+tenderHdrId.value+"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(showActions[2]!=null && obj.value==showActions[2].value){	
		document.location.href="${pageContext.request.contextPath}/tender/tenderNegotiationPDF.action?tenderResponseId="+id.value;	
	}
	if(showActions[3]!=null && obj.value==showActions[3].value){
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+ 
		tndrStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(showActions[4]!=null && obj.value==showActions[4].value){ 
		viewDocumentManager(docNumber.value);return false;
	}	
	if(showActions[5]!=null && obj.value==showActions[5].value){ 
	window.open("${pageContext.request.contextPath}/workorder/setStatus!edit.action?objectType=TenderResponseContractors&objId="+
	tndrRespContrId.value+"&setStatus="+dom.get('setStatus').value+"&appDate="+appDate.value+"&contractorName="+cName.value+"&objNo="+objNo.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}	
}	

function doSubmit(params)
{
 pagingParamValue=params.substring(params.indexOf('&page=')+6,params.indexOf('&page=')+7);

  var newparam="";
  newparam=newparam+"&page="+pagingParamValue;
  document.forms[0].action='${pageContext.request.contextPath}/tender/tenderNegotiation!search.action?mode=searchaction'+newparam;						
  document.forms[0].submit();
}

</script>
	<body>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<div id="msgsDiv" class="messagestyle">
				<s:actionmessage theme="simple" />
			</div>
		</s:if>
		<s:form
			action="/tender/tenderNegotiation!search.action"
			theme="simple" name="tenderNegotiation">
			<div class="errorstyle" id="negotiationDate_error"
				style="display: none;"></div>
			<div class="navibarshadowwk">
			</div>
			<div class="formmainbox">
				<div class="insidecontent">
					<div class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
							<table id="formTable" width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr>
									<td>
										&nbsp;
									</td>
								</tr>
								<s:hidden name="setStatus" id="setStatus" value="%{setStatus}" />
								<s:hidden name="sourcepage" id="sourcepage"
									value="%{sourcepage}" />
								<s:hidden id="tenderRespId" name="tenderRespId" />
								<s:hidden id="tenderRespContrId" name="tenderRespContrId" />
								<tr>
									<td>
										<table id="tenderNegoSearchTable" width="100%" border="0"
											cellspacing="0" cellpadding="0">
											<tr>
												<td colspan="4">
													&nbsp;
												</td>
											</tr>
											<tr>
												<td colspan="4" class="headingwk">
													<div class="arrowiconwk">
														<img
															src="${pageContext.request.contextPath}/image/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name='title.search.criteria' />
													</div>
												</td>
											</tr>
											<tr>
												<td width="11%" class="whiteboxwk">
													<s:text name="tendernegotiation.status" />
													:
												</td>
												<td width="21%" class="whitebox2wk">
													<s:if test="%{sourcepage.equals('searchNegotiationForWO')}">
														<s:select id="status" name="status" 
															 cssClass="selectwk"
															list="%{negotiationStatusesForWO}" value="%{status}"
															listKey="code" listValue="description" />
													</s:if>
													<s:else>
														<s:select id="status" name="status" headerKey="-1"
															headerValue="ALL" cssClass="selectwk"
															list="%{estimateStatuses}" value="%{status}"
															listKey="code" listValue="description" />
													</s:else>
												</td>
												<td width="11%" class="whiteboxwk">
													<s:text name="tenderNegotiation.executingDepartment" />
													:
												</td>
												<td width="21%" class="whitebox2wk">
													<s:select id="departmentId" name="departmentId"
														cssClass="selectwk" list="%{dropdownData.searchDepartmentList}"
														listKey="id" listValue="deptName" headerKey="-1"
														headerValue="--- Select ---" />
												</td>

											</tr>
											<tr>
												<td width="11%" class="greyboxwk">
													<s:text name="tenderNegotiation.negotiationNo" />
													:
												</td>
												<td width="21%" class="greybox2wk">
													<s:textfield name="negotiationNumber"
														id="negotiationNumber" cssClass="selectboldwk" />
												</td>

												<td width="15%" class="greyboxwk">
													<s:text name="tenderNegotiation.projectCode" />
													:
												</td>
												<td width="53%" class="greybox2wk">
													<s:textfield name="projectCode" id="projectCode"
														cssClass="selectboldwk" />
												</td>
											</tr>


											<tr>
												<td width="11%" class="whiteboxwk">
													<s:text name="tenderNegotiation.negotiationFromDate" />
													:
												</td>

												<td width="21%" class="whitebox2wk">
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
												<td width="15%" class="whiteboxwk">
													<s:text name="wp.todate" />
													:
												</td>
												<s:date name="toDate" var="toDateFormat" format="dd/MM/yyyy" />
												<td width="53%" class="whitebox2wk">
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
												<s:if
													test="%{tenderCretedBy.toLowerCase().equals('workspackage') || tenderCretedBy.toLowerCase().equals('both')}">
													<td width="11%" class="greyboxwk">
														<s:text name="tenderNegotiation.worksPackageNo" />
														:
													</td>

													<td width="21%" class="greybox2wk">
														<s:textfield name="wpNumber" id="wpNumber"
															cssClass="selectboldwk" />
													</td>

													<td width="11%" class="greyboxwk">
														<s:text name="estimate.tenderfilenumber" />
														:
													</td>
													<td width="21%" class="greybox2wk">
														<s:textfield name="tenderFileNumber" id="tenderFileNumber"
															cssClass="selectboldwk" />
													</td>


												</s:if>


											</tr>
											<s:if test="%{tenderCretedBy.toLowerCase().equals('estimate') || tenderCretedBy.toLowerCase().equals('both')}">
											<tr>													
												<td width="11%" class="greyboxwk">
													<s:text name="estimate.number" />
												:
												</td>
												<td width="21%" class="greybox2wk">
													<s:textfield name="estimateNumber" id="estimateNumber"
														cssClass="selectboldwk" />
												</td>
												<td width="11%" class="greyboxwk"></td>
												<td width="21%" class="greybox2wk"></td>											
											</tr>											
											</s:if>
											<tr>
												<s:if test="%{sourcepage.equals('searchNegotiationForWO')}">
													<td width="11%" class="whiteboxwk">
														Contractor :
													</td>
													<td width="21%" class="whitebox2wk" colspan="3">
														<s:select id="contractorId" name="contractorId"
															cssClass="selectwk"
															list="%{contractorForApprovedNegotiation}" listKey="id"
															listValue="name" headerKey="-1"
															headerValue="--- Select ---" value="%{contractorId}" />
													</td>
												</s:if>
												

											</tr>
											<tr>
												<td>
													&nbsp;
												</td>
											</tr>
											<tr>
												<td colspan="4">
													<div class="buttonholdersearch">
														<input type="submit" class="buttonadd" value="SEARCH"
															id="searchButton" onClick="return validate()" />
														<input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="button" onclick="window.close();" />
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
														
														<s:if test= "%{searchResult.fullListSize!= 0}">														
													
														<display:table name="searchResult" 
															uid="currentRow" cellpadding="0" cellspacing="0"
															requestURI=""
															style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
															
															<s:if
																test="%{sourcepage.equals('searchNegotiationForWO')}">
																<display:column headerClass="pagetableth"
																	class="pagetabletd" title="Select" titleKey="column.title.select">
																	<input name="radio" type="radio" id="radio"
																		value="<s:property value='%{#attr.currentRow.id}'/>"
																		onClick="setworkorderId(this);" />
															</display:column>
															</s:if>
															
															<display:column headerClass="pagetableth"
																class="pagetabletd" title="Sl.No"
																titleKey="column.title.SLNo"
																style="width:10%;text-align:right" >
																<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
															</display:column>

															<display:column headerClass="pagetableth"
																class="pagetabletd" title="Negotiation No"
																titleKey="tenderNegotiation.negotiationNo"
																style="width:10%;text-align:left" property="tenderResponse.negotiationNumber" />

															<display:column headerClass="pagetableth"
																class="pagetabletd" title="Negotiation Date"
																titleKey="tenderNegotiation.negotiationDate" 
																style="width:10%;text-align:left"  >
																<s:date name="#attr.currentRow.tenderResponse.negotiationDate" format="dd/MM/yyyy" />
															</display:column>
																
															<display:column headerClass="pagetableth"
																class="pagetabletd" title="Owner"
																titleKey="tenderNegotiation.owner"
																style="width:15%;text-align:left" >
																<s:if test="%{#attr.currentRow.tenderResponse.tenderEstimate.abstractEstimate!=null}">
																		<s:property
																			value='%{#attr.currentRow.tenderResponse.tenderEstimate.abstractEstimate.positionAndUserName}' />
																</s:if>
																<s:if test="%{#attr.currentRow.tenderResponse.tenderEstimate.worksPackage!=null}">
																		<s:property
																			value='%{#attr.currentRow.tenderResponse.tenderEstimate.worksPackage.employeeName}' />
																</s:if>
															</display:column>

															<s:if
																test="%{tenderCretedBy.toLowerCase().equals('workspackage')}">
																<display:column headerClass="pagetableth"
																class="pagetabletd" title="WorksPackage Number" titleKey="tenderNegotiation.search.worksPackageNumber" style="width:14%">
																<s:if test="%{#attr.currentRow.tenderResponse.tenderEstimate.abstractEstimate!=null}">
																	<s:property
																		value='%{#attr.currentRow.tenderResponse.tenderEstimate.abstractEstimate.estimateNumber}' />
																</s:if>
																<s:if test="%{#attr.currentRow.tenderResponse.tenderEstimate.worksPackage!=null}">
																	<s:property
																		value='%{#attr.currentRow.tenderResponse.tenderEstimate.worksPackage.wpNumber}' />
																</s:if>
																</display:column>
															</s:if>
															
															<s:if
																test="%{tenderCretedBy.toLowerCase().equals('estimate')}">
																<display:column headerClass="pagetableth"
																class="pagetabletd" title="Estimate Number" titleKey="tenderNegotiation.search.estimateNumber" style="width:14%" >
																<s:if test="%{#attr.currentRow.tenderResponse.tenderEstimate.abstractEstimate!=null}">
																	<s:property
																		value='%{#attr.currentRow.tenderResponse.tenderEstimate.abstractEstimate.estimateNumber}' />
																</s:if>
																<s:if test="%{#attr.currentRow.tenderResponse.tenderEstimate.worksPackage!=null}">
																	<s:property
																		value='%{#attr.currentRow.tenderResponse.tenderEstimate.worksPackage.wpNumber}' />
																</s:if>
																</display:column>
															</s:if>
															
															<s:if
																test="%{tenderCretedBy.toLowerCase().equals('both')}">
																<display:column headerClass="pagetableth"
																class="pagetabletd" title="WorksPackage / Estimate Number" titleKey="tenderNegotiation.search.estimateNumberworksPackageNumber" style="width:14%" >
																<s:if test="%{#attr.currentRow.tenderResponse.tenderEstimate.abstractEstimate!=null}">
																	<s:property
																		value='%{#attr.currentRow.tenderResponse.tenderEstimate.abstractEstimate.estimateNumber}' />
																</s:if>
																<s:if test="%{#attr.currentRow.tenderResponse.tenderEstimate.worksPackage!=null}">
																	<s:property
																		value='%{#attr.currentRow.tenderResponse.tenderEstimate.worksPackage.wpNumber}' />
																</s:if>
																</display:column>
															</s:if>
																														
															<display:column headerClass="pagetableth"
																class="pagetabletd" title="ExecutingDept Name"
																titleKey="tenderNegotiation.depatname"
																style=' width=16%'>
																<s:if test="%{#attr.currentRow.tenderResponse.tenderEstimate.abstractEstimate!=null}">
																	<s:property
																		value='%{#attr.currentRow.tenderResponse.tenderEstimate.abstractEstimate.executingDepartment.deptName}' />
																</s:if>
																<s:if test="%{#attr.currentRow.tenderResponse.tenderEstimate.worksPackage!=null}">
																	<s:property
																		value='%{#attr.currentRow.tenderResponse.tenderEstimate.worksPackage.userDepartment.deptName}' />
																</s:if>
															</display:column>

															<display:column headerClass="pagetableth"
																class="pagetabletd" title="Contractor Name"
																titleKey="tenderNegotiation.contractorname"
																style="width:10%;text-align:left"
																property="contractor.name" />

															<display:column headerClass="pagetableth"
																class="pagetabletd" title="Status" 
																titleKey="tenderNegotiation.status"
																style="width:10%;text-align:left" property="status" />

															<display:column headerClass="pagetableth"
																class="pagetabletd" title="Total"
																titleKey="tenderNegotiation.amount"
																style="width:10%;text-align:right" >
																<s:text name="contractor.format.number">
																	<s:param name="value" value="%{#attr.currentRow.tenderResponse.totalAmount}" />
																</s:text>
																<s:hidden name="abstractEstId" id="abstractEstId"
																		value="%{#attr.currentRow.tenderResponse.tenderEstimate.abstractEstimate.id}" />
																<s:hidden name="tenderHdrId" id="tenderHdrId"
																		value="%{#attr.currentRow.tenderResponse.tenderEstimate.tenderHeader.id}" />
																<s:hidden name="worksPckgId" id="worksPckgId"
																		value="%{#attr.currentRow.tenderResponse.tenderEstimate.worksPackage.id}" />
																<s:hidden name="tenderResponseContractorsId"
																		id="tenderResponseContractorsId" value="%{#attr.currentRow.id}" />
																<s:hidden name="tenderStateId" id="tenderStateId"
																		value="%{#attr.currentRow.tenderResponse.state.id}" />
																<s:hidden name="docNumber" id="docNumber"
																		value="%{#attr.currentRow.tenderResponse.documentNumber}" />
																<s:hidden id="objNo" name="objNo"
																		value="%{#attr.currentRow.tenderResponse.negotiationNumber}" />	
																														
																<s:hidden id="appDate" name="appDate"
																		value="%{#attr.currentRow.tenderResponse.state.createdDate}" /> 
																
																<s:hidden name="tenderResponseId"
																	id="tenderResponseId" value="%{#attr.currentRow.tenderResponse.id}" />
																<s:hidden name="contractorName"
																	id="contractorName" value="%{#attr.currentRow.contractor.name}" />
																
															</display:column> 

															<s:if test="%{!sourcepage.equals('searchNegotiationForWO')}">
																<display:column headerClass="pagetableth"
																	class="pagetabletd" title="Action"
																	titleKey="tenderNegotiation.action"
																	style="width:10%;text-align:left">
																	
																	<s:select theme="simple"
																		list="%{#attr.currentRow.tenderResponse.tenderNegotiationsActions}"
																		name="showActions" id="showActions"
																		headerValue="%{getText('default.dropdown.select')}"
																		headerKey="-1" onchange="gotoPage(this);">
																	</s:select>
																</display:column>
																									  <display:setProperty name="paging.banner.page.link">
   																	 <a href=javascript:doSubmit("{1}") title='Go to page {0}'>{0}</a>
 																 </display:setProperty>

 <display:setProperty name="paging.banner.full">
 <span class="pagelinks">[<a href=javascript:doSubmit("{1}")>First</a>/<a href=javascript:doSubmit("{2}")>Prev</a>] {0} [<a href=javascript:doSubmit("{3}")>Next</a>/<a href=javascript:doSubmit("{4}")>Last</a>]</span>
	 </display:setProperty>
	  <display:setProperty name="paging.banner.first">
	  <span class="pagelinks">[First/Prev] {0} [<a href=javascript:doSubmit("{3}")>Next</a>/<a href=javascript:doSubmit("{4}")>Last</a>]</span>
	 </display:setProperty>
	  <display:setProperty name="paging.banner.last">
	  <span class="pagelinks">[<a href=javascript:doSubmit("{1}")>First</a>/<a href=javascript:doSubmit("{2}")>Prev</a>] {0} [Next/Last]</span>
	 </display:setProperty>
															</s:if>
															<s:else>
																<display:setProperty name="paging.banner.all_items_found" value=""></display:setProperty>
																<display:setProperty name="paging.banner.one_item_found" value=""></display:setProperty>
																<display:setProperty name="paging.banner.some_items_found" value=""></display:setProperty>
															</s:else>
															
														</display:table>
														
														</s:if>
														<s:elseif test="%{searchResult.fullListSize== 0}">
															<div>
																<table width="100%" border="0" cellpadding="0"
																	cellspacing="0">
																	<tr>
																		<td align="center">
																			<font color="red">No record Found.</font>
																		</td>
																	</tr>
																</table>
															</div>
														</s:elseif>
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<P align="center">
								<s:if test="%{sourcepage.equals('searchNegotiationForWO')}">
									<input type="button" class="buttonadd"
										value="Create Work Order " id="addButton"
										name="createWorkOrderButton" onclick="gotoWorkorder()"
										align="center" />
								</s:if>
							</P>
						</div>
						<div class="rbbot2">
							<div></div>
						</div>
					</div>
				</div>
			</div>
		</s:form>
		<script type="text/javascript">					
		</script>
	</body>
</html>
