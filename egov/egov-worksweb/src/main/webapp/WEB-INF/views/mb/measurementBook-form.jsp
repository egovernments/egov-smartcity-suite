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
<form name="mbHeader" role="form" action="" method="post" id="mbHeader"
	class="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	
	<input type="hidden" name="id" id="id">
	<input type="hidden" name="workOrder.id" id="workOrderId">
	<input type="hidden" name="workOrderEstimate.id" id="workOrderEstimateId">
	<input type="hidden" name="removedActivityIds" id="removedActivityIds" value="" class="form-control table-input hidden-input"/>
	
	<div class="new-page-header"><spring:message code="lbl.createmb" /></div> 
	
	<div class="panel-title text-center" style="color: green;">
		<c:out value="${message}" /><br />
	</div>
	
	<div class="alert text-left" style="color: red;" id="errorMessage" hidden="true">
	</div>

	<div class="main-content">
		<div class="panel-heading">
			<ul class="nav nav-tabs" id="settingstab">
				<li class="active"><a data-toggle="tab" href="#mbheader"
					data-tabidx=0><spring:message code="lbl.mbheader" /></a></li>
				<li><a data-toggle="tab" href="#tenderedItems" data-tabidx=1><spring:message
							code="lbl.tendered.items" /> </a></li>
				<li><a data-toggle="tab" href="#nonTenderedItems" data-tabidx=1><spring:message
							code="lbl.nontendered.items" /> </a></li>
			</ul>
		</div>
		<div class="tab-content">
			<div class="tab-pane fade in active" id="mbheader">
				<%@ include file="mb-header.jsp"%>
			</div>
			<div class="tab-pane fade" id="tenderedItems">
				<%@ include file="mb-sor.jsp"%>
				<%@ include file="mb-nonsor.jsp"%>
			</div>
			<div class="tab-pane fade" id="nonTenderedItems">
				<%@ include file="mb-nonTenderedItems.jsp"%>
			</div>
			<c:if test="${!workflowHistory.isEmpty() && mode != null }">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">
							<spring:message code="lbl.apphistory"/>
						</div>
					</div>
					<jsp:include page="../common/commonWorkflowhistory-view.jsp"></jsp:include>
				</div>
			</c:if>
			<%-- <jsp:include page="../common/commonWorkflowMatrix.jsp"/> --%>
			<div class="buttonbottom" align="center">
				<jsp:include page="../common/commonWorkflowMatrix-button.jsp" />
			</div>
		</div>
	</div>
</form>

<script type="text/javascript"
	src="<c:url value='/resources/js/mb/measurementbook.js?rnd=${app_release_no}'/>"></script>
	<script src="<c:url value='/resources/global/js/egov/inbox.js' context='/egi'/>"></script>