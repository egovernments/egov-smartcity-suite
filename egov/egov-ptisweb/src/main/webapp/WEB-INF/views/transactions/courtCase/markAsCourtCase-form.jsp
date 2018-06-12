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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<style>
body {
	font-family: regular !important;
	font-size: 14px;
}
</style>

<c:if test="${errorMsg != ''}">
	<div class="panel-heading" style="text-align: center">
		<div class="add-margin error-msg" style="text-align: center;">
			<spring:message code="${errorMsg}" />
		</div>
	</div>
</c:if>

<div class="row">
	<div class="col-md-12">
			<form:form class="form-horizontal form-groups-bordered" method="post"
			name="markAscourtCaseForm" id="markAscourtCaseForm" action=""
			modelAttribute="propertyCourtCase">
			<div class="panel panel-primary" data-collapsed="0"
				style="text-align: left">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary" data-collapsed="0"
							style="text-align: left">
							<div class="panel-heading">
			               <div class="panel-title" style="text-align: center" >
				            <spring:message code="lbl.courtcase.title" />
			               </div>
		                    </div>
		                    
							<div class="panel-heading">
								<div class="panel-title">
									<spring:message code="lbl.hdr.propertydetails"  />
								</div>
							</div>
							<div class="panel-body">
								<div class="row add-border">
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.assmtno" />
									</div>
									<div class="col-xs-3 add-margin view-content">
										<form:hidden path="assessmentNo"
											value="${propertyCourtCase.assessmentNo}" />
										<c:out value="${propertyCourtCase.assessmentNo}">
										</c:out>
									</div>
									<div class="col-sm-3 add-margin">
										<spring:message code="lbl.doorno" />

									</div>
									<div class="col-sm-3 add-margin view-content">
										<c:out value="${doorNumber}" default="N/A" />

									</div>
								</div>
								<div class="row add-border">
									<div class="col-sm-3 add-margin">
										<spring:message code="lbl.OwnerName" />

									</div>
									<div class="col-sm-3 add-margin view-content">
										<c:out value="${ownerName}" default="N/A" />
									</div>
									<div class="col-sm-3 add-margin">
										<spring:message code="lbl.property.address" />
									</div>
									<div class="col-sm-3 add-margin view-content">
										<c:out value="${address}" />
									</div>
								</div>

								<div class="row add-border">
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.propertytype" />
									</div>
									<div class="col-xs-3 add-margin view-content">
										<c:out value="${category}"></c:out>
									</div>

									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.category.ownership" />
									</div>
									<div class="col-xs-3 add-margin view-content">
										<c:out default="N/A" value="${propertytype}"></c:out>
									</div>
								</div>
								<div class="row add-border">
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.totaltax.due" />
									</div>
									<div class="col-xs-3 add-margin view-content">
										<c:out value="${taxDues}">
										</c:out>
									</div>

									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.yearly.tax" />
									</div>
									<div class="col-xs-3 add-margin view-content">
										<c:out value="${yearlyTax}">
										</c:out>
									</div>
								</div>

								<div class="row add-border">
									<div class="col-sm-3 add-margin">
										<spring:message code="lbl.legal.case.no" />
										<span class="mandatory1">*</span>
									</div>
									<div class="col-sm-3 add-margin">
										<form:input path="caseNo" id="caseno" name="caseno"
											cssClass="form-control patternvalidation" />
									</div>

									<div class="col-sm-3 add-margin">
										<div id="caseNodiv"></div>
									</div>
								</div>

								<div class="caseDetails">
									<div class="row add-border">
										<div class="col-sm-3 add-margin">
											<spring:message code="lbl.case.type" />
										</div>
										<div class="col-sm-3 add-margin view-content">
											<form:textarea id="caseType" path="" cssClass="form-control"
												disabled="true" value="${caseType}" default="N/A" />
										</div>
										<div class="col-sm-3 add-margin">
											<spring:message code="lbl.case.title" />
										</div>
										<div class="col-sm-3 add-margin view-content">
											<form:textarea id="caseTitle" path="" cssClass="form-control"
												disabled="true" value="${caseTitle}" default="N/A" />
										</div>
									</div>
									<div class="row add-border">
										<div class="col-xs-3 add-margin">
											<spring:message code="lbl.case.status" />
										</div>
										<div class="col-xs-3 add-margin view-content">
											<form:input id="caseStatus" path="" disabled="true"
												cssClass="form-control" value="${caseStatus}" default="N/A" />
										</div>
										<div class="col-xs-3 add-margin">
											<spring:message code="lbl.judgment.type" />
										</div>
										<div class="col-xs-3 add-margin view-content">
											<form:textarea id="judgementType" path=""
												cssClass="form-control" disabled="true"
												value="${judgementType}" default="N/A" />
										</div>
									</div>
									<div class="row add-border">
										<div class="col-xs-3 add-margin">
											<spring:message code="lbl.judgment.desc" />
										</div>
										<div class="col-xs-3 add-margin view-content">
											<form:textarea id="judgementDesc" path=""
												cssClass="form-control" disabled="true"
												value="${judgementDesc}" default="N/A" />
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="text-center">
						<button type="submit" class="btn btn-primary add-margin"
							id="submitform" disabled="disabled">
							<spring:message code="lbl.submit" />
						</button>
						<a href="javascript:void(0)" class="btn btn-default"
							onclick="self.close()"><spring:message code="lbl.close" /></a>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>
<script
	src="<cdn:url value='/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}' context='/egi'/>">
	
</script>

<script type="text/javascript">
	var caseno = new Bloodhound({
		datumTokenizer : function(datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		remote : {
			url : "/lcms/legalcase/ajax-caseNumber?caseNumber=%QUERY",
			filter : function(data) {
				return $.map(data, function(ct) {
					return {
						name : ct.Value
					};
				});
			}
		}
	});

	caseno.initialize();
	var caseno_typeahead = $('#caseno').typeahead({
		hint : true,
		highlight : true,
		minLength : 2
	}, {
		displayKey : 'name',
		source : caseno.ttAdapter()
	}).on("typeahead:selected", function(obj, datum, name) {
		getCaseDetails(this);
	}).on("blur", function(obj, datum, name) {
		getCaseDetails(this);
	});

	function getCaseDetails(obj) {
		jQuery
				.ajax({
					url : "/lcms/legalcase/ajax-caseDetailsByCaseNumber?caseNumber="
							+ jQuery(obj).val(),
					type : "GET",
					success : function(response) {
						if (response.caseType) {
							jQuery('.caseDetails').show();
							jQuery('#caseNodiv').show();
							jQuery('#caseNodiv')
									.html(
											'<a href='+response.viewLegalCase+' target="_blank">View Legal Case Details</a>');
							jQuery('#caseType').val(response.caseType);
							jQuery('#caseTitle').val(response.caseTitle);
							jQuery('#caseStatus').val(response.caseStatus);
							if (response.judgmentDescription != "")
								jQuery('#judgementDesc').val(
										response.judgmentDescription);
							else
								jQuery('#judgementDesc').val('N/A');
							if (response.judgmentType != "")
								jQuery('#judgementType').val(
										response.judgmentType);
							else
								jQuery('#judgementType').val('N/A');
							jQuery('#submitform').removeAttr('disabled');
						} else {
							jQuery('.caseDetails').hide();
							jQuery('#caseNodiv').hide();
							jQuery('#caseType').val('');
							jQuery('#caseTitle').val('');
							jQuery('#caseStatus').val('');
							jQuery('#judgementDesc').val('');
							jQuery('#judgementType').val('');
							jQuery('#submitform').attr('disabled', 'disabled');
							bootbox.alert("Case Number doesnot exist!");
							return false;
						}

					},
					complete : function() {
						jQuery('.loader-class').modal('hide');
					},
					error : function() {
						return false;
					}
				});

	}

	jQuery(document).ready(function() {
		jQuery('.caseDetails').hide();
	});
</script>