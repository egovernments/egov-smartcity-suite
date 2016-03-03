<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Relation</div>
				</div>
				<div class="panel-body custom">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.code" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.code}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.name" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.name}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.address" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.address}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.mobile" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.mobile}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.email" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.email}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.narration" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.narration}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.isactive" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.isactive}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.panno" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.panno}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.tinno" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.tinno}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.regno" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.regno}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.bankaccount" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.bankaccount}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.ifsccode" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.ifsccode}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.bank" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${relation.bank.name}</div>
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