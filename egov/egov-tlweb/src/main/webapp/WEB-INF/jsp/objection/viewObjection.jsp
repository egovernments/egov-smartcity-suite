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
			<a href="/egi/docmgmt/basicDocumentManager-viewDocument.action?docNumber=${docNumber}&moduleName=egtradelicense" target="_blank">View Objection Attachment </a>
		</s:if>
	</td>
</tr>
