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
  <div class="position_alert">
			<spring:message	code="lbl.estimate.value" /> : &#8377 <span id="estimateValueTotal"><c:out value="${estimateValue}" default="0.0"></c:out></span>
		</div>
		<div>
		       <spring:hasBindErrors name="abstractEstimate">
				    <div class="col-md-10 col-md-offset-1">
						<form:errors path="*" cssClass="error-msg add-margin" /><br/>
				   </div>
	          </spring:hasBindErrors>
		</div>
		<c:if test="${lineEstimateRequired == true || mode != null }">
			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading"></div>
						<div class="panel-body">
							<div class="row add-border">
								<div class="col-md-2 col-xs-6 add-margin"><spring:message code="lbl.estimateno" /> : </div> 
								<c:choose>
									<c:when test="${abstractEstimate.lineEstimateDetails != null}">
										<div class="col-md-2 col-xs-6 add-margin view-content">
											<c:out value="${abstractEstimate.lineEstimateDetails.estimateNumber}"></c:out>
										</div>
									</c:when>
									<c:otherwise>
										<div class="col-md-2 col-xs-6 add-margin view-content">
											<c:out value="${abstractEstimate.estimateNumber}"></c:out>
										</div>
									</c:otherwise>
								</c:choose>
								
								<c:if test="${abstractEstimate.lineEstimateDetails != null}">
									<div class="col-md-2 col-xs-6 add-margin"><spring:message code="lbl.lineestimateno" /> : </div> 
									<div class="col-md-2 col-xs-6 add-margin view-content">
										<a href='javascript:void(0)' onclick="viewLineEstimate('<c:out value="${abstractEstimate.lineEstimateDetails.lineEstimate.id}"/>')">
										<c:out value="${abstractEstimate.lineEstimateDetails.lineEstimate.lineEstimateNumber}"/></a>
									</div>
								</c:if>
								<c:if test="${abstractEstimate.projectCode != null}">
									<div class="col-md-2 col-xs-6 add-margin"><spring:message code="lbl.workidentificationo" /> : </div>
									<div class="col-md-2 col-xs-6 add-margin view-content">${abstractEstimate.projectCode.code}</div>
								</c:if>
							</div>
							
							<c:if test="${lineEstimateRequired == true }">
								<div class="row add-border">
									<div class="col-md-2 col-xs-6 add-margin"><spring:message code="lbl.adminsanctionestimatamount" /> : </div> 
									<div class="col-md-2 col-xs-6 add-margin view-content">&#8377 <fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
									minFractionDigits="2" value="${abstractEstimate.lineEstimateDetails.estimateAmount}" /></div>
								</div>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</c:if>