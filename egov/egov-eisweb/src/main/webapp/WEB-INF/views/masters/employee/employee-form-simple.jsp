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
<%@ include file="/includes/taglibs.jsp" %>
<script src="<cdn:url value='/resources/js/app/employeedataentry.js'/>"></script>

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
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.code"/><span class="mandatory"></span></label>
									
										<div class="col-sm-2 add-margin">
											<form:input path="code" id="code" type="text" required="required" cssClass="form-control is_valid_alphanumeric"/>
											<form:hidden path="username"/>
											<form:errors path="code" cssClass="add-margin error-msg"/>
										</div>
										
										<div class="col-sm-1">
										   <label for="field-1" class="control-label"><spring:message code="lbl.mobile"/><span class="mandatory"></span></label>
										</div>
										
										<div class="col-sm-3 add-margin">
											<form:input type="text" path="mobileNumber" id="mobileNumber" data-inputmask="'mask': '9999999999'" 
											required="required" cssClass="form-control" maxlength="10" placeholder="Mobile Number"/>
											<form:errors path="mobileNumber" cssClass="add-margin error-msg"/>
										</div>
																				
									</div>

									<div class="form-group">
										
										<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.gender"/><span class="mandatory"></span></label>
										
										<div class="col-sm-2 col-xs-12 add-margin">
											<form:radiobutton path="gender" id="gender" value="MALE" required="required" checked="true"/>
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
										<label for="field-1"  class="col-sm-3 control-label">Department<span class="mandatory"></span></label>
										
										<div class="col-sm-6 add-margin">
										<select class="form-control" name = "deptId" id="deptId" required="required"> 
												<option value="">
													<spring:message code="lbl.select" />
												</option>
												  <c:forEach items="${department}" var="dept">
										            <option value="${dept.id}">${dept.name} </option>
										         </c:forEach>
										 </select> 
											<input type="hidden" id="deparmentId" name="departmentId" value=""/>
											<div class="error-msg departmenterror all-errors display-hide"></div>
										</div>
										
								</div>
								<div class="form-group">
										   <label for="field-1" class="col-sm-3 control-label">Designation<span class="mandatory"></span></label>
										
										<div class="col-sm-6 add-margin">
										        <input id="designationName"  name="designationName" type="text" class="form-control " autocomplete="off"
													value="" placeholder="Designation" required="required">
													<input type="hidden" id="designationId" value=""/>
												<c:forEach items="${designations}" var="designation">
													<a onclick="setDesignationId(<c:out value="${designation.id}"/>)" href="javascript:void(0)" class="btn btn-secondary btn-xs tag-element freq-ct"><c:out
															value="${designation.name }" /> </a>
												</c:forEach>
												<div class="error-msg designationerror all-errors display-hide"></div>
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
<script src="<cdn:url value='/resources/js/app/fileuploadndmaps.js'/>"></script>
