<div class="main-content">
  <div class="row">
    <div class="col-md-12">
      <div class="panel panel-primary" data-collapsed="0">
        <div class="panel-heading">
          <div class="panel-title">Relation</div>
        </div>
        <div class="panel-body">
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.code" /> <span
              class="mandatory"></span> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="code" class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                maxlength="50" required="required" />
              <form:errors path="code" cssClass="error-msg" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.name" /> <span
              class="mandatory"></span> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="name" class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                maxlength="50" required="required" />
              <form:errors path="name" cssClass="error-msg" />
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.address" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="address" class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                maxlength="300" />
              <form:errors path="address" cssClass="error-msg" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.mobile" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="mobile" class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                maxlength="10" />
              <form:errors path="mobile" cssClass="error-msg" />
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.email" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="email" class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                maxlength="25" />
              <form:errors path="email" cssClass="error-msg" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.narration" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="narration" class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                maxlength="250" />
              <form:errors path="narration" cssClass="error-msg" />
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.isactive" /> </label>
            <div class="col-sm-3 add-margin">
              <form:checkbox path="isactive" />
              <form:errors path="isactive" cssClass="error-msg" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.panno" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="panno" class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                maxlength="10" />
              <form:errors path="panno" cssClass="error-msg" />
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.tinno" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="tinno" class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                maxlength="20" />
              <form:errors path="tinno" cssClass="error-msg" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.regno" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="regno" class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                maxlength="25" />
              <form:errors path="regno" cssClass="error-msg" />
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.bank" /> </label>
            <div class="col-sm-3 add-margin">
              <form:select path="bank.id" id="bank.id" cssClass="form-control" cssErrorClass="form-control error">
                <form:option value="">
                  <spring:message code="lbl.select" />
                </form:option>
                <form:options items="${banks}" itemValue="id" itemLabel="name" />
              </form:select>
              <form:errors path="bank" cssClass="error-msg" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.bankaccount" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="bankaccount" class="form-control text-left patternvalidation"
                data-pattern="alphanumeric" maxlength="25" />
              <form:errors path="bankaccount" cssClass="error-msg" />
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.ifsccode" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="ifsccode" class="form-control text-left patternvalidation" data-pattern="alphanumeric"
                maxlength="12" />
              <form:errors path="ifsccode" cssClass="error-msg" />
            </div>
          </div>
            <input type="hidden" name="relation" value="${relation.id}" />