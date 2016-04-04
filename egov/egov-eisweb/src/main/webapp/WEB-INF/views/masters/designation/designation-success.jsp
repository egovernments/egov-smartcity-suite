<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<script src="<c:url value='/resources/js/app/designation.js'/>"></script>
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title text-center no-float">
					<strong>${message}</strong>
				</div>
			</div>
			<form:form id="designationForm" method="post" modelAttribute="designation"
				class="form-horizontal form-groups-bordered">
				<div class="panel-body">
					<div class="row">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.name" /> :
						</div>
						<div class="col-xs-3 add-margin view-content" id="desig-form">
							<c:out value="${designation.name}"/>
							<form:hidden path="name" id="desigName"/>
						</div>
						</div>
                   <br/>
					<div class="row text-center">
						<div class="row">
								<button type="button" id="buttonCreate" class="btn btn-primary">
									<spring:message code="lbl.create" />
								</button>
								<button type="button" id="buttonEdit" class="btn btn-primary">
									<spring:message code="lbl.edit" />
								</button>
								<a href="javascript:void(0)" class="btn btn-default"
									onclick="self.close()"><spring:message code="lbl.close" /></a>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
</div>

