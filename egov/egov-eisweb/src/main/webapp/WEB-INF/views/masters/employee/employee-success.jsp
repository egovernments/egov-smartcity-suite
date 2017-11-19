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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.joda.org/joda/time/tags" prefix="joda"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<script src="<cdn:url value='/resources/js/app/designation.js'/>"></script>
		 
			
			<form:form  method ="post" action="create" class="form-horizontal form-groups-bordered" modelAttribute="employee" id="employeeForm" >
				<div class="row">
						<div class="col-md-12">
							<c:if test="${not empty message}">
                    <div class="alert alert-success" role="alert">${message}</div>
                </c:if>
							<div class="panel panel-primary" data-collapsed="0">
								<div class="panel-heading">
									<div class="panel-title">
										<spring:message code="title.basic"/>
									</div>
									
								</div>
								<div class="panel-body">
									<div class="row add-border">
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.name"/> 
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<c:out value="${employee.name}"/>
										</div>
										
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.gender"/>
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<c:out value="${employee.gender}"/>
										</div>
									</div>
									<div class="row add-border">
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.mobile"/>  
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<c:out value="${employee.mobileNumber}"/>
										</div>
										
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.email"/>
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<c:out value="${employee.emailId}"/>
										</div>
									</div>
									<div class="row add-border">
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.altcontact"/>
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<c:out value="${employee.altContactNumber}"/>
										</div>
										
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.DOB"/>
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
										<fmt:formatDate value="${employee.dob}" var="DOB"
															pattern="dd/MM/yyyy" />
											<c:out value="${DOB}"/>
										</div>
									</div>
									<div class="row add-border">
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.DOA"/>
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
										<joda:format value="${employee.dateOfAppointment}" var="DOA"
											pattern="dd-MM-yyyy" />
											<c:out value="${DOA}"/>
										</div>
										
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.status"/>
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<c:out value="${employee.employeeStatus}"/>
										</div>
									</div>
									<div class="row add-border">
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.emptype"/>
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<c:out value="${employee.employeeType.name}"/>
										</div>
										
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.code"/>
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<c:out value="${employee.code}"/>
										</div>
									</div>
									<div class="row add-border">
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.pan"/>
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<c:out value="${employee.pan}"/>
										</div>
										
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.aadhar"/>
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<c:out value="${employee.aadhaarNumber}"/>
										</div>
									</div>
									<div class="row add-border">
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.sign"/>
										</div>
										<c:if test="${not empty image}">
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<img width="100" height="70" src='data:image/png;base64,${image}' /> 
										</div>	
										</c:if>
									</div>
									<div class="row">
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.useractive.view"/>
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<c:if test="${employee.active==true}" >
												Yes
											</c:if>
											<c:if test="${employee.active==false}" >
												No
											</c:if>
										</div>
										
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.username"/>
										</div>
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<c:out value="${employee.username}"/>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					
					<div class="row form-group">
										<div class="col-md-6 col-xs-6 table-header">Assignment Details</div>
										<table id="assignmentTable" class="table table-bordered">
										<thead>
											<th class="col-sm-2 table-div-column">Date Range</th>
											<th class="col-sm-2 table-div-column">Is Primary</th>
											<th class="col-sm-2 table-div-column">Department</th>
											<th class="col-sm-2 table-div-column">Designation</th>
											<th class="col-sm-2 table-div-column">Position</th>
											<th class="col-sm-2 table-div-column">HOD Department</th>
										</thead>
											<tbody>
											<c:forEach var="assignment" items="${employee.assignments}" varStatus="status">
												<tr>
													<td>
														<form:input type="hidden"
															id="assignments[${status.index}].fromDate"
															path="assignments[${status.index}].fromDate"
															/> 
														<form:input type="hidden"
															id="assignments[${status.index}].toDate"
															path="assignments[${status.index}].toDate"
															/> 
														
														<fmt:formatDate value="${assignment.fromDate}" var="fromDate"
															pattern="dd/MM/yyyy" />
														<fmt:formatDate value="${assignment.toDate}" var="toDate"
															pattern="dd/MM/yyyy" />
														<input type="text" id="table_date_range${status.index}" 
															class="form-control" readonly="readonly" style="text-align:center"
															value="<c:out value="${fromDate} - ${toDate}"/>"/>
													</td>
													<td>
														<form:input type="hidden"
															id="assignments[${status.index}].primary"
															path="assignments[${status.index}].primary"
															/>
																<c:if test="${assignment.primary==true}" >
																<input type="text" id="table_department${status.index}" class="form-control" 
																	readonly="readonly" style="text-align:center" value="Yes"/>
																</c:if>
																<c:if test="${assignment.primary==false}">
																	<input type="text" id="table_department${status.index}" class="form-control" 
																	readonly="readonly" style="text-align:center" value="No"/>
																</c:if>
													</td>
													<td>
														<form:input type="hidden"
															id="assignments[${status.index}].department"
															path="assignments[${status.index}].department"
															/>
															<input type="text" id="table_department${status.index}" class="form-control" 
															readonly="readonly" style="text-align:center" value="${assignment.department.name}"/>
															
													</td>
													<td>
														<form:input type="hidden"
															id="assignments[${status.index}].designation"
															path="assignments[${status.index}].designation"
															/>
															<input type="text" id="table_designation${status.index}" class="form-control" 
															readonly="readonly" style="text-align:center" value="${assignment.designation.name}"/>
													</td>
													<td>
														<input type="text" id="table_position${status.index}" class="form-control" 
														readonly="readonly" style="text-align:center" value="${assignment.position.name}"/>
														<form:input type="hidden"
															id="assignments[${status.index}].position"
															path="assignments[${status.index}].position"
															/>
														<form:input type="hidden"
															id="assignments[${status.index}].fund"
															path="assignments[${status.index}].fund"
															/>
														<form:input type="hidden"
															id="assignments[${status.index}].function"
															path="assignments[${status.index}].function"
															/>
														<form:input type="hidden"
															id="assignments[${status.index}].functionary"
															path="assignments[${status.index}].functionary"
															/>
														<form:input type="hidden"
															id="assignments[${status.index}].grade"
															path="assignments[${status.index}].grade"
															/>	
														<form:input type="hidden"
															id="assignments[${status.index}].employee"
															path="assignments[${status.index}].employee"
															/>
													</td>
													<td><c:if test="${assignment.deptSet!=null}">  								
															<c:forEach var="hodDept" items="${assignment.deptSet}" varStatus="hodeptStatus">
																<input type="text" id="assignments[${status.index}].hodDept[${hodeptStatus.index}].hod"
																	name="assignments[${status.index}].hodDept[${hodeptStatus.index}].hod.id"
																	value="${hodDept.hod.name}"/>						
															</c:forEach>  		
																
													  </c:if>  </td>
												</tr>
											</c:forEach>
											</tbody>
										</table>
									</div>
									<div class="row form-group">
										<div class="col-md-6 col-xs-6 table-header">Jurisdiction Details</div>
										<table id="assignmentTable" class="table table-bordered">
										<thead>
											<th class="col-sm-2 table-div-column">Boundary Type</th>
											<th class="col-sm-2 table-div-column">Boundary</th>
										</thead>
											<tbody>
											<c:forEach var="jurisdiction" items="${employee.jurisdictions}" varStatus="status">
												<tr>
													<td>
														<form:input type="hidden"
															id="jurisdictions[${status.index}].boundaryType"
															path="jurisdictions[${status.index}].boundaryType"
															/>
															<input type="text" id="table_boundaryType${status.index}" class="form-control" 
															readonly="readonly" style="text-align:center" value="${jurisdiction.boundaryType.name}"/>
															
													</td>
													<td>
														<form:input type="hidden"
															id="jurisdictions[${status.index}].boundary"
															path="jurisdictions[${status.index}].boundary"
															/>
															<input type="text" id="table_boundary${status.index}" class="form-control" 
															readonly="readonly" style="text-align:center" value="${jurisdiction.boundary.name}"/>
													</td>
												</tr>
											</c:forEach>
											</tbody>
										</table>
									</div>
									
									<div class="row text-center">
										<div class="row">
										<a href="javascript:void(0)" class="btn btn-default"
											onclick="self.close()"><spring:message code="lbl.close" /></a>
						</div>
					</div>
					
			</form:form>
