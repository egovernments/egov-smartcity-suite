<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<s:if test="%{balanceSheet.size()>0}">
<br/>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
<td>
      <div align="center"><br/>
        <table border="0" cellspacing="0" cellpadding="0" class="tablebottom" width="100%">
          <tr>
            <td colspan="6">
			<div class="subheadsmallnew"><strong><s:text name="report.balancesheet.schedule"/> <s:property value="model.financialYear.finYearRange"/><br/> Amount in <s:property value="model.currency"/></strong></div>			</td>
          </tr>
          <tr>
		    <th class="bluebgheadtd"><s:text name="report.accountCode"/></th>
            <th class="bluebgheadtd"><s:text name="report.headOfAccount"/></th>
			<s:iterator value="balanceSheet.funds" status="stat">
            	<th class="bluebgheadtd"><s:property value="name"/> </th>
			</s:iterator>
            <th class="bluebgheadtd"><s:text name="report.currentTotals"/>: <s:property value="currentYearToDate"/> </th>
            <th class="bluebgheadtd"><s:text name="report.previousTotals"/>: <s:property value="previousYearToDate"/></th>
          </tr>
		<s:iterator value="balanceSheet.entries" status="stat">
          <tr>
            <td class="blueborderfortd">
			<div align="center">
				<s:if test='%{glCode != ""}'>
					<s:if test='%{displayBold == true}'>
						<strong><s:property value="glCode"/></strong>
					</s:if>
					<s:else>
						<s:property value="glCode"/>
					</s:else>
				</s:if>&nbsp;</div>		
			</td>
            <td class="blueborderfortd">
			<div align="left">
				<s:if test='%{displayBold == true}'>
					<strong><s:property value="accountName"/></strong>
				</s:if>
				<s:else>
					<s:property value="accountName"/>
				</s:else>&nbsp;
			</div>		
			</td>
			<s:iterator value="balanceSheet.funds" status="stat">
            	<td class="blueborderfortd">
					<div align="right"><s:property value="fundWiseAmount[name]"/>&nbsp;</div>		
				</td>
			</s:iterator>
            <td class="blueborderfortd">
			<div align="right">
				<s:if test='%{displayBold == true}'>
					<strong><s:if test='%{currentYearTotal != 0}'><s:property value="currentYearTotal"/></s:if><s:else>0.0</s:else></strong>
				</s:if>
				<s:else>
					<s:if test='%{currentYearTotal != 0}'><s:property value="currentYearTotal"/></s:if><s:else>0.0</s:else>
				</s:else>&nbsp;
			</div>		
			</td>
            <td class="blueborderfortd">
			<div align="right">
				<s:if test='%{displayBold == true}'>
					<strong><s:if test='%{previousYearTotal != 0}'><s:property value="previousYearTotal"/></s:if><s:else>0.0</s:else></strong>
				</s:if>
				<s:else>
					<s:if test='%{previousYearTotal != 0}'><s:property value="previousYearTotal"/></s:if><s:else>0.0</s:else>
				</s:else>&nbsp;
			</div>		
			</td>
          </tr>
		</s:iterator>
        </table>
</div>
</td>
</tr>
</table>
</s:if>