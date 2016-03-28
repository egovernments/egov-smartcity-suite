<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<div class="main-content">
  <div class="row">
    <div class="col-md-12">
      <div class="panel panel-primary" data-collapsed="0">
        <div class="panel-heading">
          <div class="panel-title">Fund</div>
        </div>
        <div class="panel-body custom">
          <div class="row add-border">
            <div class="col-xs-3 add-margin">
              <spring:message code="lbl.name" />
            </div>
            <div class="col-sm-3 add-margin view-content">${fund.name}</div>
            <div class="col-xs-3 add-margin">
              <spring:message code="lbl.code" />
            </div>
            <div class="col-sm-3 add-margin view-content">${fund.code}</div>
          </div>
          <div class="row add-border">
            <div class="col-xs-3 add-margin">
              <spring:message code="lbl.identifier" />
            </div>
            <div class="col-sm-3 add-margin view-content">${fund.identifier}</div>
            <div class="col-xs-3 add-margin">
              <spring:message code="lbl.llevel" />
            </div>
            <div class="col-sm-3 add-margin view-content">${fund.llevel}</div>
          </div>
          <div class="row add-border">
            <div class="col-xs-3 add-margin">
              <spring:message code="lbl.parentid" />
            </div>
            <div class="col-sm-3 add-margin view-content">${fund.parentId.name}</div>
            <div class="col-xs-3 add-margin">
              <spring:message code="lbl.isnotleaf" />
            </div>
            <div class="col-sm-3 add-margin view-content">${fund.isnotleaf}</div>
          </div>
          <div class="row add-border">
            <div class="col-xs-3 add-margin">
              <spring:message code="lbl.isactive" />
            </div>
            <div class="col-sm-3 add-margin view-content">${fund.isactive}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="row text-center">
      <div class="add-margin">
        <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">Close</a>
      </div>
    </div>