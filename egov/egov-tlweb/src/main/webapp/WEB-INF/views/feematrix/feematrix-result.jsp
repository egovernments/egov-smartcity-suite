<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<div class="row">
  <div class="col-sm-12">
    <div class="table-header">The Search result is</div>
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
         <td><span class="add-padding"><i class="fa fa-trash">
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