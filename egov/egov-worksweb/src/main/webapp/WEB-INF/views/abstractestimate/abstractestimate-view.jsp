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
<style>
      .position_alert{
        position:fixed;z-index:9999;top:85px;right:20px;background:#F2DEDE;padding:10px 20px;border-radius: 5px;
      }
    </style>
<form:form name="abstractEstimateForm" role="form" method="post" modelAttribute="abstractEstimate" id="abstractEstimate"
	class="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<form:hidden path="" name="removedActivityIds" id="removedActivityIds" value="" class="form-control table-input hidden-input"/>

	<div class="new-page-header"><spring:message code="lbl.viewae" /></div>

	<div class="main-content">
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
				<%@ include file="../common/uploadDocuments.jsp"%>
			</div>
			<div class="tab-pane fade" id="workdetails">
				 <%@ include file="abstractestimate-viewsor.jsp"%> 
			 	 <%@ include file="abstractestimate-viewnonsor.jsp"%> 
			</div>
			<div class="tab-pane fade" id="overheads">
				<%@ include file="abstractestimate-viewoverheads.jsp"%>
			</div>
			<div class="tab-pane fade" id="assetandfinancials">
				<%@ include file="abstractestimate-viewfinancialdetails.jsp"%>
				<%@ include file="abstractestimate-viewassetdetails.jsp"%>
			</div>
			<div class="text-center">
				<c:choose>
					<c:when test="${mode == 'workflowView' }">
						<c:if test="${!workflowHistory.isEmpty()}">
							<div class="panel panel-primary" data-collapsed="0">
								<div class="panel-heading">
									<div class="panel-title">
										<spring:message  code="lbl.apphistory"/>
									</div>
								</div>
								<jsp:include page="../common/commonWorkflowhistory-view.jsp"></jsp:include>
							</div>
						</c:if>
						<jsp:include page="../common/commonWorkflowMatrix.jsp"/>
						<div class="buttonbottom" align="center">
							<jsp:include page="../common/commonWorkflowMatrix-button.jsp" />
						</div>
					</c:when>
					<c:otherwise>
						<%-- <button type="button" id="viewAEPdf" class="btn btn-primary"><spring:message code="lbl.viewpdf" /></button> --%>
						<c:if test="${abstractEstimate.activities.size() > 0 && !abstractEstimate.egwStatus.code.equalsIgnoreCase('NEW')}">
	                    	<a href="javascript:void(0)" class="btn btn-primary" onclick="viewBOQ();"><spring:message code="lbl.viewBOQ" /></a>
	                    </c:if>
	                    <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
					</c:otherwise>
				</c:choose>
            </div>
		</div>
	</div>
</form:form>
<script>
var msg = '<spring:message code="lbl.header" />';
</script>
<script type="text/javascript"
	src="<c:url value='/resources/js/abstractestimate/abstractestimate.js?rnd=${app_release_no}'/>"></script>
	<script src="<c:url value='/resources/global/js/egov/inbox.js' context='/egi'/>"></script>