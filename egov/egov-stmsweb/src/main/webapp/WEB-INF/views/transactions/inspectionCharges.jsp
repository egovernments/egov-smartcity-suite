<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~      accountability and the service delivery of the government  organizations.
  ~
  ~       Copyright (C) 2016  eGovernments Foundation
  ~
  ~       The updated version of eGov suite of products as by eGovernments Foundation
  ~       is available at http://www.egovernments.org
  ~
  ~       This program is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       any later version.
  ~
  ~       This program is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU General Public License for more details.
  ~
  ~       You should have received a copy of the GNU General Public License
  ~       along with this program. If not, see http://www.gnu.org/licenses/ or
  ~       http://www.gnu.org/licenses/gpl.html .
  ~
  ~       In addition to the terms of the GPL license to be adhered to in using this
  ~       program, the following additional terms are to be complied with:
  ~
  ~           1) All versions of this program, verbatim or modified must carry this
  ~              Legal Notice.
  ~
  ~           2) Any misrepresentation of the origin of the material is prohibited. It
  ~              is required that all modified versions of this material be marked in
  ~              reasonable ways as different from the original version.
  ~
  ~           3) This license does not grant any rights to any user of the program
  ~              with regards to rights under trademark law for use of the trade names
  ~              or trademarks of eGovernments Foundation.
  ~
  ~     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

 <div class="panel panel-primary" data-collapsed="0"> 
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.fees.details" />
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-striped table-bordered" id="inspectionChargesDetails" style="width:50%;margin:0 auto;">
			<thead>
			      <tr>
			        <th class="text-center"><spring:message code="lbl.slno" /></th>
					<th style="width:50%;"><spring:message code="lbl.feestype" /></th>
					<th class="text-right"><spring:message code="lbl.amount" /></th>
			      </tr>
		    </thead>
		<tbody> 

		<c:forEach items="${sewerageApplicationDetails.connectionFees}" var="fm" varStatus="counter">  
			       	<tr class=""> 
								<td class="text-center"><span id="slNo1">${counter.index+1}</span></td>
							<td >
							<c:out value="${fm.feesDetail.description}"/><c:if test="${fm.feesDetail.isMandatory == 'true'}"><span class="mandatory"></span>  </c:if> </td>   
							<td class="text-right">  
									<form:hidden path="connectionFees[${counter.index}].id" value="${fm.id}"></form:hidden>      
									<form:hidden path="connectionFees[${counter.index}].feesDetail" value="${fm.feesDetail.id}"></form:hidden>
									<form:hidden path="connectionFees[${counter.index}].applicationDetails" value="${fm.applicationDetails.id}"></form:hidden> 
									
							<c:choose>			
							  <c:when test="${fm.feesDetail.isFixedRate == 'true'}">  	
									<form:input type="text" class="form-control table-input text-right patternvalidation" data-pattern="decimalvalue" path="connectionFees[${counter.index}].amount" id="feesDetail${counter.index}amount"  value="${fm.amount}" readonly="true" /> 
						      </c:when>  
						  <c:otherwise>  
						    <c:choose>
							   <c:when test="${mode == 'edit'}">  
							     <c:choose>
							       <c:when test="${sewerageApplicationDetails.connectionFees[counter.index].feesDetail.code == 'INSPECTIONCHARGE' }">
							        	<form:input type="text" class="form-control table-input text-right patternvalidation" data-pattern="decimalvalue" path="connectionFees[${counter.index}].amount" id="feesDetail${counter.index}amount"  value="${fm.amount}" readonly="true" /> 
									</c:when>  
						          	<c:otherwise>  
						          	   <c:choose> 
							           	<c:when test="${fm.feesDetail.isMandatory == 'true'}">
								     	     <form:input type="text" class="form-control table-input text-right patternvalidation" data-pattern="decimalvalue" path="connectionFees[${counter.index}].amount" id="feesDetail${counter.index}amount"  value="${fm.amount}" required="required" /> 
								     	 </c:when>
								     	<c:otherwise>   
								     	    <form:input type="text" class="form-control table-input text-right patternvalidation" data-pattern="decimalvalue" path="connectionFees[${counter.index}].amount" id="feesDetail${counter.index}amount"  value="${fm.amount}" /> 
								        </c:otherwise> 	
								       </c:choose> 
								    </c:otherwise>
								</c:choose>
							    </c:when>
							    <c:when test="${mode == null|| mode==''}">  
							       <c:choose> 
							     	<c:when test="${fm.feesDetail.isMandatory == 'true'}">
							     	     <form:input type="text" class="form-control table-input text-right patternvalidation" data-pattern="decimalvalue" path="connectionFees[${counter.index}].amount" id="feesDetail${counter.index}amount"  value="${fm.amount}" required="required" /> 
							     	 </c:when>
							     	<c:otherwise>  
							     	<c:choose> 
							       <c:when test="${legacy && sewerageApplicationDetails.connectionFees[counter.index].feesDetail.code == 'DONATIONCHARGE' }">
								     	     <input type="text" class="form-control table-input text-right patternvalidation" data-pattern="number" id="feesDetail${counter.index}amount" name="connectionFees[${counter.index}].amount" value="<fmt:formatNumber type="number" pattern="0" maxFractionDigits="0" value="${fm.amount}" />" required="required" /> 
								     	 </c:when>
								     	<c:otherwise>  
							     	    <input type="text" class="form-control table-input text-right patternvalidation" data-pattern="number" name="connectionFees[${counter.index}].amount" id="feesDetail${counter.index}amount"  value="<fmt:formatNumber type="number" pattern="0" maxFractionDigits="0" value="${fm.amount}" />" /> 
							     	      </c:otherwise> 	
								       </c:choose>
							        </c:otherwise> 	
							       </c:choose>
							   </c:when> 
							     <c:when test="${mode !='edit'}">  
							       			<form:input type="text" class="form-control table-input text-right patternvalidation"  data-pattern="decimalvalue" path="connectionFees[${counter.index}].amount" id="feesDetail${counter.index}amount"  value="${fm.amount}" readonly="true" /> 
							   </c:when> 
						   </c:choose>	
						 </c:otherwise> 	 
						      </c:choose>	
							</td> 
					    </tr>  
			
			</c:forEach>	

		</tbody>
			<c:if test="${legacy && isDonationChargeCollectionRequired }">
				<tr>
					<td class="text-right" colspan="2"><span class="mandatory"
						id="collectedDonationAmount">Collected Donation charges</span></td>
					<td><input type="text"
						class="form-control table-input text-right patternvalidation" data-pattern="number"
						name="amountCollected" value="<fmt:formatNumber type="number" pattern="0" maxFractionDigits="0" value="${amountCollected}" />"  maxlength="6" id="amountCollected"
						required="required" /></td>
				</tr>
				<tr>
					<td class="text-right" colspan="2" id="BalanceAmount">Balance
						Donation charges</td>

					<td><input type="text" name="donationAmountForCollection" value="${pendingAmtForCollection}" data-pattern="number" 
						class="form-control table-input text-right patternvalidation"
						id="pendingAmtForCollection" readonly="readonly"  /></td>
				</tr>
			</c:if>
		</table>
	</div>
</div>