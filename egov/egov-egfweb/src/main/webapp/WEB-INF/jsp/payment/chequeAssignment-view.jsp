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
			<jsp:param name="heading" value="Cheque Assignment View" />
		</jsp:include>
		<span class="mandatory1"> <s:actionerror /> <s:fielderror />
			<s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="chq.assignment.heading.view" />
			</div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<s:if test="%{paymentMode=='cheque' || paymentMode=='cash'}">
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.partycode" /></th>

						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.serialno" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.no" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.amount" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.date" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.status" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="instrument.bankadvice" /></th>
						<s:if
							test="%{chequePrintingEnabled && chequePrintAvailableAt=='assignment'}">
							<th class="bluebgheadtdnew"></th>
						</s:if>

					</s:if>
					<s:else>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.paymentvoucherno" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.rtgs.refno" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.rtgs.amount" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.rtgs.date" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.status" /></th>
					</s:else>

				</tr>
				<s:if test="%{paymentMode=='cheque'|| paymentMode=='cash'}">
					<s:iterator var="p" value="instHeaderList" status="s">
						<tr>
							<td style="text-align: center" class="blueborderfortdnew"><s:property
									value="%{payTo}" /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:property
									value="%{serialNo.finYearRange}" /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:property
									value="%{instrumentNumber}" /></td>
							<td style="text-align: right" class="blueborderfortdnew"><s:text
									name="format.number">
									<s:param value="%{instrumentAmount}" />
								</s:text></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:date
									name="%{instrumentDate}" format="dd/MM/yyyy" /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:property
									value="%{statusId.description}" /></td>
							<td class="blueborderfortd"><div align="center">
									<a
										href='/EGF/payment/chequeAssignment-bankAdviceExcel.action?instHeaderId=<s:property value="id"/>'>
										<s:text name="instrument.bankadvice" />
									</a>
								</div></td>
							<s:if
								test="%{chequePrintingEnabled && chequePrintAvailableAt=='assignment'}">
								<td style="text-align: center" class="blueborderfortdnew">
									<input type="submit" value="Print"
									onclick="return printCheque(<s:property
						value="%{id}" />);"
									class="button" />
								</td>
							</s:if>
							<input type="hidden" name='chequeFormatId' id="chequeFormatId"
								value="<s:property value="chequeFormat"/>" />

						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<s:iterator var="p" value="instVoucherList" status="s">
						<tr>
							<td style="text-align: center" class="blueborderfortdnew"><s:property
									value="%{voucherHeaderId.voucherNumber}" /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:property
									value="%{instrumentHeaderId.transactionNumber}" /></td>
							<td style="text-align: right" class="blueborderfortdnew"><s:property
									value="%{instrumentHeaderId.instrumentAmount}" /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:date
									name="%{instrumentHeaderId.transactionDate}"
									format="dd/MM/yyyy" /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:property
									value="%{instrumentHeaderId.statusId.description}" /></td>

						</tr>
					</s:iterator>
					<input type="hidden" name='transactionNumber'
						id="transactionNumber"
						value="<s:property value="instVoucherList[0].instrumentHeaderId.id"/>" />
					<input type="hidden" name='bankAccountNoId' id="bankAccountNoId"
						value="<s:property value="instVoucherList[0].instrumentHeaderId.bankAccountId.id"/>" />
					<input type="hidden" name='bankBranchId' id="bankBranchId"
						value="<s:property value="instVoucherList[0].instrumentHeaderId.bankAccountId.bankbranch.id"/>" />
					<input type="hidden" name='bank' id="bank"
						value="<s:property value="instVoucherList[0].instrumentHeaderId.bankAccountId.bankbranch.bank.id"/>" />
					<input type="hidden" name='chequeFormatId' id="chequeFormatId"
						value="<s:property value="instVoucherList[0].instrumentHeaderId.bankAccountId.chequeformat"/>" />

				</s:else>
			</table>
			<br />
			<div class="buttonbottom">

				<s:if test="%{paymentMode=='rtgs'}">
					<input type="button" value="Generate BankAdvice Excel"
						class="buttonsubmit" onclick="return printAdviceExcel()" />
					<input type="button" value="Generate BankAdvice Pdf"
						class="buttonsubmit" onclick="return printAdvicePdf()" />
				</s:if>
				<input type="button" value="Close"
					onclick="javascript:window.close()" class="buttonsubmit" />
			</div>
		</div>
	</s:form>
	<script>      
function printAdviceExcel(){
	 	 var bank=document.getElementById("bank").value;
	 	var bankbranch=document.getElementById("bankBranchId").value;
	 	var bankaccount=document.getElementById("bankAccountNoId").value;
	 	 var instrumentnumber=document.getElementById("transactionNumber").value;
	 	// bootbox.alert("bank>>>>"+bank);   
	 	// bootbox.alert("bankbranch>>>>"+bankbranch);   
	 	// bootbox.alert("bankaccount>>>>"+bankaccount);   
	 	// bootbox.alert("instrumentnumber>>>>"+instrumentnumber);                                        
		 var url="${pageContext.request.contextPath}/report/bankAdviceReport!exportExcel.action?bank.id="+
		 			bank+"&bankbranch.id="+bankbranch+"&bankaccount.id="+bankaccount+"&instrumentnumber.id="+instrumentnumber;
		 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
function printAdvicePdf(){
	 var bank=document.getElementById("bank").value;
	var bankbranch=document.getElementById("bankBranchId").value;
	var bankaccount=document.getElementById("bankAccountNoId").value;
	 var instrumentnumber=document.getElementById("transactionNumber").value;
	// bootbox.alert("bank>>>>"+bank);   
	// bootbox.alert("bankbranch>>>>"+bankbranch);   
	// bootbox.alert("bankaccount>>>>"+bankaccount);   
	// bootbox.alert("instrumentnumber>>>>"+instrumentnumber);                                        
	 var url="${pageContext.request.contextPath}/report/bankAdviceReport!exportPDF.action?bank.id="+
	 			bank+"&bankbranch.id="+bankbranch+"&bankaccount.id="+bankaccount+"&instrumentnumber.id="+instrumentnumber;
	 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function printCheque(id)
{
 	var chequeFormat=document.getElementById("chequeFormatId");
	if(chequeFormat == "" || chequeFormat == null){
		bootbox.alert("This bank account is not attached to any cheque formats");
		return false;
	} 
	window.open('/EGF/payment/chequeAssignmentPrint-generateChequeFormat.action?instrumentHeader='+id,'Search','resizable=yes,scrollbars=yes,left=300,top=40,width=900, height=700');
    return false;
}
</script>
</body>
</html>