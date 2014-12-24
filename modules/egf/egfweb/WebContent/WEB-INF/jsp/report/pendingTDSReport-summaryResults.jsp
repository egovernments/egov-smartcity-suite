<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<s:if test="%{message != ''}">
	<label style="color: red"><s:property value="message"/></label>
</s:if>
<s:elseif test="%{remittedTDS.size()>0}">
<br/>
<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>
            <td colspan="7">
			<div class="subheadsmallnew"><strong>TDS Summary Report as on <s:property value="%{getFormattedDate(asOnDate)}"/></strong></div></td>
        </tr>
      <tr>
        <td class="blueborderfortd">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
			<tr>
				<th class="bluebgheadtd">Sl No</th>
				<th class="bluebgheadtd">Nature Of deduction</th>
				<th class="bluebgheadtd">Month</th>
                <th class="bluebgheadtd">Total Deduction(Rs)</th>
                <th class="bluebgheadtd">Total Remitted(Rs)</th>
		  </tr>
		<s:iterator value="remittedTDS" status="stat" var="p">
			<tr>
				<td class="blueborderfortd"><div align="left"><s:property value="#stat.index+1"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="natureOfDeduction"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="month"/>&nbsp;</div></td>
				<td class="blueborderfortd">
					<div align="right">
						<s:if test="%{#p.totalDeduction != null}">
							<s:text name="format.number"><s:param name="value" value="totalDeduction"/></s:text>&nbsp;
						</s:if>
						<s:else>0.00</s:else>
					</div>
				</td>
				<td class="blueborderfortd">
					<div align="right">
						<s:if test="%{#p.totalRemitted != null}">
							<s:text name="format.number"><s:param name="value" value="totalRemitted"/></s:text>&nbsp;
						</s:if>
						<s:else>0.00</s:else>
					</div>
				</td>
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

</s:elseif>
<s:else>No TDS found</s:else>
