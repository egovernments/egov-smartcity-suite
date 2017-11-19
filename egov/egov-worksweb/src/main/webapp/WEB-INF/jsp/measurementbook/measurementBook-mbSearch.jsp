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
<script>
function validateAndSubmit() {
   if(dom.get('mbStatus').value==-1 && dom.get('contractor').value==-1 && dom.get('workorderNoSearch').value==""
   && dom.get('mbRefNoSearch').value=="" &&  dom.get('fromDate').value==""  &&  dom.get('toDate').value=="" 
	    && dom.get('estimateNoSearch').value=="" && dom.get('execDeptid').value==-1){
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
	//document.getElementById('contractor').disabled=false;
	document.mbSearchForm.action='${pageContext.request.contextPath}/measurementbook/measurementBook!searchMB.action';
 	document.mbSearchForm.submit();
}

function setupWorkOrderDetails(){
    workorder_no = document.getElementById('workorderNoSearch').value;
    makeJSONCall(["xWoId","xContractorId"], 
    	'${pageContext.request.contextPath}/measurementbook/ajaxMeasurementBook!workOrderDetails.action',
    	{workOrderNumber:workorder_no},myWOSuccessHandler,myWOFailureHandler) ;
}

myWOSuccessHandler = function(req,res){
    var resultdatas=res.results;
    if(resultdatas[0].xContractorId!=''){
		document.getElementById('contractor').value=resultdatas[0].xContractorId;
		document.getElementById('contractor').disabled=true;
	}else{
		document.getElementById('contractor').value=-1;
		document.getElementById('contractor').disabled=false;
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
	if(document.getElementById('workorderNoSearch').value!=''){
		document.getElementById('contractor').disabled=true;
	}
	else
		document.getElementById('contractor').disabled=false;

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
	if(getControlInBranch(currRow,'billNo')!=null)
		dom.get("billNumber").value = getControlInBranch(currRow,'billNo').value;
	dom.get("mbRefNumber").value = getControlInBranch(currRow,'mbNo').value;
	dom.get("mbId").value = elem.value; 
}

function cancelMeasurementBook(){
	var id = document.mbSearchForm.mbId.value; 
	var cancellationReason = document.mbSearchForm.cancellationReason.value; 
	var cancelRemarks = document.mbSearchForm.cancelRemarks.value; 	
	var billId = document.mbSearchForm.billRegisterId.value; 
	var billNumber = document.mbSearchForm.billNumber.value;
	if(billId != ''){
		dom.get("mbSearch_error").style.display='';
		document.getElementById("mbSearch_error").innerHTML='<s:text name="cencelMb.bill.created.message"/>'+billNumber;
		window.scroll(0,0);
		return false;
	}
	if(id!=''){	
		if(cancellationReason=='OTHER' && cancelRemarks == ''){
			dom.get("mbSearch_error").style.display='';
			document.getElementById("mbSearch_error").innerHTML='<s:text name="validate.cancel.mb.remarks"/>';
			window.scroll(0,0);
			return false;
		}	
		if(validateCancel()){
			doLoadingMask();
			window.open('${pageContext.request.contextPath}/measurementbook/measurementBook!cancelApprovedMB.action?sourcepage=cancelMB&cancelRemarks='+cancelRemarks+'&mbId='+id+'&cancellationReason='+cancellationReason,'_self');
		}
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

var estimateNoSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

var workorderNoSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

var mbRefNoSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

function toggleCancelRemarks(obj) { 
	if(obj.value=='OTHER') {
		document.getElementById("cancelRemarksDtls").style.display='';
	}
	else {
		document.getElementById("cancelRemarksDtls").style.display='none';
		dom.get("cancelRemarks").value='';
	}	
}

</script>

<html>
	<head>
	    <title><s:text name="measurementbook.title.mb.search" /></title>
	</head>
	<body>
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
		<s:form action="measurementBook" theme="simple"	name="mbSearchForm"  onsubmit="if(!validateDateFormat(document.mbSearchForm.mbDate)){return false;}; enableSelect();">
			<div class="errorstyle" id="mbSearch_error" style="display:none;"></div>
			<s:hidden id="sourcepage" name="sourcepage" />
			<s:hidden id="mbId" name="mbId" />
			<s:hidden id="mbRefNumber" name="mbRefNumber" />
			<s:hidden id="billRegisterId" name="billRegisterId" />
			<s:hidden id="billNumber" name="billNumber" />
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
						<table id="mbSearchTable" width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img src="/egworks/resources/erp2/images/arrow.gif" />
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
									<script>disableSelect();
										window.history.forward(1);
									</script>
								</s:if> 	
								<td class="whiteboxwk">
									<s:text name="measurementbook.search.department" />
									:
								</td>
								<td class="whitebox2wk">
								<s:select id="execDeptid" name="execDeptid"
										cssClass="selectwk" list="%{dropdownData.executingDepartmentList}"
										listKey="id" listValue="deptName" headerKey="-1" value="%{execDeptid}"
										headerValue="--- Select ---" />
								</td>						
							</tr>
							<tr>								
								<td width="11%" class="greyboxwk">
									<s:text name="measurementbook.search.woNo" />:
								</td>
								<td class="greybox2wk" >
	        						<div class="yui-skin-sam">
	        							<div id="workorderNoSearch_autocomplete">
                							<div> 
	        									<s:textfield id="workorderNoSearch" name="workorderNo" value="%{workorderNo}" cssClass="selectwk" />
	        								</div>
	        								<span id="workorderNoSearchResults"></span>
	        							</div>		
	        						</div>
	        						<egov:autocomplete name="workorderNoSearch" width="20" field="workorderNoSearch" url="ajaxMeasurementBook!searchWorkOrderNumber.action?" queryQuestionMark="false" results="workorderNoSearchResults" handler="workorderNoSearchSelectionHandler" queryLength="3" />
		         				</td>
								<td width="11%" class="greyboxwk">
									<s:text name="measurementbook.search.refno" />:
								</td>
								<td class="greybox2wk" >
		        						<div class="yui-skin-sam">
		        							<div id="mbRefNoSearch_autocomplete">
	                							<div>
		        									<s:textfield id="mbRefNoSearch" name="mbRefNo" value="%{mbRefNo}" cssClass="selectwk" />
		        								</div>
		        								<span id="mbRefNoSearchResults"></span>
		        							</div>	
		        						</div>
		        						<egov:autocomplete name="mbRefNoSearch" width="20" field="mbRefNoSearch" url="ajaxMeasurementBook!searchMbRefNo.action?" queryQuestionMark="false" results="mbRefNoSearchResults" handler="mbRefNoSearchSelectionHandler" />
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
									<a
										href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
										onmouseover="window.status='Date Picker';return true;"
										onmouseout="window.status='';return true;"> <img
											src="/egworks/resources/erp2/images/calendar.png"
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
												src="/egworks/resources/erp2/images/calendar.png"
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
									<td width="21%" class="greybox2wk" >
										<s:select id="contractor" name="contractorId"
																cssClass="selectwk"
																list="%{contractorForApprovedWorkOrder}"
																headerKey="-1"															
																headerValue="%{getText('default.dropdown.select')}"
																value="%{contractorId}"/>	
									</td>
									<td width="11%" class="greyboxwk">
										<s:text name="measurementbook.search.estNO" />:
									</td>
									<td class="greybox2wk" >
		        						<div class="yui-skin-sam">
		        							<div id="estimateNoSearch_autocomplete">
	                							<div>
		        									<s:textfield id="estimateNoSearch" name="estimateNo" value="%{estimateNo}" cssClass="selectwk" />
		        								</div>
		        								<span id="estimateNoSearchResults"></span>
		        							</div>	
		        						</div>
		        						<egov:autocomplete name="estimateNoSearch" width="20" field="estimateNoSearch" url="ajaxMeasurementBook!searchEstimateNumber.action?" queryQuestionMark="false" results="estimateNoSearchResults" handler="estimateNoSearchSelectionHandler" queryLength="3"/>
			         				</td>							
								</tr>
								
								<tr>
									<td class="shadowwk" colspan="4"/>
								</tr>
									
							<tr>
								<td colspan="4">
									<div class="buttonholdersearch">
										<input type="submit" class="buttonadd" value="Search" id="searchButton" 
											onclick="return validateAndSubmit();enableSelect();" />
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
											<td colspan="10" class="headingwk">
												<div class="arrowiconwk">
													<img
														src="/egworks/resources/erp2/images/arrow.gif" />
												</div>
												<div class="headplacer">
													<s:text name="title.search.result" />
												</div>
											</td>
										</tr>
										</table>
					
					<display:table name="pagedResults" uid="currentRowObject" cellpadding="0" cellspacing="0" 
     		   						 requestURI="" class="its"  style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
						<s:text id="select" name="%{getText('column.title.select')}"></s:text>
			 			<s:text id="SlNo" name="%{getText('column.title.SLNo')}"></s:text>
						<s:text id="woNumber" name="%{getText('mb.search.column.wono')}"></s:text>
						<s:text id="mbcontractor" name="%{getText('mb.search.column.contractor')}"></s:text>
						<s:text id="mbrefer" name="%{getText('mb.search.column.refno')}"></s:text>
						<s:text id="mbages" name="%{getText('mb.search.column.pages')}"></s:text>
						<s:text id="mbDate" name="%{getText('mb.search.column.date')}"></s:text>
						<s:text id="owner" name="%{getText('mb.search.column.owner')}"></s:text>
						<s:text id="mbStatus" name="%{getText('mb.search.column.status')}"></s:text>
						<s:text id="mbAction" name="%{getText('measurementbook.search.actions')}"></s:text>
						 <tr>
	          	<s:if test="%{sourcepage=='cancelMB'}">
		          	<s:if test="%{#attr.currentRowObject.egBillregister!=null && #attr.currentRowObject.egBillregister.billstatus !='CANCELLED'}">
			          	<display:column class="hidden" headerClass="hidden"  media="html">
		 					<s:hidden id="billId" name="billId" value="%{#attr.currentRowObject.egBillregister.id}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden"  media="html">
		 				 	<s:hidden id="billNo" name="billNo" value="%{#attr.currentRowObject.egBillregister.billnumber}" />
						</display:column>
				   </s:if>		
				  <s:else>
					<display:column class="hidden" headerClass="hidden"  media="html">
					 	<s:hidden name="billId" id="billId" value="" />
					 </display:column>
					 <display:column class="hidden" headerClass="hidden"  media="html">
					 	<s:hidden name="billNo" id="billNo" value="" />
					 </display:column>
				 </s:else>	
				 <display:column  headerClass="pagetableth" class="pagetabletd" title="Select" style="width:3%;text-align:center">
					<input name="radio" type="radio" id="radio"  value="<s:property value='%{#attr.currentRowObject.id}'/>" onClick="setMBId(this);" />
			     </display:column>
			  </s:if>
			   
				<display:column class="hidden" headerClass="hidden"  media="html">
			   <s:hidden name="mbookId" id="mbookId" value="%{#attr.currentRowObject.id}" />
			    </display:column>
				 <display:column class="hidden" headerClass="hidden"  media="html">
			   <s:hidden name="mbookStateId" id="mbookStateId" value="%{#attr.currentRowObject.state.id}" />
			    </display:column>
				 <display:column class="hidden" headerClass="hidden"  media="html">
			   <s:hidden name="docNo" id="docNo" value="%{#attr.currentRowObject.documentNumber}" />
			   	 </display:column>
			   
			   <display:column  headerClass="pagetableth" class="pagetabletd" title="${SlNo}" style="width:3%;text-align:center">
			   
				<s:property value="%{#attr.currentRowObject_rowNum+ (page-1)*pageSize}"/></display:column>	
 						 	 
  			 	<display:column   headerClass="pagetableth" class="pagetabletd"  title="${woNumber}"  style="width:15%text-align:center;">
  			 	<s:property value="%{#attr.currentRowObject.workOrder.workOrderNumber}" /> </display:column>
  			 	   	
  			    <display:column   headerClass="pagetableth" class="pagetabletd"  title="${mbcontractor}"  style="width:15%text-align:center;">
  			 	<s:property value="%{#attr.currentRowObject.workOrder.contractor.name}" /> </display:column>
          			 	   	
          		<display:column   headerClass="pagetableth" class="pagetabletd"  title="${mbrefer}"  style="width:10%text-align:center;">
				<s:hidden id="mbNo" name="mbNo" 	value="%{#attr.currentRowObject.mbRefNo}" />
          	    <s:property value="%{#attr.currentRowObject.mbRefNo}" /> </display:column>
          	              			 	   
         	  	<display:column   headerClass="pagetableth" class="pagetabletd"  title="${mbages}"  style="width:8%text-align:center;">
      				<s:property value="%{#attr.currentRowObject.fromPageNo}" /> -
      				<s:if test="%{#attr.currentRowObject.toPageNo > 0 && toPageNo != ''}">
						<s:property value="%{#attr.currentRowObject.toPageNo}" />
					</s:if>
					<s:else>
						<s:property value="%{#attr.currentRowObject.fromPageNo}" />
					</s:else>
				</display:column>
				<display:column   headerClass="pagetableth" class="pagetabletd"  title="${mbDate}"  style="width:9%text-align:center;">
					<s:date name="%{#attr.currentRowObject.mbDate}"  format="dd/MM/yyyy"/>
            	</display:column>
            	<display:column title="MB Amount(Rs)" headerClass="pagetableth" class="pagetabletd" style="width:5%;text-align:right">
					<s:text name="contractor.format.number">
						<s:param name="value" value="%{#attr.currentRowObject.mbAmount}" />
					</s:text>
				</display:column>
				
				<s:if test="%{mbStatus!='APPROVED' && mbStatus!='CANCELLED'}">
	     			<display:column  headerClass="pagetableth" class="pagetabletd"   title="${owner}"  style="width:7%text-align:center;">
	          	    	<s:property value="%{#attr.currentRowObject.owner}" /> 
	          	    </display:column>
      			</s:if>
      		
      			<display:column   headerClass="pagetableth" class="pagetabletd"  title="Status"  style="width:10%text-align:right;">
					<s:property value="%{#attr.currentRowObject.egwStatus.code}"/>
				
      			</display:column>
				<display:column    headerClass="pagetableth" class="pagetabletd" title="${mbAction}"  style="width:4%text-align:center;">
          			 	   		<s:select theme="simple"
						                list="#attr.currentRowObject.mbActions"
						                name="showMBActions" id="showMBActions"
						                headerValue="%{getText('default.dropdown.select')}"
						                headerKey="-1" onchange="gotoPage(this);">
                          		 </s:select>             	
  			   	</display:column>
  		</display:table>
								</tr>	 
		<s:if test="%{mbList.size == 0}">
			<div >	
				<table width="100%" border="0" cellpadding="0"
				cellspacing="0">
					<tr>
						<td align="center">
							<font color="red">No record Found.</font>
						</td>
					</tr>
				</table>
			</div>
		</s:if>
				</td>
			</tr>
		</table>
	</div>
	</td>
</tr>

</table>
	</td>
</tr>
				<s:if test="%{sourcepage.equals('cancelMB')}"> 
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
							value="Cancel Abstract MB" id="addButton"
							name="cancelMB" onclick="cancelMeasurementBook()"
							align="center" />
						</div>
					</td>
				</tr>
				</s:if>
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
