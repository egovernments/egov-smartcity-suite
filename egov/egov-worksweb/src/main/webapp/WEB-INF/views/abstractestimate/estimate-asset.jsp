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
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="header.assetDetails" />
		</div>
	</div>
	<div class="alert alert-danger">Select the nature of work before adding asset details</div>
	<div class="panel-body">
		<table class="table table-bordered" id="tblassetdetails">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno" /></th>
					<th><spring:message code="lbl.search" /></th>
					<th><spring:message code="lbl.assetcode" /></th>
					<th><spring:message code="lbl.nameofasset" /></th>
					<th><spring:message code="lbl.actions" /></th>
				</tr>
			</thead>
			<tbody id="assetDetailsTbl">
			<c:choose>
				<c:when test="${abstractEstimate.assetValues.size() == 0}">
				<tr id="assetDetailRow" onmouseover="changeColor(this, true);"	onmouseout="changeColor(this, false);">
				    <form:hidden path="assetValues[0].id" name="assetValues[0].id" value="${assetValues.id}" class="form-control table-input hidden-input" />
				    <form:hidden path="assetValues[0].asset.id" name="assetValues[0].asset.id" value="${assetValues.asset.id}" class="form-control table-input hidden-input" />
					<td><span id="sno" class="spansno" data-sno>1</span> 
					<td><button type="button" class="btn btn-xs btn-secondary searchAssetbtn"  data-optional="0"><span class="glyphicon glyphicon-search"></span> Search</button></td>
					<td><span id="assetcode[0]" class="assetdetail"></span><input type="hidden" id="assetValues[0].asset.code" name="assetValues[0].asset.code" value=""  data-optional="0" data-errormsg="Select asset details!"/></td>
					<td><span id="assetname[0]" class="assetdetail"></span><input type="hidden" id="assetValues[0].asset.name" name="assetValues[0].asset.name" value=""  /></td>
					<td><div class="text-left"><button type="button" onclick="deleteRow('tblassetdetails',this);" class="btn btn-xs btn-danger delete-row"  data-optional="0"><span class="glyphicon glyphicon-trash"></span> Delete</button></div></td>
				</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${abstractEstimate.getAssetValues()}" var="assetValues" varStatus="item">
					 <tr id="assetDetailRow" onmouseover="changeColor(this, true);" onmouseout="changeColor(this, false);">
						<form:hidden path="assetValues[${item.index}].id" name="assetValues[${item.index}].id" value="${assetValues.id}" class="form-control table-input hidden-input" />
						<form:hidden path="assetValues[${item.index}].asset.id" name="assetValues[${item.index}].asset.id" value="${assetValues.asset.id}" class="form-control table-input hidden-input" />
						<td><span id="sno" class="spansno" data-sno><c:out value="${item.index + 1}"/></span> 
					    <td><button type="button" class="btn btn-xs btn-secondary searchAssetbtn"  data-optional="0"><span class="glyphicon glyphicon-search"></span> Search</button></td>
					     <td><span class="assetdetail" id="assetcode[${item.index}]"><c:out value="${assetValues.asset.code}"/></span><input type="hidden" id="assetValues[${item.index}].asset.code" name="assetValues[${item.index}].asset.code" value="${assetValues.asset.code}"  data-optional="0" data-errormsg="Select asset details!"/></td>
						<td><span class="assetdetail" id="assetname[${item.index}]"><c:out value="${assetValues.asset.name}"/></span><input type="hidden" id="assetValues[${item.index}].asset.name" name="assetValues[${item.index}].asset.name" value="${assetValues.asset.name}"  /></td>
						<td><div class="text-left"><button type="button" onclick="deleteRow('tblassetdetails',this);" class="btn btn-xs btn-danger delete-row"  data-optional="0"><span class="glyphicon glyphicon-trash"></span> Delete</button></div>
					</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			</tbody>
		</table>
		<div class="col-sm-12 text-center">
			<button id="addRowBtn" type="button" class="btn btn-primary"
				onclick="addRow('tblassetdetails','assetDetailRow')">
				<spring:message code="lbl.addrow" />
			</button>
		</div>
	</div>
</div>
