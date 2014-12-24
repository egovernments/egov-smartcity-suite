<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" id="estimateListTable" name="estimateListTable">
<s:iterator var="e" value="abstractEstimateList" status="s">
	<tr>		
		<td width="3%"  class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="text" name="slNo" id="slNo" size="1" disabled="true" value='<s:property value='%{#s.index+1}' />'/>
		</td>
		<td width="25%" class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
			<s:property value='%{estimateNumber}' /><s:hidden name="estId" id="estId" value="%{id}"/>
			<s:hidden name="isSpillOverWorks" value="%{isSpillOverWorks}" id="isSpillOverWorks"/>			
		</td>
		<td width="35%" class="whiteboxwkwrap" style="WORD-BREAK:BREAK-ALL">
			<s:property value='%{name}' />
		</td>
		<td width="20%" class="whitebox3wk">&nbsp;&nbsp;
			<s:date name="estimateDate" var="estDateFormat" format="dd/MM/yyyy"/>
			<s:property value='%{estDateFormat}' />
		</td>
		<td width="35%" class="whitebox3wk">
			<div align="right">
				<s:property value='%{workValue.formattedString}' />
				<s:hidden name="wvIncldTaxes" id="wvIncldTaxes" value="%{workValue.formattedString}"/>
			</div>
		</td>
		<td align="right" class="headingwk" style="border-left-width: 0px">
       		<a id="delHref" href="#" onclick="deleterow(this)">
       		<img border="0" alt="Delete Estimates" src="${pageContext.request.contextPath}/image/cancel.png" /></a>
       	</td>
	</tr>
</s:iterator>	
<tr><td colspan="5" style="background-color:#F4F4F4;" align="right"><b>Total:</b>&nbsp;
<input type="text" size="8" name="worktotalValue" id="worktotalValue" value="<s:property value="%{worktotalValue.formattedString}" />" 
class="amount" readonly="readonly"/></td>				
<td colspan="5" style="background-color:#F4F4F4;">&nbsp;</td>
</tr>	
</table>