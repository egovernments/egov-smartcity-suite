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

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%-- <form:errors path="existingConnection.arrears" cssClass="add-margin error-msg" />	 --%>
<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.feedetails" />
	</div>
</div>

<div class="form-group">	
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.monthlyfees" /></label>
	<div class="col-sm-3 add-margin">
		<form:input id="monthlyFee" path="existingConnection.monthlyFee" class="form-control text-right patternvalidation" maxlength="6" data-pattern="number"/>  
		<form:errors path="existingConnection.monthlyFee" cssClass="add-margin error-msg"/>	
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.donationcharges" /></label>
	<div class="col-sm-3 add-margin">
		<form:input path="existingConnection.donationCharges" class="form-control text-right patternvalidation" maxlength="6" data-pattern="number" />
			<form:errors path="existingConnection.donationCharges" cssClass="add-margin error-msg" />	
	</div>
</div>
<%-- <div class="form-group">  

	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.arrears" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input path="existingConnection.arrears"  maxlength="6"  class="form-control text-right patternvalidation" data-pattern="number" required="required" /> 
			<form:errors path="existingConnection.arrears" cssClass="add-margin error-msg" />	
		

	</div>	  
</div>	 --%>
<div  id="metereddetails" style="display: none" >  



<div class="form-group">	
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.metercost" /></label>
	<div class="col-sm-3 add-margin">
		<form:input id="existmeterCost" path="existingConnection.meterCost" class="form-control text-right patternvalidation" maxlength="6" data-pattern="number"  />
			<form:errors path="existingConnection.meterCost" cssClass="add-margin error-msg" />	  
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.metername" /></label>
	<div class="col-sm-3 add-margin">
		<form:input id="existmeterName" path="existingConnection.meterName" class="form-control text-left" maxlength="20"/> 
			<form:errors path="existingConnection.meterName" cssClass="add-margin error-msg" />	
	</div>
</div>

<div class="form-group">	
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.meterslno" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input id="existmeterNo" path="existingConnection.meterNo"  class="form-control text-left patternvalidation" maxlength="12" data-pattern="number"/>   
			<form:errors path="existingConnection.meterNo" cssClass="add-margin error-msg" />	
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.previousreading" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
	<form:input id="previousReading" path="existingConnection.previousReading"  class="form-control text-right patternvalidation" maxlength="12" data-pattern="number"/>   
		<form:errors path="existingConnection.previousReading" cssClass="add-margin error-msg" />	 
	</div>  
	
</div>

<div class="form-group">	
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.readingdate" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
	<form:input id="existreadingDate" path="existingConnection.readingDate"  class="form-control datepicker" data-date-end-date="0d"
								 data-inputmask="'mask': 'd/m/y'" />  
	<form:errors path="existingConnection.readingDate" cssClass="add-margin error-msg" />	     
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.currentreading" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input id="currentcurrentReading" path="existingConnection.currentReading" class="form-control text-right patternvalidation" maxlength="12" data-pattern="number" />
			<form:errors path="existingConnection.currentReading" cssClass="add-margin error-msg" />	
	</div>
</div>
</div>

				