<div class="main-content"><div class="row"><div class="col-md-12"><div class="panel panel-primary" data-collapsed="0"><div class="panel-heading"><div class="panel-title">TransactionSummary</div></div><div class="panel-body"><div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message code="lbl.accountdetailtype" />
</label><div class="col-sm-3 add-margin">
<form:select path="accountdetailtype.id" id="accountdetailtype.id" cssClass="form-control" cssErrorClass="form-control error" >
<form:option value=""> <spring:message code="lbl.select"/> </form:option>
<form:options items="${accountdetailtypes}" itemValue="id" itemLabel="name"  />
</form:select>
<form:errors path="accountdetailtype" cssClass="error-msg" /></div><label class="col-sm-3 control-label text-right"><spring:message code="lbl.financialyear" />
</label><div class="col-sm-3 add-margin">
<form:select path="financialyear.id" id="financialyear.id" cssClass="form-control" cssErrorClass="form-control error" >
<form:option value=""> <spring:message code="lbl.select"/> </form:option>
<form:options items="${cFinancialYears}" itemValue="id" itemLabel="name"  />
</form:select>
<form:errors path="financialyear" cssClass="error-msg" /></div></div>
<div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message code="lbl.fundsource" />
</label><div class="col-sm-3 add-margin">
<form:select path="fundsource.id" id="fundsource.id" cssClass="form-control" cssErrorClass="form-control error" >
<form:option value=""> <spring:message code="lbl.select"/> </form:option>
<form:options items="${fundsources}" itemValue="id" itemLabel="name"  />
</form:select>
<form:errors path="fundsource" cssClass="error-msg" /></div><label class="col-sm-3 control-label text-right"><spring:message code="lbl.fund" />
</label><div class="col-sm-3 add-margin">
<form:select path="fund.id" id="fund.id" cssClass="form-control" cssErrorClass="form-control error" >
<form:option value=""> <spring:message code="lbl.select"/> </form:option>
<form:options items="${funds}" itemValue="id" itemLabel="name"  />
</form:select>
<form:errors path="fund" cssClass="error-msg" /></div></div>
<div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message code="lbl.glcodeid" />
</label><div class="col-sm-3 add-margin">
<form:select path="glcodeid.id" id="glcodeid.id" cssClass="form-control" cssErrorClass="form-control error" >
<form:option value=""> <spring:message code="lbl.select"/> </form:option>
<form:options items="${cChartOfAccountss}" itemValue="id" itemLabel="name"  />
</form:select>
<form:errors path="glcodeid" cssClass="error-msg" /></div><label class="col-sm-3 control-label text-right"><spring:message code="lbl.openingdebitbalance" />
<span class="mandatory"></span>
</label><div class="col-sm-3 add-margin">
 <form:input path="openingdebitbalance" class="form-control text-right patternvalidation" data-pattern="number" required="required" />
<form:errors path="openingdebitbalance" cssClass="error-msg" /></div></div>
<div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message code="lbl.openingcreditbalance" />
<span class="mandatory"></span>
</label><div class="col-sm-3 add-margin">
 <form:input path="openingcreditbalance" class="form-control text-right patternvalidation" data-pattern="number" required="required" />
<form:errors path="openingcreditbalance" cssClass="error-msg" /></div><label class="col-sm-3 control-label text-right"><spring:message code="lbl.accountdetailkey" />
</label><div class="col-sm-3 add-margin">
 <form:input path="accountdetailkey" class="form-control text-right patternvalidation" data-pattern="number"  />
<form:errors path="accountdetailkey" cssClass="error-msg" /></div></div>
<div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message code="lbl.narration" />
</label><div class="col-sm-3 add-margin">
<form:input  path="narration" class="form-control text-left patternvalidation" data-pattern="alphanumeric" maxlength="300"  />
<form:errors path="narration" cssClass="error-msg" /></div><label class="col-sm-3 control-label text-right"><spring:message code="lbl.departmentid" />
</label><div class="col-sm-3 add-margin">
<form:select path="departmentid.id" id="departmentid.id" cssClass="form-control" cssErrorClass="form-control error" >
<form:option value=""> <spring:message code="lbl.select"/> </form:option>
<form:options items="${departments}" itemValue="id" itemLabel="name"  />
</form:select>
<form:errors path="departmentid" cssClass="error-msg" /></div></div>
<div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message code="lbl.functionaryid" />
</label><div class="col-sm-3 add-margin">
<form:select path="functionaryid.id" id="functionaryid.id" cssClass="form-control" cssErrorClass="form-control error" >
<form:option value=""> <spring:message code="lbl.select"/> </form:option>
<form:options items="${functionarys}" itemValue="id" itemLabel="name"  />
</form:select>
<form:errors path="functionaryid" cssClass="error-msg" /></div><label class="col-sm-3 control-label text-right"><spring:message code="lbl.functionid" />
</label><div class="col-sm-3 add-margin">
<form:select path="functionid.id" id="functionid.id" cssClass="form-control" cssErrorClass="form-control error" >
<form:option value=""> <spring:message code="lbl.select"/> </form:option>
<form:options items="${cFunctions}" itemValue="id" itemLabel="name"  />
</form:select>
<form:errors path="functionid" cssClass="error-msg" /></div></div>
<div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message code="lbl.divisionid" />
</label><div class="col-sm-3 add-margin">
 <form:input path="divisionid" class="form-control text-right patternvalidation" data-pattern="number"  />
<form:errors path="divisionid" cssClass="error-msg" /></div> <input type="hidden" name="transactionSummary" value="${transactionSummary.id}" />