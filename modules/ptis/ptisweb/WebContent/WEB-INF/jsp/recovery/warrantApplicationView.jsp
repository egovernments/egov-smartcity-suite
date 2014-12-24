<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="recovery.expenses.title"></s:text>
			</div>
		</td>
	</tr>

	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th class="bluebgheadtd">
						<s:text name="warrantFee" /> 
					</th>
					<th class="bluebgheadtd">
						<s:text name="courtFee" />
					</th>
					<th class="bluebgheadtd">
				    	<s:text name="noticeFee" />
						
					</th>
					<th class="bluebgheadtd">
						<s:text name="totalFee" />
					</th>
					<th class="bluebgheadtd">
						<s:text name="remarks.head" />
					</th>
					<th class="bluebgheadtd">
						<s:text name="notice" />
					</th>
					
				</tr>
				<tr>
					<td class="greybox">
						<div align="center">
							<s:property default="N/A"
								value="%{recovery.warrant.warrantFees[0].amount}" />

						</div>
					</td>
					<td class="greybox">
						<div align="center">
							<s:property default="N/A"
								value="%{recovery.warrant.warrantFees[1].amount}" />

						</div>
					</td>
					<td class="greybox">
						<div align="center">
							<s:property default="N/A"
								value="%{recovery.warrant.warrantFees[2].amount}" />

						</div>
					</td>
					<td class="greybox">
						<div align="center">
							<s:property default="N/A"
								value="%{recovery.warrant.warrantFees[2].amount.add(recovery.warrant.warrantFees[1].amount.add(recovery.warrant.warrantFees[0].amount))}" />
						</div>
					</td>
					<td class="greybox">
						<div align="center">
							<s:property value="%{warrant.remarks}" />
						</div>
					</td>
					<td class="greybox">
					<s:if test="%{recovery.warrant.notice != null}">
						<div align="center">
							 <a href="#" onclick="displayNotice('<s:property value='%{warrant.notice.noticeNo}'/>');"> <s:property value="%{warrant.notice.noticeNo}" /> </a>
						</div>
					</s:if><s:else>N/A </s:else>
					</td>

				</tr>
			</table>
		</td>
	</tr>

</table>

<script>
	function openWarrantApplication(recoveryId){
		url="${pageContext.request.contextPath}/recovery/recovery!getWarrantApplicaton.action?model.id="+recoveryId;
		window.open(url,'Recovery','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	}
</script>