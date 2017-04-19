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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">Standing Counsel Details</div>
			</div>
			<div class="panel-body custom">
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.salutation" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${advocateMaster.salutation}</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.names" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${advocateMaster.name}</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.issenioradvocate" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${advocateMaster.isSenioradvocate}</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.address" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${advocateMaster.address}</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.contactphone" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${advocateMaster.contactPhone}</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.mobilenumber" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${advocateMaster.mobileNumber}</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.email" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${advocateMaster.email}</div>

				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.pannumber" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${advocateMaster.panNumber}</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.specialization" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${advocateMaster.specilization}</div>

				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.isactive" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${advocateMaster.isActive}</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.paymentmode" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${advocateMaster.paymentMode}</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.monthlyrenumeration" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						<fmt:formatNumber var="formattedRate" type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${advocateMaster.monthlyRenumeration}" />
						<c:out value="${formattedRate}" />
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.isretaineradvocate" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${advocateMaster.isRetaineradvocate}</div>
				</div>
				<c:choose>
					<c:when test="${advocateMaster.paymentMode == 'RTGS'}">
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.bankname" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								${advocateMaster.bankName.name}</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.bankbranch" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								${advocateMaster.bankBranch.branchname}</div>

						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.bankaccount" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								${advocateMaster.bankAccount}</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.ifsccode" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								${advocateMaster.ifscCode}</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.tinumber" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								${advocateMaster.tinNumber}</div>

						</div>
					</c:when>
				</c:choose>
			</div>
		</div>
	</div>
	<div class="row text-center">
		<div class="add-margin">
			<a href="javascript:void(0)" class="btn btn-default"
				onclick="self.close()">Close</a>
		</div>
	</div>
</div>
