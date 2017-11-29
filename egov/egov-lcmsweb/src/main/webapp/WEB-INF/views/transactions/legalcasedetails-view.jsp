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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<form:form method="post" action="create"
	class="form-horizontal form-groups-bordered" modelAttribute="legalCase"
	id="legalCaseForm">
	<input type="hidden" name="mode" value="${mode}" />
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-body custom-form ">
					<c:if test="${not empty message}">
						<div class=role="alert">${message}</div>
					</c:if>
					<div class="panel-heading">
						<div class="panel-title" align="center">
							<spring:message code="title.legalCase.view" />
						</div>
					</div>
					<div class="panel-body">
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.courttype" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${legalCase.courtMaster.courtType.courtType}" />
							</div>

							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.petitiontype" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${legalCase.petitionTypeMaster.petitionType}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.court" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${legalCase.courtMaster.name}" />
							</div>
							<div class="col-xs-3 add-margin">Case Type</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${legalCase.caseTypeMaster.caseType}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.casenumber" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${legalCase.caseNumber}" />
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.lcnumber" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${legalCase.lcNumber}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.casedate" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								<fmt:formatDate pattern="dd/MM/yyyy"
									value="${legalCase.caseDate}" var="casedate" />
								<c:out value="${casedate}" />
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.previouscaseNumber" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${legalCase.appealNum}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.title" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${legalCase.caseTitle}" />
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.prayer" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${legalCase.prayer}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.case.receivingdate" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								<fmt:formatDate pattern="dd/MM/yyyy"
									value="${legalCase.caseReceivingDate}" var="casercdate" />
								<c:out value="${casercdate}" />
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.caDue.date" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								<fmt:formatDate pattern="dd/MM/yyyy"
									value="${legalCase.caDueDate}" var="caduedate" />
								<c:out value="${caduedate}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.officerincharge" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${legalCase.officerIncharge.name}" />
							</div>

							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.noticedate" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								<fmt:formatDate pattern="dd/MM/yyyy"
									value="${legalCase.noticeDate}" var="noticeDate" />
								<c:out value="${noticeDate}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.fieldbycarp" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								${legalCase.isFiledByCorporation}</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.oldreferencenumber" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								${legalCase.oldReferenceNumber}</div>
						</div>
					</div>
				</div>
			</div>
		<div class="panel-heading">
			<div class="panel-title" align="center" style="font-weight: bold">
				<spring:message code="lbl.bipartisanDetails.details" />
			</div>
		</div>
		<table class="table table-striped table-bordered" id="petitionDetails">
			<thead>
				<tr>
					<th class="text-center"><spring:message code="lbl.slno" /> <%-- <th class="text-center"><spring:message code="lbl.IsGovtDept" /></th> --%>
					<th class="text-center"><spring:message code="lbl.name" /><span
						class="mandatory"></span></th>
					<th class="text-center"><spring:message code="lbl.discription" /></th>
					<th class="text-center"><spring:message
							code="lbl.contactnumber" /></th>
					<%-- <th class="text-center"><spring:message code="lbl.Govt_Dept" /></th> --%>
				</tr>
			</thead>
			<tbody>

				<c:forEach var="bipartisanDetails"
					items="${legalCase.getPetitioners()}" varStatus="status">
					<tr>
						<td><input type="text" id="table_name${status.index}"
							class="form-control" readonly="readonly"
							style="text-align: center" value="${status.index+1}" /></td>
						<%-- <td><form:input type="hidden"
									id="bipartisanDetails[${status.index}].isRespondentGovernment"
									path="bipartisanDetails[${status.index}].isRespondentGovernment" />
									<c:choose>
					<c:when test="${bipartisanDetails.isRespondentGovernment}">
							<input type="checkbox" checked="true"
								id="table_isRespondentGovernment${status.index}"
								class="form-control" readonly="readonly"
								style="text-align: center"
								value="${bipartisanDetails.isRespondentGovernment}" /> 
								</c:when>
								<c:otherwise>
								<input type="checkbox" 
								id="table_isRespondentGovernment${status.index}"
								class="form-control" readonly="readonly"
								style="text-align: center"
								value="${bipartisanDetails.isRespondentGovernment}" /> 
								
								</c:otherwise>
								</c:choose></td> --%>
						<td><form:input type="hidden"
								id="bipartisanDetails[${status.index}].name"
								path="bipartisanDetails[${status.index}].name" /> <input
							type="text" id="table_name${status.index}" class="form-control"
							readonly="readonly" style="text-align: center"
							value="${bipartisanDetails.name}" /></td>
						<td><form:input type="hidden"
								id="bipartisanDetails[${status.index}].address"
								path="bipartisanDetails[${status.index}].address" /> <input
							type="text" id="table_address${status.index}"
							class="form-control" readonly="readonly"
							style="text-align: center" value="${bipartisanDetails.address}" />
						</td>

						<td><form:input type="hidden"
								id="bipartisanDetails[${status.index}].contactNumber"
								path="bipartisanDetails[${status.index}].contactNumber" /> <input
							type="text" id="table_contactNumber${status.index}"
							class="form-control" readonly="readonly"
							style="text-align: center"
							value="${bipartisanDetails.contactNumber}" /></td>

						<%-- <td><form:input type="hidden"
									id="bipartisanDetails[${status.index}].governmentDepartment"
									path="bipartisanDetails[${status.index}].governmentDepartment" />
								<input type="text"
								id="table_governmentDepartment${status.index}"
								class="form-control" readonly="readonly"
								style="text-align: center"
								value="${bipartisanDetails.governmentDepartment.name}" />
								</td> --%>

					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="panel-heading">
			<div class="panel-title" style="font-weight: bold" align="center">
				<spring:message code="lbl.bipartisanDetails.respondant" />
			</div>
		</div>
		<table class="table table-striped table-bordered"
			id="respondantDetails">
			<thead>
				<tr>
					<th class="text-center"><spring:message code="lbl.slno" /> <%-- <th class="text-center"><spring:message code="lbl.IsGovtDept" /></th> --%>
					<th class="text-center"><spring:message code="lbl.name" /><span
						class="mandatory"></span></th>
					<th class="text-center"><spring:message code="lbl.discription" /></th>
					<th class="text-center"><spring:message
							code="lbl.contactnumber" /></th>
					<%-- <th class="text-center"><spring:message code="lbl.Govt_Dept" /></th> --%>
				</tr>
			</thead>
			<tbody>

				<c:forEach var="bipartisanDetailsBeanList"
					items="${legalCase.getRespondents()}" varStatus="status">
					<tr>
						<td><input type="text" id="table_name${status.index}"
							class="form-control" readonly="readonly"
							style="text-align: center" value="${status.index+1}" /></td>
						<%-- <td>
							<form:input type="hidden"
									id="bipartisanDetailsBeanList[${status.index}].isRespondentGovernment"
									path="bipartisanDetailsBeanList[${status.index}].isRespondentGovernment" />
									<c:choose>
							<c:when test="${bipartisanDetailsBeanList.isRespondentGovernment}">
							<input type="checkbox" checked="true"
								id="table_isRespondentGovernment${status.index}"
								class="form-control" readonly="readonly"
								style="text-align: center"
								value="${bipartisanDetailsBeanList.isRespondentGovernment}" /> 
								</c:when>
								<c:otherwise>
								<input type="checkbox" 
								id="table_isRespondentGovernment${status.index}"
								class="form-control" readonly="readonly"
								style="text-align: center"
								value="${bipartisanDetailsBeanList.isRespondentGovernment}" /> 
								
								</c:otherwise>
								</c:choose>
								</td> --%>

						<td><form:input type="hidden"
								id="bipartisanDetailsBeanList[${status.index}].name"
								path="bipartisanDetailsBeanList[${status.index}].name" /> <input
							type="text" id="table_name${status.index}" class="form-control"
							readonly="readonly" style="text-align: center"
							value="${bipartisanDetailsBeanList.name}" /></td>

						<td><form:input type="hidden"
								id="bipartisanDetailsBeanList[${status.index}].address"
								path="bipartisanDetailsBeanList[${status.index}].address" /> <input
							type="text" id="table_address${status.index}"
							class="form-control" readonly="readonly"
							style="text-align: center"
							value="${bipartisanDetailsBeanList.address}" /></td>

						<td><form:input type="hidden"
								id="bipartisanDetailsBeanList[${status.index}].contactNumber"
								path="bipartisanDetailsBeanList[${status.index}].contactNumber" />
							<input type="text" id="table_contactNumber${status.index}"
							class="form-control" readonly="readonly"
							style="text-align: center"
							value="${bipartisanDetailsBeanList.contactNumber}" /></td>

						<%-- <td>
								<form:input type="hidden"
									id="bipartisanDetailsBeanList[${status.index}].governmentDepartment"
									path="bipartisanDetailsBeanList[${status.index}].governmentDepartment" />
								<input type="text"
								id="table_governmentDepartment${status.index}"
								class="form-control" readonly="readonly"
								style="text-align: center"
								value="${bipartisanDetailsBeanList.governmentDepartment.name}" />
								</td> --%>

					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-body">
					<div class="row add-border">
						<div class="col-md-2 col-xs-6 add-margin">
							<spring:message code="lbl.representedby" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${legalCase.representedby}" />
						</div>
						<div class="col-md-2 col-xs-6 add-margin"><spring:message
			code="lbl.standingcounselname" /></div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${legalCase.oppPartyAdvocate}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-md-2 col-xs-6 add-margin"><spring:message
			code="lbl.remarks" /></div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${legalCase.remarks}" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
		<div class="row form-group">
			<c:if test="${not empty legalCase.legalCaseAdvocates }">
				<div class="panel-heading">
					<div class="panel-title" align="center" style="font-weight: bold">
						Standing Counsel Details:</div>
				</div>

				<div class="main-content">
					<div class="row">
						<div class="col-md-12">
							<div class="panel panel-primary" data-collapsed="0">
								<div class="panel-body">
									<div class="form-group">
										<div class="row add-border">
											<div class="form-group">
												<label class="col-sm-3 control-label text-right"><spring:message code="lbl.standingcons" />:</label>
												<div class="col-sm-3 add-margin">
													<c:out
														value="${legalCase.legalCaseAdvocates[0].advocateMaster.name}" />
												</div>
												<label class="col-sm-2 control-label text-right">Assigned
													Date: </label>
												<div class="col-sm-3 add-margin">
													<fmt:formatDate pattern="dd/MM/yyyy"
														value="${legalCase.legalCaseAdvocates[0].assignedToDate}"
														var="assignDate" />
													<c:out value="${assignDate}" />
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label text-right">Date
													on which Vakalat filed:</label>
												<div class="col-sm-3 add-margin">
													<fmt:formatDate pattern="dd/MM/yyyy"
														value="${legalCase.legalCaseAdvocates[0].vakalatDate}"
														var="vakaltadate" />
													<c:out value="${vakaltadate}" />
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label text-right"
													id="persons">Is Senior Standing Counsel Required:</label>
												<div class="col-sm-3 add-margin">

													<c:choose>
														<c:when test="${legalCase.isSenioradvrequired}">
        Yes
         </c:when>
														<c:otherwise>
        No 
    </c:otherwise>
													</c:choose>
												</div>
											</div>
											<c:if test="${legalCase.isSenioradvrequired}">
												<div id="seniordov1" class="form-group">
													<label class="col-sm-3 control-label text-right">Senior
														Standing Counsel: </label>
													<div class="col-sm-3 add-margin">
														<c:out
															value="${legalCase.legalCaseAdvocates[0].seniorAdvocate.name}" />

													</div>
													<div class="form-group" id="seniordov3">
														<label class="col-sm-2 control-label text-right">Assigned
															On:</label>
														<div class="col-sm-3 add-margin">

															<fmt:formatDate pattern="dd/MM/yyyy"
																value="${legalCase.legalCaseAdvocates[0].assignedToDateForSenior}"
																var="assigntoDate" />
															<c:out value="${assigntoDate}" />
														</div>
													</div>
												</div>
												<div id="seniordov2" class="form-group">
													<label class="col-sm-3 control-label text-right">Order
														Date: </label>
													<div class="col-sm-3 add-margin">

														<fmt:formatDate pattern="dd/MM/yyyy"
															value="${legalCase.legalCaseAdvocates[0].orderDate}"
															var="assignDate" />
														<c:out value="${assignDate}" />
													</div>
													<label class="col-sm-2 control-label text-right">Order
														Number: </label>
													<div class="col-sm-3 add-margin">
														<c:out
															value="${legalCase.legalCaseAdvocates[0].orderNumber}" />
													</div>
											</c:if>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</c:if>
		</div>
		<jsp:include page="documentdetails-view.jsp"></jsp:include>
	<div class="form-group">
			<div class="text-center">
				<a href="javascript:void(0)" class="btn btn-default"
					onclick="self.close()"><spring:message code="lbl.close" /></a>
			</div>
		</div>
	</div>
	</div>
</form:form>