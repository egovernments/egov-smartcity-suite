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
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn" %>
<input type="hidden" value="${modify}" id="modify" />
<input type="hidden" value="${mode}" id="mode" />
<c:forEach items="${validationMessage}" var="basMessage">
	<c:out value="${basMessage}" />
	<br />
</c:forEach>
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Budget Definition</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.name" /> </label>
						<div class="col-sm-6 add-margin">
							<form:input path="name"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="50" />
							<form:errors path="name" cssClass="error-msg" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.isbere" /> </label>
						<div class="col-sm-2 col-xs-12 add-margin">
							<div class="radio">
								<label><form:radiobutton path="isbere" id="isBeRe"
										value="BE" required="required" checked="checked" onchange="resetFunction()"/>BE</label>
							</div>
						</div>
						<div class="col-sm-2 col-xs-12 add-margin">
							<div class="radio">
								<label><form:radiobutton path="isbere" id="isBeRe"
										value="RE" required="required" onchange="resetFunction()"/>RE</label>
							</div>
						</div>
						<form:errors path="isbere" cssClass="error-msg" />
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.financialyear" /> </label>
						<div class="col-sm-6 add-margin">
							<form:select path="financialYear" id="financialYear"
								cssClass="form-control disablefield"
								cssErrorClass="form-control error"
								onchange="getParentByFinancialYear(this.value);getReferenceBudgets(this.value)"
								disabled="disabled">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach items="${financialYearList}" var="mc">
									<option value="${mc.id}">${mc.finYearRange}</option>
								</c:forEach>
							</form:select>
							<form:errors path="financialYear" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">

						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.parent" /> </label>
						<div class="col-sm-6 add-margin">
							<form:select path="parent" id="parent"
								cssClass="form-control disablefield"
								cssErrorClass="form-control error" disabled="disabled">
								<c:if test="${empty modify}">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
								</c:if>
								<form:options items="${budgets}" itemValue="id" itemLabel="name" />
							</form:select>
							<form:errors path="parent" cssClass="error-msg" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.description" /> </label>
						<div class="col-sm-6 add-margin">
							<form:textarea path="description"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="250" />
							<form:errors path="description" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.isactivebudget" /> </label>
						<div class="col-sm-6 add-margin">
							<form:checkbox path="isActiveBudget" />
							<form:errors path="isActiveBudget" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.isprimarybudget" /> </label>
						<div class="col-sm-6 add-margin">
							<form:checkbox path="isPrimaryBudget" id="isPrimaryBudget" />
							<form:errors path="isPrimaryBudget" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.referencebudget" /> </label>
						<div class="col-sm-6 add-margin">
							<form:select path="referenceBudget" id="referenceBudget"
								cssClass="form-control disablefield"
								cssErrorClass="form-control error" disabled="disabled">
								<c:if test="${empty modify}">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
								</c:if>
								<form:options items="${budgets}" itemValue="id" itemLabel="name" />
							</form:select>
							<form:errors path="referenceBudget" cssClass="error-msg" />
						</div>
					</div>
					<input type="hidden" name="budget" id="budgetId"
						value="${budget.id}" /> <input type="hidden" id="financialYearId"
						value="${budget.financialYear.id}" /> <input type="hidden"
						id="parentValue" value="${budget.parent.name}" /> <input
						type="hidden" id="referenceValue"
						value="${budget.referenceBudget.id}" />
					<c:if test="${mode =='edit'}">
						<input type="hidden" id="isPrimaryBudget" name="isPrimaryBudget"
							value="${budget.isPrimaryBudget}" />
					</c:if>
					<script type="text/javascript"
						src="<cdn:url value='/resources/app/js/budgetDefinitionHelper.js?rnd=${app_release_no}'/>"></script>