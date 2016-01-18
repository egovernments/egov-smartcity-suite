<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="row">
	<div class="col-md-12">
		<form:form id="advertisementSuccessform" method="post" class="form-horizontal form-groups-bordered" 
		modelAttribute="advertisementPermitDetail" commandName="advertisementPermitDetail" enctype="multipart/form-data">
	 		<input type="hidden" name="message" id="message" value="%{message}"> 
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title text-center" align="center">
						<c:if test="${not empty message}">
				<div class="alert alert-success" role="alert">
					<spring:message code="${message}" />
				</div>
			</c:if>
					</div>
				</div>
			</div>
		</form:form>
	</div>
	<div class="row text-center">
		<div class="add-margin">
			<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
		</div>
	</div>
</div>
