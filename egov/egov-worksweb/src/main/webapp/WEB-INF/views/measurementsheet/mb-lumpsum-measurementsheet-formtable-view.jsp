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
<c:if test="${!mbdetails.measurementSheets.isEmpty() }">
	<!--only for validity head start -->                         
    <table>
	    <tr>
	        <td colspan="16"><!--only for validity head end -->
	            <div class="view-content" style="color:#f2851f"><spring:message code="lbl.measurementsheet" /> <div class="pull-right"><span class="glyphicon glyphicon-remove-circle error-msg hide-ms" style="cursor:pointer;font-size:16px;"></span></div></div>
	            <table class=" table table-bordered msheet-table" id="lumpSumMbDetails[${item.index }].mstable">
	                <thead>
	                	<tr>
		                    <th colspan="1"></th>
		                    <th colspan="8"><spring:message code="lbl.approved.qty" /></th>
	                    	<th colspan="6"><spring:message code="lbl.current.entry" /></th>
	                    </tr>
	                    <tr>
		                    <th><spring:message code="lbl.slno" /></th>
		                    <th><spring:message code="lbl.description" /></th>
		                    <th><spring:message code="lbl.number" /></th>
		                    <th><spring:message code="lbl.length" /></th>
		                    <th><spring:message code="lbl.width" /></th>
		                    <th><spring:message code="lbl.depthorheight" /></th>
		                    <th><spring:message code="lbl.quantity" /></th>
		                    <th><spring:message code="lbl.identifier" /></th>
		                    <th><spring:message code="lbl.cumulative.previous.entry" /></th>
		                    <th><spring:message code="lbl.remarks" /></th>
		                    <th><spring:message code="lbl.number" /></th>
		                    <th><spring:message code="lbl.length" /></th>
		                    <th><spring:message code="lbl.width" /></th>
		                    <th><spring:message code="lbl.depthorheight" /></th>
		                    <th><spring:message code="lbl.quantity" /></th>
	                    </tr>
	                </thead>
	                <tbody>
	                	<c:forEach begin="0" end="${mbdetails.workOrderActivity.workOrderMeasurementSheets.size() - 1 }" varStatus="index">
	                		<tr>
			                	<td id="nonsormsrowslNo_${item.index }_${index.index }">${mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).measurementSheet.slNo }</td>
			                    <td id="nonsormsrowremarks_${item.index }_${index.index }">${mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).measurementSheet.remarks }</td>
			                    <td id="nonsormsrowno_${item.index }_${index.index }" align="right">${mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).no }</td>
			                    <td id="nonsormsrowlength_${item.index }_${index.index }" align="right">${mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).length }</td>
			                    <td id="nonsormsrowwidth_${item.index }_${index.index }" align="right">${mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).width }</td>
			                    <td id="nonsormsrowdepthOrHeight_${item.index }_${index.index }" align="right">${mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).depthOrHeight }</td>
			                    <td id="nonsormsrowquantity_${item.index }_${index.index }" align="right">${mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).quantity }</td>
			                    <c:if test="${mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).measurementSheet.identifier == 'A'}">
									<c:set var="total" value="${total + mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).quantity}" />
								</c:if>
								<c:if test="${mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).measurementSheet.identifier == 'D'}">
									<c:set var="total" value="${total - mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).quantity}" />
								</c:if>
			                    <td id="nonsormsrowidentifier_${item.index }_${index.index }">
			                    	<c:choose>
			                    		<c:when test="${mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).measurementSheet.identifier == 'A'}">No</c:when>
			                    		<c:otherwise>Yes</c:otherwise>
			                    	</c:choose>
			                    </td>
			                    <td id="nonsormsrowmbmsPreviousEntry_${item.index }_${index.index }" align="right">${mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).cumulativeQuantity }</td>
			                    <td>
			                    	<c:if test="${mbdetails.measurementSheets.size() > index.index }">
			                    		<c:out default="" value="${mbdetails.measurementSheets.get(index.index).remarks }"></c:out>
			                    	</c:if>
			                    </td>
			                    <td align="right">
			                    	<c:if test="${mbdetails.measurementSheets.size() > index.index }">
			                    		<c:out default="" value="${mbdetails.measurementSheets.get(index.index).no }"></c:out>
			                    	</c:if>
			                    </td>
			                    <td align="right">
			                    	<c:if test="${mbdetails.measurementSheets.size() > index.index }">
			                    		<c:out default="" value="${mbdetails.measurementSheets.get(index.index).length }"></c:out>
			                    	</c:if>
			                    </td>
			                    <td align="right">
			                    	<c:if test="${mbdetails.measurementSheets.size() > index.index }">
			                    		<c:out default="" value="${mbdetails.measurementSheets.get(index.index).width }"></c:out>
			                    	</c:if>
			                    </td>
			                    <td align="right">
			                    	<c:if test="${mbdetails.measurementSheets.size() > index.index }">
			                    		<c:out default="" value="${mbdetails.measurementSheets.get(index.index).depthOrHeight }"></c:out>
			                    	</c:if>
			                	</td>
			                	<td align="right">
			                		<c:if test="${mbdetails.measurementSheets.size() > index.index }">
			                    		<c:out default="" value="${mbdetails.measurementSheets.get(index.index).quantity }"></c:out>
			                    		<c:if test="${mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).measurementSheet.identifier == 'A'}">
											<c:set var="net" value="${net + mbdetails.measurementSheets.get(index.index).quantity}" />
										</c:if>
										<c:if test="${mbdetails.workOrderActivity.workOrderMeasurementSheets.get(index.index).measurementSheet.identifier == 'D'}">
											<c:set var="net" value="${net - mbdetails.measurementSheets.get(index.index).quantity}" />
										</c:if>
			                    	</c:if>
			                	</td>
			                </tr>
	                	</c:forEach>
		                <tr>
		                	<td colspan="5"></td>
		                    <td class="text-right"><spring:message code="lbl.total" /></td>
		                    <td class="text-right view-content">${total }</td>
		                    <td colspan="6" class="text-right">
		                    </td>
		                    <td class="text-right"><spring:message code="lbl.total" /></td>
		                    <td id="lumpSumMbDetails[${item.index }].msnet" class="text-right">${net}</td>
		                </tr>
	                </tbody>
	            </table>
	        <!--only for validity tail start -->  
	        </td>
        </tr><!--only for validity -->
    </table> <!--only for validity tail end -->
</c:if>