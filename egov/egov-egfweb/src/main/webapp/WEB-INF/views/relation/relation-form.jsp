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
					<div class="panel-title">Supplier</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.code" /> <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="code"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="50" required="required" />
							<form:errors path="code" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.name" /> <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="name"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="50" required="required" />
							<form:errors path="name" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.address" /> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="address"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="300" />
							<form:errors path="address" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.mobile" /> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="mobile" id="mobile"
								class="form-control text-left patternvalidation"
								data-pattern="number" maxlength="10"   />
							<form:errors path="mobile" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.email" /> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="email"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="25" />
							<form:errors path="email" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.narration" /> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="narration"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="250" />
							<form:errors path="narration" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.isactive" /> </label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="isactive" />
							<form:errors path="isactive" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.panno" /> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="panno"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="10" />
							<form:errors path="panno" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.tinno" /> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="tinno"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="20" />
							<form:errors path="tinno" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.regno" /> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="regno"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="25" />
							<form:errors path="regno" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.bank" /> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="bank.id" id="bank.id" cssClass="form-control"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${banks}" itemValue="id" itemLabel="name" />
							</form:select>
							<form:errors path="bank" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.bankaccount" /> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="bankaccount"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="25" />
							<form:errors path="bankaccount" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.ifsccode" /> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="ifsccode"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="12" />
							<form:errors path="ifsccode" cssClass="error-msg" />
						</div>
					</div>
					<input type="hidden" name="relation" value="${relation.id}" />

                    <script type="text/javascript"
                            src="/egi/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}"></script>
					