<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--  <form role="form" class="form-horizontal form-groups-bordered"> --%>
<form:form method="post" action=""
	class="form-horizontal form-groups-bordered"
	modelAttribute="propertyCategory" id="categoryMasterform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading"></div>
		<div class="panel-body custom-form">
			<div class="form-group">
				<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.propertytype" /><span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="propertyType"
						data-first-option="false" id="propertyType"
						cssClass="form-control" required="required">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${propertyType}" itemValue="id"
							itemLabel="name" />
					</form:select>
					<form:errors path="propertyType"
						cssClass="add-margin error-msg" />
				</div>
				<label class="col-sm-3 control-label text-right"><spring:message code="lbl.category.type" /><span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin"  id="categorydiv">
					<form:input cssClass="form-control patternvalidation"
						data-pattern="alphabetwithspace" maxlength="25" id="categoryName"
						path="categorytype.name" required="required" />
					<form:errors path="categorytype.name"
						cssClass="add-margin error-msg" />
				</div>


			</div>

			<div class="form-group text-center">
			<button type="submit" class="btn btn-primary" id="buttonid"><spring:message code="lbl.submit"/></button>
				<%-- <form:button type="submit" class="btn btn-primary" id="buttonid">
					<spring:message code="lbl.submit" />
				</form:button> --%>
				<a onclick="self.close()" class="btn btn-default"
					href="javascript:void(0)"><spring:message code="lbl.close" /></a>
			</div>
		</div>
	</div>
</form:form>
<link rel="stylesheet" href="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
                <script src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	            type="text/javascript"></script>
                <script src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	            type="text/javascript"></script>
                <script src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	            type="text/javascript"></script>

