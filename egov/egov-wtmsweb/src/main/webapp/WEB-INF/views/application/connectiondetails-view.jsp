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

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.connection.details"/>
				</div>
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-xs-3 add-margin"><spring:message code="lbl.category"/></div>
					<div class="col-xs-3 add-margin view-content"><c:out value="${waterConnectionDetails.category.name}" /></div>
					<div class="col-xs-3 add-margin"><spring:message code="lbl.watersourcetype"/></div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${waterConnectionDetails.waterSource.waterSourceType}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin"><spring:message code="lbl.hscpipesize.inches" /></div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${waterConnectionDetails.pipeSize.code}" />
					</div>
					<div class="col-xs-3 add-margin"><spring:message code="lbl.sumpcapacity.litres" /></div>
					<div class="col-xs-3 add-margin view-content"><c:out value="${waterConnectionDetails.sumpCapacity}" /></div>
				</div>
				<div class="row add-border">
				 <c:choose>
				   <c:when test="${waterConnectionDetails.usageType.name.equals('Lodges')}">
				      <div class="col-xs-3 add-margin"><spring:message code="lbl.no.of.rooms" /></div>
				  		<div class="col-xs-3 add-margin view-content">
						  <c:choose>
                             <c:when test="${waterConnectionDetails.numberOfRooms != null}">
                               <c:out value="${waterConnectionDetails.numberOfRooms}" />
                             </c:when>
                             <c:otherwise>
                               <c:out value=" " />
                             </c:otherwise>     
                           </c:choose>
                     </c:when>   
                  <c:otherwise>
                   <div class="col-xs-3 add-margin"><spring:message code="lbl.no.of.persons" /></div>
                   <div class="col-xs-3 add-margin view-content">
                          <c:choose>
                            <c:when test="${waterConnectionDetails.numberOfPerson != null}">
                              <c:out value="${waterConnectionDetails.numberOfPerson}" />
                            </c:when>
                            <c:otherwise>
                              <c:out value=" " />
                            </c:otherwise>     
                           </c:choose>
                  </c:otherwise>          
				 </c:choose>
				 </div>   
					<div class="col-xs-3 add-margin"><spring:message code="lbl.bpl.cardholdername" /></div>
					<div class="col-xs-3 add-margin view-content">
					<c:choose>
                        <c:when test="${waterConnectionDetails.bplCardHolderName != null}">
                          <c:out value="${waterConnectionDetails.bplCardHolderName}" />
                        </c:when>
                        <c:otherwise>
                          <spring:message code="lbl.notapplicable.code"/>
                        </c:otherwise>          
                    </c:choose>
                    </div>
					</div>
					<c:if test="${mode =='search' && waterConnectionDetails.legacy=='true' }">
						<div class="row">
							<div class="col-xs-3 add-margin"><spring:message code="lbl.donationcharge"/></div>  
							<div class="col-xs-3 add-margin view-content">
								<c:choose>
								<c:when test="${not empty waterConnectionDetails.existingConnection.donationCharges}">
									<c:out value="${waterConnectionDetails.existingConnection.donationCharges}" />
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose></div>
							<div class="col-xs-3 add-margin"></div>
							<div class="col-xs-3 add-margin view-content">
							</div>
							
							<div class="col-xs-3 add-margin"><spring:message code="lbl.watersupplytype"/></div>
							<div class="col-xs-3 add-margin view-content">
								<c:choose>
									<c:when test="${waterConnectionDetails.waterSupply.waterSupplyType != null}">
										<c:out value="${waterConnectionDetails.waterSupply.waterSupplyType}" />
									</c:when>
									<c:otherwise>
										<spring:message code="lb.NA.code"/>
									</c:otherwise>
								</c:choose>
							</div>	
						</div>
					</c:if>
					<c:if test="${waterConnectionDetails.legacy=='false' }">
						<div class="row add-border">
							<div class="col-xs-3 add-margin"><spring:message code="lbl.donationcharge"/></div>  
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${waterConnectionDetails.donationCharges}"></c:out>
							</div>
							<div class="col-xs-3 add-margin"><spring:message code="lbl.estimationcharges"/></div>
							<div class="col-xs-3 add-margin view-content">
							<c:choose>
								<c:when test="${not empty waterConnectionDetails.fieldInspectionDetails.estimationCharges}">
									<c:out value="${waterConnectionDetails.fieldInspectionDetails.estimationCharges}" />
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose></div>
						</div>
						<c:if test="${waterConnectionDetails.connectionType == 'METERED'}">
							<div class="row add-border">
								<div class="col-xs-3 add-margin"><spring:message code="lbl.watersupplytype"/></div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${waterConnectionDetails.waterSupply.waterSupplyType}"/>
								</div>
								
								<div class="col-xs-3 add-margin"><spring:message code="lbl.apartmentorcomplexname"/></div>
								<div class="col-xs-3 add-margin view-content">
									<c:out value="${waterConnectionDetails.buildingName}"/>
								</div>
							</div>
						</c:if>
					</c:if>
					<div class="row add-border">
						<c:if test="${waterConnectionDetails.connection.parentConnection.id!=null}">
							<div class="col-xs-3 add-margin"><spring:message code="lbl.addconnection.reason" /></div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${waterConnectionDetails.connectionReason}"/>
							</div>
						</c:if>
						<c:if test="${waterConnectionDetails.applicationType.code == 'CLOSINGCONNECTION' && waterConnectionDetails.closeConnectionType!=null}">
							<div class="col-xs-3 add-margin"><spring:message code="lbl.closure.type" /></div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${waterConnectionDetails.closeConnectionType=='P'?'PERMANENT':'TEMPORARY'}"/>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div>					
	</div>
	<c:if test="${mode !='meterEntry'}">
		<jsp:include page="documentdetails-view.jsp"></jsp:include> 
	</c:if>
