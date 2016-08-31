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

<%@ include file="/includes/taglibs.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<script src="<cdn:url value='/resources/js/uploadsor/uploadsor.js?rnd=${app_release_no}'/>"></script>
<style>
	.file-ellipsis {
		width : auto !Important;
	}
	
	.padding-10
	{
	  padding:10px;
	}
</style>
<form:form name="uploadSORForm" role="form" method="post" modelAttribute="uploadSOR" id="uploadSOR" class="form-horizontal form-groups-bordered" enctype="multipart/form-data">
	<div class="panel panel-primary" data-collapsed="0" style=" scrollable:true;">
		<div class="panel-heading">
			<div class="panel-title">
					<spring:message code="title.upload.sor" />
			</div>
		</div>
		<input type="hidden"  id="documentsSize">
			<div>
				<table width="100%">
					<tbody>
						<tr>
							<td width="50%">
								<table width="100%">
									<tbody>
										<tr>
											<td valign="top">
											 	<table id="uploadertbl" width="100%"><tbody>
											 		<tr id="row1" align="center">			 				
														<td>
															<input type="file" name="file" id="file" onchange="isValidFile(this.id)" class="padding-10" required="required">										
														</td>		
													</tr>									 										
											 	</tbody></table>						 								
												<%-- <div class="add-margin error-msg text-center padding-10"><font size="2"><spring:message code="msg.documents.maxsize" /></font></div> --%>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
							<td>
								<table width="100%">
									<thead>
										<tr>
											<th style="text-align: center"><spring:message code="lbl.original.files" /></th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${originalFiles}" var="originalFile" varStatus="item">
											<tr>
												<td style="text-align: center" align="center">
													<a href="#" onclick="urlLoad('${originalFile.fileStoreId}');" id="originalFileId" /> ${originalFile.fileName} </a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</td>
							<td>
								<table width="100%">
									<thead>
										<tr>
											<th style="text-align: center"><spring:message code="lbl.output.files" /></th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${outPutFiles}" var="outPutFile" varStatus="item">
											<tr>
												<td style="text-align: center" align="center">
													<a href="#" onclick="urlLoad('${outPutFile.fileStoreId}');" id="originalFileId" /> ${outPutFile.fileName} </a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>	
				</table>
				<table>
					<tr>
						 <td>
								&nbsp&nbsp&nbsp <a href="/egworks/resources/app/formats/Upload_Schedule_Of_Rate_Template.xls"><spring:message code="lbl.download.template" /></a>
						 </td>
					</tr>
				</table>
			</div>
	</div>
	
			<table width="100%">
					<tbody>
						<tr>
							<td align="center">
								<input type="submit" id="uploadButton" class="btn btn-primary" value="Upload" >
								<input type="button" name="close" id="closeButton" value="Close" class="btn btn-default" onclick="window.close();" />
							</td>
						</tr>
					</tbody>
				</table>
</form:form>