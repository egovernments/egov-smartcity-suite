<%@ include file="/includes/taglibs.jsp"%>

<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%><table cellspacing="1" width="80%" cellpadding="1" border="1">
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