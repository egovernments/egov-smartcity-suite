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
			<c:out value="${abstractEstimate.estimateDate}"></c:out>
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
			<spring:message code="lbl.estimate.ward" />
		</div> 
	
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${abstractEstimate.ward.name}"></c:out>
		</div>
	</div>	
	<c:if test="${abstractEstimate.lineEstimateDetails != null}">	
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.locality" />
			</div> 
		
			<div class="col-xs-3 add-margin view-content">
				<c:out value="${abstractEstimate.lineEstimateDetails.lineEstimate.location.name}"></c:out>
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.workcategory" />
			</div> 
		
			<div class="col-xs-3 add-margin view-content">
				<c:out value="${abstractEstimate.lineEstimateDetails.lineEstimate.workCategory}"></c:out>
			</div>
		</div>
		<c:if test="${abstractEstimate.lineEstimateDetails.lineEstimate.workCategory == 'SLUM_WORK' }">
			<div class="row add-border">
				<div class="col-xs-3 add-margin">
					<spring:message code="lbl.typeofslum" />
				</div> 
			
				<div class="col-xs-3 add-margin view-content">
					<c:out value="${abstractEstimate.lineEstimateDetails.lineEstimate.typeOfSlum}"></c:out>
				</div>
				<div class="col-xs-3 add-margin">
					<spring:message code="lbl.beneficiary" />
				</div> 
			
				<div class="col-xs-3 add-margin view-content">
					<c:out value="${abstractEstimate.lineEstimateDetails.lineEstimate.beneficiary}"></c:out>
				</div>
			</div>
		</c:if>
	</c:if>	
	<div class="row add-border">
				<div class="col-xs-3 add-margin">
					<spring:message code="lbl.location" />
				</div> 
			
				<div class="col-xs-3 add-margin view-content">
					<c:out value="${abstractEstimate.location}"></c:out>
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
