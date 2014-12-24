<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<s:if test="%{incomeExpenditureStatement.size()>0}">
<div id="budgetSearchGrid" style="width:1250px;overflow-x:auto; overflow-y:hidden;">
<br/>
<div style="overflow-x:scroll; overflow-y:scroll;">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
<td>
      <div align="center"><br/>
        <table border="0" cellspacing="0" cellpadding="0" class="tablebottom" width="100%">
          <tr>
            <td colspan="7">
			<div class="subheadsmallnew"><strong><s:text name="report.ie.heading"/> <s:property value="model.financialYear.finYearRange"/><br/> <s:text name="report.amount.in"/> <s:property value="model.currency"/></strong></div></td>
          </tr>
          <tr>
		    <th class="bluebgheadtd"><s:text name="report.accountCode"/></th>
            <th class="bluebgheadtd"><s:text name="report.headOfAccount"/></th>
			<th class="bluebgheadtd"><s:text name="report.scheduleNo"/></th>
			<s:iterator value="incomeExpenditureStatement.funds" status="stat">
            	<th class="bluebgheadtd"><s:property value="name"/> </th>
			</s:iterator>
            <th class="bluebgheadtd"><s:text name="report.currentTotals"/>: <s:property value="currentYearToDate"/> (Rs)</th>
            <th class="bluebgheadtd"><s:text name="report.previousTotals"/>: <s:property value="previousYearToDate"/> (Rs)</th>
          </tr>
		<s:iterator value="incomeExpenditureStatement.entries" status="stat">
          <tr>
            <td class="blueborderfortd">
			<div align="center"><s:if test='%{glCode != ""}'>
					<s:if test='%{displayBold == true}'>
						<strong><s:property value="glCode"/></strong>
					</s:if>
					<s:else>
						<s:property value="glCode"/>
					</s:else>
				</s:if>&nbsp;
			</div>		
			</td>
            <td class="blueborderfortd">
			<div align="left"><s:if test='%{scheduleNo == ""}'><strong><s:property value="accountName"/></strong></s:if><s:else><s:property value="accountName"/></s:else>&nbsp;</div>		
			</td>
            <td class="blueborderfortd">
			<div align="center"><a href="javascript:void(0);" onclick='return showSchedule(<s:property value="glCode"/>)'><s:property value="scheduleNo"/></a>&nbsp;</div>		
			</td>
			<s:iterator value="incomeExpenditureStatement.funds" status="stat">
            	<td class="blueborderfortd">
					<div align="right">
						<s:if test='%{displayBold == true}'><strong><s:property value="fundWiseAmount[name]"/>&nbsp;</strong></s:if>
						<s:else><s:property value="fundWiseAmount[name]"/>&nbsp;</s:else>
					</div>		
				</td>
			</s:iterator>
            <td class="blueborderfortd">
			<div align="right"><s:if test='%{displayBold == true}'>
					<strong><s:if test='%{currentYearTotal != 0}'><s:property value="currentYearTotal"/></s:if><s:else>0.00</s:else></strong>
				</s:if>
				<s:else>
					<s:if test='%{currentYearTotal != 0}'><s:property value="currentYearTotal"/></s:if><s:else>0.00</s:else>
				</s:else>&nbsp;
			</div>		
			</td>
            <td class="blueborderfortd">
			<div align="right">
				<s:if test='%{displayBold == true}'>
					<strong><s:if test='%{previousYearTotal != 0}'><s:property value="previousYearTotal"/></s:if><s:else>0.00</s:else></strong>
				</s:if>
				<s:else>
					<s:if test='%{previousYearTotal != 0}'><s:property value="previousYearTotal"/></s:if><s:else>0.00</s:else>
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
<div class="buttonbottom"><s:text name="report.export.options"/>: <a href='/EGF/report/incomeExpenditureReport!generateIncomeExpenditureXls.action?showDropDown=false&model.period=<s:property value="model.period"/>&model.currency=<s:property value="model.currency"/>&model.financialYear.id=<s:property value="model.financialYear.id"/>&model.department.id=<s:property value="model.department.id"/>&model.asOndate=<s:property value="model.asOndate"/>'>Excel</a> | <a href='/EGF/report/incomeExpenditureReport!generateIncomeExpenditurePdf.action?showDropDown=false&model.period=<s:property value="model.period"/>&model.currency=<s:property value="model.currency"/>&model.financialYear.id=<s:property value="model.financialYear.id"/>&model.department.id=<s:property value="model.department.id"/>&model.asOndate=<s:property value="model.asOndate"/>'>PDF</a></div>
</div>
</s:if>