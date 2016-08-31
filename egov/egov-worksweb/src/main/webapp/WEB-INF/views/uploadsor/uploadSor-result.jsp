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

<%@ include file="/includes/taglibs.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<script
	src="<cdn:url value='/resources/js/uploadsor/uploadsor.js?rnd=${app_release_no}'/>"></script>
<style>
.file-ellipsis {
	width: auto !Important;
}

.padding-10 {
	padding: 10px;
}
</style>
<form:form name="uploadSORForm" role="form" method="post" modelAttribute="uploadSOR" id="uploadSOR" class="form-horizontal form-groups-bordered" enctype="multipart/form-data">
	<div class="panel panel-primary" data-collapsed="0"
		style="scrollable: true;">
		<div class="panel-heading">
			<div class="panel-title">
				<spring:message code="title.upload.sor.result" />
			</div>
		</div>
		<div class="panel-title text-center" style="color: green;">
			<c:out value="${message}" /><br />
		</div>
		<div>
			<spring:hasBindErrors name="uploadSOR">
				<div class="alert alert-danger col-md-10 col-md-offset-1">
		      			<form:errors path="*" /><br/>
		      	</div>
	       	</spring:hasBindErrors>
		</div>
		<div>
			<center>
				<table align="center" width="100%">
					<tr align="center">
						<th style="width: 2%; text-align: center" align="center"><a
							href="#" onclick="urlLoad('${ originalFileStoreId}');"
							id="sourceLink" /> <spring:message
								code="lbl.download.original.file" /></a></th>
	
					</tr>
					<tr align="center">
	
						<th style="width: 2%; text-align: center" align="center"><a
							href="#" onclick="urlLoad('${outPutFileStoreId}');" id="sourceLink" />
							<spring:message code="lbl.download.output.file" /></a></th>
					</tr>
				</table>
	
				<div class="buttonbottom" id="buttondiv">
					<table>
						<tr>
							<td><input type="button" value="Close"
								onclick="javascript:window.close()" class="buttonsubmit" /></td>
						</tr>
					</table>
				</div>
			</center>
		</div>
	</div>
</form:form>