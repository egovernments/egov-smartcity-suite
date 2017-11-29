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

<c:forEach items="${majorCode}" var="basMessage">
	<c:out value="${basMessage}" />
	<br />
</c:forEach>
<c:forEach items="${maxCode}" var="basMessage">
	<c:out value="${basMessage}" />
	<br />
</c:forEach>

<input type="hidden" value="${mode }" id="mode" />
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Budget Group</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.name" /> <span class="mandatory"></span> </label>
						<div class="col-sm-6 add-margin">
							<form:input path="name"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="250" required="required" />
							<form:errors path="name" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.accounttype" /><span class="mandatory"></span></label>
						<div class="col-sm-4 add-margin">
							<form:select path="accountType" id="accountType"
								cssClass="form-control disablefield"
								cssErrorClass="form-control error" disabled="disabled">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${budgetAccountTypes}" />
							</form:select>
							<form:errors path="accountType" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.budgetingtype" /> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="budgetingType" id="budgetingType"
								cssClass="form-control disablefield"
								cssErrorClass="form-control error" disabled="disabled">
								<form:options items="${budgetingTypes}" />
							</form:select>
							<form:errors path="budgetingType" cssClass="error-msg" />
						</div>

					</div>
					<input type="hidden" id="majorCodeValue"
						value="${budgetGroup.majorCode.id}" /> <input type="hidden"
						id="maxCodeValue" value="${budgetGroup.maxCode.id}" /> <input
						type="hidden" id="minCodeValue" value="${budgetGroup.minCode.id}" />
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.majorcode" /> </label>
						<div class="col-sm-6 add-margin">
							<form:select path="majorCode" id="majorCode"
								cssClass="form-control disablefield"
								cssErrorClass="form-control error" disabled="disabled">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach items="${majorCodeList}" var="mc">
									<option value="${mc.id}">${mc.glcode}----${mc.name}</option>
								</c:forEach>
							</form:select>
							<form:errors path="majorCode" cssClass="error-msg" />
						</div>
					</div>
					<tr>
						<div class="form-group">
							<label class="col-sm-6 control-label text-center">OR</label>
						</div>
					</tr>
					<br>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.mincode" /> </label>
						<div class="col-sm-6 add-margin">
							<form:select path="minCode" id="minCode"
								cssClass="form-control disablefield"
								cssErrorClass="form-control error" disabled="disabled">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach items="${minCodeList}" var="mc">
									<option value="${mc.id}">${mc.glcode}----${mc.name}</option>
								</c:forEach>
							</form:select>
							<form:errors path="minCode" cssClass="error-msg" />
						</div>

					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.maxcode" /> </label>
						<div class="col-sm-6 add-margin">
							<form:select path="maxCode" id="maxCode"
								cssClass="form-control disablefield"
								cssErrorClass="form-control error" disabled="disabled">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach items="${minCodeList}" var="mc">
									<option value="${mc.id}">${mc.glcode}----${mc.name}</option>
								</c:forEach>
							</form:select>
							<form:errors path="maxCode" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.description" /> </label>
						<div class="col-sm-6 add-margin">
							<form:textarea path="description" type="text" placeholder=""
								autocomplete="off" class="form-control low-width"
								maxlength="250" />
							<form:errors path="description" cssClass="error-msg" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.bgisactive" /> </label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="isActive" />
							<form:errors path="isActive" cssClass="error-msg" />
						</div>
					</div>
					<input type="hidden" name="budgetGroup" value="${budgetGroup.id}" />