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
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.nontendered.items" />
		</div>
	</div>
	<table class="table table-bordered" id="tblnontendered">
		<thead>
			<tr>
				<th><spring:message code="lbl.sor.category" /></th>
				<th><spring:message code="lbl.sorcode" /></th>
				<th><spring:message code="lbl.description.item" /></th>
				<th><spring:message code="lbl.uom" /></th>
				<th><spring:message code="lbl.approved.rate" /></th>
				<th colspan="5"><spring:message code="lbl.measured.quantity" /></th>
				<th><spring:message code="lbl.approved.amount" /></th>
				<th><spring:message code="lbl.remarks" /></th>
			</tr>
			<tr class="msheet-tr">
				 <th></th>
				 <th></th>
				 <th></th>
				 <th></th>
				 <th></th>
				 <th><spring:message code="lbl.number" /></th>
                 <th><spring:message code="lbl.length" /></th>
                 <th><spring:message code="lbl.width" /></th>
                 <th><spring:message code="lbl.depthorheight" /></th>
                 <th><spring:message code="lbl.quantity" /></th>
                 <th></th>
				 <th></th>
			</tr>
		</thead>  
		<tbody>
		<!-- Non Tendered  -->
			<tr>
				<td  class="text-left view-content" colspan="12"><spring:message code="title.nontendered" /> </td>
			</tr>
			<c:set var="nonTenderedTotalQty" value="${0}" scope="session" />
	        <c:set var="nonTenderedTotalAmount" value="${0}" scope="session" />
			<c:forEach items="${history.getNonTenderedMbDetails()}" var="mbdetails" varStatus="item">
				 <c:if test="${mbdetails.id == null }">
				  	 <tr> 
						<td><c:out value="${history.getNonTenActivitiesAsList().get(mbDetailsitem.index).schedule.scheduleCategory.code}"></c:out></td>
						<td><c:out value="${history.getNonTenActivitiesAsList().get(mbDetailsitem.index).schedule.code}"></c:out></td>
						<td><c:out value="${history.getNonTenActivitiesAsList().get(mbDetailsitem.index).schedule.summary}"></c:out>
							<a href="#" class="hintanchor"	title="<c:out value="${history.getNonTenActivitiesAsList().get(mbDetailsitem.index).schedule.description}"></c:out>"><i
								class="fa fa-question-circle" aria-hidden="true"></i></a></td>   
						<td><c:out value="${history.getNonTenActivitiesAsList().get(mbDetailsitem.index).uom.uom}"></c:out></td>
						<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${history.getNonTenActivitiesAsList().get(mbDetailsitem.index).estimateRate}" /></td> 
						<td></td>
						<td></td>   
						<td></td>
						<td></td>
						<td></td>
						<td></td>   
						<td></td>
					</tr>
				</c:if>
	              <c:if test="${mbdetails.workOrderActivity.activity.schedule != null }">
	              	<c:choose>
		              <c:when test="${mbdetails.measurementSheets.isEmpty() }">
			              <tr> 
							<td><c:out value="${mbdetails.workOrderActivity.activity.schedule.scheduleCategory.code}"></c:out></td>
							<td><c:out value="${mbdetails.workOrderActivity.activity.schedule.code}"></c:out></td>
							<td><c:out value="${mbdetails.workOrderActivity.activity.schedule.summary}"></c:out>
								<a href="#" class="hintanchor"	title="<c:out value="${mbdetails.workOrderActivity.activity.schedule.description}"></c:out>"><i
									class="fa fa-question-circle" aria-hidden="true"></i></a></td>   
							<td><c:out value="${mbdetails.workOrderActivity.activity.uom.uom}"></c:out></td>
							<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${mbdetails.workOrderActivity.activity.estimateRate}" /></td>
							<td class="text-right"></td>
							<td class="text-right"></td>
							<td class="text-right"></td>
							<td class="text-right"></td>
							<c:set value="${nonTenderedTotalQty + mbdetails.quantity}" var="nonTenderedTotalQty" scope="session" />
							<c:set value="${nonTenderedTotalAmount + (mbdetails.quantity * mbdetails.workOrderActivity.activity.rate)}" var="nonTenderedTotalAmount" scope="session" />
							<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${mbdetails.quantity}" /></td>
							<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${mbdetails.quantity * mbdetails.workOrderActivity.activity.rate}" /></td>
							<td><c:out value="${mbdetails.remarks}" /></td>
						</tr>
		              </c:when>
		              <c:otherwise>
		              	<c:set var="nonTenderedmsTotalQty" value="${0}" scope="session" />
		              	<c:set var="nonTenderedmsTotalAmount" value="${0}" scope="session" />
		              	<c:set var="nonTenderedmsCount" value="${0}" scope="session" />
			              <c:forEach items="${mbdetails.measurementSheets}" var="ms" varStatus="msItem">
			              	<tr> 
	             				<c:set value="${nonTenderedTotalQty + ms.quantity}" var="nonTenderedTotalQty" scope="session" />
	             				<c:set value="${nonTenderedTotalAmount + (ms.quantity * mbdetails.workOrderActivity.activity.rate)}" var="nonTenderedTotalAmount" scope="session" />
								<c:set value="${nonTenderedmsTotalQty + ms.quantity}" var="nonTenderedmsTotalQty" scope="session" />
								<c:set value="${nonTenderedmsTotalAmount + (ms.quantity * mbdetails.workOrderActivity.activity.rate)}" var="nonTenderedmsTotalAmount" scope="session" />
					              <c:choose>
					            	<c:when test="${nonTenderedmsCount == 0}">
					            		<c:set value="${nonTenderedmsCount + 1}" var="nonTenderedmsCount" scope="session" />
										<td><c:out value="${mbdetails.workOrderActivity.activity.schedule.scheduleCategory.code}"></c:out></td>
										<td><c:out value="${mbdetails.workOrderActivity.activity.schedule.code}"></c:out></td>
										<td><c:out value="${mbdetails.workOrderActivity.activity.schedule.summary}"></c:out>
											<a href="#" class="hintanchor"	title="<c:out value="${mbdetails.workOrderActivity.activity.schedule.description}"></c:out>"><i
												class="fa fa-question-circle" aria-hidden="true"></i></a></td>   
										<td><c:out value="${mbdetails.workOrderActivity.activity.uom.uom}"></c:out></td>
										<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${mbdetails.workOrderActivity.activity.estimateRate}" /></td>
							        </c:when>
					              <c:otherwise>
										<td></td>
										<td></td>
										<td></td>   
										<td></td>
										<td></td>
					              </c:otherwise>
						         </c:choose>
						         	<td align="right"><c:out default="" value="${ms.no }"></c:out></td>
				                    <td align="right"><c:out default="" value="${ms.length }"></c:out></td>
				                    <td align="right"><c:out default="" value="${ms.width }"></c:out></td>
				                    <td align="right"><c:out default="" value="${ms.depthOrHeight }"></c:out></td>
									<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${ms.quantity}" /></td>
									<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${ms.quantity * mbdetails.workOrderActivity.activity.rate}" /></td>
									<td><c:out value="${mbdetails.remarks}" /></td>
						     </tr>
			              </c:forEach>
			              <tr> 
								<td class="text-right" colspan="9"><spring:message code="lbl.total" /></td>
								<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${nonTenderedmsTotalQty}" /></td>
								<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${nonTenderedmsTotalAmount}" /></td>
								<td></td>
						  </tr>
					  </c:otherwise>
					</c:choose>
				</c:if>
				</c:forEach>
				<tr>
					<td colspan="9" class="text-right view-content" ><spring:message code="lbl.total" /></td>
					<td class="text-right view-content"> <fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${nonTenderedTotalQty}" /></td>
					<td class="text-right view-content"> <fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${nonTenderedTotalAmount}" /></td>
					<td class="text-right view-content"> </td>
			    </tr>
		<!-- Non SOR  -->
			<tr>
				<td  class="text-left view-content" colspan="12"><spring:message code="title.lumpsum" /> </td>
			</tr>
				<c:set var="lumpSumTotalQty" value="${0}" scope="session" />
		        <c:set var="lumpSumTotalAmount" value="${0}" scope="session" />
				<c:forEach items="${history.getLumpSumMbDetails()}" var="mbdetails" varStatus="item">
				 <c:if test="${mbdetails.id == null }">
				  	 <tr> 
						<td><c:out value="${history.getLumpSumActivitiesAsList().get(mbDetailsitem.index).schedule.scheduleCategory.code}"></c:out></td>
						<td><c:out value="${history.getLumpSumActivitiesAsList().get(mbDetailsitem.index).schedule.code}"></c:out></td>
						<td><c:out value="${history.getLumpSumActivitiesAsList().get(mbDetailsitem.index).schedule.summary}"></c:out>
							<a href="#" class="hintanchor"	title="<c:out value="${history.getLumpSumActivitiesAsList().get(mbDetailsitem.index).schedule.description}"></c:out>"><i
								class="fa fa-question-circle" aria-hidden="true"></i></a></td>   
						<td><c:out value="${history.getLumpSumActivitiesAsList().get(mbDetailsitem.index).uom.uom}"></c:out></td>
						<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${history.getLumpSumActivitiesAsList().get(mbDetailsitem.index).estimateRate}" /></td> 
						<td></td>
						<td></td>   
						<td></td>
						<td></td>
						<td></td>
						<td></td>   
						<td></td>
					</tr>
				</c:if>
	              <c:if test="${mbdetails.workOrderActivity.activity.schedule == null }">
	              	<c:choose>
		              <c:when test="${mbdetails.measurementSheets.isEmpty() }">
			              <tr> 
							<td></td>
							<td></td>
							<td><c:out value="${mbdetails.workOrderActivity.activity.nonSor.description}"></c:out>
								<a href="#" class="hintanchor"	title="<c:out value="${mbdetails.workOrderActivity.activity.nonSor.description}"></c:out>"><i
									class="fa fa-question-circle" aria-hidden="true"></i></a></td>   
							<td><c:out value="${mbdetails.workOrderActivity.activity.uom.uom}"></c:out></td>
							<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${mbdetails.workOrderActivity.activity.estimateRate}" /></td>
							<td class="text-right"></td>
							<td class="text-right"></td>
							<td class="text-right"></td>
							<td class="text-right"></td>
							<c:set value="${lumpSumTotalQty + mbdetails.quantity}" var="lumpSumTotalQty" scope="session" />
							<c:set value="${lumpSumTotalAmount + (mbdetails.quantity * mbdetails.workOrderActivity.activity.rate)}" var="lumpSumTotalAmount" scope="session" />
							<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${mbdetails.quantity}" /></td>
							<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${mbdetails.quantity * mbdetails.workOrderActivity.activity.rate}" /></td>
							<td><c:out value="${mbdetails.remarks}" /></td>
						</tr>
		              </c:when>
		              <c:otherwise>
		              	<c:set var="lumpSummsTotalQty" value="${0}" scope="session" />
		              	<c:set var="lumpSummsTotalAmount" value="${0}" scope="session" />
		              	<c:set var="lumpSummsCount" value="${0}" scope="session" />
			              <c:forEach items="${mbdetails.measurementSheets}" var="ms" varStatus="msItem">
			              	<tr> 
	             				<c:set value="${lumpSumTotalQty + ms.quantity}" var="lumpSumTotalQty" scope="session" />
							    <c:set value="${lumpSumTotalAmount + (ms.quantity * mbdetails.workOrderActivity.activity.rate)}" var="lumpSumTotalAmount" scope="session" />
								<c:set value="${lumpSummsTotalQty + ms.quantity}" var="lumpSummsTotalQty" scope="session" />
								<c:set value="${lumpSummsTotalAmount + (ms.quantity * mbdetails.workOrderActivity.activity.rate)}" var="lumpSummsTotalAmount" scope="session" />
					              <c:choose>
					            	<c:when test="${lumpSummsCount == 0}">
					            		<c:set value="${lumpSummsCount + 1}" var="lumpSummsCount" scope="session" />
										<td></td>
										<td></td>
										<td><c:out value="${mbdetails.workOrderActivity.activity.nonSor.description}"></c:out>
											<a href="#" class="hintanchor"	title="<c:out value="${mbdetails.workOrderActivity.activity.nonSor.description}"></c:out>"><i
												class="fa fa-question-circle" aria-hidden="true"></i></a></td>   
										<td><c:out value="${mbdetails.workOrderActivity.activity.uom.uom}"></c:out></td>
										<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${mbdetails.workOrderActivity.activity.estimateRate}" /></td>
							        </c:when>
					              <c:otherwise>
										<td></td>
										<td></td>
										<td></td>   
										<td></td>
										<td></td>
					              </c:otherwise>
						         </c:choose>
						         	<td align="right"><c:out default="" value="${ms.no }"></c:out></td>
				                    <td align="right"><c:out default="" value="${ms.length }"></c:out></td>
				                    <td align="right"><c:out default="" value="${ms.width }"></c:out></td>
				                    <td align="right"><c:out default="" value="${ms.depthOrHeight }"></c:out></td>
									<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${ms.quantity}" /></td>
									<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${ms.quantity * mbdetails.workOrderActivity.activity.rate}" /></td>
									<td><c:out value="${mbdetails.remarks}" /></td>
						    	 </tr>
			             	 </c:forEach>
			             	 <tr> 
								<td class="text-right" colspan="9"><spring:message code="lbl.total" /></td>
								<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${lumpSummsTotalQty}" /></td>
								<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${lumpSummsTotalAmount}" /></td>
								<td></td>
							  </tr>
						  </c:otherwise>
						</c:choose>
					</c:if>
				</c:forEach>
				<tr>
					<td colspan="9" class="text-right view-content" ><spring:message code="lbl.total" /></td>
					<td class="text-right view-content"> <fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${lumpSumTotalQty}" /> </td>
					<td class="text-right view-content"> <fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${lumpSumTotalAmount}" /></td>
					<td class="text-right view-content"> </td>
				</tr>
		</tbody>
		<tfoot>
		</tfoot>
	</table>
