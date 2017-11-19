
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
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Relation</div>
				</div>
				<div class="panel-body custom">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.code" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.code}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.name" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.name}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.address" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.address}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.mobile" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.mobile}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.email" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.email}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.narration" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.narration}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isactive" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.isactive}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.panno" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.panno}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.tinno" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.tinno}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.regno" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.regno}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.bankaccount" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.bankaccount}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.ifsccode" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.ifsccode}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.bank" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.bank.name}</div>
					</div>
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