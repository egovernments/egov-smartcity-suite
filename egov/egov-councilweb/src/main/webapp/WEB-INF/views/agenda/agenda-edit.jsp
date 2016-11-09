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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<form:form role="form" action="new" modelAttribute="councilPreamble"
	id="councilPreambleform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	
		<jsp:include page="../councilpreamble/councilpreamble-search-form.jsp"/>
		
</form:form>
<div class="row display-hide report-section">
	<div class="col-md-12 table-header text-left">Preamble Search
		Result</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="resultTable">
		</table>
	</div>
</div>
<form:form role="form" action="../update" modelAttribute="councilAgenda"
	id="councilAgendaform" cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	
	<!-- <div class="row display-hide agenda-section"> -->
	
	<!-- <div class="col-md-6 text-right pull-right"><button type="button" class="btn btn-primary" id="add-agenda">Add Row</button></div> -->
	<div class="row ">

		<div class="col-md-7 table-header text-left">Update Agenda</div>
		
		<label class="col-md-2 control-label text-right"><spring:message
				code="lbl.committeetype" /> </label>
		<div class="col-md-3 add-margin">
			<form:select path="committeeType" id="committeeType"
				cssClass="form-control" cssErrorClass="form-control error">
				<form:option value="">
					<spring:message code="lbl.select" />
				</form:option>
				<form:options items="${committeeType}" itemValue="id"
					itemLabel="name" />
			</form:select>
			<form:errors path="committeeType" cssClass="error-msg" /> 
		
	</div>
	
	</div>
	<div clas="row">
	<div class="report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id=agendaTable>
			<thead>
				<tr>
					<%-- <th><spring:message code="lbl.serial.no" /></th> --%>
					<th><spring:message code="lbl.preamble.number" /></th>
					<th><spring:message code="lbl.department" /></th>
					<th><spring:message code="lbl.gist.preamble" /></th>
					<th><spring:message code="lbl.amount" /></th>
					<th><spring:message code="lbl.action" /></th>
				</tr>
			</thead>


			<tbody>
				<c:choose>
					<c:when test="${!councilAgenda.councilAgendaDetailsForUpdate.isEmpty()}">
						<c:forEach items="${councilAgenda.councilAgendaDetailsForUpdate}" var="contact"
							varStatus="counter">
							<tr>
								<!--  /* <td><span class="sno">{{sno}}</span></td>+*/ -->
								<td><input type="text" class="form-control" data-unique
									name="councilAgendaDetailsForUpdate[${counter.index}].preamble.preambleNumber" 
									readonly="readonly" value="${contact.preamble.preambleNumber}" /></td>
								<td><input type="text" class="form-control"
									name="councilAgendaDetailsForUpdate[${counter.index}].preamble.department.name"
									readonly="readonly" value="${contact.preamble.department.name}" /></td>
								<td><textarea class="form-control"
									name="councilAgendaDetailsForUpdate[${counter.index}].preamble.gistOfPreamble"
									readonly="readonly" rows="3" />${contact.preamble.gistOfPreamble}</textarea></td>
								<td><input type="text" class="form-control"
									name="councilAgendaDetailsForUpdate[${counter.index}].preamble.sanctionAmount"
									readonly="readonly" value="${contact.preamble.sanctionAmount}" /></td>
								<!--  <td><input type="hidden" class="form-control" name="agendaDetails[{{idx}}].preamble.department.id" {{readonly}} value="{{departmentId}}"/> -->
								<td><input type="hidden" class="form-control"
									name="councilAgendaDetailsForUpdate[${counter.index}].preamble.id" readonly="readonly"
									value="${contact.preamble.id}" />
									<button type="button" class="btn btn-xs btn-secondary delete">
										<span class="glyphicon glyphicon-trash"></span>&nbsp;Delete
									</button></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>

						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.noAgenda.Detail" />
						</div>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<div class="form-group">
		<div class="text-center">
			<button type='submit' class='btn btn-primary' id="buttonSubmit">
				<spring:message code='lbl.update' />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
		</div>
	</div>
		
		<input type="hidden" name="councilAgenda" value="${councilAgenda.id}" />
			<form:hidden path="agendaNumber" id="agendaNumber"
				value="${councilAgenda.agendaNumber}" />
		<%-- <input type="hidden" name="councilAgenda" value="${councilAgenda.agendaDetails}" /> --%>
			<%-- <form:hidden path="agendaDetails" id="agendaDetails"
				value="${councilAgenda.agendaDetails.id}" /> --%>
		
		<%-- <input type="hidden" name="CouncilAgendaDetails" value="${CouncilAgendaDetails.id}" />
			<form:hidden path="agenda" id="agenda"
				value="${CouncilAgendaDetails.agenda}" /> --%>
	</div>
	
	</div>
	
	
		
</form:form>

<script>

$('#btnsearchPreamble').click(function(e) {
	if ($('form').valid()) {
		return true;
	} else {
		e.preventDefault();
	}
});

</script>

<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.columnFilter.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/datetime-moment.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/app/js/councilAgenda.js?rnd=${app_release_no}'/>"></script>

