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


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<script>
var totalAmount=0;
function setTotalAmount()
{
	document.getElementById('totalAmount').value=totalAmount;
	var t = document.getElementById('paymentTable').rows;
	//t.length-2 because the Total Amount is put in a seperate row
	var numberOfSelectedRows=0;
	for(var i=0;i<t.length-2;i++)
	{
		if(document.getElementById('isSelected'+i).checked==true||document.getElementById('isSelected'+i).checked=="checked")
		{
			totalAmount+=parseFloat(document.getElementById("chequeAssignmentList["+i+"].paidAmount").value);
			numberOfSelectedRows++;
		}
		document.getElementById('selectedRows').value=numberOfSelectedRows;
		document.getElementById('totalAmount').value=totalAmount.toFixed(2);
	}
}
</script>
<head>
<link rel="stylesheet" type="text/css" href="/EGF/resources/css/ccMenu.css?rnd=${app_release_no}" />
<title>Cheque Assignment Search for Salary Payments</title>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
</head>
<body onLoad="setTotalAmount()">
	<s:form action="chequeAssignment" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Cheque Assignment Search" />
		</jsp:include>
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="chq.assignment.heading.salary.search" />
			</div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0"
				id="paymentTable">
				<tr>
					<th class="bluebgheadtdnew"><s:text
							name="chq.assignment.select" />
						<s:checkbox id="selectall" name="selectall"
							onclick="checkAll(this)" /></th>
					<th class="bluebgheadtdnew"><s:text name="Sl No" /></th>
					<th class="bluebgheadtdnew"><s:text
							name="chq.assignment.partyname" /></th>
					<th class="bluebgheadtdnew"><s:text
							name="chq.assignment.payment.voucherno" /></th>
					<th class="bluebgheadtdnew"><s:text
							name="chq.assignment.payment.voucherdate" /></th>
					<th class="bluebgheadtdnew"><s:text
							name="chq.assignment.payment.amount" /></th>
					<s:if test="%{reassignSurrenderChq && paymentMode=='cheque'}">
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.serialno" /></th>
						<th class="bluebgheadtdnew" width="10%"><s:text
								name="chq.assignment.instrument.no" /><span class="mandatory">*</span></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.date" /><span class="mandatory">*</span><br>(dd/mm/yyyy)</th>
					</s:if>
					<s:elseif
						test="%{!isChequeNoGenerationAuto() && paymentMode=='cheque'}">
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.serialno" /></th>
						<th class="bluebgheadtdnew" width="10%"><s:text
								name="chq.assignment.instrument.no" /><span class="mandatory">*</span></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.date" /><span class="mandatory">*</span><br>(dd/mm/yyyy)</th>
					</s:elseif>
				</tr>
				<s:iterator var="p" value="chequeAssignmentList" status="s">
					<tr>
						<td style="text-align: center" class="blueborderfortdnew"><s:hidden
								id="voucherHeaderId"
								name="chequeAssignmentList[%{#s.index}].voucherHeaderId"
								value="%{voucherHeaderId}" />
							<s:checkbox name="chequeAssignmentList[%{#s.index}].isSelected"
								id="isSelected%{#s.index}" onclick="update(this,%{#s.index})" /></td>
						<td align="left" style="text-align: center"
							class="blueborderfortdnew" />
						<s:property value="#s.index+1" />
						</td>
						<td style="text-align: center" class="blueborderfortdnew"><s:hidden
								id="paidTo" name="chequeAssignmentList[%{#s.index}].paidTo"
								value="%{paidTo}" />
							<s:property value="%{paidTo}" /></td>
						<td style="text-align: center" class="blueborderfortdnew"><s:hidden
								id="voucherNumber"
								name="chequeAssignmentList[%{#s.index}].voucherNumber"
								value="%{voucherNumber}" />
							<s:hidden id="detailtypeid"
								name="chequeAssignmentList[%{#s.index}].detailtypeid"
								value="%{detailtypeid}" />
							<s:hidden id="detailkeyid"
								name="chequeAssignmentList[%{#s.index}].detailkeyid"
								value="%{detailkeyid}" />
							<s:property value="%{voucherNumber}" /></td>
						<td style="text-align: center" class="blueborderfortdnew"><s:hidden
								id="voucherDate"
								name="chequeAssignmentList[%{#s.index}].voucherDate"
								value="%{voucherDate}" />
							<s:date name="%{voucherDate}" var="tempPaymentDate"
								format="dd/MM/yyyy" />
							<s:date name="%{voucherDate}" format="dd/MM/yyyy" />
							<s:hidden
								name="chequeAssignmentList[%{#s.index}].tempPaymentDate"
								value="%{tempPaymentDate}"></s:hidden></td>
						<td style="text-align: right" class="blueborderfortdnew"><s:hidden
								name="chequeAssignmentList[%{#s.index}].paidAmount"
								id="chequeAssignmentList[%{#s.index}].paidAmount"
								value="%{paidAmount}" />
							<s:text name="format.number">
								<s:param value="%{paidAmount}" />
							</s:text></td>

						<s:if test="%{reassignSurrenderChq && paymentMode=='cheque'}">
							<td style="text-align: right" class="blueborderfortdnew"><s:select
									name="chequeAssignmentList[%{#s.index}].serialNo"
									id="chequeAssignmentList[%{#s.index}].serialNo"
									list="chequeSlNoMap"
									value='%{chequeAssignmentList[%{#s.index}].serialNo}' /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:textfield
									id="chequeNumber%{#s.index}"
									name="chequeAssignmentList[%{#s.index}].chequeNumber"
									value="%{chequeNumber}"
									onchange="validateReassignSurrenderChequeNumber(this)"
									size="10" /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:date
									name="chequeDate" var="tempChequeDate" format="dd/MM/yyyy" />
								<s:textfield id="chequeDate%{#s.index}"
									name="chequeAssignmentList[%{#s.index}].chequeDate"
									value="%{tempChequeDate}" size="10" /><a
								href="javascript:show_calendar('forms[0].chequeDate<s:property value="#s.index"/>');"
								style="text-decoration: none">&nbsp;<img
									src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a></td>
						</s:if>
						<s:elseif
							test="%{!isChequeNoGenerationAuto() && paymentMode=='cheque'}">
							<td style="text-align: right" class="blueborderfortdnew"><s:select
									name="chequeAssignmentList[%{#s.index}].serialNo"
									id="chequeAssignmentList[%{#s.index}].serialNo"
									list="chequeSlNoMap"
									value='%{chequeAssignmentList[%{#s.index}].serialNo}' /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:textfield
									id="chequeNumber%{#s.index}"
									name="chequeAssignmentList[%{#s.index}].chequeNumber"
									value="%{chequeNumber}" onchange="validateChequeNumber(this)"
									size="10" /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:date
									name="chequeDate" var="tempChequeDate" format="dd/MM/yyyy" />
								<s:textfield id="chequeDate%{#s.index}"
									name="chequeAssignmentList[%{#s.index}].chequeDate"
									value="%{tempChequeDate}" size="10" /><a
								href="javascript:show_calendar('forms[0].chequeDate<s:property value="#s.index"/>');"
								style="text-decoration: none">&nbsp;<img
									src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a></td>
						</s:elseif>
					</tr>
				</s:iterator>
				<s:if test="%{chequeAssignmentList!=null}">
					<s:if test="%{chequeAssignmentList.size()!=0}">
						<tr>
							<td style="text-align: center" class="blueborderfortdnew">&nbsp</td>
							<td align="left" style="text-align: center"
								class="blueborderfortdnew" /> &nbsp
							</td>  
					  	  <td align="left"   style="text-align:center" class="blueborderfortdnew"/> &nbsp </td>  
					  	  <td align="left"   style="text-align:center" class="blueborderfortdnew"/> &nbsp </td>  
		      			  <td style="text-align:center" class="blueborderfortdnew">Total Amount - </td>
		      			  <td style="text-align:right" class="blueborderfortdnew"><input type="text" id="totalAmount" readonly="readonly" style="text-align:right" name="totalAmount" /> </td>
						</tr>
					</s:if>
				</s:if>
			</table>
			<div class="subheadsmallnew" id="noRecordsDiv"
				style="visibility: hidden">No Records Found</div>
			<br />
			<div id="departmentDiv" style="visibility: visible">
				<s:hidden name="reassignSurrenderChq" />
				<table align="center" width="100%" cellspacing="0">
					<tr>
						<s:if test="%{paymentMode=='rtgs'}">
							<td class="greybox"><s:text name="chq.assignment.rtgs.refno" /><span
								class="mandatory">*</span> <s:textfield id="rtgsRefNo"
									name="rtgsRefNo" value="%{rtgsRefNo}" maxlength="150" /></td>
							<td class="greybox"><s:text name="chq.assignment.rtgs.date" /><span
								class="mandatory">*</span> <s:textfield name="rtgsDate"
									id="rtgsDate" maxlength="20" value="%{rtgsDate}"
									onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
								href="javascript:show_calendar('forms[0].rtgsDate');"
								style="text-decoration: none">&nbsp;<img
									src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)
							</td>

						</s:if>
						<s:else>
							<td class="greybox"><s:text name="chq.assignment.department" /><span
								class="mandatory">*</span> <s:select
									name="vouchermis.departmentid" id="departmentid"
									list="dropdownData.departmentList" listKey="id"
									listValue="name" headerKey="-1"
									headerValue="----Choose----"
									value="%{voucherHeader.vouchermis.departmentid.id}" /></td>
							<td class="greybox"><s:text
									name="chq.assignment.instrument.serialno" /><span
								class="mandatory">*</span> <s:select name="serialNo"
									id="serialNo" list="chequeSlNoMap" value='%{serialNo}' /></td>
							<s:if test="%{reassignSurrenderChq && paymentMode!='cheque'}">
								<td class="greybox"><s:text
										name="chq.assignment.instrument.no" /><span class="mandatory">*</span>
									<s:textfield id="chequeNumber0" name="chequeNo"
										value="%{chequeNo}"
										onchange="validateReassignSurrenderChequeNumber(this)" /></td>
								<td class="greybox"><s:text
										name="chq.assignment.instrument.date" /><span
									class="mandatory">*</span>(dd/mm/yyyy) <s:date name="chequeDt"
										var="tempChequeDate" format="dd/MM/yyyy" />
									<s:textfield id="chequeDt" name="chequeDt"
										value="%{tempChequeDate}"
										onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
									href="javascript:show_calendar('forms[0].chequeDt');"
									style="text-decoration: none">&nbsp;<img
										src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a><br />(dd/mm/yyyy)
								</td>
							</s:if>


							<s:elseif
								test="%{!isChequeNoGenerationAuto() && paymentMode!='cheque'}">
								<td class="greybox"><s:text
										name="chq.assignment.instrument.no" /><span class="mandatory">*</span>
									<s:textfield id="chequeNumber0" name="chequeNo"
										value="%{chequeNo}" onchange="validateChequeNumber(this)" /></td>
								<td class="greybox"><s:text
										name="chq.assignment.instrument.date" /><span
									class="mandatory">*</span>(dd/mm/yyyy) <s:date name="chequeDt"
										var="tempChequeDate" format="dd/MM/yyyy" />
									<s:textfield id="chequeDt" name="chequeDt"
										value="%{tempChequeDate}"
										onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
									href="javascript:show_calendar('forms[0].chequeDt');"
									style="text-decoration: none">&nbsp;<img
										src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a><br />(dd/mm/yyyy)
								</td>
							</s:elseif>
							<s:if test="%{paymentMode=='cash'}">
								<td class="greybox"><s:text
										name="chq.assignment.instrument.infavourof" /><span
									class="mandatory">*</span> <s:textfield id="inFavourOf"
										name="inFavourOf" value="%{inFavourOf}" maxlength="50" /></td>
							</s:if>
						</s:else>

					</tr>
				</table>
			</div>
			<div class="buttonbottom">
				<s:hidden id="assignmentType" name="assignmentType"
					value="%{assignmentType}" />
				<s:hidden id="selectedRows" name="selectedRows"
					value="%{selectedRows}" />
				<s:hidden id="paymentMode" name="paymentMode" value="%{paymentMode}" />
				<s:hidden id="bankaccount" name="bankaccount" value="%{bankaccount}" />
				<s:hidden id="isSalaryChequeAssignment"
					name="isSalaryChequeAssignment" value="%{isSalaryChequeAssignment}" />
				<s:if test="%{paymentMode=='rtgs'}">
					<s:submit id="assignChequeBtn"
						method="createInstrumentForSalaryPayment" value="Process"
						cssClass="buttonsubmit" onclick="return validate()" />
				</s:if>
				<s:else>
					<s:submit id="assignChequeBtn"
						method="createInstrumentForSalaryPayment" value="Assign Cheque"
						cssClass="buttonsubmit" onclick="return validate()" />
				</s:else>
				<input type="button" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
		</div>
		<s:token />
	</s:form>
	<script>
			function update(obj,index)
			{
				if(obj.checked)
				{	document.getElementById('selectedRows').value=parseInt(document.getElementById('selectedRows').value)+1;
					totalAmount+=parseFloat(document.getElementById("chequeAssignmentList["+index+"].paidAmount").value);
				}
				else
				{	
					document.getElementById('selectedRows').value=parseInt(document.getElementById('selectedRows').value)-1;
					totalAmount-=parseFloat(document.getElementById("chequeAssignmentList["+index+"].paidAmount").value);
				}
				document.getElementById('totalAmount').value=totalAmount.toFixed(2);
			}
			function validate()
			{
				if(dom.get('departmentid') && dom.get('departmentid').options[dom.get('departmentid').selectedIndex].value==-1)
				{
					bootbox.alert('Select Cheque Issued From');
					return false;
				}
				if(dom.get('inFavourOf') && dom.get('inFavourOf').value=="")
				{
					bootbox.alert('Please enter valid data for the \'In Favour Of\' field');
					return false;
				}
				
				if(document.getElementById('selectedRows').value=='' || document.getElementById('selectedRows').value==0)
				{
					bootbox.alert('Please select the payment voucher');
					return false;
				}
				if(dom.get('rtgsRefNo') && (dom.get('rtgsRefNo').value=='' ||dom.get('rtgsRefNo').value=='0'||dom.get('rtgsDate').value=='0'|| dom.get('rtgsDate').value==''))
				{
					bootbox.alert('Please enter RTGS reference number and date ');
					return false;
				}
				<s:if test="%{paymentMode!='cheque'  && paymentMode!='rtgs'}">
				    return validateChequeDateForNonChequeMode();  
				</s:if> 
				<s:if test="%{paymentMode=='rtgs'}">      
			   		 return validateChequeDateForRtgsMode();  
				</s:if> 
				///dom.get('departmentid').disabled=false;
				return true;
			}
			function validateChequeNumber(obj)
			{
				if(isNaN(obj.value))
				{
					bootbox.alert('Cheque number contains alpha characters.');
					obj.value='';
					return false;
				}
				if(obj.value.length!=6)
				{
					bootbox.alert("Cheque number must be 6 digits long.");
					obj.value='';
					return false;
				}
				//Cheque number might contain . or - which is not handled by isNaN
				var pattPeriod=/\./i;
				var pattNegative=/-/i;
				if(obj.value.match(pattPeriod)!=null || obj.value.match(pattNegative)!=null )
				{
					bootbox.alert('Cheque number should contain only numbers');
					obj.value='';
					return false;
				}
				var index = obj.id.substring(12,obj.id.length);
				if(obj.value=='')
					return true;
					
				if(dom.get('departmentid') && dom.get('departmentid').options[dom.get('departmentid').selectedIndex].value==-1)
				{
					bootbox.alert('Select Cheque Issued From');
					obj.value='';
					return false;
				}
				var name=obj.name;
				name=name.replace("chequeNo","serialNo");
				var dept = dom.get('departmentid').options[dom.get('departmentid').selectedIndex].value;
				var slNo = dom.get(name).options[dom.get(name).selectedIndex].value;
				var url = '${pageContext.request.contextPath}/voucher/common!ajaxValidateChequeNumber.action?bankaccountId='+document.getElementById('bankaccount').value+'&chequeNumber='+obj.value+'&index='+index+'&departmentId='+dept+"&serialNo="+slNo;
				var transaction = YAHOO.util.Connect.asyncRequest('POST', url,callback , null);
			}
			
			function validateReassignSurrenderChequeNumber(obj)
			{
				if(isNaN(obj.value))
				{
					bootbox.alert('Cheque number contains alpha characters.');
					obj.value='';
					return false;
				}
				if(obj.value.length!=6)
				{
					bootbox.alert("Cheque number must be 6 digits long.");
					obj.value='';
					return false;
				}
				//Cheque number might contain . or - which is not handled by isNaN
				var pattPeriod=/\./i;
				var pattNegative=/-/i;
				if(obj.value.match(pattPeriod)!=null || obj.value.match(pattNegative)!=null )
				{
					bootbox.alert('Cheque number should contain only numbers');
					obj.value='';
					return false;
				}
				var index = obj.id.substring(12,obj.id.length);
				if(obj.value=='')
					return true;
					
				if(dom.get('departmentid') && dom.get('departmentid').options[dom.get('departmentid').selectedIndex].value==-1)
				{
					bootbox.alert('Select Cheque Issued From');
					obj.value='';
					return false;
				}
				
				var name=obj.name;
				if(name=='chequeNo')
				{
				//name and Id will be same or made same .....
				name=name.replace("chequeNo","serialNo");
				}
				if(name.indexOf('chequeNumber')!=-1)
				{
				name=name.replace("chequeNumber","serialNo");
				}
				var slNo = dom.get(name).options[dom.get(name).selectedIndex].value;
				
				
				var dept = dom.get('departmentid').options[dom.get('departmentid').selectedIndex].value;
				var url = '${pageContext.request.contextPath}/voucher/common!ajaxValidateReassignSurrenderChequeNumber.action?bankaccountId='+document.getElementById('bankaccount').value+'&chequeNumber='+obj.value+'&index='+index+'&departmentId='+dept+"&serialNo="+slNo;
				var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackReassign, null);
			}
			var callback = {
				success: function(o) {
					var res=o.responseText;
					res = res.split('~');
					if(res[1]=='false')
					{
						bootbox.alert('Enter valid cheque number or This Cheque number has been already used');
						document.getElementById('chequeNumber'+parseInt(res[0])).value='';
					}
			    },
			    failure: function(o) {
			    	bootbox.alert('failure');
			    }
			}
				var callbackReassign = {
				success: function(o) {
					var res=o.responseText;
					res = res.split('~');
					if(res[1]=='false')
					{
						bootbox.alert('This cheque number is not there in the surrendered list');     
						document.getElementById('chequeNumber'+parseInt(res[0])).value='';
					}
			    },
			    failure: function(o) {
			    	bootbox.alert('failure');
			    }
			}
			function nextChqNo(obj) 
			{
				var index = obj.id.substring(11,obj.id.length);
				var sRtn = showModalDialog("../HTML/SearchNextChqNo.html?accntNoId="+document.getElementById('bankaccount').value, "","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
				if (sRtn != undefined)
					document.getElementById("chequeNumber"+index).value = sRtn;
			}
			function validateChequeDateForNonChequeMode(){
				var noOfSelectedRows=document.getElementById('selectedRows').value;
				//bootbox.alert("sizseled"+noOfSelectedRows);
				var chkCount=0;
				var isSelected=0;
				var chequeSize='<s:property value ="%{chequeAssignmentList.size()}"/>';
				var chequeDate=document.getElementById('chequeDt').value;
				var chequeNo=document.getElementById('chequeNumber0').value;
				
				if(chequeNo==null || chequeNo==''){
					bootbox.alert("Please enter a valid cheque Number");
						return false;   
				}
				if(isNaN( Date.parse( chequeDate))) {                
					bootbox.alert("Please enter a valid cheque date");
					return false;
				 }
				for(var index=0;index<chequeSize;index++){
					var paymentDate= document.getElementsByName("chequeAssignmentList["+index+"].tempPaymentDate")[0].value; 
					
					if(document.getElementById('isSelected'+index).checked){
					//bootbox.alert(document.getElementById('isSelected'+index).checked);
					if( compareDate(paymentDate,chequeDate) == -1){     
					  //  bootbox.alert(paymentDate+"----"+chequeDate);      
						bootbox.alert('Cheque Date cannot be less than  payment Date');
						document.getElementById('chequeDt').value='';
						document.getElementById('chequeDt').focus();
						return false;
					 }
					}
					
					
				}
				return true;
			}

			function validateChequeDateForRtgsMode(){
				var noOfSelectedRows=document.getElementById('selectedRows').value;
				//bootbox.alert("sizseled"+noOfSelectedRows);
				var chkCount=0;
				var isSelected=0;
				var chequeSize='<s:property value ="%{chequeAssignmentList.size()}"/>';
				var chequeDate=dom.get('rtgsDate').value;
				
				if(isNaN( Date.parse( chequeDate))) {                
					bootbox.alert("Please enter a valid cheque date");
					return false;
				 }
				for(var index=0;index<chequeSize;index++){
					var paymentDate= document.getElementsByName("chequeAssignmentList["+index+"].tempPaymentDate")[0].value; 
					
					if(document.getElementById('isSelected'+index).checked){
					//bootbox.alert(document.getElementById('isSelected'+index).checked);
					chkCount++;
					if( compareDate(paymentDate,chequeDate) == -1){     
					  //  bootbox.alert(paymentDate+"----"+chequeDate);      
						bootbox.alert('Cheque Date cannot be less than  payment Date');
						document.getElementById('rtgsDate').value='';
						document.getElementById('rtgsDate').focus();
						return false;
					 }    
					if(chkCount==noOfSelectedRows){ break;}
					}  
				}
				return true;
			}
			
			function checkAll(obj)
			{
				var t = document.getElementById('paymentTable').rows;
				if(obj.checked)
				{
					totalAmount=0;
					//t.length-2 because the Total Amount is put in a seperate row
					for(var i=0;i<t.length-2;i++)
					{
						document.getElementById('isSelected'+i).checked=true;
						totalAmount+=parseFloat(document.getElementById("chequeAssignmentList["+i+"].paidAmount").value);
					}
					document.getElementById('selectedRows').value=t.length-2;
				}
				else
				{
					//t.length-2 because the Total Amount is put in a seperate row
					for(var i=0;i<t.length-2;i++)
						document.getElementById('isSelected'+i).checked=false;
					document.getElementById('selectedRows').value=0;
					totalAmount=0;
				}
				document.getElementById('totalAmount').value=totalAmount.toFixed(2);
			}
		</script>
	<s:if
		test="chequeAssignmentList == null || chequeAssignmentList.size==0">
		<script>
				document.getElementById('noRecordsDiv').style.visibility='visible';
				document.getElementById('departmentDiv').style.visibility='hidden';
				document.getElementById('assignChequeBtn').style.display='none';
			</script>
	</s:if>
</body>
</html>
