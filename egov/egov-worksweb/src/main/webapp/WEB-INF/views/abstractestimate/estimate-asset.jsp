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
	<div class="alert alert-danger"><spring:message code="msg.alert.selectnaruteofusage" /></div>
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
				<c:when test="${abstractEstimate.tempAssetValues.size() == 0}">
				<tr id="assetDetailRow">
				    <form:hidden path="tempAssetValues[0].id" name="tempAssetValues[0].id" value="${assetValues.id}" class="form-control table-input hidden-input" />
				    <form:hidden path="tempAssetValues[0].asset.id" name="tempAssetValues[0].asset.id" value="${assetValues.asset.id}" class="form-control table-input hidden-input" />
					<td><span id="sno" class="spansno" data-sno>1</span> 
					<td><button type="button" class="btn btn-xs btn-secondary searchAssetbtn"  data-idx="0" data-optional="0"><span class="glyphicon glyphicon-search"></span> Search</button></td>
					<input type="hidden" id="tempAssetValues[0].asset.code" name="tempAssetValues[0].asset.code" value=""  data-optional="0" data-errormsg="Select asset details!"/>
					<td><a id="assetcode[0]" class="viewAsset" href='javascript:void(0);'></a>
					<br><form:errors path="tempAssetValues[0].asset.code" cssClass="add-margin error-msg" />
					</td>
					<td><span id="assetname[0]" class="assetdetail"></span>
					<input type="hidden" id="tempAssetValues[0].asset.name" name="tempAssetValues[0].asset.name" value=""  />
					<br><form:errors path="tempAssetValues[0].asset.name" cssClass="add-margin error-msg" />
					</td>
					<td><div class="text-left"><button type="button" onclick="deleteRow('tblassetdetails',this);" class="btn btn-xs btn-danger delete-row"  data-optional="0"><span class="glyphicon glyphicon-trash"></span> Delete</button></div></td>
				</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${abstractEstimate.getTempAssetValues()}" var="assetValues" varStatus="item">
					 <tr id="assetDetailRow">
						<form:hidden path="tempAssetValues[${item.index}].id" name="tempAssetValues[${item.index}].id" value="${assetValues.id}" class="form-control table-input hidden-input" />
						<form:hidden path="tempAssetValues[${item.index}].asset.id" name="tempAssetValues[${item.index}].asset.id" value="${assetValues.asset.id}" class="form-control table-input hidden-input" />
						<td><span id="sno" class="spansno" data-sno><c:out value="${item.index + 1}"/></span> 
					    <td><button type="button" class="btn btn-xs btn-secondary searchAssetbtn" data-idx="${item.index}" data-optional="0"><span class="glyphicon glyphicon-search"></span> Search</button></td>
					    <td><a id="assetcode[${item.index}]" class="viewAsset" href='javascript:void(0)' ><c:out value="${assetValues.asset.code}"/></a>
					    <br><form:errors path="tempAssetValues[${item.index}].asset.code" cssClass="add-margin error-msg" /></td>
					     <input type="hidden" id="tempAssetValues[${item.index}].asset.code" name="tempAssetValues[${item.index}].asset.code" value="${assetValues.asset.code}"  data-optional="0" data-errormsg="Select asset details!"/>
						<td><span class="assetdetail" id="assetname[${item.index}]"><c:out value="${assetValues.asset.name}"/></span>
						<br><form:errors path="tempAssetValues[${item.index}].asset.name" cssClass="add-margin error-msg" />
						<input type="hidden" id="tempAssetValues[${item.index}].asset.name" name="tempAssetValues[${item.index}].asset.name" value="${assetValues.asset.name}"  /></td>
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
