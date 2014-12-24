<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<div style="overflow-x:scroll; overflow-y:scroll;">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
<td>
      <div align="center"><br/>
        <table border="0" cellspacing="0" cellpadding="0" class="tablebottom" width="100%">
          <tr>
            <td colspan="6">
			<div class="subheadsmallnew"><strong><s:property value="%{functionwiseIE.cityName}"/></strong></div></td>
          </tr>
          <tr>
            <td colspan="6">
			<div class="subheadsmallnew"><strong>FUNCTIONWISE <s:if test='%{functionwiseIE.incExp == "I"}'>INCOME</s:if><s:else>EXPENSE</s:else> SUBSIDARY REGISTER</strong></div></td>
          </tr>
          <tr>
		    <th class="bluebgheadtd">Sl.No.</th>
            <th class="bluebgheadtd">Function Code</th>
			<th class="bluebgheadtd">Function Head</th>
			<th class="bluebgheadtd">Total <s:if test='%{functionwiseIE.incExp == "I"}'>Income</s:if><s:else>Expense</s:else> (Rs.)</th>
			<s:iterator value="functionwiseIE.majorCodeList" status="stat" var="p">
            	<th class="bluebgheadtd"><s:property value="p"/> </th>
			</s:iterator>
          </tr>
		<s:iterator value="functionwiseIE.entries" status="stat">
          <tr>
            <td class="blueborderfortd">
				<div align="center"><s:property value="slNo"/></div>		
			</td>
            <td class="blueborderfortd">
				<div align="left"><s:property value="functionCode"/></div>		
			</td>
            <td class="blueborderfortd">
				<div align="left"><s:if test='%{slNo == ""}'><strong><s:property value="functionName"/></strong></s:if><s:else><s:property value="functionName"/></s:else></div>		
			</td>
			<td class="blueborderfortd">
				<div align="right"><s:if test='%{slNo == ""}'><strong><s:property value="totalIncome"/></strong></s:if><s:else><s:property value="totalIncome"/></s:else></div>		
			</td>
			<s:iterator value="functionwiseIE.majorCodeList" var="k" status="s">
				<td class="blueborderfortd">
	            	<div align="right"><s:if test='%{slNo == ""}'><strong><s:property value="majorcodeWiseAmount[#k]"/></strong></s:if><s:else><s:property value="majorcodeWiseAmount[#k]"/></s:else></div>	
				</td>
			</s:iterator>
          </tr>
		</s:iterator>
        </table>
</div>
</td>
</tr>
</table>
<jsp:include page="report-filterhidden.jsp"/>
<input type="hidden" name="model.incExp" value='<s:property value="model.incExp"/>'/>
<div class="buttonbottom"><s:text name="report.export.options"/>: <a href='/EGF/report/functionwiseIE!generateFunctionwiseIEXls.action?model.incExp=<s:property value="model.incExp"/>&model.fund.id=<s:property value="model.fund.id"/>&model.department.id=<s:property value="model.department.id"/>&model.function.id=<s:property value="model.function.id"/>&model.functionary.id=<s:property value="model.functionary.id"/>&model.scheme.id=<s:property value="model.scheme.id"/>&model.subScheme.id=<s:property value="model.subScheme.id"/>&model.fundsource.id=<s:property value="model.fundsource.id"/>&model.field.id=<s:property value="model.field.id"/>&model.startDate=<s:property value="model.startDate"/>&model.endDate=<s:property value="model.endDate"/>'>Excel</a> | <a href='/EGF/report/functionwiseIE!generateFunctionwiseIEPdf.action?model.incExp=<s:property value="model.incExp"/>&model.fund.id=<s:property value="model.fund.id"/>&model.department.id=<s:property value="model.department.id"/>&model.function.id=<s:property value="model.function.id"/>&model.functionary.id=<s:property value="model.functionary.id"/>&model.scheme.id=<s:property value="model.scheme.id"/>&model.subScheme.id=<s:property value="model.subScheme.id"/>&model.fundsource.id=<s:property value="model.fundsource.id"/>&model.field.id=<s:property value="model.field.id"/>&model.startDate=<s:property value="model.startDate"/>&model.endDate=<s:property value="model.endDate"/>'>PDF</a></div>
</div>
