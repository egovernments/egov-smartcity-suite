<%--
  ~ eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) <2017>  eGovernments Foundation
  ~
  ~  The updated version of eGov suite of products as by eGovernments Foundation
  ~  is available at http://www.egovernments.org
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program. If not, see http://www.gnu.org/licenses/ or
  ~  http://www.gnu.org/licenses/gpl.html .
  ~
  ~  In addition to the terms of the GPL license to be adhered to in using this
  ~  program, the following additional terms are to be complied with:
  ~
  ~      1) All versions of this program, verbatim or modified must carry this
  ~         Legal Notice.
  ~ 	Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~         derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~ 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~ 	For any further queries on attribution, including queries on brand guidelines,
  ~         please contact contact@egovernments.org
  ~
  ~      2) Any misrepresentation of the origin of the material is prohibited. It
  ~         is required that all modified versions of this material be marked in
  ~         reasonable ways as different from the original version.
  ~
  ~      3) This license does not grant any rights to any user of the program
  ~         with regards to rights under trademark law for use of the trade names
  ~         or trademarks of eGovernments Foundation.
  ~
  ~  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code='license.title.applicantdetails' />
	</div>
</div>
<div class="row ">
	<label class="col-sm-3 control-label text-right"><spring:message
			code='licensee.aadhaarNo' /></label>
	<div class="col-sm-3 add-margin" style="margin-bottom: 15px;">
		<form:input path="licensee.uid" id="adhaarId"
			Class="form-control patternvalidation" data-pattern="number" value="${licensee.uid}" maxlength="12" minlength="12"
			placeholder="" autocomplete="off" />
		<div class="error-msg hide" path="licensee.uid" id="adhaarError">Should be 12 digits</div>
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code='search.licensee.mobileNo' /><span class="mandatory"></span></label>
	 <div class="col-sm-3 add-margin" style="margin-bottom:15px;">
        <div class="input-group">
            <span class="input-group-addon" id="basic-addon1">+91</span>
            <form:input path="licensee.mobilePhoneNumber" id="mobilePhoneNumber" maxlength="10" required="true"
                         cssClass="form-control patternvalidation" data-pattern="number" value="${licensee.mobilePhoneNumber}"/>
   </div> 
             <div class="error-msg hide" path="licensee.mobilePhoneNumber"  id="mobileError">Should be 10 digits</div>
   </div>
 
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code='licensee.applicantname' /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input path="licensee.applicantName" id="applicantName"
			value="${licensee.applicantName}" cssClass="form-control patternvalidation"
			maxlength="250" placeholder="" autocomplete="off"
			data-pattern="alphabetwithspace" required="true" />
		<form:errors path="licensee.applicantName" class="error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right"><spring:message
			code='licensee.fatherorspousename' /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input path="licensee.fatherOrSpouseName" id="fatherOrSpouseName"
			value="${licensee.fatherOrSpouseName}" class="form-control "
			maxlength="250" placeholder=""
			cssClass="form-control patternvalidation"
			data-pattern="alphabetwithspace" required="true" />
		<form:errors path="licensee.fatherOrSpouseName" cssClass="error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code='lbl.emailid' /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input path="licensee.emailId" id="emailId"
			value="${licensee.emailId}" class="form-control" maxlength="64"
			placeholder="abc@xyz.com" type="email" required="true" />
		<form:errors path="licensee.emailId" cssClass="error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right"><spring:message
			code='licensee.address' /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:textarea path="licensee.address" id="licenseeAddress"
			maxlength="250" onblur="checkLength(this,250)"
			class="form-control typeahead" required="true" />
		<form:errors path="licensee.address" cssClass="error-msg" />
	</div>
</div>
