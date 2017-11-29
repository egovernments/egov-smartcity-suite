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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row">
	<div class="col-md-12">
		<form:form id="scheduleOfRateform" method="post" class="form-horizontal form-groups-bordered" modelAttribute="rate" commandName="rate">
				<c:if test="${not empty message}">
                   <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
            </c:if>
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading ">
					<div class="panel-title">
						<strong><spring:message code="title.scheduleofrates"/></strong>
					</div>
				</div>
				<div class="panel-body custom-form"> 										
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label"> <spring:message
								code="lbl.category.name" /><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<input type=hidden id="mode" value="${mode}">
							<form:select name="category" path="category"
								data-first-option="false" id="category" cssClass="form-control" required="true" >
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach items="${hoardingCategories}" var="horcatType">
								 <option  value="${horcatType.id}">${horcatType.name} </option>
								</c:forEach>
								
							</form:select>
						</div>
						
						<label for="field-1" class="col-sm-2 control-label"><spring:message
										code="lbl.subcategory.name" /><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
						<form:select name="subCategory" path="subCategory" data-first-option="false" id="subCategory"
							cssClass="form-control" required="true" >
							<form:option value="">
								<spring:message code="lbl.select"/>
							</form:option>
							<form:options items="${subCategoryList}"  />  
						</form:select>
						</div>
					</div>
					 <div class="form-group">			
						<label for="field-1" class="col-sm-3 control-label"> <spring:message
							code="lbl.unitofmeasure.name" /><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select name="unitofmeasure" path="unitofmeasure"
								data-first-option="false" id="unitofmeasure" cssClass="form-control" required="true">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach items="${unitOfMeasures}" var="uom">
									<option  value="${uom.id}">${uom.description} </option>
								</c:forEach>
							</form:select>
						</div>
						<label for="field-1" class="col-sm-2 control-label"> <spring:message
							code="lbl.rateClass.name" /><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select name="rateClass" path="classtype"
								data-first-option="false" id="rateClass" cssClass="form-control" required="true" >
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<c:forEach items="${ratesClasses}" var="rateClass">
									<option  value="${rateClass.id}">${rateClass.description} </option>
								</c:forEach>
							</form:select>
						</div>
					</div>
					<div class="form-group">	
						<label for="field-1" class="col-sm-3 control-label">
						<spring:message code="lbl.financial.year" /><span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:select path="financialyear" data-first-option="false"	id="financialyear" cssClass="form-control" required="true" >
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<form:options items="${financialYears}" itemLabel="finYearRange" itemValue="id"/>
							</form:select>
						</div>	
					</div>
				</div>	
		    	<div class="row">
					<div class="text-center">
						<button type="submit" id="scheduleOfRateSearch" class="btn btn-primary"><spring:message code="lbl.search"/></button>
					    <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
					</div>
				</div>
				<div id="noscheduleofrateDataFoundDiv" class="row container-msgs"></div>
				<div id="schedleOfrateDiv" class="hidden"></div>
			</div>
		</form:form>
	</div>
</div>
<script src="<cdn:url value='/resources/app/js/scheduleOfRates.js?rnd=${app_release_no}'/>"></script>