<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Recovery</div>
				</div>
				<div class="panel-body custom">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.recoverycode" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${recovery.type}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.recoveryname" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${recovery.recoveryName}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.subledgertype" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${recovery.egPartytype.code}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.chartofaccounts" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${recovery.chartofaccounts.name}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.remitted" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${recovery.remitted}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.ifsccode" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${recovery.ifscCode}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.accountnumber" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${recovery.accountNumber}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isactive" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${recovery.isactive}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.description" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${recovery.description}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.bank" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${recovery.bank.name}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.recoverymode" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${recovery.recoveryMode}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.remittancemode" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${recovery.remittanceMode}</div>
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
