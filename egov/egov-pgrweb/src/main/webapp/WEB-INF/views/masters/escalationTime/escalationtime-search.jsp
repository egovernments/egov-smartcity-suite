<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<script>
	function deleteRow(obj) {
		var tb1 = document.getElementById("escalationTable");
		var lastRow = (tb1.rows.length) - 1;
		var curRow = getRow(obj).rowIndex;
		if (lastRow == 1) {
			bootbox.alert('you cannot delete this row ');
			return false;
		} else {
			tb1.deleteRow(curRow);
			resetSrNo();
			return true;
		}
	}
	
	function getRow(obj)    
	{
	 if(!obj)return null;
	 tag = obj.nodeName.toUpperCase();
	 while(tag != 'BODY'){
	  if (tag == 'TR') return obj;
	  obj=obj.parentNode ;
	  tag = obj.nodeName.toUpperCase();
	 }
	 return null;
	}
	
	function resetSrNo() {
		var tb1 = document.getElementById("escalationTable");
		var lastRow = (tb1.rows.length) - 1;
	}
	function addRow() {
		var table = document.getElementById("escalationTable");
		var rowCount = table.rows.length;
		var row = table.insertRow(rowCount);
		var counts = rowCount - 1;
		var newRow = document.createElement("tr");

		var newCol = document.createElement("td");
		newRow.appendChild(newCol);
		var cell1 = row.insertCell(0);
		cell1.innerHTML = counts + 1;
		var newCol = document.createElement("td");
		newRow.appendChild(newCol);
		var cell1 = row.insertCell(1);
		var description = document.createElement("input");
		description.type = "text";
		description.setAttribute("class", "form-control is_valid_alphanumeric");
		description.setAttribute("id", "escalationDescription");
		description.setAttribute("required", "required");

		description.name = "escalationList[" + counts
				+ "].designation.name";
		cell1.appendChild(description);

		var newCol = document.createElement("td");
		newRow.appendChild(newCol);
		var cell2 = row.insertCell(2);
		var numberofHour = document.createElement("input");
		numberofHour.type = "text";
		numberofHour.setAttribute("pattern", "/[^0-9]/g");
		numberofHour.setAttribute("maxlength", "5");
		numberofHour.setAttribute("required", "required");
		numberofHour.className = "form-control is_valid_number";

		numberofHour.name = "escalationList[" + counts + "].noOfHrs";
		numberofHour.setAttribute("class", "form-control");
		numberofHour.setAttribute("id", "escalationFormId");

		cell2.appendChild(numberofHour);

		var newCol = document.createElement("td");
		newRow.appendChild(newCol);
		var cell3 = row.insertCell(3);
		
		var addButton = document.createElement("input");
		addButton.type = "button";
		addButton.setAttribute("class", "btn btn-primary");
		addButton.setAttribute("onclick", "return deleteRow(this);");
		addButton.setAttribute("value", "Delete");
		cell3.appendChild(addButton);

	}
</script>
<div class="row">
	<div class="col-md-12">
		<div class="" data-collapsed="0">
			<c:if test="${not empty message}">
				<div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
			</c:if>
			<form:form id="searchEscalationTimeForm" method="post"
				class="form-horizontal form-groups-bordered"
				modelAttribute="escalationForm">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading ">
						<div class="panel-title">
							<strong><spring:message	code="lbl.escalationTime.heading.search" /></strong>
						</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-sm-3 control-label"><spring:message
									code="lbl.escalationTime.complaintType" /> <span class="mandatory"></span> </label>
							<div class="col-sm-6">
								<form:input id="com_type" path="complaintType.name" type="text"
									class="form-control typeahead is_valid_alphabet"
									placeholder="" autocomplete="off" required="required" />
								<input type=hidden id="mode" value="${mode}">
								<form:hidden path="complaintType.id" id="complaintTypeId" value="${complaintType.id}" />
								<form:errors path="complaintType.id" cssClass="add-margin error-msg" />
							</div>
						</div>

						<div class="form-group">
							<div class="text-center">
								<button type="submit" id="escalationTimeSearch" class="btn btn-primary">
									<spring:message code="lbl.escalationTime.button.search" />
								</button>
								<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">
									<spring:message code="lbl.close" /></a>
							</div>
						</div>
					</div>
				</div>

				<div id="noescalationDataFoundDiv" class="row container-msgs">
					<c:if test="${mode == 'noDataFound'}">
						<div class="form-group">
							<div class="text-center">
								<div class="panel-title view-content">
									<spring:message code="lbl.escalationTime.label.NodataFound" />
								</div>
								<br>
								<button type="button" id="escalationnewEscalation"	class="btn btn-primary">
									<spring:message	code="lbl.escalationTime.button.addnewEscalation" />
								</button>
							</div>
						</div>
					</c:if>
				</div>
			</form:form>
			<form id="saveEscalationTimeForm" method="post"	class="form-horizontal form-groups-bordered" modelAttribute="escalationForm">
				<div id="escalationDiv" class="hidden">
					<form:hidden path="escalationForm.complaintType.id"
						id="formcomplaintTypeId"
						value="${escalationForm.complaintType.id}" />
					<form:hidden path="escalationForm.complaintType.name"
						id="formcomplaintTypename"
						value="${escalationForm.complaintType.name}" />
					<table id="escalationTable" table width="100%" border="0" cellpadding="0" cellspacing="0" class="table table-bordered">
						<thead>
							<tr>
								<th><spring:message code="lbl.escalationTime.button.srNo" /></th>
								<th><spring:message code="lbl.escalationTime.designation" /></th>
								<th><spring:message code="lbl.escalationTime.noOfHours" /></th>
								<th><spring:message code="lbl.escalation.action" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="contact" items="${escalationForm.escalationList}" varStatus="status">
								<tr>
									<td>${status.count}</td>
									<td><input type="text"
										id="escalationDescription${status.index}"
										class="form-control is_valid_alphanumeric escalationDescription${status.index}"
										value="${contact.designation.name}"
										name="escalationList[${status.index}].designation.name"
										required="required"></td>

									<td><input type=hidden
										id="escalationFormId${status.index}"
										name="escalationList[${status.index}].designation.id"
										value="${contact.designation.id}"> <input
										type=hidden id="escalationId"
										name="escalationList[${status.index}].id"> <input
										type="text" class="form-control is_valid_number"
										value="${contact.noOfHrs}" maxlength="5"
										name="escalationList[${status.index}].noOfHrs"
										required="required"></td>

									<td>
										<button type="button" onclick="deleteRow(this)" id="Add"
											class="btn btn-primary"><spring:message code="lbl.delete"/></button>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="form-group">
						<div class="text-center">
							<button type="button" id="addNewRow" class="btn btn-primary">
								<spring:message code="lbl.escalationTime.button.add" />
							</button>
							<button type="submit" id="saveEscalationTime"
								class="btn btn-primary">
								<spring:message code="lbl.escalationTime.button.save" />
							</button>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript" src="<cdn:url  value='/resources/js/app/escalationtime-search.js?rnd=${app_release_no}'/>"></script>
