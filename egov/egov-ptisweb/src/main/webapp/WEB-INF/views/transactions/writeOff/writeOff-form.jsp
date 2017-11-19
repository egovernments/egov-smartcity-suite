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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<style>
body
{
  font-family:regular !important;
  font-size:14px;
}
</style>
<script type="text/javascript"
	src="<cdn:url value='/resources/javascript/validations.js'/>"></script>
<form:form id="WriteOff" method="post"
	class="form-horizontal form-groups-bordered" modelAttribute="property">
	<div class="page-container" id="page-container">
		<div class="main-content">
			<jsp:include page="../../common/ownerDetailsView.jsp"></jsp:include>
			<jsp:include page="../../common/propertyAddressDetailsView.jsp"></jsp:include>
			<jsp:include page="../../common/propertyDetailsView.jsp"></jsp:include>
			<jsp:include page="../../common/taxDetailsView.jsp"></jsp:include>
			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0"
						style="text-align: left">
						<div class="panel-heading">
							<div class="panel-title">
								<spring:message code="lbl.council.details" />
							</div>
						</div>
						<div class="panel-body">
							<c:if test="${isCorporation}">
								<div class="row add-border">
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.committee.date" /><span class="mandatory"></span>
									</div>
									<div class="col-xs-3 add-margin view-content">
										<form:input  path="" name="committeeDate" class="form-control datepicker" data-date-end-date="0d"
											id="committeeDate" data-inputmask="'mask': 'd/m/y'" required="required" />
									</div>
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.committee.regNo" /><span class="mandatory"></span>
									</div>
									<div class="col-xs-3 add-margin view-content">
										<form:input path="" name="committeeRegNo" id="committeeRegNo" maxlength="16" cssClass="form-control" /> 
									</div>
								</div>
							</c:if>
							<c:if test="${!isCorporation}">
								<div class="row add-border">
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.council.date" /><span class="mandatory"></span>
									</div>
									<div class="col-xs-3 add-margin view-content">
										<form:input  path="" name="committeeDate" class="form-control datepicker" data-date-end-date="0d"
											id="committeeDate" data-inputmask="'mask': 'd/m/y'" required="required" />
									</div>
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.council.regNo" /><span class="mandatory"></span>
									</div>
									<div class="col-xs-3 add-margin view-content">
										<form:input path="" name="committeeRegNo" id="committeeRegNo" maxlength="16" cssClass="form-control" />
									</div>
								</div>
							</c:if>
							<div class="row add-border">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.endInstallment.to.writeoff" /><span class="mandatory"></span>
								</div>
								<div class="col-xs-3 add-margin view-content">
									<form:select path="" id="installments" cssClass="form-control" >
										<form:option value="">
											<spring:message code="lbl.select" />
										</form:option>
										<form:options items="${installments}" itemLabel="description" itemValue="id"/>
									</form:select>
								</div>
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.reason.writeoff" /><span class="mandatory"></span>
								</div>
								<div class="col-xs-3 add-margin view-content">
									<form:select path="" id="writeOffReason" cssClass="form-control" onchange="toggleOtherReason();">
										<form:option value="">
											<spring:message code="lbl.select" />
										</form:option>
										<form:options items="${writeOffReasons}"/>
									</form:select>
								</div>
							</div>
							<div class="row add-border" id="otherReasonDiv">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.other.reason" /><span class="mandatory"></span>
								</div>
								<div class="col-xs-3 add-margin view-content">
									<form:input path="" name="otherReason" id="otherReason" size="256" cssClass="form-control"/>
								</div>
								<div class="col-xs-3 add-margin">
									&nbsp;
								</div>
								<div class="col-xs-3 add-margin view-content">
									&nbsp;
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<jsp:include page="../../common/commonWorkflowMatrix.jsp" />
			<jsp:include page="../../common/commonWorkflowMatrix-button.jsp" />
		</div>
	</div>
</form:form>
<script src="<cdn:url value='/resources/js/app/writeoff.js'/>"></script>
