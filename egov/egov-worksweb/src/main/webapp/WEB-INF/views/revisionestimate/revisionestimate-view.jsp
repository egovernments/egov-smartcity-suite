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
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<style>
      .position_alert{
        position:fixed;z-index:9999;top:85px;right:20px;background:#F2DEDE;padding:10px 20px;border-radius: 5px;
      }
     
.msheet-tr {
	background: #f7f7f7;
}

.msheet-table {
	border: 1px solid #ddd;
}

.msheet-table thead:first-child>tr:first-child th, .msheet-table thead:first-child>tr th {
	background: #E7E7E7;
	color: #333;
	font-weight:normal;
}
 .position_alert1{
        background:#F2DEDE;padding:10px 20px;border-radius: 5px;margin-right: 10px;color:#333;font-size:14px;position: absolute; top: 10px;right: 10px;
      }

    </style>
<form:form name="revisionEstimateForm" role="form" method="post" modelAttribute="revisionEstimate" id="revisionEstimate"
	class="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<input type="hidden" id="defaultDepartmentId" value="${defaultDepartmentId }" />
	<input type="hidden" name="removedActivityIds" id="removedActivityIds" value="${removedActivityIds }" class="form-control table-input hidden-input"/>
	<div class="new-page-header"><spring:message code="lbl.re" /></div> 
	<input type="hidden" name="revisionEstimateId" id="revisionEstimateId" value="${revisionEstimate.id}"/>
	<div class="panel-title text-center" style="color: green;">
		<c:out value="${message}" /><br />
	</div>
	<%@ include file="estimateheaderdetail.jsp"%>
		<div class="panel-heading">
			<ul class="nav nav-tabs" id="settingstab">
				<li class="active"><a data-toggle="tab" href="#revisionheader"
					data-tabidx=0><spring:message code="lbl.estimate.boq" /></a></li>
				<li><a data-toggle="tab" href="#nontendered" data-tabidx=1><spring:message
							code="lbl.nontendered.items" /> </a></li>
				<li><a data-toggle="tab" href="#changequantity" data-tabidx=1><spring:message
							code="lbl.change.quantity" /> </a></li>
			</ul>
		</div>
		<div class="tab-content">
			<div class="tab-pane fade in active" id="revisionheader">   
				<c:choose>
					<c:when test="${revisionEstimate.parent.activities.isEmpty()}">
						<div class="panel panel-primary" data-collapsed="0">
							<div class="panel-body custom-form">
								<div class="col-sm-9 add-margin error-msg text-left"><spring:message code="msg.abstractestimate.boq.null" /></div>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<%@ include file="revisionestimate-viewsor.jsp"%> 
			 			<%@ include file="revisionestimate-viewnonsor.jsp"%>
			 			<%@ include file="revisionestimate-viewnontenderedsor.jsp"%> 
			 			<%@ include file="revisionestimate-viewnontenderednonsor.jsp"%>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="tab-pane fade" id="nontendered">
				 <%@ include file="revisionestimate-viewnontendered.jsp"%>
				 <%@ include file="revisionestimate-viewlumpsum.jsp"%> 
			</div>
			<div class="tab-pane fade" id="changequantity">
				 <%@ include file="revisionestimate-viewchangequantitys.jsp"%> 
			</div>
		</div>
		
		<c:if test="${!workflowHistory.isEmpty() && mode != null }">
			<jsp:include page="../common/commonworkflowhistory-view.jsp"></jsp:include>
		</c:if>
	<c:choose>
		<c:when test="${mode != 'view' }">
			<jsp:include page="../common/commonworkflowmatrix.jsp"/>
			<div class="buttonbottom" align="center">
				<jsp:include page="../common/commonworkflowmatrix-button.jsp" />
	                   
			</div>
		</c:when>
		<c:otherwise>
			<div class="row text-center">
			    <c:if test="${revisionEstimate.egwStatus.code == 'APPROVED' }">
					<a href="javascript:void(0)" class="btn btn-primary" onclick="renderPDF()" ><spring:message code="lbl.view.revisionagreemantpdf" /></a>
				</c:if>
				<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
			</div>
		</c:otherwise>
	</c:choose>
</form:form> 
<script type="text/javascript" src="<cdn:url value='/resources/js/revisionestimate/revisionestimate.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
