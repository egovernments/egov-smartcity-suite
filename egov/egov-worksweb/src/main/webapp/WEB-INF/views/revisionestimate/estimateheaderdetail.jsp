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

  <div class="position_alert">
			<spring:message	code="lbl.re.value" /> : &#8377 <span id="estimateValueTotal"><c:out value="${estimateValue}" default="0.0"></c:out></span>
		</div>
		<div>
		       <spring:hasBindErrors name="revisionEstimate">
				    <div class="col-md-10 col-md-offset-1">
						<form:errors path="*" cssClass="error-msg add-margin" /><br/>
				   </div>
	          </spring:hasBindErrors>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading"></div>
					<div class="panel-body">
						<div class="row add-border">
							<div class="col-md-2 col-xs-6 add-margin"><spring:message code="lbl.estimateno" /> : </div> 
							<div class="col-md-2 col-xs-6 add-margin view-content">
								<a href='javascript:void(0)' onclick="viewEstimate('<c:out value="${revisionEstimate.parent.id}"/>')">
									<c:out value="${revisionEstimate.parent.estimateNumber}"></c:out>
								</a>
							</div>
							<input type="hidden" name="mode" value="${mode}" id="mode"/>
							<c:if test="${revisionEstimate.parent.lineEstimateDetails != null && lineEstimateRequired == true}">
							<div class="col-md-2 col-xs-6 add-margin"><spring:message code="lbl.lineestimateno" /> :
							</div> 
							<div class="col-md-2 col-xs-6 add-margin view-content">
								<a href='javascript:void(0)' onclick="viewLineEstimate('<c:out value="${revisionEstimate.parent.lineEstimateDetails.lineEstimate.id}"/>')">
								<c:out value="${revisionEstimate.parent.lineEstimateDetails.lineEstimate.lineEstimateNumber}"/></a>
							</div>
							</c:if>
							<div class="col-md-2 col-xs-6 add-margin"><spring:message code="lbl.workidentificationo" /> : </div>
							<div class="col-md-2 col-xs-6 add-margin view-content">${revisionEstimate.parent.projectCode.code}</div>
							<form:hidden path="" name="code" id="code" value="${revisionEstimate.parent.projectCode.code}"/>
						</div>
						
						<div class="row add-border">
						<c:if test="${revisionEstimate.parent.lineEstimateDetails != null && lineEstimateRequired}">
							<div class="col-md-2 col-xs-6 add-margin"><spring:message code="lbl.adminsanctionestimatamount" /> : </div> 
							<div class="col-md-2 col-xs-6 add-margin view-content">&#8377 <fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
							minFractionDigits="2" value="${revisionEstimate.parent.lineEstimateDetails.estimateAmount}" /></div>
							</c:if>
							<div class="col-md-2 col-xs-6 add-margin"><spring:message code="lbl.loano" /> : </div>
							<div class="col-md-2 col-xs-6 add-margin view-content">
								<a href="javascript:void(0)" onclick='viewLOA(<c:out value="${workOrderEstimate.workOrder.id }"/>)'>
									<c:out value="${workOrderEstimate.workOrder.workOrderNumber}"></c:out>
								</a>
							</div>
							<c:if test="${revisionEstimate.estimateNumber != null}">
								<div class="col-md-2 col-xs-6 add-margin"><spring:message code="lbl.revision.estimate.number" /> : </div> 
								<div class="col-md-2 col-xs-6 add-margin view-content">
									<c:out value="${revisionEstimate.estimateNumber}"></c:out>
								</div>
							</c:if>
						</div>
						
						<c:if test="${previousEstimates.size() != 0 }">
							<div class="row add-border">
								<div class="col-md-2 add-margin"><spring:message code="lbl.old.res" /> : </div> 
								<div class="add-margin view-content">
									<c:forEach var="estimate" items="${previousEstimates}" varStatus="item">
										<c:if test="${item.index <= 4 }">
											<a href='javascript:void(0)' onclick="viewRevisionEstimate('<c:out value="${estimate.key}"/>')">
											<c:out value="${estimate.value}"/></a>&nbsp
										</c:if>
									</c:forEach>
									<c:if test="${previousEstimates.size() > 5 }">
										<span id="renumbers" class="display-hide">
											<c:forEach var="estimate" items="${previousEstimates}" varStatus="item">
												<c:if test="${item.index > 4 }">
													<a href='javascript:void(0)' onclick="viewRevisionEstimate('<c:out value="${estimate.key}"/>')">
													<c:out value="${estimate.value}"/></a>&nbsp
												</c:if>
											</c:forEach>
										</span>
										<a href="javascript:void(0);" id="expandre"
										class="btn btn-secondary"><spring:message code='lbl.more' />..</a>
									</c:if>
								</div>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div>