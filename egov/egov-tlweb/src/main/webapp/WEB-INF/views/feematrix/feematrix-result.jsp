<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2016>  eGovernments Foundation
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
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div class="row">
	<div class="col-sm-12">
		<br> <input type="hidden" name="feeMatrix"
			value="${feeMatrix.id}" />
		<table class="table table-bordered fromto" id="result">
			<thead>
				<th><spring:message code="lbl.uomfrom" /></th>
				<th><spring:message code="lbl.uomto" /></th>
				<th><spring:message code="lbl.amount" /></th>
				<th></th>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty feeMatrix.getFeeMatrixDetail()}">
						<c:forEach items="${feeMatrix.feeMatrixDetail}"
							var="feeMatrixDetail" varStatus="vs">
							<tr>
								<td><input type="hidden" name="feeMatrixDetail[${vs.index}]" class="detailId" value="${feeMatrixDetail.id}" /> 
								    <input type="text" name="feeMatrixDetail[${vs.index}].uomFrom"	value="${feeMatrixDetail.uomFrom}"
									class="form-control fromRange text-right patternvalidation fromvalue"
									pattern="-?\d*" data-pattern="numerichyphen" data-fromto="from"
									maxlength="8" readonly="readonly" required="required" />
								</td>
								<td><input type="text" name="feeMatrixDetail[${vs.index}].uomTo" value="${feeMatrixDetail.uomTo}"
									class="form-control text-right patternvalidation tovalue"
									pattern="-?\d*" data-pattern="numerichyphen" data-fromto="to"
									maxlength="8" required="required" />
								</td>
								<td><input type="text" name="feeMatrixDetail[${vs.index}].amount" value="${feeMatrixDetail.amount}"
									class="form-control text-right patternvalidation" data-pattern="number" maxlength="8" required="required" />
							   </td>
								<td><span class="add-padding"><i class="fa fa-trash delete-row" aria-hidden="true"></i></span></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td><input type="hidden" name="feeMatrixDetail[0].id" class="detailId" /> <input type="text"
								name="feeMatrixDetail[0].uomFrom" value="0"
								class="form-control text-right patternvalidation fromvalue"
								pattern="-?\d*" data-pattern="numerichyphen" data-fromto="from"
								required="required" readonly="readonly"/>
							</td>
							<td><input type="text" name="feeMatrixDetail[0].uomTo"
								class="form-control text-right patternvalidation tovalue"
								pattern="-?\d*" data-pattern="numerichyphen" data-fromto="to"
								required="required" />
							</td>
							<td><input type="text" name="feeMatrixDetail[0].amount"
								class="form-control text-right patternvalidation"
								data-pattern="number" required="required" />
						   </td>
							<td><span class="add-padding"><i class="fa fa-trash delete-row" aria-hidden="true"></i></span></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
	<div class="col-sm-12 text-center">
		<button type="button" id="add-row" class="btn btn-primary"><spring:message code="lbl.add" /></button>
		<button type="button" id="save" class="btn btn-primary">Save</button>
	</div>
</div>
<script	src="<cdn:url  value='/resources/js/app/license-fee-matrix.js?rnd=${app_release_no}'/>"></script>
<script	src="<cdn:url  value='/resources/js/app/value-range-checker.js?rnd=${app_release_no}'/>"></script>