<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>File Upload</title>
</head>
<body onLoad="refreshInbox();">

<s:form theme="simple" name="challan">
	<div class="subheadnew">
	
	 	<s:property value="%{successNo}" /> Receipt Voucher(s) Created Successfully!
	 	<logic:notEmpty name="errorReceiptList">
			<div class="subheadnew">Receipts could not be created for the following Receipts : </div>
		</logic:notEmpty>
	</div>
	<br />
	
	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	<s:iterator value="errorReceiptList">
			<tr>
                        <td class="bluebox2" width="25%"> </td>
			<td class="bluebox2"><div align="right"><s:property />  </div></td>
			<td class="bluebox2"><div align="center"> - </div></td>
			
				 
				<!--  <s:iterator value="value">
				 	<s:property value="value" />
				 </s:iterator> -->
				 
			</tr>	
		</s:iterator> 
	</table>

	<div class="buttonbottom">
	<input name="buttonClose" type="button" class="buttonsubmit"
		id="buttonClose" value="Close" onclick="window.close();" />
	</div>
</s:form>
</body>
</html>
