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

		<form:form id="complaintUpdate"  modelAttribute="complaint" method="post" role="form" class="form-horizontal form-groups-bordered">
			<div class="form-group">
				<div class="col-md-3 col-xs-6 add-margin">
					<spring:message code="lbl.change.status" />
				</div>
				<div class="col-md-3 col-xs-6 add-margin">

					<form:select path="status" data-first-option="false" id="status"
						cssClass="form-control" cssErrorClass="form-control error">
						<%-- <form:option value="">
							<spring:message code="lbl.select" />
						</form:option> --%>
						<form:options items="${status}" itemValue="id" itemLabel="name" />
					</form:select>
					<form:errors path="status" cssClass="error-msg" />
				</div>


				<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.forward.complaint.to"/></div>
				<div class="col-md-3 col-xs-6 add-margin">
					<select name="test" class="form-control" data-first-option="false"
						id="ct-sel-forward">
						<option>Select</option>
						<option value="1">ADE1NZ14</option>
						<option value="2">AEDN188</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-3 add-margin">
					<spring:message code="lbl.include.message"/>  <small><i class="entypo-star error-msg"></i></small>
				</div>
				<div class="col-md-9 add-margin">
					<textarea class="form-control" id="inc_messge" placeholder=""
						maxlength="400"></textarea>
				</div>
			</div>

			<div class="form-group display-hide show-action-more">
				<div class="form-group">
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.change.complaint.type" /></div>
					<div class="col-md-3 col-xs-6 add-margin">
						<form:select path="complaintType" data-first-option="false"
							id="complaintType" cssClass="form-control"
							cssErrorClass="form-control error">
							<form:option value="">
								<spring:message code="lbl.select" />
							</form:option>
							<form:options items="${complaintType}" itemValue="id"
								itemLabel="name" />
						</form:select>
						<form:errors path="complaintType" cssClass="error-msg" />
					</div>
				</div>
			</div>
			<div class="form-group display-hide show-action-more">
				<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.change.jurisdiction"/></div>
				<div class="col-md-3 col-xs-3 add-margin">
					<form:select path="" data-first-option="false" id="ct-sel-jurisd"
						cssClass="form-control" cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${zone}" itemValue="id"
							itemLabel="bndryNameLocal" />
					</form:select>
					<form:errors path="" cssClass="error-msg" />
				</div>

				<div class="col-md-3 col-xs-3 add-margin">
					<form:select path="location" data-first-option="false"
						id="location" cssClass="form-control"
						cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${ward}" itemValue="id"
							itemLabel="bndryNameLocal" />
					</form:select>
					<form:errors path="location" cssClass="error-msg" />
				</div>
			</div>

			<div class="form-group">
				<div class="text-center">
					<a href="javascript:void(0);" id="toggleactions"
						class="btn btn-primary"><spring:message code="lbl.more"/> ..</a> 
						
						<a href="javascript:void(0);" id="btn_submit" class="btn btn-primary"><spring:message code="lbl.submit"/></a> 
						<button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
				</div>
			</div>
		</form:form>
	</div>
</div>
