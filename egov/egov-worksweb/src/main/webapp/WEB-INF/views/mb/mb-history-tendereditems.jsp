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
			<spring:message code="lbl.tendered.items" />
		</div>
	</div>
	<table class="table table-bordered tbltendered" id="tbltendered" >
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
		<!-- SOR  -->
			<tr>
				<td  class="text-left view-content" colspan="12"><spring:message code="title.sor" /> </td>
			</tr>
			<c:set var="sorTotalQty" value="${0}" scope="session" />
	        <c:set var="sorTotalAmount" value="${0}" scope="session" />
			<c:forEach items="${history.getSorMbDetails()}" var="mbdetails" varStatus="mbDetailsitem">
			
			 	<c:if test="${mbdetails.id == null }">
				  	 <tr> 
						<td><c:out value="${history.getSorActivitiesAsList().get(mbDetailsitem.index).schedule.scheduleCategory.code}"></c:out></td>
						<td><c:out value="${history.getSorActivitiesAsList().get(mbDetailsitem.index).schedule.code}"></c:out></td>
						<td><c:out value="${history.getSorActivitiesAsList().get(mbDetailsitem.index).schedule.summary}"></c:out>
							<a href="#" class="hintanchor"	title="<c:out value="${history.getSorActivitiesAsList().get(mbDetailsitem.index).schedule.description}"></c:out>"><i
								class="fa fa-question-circle" aria-hidden="true"></i></a></td>   
						<td><c:out value="${history.getSorActivitiesAsList().get(mbDetailsitem.index).uom.uom}"></c:out></td>
						<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${history.getSorActivitiesAsList().get(mbDetailsitem.index).estimateRate}" /></td> 
						<td></td>
						<td></td>   
						<td></td>
						<td></td>
						<td></td>
						<td></td>   
						<td></td>
					</tr>
					<tr> 
						<td class="text-right" colspan="9"><spring:message code="lbl.total" /></td>
						<td class="text-right">0.00</td>
						<td class="text-right">0.00</td>
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
							<c:set value="${sorTotalQty + mbdetails.quantity}" var="sorTotalQty" scope="session" />
							<c:set value="${sorTotalAmount + (mbdetails.quantity * mbdetails.workOrderActivity.activity.rate)}" var="sorTotalAmount" scope="session" />
							<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${mbdetails.quantity}" /></td>
							<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${mbdetails.quantity * mbdetails.workOrderActivity.activity.rate}" /></td>
							<td><c:out value="${mbdetails.remarks}" /></td>
						</tr>
		              </c:when>
		              <c:otherwise>
		              	<c:set var="sormsTotalQty" value="${0}" scope="session" />
		              	<c:set var="sormsTotalAmount" value="${0}" scope="session" />
		              	<c:set var="sormsCount" value="${0}" scope="session" />
			              <c:forEach items="${mbdetails.measurementSheets}" var="ms" varStatus="msItem">
			              	<tr> 
	             				<c:set value="${sorTotalQty + ms.quantity}" var="sorTotalQty" scope="session" />
	             				<c:set value="${sorTotalAmount + (ms.quantity * mbdetails.workOrderActivity.activity.rate)}" var="sorTotalAmount" scope="session" />
								<c:set value="${sormsTotalQty + ms.quantity}" var="sormsTotalQty" scope="session" />
								<c:set value="${sormsTotalAmount + (ms.quantity * mbdetails.workOrderActivity.activity.rate)}" var="sormsTotalAmount" scope="session" />
					              <c:choose>
					            	<c:when test="${sormsCount == 0}">
					            		<c:set value="${sormsCount + 1}" var="sormsCount" scope="session" />
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
								<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${sormsTotalQty}" /></td>
								<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${sormsTotalAmount}" /></td>
								<td></td>
						  </tr>
					  </c:otherwise>
					</c:choose>
				</c:if>
				</c:forEach>
				<tr>
					<td colspan="9" class="text-right view-content" ><spring:message code="lbl.total" /></td>
					<td class="text-right view-content"><fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${sorTotalQty}" /></td>
					<td class="text-right view-content"> <fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${sorTotalAmount}" /></td>
					<td class="text-right view-content"> </td>
			    </tr>
		<!-- Non SOR  -->
			<tr>
				<td  class="text-left view-content" colspan="12"><spring:message code="title.nonsor" /> </td>
			</tr>
				<c:set var="nonSorTotalQty" value="${0}" scope="session" />
		        <c:set var="nonSorTotalAmount" value="${0}" scope="session" />
				<c:forEach items="${history.getNonSorMbDetails()}" var="mbdetails" varStatus="item">
				<c:if test="${mbdetails.id == null }">
				  	 <tr> 
						<td><c:out value="${history.getNonSorActivitiesAsList().get(mbDetailsitem.index).schedule.scheduleCategory.code}"></c:out></td>
						<td><c:out value="${history.getNonSorActivitiesAsList().get(mbDetailsitem.index).schedule.code}"></c:out></td>
						<td><c:out value="${history.getNonSorActivitiesAsList().get(mbDetailsitem.index).schedule.summary}"></c:out>
							<a href="#" class="hintanchor"	title="<c:out value="${history.getNonSorActivitiesAsList().get(mbDetailsitem.index).schedule.description}"></c:out>"><i
								class="fa fa-question-circle" aria-hidden="true"></i></a></td>   
						<td><c:out value="${history.getNonSorActivitiesAsList().get(mbDetailsitem.index).uom.uom}"></c:out></td>
						<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${history.getNonSorActivitiesAsList().get(mbDetailsitem.index).estimateRate}" /></td> 
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
							<c:set value="${nonSorTotalQty + mbdetails.quantity}" var="nonSorTotalQty" scope="session" />
							<c:set value="${nonSorTotalAmount + (mbdetails.quantity * mbdetails.workOrderActivity.activity.rate)}" var="nonSorTotalAmount" scope="session" />
							<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${mbdetails.quantity}" /></td>
							<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${mbdetails.quantity * mbdetails.workOrderActivity.activity.rate}" /></td>
							<td><c:out value="${mbdetails.remarks}" /></td>
						</tr>
		              </c:when>
		              <c:otherwise>
		              	<c:set var="nonSormsTotalQty" value="${0}" scope="session" />
		              	<c:set var="nonSormsTotalAmount" value="${0}" scope="session" />
		              	<c:set var="nonSormsCount" value="${0}" scope="session" />
			              <c:forEach items="${mbdetails.measurementSheets}" var="ms" varStatus="msItem">
			              	<tr> 
	             				<c:set value="${nonSorTotalQty + ms.quantity}" var="nonSorTotalQty" scope="session" />
							    <c:set value="${nonSorTotalAmount + (ms.quantity * mbdetails.workOrderActivity.activity.rate)}" var="nonSorTotalAmount" scope="session" />
								<c:set value="${nonSormsTotalQty + ms.quantity}" var="nonSormsTotalQty" scope="session" />
								<c:set value="${nonSormsTotalAmount + (ms.quantity * mbdetails.workOrderActivity.activity.rate)}" var="nonSormsTotalAmount" scope="session" />
					              <c:choose>
					            	<c:when test="${nonSormsCount == 0}">
					            		<c:set value="${nonSormsCount + 1}" var="nonSormsCount" scope="session" />
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
								<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${nonSormsTotalQty}" /></td>
								<td class="text-right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${nonSormsTotalAmount}" /></td>
								<td></td>
							  </tr>
						  </c:otherwise>
						</c:choose>
					</c:if>
				</c:forEach>
				<tr>
					<td colspan="9" class="text-right view-content" ><spring:message code="lbl.total" /></td>
					<td class="text-right view-content"> <fmt:formatNumber groupingUsed="false" maxFractionDigits="0"	minFractionDigits="0" value="${nonSorTotalQty}" /> </td>
					<td class="text-right view-content"> <fmt:formatNumber groupingUsed="false" maxFractionDigits="2"	minFractionDigits="2" value="${nonSorTotalAmount}" /></td>
					<td class="text-right view-content"> </td>
				</tr>
		</tbody>
		<tfoot>
		</tfoot>
	</table>