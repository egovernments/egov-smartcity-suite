<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">

			<form:form method="post"
				modelAttribute="waterConnectionDetails"
				id="editmeterWaterConnectionform"
				cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
				<div class="page-container" id="page-container">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.applicant.details" />
					</div>
				</div>
					<input type="hidden" id="consumerCode" name="consumerCode"
						value="${waterConnectionDetails.connection.consumerCode}" />
						
					<form:hidden path="id" />
					<div class="panel-heading">
						<div class="panel-title">
							<spring:message code="lbl.basicdetails" />
						</div>
					</div>
					<jsp:include page="commonappdetails-view.jsp" />
					<div class="col-md-12">
					
						<table class="table table-bordered"    id="dcbOnlinePaymentTable" name="dcbOnlinePaymentTable" >
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
										<c:when test="${!waterConnectionDetails.demand.egDemandDetails.isEmpty()}">
											<c:forEach items="${waterConnectionDetails.demand.egDemandDetails}" var="var1"
												varStatus="counter">
												<tr id="Floorinfo">
												
												<td class="blueborderfortd"><form:input type="text" path="demand.egDemandDetails[${counter.index}].egDemandReason.egInstallmentMaster.description"
														class="form-control low-width" value="${var1.egDemandReason.egInstallmentMaster.description}"
														name="demand.egDemandDetails[${counter.index}].egDemandReason.egInstallmentMaster.description"
														id="demand.egDemandDetails[${counter.index}].egDemandReason.egInstallmentMaster.description"
														required="required"readonly="readonly"
														/>
														<form:input type="hidden"
														class="form-control low-width" path="demand.egDemandDetails[${counter.index}].egDemandReason.egInstallmentMaster.id" value="${var1.egDemandReason.egInstallmentMaster.id}"
														name="demand.egDemandDetails[${counter.index}].egDemandReason.egInstallmentMaster.id"
														id="demand.egDemandDetails[${counter.index}].egDemandReason.egInstallmentMaster.id"
														
														/></td>
														
														
												
												<td class="blueborderfortd">
												<form:input type="hidden" path="demand.egDemandDetails[${counter.index}].egDemandReason.id" 
														class="form-control low-width" value="${var1.egDemandReason.id}"
														name="demand.egDemandDetails[${counter.index}].egDemandReason.id"
														id="demand.egDemandDetails[${counter.index}].egDemandReason.id"
														
														/> 
														<form:input type="hidden" path="demand.egDemandDetails[${counter.index}].egDemandReason.egDemandReasonMaster.id" 
														class="form-control low-width" value="${var1.egDemandReason.egDemandReasonMaster.id}"
														name="demand.egDemandDetails[${counter.index}].egDemandReason.egDemandReasonMaster.id"
														id="demand.egDemandDetails[${counter.index}].egDemandReason.egDemandReasonMaster.id"
														
														/> 
													<%-- 		<form:input type="hidden" path="demand.egDemandDetails[${counter.index}].egDemandReason.modifiedDate" 
														class="form-control low-width" value="${var1.egDemandReason.modifiedDate}"
														name="demand.egDemandDetails[${counter.index}].egDemandReason.modifiedDate"
														id="demand.egDemandDetails[${counter.index}].egDemandReason.modifiedDate"
														
														/> 
														<form:input type="hidden" path="demand.egDemandDetails[${counter.index}].egDemandReason.egDemandReasonMaster.modifiedDate" 
														class="form-control low-width" value="${var1.egDemandReason.egDemandReasonMaster.modifiedDate}"
														name="demand.egDemandDetails[${counter.index}].egDemandReason.egDemandReasonMaster.modifiedDate"
														id="demand.egDemandDetails[${counter.index}].egDemandReason.egDemandReasonMaster.modifiedDate"
														
														/>  --%>
														<form:input type="text" path="demand.egDemandDetails[${counter.index}].egDemandReason.egDemandReasonMaster.code" 
														class="form-control low-width" value="${var1.egDemandReason.egDemandReasonMaster.reasonMaster}"
														name="demand.egDemandDetails[${counter.index}].egDemandReason.egDemandReasonMaster.code"
														id="demand.egDemandDetails[${counter.index}].egDemandReason.egDemandReasonMaster.code"
														required="required" readonly="readonly"
														/><%-- <c:out value="${var1.egDemandReason.egDemandReasonMaster.code}"/> --%>
												</td>
													<td class="blueborderfortd"><form:input type="text" path="demand.egDemandDetails[${counter.index}].amount"
														class="form-control is_valid_number" value="${var1.amount}"
														name="demand.egDemandDetails[${counter.index}].amount"
														id="demand.egDemandDetails[${counter.index}].amount"
														
														maxlength="7"
														required="required"
														/></td>
														<td class="blueborderfortd"><form:input type="text" path="demand.egDemandDetails[${counter.index}].amtCollected"
														class="form-control is_valid_number" value="${var1.amtCollected}"
														name="demand.egDemandDetails[${counter.index}].amtCollected"
														id="demand.egDemandDetails[${counter.index}].amtCollected"  maxlength="7"
														required="required"
														/>
														<form:input path="demand.egDemandDetails[${counter.index}].id"
														type="hidden" id="cmdaddListId"
														value="demand.egDemandDetails[${counter.index}].id" />
													<%-- 	
														<form:input path=""
														type="hidden" id="cmdadd33L3istId" name="demand.egDemandDetails[${counter.index}].egDemandReason.id"
														value="demand.egDemandDetails[${counter.index}].egDemandReason.id" />
														<form:input path=""
														type="hidden" id="cmdadd33ListId" name="demand.egDemandDetails[${counter.index}].egDemandReason.egDemandReasonMaster.id"
														value="demand.egDemandDetails[${counter.index}].egDemandReason.egDemandReasonMaster.id" /> --%>
													
														</td>
												</tr>
											</c:forEach>
										</c:when>
									</c:choose>
									
									</table>
									<table>
								<%-- 	<tr>
										<td class="bluebox">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; </td>
										<td class="bluebox">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; </td>
										<td class="bluebox">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; </td>
						<td class="bluebox"  colspan="2"> Remarks<span class="mandatory">*</span> :
						</td>
						<td class="bluebox" colspan="2">
						<form:textarea class="form-control" path=""  id="remarks" name="remarks" />
						
						</td>		</tr> --%>	
									</table>
									</div>
					</div>
						<div class="row">
						<div class="text-center">
							<button type="submit" class="btn btn-primary" id="submitButtonId"
								>
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
					<script	src="<c:url value='/commonjs/ajaxCommonFunctions.js' context='/egi'/>"></script>
					<script src="<c:url value='/resources/js/app/dataEntryEditDemand.js'/>"></script>
				<script src="<c:url value='/resources/js/app/applicationsuccess.js'/>"></script>	