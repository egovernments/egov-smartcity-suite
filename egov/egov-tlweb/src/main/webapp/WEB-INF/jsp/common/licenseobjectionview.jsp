<s:iterator var="obj" value="objections">
	<tr>
		<td colspan="5" class="headingwk">
			<div class="arrowiconwk">
				<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
			</div>
			<div class="headplacer">
				<s:text name="license.objection.details" />
			</div>
		</td>
	</tr>
	<c:choose>
		<c:when test="${trclass=='greybox'}">
			<c:set var="trclass" value="bluebox" />
		</c:when>
		<c:when test="${trclass=='bluebox'}">
			<c:set var="trclass" value="greybox" />
		</c:when>
	</c:choose>
	<tr>
		<td class="<c:out value="${trclass}"/>">
			&nbsp;
			<s:hidden name="docNumber" id="docNumber" />
		</td>
		<td class="<c:out value="${trclass}"/>">
			<s:text name="license.objection.raisedby" />
		</td>
		<td class="<c:out value="${trclass}"/>">
			<s:property value="name" />
		</td>
		<td class="<c:out value="${trclass}"/>">
			<s:text name="license.objection.receivedon" />
		</td>
		<td class="<c:out value="${trclass}"/>">
			<s:date name="objectionDate" id="frmtdDt" format="dd/MM/yyyy" />
			<s:property value="%{frmtdDt}" />
		</td>
	</tr>
	<c:choose>
		<c:when test="${trclass=='greybox'}">
			<c:set var="trclass" value="bluebox" />
		</c:when>
		<c:when test="${trclass=='bluebox'}">
			<c:set var="trclass" value="greybox" />
		</c:when>
	</c:choose>
	<tr>
		<td class="<c:out value="${trclass}"/>">
			&nbsp;
		</td>
		<td class="<c:out value="${trclass}"/>">
			<s:text name="license.objection.reason" />
		</td>
		<td class="<c:out value="${trclass}"/>">
			<s:property value="getObjectionReason(reason)" />
		</td>
		<td class="<c:out value="${trclass}"/>">
			<s:text name="license.objection.address" />
		</td>
		<td class="<c:out value="${trclass}"/>">
			<s:property value="address" />
		</td>
	</tr>
	<c:choose>
		<c:when test="${trclass=='greybox'}">
			<c:set var="trclass" value="bluebox" />
		</c:when>
		<c:when test="${trclass=='bluebox'}">
			<c:set var="trclass" value="greybox" />
		</c:when>
	</c:choose>
	<tr>
		<td class="<c:out value="${trclass}"/>">
			&nbsp;
		</td>
		<td class="<c:out value="${trclass}"/>">
			<s:text name="license.objection.details" />
		</td>
		<td class="<c:out value="${trclass}"/>" colspan="3">
			<s:property value="details" />
		</td>
	</tr>
</s:iterator>