
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<link rel="stylesheet"	href="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<script	src="<c:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>

<link rel="stylesheet"	href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>">
<link rel="stylesheet"	href="<c:url value='/resources/global/css/egov/custom.css' context='/egi'/>">
<link rel="stylesheet"	href="<c:url value='/resources/global/css/egov/header-custom.css' context='/egi'/>">

<div class="row" id="page-content">
	<div class="col-md-12">
		<div class="panel" data-collapsed="0">
			<div class="panel-body">
				 
		<form:form  method="post" class="form-horizontal form-groups-bordered" modelAttribute="boundary" id="boundaryPageViewForm" >
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-body custom-form">
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code="lbl.hierarchyType"/></label>
						<div class="col-sm-6" style="padding-top: 7px">
							<strong><c:out value="${boundaryType.hierarchyType.name}" /></strong>     
							<input type="hidden" id="btnHierarchyType" value="<c:out value="${boundaryType.hierarchyType.id}" />" />            
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundaryType"/>
						</label>
						<div class="col-sm-6" style="padding-top: 7px">
							<strong><c:out value="${boundaryType.name}" /></strong>
							<input type="hidden" id="btnBoundaryType" value="<c:out value="${boundaryType.id}" />" />
						</div>
					</div>
	   			</div>
	   		</div>
	   	</form:form>
	   </div>
	  </div>
	 </div>
</div>
<table class="table table-bordered datatable" id="view-boundaries">
</table>

<script src="<c:url value='/resources/js/app/viewboundaries.js'/>"></script>
