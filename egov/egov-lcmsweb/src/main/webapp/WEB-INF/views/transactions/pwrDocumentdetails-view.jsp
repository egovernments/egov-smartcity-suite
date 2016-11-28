<%@ include file="/includes/taglibs.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<script
	src="<cdn:url value='/resources/js/app/documentsupload.js?rnd=${app_release_no}'/>"></script>
<style>
	.file-ellipsis {
		width : auto !Important;
	}
	
	.padding-10
	{
	  padding:10px;
	}
</style>
<div class="panel panel-primary" data-collapsed="0" style=" scrollable:true;">
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
	<c:if test="${!legalCase.pwrList[0].pwrDocuments.isEmpty() && mode == 'view' || mode =='counteredit'}">
		<c:forEach items="${legalCase.pwrList[0].pwrDocuments}" var="pwrDocuments">
			<a href="/egi/downloadfile?fileStoreId=${pwrDocuments.supportDocs.fileStoreId}&moduleName=LCMS">${pwrDocuments.supportDocs.fileName }</a><br />
		</c:forEach>
	</c:if>
	<c:if test="${mode == 'view' && legalCase.pwrList[0].pwrDocuments.isEmpty()}">
		<spring:message code="lbl.no.documents" />
	</c:if>
	<c:if test="${ mode != 'view'}">
		<div>
			<table width="100%">
				<tbody>
					<tr>
						<td valign="top">
						 	<table id="uploadertbl" width="100%"><tbody>
						 		<tr id="row1">			 				
									<td>
										<input type="file" name="file" id="file1" onchange="isValidFile(this.id)" class="padding-10">
									</td>
								</tr>									 										
						 	</tbody></table>
						</td>
					</tr>
					<tr>
						<td align="center">
							<button id="attachNewFileBtn" type="button" class="btn btn-primary" onclick="addFileInputField()"><spring:message code="lbl.addfile" /></button>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</c:if>
</div>
