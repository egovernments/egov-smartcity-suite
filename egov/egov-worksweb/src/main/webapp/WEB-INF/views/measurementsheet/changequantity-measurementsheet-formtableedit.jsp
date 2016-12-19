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
<c:if test="${!activity.measurementSheetList.isEmpty() }">
		<c:set var="cqnet" value="0" />
		<c:set var="cqtotal" value="0" />   
	<!--only for validity head start -->                         
    <table>
	    <tr class='msheet-tr'>
	        <td colspan="13"><!--only for validity head end -->
	            <div class="view-content" style="color:#f2851f"><spring:message code="lbl.measurementsheet" /> <div class="pull-right"><span class="glyphicon glyphicon-remove-circle error-msg hide-ms" style="cursor:pointer;font-size:16px;"></span></div></div>
	            <table class=" table table-bordered msheet-table" id="changeQuantityActivities[${item.index }].mstable">
	                <thead>
	                	<tr>
		                    <th colspan="1"></th>
		                    <th colspan="7"><spring:message code="lbl.approved.qty" /></th>
	                    	<th colspan="5"><spring:message code="lbl.revised.qty" /></th>
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
		                    <th><spring:message code="lbl.number" /></th>
		                    <th><spring:message code="lbl.length" /></th>
		                    <th><spring:message code="lbl.width" /></th>
		                    <th><spring:message code="lbl.depthorheight" /></th>
		                    <th><spring:message code="lbl.quantity" /><span class="mandatory"></span></th>
	                    </tr>
	                </thead>
	                <tbody>
	                	<c:forEach begin="0" end="${activity.parent.measurementSheetList.size() - 1 }" varStatus="index">
	                		<tr>
			                	<td hidden="true">
			                        <form:input path="changeQuantityActivities[${item.index }].measurementSheetList[${index.index }].id" class="clearthis" id="changeQuantityActivities_${item.index }_measurementSheetList_${index.index }_id" />
			                        <form:input path="changeQuantityActivities[${item.index }].measurementSheetList[${index.index }].parent" id="changeQuantityActivities_${item.index }_measurementSheetList_${index.index }_parent" value="${activity.parent.measurementSheetList.get(index.index).id}" />
			                    </td>
			                	<td id="msrowslNo_${item.index }_${index.index }">${activity.parent.measurementSheetList.get(index.index).slNo }</td>
			                    <td id="msrowremarks_${item.index }_${index.index }">${activity.parent.measurementSheetList.get(index.index).remarks }</td>
			                    <td id="msrowno_${item.index }_${index.index }" align="right">${activity.parent.measurementSheetList.get(index.index).no }</td>
			                    <td id="msrowlength_${item.index }_${index.index }" align="right">${activity.parent.measurementSheetList.get(index.index).length }</td>
			                    <td id="msrowwidth_${item.index }_${index.index }" align="right">${activity.parent.measurementSheetList.get(index.index).width }</td>
			                    <td id="msrowdepthOrHeight_${item.index }_${index.index }" align="right">${activity.parent.measurementSheetList.get(index.index).depthOrHeight }</td>
			                    <td id="msrowquantity_${item.index }_${index.index }" align="right">${activity.parent.measurementSheetList.get(index.index).quantity }</td>
			                    <c:if test="${activity.parent.measurementSheetList.get(index.index).identifier == 'A'}">
									<c:set var="cqtotal" value="${cqtotal + activity.parent.measurementSheetList.get(index.index).quantity}" />
								</c:if>
								<c:if test="${activity.parent.measurementSheetList.get(index.index).identifier == 'D'}">
									<c:set var="cqtotal" value="${cqtotal - activity.parent.measurementSheetList.get(index.index).quantity}" />
								</c:if>
			                    <td id="msrowidentifier_${item.index }_${index.index }">
			                    	<c:choose>
			                    		<c:when test="${activity.parent.measurementSheetList.get(index.index).identifier == 'A'}">No</c:when>
			                    		<c:otherwise>Yes</c:otherwise>
			                    	</c:choose>
			                    </td>
			                    <td>
			                        <form:input path="changeQuantityActivities[${item.index }].measurementSheetList[${index.index }].no" onkeyup="limitCharatersBy3_2(this);" id="changeQuantityActivities[${item.index }].measurementSheetList[${index.index }].no" class="form-control text-right patternvalidation runtime-update"
			                               data-pattern="decimalvalue" data-idx="0" data-no="${activity.parent.measurementSheetList.get(index.index).no }" />
			
			                    </td>
			                    <td>                                                                     
			                        <form:input path="changeQuantityActivities[${item.index }].measurementSheetList[${index.index }].length" onkeyup="limitCharatersBy10_4(this);" id="changeQuantityActivities[${item.index }].measurementSheetList[${index.index }].length" class="form-control text-right patternvalidation runtime-update"
			                               data-pattern="decimalvalue" data-idx="0" data-length="${activity.parent.measurementSheetList.get(index.index).length }" />
			
			                    </td>
			                    <td>
			                        <form:input path="changeQuantityActivities[${item.index }].measurementSheetList[${index.index }].width" onkeyup="limitCharatersBy10_4(this);" id="changeQuantityActivities[${item.index }].measurementSheetList[${index.index }].width" class="form-control text-right patternvalidation runtime-update"
			                               data-pattern="decimalvalue"  data-idx="0" data-width="${activity.parent.measurementSheetList.get(index.index).width }" />
			
			                    </td>
			                    <td>
				                    <form:input path="changeQuantityActivities[${item.index }].measurementSheetList[${index.index }].depthOrHeight" onkeyup="limitCharatersBy10_4(this);" id="changeQuantityActivities[${item.index }].measurementSheetList[${index.index }].depthOrHeight" class="form-control text-right patternvalidation runtime-update"
				                           data-pattern="decimalvalue"  data-idx="0" data-depthOrHeight="${activity.parent.measurementSheetList.get(index.index).depthOrHeight }" />
			
			                	</td>
			                	<td>
				                    <form:input path="changeQuantityActivities[${item.index }].measurementSheetList[${index.index }].quantity" id="changeQuantityActivities[${item.index }].measurementSheetList[${index.index }].quantity" class="form-control text-right patternvalidation runtime-update"
				                           data-pattern="decimalvalue" onblur="findNet(this)" data-quantity="${activity.parent.measurementSheetList.get(index.index).quantity }" />
									<c:if test="${activity.parent.measurementSheetList.get(index.index).identifier == 'A'}">
										<c:set var="cqnet" value="${cqnet + activity.measurementSheetList.get(index.index).quantity}" />
									</c:if>
									<c:if test="${activity.parent.measurementSheetList.get(index.index).identifier == 'D'}">
										<c:set var="cqnet" value="${cqnet - activity.measurementSheetList.get(index.index).quantity}" />
									</c:if>
			                	</td>
			                </tr>
	                	</c:forEach>
		                <tr>
		                	<td colspan="5"></td>
		                    <td class="text-right"><spring:message code="lbl.total" /></td>
		                    <td class="text-right view-content cqtotal_${item.index }">${cqtotal }</td>
		                    <td colspan="4" class="text-right">
		                        <input type="button" name="resetButton" value="Reset" id="resetButton" class="btn btn-xs btn-danger reset-cq" />
		                        <input type="button" value="Submit" id="changeQuantityActivities[${item.index }].mssubmit" class="btn btn-xs btn-primary ms-submit mssubmit_${item.index }"/> 
		                    </td>
		                    <td class="text-right"><spring:message code="lbl.subtotal" /></td>
		                    <td id="changeQuantityActivities[${item.index }].msnet" class="text-right changequantity-msnet">${cqnet}</td>
		                </tr>
	                </tbody>
	            </table>
	        <!--only for validity tail start -->  
	        </td>
        </tr><!--only for validity -->
    </table> <!--only for validity tail end -->
</c:if>