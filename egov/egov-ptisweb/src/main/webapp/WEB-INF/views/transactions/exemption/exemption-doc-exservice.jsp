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

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<!-- <table class="tablebottom doctable" id="nameTable" width="100%" border="0" cellpadding="0" cellspacing="0"> -->

<c:choose>
	<c:when test="${!documentsList.isEmpty()}">
		<div class="panel-heading">
			<div class="panel-title">New Documents to attach</div>
		</div>
		<div class="form-group col-sm-12 view-content header-color hidden-xs">
			<table class="tablebottom doctable" width="100%" border="0"
				cellpadding="0" cellspacing="0">
				<th class="bluebgheadtd">
					<div class="col-sm-3 text-center">
						<spring:message code="lbl.sno" />
					</div>
					<div class="col-sm-3 text-center">
						<spring:message code="lbl.documentname" />
					</div>
					<div class="col-sm-3 text-center">
						<spring:message code="lbl.document.upload" />
						(<span class="mandatory"></span> )
					</div>
				</td>
				</th>
			</table>
		</div>
		<c:set var="index" value="0"></c:set>
		<div id="exservice">
			<c:forEach var="docs" items="${documentsExService}"
				varStatus="status">

				<div class="form-group">
					<table class="tablebottom doctable" width="100%" border="0"
						cellpadding="0" cellspacing="0" id="uploadertbl">
						<tr>
							<td>
								<div class="col-sm-3 add-margin check-text text-center">
									<%-- <c:out default="N/A" value="${docs.transactionType}"></c:out> --%>
									<c:choose>
										<c:when test="${docs.mandatory}">
											<input type="checkbox" checked disabled>&nbsp;<c:out
												value="${index + 1}" />
											<%-- <c:set var="index" value="${index}"></c:set> --%>
										</c:when>
										<c:otherwise>
											<input type="checkbox" disabled>&nbsp;<c:out
												value="${index + 1}" />
										</c:otherwise>
									</c:choose>
								</div>
								<div class="col-sm-3 add-margin check-text text-center">
									<c:choose>
										<c:when test="${docs.mandatory}">
											<input type="checkbox" id="check${index}" checked disabled>&nbsp;<c:out
												value="${docs.name}" />
										</c:when>
										<c:otherwise>
											<input type="checkbox" id="check${index}" disabled>&nbsp;<c:out
												value="${docs.name}" />
										</c:otherwise>
									</c:choose>
									<form:hidden id="taxExemptionDocumentsProxy${index}.type.id"
										path="taxExemptionDocumentsProxy[${index}].type.id"
										value="${docs.id}" />
									<form:hidden id="taxExemptionDocumentsProxy${index}.type.name"
										path="taxExemptionDocumentsProxy[${index}].type.name"
										value="${docs.name}" />
								</div>
								<div class="col-sm-3 add-margin text-center">
									<c:choose>
										<c:when test="${exserviceDocs.isEmpty()}">
											<c:choose>
												<c:when test="${docs.mandatory}">
													<input type="file" id="file${index}" data-idx="${index}"
														name="taxExemptionDocumentsProxy[${index}].file"
														class="file-ellipsis upload-file" required="true">
												</c:when>
												<c:otherwise>
													<input type="file" id="file${index}"
														name="taxExemptionDocumentsProxy[${index}].file"
														class="file-ellipsis upload-file">
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${!exserviceDocs[index].files.isEmpty()}">
													<c:forEach items="${exserviceDocs[index].files}" var="file">

														<input type="file" id="file${index}"
															name="taxExemptionDocumentsProxy[${index}].file"
															class="file-ellipsis upload-file">
														<a
															href="javascript:viewDocument('<c:out value="${file.fileStoreId}"/>')">
															<c:out value="${file.fileName}" />
														</a>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<input type="file" id="file${index}"
														name="taxExemptionDocumentsProxy[${index}].file"
														class="file-ellipsis upload-file">
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
									<form:errors path="taxExemptionDocumentsProxy[${index}].file"
										cssClass="add-margin error-msg" />
									<div class="add-margin error-msg text-left">
										<spring:message code="lbl.document.size" />
										<font size="1"> </font>
									</div>
								</div> <c:set var="index" value="${index+1}"></c:set>
							</td>
						</tr>
					</table>
				</div>
			</c:forEach>
		</div>
	</c:when>
</c:choose>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/documentsupload.js?rnd=${app_release_no}'/>"></script>


