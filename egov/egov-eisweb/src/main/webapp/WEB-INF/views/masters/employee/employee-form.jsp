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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<script src="<cdn:url value='/resources/js/app/employeecreate.js'/>"></script>


<link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/typeahead.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"/>

<script src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>

<script src="<cdn:url value='/resources/js/app/ajaxCommonFunctions.js'/>"></script>

		
		
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="/egi/resources/global/js/ie8/html5shiv.min.js"></script>
			<script src="/egi/resources/global/js/ie8/respond.min.js"></script>
		<![endif]-->
		
<div class="row">
	<div class="col-md-12">
		<form:form  method ="post" action="" class="form-horizontal form-groups-bordered" modelAttribute="employee" id="employeeForm" enctype="multipart/form-data" >
				 <c:if test="${not empty message}">
                    <div id="message" class="success">${message}</div>
                    <div class="alert alert-success" role="alert">${message}</div>
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
							<c:if test="${mode == 'create'}">
                    			<form:hidden path="password" value="12345678"/>
                            </c:if>
				 			<c:if test="${mode == 'update'}">
                    			<form:hidden path="password" value="${employee.password}"/>
                            </c:if>
								<form:hidden path="locale" value="en_IN"/>
								<input type="hidden" value="" id="removedJurisdictionIds" name ="removedJurisdictionIds"/>
								<input type="hidden" value="" id="removedassignIds" name ="removedassignIds"/>
								<input type="hidden" value="" id="editassignIds" name ="editassignIds"/>
								<input type="hidden" value="${mode}" id="mode"/>
									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.name"/><span class="mandatory"></span></label>
										<div class="col-sm-2 col-md-1 add-margin">
											<form:select path="salutation" id="salutation" 
												cssClass="form-control" cssErrorClass="form-control error">
												<form:option value="MR.">
													Mr
												</form:option>
												<form:option value="MISS.">
													Miss
												</form:option>
												<form:option value="MRS.">
													Mrs
												</form:option>
											</form:select>
										</div>
										<div class="col-sm-4 col-md-5 add-margin">
											<form:input type="text" path="name" id="name" cssClass="form-control is_valid_alphabet" maxlength="100" required="required"/>
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
											<form:radiobutton path="gender" id="gender" value="OTHERS"/>
											<label>Transgender</label>
										</div>
									</div>

									<div class="form-group">
										<label class="col-sm-3 control-label"><spring:message code="lbl.mobile"/><span class="mandatory"></span></label>
										<div class="col-sm-2 add-margin">
											<div class="input-group">
												<span class="input-group-addon">+91</span>
												  	<form:input type="text" path="mobileNumber" id="mobileNumber" data-inputmask="'mask': '9999999999'" 
											required="required" cssClass="form-control" maxlength="10" placeholder="Mobile Number"/>
											</div>
											<form:errors path="mobileNumber" cssClass="add-margin error-msg"/>
										</div>
										
										<label for="field-1" class="col-sm-1 control-label"><spring:message code="lbl.email"/></label>
										
										<div class="col-sm-3">
											<form:input type="text" cssClass="form-control" id="emial" path="emailId" placeholder="abc@xyz.com" />
											<form:errors path="emailId" cssClass="error-msg"/>
										</div>
										
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.altcontact"/></label>
										
										<div class="col-sm-2 add-margin">
											<form:input type="text" path="altContactNumber" id="altcontact" cssClass="form-control" data-inputmask="'mask': '9999999999'" maxlength="10" placeholder="Mobile Number"/>
										</div>
										
										<label for="field-1" class="col-sm-1 control-label"><spring:message code="lbl.DOB"/></label>
										
										<div class="col-sm-3 add-margin">
											<form:input id="DOB" path="dob" type="text" class="form-control datepicker" 
											data-inputmask="'mask': 'd/m/y'" placeholder="DD/MM/YYYY" />
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.DOA"/></label>
										
										<div class="col-sm-2 add-margin">
											<form:input id="DOA" path="dateOfAppointment" type="text" cssClass="form-control datepicker" data-inputmask="'mask': 'd/m/y'" placeholder="DD/MM/YYYY"/>
										</div>
										
										<label for="field-1" class="col-sm-1 control-label"><spring:message code="lbl.status"/><span class="mandatory"></span></label>
									 	
									 	<div class="col-sm-3 add-margin">
											<form:select path="employeeStatus" id="employeeStatus" required="required"
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
											<form:select path="employeeType" id="employeeType" required="required"
												cssClass="form-control" cssErrorClass="form-control error">
												<form:option value="">
													<spring:message code="lbl.select" />
												</form:option>
												<form:options items="${employeeTypes}" itemValue="id"
													itemLabel="name" />
											</form:select>
											<form:errors path="employeeType" cssClass="error-msg" />
										</div>
										
										<label for="field-1" class="col-sm-1 control-label"><spring:message code="lbl.code"/><span class="mandatory"></span></label>
											
										<div class="col-sm-3">
											<form:input path="code" id="code" type="text" required="required" cssClass="form-control is_valid_alphanumeric"/>
											<form:errors path="code" cssClass="add-margin error-msg"/>
										</div>
										
									</div>

									<div class="form-group">
									
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.aadhar"/></label>
										
										<div class="col-sm-2 add-margin">
											<form:input type="text" id="aadhaarNumber" path="aadhaarNumber" cssClass="form-control is_valid_nuber" maxlength="12"/>
											<form:errors path="aadhaarNumber" cssClass="add-margin error-msg"/>
										</div>
										
										<label for="field-1" class="col-sm-1 control-label"><spring:message code="lbl.pan"/></label>
										
										<div class="col-sm-3 add-margin">
											<form:input path="pan" id="pan" type="text" cssClass="form-control is_valid_alphanumeric" maxlength="10"/>
											<form:errors path="pan" cssClass="add-margin error-msg"/>
										</div>
										
										
									</div>
									
								    <c:if test="${not empty image}">
								    <div class="form-group">
									    <label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.sign"/></label>
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<img width="100" height="70" src='data:image/png;base64,${image}' /> 
									  	</div>
								    </div>
								    <div class="form-group">
										<div class="col-sm-3 col-xs-12 text-center" id="upload-section">
											<a href="#" id="triggerFile" class="btn btn-secondary"><spring:message code="lbl.new.signature"/></a>
											<input type="file" id="file1" name="file" data-id="1" class="filechange inline btn" style="display:none;"/>
										</div>
										<div class="col-sm-6 col-xs-12">
											<div id="file1block" class="add-margin col-sm-3 col-xs-6">
												<img id="preview1" src="#" alt="" class="display-hide "/>
												<div class="remove-img preview-cross1 display-hide" data-file-id><i class="fa fa-times-circle"></i></div>
												<div class="add-padding" id="filename1"></div>
										    </div>
										</div>	
					                 </div>	                 
								     </c:if>
								     
									<c:if test="${ empty image}">
                                    <div class="form-group">
										<div class="col-sm-3 col-xs-12 text-center" id="upload-section">
											<a href="#" id="triggerFile" class="btn btn-secondary"><spring:message code="lbl.signature"/></a>
											<input type="file" id="file1" name="file" data-id="1" class="filechange inline btn" style="display:none;"/>
										</div>
										<div class="col-sm-6 col-xs-12">
											<div id="file1block" class="add-margin col-sm-4 col-xs-4">
												<img id="preview1" src="#" alt="" class="display-hide "/>
												<div class="remove-img preview-cross1 display-hide" data-file-id><i class="fa fa-times-circle"></i></div>
												<div class="add-padding" id="filename1"></div>
										</div>
										</div>
					                </div>
					                </c:if>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.useractive"/><span class="mandatory"></span></label>
										
										<div class="col-sm-1 col-xs-12 add-margin">
											<form:radiobutton path="active" id="isactive_yes" checked="checked" value="true"/>
											<label>Yes</label>
										</div>

										<div class="col-sm-1 col-xs-12 add-margin">
											<form:radiobutton path="active" id="isactive_no" value="false"/>
											<label>No</label>
										</div>

										<label for="field-1" class="col-sm-1 control-label"><spring:message code="lbl.username"/><span class="mandatory"></span></label>
										
										<div class="col-sm-3 add-margin">
											<form:input type="text" path="username" id="username" required="required" cssClass="form-control"/>
											<form:errors path="username" cssClass="add-margin error-msg"/>
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
											<input type="radio" id="primary_yes" name="isprimary" value="true" checked>
											<label>Yes</label>
										</div>

										<div class="col-sm-1 col-xs-12 add-margin">
											<input type="radio" id="primary_no" name="isprimary" value="false" >
											<label>No</label>
										</div>


									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Date Range<span class="mandatory"></span></label>
										
										<div class="col-sm-3">
											<input type="text" class="form-control datepicker" name="fromDate" id="fromDate"
												data-inputmask="'mask': 'd/m/y'" placeholder="DD/MM/YYYY"/>
							 				<div class="error-msg fromdateerror all-errors display-hide"></div>
										</div>
										
										
										<div class="col-sm-3">
											<input type="text" class="form-control datepicker" name="toDate" id="toDate"
												data-inputmask="'mask': 'd/m/y'" placeholder="DD/MM/YYYY"/>
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
											<input id="designationName" type="text" class="form-control " autocomplete="off"
													value="${assignment.designation.name}" placeholder="Designation">
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
												<input id="positionName" type="text" class="form-control typeahead " autocomplete="off"
													value="${assignment.position.name}" placeholder="Position">
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
										
										<label for="field-1" class="col-sm-1 control-label">Function</label>
										
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
										
										<label for="field-1" class="col-sm-1 control-label">Functionary</label>
										
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
									     <div class="error-msg hoderror all-errors display-hide"></div>
								
									<div class="form-group">
										<div class="text-center">
											<button type="button" id="btn-add" 
												class="btn btn-primary" >Add / Modify</button>
											<button class="btn btn-danger" type="button">Reset</button>
										</div>
									</div>

									<div class="panel-heading custom_form_panel_heading">
										<table id="assignmentTable" class="table table-bordered">
										<thead>
										<div class="col-sm-12 table-div-border view-content header-color hidden-xs">
											<th class="col-sm-2 table-div-column">Date Range</th>
											<th class="col-sm-1 table-div-column">Is Primary</th>
											<th class="col-sm-2 table-div-column">Department</th>
											<th class="col-sm-2 table-div-column">Designation</th>
											<th class="col-sm-2 table-div-column">Position</th>
											<th class="col-sm-2 table-div-column">HOD Department</th>
											<th class="col-sm-2 table-div-column">Actions</th>
										</div>
										</thead>
											<tbody>
											<div class="error-msg assignmentserror all-errors display-hide" align="center"></div>
											   <c:if test="${not empty error}"> 
                                                 <div class="alert alert-danger" role="alert"><strong>${error}</strong></div>
                                             </c:if> 
											<c:forEach var="assign" items="${employee.assignments}" varStatus="status">
												<tr>
													<td>
													    <input type="hidden" id="table_assignid${status.index}"
													    name="assignments[${status.index}].id"
													    value="${assign.id}"/>
														<fmt:formatDate value="${assign.fromDate}" var="fromDate"
															pattern="dd/MM/yyyy" />
														<fmt:formatDate value="${assign.toDate}" var="toDate"
															pattern="dd/MM/yyyy" />
														<input type="text" id="table_date_range${status.index}" 
															class="form-control" readonly="readonly" style="text-align:center"
															value="<c:out value="${fromDate} - ${toDate}"/>"/>
														<input type="hidden" id="assignments[${status.index}].fromDate"
															name="assignments[${status.index}].fromDate"
															value="${fromDate}"/>
														<input type="hidden" id="assignments[${status.index}].toDate"
															name="assignments[${status.index}].toDate"
															value="${toDate}"/>			
													</td>
													<td>
														<input type="hidden" id="assignments[${status.index}].primary"
															name="assignments[${status.index}].primary"
															value="${assign.primary}"/>
																
																<c:if test="${assign.primary==true}" >
																<input type="text" id="table_department${status.index}" class="form-control" 
																	readonly="readonly" style="text-align:center" value="Yes"/>
																</c:if>
																<c:if test="${assign.primary==false}">
																	<input type="text" id="table_department${status.index}" class="form-control" 
																	readonly="readonly" style="text-align:center" value="No"/>
																</c:if>
													</td>
													<td>
															<input type="hidden" id="assignments[${status.index}].department"
															name="assignments[${status.index}].department"
															value="${assign.department.id}"/>	
															
															<input type="text" id="table_department${status.index}" class="form-control" 
															readonly="readonly" style="text-align:center" value="${assign.department.name}"/>
															
													</td>
													<td>
														<input type="hidden" id="assignments[${status.index}].designation"
															name="assignments[${status.index}].designation"
															value="${assign.designation.id}"/>
																
															<input type="text" id="table_designation${status.index}" class="form-control" 
															readonly="readonly" style="text-align:center" value="${assign.designation.name}"/>
													</td>
													<td>
														<input type="text" id="table_position${status.index}" class="form-control" 
														readonly="readonly" style="text-align:center" value="${assign.position.name}"/>
														<input type="hidden" id="assignments[${status.index}].position"
															name="assignments[${status.index}].position"
															value="${assign.position.id}"/>
														<input type="hidden" id="assignments[${status.index}].fund"
															name="assignments[${status.index}].fund"
															value="${assign.fund.id}"/>
														<input type="hidden" id="assignments[${status.index}].function"
															name="assignments[${status.index}].function"
															value="${assign.function.id}"/>
														<input type="hidden" id="assignments[${status.index}].functionary"
															name="assignments[${status.index}].functionary"
															value="${assign.functionary.id}"/>
														<input type="hidden" id="assignments[${status.index}].grade"
															name="assignments[${status.index}].grade"
															value="${assign.grade.id}"/>	
						</td>
											
													<td> <c:if test="${assign.deptSet!=null}">  
												       
															<c:forEach var="hodDept" items="${assign.deptSet}" varStatus="hodeptStatus">
															<input type="hidden" id="hodDeptIds${status.index}" name="hodDeptIds${status.index}" value="${assign.deptSet.size()}"/>
															<input type="hidden" id="assignments[${status.index}].deptSet[${hodeptStatus.index}].hod"
																	name="assignments[${status.index}].deptSet[${hodeptStatus.index}].hod.id"
																	value="${hodDept.hod.id}"/>	
																<input type="text" id="assignments[${status.index}].hodDept[${hodeptStatus.index}].hod"
																	name="assignments[${status.index}].hodDept[${hodeptStatus.index}].hod.id"
																	value="${hodDept.hod.name}"/>	
																	<input type="hidden" id="assignments[${status.index}].hodList[${hodeptStatus.index}].hod"
																	name="assignments[${status.index}].hodList[${hodeptStatus.index}].hod.id"
																	value="${hodDept.hod.id}"/>
															</c:forEach>  		
																
													  </c:if>  </td>
													<td>
														<span class="add-padding" data-toggle="tooltip" title="Edit"><i id="edit_row" class="fa fa-edit" value="${status.index}"></i></span>
													   <span class="add-padding" data-toggle="tooltip" title="Delete"><i
														id="delete_row" class="fa fa-remove"  value="${status.index}"></i></span>
													</td>
												</tr>
											</c:forEach>
											</tbody>
										</table>
									</div>
							<div class="panel-heading custom_form_panel_heading">
								<div class="panel-title">Jurisdiction Details</div>

							</div>
							<div class="form-group">
								<label for="field-1" class="col-sm-3 control-label">Boundary
									Type</label>

								<div class="col-sm-2 add-margin">
									<select class="form-control" id="boundaryTypeId"
										onchange="populateBoundary(this);">
										<option value="">
											<spring:message code="lbl.select" />
										</option>
										<c:forEach items="${boundaryType}" var="bndryType">
											<option value="${bndryType.id}">${bndryType.name}</option>
										</c:forEach>
									</select> <input type="hidden" id="boundaryTypeId" value="" />
									<div
										class="error-msg boundaryTypeerror all-errors display-hide"></div>
								</div>
								<div class="col-sm-1">
								<label for="field-1" class="control-label">Boundary</label>
								</div>
								<div class="col-sm-3 add-margin">
									<egov:ajaxdropdown id="boundaryAjax" fields="['Text','Value']"
										dropdownId="boundarySelect"
										url="../egi/boundaries-by-boundaryType" />
									<select class="form-control" id="boundarySelect">
										<option value="">
											<spring:message code="lbl.select" />
										</option>
									</select> <input type="hidden" id="boundaryId" value="" />
									<div class="error-msg boundaryerror all-errors display-hide"></div>
								</div>
							</div>
							<div class="error-msg duplicatejurisdictionerror all-errors display-hide"></div>
							<div class="form-group">
								<div class="text-center">
									<button type="button" id="btn-addJurdctn" class="btn btn-primary">Add
										/ Modify</button>
							</div>
							</div>
							<div class="panel-heading custom_form_panel_heading">
								<table id="jurisdictionTable" class="table table-bordered">
									<thead>
										<div
											class="col-sm-12 table-div-border view-content header-color hidden-xs">
											<th class="col-sm-2 table-div-column">Boundary Type</th>
											<th class="col-sm-2 table-div-column">Boundary</th>
											<th class="col-sm-2 table-div-column">Actions</th>
										</div>
									</thead>
									<tbody>
										<div
											class="error-msg jurisdictionserror all-errors display-hide"
											align="center"></div>
										<c:forEach var="jurdctn" items="${employee.jurisdictions}"
											varStatus="status">
											<tr>
												<td>
													<input type="hidden" id="table_jurisdictionid${status.index}"
													name="jurisdictions[${status.index}].id"
													value="${jurdctn.id}"/>
													<input type="hidden"
													id="jurisdictions[${status.index}].boundaryType"
													name="jurisdictions[${status.index}].boundaryType"
													value="${jurdctn.boundaryType.id}" /> <input type="text"
													id="table_boundaryType${status.index}" class="form-control"
													readonly="readonly" style="text-align: center"
													value="${jurdctn.boundaryType.name}" /></td>
												<td><input type="hidden"
													id="jurisdictions[${status.index}].boundary"
													name="jurisdictions[${status.index}].boundary"
													value="${jurdctn.boundary.id}" /> <input type="text"
													id="table_boundary${status.index}" class="form-control"
													readonly="readonly" style="text-align: center"
													value="${jurdctn.boundary.name}" /></td>
													<td><span class="add-padding" data-toggle="tooltip" title="Edit"><i
														id="jurdctnedit_row" class="fa fa-edit"  value="${status.index}"></i></span>
														<span class="add-padding" data-toggle="tooltip" title="Delete"><i
														id="jurdctndelete_row" class="fa fa-remove"  value="${status.index}"></i></span>
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
				
				
				
				<div class="row">
					<div class="text-center">
						<button type="submit" id="submit" class="btn btn-primary"><spring:message code="lbl.submit"/></button>
						<a href="javascript:void(0);" id="com_cancel" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
					</div>
				</div>
                
                
                
             </form:form>
    </div>
</div>
<script src="<cdn:url value='/resources/js/app/fileuploadndmaps.js'/>"></script>
