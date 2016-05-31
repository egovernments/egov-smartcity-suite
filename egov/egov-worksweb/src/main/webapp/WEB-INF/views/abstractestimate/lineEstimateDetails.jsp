<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			   <spring:message code="lbl.estimatenumber" />
			</label>
			<div class="col-sm-3 add-margin">
			<form:input path="estimateNumber" class="form-control" name="estimateNumber" readonly="true" value="${lineEstimateDetails.estimateNumber}"/>
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.workidentificationnumber" />
			</label>
			<div class="col-sm-3 add-margin">
			<form:input path="projectCode.code" class="form-control" name="projectCode.code" readonly="true" value="${lineEstimateDetails.projectCode.code}"/>
			</div>
			
		</div>
	  <div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.userdepartment" />
			</label>
			<div class="col-sm-3 add-margin">
				<form:select path="executingDepartment" data-first-option="false" disabled="true" id="executingDepartments" class="form-control" required="required">
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
			<fmt:formatDate value="${currentDate}" var="currDate" pattern="dd-MM-yyyy"/>
				<form:input path="estimateDate" name="estimateDate" class="form-control" value="${currDate}" readonly="true"/>
				<form:errors path="estimateDate" cssClass="add-margin error-msg" />
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.estimate.ward" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:input path="ward" class="form-control" name="ward" readonly="true" value="${lineEstimateDetails.lineEstimate.ward.name}"/>
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.locality" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:input path="" class="form-control" name="location" readonly="true" value="${lineEstimateDetails.lineEstimate.location.name}"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.natureofwork" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:select path="natureOfWork" data-first-option="false" id="natureOfWork"  disabled="true" class="form-control" required="required">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${natureOfWork}" itemLabel="name" itemValue="id" />
				</form:select>
				<form:errors path="natureOfWork" cssClass="add-margin error-msg" /> 
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.workcategory" />
			</label>
			<div class="col-sm-3 add-margin ">
				<form:input path="category" class="form-control" name="category" readonly="true" value="${lineEstimateDetails.lineEstimate.workCategory}"/>
			</div>
		</div> 
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.typeofslum" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:input path="" class="form-control" name="" readonly="true" value="${lineEstimateDetails.lineEstimate.typeOfSlum}"/>
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.beneficiary" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:input path="" class="form-control" name="" readonly="true" value="${lineEstimateDetails.lineEstimate.beneficiary}"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.location" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:input path="location" class="form-control" name="location" readonly="true" value="${lineEstimateDetails.lineEstimate.location.name}"/>
			</div>
			
			<a href="javascript:openMap();" id="mapAnchor" title="Click here to add/view gis marker on map" class="btn btn-primary"><i class="fa fa-location-arrow icon-inputgroup"></i></a>
		</div> 
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.nameofwork" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:input path="name" name="name" class="form-control"/>
			<form:errors path="name" cssClass="add-margin error-msg" />
			</div>
		</div>