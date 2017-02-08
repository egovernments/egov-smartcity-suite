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
	<input type="hidden" id="errorchangequantitieszero" value="<spring:message code='error.change.quantity.zero' />">
	<input type="hidden" id="errorcumulativequantity" value="<spring:message code='error.approved.quantity.cumulative' />">
	<input type="hidden" id="errorchangequantity" value="<spring:message code='error.change.quantity' />">
	<input type="hidden" id="isMeasurementsExist" value="${isMeasurementsExist }">
	
	<div class="panel-heading">
		<div class="position_alert3"><spring:message code="lbl.pagetotal" /> : &#8377 <span id="pageTotal"></span></div>
		<div class="panel-title">
			<spring:message code="lbl.change.quantity" />
			<div class="pull-right">
				<a id="searchAndAdd" href="javascript:void(0);" class="btn btn-primary">
					<i class="fa fa-plus"></i><spring:message code="lbl.add" />
				</a>
				<!-- <a id="addAll" href="javascript:void(0);" class="btn btn-primary">
					<i class="fa fa-plus"></i><spring:message code="lbl.addall" />
				</a> -->
			</div>
		</div>
	</div>
	<div align="right" class="openCloseAll">
		<input type="button" value="Close All Measurements" class="btn btn-sm btn-secondary"
			onclick="closeAllmsheet()" /> <input type="button" class="btn btn-sm btn-secondary"
			value="Open All Measurements" onclick="openAllcqmsheet()" />
	</div>
	<div class="panel-body" id="changeQuantityTable">
		<table class="table table-bordered" style="overflow: auto;" id="tblchangequantity">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.sor.category" /></th>
					<th><spring:message code="lbl.sorcode" /></th>
					<th><spring:message code="lbl.description.item" /></th>
					<th><spring:message code="lbl.uom" /></th>
					<th><spring:message code="lbl.cq.rate" /></th>
					<th><spring:message code="lbl.cq.estimate.quantity" /></th>
					<th><spring:message code="lbl.consumed.quantity" /></th>
					<th><spring:message code="lbl.addition.or.reduction.qty" /></th>
					<th><spring:message code="lbl.addition.or.reduction.amount" /></th>
					<th><spring:message code="lbl.revised.estimate.qty" /></th>
					<th><spring:message code="lbl.revised.total.amount" /></th>
					<th><spring:message code="lbl.delete" /></th>
				</tr>
			</thead>
			<tbody id="changequantitybody">
				<c:if test="${revisionEstimate.changeQuantityActivities.size() == 0}">
					<tr id="activityMessage">
				</c:if>
				<c:if test="${revisionEstimate.changeQuantityActivities.size() != 0}">
					<tr id="activityMessage" hidden="true">
				</c:if>
					<td colspan="13"><spring:message code="msg.mb.sor.table"/></td>
				</tr>
				<c:choose>
					<c:when test="${revisionEstimate.changeQuantityActivities.size() == 0}">
						<tr id="activityRow" class="activityRow" sorinvisible="true" hidden="true" align="center">
							<td>
								<span class="spanactivityslno">1</span>
								<form:input type="hidden" path="changeQuantityActivities[0].id" id="changeQuantityActivitiesId_0" class="changeQuantityActivitiesId" />
								<form:input type="hidden" path="changeQuantityActivities[0].parent.id" id="changeQuantityActivitiesParent_0" class="parent" />
							</td>
							<td>
								<span class="activityCategory_0"></span>
							</td>
							<td>
								<span class="activityCode_0"></span>
							</td>
							<td align="left">
								<span class="activitySummary_0"></span>&nbsp
								<span class="hintanchor activityDescription_0"/></span>
							</td>
							<td>
								<span class="activityUom_0"></span>
							</td>
							<td class="text-right">
								<span class="activityEstimateRate_0"></span>
							</td>
							<td class="text-right">
								<span class="activityEstimateQuantity_0"></span>
							</td>
							<td class="text-right">
								<span class="activityConsumedQuantity_0"></span>
							</td>
							<td>
								<form:input type="hidden" path="changeQuantityActivities[0].rate" id="activityUnitRate_0" class="form-control table-input text-right"/>
								<form:input type="hidden" path="changeQuantityActivities[0].amount" id="activityAmount_0" class="form-control table-input text-right"/>
								<div class="input-group" style="width:150px">
									<span class="input-group-btn number-sign">
						               <button type="button" class="btn btn-default input-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><span class="sign-text_0">+</span> &nbsp;<span class="caret"></span></button>
						               <ul class="dropdown-menu">
						                  <li><a href="javascript:void(0);" class="0" onclick="changeSign(this);">+</a></li>
						                  <li><a href="javascript:void(0);" class="0" onclick="changeSign(this);">-</a></li>
						               </ul>
						               <form:hidden path="changeQuantityActivities[0].signValue" value="+" id="changeQuantityActivitiesSignValue_0" />
						            </span>
									<form:input path="changeQuantityActivities[0].quantity" id="activityQuantity_0" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" required="required" class="form-control input-sm text-right quantity" maxlength="64" onblur="calculateActivityAmounts(this);" onkeyup="validateQuantityInput(this);"/>
									<span class="input-group-addon openmsheet" name="changeQuantityActivities[0].msadd" id="changeQuantityActivities[0].msadd" data-idx="0" onclick="addCQMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"></i></span>
								</div>
							</td>
							<td hidden="true">
	                            <input class="classmspresent" type="hidden" disabled="disabled" name="changeQuantityActivities[0].mspresent" id="changeQuantityActivities[0].mspresent" data-idx="0"/>
	                            <input class="classmsopen" type="hidden" disabled="disabled" name="changeQuantityActivities[0].msopen" id="changeQuantityActivities[0].msopen" data-idx="0"/>
								<span  class="changeQuantityActivities[0].mstd" id="changeQuantityActivities[0].mstd" data-idx="0"></span>
							</td>
							<td class="text-right">
								<span class="reActivityTotal activityEstimatedAmount_0" id="activityEstimatedAmount_0"></span>
							</td>
							<td class="text-right">
								<span class="revisedEstimateQty revisedEstimateQty_0" id="revisedEstimateQty_0"></span>
							</td>
							<td class="text-right">
								<span class="activityTotal activityTotal_0" id="activityTotal_0"></span>
							</td>
							<td>
								<span class="add-padding activityDelete_0" onclick="deleteActivity(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${revisionEstimate.changeQuantityActivities}" var="activity" varStatus="item">
							<tr id="activityRow" class="activityRow" align="center">
								<td>
									<span class="spanactivityslno">${item.index + 1 }</span>
									<form:input type="hidden" path="changeQuantityActivities[${item.index }].id" id="changeQuantityActivitiesId_${item.index }" class="changeQuantityActivitiesId" value="${activity.id }" />
									<form:input type="hidden" path="changeQuantityActivities[${item.index }].parent.id" id="changeQuantityActivitiesParent_${item.index }" class="parent" value="${activity.parent.id }" />
								</td>
								<td>
									<span class="activityCategory_${item.index }">${activity.parent.schedule.scheduleCategory.code }</span>
								</td>
								<td>
									<span class="activityCode_${item.index }">${activity.parent.schedule.code }</span>
								</td>
								<td align="left">
									<c:if test="${activity.parent.schedule != null }">
										<span class="activitySummary_${item.index }">${activity.parent.schedule.getSummary() }</span>&nbsp
										<span class="hintanchor activityDescription_${item.index }"/><a href="#" class="hintanchor" title="${activity.parent.schedule.description }"><i class="fa fa-question-circle" aria-hidden="true"></i></a></span>
									</c:if>
									<c:if test="${activity.parent.schedule == null }">
										<span class="activitySummary_${item.index }">${activity.parent.nonSor.description }</span>&nbsp
										<span class="hintanchor activityDescription_${item.index }"/><a href="#" class="hintanchor" title="${activity.parent.nonSor.description }"><i class="fa fa-question-circle" aria-hidden="true"></i></a></span>
									</c:if>
								</td>
								<td>
									<span class="activityUom_${item.index }">${activity.parent.uom.uom }</span>
								</td>
								<td class="text-right">
									<span class="activityEstimateRate_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${activity.parent.estimateRate }</fmt:formatNumber></span>
								</td>
								<td class="text-right">
									<span class="activityEstimateQuantity_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${activity.estimateQuantity }</fmt:formatNumber></span>
								</td>
								<td class="text-right">
									<span class="activityConsumedQuantity_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${activity.consumedQuantity }</fmt:formatNumber></span>
								</td>
								<td>
									<form:input type="hidden" path="changeQuantityActivities[${item.index }].rate" value="${activity.parent.rate }" id="activityUnitRate_${item.index }" class="form-control table-input text-right"/>
									<div class="input-group" style="width:150px">
										<span class="input-group-btn number-sign">
							               <button type="button" class="btn btn-default input-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
							               		<c:choose>
							               			<c:when test="${activity.revisionType == 'ADDITIONAL_QUANTITY' }">
							               				<span class="sign-text_${item.index }">+</span> &nbsp;<span class="caret"></span>
							               			</c:when>
							               			<c:otherwise>
							               				<span class="sign-text_${item.index }">-</span> &nbsp;<span class="caret"></span>
							               			</c:otherwise>
							               		</c:choose>
							               </button>
							               <ul class="dropdown-menu">
							                  <li><a href="javascript:void(0);" class="${item.index }" onclick="changeSign(this);">+</a></li>
							                  <li><a href="javascript:void(0);" class="${item.index }" onclick="changeSign(this);">-</a></li>
							               </ul>
							               <c:choose>
						               			<c:when test="${activity.revisionType == 'ADDITIONAL_QUANTITY' }">
						               				<form:hidden path="changeQuantityActivities[${item.index }].signValue" value="+" id="changeQuantityActivitiesSignValue_${item.index }" />
						               			</c:when>
						               			<c:otherwise>
						               				<form:hidden path="changeQuantityActivities[${item.index }].signValue" value="-" id="changeQuantityActivitiesSignValue_${item.index }" />
						               			</c:otherwise>
						               		</c:choose>
							            </span>
										<c:choose>
			            	        		<c:when test="${!activity.measurementSheetList.isEmpty() }">
			                	    			<form:input path="changeQuantityActivities[${item.index }].quantity" readonly="true" value="${activity.quantity }" id="activityQuantity_${item.index }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" required="required" class="form-control input-sm text-right activityQuantity" maxlength="64" onblur="calculateActivityAmounts(this);" onkeyup="validateQuantityInput(this);"/>
			                    			</c:when>
			                    			<c:otherwise>
			                    				<form:input path="changeQuantityActivities[${item.index }].quantity" value="${activity.quantity }" id="activityQuantity_${item.index }" data-errormsg="Quantity is mandatory!" data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" required="required" class="form-control input-sm text-right activityQuantity" maxlength="64" onblur="calculateActivityAmounts(this);" onkeyup="validateQuantityInput(this);"/>
				                    		</c:otherwise>
				                    	</c:choose>
										<c:choose>
											<c:when test="${!activity.measurementSheetList.isEmpty() }">
												<span class="input-group-addon openmbsheet" name="changeQuantityActivities[${item.index }].msadd" id="changeQuantityActivities[${item.index }].msadd" data-idx="0" onclick="addCQMSheet(this);return false;" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"><i  class="fa fa-plus-circle" aria-hidden="true"></i></span>
											</c:when>
											<c:otherwise>
												<span style="display: none;" class="input-group-addon openmsheet" name="changeQuantityActivities[${item.index }].msadd" id="changeQuantityActivities[${item.index }].msadd" data-idx="0" onclick="addCQMSheet(this);return false;" data-toggle="tooltip" title="" data-original-title="Add Measurement Sheet"><i  class="fa fa-plus-circle" aria-hidden="true"></i></span>
											</c:otherwise>
										</c:choose>
									</div>
								</td>
								<td hidden="true">
									<c:set var="net" value="0" />
									<c:set var="total" value="0" />
		                            <input class="classmspresent" type="hidden" disabled="disabled" name="changeQuantityActivities[${item.index }].mspresent" id="changeQuantityActivities[${item.index }].mspresent" data-idx="0"/>
		                            <input class="classmsopen" type="hidden" disabled="disabled" name="changeQuantityActivities[${item.index }].msopen" id="changeQuantityActivities[${item.index }].msopen" data-idx="0"/>
									<span  class="changeQuantityActivities[${item.index }].mstd" id="changeQuantityActivities[${item.index }].mstd" data-idx="0">
										<%@ include file="../measurementsheet/changequantity-measurementsheet-formtableedit.jsp"%>
									</span>
								</td>
								<td class="text-right">
									<span class="reActivityTotal activityEstimatedAmount_${item.index }" id = "activityEstimatedAmount_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${activity.parent.rate * activity.quantity }</fmt:formatNumber></span>
								</td>
								<td class="text-right">
									<c:if test="${activity.revisionType == 'ADDITIONAL_QUANTITY' }">

										<span class="revisedEstimateQty revisedEstimateQty_${item.index }" id="revisedEstimateQty_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${(activity.quantity + activity.estimateQuantity) }</fmt:formatNumber></span>
									</c:if>
									<c:if test="${activity.revisionType == 'REDUCED_QUANTITY' }">
										<span class="revisedEstimateQty revisedEstimateQty_${item.index }" id="revisedEstimateQty_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${(activity.estimateQuantity - activity.quantity) }</fmt:formatNumber></span>
									</c:if>
								</td>
								<td class="text-right">
									<c:if test="${activity.revisionType == 'ADDITIONAL_QUANTITY' }">
										<span class="activityTotal activityTotal_${item.index }" id="activityTotal_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${activity.parent.rate * (activity.quantity + activity.estimateQuantity) }</fmt:formatNumber></span>
									</c:if>
									<c:if test="${activity.revisionType == 'REDUCED_QUANTITY' }">
										<span class="activityTotal activityTotal_${item.index }" id="activityTotal_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${activity.parent.rate * (activity.estimateQuantity - activity.quantity) }</fmt:formatNumber></span>
									</c:if>
								</td>
								<td>
									<span class="add-padding activityDelete_${item.index }" onclick="deleteActivity(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<c:set var="recqsortotal" value="${0}" scope="session" />
				<c:if test="${revisionEstimate.changeQuantityActivities != null}">
					<c:forEach items="${revisionEstimate.changeQuantityActivities}" var="sorDtls">
						<c:if test="${sorDtls.revisionType == 'ADDITIONAL_QUANTITY' }">
							<c:set var="recqsortotal" value="${recqsortotal + (sorDtls.parent.rate * (sorDtls.quantity)) }" />
						</c:if>
						<c:if test="${sorDtls.revisionType == 'REDUCED_QUANTITY' }">
							<c:set var="recqsortotal" value="${recqsortotal - (sorDtls.parent.rate * (sorDtls.quantity)) }" />
						</c:if>
						
					</c:forEach>
				</c:if>
				<c:set var="cqsortotal" value="${0}" scope="session" />
				<c:if test="${revisionEstimate.changeQuantityActivities != null}">
					<c:forEach items="${revisionEstimate.changeQuantityActivities}" var="sorDtls">
						<c:if test="${sorDtls.revisionType == 'ADDITIONAL_QUANTITY' }">
							<c:set var="cqsortotal"	value="${cqsortotal + (sorDtls.parent.rate * (sorDtls.quantity + sorDtls.estimateQuantity)) }" />
						</c:if>
						<c:if test="${sorDtls.revisionType == 'REDUCED_QUANTITY' }">
							<c:set var="cqsortotal"	value="${cqsortotal + (sorDtls.parent.rate * (sorDtls.estimateQuantity - sorDtls.quantity)) }" />
						</c:if>
					</c:forEach>
				</c:if>
				<tr>
					<td colspan="9" class="text-right"><spring:message code="lbl.total" /></td>
					<td class="text-right"><span id="reActivityTotal"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out default="0.00" value="${recqsortotal }" /></fmt:formatNumber></span> </td>
					<td></td>
					<td class="text-right"><span id="activityTotal"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out default="0.00" value="${cqsortotal }" /></fmt:formatNumber></span> </td>
					<td></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
