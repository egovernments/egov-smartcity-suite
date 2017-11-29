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
<title><s:text name="onlineReceipts.title"/></title>
<script type="text/javascript">
function onBodyLoad(){
	
}

</script>
</head>
<body>
<s:if test="%{hasErrors()}">
    <div class="errorstyle">
      <s:actionerror/>
      <s:fielderror/>
    </div>
</s:if>
<div class="text-center">
<s:else>
	<!-- <tr>RECEIVED SUCCESS RESPONSE FROM PAYMENT GATEWAY</tr>
	<tr>
			<td>Bill Number : <s:property value="%{onlinePaymentReceiptHeader.referencenumber}" /> </td>
	</tr>
	<tr>
			<td>Receipt Number : <s:property value="%{onlinePaymentReceiptHeader.receiptnumber}" /> </td>
	</tr>
	<tr>
			<td>Transaction Amount : <s:property value="%{onlinePaymentReceiptHeader.onlinePayment.transactionAmount}" /></td>
	</tr> 
	<tr>
			<td>Transaction Number : <s:property value="%{onlinePaymentReceiptHeader.onlinePayment.transactionNumber}" /></td>
	</tr> -->
	
	<div id="paymentInfo" style="text-align: center;padding-bottom: 15px;">Your payment of Amount &#8377; <s:property value="%{onlinePaymentReceiptHeader.totalAmount}" /> has been received. The Reference Number is <s:property value="%{onlinePaymentReceiptHeader.referencenumber}" />. Please click on <span>Generate Receipt to print the receipt</span></div>
    <a href='${pageContext.request.contextPath}/citizen/onlineReceipt-view.action?receiptId=<s:property value='%{onlinePaymentReceiptHeader.id}'/>' class="btn btn-primary" id="btnGenerateReceipt">Generate Receipt</a>&nbsp;
</s:else>
</div>

<script>

if(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
	if(CitizenApp)
	{
		jQuery('#btnGenerateReceipt').text('Download Receipt');
		jQuery('#btnGenerateReceipt').attr('href','javascript:void(0);');
		jQuery('#paymentInfo').find('span').html('Download Receipt');
		CitizenApp.showSnackBar('Your payment of Amount Rs.<s:property value="%{onlinePaymentReceiptHeader.totalAmount}" /> has been received. The Reference Number is <s:property value="%{onlinePaymentReceiptHeader.referencenumber}" />.');
		jQuery('#btnGenerateReceipt').click(function(e){
			CitizenApp.downloadReceipt('<s:property value="%{onlinePaymentReceiptHeader.receiptnumber}" />', '<s:property value="%{onlinePaymentReceiptHeader.consumerCode}" />');
		});
	}
}

</script>

</body>
</html>
