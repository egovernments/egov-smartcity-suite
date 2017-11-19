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
					<div class="panel-title">Recovery</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.recoverycode" /> <span class="mandatory1">*</span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="type" required="required" id="type"
								class="form-control mandatory text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="20" />
							<form:errors path="type" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.recoveryname" /> <span class="mandatory1">*</span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="recoveryName" required="required"
								class="form-control mandatory text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="50" />
							<form:errors path="recoveryName" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.accountDetailType" /> <span class="mandatory1">*</span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="egPartytype.id" id="egPartytype"
								required="required" cssClass="form-control mandatory"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${egPartytypes}" itemValue="id"
									itemLabel="code" />
							</form:select>
							<form:errors path="egPartytype" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.chartofaccounts" /> <span class="mandatory1">*</span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="chartofaccounts.id" id="chartofaccounts"
								required="required" cssClass="form-control mandatory"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach var="chartOfAccounts" items="${chartOfAccountss}">
									<option value="${chartOfAccounts.id}">
										${chartOfAccounts.glcode} - ${chartOfAccounts.name}</option>
								</c:forEach>
							</form:select>
							<form:errors path="chartofaccounts" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.bankloan" /> </label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="bankLoan" id="bankLoan" />
							<form:errors path="bankLoan" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.isactive" /> </label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="isactive" />
							<form:errors path="isactive" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group display-hide" id="bankDiv">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.bank" /> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="bank.id" id="bank" cssClass="form-control"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${banks}" itemValue="id" itemLabel="name" />
							</form:select>
							<form:errors path="bank" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.remitted" /> <span class="mandatory1">*</span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="remitted" required="required"
								class="form-control mandatory text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="100" />
							<form:errors path="remitted" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.ifsccode" /> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="ifscCode"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="16" />
							<form:errors path="ifscCode" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">

						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.accountnumber" /> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="accountNumber"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="32" />
							<form:errors path="accountNumber" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.description" /> </label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="description"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" rows="2" cols="30" />
							<form:errors path="description" cssClass="error-msg" />
						</div>
					</div>
					<input type="hidden" name="recovery" value="${recovery.id}" /> <input
						type="hidden" name="recoveryMode" value="M" />
				</div>
			</div>
		</div>
	</div>
</div>