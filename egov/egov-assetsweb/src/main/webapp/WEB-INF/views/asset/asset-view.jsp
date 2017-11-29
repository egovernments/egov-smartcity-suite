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
<%@ include file="/includes/taglibs.jsp"%>
<div>
	<div>
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title"><spring:message code="title.asset.result.header" /></div>
			</div>
			<div class="panel-body custom">
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.code" />
					</div>
					<div class="col-sm-3 add-margin view-content">${asset.code}</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.name" />
					</div>
					<div class="col-sm-3 add-margin view-content">${asset.name}</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.assetcategory" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${asset.assetCategory.name}</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.department" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${asset.department.name}</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.assetdetails" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${asset.assetDetails}</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.modeofacquisition" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${asset.modeOfAcquisition}</div>
				</div>
				
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.dateofcreation" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						<fmt:formatDate pattern="dd/MM/yyyy"
							value="${asset.dateOfCreation}" />
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.accdepreciation" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${asset.accDepreciation}</div>
					
				</div>
				<div class="row add-border">
					
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.length" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${asset.length}</div>
						<div class="col-xs-3 add-margin">
						<spring:message code="lbl.width" />
					</div>
					<div class="col-sm-3 add-margin view-content">${asset.width}
					</div>
				</div>
				<div class="row add-border">
					
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.totalarea" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${asset.totalArea}</div>
				</div>
			</div>
			<%@ include file="location-details-view.jsp" %>
			<%@ include file="assetcategory-properties-view.jsp" %>
			<div class="row add-border">
			<div class="panel-heading">
				<div class="panel-title"><spring:message code="title.valuesummary" /></div>
			</div>
			
			<div class="col-xs-3 add-margin">
						<spring:message code="lbl.status" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${asset.status.description}</div>
			
			<div class="col-xs-3 add-margin">
						<spring:message code="lbl.grossvalue" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${asset.grossValue}</div>
			</div>
			<div class="row add-border">
			<div class="col-xs-3 add-margin">
						<spring:message code="lbl.remarks" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${asset.remarks}</div>
			</div>
			</div>
			
		</div>
		
	</div>
</div>
<div class="row text-center">
	<div class="add-margin">
		<a href="javascript:void(0)" class="btn btn-default"
			onclick="self.close()">Close</a>
	</div>
</div>