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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.advertisement.documents" />
	</div>
</div>

<div  id="hoardingattachments">
	<c:choose>
		<c:when test="${not empty hoardingDocumentTypes}">
			<form:hidden path="advertisement.latitude" id="latitude" />
			<form:hidden path="advertisement.longitude" id="longitude" />
			<div class="col-sm-12 view-content header-color hidden-xs">
				<div class="col-sm-1 table-div-column">
					<spring:message code="lbl.srl.no" />
				</div>
				<div class="col-sm-3 table-div-column ">
					<spring:message code="lbl.documentname" />
				</div>
				<div class="col-sm-3 table-div-column text-center">
					<spring:message code="lbl.enclosed" />
				</div>
				<div class="col-sm-5 table-div-column text-center">
					<spring:message code="lbl.attachdocument" />
				</div>
			</div>

			<c:forEach var="docs" items="${hoardingDocumentTypes}"
				varStatus="status1">
				<div class="form-group">
					<div class="col-sm-1 text-center">${status1.index+1}</div>
					<div class="col-sm-3 text-left">${advertisementPermitDetail.advertisement.documents[status1.index].doctype.mandatory ? "<span
				class='mandatory'></span>" : ""}${advertisementPermitDetail.advertisement.documents[status1.index].doctype.name}
					</div>
					<div class="col-sm-3 text-center">
						<input type="checkbox"
							${advertisementPermitDetail.advertisement.documents[status1.index].enclosed && advertisementPermitDetail.advertisement.documents[status1.index].files.size()>0 ? "checked='checked'  disabled='disabled'" : ""}
							name="advertisement.documents[${status1.index}].enclosed"
							${advertisementPermitDetail.advertisement.documents[status1.index].doctype.mandatory ? "required='required'" : ""}>
					</div>
					<div class="col-sm-5 text-center">
						<c:forEach var="file"
							items="${advertisementPermitDetail.advertisement.documents[status1.index].files}">
							<a
								href="/egi/downloadfile?fileStoreId=${file.fileStoreId}&moduleName=ADTAX&toSave=true">
								${file.fileName}<br>
							</a>

						</c:forEach>

						<input type="file"
							name="advertisement.documents[${status1.index}].attachments"
							class="form-control">
						<form:errors
							path="advertisement.documents[${status1.index}].attachments"
							cssClass="add-margin error-msg" />
						<form:hidden
							path="advertisement.documents[${status1.index}].doctype"
							value="${advertisement.documents[status1.index].doctype.id}" />
						<form:hidden path="advertisement.documents[${status1.index}].id"
							value="${advertisement.documents[status1.index].id}" />

					</div>


				</div>
			</c:forEach>
		</c:when>
	</c:choose>
</div>

