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
  <c:set var="sheettotal" value="0" />
<c:if test="${!details.measurementSheets.isEmpty() }">
	<!--only for validity head start -->                         
    <table>
	    <tr>
	        <td colspan="6"><!--only for validity head end -->
	            <div class="view-content" style="color:#f2851f"><spring:message code="lbl.measurementsheet" /> <div class="pull-right"><span class="glyphicon glyphicon-remove-circle error-msg hide-ms" style="cursor:pointer;font-size:16px;"></span></div></div>
	            <table class=" table table-bordered msheet-table" id="contractorMBDetails[${item.index }].mstable">
	                <thead>
	                    <tr>
		                    <th><spring:message code="lbl.slno" /></th>
		                    <th><spring:message code="lbl.identifier" /></th>
		                    <th><spring:message code="lbl.description" /></th>
		                    <th><spring:message code="lbl.number" /></th>
		                    <th><spring:message code="lbl.length" /></th>
		                    <th><spring:message code="lbl.width" /></th>
		                    <th><spring:message code="lbl.depthorheight" /></th>
		                    <th><spring:message code="lbl.quantity" /></th>
	                    </tr>
	                </thead>
	                <tbody>
	                	<c:forEach items="${details.measurementSheets }" var="sheet" varStatus="index">
	                		<tr>
			                	<td id="msrowslNo_${item.index }_${index.index }">${index.index + 1}</td>
			                    <c:if test="${sheet.identifier == 'A'}">
									<c:set var="sheettotal" value="${sheettotal + sheet.quantity}" />
								</c:if>
								<c:if test="${sheet.identifier == 'D'}">
									<c:set var="sheettotal" value="${sheettotal - sheet.quantity}" />
								</c:if>
			                    <td id="msrowidentifier_${item.index }_${index.index }">
			                    	<c:choose>
			                    		<c:when test="${sheet.identifier == 'A'}">No</c:when>
			                    		<c:otherwise>Yes</c:otherwise>
			                    	</c:choose>
			                    </td>
			                    <td>
			                    	<c:out default="" value="${sheet.remarks }"></c:out>
			                    </td>
			                    <td align="right">
			                    	<c:out default="" value="${sheet.no }"></c:out>
			                    </td>
			                    <td align="right">
		                    		<c:out default="" value="${sheet.length }"></c:out>
			                    </td>
			                    <td align="right">
		                    		<c:out default="" value="${sheet.width }"></c:out>
			                    </td>
			                    <td align="right">
		                    		<c:out default="" value="${sheet.depthOrHeight }"></c:out>
			                	</td>
			                	<td align="right">
		                    		<c:out default="" value="${sheet.quantity }"></c:out>
			                	</td>
			                </tr>
	                	</c:forEach>
		                <tr>
		                	<td colspan="6"></td>
		                    <td class="text-right"><spring:message code="lbl.total" /></td>
		                    <td class="text-right view-content">${sheettotal }</td>
		                    </td>
		                </tr>
	                </tbody>
	            </table>
	        <!--only for validity tail start -->  
	        </td>
        </tr><!--only for validity -->
    </table> <!--only for validity tail end -->
</c:if>