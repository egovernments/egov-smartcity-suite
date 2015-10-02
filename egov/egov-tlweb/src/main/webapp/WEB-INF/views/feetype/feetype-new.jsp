<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<div class="main-content"><div class="row"><div class="col-md-12"><div class="panel panel-primary" data-collapsed="0"><div class="panel-heading"><div class="panel-title">FeeType</div></div><div class="panel-body"><form:form role="form" action="feetype/create" modelAttribute="feeType" id="feeTypeform" cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data"><div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message code="lbl.name" />
<span class="mandatory"></span>
</label><div class="col-sm-3 add-margin">
<form:input  path="name" class="form-control text-left patternvalidation" data-pattern="alphanumeric" maxlength="32" required="required"/>
</div></div>
<label class="col-sm-3 control-label text-right"><spring:message code="lbl.code" />
</label><div class="col-sm-3 add-margin">
<form:input  path="code" class="form-control text-left patternvalidation" data-pattern="alphanumeric" maxlength="12" />
</div><div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message code="lbl.feeprocesstype" />
</label><div class="col-sm-3 add-margin">
<form:select path="feeProcessType.id" id="feeProcessType.id" cssClass="form-control" cssErrorClass="form-control error" >
<form:option value=""> <spring:message code="lbl.select"/> </form:option>
<form:options items="${feeProcessTypes}" itemValue="id" itemLabel="name"/>
</form:select>
</div></div>
</form:form> </div></div></div></div>