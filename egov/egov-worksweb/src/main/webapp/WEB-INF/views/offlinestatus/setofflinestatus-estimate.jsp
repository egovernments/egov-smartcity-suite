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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>

		<input type="hidden" value="${abstractEstimate.offlineStatuses.size() }" id="statusSize" />
		<input type="hidden" value="${offlineStatusSize }" id="offlineStatusSize" />
		<table class="table table-bordered" id="tblsetstatus">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno"/></th>
					<th><spring:message code="lbl.status"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.date"/><span class="mandatory"></span></th>
				</tr>
			</thead>
			<tbody id="setStatusTbl">
					<c:forEach var="i" begin="0" end="5" varStatus="item">
						<tr id="statusRow">
						<td>
							<span class="spansno"><c:out value="${item.index + 1}" /></span>
							<form:hidden path="offlineStatuses[${item.index}].id" name="offlineStatuses[${item.index}].id" value="${offlineStat.id}" id="offlineStatusesId_${item.index}" class="form-control table-input hidden-input"/>
						</td>
						<td>
							<c:forEach var="status" items="${abstractEstimateStatusses}" varStatus="i">
						 		<c:if test="${i.index == item.index}">
						 			<form:hidden path="offlineStatuses[${item.index}].egwStatus.id" value="${status.id }"/>
									${status.description}
						 		</c:if>
						 	</c:forEach>
							<form:errors path="offlineStatuses[${item.index}].egwStatus.id" cssClass="add-margin error-msg" />
						</td>
						<td>
						<c:choose>
						<c:when test="${abstractEstimate.offlineStatuses[item.index].id != null }">
							<form:input path="offlineStatuses[${item.index}].statusDate" id="statusDate_${item.index }" name="offlineStatuses[${item.index}].statusDate" value="" data-errormsg="Status Date is mandatory!" data-idx="0" data-optional="0" class="form-control datepicker statusdate "	maxlength="10" data-date-format="dd/mm/yyyy" data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'"  disabled="true" required="required" />
						</c:when>
						<c:otherwise>
							<form:input path="offlineStatuses[${item.index}].statusDate" id="statusDate_${item.index }" name="offlineStatuses[${item.index}].statusDate" value="" data-errormsg="Status Date is mandatory!" data-idx="0" data-optional="0" class="form-control datepicker statusdate"	maxlength="10" data-date-format="dd/mm/yyyy" data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'"  required="required"/>
						</c:otherwise>
						</c:choose> 
							<form:errors path="offlineStatuses[${item.index}].statusDate" cssClass="add-margin error-msg" />
						</td>
					</tr>
					</c:forEach>
				</tbody>
		</table>
<script src="<cdn:url value='/resources/js/offlinestatus-abstaractestimate.js?rnd=${app_release_no}'/>"></script>