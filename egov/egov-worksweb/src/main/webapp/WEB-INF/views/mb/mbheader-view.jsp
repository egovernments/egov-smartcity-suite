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
  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<style>
      .position_alert1{
        position:absolute;top:15px;right:20px;background:#F2DEDE;padding:10px 20px;border-radius: 5px;width:215px;
      }
      .position_alert2{
        position:absolute;top:15px;right:240px;background:#F2DEDE;padding:10px 20px;border-radius: 5px;
      }
      .position_alert3{
        background:#F2DEDE;padding:10px 20px;border-radius: 5px;margin-right: 10px;color:#333;font-size:14px;position: absolute; top: 11px;right: 180px;
      }
    </style>
<form:form name="mbHeaderSearchForm" action="" role="form" modelAttribute="mbHeader" id="mbHeader" class="form-horizontal form-groups-bordered"
	accept-charset="utf-8"
	enctype="multipart/form-data">
<input type="hidden" name="removedDetailIds" id="removedDetailIds" value="${removedDetailIds }" class="form-control table-input hidden-input"/>
<input type="hidden" id="mode" name="mode" value="${mode }">
<input name="mbHeader" type="hidden" id="id" value="${mbHeader.id }" />
<div class="position_alert1">
	<spring:message code="lbl.tender.finalizedperc" /> : <span id="tenderPerc">${mbHeader.workOrderEstimate.workOrder.tenderFinalizedPercentage}</span>
	<input type="hidden" name="tenderFinalizedPerc" id="tenderFinalizedPerc" value="${mbHeader.workOrderEstimate.workOrder.tenderFinalizedPercentage}"/>
	</div>
	<div class="position_alert2">
		<spring:message code="lbl.mb.amount" /> : &#8377 <fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${mbHeader.mbAmount}"/></fmt:formatNumber>
	</div>
<div class="new-page-header"><spring:message code="title.viewmbheader" /></div>  
	<div class="panel-heading">
			<ul class="nav nav-tabs" id="settingstab">
				<li class="active">
					<a data-toggle="tab" href="#mbheader" data-tabidx=0><spring:message code="lbl.mbheader" /></a>
				</li>
				<li>
					<a data-toggle="tab" href="#tenderitems" data-tabidx=1><spring:message code="lbl.tendered.items" /> </a>
				</li>
			</ul>
		</div>
		<div class="tab-content">
			<div class="tab-pane fade in active" id="mbheader">
				<jsp:include page="mbheader-viewheader.jsp" />
				<jsp:include page="../common/uploadDocuments.jsp"/>
			</div>
			
			<div class="tab-pane" id="tenderitems">
				<%@ include file="mbheader-viewtenderitems.jsp"%>
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
					    <a href="javascript:void(0)" class="btn btn-primary" onclick="renderMBPDF()"><spring:message code="lbl.viewmb.pdf" /></a>
	                    <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
					</c:otherwise>
				</c:choose>
            </div>
		</div>
</form:form>
<script src="<c:url value='/resources/js/mb/viewmeasurementbook.js?rnd=${app_release_no}'/>"></script>
<script src="<c:url value='/resources/global/js/egov/inbox.js' context='/egi'/>"></script>
<c:if test="${mode == 'edit' || mode == 'workflowView' }">
	<script type="text/javascript"
	src="<c:url value='/resources/js/mb/mbformsubmit.js?rnd=${app_release_no}'/>"></script>
</c:if>