<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" id="indentListTable" name="indentListTable">
<s:iterator var="e" value="indentList" status="s">
	<tr>
		<td width="3%"  class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="text" name="slNo" id="slNo" size="1" disabled="true" value='<s:property value='%{#s.index+1}' />'/>
		</td>
		<td width="30%" class="whitebox3wk">&nbsp;&nbsp;
			<s:date name="indentDate" var="indentDateFormat" format="dd/MM/yyyy"/>
			<s:property value='%{indentDateFormat}' />
		</td>
		<td width="50%" class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
			<s:property value='%{indentNumber}' /><s:hidden name="indntId" id="indntId" value="%{id}"/>
		</td>
		
		<td align="right" width="10%"  class="headingwk" style="border-left-width: 0px">
       		<a id="delHref" href="#" onclick="deleteIndentRow(this)">
       		<img border="0" alt="Delete Indents" src="${pageContext.request.contextPath}/image/cancel.png" /></a>
       	</td>
	</tr>
</s:iterator>	
</table>