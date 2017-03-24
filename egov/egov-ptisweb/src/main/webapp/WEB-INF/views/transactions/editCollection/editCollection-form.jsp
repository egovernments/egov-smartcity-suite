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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<style>
body {
	font-family: regular !important;
	font-size: 14px;
}
</style>
<script type="text/javascript"
	src="<cdn:url value='/resources/javascript/validations.js'/>"></script>
<script type="text/javascript">
	function submitForm(obj) {
		if (obj.value == "submit") {
			jQuery('#EditCollection')[0].action = '/ptis/editCollection/update';
			jQuery('#EditCollection')[0].method = 'post';
		} else if (obj.value == "reset") {
			window.location.href = '/ptis/editCollection/editForm/'+${basicProperty.upicNo};
		}
	}
	
	
	
	function checkPenaltyCollection(obj) {
		var intransaction = false;
		var taxvalue = jQuery(obj).val();
		var rowidx = jQuery(obj).data('idx');
		var penaltycollection = jQuery(
				"#Penalty-Fines-" + rowidx + "-rvsdCollection").val();
		var penaltyamount = jQuery("#Penalty-Fines-" + rowidx + "-rvsdAmount")
				.val();
		var existingpenalty = jQuery(
				"#Penalty-Fines-" + rowidx + "-actualDemand").val();
		var actualcollection = jQuery(
				"#Penalty-Fines-" + rowidx + "-actualCollection").val();
		if (taxvalue && (Number(existingpenalty) != Number(actualcollection))) {
			if (penaltycollection == '') {
				intransaction = true;
				bootbox.alert("penalty should be collected first!!");
				jQuery(obj).val("");
				return false;
			} else if (Number(penaltyamount) > Number(penaltycollection)
					&& penaltycollection) {
				bootbox.alert("penalty amount should be fully collected!");
				jQuery("#Penalty-Fines-" + rowidx + "-rvsdCollection").val("");
				return false;

			} else if (penaltyamount == ''
					&& Number(existingpenalty) > Number(penaltycollection)) {
				bootbox.alert("penalty amount should be fully collected!");
				jQuery("#Penalty-Fines-" + rowidx + "-rvsdCollection").val("");
				return false;

			}
		} else if (Number(penaltyamount) > Number(penaltycollection)
				&& penaltycollection) {
			bootbox.alert("penalty amount should be fully collected!");
			jQuery("#Penalty-Fines-" + rowidx + "-rvsdCollection").val("");
			return false;

		}

	}
</script>
<form:form id="EditCollection" method="post" action=""
	class="form-horizontal form-groups-bordered" modelAttribute="demandDetailBeansForm">
	<div class="page-container" id="page-container">
		<div class="main-content">
			<jsp:include page="../../common/ownerDetailsView.jsp"></jsp:include>
			<jsp:include page="../../common/propertyAddressDetailsView.jsp"></jsp:include>
			<table class="table table-bordered" id="editCollectionTable">
				<thead>
					<tr>
						<th><spring:message code="lbl.finYear" /></th>
						<th><spring:message code="lbl.taxName" /></th>
						<th><spring:message code="lbl.actual.demand" /></th>
						<th><spring:message code="lbl.revised.demand" /></th>
						<th><spring:message code="lbl.actual.collection" /></th>
						<th><spring:message code="lbl.revised.collection" /></th>
					</tr>
				</thead>
				<tbody>
					<form:hidden path="basicProperty.upicNo" />
					<c:set var="taxId" value="0"></c:set>
					<c:forEach var="demandDetailBean" items="${demandDetailBeans}"
							varStatus="demandInfoStatus">
							<c:set var="taxHead" value="${demandDetailBeans[demandInfoStatus.index].reasonMaster}"></c:set>
							<c:set var="taxName" value="${fn:replace(taxHead,' ','-')}"></c:set>
							<tr>
								<td class="col-xs-2" align="center">
									<form:hidden
										path="demandDetailBeans[${demandInfoStatus.index}].installment.id" />
									<c:if
										test="${demandDetailBeans[demandInfoStatus.index].installment.id == demandDetailBeans[demandInfoStatus.index - 1].installment.id }">
										&nbsp;
									</c:if>
									<c:if
										test="${demandDetailBeans[demandInfoStatus.index].installment.id != demandDetailBeans[demandInfoStatus.index - 1].installment.id }">
										<c:out
											value="${demandDetailBeans[demandInfoStatus.index].installment}" />
											<c:set var="taxId" value="${taxId+1}"></c:set>
									</c:if>
								</td>
								<td class="col-xs-2" align="left">
									<c:out
										value="${demandDetailBeans[demandInfoStatus.index].reasonMaster}" />
									<form:hidden
										path="demandDetailBeans[${demandInfoStatus.index}].reasonMaster" />
								</td>
								<td class="col-xs-2" align="right">
									<c:out
										value="${demandDetailBeans[demandInfoStatus.index].actualAmount}" />
									<form:hidden
										path="demandDetailBeans[${demandInfoStatus.index}].actualAmount" />
								<input type="hidden"
								value="${demandDetailBeans[demandInfoStatus.index].actualAmount}"
								id="${taxName}-${taxId}-actualDemand" />

							</td>
								<td class="col-xs-2" align="right">
									<c:if
										test="${demandDetailBeans[demandInfoStatus.index].reasonMaster == 'Penalty Fines'}">
										<form:input path="demandDetailBeans[${demandInfoStatus.index}].revisedAmount" onblur="validNumber(this);" id="${taxName}-${taxId}-rvsdAmount" cssClass="form-control" />
										<form:errors path="demandDetailBeans[${demandInfoStatus.index}].revisedAmount" cssClass="add-margin error-msg" />
									</c:if>
									<c:if
										test="${demandDetailBeans[demandInfoStatus.index].reasonMaster != 'Penalty Fines'}">
										N/A
									</c:if>
								</td>
								<td class="col-xs-2" align="right">
									<c:out
										value="${demandDetailBeans[demandInfoStatus.index].actualCollection}" />
									<form:hidden
										path="demandDetailBeans[${demandInfoStatus.index}].actualCollection" />
								<input type="hidden"
								value="${demandDetailBeans[demandInfoStatus.index].actualCollection}"
								id="${taxName}-${taxId}-actualCollection" />

							</td>
								<td class="col-xs-2" align="right" >
									<form:input path="demandDetailBeans[${demandInfoStatus.index}].revisedCollection" onblur="checkPenaltyCollection(this);validNumber(this);" id="${taxName}-${taxId}-rvsdCollection" data-idx="${taxId}" cssClass="form-control" />
									<form:hidden
										path="demandDetailBeans[${demandInfoStatus.index}].isCollectionEditable" />
									<form:errors path="demandDetailBeans[${demandInfoStatus.index}].revisedCollection" cssClass="add-margin error-msg" />
								</td>
							</tr>
						</c:forEach>
				</tbody>
			</table>
			<div class="row">
				<div class="col-xs-2" align="right">
					<spring:message code="lbl.receipt.number" /><span class="mandatory"></span>
				</div>
				<div class="col-xs-2" align="center">
					<form:input path="propertyReceipt.receiptNumber" id="receiptNumber" cssClass="form-control"/>
					<form:errors path="propertyReceipt.receiptNumber" cssClass="add-margin error-msg" />
				</div>
				<div class="col-xs-2" align="right">
					<spring:message code="lbl.receipt.date" /><span class="mandatory"></span>
				</div>
				<div class="col-xs-2" align="center">
					<form:input path="propertyReceipt.receiptDate" id="receiptDate" cssClass="form-control datepicker" data-date-end-date="0d"/>
					<form:errors path="propertyReceipt.receiptDate" cssClass="add-margin error-msg" />
				</div>
				<div class="col-xs-2" align="right">
					<spring:message code="lbl.receipt.amount" /><span class="mandatory"></span>
				</div>
				<div class="col-xs-2" align="center">
					<form:input path="propertyReceipt.receiptAmount"  onblur="validNumber(this);checkZero(this);" id="receiptAmount" cssClass="form-control"/>
					<form:errors path="propertyReceipt.receiptAmount" cssClass="add-margin error-msg" />
				</div>
			</div>
			<div class="row">
				&nbsp;
			</div>
			<div class="row">
				&nbsp;
			</div>
			<div class="row">
				<div class="col-xs-2" align="right">
					&nbsp;
				</div>
				<div class="col-xs-2" align="right" >
					<spring:message code="lbl.remarks" /><span class="mandatory"></span>
				</div>
				<div class="col-xs-4" align="center">
					<form:textarea path="propertyReceipt.remarks" id="remarks" cssClass="form-control"/>
					<form:errors path="propertyReceipt.remarks" cssClass="add-margin error-msg" />
				</div>
			</div>
			<div class="row">
				&nbsp;
			</div>
			<div class="row">
				&nbsp;
			</div>
			<div class="row" align="center">
				<form:button type="submit" id="submit" class="btn btn-primary" value="submit" onclick="submitForm(this)">Submit</form:button>
				<form:button type="button" id="reset" class="btn btn-primary" value="reset" onclick="submitForm(this)">Reset</form:button>
				<input type="button" name="button2" id="button2" value="Close" class="btn btn-default" onclick="window.close();" />
			</div>
		</div>
	</div>
</form:form>