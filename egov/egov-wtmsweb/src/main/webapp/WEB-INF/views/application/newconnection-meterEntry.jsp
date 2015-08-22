<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">

			<form:form role="form" method="post"
				modelAttribute="waterConnectionDetails"
				id="editmeterWaterConnectionform"
				cssClass="form-horizontal form-groups-bordered">
				<div class="page-container" id="page-container">
					<form:hidden id="mode" path="" name="mode" value="${mode}" />
					<form:hidden id="meterReadingpriviousObj" path=""
						value="${meterReadingpriviousObj}" />
					<form:hidden id="meterReadingCurrentObj" path=""
						value="${meterReadingCurrentObj}" />

					<input type="hidden" id="currentInstallmentExist"
						name="currentInstallmentExist" value="${currentInstallmentExist}" />
					<input type="hidden" id="consumerCode" name="consumerCode"
						value="${consumerCode}" />
					<form:hidden path="id" />
					<div class="panel-heading">
						<div class="panel-title text-center no-float">
							<strong>${message}</strong>
						</div>
					</div>

					<div class="panel-heading">
						<div class="panel-title">
							<spring:message code="lbl.basicdetails" />
						</div>
					</div>
					<jsp:include page="commonappdetails-view.jsp" />
					<jsp:include page="connectiondetails-view.jsp" />

					<div class="panel-heading">
						<div class="panel-title">
							<spring:message code="lbl.meterbasicdetails" />
						</div>
						<c:if test="${!currentInstallmentExist}">
							<table id="meterDetailprevioussid">
								<tr>

									<td class="col-sm-3 table-div-column"><spring:message
											code="lbl.meterread.previous" /></td>
									<td class="col-sm-3 add-margin"><c:if
											test="${meterReadingpriviousObj!=null}">
											<input class="form-control" type="" id="previousreading"
												name="previousreading"
												value="${meterReadingpriviousObj.currentReading}" min="3"
												maxlength="50" readonly="readonly" required="required" />
										</c:if></td>

									<td><c:if
											test="${meterReadingpriviousObj.currentReadingDate!=null}">
											<fmt:formatDate
												value="${meterReadingpriviousObj.currentReadingDate}"
												var="historyDate" pattern="dd/MM/yyyy" />
											<input type="hidden" id="previousreadingDate"
												class="form-control" data-date-end-date="0d"
												name="previousreadingDate" value="${historyDate}" />
										</c:if></td>
								</tr>
							</table>



							<div class="form-group">
								<table id="meterDetailsid">
									<tr>
										<td class="col-sm-3 table-div-column"><spring:message
												code="lbl.meterread.currentReading" /> (<span
											class="mandatory"></span> )</td>
										<td class="col-sm-3 table-div-column"><spring:message
												code="lbl.meterread.currentReadingdate" />(<span
											class="mandatory"></span> )</td>
									</tr>
									<tr>
										<td class="col-sm-3 add-margin"><form:input
												class="form-control" id="metercurrentReading"
												name="metercurrentReading"
												path="${meterReadingCurrentObj.currentReading}" min="3"
												maxlength="50" required="required" /> <form:errors path=""
												cssClass="add-margin error-msg" /></td>
										<td class="col-sm-3 add-margin"><form:input
												class="form-control datepicker today"
												data-date-end-date="0d" id="metercurrentReadingDate"
												name="metercurrentReadingDate"
												path="${meterReadingCurrentObj.currentReadingDate}"
												required="required" /> <form:errors path=""
												cssClass="add-margin error-msg" /></td>
										<%-- <form:hidden id="meterConnectionid" path="" value="${meterConnection.id}" />  --%>
									</tr>
								</table>

							</div>
						</c:if>
					</div>
					<div class="row">
						<div class="text-center">
							<button type="submit" class="btn btn-primary" id="submitButtonId"
								onclick="return valiateReading();">
								<spring:message code="lbl.submit" />
							</button>

							<c:if test="${currentInstallmentExist}">
								<button type="submit" class="btn btn-primary"
									onclick="return getUrlToPring()">Print Demand Notice</button>
							</c:if>
							<a href="javascript:void(0);" class="btn btn-primary"
								onclick="self.close()"> <spring:message code='lbl.close' />
							</a>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
</div>
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>

<script>
	var currentInstallmentExist = $('#currentInstallmentExist').val();
	if (currentInstallmentExist) {
		$('#submitButtonId').hide();
	}
	function getUrlToPring() {
		var consumerCode = $('#consumerCode').val();
		var url = '/wtms/application/meterdemandnotice?pathVar=' + consumerCode;
		$('#editmeterWaterConnectionform').attr('method', 'get');
		$('#editmeterWaterConnectionform').attr('action', url);
		window.location = url;
	}
	function valiateReading() {
		var previousReading = $('#previousreading').val();
		var currentReading = $('#metercurrentReading').val();
		var currentMeterDate = $('#metercurrentReadingDate').val();
		var previousMeterDate = $('#previousreadingDate').val();
		if ($('#metercurrentReading').val() == '') {
			alert('Current Meter Reading is required');
			return false;
		}
		if ((currentReading - previousReading) < 0) {
			alert('Current Meter Reading should not be less than Previous Meter Reading');
			$('#metercurrentReading').val('');
			return false;
		}
		document.forms[0].submit;
		return true;
		/* if (currentMeterDate != undefined && previousMeterDate != undefined) {
			if (!validateDateRange(previousMeterDate, currentMeterDate)) {
				alert("Entered Metered Date allready present in System");
				$('#metercurrentReadingDate').val('');
				return false;
			}
		} else {
			document.forms[0].submit;
			return true;
		} */
	}

	function validateDateRange(fromDate, toDate) {
		if (fromDate != "" && toDate != "") {
			var stsplit = fromDate.split("/");
			var ensplit = toDate.split("/");

			startDate = Date.parse(stsplit[1] + "/" + stsplit[0] + "/"
					+ stsplit[2]);
			endDate = Date.parse(ensplit[1] + "/" + ensplit[0] + "/"
					+ ensplit[2]);

			// Check the date range, 86400000 is the number of milliseconds in one day
			var difference = (endDate - startDate) / (86400000 * 7);
			if (difference = 0) {
				return false;
			} else {
				return true;
			}
		}
		return true;
	}
</script>
