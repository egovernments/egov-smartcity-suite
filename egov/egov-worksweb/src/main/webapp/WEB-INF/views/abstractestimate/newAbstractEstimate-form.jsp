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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<form:form name="abstractEstimateForm" role="form"	modelAttribute="abstractEstimate" id="abstractEstimate"	class="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">

	<div class="new-page-header">Create Abstract Estimate</div>

	<div class="panel-heading">
		<ul class="nav nav-tabs" id="settingstab">
			<li class="active"><a data-toggle="tab" href="#estimateheader" data-tabidx=0><spring:message code="lbl.header" /></a></li>
			<li><a data-toggle="tab" href="#workdetails" data-tabidx=1><spring:message code="lbl.workdetails" /> </a></li>
			<li><a data-toggle="tab" href="#overheads" data-tabidx=1><spring:message code="tab.header.overhead" /> </a></li>
			<li><a data-toggle="tab" href="#assetandfinancials" data-tabidx=2> <spring:message	code="tab.header.assetandfinancials" />
			</a></li>
		</ul>
	</div>
	<div class="tab-content">
		<div class="tab-pane fade in active" id="estimateheader">
			<%@ include file="estimate-header.jsp"%>
			<%@ include file="estimate-multiYearEstimate.jsp"%>
			<%@ include file="uploadDocuments.jsp"%>
		</div>
		<div class="tab-pane fade" id="workdetails">
			<%@ include file="estimate-sor.jsp"%>
		</div>
		<div class="tab-pane fade" id="overheads">
			<%@ include file="estimate-overheads.jsp"%>
		</div>
		<div class="tab-pane fade" id="assetandfinancials">
			<%@ include file="estimate-asset.jsp"%>
			<%@ include file="estimate-financialdetails.jsp"%>
		</div>
	</div>

	<div class="row">
		<div class="col-sm-12 text-center">
			<button type="submit" name="submit" id="save" class="btn btn-primary"
				value="Save">
				<spring:message code="lbl.save" />
			</button>
			<button type="button" class="btn btn-default" id="button2"
				onclick="window.close();">
				<spring:message code="lbl.close" />
			</button>
		</div>
	</div>
</form:form>
<script type="text/javascript"
	src="<c:url value='/resources/js/abstractestimate.js?rnd=${app_release_no}'/>"></script>