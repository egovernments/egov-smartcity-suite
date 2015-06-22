<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
	The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib  uri="http://www.joda.org/joda/time/tags" prefix="joda"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/font-awesome-4.3.0/css/font-awesome.min.css' context='/egi'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/egov/custom.css' context='/egi'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/egov/header-custom.css' context='/egi'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css' context='/egi'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"/>
<link rel="stylesheet" href="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>"/>

<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>	
<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/egov/custom.js' context='/egi'/>"></script>

<script src="<c:url value='/commonjs/ajaxCommonFunctions.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>

<div class="page-container" id="page-container">
			<header class="navbar navbar-fixed-top"><!-- set fixed position by adding class "navbar-fixed-top" -->
				
				<nav class="navbar navbar-default navbar-custom navbar-fixed-top">
					<div class="container-fluid">
						<div class="navbar-header col-md-10 col-xs-10">
							<a class="navbar-brand" href="javascript:void(0);">
								<img src="<c:url value='/resources/global/images/rmclogo.jpg' context='/egi'/>" height="60">
								<div>
									
									<span class="title2"><spring:message code="lbl.search.employee"/></span>
								</div>
							</a>
						</div>
						
						<div class="nav-right-menu col-md-2 col-xs-2">
							<ul class="hr-menu text-right">
								<li class="ico-menu">
									<a href="javascript:void(0);">
										<img src="<c:url value='/resources/global/images/logo@2x.png' context='/egi'/>" title="Powered by eGovernments" height="20px">
									</a>
								</li>
								
							</ul>
						</div>
						
					</div>
				</nav>
				
			</header>
		<form:form  class="form-horizontal form-groups-bordered"
					id="searchEmployeeForm" modelAttribute="employee" action="search">
			<div class="main-content">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary" data-collapsed="0">
							<div class="panel-heading">
								<div class="panel-title">
									<spring:message code="lbl.search.employee"/>
								</div>
							</div>
							<div class="panel-body custom-form">
								<form role="form" id="searchempform" class="form-horizontal form-groups-bordered">
									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.search"/><span class="mandatory"></span></label>
										
										<div class="col-sm-6 add-margin">
											<input name="searchText" type="text" class="form-control" placeholder="Search name, mobile number etc.."/>
										</div>
									</div>
									
									<div class="form-group">
										<div class="col-sm-3 col-xs-12 add-margin">
											<button type="button" class="btn btn-secondary adv-button" data-advanced=false><spring:message code="lbl.advanced"/></button>
										</div>
									</div>

									<div class="form-group advanced-forms display-hide">
										
										<div class="col-sm-4 add-margin">
											<input type="text" name="" class="form-control" placeholder="Name">
										</div>
										<div class="col-sm-4 add-margin">
											<select class="form-control" id="deptId" name="searchText">
												<option value="">
													<spring:message code="lbl.select" />
												</option>
												  <c:forEach items="${department}" var="dept">
										            <option value="${dept.name}">${dept.name} </option>
										         </c:forEach>
											</select>
										</div>
										<div class="col-sm-4 add-margin">
											<select class="form-control" id="desigId" name="searchText">
												<option value="">
													<spring:message code="lbl.select" />
												</option>
												  <c:forEach items="${desigList}" var="desig">
										            <option value="${desig.name}">${desig.name} </option>
										         </c:forEach>
											</select>
										</div>
										
									</div>

									<div class="form-group advanced-forms display-hide">
										
										<div class="col-sm-4 add-margin">
											<select class="form-control" id="functionId" name="searchText">
												<option value="">
													<spring:message code="lbl.select" />
												</option>
												  <c:forEach items="${functionList}" var="function">
										            <option value="${function.name}">${function.name} </option>
										         </c:forEach>
											</select>
										</div>
										<div class="col-sm-4 add-margin">
											<select class="form-control" id="functionaryId" name="searchText">
												<option value="">
													<spring:message code="lbl.select" />
												</option>
												  <c:forEach items="${functionaryList}" var="functionary">
										            <option value="${functionary.name}">${functionary.name} </option>
										         </c:forEach>
											</select>
										</div>
										<div class="col-sm-4 add-margin">
											<input type="text" name="" class="form-control" placeholder="Code">
										</div>
									</div>

									<div class="form-group advanced-forms display-hide">
										
										<div class="col-sm-4 add-margin">
											<select class="form-control" id="functionId" name="searchText">
												<option value="">
													<spring:message code="lbl.select" />
												</option>
												  <c:forEach items="${employeeStatus}" var="empStatus">
										            <option value="${empStatus}">${empStatus} </option>
										         </c:forEach>
											</select>
										</div>
										<div class="col-sm-4 add-margin">
											<select name="employeeType" id="employeeType" name="searchText" 
												class="form-control">
												<option value="">
													<spring:message code="lbl.select" />
												</option>
												 <c:forEach items="${employeeTypes}" var="empType">
										            <option value="${empType.name}">${empType.name} </option>
										         </c:forEach>
											</select>
											
										</div>
										<div class="col-sm-4 add-margin">
											<input type="text" name="searchText" class="form-control" placeholder="Aadhaar">
										</div>
										
									</div>

									<div class="form-group advanced-forms display-hide">
										<div class="col-sm-4 add-margin">
											<input type="text" name="searchText" class="form-control" placeholder="Mobile Number">
										</div>
										<div class="col-sm-4 add-margin">
											<input type="text" name="searchText" class="form-control" placeholder="Email">
										</div>
										<div class="col-sm-4 add-margin">
											<input type="text" name="searchText" class="form-control" placeholder="PAN">
										</div>
										
									</div>

										<div class="text-center">
											<button type="submit" class="btn btn-primary">Search Employee</button>
											<button class="btn btn-danger" type="reset">Reset</button>
											<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">Close</a>
										</div>
									</div>


								</form>
							</div>
						</div>					
					</div>
				</div>
			<c:if test="${employees != null}">
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
									<th>Actions</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach var="employee" items="${employees}" varStatus="status">
							<tr>
								<td><c:out value="${status.index + 1}"/></td>
								<td><c:out value="${employee.name}"/></td>
								<td><c:out value="${employee.code}"/><input type="hidden" value="${employee.code}" id="empCode"/></td>
								<fmt:formatDate value="${employee.assignments[0].fromDate}" pattern="dd/MM/yyyy" var="fromDate" />
								<fmt:formatDate value="${employee.assignments[0].toDate}" pattern="dd/MM/yyyy" var="toDate" />
								<td><c:out value="${employee.assignments[0].department.name}"/></td>
								<td><c:out value="${employee.assignments[0].designation.name}"/></td>
								<td><c:out value="${employee.assignments[0].position.name}"/></td>
								<td><c:out value="${fromDate}"/> - <c:out value="${toDate}"/></td>
								<td><button type="button" class="btn btn-xs btn-secondary edit-employee">
										<span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</button>
								<button type="button" class="btn btn-xs btn-secondary view-employee">
										<span class="glyphicon glyphicon-edit"></span>&nbsp;View</button></td>
							</tr>			
							</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</c:if>	

			</div>
</form:form>

		</div>
<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>		
<script src="<c:url value='/resources/js/app/employeesearch.js'/>"></script>