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
<script src="<c:url value='/resources/js/app/employeecreate.js'/>"></script>

<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/font-awesome-4.3.0/css/font-awesome.min.css' context='/egi'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/egov/custom.css' context='/egi'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/egov/header-custom.css' context='/egi'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css' context='/egi'/>">

<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>	
	
	<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js' context='/egi'/>"></script>
	<script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
	<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"/>
<script src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script src="<c:url value='/commonjs/ajaxCommonFunctions.js' context='/egi'/>"></script>

		<!--[if lt IE 9]><script src="resources/js/ie8-responsive-file-warning.js"></script><![endif]-->
		
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
			<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
		
<div class="row" id="page-content">
	<div class="col-md-12">
		<div class="panel" data-collapsed="0">
		<form:form  method ="post" action="" class="form-horizontal form-groups-bordered" modelAttribute="employee" id="employeeForm" >
			<div class="panel-body">
				 <c:if test="${not empty message}">
                    <div id="message" class="success">${message}</div>
                </c:if>
                
                <div class="row">
					<div class="col-md-12">
						
						<div class="panel panel-primary" data-collapsed="0">
							
							<div class="panel-heading">
								<div class="panel-title">
									<spring:message code="title.empdetails"/>
								</div>
								
							</div>
							
							<div class="panel-body custom-form">
								<form:hidden path="password" value="804Mqu@123"/>
								<form:hidden path="locale" value="en IN"/>
								<form:hidden path="pwdExpiryDate" id="pwdExpiryDate" value="01/01/2015"/>
								<input type="hidden" value="${mode}" id="mode"/>
									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.name"/><span class="mandatory"></span></label>
										<div class="col-sm-2 col-md-1 add-margin">
											<form:select path="salutation" id="salutation"
												cssClass="form-control" cssErrorClass="form-control error">
												<form:option value="">
													Mr
												</form:option>
												<form:option value="">
													Miss
												</form:option>
												<form:option value="">
													Mrs
												</form:option>
											</form:select>
										</div>
										<div class="col-sm-4 col-md-5 add-margin">
											<form:input type="text" path="name" id="name" cssClass="form-control is_valid_alphabet" maxlength="100"/>
										</div>
									</div>

									<div class="form-group">
										
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.gender"/><span class="mandatory"></span></label>
										
										<div class="col-sm-2 col-xs-12 add-margin">
											<form:radiobutton path="gender" id="gender" value="MALE" checked="true"/>
											<label>Male</label>
										</div>
										<div class="col-sm-2 col-xs-12 add-margin">
											<form:radiobutton path="gender" id="gender" value="FEMALE"/>
											<label>Female</label>
										</div>
										<div class="col-sm-2 col-xs-12 add-margin">
											<form:radiobutton path="gender" id="gender" value="TRANSGENDER"/>
											<label>Transgender</label>
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.mobile"/><span class="mandatory"></span></label>
										
										<div class="col-sm-2 add-margin">
											<form:input type="text" path="mobileNumber" id="mobileNumber" data-inputmask="'mask': '9999999999'" cssClass="form-control" maxlength="10" placeholder="Mobile Number"/>
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label"><spring:message code="lbl.email"/><span class="mandatory"></span></label>
										</div>
										
										<div class="col-sm-3">
											<form:input type="text" cssClass="form-control" id="emial" path="emailId" placeholder="abc@xyz.com"/>
										</div>
										
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.altcontact"/><span class="mandatory"></span></label>
										
										<div class="col-sm-2 add-margin">
											<form:input type="text" path="altContactNumber" id="altcontact" cssClass="form-control" data-inputmask="'mask': '9999999999'" maxlength="10" placeholder="Mobile Number"/>
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label"><spring:message code="lbl.DOB"/><span class="mandatory"></span></label>
										</div>
										
										<div class="col-sm-3 add-margin">
											<form:input id="DOB" path="dateOfBirth" type="text" class="form-control datepicker" data-inputmask="'mask': 'd/m/y'" placeholder="24/01/1992" />
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.DOA"/>t<span class="mandatory"></span></label>
										
										<div class="col-sm-2 add-margin">
											<form:input id="DOA" path="dateOfAppointment" type="text" cssClass="form-control datepicker" data-inputmask="'mask': 'd/m/y'" placeholder="24/01/1992"/>
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label"><spring:message code="lbl.status"/><span class="mandatory"></span></label>
										</div>
										<div class="col-sm-3 add-margin">
											<form:select path="employeeStatus" id="employeeStatus"
												cssClass="form-control" cssErrorClass="form-control error">
												<form:option value="">
													<spring:message code="lbl.select" />
												</form:option>
												<form:options items="${employeeStatus}" />
											</form:select>
											<form:errors path="employeeStatus" cssClass="error-msg" />
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.emptype"/><span class="mandatory"></span></label>
										
										<div class="col-sm-2 add-margin">
											<form:select path="employeeType" id="employeeType"
												cssClass="form-control" cssErrorClass="form-control error">
												<form:option value="">
													<spring:message code="lbl.select" />
												</form:option>
												<form:options items="${employeeTypes}" itemValue="id"
													itemLabel="name" />
											</form:select>
											<form:errors path="employeeType" cssClass="error-msg" />
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label"><spring:message code="lbl.code"/><span class="mandatory"></span></label>
										</div>
										
										<div class="col-sm-3">
											<form:input path="code" id="code" type="text" cssClass="form-control is_valid_alphanumeric"/>
										</div>
										
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.pan"/><span class="mandatory"></span></label>
										
										<div class="col-sm-2 add-margin">
											<form:input path="pan" id="pan" type="text" cssClass="form-control is_valid_alphanumeric" maxlength="10"/>
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label"><spring:message code="lbl.aadhar"/><span class="mandatory"></span></label>
										</div>
										
										<div class="col-sm-3 add-margin">
											<form:input type="text" id="aadhaarNumber" path="aadhaarNumber" cssClass="form-control is_valid_nuber" maxlength="12"/>
										</div>
									</div>


									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.useractive"/><span class="mandatory"></span></label>
										
										<div class="col-sm-1 col-xs-12 add-margin">
											<form:radiobutton path="active" id="isactive_yes" value="true" checked="true"/>
											<label>Yes</label>
										</div>

										<div class="col-sm-1 col-xs-12 add-margin">
											<form:radiobutton path="active" id="isactive_no" value="false"/>
											<label>No</label>
										</div>

										<label for="field-1" class="col-sm-1 control-label"><spring:message code="lbl.username"/></label>
										
										<div class="col-sm-3 add-margin">
											<form:input type="text" path="username" id="username" cssClass="form-control"/>
										</div>

									</div>

									<div class="panel-heading custom_form_panel_heading">
										<div class="panel-title">
											Assignment Details
										</div>
										
									</div>
									
									
									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Is Primary<span class="mandatory"></span></label>
										
										<div class="col-sm-1 col-xs-12 add-margin">
											<input type="radio" id="primary_yes" name="isprimary" value="true" >
											<label>Yes</label>
										</div>

										<div class="col-sm-1 col-xs-12 add-margin">
											<input type="radio" id="primary_no" name="isprimary" value="false" checked>
											<label>No</label>
										</div>


									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Date Range<span class="mandatory"></span></label>
										
										<div class="col-sm-3">
											<input type="text" class="form-control datepicker" name="fromDate" id="fromDate"
												data-inputmask="'mask': 'd/m/y'" placeholder="From Date"/>
							 				<div class="error-msg fromdateerror all-errors display-hide"></div>
										</div>
										
										
										<div class="col-sm-3">
											<input type="text" class="form-control datepicker" name="toDate" id="toDate"
												data-inputmask="'mask': 'd/m/y'" placeholder="From Date"/>
							 				<div class="error-msg todateerror all-errors display-hide"></div>
										</div>
										
									</div>
									
									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Department<span class="mandatory"></span></label>
										
										<div class="col-sm-6 add-margin">
											<select class="form-control" id="deptId">
												<option value="">
													<spring:message code="lbl.select" />
												</option>
												  <c:forEach items="${department}" var="dept">
										            <option value="${dept.id}">${dept.name} </option>
										         </c:forEach>
											</select>
											<input type="hidden" id="deparmentId" value=""/>
											<div class="error-msg departmenterror all-errors display-hide"></div>
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Designation<span class="mandatory"></span></label>
										
										<div class="col-sm-6 add-margin">
											<input id="designationName" type="text" class="form-control typeahead is_valid_letters_space_hyphen_underscore" autocomplete="off"
													required="required" value="${assignment.designation.name}" placeholder="Designation">
													<input type="hidden" id="designationId" value=""/>
												<c:forEach items="${designations}" var="designation">
													<a onclick="setDesignationId(<c:out value="${designation.id}"/>)" href="javascript:void(0)" class="btn btn-secondary btn-xs tag-element freq-ct"><c:out
															value="${designation.name }" /> </a>
												</c:forEach>
												<div class="error-msg designationerror all-errors display-hide"></div>
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Position<span class="mandatory"></span></label>
										
										<div class="col-sm-6 add-margin">
												<input id="positionName" type="text" class="form-control typeahead is_valid_letters_space_hyphen_underscore" autocomplete="off"
													required="required" value="${assignment.position.name}" placeholder="Position">
													<input type="hidden" id="positionId" value=""/>
												<c:forEach items="${positions}" var="position">
													<a onclick="setPositionId(<c:out value="${position.id}"/>)" href="javascript:void(0)" class="btn btn-secondary btn-xs tag-element freq-ct"><c:out
															value="${position.name }" /> </a>
												</c:forEach>
												<div class="error-msg positionerror all-errors display-hide"></div>
										</div>
									</div>
						
									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Fund</label>
										
										<div class="col-sm-2 add-margin">
											<select class="form-control" id="fundId">
												<option value="">
													<spring:message code="lbl.select" />
												</option>
												  <c:forEach items="${fundList}" var="fund">
										            <option value="${fund.id}">${fund.name} </option>
										         </c:forEach>
											</select>
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label">Function</label>
										</div>
										
										<div class="col-sm-3 add-margin">
											<select class="form-control" id="functionId">
												<option value="">
													<spring:message code="lbl.select" />
												</option>
												  <c:forEach items="${functionList}" var="function">
										            <option value="${function.id}">${function.name} </option>
										         </c:forEach>
											</select>
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Grade</label>
										
										<div class="col-sm-2 add-margin">
											<select class="form-control" id="gradeId">
												<option value="">
													<spring:message code="lbl.select" />
												</option>
												  <c:forEach items="${gradeList}" var="grades">
										            <option value="${grades.id}">${grades.name} </option>
										         </c:forEach>
											</select>
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label">Functionary</label>
										</div>
										
										<div class="col-sm-3 add-margin">
											<select class="form-control" id="functionaryId">
												<option value="">
													<spring:message code="lbl.select" />
												</option>
												  <c:forEach items="${functionaryList}" var="functionary">
										            <option value="${functionary.id}">${functionary.name} </option>
										         </c:forEach>
											</select>
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">If Head ofDepartment<span class="mandatory"></span></label>
										
										<div class="col-sm-1 col-xs-12 add-margin">
											<input type="radio" id="isHodYes" name="ishod" value="">
											<label>Yes</label>
										</div>

										<div class="col-sm-1 col-xs-12 add-margin">
											<input type="radio" id="isHodNo" name="ishod" value="" checked>
											<label>No</label>
										</div>
										
										<div class="col-sm-6 add-margin" style="display:none" id="hodDeptDiv">
											<select class="form-control" id="hodDeptId" multiple="multiple" size="6">
												  <c:forEach items="${department}" var="dept">
										            <option value="${dept.id}">${dept.name} </option>
										         </c:forEach>
											</select>
										</div>

									</div>

									<div class="form-group">
										<div class="text-center">
											<button type="button" id="btn-add" 
												class="btn btn-primary" >Add / Modify</button>
											<button class="btn btn-danger" type="button">Reset</button>
										</div>
									</div>

									<div class="row form-group">
										<table id="assignmentTable" class="table table-bordered">
										<thead>
										<div class="col-sm-12 table-div-border view-content header-color hidden-xs">
											<th class="col-sm-2 table-div-column">Date Range</th>
											<th class="col-sm-2 table-div-column">Is Primary</th>
											<th class="col-sm-2 table-div-column">Department</th>
											<th class="col-sm-2 table-div-column">Designation</th>
											<th class="col-sm-2 table-div-column">Position</th>
											<th class="col-sm-2 table-div-column">Actions</th>
										</div>
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
															path="assignments[${status.index}].department.id"
															/>
															<input type="text" id="table_department${status.index}" class="form-control" 
															readonly="readonly" style="text-align:center" value="${assignment.department.name}"/>
															
													</td>
													<td>
														<form:input type="hidden"
															id="assignments[${status.index}].designation"
															path="assignments[${status.index}].designation.id"
															/>
															<input type="text" id="table_designation${status.index}" class="form-control" 
															readonly="readonly" style="text-align:center" value="${assignment.designation.name}"/>
													</td>
													<td>
														<input type="text" id="table_position${status.index}" class="form-control" 
														readonly="readonly" style="text-align:center" value="${assignment.position.name}"/>
														<form:input type="hidden"
															id="assignments[${status.index}].position"
															path="assignments[${status.index}].position.id" 
															/>
														<form:input type="hidden"
															id="assignments[${status.index}].fund"
															path="assignments[${status.index}].fund.id"
															/>
														<form:input type="hidden"
															id="assignments[${status.index}].function"
															path="assignments[${status.index}].function.id"
															/>
														<form:input type="hidden"
															id="assignments[${status.index}].functionary"
															path="assignments[${status.index}].functionary.id"
															/>
														<form:input type="hidden"
															id="assignments[${status.index}].grade"
															path="assignments[${status.index}].grade.id"
															/>	
														<form:input type="hidden"
															id="assignments[${status.index}].employee"
															path="assignments[${status.index}].employee.id"
															/>
													</td>
													<td>
														<span class="parallel-actions"><i id="edit_row" class="fa fa-edit" value="${status.index}"></i></span>
													</td>
												</tr>
											</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				
				<div class="row">
					<div class="text-center">
						<button type="submit" class="btn btn-primary"><spring:message code="lbl.submit"/></button>
						<a href="javascript:void(0);" id="com_cancel" class="btn btn-default">Cancel</a>
					</div>
				</div>
                
                
                
             </div>
             </form:form>
         </div>
    </div>

