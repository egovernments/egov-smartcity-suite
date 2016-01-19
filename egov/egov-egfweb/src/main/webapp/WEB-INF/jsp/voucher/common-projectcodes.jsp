<%@ include file="/includes/taglibs.jsp"%>

<table cellspacing="1" width="80%" cellpadding="1" border="1">
	<tr>
		<td class="bluebgheadtd" style="align: center" width="5%">SlNo</td>
		<td class="bluebgheadtd" style="align: center" width="17%">Code</td>
		<td class="bluebgheadtd" style="align: center" width="60%">Name</td>
		<td class="bluebgheadtd" style="align: center" width="6%">Check
			box</td>
	</tr>
	<c:set var="trclass" value="greybox" />


	<s:if test="%{projectCodeList!=null && projectCodeList.size()>0}">
		<s:iterator var="pc" value="%{projectCodeList}" status="stat">
			<c:choose>
				<c:when test="${trclass=='greybox'}">
					<c:set var="trclass" value="bluebox" />
				</c:when>
				<c:when test="${trclass=='bluebox'}">
					<c:set var="trclass" value="greybox" />
				</c:when>
			</c:choose>
			<tr>
				<td class='<c:out value="${trclass}"/>' style='text-align: center'><s:property
						value="#stat.index+1" /></td>
				<td class='<c:out value="${trclass}"/>' style='text-align: center'><s:property
						value="%{code}" /></td>
				<td class='<c:out value="${trclass}"/>' style='text-align: center'><s:property
						value="%{name}" /></td>
				<td class='<c:out value="${trclass}"/>' style='text-align: center'><input
					type="checkbox"
					name='projectCodeIdList[<s:property value="#stat.index"/>]'
					value='<s:property value="%{id}" />' /></td>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</s:else>
</table>
