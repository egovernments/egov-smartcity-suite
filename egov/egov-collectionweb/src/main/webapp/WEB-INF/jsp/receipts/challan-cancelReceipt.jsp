
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

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="cancelreceipt.title"/></title>
<script>
function warningInfo()
{
	dom.get("cancellationreasonerror").style.display="none";
	if(trimAll(document.getElementById("reasonForCancellation").value).length==0 || trimAll(document.getElementById("reasonForCancellation").value)=="")
	{
		dom.get("cancellationreasonerror").style.display="block";
		return false;
	}
	else
	{
		document.cancelChallanReceiptForm.action="challan-saveOnCancel.action";
		document.cancelChallanReceiptForm.submit();
	}
}

</script>
</head>
<body >
<span align="center" style="display:none" id="cancellationreasonerror">
  <li>
     <font size="2" color="red"><b><s:text name="cancellationreason.error"/></b></font>
  </li>
</span>
<s:form theme="simple" name="cancelChallanReceiptForm" action="challan">
<s:push value="model">
<div class="formmainbox">
	<div class="subheadnew"><s:text name="cancelreceipt.title"/></div>
	
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<s:hidden label="receiptId" id="receiptId" name="receiptId" value="%{model.id}"/>
			<tr>
				<td width="4%" class="bluebox2">&nbsp;</td>
				<td width="21%" class="bluebox2"><s:text name="viewReceipt.receiptno"/></td>
				<td width="24%" class="bluebox2"><b><s:property value="receiptnumber" /></b></td>
				<td width="21%" class="bluebox2"><s:text name="viewReceipt.receiptdate"/></td>
				<td width="30%" class="bluebox2"><b><s:date name="receiptDate" format="dd/MM/yyyy"/></b></td>
			</tr>
			<tr><td colspan="5">
			<%@ include file='challandetails.jsp'%>
			</td></tr>
			<tr>
					<table cellspacing="0" cellpadding="0" align="center" width="100%" class="tablebottom">
						<tr>
							<td width="4%" class="bluebox2">&nbsp;</td>
							<td width="21%" class="bluebox2"><b><s:text name="viewReceipt.amtinwords"/></b></td>
							<td width="75%" class="bluebox2" colspan="7"><u><b><s:property value="amountInWords(totalAmount)" /> </b></u></td>
						</tr>
						<tr>
							<td width="4%" class="bluebox">&nbsp;</td>
							<td width="21%" class="bluebox"><s:text name="viewReceipt.payee.name"/></td>
							<td width="75%" class="bluebox" colspan="7"><s:property value="receiptPayeeDetails.payeename" /></td>
						</tr>

						<s:iterator value='%{getInstruments("cash")}' >

						<tr>
							<td width="4%" class="bluebox2">&nbsp;</td>
							<td width="21%" class="bluebox2"><s:text name="viewReceipt.cashReceived"/></td>
							<fmt:formatNumber var="totalRecievedAmount" value='${amount}' pattern='#0.00' />
							<td width="75%" class="bluebox2" colspan="7">${totalRecievedAmount}</td>
						</tr>

						</s:iterator>

						<s:iterator value='%{getInstruments("cheque")}' >
						<tr>
							<td width="4%" class="bluebox2">&nbsp;</td>
							<td width="15%" class="bluebox2"><s:text name="viewReceipt.chequedate"/></td>
							<td width="21%" class="bluebox2"><s:property value="%{instrumentNumber}"/>&nbsp;-&nbsp;<s:date name="%{instrumentDate}" format="dd/MM/yyyy" /></td>
							<td width="10%" class="bluebox2"><s:text name="viewReceipt.chequeamount"/></td>
							<fmt:formatNumber var="chequeAmount" value='${instrumentAmount}' pattern='#0.00' />
							<td width="15%" class="bluebox2">${chequeAmount}</td>
							<td width="10%" class="bluebox2"><s:text name="viewReceipt.bankbranch"/></td>
							<td width="25%" class="bluebox2"><s:property value="%{bankId.name}"/>&nbsp;-&nbsp;<s:property value="%{bankBranchName}" /></td>
						</tr>
						</s:iterator>

						<s:iterator value='%{getInstruments("dd")}' >
						<tr>
							<td width="4%" class="bluebox2">&nbsp;</td>
							<td width="21%" class="bluebox2"><s:text name="viewReceipt.dddate"/></td>
							<td width="15%" class="bluebox2"><s:property value="%{instrumentNumber}"/>&nbsp;-&nbsp;<s:date name="%{instrumentDate}" format="dd/MM/yyyy" /></td>
							<td width="10%" class="bluebox2"><s:text name="viewReceipt.ddamount"/></td>
							<fmt:formatNumber var="chequeAmount" value='${instrumentAmount}' pattern='#0.00' />
							<td width="15%" class="bluebox2">${chequeAmount}</td>
							<td width="10%" class="bluebox2"><s:text name="viewReceipt.bankbranch"/></td>
							<td width="25%" class="bluebox2"><s:property value="%{bankId.name}"/>
							<s:if test="bankBranchName!=null">
							&nbsp;-&nbsp;<s:property value="%{bankBranchName}" />
							</s:if>
							</td>
						</tr>
						
						</s:iterator>

						<s:iterator value='%{getInstruments("card")}' >
						<tr>
							<td width="4%" class="bluebox2">&nbsp;</td>
							<td width="21%" class="bluebox2"><s:text name="viewReceipt.creditcardno"/></td>
							<td width="24%" class="bluebox2" colspan="3"><s:property value="%{instrumentNumber}" /></td>
						</tr>
						</s:iterator>

						
						<tr>
							<td width="4%" class="bluebox2">&nbsp;</td>
							<td width="21%" class="bluebox2"><s:text name="billreceipt.reasonforcancellation"/><span class="mandatory"></span></td>
							<td width="24%" class="bluebox2" colspan="7"><s:textarea id="reasonForCancellation" label="reasonForCancellation" cols="90" rows="8" name="reasonForCancellation" value="%{reasonForCancellation}" /></td>
						</tr>
					</table>
			</tr>	
			
			
		</table>

		<br/>
		<div align="left" class="mandatorycoll"><s:text name="common.mandatoryfields"/></div>
		<div class="buttonbottom">
		<input name="button32" type="button" class="buttonsubmit" id="button32"  value="Cancel Receipt" onclick="return warningInfo();"/>
		<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close"/>
		<input name="buttonBack" type="button" class="button" id="buttonBack" value="Back" onclick="window.location='${pageContext.request.contextPath}/receipts/searchReceipt.action';"  /> 
		</div>
</div>
</s:push>
</s:form>
</body>
</html>
