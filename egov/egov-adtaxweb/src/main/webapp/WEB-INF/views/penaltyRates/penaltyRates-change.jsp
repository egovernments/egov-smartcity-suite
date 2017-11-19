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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<script type="text/javascript" src="<cdn:url value='/resources/js/app/appconfig.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}' context='/egi'/>"></script>
<div class="row">
	<div class="col-md-12">
		<form:form id="penaltyRatesChangeForm" method="post" class="form-horizontal form-groups-bordered" modelAttribute="hoardingPenaltyRates" commandName="hoardingPenaltyRates" >
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading ">
					<div class="panel-title">
						<strong><spring:message code="title.adtax.penaltyRateChange" /></strong>
					</div>
				</div>
				<div class="panel-body custom-form">
					<table cellpadding="10" class="table table-bordered datatable dt-responsive multiheadertbl" role="grid" id="penaltyRatesTable" sortable="sortable">
						<thead>
							<th><spring:message code="lbl.penaltyRates.rangeFrom"/></th>
							<th><spring:message code="lbl.penaltyRates.rangeTo"/></th>
							<th><spring:message code="lbl.penaltyRates.percentage"/></th>
							<th><spring:message code="lbl.scheduleorrate.action"/></th>
						</thead>
						<tbody>
							<c:forEach var="penalty" items="${hoardingPenaltyRates.advtPenaltyRatesList}" varStatus="status" >
								<tr>
									<form:hidden path="id" id="id" value="${penalty.id}" />
									<td>
										<form:hidden path="advtPenaltyRatesList[${status.index}].id" id="id" value="${penalty.id}" />
										<input type="text" class="form-control patternvalidation range-from" 
										id="advertisementPenaltyRatesRangeFrom${status.index}" style="text-align: center;  font-size: 12px;"
										value='<fmt:formatNumber value="${penalty.rangeFrom}"  />'
										maxlength="8" data-pattern="numerichyphen"
										name="advtPenaltyRatesList[${status.index}].rangeFrom"
										autocomplete="off" required="required" readonly="readonly" />
									</td>
									<td>	 
										<input type="text" id="advertisementPenaltyRatesRangeTo${status.index}" class="form-control patternvalidation range-to" style="text-align: center; font-size: 12px;" 
										value='<fmt:formatNumber type="number" value="${penalty.rangeTo}" groupingUsed="false" maxFractionDigits="0" />'
										maxlength="8" data-pattern="numerichyphen" name="advtPenaltyRatesList[${status.index}].rangeTo" onchange="return validateRangeToValue(this);"
										autocomplete="off" required="required" />
									</td>	
									<td>	 
										<input type="text" class="form-control patternvalidation percentage" 
										id="advertisementPenaltyRatesPercentage${status.index}" style="text-align: center; font-size: 12px;"
										value='<fmt:formatNumber type="number" value="${penalty.percentage}" groupingUsed="false" maxFractionDigits="5" />'
										maxlength="5" data-pattern="decimalvalue" 
										name="advtPenaltyRatesList[${status.index}].percentage"
										 autocomplete="off" required="required"  />
									</td>	 
									<td>
										<button type="button" onclick="deleteRow(this)" id="Add" 
										class="btn btn-primary display-hide delete-button"><spring:message code="lbl.adtax.deleteRow" /></button>
									</td>	
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<script>
								$( "#penaltyRatesTable tr:last .delete-button").show();
								$( "#penaltyRatesTable tr:last .range-to").prop("readonly", false);
					</script>
					<div class="form-group">
						<div class="text-center">
							<button type="button" id="btn-addRow" class="btn btn-primary btn-addRow"><spring:message code="lbl.adtax.addRow"></spring:message></button>
							<button type="submit" class="btn btn-primary penaltyRatesSaveButton" id="penaltyRatesSaveButton" onclick="return validateValue();">
								Save
							</button>
							<a href="javascript:void(0);" onclick="self.close()" class="btn btn-default">
							<spring:message code="lbl.close"/></a>
						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>
<script src="<cdn:url value='/resources/app/js/penaltyRates.js?rnd=${app_release_no}'/>"></script>