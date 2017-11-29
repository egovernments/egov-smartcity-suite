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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
		<form:form name="lineEstimateForm" action="create-spillover" role="form" modelAttribute="lineEstimate" id="lineEstimateForm" class="form-horizontal form-groups-bordered" method="POST" enctype="multipart/form-data">
			<form:hidden path="" name="removedLineEstimateDetailsIds" id="removedLineEstimateDetailsIds" value="" class="form-control table-input hidden-input"/>
			<form:hidden path="" name="lineEstimateId" id="lineEstimateId" value="${lineEstimate.id}" class="form-control table-input hidden-input"/>
			<form:hidden path="" id="workFlowAction" name="workFlowAction"/>
			<input type="hidden" value="<spring:message code="error.actualestimateamount.required" />" id="erroractualestimateamount" />
			<form:hidden id="hiddenfields" path="" value="${hiddenfields}" />
			<div class="row">
				<div class="col-md-12">
					<jsp:include page="lineEstimateHeader.jsp"/>
					<jsp:include page="lineEstimateDetails.jsp"/>
				</div>
			</div>
			<jsp:include page="lineEstimateAdminSanctionDetails.jsp"></jsp:include>
			<jsp:include page="../uploadDocuments.jsp"/>
			<div class="buttonbottom" align="center">
				<table>
					<tr>
						<td id="actionButtons">
							<input type="submit" id="<spring:message code="lbl.save" />" class="btn btn-primary"  value="<spring:message code="lbl.save" />" />
							<input type="button" value="<spring:message code="lbl.close" />"
							class="btn btn-default" onclick="window.close();" /></td>
					</tr>
				</table>
			</div>
		</form:form>  
<script src="<cdn:url value='/resources/js/lineestimate/spillover/spillover.js?rnd=${app_release_no}'/>"></script>