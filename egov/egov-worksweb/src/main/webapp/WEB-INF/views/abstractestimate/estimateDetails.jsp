<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			   <spring:message code="lbl.estimatenumber" />
			</label>
			<div class="col-sm-3 add-margin">
			<form:input path="estimateNumber" class="form-control" id="estimateNumber" name="estimateNumber" disabled="true" value="${lineEstimateDetails.estimateNumber}"/>
			</div>
			<%-- 	<label class="col-sm-2 control-label text-right">
			 <spring:message code="lbl.workidentificationnumber" />
			</label>
			<div class="col-sm-3 add-margin">
			<form:input path="projectCode.code" class="form-control" name="projectCode.code" id="projectCode" disabled="true" value="${lineEstimateDetails.projectCode.code}"/>
			</div> --%>
		</div>
	  <div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.department" />
			</label>
			<div class="col-sm-3 add-margin">
				<form:select path="executingDepartment" data-first-option="false" disabled="true" id="executingDepartment" class="form-control" required="required">
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
				<form:input path="estimateDate" name="estimateDate" class="form-control" value="${estimateDate}" disabled="true"/>
				<form:errors path="estimateDate" cssClass="add-margin error-msg" />
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
			    <spring:message code="lbl.estimate.ward" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:hidden path="ward" class="form-control" name="ward" disabled="true" value="${lineEstimateDetails.lineEstimate.ward}"/>
			<form:input id="wardInput" path="ward.name" class="form-control" type="text" disabled="true" required="required"/>
			</div>
			
		</div>
		
		<c:if test="${lineEstimateDetails.estimateNumber != ''}">
		<div class="form-group">
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.locality" />
			</label>
			<div class="col-sm-3 add-margin ">
			<input class="form-control" name="locality" disabled="true" id="" value="${lineEstimateDetails.lineEstimate.location.name}"/>
			</div>
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.workcategory" />
			</label>
			<div class="col-sm-3 add-margin ">
				<input class="form-control" name="category" disabled="true" value="${workCategory}"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.typeofslum" />
			</label>
			<div class="col-sm-3 add-margin ">
			<input class="form-control" name="" disabled="true" value="${lineEstimateDetails.lineEstimate.typeOfSlum}"/>
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.beneficiary" />
			</label>
			<div class="col-sm-3 add-margin ">
			<input class="form-control" name="" disabled="true" value="${lineEstimateDetails.lineEstimate.beneficiary}"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.locality" />
			</label>
			<div class="col-sm-3 add-margin ">
			<input class="form-control" name="locality" disabled="true" id="" value="${lineEstimateDetails.lineEstimate.location.name}"/>
			</div>
		</div>
		</c:if>
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.location" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:input path="location" class="form-control" name="location" value=""/> 
			</div>
			<a href="javascript:openMap();" id="mapAnchor" title="Click here to add/view gis marker on map" class="btn btn-primary"><i class="fa fa-location-arrow icon-inputgroup"></i></a>
		</div> 
		
		<%-- <div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.latitude" />
			</label>
			<div class="col-sm-3 add-margin ">
			<form:input path="latitude" name="latitude" class="form-control" />
			<form:errors path="latitude" cssClass="add-margin error-msg" />
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.longitude" />
			</label>
			<div class="col-sm-3 add-margin view-content">
				<form:input path="longitude" name="longitude" class="form-control" />
				<form:errors path="longitude" cssClass="add-margin error-msg" />
			</div>
		</div> --%>
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.nameofwork" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin ">
			<form:textarea path="name" name="name" class="form-control" value="${lineEstimateDetails.nameOfWork}"/>
			<form:errors path="name" cssClass="add-margin error-msg" />
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.workdescription" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<form:textarea path="description" name="description" class="form-control"  maxlength="1024" required="required"/>
				<form:errors path="lineEstimateDetails" cssClass="add-margin error-msg" />
			</div>
		</div>
		<div class="panel panel-primary" data-collapsed="0">
</div>
		
		