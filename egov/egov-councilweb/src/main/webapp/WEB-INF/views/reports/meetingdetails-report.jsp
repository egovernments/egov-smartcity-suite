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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<form:form role="form" action="" modelAttribute="searchRequest"
	id="councilPreamblesearchform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	
	<div class="row">
			<div class="col-md-12">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">Search Meeting</div>
					</div>
					<div class="panel-body">
					
					<div class="form-group">
							<label class="col-sm-2 control-label text-right"><spring:message
									code="lbl.meeting.type" /> </label>
							<div class="col-sm-3 add-margin">
								<form:select path="committeeType" id="committeeType"
									cssClass="form-control" cssErrorClass="form-control error">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<form:options items="${committeeType}" itemValue="name"
										itemLabel="name" />
								</form:select>
								<form:errors path="committeeType" cssClass="error-msg" />
							</div>
							<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.meeting.number" /> </label>
							<div class="col-sm-3 add-margin">
								<form:input type="text" cssClass="form-control"
									path="meetingNumber" id="meetingNumber"/>
								<form:errors path="meetingNumber" cssClass="error-msg" />
							</div>
					</div>
						<div class="form-group">
							 <label class="col-sm-2 control-label text-right"><spring:message
									code="lbl.fromdate" /> </label>
							<div class="col-sm-3 add-margin">
									<form:input path="from"
										class="form-control text-left patternvalidation datepicker"
										 data-date-end-date="0d"  />
									<form:errors path="from" cssClass="error-msg" />
							</div> 
							
							<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.todate" /> </label>
							<div class="col-sm-3 add-margin">
								<form:input type="text" cssClass="form-control datepicker"
									path="to" id="meetingDate"/>
								<form:errors path="to" cssClass="error-msg" />
							</div>
						</div>
						
						<input type="hidden" id="mode" name="mode" value="${mode}" />
					</div>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="text-center">
				<button type='button' class='btn btn-primary' id="btnsearch">
					<spring:message code='lbl.search' />
				</button>
				<button type="reset" class="btn btn-danger"><spring:message code="lbl.reset"/></button>
				<a href='javascript:void(0)' class='btn btn-default'
					onclick='self.close()'><spring:message code='lbl.close' /></a>
			</div>
		</div>
	
</form:form>
<div class="row display-hide report-section">
	<div class="col-md-12 table-header text-left"><spring:message code="lbl.title.meeting.details.report" /></div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="meetingDetailsTable">
			<thead>
				<tr>
					<th><spring:message code="lbl.serial.no" /></th>
					<th><spring:message code="lbl.meeting.type" /></th>
					<th><spring:message code="lbl.meeting.number" /></th>
					<th><spring:message code="lbl.meeting.date" /></th>
					<th><spring:message code="lbl.meeting.place" /></th>
					<th><spring:message code="lbl.meeting.time" /></th>
					<th><spring:message code="lbl.total.preambles" /></th>
					<th><spring:message code="lbl.approved.preambles" /></th>
					<th><spring:message code="lbl.adjourned.preambles" /></th>
					<th><spring:message code="lbl.rejected.preambles" /></th>
					<th><spring:message code="lbl.total.members" /></th>
					<th><spring:message code="lbl.present" /></th>
					<th><spring:message code="lbl.absent" /></th>
				</tr>
			</thead>
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
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/datetime-moment.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/app/js/councilmeeting-details-report.js?rnd=${app_release_no}'/>"></script>
</script>
