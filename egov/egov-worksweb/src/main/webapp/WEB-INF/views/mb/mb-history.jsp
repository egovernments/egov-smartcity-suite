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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<style>
	
	.msheet-th {
		background: #E7E7E7;
		color: #333;
		font-weight: normal;
	}
	
	
</style>
<div class="panel panel-primary" data-collapsed="0" style="text-align: left">  

	<table class="table" >
	<thead>
	</thead>
	<tbody>
		<tr>
			<c:forEach items="${mbHistorys}" var="history" varStatus="item">
					<td>
						<table class="table table-bordered" >
						 <thead>
				  			<tr>
				  				<td colspan="4">${history.ownerName}</td>
				  				<td><spring:message code="lbl.status" /></td>
				  				<td>${history.status}</td>
				  				<td><spring:message code="lbl.date.time" /></td>
				  				<td colspan="2">${history.dateTime}</td>
				  				<td><spring:message code="lbl.mbamount" /></td>
				  				<td colspan="2" ><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${history.mbAmount}" /></td>
				  			</tr>
							</thead>
							<tbody>
				  			<tr>
				  				<td colspan="12">
									<%@ include file="mb-history-tendereditems.jsp" %>
									<%@ include file="mb-history-nontendereditems.jsp" %>
								</td>
							</tr>
						    </tbody>
						</table> 
					</td>
			</c:forEach>
		</tr>
	</tbody>
	<tfoot>
	</tfoot>
	</table>
</div>
<script type="text/javascript" src="<cdn:url value='/resources/js/mb/mbhistory.js?rnd=${app_release_no}'/>"></script>