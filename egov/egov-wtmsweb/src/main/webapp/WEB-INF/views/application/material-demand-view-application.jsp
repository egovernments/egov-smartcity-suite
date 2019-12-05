<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
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
  
  <%@ page contentType="text/html; charset=UTF-8" language="java"%>
  <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
  <%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  <%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
  <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
  
  <div class="row">
  	<div class="col-md-12">
  		<form:form method="post" role="form" id="materialDemandApplicationUpdateForm" modelAttribute="waterApplicationDetails" 
  		 action="" cssClass="form-horizontal form-groups-bordered">
  			<input type="hidden" name="applicationNumber" value="${waterConnectionDetails.applicationNumber}" />
  			<input type="hidden" id="ulbMaterialValue" name="ulbMaterialValue" value="${waterConnectionDetails.ulbMaterial}" /> 
			<input type="hidden" id="validationMessage" name="validationMessage" value="${validationMessage}" /> 
  			<div class="panel panel-primary">
  				<div class="panel-heading">
  					<c:if test="${mode=='success'}">
  						<div class="alert alert-success" style="color:#3c763d; background-color:#dff0d8; border-color:#d6e9c6;">
  							<spring:message code="${message}"/>
  						</div>
  					</c:if>
  					<div class="panel-title">
  						<spring:message code="lbl.fieldinspection.details"/>
  					</div>
  				</div>
  				<div class="panel-body">
  					<div class="form-group">
  						<table class="table table-bordered table-hover" id="estimateDetails">
  							<thead>
  								<tr>
  									<th><spring:message code="lbl.slno"/></th>
  									<th><spring:message code="lbl.material"/></th>
  									<th><spring:message code="lbl.quantity"/></th>
  									<th><spring:message code="lbl.uom"/></th>
  									<th><spring:message code="lbl.rate"/></th>
  									<th><spring:message code="lbl.amount"/></th>
  								</tr>
  							</thead>
  							<tbody>
  								<c:forEach var="estimationDetails" items="${waterConnectionDetails.estimationDetails}" varStatus="status">
  									<tr>
  										<td class="text-center"><span id="serialNumber">${status.index+1}</span></td>
  										<td><text:area class="form-control table-input" id="itemDescription" readOnly="readOnly" value="${estimationDetails.itemDescription}">${estimationDetails.itemDescription}</text:area></td>
  										<td class="text-right"><input type="text" class="form-control table-input text-right" data-pattern="decimalvalue" name="estimationDetails[${status.index}].quantity" id="estimationDetails${status.index}quantity" maxlength="8" onblur="calculateTotalAmount();" value="${estimationDetails.quantity}" readOnly="readonly"></td>
										<td class="text-right"><input type="text" class="form-control table-input patternvalidation" data-pattern="alphanumerichyphenbackslash" name="estimationDetails[${status.index}].unitOfMeasurement" id="estimationDetails${status.index}unitOfMeasurement" maxlength="50" value="${estimationDetails.unitOfMeasurement}" readOnly="readonly"></td>
										<td class="text-right"><input type="text" class="form-control table-input text-right patternvalidation unitrate" data-pattern="decimalvalue" name="estimationDetails[${status.index}].unitRate" id="estimationDetails${status.index}unitRate" maxlength="8" onblur="calculateTotalAmount();" value="${estimationDetails.unitRate}" readOnly="readonly"></td>
										<td class="text-right"><input type="text" class="form-control table-input text-right" id="estimationDetails${status.index}amount" readOnly="readonly" "></td>
					      			</tr>
  								</c:forEach>
  								<tr>
									<td class="text-center"></td>
									<td class="text-center"></td>
									<td class="text-right"></td>
									<td class="text-right"></td>
									<td class="text-right"><spring:message code="lbl.grandtotal" /></td>
									<td class="text-right"><input type="text" class="form-control text-right" id="grandTotal" readOnly="readonly"></td>
					     		 </tr>
  							</tbody>
  							<tfoot>
  							</tfoot>
  						</table>
  					</div>
  					<br/>
  					<br/>
  					<div class="row add-border">
  						<div class="col-xs-2 add-margin"><spring:message code="lbl.application.no"/></div>
  						<div class="col-xs-3 add-margin view-content">
  							<c:out value="${waterConnectionDetails.applicationNumber}"/>
  						</div>
  						<label class="col-sm-3 control-label"><span class="mandatory"></span><spring:message code="lbl.materials.supplied.by.ulb"/></label>
  						<div class="col-sm-3 add-margin dropdown-div">
  							<select class="form-control" id="ulbMaterial" name="ulbMaterial" required="required">
  								<option value=""></option>
  								<option value="YES"><c:out value="YES"/></option>
  								<option value="NO"><c:out value="NO"/></option>
  							</select>
  						</div>
  					</div>
  					</div>
  					<br/>
  					<br/>
  					<div class="form-group">
  						<div class="text-center">
  						<c:if test="${waterConnectionDetails.ulbMaterial==null}">
  							<button type="submit" id="submit" class="btn btn-primary" value="Save"><spring:message code="lbl.save.button"/></button>
  						</c:if>
  							<a href="javascript:void(0)" onclick="self.close()" class="btn btn-default">
  								<spring:message code="lbl.close"/>
  							</a>
  						</div>
  					</div>
  				</div>
  		</form:form>
  	</div>
 </div>
 <table id="application-search-result-table" class="table table-bordered table-hover multiheadertbl" width="200%">
 </table>
 
 
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/js/app/estimatedetails.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/js/app/material-demand-generation.js?rnd=${app_release_no}'/>"></script>
 
  