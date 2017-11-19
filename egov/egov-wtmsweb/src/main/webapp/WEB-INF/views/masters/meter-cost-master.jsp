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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<form:form method="post" action="" class="form-horizontal form-groups-bordered" modelAttribute="meterCost" id="meterCostform"
	cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading"></div>
			<div class="panel-body custom-form">
			<c:if test="${not empty message}">
                   <div role="alert">${message}</div>
                </c:if>
                <spring:hasBindErrors name="meterCost">
        		<%-- <form:errors path="pipeSize" cssClass="add-margin error-msg" /> --%>
          		<form:errors path="meterMake" cssClass="add-margin error-msg" /> 
         </spring:hasBindErrors>
				<div class="form-group">
					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.hscpipesize" />:<span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="pipeSize" data-first-option="false" id="pipeSize" cssClass="form-control" required="required">
						<form:option value=""><spring:message code="lbl.select" /></form:option>
						<%-- <form:options items="${pipeSize}" itemValue="id" itemLabel="name" /> --%>
						<c:forEach var="pipeSize" items="${pipeSize}">
						<form:option value="${pipeSize}"><c:out value="${pipeSize.code}"/></form:option>  
						</c:forEach>
					</form:select>
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.makeofthemeter" />:<span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin" id=meterMark>
					<form:input class="form-control patternvalidation"  data-pattern="alphanumericwithspecialcharacters"  maxlength="50" id="meterMake" path="meterMake" required="required" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.metercost" />:<span class="mandatory"></span></label>
					<div class="col-sm-3 add-margin"  id="amount">
						<form:input cssClass="form-control patternvalidation" data-pattern="decimalvalue"  pattern="\d{0,5}(\.\d{1,2})?" title="It will allow upto 5 digits and 2 decimal points"   data-first-option="false&true"
							maxlength="8"  id="amountid" path="amount" required="required" />
							 <form:errors path="amount" cssClass="add-margin error-msg" />
					</div>
				</div>
			<div class="form-group">
			<div class="form-group" id="statusdiv">
				<label class="col-sm-3 control-label text-right"><spring:message code="lbl.active"/></label>
					<div class="col-sm-3 add-margin" >
						<form:checkbox id="activeid" path="active" value ="active" />
						<form:errors path="active" />
					</div>
			</div>	
		</div>
			<input type="hidden" name="meterCost" value="${meterCost.id}" />
				<form:hidden id="reqAttr" path="" value="${reqAttr}"/>
<input type="hidden" value="${mode}" id="mode" />
			<div class="form-group text-center">
				<button type="submit" class="btn btn-primary" value="Save" id="buttonid"><spring:message code="lbl.save.button"/></button>
				<button type="button" class="btn btn-default" value="Reset" id="resetid" ><spring:message code="lbl.reset"/></button>
				<a onclick="self.close()" class="btn btn-default" href="javascript:void(0)"><spring:message code="lbl.close" /></a>
			</div>
		
	</div>
</div>
</form:form>
<link rel="stylesheet" href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
                <script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	            type="text/javascript"></script>
                <script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	            type="text/javascript"></script>
                <script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	            type="text/javascript"></script>
	            <script src="<cdn:url value='/resources/js/app/meter-cost-master.js?rnd=${app_release_no}'/>"></script>