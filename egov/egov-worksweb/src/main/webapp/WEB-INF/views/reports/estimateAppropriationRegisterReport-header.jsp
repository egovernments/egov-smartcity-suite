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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title" style="text-align: center;">
			<spring:message code="title.estimateappropriationregister.search" />
		</div>
	</div>
	<div class="panel-body custom-form">
		<div class="form-group">
			<label class="col-sm-2 control-label text-right"><spring:message
					code="lbl.department" /><span class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:select path="department" data-first-option="false"
					id="departments" class="form-control" required="required">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${departments}" itemValue="id"
						itemLabel="name" />
				</form:select>
				<form:errors path="department" cssClass="add-margin error-msg" />
			</div>
			<label class="col-sm-3 control-label text-right"><spring:message
					code="lbl.financialyear" /><span class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:select path="financialYear" data-first-option="false"
					id="financialYear" class="form-control" required="required">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${financialYear}" itemValue="id"
						itemLabel="finYearRange" />
				</form:select>
				<form:errors path="financialYear" cssClass="add-margin error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label text-right"><spring:message
					code="lbl.asondate" /><span class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:input type="text" class="form-control datepicker"
					id="asOnDate" data-date-end-date="0d" path="asOnDate"
					required="required" />
				<form:errors path="asOnDate" cssClass="add-margin error-msg" />
			</div>
			<label class="col-sm-3 control-label text-right"><spring:message
					code="lbl.fund" /><span class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:select path="fund" data-first-option="false"
					class="form-control" id="fund" required="required">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${funds}" itemValue="id" itemLabel="name" />
				</form:select>
				<form:errors path="fund" cssClass="add-margin error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label text-right"><spring:message
					code="lbl.function" /><span class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:select path="function" data-first-option="false"
					name="function" class="form-control" id="function"
					required="required">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<c:forEach var="functions" items="${functions}">
						<form:option value="${functions.id}">
							<c:out value="${functions.code} - ${functions.name}" />
						</form:option>
					</c:forEach>
				</form:select>
				<form:errors path="function" cssClass="add-margin error-msg" />
			</div>
			<label class="col-sm-3 control-label text-right"><spring:message
					code="lbl.budgethead" /><span class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:select path="budgetHead" data-first-option="false"
					id="budgetHead" class="form-control" required="required">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${budgetHeads}" itemValue="id"
						itemLabel="name" />
				</form:select>
				<form:errors path="budgetHead" cssClass="add-margin error-msg" />
			</div>
		</div>
	</div>
</div>