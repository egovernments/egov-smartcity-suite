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
	<div class="panel-body">
	<c:if test="${waterConnectionDetails.legacy=='false' && mode!='addconnection' && mode!='changeOfUse'}">
		<div class="row add-border">
			<div class="col-xs-3 add-margin"><spring:message code="lbl.ack.number"/></div>
			<div class="col-xs-3 add-margin view-content" id="applicationNumber">
				<c:choose>
					<c:when test="${not empty waterConnectionDetails.applicationNumber}">
						<c:out value="${waterConnectionDetails.applicationNumber}" />
					</c:when>
					<c:otherwise><spring:message code="lb.NA.code"/></c:otherwise>
				</c:choose>
			</div>
			<div class="col-xs-3 add-margin"><spring:message code="lbl.application.date"/></div>
			<div class="col-xs-3 add-margin view-content" id="applicationDate">
				<fmt:formatDate pattern="dd/MM/yyyy" value="${waterConnectionDetails.applicationDate}" />
			</div>
		</div>
	</c:if>	
	
	<c:if test="${mode =='search' && waterConnectionDetails.legacy=='true' }">
	<div class="row add-border">
		
		</div>
	</c:if>	
	<div class="row add-border">
		<div class="col-xs-3 add-margin"><spring:message code="lbl.ptassesmentnumber"/></div>
		<div class="col-xs-3 add-margin view-content" id='propertyIdentifier'>
			<c:out value="${waterConnectionDetails.connection.propertyIdentifier}" />
		</div>
		<div class="col-xs-3 add-margin"><spring:message code="lbl.consumer.number"/></div>
		<div class="col-xs-3 add-margin view-content">
			<c:choose>
				<c:when test="${not empty waterConnectionDetails.connection.consumerCode}">
					<c:out value="${waterConnectionDetails.connection.consumerCode}" />
				</c:when>
				<c:otherwise><spring:message code="lb.NA.code"/></c:otherwise>
			</c:choose>
		</div>
		
		
	</div>
	<div class="row add-border">
			
		<div class="col-xs-3 add-margin"><spring:message code="lbl.mobileNo"/></div>
		<div class="col-xs-3 add-margin view-content" id="mobileNumber"></div>
		<div class="col-xs-3 add-margin"><spring:message code="lbl.email"/> </div>
		<div class="col-xs-3 add-margin view-content" id="email" ></div>
	</div>
	<div class="row add-border">
		
		<div class="col-xs-3 add-margin"><spring:message code="lbl.applicantname"/></div>
		<div class="col-xs-3 add-margin view-content" id="applicantname"></div>
		<div class="col-xs-3 add-margin"><spring:message code="lbl.locality" /></div>
		<div class="col-xs-3 add-margin view-content" id="locality"></div>
	</div>
		
	<div class="row add-border">
		<div class="col-xs-3 add-margin"><spring:message code="lbl.address" /></div>
		<div class="col-xs-3 add-margin view-content" id="propertyaddress" ></div>
		<div class="col-xs-3 add-margin"><spring:message code="lbl.zonewardblock"/></div>
		<div class="col-xs-3 add-margin view-content" id="zonewardblock"></div>
	</div>
		
	<div class="row add-border">
	<div class="col-xs-3 add-margin"><spring:message code="lbl.aadhar"/></div>
		<div class="col-xs-3 add-margin view-content" id="aadhaar"></div>
	<div class="col-xs-3 add-margin"><spring:message code="lbl.nooffloors"/></div>
		<div class="col-xs-3 add-margin view-content" id="nooffloors"></div>
		
		
		
	</div>
		
	<div class="row add-border">
		<div class="col-xs-3 add-margin"><spring:message code="lbl.connectiontype"/></div>
		<div class="col-xs-3 add-margin view-content"><c:out value="${connectionType}" /></div>
		<div class="col-xs-3 add-margin"><spring:message code="lbl.usagetype"/></div>
		<div class="col-xs-3 add-margin view-content"><c:out value="${waterConnectionDetails.usageType.name}"/></div>
	</div>	
		
		
		
		<div class="row add-border">
			<div class="col-xs-3 add-margin"><spring:message code="lbl.pt.tax"/></div>
			<div class="col-xs-3 add-margin view-content" id="propertytaxdue"></div>
			<div class="col-xs-3 add-margin"><spring:message code="lbl.current.due"/></div>
			
			<c:choose>
				<c:when test="${null!=mode && (mode =='closureConnection' ||  mode=='meterEntry'  || mode =='changeOfUse' || mode=='search' || mode=='addconnection' || mode=='waterTaxCollection' || mode=='viewdcb' || mode=='editdemand' || mode=='editcollection') && waterTaxDueforParent > 0}">
					<div class="col-xs-3 add-margin view-content error-msg"><c:out value="${waterTaxDueforParent}" /></div>
				</c:when>	
				<c:otherwise>
					<div class="col-xs-3 add-margin view-content"><spring:message code="lb.ZERO.code"/></div>
				</c:otherwise>
			</c:choose>
			
		</div>
		<div class="row">
		<div class="col-xs-3 add-margin"><spring:message code="lbl.connectiondate"/></div>
		
		<div class="col-xs-3 add-margin view-content"><fmt:formatDate pattern="dd/MM/yyyy" value="${waterConnectionDetails.executionDate}" /></div>
		
		<div class="col-xs-3 add-margin"><spring:message code="lbl.oldconsumerno"/></div>
		
		<div class="col-xs-3 add-margin view-content">
			<c:choose>
				<c:when test="${not empty waterConnectionDetails.connection.oldConsumerNumber}">
					<c:out value="${waterConnectionDetails.connection.oldConsumerNumber}" />
				</c:when>
				<c:otherwise><spring:message code="lb.NA.code"/></c:otherwise>
			</c:choose>
		</div>
		</div>
	</div>

