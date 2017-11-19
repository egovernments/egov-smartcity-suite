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
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<c:if test="${mode == 'create'}">
				<div class="panel-heading">
					<div class="panel-title">Hearing Details</div>
				</div>
			</c:if>
			<c:if test="${mode == 'edit'}">
				<div class="panel-heading">
					<div class="panel-title">Edit Hearing Details</div>
				</div>
			</c:if>
			<div class="panel-body">
				<div class="form-group">
					<label class="col-sm-2 control-label text-left"><spring:message
							code="lbl.hearingdate" /> :<span class="mandatory"></span> </label>
					<div class="col-sm-3 add-margin">
						<form:input path="hearingDate" class="form-control datepicker"
							id="hearingDate" data-date-end-date=""
							data-inputmask="'mask': 'd/m/y'" required="required" />
						<form:errors path="hearingDate" cssClass="error-msg" />
					</div>

				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label text-left"><spring:message
							code="lbl.purposeofhearing" /> :<span class="mandatory"></span>
					</label>
					<div class="col-sm-3 add-margin">
						<form:textarea path="purposeofHearings"
							class="form-control text-left patternvalidation"
							data-pattern="alphanumericwithspecialcharacterswithspace"
							maxlength="1024" required="required" />
						<form:errors path="purposeofHearings" cssClass="error-msg" />
					</div>

					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.outcomeofhearing" /> :</label>
					<div class="col-sm-3 add-margin">
						<form:textarea path="hearingOutcome"
							class="form-control text-left patternvalidation"
							data-pattern="alphanumericwithspecialcharacterswithspace"
							maxlength="2056" />
						<form:errors path="hearingOutcome" cssClass="error-msg" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.additionallawyer" />:</label>
					<div class="col-sm-3 add-margin">
						<form:input path="additionalLawyers"
							class="form-control text-left patternvalidation"
							data-pattern="alphanumericwithspecialcharacterswithspace"
							maxlength="50" />
						<form:errors path="additionalLawyers" cssClass="error-msg" />
					</div>
					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.standingcounsel" /> ?</label>
					<div class="col-sm-2 add-margin">
						<form:checkbox path="isStandingCounselPresent"
							value="${isStandingCounselPresent}" />
						<form:errors path="isStandingCounselPresent" cssClass="error-msg" />
					</div>
				</div>
				<div class="form-group">
					<c:choose>
						<c:when test="${not empty hearings.getTempEmplyeeHearing()}">
							<div class="panel-heading">
								<div class="panel-title">Employee</div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="panel-heading">
								<div class="panel-title">Employee Details</div>
							</div>
						</c:otherwise>
					</c:choose>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.positionofemployee" /> :</label>
						<div class="col-sm-3 add-margin">
							<form:input id="positionEmpName" type="text"
								class="form-control " autocomplete="off" path="" name=""
								value="" placeholder="" />
							<input type="hidden" id="positionEmpId" value="" />

						</div>
						<button type="button" class="btn btn-default" value="Add"
							id="addid">Add</button>
					</div>
					<table class="table table-striped table-bordered"
						id="employeeDetails">
						<thead>
							<tr>
								<th class="text-center">Position-Employee</th>
							</tr>
						</thead>

						<tbody>
							<c:choose>
								<c:when test="${not empty hearings.getTempEmplyeeHearing()}">
									<c:forEach items="${hearings.getTempEmplyeeHearing()}"
										var="positionTemplList" varStatus="counter">
										<tr>
											<td class="text-right"><form:input
													path="positionTemplList[${counter.index}].employee.name"
													cssClass="form-control confValues"
													value="${positionTemplList.employee.name}"
													id="positionTemplList[${counter.index}].employee.name" />
												<%--   <input type="hidden" name="positionTemplList[${counter.index}].id" value="${positionTemplList.id}" />  --%>
											</td>

										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>

									<tr>
										<td><form:input path="positionTemplList[0].empPosName"
												cssClass="form-control" id="positionTemplList[0].empPosName" />
										</td>

										<td><span class="add-padding"><i
												class="fa fa-trash" data-func="add" aria-hidden="true"
												id="emp_delete_row"></i></span></td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>

				<input type="hidden" name="hearings" value="${hearings.id}" />
			</div>
		</div>
	</div>
</div>
