<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css'/>">
<div class="row">
	<div class="col-md-12">
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
						<div class="col-sm-2 col-xs-12 add-margin dynamic-span capitalize">
							<form:radiobuttons path="gender" element="span"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label">Mobile</label>
						<div class="col-sm-2 add-margin">
							<form:input path="mobileNumber" cssClass="form-control" data-inputmask="'mask': '9999999999'" id="mobileNumber" placeholder="Mobile Number" maxlength="10"/>
						</div>
						
						<div class="col-sm-1">
							<label for="field-1" class="control-label">Email</label>
						</div>
						
						<div class="col-sm-3">
							<form:input path="emailId" cssClass="form-control" id="emailId" placeholder="abc@xyz.com" maxlength="50"/>
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
						</div>
						
						<div class="col-sm-1">
							<label for="field-1" class="control-label">Aadhaar</label>
						</div>
						
						<div class="col-sm-3 add-margin">
							<form:input path="aadhaarNumber" cssClass="form-control" placeholder="123456789012" maxlength="12"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label">Preferred Language</label>
						<div class="col-sm-6 add-margin">
							<select class="form-control" id="pre_language">
								<option value="en_IN">English</option>
								<option value="hi_IN">Hindi</option>
							</select>
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
<script src="<c:url value='/resources/js/app/editprofile.js'/>"></script>
