<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<form:form name="aadharseedingapproval-form"
	class="form-horizontal form-groups-bordered"
	id="aadharseedingapproval-form" modelAttribute="aadharSearchResult"
	method="POST">
	<div>
		<div class="panel-body">
			<table
				class="table table-bordered datatable dt-responsive table-hover"
				id="approvalTable">
				<thead>
					<tr>
						<th style="width: 5%"><input type="checkbox"
							name="select_all" class="allCheckBoxClass" /></th>
						<th align="center" class="bluebgheadtd" style="width: 15%"><spring:message
								code="lbl.assessment" /></th>
						<th align="center" class="bluebgheadtd" style="width: 25%"><spring:message
								code="lbl.ownerName" /></th>
						<th align="center" class="bluebgheadtd" style="width: 15%"><spring:message
								code="lbl.doorNumber" /></th>
						<th align="center" class="bluebgheadtd" style="width: 30%"><spring:message
								code="lbl.address" /></th>
						<th align="center" class="bluebgheadtd" style="width: 10%"><spring:message
								code="lbl.button.view" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${serachResult}" var="ownerInfo">
						<tr>
							<td><input type="checkbox" class="check_box" name="checkbox"></td>
							<td>
								<div>
									<strong><c:out value="${ownerInfo.assessmentNo}"
											default="N/A" /> </strong>
								</div>
							</td>
							<td>
								<div>
									<strong><c:out value="${ownerInfo.ownerName}"
											default="N/A" /> </strong>
								</div>
							</td>
							<td>
								<div>
									<strong><c:out value="${ownerInfo.doorNo}"
											default="N/A" /> </strong>
								</div>
							</td>
							<td>
								<div>
									<strong><c:out value="${ownerInfo.address}"
											default="N/A" /> </strong>
								</div>
							</td>
							<td><input type="button" name="showOwnerDetails" id="viewButton"
						value="View Aadhar Details" class="viewButton"/></td>
						</tr>
					</c:forEach>
				</tbody>

			</table>
			<div class="row">
				<div class="text-center">
					<button type="button" class="btn btn-primary add-margin"
						id="updateBtn">
						<spring:message code="lbl.aadhar.approve" />
					</button>
					<button type="button" class="btn btn-primary add-margin"
						id="rejectBtn">
						<spring:message code="lbl.aadhar.reject" />
					</button>
					<button type="button" id="btnClose" class="btn btn-default" onClick=window.close()>Close</button>
				</div class="text-center">
			</div>
		</div>
	</div>
</form:form>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/aadharseeding.js?rnd=${app_release_no}'/>"></script>

