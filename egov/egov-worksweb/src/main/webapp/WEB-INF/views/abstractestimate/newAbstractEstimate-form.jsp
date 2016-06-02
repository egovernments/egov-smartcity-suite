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
<style>
      .position_alert{
        position:fixed;z-index:9999;top:85px;right:20px;background:#F2DEDE;padding:10px 20px;border-radius: 5px;
      }
    </style>
<form:form name="abstractEstimateForm" role="form"
	modelAttribute="abstractEstimate" id="abstractEstimate"
	class="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">

	<div class="new-page-header">Create Abstract Estimate</div>

	<div class="main-content">
		<div class="position_alert">
			Estimate Value : &#8377 <span>56321.05</span>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading"></div>
					<div class="panel-body">
						<div class="row add-border">
							<div class="col-md-2 col-xs-6 add-margin">Estimate No.</div>
							<div class="col-md-2 col-xs-6 add-margin view-content">
							<c:out value="${lineEstimateDetails.estimateNumber}"></c:out>
							</div>
								
							<form:hidden path="estimateNumber" name="estimateNumber" value="${lineEstimateDetails.estimateNumber}"/>
							<div class="col-md-2 col-xs-6 add-margin">Line Estimate No.
							</div>
							<div class="col-md-2 col-xs-6 add-margin view-content">
								<a href='javascript:void(0)' onclick="viewLineEstimate('<c:out value="${lineEstimate.id}"/>')"><c:out value="${lineEstimate.lineEstimateNumber}"/></a>
							</div>
							<div class="col-md-2 col-xs-6 add-margin">Work Identification No.</div>
							<div class="col-md-2 col-xs-6 add-margin view-content">${abstractEstimate.projectCode.code}</div>
							<form:hidden path="" name="code" id="code" value="${abstractEstimate.projectCode.code}"/>
						</div>
						<div class="row add-border">
						<c:if test="workOrder != null && workOrder.id != null">
							<div class="col-md-2 col-xs-6 add-margin">LOA No.</div>
							<div class="col-md-2 col-xs-6 add-margin view-content">
								<a href="javascript:void(0)" onclick='viewLOA(<c:out value="%{workOrder.id"/>)'><c:out value="%{workOrder.workOrderNumber}"/></a>
							</div>
						</c:if>
							<div class="col-md-2 col-xs-6 add-margin">Payments
								Released.</div>
							<div class="col-md-2 col-xs-6 add-margin view-content">
								&#8377 20000.00</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="panel-heading">
			<ul class="nav nav-tabs" id="settingstab">
				<li class="active"><a data-toggle="tab" href="#estimateheader"
					data-tabidx=0><spring:message code="lbl.header" /></a></li>
				<li><a data-toggle="tab" href="#workdetails" data-tabidx=1><spring:message
							code="tab.header.scheduleA" /> </a></li>
				<li><a data-toggle="tab" href="#overheads" data-tabidx=1><spring:message
							code="tab.header.scheduleA" /> </a></li>
				<li><a data-toggle="tab" href="#assetandfinancials"
					data-tabidx=2> <spring:message
							code="tab.header.assetandfinancials" />
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
			  <%@ include file="estimate-nonsor.jsp"%>
			</div>
			<div class="tab-pane fade" id="overheads">
				<%@ include file="estimate-overheads.jsp"%>
			</div>
			<div class="tab-pane fade" id="assetandfinancials">
				<%@ include file="estimate-financialdetails.jsp"%>
				<%@ include file="estimate-asset.jsp"%>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12 text-center">
				<button type="submit" name="submit" id="save"
					class="btn btn-primary" onclick="return enableFileds();"
					value="Save">
					<spring:message code="lbl.save" />
				</button>
				<button type="button" class="btn btn-default" id="button2"
					onclick="window.close();">
					<spring:message code="lbl.close" />
				</button>
			</div>
		</div>
	</div>
</form:form>

<script type="text/javascript"
	src="<c:url value='/resources/js/abstractestimate.js?rnd=${app_release_no}'/>"></script>