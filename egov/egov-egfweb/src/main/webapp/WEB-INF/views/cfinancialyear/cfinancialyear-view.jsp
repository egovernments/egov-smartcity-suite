<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">FinancialYear</div>
				</div>
				<div class="panel-body custom">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.finyearrange" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${cFinancialYear.finYearRange}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.startingdate" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<fmt:formatDate pattern="dd/MM/yyyy"
								value="${cFinancialYear.startingDate}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.endingdate" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<fmt:formatDate pattern="dd/MM/yyyy"
								value="${cFinancialYear.endingDate}" />
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isactive" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${cFinancialYear.isActive}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isactiveforposting" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${cFinancialYear.isActiveForPosting}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isclosed" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${cFinancialYear.isClosed}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.transferclosingbalance" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${cFinancialYear.transferClosingBalance}</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row text-center">
			<div class="add-margin">
				<a href="javascript:void(0)" class="btn btn-default"
					onclick="self.close()">Close</a>
			</div>
		</div>