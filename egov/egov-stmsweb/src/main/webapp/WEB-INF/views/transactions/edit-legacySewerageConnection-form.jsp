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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div class="row">
	<div class="col-md-12"> 
		<div class="text-right error-msg" style="font-size:14px;"><spring:message code="lbl.application.date"/> : <fmt:formatDate pattern="dd/MM/yyyy" value="${sewerageApplicationDetails.applicationDate}" /></div>
		<form:form role="form" method="post" action="/stms/transactions/sewerageLegacyApplication-update" 
			modelAttribute="sewerageApplicationDetails" id="sewerageLegacyConnectionform"
			cssClass="form-horizontal form-groups-bordered"
			enctype="multipart/form-data">
			<form:hidden path="applicationType" id="applicationType" value="${sewerageApplicationDetails.applicationType.id}"/>
			<form:hidden path="connection.status" id="connection.status" value="${sewerageApplicationDetails.connection.status}"/>
			<input type="hidden" id="sewerageApplicationDetails"  name="sewerageApplicationDetails" value="${sewerageApplicationDetails.id}" />
			<input type="hidden" id="isDonationChargeCollectionRequired" value="${isDonationChargeCollectionRequired}"/>
			<form:hidden id="mode" path=""  value="${mode}"/>
			<form:hidden id="documentName" path="" value="${documentName}"/> 
			<form:hidden id="demandDetailList" path="" value="${demandDetailList}"/>
				<jsp:include page="applicantdetails.jsp"></jsp:include>
				<jsp:include page="legacydetails.jsp"></jsp:include> 
				<jsp:include page="connectiondetails.jsp"></jsp:include>
				<jsp:include page="inspectionCharges.jsp"></jsp:include>   
				<jsp:include page="documentdetails.jsp"></jsp:include>	
			<div class="text-center">
				<button type="submit" class="btn btn-primary" id = "submit"><spring:message code="lbl.submit"/></button>
			    <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
			</div>
		</form:form>
	</div>
</div> 
<script src="<cdn:url  value='/resources/js/transactions/updatelegacyconnection.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/js/transactions/documentsupload.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script src="<cdn:url  value='/resources/javascript/helper.js?rnd=${app_release_no}'/>"></script>   

