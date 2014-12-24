<%@ include file="/includes/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="coc.title"/></title>
</head>
<body >
<s:form theme="simple" name="bankRemittanceForm" action="bankRemittance">


<table width="100%" cellpadding="0" cellspacing="0" border="0" class="main" align="center">
<tr>
<td class="mainheading" colspan="6" align="center"><s:text name="bankremittance.confirmation"/><br/></td>
</tr>
<tr>
<td>&nbsp;</td>
</tr>
	<td align="center"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
		<tr>
			<th class="bluebgheadtd" width="20%" ><s:text name="bankremittance.vouchernumber"/></th>
			<th class="bluebgheadtd" width="20%" ><s:text name="bankremittance.voucherdate"/></th>
			<th class="bluebgheadtd" width="20%" ><s:text name="bankremittance.vouchertype"/></th>
			<th class="bluebgheadtd" width="20%" ><s:text name="bankremittance.fund"/></th>
			<th class="bluebgheadtd" width="20%" ><s:text name="bankremittance.department"/></th>
		</tr>
		<s:iterator value="%{voucherHeaderValues}"> 
		<tr>
			<td class="blueborderfortd"><div align="center"><s:property value="%{voucherNumber}" /></div></td>
			<td class="blueborderfortd"><div align="center"><s:date name="voucherDate" var="cdFormat" format="dd/MM/yyyy"/><s:property value="%{cdFormat}" /></div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="%{name}" /></div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="%{fundId.name}" /></div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="%{vouchermis.departmentid.deptName}" /></div></td>	
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
