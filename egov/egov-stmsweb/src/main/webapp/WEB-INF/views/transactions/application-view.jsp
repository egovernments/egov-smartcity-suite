<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>


<form:form  id="sewerageConnectionSuccess" method ="get" class="form-horizontal form-groups-bordered" modelAttribute="sewerageApplicationDetails" >				
<div class="page-container" id="page-container">
	<input type="hidden" id="mode" name="hidden" value="${mode}"/>
	<input type="hidden" id="applicationTypeCode"  value="<c:out value="${sewerageApplicationDetails.applicationType.code}" />" />
	<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message  code="lbl.basicdetails"/>
				</div>
			</div>
			<input type="hidden" id="sewerageTaxDueforParent" value="${sewerageTaxDueforParent}" name="sewerageTaxDueforParent"/>
				<input type="hidden"  id="citizenRole" value="${citizenRole}" />
				<input type="hidden"  id="cscUserRole" value="${cscUserRole}" />
				<input type="hidden"  id="checkOperator" value="${checkOperator}" />
				<input type="hidden"  id="ulbUserRole" value="${ulbUserRole}" />
				
			<input id="applicationCode" type="hidden" value="<c:out value="${sewerageApplicationDetails.applicationNumber}" />" />  
			<input id="consumerCode" type="hidden" value="<c:out value="${sewerageApplicationDetails.connection.shscNumber}" />" /> 						
				<jsp:include page="commonApplicationDetails-view.jsp"></jsp:include>
		</div>
	<jsp:include page="connectionDetails-view.jsp"></jsp:include>
</div>			
<div class="row text-center">
	<div class="add-margin">
	<c:if test="${sewerageApplicationDetails.status.code == 'ESTIMATIONNOTICEGENERATED' && sewerageApplicationDetails.status.code != 'VERIFIED'  && (checkOperator ) }">
		<button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.collect.fees"/></button>
	</c:if>
	
	<c:if test="${sewerageApplicationDetails.status.code == 'ESTIMATIONNOTICEGENERATED' && ( citizenRole && !checkOperator) }">
		<button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.pay.online"/></button>
	</c:if>
	<c:if test="${sewerageApplicationDetails.status.code == 'SANCTIONED' && checkOperator && sewerageTaxDueforParent > 0}">
		<button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.pay.tax"/></button>
	</c:if>
	<c:if test="${sewerageApplicationDetails.status.code != 'ESTIMATIONNOTICEGENERATED' && sewerageTaxDueforParent > 0 && (citizenRole && !checkOperator) }">
		<button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.pay.online"/></button>
	</c:if>
	<c:if test="${sewerageApplicationDetails.status.code != 'CREATED' && sewerageApplicationDetails.status.code != 'VERIFIED' && sewerageApplicationDetails.status.code != 'APPROVED'
	&& sewerageApplicationDetails.status.code != 'CANCELLED' && sewerageApplicationDetails.fieldInspections.fieldInspectionDetails != null && !citizenRole}">
		<button type="submit" class="btn btn-primary" id="viewEstimationNotice"><spring:message code="lbl.printestimationnotice"/></button>
	</c:if>
	<c:if test="${!citizenRole && (sewerageApplicationDetails.status.code == 'APPROVED' || sewerageApplicationDetails.status.code == 'WORKORDERGENERATED')}">
		<button type="submit" class="btn btn-primary" id="viewWorkOrder"><spring:message code="lb.printworkorder"/></button>
	</c:if>
	
		<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
	</div>
</div>
</form:form>

<script src="<cdn:url  value='/resources/js/transactions/applicationsuccess.js?rnd=${app_release_no}'/>"></script>