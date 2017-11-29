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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<form:form role="form"  method="post" modelAttribute="vacateStay"
	id="vacateStayform" cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<%--  <input type="hidden" id="lcInterimOrderid" name="lcInterimOrderid"
		value="${lcInterimOrderId}" />  --%>
	 <input type="hidden" id="legalCaseInterimOrder" name="legalCaseInterimOrder"
		value="${legalCaseInterimOrder.id}" /> 
		<input type="hidden" id="lcNumber" name="lcNumber"
		value="${legalCase.lcNumber}" />
		<input type="hidden" name="legalCase" value="${legalCase.id}" />
		<input type="hidden" name="lcInterimOrderList" id="lcInterimOrderList"
		value="${lcInterimOrderList}">
	<input type="hidden" value="${mode}" id="mode" />
	<input type="hidden" name="lcInterimOrderId" value="${legalCaseInterimOrder.id}" />
	<%@ include file="vacatestay-form.jsp"%>
	<c:if test="${not empty message}">
		<div role="alert">${message}</div>
	</c:if>
	</div>
	</div>
	</div>
	</div>
	<div class="form-group">
		<div class="text-center">
			<button type="submit" name="submit" id="save" class="btn btn-primary"
				value="Save">
				<spring:message code="lbl.submit" />
			</button>
			<button type='button' class='btn btn-default' id="btnclose">
				<spring:message code='lbl.close' />
			</button>
			<button type="button" class="btn btn-default" id="buttonBack" onclick="goback()"><spring:message code="lbl.back"/></button>

		</div>
	</div>
</form:form>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>">
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/lcInterimOrderHelper.js?rnd=${app_release_no}'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/vacateStay.js?rnd=${app_release_no}'/>"></script>