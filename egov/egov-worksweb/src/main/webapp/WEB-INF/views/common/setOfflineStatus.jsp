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
<script src="<c:url value='/resources/js/offlinestatus.js?rnd=${app_release_no}'/>"></script>

		<input type="hidden" value="${workOrder.offlineStatuses.size() }" id="statusSize" />
		<input type="hidden" value="true" id="isOfflineStatusValuesLoading" />
		<table class="table table-bordered" id="tblsetstatus">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno"/></th>
					<th><spring:message code="lbl.status"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.date"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.action"/></th>
				</tr>
			</thead>
			<tbody id="setStatusTbl">
			<c:choose>
				<c:when test="${workOrder.offlineStatuses.size() == 0}">
					<tr id="statusRow">
						<td>
							<span class="spansno">1</span>
							<form:hidden path="offlineStatuses[0].id" name="offlineStatuses[0].id" value="${offlineStatuses.id}" class="form-control table-input hidden-input"/>
						</td>
						<td>
						<c:choose>
						<c:when test="${offlineStatuses.id != null }">
							<form:select path="offlineStatuses[0].egwStatus.id" data-first-option="false" id="offlineStatuses[0].egwStatus" class="form-control offlineStatusValue" onchange="cheackOfflineStatus(this);"  required="required">
							 	<form:option value=""><spring:message code="lbl.select" /></form:option>
							 	<c:forEach var="status" items="${egwStatus}">
							 		<form:option value="${status.id}"><c:out value="${status.description}" /> </form:option>
							 	</c:forEach>
							</form:select>
						</c:when>
						<c:otherwise>
							<form:select path="offlineStatuses[0].egwStatus.id" data-first-option="false" id="offlineStatuses[0].egwStatus" class="form-control offlineStatusValue" onchange="cheackOfflineStatus(this);"  required="required">
							 	<form:option value=""><spring:message code="lbl.select" /></form:option>
							 	<c:forEach var="status" items="${egwStatus}">
							 		<form:option value="${status.id}"><c:out value="${status.description}" /> </form:option>
							 	</c:forEach>
							</form:select>
						</c:otherwise>
						</c:choose>
							<form:errors path="offlineStatuses[0].egwStatus.id" cssClass="add-margin error-msg" />
						</td>
						<td>
							<form:input path="offlineStatuses[0].statusDate" id="offlineStatuses[0].statusDate" data-inputmask="'mask': 'd/m/y'" name="offlineStatuses[0].statusDate" value="" data-errormsg="Status Date is mandatory!" data-idx="0" data-optional="0" class="form-control datepicker statusdate statusdatedisable"	maxlength="10" data-date-format="dd/mm/yyyy" data-date-end-date="0d"  required="required" />
							<form:errors path="offlineStatuses[0].statusDate" cssClass="add-margin error-msg" />
						</td>
						<td>
							<span name="spandelete" data-idx="0" class="add-padding spandelete"  id="spandelete_0" onclick="deleteSetStatus(this);" readonly="true" ><i class="fa fa-trash spandelete" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${workOrder.getOfflineStatuses()}" var="offlineStat" varStatus="item">
						<tr id="statusRow">
						<td>
							<span class="spansno"><c:out value="${item.index + 1}" /></span>
							<form:hidden path="offlineStatuses[${item.index}].id" name="offlineStatuses[${item.index}].id" value="${offlineStat.id}" class="form-control table-input hidden-input"/>
						</td>
						<td>
						<c:choose>
						<c:when test="${offlineStat.id != null }">
							<form:select path="offlineStatuses[${item.index}].egwStatus.id" data-first-option="false" id="offlineStatuses[${item.index}].egwStatus" class="form-control offlineStatusValue" onchange="cheackOfflineStatus(this);"  disabled="true" required="required">
							 	<form:option value=""><spring:message code="lbl.select" /></form:option>
							 	<c:forEach var="status" items="${egwStatus}">
							 		<form:option value="${status.id}"><c:out value="${status.description}" /> </form:option>
							 	</c:forEach>
							</form:select>
						</c:when>
						<c:otherwise>
							<form:select path="offlineStatuses[${item.index}].egwStatus.id" data-first-option="false" id="offlineStatuses[${item.index}].egwStatus" class="form-control offlineStatusValue" onchange="cheackOfflineStatus(this);" required="required">
							 	<form:option value=""><spring:message code="lbl.select" /></form:option>
							 	<c:forEach var="status" items="${egwStatus}">
							 		<form:option value="${status.id}"><c:out value="${status.description}" /> </form:option>
							 	</c:forEach>
							</form:select>
						</c:otherwise>
						</c:choose>
							<form:errors path="offlineStatuses[${item.index}].egwStatus.id" cssClass="add-margin error-msg" />
						</td>
						<td>
						<c:choose>
						<c:when test="${offlineStat.id != null }">
							<form:input path="offlineStatuses[${item.index}].statusDate" id="offlineStatuses[${item.index}].statusDate" name="offlineStatuses[${item.index}].statusDate" value="" data-errormsg="Status Date is mandatory!" data-idx="0" data-optional="0" class="form-control datepicker statusdate "	maxlength="10" data-date-format="dd/mm/yyyy" data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'"  disabled="true" required="required" />
						</c:when>
						<c:otherwise>
							<form:input path="offlineStatuses[${item.index}].statusDate" id="offlineStatuses[${item.index}].statusDate" name="offlineStatuses[${item.index}].statusDate" value="" data-errormsg="Status Date is mandatory!" data-idx="0" data-optional="0" class="form-control datepicker statusdate"	maxlength="10" data-date-format="dd/mm/yyyy" data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'"  required="required"/>
						</c:otherwise>
						</c:choose>
							<form:errors path="offlineStatuses[${item.index}].statusDate" cssClass="add-margin error-msg" />
						</td>
						<td>
							<span style="display: none;" name="spandelete" data-idx="0" class="add-padding spandelete" id="spandelete_${item.index}" onclick="deleteSetStatus(this);" ><i class="fa fa-trash spandelete" id="spandelete" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
						</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
				</tbody>
		</table>
			<div id="offlineStatus">
			</div>
			<div class="col-sm-12 text-center">
				<button id="addRowBtn" type="button" class="btn btn-primary" onclick="addNewStatus()"><spring:message code="lbl.addstatus" /></button>
			</div>