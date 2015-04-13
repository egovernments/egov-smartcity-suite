<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css'/>">
<div class="row">
	<div class="col-md-12">
			<c:if test="${not empty message}">
                    <div id="message" class="success">${message}</div>
            </c:if>
		<form:form  id="boundaryTypeUpdateform" method ="post" 
		 class="form-horizontal form-groups-bordered" modelAttribute="boundaryType" >
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code="title.updateBoundaryType"/></strong>
					</div>
				</div> 
				<div class="panel-body custom-form">
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.boundaryType.heirarchyType"/><small><i class="entypo-star error-msg"></i></small></label>
								<div class="col-sm-6">
									<form:input path="hierarchyType.name" id="hierarchyType" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required" readonly="true"/>
					                <form:errors path="hierarchyType" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<c:if test="${not empty boundaryType.parent.name}">
								<div class="form-group">
									<label class="col-sm-3 control-label"><spring:message code="lbl.parent.boundaryType"/><small><i class="entypo-star error-msg"></i></small></label>
									<div class="col-sm-6">
										<form:input path="parent.name" id="name" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required" readonly="true"/>
			                            <form:errors path="name" cssClass="add-margin error-msg"/>
									</div>
								</div>
							</c:if>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.boundaryType.boundaryType"/><small><i class="entypo-star error-msg"></i></small></label>
								<div class="col-sm-6">
									<form:input path="name" id="name" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
		                            <form:errors path="name" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.boundaryType.boundaryTypeLocal"/></label>
								<div class="col-sm-6">
									<form:input path="localName" id="localName" type="text" class="form-control low-width" placeholder="" autocomplete="off" />
		                            <form:errors path="localName" cssClass="add-margin error-msg"/>
								</div>
							</div>
						</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="submit" class="btn btn-primary"><spring:message code="lbl.update"/></button>
					<button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
			       <button type="button" class="btn btn-default" data-dismiss="modal" onclick="self.close()" ><spring:message code="lbl.close"/></button>
				</div>
			</div>
		</form:form>
	</div>
</div>


<script src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js'/>"></script>

