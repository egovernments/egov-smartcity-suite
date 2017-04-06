<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2017>  eGovernments Foundation
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

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		lbl.connection.details
	</div>
</div>

<div class="row add-border">
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.build.plan.apprvl.date" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.buildingPlanApprovalDate}"></c:out>
	</div>
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.plan.approval.no" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.buildingplanapprovalnumber}"
			default="N/A"></c:out>
	</div>
</div>

<div class="row add-border">
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.build.plan.permission.date" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.planPermissionDate}"></c:out>
	</div>
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.plan.submission.no" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.planPermissionNumber}"
			default="N/A"></c:out>
	</div>
</div>

<div class="row add-border">
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.service.type" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.serviceType}"></c:out>
	</div>
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.stakeholder.type" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.stakeHolder[0].stakeHolder}"
			default="N/A"></c:out>
	</div>
</div>

<div class="row add-border">
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.application.no" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.applicationNumber}"></c:out>
	</div>
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.application.date" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.applicationDate}"
			default="N/A"></c:out>
	</div>
</div>

<div class="row add-border">
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.old.appln.no" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.oldApplicationNumber}"></c:out>
	</div>
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.approval.date" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.approveDate}"
			default="N/A"></c:out>
	</div>
</div>
	
<div class="row add-border">
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.assessmt.no" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.assessmentNumber}"></c:out>
	</div>
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.occupancy" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.occupancy}"
			default="N/A"></c:out>
	</div>
</div>	

<div class="row add-border">
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.applicant.type" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.applicantType}"></c:out>
	</div>
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.source" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.source}"
			default="N/A"></c:out>
	</div>
</div>	
	
<div class="row add-border">
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.tapal.no" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.tapalNumber}"></c:out>
	</div>
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.government.type" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.governmentType}"
			default="N/A"></c:out>
	</div>
</div>
	
<div class="row add-border">
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.project.name" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.projectName}"></c:out>
	</div>
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.group.development" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.groupDevelopment}"
			default="N/A"></c:out>
	</div>
</div>

<div class="row add-border">
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.remarks" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.remarks}"></c:out>
	</div>
	<div class="col-sm-3 add-margin">
		<spring:message code="lbl.admission.fees" />
	</div>
	<div class="col-sm-3 add-margin view-content">
		<c:out value="${bpaApplication.admissionfeeAmount}"
			default="N/A"></c:out>
	</div>
</div>
			