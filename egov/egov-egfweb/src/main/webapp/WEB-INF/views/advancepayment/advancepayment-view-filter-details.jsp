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
		<label class="col-md-3 col-xs-6 add-margin"><spring:message
				code="lbl.voucherdate" /></label>
		<div class="col-md-3 col-xs-6 add-margin view-content">${paymentHeader.voucherheader.voucherDate}
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-3 col-xs-6 add-margin"><spring:message
				code="lbl.department" /></label>
		<div class="col-md-3 col-xs-6 add-margin view-content">${paymentHeader.voucherheader.vouchermis.departmentid.name}
		</div>
		<label class="col-md-3 col-xs-6 add-margin"><spring:message
				code="lbl.function" /></label>
		<div class="col-md-3 col-xs-6 add-margin view-content">${paymentHeader.voucherheader.vouchermis.function.name}
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-3 col-xs-6 add-margin"><spring:message
				code="lbl.bankbranch" /></label>
		<div class="col-md-3 col-xs-6 add-margin view-content">${paymentHeader.bankaccount.bankbranch.bank.name}-${paymentHeader.bankaccount.bankbranch.branchname}
		</div>
		<label class="col-md-3 col-xs-6 add-margin"><spring:message
				code="lbl.accountnumber" /></label>
		<div class="col-md-3 col-xs-6 add-margin view-content">${paymentHeader.bankaccount.accountnumber}-${paymentHeader.bankaccount.bankbranch.branchname}
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-3 col-xs-6 add-margin"><spring:message
				code="lbl.payment.mode" /> </label>
		<div class="col-md-3 col-xs-6 add-margin view-content">${paymentHeader.type}
		</div>
		<label class="col-md-3 col-xs-6 add-margin"><spring:message
				code="lbl.narration" /> </label>
		<div class="col-md-3 col-xs-6 add-margin view-content">${paymentHeader.voucherheader.description}
		</div>
	</div>
</div>
