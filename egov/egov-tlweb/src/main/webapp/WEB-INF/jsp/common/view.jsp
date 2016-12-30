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

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="panel-heading  custom_form_panel_heading subheadnew">
    <div class="panel-title"><s:text name='license.title.applicantdetails' /></div>
</div>
<div class="panel-body">
	
	<div class="row add-border">
		<div class="col-xs-3 add-margin"><s:text name='licensee.aadhaarNo' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{licensee.uid}" /></div>
		<div class="col-xs-3 add-margin"><s:text name='licensee.mobilephone' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{licensee.mobilePhoneNumber}" /></div>
	</div>
	
	<div class="row add-border">
		<div class="col-xs-3 add-margin"><s:text name='licensee.applicantname' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{licensee.applicantName}" /></div>
		<div class="col-xs-3 add-margin"><s:text name='licensee.fatherorspousename' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{licensee.fatherOrSpouseName}" /></div>
	</div>
	
	<div class="row">
		<div class="col-xs-3 add-margin"><s:text name='licensee.emailId' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{licensee.emailId}" /></div>
		<div class="col-xs-3 add-margin"><s:text name='licensee.address' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{licensee.address}" /></div>
	</div>

</div>

<div class="panel-heading  custom_form_panel_heading subheadnew">
    <div class="panel-title"><s:text name='license.location.lbl' /></div>
</div>
<div class="panel-body">

	<div class="row add-border">
		<div class="col-xs-3 add-margin"><s:text name='license.propertyNo.lbl' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{assessmentNo}" /></div>
		<div class="col-xs-3 add-margin"><s:text name='license.locality.lbl' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{boundary.name}" /></div>
	</div>
	
	<div class="row add-border">
		<div class="col-xs-3 add-margin"><s:text name='license.division' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{parentBoundary.name}" /></div>
	</div>
	
	<div class="row">
		<div class="col-xs-3 add-margin"><s:text name='license.ownerShipType.lbl' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{ownershipType}" /></div>
		<div class="col-xs-3 add-margin"><s:text name='license.address' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{address}" /></div>
	</div>
	
</div>

<div class="panel-heading  custom_form_panel_heading subheadnew">
    <div class="panel-title"><s:text name='license.details.lbl' /></div>
</div>
<div class="panel-body">

	<div class="row add-border">
		<div class="col-xs-3 add-margin"><s:text name='license.licensenumber' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{licenseNumber}" /></div>
		<div class="col-xs-3 add-margin"><s:text name='license.oldlicensenum' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{oldLicenseNumber}" /></div>
	</div>

	<div class="row add-border">
		<div class="col-xs-3 add-margin"><s:text name='license.establishmentname' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{nameOfEstablishment}" /></div>
		<div class="col-xs-3 add-margin"><s:text name='license.tradeType.lbl' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{natureOfBusiness.name}" /></div>
	</div>
	
	<div class="row add-border">
		<div class="col-xs-3 add-margin"><s:text name='license.category.lbl' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{category.name}" /></div>
		<div class="col-xs-3 add-margin"><s:text name='license.subCategory.lbl' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{tradeName.name}" /></div>
	</div>
    <div class="row add-border">
        <div class="col-xs-3 add-margin"><s:text name='license.uom.lbl' /></div>
        <div class="col-xs-3 add-margin view-content"><s:property value="%{tradeName.licenseSubCategoryDetails.iterator.next.uom.name}" /></div>
        <div class="col-xs-3 add-margin"><s:text name='license.premises.lbl' /></div>
        <div class="col-xs-3 add-margin view-content"><s:property value="%{tradeArea_weight}" /></div>
    </div>
	<div class="row">
		<div class="col-xs-3 add-margin"><s:text name='license.remarks' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{remarks}" /></div>
        <div class="col-xs-3 add-margin"><s:text name='license.startdate' /></div>
        <div class="col-xs-3 add-margin view-content">
            <s:date name="commencementDate" format="dd/MM/yyyy" var="commencementDateFrmttd"/>
            <s:property value="%{commencementDateFrmttd}" />
        </div>
	</div>
	
</div>

<s:if test="%{agreementDate!=null}">
<div class="panel-heading  custom_form_panel_heading subheadnew">
    <div class="panel-title"><s:text name='license.AgreementDetails.lbl' /></div>
</div>
<div class="panel-body">
<div class="row add-border">
		<div class="col-xs-3 add-margin"><s:text name='license.agreementDate.lbl' /></div>
		<div class="col-xs-3 add-margin view-content">
			<s:date name="agreementDate" format="dd/MM/yyyy" var="agreementDateFrmttd"/>
			<s:property value="%{agreementDateFrmttd}" /></div>
		<div class="col-xs-3 add-margin"><s:text name='license.agreementDocNo.lbl' /></div>
		<div class="col-xs-3 add-margin view-content"><s:property value="%{agreementDocNo}" /></div>
	</div>
</div>
</s:if>

<s:set value="outstandingFee" var="feeInfo"></s:set>
<s:if test="%{#attr.feeInfo.size > 0}">
<div class="panel-heading  custom_form_panel_heading subheadnew">
    <div class="panel-title"><s:text name='license.title.feedetail' /></div>
</div>
<table class="table table-bordered" style="width:97%;margin:0 auto;">
	<thead>
		<tr>
			<th><s:text name='license.fee.type' /></th>
			<th><s:text name='license.fee.current' /></th>
			<th><s:text name='license.fee.arrears' /></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="feeInfo" var="fee" status="status">
			<tr>
				<td>${fee.key}</td>
				<td>${fee.value['current']}</td>
				<td>${fee.value['arrear']}</td>
			</tr>
		</s:iterator>
	</tbody>
</table>
</s:if>
<div class="panel-heading  custom_form_panel_heading subheadnew">
    <div class="panel-title"><s:text name='license.title.documentDetails' /></div>
</div>
<div class="panel-body">
		<div class="row add-border">
			<%@ include file="supportdocs-view.jsp" %>
		</div> 
</div>
