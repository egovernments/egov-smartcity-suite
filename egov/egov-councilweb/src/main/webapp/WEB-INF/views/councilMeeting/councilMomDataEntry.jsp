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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<form:form role="form" action="savedataentry"
	modelAttribute="MeetingMOM" id="councilMeetingform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<%@ include file="councilmeeting-dataentry.jsp"%>

	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading custom_form_panel_heading">
			<div class="panel-title">Agenda Details</div>
		</div>
		<div class="panel-body">
			<div class="form-group">
				<label class="col-md-2 col-sm-2 control-label text-right"><spring:message
						code="lbl.agendaNumber" /></label>
				<div class="col-md-2 col-sm-2">
					<form:input path="agenda.agendaNumber" id="meetingDate" type="text"
						class="form-control text-left patternvalidation" maxlength="5"
						placeholder="" autocomplete="off" />
					<form:errors path="agenda.agendaNumber" cssClass="error-msg" />
				</div>
			</div>
			<div>
				<table id="preambleTable" class="table table-bordered">
					<thead>
						<tr>
							<th width="5%" align="center"><spring:message
									code="lbl.serial.no" /><span class="mandatory"></span></th>
							<th width="10%"><spring:message code="lbl.department" /><span
								class="mandatory"></span></th>
							<th width="10%"><spring:message code="lbl.preamble.number" /><span
								class="mandatory"></span></th>
							<th width="20%"><spring:message code="lbl.gistofpreamble" /><span
								class="mandatory"></span></th>
							<th width="10%"><spring:message code="lbl.resolutionNumber" /><span
								class="mandatory"></span></th>
							<th width="20%"><spring:message code="lbl.comments" /><span
								class="mandatory"></span></th>
							<th width="10%"><spring:message code="lbl.amount" /></th>
							<th width="10%"><spring:message code="lbl.status" /><span
								class="mandatory"></span></th>
							<th width="5%"><spring:message code="lbl.action" /></th>

						</tr>
					</thead>
					<tbody data-existing-len="${fn:length(meetingMOMs)}">
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="form-group">
		<div class="text-center">
			<button type="button" id="add-preamble" class='btn btn-primary'>
				<spring:message code='lbl.addpreamble' />
			</button>
			<button type='submit' class='btn btn-primary' id="buttonSubmit">
				<spring:message code='lbl.save' />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
		</div>
	</div>
	
	<div class="modal fade" id="textarea-modal" role="dialog">
		<div class="modal-dialog modal-lg">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="textarea-header"></h4>
				</div>
				<div class="modal-body">
					<textarea class="form-control textarea-content-of-modal"
						id="textarea-updatedcontent" rows="10"></textarea>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary"
						id="textarea-btnupdate" data-dismiss="modal">Update</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</form:form>

<script
	src="<cdn:url value='/resources/app/js/councilMoMDataentry.js?rnd=${app_release_no}'/>"></script>
<script
	src="<cdn:url value='/resources/app/js/showMoreorLessContent.js?rnd=${app_release_no}'/>"></script>


<style>
.morecontent span {
	display: none;
}
</style>