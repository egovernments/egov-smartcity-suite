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
<head>
<link rel="stylesheet" type="text/css"
	href="/EGF/resources/css/ccMenu.css?rnd=${app_release_no}" />
<title>Cheque Assignment View</title>
</head>
<body>
	<s:form action="chequeAssignment" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="RTGS Ref. No Assignment View" />
		</jsp:include>
		<span class="mandatory1"> <s:actionerror /> <s:fielderror />
			<s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">RTGS Ref. No Assignment View</div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>

					<th class="bluebgheadtdnew"><s:text
							name="chq.assignment.rtgs.refno" /></th>
					<th class="bluebgheadtdnew"><s:text
							name="chq.assignment.paymentvoucherno" /></th>
					<th class="bluebgheadtdnew"><s:text
							name="chq.assignment.rtgs.amount" /></th>
					<th class="bluebgheadtdnew"><s:text
							name="chq.assignment.rtgs.date" /></th>
					<th class="bluebgheadtdnew"><s:text
							name="chq.assignment.instrument.status" /></th>

				</tr>

				<s:set var="PreRtgsNumber" value="" />
				<s:iterator var="p" value="instVoucherList" status="s">
					<s:set var="rtgsNumber"
						value="%{instrumentHeaderId.transactionNumber}" />
					<s:if test="%{#rtgsNumber!=#PreRtgsNumber}">
						<tr>
							<td style="text-align: center" class="blueborderfortdnew"><strong><s:property
										value='%{instrumentHeaderId.transactionNumber}' /></strong> <a
								href="#"
								onclick="generateReport('pdf','<s:property value='%{instrumentHeaderId.id}'/>',
							'<s:property value="%{instrumentHeaderId.bankAccountId.id}" />',
							'<s:property value="%{instrumentHeaderId.bankAccountId.bankbranch.id}" />',
							'<s:property value="%{instrumentHeaderId.bankAccountId.bankbranch.bank.id}" />'); ">Generate
									pdf,</a> <a href="#"
								onclick="generateReport('xls','<s:property value='%{instrumentHeaderId.id}'/>',
							'<s:property value="%{instrumentHeaderId.bankAccountId.id}" />',
							'<s:property value="%{instrumentHeaderId.bankAccountId.bankbranch.id}" />',
							'<s:property value="%{instrumentHeaderId.bankAccountId.bankbranch.bank.id}" />'); ">Generate
									xls</a> <s:if test='%{billSubType.equalsIgnoreCase("TNEB")}'>
									<a href="#"
										onclick="generateReport('text','<s:property value='%{instrumentHeaderId.id}'/>',
							'<s:property value="%{instrumentHeaderId.bankAccountId.id}" />',
							'<s:property value="%{instrumentHeaderId.bankAccountId.bankbranch.id}" />',
							'<s:property value="%{instrumentHeaderId.bankAccountId.bankbranch.bank.id}" />'); ">Download
										text</a>
								</s:if> <!-- <a href="#" onclick="generateReport('xls','<s:property value='%{instrumentHeaderId.id}'/>',
							'<s:property value="%{instrumentHeaderId.bankAccountId.id}" />',
							'<s:property value="%{instrumentHeaderId.bankAccountId.bankbranch.id}" />',
							'<s:property value="%{instrumentHeaderId.bankAccountId.bankbranch.bank.id}" />');">Generate Excel</a> -->
							</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
						<s:set var="PreRtgsNumber"
							value="instVoucherList[0].instrumentHeaderId.transactionNumber" />
					</s:if>

					<tr>

						<td style="text-align: center" class="blueborderfortdnew"><s:property
								value="%{instrumentHeaderId.transactionNumber}" /></td>
						<td style="text-align: center" class="blueborderfortdnew"><s:property
								value="%{voucherHeaderId.voucherNumber}" /></td>
						<td style="text-align: right" class="blueborderfortdnew"><s:property
								value="%{paymentAmount}" /></td>
						<td style="text-align: center" class="blueborderfortdnew"><s:date
								name="%{instrumentHeaderId.transactionDate}" format="dd/MM/yyyy" /></td>
						<td style="text-align: center" class="blueborderfortdnew"><s:property
								value="%{instrumentHeaderId.statusId.description}" /></td>
						<s:set var="PreRtgsNumber"
							value="%{instrumentHeaderId.transactionNumber}" />
					</tr>

				</s:iterator>

			</table>
			<br />
			<s:hidden name="billSubType" id="billSubType" value="%{billSubType}" />
			<div id="buttons" class="buttonbottom">
				<input type="button" value="Close"
					onclick="javascript:window.close()" class="buttonsubmit" />
			</div>
		</div>
		</div>
	</s:form>
	<script>   
function generateReport(type,instrumentnumber,bankaccount,bankbranch,bank){
	if(type=='pdf'){
		 var url="${pageContext.request.contextPath}/report/bankAdviceReport-exportPDF.action?bank.id="+
			bank+"&bankbranch.id="+bankbranch+"&bankaccount.id="+bankaccount+"&instrumentnumber.id="+instrumentnumber;
	}
	else if(type=='xls'){
		 var url="${pageContext.request.contextPath}/report/bankAdviceReport-exportExcel.action?bank.id="+
			bank+"&bankbranch.id="+bankbranch+"&bankaccount.id="+bankaccount+"&instrumentnumber.id="+instrumentnumber;
	}
	
	else{
	 	 var url="${pageContext.request.contextPath}/report/bankAdviceReport-exportText.action?bank.id="+
	 			bank+"&bankbranch.id="+bankbranch+"&bankaccount.id="+bankaccount+"&instrumentnumber.id="+instrumentnumber;
	}
	 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
// <a href="#" onclick="openVoucher('<s:property value='%{#attr.currentRowObject.id}'/>','<s:property value="%{#attr.currentRowObject.vouchernumber}" />','<s:date name="%{#attr.currentRowObject.voucherdate}" format="dd/MM/yyyy"/>');"><s:property value="%{#attr.currentRowObject.vouchernumber}" /> </display:column>
</script>
</body>
</html>
