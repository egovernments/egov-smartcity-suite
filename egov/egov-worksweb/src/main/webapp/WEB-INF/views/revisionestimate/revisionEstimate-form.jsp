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

.msheet-table thead:first-child>tr:first-child th {
	background: #E7E7E7;
	color: #333;
}
 .position_alert1{
        background:#F2DEDE;padding:10px 20px;border-radius: 5px;margin-right: 10px;color:#333;font-size:14px;position: absolute; top: 10px;right: 10px;
      }

    </style>
<form:form name="revisionEstimateForm" role="form" method="post" modelAttribute="revisionEstimate" id="revisionEstimate"
	class="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
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
	<input type="hidden" id="workValue" name="workValue" value='<c:out value="${revisionEstimate.workValue}" default="0.0" />'/>
	<input type="hidden" id="exceptionaluoms" name="exceptionaluoms" value='<c:out value="${exceptionaluoms}"/>'/>
	<input type="hidden" name="workOrderDate" id="workOrderDate"  data-idx="0" data-optional="0" class="form-control datepicker estimateDateClassId" maxlength="10" data-inputmask="'mask': 'd/m/y'" data-date-end-date="-0d" style="display: none" value='${workOrderDate}'  />
	<input id="cancelConfirm" type="hidden" value="<spring:message code="lbl.estimate.confirm" />" />
	<%@ include file="estimateHeaderDetail.jsp"%>
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
			</div>
			<div class="tab-pane fade" id="nontendered">
				<%@ include file="revisionEstimate-nonTendered.jsp"%>
				<%@ include file="revisionEstimate-lumpSum.jsp"%>
			</div>
			<div class="tab-pane fade" id="changequantity">
			</div>
		</div>
  

</form:form> 
<script type="text/javascript" src="<cdn:url cdn='${applicationScope.cdn}' value='/resources/js/revisionestimate/revisionestimate.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>

<div id="measurement" >
<%@ include file="../measurementsheet/measurementsheet-formtable.jsp"%>
</div>      
