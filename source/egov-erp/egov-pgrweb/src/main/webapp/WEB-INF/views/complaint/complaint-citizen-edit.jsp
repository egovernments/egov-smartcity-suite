<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<jsp:include page="view-complaint.jsp"></jsp:include>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<strong>Actions</strong>
		</div>
	</div>
	<div class="panel-body">

		<form:form id="complaintUpdate" modelAttribute="complaint"
			method="post" role="form"
			class="form-horizontal form-groups-bordered">
			<div class="form-group">
				<div class="col-md-3 col-xs-6 add-margin">
					<spring:message code="lbl.change.status" />
				</div>
				<div class="col-md-3 col-xs-6 add-margin">

					<form:select path="status" data-first-option="false" id="status"
						cssClass="form-control" cssErrorClass="form-control error">
						<form:options items="${status}" itemValue="id" itemLabel="name" />
					</form:select>
					<form:errors path="status" cssClass="error-msg" />
				</div>
				
			</div>
		
			<div class="form-group">
				<div class="col-md-3 add-margin">
					<spring:message code="lbl.include.message" /><span class="mandatory"></span>
				</div>
				<div class="col-md-9 add-margin">
					<textarea class="form-control" id="inc_messge" placeholder=""
						maxlength="400" name="approvalComent"></textarea>
				</div>
			</div>
	<div class="form-group">
		<div class="text-center">
			 <button type="submit" class="btn btn-success">
				<spring:message code="lbl.submit" />
			</button>
			<button type="reset" class="btn btn-default">
				<spring:message code="lbl.reset" />
			</button>
		</div>
	</div>
	</form:form>
</div>
</div>
