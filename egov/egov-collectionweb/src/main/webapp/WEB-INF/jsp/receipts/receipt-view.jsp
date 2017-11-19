
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
<title><s:text name="page.title"/></title>
<style type="text/css">
.main{
border:1px solid #666666;
font-family:"Times New Roman", Times, serif;
}

.mainheading{
font-size:20px;
font-weight:bold;
border-bottom:1px solid #666666;
padding:3px;
}

.heading{
font-size:16px;
font-weight:bold;
border-bottom:1px solid #666666;
}

.headingright{
font-size:16px;
font-weight:bold;
padding-left:3px;
border-right:1px solid #666666;
border-bottom:1px solid #666666;
}

.headingleft{
font-size:12px;
font-weight:normal;
padding:5px;
}

.container{
font-size:14px;
border: 1px solid #666666;
}

.content{
border-right:1px solid #666666;
border-bottom:1px solid #666666;
font-size:13px;
padding:2px;
}

.subcontent {
border-top:1px solid #666666;
font-size:13px;
}

.type {
border-right:1px solid #666666;
font-size:13px;
}

.signature{
border-bottom:1px solid #666666;
padding:3px;
font-size:12px;
text-align:left;
font-weight:bold;
}

.signatureright{
border-bottom:1px solid #666666;
padding:3px;
font-size:12px;
text-align:right;
font-weight:bold;
}

.subtable{
padding-left:10px;
}
</style>
</head>
<body >
<s:form theme="simple" name="searchReceiptForm" action="searchReceipt">

<s:iterator value="%{receipts}">
<table cellpadding="0" cellspacing="0" border="0" class="main" align="center">
<tr>
<td class="mainheading" colspan="6" align="center"><s:text name="page.title"/><br/><div style="font-size:12px;padding:0px 5px 5px 5px;">RECEIPT</div></td>
</tr>
<tr>
<td class="headingright" colspan="1" width="15%"><s:text name="viewReceipt.receiptno"/></td>
<td class="headingright" colspan="1" width="43%"><s:property value="receiptnumber" /></td>
<td class="headingright" colspan="2" width="31%"><s:text name="viewReceipt.receiptdate"/><s:date name="receiptDate" format="dd/MM/yyyy"/></td>
</tr>
<tr>
<td class="headingright" colspan="1"><s:text name="viewReceipt.nameaddress"/></td>
<td class="heading" colspan="4"><s:property value="receiptPayeeDetails.payeename" /><br/><s:property value="receiptPayeeDetails.payeeAddress" /></td>
</tr>
<tr>
<td class="headingright" colspan="1" valign="top"><s:text name="viewReceipt.billdetails"/></td>
<td class="heading" colspan="4">
<table align="left" cellpadding="0" cellspacing="0" border="0" width="100%" class="subtable">
<tr><td><s:text name="viewReceipt.billno"/>&nbsp;<s:property value="referencenumber" /></td></tr>
<tr><td><s:text name="viewReceipt.servicename"/>&nbsp;<s:property value="service.serviceName" /></td></tr>
<tr><td><s:text name="viewReceipt.description"/>&nbsp;<s:property value="referenceDesc" /></td></tr>
<tr><td><s:text name="billreceipt.counter.paidby"/>&nbsp;<s:property value="paidBy" /></td></tr>
</table>
</td>
</tr>
<tr>
<td colspan="8">
<table cellspacing="0" cellpadding="0" align="center" width="100%" class="container">
<tbody><tr>
<td align="center" width="30%" class="content"><b><s:text name="viewReceipt.function"/></b></td>
<td align="center" width="25%" class="content"><b><s:text name="viewReceipt.accountcode"/></b></td>
<td align="center" width="30%" class="content"><b><s:text name="viewReceipt.accounthead.desc"/></b></td>
<td width="25%" align="center" class="content"><b><s:text name="viewReceipt.accounthead.amt"/><br/>
  <s:text name="viewReceipt.amt.denom"/></b></td>
</tr>

<s:iterator value="%{receiptDetails}" >
<s:if test="cramount!=0">
<tr>
<td class="content" align="center">&nbsp;<s:property value="%{function.name}" /></td>
<td class="content" align="center">&nbsp;<s:property value="%{accounthead.glcode}" /></td>
<td class="content" align="center">&nbsp;<s:property value="%{accounthead.name}" /></td>
<td class="content" align="right">&nbsp;<s:property value="%{cramount}" /></td>
</tr>
</s:if>
</s:iterator>

<tr>
<td class="content" colspan="4" style="padding:3px 3px 8px 3px;"><br/><b><s:text name="viewReceipt.amtinwords"/></b>
<u> <b><s:property value="amountInWords(amount)" /> </b></u></td>

</tr>

</tbody>
</table>
<table width="100%" class="container" cellpadding="0" cellspacing="0">
<tr>
<td class="signature" colspan="2"><s:text name="viewReceipt.payee.name"/>&nbsp;<s:property value="receiptPayeeDetails.payeename" /></td> 
</tr>
<s:iterator value='%{getInstruments("cash")}' >
<tr>
<td class="signature" colspan="2"><s:text name="viewReceipt.cashReceived"/>&nbsp;<s:property value="%{amount}" /></td>
</tr>
</s:iterator>
<s:iterator value='%{getInstruments("cheque")}' >
<tr>
<td class="signature" colspan="2"><s:text name="viewReceipt.chequedate"/>&nbsp;<s:property value="%{instrumentNumber}" />&nbsp;-&nbsp;<s:date name="%{instrumentDate}" format="dd/MM/yyyy" /></td>
</tr>
</s:iterator>
<s:iterator value='%{getInstruments("dd")}' >
<tr>
<td class="signature" colspan="2"><s:text name="viewReceipt.dddate"/>&nbsp;<s:property value="%{instrumentNumber}" />&nbsp;-&nbsp;<s:date name="%{instrumentDate}" format="dd/MM/yyyy" /></td>
</tr>
</s:iterator>
<s:iterator value='%{getInstruments("card")}' >
<tr>
<td class="signature" colspan="2"><s:text name="viewReceipt.creditcardno"/>&nbsp;<s:property value="%{instrumentNumber}" /></td>
</tr>
</s:iterator>
<tr>
<td class="signature" colspan="2"><s:text name="viewReceipt.comment"/>&nbsp;</td>
</tr>
<tr>
<td class="signatureright" colspan="1" width="80%"><s:text name="viewReceipt.officer"/></td>
<td class="signatureright" colspan="1">&nbsp;</td>
</tr>

</tbody></table>
</td>
</tr>
</table>
<br/>
</s:iterator>
</s:form>
</body>
</html>
