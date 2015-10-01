<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<form:form  id="waterConnectionSuccess" method ="get" class="form-horizontal form-groups-bordered" modelAttribute="waterConnectionDetails" >				
<div class="page-container" id="page-container">
	<input type="hidden" id="mode" name="hidden" value="${mode}"/>
	<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message  code="lbl.basicdetails"/>
				</div>
			</div>
			<input id="applicationCode" type="hidden" value="<c:out value="${waterConnectionDetails.applicationNumber}" />" />  						
				<jsp:include page="commonappdetails-view.jsp"></jsp:include>
		</div>
	<jsp:include page="connectiondetails-view.jsp"></jsp:include>
</div>			
<div class="row text-center">
	<div class="add-margin">
	<c:choose>
	<c:when test="${waterConnectionDetails.legacy}">
	<input type="button" class="btn btn-primary" id="viewWorkOrder" value="Edit" onclick="showEdit('<c:out value="${waterConnectionDetails.connection.consumerCode}" />')"/>
	</c:when>
	<c:otherwise>
	<c:if test="${waterConnectionDetails.status.code == 'ESTIMATIONNOTICEGENERATED' && checkOperator }">
		<button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.collect.fees"/></button>
	</c:if>
	<c:if test="${(waterConnectionDetails.demand.baseDemand-waterConnectionDetails.demand.amtCollected)>0 && checkOperator }">
		<button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.pay.tax"/></button>
	</c:if>
	<c:if test="${waterConnectionDetails.status.code != 'CREATED' && waterConnectionDetails.status.code != 'VERIFIED' 
	&& waterConnectionDetails.status.code != 'CANCELLED' && waterConnectionDetails.fieldInspectionDetails != null && !legacy && waterConnectionDetails.applicationType.code !='CLOSINGCONNECTION'}">
		<button type="submit" class="btn btn-primary" id="viewEstimationNotice"><spring:message code="lbl.printestimationnotice"/></button>
	</c:if>
	<c:if test="${!legacy&& (waterConnectionDetails.status.code == 'SANCTIONED' || waterConnectionDetails.status.code == 'WORKORDERGENERATED')}">
		<button type="submit" class="btn btn-primary" id="viewWorkOrder"><spring:message code="lb.printworkorder"/></button>
	</c:if>
	</c:otherwise>
	</c:choose>
		<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
	</div>
</div>
</form:form>
<script>
function showEdit(obj)
{
window.location="/wtms/application/newConnection-editExisting/"+obj;
}
</script>

<script src="<c:url value='/resources/js/app/applicationsuccess.js'/>"></script>