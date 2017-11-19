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

<%@page import="org.python.modules.jarray"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading custom_form_panel_heading">
				<div class="panel-title">Uploaded Document Details</div>
			</div>
			<div class="panel-body custom">
				<div class="col-sm-12 add-margin">
					<div class="files-upload-container" data-file-max-size="2"
						data-allowed-extenstion="doc,docx,xls,xlsx,rtf,pdf,txt,zip,jpeg,jpg,png,gif,tiff">
						<div class="files-viewer">
							<c:choose>
								<c:when test="${not empty councilMeeting.getSupportDocs()}">
									<c:forEach items="${councilMeeting.getSupportDocs()}"
										var="file">
										<div class="file-viewer" data-toggle="tooltip"
											data-placement="top" title="${file.fileName}">
											<a class="download" target="_blank"
												href="/egi/downloadfile?fileStoreId=${file.fileStoreId}&moduleName=COUNCIL"></a>
											<c:choose>
												<c:when test="${file.contentType eq 'application/pdf'}">
													<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
												</c:when>
												<c:when test="${file.contentType eq 'application/txt'}">
													<i class="fa fa-file-text-o" aria-hidden="true"></i>
												</c:when>
												<c:when
													test="${file.contentType eq 'application/rtf' || file.contentType eq 'application/doc' || file.contentType eq 'application/docx'}">
													<i class="fa fa-file-word-o" aria-hidden="true"></i>
												</c:when>
												<c:when test="${file.contentType eq 'application/zip'}">
													<i class="fa fa-file-archive-o" aria-hidden="true"></i>
												</c:when>
												<c:when
													test="${file.contentType eq 'image/jpg' || file.contentType eq 'image/jpeg' || file.contentType eq 'image/png' || file.contentType eq 'image/gif' || file.contentType eq 'image/tiff'}">
													<i class="fa fa-picture-o" aria-hidden="true"></i>
												</c:when>
												<c:when
													test="${file.contentType eq 'application/xls' || file.contentType eq 'application/xlsx'}">
													<i class="fa fa-file-excel-o" aria-hidden="true"></i>
												</c:when>
												<c:otherwise>
													<i class="fa fa-file-o" aria-hidden="true"></i>
												</c:otherwise>
											</c:choose>
										</div>
									</c:forEach>
								</c:when>
							<c:otherwise>
									No Attachment Found
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>



<script
	src="<cdn:url value='/resources/app/js/document-upload-helper.js?rnd=${app_release_no}'/>"></script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/app/css/council-style.css?rnd=${app_release_no}'/>">
