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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> 
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<form:hidden path="mbHeader.workOrder.id"  value="${workOrderEstimate.workOrder.id}" /> 
<form:hidden path="mbHeader.id"  value="${contractorBillRegister.mbHeader.id}" /> 
<form:hidden path="mbHeader.egBillregister.id"  value="${contractorBillRegister.id}" />
<input type="hidden" name="activitiesSize" id="activitiesSize" value="${workOrderEstimate.workOrderActivities.size()}"/>
<div class="panel-body">
<c:if test="${workOrderEstimate.workOrderActivities.size() == 0 }">
	<div class="form-group">
		<label class="col-sm-3 control-label text-right"><spring:message code="lbl.mb.referencenumber" /><span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin">
			<form:input class="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash" id="mbRefNo" path="mbHeader.mbRefNo" maxlength="32" required="required" />
			<form:errors path="mbHeader.mbRefNo" cssClass="add-margin error-msg" />		
		</div>
		<label class="col-sm-2 control-label text-right"><spring:message code="lbl.mb.pagenumber" /><span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin">
				<div class="col-sm-6">
					<form:input class="form-control patternvalidation" data-pattern="number" id="fromPageNo" path="mbHeader.fromPageNo" maxlength="4" required="required" placeholder="From" />
					<form:errors path="mbHeader.fromPageNo" cssClass="add-margin error-msg" />	
				</div>
				<div class="col-sm-6">
					<form:input class="form-control patternvalidation" data-pattern="number" id="toPageNo" path="mbHeader.toPageNo" maxlength="4" required="required" placeholder="To" />
					<form:errors path="mbHeader.toPageNo" cssClass="add-margin error-msg" /> 
				</div>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-3 control-label text-right"><spring:message code="lbl.mb.date" /><span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin">
			<form:input id="mbDate" path="mbHeader.mbDate" class="form-control datepicker" data-date-end-date="0d" required="required"  />
			<form:errors path="mbHeader.mbDate" cssClass="add-margin error-msg" />
			<input type="hidden" id="errorMBDate" value="<spring:message code='error.validate.mbdate.lessthan.loadate' />" />
		</div>
		<label class="col-sm-2 control-label text-right workcompletion"><spring:message code="lbl.workcompletion.date" /><span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin workcompletion">
			<form:input id="workCompletionDate" path="workOrderEstimate.workCompletionDate" class="form-control datepicker" data-date-end-date="0d" required=""/>
			<form:errors path="workOrderEstimate.workCompletionDate" cssClass="add-margin error-msg" /> 
		</div>
	</div>
</c:if>
<c:if test="${workOrderEstimate.workOrderActivities.size() > 0 }">
    <div class="form-group">
         <label class="col-sm-3 control-label text-right workcompletion"><spring:message code="lbl.workcompletion.date" /><span class="mandatory"></span></label>
	     <div class="col-sm-3 add-margin workcompletion"> 
		 <form:input id="workCompletionDate" path="workOrderEstimate.workCompletionDate" class="form-control datepicker" data-date-end-date="0d" required=""/>
		 <form:errors path="workOrderEstimate.workCompletionDate" cssClass="add-margin error-msg" /> 
		</div>
	</div>
	<c:if test="${workOrderEstimate.assetValues.size() > 0}">
		<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.assetcodeorname" /><span class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:select path="assetDetailsList[0].asset" data-first-option="false" name="assetDetailsList[0].asset" id="assetDetailsList[0].asset" class="form-control" required="required" value = "${asset.id}">
					<c:if test="${assetValues.size() == 1 }">
						<c:forEach var="assetValue" items="${assetValues}">
							<form:option value="${assetValue.asset.id}" selected = "selected"><c:out value="${assetValue.asset.code}" /> - <c:out value="${assetValue.asset.name}" /></form:option>
						</c:forEach>
					</c:if>
					<c:if test="${assetValues.size() > 1 }">
						<form:option value=""> <spring:message code="lbl.select" /> </form:option>
						<c:forEach var="assetValue" items="${assetValues}">
							<c:choose>
								<c:when test="${billAssetValue !=null &&  assetValue.asset.id == billAssetValue.asset.id }">
									<form:option value="${assetValue.asset.id}" selected = "selected" ><c:out value="${assetValue.asset.code}" /> - <c:out value="${assetValue.asset.name}" /></form:option>
								</c:when>
								<c:otherwise>
									<form:option value="${assetValue.asset.id}" ><c:out value="${assetValue.asset.code}" /> - <c:out value="${assetValue.asset.name}" /></form:option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:if>
				</form:select>
				<form:errors path="assetDetailsList[0].asset" cssClass="add-margin error-msg" />	
			</div>
	</div>
	</c:if>
	</br>
	<table class="table table-bordered" id="mbdetails">
		<thead>
			<tr>
				<th><spring:message code="lbl.slNo" /></th>
				<th><spring:message code="lbl.mb.referencenumber" /></th>
				<th><spring:message code="lbl.mb.pagenumber" /></th>
				<th><spring:message code="lbl.mb.date" /></th>
				<th><spring:message code="lbl.mbamount" /></th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${mbHeaders.size() != 0}">
				    <c:set var="SlNo" value="${1}" scope="session" />
					<c:forEach items="${mbHeaders}" var="mbDtls" varStatus="item">
							<tr id="mbdetailsrow">
							    <form:hidden path="mbHeaderIds" id="mbheaderid_${item.index}" value="${mbDtls.id}"/>
								<td><span class="spansno"><c:out value="${SlNo}" /></span></td>
								<td><a id="mbrefno_${item.index}" href='javascript:void(0)' onclick="viewMB('<c:out value="${mbDtls.id}"/>')"><c:out value="${mbDtls.mbRefNo}"/></a></td>
								<td><span id="pageno_${item.index}"><c:out value="${mbDtls.fromPageNo} - ${mbDtls.toPageNo}"></c:out></span></td>
								<td><span id="mbdate_${item.index}"><fmt:formatDate value="${mbDtls.mbDate}" pattern="dd/MM/yyyy" /></span></td>
								<td class="text-right"><span id="mbamount_${item.index}"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${mbDtls.mbAmount}" /></fmt:formatNumber></span></td>
							</tr>
							<c:set value="${SlNo + 1}" var="SlNo" scope="session" />
					</c:forEach>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose> 
		</tbody>
		<tfoot>
			<c:set var="mbtotal" value="${0}" scope="session" />
			<c:if test="${mbHeaders.size() != 0}">
				<c:forEach items="${mbHeaders}" var="mb">
						<c:set var="mbtotal" value="${mbtotal + mb.mbAmount }" />
				</c:forEach>
			</c:if>
			<tr>
			<td colspan="4" class="text-right"><spring:message code="lbl.total" /></td>
			<td class="text-right"><div id="mbTotalAmount"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${mbtotal}" /></fmt:formatNumber></div>
			</td>
			</tr>
		</tfoot>
	</table>
</c:if>
</div>	
