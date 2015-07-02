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
	<header class="navbar navbar-fixed-top"><!-- set fixed position by adding class "navbar-fixed-top" -->
		<nav class="navbar navbar-default navbar-custom navbar-fixed-top">
			<div class="container-fluid">
				<div class="navbar-header col-md-10 col-xs-10">
					<a class="navbar-brand" href="javascript:void(0);">
						<img src="<c:url value='/resources/global/images/rmclogo.jpg' context='/egi'/>" height="60">
						<div>
							<span class="title2"><spring:message  code="title.viewconnection"/></span>
						</div>	
					</a>
				</div>
				
				<div class="nav-right-menu col-md-2 col-xs-2">
					<ul class="hr-menu text-right">
						<li class="ico-menu">
							<a href="javascript:void(0);">
								<img src="<c:url value='/resources/global/images/logo@2x.png' context='/egi'/>">
							</a>
						</li>
						
					</ul>
				</div>
				
			</div>
		</nav>
				
	</header>
	<input type="hidden" id="mode" name="hidden" value="${mode}"/>
	<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message  code="lbl.basicdetails"/>
				</div>
			</div>
									
				<jsp:include page="commonappdetails-view.jsp"></jsp:include>
		</div>
	<jsp:include page="connectiondetails-view.jsp"></jsp:include>
	<c:if test="${null!=mode && mode!='search' }">
		<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message  code="lbl.apphistory"/>
					</div>
				</div>
					<jsp:include page="applicationhistory-view.jsp"></jsp:include>
			</div>
	</c:if>
</div>			
</form:form>
<div class="row text-center">
	<div class="add-margin">
		<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
	</div>
</div>
<script  type="text/javascript"  src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"  src="<c:url value='/resources/global/js/egov/custom.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/js/app/applicationsuccess.js'/>"></script>