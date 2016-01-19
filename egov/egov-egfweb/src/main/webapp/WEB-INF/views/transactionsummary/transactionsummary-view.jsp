<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<div class="main-content"><div class="row"><div class="col-md-12"><div class="panel panel-primary" data-collapsed="0"><div class="panel-heading"><div class="panel-title">TransactionSummary</div></div><div class="panel-body custom"><div class="row add-border"><div class="col-xs-3 add-margin"><spring:message code="lbl.accountdetailtype" />
</div><div class="col-sm-3 add-margin view-content">
${transactionSummary.accountdetailtype.name}
</div><div class="col-xs-3 add-margin"><spring:message code="lbl.financialyear" />
</div><div class="col-sm-3 add-margin view-content">
${transactionSummary.financialyear.name}
</div></div>
<div class="row add-border"><div class="col-xs-3 add-margin"><spring:message code="lbl.fundsource" />
</div><div class="col-sm-3 add-margin view-content">
${transactionSummary.fundsource.name}
</div><div class="col-xs-3 add-margin"><spring:message code="lbl.fund" />
</div><div class="col-sm-3 add-margin view-content">
${transactionSummary.fund.name}
</div></div>
<div class="row add-border"><div class="col-xs-3 add-margin"><spring:message code="lbl.glcodeid" />
</div><div class="col-sm-3 add-margin view-content">
${transactionSummary.glcodeid.name}
</div><div class="col-xs-3 add-margin"><spring:message code="lbl.openingdebitbalance" />
</div><div class="col-sm-3 add-margin view-content">
${transactionSummary.openingdebitbalance}
</div></div>
<div class="row add-border"><div class="col-xs-3 add-margin"><spring:message code="lbl.openingcreditbalance" />
</div><div class="col-sm-3 add-margin view-content">
${transactionSummary.openingcreditbalance}
</div><div class="col-xs-3 add-margin"><spring:message code="lbl.accountdetailkey" />
</div><div class="col-sm-3 add-margin view-content">
${transactionSummary.accountdetailkey}
</div></div>
<div class="row add-border"><div class="col-xs-3 add-margin"><spring:message code="lbl.narration" />
</div><div class="col-sm-3 add-margin view-content">
${transactionSummary.narration}
</div><div class="col-xs-3 add-margin"><spring:message code="lbl.departmentid" />
</div><div class="col-sm-3 add-margin view-content">
${transactionSummary.departmentid.name}
</div></div>
<div class="row add-border"><div class="col-xs-3 add-margin"><spring:message code="lbl.functionaryid" />
</div><div class="col-sm-3 add-margin view-content">
${transactionSummary.functionaryid.name}
</div><div class="col-xs-3 add-margin"><spring:message code="lbl.functionid" />
</div><div class="col-sm-3 add-margin view-content">
${transactionSummary.functionid.name}
</div></div>
<div class="row add-border"><div class="col-xs-3 add-margin"><spring:message code="lbl.divisionid" />
</div><div class="col-sm-3 add-margin view-content">
${transactionSummary.divisionid}
</div></div></div></div></div><div class="row text-center"><div class="add-margin"><a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">Close</a></div></div>