<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css'/>">
<div class="row">
	<div class="col-md-12">
		<form:form  id="boundaryTypeViewform" mothod ="post" 
		 class="form-horizontal form-groups-bordered" modelAttribute="boundaryType" >
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code="title.viewBoundaryType"/></strong>
					</div>
				</div> 
				<div class="panel-body">
					<div class="row add-border">
						<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.boundaryType.heirarchyType"/></div>
						<div class="col-md-9 col-xs-6 add-margin" id="ct-name">
							<c:out value="${boundaryType.hierarchyType.name}"></c:out>
						</div>
					</div>
					<c:if test="${not empty boundaryType.parent.name}">
						<div class="row add-border">
							<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.parent.boundaryType"/></div>
							<div class="col-md-9 col-xs-6 add-margin" id="ct-name">
								<c:out value="${boundaryType.parent.name}"></c:out>
							</div>
						</div>
					</c:if>
					<div class="row add-border">
						<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.boundaryType.boundaryType"/></div>
						<div class="col-md-9 col-xs-6 add-margin" id="ct-name">
							<c:out value="${boundaryType.name}"></c:out>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">
			       <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="lbl.close"/></button>
				</div>
			</div>
		</form:form>
	</div>
</div>


<script src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js'/>"></script>

