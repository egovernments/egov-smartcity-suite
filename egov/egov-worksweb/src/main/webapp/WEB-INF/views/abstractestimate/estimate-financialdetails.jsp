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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="panel panel-primary" data-collapsed="0"
	style="text-align: left">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.financialdetails" />
		</div>
	</div>
	<div class="panel-body custom-form">
		<div class="form-group">
			<label class="col-sm-2 control-label text-right"> <spring:message
					code="lbl.fund" /><c:if test="${abstractEstimate.lineEstimateDetails == null }"><span class="mandatory"></span></c:if>
			</label>
			<div class="col-sm-3 add-margin">
					<c:choose>
						<c:when test="${budgetControlType != 'NONE' }">
							<form:select path="financialDetails[0].fund" data-first-option="false" id="fund"  class="form-control disablefield" onchange="getFunctionsByFundAndDepartment();getBudgetHeads();getSchemsByFundId(this.value);" required="required">
							<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${funds}" itemValue="id" itemLabel="name" />
				</form:select>
						</c:when>
						<c:otherwise>
							<form:select path="financialDetails[0].fund" data-first-option="false" id="fund"  class="form-control disablefield" onchange="getSchemsByFundId(this.value);" required="required">
							<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${funds}" itemValue="id" itemLabel="name" />
				</form:select>
						</c:otherwise>
					</c:choose>
				<form:errors path="financialDetails[0].fund"
					cssClass="add-margin error-msg" />
			</div>

			<label class="col-sm-2 control-label text-right"> <spring:message code="lbl.function" /><c:if test="${abstractEstimate.lineEstimateDetails == null }"><span class="mandatory"></span></c:if></label>
			<div class="col-sm-3 add-margin">
				<c:choose>
					<c:when test="${budgetControlType != 'NONE' }">
						<form:select path="financialDetails[0].function" data-first-option="false" id="function" class="form-control disablefield" onchange="getBudgetHeads();" required="required">
						<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${functions}" itemValue="id" itemLabel="name" />
					</form:select>
					</c:when>
					<c:otherwise>
						<form:select path="financialDetails[0].function" data-first-option="false" id="function" class="form-control disablefield" required="required">	
						<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${functions}" itemValue="id" itemLabel="name" />
					</form:select>
						</c:otherwise>
				</c:choose>
				<form:errors path="financialDetails[0].function" cssClass="add-margin error-msg" />
			</div>
			<input type="hidden" id="functionId" value="${abstractEstimate.financialDetails.get(0).function.id }" />
		</div>
		<div class="form-group">
				<label class="col-sm-2 control-label text-right">
					<spring:message code="lbl.budgethead" /><c:if test="${abstractEstimate.lineEstimateDetails == null }"><span class="mandatory"></span></c:if>
				</label>
				<div class="col-xs-3 add-margin">
					<form:select path="financialDetails[0].budgetGroup" data-first-option="false" id="budgethead" class="form-control disablefield" required="required" >
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${budgetHeads}" itemValue="id" itemLabel="name" />
					</form:select>
					<form:errors path="financialDetails[0].budgetGroup"	cssClass="add-margin error-msg" />
				</div>
				<input type="hidden" id="budgetHeadValue" value="${abstractEstimate.financialDetails.get(0).budgetGroup.id }" />
		</div> 
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
					<spring:message code="lbl.scheme" />
				</label>
				<c:choose>
					<c:when test="${abstractEstimate.lineEstimateDetails != null}">
						<input type="hidden" id="schemeValue" value="${abstractEstimate.lineEstimateDetails.lineEstimate.scheme.id }" />
					</c:when>
					<c:otherwise>
						<input type="hidden" id="schemeValue" value="${abstractEstimate.financialDetails.get(0).scheme.id }" />
					</c:otherwise>
				</c:choose>
				<div class="col-sm-3 add-margin">
					<form:select path="financialDetails[0].scheme" data-first-option="false" id="scheme" class="form-control disablefield"  onchange="getSubSchemsBySchemeId(this.value);" >
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
					</form:select>
					<form:errors path="financialDetails[0].scheme"	cssClass="add-margin error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right">
					<spring:message code="lbl.subscheme" />
				</label>
				<c:choose>
					<c:when test="${abstractEstimate.lineEstimateDetails != null}">
						<input type="hidden" id="subSchemeValue" value="${abstractEstimate.lineEstimateDetails.lineEstimate.subScheme.id }" />
					</c:when>
					<c:otherwise>
						<input type="hidden" id="subSchemeValue" value="${abstractEstimate.financialDetails.get(0).subScheme.id }" />
					</c:otherwise>
				</c:choose>
				<div class="col-sm-3 add-margin">
					<form:select path="financialDetails[0].subScheme" data-first-option="false" id="subScheme" class="form-control disablefield">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
					</form:select>
				</div>
			</div>
		</div>
	</div>
