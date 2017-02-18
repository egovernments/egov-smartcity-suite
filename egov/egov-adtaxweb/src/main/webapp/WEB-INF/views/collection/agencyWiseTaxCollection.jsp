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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/typeahead.css?rnd=${app_release_no}' context='/egi'/>">
<html>
<head>
<title><spring:message code="title.collect.Advtax.view" /></title>
<script type="text/javascript">

	 	jQuery(document).ready( function() {
	 		$('#agencysearch').click(function() {  
	 			if($('#totalAmountforSubmit').html()!=undefined &&  parseFloat($('#totalAmountforSubmit').html()) >0){
		 				var action = '/adtax/hoarding/collectTaxByAgency/' + $("#agencyName").val();
			 			$('#agencycollection').attr('method', 'post');
			 			$('#agencycollection').attr('action', action);
		 			}else
			 			{
			 			alert('Please select atleast one record.');
			 			return false;
			 			}
	 		
	 		});
		});
	 	
	  	function calculateGrandTotal(obj){
	 		var amountselected = 0;
		 	if($(obj).attr('id') == 'selectAll'){
			 	if(obj.checked){
			 		//select all these checkboxes
			 		$("#agencyWiseAmtPayTable tbody tr").each(function() {
				 		$('.checkboxselect').prop('checked', true);
				 		//console.log($(this).find('.amount').val());
				 		amountselected += parseFloat($(this).find('.amount').val());
			 	    });
			 	}else{
			 		//unselect all these checkboxes
			 		$("#agencyWiseAmtPayTable tbody tr").each(function() {
				 		$('.checkboxselect').prop('checked', false);
				 	//	console.log($(this).find('.amount').val());
				 		amountselected = 0;
			 	    });
			 	}
			 	$('#totalAmountforSubmit').html(amountselected);	
		 	}else{
		 		if(obj.checked){
					amountselected = parseFloat($(obj).parent().parent().find('.amount').val());
					if($('#totalAmountforSubmit').html()!=undefined)
					  $('#totalAmountforSubmit').html(parseFloat($('#totalAmountforSubmit').html())+amountselected);
					else
						$('#totalAmountforSubmit').html(amountselected);
					
		 			$('#selectAll').prop('checked', false);
				 	}else
					 	{
				 		amountselected = parseFloat($(obj).parent().parent().find('.amount').val());
				 		$('#totalAmountforSubmit').html(parseFloat($('#totalAmountforSubmit').html())- amountselected);
				 		$('#selectAll').prop('checked', false);
				 		 	}
 	 			}
		 	$('#totamount').removeClass('hide');

		 	
	 	}
	 	
		</script>
</head>
<body>
	<div class="row">
		<div class="col-md-12">
			<div id="totamount"
				style="position: fixed; z-index: 10; right: 30px; top: 100px; background: #dff0d8; padding: 10px;"
				class="hide">
				Total amount selected : <span id="totalAmountforSubmit">0</span>
			</div>
			<form:form id="agencycollection" method="post"
				class="form-horizontal form-groups-bordered"
				modelAttribute="agencyWiseCollectionSearch"
				commandName="agencyWiseCollectionSearch">
				<c:if test="${not empty message}">
					<div class="alert alert-success" role="alert">
						<spring:message code="${message}" />
					</div>
				</c:if>


				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading ">
						<div class="panel-title">
							<strong><spring:message code="title.collect.Advtax.view" /></strong>
						</div>
					</div>

					<div class="panel-body custom-form">
						<div class="form-group row add-border">
							<div class="col-md-3 col-xs-6 add-margin">
								&nbsp;&nbsp;&nbsp;&nbsp;<spring:message code="lbl.agency.name" />
							</div>
							<div class="col-md-3 col-xs-6 add-margin view-content">
								<c:out value="${agencyWiseCollectionSearch.agencyName}"></c:out>

								<input type=hidden id="agencyName" name="agencyName"
									value="${agencyWiseCollectionSearch.agencyName}">

							</div>
						</div>

					</div>


					<table id="agencyWiseAmtPayTable" style="clear: both" table
						width="100%" border="0" cellpadding="0" cellspacing="0"
						class="table table-bordered">
						<thead>
							<tr>
								<th><spring:message code="lbl.hoarding.no" /></th>
								<th>Owner Detail</th>
								<th><spring:message code="lbl.advertisement.application.no" /></th>
								<th><spring:message code="lbl.hoardingReport.taxReason" /></th>
								<th><spring:message code="lbl.additionalTax" /></th>
								<th><spring:message code="lbl.penalty" /></th>
								<th><spring:message code="lbl.dcbreport.total" /></th>
								<th><input type="checkbox" id="selectAll" name="selectAll"
									onchange="calculateGrandTotal(this);"> Select All</th>
							</tr>
						</thead>


						<tbody>

							<c:forEach var="contact"
								items="${agencyWiseCollectionSearch.agencyWiseCollectionList}"
								varStatus="status">
								<tr>

									<td><input type=hidden
										name="agencyWiseCollectionList[${status.index}].advertisementPermitId"
										value="${contact.advertisementPermitId}"> <input
										type="text" class="form-control patternvalidation"
										id="agencyWiseCollectionListAdvNum[${status.index}]"
										value="${contact.advertisementNumber}" maxlength="20"
										name="agencyWiseCollectionList[${status.index}].advertisementNumber"
										autocomplete="off" readonly="readonly"></td>
										<td><input type="text"
										class="form-control patternvalidation "
										id="agencyWiseCollectionListAppNumber[${status.index}]"
										value="${contact.ownerDetail}" maxlength="19"
										name="agencyWiseCollectionList[${status.index}].applicationNumber"
										autocomplete="off" readonly="readonly"></td>

									<td><input type="text"
										class="form-control patternvalidation "
										id="agencyWiseCollectionListAppNumber[${status.index}]"
										value="${contact.applicationNumber}" maxlength="19"
										name="agencyWiseCollectionList[${status.index}].applicationNumber"
										autocomplete="off" readonly="readonly"></td>

									<td><input type="text"
										class="form-control patternvalidation "
										id="agencyWiseCollectionListPendingAmt[${status.index}]"
										value="${contact.pendingDemandAmount}" maxlength="19"
										name="agencyWiseCollectionList[${status.index}].pendingDemandAmount"
										autocomplete="off" readonly="readonly"></td>
									<td><input type="text"
										class="form-control patternvalidation "
										id="agencyWiseCollectionListPendingAmt[${status.index}]"
										value="${contact.additionalTaxAmount}" maxlength="19"
										name="agencyWiseCollectionList[${status.index}].additionalTaxAmount"
										autocomplete="off" readonly="readonly"></td>
									<td><input type="text"
										class="form-control patternvalidation"
										id="agencyWiseCollectionListPenaltyAmt[${status.index}]"
										value="${contact.penaltyAmount}" maxlength="19"
										name="agencyWiseCollectionList[${status.index}].penaltyAmount"
										autocomplete="off" readonly="readonly"></td>
									<td><input type="text"
										class="form-control patternvalidation amount"
										id="agencyWiseCollectionListTotalAmt[${status.index}]"
										value="${contact.totalAmount}" maxlength="19"
										name="agencyWiseCollectionList[${status.index}].totalAmount"
										autocomplete="off" readonly="readonly"></td>
									<td><input type="checkbox"
										id="agencyWiseCollectionListSelected[${status.index}]"
										class="checkboxselect"
										name="agencyWiseCollectionList[${status.index}].selectedForCollection"
										onchange="calculateGrandTotal(this);"></td>


								</tr>
							</c:forEach>

						</tbody>
					</table>
				</div>
				<div class="text-center">
					<button type="submit" id="agencysearch" class="btn btn-primary">
						<spring:message code="lbl.collectFee" />
					</button>
					<a href="javascript:void(0)" class="btn btn-default"
						onclick="self.close()"><spring:message code="lbl.close" /></a>
				</div>

			</form:form>
		</div>
	</div>
</body>
</html>
