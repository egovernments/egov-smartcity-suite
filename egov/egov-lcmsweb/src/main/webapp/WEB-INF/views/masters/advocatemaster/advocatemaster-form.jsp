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
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">Standing Counsel</div>
			</div>
			<form:hidden id="mode" path="" value="${mode}" />
			<div class="panel-body">
				<div class="form-group">
					<label for="field-1" class="col-sm-2 control-label"><spring:message
							code="lbl.names" />:<span class="mandatory"></span></label>
					<div class="col-sm-2 col-md-2 add-margin">
						<form:select path="salutation" id="salutation"
							cssClass="form-control" cssErrorClass="form-control error"
							required="required">
							<form:option value="">
								<spring:message code="lbl.select" />
							</form:option>
							<form:option value="Mr.">
													Mr.
											</form:option>
							<form:option value="Ms.">
													Ms.
											</form:option>
							<form:option value="Mrs.">
													Mrs.
											</form:option>
						</form:select>
						<form:errors path="salutation" cssClass="error-msg" />
					</div>
					<div class="col-sm-3 col-md-2 add-margin">
						<form:input type="text" path="name" id="name"
							cssClass="form-control text-left patternvalidation"
							data-pattern="alphanumericspecialcharacters" maxlength="100"
							required="required" />
						<form:errors path="name" cssClass="error-msg" />
					</div>
					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.issenioradvocate" />:</label>
					<div class="col-sm-2 add-margin">
						<form:checkbox path="isSenioradvocate" />
						<form:errors path="isSenioradvocate" cssClass="error-msg" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.address" />:</label>
					<div class="col-sm-3 add-margin">
						<form:textarea class="form-control text-left patternvalidation"
							path="address" id="address" name="address"
							data-pattern="alphanumericwithspecialcharacterswithspace"
							maxlength="256" />
						<form:errors path="address" cssClass="error-msg" />
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.contactphone" />:</label>
					<div class="col-sm-3 add-margin">
						<form:input path="contactPhone"
							class="form-control text-left patternvalidation"
							data-pattern="number" maxlength="20" />
						<form:errors path="contactPhone" cssClass="error-msg" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><spring:message
							code="lbl.mobilenumber" />:<span class="mandatory"></span></label>
					<div class="col-sm-3 add-margin">
						<div class="input-group">
							<span class="input-group-addon">+91</span>
							<form:input type="text" path="mobileNumber" id="mobileNumber"
								data-inputmask="'mask': '9999999999'" required="required"
								cssClass="form-control" maxlength="12" data-pattern="number"
								placeholder="Mobile Number" />
						</div>
						<form:errors path="mobileNumber" cssClass="add-margin error-msg" />
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.email" />:</label>
					<div class="col-sm-3 add-margin">
						<form:input path="email"
							class="form-control text-left patternvalidation"
							data-pattern="alphanumericwithspecialcharacters" maxlength="64"
							placeholder="abc@xyz.com" />
						<form:errors path="email" cssClass="error-msg" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.pannumber" />:<span class="mandatory"></span> </label>
					<div class="col-sm-3 add-margin">
						<form:input path="panNumber"
							class="form-control text-left patternvalidation" id="panNumber"
							data-pattern="alphanumeric" maxlength="10" required="required" />
						<form:errors path="panNumber" cssClass="error-msg" />
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.specialization" />:</label>
					<div class="col-sm-3 add-margin">
						<form:input path="specilization"
							class="form-control text-left patternvalidation"
							data-pattern="alphanumericspecialcharacters" maxlength="100" />
						<form:errors path="specilization" cssClass="error-msg" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.status" />:<span class="mandatory"></span> </label>
					<div class="col-sm-2 add-margin">
						<form:select path="isActive" id="isActive" cssClass="form-control"
							cssErrorClass="form-control error" required="required">
							<form:option value="true">Active</form:option>
							<form:option value="false">InActive</form:option>
							<form:errors path="isActive" cssClass="error-msg" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.paymentmode" />:<span class="mandatory"></span> </label>
					<div class="col-sm-2 add-margin">
						<form:select id="paymentMode" name="paymentMode"
							path="paymentMode" cssClass="form-control"
							cssErrorClass="form-control error">
							<c:forEach items="${paymentModeList}" var="paymentmodevalue">
								<form:option value="${paymentmodevalue}">${paymentmodevalue}</form:option>
							</c:forEach>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.monthlyrenumeration" />:<span class="mandatory"></span>
					</label>
					<div class="col-sm-3 add-margin">
						<form:input path="monthlyRenumeration" maxlength="10"
							id="monthlyRenumeration" class="form-control patternvalidation"
							data-pattern="decimalvalues" required="required"
							pattern="\d{0,6}(\.\d{1,2})?"
							title="It will allow upto 7 digits and 2 decimal points"
							data-first-option="false&true" />
						<form:errors path="monthlyRenumeration" cssClass="error-msg" />
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.isretaineradvocate" />:</span></label>
					<div class="col-sm-3 add-margin">
						<form:checkbox path="isRetaineradvocate" />
						<form:errors path="isRetaineradvocate" cssClass="error-msg" />
					</div>
				</div>
				<div class="form-group">
					<c:if test="${mode == 'edit'}">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.userName" />:</label>
					<div class="col-sm-3 add-margin text-center">
						<form:input class="form-control patternvalidation"
						  id="username"
							readonly="readonly" path="advocateUser.username" />
					</div>
					<form:errors path="advocateUser.username" cssClass="add-margin error-msg" />
				</c:if>
				</div>
				<div class="form-group" id="b1details">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.bankname" />:<span class="mandatory"></span></label>
					<div class="col-sm-3 add-margin">
						<form:select path="bankName.id" id="bankId"
							cssClass="form-control" cssErrorClass="form-control error"
							required="required">
							<form:option value="">
								<spring:message code="lbl.select" />
							</form:option>
							<form:options items="${banks}" itemValue="id" itemLabel="name" />
						</form:select>
						<form:errors path="bankName.id" cssClass="error-msg" />
					</div>

					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.bankbranch" /> :<span class="mandatory"></span></label>

					<div class="col-sm-3 add-margin">
						<%-- <c:if test="${ mode == 'create' && mode=='edit'}"> --%>
						<input type="hidden" id="bankBranchId"
							value="${advocateMaster.bankBranch.id }" />
						<form:select path="bankBranch.id" id="bankBranch"
							cssClass="form-control" cssErrorClass="form-control error"
							required="required">
							<form:option value="">
								<spring:message code="lbl.select" />
							</form:option>

							<c:forEach items="${bankbranchlist}" var="bankBranch">
								<option value="${bankBranch.branchname}">
									${bankBranch.branchname}</option>
							</c:forEach>

						</form:select>
						<form:errors path="bankBranch" cssClass="error-msg" />
					</div>
				</div>
				<div class="form-group" id="b2details">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.bankaccount" />:<span class="mandatory"></span></label>
					<div class="col-sm-3 add-margin">
						<form:input path="bankAccount"
							class="form-control text-left patternvalidation"
							data-pattern="number" maxlength="20" required="required" />
						<form:errors path="bankAccount" cssClass="error-msg" />
					</div>

				</div>
				<div class="form-group" id="b3details">
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.ifsccode" />:<span class="mandatory"></span></label>
					<div class="col-sm-3 add-margin">
						<form:input path="ifscCode"
							class="form-control text-left patternvalidation"
							data-pattern="alphanumeric" maxlength="20" required="required" />
						<form:errors path="ifscCode" cssClass="error-msg" />
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.tinumber" />:<span class="mandatory"></span> </label>
					<div class="col-sm-3 add-margin">
						<form:input path="tinNumber"
							class="form-control text-left patternvalidation"
							data-pattern="alphanumeric" maxlength="20" required="required" />
						<form:errors path="tinNumber" cssClass="error-msg" />
					</div>

				</div>
				<input type="hidden" name="advocateMaster"
					value="${advocateMaster.id}" />
			</div>
		</div>
	</div>
</div>
