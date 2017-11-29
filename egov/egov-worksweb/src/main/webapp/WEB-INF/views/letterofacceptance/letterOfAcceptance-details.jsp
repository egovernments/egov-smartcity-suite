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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<input type="hidden" id="technicalSanctionDate" class="form-control datepicker" maxlength="10" data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" value='<fmt:formatDate value="${lineEstimateDetails.lineEstimate.technicalSanctionDate }" pattern="dd/MM/yyyy"/>' />
<input type="hidden" id="errorFileDate" value="<spring:message code='error.loa.filedate'/>">
<input type="hidden" id="errorWorkOrderDate" value="<spring:message code='error.loa.workorderdate'/>">
<input type="hidden" id="spillOverFlag" value="${lineEstimateDetails.lineEstimate.spillOverFlag }">
<input type="hidden" id="workOrderCreated" value="${lineEstimateDetails.lineEstimate.workOrderCreated }">
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.file.no" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash" id="fileNumber" path="fileNumber" maxlength="34" required="required" />
		<form:errors path="fileNumber" cssClass="add-margin error-msg" />		
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.file.date" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input id="fileDate" path="fileDate" class="form-control datepicker" data-date-end-date="0d" required="required" />
		<form:errors path="fileDate" cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.estimateamount" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control text-right" data-pattern="decimalvalue" id="estimateAmount" value="${lineEstimateDetails.actualEstimateAmount}" disabled> 
	</div>
	
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.tender.finalized.percentage" /></label><div class="col-sm-3">
		<div class="input-group" style="margin-bottom: 0;">
		    <div class="input-group-btn number-sign">
               <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><span class="sign-text">+</span> &nbsp;<span class="caret"></span></button>
               <ul class="dropdown-menu">
                  <li><a href="javascript:void(0);">+</a></li>
                  <li><a href="javascript:void(0);">-</a></li>                  
               </ul>
               <form:hidden path="percentageSign" name="percentageSign" id="percentageSign" />
            </div>
            <form:input path="tenderFinalizedPercentage" name="tenderFinalizedPercentage" id="tenderFinalizedPercentage" type="text" class="form-control patternvalidation" maxlength="8" />
			<form:errors path="tenderFinalizedPercentage" cssClass="add-margin error-msg" />
        </div> 
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.agreement.amount" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		 <form:input path="workOrderAmount" name="workOrderAmount" id="workOrderAmount"  type="text" class="form-control text-right patternvalidation" maxlength="12" data-pattern="decimalvalue" required="required"/>
		 <form:errors path="workOrderAmount" cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.dateofagreement" />
		<c:if test="${lineEstimateDetails.lineEstimate.spillOverFlag && lineEstimateDetails.lineEstimate.workOrderCreated }">
			<span class="mandatory"></span>
		</c:if>
	</label>
	<div class="col-sm-3 add-margin">
		<c:choose>
			<c:when test="${lineEstimateDetails.lineEstimate.spillOverFlag && lineEstimateDetails.lineEstimate.workOrderCreated }">
				<form:input path="workOrderDate" id="workOrderDate" type="text" class="form-control datepicker" data-date-end-date="0d" required="required"/>
			</c:when>
			<c:otherwise>
				<form:input path="workOrderDate" id="workOrderDate" type="text" class="form-control" value="${workOrderDate}" readonly="true" />
			</c:otherwise>
		</c:choose>
		<form:errors path="workOrderDate" cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.nameofagency" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin" style="margin-bottom: 0;">
		<div class="input-group">
			<input id="contractorSearch" name="contractorSearch" value="${contractorSearch}" class="form-control patternvalidation" autocomplete="off" data-pattern="alphanumericspecialcharacters" maxlength="100" required="required" type="text" placeholder="Type first 3 letters of Agency /Firm Name" > 
			<span class="input-group-addon"> <i class="fa fa-search specific" onclick="searchContractor();"></i></span>
			<form:hidden path="contractor.id" name="contractor" id="contractor" />
		</div>	
		<form:errors path="contractor" cssClass="add-margin error-msg" />  
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.contractor.code" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="contractorCode" name="contractorCode" value="${contractorCode}" disabled>
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.preparedby" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="preparedBy" value="${loggedInUser}" disabled>
	</div>
</div>

<div class="form-group" style="display: block;">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.emd.amount" /></label>
	<div class="col-sm-3 add-margin">
		<form:input path="emdAmountDeposited" name="emdAmountDeposited" id="emdAmountDeposited" type="text" class="form-control text-right patternvalidation" maxlength="12" data-pattern="decimalvalue" />
		<form:errors path="emdAmountDeposited" cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.bank.guarantee" /></label>
    <div class="col-sm-3 add-margin">
    	<form:textarea name="bankGuarantee" path="bankGuarantee" id="bankGuarantee" class="form-control" maxlength="1024" ></form:textarea>
		<form:errors path="bankGuarantee" cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.additional.security.deposit" /></label>
	<div class="col-sm-3 add-margin">
	 	<form:input path="securityDeposit" name="securityDeposit" type="text" class="form-control text-right patternvalidation" maxlength="12" data-pattern="decimalvalue" />
		<form:errors path="securityDeposit" cssClass="add-margin error-msg" />
	</div>	
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.contract.period" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input path="contractPeriod" name="contractPeriod" id="contractPeriod" type="text" class="form-control text-right patternvalidation" maxlength="4" data-pattern="number" required="required" />
		<form:errors path="contractPeriod" cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.dlp" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input path="defectLiabilityPeriod" name="defectLiabilityPeriod" id="defectLiabilityPeriod" type="text" class="form-control text-right patternvalidation" maxlength="4" data-pattern="decimalvalue" required="required" />
		<form:errors path="defectLiabilityPeriod" cssClass="add-margin error-msg" />
	</div> 
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.engineer.incharge" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select path="engineerIncharge" data-first-option="false" id="engineerIncharge" class="form-control" required="required">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<c:forEach var="assignment" items="${engineerInchargeList}">
		        <form:option value="${assignment.employee.id}"><c:out value="${assignment.employee.name} - ${assignment.designation.name}"/></form:option>
		    </c:forEach>   
		</form:select>
		<form:errors path="engineerIncharge" cssClass="add-margin error-msg" />
	</div>
</div>
