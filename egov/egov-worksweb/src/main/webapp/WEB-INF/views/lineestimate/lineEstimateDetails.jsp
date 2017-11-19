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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title"><spring:message code="lbl.workdetails" /></div>
	</div>
	<input type="hidden" value="${lineEstimate.lineEstimateDetails.size() }" id="detailsSize" />
	<div class="panel-body">
		<table class="table table-bordered" id="tblestimate">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno"/></th>
					<th><spring:message code="lbl.nameofwork"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.estimatedamount"/><span class="mandatory"></span></th>
					<c:if test="${workdetailsadd}">
						<th><spring:message code="lbl.action"/></th>
					</c:if>
				</tr>
			</thead>
			<tbody id="lineEstimateDetailsTbl">
				<c:choose>
					<c:when test="${lineEstimate.tempLineEstimateDetails.size() == 0}">
						<tr id="estimateRow">
							<td>
								<span class="spansno">1</span>
								<form:hidden path="tempLineEstimateDetails[0].id" name="lineEstimateDetails[0].id" value="${lineEstimateDetails[0].id}" class="form-control table-input hidden-input"/>
							</td>
							<td>
								<form:textarea path="tempLineEstimateDetails[0].nameOfWork" name="lineEstimateDetails[0].nameOfWork" value="${lineEstimateDetails[0].nameOfWork}" data-errormsg="Name of the work is mandatory!" data-idx="0" data-optional="0" class="form-control table-input" maxlength="1024" required="required"/>
								<form:errors path="tempLineEstimateDetails[0].nameOfWork" cssClass="add-margin error-msg" />
							</td>
							<td>
								<form:input path="tempLineEstimateDetails[0].estimateAmount" name="lineEstimateDetails[0].estimateAmount" value="${lineEstimateDetails[0].estimateAmount}" data-errormsg="Estimated amount is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right estimateAmount" onkeyup="calculateEstimatedAmountTotal();" onblur="calculateEstimatedAmountTotal();" required="required"/>
								<form:errors path="tempLineEstimateDetails[0].estimateAmount" cssClass="add-margin error-msg" />
							</td>
							<c:if test="${workdetailsadd}">
								<td> <span class="add-padding" onclick="deleteLineEstimate(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
							</c:if>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${lineEstimate.tempLineEstimateDetails}" var="lineEstimateDtls" varStatus="item">
							<tr id="estimateRow">
								<td> 
									<span class="spansno"><c:out value="${item.index + 1}" /></span>
									<form:hidden path="tempLineEstimateDetails[${item.index}].id" name="lineEstimateDetails[${item.index}].id" value="${lineEstimateDtls.id}" class="form-control table-input hidden-input"/>
								</td>
								<td>
									<form:textarea path="tempLineEstimateDetails[${item.index}].nameOfWork" name="lineEstimateDetails[${item.index}].nameOfWork" value="${lineEstimateDtls.nameOfWork}" data-errormsg="Name of the work is mandatory!" data-idx="0" data-optional="0" class="form-control table-input" maxlength="1024" required="required"/>
									<form:errors path="tempLineEstimateDetails[${item.index}].nameOfWork" cssClass="add-margin error-msg" />
								</td>
								<td>
									<form:input path="tempLineEstimateDetails[${item.index}].estimateAmount" name="lineEstimateDetails[${item.index}].estimateAmount" value="${lineEstimateDtls.estimateAmount}" data-errormsg="Estimated amount is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right estimateAmount" onkeyup="calculateEstimatedAmountTotal();"  required="required"/>
									<form:errors path="tempLineEstimateDetails[${item.index}].estimateAmount" cssClass="add-margin error-msg" />
								</td>
								<c:if test="${workdetailsadd}">
									<td> 
										<span class="add-padding" onclick="deleteLineEstimate(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> 
									</td>
								</c:if>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<c:set var="total" value="${0}" scope="session"/>
				<c:if test="${lineEstimate.getTempLineEstimateDetails() != null}">
					<c:forEach items="${lineEstimate.getTempLineEstimateDetails()}" var="lineEstimateDtls">
						<c:set var="total" value="${total + lineEstimateDtls.estimateAmount}"/>
					</c:forEach>
				</c:if>
				<tr>
					<td colspan="2" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"> <span id="estimateTotal"><c:out value="${total}"/></span> </td>
					<c:if test="${workdetailsadd}">
						<td></td>
					</c:if>
				</tr>
			</tfoot>
		</table>
		<div id="documentDetails">
		</div>
		<div class="col-sm-12 text-center">
			<c:if test="${workdetailsadd}">
				<button id="addRowBtn" type="button" class="btn btn-primary" onclick="addLineEstimate()"><spring:message code="lbl.addrow" /></button>
			</c:if>
		</div>
	</div>
</div>
