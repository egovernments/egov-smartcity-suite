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
	<input type="hidden" id="errorlocation" value="<spring:message code='error.locationdetails.required' />">
	<form:hidden path="" name="removedActivityIds" id="removedActivityIds" value="${removedActivityIds }" class="form-control table-input hidden-input"/>
	<input type="hidden" name="locationAppConfig" id="locationAppConfig" value="${isLocationDetailsRequired}"/>
	<input type="hidden" name="estimateId" id="estimateId" value="${revisionEstimate.parent.id}"/>
	<div class="new-page-header"><spring:message code="lbl.createre" /></div> 
	
	<div class="panel-title text-center" style="color: green;">
		<c:out value="${message}" /><br />
	</div>
	<input type="hidden" id="exceptionaluoms" name="exceptionaluoms" value='<c:out value="${exceptionaluoms}"/>'/>
	<form:hidden path="estimateValue" id="estimateValue" name="estimateValue" value='<c:out value="${estimateValue}" default="0.0" />'/>
	<form:hidden path="parent" id="parent" name="parent" value="${revisionEstimate.parent.id }"/>
	<form:hidden path="id" id="id" name="id" value="${revisionEstimate.id }"/>
	<input type="hidden" id="workValue" name="workValue" value='<c:out value="${revisionEstimate.workValue}" default="0.0" />'/>
	<input type="hidden" id="exceptionaluoms" name="exceptionaluoms" value='<c:out value="${exceptionaluoms}"/>'/>
	<input type="hidden" name="workOrderDate" id="workOrderDate"  data-idx="0" data-optional="0" class="form-control datepicker estimateDateClassId" maxlength="10" data-inputmask="'mask': 'd/m/y'" data-date-end-date="-0d" style="display: none" value='${workOrderDate}'  />
	<input id="cancelConfirm" type="hidden" value="<spring:message code="msg.revisionestimate.confirm" />" />
	<input type="hidden" id="workOrderNumber" name="workOrderNumber" value='<c:out value="${workOrderEstimate.workOrder.workOrderNumber }"/>'/>
	<input type="hidden" id="workOrderEstimateId" name="workOrderEstimateId" value='<c:out value="${workOrderEstimate.id }"/>'/>
	<input type="hidden" name="removedActivityIds" id="removedActivityIds" value="${removedActivityIds }" class="form-control table-input hidden-input"/>
	<input type="hidden" id="erroractivitymandatory" value="<spring:message code='error.re.activity.mandatory' />">
	<input type="hidden" id="errorworkvaluenegative" value="<spring:message code='error.workvalue.negative' />">
	<input id="errorAmountRuleForward" type="hidden" value="<spring:message code="error.forward.approve" />" />
    <input id="errorAmountRuleApprove" type="hidden" value="<spring:message code="error.create.approve" />" />
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
				 <%@ include file="revisionestimate-nontendered.jsp"%>
				 <%@ include file="revisionestimate-lumpsum.jsp"%> 
			</div>
			<div class="tab-pane fade" id="changequantity">
				<%@ include file="revisionestimate-changequantity.jsp"%>
			</div>
		</div>
		<%-- <div class="col-sm-12 text-center">
			<form:button type="button" id="submitForm" class="btn btn-primary" value="Save" ><spring:message code="lbl.save"/></form:button>
			<form:button type="button" class="btn btn-default" id="button2" onclick="window.close();"><spring:message code="lbl.close"/></form:button>
		</div> --%>
		
		<c:if test="${!workflowHistory.isEmpty() && mode != null }">
			<jsp:include page="../common/commonworkflowhistory-view.jsp"></jsp:include>
		</c:if>
		<jsp:include page="../common/commonworkflowmatrix.jsp"/>
		<div class="buttonbottom" align="center">
			<jsp:include page="../common/commonworkflowmatrix-button.jsp" />
                   
		</div>
				
</form:form> 
<script type="text/javascript" src="<cdn:url value='/resources/js/revisionestimate/revisionestimate.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>

<div id="measurement" >
<%@ include file="../measurementsheet/measurementsheet-formtable.jsp"%>
</div>

<div id="cqMeasurement" >
<%@ include file="../measurementsheet/changequantity-measurementsheet-formtable.jsp"%>
</div>      
