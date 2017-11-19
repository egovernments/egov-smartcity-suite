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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<script type="text/javascript" src="<cdn:url value='/resources/javascript/validations.js'/>"></script>
<script type="text/javascript">
$(document).ready(function()
{	
	if($("#currentDesignation").val() == 'Commissioner') {
		$('#Forward').hide();
	}
}); 
</script>
<style>
body
{
  font-family:regular !important;
  font-size:14px;
}
</style>

<form:form id="demolition" method="post" class="form-horizontal form-groups-bordered" modelAttribute="property">
	<div class="page-container" id="page-container">
		<div class="main-content">
			
				<jsp:include page="../common/commonPropertyDetailsView.jsp"></jsp:include>
				<jsp:include page="../common/ownerDetailsView.jsp"></jsp:include>
           <input type="hidden" id="currentDesignation" name="currentDesignation" value="${currentDesignation}"/>
			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0">

						<div class="panel-heading">
							<div class="panel-title">
								<spring:message code="lbl.hdr.demolition.details" />
							</div>
						</div>
						<div class="panel-body custom-form">
								<div class="form-group">
									<label class="col-sm-3 control-label text-right"><spring:message
											code="lbl.demolition.reason" />
									</label>
									<div class="col-sm-3 add-margin">
										<c:out default="N/A" value="${property.demolitionReason}"></c:out>
									</div>
								</div>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0">

						<div class="panel-heading" style="text-align: left">
							<div class="panel-title">
								<spring:message code="lbl.hdr.vacantland.details" />
							</div>
						</div>
                         <form:hidden path="" name="mode" id="mode" value="${mode}"/>
						 <table width="100%" border="0" cellspacing="0" cellpadding="0"
								class="tablebottom" id="vacantLandTable">

								<tbody>
									<tr>
										<th class="bluebgheadtd"><spring:message code="lbl.surveyNumber" /></th>
										<th class="bluebgheadtd"><spring:message code="lbl.pattaNumber" /></th>
										<th class="bluebgheadtd"><spring:message code="lbl.vacantland.area" /></th>
										<th class="bluebgheadtd"><spring:message code="lbl.MarketValue" /></th>
										<th class="bluebgheadtd"><spring:message code="lbl.currentCapitalValue" /></th>
									</tr>

									<tr id="vacantLandRow">
										<td class="blueborderfortd" align="center"><c:out default="N/A" value="${property.propertyDetail.surveyNumber}"></c:out></td>
										<td class="blueborderfortd" align="center"><c:out default="N/A" value="${property.propertyDetail.pattaNumber}"></c:out></td>
										<td class="blueborderfortd" align="center"><c:out default="N/A" value="${property.propertyDetail.sitalArea.area}"></c:out></td>
										<td class="blueborderfortd" align="center"><c:out default="N/A" value="${property.propertyDetail.marketValue}"></c:out></td>
										<td class="blueborderfortd" align="center"><c:out default="N/A" value="${property.propertyDetail.currentCapitalValue}"></c:out></td>

									</tr>

									<tr>
										<td colspan="6"><br>
											<table class="tablebottom" style="width: 100%;"
												id="boundaryData">
												<tbody>
													<tr>
														<th class="bluebgheadtd">North<span
															class="mandatory1">*</span></th>
														<th class="bluebgheadtd">East<span class="mandatory1">*</span></th>
														<th class="bluebgheadtd">West<span class="mandatory1">*</span></th>
														<th class="bluebgheadtd">South<span
															class="mandatory1">*</span></th>
													</tr>
													<tr>
														<td class="blueborderfortd" align="center"><c:out default="N/A" value="${property.basicProperty.propertyID.northBoundary}"></c:out></td>
														<td class="blueborderfortd" align="center"><c:out default="N/A" value="${property.basicProperty.propertyID.eastBoundary}"></c:out></td>
														<td class="blueborderfortd" align="center"><c:out default="N/A" value="${property.basicProperty.propertyID.westBoundary}"></c:out></td>
														<td class="blueborderfortd" align="center"><c:out default="N/A" value="${property.basicProperty.propertyID.southBoundary}"></c:out></td>
													</tr>
												</tbody>
											</table></td>
									</tr>
								</tbody>
							</table> 
					</div>
				</div>
			</div>
		    <c:choose>
            <c:when test="${currentDesignation != 'Commissioner'}">
			<jsp:include page="../common/commonWorkflowMatrix.jsp"/>
			</c:when>
			<c:otherwise>
			<c:if test="${!endorsementNotices.isEmpty()}"> 
 			<jsp:include page="/WEB-INF/views/common/endorsement_history.jsp"/>
			</c:if>
			<div class="row">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.comments"/></label>
					<div class="col-sm-8 add-margin">
						<form:textarea class="form-control" path=""  id="approvalComent" name="approvalComent" />
					</div>
				</div></c:otherwise> 
			</c:choose> 
	 	    <jsp:include page="../common/commonWorkflowMatrix-button.jsp"/>

		</div>
	</div>
</form:form>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>


