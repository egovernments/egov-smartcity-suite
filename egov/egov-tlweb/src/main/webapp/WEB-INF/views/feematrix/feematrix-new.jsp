<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<script src="<c:url value='/resources/app/js/feematrix.js' context='/tl'/>"></script>
<div class="main-content">
  <div class="row">
    <div class="col-md-12">
      <div class="panel panel-primary" data-collapsed="0">
        <div class="panel-heading">
          <div class="panel-title">FeeMatrix</div>
        </div>
        <div class="panel-body">
          <form:form role="form" action="feematrix/create" modelAttribute="feeMatrix" id="feematrix-new"
            cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
            <div class="form-group">
              <label class="col-sm-3 control-label text-right"><spring:message code="lbl.natureofbusiness" /> <span
                class="mandatory"></span> </label>
              <div class="col-sm-3 add-margin">
                <form:select path="natureOfBusiness" id="natureOfBusiness" cssClass="form-control"   required="required"
                  cssErrorClass="form-control error" >
                  <form:option value="">
                    <spring:message code="lbl.select" />
                  </form:option>
                  <form:options items="${natureOfBusinesss}" itemValue="id" itemLabel="name" />
                </form:select>
              </div>
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.licensecategory" /> <span
              class="mandatory"></span> </label>
            <div class="col-sm-3 add-margin">
              <form:select path="licenseCategory" id="licenseCategory" cssClass="form-control"   required="required"
                cssErrorClass="form-control error">
                <form:option value="">
                  <spring:message code="lbl.select" />
                </form:option>
                <form:options items="${licenseCategorys}" itemValue="id" itemLabel="name" />
              </form:select>
            </div>
            <div class="form-group">
              <label class="col-sm-3 control-label text-right"><spring:message code="lbl.subcategory" /> <span
                class="mandatory"></span> </label>
              <div class="col-sm-3 add-margin">
                <form:select path="subCategory" id="subCategory" cssClass="form-control"   required="required"
                  cssErrorClass="form-control error">
                  <form:option value="">
                    <spring:message code="lbl.select" />
                  </form:option>
                  <form:options items="${subCategorys}" itemValue="id" itemLabel="name" />
                </form:select>
              </div>
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.licenseapptype" /> <span
              class="mandatory"></span> </label>
            <div class="col-sm-3 add-margin">
              <form:select path="licenseAppType" id="licenseAppType" cssClass="form-control"   required="required"
                cssErrorClass="form-control error">
                <form:option value="">
                  <spring:message code="lbl.select" />
                </form:option>
                <form:options items="${licenseAppTypes}" itemValue="id" itemLabel="name" />
              </form:select>
            </div>
            <div class="form-group">
              <label class="col-sm-3 control-label text-right"><spring:message code="lbl.feetype" /> <span
                class="mandatory"></span> </label>
              <div class="col-sm-3 add-margin">
                <form:select path="feeType" id="feeType" cssClass="form-control"   required="required"
                  cssErrorClass="form-control error">
                  <form:option value="">
                    <spring:message code="lbl.select" />
                  </form:option>
                  <form:options items="${feeTypes}" itemValue="id" itemLabel="name" />
                </form:select>
              </div>
            </div>
            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.unitofmeasurement" /> <span
              class="mandatory"></span> </label>
            <div class="col-sm-3 add-margin">
              <form:select path="unitOfMeasurement" id="unitOfMeasurement" cssClass="form-control"   required="required"
                cssErrorClass="form-control error">
                <form:option value="">
                  <spring:message code="lbl.select" />
                </form:option>
                <form:options items="${unitOfMeasurements}" itemValue="id" itemLabel="name" />
              </form:select>
            </div>
            <div class="text-center">
                   <button type="button" class="btn btn-primary" id="search">Submit</button>
                  <button type="button" class="btn btn-default" data-dismiss="modal" onclick="window.close();"> Close</button>
            </div>
            <c:if test="${hideTemporary}">
            <script>
            $("#natureOfBusiness option:contains('Permanent')").attr('selected', 'selected');
            $('#natureOfBusiness').attr("disabled", true); 
            </script>
            </c:if>
            
            <c:if test="${hideRenew}">
            <script>
            $("#licenseAppType option:contains('New')").attr('selected', 'selected');
            $('#licenseAppType').attr("disabled", true); 
            </script>
            </c:if>
            <div id="resultdiv">
            </div>
          </form:form>
        </div>
      </div>
    </div>
  </div>