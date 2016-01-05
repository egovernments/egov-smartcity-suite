<%@ include file="/includes/taglibs.jsp"%>
<div class="main-content">
  <div class="row">
    <div class="col-md-12">
      <div class="panel panel-primary" data-collapsed="0">
        <div class="panel-heading">
          <div class="panel-title">Validity</div>
        </div>
        <div class="panel-body">
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.natureofbusiness" /> <span
              class="mandatory"></span> </label>
            <div class="col-sm-3 add-margin">
              <form:select path="natureOfBusiness.id" id="natureOfBusiness.id" cssClass="form-control"
                cssErrorClass="form-control error">
                <form:option value="">
                  <spring:message code="lbl.select" />
                </form:option>
                <form:options items="${natureOfBusinesss}" itemValue="id" itemLabel="name" required="required" />
              </form:select>
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.licensecategory" /> <span
              class="mandatory"></span> </label>
            <div class="col-sm-3 add-margin">
              <form:select path="licenseCategory.id" id="licenseCategory.id" cssClass="form-control"
                cssErrorClass="form-control error">
                <form:option value="">
                  <spring:message code="lbl.select" />
                </form:option>
                <form:options items="${licenseCategorys}" itemValue="id" itemLabel="name" required="required" />
              </form:select>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.day" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="day" class="form-control text-right patternvalidation" data-pattern="number" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.week" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="week" class="form-control text-right patternvalidation" data-pattern="number" />
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.month" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="month" class="form-control text-right patternvalidation" data-pattern="number" />
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.year" /> </label>
            <div class="col-sm-3 add-margin">
              <form:input path="year" class="form-control text-right patternvalidation" data-pattern="number" />
            </div>
          </div>
          </div>
          </div>
          </div>
          </div>
          </div>
        
          