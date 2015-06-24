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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.application.date" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input id="applicationdate" path="applicationDate" class="form-control datepicker today" required="required" data-inputmask="'mask': 'd/m/y'" />
		<form:errors path="applicationDate" cssClass="add-margin error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.ptassesmentnumber" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<div class="input-group">
			<form:input id="propertyIdentifier" path="connection.propertyIdentifier" class="form-control" min="3" maxlength="50" required="required" /> <span
				class="input-group-addon"> <i class="fa fa-search"></i>
			</span>
			<form:errors path="connection.propertyIdentifier" cssClass="add-margin error-msg" />
		</div>
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.bpanumber" /></label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control" id="bpaIdentifier" path="connection.bpaIdentifier" min="3" maxlength="50" />
		<form:errors path="connection.bpaIdentifier" cssClass="add-margin error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.applicantname" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="applicantname" disabled> 
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.mobilenumber" /></label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation" data-pattern="number" id="mobileNumber" path="connection.mobileNumber" min="10" maxlength="12" />
		<form:errors path="connection.mobileNumber" cssClass="add-margin error-msg" /> 
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.address" /></label>
	<div class="col-sm-3 add-margin">
		<textarea class="form-control" id="propertyaddress" disabled></textarea>
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.zonewardblock" /></label>
	<div class="col-sm-3 add-margin">
		<textarea class="form-control" id="zonewardblock" disabled></textarea>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.pt.due" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control text-right" id="propertytaxdue"  disabled value="0.00"> 
	</div>
</div>				