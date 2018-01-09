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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${!sewerageApplicationDetails.fieldInspections.isEmpty()}">
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.fieldinspection.details"/>
				</div>
			</div>
			
			<div class="panel-body">
			  	<div class="row">
					<div class="col-xs-3 add-margin"><spring:message code="lbl.inspectiondate"/></div>
					<div class="col-xs-3 add-margin view-content" >
						<fmt:formatDate pattern="dd/MM/yyyy" value="${sewerageApplicationDetails.fieldInspections[0].inspectionDate}" />
					</div>
					
					<c:if test="${sewerageApplicationDetails.fieldInspections[0].fileStore != null}">
						<div class="col-xs-3 add-margin"><spring:message code="lbl.fileattached" /></div>
						<div class="col-xs-3 add-margin view-content">
						<a href="/stms/transactions/downloadFile?applicationNumber=${sewerageApplicationDetails.applicationNumber }">${sewerageApplicationDetails.fieldInspections[0].fileStore.fileName }</a>
						</div>
					</c:if>
				</div>
			
			<c:if test="${!sewerageApplicationDetails.fieldInspections[0].fieldInspectionDetails.isEmpty()}">
				<div class="panel-heading">
					<div class="panel-title">
							<spring:message code="lbl.pipe.details" />
					</div>	
				</div>
				<table class="table table-striped table-bordered" id="fieldInspectionDetails">
					<thead>
					      <tr>
					      	<th class="text-center"><spring:message code="lbl.slno" /></th>
							<th class="text-center"><spring:message code="lbl.noofpipes" /></th>
							<th class="text-center"><spring:message code="lbl.pipesize.inches" /></th>
							<th class="text-center"><spring:message code="lbl.pipelength" /></th>
							<th class="text-center"><spring:message code="lbl.screwsize.inches" /></th>
							<th class="text-center"><spring:message code="lbl.noOfScrews" /></th>
							<th class="text-center"><spring:message code="lbl.property.distance" /></th>
							<th class="text-center"><spring:message code="lbl.roaddigging" /></th>
							<th class="text-center"><spring:message code="lbl.roadlength" /></th>
							<th class="text-center"><spring:message code="lbl.roadowner" /></th>
					      </tr>
				     </thead>
					<tbody>
						<c:forEach items="${sewerageApplicationDetails.fieldInspections[0].fieldInspectionDetails}" var="fid"
							varStatus="counter"> 
						      <tr class="">
								<td class="text-center"><span id="slNo1">${counter.index+1}</span></td> 
								<td class="text-center"><c:out value="${fid.noOfPipes}" /></td>
								<td class="text-center"><c:out value="${fid.pipeSize}" /></td>
								<td class="text-center"><c:out value="${fid.pipeLength}"/></td>
								<td class="text-center"><c:out value="${fid.screwSize}"/></td>
								<td class="text-center"><c:out value="${fid.noOfScrews}"/></td> 
								<td class="text-center"><c:out value="${fid.distance}"/></td>
								<c:choose> 
								  <c:when test="${fid.roadDigging}">
								    <td class="text-center"><c:out value="Yes"/></td>
								  </c:when>
								  <c:otherwise>
								    <td class="text-center"><c:out value="No"/></td>
								  </c:otherwise>
								</c:choose>
								<td class="text-center"><c:out value="${fid.roadLength}" default="N/A"/></td>
								<td><c:out value="${fid.roadOwner}" default="N/A"/></td>
						      </tr>
			       		</c:forEach>
					</tbody>
				</table>
			</c:if>
			
			<c:if test="${!sewerageApplicationDetails.estimationDetails.isEmpty()}">
				<div class="panel-heading">
					<div class="panel-title">
							<spring:message code="lbl.estimation.details" />
					</div>	
				</div>
				<table class="table table-striped table-bordered" id="estimateDetails">
					<thead>
					      <tr>
							<th class="text-center"><spring:message code="lbl.slno" /></th>
							<th class="text-center"><spring:message code="lbl.material" /></th>
							<th class="text-center"><spring:message code="lbl.quantity" /></th>
							<th class="text-center"><spring:message code="lbl.uom" /></th>
							<th class="text-center"><spring:message code="lbl.rate" /></th>
							<th class="text-center"><spring:message code="lbl.amount" /></th>
					      </tr>
				         </thead>
					<tbody>
						<c:forEach items="${sewerageApplicationDetails.estimationDetails}" var="fid"
							varStatus="counter"> 
						      <tr class="">
								<td class="text-center"><span id="slNo1">${counter.index+1}</span></td> 
								<td class="text-left"><c:out value="${fid.itemDescription}" default="N/A"/></td>
								<td class="text-center"><c:out value="${fid.quantity}" default="N/A"/></td>
								<td class="text-center"><c:out value="${fid.unitOfMeasurement.uom}" default="N/A" /></td>
								<td class="text-center"><c:out value="${fid.unitRate}" default="N/A"/></td>
								<td class="text-right"><c:out value="${fid.amount}" default="N/A"/></td>
						      </tr>
			       		</c:forEach>
					</tbody>
					</table>
				</c:if> 					
			</div>
		</div>
	</div>
</div>
</c:if>