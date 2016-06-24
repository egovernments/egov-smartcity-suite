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
<div id="baseSORTable" class="panel panel-primary" data-collapsed="0">
	<input type="hidden" id="errorquantitieszero" value="<spring:message code='error.quantity.zero' />">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="title.mb.details" />
			<div class="pull-right">
				<a id="searchAndAdd" href="javascript:void(0);" class="btn btn-primary">
					<i class="fa fa-plus"></i><spring:message code="lbl.add" />
				</a>
				<a id="addAll" href="javascript:void(0);" class="btn btn-primary">
					<i class="fa fa-plus"></i><spring:message code="lbl.addall" />
				</a>
			</div>
		</div>
	</div>
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.sor" />
		</div>
	</div>
	<div class="panel-body" id="sorHeaderTable">
		<table class="table table-bordered" style="overflow: auto;" id="tblsor">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.sor.category" /></th>
					<th><spring:message code="lbl.sorcode" /></th>
					<th><spring:message code="lbl.description.item" /></th>
					<th><spring:message code="lbl.uom" /></th>
					<th><spring:message code="lbl.approved.quantity" /></th>
					<th><spring:message code="lbl.approved.rate" /></th>
					<th><spring:message code="lbl.cumulative.previous.entry" /></th>
					<th><spring:message code="lbl.current.entry" /></th>
					<th><spring:message code="lbl.cumulative.quantity.current.entry" /></th>
					<th><spring:message code="lbl.amount.current.entry" /></th>
					<th><spring:message code="lbl.cumulative.amount.current.entry" /></th>
					<th><spring:message code="lbl.approved.amount" /></th>
					<th><spring:message code="lbl.remarks" /></th>
					<th><spring:message code="lbl.delete" /></th>
				</tr>
			</thead>
			<tbody id="sorTable">
				<tr id="message">
					<td colspan="15"><spring:message code="msg.mb.sor.table"/></td>
				</tr>
				<tr id="sorRow" class="sorRow" sorinvisible="true" hidden="true" align="center">
					<td>
						<span class="spansorslno">1</span>
						<input type="hidden" name="sorMbDetails[0].id" id="sorMbDetailsid_0" class="sorMbDetailsId" />
						<input type="hidden" name="sorMbDetails[0].workOrderActivity.id" id="workOrderActivity_0" class="workOrderActivity" />
					</td>
					<td>
						<span class="sorCategory_0"></span>
					</td>
					<td>
						<span class="sorCode_0"></span>
					</td>
					<td align="left">
						<span class="summary_0"></span>
						<span class="hintanchor description_0"/></span>
					</td>
					<td>
						<span class="uom_0"></span>
					</td>
					<td>
						<span class="approvedQuantity_0"></span>
					</td>
					<td>
						<span class="approvedRate_0"></span>
					</td>
					<td>
						<span class="cumulativePreviousEntry_0"></span>
					</td>
					<td>
						<input name="sorMbDetails[0].quantity" id="quantity_0" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" required="required" class="form-control table-input text-right quantity" maxlength="64" onblur="calculateSorAmounts(this);" onkeyup="validateQuantityInput(this);"/>
						<input type="hidden" name="sorMbDetails[0].rate" id="unitRate_0" class="form-control table-input text-right"/>
					</td>
					<td>
						<span class="cumulativeIncludingCurrentEntry_0"></span>
					</td>
					<td>
						<span class="amountCurrentEntry_0"></span>
					</td>
					<td>
						<span class="amountIncludingCurrentEntry amountIncludingCurrentEntry_0"></span>
					</td>
					<td>
						<span class="approvedAmount_0"></span>
					</td>
					<td>
						<textarea name="sorMbDetails[0].remarks" id="remarks_0" data-idx="0" data-optional="1" class="form-control table-input" maxlength="1024"></textarea>
					</td>
					<td>
						<span class="add-padding delete_0" onclick="deleteSor(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
					</td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="11" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"> <span id="sorTotal">0.00</span> </td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
