
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="cancelreceipt.title"/></title>
<script>

jQuery.noConflict();
jQuery(document).ready(function() {
  	 
     jQuery(" form ").submit(function( event ) {
    	 doLoadingMask();
    });
     doLoadingMask();
 });

jQuery(window).load(function () {
	undoLoadingMask();
});

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
		document.searchReceiptForm.action="receipt-saveOnCancel.action";
		document.searchReceiptForm.submit();
	}

	doLoadingMask('#loadingMask');
}

</script>
</head>

<body >
<span align="center" style="display:none" id="cancellationreasonerror">
  <li>
     <font size="2" color="red"><b><s:text name="cancellationreason.error"/></b></font>
  </li>
</span>
<s:form theme="simple" name="searchReceiptForm" action="searchReceipt">
<div class="formmainbox">
	<div class="subheadnew"><s:text name="cancelreceipt.title"/></div>
	<s:iterator value="%{receipts}">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<s:hidden label="oldReceiptId" id="oldReceiptId" name="oldReceiptId" value="%{id}"/>
			<s:hidden label="cancelreceiptno" id="cancelreceiptno" name="cancelreceiptno" value="%{receiptnumber}"/>
			<%-- <s:hidden label="payeeDetailsId" id="payeeDetailsId" name="payeeDetailsId" value="%{receiptPayeeDetails.id}"/> --%>
			
			<tr>
				<td width="4%" class="bluebox2">&nbsp;</td>
				<td width="21%" class="bluebox2"><s:text name="viewReceipt.receiptno"/></td>
				<td width="24%" class="bluebox2"><b><s:property value="receiptnumber" /></b></td>
				<td width="21%" class="bluebox2"><s:text name="viewReceipt.receiptdate"/></td>
				<td width="30%" class="bluebox2"><b> 
				<fmt:formatDate value="${receiptDate}" var="receiptDate" pattern="dd-MM-yyyy"/>
				<c:out value="${receiptDate}"></c:out></b></td>
			</tr>
			<tr>
				<td width="4%" class="bluebox">&nbsp;</td>
				<td width="21%" class="bluebox"><s:text name="viewReceipt.nameaddress"/></td>
				<td width="75%" class="bluebox" colspan="3"><s:property value="payeeName" /><br/><s:property value="payeeName" /></td>
			</tr>
			<tr>
				<td width="4%" class="bluebox2">&nbsp;</td>
				<td width="21%" class="bluebox2"><s:text name="viewReceipt.billdetails"/></td>
				<td width="75%" class="bluebox2" colspan="3">
					<table align="left" cellpadding="0" cellspacing="0" border="0" width="100%" class="subtable">
					<s:if test="referencenumber!=null">
						<tr><td><s:text name="viewReceipt.billno"/>&nbsp;<s:property value="referencenumber" /></td></tr>
					</s:if>
					<s:if test="receiptMisc.fund!=null">
						<tr><td><s:text name="viewReceipt.fund"/>&nbsp;<s:property value="receiptMisc.fund.name" /></td></tr>
					</s:if>
					<s:if test="receiptMisc.department!=null">
						<tr><td><s:text name="viewReceipt.department"/>&nbsp;<s:property value="receiptMisc.department.name" /></td></tr>
					</s:if>
					<s:if test="receiptMisc.fundsource!=null">
						<tr><td><s:text name="viewReceipt.fundsource"/>&nbsp;<s:property value="receiptMisc.fundsource.name" /></td></tr>
					</s:if>
					<s:if test="receiptMisc.functionary!=null">
						<tr><td><s:text name="viewReceipt.functionary"/>&nbsp;<s:property value="receiptMisc.functionary.name" /></td></tr>
					</s:if>
					<s:if test="receiptMisc.scheme!=null">
						<tr><td><s:text name="viewReceipt.scheme"/>&nbsp;<s:property value="receiptMisc.scheme.name" /></td></tr>
					</s:if>
					<s:if test="receiptMisc.subscheme!=null">
						<tr><td><s:text name="viewReceipt.subscheme"/>&nbsp;<s:property value="receiptMisc.subscheme.name" /></td></tr>
					</s:if>
						<tr><td><s:text name="viewReceipt.servicename"/>&nbsp;<s:property value="service.name" /></td></tr>
					<s:if test="referenceDesc!=null">
						<tr><td><s:text name="viewReceipt.description"/>&nbsp;<s:property value="referenceDesc" /></td></tr>
					</s:if>
						<tr><td><s:text name="billreceipt.counter.paidby"/>&nbsp;<s:property value="paidBy" /></td></tr>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="5">
					<table cellspacing="0" cellpadding="0" align="center" width="100%" class="tablebottom no-border" style="empty-cells:show;">
						<tr><td>
						<div class="subheadsmallnew"><span class="subheadnew"><s:text name="billreceipt.billdetails.Credit"/></span></div>
						</td></tr>
						
						<tr>
						 <td>
						 <table width="100%" class="tablebottom">
						 <tr>
							<th class="bluebgheadtd" width="30%"><s:text name="viewReceipt.function"/></th>
							<th class="bluebgheadtd" width="25%"><s:text name="viewReceipt.accountcode"/></th>
							<th class="bluebgheadtd" width="30%"><s:text name="viewReceipt.accounthead.desc"/></th>
							<th class="bluebgheadtd" width="25%"><s:text name="viewReceipt.accounthead.amt"/>&nbsp;(<s:text name="viewReceipt.amt.denom"/>)</th>
						</tr>
<%! int rebateaccountavalailable=0;%>
						<s:iterator value="%{receiptDetails}" >

						<s:if test="cramount!=0">
						<tr>
							<td class="blueborderfortd"><div align="center"><s:property value="%{function.name}" /></div></td>
							<td class="blueborderfortd"><div align="center"><s:property value="%{accounthead.glcode}" /></div></td>
							<td class="blueborderfortd"><div align="center"><s:property value="%{description}" /></div></td>						<fmt:formatNumber var="newcramount" value='${cramount}' pattern='#0.00' />
							<td class="blueborderfortd"><div align="center">${newcramount}</div></td>
						</tr>
						</s:if>
						<s:if test="dramount!=0 && !isRevenueAccountHead(accounthead)">
						<% rebateaccountavalailable=1; %>
						</s:if>
						
						</s:iterator>
						</table>
						</td>
						</tr>
						
						<%if(rebateaccountavalailable==1){ %>
						<tr><td>
						<div class="subheadsmallnew"><span class="subheadnew"><s:text name="billreceipt.billdetails.Rebate"/></span></div>
						</td></tr>
						<tr><td>
						<table cellspacing="0" cellpadding="0" align="center" width="100%" class="tablebottom" style="empty-cells:show;">
						<tr>
							<th class="bluebgheadtd" width="30%"><s:text name="viewReceipt.function"/></th>
							<th class="bluebgheadtd" width="25%"><s:text name="viewReceipt.accountcode"/></th>
							<th class="bluebgheadtd" width="30%"><s:text name="viewReceipt.accounthead.desc"/></th>
							<th class="bluebgheadtd" width="25%"><s:text name="viewReceipt.accounthead.amt"/>&nbsp;(<s:text name="viewReceipt.amt.denom"/>)</th>
						</tr>
						<%} %>
						<s:iterator value="%{receiptDetails}" >
						<s:if test="dramount!=0 && !isRevenueAccountHead(accounthead)">
						<tr>
							<td class="blueborderfortd"><div align="center"><s:property value="%{function.name}" /></div></td>
							<td class="blueborderfortd"><div align="center"><s:property value="%{accounthead.glcode}" /></div></td>
							<td class="blueborderfortd"><div align="center"><s:property value="%{description}" /></div></td>						<fmt:formatNumber var="newdramount" value='${dramount}' pattern='#0.00' />
							<td class="blueborderfortd"><div align="center">${newdramount}</div></td>
						</tr>
						
						
						</s:if>
						</s:iterator>
						<tr>
						    <td colspan="2" class="blueborderfortd">&nbsp;</td>
							<td class="blueborderfortd"><div align="right"><b>Total&nbsp;&nbsp;</b></div></td>							<fmt:formatNumber var="newtotalamount" value='${amount}' pattern='#0.00' />	
							<td class="blueborderfortd"><div align="center"><b>${newtotalamount}</b></div></td>			
						</tr>
						<%if(rebateaccountavalailable==1){ %>
						</table>
						</td></tr>
						<%} %>
							
					</table>
					
				</td>
			</tr>
			<tr>
					<table cellspacing="0" cellpadding="0" align="center" width="100%" class="tablebottom">
						<tr>
							<td width="4%" class="bluebox2">&nbsp;</td>
							<td width="21%" class="bluebox2"><b><s:text name="viewReceipt.amtinwords"/></b></td>
							<td width="75%" class="bluebox2" colspan="7"><u><b><s:property value="amountInWords(amount)" /> </b></u></td>
						</tr>
						<tr>
							<td width="4%" class="bluebox">&nbsp;</td>
							<td width="21%" class="bluebox"><s:text name="viewReceipt.payee.name"/></td>
							<td width="75%" class="bluebox" colspan="7"><s:property value="payeeName" /></td>
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
							<td width="21%" class="bluebox2"><s:text name="billreceipt.reasonforcancellation"/><span class="mandatory1">*</span></td>
							<td width="24%" class="bluebox2" colspan="7"><s:textarea id="reasonForCancellation" label="reasonforcancellation" cols="90" rows="8" name="reasonForCancellation"/></td>
						</tr>
					</table>
			</tr>	
			
			
		</table>

		   <div id="loadingMask" style="display:none;overflow:hidden;text-align: center"><img src="/collection/resources/images/bar_loader.gif"/> <span style="color: red">Please wait....</span></div>
		   
		<div align="left" class="mandatorycoll"><s:text name="common.mandatoryfields"/></div>
		<div class="buttonbottom">
		<s:if test="isReceiptCancelEnable">
		<input name="button32" type="button" class="buttonsubmit" id="button32"  value="Cancel Receipt" onclick="return warningInfo()"/>
		</s:if>
		<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close"/>
		<input name="buttonBack" type="button" class="button" id="buttonBack" value="Back" onclick="window.location='${pageContext.request.contextPath}/receipts/searchReceipt.action';"  /> 
		</div>
	</s:iterator>
</div>
</s:form>
</body>
</html>

