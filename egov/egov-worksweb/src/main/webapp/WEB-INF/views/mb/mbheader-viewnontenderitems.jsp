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

<div class="panel panel-primary" data-collapsed="0"
	style="text-align: left">
	<div class="panel-heading">
	<div class="position_alert3"><spring:message code="lbl.pagetotal" /> : &#8377 <span class="text-right" id="nonTenderedPageTotal"></span></div>
		<div class="panel-title">
			<spring:message code="title.mb.details" />
		</div>
	</div>
		<c:if test="${mbHeader.mbDetails.size() != 0}">
							<div class="panel-body">
								<div class="panel-heading">
									<div class="panel-title">
										<spring:message code="title.nontendered" />
									</div>
								</div>
								<div align="right" class="openCloseAll">
									<input type="button" value="Close all m-sheet" class="btn btn-sm btn-secondary"
										onclick="closeAllmsheet()" /> <input type="button" class="btn btn-sm btn-secondary"
										value="Open all m-sheet" onclick="openAllmsheet()" />
								</div>
								<table class="table table-bordered" id="tblNonTenderedItems">
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
										</tr>
									</thead>  
									<tbody>
									<c:set var="sorSlNo" value="${1}" scope="session" />
									<c:forEach items="${mbHeader.getNonTenderedMBDetails()}" var="mbdetails" varStatus="item">
					                <c:if test="${mbdetails.workOrderActivity.activity.schedule != null }">
										<tr> 
											<td><span><c:out value="${sorSlNo}" /></span></td>
											<input type="hidden" name="unitrate" id="unitrate" value="${mbdetails.workOrderActivity.activity.rate}" />
											<td><c:out value="${mbdetails.workOrderActivity.activity.schedule.scheduleCategory.code}"></c:out></td>
											<td><c:out value="${mbdetails.workOrderActivity.activity.schedule.code}"></c:out></td>
											<td><c:out value="${mbdetails.workOrderActivity.activity.schedule.summary}"></c:out>
												<a href="#" class="hintanchor"	title="<c:out value="${mbdetails.workOrderActivity.activity.schedule.description}"></c:out>"><i
													class="fa fa-question-circle" aria-hidden="true"></i></a></td>   
											<td><c:out value="${mbdetails.workOrderActivity.activity.uom.uom}"></c:out></td>
											<td align="right"><c:out value="${mbdetails.workOrderActivity.approvedQuantity}" /></td>
											<input type="hidden" name="apprQuantity" id="apprQuantity" value="${mbdetails.workOrderActivity.approvedQuantity}" />
											<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${mbdetails.workOrderActivity.activity.estimateRate}" /></td>
											<td align="right"><c:out value="${mbdetails.prevCumlvQuantity}" /></td>
											<td class="text-right">
												<div class="input-group" style="width:150px">
													<c:out value="${mbdetails.quantity}" />
														<c:choose>
															<c:when test="${!mbdetails.measurementSheets.isEmpty() }">
																<button class="btn btn-default openmbsheet" name="nonTenderedMbDetails[${item.index }].msadd" id="nonTenderedMbDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true"></i></button>
															</c:when>
															<c:otherwise>
																<button style="display: none;" class="btn btn-default openmbsheet" name="nonTenderedMbDetails[${item.index }].msadd" id="nonTenderedMbDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true"></i></button>
															</c:otherwise>
														</c:choose>
												</div>
											</td>
											<td hidden="true">
												<c:set var="net" value="0" />
												<c:set var="total" value="0" />
					                            <input class="classmspresent" type="hidden" disabled="disabled" name="nonTenderedMbDetails[${item.index }].mspresent" id="nonTenderedMbDetails[${item.index }].mspresent" data-idx="0"/>
					                            <input class="classmsopen" type="hidden" disabled="disabled" name="nonTenderedMbDetails[${item.index }].msopen" id="nonTenderedMbDetails[${item.index }].msopen" data-idx="0"/>
												<span  class="nonTenderedMbDetails[${item.index }].mstd" id="nonTenderedMbDetails[${item.index }].mstd" data-idx="0">
													<%@ include file="../measurementsheet/mb-nontendered-measurementsheet-formtable-view.jsp"%>
												</span>
											</td>
											<input type="hidden" name="currMbEnrty" id="currMbEnrty" value="${mbdetails.quantity}" />
											<td align="right"><c:out value="${mbdetails.prevCumlvQuantity + mbdetails.quantity}" /></td>
											<input type="hidden" name="cumulativeQuantitycurrEnrty" id="cumulativeQuantitycurrEnrty"
												value="${mbdetails.prevCumlvQuantity + mbdetails.quantity}" />
											<td class="text-right"><span id="amountCurrentEntry"></span></td>
											<td class="text-right"><span id="cumulativeAmountCurrentEntry"></span></td>
											<td class="text-right"><span id="approvedAmount"></span></td>
											<td><c:out value="${mbdetails.remarks}" /></td>
										</tr>
										<c:set value="${sorSlNo + 1}" var="sorSlNo" scope="session" />
										</c:if>
										</c:forEach>
									</tbody>
									<tfoot>
									<tfoot>
										<tr>
											<td colspan="10" class="text-right view-content" ><spring:message code="lbl.total" /></td>
											<td class="text-right view-content"><span data-pattern="decimalvalue"
												id="nonTenderedTotal"></span></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									</tfoot>
								</table>
							</div>
							<div class="panel-body">
								<div class="panel-heading">
									<div class="panel-title">
										<spring:message code="title.lumpsum" />
									</div>
								</div>
								<table class="table table-bordered" id="tblLumpsum">
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
										</tr>
									</thead>
									<tbody>
									<c:set var="slNo" value="${1}" scope="session" />
									<c:forEach items="${mbHeader.getLumpSumMBDetails()}" var="mbdetails" varStatus="item">
					                <c:if test="${mbdetails.workOrderActivity.activity.schedule == null }">
										<tr> 
											<td><span><c:out value="${slNo}" /></span></td>
											<input type="hidden" name="nonSorUnitrate" id="nonSorUnitrate" value="${mbdetails.workOrderActivity.activity.rate}" />
											<td><c:out value="${mbdetails.workOrderActivity.activity.nonSor.description}"></c:out>
												<a href="#" class="hintanchor"
												title="<c:out value="${mbdetails.workOrderActivity.activity.nonSor.description}"></c:out>"><i
													class="fa fa-question-circle" aria-hidden="true"></i></a></td>
											<td><c:out value="${mbdetails.workOrderActivity.activity.uom.uom}"></c:out></td>
											<td align="right"><c:out value="${mbdetails.workOrderActivity.approvedQuantity}" /></td>
											<input type="hidden" name="nonSorApprQuantity" id="nonSorApprQuantity" value="${mbdetails.workOrderActivity.approvedQuantity}" />
											<td class="text-right"><c:out value="${mbdetails.workOrderActivity.activity.estimateRate}" /></td>
											<td align="right"><c:out value="${mbdetails.prevCumlvQuantity}" /></td>
											<td class="text-right">
												<div class="input-group" style="width:150px">
													<c:out value="${mbdetails.quantity}" />
													<c:choose>
														<c:when test="${!mbdetails.measurementSheets.isEmpty() }">
															<button class="btn btn-default openmbsheet" name="lumpSumMbDetails[${item.index }].msadd" id="lumpSumMbDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true"></i></button>
														</c:when>
														<c:otherwise>
															<button style="display: none;" class="btn btn-default openmbsheet" name="lumpSumMbDetails[${item.index }].msadd" id="lumpSumMbDetails[${item.index }].msadd" data-idx="0" onclick="addMBMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true"></i></button>
														</c:otherwise>
													</c:choose>
												</div>
											</td>
											<td hidden="true">
												<c:set var="net" value="0" />
												<c:set var="total" value="0" />
					                            <input class="classmspresent" type="hidden" disabled="disabled" name="lumpSumMbDetails[${item.index }].mspresent" id="lumpSumMbDetails[${item.index }].mspresent" data-idx="0"/>
					                            <input class="classmsopen" type="hidden" disabled="disabled" name="lumpSumMbDetails[${item.index }].msopen" id="lumpSumMbDetails[${item.index }].msopen" data-idx="0"/>
												<span  class="lumpSumMbDetails[${item.index }].mstd" id="lumpSumMbDetails[${item.index }].mstd" data-idx="0">
													<%@ include file="../measurementsheet/mb-lumpsum-measurementsheet-formtable-view.jsp"%>
												</span>
											</td>
											<input type="hidden" name="nonSorCurrMbEnrty" id="nonSorCurrMbEnrty" value="${mbdetails.quantity}" />
											<td align="right"><c:out value="${mbdetails.prevCumlvQuantity + mbdetails.quantity}" /></td>
											<input type="hidden" name="nonSorCumulativeQuantityCurrEnrty" id="nonSorCumulativeQuantityCurrEnrty"
												value="${mbdetails.prevCumlvQuantity + mbdetails.quantity}" />
											<td class="text-right"><span id="nonSorAmountCurrentEntry"></span></td>
											<td class="text-right"><span id="nonSorCumulativeAmountCurrentEntry"></span></td>
											<td class="text-right"><span id="nonSorApprovedAmount"></span></td>
											<td><c:out value="${mbdetails.remarks}" /></td>
										</tr>
										<c:set value="${slNo + 1}" var="slNo" scope="session" />
										</c:if>
										</c:forEach>
									</tbody>
									<tfoot>
										<tr>
											<td colspan="8" class="text-right view-content"><spring:message code="lbl.total" /></td>
											<td class="text-right view-content"><span data-pattern="decimalvalue"
												id="totalLumpsumTotal"></span></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									</tfoot>
								</table>
							</div>
					</c:if>
</div>