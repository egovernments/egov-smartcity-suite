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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<style>
.position_alert {
	position: fixed;
	z-index: 9999;
	top: 85px;
	right: 20px;
	background: #F2DEDE;
	padding: 10px 20px;
	border-radius: 5px;
}

.msheet-tr {
	background: #f7f7f7;
}

.msheet-table {
	border: 1px solid #ddd;
}

.msheet-table thead:first-child>tr:first-child th {
	background: #E7E7E7;
	color: #333;
}

 .position_alert3{
        background:#F2DEDE;padding:10px 20px;border-radius: 5px;margin-right: 10px;color:#333;font-size:14px;position: absolute; top: 10px;right: 10px;
      }
</style>
<form:form name="abstractEstimateForm" role="form" method="post" modelAttribute="abstractEstimate" id="abstractEstimate"
	class="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<input type="hidden" id="defaultDepartmentId" value="${defaultDepartmentId }" />
	<form:hidden path="" name="removedActivityIds" id="removedActivityIds" value="" class="form-control table-input hidden-input"/>
	<input type="hidden" id="isEstimateDeductionGrid" value="${isEstimateDeductionGrid}">
	<c:choose>
		<c:when test="${abstractEstimate.spillOverFlag }">
			<div class="new-page-header"><spring:message code="lbl.viewspilloverae" /></div>
		</c:when>
		<c:otherwise>
			<div class="new-page-header"><spring:message code="lbl.viewae" /></div>
		</c:otherwise>
	</c:choose>
	<form:hidden path="spillOverFlag" value="${abstractEstimate.spillOverFlag }" id="spillOverFlag" />
	<input type="hidden" name="lineEstimateRequired" id="lineEstimateRequired" value="${lineEstimateRequired }"/>

	<%@ include file="abstractestimate-vewheaderdetail.jsp"%>
		<div class="panel-heading">
			<ul class="nav nav-tabs" id="settingstab">
				<li class="active"><a data-toggle="tab" href="#estimateheader"
					data-tabidx=0><spring:message code="lbl.header" /></a></li>
				<li><a data-toggle="tab" href="#workdetails" data-tabidx=1><spring:message
							code="tab.header.scheduleA" /> </a></li>
				<li><a data-toggle="tab" href="#overheads" data-tabidx=1><spring:message
							code="tab.header.scheduleB" /> </a></li>
				<li><a data-toggle="tab" href="#assetandfinancials"
					data-tabidx=2> <spring:message
							code="tab.header.assetandfinancials" />
				</a></li>
			</ul>
		</div>
		<input type="hidden" name="estimateId" id="estimateId" value="${abstractEstimate.id}"/>
		<div class="tab-content">
			<div class="tab-pane fade in active" id="estimateheader">
				<%@ include file="abstractestimate-viewheader.jsp"%>
				<%@ include file="abstractestimate-viewmultiyearestimate.jsp"%>
				<c:if test="${lineEstimateRequired == 'false' && abstractEstimate.lineEstimateDetails == null }">
					<jsp:include page="estimateadminsanctiondetails.jsp" />
				</c:if>
				<c:if test="${(abstractEstimate.lineEstimateDetails != null && abstractEstimate.lineEstimateDetails.lineEstimate.abstractEstimateCreated == true) || abstractEstimate.spillOverFlag}">
					<%@ include file="spilloverestimate-viewtechnicalsanction.jsp"%>
				</c:if>
				<%@ include file="../common/uploaddocuments.jsp"%>
			</div>
			<div class="tab-pane fade" id="workdetails">
				<c:choose>
					<c:when test="${abstractEstimate.activities.isEmpty()}">
						<div class="panel panel-primary" data-collapsed="0">
							<div class="panel-body custom-form">
								<div class="col-sm-9 add-margin error-msg text-left"><spring:message code="msg.abstractestimate.boq.null" /></div>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<%@ include file="abstractestimate-viewsor.jsp"%> 
			 			<%@ include file="abstractestimate-viewnonsor.jsp"%> 
					</c:otherwise>
				</c:choose>  
			</div>
			<div class="tab-pane fade" id="overheads">
				<%@ include file="abstractestimate-viewoverheads.jsp"%>
				<c:if test="${isEstimateDeductionGrid == true }">
					<jsp:include page="abstractestimate-viewdeduction.jsp" />
				</c:if>
			</div>
			<div class="tab-pane fade" id="assetandfinancials">
				<%@ include file="abstractestimate-viewfinancialdetails.jsp"%>
				<%@ include file="abstractestimate-viewassetdetails.jsp"%>
			</div>
			<div class="text-center">
				<c:if test="${!workflowHistory.isEmpty() && !abstractEstimate.spillOverFlag}">
						<jsp:include page="../common/commonworkflowhistory-view.jsp"></jsp:include>
				</c:if>
				<c:choose>
					<c:when test="${mode == 'workflowView' }">
						<jsp:include page="../common/commonworkflowmatrix.jsp"/>
						<div class="buttonbottom" align="center">
							<jsp:include page="../common/commonworkflowmatrix-button.jsp" />
						</div>
					</c:when>
					<c:otherwise>
						<c:if test="${abstractEstimate.activities.size() > 0 && !abstractEstimate.egwStatus.code.equalsIgnoreCase('NEW') && !abstractEstimate.egwStatus.code.equalsIgnoreCase('CANCELLED')}">
	                    	<a href="javascript:void(0)" class="btn btn-primary" onclick="viewBOQ();"><spring:message code="lbl.viewBOQ" /></a>
	                    	<a href="javascript:void(0)" class="btn btn-primary" onclick="viewEstimatePDF();"><spring:message code="lbl.abstractestimate.pdf" /></a>
	                    </c:if>
	                    <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
					</c:otherwise>
				</c:choose>
            </div>
		</div>
</form:form>
<jsp:include page="abstractestimate-viewlocation.jsp" />
<script>
var msg = '<spring:message code="lbl.header" />';
</script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/abstractestimate/abstractestimate.js?rnd=${app_release_no}'/>"></script>
	<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
	<script src="<cdn:url value='/resources/js/viewlocationmap.js?rnd=${app_release_no}'/>"></script>
	<script src="<cdn:url value='/resources/js/loadmaps.js?rnd=${app_release_no}'/>"></script>
