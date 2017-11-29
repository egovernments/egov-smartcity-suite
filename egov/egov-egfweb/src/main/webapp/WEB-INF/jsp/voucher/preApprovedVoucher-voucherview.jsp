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
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn" %>

<html>

<head>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"> </script>

<script>
function printEJV(){
	var id = '<s:property value="voucherHeader.id"/>';
	window.open("${pageContext.request.contextPath}/report/expenseJournalVoucherPrint-print.action?id="+id,'Print','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}
function printJV(){
	var id = '<s:property value="voucherHeader.id"/>';
	window.open("${pageContext.request.contextPath}/voucher/journalVoucherPrint-print.action?id="+id,'Print','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}
function openSource(){
	if("<s:property value='%{voucherHeader.vouchermis.sourcePath}' escapeHtml='false'/>"=="" || "<s:property value='%{voucherHeader.vouchermis.sourcePath}'/>"=='null')
		bootbox.alert('Source is not available');
	else{
		if("<s:property value='%{voucherHeader.vouchermis.sourcePath}' escapeHtml='false'/>".indexOf('EGF') > -1
				&& "<s:property value='%{billRegister.egBillregistermis.sourcePath}' escapeHtml='false'/>".indexOf('EGF') <= -1)
			var url = '<s:property value="%{voucherHeader.vouchermis.sourcePath}" escapeHtml="false"/>'+ '&showMode=view';
		else
			var url = '<s:property value="%{voucherHeader.vouchermis.sourcePath}" escapeHtml="false"/>';
		window.open(url,'Source','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')

	}   
	//window.open(url,'Source','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	
}
function validate(name,value){
	document.getElementById("actionName").value= name;
	document.getElementById('lblError').innerHTML ="";
<s:if test="%{wfitemstate !='END'}">
	 if( (value == 'Approve' || value=='Send for Approval'|| value == 'Forward' ) && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
		document.getElementById('lblError').innerHTML ="Please Select the user";
		return false;
	}
</s:if>
	return true;
}
function onSubmit()
{
			document.forms[0].action='${pageContext.request.contextPath}/voucher/preApprovedVoucher-update.action';
    		document.forms[0].submit();
			
}
</script>

<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title><s:property value="type" /> Journal Voucher Approval</title>
</head>

<body onload="refreshInbox()">
	<s:form action="preApprovedVoucher" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="PJV-Approval" />
		</jsp:include>
		<s:token />
		<font style='color: red;'>
			<p class="error-block" id="lblError" style="font: bold"></p>
		</font>
		<span class="mandatory1"> <s:actionerror /> <s:fielderror />
			<s:actionmessage />
		</span>
		<div class="formmainbox">

			<div class="subheadnew">
				<s:property value="type" />
				Journal Voucher Approval
			</div>
			<div id="listid" style="display: block">
				<br />
				<div align="center">
					<table border="0" width="100%" cellspacing="0">
						<tr>
							<td width="10%" class="greybox"><s:property value="type" />
								Journal Voucher Number :</td>
							<td width="25%" class="greybox"><s:property
									value="%{voucherHeader.voucherNumber}" /></td>
							<td width="10%" class="greybox">Date :</td>
							<td width="25%" class="greybox"><s:date
									name="voucherHeader.voucherDate" format="dd/MM/yyyy" /></td>
						</tr>
					</table>
				</div>
				<jsp:include page="voucherViewHeader.jsp" />
				<div align="center">
					<table border="0" width="100%" cellspacing="0">
						<tr>
							<td width="10%" class="<c:out value='${tdclass}' />">Bill
								Number :</td>
							<td width="25%" class="<c:out value='${tdclass}' />"><s:property
									value="%{getMasterName('billnumber')}" /></td>
							<td width="10%"></td>
							<td width="25%"></td>
						</tr>
					</table>
					<s:hidden id="vhid" name="vhid" value="%{voucherHeader.id}" />
					<s:hidden id="id" name="id" value="%{voucherHeader.id}" />

					<table>
						<tr class="bluebox">
							<a href="#" onclick=" return openSource();">Source</a>
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
									<td width="17%" class="bluebox setborder"><s:property
											value="glcode" /></td>
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
								<td class="greybox" style="text-align: right" colspan="3" />Total
								</td>
								<td class="greybox" style="text-align: right"><fmt:formatNumber
										value="${db}" pattern="#0.00" /></td>
								<td class="greybox" style="text-align: right"><fmt:formatNumber
										value="${cr}" pattern="#0.00" /></td>
							</tr>
						</table>
						<s:hidden name="actionName" id="actionName" />
					</div>
					<br />
				</div>
				<div align="center">
					<table border="1" width="100%">
						<tr>
							<td colspan="4"><strong>Sub-Ledger Details</strong></td>
						</tr>
						<tr>
							<th class="bluebgheadtd" width="18%">Account Code</th>
							<th class="bluebgheadtd" width="17%">Detail Type</th>
							<th class="bluebgheadtd" width="19%">Detail Key</th>
							<th class="bluebgheadtd" width="17%">Amount</th>
						</tr>
						<s:iterator var="p" value="%{getMasterName().tempList}" status="s">
							<tr>
								<td width="18%" class="bluebox setborder"><s:property
										value="glcode" /></td>
								<td width="18%" class="bluebox setborder"><s:property
										value="detailtype" /></td>
								<td width="18%" class="bluebox setborder"><s:property
										value="detailkey" /></td>
								<td width="18%" class="bluebox setborder"
									style="text-align: right"><s:text name="format.number">
										<s:param value="%{amount}" />
									</s:text></td>
							</tr>
						</s:iterator>
					</table>
				</div>

				<div id="wfHistoryDiv">
					<jsp:include page="../workflow/workflowHistory.jsp" />
				</div>
				<%@ include file='../workflow/commonWorkflowMatrix.jsp'%>
				<%@ include file='../workflow/commonWorkflowMatrix-button.jsp'%>


			</div>
		</div>
		<div class="buttonbottom" id="buttondiv">

			<s:if test="%{type == finConstExpendTypeContingency}">
				<input type="button" class="button" id="print" value="Print Preview"
					action="expenseJournalVoucherPrint" method="print"
					onclick="printEJV()" />
			</s:if>
			<s:else>
				<input type="button" class="button" id="print" value="Print Preview"
					action="journalVoucherPrint" method="print" onclick="printJV()" />
			</s:else>

		</div>



	</s:form>
</body>

</html>
