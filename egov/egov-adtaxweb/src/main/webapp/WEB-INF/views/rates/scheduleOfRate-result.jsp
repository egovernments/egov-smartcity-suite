<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>">
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/bootstrap/typeahead.css' context='/egi'/>">

<script type="text/javascript"
	src="<c:url value='/commonjs/ajaxCommonFunctions.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<c:url value='/global/js/egov/patternvalidation.js' context='/egi'/>"></script>
<script>
	function deleteRow(obj) {
		var tb1 = document.getElementById("schedleOfrateTable");
		var lastRow = (tb1.rows.length) - 1;
		var j;
		var curRow = getRow(obj).rowIndex;

			if (curRow==1||lastRow == 1) {
			alert('You cannot delete this row.');
			return false;
		} else {
		
			for (j =curRow; j < lastRow; j++) {
				$("#advertisementRatesDetailsId"+j).attr('name',"advertisementRatesDetails["+(j-1)+"].id");
				$("#advertisementRatesDetailsId"+j).attr('id',"advertisementRatesDetailsId"+(j-1));

				$("#advertisementRatesDetailsUnitFrom"+j).attr('name',"advertisementRatesDetails["+(j-1)+"].unitFrom");
				$("#advertisementRatesDetailsUnitFrom"+j).attr('id',"advertisementRatesDetailsUnitFrom"+(j-1));

				$("#advertisementRatesDetailsUnitTo"+j).attr('name',"advertisementRatesDetails["+(j-1)+"].unitTo");
				$("#advertisementRatesDetailsUnitTo"+j).attr('id',"advertisementRatesDetailsUnitTo"+(j-1));

				$("#advertisementRatesDetailsAmount"+j).attr('name',"advertisementRatesDetails["+(j-1)+"].amount");
				$("#advertisementRatesDetailsAmount"+j).attr('id',"advertisementRatesDetailsAmount"+(j-1));
			}
			tb1.deleteRow(curRow);
			$( "#schedleOfrateTable tr:last .delete-button").show(); 
			$( "#schedleOfrateTable tr:last .unit-to").prop("readonly", false);
    	    
			return true;
		}
	}
</script>	
<div class="row">
	<div class="col-md-12">
		<form:form id="scheduleOfRateform" method="post" class="form-horizontal form-groups-bordered" modelAttribute="rate" commandName="rate">
				<c:if test="${not empty message}">
                   <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
            </c:if>
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading ">
					<div class="panel-title">
						<strong><spring:message code="title.scheduleofrates"/></strong>
					</div>
				</div>
				<div class="panel-body custom-form"> 										
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label"> <spring:message
								code="lbl.category.name" /></label>
						<div class="col-sm-3 add-margin">
							<input type=hidden id="mode" value="${mode}">
							<c:out value="${rate.category.name}"></c:out>
						</div>
						
						<label for="field-1" class="col-sm-2 control-label"><spring:message
										code="lbl.subcategory.name" /></label>
						<div class="col-sm-3 add-margin">
							<c:out value="${rate.subCategory.description}"></c:out>
						
						</div>
				</div>
				 <div class="form-group">			
						<label for="field-1" class="col-sm-3 control-label"> <spring:message
								code="lbl.unitofmeasure.name" /></label>
						<div class="col-sm-3 add-margin">
							  <form:hidden path="id" id="id" value="${rate.id}"/>
							<c:out value="${rate.unitofmeasure.description}"></c:out>
						</div>
						
						<label for="field-1" class="col-sm-2 control-label"> <spring:message
								code="lbl.rateClass.name" /></label>
						<div class="col-sm-3 add-margin">
									<c:out value="${rate.classtype.description}"></c:out>
			
						</div>
			</div>
			
			    	<div class="row">
				<div class="text-center">
					<button type="submit" id="scheduleOfRateSearchAgain" class="btn btn-primary">Search Again</button>
				    <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
				</div>
			</div>
			
			<div id="noscheduleofrateDataFoundDiv" class="row container-msgs">
				<c:if test="${mode == 'noDataFound'}">
					<div class="form-group">
						<div class="text-center">
							<div class="panel-title view-content">
								<spring:message code="lbl.scheduleorrate.label.NodataFound" />
							</div>
							<br>
							<button type="button" id="addnewscheduleofrate"
								class="btn btn-primary">
								<spring:message
									code="lbl.scheduleorrate.button.addnewscrate" />
							</button>
						</div>
					</div>
				</c:if>
			</div>
	</form:form>
	<form:form id="scheduleOfRateformResult" method="post" class="form-horizontal form-groups-bordered" modelAttribute="rate" commandName="rate">
			
		<div id="schedleOfrateDiv" class="hidden">	
					<table id="schedleOfrateTable" table width="100%" border="0" cellpadding="0" cellspacing="0" class="table table-bordered">
					<thead>
						<tr>
							<th><spring:message code="lbl.scheduleorrate.unitfrom" /></th>
							<th><spring:message code="lbl.scheduleorrate.unitto" /></th>
							<th><spring:message code="lbl.scheduleorrate.rate" /></th>
							<th><spring:message code="lbl.scheduleorrate.action" /></th>
										</tr>
					</thead>
				<tbody>
					    
					    <form:hidden path="id" id="id" value="${rate.id}"/>
						<form:hidden path="category" id="category" value="${rate.category.id}"/>
						<form:hidden path="subCategory" id="subCategory" value="${rate.subCategory.id}"/> 
						<form:hidden path="unitofmeasure" id="unitofmeasure" value="${rate.unitofmeasure.id}"/>
						<form:hidden path="classtype" id="classtype" value="${rate.classtype.id}"/>
				
						<c:forEach var="contact" items="${rate.advertisementRatesDetails}" varStatus="status">
						<tr>
							<td>
							<%-- <input type=hidden
									id="advertisementRatesDetailsId${status.index}"
									name="advertisementRatesDetails[${status.index}].id"
									value="${contact.id}">  --%>
						<c:if test="${mode == 'noDataFound'}">
							<input type="text" class="form-control patternvalidation unit-from" data-pattern="decimalvalue"
									id="advertisementRatesDetailsUnitFrom${status.index}"
									value="0"  maxlength="10"
									name="advertisementRatesDetails[${status.index}].unitFrom"
									 autocomplete="off" required="required"  readonly="readonly">
							</c:if>	
							<c:if test="${mode == 'dataFound'}">		 
							<input type="text" class="form-control patternvalidation unit-from" data-pattern="decimalvalue"
									id="advertisementRatesDetailsUnitFrom${status.index}"
									value="${contact.unitFrom}"  maxlength="10"
									name="advertisementRatesDetails[${status.index}].unitFrom"
									 autocomplete="off" required="required"  readonly="readonly">
									 
									</c:if>	 		 
								</td>	
							<td>	 <input type="text" class="form-control patternvalidation unit-to" data-pattern="decimalvalue"
									id="advertisementRatesDetailsUnitTo${status.index}"
									value="${contact.unitTo}"  maxlength="10"
									name="advertisementRatesDetails[${status.index}].unitTo" onblur="return validateUnitToValue(this);"
									 autocomplete="off" required="required"  readonly="readonly" >
									</td>	
								<td>	 <input type="text" class="form-control patternvalidation unit-amount" data-pattern="decimalvalue"
									id="advertisementRatesDetailsAmount${status.index}"
									value="${contact.amount}"  maxlength="19"
									name="advertisementRatesDetails[${status.index}].amount"
									 autocomplete="off" required="required"  >
									</td>	 	 
									<td>
										<button type="button" onclick="deleteRow(this)" id="Add"
										class="btn btn-primary display-hide delete-button"><spring:message code="lbl.delete" /></button>
									</td>
								</tr>
						</c:forEach>
						<script>
						$( "#schedleOfrateTable tr:last .delete-button").show();
						$( "#schedleOfrateTable tr:last .unit-to").prop("readonly", false);
					    
						</script>
					</tbody>
				</table>
				<div class="form-group">
					<div class="text-center">
						<br><br>
						<button type="button" id="btn-add" class="btn btn-primary btn-add"><spring:message code="lbl.add.row"/></button>
						<button type="submit" class="btn btn-primary schedleOfrateBtn" 
						onclick="return checkUniqueDesignationSelected();" id="schedleOfrateBtn">
							<spring:message code="lbl.update"/>
						</button>
						<a href="javascript:void(0);" onclick="self.close()" class="btn btn-default">
							<spring:message code="lbl.close"/></a>
					</div>
				</div>
			</div>
		</form:form>
			  
		
			  
	</div>
</div>
<script src="<c:url value='/resources/app/js/scheduleOfRates.js'/>"></script>