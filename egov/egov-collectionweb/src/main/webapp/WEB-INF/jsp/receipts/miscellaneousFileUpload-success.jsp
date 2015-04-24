<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Miscellaneous Receipts File Upload</title>
</head>
<body >

<s:form theme="simple" name="challan">
	<div class="subheadnew">
	 <s:property value="%{successNo}" /> Miscellaneous Records Created and Uploaded Successfully!
	</div>
	<br />
	
	<logic:notEmpty name="errorRowMap">
		<div class="subheadnew">The following rows could not be uploaded.</div>
	</logic:notEmpty>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	<s:iterator value="errorRowMap">
			<tr>
                        <td class="bluebox2" width="25%"> </td>
			<td class="bluebox2"><div align="right">Unable to load Row <s:property value="key" /></div></td>
			<td class="bluebox2"><div align="center"> - </div></td>
			<td class="bluebox2"><div align="left"><s:property value="value" /></div></td>
				 
				<!--  <s:iterator value="value">
				 	<s:property value="value" />
				 </s:iterator> -->
				 
			</tr>	
		</s:iterator> 
	</table>
	
	<div class="buttonbottom">
	<input name="buttonClose" type="button" class="buttonsubmit"
		id="buttonClose" value="Close" onclick="window.close()" />
	</div>
</s:form>
</body>
</html>
