
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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<link rel="stylesheet" href="<cdn:url  value='/resources/global/css/bootstrap/typeahead.css' context='/egi'/>">
<script type="text/javascript"
        src="<cdn:url  value='/resources/js/app/appconfig.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script src="<cdn:url  value='/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}' context='/egi'/>"></script>

<form:form role="form" action="/stms/masters/donationmaster" class="form-horizontal form-groups-bordered"
	modelAttribute="donationMaster" id="donationRatesSearchForm" commandName="donationMaster">
	<c:if test="${not empty message}">
         <div class="alert alert-danger" role="alert" id="activeDateValidateMsg">
         	<spring:message code="${message}"></spring:message>
         	<fmt:formatDate pattern="dd-MM-yyyy" value="${existingActiveDate}"/>
         </div>
    </c:if>
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title">
				<strong><spring:message code="title.create.donation.master"/></strong>			
			</div>
		</div>
		<div class="panel-body custom-form">
			<div class="form-group">
				<label class="col-sm-3 control-label text-right"><spring:message
						code="lbl.propertytype" /> <span class="mandatory"></span>
				</label>
				<div class="col-sm-3 add-margin">
					<form:select path="propertyType" data-first-option="false"
						id="propertyType" cssClass="form-control" required="required">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${propertyTypes}" />
					</form:select>
					<form:errors path="propertyType" cssClass="add-margin error-msg" />
				</div>
				
			    <input type="hidden" id="donationDetailsize" value="${donationMaster.donationDetail.size()}"/>
			    
			    <fmt:formatDate var="formattedEndDate" pattern="dd-MM-yyyy"  value="${endDate}" />
				<input type="hidden" id="effectiveEndDate" value='${formattedEndDate}'/>
				
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.effective.fromdate" /><span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:input path="fromDate" class="form-control datepicker" 
						id="effectiveDate" data-inputmask="'mask': 'd/m/y'"
						required="required" />
					<form:errors path="fromDate" cssClass="add-margin error-msg" />
				</div>
			</div>
			<br/>
			<div class="panel-body">
				<table  class="table table-bordered" role="grid" id="donationMasterTable" style="width:65%; margin:0 auto">
					<thead>
						<tr>
							<th><spring:message code="lbl.noofclosets"/></th>
							<th><spring:message code="lbl.donation.amount"/></th>	
							<th><spring:message code="lbl.actions"/></th>		
						</tr>	
					</thead>
					<tbody><form:hidden path="id" id="id" value="${id}"/> 
						<c:choose>
							<c:when test="${empty donationMaster.donationDetail}">
								<tr>
									<td>
										<form:input type="text" class="form-control patternvalidation donationRatesNoOfClosets" 
										style="text-align: left; font-size: 12px;" data-pattern="number" 
										id="donationDetail[0].noOfClosets" 
									    path="donationDetail[0].noOfClosets" 
										maxlength="8" required="required" />
										<form:errors path="donationDetail[0].noOfClosets" cssClass="add-margin error-msg"/>
									</td>
									<td>
										<input type="text" class="form-control patternvalidation donationRatesAmount" 
										id="donationDetail[0].amount"  
										style="text-align: right; font-size: 12px;" data-pattern="decimalvalue" 
										name="donationDetail[0].amount"  maxlength="8" required="required" />
										<form:errors path="donationDetail[0].amount" cssClass="add-margin error-msg"/>
									</td>
									<td>
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								 <c:forEach var="rates" items="${donationMaster.donationDetail}" varStatus="status" >
									<tr>
										<td>
											<form:input type="text" class="form-control patternvalidation donationRatesNoOfClosets" 
											style="text-align: left; font-size: 12px;" data-pattern="number" 
											id="donationDetail[${status.index}].noOfClosets" 
											value="${rates.noOfClosets}"  path="donationDetail[${status.index}].noOfClosets" 
											maxlength="8" required="required" />
											
											<form:errors path="donationDetail[${status.index}].noOfClosets" cssClass="add-margin error-msg"/>
										</td>
										<td>
											<input type="text" class="form-control patternvalidation donationRatesAmount" 
											id="donationDetail[${status.index}].amount"  
											style="text-align: right; font-size: 12px;" value="${rates.amount}" data-pattern="decimalvalue" 
											name="donationDetail[${status.index}].amount"  maxlength="8" required="required" />
											<form:errors path="donationDetail[${status.index}].amount" cssClass="add-margin error-msg" />
										</td>
										<td>
											<button type="button" onclick="deleteRow(this)" id="Add" 
											class="btn btn-primary display-hide delete-button"><spring:message code="lbl.swtax.deleteRow" /></button>
											
										</td>
									</tr>
									<script type="text/javascript">
										$( "#donationMasterTable tr:last .delete-button").show();
									</script>
				 				</c:forEach>
				 			</c:otherwise>
				 		</c:choose>
					</tbody>
				</table>
			</div>
		</div>
		<div class="text-center">
			<button type="button" id="btn-addRow" class="btn btn-primary btn-addRow"><spring:message code="lbl.swtax.addRow"></spring:message></button>
		</div>
	</div>
	<div class="form-group text-center">
			<input type="submit" value="Submit" class="btn btn-primary" id="submitformvalue" />
			<a onclick="self.close()" class="btn btn-default"
				href="javascript:void(0)"><spring:message code="lbl.close" /></a>
		</div>
</form:form>

<script src="<cdn:url  value='/resources/javascript/helper.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/js/masters/donationMaster.js?rnd=${app_release_no}'/>"></script>

