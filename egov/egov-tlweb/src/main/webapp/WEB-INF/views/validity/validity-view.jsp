<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<div class="main-content"><div class="row"><div class="col-md-12"><div class="panel panel-primary" data-collapsed="0"><div class="panel-heading"><div class="panel-title">Validity</div></div><div class="panel-body custom"><div class="row add-border"><div class="col-xs-3 add-margin"><spring:message code="lbl.natureofbusiness" />
</div><div class="col-sm-3 add-margin view-content">
${validity.natureOfBusiness.name}
</div><div class="col-xs-3 add-margin"><spring:message code="lbl.licensecategory" />
</div><div class="col-sm-3 add-margin view-content">
${validity.licenseCategory.name}
</div></div>
<div class="row add-border"><div class="col-xs-3 add-margin"><spring:message code="lbl.day" />
</div><div class="col-sm-3 add-margin view-content">
${validity.day}
</div><div class="col-xs-3 add-margin"><spring:message code="lbl.week" />
</div><div class="col-sm-3 add-margin view-content">
${validity.week}
</div></div>
<div class="row add-border"><div class="col-xs-3 add-margin"><spring:message code="lbl.month" />
</div><div class="col-sm-3 add-margin view-content">
${validity.month}
</div><div class="col-xs-3 add-margin"><spring:message code="lbl.year" />
</div><div class="col-sm-3 add-margin view-content">
${validity.year}
</div></div>
</div></div></div></div><div class="row text-center"><div class="add-margin"><a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">Close</a></div></div>