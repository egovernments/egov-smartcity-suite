<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
			<spring:message code="title.estimatetemplate" />
		</div>
	</div>
	<div class="form-group">
		<input type="hidden" id="msgschedulecategory" value="<spring:message code='msg.select.scheduleofcategory' />">
		<input type="hidden" id="erroradded" value="<spring:message code='error.sor.added' />">
		<label class="col-sm-2 control-label text-right"> <spring:message
				code="lbl.typeofwork" />
		</label>
		<div class="col-sm-3 add-margin">
			<form:select path="parentCategory" data-first-option="false" id="parentCategory" disabled="true" class="form-control"
				required="required">
				<form:option value="">
					<spring:message code="lbl.select" />
				</form:option>
				<form:options items="${typeOfWork}" itemValue="id"
					itemLabel="description" />
			</form:select>
		</div>

		<label class="col-sm-2 control-label text-right"> <spring:message
				code="lbl.subtypeofwork" />
		</label>
		<div class="col-sm-3 add-margin">
            <input type="hidden" id="subTypeOfWorkValue" value="${lineEstimate.subTypeOfWork.id }"/>
			<form:select path="category" data-first-option="false" id="category" class="form-control" disabled="true" >
				<form:option value="">
					<spring:message code="lbl.select" />
				</form:option>
				<form:options items="${typeOfWork}" itemValue="id"
					itemLabel="description" />
			</form:select>
		</div>

	</div>
	<div class="panel-body custom-form">  
		<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.templatecode" /></label>
			<div class="col-sm-3 add-margin">
			<input name="code" id="templateCode" class="form-control" placeholder="Type first 3 letters of Template Code" />
			<input type="hidden" value="" id="templateId" />	
			<input type="hidden" id="estimateTemplateConfirmMsg"  value="<spring:message code='masg.estimate.template.confirm.reset' />"/>
			</div>
		</div>
	</div>
	
	<div align="center">
		<button type='button' class='btn btn-primary' id="searchTemplate">
			<spring:message code='lbl.searchestimatetemplate' />
		</button>
	</div>
</div>
<div id="baseSORTable" class="panel panel-primary" data-collapsed="0">
	<input type="hidden" id="isServiceVATRequired" value="${isServiceVATRequired }">
	<div class="panel-heading">
		<div class="panel-title"><spring:message code="title.sor" />
			<div class="pull-right mb-5 small-note-title"><spring:message code="estimate.rate.disclaimer" /></div>
		</div>
	</div>
	<div class="panel-body" id="sorHeaderTable">
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.schedulecategory" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<select multiple="true" name="scheduleCategory" data-first-option="false" id="scheduleCategory" class="form-control">
					<c:forEach items="${scheduleCategories }" var="scheduleCategory">
						<option value="${scheduleCategory.id }" label="${scheduleCategory.code }" />
					</c:forEach>
				</select>
				<form:errors path="executingDepartment" cssClass="add-margin error-msg" />
			</div>
			<!-- <label class="col-sm-5 control-label add-margin">
			</label> -->

		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.addsor" />
			</label>
			<div class="col-sm-8 add-margin">
				<div id="sorSearch_autocomplete">
				   <div class="right-inner-addon">
					    <input id="sorSearch" type="text" name="item" class="form-control">
	    			</div>    
			    </div> 
			</div>
		</div>
		
		<div class="form-group no-margin-bottom">
			<div class="col-sm-offset-2 col-sm-8">
				<div class="alert alert-danger no-margin mt-5" id="sor_error" style="display:none;"></div>
			</div>
		</div>
		
		<table class="table table-bordered" id="tblsor">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.code" /></th>
					<th><spring:message code="lbl.description" /></th>
					<th><spring:message code="lbl.uom" /></th>
					<th><spring:message code="lbl.rate" /></th>
					<th><spring:message code="lbl.estimatedquantity" /><span class="mandatory"></span></th>
					<th><spring:message code="lbl.estimatedamount" /></th>
					<th hidden="true" id="serviceVatHeader"><spring:message code="lbl.service.vat" /></th>
					<th hidden="true" id="vatAmountHeader"><spring:message code="lbl.service.vat.amount" /></th>
					<th><spring:message code="lbl.total" /></th>
					<th><spring:message code="lbl.delete" /></th>
				</tr>
			</thead>
			<tbody id="sorTable">
				<tr id="message">
					<c:if test="${isServiceVATRequired == true }">
						<td colspan="11"><spring:message code="msg.sor.table"/></td>
					</c:if>
					<c:if test="${isServiceVATRequired == false }">
						<td colspan="9"><spring:message code="msg.sor.table"/></td>
					</c:if>
				</tr>
				<tr id="sorRow" class="sorRow" hidden="true" align="center">
					<td>
						<span class="spansorslno">1</span>
						<form:hidden path="activities[0].schedule.id" id="id_0" class="sorhiddenid" />
					</td>
					<td>
						<span class="code_0"></span>
					</td>
					<td>
						<span class="summary_0"></span>
						<span class="hintanchor description_0"/></span>
					</td>
					<td>
						<span class="uom_0"></span>
					</td>
					<td align="right">
						<span class="rate_0"></span>
					</td>
					<td>
						<form:input path="activities[0].quantity" id="quantity_0" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" required="required" class="form-control table-input text-right" maxlength="64" onblur="calculateEstimateAmount(this);" onkeyup="validateInput(this);"/>
					</td>
					<td align="right">
						<span class="amount_0 amount"></span>
					</td>
					<td hidden="true" class="serviceTaxPerc">
						<form:input path="activities[0].serviceTaxPerc" id="vat_0" data-pattern="decimalvalue" data-idx="0" data-optional="1" class="form-control table-input text-right" maxlength="64" onblur="calculateVatAmount(this);" onkeyup="validateInput(this);"/>
					</td>
					<td hidden="true" align="right" class="vatAmount">
						<span class="vatAmount_0 vatAmt"></span>
					</td>
					<td align="right">
						<span class="total_0 total"></span>
					</td>
					<td>
						<span class="add-padding" onclick="deleteSor(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
					</td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="6" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"> <span id="sorEstimateTotal">0.00</span> </td>
					<td hidden="true" class="emptytd"></td>
					<td hidden="true" class="text-right serviceVatAmt"> <span id="serviceVatAmtTotal">0.00</span> </td>
					<td class="text-right"> <span id="sorTotal">0.00</span> </td>
					<td></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
