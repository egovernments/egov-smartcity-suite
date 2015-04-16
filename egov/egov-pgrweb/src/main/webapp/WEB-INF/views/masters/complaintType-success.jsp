<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.joda.org/joda/time/tags" prefix="joda"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<script src="<c:url value='/resources/js/app/complaintype.js'/>"></script>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title text-center no-float">
					<strong>${message}</strong>
				</div>
			</div>
			<form:form id="complaintTypeViewForm" method="post"
				class="form-horizontal form-groups-bordered">
				<div class="panel-body">
					<div class="row">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.complaintTypeName" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin" id="ct-name">
							<c:out value="${complaintType.name }"></c:out>
							<input id="compTypeName" type="hidden"
								value="<c:out value="${complaintType.name }" />" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.complaintTypeCode" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin" id="ct-code">
							<c:out value="${complaintType.code}"></c:out>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.department" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin" id="ct-dept">
							<c:out value="${complaintType.department.name}"></c:out>
						</div>

						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.complaintType.loc" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin" id="ct-loc">
							${complaintType.locationRequired}</div>
					</div>
					<div class="row">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.isactive" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin" id="ct-isactive">
							${complaintType.isActive}</div>
					</div>

					<div class="row text-center">
						<div class="row">
							<div class="text-center">
								<button type="submit" id="buttonCreate" class="btn btn-success">
									<spring:message code="lbl.create" />
								</button>
								<button type="submit" id="buttonEdit" class="btn btn-success">
									<spring:message code="lbl.edit" />
								</button>
								<a href="javascript:void(0)" class="btn btn-default"
									onclick="self.close()"><spring:message code="lbl.close" /></a>
							</div>
						</div>
					</div>
				</div>
			</form:form>

		</div>
	</div>
</div>
