<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>
   
<html> 
<head>
	<title><s:text name='page.title.search.contractorbill' /></title>
	
</head>	
<style>  
.ui-button
{
	position: absolute;
	height: 2.0em;
}
</style>
<script type="text/javascript">



function disableSelect(){
	document.getElementById('status').disabled=true
}
function enableSelect(){
	document.getElementById('status').disabled=false
}
function validateSearch()
{
	
	if(dom.get('contractorId').value==-1 && dom.get('workordercode').value=="" && dom.get('billno').value=="" && dom.get('fromDate').value=="" && dom.get('toDate').value=="" && dom.get('status').value==-1)
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
	else if(!$F('workordercode').blank() && $F('workordercode').strip().length < 4) {
		$('searchBill_error').show();
		dom.get("searchBill_error").innerHTML='Work order number should be minimum four characters'; 			
		return false;
    }
    else{
    if(dom.get("errorstyle")){
     dom.get("errorstyle").style.display='none';
	 dom.get("errorstyle").innerHTML=''; 
	 }
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

function getApprovedRSD(workOrderId){
    makeJSONCall(["workOrderNumber"],'${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!getApprovedReturnSecurityDepositForWO.action',{workOrderId:workOrderId},rsdLoadSuccessHandler,rsdLoadFailureHandler) ;
}

rsdLoadSuccessHandler = function(req,res){
	  results=res.results;
	  var woNo=''
	  if(results != '') {
	  	woNo=results[0].workOrderNumber;
	  }
	if(woNo != ''){
		dom.get("searchBill_error").style.display='';
		document.getElementById("searchBill_error").innerHTML='<s:text name="cancelContractorBill.returnSecurityDeposit.created.message"/>'+woNo;
		window.scroll(0,0);
		return false;
	}	

	var id = document.searchBillForm.contractorBillId.value;
	var workordercode=dom.get('workordercode').value;
	var contractorId=dom.get('contractorId').value;
	var billNumber=dom.get('billno').value;
	var fromDate= dom.get('fromDate').value;
	var toDate=dom.get('toDate').value;
	var status= dom.get('status').value;
	var currentPage=dom.get('currentPageNum').value;
	window.open('${pageContext.request.contextPath}/contractorBill/searchBill!cancelApprovedBill.action?contractorBillId='+id+'&sourcePage=cancelBill'
	+'&workordercode='+workordercode+'&contractorId='+contractorId+'&billNumber='+billNumber+'&fromDate='+fromDate+'&toDate='+toDate+'&status='+status+'&page='+currentPage,'_self');
		  
}

rsdLoadFailureHandler= function(){
    dom.get("searchBill_error").style.display='';
	document.getElementById("searchBill_error").innerHTML='Unable to get Return Security Deposit for Work Order';
}



function cancelContractorBill(){
	var id = document.searchBillForm.contractorBillId.value;
	var woId = document.searchBillForm.workOrderId.value;
	if(id!=''){
		getApprovedRSD(woId);	
	}
	else{
		dom.get("searchBill_error").style.display='';
		document.getElementById("searchBill_error").innerHTML='<s:text name="contractorBill.not.selected" />';
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
	dom.get("contractorBillId").value = elem.value; 
}

</script>

<body onload="init();">
 <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
<div class="errorstyle" id="searchBill_error" style="display: none;"></div>
<s:form name="searchBillForm" action="searchBill" theme="simple" onsubmit="enableSelect()">
<s:hidden id="sourcePage" name="sourcePage" />
<s:hidden id="contractorBillId" name="contractorBillId" />
<s:hidden id="billNumber" name="billNumber" />
<s:hidden id="workOrderId" name="workOrderId" />
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
	        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
	            <div class="headplacer"><s:text name="page.subheader.search.estimate" /></div></td>
	        </tr>

			 <tr>
				 <td width="11%" class="whiteboxwk"><s:text name="contractorBill.status" />:</td>
		         <td width="21%" class="whitebox2wk" colspan="3"><s:select id="status" name="status" value="%{status}" headerKey="-1" headerValue="ALL" cssClass="selectwk" list="%{billStatuses}"  listKey="code" listValue="description" /></td>
		         <s:if test="%{sourcePage=='cancelBill'}">
						<script>disableSelect();</script>
				</s:if>				
			</tr>
		    <tr>
		         <td width="11%" class="greyboxwk"><s:text name="contractorBill.workorderCode" />:</td>
		         <td width="21%" class="greybox2wk"><s:textfield id="workordersearch" name="workordercode" value="%{workordercode}" id="workordercode" cssClass="selectwk" /></td>
		         <td width="15%" class="greyboxwk"><s:text name="contractorBill.billNo" />:</td>
		         <td width="53%" class="greybox2wk"><s:textfield name="billno" value="%{billno}" id="billno" cssClass="selectwk" /></td>
		         
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
												src="${pageContext.request.contextPath}/image/calendar.png"
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
															src="${pageContext.request.contextPath}/image/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>
										<span id='errortoDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
				</td>	 
		   </tr>
		   <tr>
		   		 <td width="15%" class="greyboxwk"><s:text name="contractorBill.contractor" />:</td>
				 <td width="21%" class="greybox2wk" colspan="3"><s:select id="contractorId" name="contractorId"
															cssClass="selectwk"
															list="%{contractorForApprovedWorkOrder}"
															headerKey="-1"															
															headerValue=" " 
															value="%{contractorId}"/>
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
			
					<tr><td><div class="buttonholderwk">
							</div>
						</td>
					</tr>
				</table>	
		</table>
		</td>
        </tr>
	</table>
   <%@ include file='searchBill-searchresults.jsp'%>
   </div><!-- end of rbroundbox2 -->
   <s:if test="%{sourcePage.equals('cancelBill')}">
	<P align="center">
		<input type="button" class="buttonadd"
			value="Cancel Bill" id="addButton"
			name="cancelBill" onclick="cancelContractorBill()"
			align="center" />
	</P>
	</s:if>
   <div class="rbbot2"><div></div></div>
   </div><!-- end of insidecontent -->
   </div><!-- end of formmainbox -->
</s:form>
</body>
</html>