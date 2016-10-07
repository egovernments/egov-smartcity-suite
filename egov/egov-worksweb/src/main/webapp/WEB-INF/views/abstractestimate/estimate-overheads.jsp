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

<div id="overheadsHeaderTable" class="panel panel-primary" data-collapsed="0">
<input type="hidden" value="true" id="isOverheadValuesLoading" />

<div class="panel-heading custom_form_panel_heading" >
	<div class="panel-title">
	   <spring:message code="lbl.overheads" />
	   <div class="pull-right">
	    <a href="javascript:void(0);" class="btn btn-primary" 
	  	       onclick="recalculateOverheads();">
   	        <spring:message code="lbl.recalculate" />
   	    </a>
	   </div>
	</div>
</div>
<div class="panel-body">
		<table class="table table-bordered" id="overheadTable">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno" /></th>
					<th><spring:message code="lbl.name"/></th>
					<th><spring:message code="lbl.percentage"/></th>
					<th><spring:message code="lbl.amount"/></th>
					<th><spring:message code="lbl.action"/></th> 					
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${abstractEstimate.tempOverheadValues.size() == 0}">
						<tr id="overheadRow">
							<td><span id="sno" class="spansno" data-sno>1</span>
								<form:hidden path="tempOverheadValues[0].id"  name="tempOverheadValues[0].id" id="tempOverheadValues[0].id"  class="form-control table-input hidden-input"/>
							</td>
							<td>
								<form:select path="" data-first-option="false" name="tempOverheadValues[0].name" id="tempOverheadValues[0].name" class="form-control overheadValueName" onchange="getPercentageOrLumpsumByOverhead(this);">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<c:forEach var="overhead" items="${overheads}">
										<c:forEach var="overheadrate" items="${overhead.overheadRates}">
										<c:set var="estDate" value="<%=new java.util.Date()%>" scope="session"/>
										<c:set var="fromDate" value="${overheadrate.validity.startDate}" scope="session"/>
										<c:set var="toDate" value="${overheadrate.validity.endDate}" scope="session"/>
										<c:choose>
											<c:when  test="${overheadrate.validity != null}">
												<c:choose>
													<c:when  test="${fromDate <= estDate && (estDate <= toDate || toDate == null || toDate == '')}">
														<c:choose>
															<c:when test="${overheadrate.percentage > 0}">
																<form:option value="${overhead.id}">
																	<c:out value="${overhead.name}" /> <c:out value="${overheadrate.percentage}" /> %
																</form:option>
															</c:when>
															<c:otherwise>
																<form:option value="${overhead.id}">
																	<c:out value="${overhead.name}" /> 
																</form:option>
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
															<form:option value="${overhead.id}">
																<c:out value="${overhead.name}" /> 
															</form:option>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
														<form:option value="${overhead.id}">
															<c:out value="${overhead.name}" /> 
														</form:option>
											</c:otherwise>
										</c:choose>
										</c:forEach>
									</c:forEach>
								</form:select> 
								<form:hidden path="tempOverheadValues[0].overhead.id"  name="tempOverheadValues[0].overhead.id" id="tempOverheadValues[0].overhead.id"  class="form-control table-input hidden-input"/>
								<form:errors path="tempOverheadValues[0].overhead.id" cssClass="add-margin error-msg" /> 
							</td>
							<td>
								<input type="text" id="tempOverheadValues[0].percentage" name="tempOverheadValues[0].percentage"  class="form-control text-right" disabled>  
							</td>
							<td>
								<form:input path="tempOverheadValues[0].amount" id="tempOverheadValues[0].amount" name="tempOverheadValues[0].amount"  data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right" onblur="calculateOverheadTotalAmount();"  maxlength="12" />
								<form:errors path="tempOverheadValues[0].amount" cssClass="add-margin error-msg" /> 
							</td> 
							<td class="text-center">
								<button type="button" onclick="deleteOverheadRow(this);" class="btn btn-xs btn-secondary delete-row"><span class="glyphicon glyphicon-trash"></span> Delete</button>
							 </td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${abstractEstimate.tempOverheadValues}" var="overheadValue" varStatus="item">
							<tr id="overheadRow">
								<td><span id="sno" class="spansno" data-sno><c:out value="${item.index + 1}"/></span>
								<form:hidden path="tempOverheadValues[${item.index }].id"  name="tempOverheadValues[${item.index }].id" id="tempOverheadValues[${item.index }].id" value="${overheadValue.id }"  class="form-control table-input hidden-input"/>
								</td>
								<td>
									<form:select path="" data-first-option="false" name="tempOverheadValues[${item.index }].name" id="tempOverheadValues[${item.index }].name" class="form-control overheadValueName" onchange="getPercentageOrLumpsumByOverhead(this);">
										<form:option value="">
											<spring:message code="lbl.select" />
										</form:option>
										<c:set var="percentage" value="" scope="session"/>
										<c:forEach var="overhead" items="${overheads}">
											<c:forEach var="overheadrate" items="${overhead.overheadRates}">
											<c:set var="estDate" value="<%=new java.util.Date()%>" scope="session"/>
											<c:set var="fromDate" value="${overheadrate.validity.startDate}" scope="session"/>
											<c:set var="toDate" value="${overheadrate.validity.endDate}" scope="session"/>
											<c:choose>
												<c:when  test="${overheadrate.validity != null}">
													<c:choose>
														<c:when  test="${fromDate <= estDate && (estDate <= toDate || toDate == null || toDate == '')}">
															<c:choose>
																<c:when test="${overheadrate.percentage > 0}">
																	<c:if test="${overhead.id == overheadValue.overhead.id }">
																		<c:set var="percentage" value="${overheadrate.percentage}"/>
																		<form:option value="${overhead.id}" selected="selected">
																			<c:out value="${overhead.name}" /> <c:out value="${overheadrate.percentage}" /> %
																		</form:option>
																	</c:if>
																	<c:if test="${overhead.id != overheadValue.overhead.id }">
																		<form:option value="${overhead.id}">
																			<c:out value="${overhead.name}" /> <c:out value="${overheadrate.percentage}" /> %
																		</form:option>
																	</c:if>
																</c:when>
																<c:otherwise>
																	<c:if test="${overhead.id == overheadValue.overhead.id }">
																		<c:set var="percentage" value=""/>
																		<form:option value="${overhead.id}" selected="selected">
																			<c:out value="${overhead.name}" />
																		</form:option>
																	</c:if>
																	<c:if test="${overhead.id != overheadValue.overhead.id }">
																		<form:option value="${overhead.id}">
																			<c:out value="${overhead.name}" />
																		</form:option>
																	</c:if>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise>
															<c:if test="${overhead.id == overheadValue.overhead.id }">
																<c:set var="percentage" value="${overheadrate.percentage}"/>
																<form:option value="${overhead.id}" selected="selected">
																	<c:out value="${overhead.name}" /> <c:out value="${overheadrate.percentage}" /> %
																</form:option>
															</c:if>
															<c:if test="${overhead.id != overheadValue.overhead.id }">
																<form:option value="${overhead.id}">
																	<c:out value="${overhead.name}" /> <c:out value="${overheadrate.percentage}" /> %
																</form:option>
															</c:if>
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:otherwise>
													<c:if test="${overhead.id == overheadValue.overhead.id }">
														<form:option value="${overhead.id}" selected="selected">
															<c:set var="percentage" value="${overheadrate.percentage}"/>
															<c:out value="${overhead.name}" /> <c:out value="${overheadrate.percentage}" /> %
														</form:option>
													</c:if>
													<c:if test="${overhead.id != overheadValue.overhead.id }">
														<form:option value="${overhead.id}">
															<c:out value="${overhead.name}" /> <c:out value="${overheadrate.percentage}" /> %
														</form:option>
													</c:if>
												</c:otherwise>
											</c:choose>
											</c:forEach>
										</c:forEach>
									</form:select> 
									<form:hidden path="tempOverheadValues[${item.index }].overhead.id"  name="tempOverheadValues[${item.index }].overhead.id" value="${overheadValue.overhead.id}" id="tempOverheadValues[${item.index }].overhead.id"  class="form-control table-input hidden-input"/>
									<form:errors path="tempOverheadValues[${item.index }].overhead.id" cssClass="add-margin error-msg" /> 
								</td>
								<td>
									<input type="text" id="tempOverheadValues[${item.index }].percentage" name="tempOverheadValues[${item.index }].percentage"  class="form-control text-right" disabled value="${percentage}">  
								</td>
								<td>
									<form:input path="tempOverheadValues[${item.index }].amount" id="tempOverheadValues[${item.index }].amount" value="${overheadValue.amount }" name="tempOverheadValues[${item.index }].amount"  data-pattern="decimalvalue" data-idx="${item.index }" data-optional="0" class="form-control table-input text-right" onblur="calculateOverheadTotalAmount();"  maxlength="12" />
									<form:errors path="tempOverheadValues[${item.index }].amount" cssClass="add-margin error-msg" /> 
								</td> 
								<td class="text-center">
									<button type="button" onclick="deleteOverheadRow(this);" class="btn btn-xs btn-secondary delete-row"><span class="glyphicon glyphicon-trash"></span> Delete</button>
								 </td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<table class="table table-bordered" >
			<tr>
				<td width="66.5%" style="text-align:right"><spring:message code="lbl.total" /></td>
				<td class="text-right"> <span id="overheadTotalAmount">0.00</span> </td>
				<td width="8.8%"></td>
			</tr>
		</table>
		<div class="panel-title">
			<div class="text-center">
				<a id="addOverheadRow" href="javascript:void(0);" class="btn btn-primary">
					<spring:message code="lbl.addrow" />
				</a>
			</div>
		</div>
</div>

</div>