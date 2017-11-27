<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

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

<div class="row">
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title">Create Preamble</div>
		</div>
		<div class="panel-body">
			<div class="form-group">
				<c:if test="${!autoPreambleNoGenEnabled && currentState=='NEW'}">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.preamble.number" /> <span class="mandatory"></span></label>
					<div class="col-sm-3 add-margin">
						<form:input path="preambleNumber" required="required"
							id="preambleNumber"
							class="form-control text-left patternvalidation" />
						<form:errors path="preambleNumber" cssClass="error-msg" />
					</div>
				</c:if>

				<c:if
					test="${councilPreamble.preambleNumber!= null && !''.equalsIgnoreCase(councilPreamble.preambleNumber)}">
					<label class="col-sm-2 control-label text-right"> <spring:message
							code="lbl.preamble.number" />
					</label>
					<div class="col-sm-3 add-margin">
						${councilPreamble.preambleNumber}</div>
				</c:if>

				<input type="hidden" name="councilPreamble"
					value="${councilPreamble.id}" />
				<form:hidden path="type" id="type" value="${councilPreamble.type}" />
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.department" /> <span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="department" id="department"
						cssClass="form-control" cssErrorClass="form-control error"
						required="required">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${departments}" itemValue="id"
							itemLabel="name" />
					</form:select>
					<form:errors path="department" cssClass="error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.amount" /></label>
				<div class="col-sm-3 add-margin">
					<form:input path="sanctionAmount"
						class="form-control text-left patternvalidation"
						data-pattern="number" />
					<form:errors path="sanctionAmount" cssClass="error-msg" />
				</div>
				<form:hidden path="" id="URL" name="URL" value="${URL}" />
				<a
					onclick="window.open('${URL}','name','width=800, height=600,scrollbars=yes')">Click
					here to Check Budget</a>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.gistofpreamble" /><span class="mandatory"></span></label>
				<div class="col-sm-8 add-margin">
					<form:textarea path="gistOfPreamble" id="gistOfPreamble"
						data-role="none" rows="10"
						class="form-control text-left patternvalidation" maxlength="10000"
						required="required" />
					<form:errors path="gistOfPreamble" cssClass="error-msg" />
				</div>

			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.upload" /><span class="mandatory"></span></label>
				<%-- <div class="col-sm-3 add-margin">
							<input type="file" id="attachments" name="attachments"
								data-id="1" class="filechange inline btn" />
							<form:errors path="attachments" cssClass="error-msg" />
				</div> --%>
				<div class="col-sm-3 add-margin">
					<c:choose>
						<c:when test="${councilPreamble.filestoreid != null}">

							<form:input path="attachments" type="file" id="attachments"
								name="attachments" data-id="1"
								class="filechange inline btn upload-file" />
							<form:errors path="attachments" cssClass="error-msg" />

							<form:hidden path="filestoreid.id"
								value="${councilPreamble.filestoreid.id}" />
							<form:hidden path="filestoreid.fileStoreId"
								value="${councilPreamble.filestoreid.fileStoreId}" />

							<a
								href="/council/councilmember/downloadfile/${councilPreamble.filestoreid.fileStoreId}"
								data-gallery> ${councilPreamble.filestoreid.fileName}</a>
							<small class="error-msg"><spring:message
									code="lbl.mesg.document" /></small>
						</c:when>
						<c:otherwise>
							<form:input path="attachments" type="file" id="attachments"
								name="attachments" required="true" data-id="1"
								class="filechange inline btn upload-file" />
							<small class="error-msg"><spring:message
									code="lbl.mesg.document" /></small>
							<form:errors path="attachments" cssClass="error-msg" />
						</c:otherwise>
					</c:choose>
				</div>

				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.ward.no" /></label>
				<div class="col-sm-3 add-margin">
					<select name="wards" multiple id="wards" size="7"
						class="form-control wards tick-indicator">
						<option value="">All</option>
						<c:forEach items="${wards}" var="ward">
							<option value="${ward.id}" title="${ward.name}"
								<c:if test="${fn:contains(councilPreamble.wards, ward)}"> Selected </c:if>>${ward.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div>Note: After getting the council preamble prepared and
				approved by the head of the section, the same should be uploaded
				here and forward to the competent authority for further action</div>
		</div>
	</div>
</div>
