<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<table width="100%" border="0" cellspacing="0" cellpadding="0" id="estimateListTable" name="estimateListTable">
<s:iterator var="e" value="abstractEstimateList" status="s">
	<tr>
		<input type="hidden" name="estId" id="estId" value="<s:property value='%{id}'/>" />
		<input type="hidden" name="wvIncldTaxes" id="wvIncldTaxes" value="<s:property value='%{workValueIncludingTaxes.formattedString}'/>" />
		<td width="5%"  class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="text" name="slNo" id="slNo" size="1" disabled="true" value='<s:property value='%{#s.index+1}' />'/>
		</td>
		<td width="15%" class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
			<s:property value='%{estimateNumber}' />
		</td>
		<td width="40%" class="whiteboxwkwrap" style="WORD-BREAK:BREAK-ALL">
			<s:property value='%{name}' />
		</td>
		<td width="10%" class="whitebox3wk">&nbsp;&nbsp;
			<s:date name="estimateDate" format="dd/MM/yyyy" /> 
		</td>
		<td width="15%" class="whitebox3wk">
			<div align="right">
				<s:property value='%{workValueIncludingTaxes.formattedString}' />
			</div>
		</td>
		<td align="right" width="10%" class="headingwk" style="border-left-width: 0px">
       		<a id="delHref" href="#" onclick="deleterow(this)">
       		<img border="0" alt="Delete Estimates" src="/egworks/resources/erp2/images/cancel.png" /></a>
       	</td>
	</tr>
</s:iterator>	
<tr><td colspan="5" style="background-color:#F4F4F4;" align="right"><b>Total:</b>&nbsp;
<input type="text" size="8" name="totalAmount" id="totalAmount" value="<s:property value="%{worktotalValue.formattedString}" />" 
class="amount" readonly="readonly"/></td>				
<td colspan="5" style="background-color:#F4F4F4;">&nbsp;</td>
</tr>	
</table>
