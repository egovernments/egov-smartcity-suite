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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<div class="row">
	<div class="col-md-12">
		<div class="" data-collapsed="0">
			<c:if test="${not empty message}">
				<div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
			</c:if>
			<form:form id="searchEscalationTimeForm" method="post"
				class="form-horizontal form-groups-bordered"
				modelAttribute="escalation">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading ">
						<div class="panel-title">
							<strong><spring:message	code="lbl.escalationTime.heading.search" /></strong>
						</div>
					</div>
					<div class="panel-body custom-form">
						<div class="form-group">
							<label class="col-sm-3 control-label"><spring:message
									code="lbl.escalationTime.complaintType" /> </label>
							<div class="col-sm-6">

								<form:input id="com_type" path="complaintType.name" type="text"
									class="form-control typeahead is_valid_alphabet"
									placeholder="" autocomplete="off"  />
								<input type=hidden id="mode" value="${mode}">
								<form:hidden path="complaintType.id" id="complaintTypeId"
									value="${complaintType.id}" />
								<form:errors path="complaintType.id"
									cssClass="add-margin error-msg" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label"><spring:message
									code="lbl.escalationTime.designation" /> </label>
							<div class="col-sm-6">

								<form:input id="designation_type" path="designation.name" type="text"
									class="form-control typeahead is_valid_alphabet"
									placeholder="" autocomplete="off" />
									<form:hidden path="designation.id" id="designationId"
									value="${designation.id}" />
								<form:errors path="designation.id"
									cssClass="add-margin error-msg" />
							</div>
						</div>
						<div class="form-group">
							<div class="text-center">
								<button type="button" id="escalationTimeSearch"
									class="btn btn-primary">
									<spring:message code="lbl.escalationTime.button.search" />
								</button>
								<a href="javascript:void(0)" class="btn btn-default"
									onclick="self.close()"> <spring:message code="lbl.close" /></a>
							</div>
						</div>
					</div>
				</div>
				
			</form:form>
			<div class="row display-hide report-section">
				<div class="col-md-6 col-xs-6 table-header"><spring:message code="lbl.escalationtime.details"/></div>
				<div class="col-md-12">
					<table class="table table-bordered datatable" id="escalationTime-table">
						<thead>
							<th><spring:message code="lbl.escalationTime.complaintType" /></th>
							<th><spring:message code="lbl.escalationTime.designation" /></th>
							<th><spring:message code="lbl.escalationTime.noOfHours" /></th>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>

<link rel="stylesheet" href="<cdn:url  value='/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/js/app/escalationTimeview.js?rnd=${app_release_no}'/>"></script>
