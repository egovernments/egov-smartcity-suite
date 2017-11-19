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

<form:form method="post" action="" class="form-horizontal form-groups-bordered" modelAttribute="usageSlab" id="usageSlabMasterform"
	cssClass="form-horizontal form-groups-bordered">
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<c:if test="${not empty message}">
				<div role="alert">${message}</div>
			</c:if>
		</div>
		<div class="panel-body custom-form">
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.usagetype" />:<span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="usage" data-first-option="false" id="usage" cssClass="form-control" onfocusout="setSlabName();" required="required">
						<form:option value=""><spring:message code="lbl.select" /></form:option>
						<form:options items="${usageType}" itemValue="name" itemLabel="name" />
					</form:select>
					<form:errors path="usage" cssClass="add-margin error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.slabname"/><span class="mandatory"/></label>
				<div class="col-sm-3 add-margin">
					<form:input path="slabName" class="form-control patternvalidation" id="slabName" maxlength="100" readonly="true"/>
					<form:errors path="slabName" cssClass="add-margin error-msg"/>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.fromvolume"/><span class="mandatory"/></label>
				<div class="col-sm-3 add-margin">
					<form:input path="fromVolume" id="fromVolume" class="form-control patternvalidation" maxlength="100" data-pattern="decimalvalue" onblur="setSlabName();" required="required"/>
					<form:errors path="fromVolume" cssClass="add-margin error-msg"/>
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.tovolume"/></label>
				<div class="col-sm-3 add-margin">
					<form:input path="toVolume" id="toVolume" class="form-control patternvalidation" maxlength="100" data-pattern="decimalvalue" onfocusout="setSlabName();" />
					<form:errors path="toVolume" cssClass="add-margin error-msg"/>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label text-right"><spring:message code="lbl.active"></spring:message></label>
				<div class="col-sm-3 add-margin">
					<form:checkbox path="active" id="isActive" checked="checked"/>
					<form:errors path="active" cssClass="add-margin error-msg"/>
				</div>
			</div>
		
			<div class="row">
				<div class="text-center">
					<button type="submit" class="btn btn-primary" id="save"><spring:message code="lbl.save"/></button>
					<button type="reset" class="btn btn-default" id="reset"><spring:message code="lbl.reset"/></button>
					<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()" ><spring:message code="lbl.close"/></a>
				</div>
			</div>
		</div>
	</div>
</form:form>
<script src="<cdn:url value='/resources/js/app/usage-slab-master.js?rnd=${app_release_no}'/>"></script>
<link rel="stylesheet" href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>" type="text/javascript"></script>
