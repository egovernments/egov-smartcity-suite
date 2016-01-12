<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>
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

<s:if test="%{license.boundary.parent.name.equalsIgnoreCase(@org.egov.tl.utils.Constants@CITY_NAME)}">
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
