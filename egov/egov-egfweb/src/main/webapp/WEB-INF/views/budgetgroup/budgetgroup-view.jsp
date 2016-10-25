
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Budget Group</div>
				</div>
				<div class="panel-body custom">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.name" />
						</div>
						<div class="col-sm-3 add-margin view-content">${budgetGroup.name}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.description" />
						</div>
						<div class="col-sm-3 add-margin view-content">${budgetGroup.description}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.majorcode" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${budgetGroup.majorCode.name}</div>
						<div class="col-xs-3 add-margin ">
							<spring:message code="lbl.maxcode" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${budgetGroup.maxCode.name}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin ">
							<spring:message code="lbl.mincode" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${budgetGroup.minCode.name}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.accounttype" />
						</div>
						<div class="col-sm-3 add-margin view-content">${budgetGroup.accountType}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.budgetingtype" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${budgetGroup.budgetingType}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isactive" />
						</div>
						<div class="col-sm-3 add-margin view-content">${budgetGroup.isActive}</div>
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
</div>