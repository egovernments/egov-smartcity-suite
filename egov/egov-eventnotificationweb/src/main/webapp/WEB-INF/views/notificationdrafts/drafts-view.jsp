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

<div class="row">
	<div class="col-md-12">
		<form:form modelAttribute="NotificationDraft" name="searchDraftForm"
			id="searchDraftForm" class="form-horizontal form-groups-bordered">
			<div class="panel panel-primary" data-collapsed="0">

				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.draft.search" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.draft.notificationtype" />:</label>
					<div class="col-sm-3 add-margin">
						<select id="type" name="type" class="form-control">
							<option value=""><spring:message code="lbl.select" /></option>
							<c:forEach var="item" items="${draftList}">
								<option value="${item.id}">${item.name}</option>
							</c:forEach>
						</select>
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.draft.name" />:</label>
					<div class="col-sm-3 add-margin">
						<input id="name" name="name" class="form-control text-left"
							maxlength="100" />
					</div>
				</div>

				<div class="row">
					<div class="text-center">
						<a href="javascript:void(0);" id="draftSearch"
							class="btn btn-primary"><spring:message code='lbl.search' /></a>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>
<div class="row">
	<div class="col-md-12 table-header text-left">
		<spring:message code="title.draft.view.all" />
		<button type='button' class='btn btn-primary' id="buttonSubmit"
			style="float: right;">
			<spring:message code='lbl.add' />
		</button>
	</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="draftViewTable">
			<thead>
				<tr>
					<th><spring:message code="lbl.draft.slno" /></th>
					<th><spring:message code="lbl.draft.name" /></th>
					<th><spring:message code="lbl.draft.type" /></th>
					<th><spring:message code="lbl.draft.module" /></th>
					<th><spring:message code="lbl.draft.category" /></th>
					<th><spring:message code="lbl.draft.notificationMessage" /></th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty NotificationDrafts}">
					<c:forEach var="listVar" items="${NotificationDrafts}">
						<tr>
							<td><c:out value="${listVar.id}" /></td>
							<td><c:out value="${listVar.name}" /></td>
							<td><c:out value="${listVar.draftType.name}" /></td>
							<td><c:out value="${listVar.category.name}" /></td>
							<td><c:out value="${listVar.subCategory.name}" /></td>
							<td><c:out value="${listVar.message}" /></td>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${empty NotificationDrafts}">
					<tr class="odd">
						<td colspan="13" class="dataTables_empty" valign="top"><spring:message code="lbl.norecords" /></td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</div>
<script>
	$('#btnsearch').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/draft-view.js?rnd=${app_release_no}'/>"></script>
