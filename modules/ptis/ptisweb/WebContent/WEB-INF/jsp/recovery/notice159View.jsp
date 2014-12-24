<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<!--View Property Details -  Start   -->

	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="notice159.title"></s:text>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th class="bluebgheadtd">
						<s:text name="execution.date.head"></s:text>
					</th>
					<th class="bluebgheadtd">
						<s:text name="remarks.head"></s:text>
					</th>
					
					<th class="bluebgheadtd">
						<s:text name="notice159.head"></s:text>
					</th>
				</tr>
				<tr>
					<s:date name="ceaseNotice.executionDate" var="executionDateId" format="dd/MM/yyyy" />
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{executionDateId}" />
								</div>
							</td>
					<td class="greybox">
						<div align="center">
							<s:property value="%{ceaseNotice.remarks}" />
						</div>
					</td>
					
					<td class="greybox">
					<s:if test="%{ceaseNotice.notice != null}">
						<div align="center">
							 <a href="#" onclick="displayNotice('<s:property value='%{ceaseNotice.notice.noticeNo}'/>');"> <s:property value="%{ceaseNotice.notice.noticeNo}" /> </a>
						</div>
					</s:if><s:else>N/A </s:else>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>