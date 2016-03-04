<!---------------------------------------------------------------------------------
 	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
     Copyright (C) <2015>  eGovernments Foundation
 
     The updated version of eGov suite of products as by eGovernments Foundation 
     is available at http://www.egovernments.org
 
     This program is free software: you can redistribute it and/or modify
     it under the terms of the GNU General Public License as published by
     the Free Software Foundation, either version 3 of the License, or
     any later version.
 
     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.
 
     You should have received a copy of the GNU General Public License
     along with this program. If not, see http://www.gnu.org/licenses/ or 
     http://www.gnu.org/licenses/gpl.html .
 
     In addition to the terms of the GPL license to be adhered to in using this
     program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
--------------------------------------------------------------------------------->
<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<style>
body
{
  font-family:regular !important;
  font-size:14px;
}
</style>

<div class="row">
	<div class="col-md-12">
		<form:form class="form-horizontal form-groups-bordered" method="post"
			name="ownerDetailsForm" id="ownerDetailsForm" action=""
			modelAttribute="property">
			<div class="panel panel-primary" data-collapsed="0"
				style="text-align: left">
				<jsp:include page="../../common/commonPropertyDetailsView.jsp"></jsp:include>
			<div class="panel panel-primary">
					<div class="panel-body custom-form"> 
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.doorNumber" />
								</label>
								<div class="col-sm-3 add-margin">
									<form:input path="" id="doorNumber" name="doorNumber"
												maxlength="15" cssClass="form-control" /> 
								</div>
							</div>
						</div>
						
						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							class="table table-bordered" id="vacantLandTable">

							<tbody>
								<tr>
									<th class="bluebgheadtd"><spring:message
											code="lbl.adharno" /></th>
									<th class="bluebgheadtd"><spring:message
											code="lbl.MobileNumber" /></th>
									<th class="bluebgheadtd"><spring:message
											code="lbl.OwnerName" /></th>
									<th class="bluebgheadtd"><spring:message
											code="lbl.gender" /></th>
									<th class="bluebgheadtd"><spring:message
											code="lbl.EmailAddress" /></th>
									<th class="bluebgheadtd"><spring:message
											code="lbl.GuardianRelation" /></th>
									<th class="bluebgheadtd"><spring:message
											code="lbl.Guardian" /></th>
								</tr>
							<c:choose>
								<c:when
									test="${!property.basicProperty.propertyOwnerInfoProxy.isEmpty()}">
									<c:forEach var="ownerInfo"
										items="${property.basicProperty.propertyOwnerInfoProxy}"
										varStatus="status">
										<tr id="ownerDetailsRow">
											<td class="blueborderfortd" align="center"><form:input
													path="basicProperty.propertyOwnerInfoProxy[${status.index}].owner.aadhaarNumber"
													id="aadharNumber" maxlength="12" cssClass="form-control" />
											</td>
											<td class="blueborderfortd" align="center"><form:input
													path="basicProperty.propertyOwnerInfoProxy[${status.index}].owner.mobileNumber"
													id="mobileNumber" maxlength="10" cssClass="form-control" />
											</td>
											<td class="blueborderfortd" align="center"><form:input
													path="basicProperty.propertyOwnerInfoProxy[${status.index}].owner.name"
													id="name" maxlength="10" cssClass="form-control" /></td>
											<td class="blueborderfortd" align="center"><form:select
													path="basicProperty.propertyOwnerInfoProxy[${status.index}].owner.gender"
													id="gender" name="gender" data-first-option="false"
													cssClass="form-control">
													<form:options items="${gender}" />
												</form:select></td>
											<td class="blueborderfortd" align="center"><form:input
													path="basicProperty.propertyOwnerInfoProxy[${status.index}].owner.emailId"
													id="emailId" maxlength="32" cssClass="form-control" /></td>
											<td class="blueborderfortd" align="center"><form:select
													path="basicProperty.propertyOwnerInfoProxy[${status.index}].owner.guardianRelation"
													id="guardianRelation" name="guardianRelation"
													data-first-option="false" cssClass="form-control">
													<form:options items="${guardianRelationMap}" />
												</form:select></td>
											<td class="blueborderfortd" align="center"><form:input
													path="basicProperty.propertyOwnerInfoProxy[${status.index}].owner.guardian"
													id="guardianName" maxlength="32" cssClass="form-control" />
											</td>

										</tr>
									</c:forEach>
								</c:when>
							</c:choose>
						</tbody>
						</table>
								
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="submit" 
						class="btn btn-primary add-margin">
						<spring:message code="lbl.submit" />
					</button>	
					<a href="javascript:void(0)" class="btn btn-default"
						onclick="self.close()"><spring:message code="lbl.close" /></a>
				</div>
			</div>
		</form:form>
	</div>
</div>
