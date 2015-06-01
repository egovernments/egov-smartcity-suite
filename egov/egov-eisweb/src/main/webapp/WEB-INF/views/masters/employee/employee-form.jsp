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
<script src="<c:url value='/resources/js/app/employeecreate.js'/>"></script>

<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/font-awesome-4.3.0/css/font-awesome.min.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/egov/custom.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/egov/header-custom.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css'/>">
<script src="<c:url value='/resources/global/js/jquery/jquery.js'/>"></script>

		<!--[if lt IE 9]><script src="resources/js/ie8-responsive-file-warning.js"></script><![endif]-->
		
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
			<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
		
<div class="row" id="page-content">
	<div class="col-md-12">
		<div class="panel" data-collapsed="0">
		<form:form  method ="post" action="create" class="form-horizontal form-groups-bordered" modelAttribute="employeeBean" id="employeeForm" >
			<div class="panel-body">
				 <c:if test="${not empty message}">
                    <div id="message" class="success">${message}</div>
                </c:if>
                
                <div class="row">
					<div class="col-md-12">
						
						<div class="panel panel-primary" data-collapsed="0">
							
							<div class="panel-heading">
								<div class="panel-title">
									Employee Details
								</div>
								
							</div>
							
							<div class="panel-body custom-form">
								
								<form role="form" id="complaintform" class="form-horizontal form-groups-bordered">
									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Name<span class="mandatory"></span></label>
										<div class="col-sm-2 col-md-1 add-margin">
											<select class="form-control" id="salutation">
												<option>Mr</option>
												<option>Miss</option>
												<option>Mrs</option>
											</select>
										</div>
										<div class="col-sm-4 col-md-5 add-margin">
											<input type="text" class="form-control is_valid_alphabet" maxlength="100">
										</div>
									</div>

									<div class="form-group">
										
										<label for="field-1" class="col-sm-3 control-label">Gender<span class="mandatory"></span></label>
										
										<div class="col-sm-2 col-xs-12 add-margin">
											<input type="radio" id="" name="gen" value="male" checked="">
											<label>Male</label>
										</div>
										<div class="col-sm-2 col-xs-12 add-margin">
											<input type="radio" id="" name="gen" value="female">
											<label>Female</label>
										</div>
										<div class="col-sm-2 col-xs-12 add-margin">
											<input type="radio" id="" name="gen" value="tg">
											<label>Transgender</label>
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Mobile<span class="mandatory"></span></label>
										
										<div class="col-sm-2 add-margin">
											<input type="text" class="form-control" data-inputmask="'mask': '9999999999'" id="mob-no" placeholder="Mobile Number">
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label">Email<span class="mandatory"></span></label>
										</div>
										
										<div class="col-sm-3">
											<input type="text" class="form-control" id="email" placeholder="abc@xyz.com">
										</div>
										
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Alternate Mobile<span class="mandatory"></span></label>
										
										<div class="col-sm-2 add-margin">
											<input type="text" class="form-control" data-inputmask="'mask': '9999999999'" maxlength="10" placeholder="Mobile Number">
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label">DOB<span class="mandatory"></span></label>
										</div>
										
										<div class="col-sm-3 add-margin">
											<input type="text" class="form-control datepicker" data-inputmask="'mask': 'd/m/y'" placeholder="24/01/1992" value="">
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Date Of Appointment<span class="mandatory"></span></label>
										
										<div class="col-sm-2 add-margin">
											<input type="text" class="form-control datepicker" data-inputmask="'mask': 'd/m/y'" placeholder="24/01/1992">
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label">Status<span class="mandatory"></span></label>
										</div>
										<div class="col-sm-3 add-margin">
											<form:select path="employee.employeeStatus" id="employeeStatus"
												cssClass="form-control" cssErrorClass="form-control error">
												<form:option value="">
													<spring:message code="lbl.select" />
												</form:option>
												<form:options items="${employeeStatus}" />
											</form:select>
											<form:errors path="employee.employeeStatus" cssClass="error-msg" />
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Employment Type<span class="mandatory"></span></label>
										
										<div class="col-sm-2 add-margin">
											<form:select path="assignment.department" id="employeeType"
												cssClass="form-control" cssErrorClass="form-control error">
												<form:option value="">
													<spring:message code="lbl.select" />
												</form:option>
												<form:options items="${employeeTypes}" itemValue="id"
													itemLabel="name" />
											</form:select>
											<form:errors path="employee.employeeType" cssClass="error-msg" />
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label">Code<span class="mandatory"></span></label>
										</div>
										
										<div class="col-sm-3">
											<input type="text" class="form-control is_valid_alphanumeric">
										</div>
										
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">PAN<span class="mandatory"></span></label>
										
										<div class="col-sm-2 add-margin">
											<input type="text" class="form-control is_valid_alphanumeric" maxlength="10">
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label">Aadhaar<span class="mandatory"></span></label>
										</div>
										
										<div class="col-sm-3 add-margin">
											<input type="text" class="form-control is_valid_number" maxlength="12">
										</div>
									</div>


									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Is User Active<span class="mandatory"></span></label>
										
										<div class="col-sm-1 col-xs-12 add-margin">
											<input type="radio" id="" name="useractive" value="Phone" checked>
											<label>Yes</label>
										</div>

										<div class="col-sm-1 col-xs-12 add-margin">
											<input type="radio" id="" name="useractive" value="Phone">
											<label>No</label>
										</div>

										<label for="field-1" class="col-sm-1 control-label">Username</label>
										
										<div class="col-sm-3 add-margin">
											<input type="text" class="form-control">
										</div>

									</div>

									<div class="form-group">
										
										<label class="col-sm-3 control-label">Remarks<span class="mandatory"></span></label>
										<div class="col-sm-6">
											<textarea class="form-control autogrow" maxlength="400"></textarea>
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
											<input type="radio" id="" name="isprimary" value="Phone" checked>
											<label>Yes</label>
										</div>

										<div class="col-sm-1 col-xs-12 add-margin">
											<input type="radio" id="" name="isprimary" value="Phone">
											<label>No</label>
										</div>

									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Date Range<span class="mandatory"></span></label>
										
										<div class="col-sm-3">
											<input type="text" class="form-control datepicker" placeholder="From Date" data-inputmask="'mask': 'd/m/y'">
										</div>
										
										
										<div class="col-sm-3">
											<input type="text" class="form-control datepicker" placeholder="To Date" data-inputmask="'mask': 'd/m/y'">
										</div>
										
									</div>
						
									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Fund</label>
										
										<div class="col-sm-2 add-margin">
											<select class="form-control">
												<option>Complaint Cell</option>
												<option>Mayor</option>
												<option>Zone Office</option>
												<option>Commissioner Office</option>
												<option>CM Office</option>
											</select>
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label">Function</label>
										</div>
										
										<div class="col-sm-3 add-margin">
											<select class="form-control">
												<option>Complaint Cell</option>
												<option>Mayor</option>
												<option>Zone Office</option>
												<option>Commissioner Office</option>
												<option>CM Office</option>
											</select>
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Grade</label>
										
										<div class="col-sm-2 add-margin">
											<select class="form-control">
												<option>Complaint Cell</option>
												<option>Mayor</option>
												<option>Zone Office</option>
												<option>Commissioner Office</option>
												<option>CM Office</option>
											</select>
										</div>
										
										<div class="col-sm-1">
											<label for="field-1" class="control-label">Functionary</label>
										</div>
										
										<div class="col-sm-3 add-margin">
											<select class="form-control">
												<option>Complaint Cell</option>
												<option>Mayor</option>
												<option>Zone Office</option>
												<option>Commissioner Office</option>
												<option>CM Office</option>
											</select>
										</div>
									</div>


									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Department<span class="mandatory"></span></label>
										
										<div class="col-sm-6 add-margin">
											<form:select path="assignment.department" id="comp_type_dept"
												cssClass="form-control" cssErrorClass="form-control error">
												<form:option value="">
													<spring:message code="lbl.select" />
												</form:option>
												<form:options items="${department}" itemValue="id"
													itemLabel="name" />
											</form:select>
											<form:errors path="assignment.department" cssClass="error-msg" />
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Designation<span class="mandatory"></span></label>
										
										<div class="col-sm-6 add-margin">
											<input type="text" class="form-control typeahead" autocomplete="off">
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">Position<span class="mandatory"></span></label>
										
										<div class="col-sm-6 add-margin">
											<input type="text" class="form-control typeahead" autocomplete="off">
										</div>
									</div>

									<div class="form-group">
										<label for="field-1" class="col-sm-3 control-label">If Head ofDepartment<span class="mandatory"></span></label>
										
										<div class="col-sm-1 col-xs-12 add-margin">
											<input type="radio" id="" name="ishod" value="">
											<label>Yes</label>
										</div>

										<div class="col-sm-1 col-xs-12 add-margin">
											<input type="radio" id="" name="ishod" value="" checked>
											<label>No</label>
										</div>

									</div>

									<div class="form-group">
										<div class="text-center">
											<button class="btn btn-primary">Save / Modify</button>
											<button class="btn btn-danger" type="reset">Reset</button>
										</div>
									</div>

									<div class="row form-group">
										<div class="col-sm-12 table-div-border view-content header-color hidden-xs">
<div class="col-sm-2 table-div-column">Date Range</div>											
<div class="col-sm-2 table-div-column">Is Primary</div>											
<div class="col-sm-2 table-div-column">Department</div>											
<div class="col-sm-2 table-div-column">Designation</div>											
<div class="col-sm-2 table-div-column">Position</div>											
<div class="col-sm-2 table-div-column">Actions</div>											
										</div>
<div class="col-sm-2 table-div-column"><span class="parallel-actions"><i class="fa fa-edit"></i></span><span class="parallel-actions"><i class="fa fa-remove"></i></span></div>											
										</div>
									</div>
									
									
								</form>
							</div>
							
							
						</div>
						
					</div>
				</div>
				
				
				<div class="row">
					<div class="text-center">
						<a href="complaintsuccess.html" id="com_submit" class="btn btn-primary">Register Employee</a>
						<a href="javascript:void(0);" id="com_cancel" class="btn btn-default">Cancel</a>
					</div>
				</div>
                
                
                
             </div>
             </form:form>
         </div>
    </div>
 </div>               		