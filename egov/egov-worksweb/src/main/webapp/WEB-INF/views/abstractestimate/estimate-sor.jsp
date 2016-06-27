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
		<input type="hidden" id="msgestimatedate" value="<spring:message code='error.abstractestimate.estimate.date' />">
		<input type="hidden" id="erroradded" value="<spring:message code='error.sor.added' />">
		<input type="hidden" id="errorrateszero" value="<spring:message code='error.rates.zero' />">
		<input type="hidden" id="errorquantityzero" value="<spring:message code='error.quantity.zero' />">
		<input type="hidden" id="errorsornonsor" value="<spring:message code='error.sor.nonsor.required' />">
		<input type="hidden" value="${abstractEstimate.sorActivities.size() }" id="sorActivitiesSize" />
		<label class="col-sm-2 control-label text-right"> <spring:message code="lbl.typeofwork" />
		</label>
		<div class="col-sm-3 add-margin">
			<form:select path="parentCategory" data-first-option="false" id="parentCategory" class="form-control disablefield"
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
			<form:select path="category" data-first-option="false" id="category" class="form-control disablefield">
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
			<input type="hidden" id="estimateTemplateConfirmMsg"  value="<spring:message code='msg.estimate.template.confirm.reset' />"/>
			</div>
		</div>
	</div>
	
	<div align="center">
		<button type='button' class='btn btn-primary' id="searchTemplate">
			<spring:message code='lbl.searchestimatetemplate' />
		</button>
	</div>
	<br/>
	<div class="add-margin error-msg text-right"><spring:message code="estimate.template.rate.disclaimer" />&nbsp;&nbsp;&nbsp;</div>
</div>
<div id="baseSORTable" class="panel panel-primary" data-collapsed="0">
	<input type="hidden" id="isServiceVATRequired" value="${isServiceVATRequired }">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="title.sor" />
		</div>
	</div>
	<div class="panel-body" id="sorHeaderTable">
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.schedulecategory" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<form:select path="" multiple="true" name="scheduleCategory" data-first-option="false" id="scheduleCategory" class="form-control">
					<c:forEach items="${scheduleCategories }" var="scheduleCategory">
						<form:option value="${scheduleCategory.id }">${scheduleCategory.code } -- ${scheduleCategory.description }</form:option>
					</c:forEach>
				</form:select>
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
					    <input id="sorSearch" placeholder="Start typing SOR code or description and select an item" type="text" name="item" class="form-control">
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
					<th><spring:message code="lbl.sor.category" /></th>
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
				<c:if test="${abstractEstimate.sorActivities.size() == 0}">
					<tr id="message">
				</c:if>
				<c:if test="${abstractEstimate.sorActivities.size() != 0}">
					<tr id="message" hidden="true">
				</c:if>
					<c:if test="${isServiceVATRequired == true }">
						<td colspan="12"><spring:message code="msg.sor.table"/></td>
					</c:if>
					<c:if test="${isServiceVATRequired == false }">
						<td colspan="10"><spring:message code="msg.sor.table"/></td>
					</c:if>
				</tr>
				<c:choose>
					<c:when test="${abstractEstimate.sorActivities.size() == 0}">
						<tr id="sorRow" class="sorRow" sorinvisible="true" hidden="true" align="center">
							<td>
								<span class="spansorslno">1</span>
								<form:hidden path="sorActivities[0].id" id="activityid_0" class="activityid" />
								<form:hidden path="sorActivities[0].schedule.id" id="id_0" class="sorhiddenid" />
							</td>
							<td>
								<span class="categoryCode_0"></span>
							</td>
							<td>
								<span class="code_0"></span>
							</td>
							<td align="left">
								<span class="summary_0"></span>
								<span class="hintanchor description_0"/></span>
							</td>
							<td>
								<span class="uom_0"></span>
								<form:hidden path="sorActivities[0].uom.id" id="sorUomid_0" class="uomhiddenid"/>
							</td>
							<td align="right">
								<span class="estimateRate estimateRate_0"></span>
								<form:hidden path="sorActivities[0].rate" id="rate_0" />
								<form:hidden path="sorActivities[0].estimateRate" id="estimateRate_0" />
							</td>
							<td>
								<form:input path="sorActivities[0].quantity" id="quantity_0" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" required="required" class="form-control table-input text-right quantity" maxlength="64" onblur="calculateEstimateAmount(this);" onkeyup="validateQuantityInput(this);"/>
							</td>
							<td align="right">
								<span class="amount_0 amount"></span>
							</td>
							<td hidden="true" class="serviceTaxPerc">
								<form:input path="sorActivities[0].serviceTaxPerc" id="vat_0" data-pattern="decimalvalue" data-idx="0" data-optional="1" class="form-control table-input text-right" maxlength="64" onblur="calculateVatAmount(this);" onkeyup="validateInput(this);"/>
							</td>
							<td hidden="true" align="right" class="vatAmount">
								<span class="vatAmount_0 vatAmt"></span>
							</td>
							<td align="right">
								<span class="total_0 total"></span>
							</td>
							<td>
								<span class="add-padding delete_0" onclick="deleteSor(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${abstractEstimate.sorActivities}" var="activity" varStatus="item">
							<c:if test="${activity.schedule != null }">
								<tr id="sorRow" class="sorRow" align="center">
									<td>
										<span class="spansorslno">${item.index + 1 }</span>
										<form:hidden path="sorActivities[${item.index }].id" id="soractivityid_${item.index }" class="soractivityid" value="${activity.id }" />
										<form:hidden path="sorActivities[${item.index }].schedule.id" id="id_${item.index }" class="sorhiddenid" value="${activity.schedule.id }" />
									</td>
									<td>
										<span class="categoryCode_${item.index }">${activity.schedule.scheduleCategory.code }</span>
									</td>
									<td>
										<span class="code_${item.index }">${activity.schedule.code }</span>
									</td>
									<td align="left">
										<span class="summary_${item.index }">${activity.schedule.getSummary() }</span>
										<span class="hintanchor description_${item.index }"/><a href="#" class="hintanchor" title="${activity.schedule.description }"><i class="fa fa-question-circle" aria-hidden="true"></i></a></span>
									</td>
									<td>
										<span class="uom_${item.index }">${activity.schedule.uom.uom }</span>
										<form:hidden path="sorActivities[${item.index }].uom.id" id="sorUomid_${item.index }" class="uomhiddenid" value="${activity.schedule.uom.id }"/>
									</td>
									<td align="right">
										<span class="estimateRate estimateRate_${item.index }">${activity.rate }</span>
										<form:hidden path="sorActivities[${item.index }].rate" id="rate_${item.index }" value="${activity.rate }" />
										<form:hidden path="sorActivities[${item.index }].estimateRate" id="estimateRate_${item.index }" value="${activity.estimateRate }" />
									</td>
									<td>
										<form:input path="sorActivities[${item.index }].quantity" id="quantity_${item.index }" value="${activity.quantity }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" required="required" class="form-control table-input text-right quantity" maxlength="64" onblur="calculateEstimateAmount(this);" onkeyup="validateQuantityInput(this);"/>
									</td>
									<td align="right">
										<span class="amount_${item.index } amount"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2">${activity.rate * activity.quantity }</fmt:formatNumber></span>
									</td>
									<td hidden="true" class="serviceTaxPerc">
										<form:input path="sorActivities[${item.index }].serviceTaxPerc" value="${activity.serviceTaxPerc }" id="vat_${item.index }" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="1" class="form-control table-input text-right" maxlength="64" onblur="calculateVatAmount(this);" onkeyup="validateInput(this);"/>
									</td>
									<td hidden="true" align="right" class="vatAmount">
										<span class="vatAmount_${item.index } vatAmt"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2">${(activity.rate * activity.quantity) * (activity.serviceTaxPerc / 100) }</fmt:formatNumber></span>
									</td>
									<td align="right">
										<span class="total_${item.index } total"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2">${(activity.rate * activity.quantity) + ((activity.rate * activity.quantity) * (activity.serviceTaxPerc / 100)) }</fmt:formatNumber></span>
									</td>
									<td>
										<span class="add-padding delete_${item.index }" onclick="deleteSor(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
									</td>
								</tr>
							</c:if>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="7" class="text-right"><spring:message code="lbl.total" /></td>
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
