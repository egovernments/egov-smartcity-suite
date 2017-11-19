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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<div class="row">
	<div class="col-md-12">
		<form:form id="categoryform" method="post"
			class="form-horizontal form-groups-bordered"
			modelAttribute="hoardingCategory" commandName="hoardingCategory">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading ">
					<div class="panel-title">
						<strong><spring:message code="title.category.details" /></strong>
					</div>
				</div>
				<div class="panel-body custom-form">
					<div class="form-group">

						<c:choose>
							<c:when test="${empty hoardingCategory.id}">
								<label class="col-sm-3 control-label text-right"><spring:message
										code="lbl.category.code" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input type="text"
										cssClass="form-control patternvalidation"
										data-pattern="alphanumericwithspacehyphenunderscore"
										maxlength="25" path="code" id="categorycode"
										required="required" />
									<form:errors path="code" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message
										code="lbl.category.name" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:textarea cssClass="form-control patternvalidation"
										path="name" id="classdesc" rows="4" cols="4"
										readonly="readonly" />
									<form:errors path="name" cssClass="error-msg" />

									<form:input type="hidden" cssClass="form-control" path="id" />
								</div>
							</c:when>
							<c:otherwise>
								<label class="col-sm-3 control-label text-right"><spring:message
										code="lbl.category.code" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input type="hidden" cssClass="form-control" path="id" />
									<form:input type="text"
										cssClass="form-control patternvalidation"
										data-pattern="alphanumericwithspacehyphenunderscore"
										maxlength="25" path="code" id="categorycode" readonly="true"
										required="required" />
									<form:errors path="code" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message
										code="lbl.category.name" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="name"
										id="classdesc" required="required" readonly="true" />

									<form:errors path="name" cssClass="error-msg" />
								</div>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.category.active" /></label>
						<div class="col-sm-1 add-margin">
							<form:checkbox path="active" id="classactive"
								cssClass="form-control" />
							<form:errors path="active" cssClass="error-msg" />
						</div>

						<label class="col-sm-4 control-label text-right"><spring:message
								code="lbl.property.id" /></label>
						<div class="col-sm-1 add-margin">
							<form:checkbox path="propertyMandatory"
								id="classPropertyMandatory" cssClass="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="submit" class="btn btn-primary">
						<spring:message code="lbl.submit" />
					</button>
					<c:if test="${empty hoardingCategory.id}">
						<button type="reset" class="btn btn-default">
							<spring:message code="lbl.reset" />
						</button>
					</c:if>
					<a href="javascript:void(0)" class="btn btn-default"
						onclick="self.close()"><spring:message code="lbl.close" /></a>
				</div>
			</div>
		</form:form>
	</div>
</div>



