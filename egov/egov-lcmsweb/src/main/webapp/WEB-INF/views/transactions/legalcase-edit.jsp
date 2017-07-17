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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<form:form method="post" action="" modelAttribute="legalCase"
	id="newlegalcaseForm" class="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<div class="row">
		<div class="col-md-12">
			<form:hidden id="mode" path="" value="${mode}" />
			<input type="hidden" name="legalCase" value="${legalCase.id}" /> <input
				type="hidden" name="pettempList" value="${pettempList}" /> <input
				type="hidden" name="respoTempList" value="${respoTempList}" />
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.editlegalcase" />
					</div>
				</div>
				<div class="panel-body custom-form ">
					<%@ include file="legalcasedetails.jsp"%>
					<%@ include file="legalcase-edit-petitiondetails.jsp"%>
					</div>
			</div>
			<jsp:include page="documentdetails-view.jsp"></jsp:include>

			<div class="buttonbottom" align="center">
				<div class="form-group text-center">
					<button type='submit' class='btn btn-primary' id="buttonSubmit">
						<spring:message code='lbl.update' />
					</button>
					<button type="button" class="btn btn-default" id="btnclose">
						<spring:message code="lbl.close" />
					</button>
				</div>
			</div>

		</div>
	</div>
</form:form>

<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/js/app/legalcase-ajax.js?rnd=${app_release_no}'/>"></script>
<script
	src="<cdn:url value='/resources/js/app/populateDropdown.js?rnd=${app_release_no}'/>"></script>
<script
	src="<cdn:url value='/resources/js/app/legalcasenew.js?rnd=${app_release_no}'/>"></script>