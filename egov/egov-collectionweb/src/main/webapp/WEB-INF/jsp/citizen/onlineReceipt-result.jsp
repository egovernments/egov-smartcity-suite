<%@ include file="/includes/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="coc.title"/></title>
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
<s:else>
	<table cellpadding="0" cellspacing="0" border="0" class="main" align="center">
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
	
	<tr>Your payment of Amount Rs.  <s:property value="%{onlinePaymentReceiptHeader.totalAmount}" /> for Property tax has been received. The Reference Number is <a href='${pageContext.request.contextPath}/citizen/onlineReceipt!view.action?receiptId=<s:property value='%{onlinePaymentReceiptHeader.id}'/>'> <s:property value="%{onlinePaymentReceiptHeader.referencenumber}" /></a>. Please click  to generate and print the receipt</tr>
	
	</table>
	
</s:else>

<br/>
<div class="buttonbottom">
<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close"/>
</div>
</body>
</html>
