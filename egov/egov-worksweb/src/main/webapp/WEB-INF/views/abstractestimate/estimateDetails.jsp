
<link rel="stylesheet" href="<c:url value='/resources/global/css/egov/map-autocomplete.css' context='/egi'/>">
	  <div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.department" />
			</label>
			<div class="col-sm-3 add-margin">
				<form:select path="executingDepartment" data-first-option="false" id="executingDepartment" class="form-control disablefield" required="required">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${departments}" itemValue="id"	itemLabel="name" />
				</form:select>
				<form:errors path="executingDepartment" cssClass="add-margin error-msg" />
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.abstractestimatedate" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<form:input path="estimateDate" name="estimateDate" class="form-control disablefield" value="${estimateDate}"/>
				<form:errors path="estimateDate" cssClass="add-margin error-msg" />
			</div>
		</div>
		
		<div class="form-group">
		<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.natureofwork" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:select path="natureOfWork" data-first-option="false" id="natureOfWork" class="form-control disablefield" required="required">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${natureOfWork}" itemLabel="name" itemValue="id" />
				</form:select>
				
				<form:errors path="natureOfWork" cssClass="add-margin error-msg" /> 
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.estimate.ward" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:hidden path="ward" class="form-control" name="ward" value="${lineEstimateDetails.lineEstimate.ward.id}"/>
			<form:input id="wardInput" path="ward.name" class="form-control disablefield" type="text" required="required"/>
			</div>
			
		</div>
		
		<c:if test="${lineEstimateDetails.estimateNumber != ''}">
		<div class="form-group">
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.locality" />
			</label>
			<div class="col-sm-3 add-margin ">
			<input class="form-control disablefield" name="locality"  id="" value="${lineEstimateDetails.lineEstimate.location.name}"/>
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.workcategory" />
			</label>
			<div class="col-sm-3 add-margin ">
				<input class="form-control disablefield" name="workCategory" id="workCategory" value="${lineEstimateDetails.lineEstimate.workCategory}"/>
			</div>
		</div>
		<c:if test="${lineEstimate.workCategory == 'SLUM_WORK' }">
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.typeofslum" />
			</label>
			<div class="col-sm-3 add-margin ">
			<input class="form-control disablefield" name="" id="typeOfSlum" value="${lineEstimateDetails.lineEstimate.typeOfSlum}"/>
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.beneficiary" />
			</label>
			<div class="col-sm-3 add-margin ">
			<input class="form-control disablefield" name="" id="beneficiary" value="${lineEstimateDetails.lineEstimate.beneficiary}"/>
			</div>
		</div>
		</c:if>
		</c:if>
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.location" />
			</label>
		     <div class="col-sm-3 add-margin">
                  <div class="input-group">
                       <form:input path="location" name="location" type="text" id="location" class="form-control"/>
                       <span class="input-group-addon">
                           <span class="glyphicon glyphicon-globe" onclick="jQuery('#modal-6').modal('show', {backdrop: 'static'});"></span>
                       </span>
                   </div>
              </div>
		</div> 
		<div class="form-group" id="latlonDiv">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.latitude" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:input path="latitude" name="latitude" id="latitude" class="form-control disablefield" />
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.longitude" />
			</label>
			<div class="col-sm-3 add-margin">
				<form:input path="longitude" id="longitude" name="longitude" class="form-control disablefield" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.nameofwork" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:textarea path="name" name="name" id="workName" class="form-control disablefield" />
			<input type="hidden" id="nameOfWork" name="nameOfWork" value="${lineEstimateDetails.nameOfWork}"/>
			<form:errors path="name" cssClass="add-margin error-msg" />
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.workdescription" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<form:textarea path="description" name="description" class="form-control"  maxlength="1024" required="required"/>
				<form:errors path="description" cssClass="add-margin error-msg" />
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
								<div class="panel-title"><spring:message code="lbl.worksmanagement"/></div>
							</div> 
						
							<!-- panel body -->
							<div class="panel-body no-padding">
								<script type="text/javascript" src="https://maps.google.com/maps/api/js?sensor=false&amp;libraries=places"></script>
								<script type="text/javascript" src="<c:url value='/resources/global/js/geolocation/geolocationmarker-compiled.js' context='/egi'/>"></script>
								<div id="normal" class="img-prop"></div>
								<input id="pac-input" class="btn" type="text" placeholder="Enter a location">
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="modal-footer">
				<button type="button" class="btn btn-info btn-save-location" data-dismiss="modal"><spring:message code="lbl.capture"/></button>
				<button type="button" class="btn btn-info clear" data-dismiss="modal"><spring:message code="lbl.clear"/></button>
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="lbl.close"/></button>
			</div>
		</div>
	</div>
</div>
<script src="<c:url value='/resources/js/loadmaps.js?rnd=${app_release_no}'/>"></script>
		