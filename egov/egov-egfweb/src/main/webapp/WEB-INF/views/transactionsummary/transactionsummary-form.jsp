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


<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="/WEB-INF/tags/sitemesh-decorator.tld" prefix="decorator"%>
<%@ taglib uri="/WEB-INF/tags/sitemesh-page.tld" prefix="page"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<c:if test="${not empty message}">
					<div class="alert alert-success" role="alert">
						<spring:message code="${message}" />
					</div>
				</c:if>
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="title.transactionsummary" />
					</div>
				</div>
				<center>
					<span class="mandatory1">
						<div id="errors"></div>
					</span>
				</center>
				<div class="panel-body" id="mainTSForm">
					<div class="form-group">
						<form:input type="hidden" path="id" />
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.financialyear" /><span class="mandatory1">*</span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="financialyear.id" id="financialyear.id"
								cssClass="form-control mandatory"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${cFinancialYears}" itemValue="id"
									itemLabel="finYearRange" />
							</form:select>
							<form:errors path="financialyear" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.departmentid" /><span class="mandatory1">*</span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="departmentid.id" id="departmentid.id"
								cssClass="form-control mandatory"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${departments}" itemValue="id"
									itemLabel="name" />
							</form:select>
							<form:errors path="departmentid" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.fund" /><span class="mandatory1">*</span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="fund.id" id="fund.id"
								cssClass="form-control mandatory"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${funds}" itemValue="id" itemLabel="name" />
							</form:select>
							<form:errors path="fund" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.functionid" /><span class="mandatory1">*</span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="functionid.id" id="functionid.id"
								cssClass="form-control mandatory"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach var="function" items="${cFunctions}">
									<option value="${function.id}">
										${function.code} -  ${function.name}</option>
								</c:forEach>
							</form:select>
							<form:errors path="functionid" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">

						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.type" /><span class="mandatory1">*</span></label>
						<div class="col-sm-3 add-margin">
							<select name="type" id="type" class="form-control mandatory">
								<option value="">
									<spring:message code="lbl.select" />
								</option>
								<option value="A"><spring:message code="value.asset" /></option>
								<option value="L"><spring:message
										code="value.liability" /></option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.major" /></label>
						<div class="col-sm-3 add-margin">
							<select name="major" id="major" class="form-control">
							</select>
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.minor" /></label>
						<div class="col-sm-3 add-margin">
							<select name="minor" id="minor" class="form-control">
							</select>
						</div>
					</div>
					<div class="form-group" id="divProceed">
						<div class="text-center">
							<button type='button' class='btn btn-primary' id="buttonProceed">
								<spring:message code='lbl.proceed' />
							</button>
							<a href='javascript:void(0)' class='btn btn-default'
								onclick='self.close()'><spring:message code='lbl.close' /></a>
						</div>
					</div>
			</div>
		</div>
	</div>
</div>