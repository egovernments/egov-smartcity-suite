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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div class="row">
	<div class="col-md-12">
		<form:form class="form-horizontal form-groups-bordered" method="post"
			name="recoveryNoticesForm" id="recoveryNoticesForm" modelAttribute="noticeRequest">
			<div>
				<spring:hasBindErrors name="noticeRequest">  
				 		<div class="alert alert-danger">
				  			<form:errors path="*" cssClass="error-msg add-margin" /><br/>
				      	</div>
				</spring:hasBindErrors>
			</div>
			<div class="panel panel-primary" data-collapsed="0"
				style="text-align: left">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.title.recovery.notices" />
					</div>
				</div>
				<div class="panel-body">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.noticetype"/><span class="mandatory"></span>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<form:select path="noticeType" id="noticeType" cssClass="form-control">
								<form:option value="-1">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${noticeTypes}" />
							</form:select>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.revenue.ward"/>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<form:select path="ward" id="ward" cssClass="form-control">
								<form:option value="-1">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${wards}" itemLabel="name" itemValue="id"/>
							</form:select>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.revenue.block"/>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<form:select path="block" id="block" cssClass="form-control">
								<form:option value="-1">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${blocks}" itemLabel="name" itemValue="id"/>
							</form:select>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.billcollector"/>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<form:select path="billCollector" id="billCollector" cssClass="form-control">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${billCollectors}" />
							</form:select>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.category.ownership"/>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<form:select path="propertyType" id="propertyType" cssClass="form-control">
								<form:option value="-1">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${propertyTypes}" itemLabel="type" itemValue="id"/>
							</form:select>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.propertytype"/>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<form:select path="categoryType" id="categoryType" cssClass="form-control">
								<form:option value="-1">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${categoryTypes}" />
							</form:select>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.assmtno"/>
						</div>
						<div class="col-xs-3 add-margin view-content">
							<form:input path="propertyId" id="propertyId" cssClass="form-control"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="button" id="btngenerate" class="btn btn-primary">
						<spring:message code="lbl.generate" />
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal"
						onclick="window.close();">
						<spring:message code="lbl.close" />
					</button>
				</div>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/recoveryNotices.js?rnd=${app_release_no}'/>"></script>