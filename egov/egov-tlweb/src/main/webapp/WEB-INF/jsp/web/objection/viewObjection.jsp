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
		<s:textarea name="address" rows="2" cols="33" id="objectionAddress" value="%{address}" disabled="true" />
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
		<s:textarea name="details" rows="2" cols="100" id="objectionDetails" value="%{details}" disabled="true" />
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
	<td colspan="4" height="22" align="right" class="<c:out value="${trclass}"/>">
		<s:if test="%{docNumber != null && docNumber != ''}">
			<a href="/egi/docmgmt/basicDocumentManager!viewDocument.action?docNumber=${docNumber}&moduleName=egtradelicense" target="_blank">View Objection Attachment </a>
		</s:if>
	</td>
</tr>