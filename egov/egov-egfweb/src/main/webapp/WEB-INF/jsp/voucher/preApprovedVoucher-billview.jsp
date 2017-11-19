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


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>

<html>

<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title><s:property value="type" /> JV-Create</title>
</head>
<script>
	function checkBillIdBillview(){
		if(document.getElementById('id').value!=''){
			document.getElementById('aa_approve').disabled=true;
		}else{
			document.getElementById('aa_approve').disabled=false;
		}
		if('<s:property value="voucherHeader.id"/>' ==''){
			document.getElementById('print').disabled=true;
		}else{
			document.getElementById('print').disabled=false;
		}
		if(document.getElementById('approverDepartment'))
			document.getElementById('approverDepartment').value = "-1";
	}
	
	function checkLength(obj){
		if(obj.value.length>1024)
		{
			bootbox.alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
			obj.value = obj.value.substring(1,1024);
		}
	}
	
	function printEJV(){
		var id = '<s:property value="voucherHeader.id"/>';
		window.open("${pageContext.request.contextPath}/report/expenseJournalVoucherPrint-print.action?id="+id,'Print','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	}
	function printJV(){
		var id = '<s:property value="voucherHeader.id"/>';
		window.open("${pageContext.request.contextPath}/voucher/journalVoucherPrint-print.action?id="+id,'Print','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	}
function openSource(){
	var url = '<s:property value='%{getSourcePath()}' />'
	if(url!=null && url==""){
		bootbox.alert("Source is not available");
		return false;
	}
	window.open(url,'Source','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')
}
function validateCutOff()
{
	console.log(document.getElementById("cutOffDate"));
var cutOffDatePart=document.getElementById("cutOffDate").value.split("/");
var voucherDatePart=document.getElementById("voucherDate").value.split("/");
var cutOffDate = new Date(cutOffDatePart[1] + "/" + cutOffDatePart[0] + "/"
		+ cutOffDatePart[2]);
var voucherDate = new Date(voucherDatePart[1] + "/" + voucherDatePart[0] + "/"
		+ voucherDatePart[2]);
if(voucherDate<=cutOffDate)
{
	return true;
}
else{
	var msg1='<s:text name="wf.vouchercutoffdate.message"/>';
	var msg2='<s:text name="wf.cutoffdate.msg"/>';
	bootbox.alert(msg1+" "+document.getElementById("cutOffDate").value+" "+msg2);
		return false;
	}
}
function onSubmit()
{
	var voucherdate =document.getElementById('voucherDate').value ;
	if(voucherdate!=null && voucherdate!=""){
		document.preApprovedVoucher.action='${pageContext.request.contextPath}/voucher/preApprovedVoucher-save.action';
		return true;
	}else{
		bootbox.alert("Please select voucher date");
		return false;
		}
}
jQuery(document).ready(function() {
jQuery("#voucherDate").datepicker().datepicker("setDate", new Date());
});

</script>
<body onload="checkBillIdBillview()">
	<s:form action="preApprovedVoucher" theme="simple"
		name="preApprovedVoucher" id="preApprovedVoucher">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Bill Voucher -Create" />
		</jsp:include>
		<font style='color: red;'>
			<p class="error-block" id="lblError" style="font: bold"></p>
		</font>
		<span class="mandatory1"> <s:actionerror /> <s:fielderror />
			<s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">
				Generate
				<s:property value="type" />
				Bill Voucher
			</div>
			<div id="listid" style="display: block">
				<br />
				<s:if test="%{isShowVoucherDate()}">
					<div align="center">
						<table border="0" width="100%" cellspacing="0">
							<tr>
								<td class="greybox" width="12%"><s:text name="voucher.date" /><span
									class="mandatory1">*</span></td>
								<td class="greybox" width="25%">
									<div name="daterow">
										<s:date name="voucherDate" var="voucherDateId"
											format="dd/MM/yyyy" />
										<s:textfield id="voucherDate" name="voucherDate"
											class="form-control datepicker" data-date-end-date="0d" />
									</div>
								</td>
								<td class="greybox" width="25%" />
								<td class="greybox" width="25%" />
							</tr>
						</table>
					</div>
				</s:if>
				<jsp:include page="voucherViewHeader.jsp" />

				<s:hidden id="billid" name="billid" value="%{egBillregister.id}" />
				<s:hidden id="vhid" name="vhid" value="%{voucherHeader.id}" />
				<s:hidden id="id" name="id" value="%{voucherHeader.id}" />

				<table align="center">
					<tr class="bluebox">
						<td><a href="#" onclick=" return openSource()">Source</a></td>
					</tr>
				</table>

				<br />
				<div align="center">
					<table border="1" width="100%">
						<tr>
							<td colspan="5"><strong>Account Details</strong></td>
						</tr>
						<tr>
							<th class="bluebgheadtd" width="18%">Function Name</th>
							<th class="bluebgheadtd" width="17%">Account&nbsp;Code</th>
							<th class="bluebgheadtd" width="19%">Account Head</th>
							<th class="bluebgheadtd" width="17%"><s:text
									name="billVoucher.approve.dbtamt" /></th>
							<th class="bluebgheadtd" width="16%"><s:text
									name="billVoucher.approve.crdamt" /></th>
						</tr>
						<s:iterator var="p" value="%{billDetails.tempList}" status="s">
							<tr>
								<td width="18%" class="bluebox setborder"><s:property
										value="function" /></td>
								<td width="17%" style="text-align: center"
									class="bluebox setborder"><s:property value="glcode" /></td>
								<td width="19%" class="bluebox setborder"><s:property
										value="accounthead" /></td>
								<td width="17%" class="bluebox setborder"
									style="text-align: right"><s:text name="format.number">
										<s:param value="%{debitamount}" />
									</s:text></td>
								<td width="16%" class="bluebox setborder"
									style="text-align: right"><s:text name="format.number">
										<s:param value="%{creditamount}" />
									</s:text></td>
								<c:set var="db" value="${db+debitamount}" />
								<c:set var="cr" value="${cr+creditamount}" />
							</tr>
						</s:iterator>
						<tr>
							<td class="greybox setborder" style="text-align: right"
								colspan="3" />Total
							</td>
							<td class="greybox setborder" style="text-align: right"><fmt:formatNumber
									value="${db}" pattern="#0.00" /></td>
							<td class="greybox setborder" style="text-align: right"><fmt:formatNumber
									value="${cr}" pattern="#0.00" /></td>
						</tr>
					</table>
					<s:hidden name="actionName" id="actionName" />
				</div>
				<div align="center">
					<table border="1" width="100%">
						<tr>
							<td colspan="5"><strong>Bill Payee Details</strong></td>
						</tr>
						<tr>
							<th class="bluebgheadtd" width="18%">Account Code</th>
							<th class="bluebgheadtd" width="17%">Detail Type</th>
							<th class="bluebgheadtd" width="19%">Detail Key</th>
							<th class="bluebgheadtd" width="17%"><s:text
									name="billVoucher.approve.dbtamt" /></th>
							<th class="bluebgheadtd" width="16%"><s:text
									name="billVoucher.approve.crdamt" /></th>
						</tr>
						<s:iterator var="p" value="%{billDetails.payeeList}" status="s">
							<tr>
								<td width="17%" style="text-align: center"
									class="bluebox setborder"><s:property value="glcode" /></td>
								<td width="19%" class="bluebox setborder"><s:property
										value="detailtype" /></td>
								<td width="17%" class="bluebox setborder"><s:property
										value="detailkey" /></td>
								<td width="16%" class="bluebox setborder"
									style="text-align: right"><s:text name="format.number">
										<s:param value="%{debitamount}" />
									</s:text></td>
								<td width="16%" class="bluebox setborder"
									style="text-align: right"><s:text name="format.number">
										<s:param value="%{creditamount}" />
									</s:text></td>
							</tr>
						</s:iterator>
					</table>
				</div>
				<s:if test='%{! wfitemstate.equalsIgnoreCase("END")}'>
					<%@include file="workflowApproval.jsp"%>
				</s:if>
				<div align="center">
					<table border="0" width="100%">
						<tr>
							<td class="bluebox">Comments</td>
							<td class="bluebox"><s:textarea name="comments"
									id="comments" cols="150" rows="3" onblur="checkLength(this)" /></td>
							<td><s:hidden id="methodName" name="methodName" value="save" /></td>
						</tr>
						<br />
					</table>
				</div>
				<s:if test="%{!mode.equalsIgnoreCase('save')}">
					<s:hidden id="cutOffDate" name="cutOffDate" />
					<%@ include file='../workflow/commonWorkflowMatrix.jsp'%>
					<%@ include file='../workflow/commonWorkflowMatrix-button.jsp'%>
				</s:if>
				<s:else>
					<div class="buttonbottom" align="center">
						<input type="button" name="button2" id="button2" value="Close"
							class="button" onclick="window.close();" />
					</div>
				</s:else>
			</div>
		</div>
		<s:if test="%{hasErrors()}">
			<script>
document.getElementById('id').value='';
	</script>
		</s:if>
		<s:token />
	</s:form>
</body>

</html>
