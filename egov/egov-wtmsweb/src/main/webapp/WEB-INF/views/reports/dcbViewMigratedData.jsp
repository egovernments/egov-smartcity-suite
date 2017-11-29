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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<form:form method ="post" action="" class="form-horizontal form-groups-bordered"  id="dcbmig-view"
			cssClass="form-horizontal form-groups-bordered"
			enctype="multipart/form-data">
			
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
						</div>
					
					<div class="panel-body custom-form ">
					<div class="form-group" align ="center">
								For Consumer Number :<c:out value="${consumerNumber}" />
								</div>
 					
	              <c:choose>
    <c:when test="${waterChargesReceiptInfo.isEmpty()}">
    <div class="form-group" align ="center">
       No Receipts are available        
     </div>
     </c:when>
    <c:otherwise>
      <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="table table-bordered">
                  <thead>
					<tr>
						<th colspan="6">
                         Receipts
						</th>
					</tr>
					<tr>
						<th colspan="1" >
							<div align="center">
						 <spring:message code="lbl.bookno" /> 
						    </div>
					
							
						</th>
						<th   colspan="1" >
						   <div align="center">
						<spring:message code="lbl.receiptNo" /> 
						    </div>
							
						</th>
						<th   colspan="1" >
						   <div align="center">
						<spring:message code="lbl.receiptDate" />
						   </div>
							
						</th>
						<th  align="center" colspan="1" >
						   <div align="center">
						<spring:message code="lbl.fromDate" />
						    </div>
						
						</th>
						<th   colspan="1" >
						   <div align="center">
						<spring:message code="lbl.toDate" />
						    </div>
					
						</th>
						<th   colspan="1" >
						    <div align="center">
						<spring:message code="lbl.receiptAmt" />
						    </div>
						
						</th>
					</tr>
					</thead>
					<c:forEach var="receipt" items="${waterChargesReceiptInfo}" >
					<tr>
							<td  colspan="1" >
								<div align="center">
								<c:out value="${receipt.bookNumber}" />
								<!-- 	<s:property value="bookNumber" /> -->
								</div>
							</td>
							<td  colspan="1" >
								<div align="center">
								<c:out value="${receipt.receiptNumber}" />
								
								</div>
							</td>
							<td  colspan="1" >
								<div align="center">
								<fmt:formatDate pattern="dd-MM-yyyy" value="${receipt.receiptDate}"/>
								</div>
							</td>
							<td  colspan="1" >
								<div align="center">
								<fmt:formatDate pattern="dd-MM-yyyy" value="${receipt.fromDate}"/>
								</div>
							</td>
							<td  colspan="1" >
								<div align="center">
								<fmt:formatDate pattern="dd-MM-yyyy" value="${receipt.toDate}"/>
								</div>
							</td>
							<td  colspan="1">
								<div align="center">
								<c:out value="${receipt.receiptAmount}" />
								
								</div>
							</td>
						</tr>
						</c:forEach> 	
					</table>
    </c:otherwise>
     </c:choose>
     <div class="form-group text-center" >
								
								<a onclick="self.close()" class="btn btn-default" href="javascript:void(0)"><spring:message code="lbl.close"/></a>
							</div>
     </div>
     </div>
    
       
					
		</form:form>	
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.columnFilter.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>" ></script>
