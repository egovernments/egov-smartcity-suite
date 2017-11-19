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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="panel panel-primary" data-collapsed="0">
<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="lbl.applicant.details" />
	</div>
</div>

<div>
	<spring:hasBindErrors name="sewerageApplicationDetails">
	<div class="alert alert-danger col-md-10 col-md-offset-1">
     			<form:errors path="*" /><br/>
   	</div>
    </spring:hasBindErrors>
     <br/>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.ptassesmentnumber" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<div class="input-group">
			<c:choose>
				<c:when test="${mode != null && mode.equals('edit')}">
					<form:input id="propertyIdentifier" path="connectionDetail.propertyIdentifier" class="form-control patternvalidation" data-pattern="number" disabled="true" />
					<span class="input-group-addon"> <i class="fa fa-search specific"></i></span>
				</c:when>
				<c:otherwise>
					<form:input id="propertyIdentifier" path="connectionDetail.propertyIdentifier" class="form-control patternvalidation" data-pattern="number" maxlength="15" min="3" required="required" />
					<span class="input-group-addon"> <i class="fa fa-search specific"></i></span>
					<form:errors path="connectionDetail.propertyIdentifier" id="propertyIdentifierError" cssClass="add-margin error-msg" />
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.applicantname" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="applicantname" disabled> 
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.mobileNo" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="mobileNumber" disabled>
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.email" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="email" disabled>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.aadhar" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control text-left" id="aadhaar"  disabled> 
	</div>	
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.locality" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="locality" disabled>  
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
			code="lbl.nooffloors" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="nooffloors" disabled>
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.properttax" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control text-right" id="propertytax"  disabled value="0.00"> 
	</div>	
	
</div>	
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.waterTax" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="watercharges" disabled value="0.00">
	</div>
</div>
</div>				