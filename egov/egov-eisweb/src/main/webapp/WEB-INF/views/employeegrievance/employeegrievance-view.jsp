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
<%@ include file="/includes/taglibs.jsp"%>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">${message }</div>
	</div>

	<div class="panel-body">
		<div class="row ">
			<div class="col-md-3 col-xs-6 add-margin">
				<spring:message code="lbl.grievancenumber" />
			</div>
			<div class="col-md-3  col-xs-6 add-margin view-content">
				${employeeGrievance.grievanceNumber}</div>
			<div class="col-md-3 col-xs-6 add-margin">
				<spring:message code="lbl.grievanceDate" />
			</div>


			<div class="col-md-3 col-xs-6 add-margin view-content">
				<fmt:formatDate value="${employeeGrievance.createdDate}"
					pattern="dd	-MM-yyyy HH:mm:ss" />
			</div>

		</div>
		<div class="row ">
			<div class="col-md-3 col-xs-6 add-margin">
				<spring:message code="lbl.employeecode" />
			</div>
			<div class="col-md-3 col-xs-6 add-margin view-content">
				${employeeGrievance.employee.code}</div>
			<div class="col-md-3 col-xs-6 add-margin">
				<spring:message code="lbl.employeename" />
			</div>
			<div class="col-md-3 col-xs-6 add-margin view-content">
				${employeeGrievance.employee.name}</div>

		</div>

		<div class="row ">
			<div class="col-md-3 col-xs-6 add-margin ">
				<spring:message code="lbl.employeegrievancetype" />
			</div>
			<div class="col-md-3 col-xs-6 add-margin  view-content">
				${employeeGrievance.employeeGrievanceType.name}</div>

			<div class="col-md-3 col-xs-6 add-margin ">
				<spring:message code="lbl.currentstatus" />
			</div>
			<div class="col-md-3 col-xs-6 add-margin  view-content">
				${employeeGrievance.status}</div>
		</div>
		<div class="row ">
			<div class="col-md-3 col-xs-6 add-margin ">
				<spring:message code="lbl.description" />
			</div>
			<div class="col-md-3 col-xs-6 add-margin  view-content">
				${employeeGrievance.details}</div>
			<c:if
				test="${employeeGrievance.grievanceDocs != null &&  !employeeGrievance.grievanceDocs.isEmpty()}">

				<div class="col-md-3 col-xs-6 add-margin ">
					<spring:message code="lbl.attachments" />
				</div>
				<div class="col-md-3 col-xs-6 add-margin ">

					<c:forEach items="${employeeGrievance.grievanceDocs}"
						var="documentDetials">
						<a
							href="/eis/employeegrievance/downloadfile/${documentDetials.fileStoreId}">${documentDetials.fileName }</a>
						<br />
					</c:forEach>
				</div>
			</c:if>
		</div>

		<div class="row add-border">
			<c:if test="${employeeGrievance.grievanceResolution != null}">
				<div class="col-md-3 col-xs-6 add-margin ">
					<spring:message code="lbl.grievanceresolution" />
				</div>
				<div class="col-md-3 col-xs-6 add-margin  view-content">
					${employeeGrievance.grievanceResolution}</div>
			</c:if>
		</div>

	</div>
</div>
<div class="row text-center">
	<div class="add-margin">
		<a href="javascript:void(0)" class="btn btn-default"
			onclick="self.close()">Close</a>
	</div>
</div>