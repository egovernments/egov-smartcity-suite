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
<html>
<head>
	<title><s:text name='page.title.search.contractorbill' /></title>
</head>

<script type="text/javascript">
     
function disableSelect(){
	document.getElementById('status').disabled=true
}
function enableSelect(){
	document.getElementById('status').disabled=false
}
function validateSearch()
{
	if(dom.get('contractor').value==-1 && dom.get('execDeptid').value==-1 && dom.get('billDeptId').value==-1  
			&& dom.get('workordercodeSearch').value=="" && dom.get('billnoSearch').value=="" && dom.get('fromDate').value=="" && dom.get('toDate').value=="" && dom.get('status').value==-1 && dom.get('estimateNoSearch').value=="")
	{
		dom.get("searchBill_error").innerHTML='Please Select at least one criteria'; 
        dom.get("searchBill_error").style.display='';
		return false;
	 }	
	 else if(!dom.get('fromDate').value==""  && !validateDateFormat(document.searchBillForm.fromDate)){	
			dom.get('errorfromDate').style.display='none';
			dom.get("searchBill_error").style.display='block';
			dom.get("searchBill_error").innerHTML='<s:text name="invalid.fieldvalue.billDate" />'; 
			document.searchBillForm.fromDate.focus();
		   	return false;
	}	
	else if(!dom.get('toDate').value==""  && !validateDateFormat(document.searchBillForm.toDate)){
	 		dom.get('errortoDate').style.display='none';
			dom.get("searchBill_error").style.display='block';
			document.getElementById("searchBill_error").innerHTML='<s:text name="invalid.fieldvalue.billDate" />';
			document.searchBillForm.toDate.focus();
		   	return false;
	}
	else if(!$F('workordercodeSearch').blank() && $F('workordercodeSearch').strip().length < 3) {
		$('searchBill_error').show();
		dom.get("searchBill_error").innerHTML='Work order number should be minimum three characters'; 			
		return false;
    }
    else{
	 dom.get("searchBill_error").style.display='none';
	 dom.get("searchBill_error").innerHTML=''; 
	 }
	return true;
}	


function gotoPage(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'billId');
	var  billStateId=getControlInBranch(currRow,'billStateId');	
    var document_Number = getControlInBranch(currRow,'docNo');
    var billNumber = getControlInBranch(currRow,'billNo');
	var workorderId = getControlInBranch(currRow,'woId');
	
	var showActions = getControlInBranch(currRow,'showBillActions');
	if(obj.value=='View'){	
		window.open("${pageContext.request.contextPath}/contractorBill/contractorBill!edit.action?id="+id.value+"&workOrderId="+workorderId.value+"&billnumber="+billNumber.value+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='View PDF'){	
	window.open("${pageContext.request.contextPath}/contractorBill/contractorBillPDF.action?egbillRegisterId="+id.value,'');
	}
	if(obj.value=='View Completion Certificate'){
		window.open("${pageContext.request.contextPath}/contractorBill/contractorBill!viewCompletionCertificate.action?id="+
		id.value,'');
	}
	if(obj.value=='WorkFlow History'){
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+
		billStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='View Document'){  
		viewDocumentManager(document_Number.value);return false;
	}
}	

function gotoPage(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'billId');
	var  billStateId=getControlInBranch(currRow,'billStateId');	
    var document_Number = getControlInBranch(currRow,'docNo');
    var billNumber = getControlInBranch(currRow,'billNo');
	var workorderId = getControlInBranch(currRow,'woId');
	
	var showActions = getControlInBranch(currRow,'showBillActions');
	if(obj.value=='View'){	
		window.open("${pageContext.request.contextPath}/contractorBill/contractorBill!edit.action?id="+id.value+"&workOrderId="+workorderId.value+"&billnumber="+billNumber.value+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='View PDF'){	
	window.open("${pageContext.request.contextPath}/contractorBill/contractorBillPDF.action?egbillRegisterId="+id.value,'');
	}
	if(obj.value=='View Completion Certificate'){
		window.open("${pageContext.request.contextPath}/contractorBill/contractorBill!viewCompletionCertificate.action?id="+
		id.value,'');
	}
	if(obj.value=='View Contract Certificate'){
		window.open("${pageContext.request.contextPath}/contractorBill/contractorBill!viewContractCertificate.action?id="+
		id.value,'');
	}
	if(obj.value=='WorkFlow History'){
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+
		billStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='View Document'){  
		viewDocumentManager(document_Number.value);return false;
	}
}	

function getProjectClosureEstimates(contractorBillId){
    makeJSONCall(["estimateNumber"],'${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!getProjectClosureEstimateForBill.action',{contractorBillId:contractorBillId},projectClosureEstimateLoadSuccessHandler,projectClosureEstimateLoadFailureHandler) ;
}

projectClosureEstimateLoadSuccessHandler = function(req,res){
	  results=res.results;
	  var estimateNo=''
	  if(results != '') {
		  estimateNo=results[0].estimateNumber;
	  }
	if(estimateNo != ''){
		dom.get("searchBill_error").style.display='';
		document.getElementById("searchBill_error").innerHTML='<s:text name="cancel.bill.projectClosureEstimate.message"/>';
		window.scroll(0,0);
		return false;
	}	
	saveCancelBill();	  
}

projectClosureEstimateLoadFailureHandler= function(){
    dom.get("searchBill_error").style.display='';
	document.getElementById("searchBill_error").innerHTML='<s:text name="cancel.bill.projectClosureEstimate.ajax.failure"/>';
}

function saveCancelBill() {
	var contractorBillId = document.searchBillForm.contractorBillId.value;
	var cancellationReason = document.searchBillForm.cancellationReason.value; 
	var cancelRemarks = document.searchBillForm.cancelRemarks.value; 	
	 if(contractorBillId !='' ) {	
		if(cancellationReason=='OTHER' && cancelRemarks == ''){
			dom.get("searchBill_error").style.display='';
			document.getElementById("searchBill_error").innerHTML='<s:text name="validate.cancel.bill.remarks"/>'; 
			window.scroll(0,0);
			return false;
		}	
		if(validateCancel()) {
			doLoadingMask();
			window.open('${pageContext.request.contextPath}/contractorBill/searchBill!cancelApprovedBill.action?&cancelRemarks='+cancelRemarks+'&contractorBillId='+contractorBillId+'&cancellationReason='+cancellationReason,'_self');
		}
		else {
			return false;
		}
	}	
}

function cancelContractorBill(){
	var contractorBillId = document.searchBillForm.contractorBillId.value;
	var woId = document.searchBillForm.workOrderId.value;
	var billType = document.searchBillForm.billType.value;
	var voucherNumber = document.searchBillForm.voucherNumber.value;
	if(contractorBillId != '' && voucherNumber != ''){
		dom.get("searchBill_error").style.display='';
		document.getElementById("searchBill_error").innerHTML='<s:text name="cencel.bill.voucher.created.message"/>'+voucherNumber;
		window.scroll(0,0);
		return false;
  	}
	if(contractorBillId != '' && billType == '<s:property value="%{finalBillTypeConfigValue}" />'){
		getProjectClosureEstimates(contractorBillId);	
	}	
	else if(contractorBillId == ''){
		dom.get("searchBill_error").style.display='';
		document.getElementById("searchBill_error").innerHTML='<s:text name="contractorBill.not.selected" />';
		return false;
  	}
	else {
		if(!saveCancelBill())
			return false;
	}		
	  dom.get("searchBill_error").style.display='none';
	  document.getElementById("searchBill_error").innerHTML='';
	  if(dom.get("errorstyle")){
	  	dom.get("errorstyle").style.display='none';
	 	dom.get("errorstyle").innerHTML='';
	  }
}

function setBillId(elem){
	var currRow=getRow(elem);
	dom.get("workOrderId").value = getControlInBranch(currRow,'woId').value;
    dom.get("billNumber").value = getControlInBranch(currRow,'billNo').value;
    dom.get("billType").value = getControlInBranch(currRow,'conBillType').value;  
    dom.get("voucherNumber").value = getControlInBranch(currRow,'voucherNo').value;        
	dom.get("contractorBillId").value = elem.value; 
}

function validateCancel() {
	var msg='<s:text name="bill.cancel.confirm"/>';
	var billNo=dom.get("billNumber").value; 
	if(!confirmCancel(msg,billNo)) {
		return false;
	}
	else {
		return true;
	}
}

function toggleCancelRemarks(obj) { 
	if(obj.value=='OTHER') {
		document.getElementById("cancelRemarksDtls").style.display='';
	}
	else {
		document.getElementById("cancelRemarksDtls").style.display='none';
		dom.get("cancelRemarks").value='';
	}	
}

var workordercodeSearchSelectionHandler = function(sType, arguments){  
	var oData = arguments[2];
};

var billnoSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

var estimateNoSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

</script>

<body>
 <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
<div class="errorstyle" id="searchBill_error" style="display: none;"></div>
<s:form name="searchBillForm" action="searchBill" theme="simple" onsubmit="enableSelect();">
<s:hidden id="sourcePage" name="sourcePage" />
<s:hidden id="contractorBillId" name="contractorBillId" />
<s:hidden id="billNumber" name="billNumber" />
<s:hidden id="workOrderId" name="workOrderId" />
<s:hidden id="billType" name="billType" />
<s:hidden id="voucherNumber" name="voucherNumber" />
	<div class="formmainbox"></div>
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
	        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
	            <div class="headplacer"><s:text name="page.subheader.search.estimate" /></div></td>
	        </tr>

			 <tr>
				 <td width="11%" class="whiteboxwk"><s:text name="contractorBill.status" />:</td>
		         <td width="21%" class="whitebox2wk"><s:select id="status" name="status" headerKey="-1" headerValue="ALL" cssClass="selectwk" list="%{billStatuses}"  listKey="code" listValue="description" /></td>
		          <s:if test="%{sourcePage=='cancelBill'}">
						<script>disableSelect();
							window.history.forward(1);
						</script>
				  </s:if>
				  <td class="whiteboxwk">
					<s:text name="bill.search.bill.department" />
					:
				 </td>
				 <td class="whitebox2wk">
				 <s:select id="billDeptId" name="billDeptId"
						cssClass="selectwk" list="%{dropdownData.billDepartmentList}"
						listKey="id" listValue="deptName" headerKey="-1" value="%{billDeptId}"
						headerValue="%{getText('default.dropdown.select')}" />
				 </td>								
			</tr>
		    <tr>
		         <td width="11%" class="greyboxwk"><s:text name="contractorBill.workorderCode" />:</td>
		         <td class="greybox2wk" >
  						<div class="yui-skin-sam">
  							<div id="workordercodeSearch_autocomplete">
         							<div> 
  									<s:textfield id="workordercodeSearch" name="workordercode" value="%{workordercode}" cssClass="selectwk" />
  								</div>
  								<span id="workordercodeSearchResults"></span>
  							</div>		
  						</div>
  						<egov:autocomplete name="workordercodeSearch" width="20" field="workordercodeSearch" url="ajaxContractorBill!searchWorkOrderNumber.action?" queryQuestionMark="false" results="workordercodeSearchResults" handler="workordercodeSearchSelectionHandler" queryLength="3" />
    			 </td>
		         <td width="15%" class="greyboxwk"><s:text name="contractorBill.billNo" />:</td>
		         <td class="greybox2wk" >
    						<div class="yui-skin-sam">
    							<div id="billnoSearch_autocomplete">
           							<div>
    									<s:textfield id="billnoSearch" name="billno" value="%{billno}" cssClass="selectwk" />
    								</div>
    								<span id="billnoSearchResults"></span>
    							</div>	
    						</div>
    						<egov:autocomplete name="billnoSearch" width="20" field="billnoSearch" url="ajaxContractorBill!searchContractorBillNo.action?" queryQuestionMark="false" results="billnoSearchResults" handler="billnoSearchSelectionHandler" queryLength="3" />
       			 </td>				
		   </tr>
		   <tr> 
				<td width="15%" class="whiteboxwk"><s:text name="contractorBill.search.fromdate" />:</td>
				<td class="whitebox2wk"><s:date name="fromDate" var="fromBillDate"	format="dd/MM/yyyy" />
										<s:textfield name="fromDate" id="fromDate"
											cssClass="selectwk" value="%{fromBillDate}"
											onfocus="javascript:vDateType='3';"
											onkeyup="DateFormat(this,this.value,event,false,'3')" />
										<a	href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
											onmouseover="window.status='Date Picker';return true;"
											onmouseout="window.status='';return true;"> <img
												src="/egworks/resources/erp2/images/calendar.png"
												alt="Calendar" width="16" height="16" border="0"
												align="absmiddle" />
										</a>
									<span id='errorfromDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
				</td>
				<td width="17%" class="whiteboxwk"><s:text name="contractorBill.search.todate" />:</td>
				<td width="17%" class="whitebox2wk"><s:date name="toDate" var="toBillDate"	format="dd/MM/yyyy" />
													<s:textfield name="toDate" id="toDate"
														value="%{toBillDate}" cssClass="selectwk"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a	href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
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
		   		 <td width="15%" class="greyboxwk"><s:text name="contractorBill.contractor" />:</td>
				 <td width="21%" class="greybox2wk"><s:select id="contractor" name="contractorId"
															cssClass="selectwk"
															list="%{contractorForApprovedWorkOrder}"
															headerKey="-1"															
															headerValue="%{getText('default.dropdown.select')}" 
															value="%{contractorId}"/>
				 </td>
				  <td class="greyboxwk">
					<s:text name="bill.search.executing.department" />
					:
				 </td>
				 <td class="greybox2wk">
				 <s:select id="execDeptid" name="execDeptid"
						cssClass="selectwk" list="%{dropdownData.executingDepartmentList}"
						listKey="id" listValue="deptName" headerKey="-1" value="%{execDeptid}"
						headerValue="%{getText('default.dropdown.select')}" />
				 </td>	
		   </tr>
		   <tr>
		   			<td width="11%" class="whiteboxwk">
							<s:text name="measurementbook.search.estNO" />:
					</td>
									<td class="whitebox2wk" >
		        						<div class="yui-skin-sam">
		        							<div id="estimateNoSearch_autocomplete">
	                							<div>
		        									<s:textfield id="estimateNoSearch" name="estimateNo" value="%{estimateNo}" cssClass="selectwk" />
		        								</div>
		        								<span id="estimateNoSearchResults"></span>
		        							</div>	
		        						</div>
		        						<egov:autocomplete name="estimateNoSearch" width="20" field="estimateNoSearch" url="ajaxContractorBill!searchEstimateNumber.action?" queryQuestionMark="false" results="estimateNoSearchResults" handler="estimateNoSearchSelectionHandler" queryLength="3"/>
			         				</td>
		   
		   </tr>
		   <tr>
	       		<td colspan="4" class="shadowwk"></td>
	       </tr>
	       <tr>
	       		<td colspan="4">
	       			<div class="buttonholdersearch" align = "center">
	            	  <s:submit value="Save" cssClass="buttonfinal" value="SEARCH" id="saveButton" name="button" onClick="return validateSearch();enableSelect()"/>
	          		</div>
	            </td>
	      	</tr>
	
			<div class="errorstyle" id="error_search" style="display: none;"></div>
	
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			
					<tr><td><%@ include file='searchBill-searchresults.jsp'%></td></tr>
					 <s:if test="%{sourcePage.equals('cancelBill')}">
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
								value="Cancel Bill" id="addButton"
								name="cancelBill" onclick="cancelContractorBill()"
								align="center" />
							</div>
						</td>
					</tr>
					</s:if>
				</table>	
		</table>
		</td>
        </tr>
	</table>
   </div><!-- end of rbroundbox2 -->
   <div class="rbbot2"><div></div></div>
   </div><!-- end of insidecontent -->
   </div><!-- end of formmainbox -->
</s:form>
</body>
</html>
