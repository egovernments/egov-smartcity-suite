<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Budget Definition</div>
				</div>
				<div class="panel-body custom">

					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.name" />
						</div>
						<div class="col-sm-3 add-margin view-content">${budget.name}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isbere" />
						</div>
						<div class="col-sm-3 add-margin view-content">${budget.isbere}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.financialyear" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${budget.financialYear.finYearRange}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.parent" />
						</div>
						<div class="col-sm-3 add-margin view-content">${budget.parent.name}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isactivebudget" />
						</div>
						<div class="col-sm-3 add-margin view-content">${fn:toUpperCase(budget.isActiveBudget)}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isprimarybudget" />
						</div>
						<div class="col-sm-3 add-margin view-content">${fn:toUpperCase(budget.isPrimaryBudget)}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.referencebudget" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${budget.referenceBudget.name}</div>
					 
					<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isprimarybudget" />
						</div>
						<div class="col-sm-3 add-margin view-content">${budget.materializedPath}</div>
						</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.description" />
						</div>
						<div class="col-sm-9 add-margin view-content">${budget.description}</div>
					</div>
				</div>
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