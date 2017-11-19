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
  --%>

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">

			<div class="panel-heading" style="text-align: left">
				<div class="panel-title">
					<spring:message code="lbl.hdr.ownerdetails" />
				</div>
			</div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				class="table table-bordered" id="vacantLandTable">
				<thead>
					<tr>
						<th><spring:message code="lbl.adharno" /></th>
						<th><spring:message
								code="lbl.MobileNumber" /></th>
						<th><spring:message code="lbl.OwnerName" /></th>
						<th><spring:message code="lbl.gender" /></th>
						<th><spring:message
								code="lbl.EmailAddress" /></th>
						<th><spring:message
								code="lbl.GuardianRelation" /></th>
						<th><spring:message code="lbl.Guardian" /></th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when
							test="${!property.basicProperty.propertyOwnerInfo.isEmpty()}">
							<c:forEach items="${property.basicProperty.propertyOwnerInfo}"
								var="ownerInfo">
								<tr id="ownerDetailsRow">
								
									<td class="blueborderfortd" align="center" ><c:out
											default="N/A" value="${ownerInfo.owner.aadhaarNumber}"></c:out></td>
									<td class="blueborderfortd" align="center"><c:out
											default="N/A" value="${ownerInfo.owner.mobileNumber}"></c:out></td>
									<td class="blueborderfortd" align="center"><c:out
											default="N/A" value="${ownerInfo.owner.name}"></c:out></td>
									<td class="blueborderfortd" align="center"><c:out
											default="N/A" value="${ownerInfo.owner.gender}"></c:out></td>
									<td class="blueborderfortd" align="center"><c:out
											default="N/A" value="${ownerInfo.owner.emailId}"></c:out></td>
									<td class="blueborderfortd" align="center"><c:out
											default="N/A" value="${ownerInfo.owner.guardianRelation}"></c:out></td>
									<td class="blueborderfortd" align="center"><c:out
											default="N/A" value="${ownerInfo.owner.guardian}"></c:out></td>

								</tr>
							</c:forEach>
						</c:when>
					</c:choose>

				</tbody>
			</table>
		</div>
	</div>

</div>

