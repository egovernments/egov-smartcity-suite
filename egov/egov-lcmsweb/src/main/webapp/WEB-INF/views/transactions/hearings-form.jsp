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
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Hearing Details</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-2 control-label text-left">Hearing
							Date :<span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:input path="hearingDate" class="form-control datepicker"
								data-date-end-date="" data-inputmask="'mask': 'd/m/y'" required="required"/>
							<form:errors path="hearingDate" cssClass="error-msg" />
						</div>

					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label text-left">Purpose Of
							Hearing :<span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="purposeofHearings"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="1024" required="required" />
							<form:errors path="purposeofHearings" cssClass="error-msg" />
						</div>

						<label class="col-sm-3 control-label text-right">Outcome
							Of Hearing :</label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="hearingOutcome"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="2056" />
							<form:errors path="hearingOutcome" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right">Additional
							Lawyer :</label>
						<div class="col-sm-3 add-margin">
							<form:input path="additionalLawyers"
								class="form-control text-left patternvalidation"
								data-pattern="alphabetwithspace" maxlength="50" />
							<form:errors path="additionalLawyers" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right">Was
							Standing Counsel Present ?</label>
						<div class="col-sm-2 add-margin">
							<form:checkbox path="isStandingCounselPresent" value="${isStandingCounselPresent}"/>
							<form:errors path="isStandingCounselPresent" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<div class="panel-heading">
							<div class="panel-title">Employee Details</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label text-right">Search
								Position Of Employee :</label>
							<div class="col-sm-3 add-margin">
								<form:input id="positionName" type="text" class="form-control "
									autocomplete="off" path="" name="" value="" placeholder="" />
								<input type="hidden" id="positionId" value="" />
								<c:forEach items="${position}" var="position">
									<a onclick="setPositionId(<c:out value="${position.id}"/>)"
										href="javascript:void(0)"
										class="btn btn-secondary btn-xs tag-element freq-ct"><c:out
											value="${position.name }" /> </a>
								</c:forEach>
							</div>
							<button type="button" class="btn btn-default" value="Add"
								id="addid">Add</button>
						</div>
						<table class="table table-striped table-bordered"
							id="employeeDetails">
							<thead>
								<tr>
									<th class="text-center">Position-Employee</th>
									<th class="text-center">Delete Employee</th>
								</tr>
							</thead>

							<tbody>
								
							</tbody>
						</table>
					</div>

					<input type="hidden" name="hearings" value="${hearings.id}" />
					