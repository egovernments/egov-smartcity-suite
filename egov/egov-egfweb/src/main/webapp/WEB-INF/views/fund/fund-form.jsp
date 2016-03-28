<div class="main-content">
  <div class="row">
    <div class="col-md-12">
      <div class="panel panel-primary" data-collapsed="0">
        <div class="panel-heading">
          <div class="panel-title">Fund</div>
        </div>
        <div class="panel-body">
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.name" /> <span
              class="mandatory"></span> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="name" class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                maxlength="50" required="required" />
              <form:errors path="name" cssClass="error-msg" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.code" /> <span
              class="mandatory"></span> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="code" class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                maxlength="50" required="required" />
              <form:errors path="code" cssClass="error-msg" />
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.identifier" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="identifier" class="form-control text-right patternvalidation" data-pattern="number" />
              <form:errors path="identifier" cssClass="error-msg" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.llevel" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="llevel" class="form-control text-right patternvalidation" data-pattern="number" />
              <form:errors path="llevel" cssClass="error-msg" />
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.parentid" /> </label>
            <div class="col-sm-3 add-margin">
              <form:select path="parentId" id="parentId.id" cssClass="form-control"
                cssErrorClass="form-control error">
                <form:option value="">
                  <spring:message code="lbl.select" />
                </form:option>
                <form:options items="${funds}" itemValue="id" itemLabel="name" />
              </form:select>
              <form:errors path="parentId" cssClass="error-msg" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.isnotleaf" /> </label>
            <div class="col-sm-3 add-margin">
              <form:checkbox path="isnotleaf" />
              <form:errors path="isnotleaf" cssClass="error-msg" />
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.isactive" /> </label>
            <div class="col-sm-3 add-margin">
              <form:checkbox path="isactive" />
              <form:errors path="isactive" cssClass="error-msg" />
            </div>
            <input type="hidden" name="fund" value="${fund.id}" />