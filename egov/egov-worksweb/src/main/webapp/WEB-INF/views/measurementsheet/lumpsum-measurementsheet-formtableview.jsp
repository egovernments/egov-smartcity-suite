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


 
<c:choose>
<c:when test="${nonSorDtls.measurementSheetList.size() > 0 }">
<c:set var="net" value="0" />      
 <td hidden="true">
                             <input class="classmspresent" type="hidden" disabled="disabled" name="lumpSumActivities[${item.index }].mspresent" value="1" id="lumpSumActivities[${item.index }].mspresent" data-idx="0"/>
                             <input class="classmsopen" type="hidden" disabled="disabled" name="lumpSumActivities[${item.index }].msopen" value="0" id="lumpSumActivities[${item.index }].msopen" data-idx="0"/>
                       
                             <span name="lumpSumActivities[${item.index }].mstd" class="lumpSumActivities[${item.index }].mstd" id="lumpSumActivities[${item.index }].mstd" data-idx="0">
    <!--only for validity head start -->                         
    <table>
    <tr>
        <td colspan="9"><!--only for validity head end -->
            <div class="view-content" style="color:#f2851f" ><spring:message code="lbl.measurementsheet" /><div class="pull-right"><span class="glyphicon glyphicon-remove-circle error-msg hide-ms" style="cursor:pointer;font-size:16px;"></span></div>
            </div>

            <table class=" table table-bordered  msheet-table" id="lumpSumActivities[${item.index }].mstable">
                <thead>
                <th><spring:message code="lbl.slno" /></th>
                <th><spring:message code="lbl.identifier" /></th>
                <th><spring:message code="lbl.description" /> </th>
                <th><spring:message code="lbl.number" /></th>
                <th><spring:message code="lbl.length" /></th>
                <th><spring:message code="lbl.width" /></th>
                <th><spring:message code="lbl.depthorheight" /></th>
                <th><spring:message code="lbl.quantity" /> </span></th>
                </thead>
                <tbody id="msrow1">
                <c:forEach items="${nonSorDtls.measurementSheetList}" var="ms" varStatus="msindex" >
                <tr id="msrow">
                    <td class="text-right">
                        <c:out value="${ms.slNo}" />
                    </td>
                    <td class="text-center">
                        <c:if test="${ms.identifier=='A'}"> No </c:if>
                        <c:if test="${ms.identifier=='D'}"> Yes </c:if>    
                            </select>
                    </td>
                    <td>
                       ${ms.remarks}

                    </td>
                    <td class="text-right">
                        <c:out value="${ms.no}"/>

                    </td>
                    <td class="text-right">                                                                     
                       <c:out  value="${ms.length}" />

                    </td>
                    <td class="text-right">
                        <c:out  value="${ms.width}" />

                    </td><td class="text-right">
                    <c:out  value="${ms.depthOrHeight}" />

                </td><td class="text-right">
                    <c:out  value="${ms.quantity}" />
                   
			<c:if test="${ms.identifier=='A'}">
				<c:set var="net" value="${net + ms.quantity}" />
			</c:if> 
			<c:if test="${ms.identifier=='D'}">
				<c:set var="net" value="${net - ms.quantity}" />
			</c:if>
											</td>
                </tr>
                </c:forEach>
                <tr>
                    <td colspan="6" class="text-right">
                        
                    </td>
                    <td class="text-right"><spring:message code="lbl.subtotal" /></td>
                    <td id="lumpSumActivities[${item.index }].msnet"  class="text-right">${net}</td>
                </tr>
                
                <tbody>
            </table>
       <!--only for validity tail start -->  
        </td>
        </tr><!--only for validity -->
    </table> <!--only for validity tail end -->  
 
</span>
</td>
</c:when>
<c:otherwise>
<td hidden="true">
<input class="classmspresent" type="hidden" disabled="disabled" name="lumpSumActivities[${item.index }].mspresent" value="0" id="lumpSumActivities[${item.index }].mspresent" data-idx="0"/>
<input class="classmsopen" type="hidden" disabled="disabled" name="lumpSumActivities[${item.index }].msopen" value="0" id="lumpSumActivities[${item.index }].msopen" data-idx="0"/>
<span  class="lumpSumActivities[${item.index }].mstd" id="lumpSumActivities[${item.index }].mstd" data-idx="${item.index }"></span>
</td>
</c:otherwise>
</c:choose>


