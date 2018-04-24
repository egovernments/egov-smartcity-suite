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
<form:form method="post" action="" modelAttribute="event" id="createEventform" cssClass="form-horizontal form-groups-bordered"
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
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.eventType" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="eventType" id="eventType" name="eventType"
								cssClass="form-control" cssErrorClass="form-control error"	required="required">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<form:options items="${eventList}" />
							</form:select>
							<form:errors path="eventType" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"></label>
						<div class="col-sm-3 add-margin"></div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.name" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="name" id="name" name="name" 
								class="form-control text-left patternvalidation" maxlength="100" required="required"/>
							<form:errors path="name" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.description" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="description" id="description" name="description"
								class="form-control text-left patternvalidation"
								maxlength="200" required="required"/>
							<form:errors path="description" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.startdate" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin text-center">
							<form:input path="startDate" id="startDate" name="startDate" 
								class="form-control datepicker" title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}" 
								data-inputmask="'mask': 'd/m/y'" required="required" data-date-start-date="0d"/>
							<form:errors path="startDate" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.starttime" />:<span class="mandatory"></span></label>
						<div class="col-sm-2 add-margin text-center">
							<form:select path="startHH" id="startHH" name="startHH"
								cssClass="form-control" cssErrorClass="form-control error"	required="required">
								<form:option value=""><spring:message code="lbl.hours" /></form:option>
								<form:options items="${hourList}" />
							</form:select>
							<form:errors path="startHH" cssClass="error-msg" />
						</div>
						<label class="col-sm-1 control-label text-right">:</label>
						<div class="col-sm-2 add-margin text-center">
							<form:select path="startMM" id="startMM" name="startMM"
									cssClass="form-control" cssErrorClass="form-control error"	required="required">
									<form:option value=""><spring:message code="lbl.minutes" /></form:option>
									<form:options items="${minuteList}"/>
								</form:select>
								<form:errors path="startMM" cssClass="error-msg" />
						</div>
					</div>
				<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.enddate" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin text-center">
							<form:input path="endDate" id="endDate" name="endDate" 
								class="form-control datepicker" title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}" 
								data-inputmask="'mask': 'd/m/y'" required="required" data-date-start-date="0d"/>
							<form:errors path="endDate" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.endtime" />:<span class="mandatory"></span></label>
						<div class="col-sm-2 add-margin text-center">
							<form:select path="endHH" id="endHH" name="endHH"
								cssClass="form-control" cssErrorClass="form-control error"	required="required">
								<form:option value=""><spring:message code="lbl.hours" /></form:option>
								<form:options items="${hourList}" />
							</form:select>
							<form:errors path="endHH" cssClass="error-msg" />
						</div>
						<label class="col-sm-1 control-label text-right">:</label>
						<div class="col-sm-2 add-margin text-center">
							<form:select path="endMM" id="endMM" name="endMM"
									cssClass="form-control" cssErrorClass="form-control error"	required="required">
									<form:option value=""><spring:message code="lbl.minutes" /></form:option>
									<form:options items="${minuteList}"/>
								</form:select>
								<form:errors path="endMM" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.host" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="eventhost" id="eventhost" name="eventhost" 
								class="form-control text-left patternvalidation" maxlength="20" required="required"/>
							<form:errors path="eventhost" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.location" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="eventlocation" id="eventlocation" name="eventlocation"
								class="form-control text-left patternvalidation" maxlength="20" required="required"/>
							<form:errors path="eventlocation" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.address" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="address" id="address" name="address" 
								class="form-control text-left patternvalidation" maxlength="256" required="required"/>
							<form:errors path="address" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.Wallpaper" />:</label>
						<div class="col-sm-3 add-margin">
							<input type="file" id="file" name="file">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.ispaid" />:</label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="ispaid" id="ispaid" name="ispaid"/>
							<form:errors path="ispaid" cssClass="error-msg" />
						</div>
						<label id="costLabel" class="col-sm-2 control-label text-right" style="display:none;"><spring:message code="lbl.event.cost" />:</label>
						<div class="col-sm-3 add-margin" style="display:none;" id="costDiv">
							<form:input path="cost" id="cost" name="cost" 
								class="form-control text-left patternvalidation" maxlength="20" />
							<form:errors path="cost" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"></label>
						<div class="col-sm-3 add-margin"></div>
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
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>" />
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script	type="text/javascript" src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/js/app/eventNewHelper.js?rnd=${app_release_no}'/>"></script>
