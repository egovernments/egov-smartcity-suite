<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>

<s:if test="%{chequeDetails.size()>0}">
<br/>
<table width="99%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="blueborderfortd">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
			<tr>
                <th class="bluebgheadtd" colspan="5">Cheque Number</th>
                <th class="bluebgheadtd" colspan="5">Cheque Date</th>
                <th class="bluebgheadtd" colspan="5">Cheque Amount</th>
			  </tr>            
		<s:iterator value="chequeDetails" status="stat" var="p">
			<tr>
				<td class="blueborderfortd" colspan="5"><s:property value="#p.instrumentHeaderId.instrumentNumber"/>&nbsp;</td>
				<td class="blueborderfortd" colspan="5"><s:property value="%{getFormattedDate(#p.instrumentHeaderId.instrumentDate)}"/>&nbsp;</td>
				<td class="blueborderfortd" colspan="5"><div style="text-align: right">
				<s:text name="format.number"><s:param value="#p.instrumentHeaderId.instrumentAmount"></s:param></s:text>&nbsp;
				</div></td>
			</tr>
		</s:iterator>
			</table>
        </div></td>
      </tr>
	</table></td>
	</tr>
</table>
</s:if>
<s:else>No data found</s:else>
