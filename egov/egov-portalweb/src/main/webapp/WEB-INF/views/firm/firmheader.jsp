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
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title" style="text-align:center;"><spring:message code="lbl.createfirm" /></div>
	</div>
	
	<input type="hidden" value="<spring:message code="error.firm.pan" />" id="errorPanNumber" />
	<input type="hidden" value="<spring:message code="Pattern.citizen.mobileNumber" />" id="errorMobileNumber" />
	<input type="hidden" value="<spring:message code="error.firm.mobilenumber.unique" />" id="errorUniqueMobileNumber" />
	<input type="hidden" value="<spring:message code="error.firm.email.unique" />" id="errorUniqueEmailid" />
	<input type="hidden" value="<spring:message code="error.firm.emailid" />" id="errorEmailId" />
	
	
	
	<div class="panel-body">
		<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.firmname" /><span
						class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:input path="name" name="name" value="${name}" id="firmname" class="form-control table-input name" maxlength="250"/>
				<form:errors path="name" cssClass="add-margin error-msg" />
			</div>
			<label class="col-sm-2 control-label text-right"><spring:message code="lbl.pan" /><span
						class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:input path="pan" class="form-control" id="pan" maxlength="10" />
				<form:errors path="pan" cssClass="add-margin error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.address" /><span
						class="mandatory"></span></label>	
			<div class="col-sm-3 add-margin">
				<form:textarea path="address" class="form-control autogrow" id="address" maxlength="1024" required="required" />
				<form:errors path="address" cssClass="add-margin error-msg" />
			</div>
		</div>
	</div>
</div>