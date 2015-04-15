<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css'/>"/>
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css'/>"/>

<div class="row">
	<div class="col-md-12">
		<c:if test="${not empty message}">
            <div id="message" class="success">${message}</div>
        </c:if>
		<form:form id="departmentViewForm" method="post" modelAttribute="department" class="form-horizontal form-groups-bordered">
			<div class="panel panel-primary" data-collapsed = "0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code="title.department.view"/></strong>
					</div>
				</div>
				<div class="panel-body">
					<div class="row add-border">
						<div class="col-sm-3 control-label"><spring:message code="lbl.departmentName" /></div>
						<div class="col-sm-6 add-margin" style="padding-top: 7px" id="ct-name">
							<strong><c:out value="${department.name}"/></strong>
							<input id="deptName" type="hidden" value="<c:out value="${department.name}" />" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-sm-3 control-label"><spring:message code="lbl.departmentCode" /></div>
						<div class="col-sm-6 add-margin" style="padding-top: 7px" id="ct-name">
							<strong><c:out value="${department.code}"/></strong>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="submit" id="createDept" class="btn btn-primary"><spring:message code="lbl.create"/></button>
					<button type="submit" id="editDept" class="btn btn-primary"><spring:message code="lbl.edit"/></button>
					<button type="button" class="btn btn-default" data-dismiss="modal" onclick="self.close();"><spring:message code="lbl.close"/></button>
				</div>
			</div>
		</form:form>
	</div>
</div>
<script src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js'/>"></script>
<script src="<c:url value='/resources/js/app/department.js'/>"></script>