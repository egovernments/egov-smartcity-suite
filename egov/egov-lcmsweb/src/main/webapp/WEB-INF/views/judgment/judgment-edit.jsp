<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<form:form role="form" method="post" modelAttribute="judgment"
	id="judgmentform" cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<div class="panel-heading">
					<div class="panel-title">Edit Judgment</div>
				</div>
	 <jsp:include page="../application/viewSummarizedCase.jsp"/>  
	<%@ include file="judgment-form.jsp"%>
	<input type="hidden" name="judgment" value="${judgment.id}" />

	</div>
	</div>
	</div>
	</div>
	<div class="form-group">
		<div class="text-center">
			<button type='submit' class='btn btn-primary' id="buttonSubmit">
				<spring:message code='lbl.update' />
			</button>
			<button type="button" class="btn btn-default" id="btnclose">
				<spring:message code="lbl.close" />
			</button>
		</div>
	</div>
</form:form>
<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>">
<script type="text/javascript"
	src="<c:url value='/resources/js/app/judgmentHelper.js?rnd=${app_release_no}'/>"></script>
<script type="text/javascript"
	src="<c:url value='/resources/js/app/legalcaseSearch.js?rnd=${app_release_no}'/>"></script>

