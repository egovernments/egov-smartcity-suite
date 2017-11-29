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

<div class="panel-heading">
	<div class="panel-title">View Past Hearings</div>
</div>
<c:choose>
	<c:when test="${hearingsList.isEmpty()}">
		<div class="form-group" align="center">Hearing details is not
			present.</div>
	</c:when>
	<c:otherwise>
		<table width="100%" border="1" align="center" cellpadding="0"
			cellspacing="0" class="table table-bordered datatable"
			id="hearingsTbl">
			<thead>
				<tr>
					<th colspan="1" class="text-center"><spring:message
							code="lbl.hearingdate" /></th>
					<th colspan="1" class="text-center"><spring:message
							code="lbl.purposeofhearing" /></th>
					<th align="center" colspan="1" class="text-center"><spring:message
							code="lbl.outcomeofhearing" /></th>
					<th align="center" colspan="1" class="text-center"><spring:message
							code="lbl.additionallawyer" /></th>
					<th align="center" colspan="1" class="text-center"><spring:message
							code="lbl.standingcounsel" /></th>
					<th align="center" colspan="1" class="text-center">Position-Employee</th>
					<th colspan="1" class="text-center"><spring:message
							code="lbl.edit" /></th>
				</tr>
			</thead>
			<c:forEach var="hearings" items="${hearingsList}">
				<tr>
					<td colspan="1">
						<div align="center">
							<fmt:formatDate value="${hearings.hearingDate}" var="HD"
								pattern="dd/MM/yyyy" />
							<c:out value="${HD}" />
						</div>
					</td>

					<td colspan="1">
						<div align="center">
							<c:out value="${hearings.purposeofHearings}" />
						</div>
					</td>
					<td colspan="1">
						<div align="center">
							<c:out value="${hearings.hearingOutcome}" />
						</div>
					</td>
					<td colspan="1">
						<div align="center">
							<c:out value="${hearings.additionalLawyers}" />
						</div>
					</td>
					<td colspan="1">
						<div align="center">
							<c:out value="${hearings.isStandingCounselPresent}" />
						</div>
					</td>
					<td colspan="1">
						<div align="center">
							<c:choose>
								<c:when test="${not empty hearings.employeeHearingList}">
									<c:forEach items="${hearings.employeeHearingList}"
										var="employeeHearingList" varStatus="counter">

										<c:out value="${employeeHearingList.employee.name}"></c:out>- <c:out
											value="${employeeHearingList.employee.username}"></c:out>

									</c:forEach>
								</c:when>
							</c:choose>
						</div>
					</td>
					<td colspan="1">
						<div align="center">
							<a href="javascript:void(0);"
								onclick="edit('<c:out value="${hearings.id}" />');">Edit</a> <input
								type="hidden" id="hearings.id" name="hearings.id"
								value="${hearings.id}" />
						</div>
					</td>

				</tr>
			</c:forEach>
		</table>
	</c:otherwise>
</c:choose>

<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>">
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/hearings.js?rnd=${app_release_no}'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/legalcaseSearch.js?rnd=${app_release_no}'/>"></script>