<%@ include file="/includes/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="coc.title"/></title>
</head>
<body >
<s:form theme="simple" name="searchReceiptForm" action="searchReceipt">


<table width="100%" cellpadding="0" cellspacing="0" border="0" class="main" align="center">
<tr>
<td class="mainheading" colspan="6" align="center"><s:text name="coc.title"/><br/></td>
</tr>
<tr>
<td>&nbsp;</td>
</tr>
	<td align="center"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
		<tr align="center">
			<font size="2" color="red"><b>
			<s:if test="target=='view'">
				<div align="center"><s:text name="billreceipt.payement.confirmatiommessage"/></div>
			</s:if>		
			<s:else >
				<div align="center"><s:text name="billreceipt.cancel.confirmatiommessage"/></div>
			</s:else>
			</b></font>
		</tr>
		<s:iterator value="%{receiptHeaderValues}"> 
		<tr>
			<s:if test="%{receipttype=='B'}">
			<th class="bluebgheadtd" width="20%" ><s:text name="billreceipt.billnumber.confirmation"/></th>
			</s:if>
			<s:if test="%{receipttype=='C'}">
			<th class="bluebgheadtd" width="20%" ><s:text name="billreceipt.challannumber.confirmation"/></th>
			</s:if>
			
			<th class="bluebgheadtd" width="30%" ><s:text name="billreceipt.receiptnumber.confirmation"/></th>
			<th class="bluebgheadtd" width="25%" ><s:text name="billreceipt.receiptdate.confirmation"/></th>
			<th class="bluebgheadtd" width="25%" ><s:text name="billreceipt.receiptstatus.confirmation"/></th>
		</tr>
		<tr>
			<s:if test="%{receipttype=='B'}">
			<td class="blueborderfortd"><div align="center"><s:property value="%{referencenumber}" /></div></td>
			</s:if>
			<s:if test="%{receipttype=='C'}">
			<td class="blueborderfortd"><div align="center"><s:property value="%{challan.challanNumber}" /></div></td>
			</s:if>
			<td class="blueborderfortd"><div align="center"><s:property value="%{receiptnumber}" /></div></td>
			<td class="blueborderfortd"><div align="center"><s:date name="createdDate" var="cdFormat" format="dd/MM/yyyy"/><s:property value="%{cdFormat}" /></div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="%{status.description}" /></div></td>
		</tr>
		</s:iterator>
	</table></td>
</table>
<br/>
<div class="buttonbottom">
<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close"/>
</div>
</s:form>
</body>
</html>
