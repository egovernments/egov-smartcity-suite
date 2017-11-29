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
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Judgment Implementation</div>
				</div>
				<div class="panel-body">
					<div class="form-group">

						<label class="col-sm-3 control-label"><spring:message
								code="lbl.iscomplied" />:<span class="mandatory"></span></label>

						<div class="col-sm-2 col-xs-12 add-margin">

							<div class="radio">
								<label><form:radiobutton path="judgmentImplIsComplied"
										id="IsCompliedYes" value="YES" required="required"
										checked="checked" />Yes</label>
							</div>

						</div>
						<div class="col-sm-2 col-xs-12 add-margin">

							<div class="radio">
								<label><form:radiobutton path="judgmentImplIsComplied"
										id="IsCompliedNo" value="NO" required="required" />No</label>
							</div>

						</div>

						<div class="col-sm-2 col-xs-12 add-margin">

							<div class="radio">
								<label><form:radiobutton path="judgmentImplIsComplied"
										id="IsCompliedInProgress" value="INPROGRESS"
										required="required" />In Progress</label>
							</div>

						</div>
						<form:errors path="judgmentImplIsComplied" cssClass="error-msg" />
					</div>
					<div class="form-group" id="dateofcomp1" style="display: none">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.dateofcompliance" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="dateOfCompliance"
								class="form-control datepicker" data-date-end-date="0d"
								data-inputmask="'mask': 'd/m/y'" id="dateOfCompliance"
								required="required" />
							<form:errors path="dateOfCompliance" cssClass="error-msg " />
						</div>
					</div>
					<div class="form-group" id="dateofcomp2" style="display: none">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.compliancereport" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="complianceReport" id="complianceReport"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumericwithspecialcharacterswithspace"
								maxlength="1024" required="required" />
							<form:errors path="complianceReport" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group" id="reason" style="display: none">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.reason" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="implementationFailure"
								id="implementationFailure" cssClass="form-control"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${implementationFailure}" />
							</form:select>
							<form:errors path="implementationFailure" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group" id="judgmentdetails" style="display: none">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.details" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:textarea id="details" path="details"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumericwithspecialcharacterswithspace"
								maxlength="1024" required="required" />
							<form:errors path="details" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group" id="apealFields1" style="display: none">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.srnumber" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input id="appeal[0].srNumber" path="appeal[0].srNumber"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="50" required="required" />
							<form:errors path="appeal[0].srNumber" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.appealfieldon" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="appeal[0].appealFiledOn"
								class="form-control datepicker" data-date-end-date="0d"
								data-inputmask="'mask': 'd/m/y'" required="required" />
							<form:errors path="appeal[0].appealFiledOn" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group" id="apealFields2" style="display: none">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.appealfieldby" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="appeal[0].appealFiledBy"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="36" required="required" />
							<form:errors path="appeal[0].appealFiledBy" cssClass="error-msg" />
						</div>
						</div>
					<div class="form-group" id="apealFields3" style="display: none">
					 <jsp:include page="appealdocuments-view.jsp"></jsp:include> 
						</div>

					<div class="form-group" id="contempFields1" style="display: none">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.canumber" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input id="contempt[0].caNumber" path="contempt[0].caNumber"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="36" required="required" />
							<form:errors path="contempt[0].caNumber" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.receiveddate" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="contempt[0].receivingDate"
								class="form-control datepicker" data-date-end-date="0d"
								data-inputmask="'mask': 'd/m/y'" required="required" />
							<form:errors path="contempt[0].receivingDate"
								cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group" id="contempFields2" style="display: none">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.appearancecomm" />:</label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="contempt[0].iscommapprRequired" />
							<form:errors path="contempt[0].iscommapprRequired"
								cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.dateofapp" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input path="contempt[0].commappDate"
								class="form-control datepicker" data-date-end-date="0d"
								data-inputmask="'mask': 'd/m/y'" />
							<form:errors path="contempt[0].commappDate" cssClass="error-msg" />
						</div>
						<input type="hidden" name="judgmentImpl.contempt"
							value="${contempt[0].id}" />
					</div>
					<div id="judgmentImplDocuments"></div>
					<input type="hidden" name="judgmentImpl" value="${judgmentImpl.id}" />
					<input
						type="hidden" name="mode" value="${mode}" />