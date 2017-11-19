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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<div class="row">
	<div class="col-md-12">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
		</c:if>
		<form:form id="advertisementform" action="" method="post" class="form-horizontal form-groups-bordered" 
		modelAttribute="advertisementPermitDetail" commandName="advertisementPermitDetail" enctype="multipart/form-data">
		<form:hidden path="" id="workFlowAction" name="workFlowAction"/>
		<form:hidden path="" id="statuscode" value="${advertisementPermitDetail.status.code}"/>
		<form:hidden path="" id="wfstate" value="${advertisementPermitDetail.state.id}"/> 
		<form:hidden path="" id="adtaxpermitId" value="${advertisementPermitDetail.id}"/> 
		<form:hidden path="" id="isReassignEnabled" value="${isReassignEnabled}"/> 
		<form:hidden path="" id="applicationType" value="${applicationType}"/>
		<form:hidden path="" id="nextAction" value="${advertisementPermitDetail.state.nextAction}"/> 
		 
		<div class="panel panel-primary" data-collapsed="0" id="adtaxdetailsbody">
			<div class="panel-heading">
				<ul class="nav nav-tabs" id="settingstab">
					<li class="active"><a data-toggle="tab"
						href="#hoardingdetails" data-tabidx="0" aria-expanded="false"><spring:message code="lbl.hoarding.details"/></a></li>
				</ul>
			</div>
			<div class="panel-body custom-form">
			<div class="tab-content">
				<div class="tab-pane fade active in" id="hoardingdetails">	
					<div class="form-group">
						<label class="col-sm-3 control-label text-right">
						<spring:message code="lbl.category"/>
						<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="advertisement.category" id="category" cssClass="form-control" cssErrorClass="form-control error" required="required" disabled="true">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<form:options items="${hoardingCategories}" itemLabel="name" itemValue="id"/>
							</form:select>
							<form:errors path="advertisement.category" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right">
						<spring:message code="lbl.subcategory"/>
						<span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:select path="advertisement.subCategory" id="subCategory" cssClass="form-control" cssErrorClass="form-control error" required="required">
								<form:option value=""><spring:message code="lbl.select"/></form:option>
							</form:select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right">
						<spring:message code="lbl.advertisement.class"/>
						<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="advertisement.rateClass" id="rateClass" cssClass="form-control" cssErrorClass="form-control error" required="required">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<form:options items="${rateClasses}" itemLabel="description" itemValue="id"/>
							</form:select>
							<form:errors path="advertisement.rateClass" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right">
						<spring:message code="lbl.rev.inspector"/><span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:select path="advertisement.revenueInspector" id="revenueInspector" cssClass="form-control" cssErrorClass="form-control error" required="required">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<form:options items="${revenueInspectors}" itemLabel="name" itemValue="id"/>
							</form:select>
							<form:errors path="advertisement.revenueInspector" cssClass="error-msg" />
						</div>
					</div>
						<div class="form-group">
							<label class="col-sm-3 control-label text-right">
							<spring:message code="lbl.advertisement.electricityservicenumber"/>
							</label>
							<div class="col-sm-3 add-margin">
								<form:input type="text" class="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash"  maxlength="25"  path="advertisement.electricityServiceNumber" id="electricityServiceNumber"/>
                              		<form:errors path="advertisement.electricityServiceNumber" cssClass="error-msg" />
                          		</div>
						</div>
							
					<div class="form-group">
						<label class="col-sm-3 control-label text-right">
						<spring:message code="lbl.advertisement.type"/>
						<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin dynamic-span capitalize">
							<form:radiobuttons path="advertisement.type" element="span" />
							<form:errors path="advertisement.type" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right">
						<spring:message code="lbl.advertisement.prop.type"/>
						<span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:select path="advertisement.propertyType" id="propertyType" cssClass="form-control" cssErrorClass="form-control error" required="required">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<form:options items="${propertyType}"/>
							</form:select>
							<form:errors path="advertisement.propertyType" cssClass="error-msg" />
						</div>
					</div>
					<div class="panel-heading custom_form_panel_heading">
						<div class="panel-title">
						<spring:message code="lbl.advertisement.permitDetail"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right">
						<spring:message code="lbl.advertisement.application.no"/>
						</label>
						<div class="col-sm-3 add-margin">
						<form:hidden path="advertisement.legacy" id="legacy" value="${advertisementPermitDetail.advertisement.legacy}" />
						<form:hidden path="advertisement.status" id="advStatus" value="${advertisement.status}" />
						<%-- 
						<form:hidden path="status" id="status" value="${status.id}" /> 
						 --%>
						<form:input type="text"  cssClass="form-control patternvalidation" 
                      	      data-pattern="alphanumerichyphenbackslash" path="applicationNumber" maxlength="25" id="applicationNumber" readonly="true"/>
                        	<form:errors path="applicationNumber" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right">
						<spring:message code="lbl.application.date"/>
						<span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:input type="text" cssClass="form-control datepicker" path="applicationDate" id="applicationDate" required="required" readonly="true"/>
                            <form:errors path="applicationDate" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right">
						<spring:message code="lbl.advertisement.permission.no"/>
						</label>
						<div class="col-sm-3 add-margin">
							<form:input type="text" cssClass="form-control patternvalidation" 
                      	      data-pattern="alphanumerichyphenbackslash" maxlength="25"  path="permissionNumber" id="permissionNumber" readonly="true"/>
                             		<form:errors path="permissionNumber" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right">
						<spring:message code="lbl.advertisement.no"/></label>
						<div class="col-sm-3 add-margin">
							<form:input type="text" cssClass="form-control patternvalidation" 
                      	      data-pattern="username" maxlength="25"  path="advertisement.advertisementNumber" id="advertisementnumber" readonly="true"/>
                             		<form:errors path="advertisement.advertisementNumber" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right">
						<spring:message code="lbl.advertisement.agency"/>
						</label>
						<div class="col-sm-3 add-margin">
							<input type="text" id="agencyTypeAhead" class="form-control typeahead" autocomplete="off" value="${advertisementPermitDetail.agency.name}">
							<form:hidden path="agency" id="agencyId" value="${advertisementPermitDetail.agency.id}" />
							<form:errors path="agency" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right">
						<spring:message code="lbl.advertisement.adv"/>
						</label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="advertiser" id="advertiser" cols="5" rows="2" class="form-control patternvalidation"  data-pattern="alphanumericwithspace"  minlength="5" maxlength="125"/>
                            <form:errors path="advertiser" cssClass="error-msg" /> 
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right">
						<spring:message code="lbl.advertisement.ad.particulars"/>
						<span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="advertisementParticular" cols="5" rows="2" class="form-control patternvalidation" data-pattern="alphanumericwithspace"  required="required" minlength="5" maxlength="256"/>
							<form:errors path="advertisementParticular" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right">
						<spring:message code="lbl.owner.detail"/>
						</label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="ownerDetail" cols="5" rows="2" class="form-control patternvalidation" data-pattern="alphanumericwithspace" minlength="5" maxlength="125"/>
                            <form:errors path="ownerDetail" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right">
						<spring:message code="lbl.advertisement.permit.fromdate"/>
						<span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:input type="text" cssClass="form-control datepicker" path="permissionstartdate" id="permissionstartdate" required="required"/>
                            <form:errors path="permissionstartdate" cssClass="error-msg" />
                           	</div>
				     	<label class="col-sm-2 control-label text-right">
						<spring:message code="lbl.advertisement.permit.todate"/>
						<span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:input type="text" cssClass="form-control datepicker" path="permissionenddate" id="permissionenddate" required="required"/>
                            <form:errors path="permissionenddate" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right">
							<spring:message code="lbl.advertisement.duration"/> <span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:select path="advertisementDuration" id="advertisementDuration" cssClass="form-control" cssErrorClass="form-control error" required="required">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<form:options items="${advertisementDuration}"/>
							</form:select>
							<form:errors path="advertisementDuration" cssClass="error-msg" />
						</div>
					</div>	
								
					<div id="locationDiv">
						<jsp:include page="hoarding-location.jsp"></jsp:include>
					</div>
					<div class="panel-heading custom_form_panel_heading">
						<div class="panel-title">
						<spring:message code="lbl.advertisement.struct"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right">
						<spring:message code="lbl.advertisement.measurement"/>
						<span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:input type="text" class="form-control patternvalidation" data-pattern="decimalvalue"  maxlength="15" path="measurement" id="measurement" required="required"/>
                            <form:errors path="measurement" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right">
						<spring:message code="lbl.uom"/>
						<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="unitOfMeasure" id="unitOfMeasure" cssClass="form-control" cssErrorClass="form-control error" required="required">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<form:options items="${uom}" itemLabel="description" itemValue="id"/>
							</form:select>
							<form:errors path="unitOfMeasure" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right">
						<spring:message code="lbl.length"/>
						</label>
						<div class="col-sm-3 add-margin">
							<form:input type="text" class="form-control patternvalidation" data-pattern="decimalvalue"  maxlength="15" path="length" id="length"/>
                            <form:errors path="length" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right">
						<spring:message code="lbl.width"/>
						</label>
						<div class="col-sm-3 add-margin">
							<form:input type="text" class="form-control patternvalidation" data-pattern="decimalvalue"  maxlength="15"  path="width" id="width"/>
                            <form:errors path="width" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right">
						<spring:message code="lbl.advertisement.total.height"/>
						</label>
						<div class="col-sm-3 add-margin">
							<form:input type="text" class="form-control patternvalidation" data-pattern="decimalvalue"  maxlength="15"  path="totalHeight" id="totalHeight" />
                            <form:errors path="totalHeight" cssClass="error-msg" />
						</div>
					</div>
						
					<div class="panel-heading custom_form_panel_heading">
						<div class="panel-title">
						<spring:message code="lbl.advertisement.tax.feeDetails"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right">
						<spring:message code="lbl.advertisement.currentYearTax"/>
						<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input type="text" class="form-control patternvalidation" data-pattern="decimalvalue"  maxlength="15"  path="taxAmount" id="taxAmount" required="required"/>
                            <form:errors path="taxAmount" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right">
						<spring:message code="lbl.advertisement.enc.fee"/>
						</label>
						<div class="col-sm-3 add-margin">
							<form:input type="text" class="form-control patternvalidation" data-pattern="decimalvalue"  maxlength="15"  path="encroachmentFee" id="encroachmentFee"/>
                            <form:errors path="encroachmentFee" cssClass="error-msg" />
						</div>
					</div>
				</div>
					<jsp:include page="document-update.jsp"></jsp:include>
			</div>
			</div>
		</div>
		<c:if test="${isEmployee}">
			<jsp:include page="../workflow/commonWorkflowMatrix.jsp"/>
			</c:if>
			<div class="buttonbottom" align="center">
				<jsp:include page="../workflow/commonWorkflowMatrix-button.jsp" />
			</div>
		</form:form>
	</div>
</div>
<script>

//this is to reset the sub combobox upon field error
var subcategory = '${advertisementPermitDetail.advertisement.subCategory.id}';
var adminBoundry = '${advertisementPermitDetail.advertisement.ward.id}';
var revenueBoundary = '${advertisementPermitDetail.advertisement.locality.id}';
var statuscode = '${advertisementPermitDetail.status.code}';
$("#locationDiv input, #locationDiv textarea, #locationDiv select").prop("disabled", true);
if(statuscode=='APPROVED' || statuscode=='ADTAXAMTPAYMENTPAID' || statuscode=='ADTAXAMTPAYMENTPENDING'){
	$("#adtaxdetailsbody input, #adtaxdetailsbody textarea, #adtaxdetailsbody select").prop("disabled", true);
}

$( ".workflow-submit" ).click(function( e ) {
	if($('#advertisementform').valid()){
		if(DateValidation($('#permissionstartdate').val() , $('#permissionenddate').val())){
			if(parseFloat($('#measurement').val()) <= 0){
				bootbox.alert('Please enter valid measurement');
				e.preventDefault();
				return false;
			}else{ 
				if(document.getElementById("workFlowAction").value!=null 
						&& document.getElementById("workFlowAction").value=='Reject'
						&& document.getElementById("nextAction").value=='Rejected')
				{
					if(confirm("Advertisement will be cancelled. Do you want to proceed?"))
						document.forms['advertisementform'].submit(); 
					else
						return false;
				}else 
					document.forms['advertisementform'].submit();
			}
		}else{
			e.preventDefault();
		}
	}else {
		e.preventDefault();
		return false;
	}

});
	

</script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/exif.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/app/js/hoarding.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>