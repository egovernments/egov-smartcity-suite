
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet"	href="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<script	src="<c:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<link rel="stylesheet"	href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>">
<link rel="stylesheet"	href="<c:url value='/resources/global/css/egov/custom.css' context='/egi'/>">
<link rel="stylesheet"	href="<c:url value='/resources/global/css/egov/header-custom.css' context='/egi'/>">

<script src="<c:url value='/resources/js/app/viewcomplaintype.js'/>"></script>

<table class="table table-bordered datatable" id="view-complaint-type">
</table>
<div class="form-group">
<div class="text-center">
<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
</div>
</div>
</body>

