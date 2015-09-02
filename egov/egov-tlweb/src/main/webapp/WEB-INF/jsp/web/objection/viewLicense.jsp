<tr>
	<td colspan="5" class="headingwk">
		<div class="arrowiconwk">
			<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
		</div>
		<div class="headplacer">
			<s:text name='license.title.licensedetails' />
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
		<s:text name='licensee.applicantname' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:property value="licensee.applicantName" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.subcategory" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:property value="tradeName.name" />
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
		<s:text name="license.establishmentname" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:property value="nameOfEstablishment" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.license.number" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:property value="licenseNumber" />
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

<s:if test="%{license.boundary.parent.name.equalsIgnoreCase(@org.egov.license.utils.Constants@CITY_NAME)}">
	<tr>
		<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
		<td class="<c:out value="${trclass}"/>"><s:text
					name="license.zone" /></td>
		<td class="<c:out value="${trclass}"/>"><s:property
				value="license.boundary.name" /></td>
		<td class="<c:out value="${trclass}"/>" colspan="2"></td>
	</tr>
</s:if>
<s:else>
	<tr>
		<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
		<td class="<c:out value="${trclass}"/>"><s:text
					name="license.zone" /></td>
		<td class="<c:out value="${trclass}"/>"><s:property
				value="license.boundary.parent.name" /></td>
		<td class="<c:out value="${trclass}"/>"><s:text
					name="license.division" /></td>
		<td class="<c:out value="${trclass}"/>"><s:property
				value="license.boundary.name" /></td>
	</tr>
</s:else>
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
		<s:text name='license.housenumber' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:property value="address.houseNo" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name='license.housenumber.old' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:property value="address.streetAddress2" />
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
		<s:text name='license.pincode' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:property value="address.pinCode" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name='license.address.phonenumber' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:property value="phoneNumber" />
	</td>
</tr>
