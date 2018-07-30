<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
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
<%@ include file="/includes/taglibs.jsp"%>

<form:form method="post" action=""
	class="form-horizontal form-groups-bordered" modelAttribute="drafts"
	id="draftForm">
	<input type="hidden" name="mode" value="${mode}" />
	<input type="hidden" name="draftId" id="draftId"
		value="${notificationDraft.id}" />
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-body custom-form ">
					<c:if test="${not empty message}">
						<div role="alert">
							<spring:message code="${message}" />
						</div>
					</c:if>
					<div class="panel-heading">
						<div class="panel-title">
							<spring:message code="title.draft.view" />
						</div>
					</div>
					<div class="panel-body">
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.draft.name" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${notificationDraft.name}" />
							</div>

							<div class="col-xs-3 add-margin"></div>
							<div class="col-xs-3 add-margin view-content"></div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.draft.type" />
							</div>
							<div class="col-xs-9 add-margin view-content">
								<c:out value="${notificationDraft.draftType.name}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.draft.module" />
							</div>
							<div class="col-xs-9 add-margin view-content">
								<c:out value="${notificationDraft.category.name}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.draft.category" />
							</div>
							<div class="col-xs-9 add-margin view-content">
								<c:out value="${notificationDraft.subCategory.name}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.draft.notificationMessage" />
							</div>
							<div class="col-xs-9 add-margin view-content">
								<c:out value="${notificationDraft.message}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.draft.url" />
							</div>
							<div class="col-xs-9 add-margin view-content">
								<c:choose>
									<c:when test="${empty notificationDraft.url}">
										NA
									</c:when>
									<c:otherwise>
										<c:out value="${notificationDraft.url}" />
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.draft.method" />
							</div>
							<div class="col-xs-9 add-margin view-content">
								<c:choose>
									<c:when test="${empty notificationDraft.method}">
										NA
									</c:when>
									<c:otherwise>
										<c:out value="${notificationDraft.method}" />
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="text-center">
					<button type='button' class='btn btn-primary' id="buttonSubmit">
						<spring:message code='lbl.edit' />
					</button>
					<a href="javascript:void(0)" class="btn btn-default"
						id='buttonClose'><spring:message code="lbl.close" /></a>
				</div>
			</div>
		</div>
	</div>
</form:form>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>" />
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/drafts-view-individual.js?rnd=${app_release_no}' />"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/window-reload-and-close.js?rnd=${app_release_no}' />"></script>
