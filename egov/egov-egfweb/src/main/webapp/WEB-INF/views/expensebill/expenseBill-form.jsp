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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style>
   .position_alert{
        position:fixed;z-index:9999;top:85px;right:20px;background:#F2DEDE;padding:10px 20px;border-radius: 5px;
      }
</style>
<form:form name="expenseBillForm" role="form" action="create" modelAttribute="egBillregister" id="egBillregister" class="form-horizontal form-groups-bordered" enctype="multipart/form-data">
 	<div class="position_alert">
		<spring:message	code="lbl.expense.bill.amount" /> : &#8377 <span id="expenseBillTotalAmount"><c:out value="${expenseBillAmount}" default="0.0"></c:out></span>
	</div>
	
	<div>
		<spring:hasBindErrors name="egBillregister">
			<div class="alert alert-danger col-md-10 col-md-offset-1">
	      			<form:errors path="*" /><br/>
	      	</div>
	   </spring:hasBindErrors>
   </div>
	<form:hidden path="billamount" id="billamount" class ="billamount"/>
	<div class="panel-title text-center" style="color: green;">
		<c:out value="${message}" /><br />
	</div>
			<ul class="nav nav-tabs" id="settingstab">
				<li class="active"><a data-toggle="tab" href="#expensebillheader"
					data-tabidx=0><spring:message code="lbl.header" /></a></li>
				<li><a data-toggle="tab" href="#checklist" data-tabidx=1><spring:message
							code="lbl.checklist" /> </a></li>
			</ul>
	
		<div class="tab-content">
			<div class="tab-pane fade in active" id="expensebillheader">   
				<jsp:include page="expenseBill-header.jsp"/>
				<jsp:include page="expenseBill-subLedgerDetails.jsp"/>
				<div class="panel panel-primary" data-collapsed="0">
					<jsp:include page="expenseBill-debitDetails.jsp"/>
					<jsp:include page="expenseBill-creditDetails.jsp"/>
					<jsp:include page="expenseBill-netPayable.jsp"/>
				</div>
				<jsp:include page="expenseBill-accountDetails.jsp"/>
				<jsp:include page="expenseBill-subLedgerAccountDetails.jsp"/>
			</div>
			<div class="tab-pane fade" id="checklist">
				<jsp:include page="expenseBill-checkList.jsp"/>
			</div>
			<div class="text-center">
				<input type="submit" id="submit" class="btn btn-primary" value="Submit"/>
			</div>
		</div>
  
</form:form>  
<script src="<cdn:url value='/resources/app/js/common/helper.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/app/js/common/voucherBillHelper.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/app/js/expensebill/expensebill.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/patternvalidation.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js' context='/egi'/>"></script>
