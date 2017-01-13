<link rel="stylesheet" href="<cdn:url value='/resources/global/css/egov/map-autocomplete.css?rnd=${app_release_no}' context='/egi'/>">
	  <div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.department" /><c:if test="${abstractEstimate.lineEstimateDetails == null }"><span class="mandatory"></span></c:if>
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
				<c:choose>
						<c:when test="${abstractEstimate.lineEstimateDetails != null && abstractEstimate.lineEstimateDetails.lineEstimate.abstractEstimateCreated == true }" >
							 <form:input path="estimateDate" id="estimateDate" data-errormsg="Estimate Date of the work is mandatory!" data-idx="0" data-optional="0" class="form-control datepicker estimateDateClassId" maxlength="10" data-inputmask="'mask': 'd/m/y'" data-date-end-date="-0d" required="required"  />
							 <input type="hidden" id="prevEstimateDate" name="prevEstimateDate" />
						</c:when>
						<c:otherwise>
							<form:input path="estimateDate" name="estimateDate" class="form-control disablefield" value="${estimateDate}"/>						
						</c:otherwise>
				</c:choose>
				<form:errors path="estimateDate" cssClass="add-margin error-msg" />
			</div>
		</div>
		
		<div class="form-group">
		<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.natureofwork" /><c:if test="${abstractEstimate.lineEstimateDetails == null }"><span class="mandatory"></span></c:if>
			</label>
			<div class="col-sm-3 add-margin ">
			<form:select path="natureOfWork" data-first-option="false" id="natureOfWork" class="form-control disablefield" onchange="getBudgetHeads();" required="required">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${natureOfWork}" itemLabel="name" itemValue="id" />
				</form:select>
				
				<form:errors path="natureOfWork" cssClass="add-margin error-msg" /> 
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.election.ward" /><c:if test="${abstractEstimate.lineEstimateDetails == null }"><span class="mandatory"></span></c:if>
			</label>
			<div class="col-sm-3 add-margin ">
			<form:hidden path="ward" class="form-control" name="ward" value="${abstractEstimate.lineEstimateDetails.lineEstimate.ward.id}"/>
			<c:choose>
				<c:when test="${abstractEstimate.lineEstimateDetails != null }">
					<c:choose>
						<c:when test="${abstractEstimate.lineEstimateDetails.lineEstimate.ward.boundaryType.name.toUpperCase() == 'CITY'}">
							<form:input id="wardInput" path=""  value="${abstractEstimate.lineEstimateDetails.lineEstimate.ward.name}" class="form-control disablefield" type="text" required="required"/>
						</c:when>
						<c:otherwise>
							<form:input id="wardInput" path="" value="${abstractEstimate.lineEstimateDetails.lineEstimate.ward.boundaryNum}" class="form-control disablefield" type="text" required="required"/>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${abstractEstimate.ward.boundaryType.name.toUpperCase() == 'CITY'}">
							<form:input id="wardInput" path=""  value="${abstractEstimate.ward.name}" class="form-control disablefield" type="text" required="required"/>
						</c:when>
						<c:otherwise>
							<form:input id="wardInput" path="" value="${abstractEstimate.ward.boundaryNum}" class="form-control disablefield" type="text" required="required"/>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
			</div>
		</div>
		
		<div class="form-group">
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.locality" />
			</label>
			<div class="col-sm-3 add-margin ">
			<input class="form-control disablefield" name="locality"  id="" value="${abstractEstimate.lineEstimateDetails.lineEstimate.location.name}"/>
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.workcategory" /><c:if test="${abstractEstimate.lineEstimateDetails == null }"><span class="mandatory"></span></c:if>
			</label>
			<div class="col-sm-3 add-margin ">
				<form:select path="workCategory" data-first-option="false" id="workCategory" cssClass="form-control disablefield" required="required">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${workCategory}" />
				</form:select>
				<form:errors path="workCategory" cssClass="add-margin error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.beneficiary" />
			</label>
			<div class="col-sm-3 add-margin ">
			<input class="form-control disablefield" name="" id="beneficiary" value="${abstractEstimate.lineEstimateDetails.lineEstimate.beneficiary}"/>
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.location" /><span id="spanlocation" class="mandatory"></span>
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
			    <spring:message code="lbl.nameofwork" /><c:if test="${abstractEstimate.lineEstimateDetails == null }"><span class="mandatory"></span></c:if>
			</label>
			<div class="col-sm-3 add-margin ">
			<form:textarea path="name" name="name" id="workName" class="form-control disablefield patternvalidation" data-pattern="alphanumericwithallspecialcharacters" required="required"/>
			<c:choose>
				<c:when test="${abstractEstimate.lineEstimateDetails != null }">
					<input type="hidden" id="nameOfWork" name="nameOfWork" value="${abstractEstimate.lineEstimateDetails.nameOfWork}"/>
				</c:when>
				<c:otherwise>
					<input type="hidden" id="nameOfWork" name="nameOfWork" value="${abstractEstimate.name}"/>
				</c:otherwise>
			</c:choose>
			<form:errors path="name" cssClass="add-margin error-msg" />
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.workdescription" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<form:textarea path="description" id="description" name="description" class="form-control patternvalidation" data-pattern="alphanumericwithallspecialcharacters" maxlength="1024" required="required"/>
				<form:errors path="description" cssClass="add-margin error-msg" />
			</div>
		</div>
		<c:if test="${abstractEstimate.lineEstimateDetails != null && abstractEstimate.lineEstimateDetails.lineEstimate.abstractEstimateCreated == true }">
			<div class="form-group">
				<label class="col-sm-4 control-label text-right view-content">
				    <spring:message code="lbl.spilloverwork" />
				</label>
			</div>
		</c:if>