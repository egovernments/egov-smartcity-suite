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
<%@ include file="/includes/taglibs.jsp"%>
<form:form role="form" action="create" id="createEventform" cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title"><spring:message code="title.event.new" /></div>
				</div>
				<form:hidden id="mode" path="" value="${mode}" />
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.name" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input path="eventName" id="eventName" 
								class="form-control text-left patternvalidation" maxlength="20" />
							<form:errors path="eventName" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.description" />:</label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="eventDescription" id="eventDescription"
								class="form-control text-left patternvalidation"
								data-pattern="number" maxlength="256" />
							<form:errors path="eventDescription" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.startdate" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input path="startDate" id="startDate" 
								class="form-control text-left patternvalidation" maxlength="20" />
							<form:errors path="startDate" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.starttime" />:</label>
						<div class="col-sm-3 add-margin">
							<form:select path="hh" id="hh"
								cssClass="form-control" cssErrorClass="form-control error"	required="required">
								<form:option value=""><spring:message code="lbl.hours" /></form:option>
								<form:options items="${hourList}" itemValue="id" id="courtTypeDropdown" itemLabel="petitionType" />
							</form:select>
							<form:errors path="hh" cssClass="error-msg" />
							<label class="col-sm-2 control-label text-right">:</label>
							<div class="col-sm-3 add-margin">
							<form:select path="mm" id="mm"
								cssClass="form-control" cssErrorClass="form-control error"	required="required">
								<form:option value=""><spring:message code="lbl.minutes" /></form:option>
								<form:options items="${minuteList}" id="mmDropdown"/>
							</form:select>
							<form:errors path="mm" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.enddate" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input path="startDate" id="startDate" 
								class="form-control text-left patternvalidation" maxlength="20" />
							<form:errors path="startDate" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.endtime" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input path="startTime" id="startTime"
								class="form-control text-left patternvalidation" maxlength="20" />
							<form:errors path="startTime" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.host" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input path="eventHost" id="eventHost" 
								class="form-control text-left patternvalidation" maxlength="20" />
							<form:errors path="eventHost" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.location" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input path="eventLocation" id="eventLocation"
								class="form-control text-left patternvalidation" maxlength="20" />
							<form:errors path="eventLocation" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.address" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input path="eventAddress" id="eventAddress" 
								class="form-control text-left patternvalidation" maxlength="256" />
							<form:errors path="eventAddress" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.ispaid" />:</label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="isPaid" />
							<form:errors path="isPaid" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.cost" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input path="cost" id="cost" 
								class="form-control text-left patternvalidation" maxlength="20" />
							<form:errors path="cost" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"></label>
						<div class="col-sm-3 add-margin"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="form-group">
		<div class="text-center">
			<button type='submit' class='btn btn-primary' id="buttonSubmit">
				<spring:message code='lbl.create' />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
		</div>
	</div>
</form:form>
<script>
	$('#buttonSubmit').click(function(e) {
		if(!checkPanNumber())
			return false;
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>" />
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
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
	src="<cdn:url value='/resources/js/app/eventNewHelper.js?rnd=${app_release_no}'/>"></script>
