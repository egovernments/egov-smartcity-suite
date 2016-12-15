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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<style>
      .position_alert{
        position:fixed;z-index:9999;top:85px;right:20px;background:#F2DEDE;padding:10px 20px;border-radius: 5px;
      }
</style>
<form:form id="contractorAdvanceForm" class="form-horizontal form-groups-bordered" modelAttribute="contractorAdvanceRequisition" role="form" method="post" enctype="multipart/form-data">
	<form:hidden path="id" />
	<input type="hidden" name="woeId" id="woeId" value="${woeId}" />
	<form:hidden path="workOrderEstimate" id="workOrderEstimate" value="${workOrderEstimate.id}" /> 
	<input type="hidden" id="sourcePath" value="${contractorAdvanceRequisition.egAdvanceReqMises.egBillregister.egBillregistermis.sourcePath}" />
	<input type="hidden" name="mode" id="mode" value="${mode }" />
	<div class="position_alert">
		<form:hidden path="advanceRequisitionAmount" id="advanceRequisitionAmount" value='<c:out value="${contractorAdvanceRequisition.advanceRequisitionAmount}" default="0.0" />'/>
		<spring:message	code="lbl.advance.amount" /> : &#8377 <span id="advanceAmount"><c:out value="${contractorAdvanceRequisition.advanceRequisitionAmount }" default="0.0"></c:out></span>
	</div>
	<div class="new-page-header"><spring:message code="hdr.viewcontractoradvance" /></div> 
	<jsp:include page="contractoradvance-header.jsp"/>
	<jsp:include page="contractoradvance-detailsview.jsp"/>
	<jsp:include page="../common/uploaddocuments.jsp"/>
	<c:if test="${!workflowHistory.isEmpty() }">
		<jsp:include page="../common/commonworkflowhistory-view.jsp"></jsp:include>
	</c:if>
	<div class="row">
		<div class="col-sm-12 text-center">
			<c:if test="${contractorAdvanceRequisition.status.code == 'APPROVED' }">
				<button type='submit' class='btn btn-primary' id="viewAdvanceBill" ><spring:message code='lbl.viewadvancebill' /></button>
			</c:if>
			<input type="submit" name="closeButton"	id="closeButton" value="Close" Class="btn btn-default" onclick="window.close();" />
		</div>
	</div>
</form:form>
<script src="<cdn:url value='/resources/js/contractoradvance/contractoradvance.js?rnd=${app_release_no}'/>"></script>