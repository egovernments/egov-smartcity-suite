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
<c:when test="${detail.measurementSheets.size() > 0 }">
<c:set var="net" value="0" />      
 <td hidden="true">
                             <input class="classmspresent" type="hidden" disabled="disabled" name="additionalMBDetails[${item.index }].mspresent" value="1" id="additionalMBDetails[${item.index }].mspresent" data-idx="0"/>
                             <input class="classmsopen" type="hidden" disabled="disabled" name="additionalMBDetails[${item.index }].msopen" value="0" id="additionalMBDetails[${item.index }].msopen" data-idx="0"/>
                       
                             <span name="additionalMBDetails[${item.index }].mstd" class="additionalMBDetails[${item.index }].mstd" id="additionalMBDetails[${item.index }].mstd" data-idx="0">
    <!--only for validity head start -->                         
    <table>
    <tr class='msheet-tr'>
        <td colspan="9"><!--only for validity head end -->
            <div class="view-content" style="color:#f2851f"><spring:message code="lbl.measurementsheet" /><div class="pull-right"><span class="glyphicon glyphicon-remove-circle error-msg hide-ms" style="cursor:pointer;font-size:16px;"></span></div>
            </div>

            <table class=" table table-bordered  msheet-table" id="additionalMBDetails[${item.index }].mstable">
                <thead>
                <th><spring:message code="lbl.slno" /></th>
                <th><spring:message code="lbl.identifier" /></th>
                <th><spring:message code="lbl.description" /></th>
                <th><spring:message code="lbl.number" /></th>
                <th><spring:message code="lbl.length" /></th>
                <th><spring:message code="lbl.width" /></th>
                <th><spring:message code="lbl.depthorheight" /></th>
                <th><spring:message code="lbl.quantity" /><span class="mandatory"></span></th>
                <th><spring:message code="lbl.delete" /></th>
                </thead>
                <tbody id="msrow1">
                <c:forEach items="${detail.measurementSheets}" var="ms" varStatus="msindex" >
                <tr id="msrow">
                    <td>
                        <input  name="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].slNo" value="${msindex.index + 1 }" readonly="readonly" id="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].slNo" class="form-control text-right patternvalidation runtime-update spanslno" data-pattern="decimalvalue" />
                   <input type="hidden"  name="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].id" value="${ms.id}"/>
                    </td>
                    <td>
                        <select name="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].identifier"    id="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].identifier"  onchange="findNet(this)" class="form-control runtime-update"   >
                            <option value="A" <c:if test="${ms.identifier=='A'}"> selected="selected" </c:if>    >No</option>
                            <option value="D" <c:if test="${ms.identifier=='D'}"> selected="selected" </c:if>    >Yes</option>
                            </select>
                    </td>
                    <td>
                        <textarea name="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].remarks"    id="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].remarks" class="form-control text-left patternvalidation runtime-update"
                               data-pattern="alphanumeric" maxlength="1024" >${ms.remarks}</textarea>

                    </td>
                    <td>
                        <input name="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].no" value="${ms.no}" maxlength="6" onkeyup="limitCharatersBy3_2(this);" id="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].no" class="form-control text-right patternvalidation runtime-update"
                               data-pattern="decimalvalue" data-idx="0" />

                    </td>
                    <td>                                                                     
                        <input name="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].length" maxlength="15" onkeyup="limitCharatersBy10_4(this);" value="${ms.length}"  id="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].length" class="form-control text-right patternvalidation runtime-update"
                               data-pattern="decimalvalue" data-idx="0" />

                    </td>
                    <td>
                        <input name="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].width" maxlength="15" onkeyup="limitCharatersBy10_4(this);"  value="${ms.width}"  id="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].width" class="form-control text-right patternvalidation runtime-update"
                               data-pattern="decimalvalue"  data-idx="0" />

                    </td><td>
                    <input name="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].depthOrHeight"  maxlength="15" onkeyup="limitCharatersBy10_4(this);" id="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].depthOrHeight" class="form-control text-right patternvalidation runtime-update"
                           data-pattern="decimalvalue"  value="${ms.depthOrHeight}" data-idx="0" />

                </td><td>
                    <input name="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].quantity" id="additionalMBDetails[${item.index }].measurementSheets[${msindex.index}].quantity" class="form-control text-right patternvalidation runtime-update"
                           data-pattern="decimalvalue"  value="${ms.quantity}" required="required" onblur="findNet(this)" />
                   
			<c:if test="${ms.identifier=='A'}">
				<c:set var="net" value="${net+ms.quantity}" />
			</c:if> 
			<c:if test="${ms.identifier=='D'}">
				<c:set var="net" value="${net-ms.quantity}" />
			</c:if>
											</td>
                    <td><span class="glyphicon glyphicon-trash"   data-toggle="tooltip" title="" data-original-title="Delete!"   onclick="deleteThisRow(this)" data-idx="${msindex.index}"></span></td>
                </tr>
                </c:forEach>
                <tr>
                    <td colspan="6" class="text-right">
                        <input type="button" value ="<spring:message code="lbl.addrow" />" class="btn btn-xs btn-info add-msrow">
                        <button   class="btn btn-xs btn-danger reset-ms"><spring:message code="lbl.reset" /></button>
                        <input type="button" value="<spring:message code="lbl.submit" />"  id="additionalMBDetails[${item.index }].mssubmit" class="btn btn-xs btn-primary ms-submit"/> 
                    </td>
                    <td class="text-right"><spring:message code="lbl.subtotal" /></td>
                    <td id="additionalMBDetails[${item.index }].msnet"  class="text-right">${net}</td>
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
<input class="classmspresent" type="hidden" disabled="disabled" name="additionalMBDetails[${item.index }].mspresent" value="0" id="additionalMBDetails[${item.index }].mspresent" data-idx="0"/>
<input class="classmsopen" type="hidden" disabled="disabled" name="additionalMBDetails[${item.index }].msopen" value="0" id="additionalMBDetails[${item.index }].msopen" data-idx="0"/>
<span  class="additionalMBDetails[${item.index }].mstd" id="additionalMBDetails[${item.index }].mstd" data-idx="0"></span>
</td>
</c:otherwise>
</c:choose>     



