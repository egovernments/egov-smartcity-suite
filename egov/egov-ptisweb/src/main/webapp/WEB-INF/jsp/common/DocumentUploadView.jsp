<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="docsectiontitle" />
			</div>
		</td>
	</tr>

	<tr>
		<td colspan="5">
			<table class="tablebottom" id="nameTable" width="100%" border="0" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<th class="bluebgheadtd"><s:text name="doctable.docenclosed" /></th>
						<th class="bluebgheadtd"><s:text name="doctable.doctype" /></th>
						<th class="bluebgheadtd"><s:text name="doctable.docdate" /></th>
						<th class="bluebgheadtd"><s:text name="doctable.docdetails" /></th>
						<th class="bluebgheadtd">Upload File</th>
					</tr>
					<s:iterator value="documentTypes" status="status" var="documentType">
						<tr>
							<td class="blueborderfortd" align="center">
								<s:checkbox name="documents[%{#status.index}].enclosed" disabled="true" />
							</td>
							<td class="blueborderfortd" style="text-align: left">
								<s:property	value="%{name}" />
								<s:hidden name="documents[%{#status.index}].type.id" value="%{id}"></s:hidden>
							</td>
							<td class="blueborderfortd" align="center">
								<s:date name="%{documents[#status.index].docDate}" format="dd/MM/yyyy" var="documentDate" />
								<s:property value="%{#documentDate}" />
							</td>
							<td class="blueborderfortd" align="center">
								<s:property value="%{documents[#status.index].description}" />
							</td>
							<td class="blueborderfortd" align="center">
								<s:iterator value="%{documents[#status.index].files}">
									<s:property value="%{fileName}" />
								</s:iterator>
							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</td>
	</tr>
</table>