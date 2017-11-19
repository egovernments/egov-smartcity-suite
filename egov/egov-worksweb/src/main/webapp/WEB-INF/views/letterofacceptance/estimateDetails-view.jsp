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
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.estimatenumber" /></label>
	<div class="col-sm-3 add-margin">  
		<form:input path="estimateNumber" name="estimateNumber" type="text" class="form-control" readonly="true" /> 
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.workidentificationnumber" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="projectCode" value="${lineEstimateDetails.projectCode.code}" disabled>
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.nameofwork" /></label>
	<div class="col-sm-3 add-margin">
		<textarea class="form-control" id="nameOfWork" disabled>${lineEstimateDetails.nameOfWork}</textarea>
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.department" /></label>
	<div class="col-sm-3 add-margin">	
		<input type="text" class="form-control" id="department" value="${lineEstimateDetails.lineEstimate.executingDepartment.name}" disabled>
	</div>
</div>
<c:if test="${lineEstimateDetails.lineEstimate.spillOverFlag && lineEstimateDetails.lineEstimate.workOrderCreated }">
	<div class="form-group">
		<label class="col-sm-3 control-label text-right"><spring:message code="lbl.loanumber" /><span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin">
			 <form:input path="workOrderNumber" name="workOrderNumber" id="workOrderNumber" type="text" onkeyup="alphanumerichyphenbackslash(this);" class="form-control" maxlength="32" required="required"/>
			 <form:errors path="workOrderNumber" cssClass="add-margin error-msg" />
		</div>
	</div>
</c:if>