<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:choose>
<c:when test="${activity.measurementSheetList.size() > 0 }">
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
                <th><spring:message code="lbl.delete" /></th>
                </thead>
                <tbody id="msrow1">
                <c:forEach items="${activity.measurementSheetList}" var="ms" varStatus="msindex" >
                <tr id="msrow">
                    <td>
                        <input  name="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].slNo" value="${ms.slNo}" readonly="readonly" id="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].slNo" class="form-control text-right patternvalidation runtime-update spanslno" data-pattern="decimalvalue" />
                   <input type="hidden"  name="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].id" value="${ms.id}"/>
                    <input type="hidden"  name="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].activity" value="${ms.activity.id}"/> 
                    
                    </td>
                    <td>
                        <select name="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].identifier"    id="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].identifier"  onchange="findNet(this)" class="form-control runtime-update"   >
                            <option value="A" <c:if test="${ms.identifier=='A'}"> selected="selected" </c:if>    >+</option>
                            <option value="D" <c:if test="${ms.identifier=='D'}"> selected="selected" </c:if>    >-</option>
                            </select>
                    </td>
                    <td>
                        <textarea name="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].remarks"    id="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].remarks" class="form-control text-left patternvalidation runtime-update"
                               data-pattern="alphanumeric" maxlength="1024" >${ms.remarks}</textarea>

                    </td>
                    <td>
                        <input name="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].no" value="${ms.no}"  id="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].no" class="form-control text-right patternvalidation runtime-update"
                               data-pattern="decimalvalue" data-idx="0" />

                    </td>
                    <td>                                                                     
                        <input name="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].length" value="${ms.length}"  id="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].length" class="form-control text-right patternvalidation runtime-update"
                               data-pattern="decimalvalue" data-idx="0" />

                    </td>
                    <td>
                        <input name="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].width"  value="${ms.width}"  id="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].width" class="form-control text-right patternvalidation runtime-update"
                               data-pattern="decimalvalue"  data-idx="0" />

                    </td><td>
                    <input name="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].depthOrHeight" id="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].depthOrHeight" class="form-control text-right patternvalidation runtime-update"
                           data-pattern="decimalvalue"  value="${ms.depthOrHeight}" data-idx="0" />

                </td><td>
                    <input name="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].quantity" id="nonSorActivities[${item.index }].measurementSheetList[${msindex.index}].quantity" class="form-control text-right patternvalidation runtime-update"
                           data-pattern="decimalvalue"  value="${ms.quantity}" required="required" onblur="findNet(this)" />
                   
			<c:if test="${ms.identifier=='A'}">
				<c:set var="net" value="${net+ms.quantity}" />
			</c:if> 
			<c:if test="${ms.identifier=='D'}">
				<c:set var="net" value="${net-ms.quantity}" />
			</c:if>
											</td>
                    <td><span class="glyphicon glyphicon-trash" onclick="deleteThisRow(this)" data-idx="${msindex.index}"></span></td>
                </tr>
                </c:forEach>
                <tr>
                    <td colspan="6" class="text-right">
                        <input type="button" value ="Add Row" class="btn btn-xs btn-info add-msrow">
                        <button   class="btn btn-xs btn-danger">Reset</button>
                        <input type="button" value="Submit"  id="nonSorActivities[${item.index }].mssubmit" class="btn btn-xs btn-primary ms-submit"/> 
                    </td>
                    <td class="text-right">Grand Total</td>
                    <td id="nonSorActivities[${item.index }].msnet"  class="text-right">${net}</td>
                    <td></td>
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
<span  class="nonSorActivities[${item.index }].mstd" id="nonSorActivities[${item.index }].mstd" data-idx="0"></span>
</td>
</c:otherwise>
</c:choose>



