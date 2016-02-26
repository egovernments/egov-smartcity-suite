<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
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
<div class="row">
  <div class="col-sm-12">
  <br>
    <input type="hidden"  name="feeMatrix"  value="${feeMatrix.id}"  />
    <table class="table table-bordered" id="result">
      <thead>
        <th><spring:message code="lbl.uomfrom" /></th>
        <th><spring:message code="lbl.uomto" /></th>
        <th><spring:message code="lbl.amount" /></th>
        <th>action</th>
      </thead>
      <c:choose>
      <c:when test="${not empty feeMatrix.getFeeMatrixDetail()}">  
      <tbody>
      <c:forEach items="${feeMatrix.feeMatrixDetail}" var="feeMatrixDetail" varStatus="vs">
      <tr id="resultrow${vs.index}">
        <td><input type="hidden"  name="feeMatrixDetail[${vs.index}]" id="detailId" value="${feeMatrixDetail.id}" />
        <input type="text"  name="feeMatrixDetail[${vs.index}].uomFrom"  id="uomFrom" value="${feeMatrixDetail.uomFrom}" class="form-control text-right patternvalidation"
            data-pattern="number" maxlength="8" readonly="readonly" /></td>
        <td><input type="text"  name="feeMatrixDetail[${vs.index}].uomTo" id="uomTo" value="${feeMatrixDetail.uomTo}" class="form-control text-right patternvalidation"
            data-pattern="number" maxlength="8" onchange="return checkValue(this);" /></td>
        <td><input type="text"  name="feeMatrixDetail[${vs.index}].amount" id="amount" value="${feeMatrixDetail.amount}"  class="form-control text-right patternvalidation"
            data-pattern="number"  maxlength="8"  /></td>
        <td><button type="button" id="del-row" class="btn btn-primary" onclick="deleteThisRow(this)">Delete Row</button></td>
     </tr>   
      </c:forEach>
       </tbody>
      </c:when>
      <c:otherwise>
       <tbody>
       <tr id="resultrow0">
       <td><input type="hidden"  name="feeMatrixDetail[0].id" id="detailId"/>
        <input type="text"  name="feeMatrixDetail[0].uomFrom"  id="uomFrom" value="0" class="form-control text-right patternvalidation"
            data-pattern="number" readonly="readonly" /></td>
        <td><input type="text"  name="feeMatrixDetail[0].uomTo" id="uomTo" class="form-control text-right patternvalidation"
            data-pattern="number" onchange="return checkValue(this);"/></td>
        <td><input type="text"  name="feeMatrixDetail[0].amount" id="amount"   class="form-control text-right patternvalidation"
            data-pattern="number"  /></td>
         <td><span class="add-padding">
         	<button type="button" id="del-row" class="btn btn-primary" onclick="deleteThisRow(this)">Delete Row</button></i></span></td>  
         </tr> 
         <tbody>
      </c:otherwise>
      </c:choose>
    </table> 
  </div>
  <div class="col-sm-12 text-center">
    <button type="button" id="add-row" class="btn btn-primary">Add Row</button>
    <button type="button" id="save" class="btn btn-primary">Save</button>
  </div>
</div>
<script src="<c:url value='/resources/global/js/egov/patternvalidation.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/js/app/feematrix.js' context='/tl'/>"></script>