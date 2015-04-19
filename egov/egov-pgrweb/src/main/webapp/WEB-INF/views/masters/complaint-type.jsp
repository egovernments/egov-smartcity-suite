<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<script src="<c:url value='/resources/js/app/complaintype.js'/>"></script>
<div class="row">
	<div class="col-md-12">
		<div class="panel" data-collapsed="0">
			<form:form id="addcomplaint" method="post" class="form-horizontal form-groups-bordered"	modelAttribute="complaintType">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">
							<strong><spring:message code="lbl.complaintType" /></strong>
						</div>
					</div>
					<div class="panel-body custom-form">
						<div class="form-group">
							<label class="col-sm-3 control-label"> <spring:message
									code="lbl.complaintTypeName" />
							</label>

							<div class="col-sm-6 add-margin">
								<form:input path="name" id="comp_type_name"
									cssClass="form-control is_valid_alphabet"
									cssErrorClass="form-control error" />
								<form:errors path="name" cssClass="error-msg" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label"> <spring:message
									code="lbl.complaintTypeCode" />
							</label>

							<div class="col-sm-6 add-margin">
								<form:input path="code" id="comp_type_code"
									cssClass="form-control is_valid_alphaNumWithsplchar"
									cssErrorClass="form-control error" />
								<form:errors path="code" cssClass="error-msg" />
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-3 control-label"> <spring:message
									code="lbl.department" />
							</label>

							<div class="col-sm-6 add-margin">
								<form:select path="department" id="comp_type_dept"
									cssClass="form-control" cssErrorClass="form-control error">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<form:options items="${departments}" itemValue="id"
										itemLabel="name" />
								</form:select>
								<form:errors path="department" cssClass="error-msg" />
							</div>

						</div>

						<div class="form-group">

							<label for="field-1" class="col-sm-3 control-label"><spring:message
									code="lbl.complaintType.loc" /><span class="mandatory"></span>
							</label>

							<div class="col-sm-1 col-xs-12 add-margin">
								<form:radiobutton path="locationRequired" id="comp_type_loc_yes" value="yes"/>
								<label>Yes</label>
							</div>
							<div class="col-sm-1 col-xs-12 add-margin">
								<form:radiobutton path="locationRequired" id="comp_type_loc_yno" value="no"/>
								<label>No</label>
							</div>
						</div>

						<div class="form-group">

							<label for="field-1" class="col-sm-3 col-xs-2 control-label"><spring:message
									code="lbl.isactive"></spring:message></label>

							<div class="col-sm-3 col-xs-8 add-margin">
								<form:checkbox path="isActive" id="comp_type_isactive" checked="checked"/>
								<form:errors path="isActive" cssClass="error-msg" />
							</div>

						</div>
						
						<div class="form-group">
							<label class="col-sm-3 control-label"> <spring:message
									code="lbl.complaintTypeDesc" />
							</label>

							<div class="col-sm-6 add-margin">
								<form:textarea path="description" id="comp_type_desc"
									cssClass="form-control is_valid_alphabet"
									cssErrorClass="form-control error" maxlength="50"/>
								<%-- <form:errors path="description" cssClass="error-msg" /> --%>
							</div>
						</div>
						

					</div>
				</div>
				<div class="form-group">
					<div class="text-center">
						<button type="submit" class="btn btn-success"><spring:message code="lbl.submit"/></button>
						<button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
						<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
					</div>
				</div>
			</form:form>
		</div>
	</div>
</div>

