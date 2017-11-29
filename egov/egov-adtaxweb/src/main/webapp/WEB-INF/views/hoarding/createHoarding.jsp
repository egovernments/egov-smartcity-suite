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
		
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.application.no"/>
								</label>
								<div class="col-sm-3 add-margin">
								<form:hidden path="advertisement.legacy" id="legacy" value="${advertisement.legacy}" />
								<form:hidden path="advertisement.status" id="advStatus" value="${advertisement.status}" />
								<%-- <form:hidden path="status" id="status" value="${status}" /> --%>
								
								<form:hidden path="advertisement.penaltyCalculationDate" id="penaltyCalculationDate" value="${advertisement.penaltyCalculationDate}" />
								
									<form:input type="text"  cssClass="form-control patternvalidation" 
                        	      data-pattern="alphanumerichyphenbackslash" path="applicationNumber" maxlength="25" id="applicationNumber" />
                               		<form:errors path="applicationNumber" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.application.date"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control datepicker" path="applicationDate" id="applicationDate" required="required"/>
                               		<form:errors path="applicationDate" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<%-- <label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.permission.no"/>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control patternvalidation" 
                        	      data-pattern="alphanumerichyphenbackslash" maxlength="25"  path="permissionNumber" id="permissionNumber" />
                               		<form:errors path="permissionNumber" cssClass="error-msg" />
								</div> --%>
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.advertisement.number"/>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control patternvalidation" 
                        	      data-pattern="username" maxlength="25"  path="advertisement.advertisementNumber" id="advertisementnumber" />
                               		<form:errors path="advertisement.advertisementNumber" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.type"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin dynamic-span capitalize">
									<form:radiobuttons path="advertisement.type" element="span"/>
									<form:errors path="advertisement.type" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.agency"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<input type="text" id="agencyTypeAhead" class="form-control typeahead" autocomplete="off" required="required" value="${advertisementPermitDetail.agency.name}">
									<form:hidden path="agency" id="agencyId" value="${advertisementPermitDetail.agency.id}" />
									<form:errors path="agency" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.hoarding.adv"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:textarea path="advertiser" cols="5" rows="2" class="form-control patternvalidation"  data-pattern="alphanumericwithspace" required="required" minlength="5" maxlength="125"/>
                               		<form:errors path="advertiser" cssClass="error-msg" /> 
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.ad.particulars"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:textarea path="advertisementParticular" cols="5" rows="2" class="form-control patternvalidation" data-pattern="alphanumericwithspace"  required="required" minlength="5" maxlength="256"/>
									<form:errors path="advertisementParticular" cssClass="error-msg" />
								</div>
									<label class="col-sm-2 control-label text-right">
									<spring:message code="lbl.hoarding.prop.type"/>
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
							<div class="form-group">
								<%-- <label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.status"/>
									<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:select path="status" id="status" cssClass="form-control" cssErrorClass="form-control error" required="required">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${status}"/>
									</form:select>
									<form:errors path="status" cssClass="error-msg" />
								</div> --%>
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.owner.detail"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:textarea path="ownerDetail" cols="5" rows="2" class="form-control patternvalidation" data-pattern="alphanumericwithspace"  required="required" minlength="5" maxlength="125"/>
                               		<form:errors path="ownerDetail" cssClass="error-msg" />
								</div>
							</div>
					
								<jsp:include page="hoarding-location.jsp"></jsp:include>
						
							<div class="panel-heading custom_form_panel_heading">
								<div class="panel-title">
								<spring:message code="lbl.hoarding.struct"/>
								</div>
							</div>
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
								<spring:message code="lbl.hoarding.measurement"/>
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
							<%-- 	<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.breadth"/>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" class="form-control patternvalidation" data-pattern="decimalvalue"  maxlength="15"  path="breadth" id="breadth"/>
                               		<form:errors path="breadth" cssClass="error-msg" />
								</div>
						 --%>		<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.total.height"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" class="form-control patternvalidation" data-pattern="decimalvalue"  maxlength="15"  path="totalHeight" id="totalHeight" required="required"/>
                               		<form:errors path="totalHeight" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.class"/>
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
								<spring:message code="lbl.hoarding.duration"/> 	<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:select path="advertisementDuration" id="advertisementDuration" cssClass="form-control" cssErrorClass="form-control error" required="required">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${advertisementDuration}"/>
									</form:select>
									<form:errors path="advertisementDuration" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.hoarding.electricityservicenumber"/>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" class="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash"  maxlength="25"  path="advertisement.electricityServiceNumber" id="electricityServiceNumber"/>
                               		<form:errors path="advertisement.electricityServiceNumber" cssClass="error-msg" />
                           		</div>
							</div>
							<div class="panel-heading custom_form_panel_heading">
								<div class="panel-title">
								<spring:message code="lbl.hoarding.tax.feeDetails"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.tax"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" class="form-control patternvalidation" data-pattern="decimalvalue"  maxlength="15"  path="taxAmount" id="taxAmount" required="required"/>
                               		<form:errors path="taxAmount" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.hoarding.enc.fee"/>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" class="form-control patternvalidation" data-pattern="decimalvalue"  maxlength="15"  path="encroachmentFee" id="encroachmentFee"/>
                               		<form:errors path="encroachmentFee" cssClass="error-msg" />
								</div>
							</div>
					</div>
					<div class="tab-pane fade" id="hoardingattachments">
						<c:choose>
							<c:when test="${not empty hoardingDocumentTypes}">
							<form:hidden path="advertisement.latitude" id="latitude"/> 
							<form:hidden path="advertisement.longitude" id="longitude" /> 
							<div class="col-sm-12 view-content header-color hidden-xs">
								<div class="col-sm-1 table-div-column"><spring:message code="lbl.srl.no"/></div>
								<div class="col-sm-5 table-div-column"><spring:message code="lbl.documentname"/></div>
								<div class="col-sm-3 table-div-column"><spring:message code="lbl.enclosed"/></div>
								<div class="col-sm-3 table-div-column"><spring:message code="lbl.attachdocument"/></div>
							</div>
							
							<c:forEach var="docs" items="${hoardingDocumentTypes}" varStatus="status">	
								<div class="form-group">
									<div class="col-sm-1 text-center">${status.index+1}</div>
									<div class="col-sm-5 text-center">${docs.mandatory ? "<span
									class='mandatory'></span>" : ""}${docs.name}
									</div>
									<div class="col-sm-3 text-center">
										<input type="checkbox" ${advertisement.documents[status.index].enclosed ? "checked='checked'" : ""} 
										name="advertisement.documents[${status.index}].enclosed" ${docs.mandatory ? "required='required'" : ""}>
									</div>
									<div class="col-sm-3 text-center">
										<input type="file" name="advertisement.documents[${status.index}].attachments" class="form-control" >
										<form:errors path="advertisement.documents[${status.index}].attachments" cssClass="add-margin error-msg" />
										<form:hidden path="advertisement.documents[${status.index}].doctype" value="${docs.id}" /> 
									</div>
								</div>
							</c:forEach> 
							</c:when>
						</c:choose>
					</div>
			
