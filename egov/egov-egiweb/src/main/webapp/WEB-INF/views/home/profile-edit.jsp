<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css'/>">
<div class="row">
	<div class="col-md-12">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
		</c:if>
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					Account Details
				</div>
			</div>
			<div class="panel-body custom-form">
				<form:form role="form" action="edit" modelAttribute="user" commandName="user" id="user" cssClass="form-horizontal form-groups-bordered" >
					
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label">Full Name</label>
						<div class="col-sm-2 col-md-1 add-margin">
							<form:select class="form-control" id="salutation" path="salutation">
								<form:option value=""></form:option>
								<form:option value="Mr">Mr</form:option>
								<form:option value="Mrs">Mrs</form:option>
								<form:option value="Miss">Miss</form:option>
							</form:select>
						</div>
						<div class="col-sm-4 col-md-5 add-margin">
							<form:input path="name" cssClass="form-control" placeholder="Name" maxlength="100"/>
						</div>
					</div>
					
					<div class="form-group">
						
						<label for="field-1" class="col-sm-3 control-label">Gender</label>
						<div class="col-sm-6 col-xs-12 add-margin dynamic-span capitalize">
							<form:radiobuttons path="gender" element="span"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label">Mobile</label>
						<div class="col-sm-2 add-margin">
							<form:input path="mobileNumber" cssClass="form-control" data-inputmask="'mask': '9999999999'" id="mobileNumber" placeholder="Mobile Number" maxlength="10"/>
							<form:errors path="mobileNumber" cssClass="add-margin error-msg"/>
						</div>
						
						<div class="col-sm-1">
							<label for="field-1" class="control-label">Email</label>
						</div>
						
						<div class="col-sm-3">
							<form:input path="emailId" cssClass="form-control" id="emailId" placeholder="abc@xyz.com" maxlength="50"/>
							<form:errors path="emailId" cssClass="add-margin error-msg"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label">Alternate Contact No.</label>
						
						<div class="col-sm-2 add-margin">
							<form:input path="altContactNumber" cssClass="form-control" data-inputmask="'mask': '9999999999'" id="altContactNumber" placeholder="8080808080" maxlength="10"/>
						</div>
						
						<div class="col-sm-1">
							<label for="field-1" class="control-label">DOB</label>
						</div>
						
						<div class="col-sm-3 add-margin">
							<form:input path="dob" cssClass="form-control datepicker"  data-inputmask="'mask': 'd/m/y'" id="dob" placeholder="24/01/1992"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label">PAN</label>
						
						<div class="col-sm-2 add-margin">
							<form:input path="pan" cssClass="form-control" placeholder="AHWPU1117T" maxlength="10"/>
							<form:errors path="pan" cssClass="add-margin error-msg"/>
						</div>
						
						<div class="col-sm-1">
							<label for="field-1" class="control-label">Aadhaar</label>
						</div>
						
						<div class="col-sm-3 add-margin">
							<form:input path="aadhaarNumber" cssClass="form-control" placeholder="123456789012" maxlength="12"/>
							<form:errors path="aadhaarNumber" cssClass="add-margin error-msg"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label">Preferred Language</label>
						<div class="col-sm-6 add-margin">
							<form:select class="form-control" id="locale" path="locale">
								<form:option value=""></form:option>
								<form:option value="en_IN">English</form:option>
								<form:option value="hi_IN">Hindi</form:option>
							</form:select>
						</div>
					</div>
					
					<div class="form-group">
						<div class="text-center">
							<button type="submit" class="btn btn-primary">Save Changes</button>
							<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">Close</a>
						</div>
					</div>
					
				</form:form>
			</div>
		</div>
	</div>
</div>

<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js'/>"></script>
<script src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js'/>"></script>
