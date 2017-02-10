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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0"
			style="text-align: left">
			<div class="panel-title" style="text-align:center;">
				<c:if test="${mode == 'view' }">
					<div class="panel-heading">
						<div class="panel-title" style="text-align:center;" ><spring:message code="lbl.viewcontractorclass" /></div>
					</div>
				</c:if>
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.contractorclass" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${contractorGrade.grade}" />
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.description" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:choose>
							<c:when test = "${empty contractorGrade.description}">
								<c:out value="NA" />
							</c:when>
							<c:otherwise>
								<c:out value="${contractorGrade.description}" />
							</c:otherwise>
						</c:choose>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.contractorclass.minamount" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<fmt:formatNumber groupingUsed="false"
								maxFractionDigits="2" minFractionDigits="2"
								value="${contractorGrade.minAmount}" />
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.contractorclass.minamount" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<fmt:formatNumber groupingUsed="false"
								maxFractionDigits="2" minFractionDigits="2"
								value="${contractorGrade.maxAmount}" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>