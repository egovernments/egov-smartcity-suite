<%@ include file="/includes/taglibs.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
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

<script
	src="<cdn:url value='/resources/js/app/documentsupload.js?rnd=${app_release_no}'/>"></script>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
		<c:choose>
			<c:when test="${mode != 'view'}">
			<spring:message code="lbl.documents" />
			</c:when>
			<c:otherwise>
				<spring:message code="lbl.upload.document" />
			</c:otherwise>
		</c:choose>
		</div>
	</div>
	<c:if test="${!judgment.judgmentDocuments.isEmpty() && mode == 'view' || mode =='edit'}">
		<c:forEach items="${judgment.judgmentDocuments}" var="judgmentDocuments">
			<a href="/egi/downloadfile?fileStoreId=${judgmentDocuments.supportDocs.fileStoreId}&moduleName=LCMS">${judgmentDocuments.supportDocs.fileName }</a><br />
		</c:forEach>
	</c:if>
	<c:if test="${mode == 'view' && judgment.judgmentDocuments.isEmpty()}">
		<spring:message code="lbl.no.documents" />
	</c:if>
	<c:if test="${ mode != 'view'}">
		<div>
			<table>
				<tbody>
					<tr>
						<td valign="top">
						 	<table id="uploadertbl"><tbody>
						 		<tr id="row1">			 				
									<td>
										<input type="file" name="file" id="file1" onchange="isValidFile(this.id)">
										<div class="add-margin error-msg text-left" ><font size="2">
								<spring:message code="lbl.mesg.document"/>	
								</font></div>
									</td>
								</tr>									 										
						 	</tbody></table>
						</td>
					</tr>
				</tbody>
			</table>
			<div class="buttonbottom" align="center">
				<div class="form-group text-center">
				<button id="attachNewFileBtn" type="button" class="btn btn-primary" onclick="addFileInputField()"><spring:message code="lbl.addfile" /></button>
				</div>
			</div>
		</div>
	</c:if>
</div>