<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<div class="row" id="page-content">
	<div class="col-md-12">
		<form:form method="post" class="form-horizontal form-groups-bordered"
			id="structureclassification" modelAttribute="structureClassification">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.structureclassification.edit" />
					</div>
					<spring:hasBindErrors name="structureClassification">
						<div class="alert alert-danger col-md-10 col-md-offset-1">
							<form:errors path="*" cssClass="error-msg add-margin" />
							<br />
						</div>
					</spring:hasBindErrors>
				</div>
				<div class="panel-body custom-form">
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.name" /><span class="mandatory"></span></label>

						<div class="col-sm-6 add-margin">
							<form:input id="typeName" path="typeName" type="text"
								value="${structureClassification.typeName}"
								cssClass="form-control is_valid_alphabet" autocomplete="off"
								required="required" />
							<form:errors path="typeName" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.code" /><span class="mandatory"></span></label>
						<div class="col-sm-6 add-margin">
							<form:input id="constrTypeCode" path="constrTypeCode" type="text"
								value="${structureClassification.constrTypeCode}"
								cssClass="form-control is_valid_alphanumeric"
								style="text-transform:uppercase" autocomplete="off"
								required="required" maxlength="16" />
							<form:errors path="constrTypeCode" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.description" /><span class="mandatory"></span></label>

						<div class="col-sm-6 add-margin">
							<form:input id="description" path="description" type="text"
								value="${structureClassification.description}"
								cssClass="form-control is_valid_alphanumeric" autocomplete="off" />
							<form:errors path="description" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.isactive" /><span class="mandatory"></span></label>

						<div class="col-sm-6 add-margin">
							<form:checkbox id="isActive" path="isActive" checked="checked" />
							<form:errors path="isActive" cssClass="error-msg" />
						</div>
					</div>

				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="submit" id="update"
						class="btn btn-primary add-margin">
						<spring:message code="lbl.update" />
					</button>
					<button type="button" class="btn btn-default"
						onclick="self.close()">
						<spring:message code="lbl.close" />
					</button>
				</div>
			</div>
		</form:form>
	</div>
</div>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/structure.js?rnd=${app_release_no}'/>"></script>
