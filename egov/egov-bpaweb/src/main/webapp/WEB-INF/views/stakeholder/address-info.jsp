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


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>



<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="${param.subhead}" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label"> <spring:message
			code="lbl.addr.dno" /><span class="mandatory"></span>
	</label>
	<div class="col-sm-3">
		<form:input path="${address}.houseNoBldgApt" id="${address}.houseNoBldgApt"
			type="text" cssClass="form-control inline-elem"
			placeholder="" maxlength="32" autocomplete="off" required="required" />
		<form:errors path="${address}.houseNoBldgApt"
			cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label"> <spring:message
			code="lbl.addr.Steet.name" /><span class="mandatory"></span>
	</label>
	<div class="col-sm-3">
		<form:input path="${address}.streetRoadLine" id="${address}.streetRoadLine"
			type="text" cssClass="form-control is_valid_alphabet inline-elem"
			placeholder="" maxlength="256" autocomplete="off" required="required" />
		<form:errors path="${address}.streetRoadLine"
			cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label"> <spring:message
			code="lbl.locality" /><span class="mandatory"></span>
	</label>
	<div class="col-sm-3">
		<form:input path="${address}.areaLocalitySector" id="${address}.areaLocalitySector"
			type="text" cssClass="form-control is_valid_alphabet inline-elem"
			placeholder="" maxlength="256" autocomplete="off" required="required" />
		<form:errors path="${address}.areaLocalitySector"
			cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label"> <spring:message
			code="lbl.city" /><span class="mandatory"></span>
	</label>
	<div class="col-sm-3">
		<form:input path="${address}.cityTownVillage" id="${address}.cityTownVillage"
			type="text" cssClass="form-control is_valid_alphabet inline-elem"
			placeholder="" maxlength="256" autocomplete="off" required="required" />
		<form:errors path="${address}.cityTownVillage"
			cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label"> <spring:message
			code="lbl.district" /><span class="mandatory"></span>
	</label>
	<div class="col-sm-3">
		<form:input path="${address}.district" id="${address}.district"
			type="text" cssClass="form-control is_valid_alphabet inline-elem"
			placeholder="" maxlength="100" autocomplete="off" required="required" />
		<form:errors path="${address}.district"
			cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label"> <spring:message
			code="lbl.state" /><span class="mandatory"></span>
	</label>
	<div class="col-sm-3">
		<form:input path="${address}.state" id="${address}.state"
			type="text" cssClass="form-control is_valid_alphabet inline-elem"
			placeholder="" maxlength="100" autocomplete="off" required="required" />
		<form:errors path="${address}.state"
			cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label"> <spring:message
			code="lbl.pincode" /><span class="mandatory"></span>
	</label>
	<div class="col-sm-3">
		<form:input path="${address}.pinCode" id="${address}.pinCode"
			type="text" cssClass="form-control inline-elem patternvalidation" data-pattern="number"
			placeholder="" maxlength="6" autocomplete="off" required="required" />
		<form:errors path="${address}.pinCode"
			cssClass="add-margin error-msg" />
	</div>
</div>

