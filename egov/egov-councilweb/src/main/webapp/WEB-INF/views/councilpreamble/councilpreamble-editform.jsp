<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
				<%-- <c:if test="${!autoPreambleNoGenEnabled && currentState=='NEW'}">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.preamble.number" /><span class="mandatory"></span> : </label>
					<div class="col-sm-3 add-margin">
						<form:input path="preambleNumber" required="required"
							id="preambleNumber"
							class="form-control text-left patternvalidation" />
						<form:errors path="preambleNumber" cssClass="error-msg" />
					</div>
				</c:if> --%>

				<c:if
					test="${councilPreamble.preambleNumber!= null && !''.equalsIgnoreCase(councilPreamble.preambleNumber)}">
					<label class="col-sm-2 control-label text-right"> <spring:message
							code="lbl.preamble.number" /> :
					</label>
					<div class="col-sm-3 add-margin">
						${councilPreamble.preambleNumber}</div>
				</c:if>
				<label class="col-sm-2 control-label text-right"> <spring:message
						code="lbl.preamble.referencenumber" /> :
				</label>
				<div class="col-sm-3 add-margin">
					${councilPreamble.referenceNumber}</div>

				<input type="hidden" name="councilPreamble"
					value="${councilPreamble.id}" />
				<form:hidden path="type" id="type" value="${councilPreamble.type}" />
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.department" /> : </label>
				<div class="col-sm-3 add-margin">
					${councilPreamble.department.name}</div>

				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.amount" /> : </label>
				<div class="col-sm-3 add-margin">
					${councilPreamble.sanctionAmount}</div>

			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.ward.no" /> : </label>
				<div class="col-sm-3 add-margin">
					<div class=" add-margin ">
						<c:forEach items="${councilPreamble.wards}" var="ward"
							varStatus="i">
							<c:if test="${i.index ne 0}">, </c:if> ${ward.name}
						</c:forEach>
					</div>
				</div>
			</div>

			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.gistofpreamble" /> : </label>
				<div class="col-sm-8 add-margin">
					<form:textarea path="gistOfPreamble" id="gistOfPreamble"
						data-role="none" rows="5"
						class="form-control text-left patternvalidation" maxlength="10000"
						readonly="true" required="required" />
					<form:errors path="gistOfPreamble" cssClass="error-msg" />
				</div>

			</div>


			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.integrated.gistofpreamble" /><span class="mandatory"></span>
					: </label>
				<div class="col-sm-8 add-margin">
					<form:textarea path="addtionalGistOfPreamble"
						id="addtionalGistOfPreamble" data-role="none" rows="5"
						class="form-control text-left patternvalidation" maxlength="10000"
						required="required" />
					<form:errors path="addtionalGistOfPreamble" cssClass="error-msg" />
				</div>

			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.upload" /><span class="mandatory"></span></label>
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
			</div>
			<div>Note: After getting the council preamble prepared and
				approved by the head of the section, the same should be uploaded
				here and forward to the competent authority for further action</div>
		</div>
	</div>

	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title">Bidders Details</div>
		</div>
		<form:hidden path="" id="bidders" name="bidders" />
		<div class="panel-body">

			<div
				class="row add-margin hidden-xs visible-sm visible-md visible-lg header-color">
				<label class="col-sm-2 col-xs-6 add-margin"><spring:message
						code="lbl.name" /></label> <label class="col-sm-2 col-xs-6 add-margin"><spring:message
						code="lbl.amount" /> </label> <label class="col-sm-2 col-xs-6 add-margin"><spring:message
						code="lbl.percentage" /></label> <label
					class="col-sm-2 col-xs-6 add-margin"><spring:message
						code="lbl.position" /></label>
			</div>

			<c:choose>
				<c:when test="${!bidders.isEmpty()}">
					<c:forEach items="${bidders}" var="bidders">
						<div class="row add-margin">
							<div class="col-sm-2 col-xs-12 add-margin">
								<c:out value="${bidders.bidder.name}" />
							</div>
							<div class="col-sm-2 col-xs-12 add-margin">
								<c:out value="${bidders.quotedAmount}" />
							</div>
							<c:if test="${bidders.percentage!=null}">
							<div class="col-sm-2 col-xs-12 add-margin">
								<c:out value="${bidders.percentage}" />
								%
							</div>
							</c:if>
							<div class="col-sm-2 col-xs-12 add-margin">
								<c:out value="${bidders.position}" />
							</div>
						</div>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="col-md-3 col-xs-6 add-margin">No Bidders Present.</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
