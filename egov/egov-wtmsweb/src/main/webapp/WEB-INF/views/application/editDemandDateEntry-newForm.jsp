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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">

			<form:form method="post"
				modelAttribute="waterConnectionDetails"
				id="editDemandWaterConnectionform"
				cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
				<div class="page-container" id="page-container">
					<input type="hidden" id="consumerCode" name="consumerCode"
						value="${waterConnectionDetails.connection.consumerCode}" />
						<input type="hidden" id="current2HalfInstallment" name="current2HalfInstallment"
						value="${current2HalfInstallment}" />
						<input type="hidden" id="current1HalfInstallment" name="current1HalfInstallment"
						value="${current1HalfInstallment}" />
						<input type="hidden" id="connectionTypeValue" value="${waterConnectionDetails.connectionType}">
					<form:hidden path="id" />
					<div class="panel-heading">
						<div class="panel-title">
							<spring:message code="lbl.basicdetails" />
						</div>
					</div>
					<jsp:include page="commonappdetails-view.jsp" />
					<div class="col-md-12">
					
						<table class="table table-bordered"    id="dcbOnlinePaymentTable"  >
									<tr>
										<th class="bluebgheadtd" width="2%">
											Installment
										</th>
										<th class="bluebgheadtd" width="3%">
											Tax
										</th>
										<th class="bluebgheadtd" width="3%">
											Demand
										</th>
										<th class="bluebgheadtd" width="2%">
											Collection
										</th>
										</tr>
										<c:choose>
										<c:when test="${!demandDetailBeanList.isEmpty()}">
											<c:forEach items="${demandDetailBeanList}" var="var1"
												varStatus="counter">
												<tr id="Floorinfo" class="item">
												
												<td class="blueborderfortd"><form:input type="text" path=""
														class="form-control read-only" value="${var1.installment}"
														name="demandDetailBeanList[${counter.index}].installment"
														id="installment"
														required="required" 
														/>
													</td>
											<td class="blueborderfortd">
												<form:input type="hidden" path="" 
														class="form-control low-width" value="${var1.reasonMaster}"
														name="demandDetailBeanList[${counter.index}].reasonMaster"
														id="demandDetailBeanList[${counter.index}].reasonMaster"
														
														/>
														
												<form:input type="text" path="" 
														class="form-control read-only" value="${var1.reasonMasterDesc}"
														name="demandDetailBeanList[${counter.index}].reasonMasterDesc"
														id="demandDetailBeanList[${counter.index}].reasonMasterDesc"
														required="required" 
														/>
														
												</td>
													<td class="blueborderfortd"><form:input type="text" path=""
														class="form-control is_valid_number" value="${var1.actualAmount}"
														name="demandDetailBeanList[${counter.index}].actualAmount"
														data-old-value="${var1.actualAmount}"
														id="actualAmount"
														maxlength="7"
														required="required"  onblur="return calculateAmount(this);"
														/></td>
														<td class="blueborderfortd">
														
														<c:choose>
														  <c:when test="${demandDetailBeanList[counter.index].actualAmount gt 0}">
														    <form:input type="text" path=""
															class="form-control read-only" value="${var1.actualCollection}"
															name="demandDetailBeanList[${counter.index}].actualCollection"
															id="actualCollection"  maxlength="7"  onblur=" return calculateCollectionAmount(this)"

															/>
														  </c:when>
														  
														  <c:otherwise>
														    
														    <form:input type="text" path=""
															class="form-control is_valid_number" value="${var1.actualCollection}"
															name="demandDetailBeanList[${counter.index}].actualCollection"
															id="actualCollection"  maxlength="7"  onblur=" return calculateCollectionAmount(this)"
															required="required" 
															/>
															
														  </c:otherwise>
														</c:choose>
														
														
														
														<form:input type="hidden" path="" 
														class="form-control low-width" value="${var1.id}"
														name="demandDetailBeanList[${counter.index}].id"
														id="demandDetailBeanList[${counter.index}].id"
														
														/>
													
														</td>
												</tr>
											</c:forEach>
										</c:when>
									</c:choose>
									
									</table>
									
									</div>
					</div>
						<div class="row">
						<div class="text-center">
							<button type="submit" class="btn btn-primary" id="submitButtonId">
								<spring:message code="lbl.submit" />
							</button>
							<a href="javascript:void(0);" class="btn btn-primary"
								onclick="self.close()"> <spring:message code='lbl.close' />
							</a>
						</div>
					</div>
					
					</form:form>
					</div>
					</div>
					</div>
					<script	src="<cdn:url value='/resources/js/commonjs/ajaxCommonFunctions.js?rnd=${app_release_no}'/>"></script>
					<script src="<cdn:url value='/resources/js/app/dataEntryEditDemand.js?rnd=${app_release_no}'/>"></script>
				<script src="<cdn:url value='/resources/js/app/applicationsuccess.js?rnd=${app_release_no}'/>"></script>