<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<s:if test="%{budgetVarianceEntries.size()>0}">
<br/>
<table width="99%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="blueborderfortd">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
			<tr>
				<th class="bluebgheadtd">Budget</th>
				<th class="bluebgheadtd">Budget Head</th>
				<th class="bluebgheadtd">Department</th>
                <th class="bluebgheadtd">Function</th>
                <th class="bluebgheadtd">Fund</th>
                <th class="bluebgheadtd"><s:property value="type"/> Estimate(Rs)</th>
                <th class="bluebgheadtd">Additional Appropriation(Rs)</th>
                <th class="bluebgheadtd">Actual(Rs)</th>
                <th class="bluebgheadtd">Variance(Rs)</th>
			  </tr>
		<s:iterator value="budgetVarianceEntries" status="stat" var="p">
			<tr>
					<td class="blueborderfortd"><div align="left"><s:property value="budgetCode"/>&nbsp;</div></td>
					<td class="blueborderfortd"><div align="left"><s:property value="budgetHead"/>&nbsp;</div></td>
					<td class="blueborderfortd"><div align="left"><s:property value="departmentName"/>&nbsp;</div></td>
					<td class="blueborderfortd"><div align="left"><s:property value="functionCode" /> </div>&nbsp;</td>
					<td class="blueborderfortd"><div align="left"><s:property value="fundCode"/>&nbsp;</div></td>
					<td class="blueborderfortd"><div align="right">
						<s:if test="%{#p.estimate != null}">
							<s:text name="format.number"><s:param name="value" value="estimate"/></s:text>&nbsp;
						</s:if>
						<s:else>0.00</s:else>
					</div></td>
					<td class="blueborderfortd"><div align="right">
						<s:if test="%{#p.additionalAppropriation != null}">
							<s:text name="format.number"><s:param name="value" value="additionalAppropriation"/></s:text>&nbsp;
						</s:if>
						<s:else>0.00</s:else>
					</div></td>
					<td class="blueborderfortd"><div align="right">
						<s:if test="%{#p.actual != null}">
							<s:text name="format.number"><s:param name="value" value="actual"/></s:text>&nbsp;
						</s:if>
						<s:else>0.00</s:else>
					</div></td>
					<td class="blueborderfortd"><div align="right">
						<s:if test="%{#p.variance != null}">
							<s:text name="format.number"><s:param name="value" value="variance"/></s:text>&nbsp;
						</s:if>
						<s:else>0.00</s:else>
					</div></td>
			</tr>
		</s:iterator>
			</table>
        </div></td>
      </tr>
	</table></td>
	</tr>
</table>
		<div class="buttonbottom" align="center">
		Export Options: 
		<label onclick="exportXls()"><a href='javascript:void(0);'>Excel</a></label> 
		| <label onclick="exportPdf()"><a href="javascript:void(0);">PDF</a></label>
		</div>
</s:if>
<s:else>No data found</s:else>
