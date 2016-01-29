<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
	The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--  <form role="form" class="form-horizontal form-groups-bordered"> --%>
<form:form method="post" action=""
	class="form-horizontal form-groups-bordered"
	modelAttribute="waterPropertyUsage" id="usageTypeMasterform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading"></div>
		<div class="panel-body custom-form">
			<div class="form-group">
				<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.propertytype" /><span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="propertyType"
						data-first-option="false" id="propertyType"
						cssClass="form-control" required="required">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${propertyType}" itemValue="id"
							itemLabel="name" />
					</form:select>
					<form:errors path="propertyType"
						cssClass="add-margin error-msg" />
				</div>
				<label class="col-sm-3 control-label text-right"><spring:message code="lbl.usagetype" /><span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin"  id="usagediv">
					<form:input class="form-control patternvalidation"  data-pattern="alphabetwithspace"  maxlength="24" id="usageTypeName"
						path="usageType.name" required="required" />
						
					<form:errors path="usageType.name"
						cssClass="add-margin error-msg" />
				</div>

			</div>

			<div class="form-group text-center">
			<button type="submit" class="btn btn-primary" id="buttonid"><spring:message code="lbl.submit"/></button>
				<%-- <form:button type="submit" class="btn btn-primary" id="buttonid">
					<spring:message code="lbl.submit" />
				</form:button> --%>
				<a onclick="self.close()" class="btn btn-default"
					href="javascript:void(0)"><spring:message code="lbl.close" /></a>
			</div>
		</div>
	</div>
</form:form>
<link rel="stylesheet" href="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
                <script src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	            type="text/javascript"></script>
                <script src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	            type="text/javascript"></script>
                <script src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	            type="text/javascript"></script>
	            <script src="<c:url value='/resources/js/app/usage-type-master.js?rnd=${app_release_no}'/>"></script>

