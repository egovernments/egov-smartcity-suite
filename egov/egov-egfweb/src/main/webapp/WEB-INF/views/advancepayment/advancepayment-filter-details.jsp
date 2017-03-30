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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="lbl.account.deatails" />
	</div>
</div>
<div class="panel-body">
	<div class="form-group">
		<label class="col-sm-3 control-label text-right"><spring:message
				code="lbl.voucherdate" /> <span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin">
			<form:input id="voucherDate" path="voucherDate"
				class="form-control datepicker" data-date-end-date="0d"
				required="required" value="${voucherDate}" />
			<form:errors path="voucherDate" cssClass="add-margin error-msg" />
		</div>
		<label class="col-sm-2 control-label text-right"></label>
		<div class="col-sm-3 add-margin"></div>
	</div>
	<jsp:include page="payment-trans-filter.jsp" />
	<div class="form-group">
		<label class="col-sm-3 control-label text-right"><spring:message
				code="lbl.bankbranch" /> <span class="mandatory"></span> </label>
		<div class="col-sm-3 add-margin">
			<form:select path="" data-first-option="false" id="bankbranch"
				class="form-control" required="required">
				<form:option value="">
					<spring:message code="lbl.select" />
				</form:option>
				<c:forEach items="${bankbranchList}" var="bankbranchs">
					<form:option value="${bankbranchs.id}">${bankbranchs.bank.name} - ${bankbranchs.branchname}</form:option>
				</c:forEach>
			</form:select>
		</div>
		<label class="col-sm-2 control-label text-right"><spring:message
				code="lbl.accountnumber" /> <span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin">
			<form:select path="" id="bankaccount" name="bankaccount"
				class="form-control" data-first-option="false" required="required">
				<form:option value="">
					<spring:message code="lbl.select" />
				</form:option>
				<form:options items="${bankaccounts}" itemValue="id" />
			</form:select>
			<input type="hidden" name="bankaccount" id="bankaccount"
				value="${bankaccount}" />
			<form:input path="" class="form-control text-left "
				id="paymentbalance" name="paymentbaalnce" readonly="true" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label text-right"><spring:message
				code="lbl.payment.mode" /><span class="mandatory"></span> </label>
		<div class="col-sm-3 add-margin">
			<form:radiobuttons path="" id="modeOfPayment" name="modeOfPayment"
				items="${modeOfPaymentMap}" />
			<input type="hidden" name="modeOfPayment" id="modeOfPayment"
				value="${modeOfPayment}" />
		</div>
		<label class="col-sm-2 control-label text-right"><spring:message
				code="lbl.narration" /> </label>
		<div class="col-sm-3 add-margin">
			<form:textarea path="description" id="narration" class="form-control"
				maxlength="300"></form:textarea>
			<form:errors path="description" cssClass="add-margin error-msg" />
		</div>
	</div>
</div>
