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
<div id="baseNonSORTable" class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.nonsor" />
		</div>
	</div>
	<div class="panel-body" id="sorHeaderTable">
		<table class="table table-bordered" style="overflow: auto;" id="tblnonsor">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
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
			<tbody id="nonSorTable">
				<tr id="message">
					<td colspan="15"><spring:message code="msg.mb.sor.table"/></td>
				</tr>
				<tr id="nonSorRow" class="nonSorRow" nonsorinvisible="true" hidden="true" align="center">
					<td>
						<span class="spanslno">1</span>
						<input type="hidden" name="nonSorMbDetails[0].id" id="nonSorMbDetails_0" class="nonSorMbDetailsId" />
						<input type="hidden" name="nonSorMbDetails[0].workOrderActivity.id" id="nonSorWorkOrderActivity_0" class="nonSorWorkOrderActivity" />
					</td>
					<td>
						<span class="nonSorCategory_0"></span>
					</td>
					<td>
						<span class="nonSorCode_0"></span>
					</td>
					<td align="left">
						<span class="nonSorSummary_0"></span>
						<span class="hintanchor nonSorDescription_0"/></span>
					</td>
					<td>
						<span class="nonSorUom_0"></span>
					</td>
					<td>
						<span class="nonSorApprovedQuantity_0"></span>
					</td>
					<td>
						<span class="nonSorApprovedRate_0"></span>
					</td>
					<td>
						<span class="nonSorCumulativePreviousEntry_0"></span>
					</td>
					<td>
						<input name="nonSorMbDetails[0].quantity" id="nonSorQuantity_0" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" required="required" class="form-control table-input text-right quantity" maxlength="64" onblur="calculateEstimateAmount(this);" onkeyup="validateQuantityInput(this);"/>
					</td>
					<td>
						<span class="nonSorCumulativeIncludingCurrentEntry_0"></span>
					</td>
					<td>
						<span class="nonSorAmountCurrentEntry_0"></span>
					</td>
					<td>
						<span class="nonSorAmountIncludingCurrentEntry_0"></span>
					</td>
					<td>
						<span class="nonSorApprovedAmount_0"></span>
					</td>
					<td>
						<textarea name="nonSorMbDetails[0].remarks" id="nonSorRemarks_0" data-idx="0" data-optional="1" class="form-control table-input" maxlength="1024"></textarea>
					</td>
					<td>
						<span class="add-padding nonSorDelete_0" onclick="deleteSor(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
					</td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="10" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"> <span id="nonSorEstimateTotal">0.00</span> </td>
					<td></td>
					<td></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
