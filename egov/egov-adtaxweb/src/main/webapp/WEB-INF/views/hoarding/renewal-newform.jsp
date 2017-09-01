<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~      accountability and the service delivery of the government  organizations.
  ~
  ~       Copyright (C) 2016  eGovernments Foundation
  ~
  ~       The updated version of eGov suite of products as by eGovernments Foundation
  ~       is available at http://www.egovernments.org
  ~
  ~       This program is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       any later version.
  ~
  ~       This program is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU General Public License for more details.
  ~
  ~       You should have received a copy of the GNU General Public License
  ~       along with this program. If not, see http://www.gnu.org/licenses/ or
  ~       http://www.gnu.org/licenses/gpl.html .
  ~
  ~       In addition to the terms of the GPL license to be adhered to in using this
  ~       program, the following additional terms are to be complied with:
  ~
  ~           1) All versions of this program, verbatim or modified must carry this
  ~              Legal Notice.
  ~
  ~           2) Any misrepresentation of the origin of the material is prohibited. It
  ~              is required that all modified versions of this material be marked in
  ~              reasonable ways as different from the original version.
  ~
  ~           3) This license does not grant any rights to any user of the program
  ~              with regards to rights under trademark law for use of the trade names
  ~              or trademarks of eGovernments Foundation.
  ~
  ~     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row">
	<div class="col-md-12">
		<input type="hidden" name="isEmployee" id="isEmployee" value="${isEmployee}" /> 
	
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
		</c:if>
		<form:form id="advertisementform" action="" method="post" class="form-horizontal form-groups-bordered" 
		modelAttribute="renewalPermitDetail" commandName="renewalPermitDetail" enctype="multipart/form-data">
		<form:hidden path="" id="workFlowAction" name="workFlowAction"/>
		<form:hidden path="" id="statuscode" value="${renewalPermitDetail.status.code}"/>
		<form:hidden path="" id="wfstate" value="${renewalPermitDetail.state.id}"/> 
		<form:hidden path="previousapplicationid" id="previousapplicationid" value="${renewalPermitDetail.previousapplicationid.id}"/> 
		<form:hidden path="" id="renewpermitId" value="${renewalPermitDetail.id}"/> 
		<form:hidden path="advertisement" id="adid" value="${renewalPermitDetail.advertisement.id}"/>
		
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
							<form:select path="advertisement.category" id="category" cssClass="form-control" cssErrorClass="form-control error" required="required">
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
						<form:hidden path="advertisement.legacy" id="legacy" value="${renewalPermitDetail.advertisement.legacy}" />
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
							<input type="text" id="agencyTypeAhead" class="form-control typeahead" autocomplete="off" value="${renewalPermitDetail.agency.name}">
							<form:hidden path="agency" id="agencyId" value="${renewalPermitDetail.agency.id}" />
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
					<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.advertisement.documents" />
	</div>
</div>

<div  id="hoardingattachments">
	<c:choose>
		<c:when test="${not empty hoardingDocumentTypes}">
			<form:hidden path="advertisement.latitude" id="latitude" />
			<form:hidden path="advertisement.longitude" id="longitude" />
			<div class="col-sm-12 view-content header-color hidden-xs">
				<div class="col-sm-1 table-div-column">
					<spring:message code="lbl.srl.no" />
				</div>
				<div class="col-sm-3 table-div-column ">
					<spring:message code="lbl.documentname" />
				</div>
				<div class="col-sm-3 table-div-column text-center">
					<spring:message code="lbl.enclosed" />
				</div>
				<div class="col-sm-5 table-div-column text-center">
					<spring:message code="lbl.attachdocument" />
				</div>
			</div>

			<c:forEach var="docs" items="${hoardingDocumentTypes}"
				varStatus="status1">
				<div class="form-group">
					<div class="col-sm-1 text-center">${status1.index+1}</div>
					<div class="col-sm-3 text-left">${renewalPermitDetail.advertisement.documents[status1.index].doctype.mandatory ? "<span
				class='mandatory'></span>" : ""}${renewalPermitDetail.advertisement.documents[status1.index].doctype.name}
					</div>
					<div class="col-sm-3 text-center">
						<input type="checkbox"
							${renewalPermitDetail.advertisement.documents[status1.index].enclosed && renewalPermitDetail.advertisement.documents[status1.index].files.size()>0 ? "checked='checked'  disabled='disabled'" : ""}
							name="advertisement.documents[${status1.index}].enclosed"
							${renewalPermitDetail.advertisement.documents[status1.index].doctype.mandatory ? "required='required'" : ""}>
					</div>
					<div class="col-sm-5 text-center">
						<c:forEach var="file"
							items="${renewalPermitDetail.advertisement.documents[status1.index].files}">
							<a
								href="/egi/downloadfile?fileStoreId=${file.fileStoreId}&moduleName=ADTAX&toSave=true">
								${file.fileName}<br>
							</a>

						</c:forEach>

						<input type="file"
							name="advertisement.documents[${status1.index}].attachments"
							class="form-control">
						<form:errors
							path="advertisement.documents[${status1.index}].attachments"
							cssClass="add-margin error-msg" />
						<form:hidden
							path="advertisement.documents[${status1.index}].doctype"
							value="${advertisement.documents[status1.index].doctype.id}" />
						<form:hidden path="advertisement.documents[${status1.index}].id"
							value="${advertisement.documents[status1.index].id}" />

					</div>


				</div>
			</c:forEach>
		</c:when>
	</c:choose>
</div>
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
var subcategory = '${renewalPermitDetail.advertisement.subCategory.id}';
var adminBoundry = '${renewalPermitDetail.advertisement.ward.id}';
var revenueBoundary = '${renewalPermitDetail.advertisement.locality.id}';
var statuscode = '${renewalPermitDetail.status.code}';
$("#locationDiv input, #locationDiv textarea, #locationDiv select").prop("disabled", true);
if(statuscode=='APPROVED' || statuscode=='ADTAXAMTPAYMENTPAID' || statuscode=='ADTAXAMTPAYMENTPENDING'){
	$("#adtaxdetailsbody input, #adtaxdetailsbody textarea, #adtaxdetailsbody select").prop("disabled", true);
}

$( ".workflow-submit" ).click(function( e ) {
	if($('#advertisementform').valid()){
		if(DateValidation($('#permissionstartdate').val() , $('#permissionenddate').val())){
			if(parseInt($('#measurement').val()) <= 0){
				bootbox.alert('Please enter valid measurement');
				e.preventDefault();
				return false;
			}else{ 
				return true;
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