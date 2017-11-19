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
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/contra.js?rnd=${app_release_no}"></script>

<html>
<head>
<title>Payment Search</title>
</head>
<body>
	<s:form action="paymentReversal" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Voucher Search" />
		</jsp:include>
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">Payment Search</div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="greybox"><s:text name="voucher.mode.of.payment" />
					</td>
					<td class="greybox"><s:select name="name" id="voucherName"
							list="dropdownData.voucherNameList" headerKey="-1"
							headerValue="----Choose----" /></td>
					<td class="greybox"><s:text name="voucher.number" /></td>
					<td class="greybox"><s:textfield name="voucherNumber"
							id="voucherNumber" maxlength="25" value="%{voucherNumber}" /></td>
				</tr>
				<jsp:include page="../voucher/voucher-filter.jsp" />
				<tr>
					<td class="bluebox"><s:text name="voucher.fromdate" /><span
						class="mandatory">*</span></td>
					<td class="bluebox"><input type="text" id="fromDate"
						name="fromDate" style="width: 100px"
						value='<s:date name="fromDate" format="dd/MM/yyyy"/>'
						onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('forms[0].fromDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
					<td class="bluebox"><s:text name="voucher.todate" /><span
						class="mandatory">*</span></td>
					<td class="bluebox"><input type="text" id="toDate"
						name="toDate" style="width: 100px"
						value='<s:date name="toDate" format="dd/MM/yyyy"/>'
						onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('forms[0].toDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
				</tr>

				<tr>
					<td class="greybox" width="30%"><s:text name="payin.bank" /></td>
					<td class="greybox"><s:select name="bankBranch" id="bankId"
							list="dropdownData.bankList" listKey="bankBranchId"
							listValue="bankBranchName" headerKey="-1"
							headerValue="----Choose----" onChange="populateAccNum(this);" /></td>
					<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']"
						dropdownId="accountNumber"
						url="voucher/common!ajaxLoadAccNum.action" />
					<td class="greybox"><s:text name="payin.accountNum" /></td>
					<td class="greybox"><s:select name="bankAccount"
							id="accountNumber" list="dropdownData.accNumList" listKey="id"
							listValue="accountnumber" headerKey="-1"
							headerValue="----Choose----" /></td>
				</tr>

			</table>
			<div class="buttonbottom">
				<s:submit method="searchVouchersForReversal" value="Search"
					cssClass="buttonsubmit" onclick="return validate();" />
				<input type="submit" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
			<br />
			<div id="listid" style="display: none">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">
					<tr>
						<th class="bluebgheadtd">Sl.No.</th>
						<th class="bluebgheadtd">Voucher Number</th>
						<th class="bluebgheadtd">Voucher Type</th>
						<th class="bluebgheadtd">Voucher Name</th>
						<th class="bluebgheadtd">Voucher Date</th>
						<th class="bluebgheadtd">Fund Name</th>
						<th class="bluebgheadtd">Amount</th>
						<th class="bluebgheadtd">Status</th>
					</tr>
					<c:set var="trclass" value="greybox" />

					<s:iterator var="p" value="paymentHeaderList" status="s">
						<tr>
							<td class="<c:out value="${trclass}"/>"><s:property
									value="#s.index+1" /></td>
							<td align="left" class="<c:out value="${trclass}"/>"><s:if
									test="%{voucherheader.name=='Direct Bank Payment'}">
									<a href="#"
										onclick="openVoucher(<s:property value='%{voucherheader.id}'/>,'directBankPayment!beforeReverse.action?voucherHeader.id' );"><s:property
											value="%{voucherheader.voucherNumber}" /> </a>
								</s:if> <s:else>
									<a href="#"
										onclick="openVoucher(<s:property value='%{id}'/>,'paymentReversal!reverse.action?paymentHeader.id' );"><s:property
											value="%{voucherheader.voucherNumber}" /> </a>
								</s:else></td>
							<td align="left" class="<c:out value="${trclass}"/>"><s:property
									value="%{voucherheader.type}" /></td>
							<td class="<c:out value="${trclass}"/>"><s:property
									value="%{voucherheader.name}" /></td>
							<td class="<c:out value="${trclass}"/>"><s:date
									name="%{voucherheader.voucherDate}" format="dd/MM/yyyy" /></td>
							<td align="left" class="<c:out value="${trclass}"/>"><s:property
									value="%{voucherheader.fundId.name}" /></td>
							<td style="text-align: right" class="<c:out value="${trclass}"/>">
								<s:text name="format.number">
									<s:param value="%{voucherheader.totalAmount}" />
								</s:text>
							</td>
							<td class="<c:out value="${trclass}"/>"><s:if
									test="%{voucherheader.isConfirmed == 0}">UnConfirmed</s:if> <s:else>Confirmed</s:else>
							</td>
							<c:choose>
								<c:when test="${trclass=='greybox'}">
									<c:set var="trclass" value="bluebox" />
								</c:when>
								<c:when test="${trclass=='bluebox'}">
									<c:set var="trclass" value="greybox" />
								</c:when>
							</c:choose>
						</tr>
					</s:iterator>
					<s:hidden name="targetvalue" value="%{target}" id="targetvalue" />
				</table>
			</div>
			<br />
			<div id="msgdiv" style="display: none">
				<table align="center" class="tablebottom" width="80%">
					<tr>
						<th class="bluebgheadtd" colspan="7">No Records Found
						</td>
					</tr>
				</table>
			</div>
			<br /> <br />
			<s:hidden name="showMode" id="showMode" />
	</s:form>

	<script>
		function openVoucher(pid,url){
			var url =  url+'='+ pid;
			window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
		}
 		   <s:if test="%{paymentHeaderList.size==0 and message!=''}">
				dom.get('msgdiv').style.display='block';
			</s:if>
			<s:if test="%{paymentHeaderList.size!=0}">
				dom.get('msgdiv').style.display='none';
				dom.get('listid').style.display='block';
			</s:if>	
	function validate(){
		var fromDate = document.getElementById('fromDate').value;
		var toDate = document.getElementById('toDate').value;
		if(fromDate == ''){
			bootbox.alert('Select From Date');
			return false;
		}
		if(toDate == ''){
			bootbox.alert('Select To Date');
			return false;
		}
		return true;
	}
		</script>
</body>
</html>
