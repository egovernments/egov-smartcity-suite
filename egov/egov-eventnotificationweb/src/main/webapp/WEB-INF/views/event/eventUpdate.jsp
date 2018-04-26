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
<link rel="stylesheet" href="<cdn:url  value='/resources/global/css/egov/map-autocomplete.css?rnd=${app_release_no}' context='/egi'/>">
<form:form method="post" action="" modelAttribute="event" id="updateEventform" cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data" onsubmit="return checkcreateform()">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title"><spring:message code="lbl.event.update" /></div>
				</div>
				<input type="hidden" id="mode" path="" value="${mode}" />
				<input type="hidden" id="id" name="id" value="${event.id}" />
				<input type="hidden" id="eventTypeHid" value="${event.eventType}" />
				<input type="hidden" id="startHHHid" value="${event.eventDetails.startHH}" />
				<input type="hidden" id="startMMHid" value="${event.eventDetails.startMM}" />
				<input type="hidden" id="endHHHid" value="${event.eventDetails.endHH}" />
				<input type="hidden" id="endMMHid" value="${event.eventDetails.endMM}" />
				<input type="hidden" id="ispaidHid" value="${event.ispaid}" />
				<input type="hidden" id="statusHid" value="${event.status}" />
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
						<div class="col-sm-10 add-margin">
							<form:input path="name" id="name" name="name" 
								class="form-control text-left patternvalidation" maxlength="100" required="required" value="${event.name}"/>
							<form:errors path="name" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.description" />:<span class="mandatory"></span></label>
						<div class="col-sm-10 add-margin">
							<form:textarea path="description" id="description" name="description"
								class="form-control text-left patternvalidation"
								maxlength="200" required="required" value="${event.description}"/>
							<form:errors path="description" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.message" />:<span class="mandatory"></span></label>
						<div class="col-sm-10 add-margin">
							<form:textarea path="message" id="message" name="message"
								class="form-control text-left patternvalidation"
								maxlength="200" required="required" value="${event.message}"/>
							<form:errors path="message" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.startdate" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin text-center">
							<fmt:formatDate pattern="dd/MM/yyyy" value="${event.eventDetails.startDt}" var="startDate" />
							<form:input path="eventDetails.startDt" id="startDt" name="eventDetails.startDt" 
								class="form-control datepicker" title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}" 
								data-inputmask="'mask': 'd/m/y'" required="required" data-date-start-date="0d" value="<c:out value='${startDate}' />"/>
							<form:errors path="eventDetails.startDt" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.starttime" />:<span class="mandatory"></span></label>
						<div class="col-sm-2 add-margin text-center">
							<form:select path="eventDetails.startHH" id="startHH" name="eventDetails.startHH"
								cssClass="form-control" cssErrorClass="form-control error"	required="required">
								<form:option value=""><spring:message code="lbl.hours" /></form:option>
								<form:options items="${hourList}" />
							</form:select>
							<form:errors path="eventDetails.startHH" cssClass="error-msg" />
						</div>
						<label class="col-sm-1 control-label text-right">:</label>
						<div class="col-sm-2 add-margin text-center">
							<form:select path="eventDetails.startMM" id="startMM" name="eventDetails.startMM"
									cssClass="form-control" cssErrorClass="form-control error"	required="required">
									<form:option value=""><spring:message code="lbl.minutes" /></form:option>
									<form:options items="${minuteList}"/>
								</form:select>
								<form:errors path="eventDetails.startMM" cssClass="error-msg" />
						</div>
					</div>
				<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.enddate" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin text-center">
							<fmt:formatDate pattern="dd/MM/yyyy" value="${event.eventDetails.endDt}" var="endDate" />
							<form:input path="eventDetails.endDt" id="endDt" name="eventDetails.endDt" 
								class="form-control datepicker" title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}" 
								data-inputmask="'mask': 'd/m/y'" required="required" data-date-start-date="0d" value="<c:out value='${endDate}' />"/>
							<form:errors path="eventDetails.endDt" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.endtime" />:<span class="mandatory"></span></label>
						<div class="col-sm-2 add-margin text-center">
							<form:select path="eventDetails.endHH" id="endHH" name="eventDetails.endHH"
								cssClass="form-control" cssErrorClass="form-control error"	required="required">
								<form:option value=""><spring:message code="lbl.hours" /></form:option>
								<form:options items="${hourList}" />
							</form:select>
							<form:errors path="eventDetails.endHH" cssClass="error-msg" />
						</div>
						<label class="col-sm-1 control-label text-right">:</label>
						<div class="col-sm-2 add-margin text-center">
							<form:select path="eventDetails.endMM" id="endMM" name="eventDetails.endMM"
									cssClass="form-control" cssErrorClass="form-control error"	required="required">
									<form:option value=""><spring:message code="lbl.minutes" /></form:option>
									<form:options items="${minuteList}"/>
								</form:select>
								<form:errors path="eventDetails.endMM" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.host" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="eventhost" id="eventhost" name="eventhost" 
								class="form-control text-left patternvalidation" maxlength="20" required="required" value="${event.eventhost}"/>
							<form:errors path="eventhost" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.location" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<div class="input-group">
								<form:input path="eventlocation" id="eventlocation" name="eventlocation" class="form-control text-left patternvalidation" maxlength="100" required="required" value="${event.eventlocation}"/>
								<span class="input-group-addon map-class btn-secondary" title="See on map" onclick="jQuery('#modal-6').modal('show', {backdrop: 'static'});"><i class="fa fa-map-marker specific"></i></span>
								<form:hidden path="eventDetails.crossHierarchyId" id="crosshierarchyId"/>
	                            <form:hidden path="eventDetails.lat" id="lat"/>
	                            <form:hidden path="eventDetails.lng" id="lng"/>
								<form:errors path="eventlocation" cssClass="error-msg" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.address" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="address" id="address" name="address" 
								class="form-control text-left patternvalidation" maxlength="256" required="required" value="${event.address}"/>
							<form:errors path="address" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.Wallpaper" />:</label>
						<div class="col-sm-3 add-margin">
							<input type="file" id="file" name="eventDetails.file">
							<label>Note: Minimum image dimension is 100*100 and it support jpg, jpeg, bmp, gif and png file type. </label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.status" />:</label>
						<div class="col-sm-3 add-margin">
							<form:select path="status" id="status" name="status"
								cssClass="form-control" cssErrorClass="form-control error"	required="required">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<form:options items="${eventStatusList}" />
							</form:select>
							<form:errors path="status" cssClass="error-msg" />
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
								class="form-control text-left patternvalidation" maxlength="20" value="${event.cost}"/>
							<form:errors path="cost" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"></label>
						<div class="col-sm-3 add-margin"></div>
					</div>
					<div class="form-group">
						<label id="urllabel" class="col-sm-2 control-label text-right" style="display:none;"><spring:message code="lbl.event.url" />:<span class="mandatory"></span></label>
						<div id="urldiv" class="col-sm-10 add-margin" style="display:none;">
							<form:input path="url" id="url" name="url"
								class="form-control text-left patternvalidation"
								maxlength="200" required="required" value="${event.url}"/>
							<form:errors path="url" cssClass="error-msg" />
						</div>
					</div>
			</div>
		</div>
	</div>
	<div class="form-group">
		<div class="text-center">
			<button type='submit' class='btn btn-primary' id="buttonSubmit">
				<spring:message code='lbl.update.button' />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
		</div>
	</div>
</form:form>
<div class="modal fade" id="modal-6">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <div class="panel panel-primary" data-collapsed="0"><!-- to apply shadow add class "panel-shadow" -->
                            <!-- panel head -->
                            <div class="panel-heading">
                                <div class="panel-title"><spring:message code="lbl.complaintLoc"/></div>
                            </div>

                            <!-- panel body -->
                            <div class="panel-body no-padding">
                                <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=${sessionScope.googleApiKey}&libraries=places"></script>
                                <script type="text/javascript" src="<cdn:url  value='/resources/global/js/geolocation/geolocationmarker-compiled.js' context='/egi'/>"></script>
                                <div id="normal" class="img-prop"></div>
                                <input id="pac-input" class="controls" type="text" placeholder="Enter a location">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-info btn-save-location" data-dismiss="modal"><spring:message code="lbl.ok"/></button>
                <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="lbl.close"/></button>
            </div>
        </div>
    </div>
</div>
<script>
	$('#buttonSubmit').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script><link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>" />
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />

<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script	type="text/javascript" src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/exif.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/js/app/eventUpdate.js?rnd=${app_release_no}'/>"></script>
