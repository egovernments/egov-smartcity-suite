<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
	The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript" src="<c:url value='/resources/javascript/validations.js'/>"></script>

<form:form id="demolition" method="post" class="form-horizontal form-groups-bordered" modelAttribute="property">
	<div class="page-container" id="page-container">
		<div class="main-content">
			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0"
						style="text-align: left">
						<div class="panel-heading">
							<div class="panel-title">Property Details</div>
						</div>
						<div class="panel-body">
							<div class="row add-border">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.assmtno" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${property.basicProperty.upicNo}"></c:out>
								</div>
								
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.assmtno.parentproperty" />
								</div>
								<div class="col-xs-3 add-margin view-content">N/A</div>
							</div>
							<div class="row add-border">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.propertytype" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${property.propertyDetail.propertyType}"></c:out>
								</div>
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.excemption" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${property.taxExemptedReason.name}"></c:out>
								</div>
							</div>
							<div class="row add-border">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.annualvalue" />
								</div>
								<div class="col-xs-3 add-margin view-content">Rs.format.money</div>
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.effectivedate" />
								</div>
								<div class="col-xs-3 add-margin view-content">N/A</div>
							</div>
							<div class="row add-border">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.category.ownership" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${property.propertyDetail.categoryType}"></c:out>
								</div>
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.appartmentorcomplex" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${property.propertyDetail.apartment.name}"
										default="N/A"></c:out>
								</div>
							</div>
							<div class="row add-border">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.extentofsite" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${property.propertyDetail.sitalArea.area}"></c:out>
								</div>
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.extent.appurtenant" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out
										value="${property.propertyDetail.extentAppartenauntLand}"></c:out>
								</div>
							</div>
							<div class="row add-border">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.superstructure" />
								</div>
								<c:choose>
									<c:when test="${property.propertyDetail.structure == 'true'}">
										<div class="col-xs-3 add-margin view-content">Yes</div>
									</c:when>
									<c:otherwise>
										<div class="col-xs-3 add-margin view-content">No</div>
									</c:otherwise>
								</c:choose>
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.siteowner" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${property.propertyDetail.siteOwner}"></c:out>
								</div>
							</div>
							<div class="row add-border">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.registrationDoc.no" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${property.basicProperty.regdDocNo}"></c:out>
								</div>
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.registrationDoc.date" />
								</div>
								<fmt:formatDate value="${property.basicProperty.regdDocDate}"
									var="registrationDate" pattern="dd-MM-yyyy" />
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${registrationDate}"></c:out>
								</div>
							</div>
							<div class="row add-border">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.bpno" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${property.propertyDetail.buildingPermissionNo}"></c:out>
								</div>
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.bpdate" />
								</div>
								<fmt:formatDate
									value="${property.propertyDetail.buildingPermissionDate}"
									var="bpregistrationDate" pattern="dd-MM-yyyy" />
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${bpregistrationDate}"></c:out>
								</div>
							</div>
							<div class="row add-border">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.percentagedeviation" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${property.propertyDetail.deviationPercentage}"></c:out>
								</div>
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.reason.creation" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${property.propertyModifyReason}"></c:out>
								</div>
							</div>

						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0"
						style="text-align: left">
						<div class="panel-heading">
							<div class="panel-title">
								<spring:message code="lbl.hdr.taxdetails" />
							</div>
						</div>
						<div class="panel-body">
							<div class="row add-border">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.currenttax" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${currTax}"></c:out>
								</div>
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.edutax" />
								</div>
								<div class="col-xs-3 add-margin view-content">N/A</div>
							</div>
							<div class="row add-border">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.currenttax.due" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${currTaxDue}"></c:out>
								</div>
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.librarycess" />
								</div>
								<div class="col-xs-3 add-margin view-content">N/A</div>
							</div>
							<div class="row add-border">
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.totalarrears.due" />
								</div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${totalArrDue}"></c:out>
								</div>
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.propertytax" />
								</div>
								<div class="col-xs-3 add-margin view-content">N/A</div>
							</div>
							<div class="row add-border">
								<div class="col-xs-6 add-margin"></div>
								<div class="col-xs-3 add-margin">
									<spring:message code="lbl.total.propertytax" />
								</div>
								<div class="col-xs-3 add-margin view-content">N/A</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<%-- <div id="OwnerNameDiv">
				<jsp:include page="../common/ownerDetailsFrom.jsp"></jsp:include>
			</div> --%>

			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0">

						<div class="panel-heading" style="text-align: left">
							<div class="panel-title">
								<spring:message code="lbl.hdr.demolition.details" />
							</div>
						</div>
						<div class="panel-body custom-form">
								<div class="form-group">
									<label class="col-sm-3 control-label text-right"><spring:message
											code="lbl.demolition.reason" /><span class="mandatory"></span>
									</label>
									<div class="col-sm-8 add-margin">
										<form:textarea path="demolitionReason" class="form-control"></form:textarea>
									</div>
								</div>
						</div>
					</div>
				</div>
			</div>


			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0">

						<div class="panel-heading" style="text-align: left">
							<div class="panel-title">
								<spring:message code="lbl.hdr.vacantland.details" />
							</div>
						</div>

						 <table width="100%" border="0" cellspacing="0" cellpadding="0"
								class="tablebottom" id="vacantLandTable">

								<tbody>
									<tr>
										<th class="bluebgheadtd"><spring:message code="lbl.surveyNumber" /><span
											class="mandatory">*</span>
										</th>
										<th class="bluebgheadtd"><spring:message code="lbl.pattaNumber" /><span
											class="mandatory1">*</span></th>
										<th class="bluebgheadtd"><spring:message code="lbl.vacantland.area" /><span
											class="mandatory1">*</span></th>
										<th class="bluebgheadtd"><spring:message code="lbl.MarketValue" /><span
											class="mandatory1">*</span>
										</th>
										<th class="bluebgheadtd"><spring:message code="lbl.currentCapitalValue" /><span
											class="mandatory1">*</span></th>
									</tr>

									<tr id="vacantLandRow">
										<td class="blueborderfortd" align="center"><form:input path="propertyDetail.surveyNumber" id="surveyNumber" maxlength="15"
											/></td>
										<td class="blueborderfortd" align="center"><form:input path="propertyDetail.pattaNumber" id="pattaNumber" maxlength="15"
											/></td>
										<td class="blueborderfortd" align="center"><form:input
											type="text" path="propertyDetail.sitalArea.area"
											maxlength="15" value="" id="vacantLandArea"
											onblur="trim(this,this.value);checkForTwoDecimals(this,'propertyDetail.sitalArea.area');checkZero(this,'propertyDetail.sitalArea.area');"/>
										</td>
										<td class="blueborderfortd" align="center"><form:input
											type="text" path="propertyDetail.marketValue" maxlength="15"
											value="" id="marketValue"
											onblur="trim(this,this.value);checkForTwoDecimals(this,'propertyDetail.marketValue');checkZero(this,'propertyDetail.marketValue');"/>
										</td>

										<td class="blueborderfortd"><form:input type="text"
											path="propertyDetail.currentCapitalValue" maxlength="15"
											value="" id="currentCapitalValue"
											onblur="trim(this,this.value);checkForTwoDecimals(this,'propertyDetail.currentCapitalValue');checkZero(this,'propertyDetail.currentCapitalValue');"
											readonly="true"/></td>

									</tr>

									<tr>
										<td colspan="6"><br>
											<table class="tablebottom" style="width: 100%;"
												id="boundaryData">
												<tbody>
													<tr>
														<th class="bluebgheadtd">North<span
															class="mandatory1">*</span></th>
														<th class="bluebgheadtd">East<span class="mandatory1">*</span></th>
														<th class="bluebgheadtd">West<span class="mandatory1">*</span></th>
														<th class="bluebgheadtd">South<span
															class="mandatory1">*</span></th>
													</tr>
													<tr>
														<td class="blueborderfortd" align="center"><input
															type="text" name="northBoundary" maxlength="64" value=""
															id="northBoundary"></td>
														<td class="blueborderfortd" align="center"><input
															type="text" name="eastBoundary" maxlength="64" value=""
															id="eastBoundary"></td>
														<td class="blueborderfortd" align="center"><input
															type="text" name="westBoundary" maxlength="64" value=""
															id="westBoundary"></td>
														<td class="blueborderfortd" align="center"><input
															type="text" name="southBoundary" maxlength="64" value=""
															id="southBoundary"></td>
													</tr>
												</tbody>
											</table></td>
									</tr>
								</tbody>
							</table> 
					</div>
				</div>
			</div>

			<%-- <div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0">

						<div class="panel-heading" style="text-align: left">
							<div class="panel-title">Approval Details</div>
						</div>
						<div class="panel-body custom-form">
							<form role="form" class="form-horizontal form-groups-bordered">
								<div class="form-group">
									<label class="col-md-2 col-xs-12 text-left">Approver
										Department <span class="mandatory"></span>
									</label>
									<div class="col-md-2 col-xs-12 add-margin">
										<select class="form-control"><option>select</option></select>
									</div>
									<label class="col-md-2 col-xs-12 text-left">Approver
										Designation <span class="mandatory"></span>
									</label>
									<div class="col-md-2 col-xs-12 add-margin">
										<select class="form-control"><option>select</option></select>
									</div>
									<label class="col-md-2 col-xs-12 text-left">Approver <span
										class="mandatory"></span>
									</label>
									<div class="col-md-2 col-xs-12 add-margin">
										<select class="form-control"><option>select</option></select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 col-xs-12 text-left"> Approver
										Remarks </label>
									<div class="col-sm-4 add-margin">
										<textarea class="form-control"></textarea>
									</div>
								</div>
								<br />
							</form>
						</div>
					</div>
				</div>
			</div> --%>

			<div class="form-group text-center">
				<button type="submit" class="btn btn-success"><spring:message code="lbl.submit"/></button>
				&nbsp; <input type="button" class="button" value="Close">
			</div>
		</div>
	</div>
</form:form>
<script type="text/javascript">
	 jQuery("#marketValue").blur(function() {
		 var vacantLandArea = jQuery("#vacantLandArea").val();
		 var marketValue =  jQuery("#marketValue").val();
		 //1 square yard = 0.836127 sqr mtrs
		 var capitalValue = vacantLandArea * marketValue * 0.836127;
		 jQuery("#currentCapitalValue").val(roundoff(capitalValue));
	 });
</script>


