<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

	<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%> 
	<div class="form-group">
								<label class="col-sm-3 control-label text-right">
							<input type="hidden" name="isEmployee" id="isEmployee" value="${isEmployee}" /> 
							<input type="hidden" name="applicationSource" id="applicationSource" value="${applicationSource}" />
								
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
							
							<div  class="form-group">
							<form:hidden path="advertisement.legacy" id="legacy" value="${advertisementPermitDetail.advertisement.legacy}" />
								<form:hidden path="advertisement.status" id="advStatus" value="${advertisement.status}" />
								<c:choose>
								<c:when test="${applicationSource !='online' || advertisementPermitDetail.advertisement.legacy == 'true'}">
								<label class="col-sm-3 control-label text-right ">
								<spring:message code="lbl.advertisement.application.no"/>
								</label>
								<div class="col-sm-3 add-margin">
								
								 
								<%-- 
								<form:hidden path="status" id="status" value="${status.id}" /> 
								 --%>
										<form:input type="text"  cssClass="form-control patternvalidation" 
                        	      data-pattern="alphanumerichyphenbackslash" path="applicationNumber" maxlength="25" id="applicationNumber" />
                               		<form:errors path="applicationNumber" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">								
								<spring:message code="lbl.application.date"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
								<form:input  path="applicationDate"  type="text"
                                class="form-control datepicker"   title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}" data-date-end-date="0d" 
                                id="applicationDate" data-inputmask="'mask': 'd/m/y'"  required="required"/>
                               		<form:errors path="applicationDate" cssClass="error-msg" />
								</div>
								</c:when>
								<c:otherwise>
								<label class="col-sm-3 control-label text-left">								
								<spring:message code="lbl.application.date"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
								<form:input  path="applicationDate"  type="text"
                                class="form-control datepicker"   title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}" data-date-end-date="0d" 
                                id="applicationDate" data-inputmask="'mask': 'd/m/y'"  required="required"/>
                               		<form:errors path="applicationDate" cssClass="error-msg" />
                               		</div>
                               		</c:otherwise>
                               		</c:choose>
								
							</div>
							
							
							<div class="form-group">
							<c:choose>
								<c:when test="${advertisementPermitDetail.advertisement.legacy == 'true'}">
									<label class="col-sm-3 control-label text-right">
									<spring:message code="lbl.advertisement.permission.no"/>
										</label>
									<div class="col-sm-3 add-margin">
										<form:input type="text" cssClass="form-control patternvalidation" 
	                        	      data-pattern="alphanumerichyphenbackslash" maxlength="25"  path="permissionNumber" id="permissionNumber" />
	                               		<form:errors path="permissionNumber" cssClass="error-msg" />
									</div>
									<label class="col-sm-2 control-label text-right">
									<spring:message code="lbl.advertisement.no"/></label>
									<div class="col-sm-3 add-margin">
										<form:input type="text" cssClass="form-control patternvalidation" 
	                        	      data-pattern="alphanumerichyphenbackslash" maxlength="25"  path="advertisement.advertisementNumber" id="advertisementnumber" />
	                               		<form:errors path="advertisement.advertisementNumber" cssClass="error-msg" />
									</div>
								</c:when>
						</c:choose>
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
								<form:input  path="permissionstartdate"  
                                class="form-control datepicker" title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}" 
                                id="permissionstartdate" data-inputmask="'mask': 'd/m/y'" required="required" />
                               		<form:errors path="permissionstartdate" cssClass="error-msg" />
                             	</div>
						     	<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.advertisement.permit.todate"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
                                <form:input  path="permissionenddate"  
                                class="form-control datepicker" pattern="\d{1,2}/\d{1,2}/\d{4}"
                                id="permissionenddate" data-inputmask="'mask': 'd/m/y'" required="required" />                               		
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
							<jsp:include page="hoarding-location.jsp"></jsp:include>
						
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
									<form:input type="text" class="form-control" maxlength="15" path="measurement" id="measurement" required="required"/>
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
							
						<c:choose>
						
						<c:when test="${isEmployee == 'true' || advertisementPermitDetail.advertisement.legacy == 'true'}">
							
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
									<form:input type="text" class="form-control patternvalidation" data-pattern="decimalvalue"  maxlength="15"  path="taxAmount" id="taxAmount" />
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
									</c:when>
								<c:otherwise>
								<div class="form-group">
								
								<form:hidden path="taxAmount"  value="0"/>
								</div>
								</c:otherwise>
							</c:choose>
							
								
								
								
								
							
						<c:if test="${advertisementPermitDetail.advertisement.legacy == 'true'}">
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.advertisement.currentYearTax.paid"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:radiobutton path="advertisement.taxPaidForCurrentYear" value="true"/>Yes 
									<form:radiobutton path="advertisement.taxPaidForCurrentYear" value="false"/>No 
								</div>
									
								
								
							</div>
							<div class="panel-heading custom_form_panel_heading">
								<div class="panel-title">
								<spring:message code="lbl.advertisement.arrerasTax.feeDetails"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.pendingtax"/> 
							
								<fmt:formatDate value="${previousFinancialYear.endingDate}" pattern="dd-MM-yyyy" var="previousFinancialYrEndDte"/>
								<strong><c:out value="${previousFinancialYrEndDte}" /></strong>
									<span class="mandatory"></span>
								</label>
								
								<div class="col-sm-3 add-margin">
									<form:input type="text" class="form-control patternvalidation" data-pattern="decimalvalue"  maxlength="15"  path="advertisement.pendingTax" id="pendingTax" required="required"/>
                               		<form:errors path="advertisement.pendingTax" cssClass="error-msg" />
								</div>
							</div>
						</c:if>
					
					
						