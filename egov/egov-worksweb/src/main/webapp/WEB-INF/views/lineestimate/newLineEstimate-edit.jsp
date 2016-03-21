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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div class="page-container" id="page-container">
	<div class="main-content">
		<div style="font-weight:bold; color:green; text-align:center;">
			<c:choose>
				<c:when test="${message.equals('create')}">
					<spring:message code="lineestimate.create.success" arguments="${lineEstimate.getLineEstimateNumber()}"/>
				</c:when>
				<c:otherwise>
					<spring:message code="lineestimate.update.success" arguments="${lineEstimate.getLineEstimateNumber()}"/>
				</c:otherwise>
			</c:choose>
		</div>
		<form:form name="lineEstimateForm" action="" role="form" modelAttribute="lineEstimate" id="lineEstimate" class="form-horizontal form-groups-bordered" method="POST" enctype="multipart/form-data">
			<form:hidden path="" name="removedLineEstimateDetailsIds" id="removedLineEstimateDetailsIds" value="" class="form-control table-input hidden-input"/>
			<form:hidden path="" name="lineEstimateId" value="${lineEstimate.id}" class="form-control table-input hidden-input"/>
			<input type="hidden" value="${mode}" id="mode"/>
			<div class="row">
				<div class="col-md-12">
					<jsp:include page="lineEstimateHeader.jsp"/>
					<jsp:include page="lineEstimateDetails.jsp"/>
					<jsp:include page="uploadDocuments.jsp"/>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 text-center">
					<form:button type="submit" name="submit" class="btn btn-primary" value="Submit"><spring:message code="lbl.submit"/></form:button>
					<form:button type="button" class="btn btn-default" id="button2" onclick="window.close();"><spring:message code="lbl.close"/></form:button>
				</div>
			</div>
		</form:form>  
	</div>
</div>
<script src="<c:url value='/resources/js/lineestimate.js'/>"></script>
