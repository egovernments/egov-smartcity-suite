<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%try{ %>
<c:choose>
<c:when test="${nonSorDtls.measurementSheetList.size() > 0 }">
<c:set var="net" value="0" />      
 <td hidden="true">
                             <input class="classmspresent" type="hidden" disabled="disabled" name="nonSorActivities[${item.index }].mspresent" value="1" id="nonSorActivities[${item.index }].mspresent" data-idx="0"/>
                             <input class="classmsopen" type="hidden" disabled="disabled" name="nonSorActivities[${item.index }].msopen" value="0" id="nonSorActivities[${item.index }].msopen" data-idx="0"/>
                       
                             <span name="nonSorActivities[${item.index }].mstd" class="nonSorActivities[${item.index }].mstd" id="nonSorActivities[${item.index }].mstd" data-idx="0">
    <!--only for validity head start -->                         
    <table>
    <tr>
        <td colspan="9"><!--only for validity head end -->
            <div class="view-content">Measurement Sheet <div class="pull-right"><span class="glyphicon glyphicon-remove-circle error-msg hide-ms" style="cursor:pointer;font-size:16px;"></span></div>
            </div>

            <table class=" table table-bordered" id="nonSorActivities[${item.index }].mstable">
                <thead>
                <th><spring:message code="lbl.slno" /></th>
                <th><spring:message code="lbl.identifier" /></th>
                <th><spring:message code="lbl.remarks" /><span class="mandatory"></th>
                <th><spring:message code="lbl.no" /></th>
                <th><spring:message code="lbl.length" /></th>
                <th><spring:message code="lbl.width" /></th>
                <th><spring:message code="lbl.depthorheight" /></th>
                <th><spring:message code="lbl.quantity" /><span class="mandatory"></span></th>
                </thead>
                <tbody id="msrow1">
                <c:forEach items="${nonSorDtls.measurementSheetList}" var="ms" varStatus="msindex" >
                <tr id="msrow">
                    <td class="text-right">
                        <c:out value="${ms.slNo}" />
                    </td>
                    <td class="text-center">
                        <c:if test="${ms.identifier=='A'}"> + </c:if>
                        <c:if test="${ms.identifier=='D'}"> - </c:if>    
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
                    <td class="text-right">Grand Total</td>
                    <td id="nonSorActivities[${item.index }].msnet"  class="text-right">${net}</td>
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
<input class="classmspresent" type="hidden" disabled="disabled" name="nonSorActivities[${item.index }].mspresent" value="0" id="nonSorActivities[${item.index }].mspresent" data-idx="0"/>
<input class="classmsopen" type="hidden" disabled="disabled" name="nonSorActivities[${item.index }].msopen" value="0" id="nonSorActivities[${item.index }].msopen" data-idx="0"/>
<span  class="nonSorActivities[${item.index }].mstd" id="nonSorActivities[${item.index }].mstd" data-idx="${item.index }"></span>
</td>
</c:otherwise>
</c:choose>

<%
 }catch(Exception e){ 
	 System.out.print("estimate  sor-measurement sheet------------------------");
	 e.printStackTrace();
 }
 %>


