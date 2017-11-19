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

<div class="panel-heading  custom_form_panel_heading subheadnew">
	<div class="panel-title">
		<spring:message code='license.title.applicantdetails' />
	</div>
</div>
<div class="panel-body">

	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code='licensee.aadhaarNo' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.licensee.uid}" />
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code='search.licensee.mobileNo' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.licensee.mobilePhoneNumber}" />
		</div>
	</div>

	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code='licensee.applicantname' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.licensee.applicantName}" />
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code='licensee.fatherorspousename' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.licensee.fatherOrSpouseName}" />
		</div>
	</div>

	<div class="row">
		<div class="col-xs-3 add-margin">
			<spring:message code='lbl.emailid' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.licensee.emailId}" />
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code='licensee.address' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.licensee.address}" />
		</div>
	</div>

</div>

<div class="panel-heading  custom_form_panel_heading subheadnew">
	<div class="panel-title">
		<spring:message code='license.location.lbl' />
	</div>
</div>
<div class="panel-body">

	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code='license.propertyNo.lbl' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.assessmentNo}" />
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code='lbl.locality' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.boundary.name}" />
		</div>
	</div>

	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code='baseregister.ward' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.parentBoundary.name}" />
		</div>
	</div>

	<div class="row">
		<div class="col-xs-3 add-margin">
			<spring:message code='license.ownerShipType.lbl' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.ownershipType}" />
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code='license.address' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.address}" />
		</div>
	</div>

</div>

<div class="panel-heading  custom_form_panel_heading subheadnew">
	<div class="panel-title">
		<spring:message code='license.tradedetail' />
	</div>
</div>
<div class="panel-body">

	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code='license.licensenumber' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.licenseNumber}" />
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code='license.oldlicensenum' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.oldLicenseNumber}" />
		</div>
	</div>

	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code='search.license.establishmentname' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.nameOfEstablishment}" />
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code='license.tradeType.lbl' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.natureOfBusiness.name}" />
		</div>
	</div>

	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code='license.category.lbl' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.category.name}" />
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code='license.subCategory.lbl' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.tradeName.name}" />
		</div>
	</div>
	<div class="row add-border">
		<div class="col-xs-3 add-margin">
			<spring:message code='license.uom.lbl' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out
				value="${tradeLicense.tradeName.licenseSubCategoryDetails[0].uom.name}" />
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code='license.premises.lbl' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.tradeArea_weight}" />
		</div>
	</div>
	<div class="row">
		<div class="col-xs-3 add-margin">
			<spring:message code='license.remarks' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<c:out value="${tradeLicense.remarks}" />
		</div>
		<div class="col-xs-3 add-margin">
			<spring:message code='license.startdate' />
		</div>
		<div class="col-xs-3 add-margin view-content">
			<fmt:formatDate value="${tradeLicense.commencementDate}"
				pattern="dd/MM/yyyy" var="commencementDateFrmttd" />
			<c:out value="${commencementDateFrmttd}" />
		</div>
	</div>

</div>

<c:if test="${tradeLicense.agreementDate!=null}">
	<div class="panel-heading  custom_form_panel_heading subheadnew">
		<div class="panel-title">
			<spring:message code='license.AgreementDetails.lbl' />
		</div>
	</div>
	<div class="panel-body">
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code='license.agreementDate.lbl' />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<fmt:formatDate value="${tradeLicense.agreementDate}"
					pattern="dd/MM/yyyy" var="agreementDateFrmttd" />
				<c:out value="${agreementDateFrmttd}" />
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code='license.agreementDocNo.lbl' />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<c:out value="${tradeLicense.agreementDocNo}" />
			</div>
		</div>
	</div>
</c:if>
<c:set value="${outstandingFee}" var="feeInfo"></c:set>
<c:if test="${feeInfo.size()> 0}">
	<div class="panel-heading  custom_form_panel_heading subheadnew">
		<div class="panel-title">
			<spring:message code='license.title.feedetail' />
		</div>
	</div>
	<table class="table table-bordered" style="width: 97%; margin: 0 auto;">
		<thead>
			<tr>
				<th><spring:message code='lbl.feetype' /></th>
				<th><spring:message code='lbl.current' /></th>
				<th><spring:message code='license.fee.arrears' /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${feeInfo}" var="fee" varStatus="status">
				<tr>
					<td>${fee.key}</td>
					<td>${fee.value['current']}</td>
					<td>${fee.value['arrear']}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</c:if>
<c:if test="${tradeLicense.documents.size()>0}">
<div class="panel-heading  custom_form_panel_heading subheadnew">

	<div class="panel-title">
		<spring:message code='license.support.docs' />
	</div>
</div>
</c:if>
<div class="panel-body">
	<div class="row add-border">
		<%@ include file="supportdocs-view.jsp"%>
	</div>
</div>
