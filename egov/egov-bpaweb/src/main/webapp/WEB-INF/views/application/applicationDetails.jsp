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

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">Application Details</div>
</div>

<div id="appDet">
<div class="form-group">
	<label class="col-sm-3 control-label text-right">ServiceType <span
		class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select path="serviceType" data-first-option="false" id="serviceType"
			cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${serviceTypeList}"  itemValue="id"
						itemLabel="description" />
		</form:select>
		<form:errors path="serviceType" cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">Stake
		HolderType </label>
	<div class="col-sm-2 add-margin">
		<form:select path="stakeHolder[0].stakeHolder"
			data-first-option="false" id="" cssClass="form-control">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${stakeHolderList}"  itemValue="id"
						itemLabel="id" />
		</form:select>
		<form:errors path="stakeHolder[0].stakeHolder"
			cssClass="add-margin error-msg" />
	</div>
</div>



<div class="form-group">
	<label class="col-sm-3 control-label text-right">ApplicationNumber
		<span class="mandatory"></span>
	</label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation" maxlength="50"
			id="applicationNumber" path="applicationNumber"  disabled="true"  />
		<form:errors path="applicationNumber" cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">Application
		Date <span class="mandatory"></span>
	</label>
	<div class="col-sm-2 add-margin">
		<form:input path="applicationDate" class="form-control datepicker"
			data-date-end-date="0d" id="applicationDate"
			data-inputmask="'mask': 'd/m/y'" required="required" />
		<form:errors path="applicationDate" cssClass="add-margin error-msg" />
	</div>


</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right">Old
		Application Number <span class="mandatory"></span>
	</label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control" maxlength="50"
			id="oldApplicationNumber" path="oldApplicationNumber" />
		<form:errors path="oldApplicationNumber"
			cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right">Approval
				Date 
			</label>
		<c:choose>
		<c:when test="%{approvalDate!=null}">
			<div class="col-sm-2 add-margin">
			<form:input path="approvalDate" class="form-control datepicker"
				data-date-end-date="0d" id="approveDate"
				data-inputmask="'mask': 'd/m/y'" required="required" />
			<form:errors path="approvalDate" cssClass="add-margin error-msg" /></div>
		</c:when>
		<c:otherwise>
			<div class="col-sm-2 add-margin">
			<form:input path="approvalDate" class="form-control datepicker"
				data-date-end-date="0d" id="approveDate"
				data-inputmask="'mask': 'd/m/y'" disabled="true" />
			<form:errors path="approvalDate" cssClass="add-margin error-msg" /></div>
		</c:otherwise>
	</c:choose>

</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right">Assessment
		Number<span class="mandatory"></span>
	</label>
	<div class="col-sm-3 add-margin">
		<div class="input-group">
			<form:input id="assessmentNumber" path="assessmentNumber"
				class="form-control" maxlength="50" />
			<span class="input-group-addon"> <i
				class="fa fa-search specific"></i></span>
		</div>
		<form:errors path="assessmentNumber" id="assessmentNumber"
			cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right">Occupancy <span
		class="mandatory"></span></label>
	<div class="col-sm-2 add-margin">
		<form:select path="occupancy" data-first-option="false" id=""
			cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${occupancy}" />
		</form:select>
		<form:errors path="occupancy" cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right">Applicant Type<span
		class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
			<form:input id="applicantType" path="applicantType"
				class="form-control patternvalidation" data-pattern="string"
				maxlength="50" />
		<form:errors path="applicantType" id="applicantType"
			cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right">Source <span
		class="mandatory"></span></label>
	<div class="col-sm-2 add-margin">
		<form:select path="source" data-first-option="false" id=""
			cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${souceList}" />
		</form:select>
		<form:errors path="source" cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right">Project Name </label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation" maxlength="50"
			id="projectName" path="projectName" />
		<form:errors path="projectName" cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">Group
		Development</label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation" maxlength="50"
			id="groupDevelopment" path="groupDevelopment" />
		<form:errors path="groupDevelopment" cssClass="add-margin error-msg" />
	</div>

</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right">Tapan Number </label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation" maxlength="50"
			id="tapalNumber" path="tapalNumber" />
		<form:errors path="tapalNumber" cssClass="add-margin error-msg" />
	</div>
<label class="col-sm-2 control-label text-right">Admission Fees
	</label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation" maxlength="50" name="admissionfeeAmount"
			id="admissionfeeAmount" path="admissionfeeAmount" />
		<form:errors path="admissionfeeAmount" cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right">Remarks</label>
	<div class="col-sm-3 add-margin">
		<form:textarea class="form-control patternvalidation"
			data-pattern="string" maxlength="128" id="remarks" path="remarks"
			 />
		<form:errors path="remarks" cssClass="add-margin error-msg" />
	</div>

	
</div>
<div class="form-group">
	<div class="col-sm-3 add-margin">
		<c:choose>
			<c:when test="%{buildingPlanApprovalDate!=null}">
				<label class="col-sm-3 control-label text-right">Building
					PlanApproval Date <span class="mandatory"></span>
				</label>
				<form:input path="buildingPlanApprovalDate"
					class="form-control datepicker" data-date-end-date="0d"
					id="buildingPlanApprovalDate" data-inputmask="'mask': 'd/m/y'"
					required="required" />
				<form:errors path="buildingPlanApprovalDate"
					cssClass="add-margin error-msg" />
			</c:when>
			<c:otherwise>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="col-sm-3 add-margin">
		<c:choose>
			<c:when test="%{buildingplanapprovalnumber!=null}">
				<label class="col-sm-3 control-label text-right">Building
					PlanApproval Number <span class="mandatory"></span>
				</label>
				<form:input class="form-control patternvalidation" maxlength="50"
					id="buildingplanapprovalnumber" path="buildingplanapprovalnumber" />
				<form:errors path="buildingplanapprovalnumber"
					cssClass="add-margin error-msg" />
			</c:when>
			<c:otherwise>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<div class="col-sm-3 add-margin">
	<c:choose>
		<c:when test="%{planPermissionDate!=null}">
			<label class="col-sm-3 control-label text-right">Plan
				Submission Date <span class="mandatory"></span>
			</label>
			<form:input path="planPermissionDate" class="form-control datepicker"
				data-date-end-date="0d" id="planPermissionDate"
				data-inputmask="'mask': 'd/m/y'" required="required" />
			<form:errors path="planPermissionDate"
				cssClass="add-margin error-msg" />
		</c:when>
		<c:otherwise>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
		</c:otherwise>
	</c:choose>
</div>
<div class="col-sm-3 add-margin">
	<c:choose>
		<c:when test="%{planPermissionNumber!=null}">
			<label class="col-sm-3 control-label text-right">PlanSubmission
				Number <span class="mandatory"></span>
			</label>
			<form:input class="form-control patternvalidation" maxlength="50"
				id="planPermissionNumber" path="planPermissionNumber" />
			<form:errors path="planPermissionNumber"
				cssClass="add-margin error-msg" />
		</c:when>
		<c:otherwise>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
		</c:otherwise>
	</c:choose>
</div>