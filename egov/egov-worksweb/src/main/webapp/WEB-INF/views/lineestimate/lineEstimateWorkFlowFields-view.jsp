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
  
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${fieldsRequiredMap.contractCommitteeDetailsRequired == true}">
<div class="panel panel-primary contractcommittee" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message  code="lbl.contractcomittee.details"/>
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered" id="tblestimate">
			<thead>
				<tr>
					<th><spring:message code="lbl.contractcomitteeNumber"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.contractcomitteeDate"/><span class="mandatory"></span></th>
				</tr>
			</thead>
			<tbody >
			<tr>
					<td>
						<c:out value="${lineEstimate.contractCommitteeApprovalNumber }"></c:out>
					</td>
					<td>
						<fmt:formatDate value="${lineEstimate.contractCommitteeApprovalDate }" pattern="dd/MM/yyyy"/>
					</td>
					</tr>
			</tbody>
			</table>
			</div>
	</div>
</c:if>
<c:if test="${fieldsRequiredMap.standingCommitteeDetailsRequired == true}">
	<div class="panel panel-primary standingcommittee" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message  code="lbl.standingcommittee.details"/>
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered" id="tblestimate">
			<thead>
				<tr>
					<th><spring:message code="lbl.standingcommitteenumber"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.standingcommitteedate"/><span class="mandatory"></span></th>
				</tr>
			</thead>
			<tbody >
			<tr>
				<td>
					<c:out value="${lineEstimate.standingCommitteeApprovalNumber }"></c:out>
				</td>
				<td>
					<fmt:formatDate value="${lineEstimate.standingCommitteeApprovalDate }" pattern="dd/MM/yyyy"/>
				</td>
				</tr>
			</tbody>
			</table>
			</div>
	</div>
</c:if>

<c:if test="${fieldsRequiredMap.councilResolutionDetailsRequired == true}">
<div class="panel panel-primary councilresolution" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message  code="lbl.councilresolution.details"/>
	</div>
</div>
<div class="panel-body councilresolutiontbl">
	<table class="table table-bordered" id="councilresolutiontbl">
		<thead>
			<tr>
				<th><spring:message code="lbl.councilresolutionnumber"/><span class="mandatory"></span></th>
				<th><spring:message code="lbl.councilresolutiondate"/><span class="mandatory"></span></th>
			</tr>
		</thead>
		<tbody >
		<tr>
			<td>
				<c:out value="${lineEstimate.councilResolutionNumber }"></c:out>
			</td>
			<td>
				<fmt:formatDate value="${lineEstimate.councilResolutionDate }" pattern="dd/MM/yyyy"/>
			</td>
		</tr>
		</tbody>
		</table>
		</div>
</div>
</c:if>
<c:if test="${fieldsRequiredMap.governmentApprovalRequired == true}">
<div class="panel panel-primary governmentapproval" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message  code="lbl.governmentapproval.details"/>
	</div>
</div>
<div class="panel-body governmentapproval">
	<table class="table table-bordered" id="tblgovernmentapproval">
		<thead>
			<tr>
				<th><spring:message code="lbl.governmentapprovalnumber"/><span class="mandatory"></span></th>
				<th><spring:message code="lbl.governmentapprovaldate"/><span class="mandatory"></span></th>
			</tr>
		</thead>
		<tbody >
		<tr>
			<td>
				<c:out value="${lineEstimate.governmentApprovalNumber }"></c:out>
			</td>
			<td>
				<fmt:formatDate value="${lineEstimate.governmentApprovalDate }" pattern="dd/MM/yyyy"/>
			</td>
				</tr>
		</tbody>
		</table>
		</div>
</div>
</c:if>