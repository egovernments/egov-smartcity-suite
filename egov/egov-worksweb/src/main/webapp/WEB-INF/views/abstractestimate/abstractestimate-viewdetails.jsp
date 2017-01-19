<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code="lbl.department" />
		</div> 
	
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${abstractEstimate.executingDepartment.name}"></c:out>
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code="lbl.abstractestimatedate" />
		</div> 
	
		<div class="col-xs-3 add-margin view-content">
			<fmt:formatDate pattern="dd/MM/yyyy" value="${abstractEstimate.estimateDate}" />
		</div>
	</div>	
	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code="lbl.natureofwork" />
		</div> 
	
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${abstractEstimate.natureOfWork.name}"></c:out>
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code="lbl.election.ward" />
		</div> 
	
		<div class="col-xs-3 add-margin view-content">
			<c:choose>
				<c:when test="${abstractEstimate.ward.boundaryType.name.toUpperCase() == 'CITY'}">
					<c:out value="${abstractEstimate.ward.name}"></c:out>
				</c:when>
				<c:otherwise>
					<c:out value="${abstractEstimate.ward.boundaryNum}"></c:out>
				</c:otherwise>
			</c:choose>
		</div>
	</div>	
	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code="lbl.locality" />
		</div>
		<div class="col-xs-3 add-margin view-content"> 
			<c:choose>
				<c:when test="${abstractEstimate.locality != null}">
					<c:out value="${abstractEstimate.locality.name}"></c:out>
				</c:when>
				<c:otherwise>
					<c:out default="N/A" value="N/A"></c:out>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code="lbl.workcategory" />
		</div> 
	
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${abstractEstimate.workCategory}"></c:out>
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code="lbl.beneficiary" />
		</div> 
	
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${abstractEstimate.beneficiary}"></c:out>
		</div>
	</div>
	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code="lbl.modeofallotment" />
		</div> 
	
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${abstractEstimate.modeOfAllotment}"></c:out>
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code="lbl.location" />
		</div> 
		<div class="col-xs-3 add-margin view-content">
		    <input type="hidden" id="locationHidden" name="locationHidden" value="${abstractEstimate.location}"/>
			<c:choose>
				<c:when test="${abstractEstimate.location != null}">
				<span class="map-tool-class btn-secondary" data-toggle="tooltip"
							data-placement="top" title="" data-original-title="Locate on map"
							onclick="jQuery('#view-location').modal('show', {backdrop: 'static'});">
							<i class="fa fa-map-marker"></i></span>
					<c:out value="${abstractEstimate.location}"></c:out>
				</c:when>
				<c:otherwise>
					<c:out default="N/A" value="N/A"></c:out>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="row add-border">
	     <div class="col-xs-3 add-margin">
				<spring:message code="lbl.latitude" />
			</div>  
			<div class="col-xs-3 add-margin view-content">
				<c:choose>
					<c:when test="${abstractEstimate.latitude != null}">
						<c:out value="${abstractEstimate.latitude}"></c:out>
					</c:when>
					<c:otherwise>
						<c:out default="N/A" value="N/A"></c:out>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.longitude" />
			</div> 
			<div class="col-xs-3 add-margin view-content">
				<c:choose>
					<c:when test="${abstractEstimate.longitude != null}">
						<c:out value="${abstractEstimate.longitude}"></c:out>
					</c:when>
					<c:otherwise>
						<c:out default="N/A" value="N/A"></c:out>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code="lbl.nameofwork" />
		</div> 
	
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${abstractEstimate.name}"></c:out>
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code="lbl.workdescription" />
		</div> 
	
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${abstractEstimate.description}"></c:out>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-3 add-margin">
			<spring:message code="lbl.typeofwork" />
		</div> 
	
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${abstractEstimate.parentCategory.name}"></c:out>
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code="lbl.subtypeofwork" />
		</div> 
	
		<div class="col-xs-3 add-margin view-content">
			<c:choose>
					<c:when test="${abstractEstimate.category != null}">
						<c:out value="${abstractEstimate.category.name}"></c:out>
					</c:when>
					<c:otherwise>
						<c:out default="N/A" value="N/A"></c:out>
					</c:otherwise>
				</c:choose>  
		</div>
	</div>	
<script>
var lat = '${abstractEstimate.latitude}';
var lng = '${abstractEstimate.longitude}';
</script>