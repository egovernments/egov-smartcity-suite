<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) 2016  eGovernments Foundation
  ~
  ~  The updated version of eGov suite of products as by eGovernments Foundation
  ~  is available at http://www.egovernments.org
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program. If not, see http://www.gnu.org/licenses/ or
  ~  http://www.gnu.org/licenses/gpl.html .
  ~
  ~  In addition to the terms of the GPL license to be adhered to in using this
  ~  program, the following additional terms are to be complied with:
  ~
  ~      1) All versions of this program, verbatim or modified must carry this
  ~         Legal Notice.
  ~
  ~      2) Any misrepresentation of the origin of the material is prohibited. It
  ~         is required that all modified versions of this material be marked in
  ~         reasonable ways as different from the original version.
  ~
  ~      3) This license does not grant any rights to any user of the program
  ~         with regards to rights under trademark law for use of the trade names
  ~         or trademarks of eGovernments Foundation.
  ~
  ~  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div class="row" id="page-content">
	<div class="col-md-12">
		<form:form mothod="post" class="form-horizontal form-groups-bordered"
			modelAttribute="mutationFeeDetails" id="mutationForm">
			<c:if test="${not empty message}">
				<div class="alert alert-success" role="alert">
					<spring:message code="${message}" />
				</div>
			</c:if>
			<spring:hasBindErrors name="mutationFeeDetails">
				<div class="alert alert-danger">
					<form:errors path="*" cssClass="error-msg add-margin" />
					<br />
				</div>
			</spring:hasBindErrors>
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code="lbl.hdr.addMutation" /></strong>
					</div>
				</div>
				<div class="panel-body custom-form">
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message
								code="lbl.fromVal" /><span class="mandatory"></span></label>
						<div class="col-sm-6">
							<form:input path="lowLimit" id="lowLimit" type="text"
								name="lowLimit" class="form-control low-width" placeholder=""
								required="required" autocomplete="off" />
							<form:errors path="lowLimit" cssClass="add-margin error-msg" />
						</div>
					</div>
					<div></div>
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message
								code="lbl.toVal" /></label>
						<div class="col-sm-6">
							<form:input path="highLimit" id="highLimit" type="text"
								class="form-control low-width" placeholder="ABOVE"
								autocomplete="off" onblur="return validate();" />

							<form:errors path="highLimit" cssClass="add-margin error-msg" />
							<div id="highlimiterror" class="error-msg display-hide">To
								Value should greater than from value</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message
								code="lbl.flatRate" /></label>
						<div class="col-sm-6">
							<form:input path="flatAmount" id="flatAmount" type="text"
								class="form-control low-width" placeholder="" autocomplete="off" />
							<form:errors path="flatAmount" cssClass="add-margin error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message
								code="lbl.percentRate" /></label>
						<div class="col-sm-6">
							<form:input path="percentage" id="percentage" type="text"
								class="form-control low-width" placeholder="" autocomplete="off" />
							<form:errors path="percentage" cssClass="add-margin error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message
								code="lbl.recVal" /></label>
						<div class="col-sm-6">
							<form:input path="recursiveFactor" id="recursiveFactor"
								type="text" class="form-control low-width" placeholder=""
								autocomplete="off" />
							<form:errors path="recursiveFactor"
								cssClass="add-margin error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message
								code="lbl.addRate" /></label>
						<div class="col-sm-6">
							<form:input path="recursiveAmount" id="recursiveAmount"
								type="text" class="form-control low-width" placeholder=""
								autocomplete="off" />
							<form:errors path="recursiveAmount"
								cssClass="add-margin error-msg" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label"> <spring:message
								code="lbl.effectiveFrom" /><span class="mandatory"></span></label>
						<div class="col-sm-6">
							<form:input path="fromDate" id="fromDate" type="text"
								class="form-control low-width datepicker" required="required"
								autocomplete="off" />
							<form:errors path="fromDate" cssClass="add-margin error-msg" />
							<div id="fromdate" class="col-sm-10"></div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message
								code="lbl.effectiveTo" /><span class="mandatory"></span></label>
						<div class="col-sm-6">
							<form:input path="toDate" id="toDate" type="text"
								class="form-control low-width datepicker" required="required"
								autocomplete="off" />
							<form:errors path="toDate" cssClass="add-margin error-msg" />
							<div id="todateerror" class="error-msg display-hide">To
								Date must be greater than From date</div>
						</div>
					</div>
				</div>
			</div>
			<div class="text-center">
				<button type="submit" class="btn btn-primary" id="submit">
					<spring:message code="lbl.submit" />
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal"
					onclick="window.close();">
					<spring:message code="lbl.close" />
				</button>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/mutationfee.js?rnd=${app_release_no}'/>"></script>

