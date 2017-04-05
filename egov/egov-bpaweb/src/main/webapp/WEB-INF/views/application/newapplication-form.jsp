<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~      accountability and the service delivery of the government  organizations.
  ~
  ~       Copyright (C) 2016  eGovernments Foundation
  ~
  ~       The updated version of eGov suite of products as by eGovernments Foundation
  ~       is available at http://www.egovernments.org
  ~
  ~       This program is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       any later version.
  ~
  ~       This program is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU General Public License for more details.
  ~
  ~       You should have received a copy of the GNU General Public License
  ~       along with this program. If not, see http://www.gnu.org/licenses/ or
  ~       http://www.gnu.org/licenses/gpl.html .
  ~
  ~       In addition to the terms of the GPL license to be adhered to in using this
  ~       program, the following additional terms are to be complied with:
  ~
  ~           1) All versions of this program, verbatim or modified must carry this
  ~              Legal Notice.
  ~
  ~           2) Any misrepresentation of the origin of the material is prohibited. It
  ~              is required that all modified versions of this material be marked in
  ~              reasonable ways as different from the original version.
  ~
  ~           3) This license does not grant any rights to any user of the program
  ~              with regards to rights under trademark law for use of the trade names
  ~              or trademarks of eGovernments Foundation.
  ~
  ~     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div class="row">
	<div class="col-md-12">
		<div class="text-right error-msg" style="font-size: 14px;">
			<spring:message code="lbl.application.date" />
			:
			<fmt:formatDate pattern="dd/MM/yyyy"
				value="${waterConnectionDetails.applicationDate}" />
		</div>
		<form:form role="form" action="newApplication-create" method="post"
			modelAttribute="bpaApplication" id="newApplicationform"
			cssClass="form-horizontal form-groups-bordered"
			enctype="multipart/form-data">
			<input type="hidden" id="bpaApplication" name="bpaApplication" value="${bpaApplication.id}" />

			<form:hidden id="mode" path="" value="${mode}" />

			<div class="panel panel-primary" data-collapsed="0">

				<div class="panel-body custom-form ">
					<jsp:include page="applicationDetails.jsp"></jsp:include>
					<jsp:include page="applicantDetailForm.jsp"></jsp:include>
					<jsp:include page="siteDetail.jsp"></jsp:include>

				</div>
			</div>
			<%-- <jsp:include page="../common/commonWorkflowMatrix.jsp"/>
				<div class="buttonbottom" align="center">
					<jsp:include page="../common/commonWorkflowMatrix-button.jsp" />
				</div> --%>
			<div class="buttonbottom" align="center">
				<table>
					<tr>
						<td><form:button type="submit" id="Create"
								class="btn btn-primary" value="Create">Submit</form:button> <input
							type="button" name="button2" id="button2" value="Close"
							class="btn btn-primary" onclick="window.close();" /></td>
					</tr>
				</table>
			</div>
		</form:form>
	</div>
</div>

<script
	src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/js/app/application-new.js?rnd=${app_release_no}'/>"></script>
	