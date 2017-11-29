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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
		<form:form  class="form-horizontal form-groups-bordered" 
					id="searchEmployeeForm" modelAttribute="employee" action="search">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary" data-collapsed="0">
							<div class="panel-heading">
								<div class="panel-title">
									<spring:message code="lbl.search.employee"/>
								</div>
							</div>
							<div class="panel-body">
									<div class="form-group advanced-forms">
										
										<div class="col-sm-4 add-margin">
											<form:input path="name" id="name" cssClass="form-control" placeholder="Name"/>
										</div>
										 <div class="col-sm-4 add-margin">
											<form:select cssClass="form-control" id="department" name="department" path="department">
												<form:option value="">
													<spring:message code="lbl.departments" />
												</form:option>
												  <c:forEach items="${department}" var="dept">
										            <form:option value="${dept.name}">${dept.name} </form:option>
										         </c:forEach>
											</form:select>
										</div>
										<div class="col-sm-4 add-margin">
											<form:select cssClass="form-control" id="designation" name="designation" path="designation">
												<form:option value="">
													<spring:message code="lbl.designations" />
												</form:option>
												  <c:forEach items="${desigList}" var="desig">
										            <form:option value="${desig.name}">${desig.name} </form:option>
										         </c:forEach>
											</form:select>
										</div>
										
									</div>

									<div class="form-group advanced-forms">
										
										<div class="col-sm-4 add-margin">
											<form:select cssClass="form-control" id="function" name="function" path="function">
												<form:option value="">
													<spring:message code="lbl.functions" />
												</form:option>
												  <c:forEach items="${functionList}" var="function">
										            <form:option value="${function.name}">${function.name} </form:option>
										         </c:forEach>
											</form:select>
										</div>
										<div class="col-sm-4 add-margin">
											<form:select cssClass="form-control" id="functionary" name="functionary" path="functionary">
												<form:option value="">
													<spring:message code="lbl.functionaries" />
												</form:option>
												  <c:forEach items="${functionaryList}" var="functionary">
										            <form:option value="${functionary.name}">${functionary.name} </form:option>
										         </c:forEach>
											</form:select>
										</div>
										<div class="col-sm-4 add-margin">
											<form:select cssClass="form-control" id="status" name="status" path="status">
												<form:option value="">
													<spring:message code="lbl.status" />
												</form:option>
												  <c:forEach items="${employeeStatus}" var="status">
										            <form:option value="${status}">${status} </form:option>
										         </c:forEach>
											</form:select>
										</div>
									</div>

									<div class="form-group advanced-forms">
										
										<div class="col-sm-4 add-margin">
											<form:select cssClass="form-control" id="employeeType" name="employeeType" path="employeeType">
												<form:option value="">
													<spring:message code="lbl.types" />
												</form:option>
												  <c:forEach items="${employeeTypes}" var="type">
										            <form:option value="${type.name}">${type.name} </form:option>
										         </c:forEach>
											</form:select>
										</div>
										<div class="col-sm-4 add-margin">
											<form:input type="text" name="pan" id="pan" cssClass="form-control" path="pan" placeholder="PAN"/>
										</div>
										<div class="col-sm-4 add-margin">
											<form:input type="text" name="aadhaar" id="aadhaar" cssClass="form-control" path="aadhaar" placeholder="Aadhaar"/>
										</div>
										
									</div>

									<div class="form-group advanced-forms">
										<div class="col-sm-4 add-margin">
											<form:input type="text" name="mobileNumber" id="mobileNumber" cssClass="form-control" path="mobileNumber" placeholder="Mobile Number"/>
										</div>
										<div class="col-sm-4 add-margin">
											<form:input type="text" name="email" id="email" cssClass="form-control" path="email" placeholder="Email"/>
										</div>
										<div class="col-sm-4 add-margin">
											<form:input type="text" name="code" id="code" cssClass="form-control" path="code" placeholder="Code"/>
										</div>
										
									</div>
									
									<div class="form-group advanced-forms">
								    
									 <label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.hod"/> :</label>
								
									<div class="col-sm-4 add-margin">
									<form:checkbox path="isHOD"/>
					                <form:errors path="isHOD" />
									</div>
									</div> 
		                            <input type="hidden" id="mode" name="mode" value="${mode}" />
										<div class="text-center">
											<button type="button"  class="btn btn-primary" id="searchbtn">Search Employee</button>
											<button class="btn btn-danger" type="reset">Reset</button>
											<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">Close</a>
										</div>
									</div>


							</div>
						</div>					
					</div>
				</form:form>
				<div class="row">
					<div class="col-md-6 col-xs-6 table-header">List of Employee</div>
					<div class="col-md-6 col-xs-6 add-margin text-right">
						<span class="inline-elem">Search</span>
						<span class="inline-elem"><input type="text" id="searchemployee" class="form-control input-sm"></span>
					</div>
					<div class="col-md-12">
						<table class="table table-bordered datatable" id="employee-table">
							<thead>
								<tr>
									<th>S.No</th>
									<th>Employee Name</th>
									<th>Code</th>
									<th>Department</th>
									<th>Designation</th>
									<th>Position</th>
									<th>Date Range</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>


<script src="<cdn:url value='/resources/js/app/employeesearch.js'/>"></script>
<link rel="stylesheet" href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>		
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
