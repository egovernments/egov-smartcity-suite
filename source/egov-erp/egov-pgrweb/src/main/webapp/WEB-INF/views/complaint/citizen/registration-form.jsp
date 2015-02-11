<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css'/>">
<div class="row">
	<div class="col-md-12">
		<form:form role="form" action="register" modelAttribute="complaint" commandName="complaint" id="complaintform" cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code="lbl.hdr.complaintInfo"/></strong>
					</div>
				</div> 
				<div class="panel-body custom-form">
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code="lbl.complaintType"/><small><i class="entypo-star error-msg"></i></small></label>
						<div class="col-sm-6">
							<form:input path="complaintType" id="complaintType" cssClass="form-control typeahead is_valid_alphabet" cssErrorClass="form-control error" placeholder="" autocomplete="off" required="required"/>
                            <form:errors path="complaintType" cssClass="add-margin error-msg"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code="lbl.compDetails"/> <small><i class="entypo-star error-msg"></i></small></label>
						<div class="col-sm-6">
							<form:textarea path="details" id="doc" placeholder="" maxlength="400" cssClass="form-control autogrow" required="required"/>
							<div class="text-right"><small id="rcc"><spring:message code="lbl.remainingChars"/> : 400</small></div>
							<form:errors path="details" cssClass="add-margin error-msg"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code="lbl.uploadPhotoVid"/></label>
						<div class="col-sm-6" >
							<div id="file1block" class="add-margin"><input name="files" type="file" id="file1" class="filechange inline btn" style="display:none;"/><a href="#" id="triggerFile1" class="btn btn-secondary">Browse</a><span class="add-padding" id="filename1"></span></div>
							<div id="file2block" class="add-margin display-hide"><input name="files" type="file" id="file2" class="filechange inline btn" style="display:none;"/><a href="#" id="triggerFile2" class="btn btn-secondary">Browse</a><span class="add-padding" id="filename2"></span></div>
						    <div id="file3block" class="add-margin display-hide"><input name="files" type="file" id="file3" class="filechange inline btn" style="display:none;"/><a href="#" id="triggerFile3" class="btn btn-secondary">Browse</a><span class="add-padding" id="filename3"></span></div>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code="lbl.complaintLoc"/><small><i class="entypo-star error-msg"></i></small></label>
						<div class="col-sm-6">
							<div class="input-group">
								<input id="location" type="text" class="form-control low-width" placeholder="" autocomplete="off" />
								<span class="input-group-addon map-class btn-secondary" title="See on map" onclick="jQuery('#modal-6').modal('show', {backdrop: 'static'});"><i class="entypo-globe"></i></span>
								<form:hidden path="location" id="location" value="0"/>
				   				<form:hidden path="lat" id="lat"/>
                   				<form:hidden path="lng" id="lng"/>
							</div>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code="lbl.landmark"/></label>
						<div class="col-sm-6">
							<form:textarea path="landmarkDetails" class="form-control" id="landmarkDetails" placeholder="" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="submit" class="btn btn-primary"><spring:message code="lbl.submit"/></button>
			        <button type="reset" class="btn btn-default"><spring:message code="lbl.cancel"/></button>
				</div>
			</div>
		</form:form>
	</div>
</div>


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
								<script type="text/javascript" src="//maps.google.com/maps/api/js?sensor=true"></script>
								<div id="normal" class="img-prop"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="modal-footer">
				<button type="button" class="btn btn-info" data-dismiss="modal"><spring:message code="lbl.ok"/></button>
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="lbl.close"/></button>
			</div>
		</div>
	</div>
</div>
<script src="<c:url value='/resources/js/app/fileuploadndmaps.js'/>"></script>
<script src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js'/>"></script>

<script src="<c:url value='/resources/js/app/complaint.js'/>"></script>
