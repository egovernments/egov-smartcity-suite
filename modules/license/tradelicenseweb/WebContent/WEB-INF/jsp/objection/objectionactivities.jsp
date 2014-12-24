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
		<s:text name="objection.activity.date" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:date name="#act.activityDate" id="frmtdDt" format="dd/MM/yyyy" />
		<s:property value="%{frmtdDt}" />
	</td>
	<td class="<c:out value="${trclass}"/>" colspan="2">
		<s:if test="%{#act.docNumber != null && #act.docNumber != ''}">
			<a href="/egi/docmgmt/basicDocumentManager!viewDocument.action?docNumber=<s:property value="#act.docNumber"/>&moduleName=egtradelicense" target="_blank">View <s:property value="#act.type" /> Attachment </a>
		</s:if>
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
		<s:text name="objection.activitydetails" />
	</td>
	<td class="<c:out value="${trclass}"/>" colspan="3">
		<s:textarea name="#act.details" rows="2" cols="100" id="" disabled="true" tabindex="1" />
	</td>
</tr>
<tr>
	<td colspan="5">
		&nbsp;
	</td>
</tr>
