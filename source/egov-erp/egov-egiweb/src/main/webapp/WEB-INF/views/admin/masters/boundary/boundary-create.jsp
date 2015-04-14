<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css'/>">

<div class="row" id="page-content">
	<div class="col-md-12">
		<div class="panel" data-collapsed="0">
			<div class="panel-body">
				 <c:if test="${not empty message}">
                    <div id="message" class="success">${message}</div>
                </c:if>
        <c:choose>
			<c:when test="${isUpdate}">
				<c:set value="" var="actionUrl" />
			</c:when>
			<c:otherwise>
				<c:set value="${pageContext.request.contextPath}/controller/boundary/create" var="actionUrl" />
			</c:otherwise>
		</c:choose>
		<form:form  method="post" action="${actionUrl}" class="form-horizontal form-groups-bordered" commandName="boundary" id="boundaryCreateOrUpdate" >
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<c:choose>
						<c:when test="${isUpdate}">
							<strong><spring:message code="lbl.hdr.updateBoundary"/>  <c:out value="${boundary.name}"/></strong>
						</c:when>
						<c:otherwise>
							<strong><spring:message code="lbl.hdr.updateBoundary"/></strong>
						</c:otherwise>
					</c:choose>
					</div>
				</div> 
				
				<div class="panel-body custom-form">
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code="lbl.hierarchyType"/></label>
						<div class="col-sm-6" style="padding-top: 7px">
							<strong><c:out value="${boundaryType.hierarchyType.name}" /></strong>                 
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundaryType"/>
						</label>
						<div class="col-sm-6" style="padding-top: 7px">
							<strong><c:out value="${boundaryType.name}" /></strong>
							<input type="hidden" name="boundaryTypeId" value="<c:out value="${boundaryType.id}" />" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.name"/><span class="mandatory"></span>
						</label>
						<div class="col-sm-6">
							<form:input path="name" id="name" type="text" class="form-control low-width is_valid_alphanumeric" placeholder="" autocomplete="off" required="required"/>
                            <form:errors path="name" cssClass="add-margin error-msg"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.nameLocal"/>
						</label>
						<div class="col-sm-6">
							<form:input path="localName" id="name" type="text" class="form-control low-width is_valid_alphanumeric" placeholder="" autocomplete="off" />
                            <form:errors path="localName" cssClass="add-margin error-msg"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.number"/><span class="mandatory"></span>
						</label>
						<div class="col-sm-6">
							<form:input path="boundaryNum" id="name" type="text" class="form-control low-width is_valid_number" placeholder="" autocomplete="off" required="required"/>
                            <form:errors path="boundaryNum" cssClass="add-margin error-msg"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.fromDate"/><span class="mandatory"></span>
						</label>
						<div class="col-sm-6">
							<form:input path="fromDate" id="boundaryFromDate" type="text" class="form-control low-width datepicker" data-inputmask="'mask': 'd/m/y'" placeholder="" autocomplete="off" required="required"/>
                            <form:errors path="fromDate" cssClass="add-margin error-msg"/>
						</div>
					</div>		
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.toDate"/>
						</label>
						<div class="col-sm-6">
							<form:input path="toDate" id="boundaryToDate" type="text" class="form-control low-width" placeholder="" autocomplete="off" />
                            <form:errors path="toDate" cssClass="add-margin error-msg"/>
						</div>
					</div>		
				</div>
			</div>
			<div class="row">
				<div class="text-center">
				   <button type="submit" class="btn btn-success">
                          <spring:message code="lbl.submit"/>
                   </button>
			       <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
				</div>
			</div>
		</form:form>
			</div>
        </div>
    </div>
</div>

<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css'/>" />
<script src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js'/>"></script>

<script src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js'/>"></script>

<script src="<c:url value='/resources/global/js/egov/custom.js'/>"></script>
<script src="<c:url value='/resources/js/app/boundary.js'/>"></script>