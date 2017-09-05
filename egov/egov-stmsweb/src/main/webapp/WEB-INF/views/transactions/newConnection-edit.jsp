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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<form:form role="form" action="/stms/transactions/update/${sewerageApplicationDetails.applicationNumber}" method="post" modelAttribute="sewerageApplicationDetails" id="editSewerageApplicationDetailsForm" cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">				
	<form:hidden id="mode" path="" name="mode" value="${mode}"/> 
	<form:hidden id="editdonationcharge" path="" name="editdonationcharge" value="${editdonationcharge}"/> 
	
	<form:hidden id="showApprovalDtls" path="" name="showApprovalDtls" value="${showApprovalDtls}"/> 
	<form:hidden path="" id="approvalPositionExist" value="${approvalPositionExist}"/>
	<form:hidden path="status.code" id="statuscode" value="${sewerageApplicationDetails.status.code}"/>
	<%-- <form:hidden path="status" id="status" value="${sewerageApplicationDetails.status}"/>
	 --%><form:hidden path="state" id="wfstate" value="${sewerageApplicationDetails.state.id}"/> 
	<input type="hidden" id="sewerageInspectionDate"  value='<fmt:formatDate value="${sewerageApplicationDetails.fieldInspections[0].inspectionDate}" pattern="dd/MM/yyyy"/>' />
	<input type="hidden" id="currentUser" value="${currentUser}"/>  
	<input type="hidden" id="sewerageTaxDue" value="${sewerageTaxDue}" name="sewerageTaxDue"/>  
	<input type="hidden" id="estimationChargesExists" value="${estimationChargesExists}"/>  
	<form:hidden path="" id="workFlowAction" name="workFlowAction"/>
	<form:hidden path="applicationType" id="applicationType" value="${sewerageApplicationDetails.applicationType.id}"/>
	<form:hidden path="connection.status" id="connection.status" value="${sewerageApplicationDetails.connection.status}"/>
	<%-- <form:hidden path="connection" id="connection" value="${sewerageApplicationDetails.connection.id}"/>
	<form:hidden path="id" id="id" value="${sewerageApplicationDetails.id}"/> 
	 --%><%-- <form:hidden path="connectionFees" id="connectionFees" value="${sewerageApplicationDetails.connectionFees.id}"/>
	<form:hidden path="fieldInspections" id="fieldInspections" value="${sewerageApplicationDetails.fieldInspections}"/>
	 --%><%-- <form:hidden path="connection.connectionDetail" id="connection.connectionDetail" value="${sewerageApplicationDetails.connection.connectionDetail}"/>
	<form:hidden path="connection.demand" id="connection.demand" value="${sewerageApplicationDetails.connection.demand}"/> --%>
	<input type="hidden" value="" id="removedInspectRowId" name="removedInspectRowId" />
	<input type="hidden" value="" id="removedEstimationDtlRowId" name="removedEstimationDtlRowId" />
	<input type="hidden" id="applicationNumber" value="${sewerageApplicationDetails.applicationNumber}"/>  
	
	<c:if test="${sewerageApplicationDetails.status.code =='COLLECTINSPECTIONFEE'}"> 
	 <div  data-collapsed="0">
		<div class="panel-heading">
			<div  style="color: red; font-size: 16px;" align="center">
				<spring:message  code="lbl.collect.inspectionFee"/> 
			</div>
		</div>
	</div>	
	 </c:if>
	 <c:if test="${sewerageApplicationDetails.status.code =='ESTIMATIONNOTICEGENERATED'}"> 
	 <div  data-collapsed="0">
		<div class="panel-heading">
			<div  style="color: red; font-size: 16px;" align="center">
				<spring:message  code="lbl.collect.donationFee"/> 
			</div>
		</div>
	</div>	
	 </c:if>
	<c:choose>
	<c:when test="${mode =='editOnReject'}">
				<jsp:include page="applicantdetails.jsp"></jsp:include>
				<jsp:include page="connectiondetails.jsp"></jsp:include>
				<jsp:include page="documentdetails-view.jsp"></jsp:include>
				<jsp:include page="inspectionCharges.jsp"></jsp:include>
	</c:when>
	<c:otherwise>
			<jsp:include page="commonApplicationDetails-view.jsp"></jsp:include>
		<c:choose>
			<c:when test="${mode =='edit'}">
			 		<jsp:include page="connectiondetails.jsp"></jsp:include> 
					<jsp:include page="estimationdetails.jsp"></jsp:include>
					<jsp:include page="documentdetails-view.jsp"></jsp:include> 
					<jsp:include page="inspectionCharges.jsp"></jsp:include>
			</c:when>
			<c:when test="${mode =='closetview'}">
			 		<jsp:include page="connectiondetails.jsp"></jsp:include> 
					<jsp:include page="documentdetails-view.jsp"></jsp:include> 
			</c:when>
			<c:otherwise>
				<jsp:include page="connectionDetails-view.jsp"></jsp:include> 
				<jsp:include page="documentdetails-view.jsp"></jsp:include>
				<jsp:include page="estimationdetails-view.jsp"></jsp:include>
				<jsp:include page="seweragechargesdetails.jsp"/>
			</c:otherwise>
		</c:choose>
		
		<c:if test="${sewerageApplicationDetails.status.code == 'WORKORDERGENERATED'}">
			<jsp:include page="connectionexecutiondetails-form.jsp"></jsp:include>
		</c:if>
			<jsp:include page="applicationhistory-view.jsp"></jsp:include>
	</c:otherwise>
	</c:choose>	
		<c:if test="${!isCitizenPortalUser}">
	 	<jsp:include page="../common/commonWorkflowMatrix.jsp"/>
	 	</c:if>
	 	<c:if test="${isInspectionFeePaid || isInspectionFeePaid==null}">
	 	
	 	<jsp:include page="../common/commonWorkflowMatrix-button.jsp"/>
	 	</c:if>
	 	<div class="row text-center">
       <div class="add-margin">
               <c:if test="${sewerageApplicationDetails.status.code == 'FINALAPPROVED' }">
                       <a href="javascript:void(0)" class="btn btn-default" onclick="renderWorkOrderPdf()" >Generate Work Order</a>
               </c:if>
       </div>
       
       
	</div>
	
	<div class="row text-center">
       <div class="add-margin">
       <c:if test="${isCitizenPortalUser && !isInspectionFeePaid}">
				<a href="javascript:void(0)" class="btn btn-default"
					onclick="window.open('/stms/citizen/search/sewerageGenerateonlinebill/${sewerageApplicationDetails.applicationNumber}/${sewerageApplicationDetails.connectionDetail.propertyIdentifier}','name','width=800, height=600,scrollbars=yes')">
					 Pay Fee</a>
	 	       </c:if>
	 	       </div>
	 	       </div>
       
</form:form>
<script src="<cdn:url  value='/resources/js/transactions/applicationsuccess.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/js/transactions/newconnectionupdate.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script src="<cdn:url  value='/resources/javascript/helper.js?rnd=${app_release_no}'/>"></script>    


<script type="text/javascript">
function renderWorkOrderPdf() {
    window.open("/stms/transactions/workordernotice?pathVar=${sewerageApplicationDetails.applicationNumber}", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
</script>